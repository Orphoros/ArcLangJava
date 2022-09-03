package nl.cos.compiler;

import nl.cos.ArcLangBaseVisitor;
import nl.cos.ArcLangParser;
import nl.cos.exceptions.CompilerException;
import nl.cos.jasmin.DataType;
import nl.cos.typing.SymbolTable;
import nl.cos.typing.symbols.*;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.ArrayList;
import java.util.HashMap;

public class TypeChecker extends ArcLangBaseVisitor<DataType> {
    private final ParseTreeProperty<DataType> pt;
    private final ParseTreeProperty<SymbolTable> st;
    private SymbolTable symbolTable;
    private final HashMap<String, DataType> functionReturnType;
    private final HashMap<String, DataType[]> functionParams;
    private String lastFuncName;

    public TypeChecker(ParseTreeProperty<DataType> pt, ParseTreeProperty<SymbolTable> st, SymbolTable symbolTable) {
        this.pt = pt;
        this.st = st;
        this.symbolTable = symbolTable;
        this.functionReturnType = new HashMap<>();
        this.functionParams = new HashMap<>();
        this.lastFuncName = null;
    }

    @Override
    public DataType visitFunctionInit(ArcLangParser.FunctionInitContext ctx) {
        //Set up scope, name, and return type data
        symbolTable = symbolTable.newFunctionScope();
        DataType functionReturnType;
        String name = this.lastFuncName;
        this.lastFuncName = null;

        if (name == null) throw new CompilerException("Cannot assign function to non-function variable");
        if (ctx.E_NULL() != null) functionReturnType = DataType.VOID;
        else functionReturnType = visit(ctx.varType());
        if (functionReturnType == DataType.PARAM_FUNCTION) throw new CompilerException("Cannot return a Function Parameter");
        this.functionReturnType.put(name,functionReturnType);

        //Add function parameters if present
        handleFunctionParams(ctx,name);

        visit(ctx.codeBlockFunc());

        //Make sure scope return matches declared return
        checkReturnTypes(ctx,name);

        symbolTable = symbolTable.closeScope();
        st.put(ctx,symbolTable);
        pt.put(ctx,DataType.FUNCTION);
        return DataType.FUNCTION;
    }

    private void handleFunctionParams(ArcLangParser.FunctionInitContext ctx, String name){
        if (ctx.variableDecList() != null) {
            visit(ctx.variableDecList());
            DataType[] params = new DataType[st.get(ctx.variableDecList()).getCurrentScopeSymbols().size()];
            ArrayList<Symbol> paramsList = st.get(ctx.variableDecList()).getCurrentScopeSymbols();
            for (int i = 0; i < params.length; i++) {
                params[((FunctionParamVariableSymbol) paramsList.get(i)).getParamIndex()] = ((FunctionParamVariableSymbol) paramsList.get(i)).getType();
            }
            this.functionParams.put(name,params);
        } else this.functionParams.put(name, new DataType[0]);
    }

    private void checkReturnTypes(ArcLangParser.FunctionInitContext ctx, String name){
        DataType actualReturnType = pt.get(ctx.codeBlockFunc().returnScope());
        DataType expectedReturnType = this.functionReturnType.get(name);
        if(expectedReturnType == DataType.VOID && actualReturnType != null) throw new CompilerException("Cannot return from a function with a return type of NIL");
        if(expectedReturnType != DataType.VOID && actualReturnType == null) throw new CompilerException("Function is supposed to return " + expectedReturnType + ", but does not");
        if(expectedReturnType != DataType.VOID && actualReturnType != expectedReturnType) throw new CompilerException("Function expected to return " + expectedReturnType + ", but found " + actualReturnType);
    }

    @Override
    public DataType visitCodeBlockFunc(ArcLangParser.CodeBlockFuncContext ctx) {
        st.put(ctx,symbolTable);
        symbolTable = symbolTable.newScope();
        visitChildren(ctx);
        symbolTable = symbolTable.closeScope();
        return null;
    }

    @Override
    public DataType visitReturnScope(ArcLangParser.ReturnScopeContext ctx) {
        DataType returnType = visit(ctx.expression());
        pt.put(ctx,returnType);
        return returnType;
    }

    @Override
    public DataType visitFunctionCall(ArcLangParser.FunctionCallContext ctx) {
        String name = ctx.E_IDENTIFIER().getText();
        Symbol symbol = symbolTable.lookup(name);

        if(symbol == null) throw new CompilerException("Could not resolve variable '" + name + "'!");
        if(!(symbol instanceof FunctionSymbolInterface)) throw new CompilerException("Could not execute code given to variable '" + name + "', as it is not type of " + DataType.FUNCTION);
        if(((FunctionSymbolInterface) symbol).getParams() == null) throw new CompilerException("Cannot execute function variable '" + name + "', as it is not initialized");

        DataType returnType = ((FunctionSymbolInterface) symbol).getReturnType();
        checkCallParams(ctx);
        pt.put(ctx,returnType);
        st.put(ctx,symbolTable);
        return returnType;
    }

    private void checkCallParams(ArcLangParser.FunctionCallContext ctx) {
        String fName = ctx.E_IDENTIFIER().getText();
        DataType[] params = ((FunctionSymbolInterface) symbolTable.lookup(fName)).getParams();

        if (ctx.expressionList() != null) {
            int expectedParams = (ctx.expressionList().getChildCount() + 1) / 2;
            if (params.length != expectedParams)
                throw new CompilerException("Expected " + params.length + " parameters for function, but found " + expectedParams);

            for (int i = 0; i < params.length; i++) {
                DataType p = visit(ctx.expressionList().expression(i));
                if (params[i] != p)
                    if (params[i] == DataType.PARAM_FUNCTION && p != DataType.FUNCTION)
                        throw new CompilerException("Expected to find " + params[i] + " for function parameter (at location " + (i + 1) + "), but found " + p);
            }
        }
    }

    @Override
    public DataType visitVariableDecList(ArcLangParser.VariableDecListContext ctx) {
        for(int i = 0; i < ctx.E_IDENTIFIER().size(); i++){
            DataType t = visit(ctx.varType(i));

            if (t == DataType.PARAM_FUNCTION) {
                ArcLangParser.VarTypeFunctionParamContext paramFunc = (ArcLangParser.VarTypeFunctionParamContext) ctx.varType(i);
                ArrayList<DataType> requiredDataTypes = new ArrayList<>();
                if (paramFunc.varTypeList() != null) {
                    visit(paramFunc.varTypeList());
                    for (ArcLangParser.VarTypeContext entry : paramFunc.varTypeList().varType())
                        requiredDataTypes.add(pt.get(entry));
                }
                symbolTable.addFuncParamFuncVar(ctx.E_IDENTIFIER(i).getText(), t, i, paramFunc.E_NULL() != null ? DataType.VOID : pt.get(paramFunc), requiredDataTypes.toArray(new DataType[0]));
            } else symbolTable.addFuncParamVariable(ctx.E_IDENTIFIER(i).getText(), t,i);

            st.put(ctx,symbolTable);
            pt.put(ctx, t);
        }
        return null;
    }

    @Override
    public DataType visitVarTypeList(ArcLangParser.VarTypeListContext ctx) {
        for (ArcLangParser.VarTypeContext entry : ctx.varType()) pt.put(entry, visit(entry));
        return null;
    }

    @Override
    public DataType visitVarTypeInt(ArcLangParser.VarTypeIntContext ctx) {
        return DataType.INT;
    }

    @Override
    public DataType visitVarTypeFloat(ArcLangParser.VarTypeFloatContext ctx) {
        return DataType.FLOAT;
    }

    @Override
    public DataType visitVarTypeString(ArcLangParser.VarTypeStringContext ctx) {
        return DataType.STRING;
    }

    @Override
    public DataType visitVarTypeBoolean(ArcLangParser.VarTypeBooleanContext ctx) {
        return DataType.BOOLEAN;
    }

    @Override
    public DataType visitVarTypeFunction(ArcLangParser.VarTypeFunctionContext ctx) {
        return DataType.FUNCTION;
    }

    @Override
    public DataType visitNegativeFloat(ArcLangParser.NegativeFloatContext ctx) {
        return DataType.FLOAT;
    }

    @Override
    public DataType visitNegativeInt(ArcLangParser.NegativeIntContext ctx) {
        return DataType.INT;
    }

    @Override
    public DataType visitPositiveInt(ArcLangParser.PositiveIntContext ctx) {
        return DataType.INT;
    }

    @Override
    public DataType visitPositiveFloat(ArcLangParser.PositiveFloatContext ctx) {
        return DataType.FLOAT;
    }

    @Override
    public DataType visitVarTypeFunctionParam(ArcLangParser.VarTypeFunctionParamContext ctx) {
        if (ctx.varType() != null) pt.put(ctx, visit(ctx.varType()));
        return DataType.PARAM_FUNCTION;
    }

    @Override
    public DataType visitLiteralNumber(ArcLangParser.LiteralNumberContext ctx) {
        DataType t = visit(ctx.number());
        pt.put(ctx,t);
        return t;
    }

    @Override
    public DataType visitLiteralString(ArcLangParser.LiteralStringContext ctx) {
        return DataType.STRING;
    }

    @Override
    public DataType visitLiteralTrue(ArcLangParser.LiteralTrueContext ctx) {
        return DataType.BOOLEAN;
    }

    @Override
    public DataType visitLiteralFalse(ArcLangParser.LiteralFalseContext ctx) {
        return DataType.BOOLEAN;
    }

    @Override
    public DataType visitLiteralNull(ArcLangParser.LiteralNullContext ctx) {
        return DataType.VOID;
    }

    @Override
    public DataType visitFunctionCallExpr(ArcLangParser.FunctionCallExprContext ctx) {
        DataType t = visit(ctx.functionCall());
        pt.put(ctx,t);
        return t;
    }

    @Override
    public DataType visitMathSubExpr(ArcLangParser.MathSubExprContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        DataType t = checkOperationTypes(pt.get(ctx.left), pt.get(ctx.right));
        pt.put(ctx,t);
        return t;
    }

    @Override
    public DataType visitMathMulExpr(ArcLangParser.MathMulExprContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        DataType t = checkOperationTypes(pt.get(ctx.left), pt.get(ctx.right));
        pt.put(ctx,t);
        return t;
    }

    @Override
    public DataType visitMathModExpr(ArcLangParser.MathModExprContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        DataType t = checkOperationTypes(pt.get(ctx.left), pt.get(ctx.right));
        pt.put(ctx,t);
        return t;
    }

    @Override
    public DataType visitMathDivExpr(ArcLangParser.MathDivExprContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        DataType t = checkOperationTypes(pt.get(ctx.left), pt.get(ctx.right));
        pt.put(ctx,t);
        return t;
    }

    @Override
    public DataType visitMathAddExpr(ArcLangParser.MathAddExprContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        DataType t = checkOperationTypes(pt.get(ctx.left), pt.get(ctx.right));
        pt.put(ctx,t);
        return t;
    }

    @Override
    public DataType visitBoolNotExpr(ArcLangParser.BoolNotExprContext ctx) {
        visit(ctx.expression());

        DataType t = pt.get(ctx.expression());
        if(t != DataType.BOOLEAN) throw new CompilerException("Can only do NOT operation on a boolean!");
        pt.put(ctx,t);
        return t;
    }

    @Override
    public DataType visitLiteralExpr(ArcLangParser.LiteralExprContext ctx) {
        DataType t = visit(ctx.literal());
        pt.put(ctx,t);
        return t;
    }

    @Override
    public DataType visitParenedExpr(ArcLangParser.ParenedExprContext ctx) {
        DataType t = visit(ctx.expression());
        pt.put(ctx,t);
        return t;
    }

    @Override
    public DataType visitSystemReturn(ArcLangParser.SystemReturnContext ctx) {
        DataType t = visit(ctx.expression());
        pt.put(ctx,t);
        return t;
    }

    @Override
    public DataType visitBoolLogicalComprExpr(ArcLangParser.BoolLogicalComprExprContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        if(pt.get(ctx.left) != DataType.BOOLEAN || pt.get(ctx.right) != DataType.BOOLEAN) throw new CompilerException("Can only do boolean logical compare on boolean values!");
        pt.put(ctx, DataType.BOOLEAN);
        return DataType.BOOLEAN;
    }

    @Override
    public DataType visitBoolComprExpr(ArcLangParser.BoolComprExprContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        checkOperationTypes(pt.get(ctx.left),pt.get(ctx.right));
        pt.put(ctx, DataType.BOOLEAN);
        return DataType.BOOLEAN;
    }

    @Override
    public DataType visitCodeBlock(ArcLangParser.CodeBlockContext ctx) {
        st.put(ctx,symbolTable);

        symbolTable = symbolTable.newScope();
        visitChildren(ctx);
        symbolTable = symbolTable.closeScope();

        return null;
    }

    @Override
    public DataType visitVariableEmptyDec(ArcLangParser.VariableEmptyDecContext ctx) {
        String name = ctx.E_IDENTIFIER().getText();
        DataType type = visit(ctx.varType());

        if(type == DataType.FUNCTION) {
            symbolTable.addFunction(name,DataType.VOID, null);
        } else symbolTable.addVariable(name, type);

        st.put(ctx,symbolTable);
        pt.put(ctx, visit(ctx.varType()));
        return null;
    }

    @Override
    public DataType visitVariableInitDec(ArcLangParser.VariableInitDecContext ctx) {
        String name = ctx.E_IDENTIFIER().getText();

        DataType declaredType = visit(ctx.varType());
        if(declaredType == DataType.FUNCTION) lastFuncName = name;
        DataType initiatedType = visit(ctx.expression());

        if(declaredType == DataType.FUNCTION) {
            symbolTable.addFunction(name,this.functionReturnType.get(name), this.functionParams.get(name));
        } else symbolTable.addVariable(name, declaredType);
        st.put(ctx,symbolTable);

        if(declaredType == DataType.STRING && initiatedType == DataType.VOID) pt.put(ctx, declaredType);
        else if(declaredType != initiatedType) throw new CompilerException("Cannot assign " + initiatedType + " to " + declaredType);
        else pt.put(ctx, declaredType);
        return null;
    }

    @Override
    public DataType visitVariableInitialization(ArcLangParser.VariableInitializationContext ctx) {
        String varName = ctx.variable().E_IDENTIFIER().getText();
        VariableSymbol symbol = (VariableSymbol) symbolTable.lookup(varName);

        if (symbol instanceof FunctionSymbol) lastFuncName = varName;
        if (symbol == null) throw new CompilerException("Could not resolve variable '" + varName + "'!");
        if (symbol.getType() != visit(ctx.expression())) throw new CompilerException("Could not initialize variable. Data types are incompatible.");
        if (symbol instanceof FunctionSymbol) symbolTable.updateFunctionDeclaration(varName,this.functionReturnType.get(varName), this.functionParams.get(varName));

        pt.put(ctx,symbol.getType());
        st.put(ctx,symbolTable);
        return symbol.getType();
    }

    @Override
    public DataType visitVariableExpr(ArcLangParser.VariableExprContext ctx) {
        DataType t = visit(ctx.variable());
        pt.put(ctx,t);
        st.put(ctx,symbolTable);
        return t;
    }

    @Override
    public DataType visitVariable(ArcLangParser.VariableContext ctx) {
        String varName = ctx.E_IDENTIFIER().getText();
        VariableSymbol symbol = (VariableSymbol) symbolTable.lookup(varName);

        if (symbol == null) throw new CompilerException("Could not resolve variable '" + varName + "'!");
        pt.put(ctx,symbol.getType());
        st.put(ctx,symbolTable);
        return symbol.getType();
    }

    @Override
    public DataType visitLoop(ArcLangParser.LoopContext ctx) {
        visit(ctx.codeBlock());
        if(visit(ctx.expression()) != DataType.BOOLEAN) throw new CompilerException("Loop expected a boolean condition");
        return null;
    }

    @Override
    public DataType visitSystemInput(ArcLangParser.SystemInputContext ctx) {
        visit(ctx.right);
        VariableSymbol symbol = (VariableSymbol) symbolTable.lookup(ctx.right.getText());

        if (symbol == null) throw new CompilerException("Could not resolve variable '" + ctx.right.getText() + "'!");
        if(symbol.getType() != DataType.STRING) throw new CompilerException("Cannot place system input into a variable that is not the type of STRING");
        st.put(ctx,symbolTable);
        pt.put(ctx,symbol.getType());
        return DataType.STRING;
    }

    public static DataType checkOperationTypes(DataType l, DataType r){
        if(l.getMnemonic().equals("null")
                || l.getMnemonic().equals("a")
                || r.getMnemonic().equals("null")
                || r.getMnemonic().equals("a")) throw new CompilerException("Can only do operation on numbers or fractions");
        if(!l.getDescriptor().equals(r.getDescriptor())) throw new CompilerException("Can only do operation on the same type");

        return l;
    }
}
