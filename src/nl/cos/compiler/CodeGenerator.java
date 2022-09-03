package nl.cos.compiler;

import nl.cos.ArcLangBaseVisitor;
import nl.cos.ArcLangParser;
import nl.cos.exceptions.CompilerException;
import nl.cos.jasmin.DataType;
import nl.cos.jasmin.MathInstr;
import nl.cos.typing.SymbolTable;
import nl.cos.typing.symbols.FunctionSymbolInterface;
import nl.cos.typing.symbols.Symbol;
import nl.cos.typing.symbols.VariableSymbol;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

import java.util.*;

public class CodeGenerator extends ArcLangBaseVisitor<Void> {
    private final String className;
    private final ArrayList<String> jasminMainCode;
    private final HashMap<String, ArrayList<String>> jasminMethodCode;
    private final ParseTreeProperty<DataType> pt;
    private final ParseTreeProperty<SymbolTable> st;
    private boolean writeToMethod;
    private int labelCount;
    private final Stack<String> functionNameStack;

    public CodeGenerator(ParseTreeProperty<DataType> pt, ParseTreeProperty<SymbolTable> st, String className) {
        jasminMainCode = new ArrayList<>();
        jasminMethodCode = new HashMap<>();
        this.labelCount = 0;
        this.pt = pt;
        this.st = st;
        writeToMethod = false;
        this.className = className;
        this.functionNameStack = new Stack<>();
    }

    @Override
    public Void visitFunctionInit(ArcLangParser.FunctionInitContext ctx) {
        String returnType = ctx.codeBlockFunc().returnScope() == null ? DataType.VOID.getDescriptor() : pt.get(ctx.codeBlockFunc().returnScope()).getDescriptor();
        String fName = functionNameStack.peek();
        jasminMethodCode.put(fName, new ArrayList<>());

        //Generate method signature
        jasminMethodCode.get(fName).add(
                generateMethodSignature(ctx,returnType,fName)
        );

        //Add method code
        writeToMethod = true;
        addJasminCode(".limit stack 99");
        addJasminCode(".limit locals 99");
        visit(ctx.codeBlockFunc());
        if (returnType.equals(DataType.VOID.getDescriptor())) addJasminCode("return");
        functionNameStack.pop();
        if (functionNameStack.empty()) writeToMethod = false;
        jasminMethodCode.get(fName).add(".end method");
        return null;
    }

    private String generateMethodSignature(ArcLangParser.FunctionInitContext ctx, String returnType, String fName){
        StringBuilder initString = new StringBuilder();
        initString.append(".method public static ").append(fName).append("(");

        if (ctx.variableDecList() != null) {
            for (Symbol symbol : st.get(ctx.variableDecList()).getCurrentScopeSymbols()) {
                initString.append(((VariableSymbol) symbol).getType().getDescriptor());
            }
        }

        initString.append(")").append(returnType);
        return initString.toString();
    }

    @Override
    public Void visitFunctionCall(ArcLangParser.FunctionCallContext ctx) {
        //Get method handle
        int index = ((VariableSymbol) st.get(ctx).lookup(ctx.E_IDENTIFIER().getText())).getIndex();
        FunctionSymbolInterface functionSymbol = (FunctionSymbolInterface) st.get(ctx).lookup(ctx.E_IDENTIFIER().getText());
        addJasminCode("aload "+index);

        //Figure out the invocation handle
        if (ctx.expressionList() != null) visit(ctx.expressionList());
        StringBuilder invocationDescriptors = new StringBuilder();
        for (DataType param : functionSymbol.getParams()){
            invocationDescriptors.append(param.getDescriptor());
        }
        addJasminCode("invokevirtual java/lang/invoke/MethodHandle/invoke("+invocationDescriptors+")"+functionSymbol.getReturnType().getDescriptor());
        return null;
    }

    @Override
    public Void visitSystemInput(ArcLangParser.SystemInputContext ctx) {
        addJasminCode("new java/util/Scanner");
        addJasminCode("dup");
        addJasminCode("getstatic java/lang/System/in Ljava/io/InputStream;");
        addJasminCode("invokenonvirtual java/util/Scanner/<init>(Ljava/io/InputStream;)V");
        addJasminCode("invokevirtual java/util/Scanner/nextLine()Ljava/lang/String;");

        visit(ctx.right);
        DataType t = pt.get(ctx);
        int index = ((VariableSymbol)st.get(ctx).lookup(ctx.variable().E_IDENTIFIER().getText())).getIndex();
        addJasminCode(t.getMnemonic() + "store " + index);
        return null;
    }

    @Override
    public Void visitLoop(ArcLangParser.LoopContext ctx) {
        String labelLoopStart = "BR" + labelCount++;
        String labelLoopEnd = "BR" + labelCount++;

        addJasminCode(labelLoopStart + ":");
        visit(ctx.expression());
        addJasminCode("ifeq "+ labelLoopEnd);
        visit(ctx.codeBlock());
        addJasminCode("goto " + labelLoopStart);
        addJasminCode(labelLoopEnd + ":");

        return null;
    }

    @Override
    public Void visitVariableExpr(ArcLangParser.VariableExprContext ctx) {
        DataType t = pt.get(ctx);
        int index = ((VariableSymbol) st.get(ctx).lookup(ctx.variable().E_IDENTIFIER().getText())).getIndex();
        addJasminCode(t.getMnemonic() + "load " + index);
        return null;
    }

    @Override
    public Void visitVariableEmptyDec(ArcLangParser.VariableEmptyDecContext ctx) {
        DataType t = pt.get(ctx);
        int index = ((VariableSymbol)st.get(ctx).lookup(ctx.E_IDENTIFIER().getText())).getIndex();
        switch (t) {
            case STRING:
                addJasminCode("aconst_null");
                break;
            case FUNCTION:
                addJasminCode("aconst_null");
                functionNameStack.push(ctx.E_IDENTIFIER().getText());
                break;
            case FLOAT:
                addJasminCode("ldc 0.0");
                break;
            case INT:
            case BOOLEAN:
                addJasminCode("ldc 0");
                break;
        }
        if (t != DataType.FUNCTION) addJasminCode(t.getMnemonic() + "store " + index);
        return null;
    }

    @Override
    public Void visitVariableInitDec(ArcLangParser.VariableInitDecContext ctx) {
        DataType t = pt.get(ctx);
        if (t == DataType.FUNCTION) functionNameStack.push(ctx.E_IDENTIFIER().getText());
        VariableSymbol symbol = (VariableSymbol)st.get(ctx).lookup(ctx.E_IDENTIFIER().getText());

        visit(ctx.expression());

        if (t == DataType.FUNCTION) {
            initializeVariable(t,symbol,ctx.E_IDENTIFIER().getText(),st.get(ctx).lookup(ctx.E_IDENTIFIER().getText()));
        } else initializeVariable(t,symbol, null, null);
        return null;
    }

    @Override
    public Void visitVariableInitialization(ArcLangParser.VariableInitializationContext ctx) {
        visit(ctx.expression());

        DataType t = pt.get(ctx);
        VariableSymbol symbol = (VariableSymbol)st.get(ctx).lookup(ctx.variable().E_IDENTIFIER().getText());

        if (t == DataType.FUNCTION) {
            initializeVariable(t,symbol,ctx.variable().E_IDENTIFIER().getText(),st.get(ctx).lookup(ctx.variable().E_IDENTIFIER().getText()));
        } else initializeVariable(t,symbol, null, null);
        return null;
    }

    private void initializeVariable(DataType type, VariableSymbol variableSymbol, String functionName, Symbol functionSymbol){
        if (type == DataType.FUNCTION) generateMethodHandleText(functionName, functionSymbol);
        int index = variableSymbol.getIndex();
        addJasminCode(type.getMnemonic() + "store " + index);
    }

    @Override
    public Void visitSystemReturn(ArcLangParser.SystemReturnContext ctx) {
        addJasminCode("getstatic java/lang/System/out Ljava/io/PrintStream;");
        visit(ctx.expression());
        DataType t = pt.get(ctx);
        if(t == DataType.VOID) t = DataType.STRING;
        addJasminCode("invokevirtual java/io/PrintStream/println(" + t.getDescriptor() + ")V");
        return null;
    }

    @Override
    public Void visitNegativeFloat(ArcLangParser.NegativeFloatContext ctx) {
        generateFloatCode(ctx.getText());
        return null;
    }

    @Override
    public Void visitNegativeInt(ArcLangParser.NegativeIntContext ctx) {
        generateIntCode(ctx.getText());
        return null;
    }

    @Override
    public Void visitPositiveInt(ArcLangParser.PositiveIntContext ctx) {
        generateIntCode(ctx.getText());
        return null;
    }

    @Override
    public Void visitPositiveFloat(ArcLangParser.PositiveFloatContext ctx) {
        generateFloatCode(ctx.getText());
        return null;
    }

    @Override
    public Void visitLiteralTrue(ArcLangParser.LiteralTrueContext ctx) {
        addJasminCode("ldc 1");
        return null;
    }

    @Override
    public Void visitLiteralFalse(ArcLangParser.LiteralFalseContext ctx) {
        addJasminCode("ldc 0");
        return null;
    }

    @Override
    public Void visitLiteralString(ArcLangParser.LiteralStringContext ctx) {
        addJasminCode("ldc " + ctx.getText());
        return null;
    }

    @Override
    public Void visitLiteralNull(ArcLangParser.LiteralNullContext ctx) {
        addJasminCode("aconst_null");
        return null;
    }

    @Override
    public Void visitBoolNotExpr(ArcLangParser.BoolNotExprContext ctx) {
        visit(ctx.expression());
        String labelTrue = "BR"+new Random().nextInt(Integer.MAX_VALUE);
        String labelFalse = "BR"+new Random().nextInt(Integer.MAX_VALUE);
        addJasminCode("ifeq "+labelTrue);
        generateComparisonOnStack(labelTrue, labelFalse);
        return null;
    }

    @Override
    public Void visitMathAddExpr(ArcLangParser.MathAddExprContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        addJasminCode(pt.get(ctx.left).getMnemonic() + MathInstr.ADD.getCode());

        return null;
    }

    @Override
    public Void visitMathSubExpr(ArcLangParser.MathSubExprContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        String typeL = pt.get(ctx.left).getMnemonic();
        addJasminCode(typeL + MathInstr.SUB.getCode());

        return null;
    }

    @Override
    public Void visitMathMulExpr(ArcLangParser.MathMulExprContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        String typeL = pt.get(ctx.left).getMnemonic();
        addJasminCode(typeL + MathInstr.MUL.getCode());

        return null;
    }

    @Override
    public Void visitMathDivExpr(ArcLangParser.MathDivExprContext ctx) {
        if(ctx.right.getText().equals("0")) throw new CompilerException("Cannot do division by zero!");
        visit(ctx.left);
        visit(ctx.right);

        String typeL = pt.get(ctx.left).getMnemonic();
        addJasminCode(typeL + MathInstr.DIV.getCode());

        return null;
    }

    @Override
    public Void visitMathModExpr(ArcLangParser.MathModExprContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        String typeL = pt.get(ctx.left).getMnemonic();
        addJasminCode(typeL + MathInstr.REM.getCode());

        return null;
    }

    @Override
    public Void visitCondition(ArcLangParser.ConditionContext ctx) {
        visit(ctx.expression());
        if(pt.get(ctx.expression()) != DataType.BOOLEAN) throw new CompilerException("Condition statement expected a boolean!");
        String labelTrue = "BR" + labelCount++;
        String labelFalse = "BR" + labelCount++;

        addJasminCode("ifne "+labelTrue); // Are we TRUE?
        if (ctx.conditionBranchFalse() != null) visit(ctx.conditionBranchFalse()); // Create FALSE code if we have it
        addJasminCode("goto "+labelFalse); // Exit statement after FALSE
        addJasminCode(labelTrue+":");
        visit(ctx.conditionBranchTrue()); // Create TRUE code
        addJasminCode(labelFalse+":"); // Go here to exit
        return null;
    }

    @Override
    public Void visitBoolComprExpr(ArcLangParser.BoolComprExprContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        generateComparison(ctx.booleanComparator(), null, pt.get(ctx.left).getMnemonic());
        visit(ctx.booleanComparator());
        return null;
    }

    @Override
    public Void visitBoolLogicalComprExpr(ArcLangParser.BoolLogicalComprExprContext ctx) {
        visit(ctx.left);
        visit(ctx.right);

        generateComparison(null,ctx.booleanLogicalComperator(), pt.get(ctx.left).getMnemonic());
        visit(ctx.booleanLogicalComperator());
        return null;
    }

    @Override
    public Void visitReturnScope(ArcLangParser.ReturnScopeContext ctx) {
        visit(ctx.expression());
        DataType returnType = pt.get(ctx);

        addJasminCode(returnType.getMnemonic() + "return");
        return null;
    }

    private void generateIntCode(String intNum){
        try {
            Integer.parseInt(intNum);
        }catch (NumberFormatException e){
            throw new CompilerException("Number literal is incorrect!");
        }
        addJasminCode("ldc " + intNum);
    }

    private void generateFloatCode(String floatNum){
        try {
            Float.parseFloat(floatNum);
        }catch (NumberFormatException e){
            throw new CompilerException("Float literal is incorrect!");
        }
        addJasminCode("ldc " + floatNum);
    }

    private void generateComparison(ArcLangParser.BooleanComparatorContext ctx, ArcLangParser.BooleanLogicalComperatorContext ctx2, String type){
        String labelTrue = "BR" + labelCount++;
        String labelFalse = "BR" + labelCount++;
        ArrayList<String> instructions = new ArrayList<>();

        //Float comparisons require special treatment, so we check it here
        if (!type.equals("i")) instructions.addAll(generateFloatCompare(ctx));
        else instructions.addAll(generateIntegerCompare(ctx, ctx2));

        instructions.set(instructions.size()-1, instructions.get(instructions.size()-1)+" "+labelTrue);
        if(writeToMethod) jasminMethodCode.get(functionNameStack.peek()).addAll(instructions);
        else jasminMainCode.addAll(instructions);

        generateComparisonOnStack(labelTrue, labelFalse);
    }

    private void generateComparisonOnStack(String labelTrue, String labelFalse){
        addJasminCode("iconst_0");
        addJasminCode("goto "+labelFalse);
        addJasminCode(labelTrue+":");
        addJasminCode("iconst_1");
        addJasminCode(labelFalse+":");
    }

    private ArrayList<String> generateIntegerCompare(ArcLangParser.BooleanComparatorContext ctx, ArcLangParser.BooleanLogicalComperatorContext ctx2){
        ArrayList<String> instructions = new ArrayList<>();
        if(ctx != null){
            if (ctx.B_GRATER() != null) instructions.add("if_icmpgt");
            if (ctx.B_EQUAL() != null) instructions.add("if_icmpeq");
            if (ctx.B_LESS() != null) instructions.add("if_icmplt");
            if (ctx.B_GREATER_EQUAL() != null) instructions.add("if_icmpge");
            if (ctx.B_LESS_EQUAL() != null) instructions.add("if_icmple");
            if (ctx.B_NOT_EQUAL() != null) instructions.add("if_icmpne");
        }
        else if(ctx2 != null) {
            if (ctx2.B_OR() != null) {
                instructions.add("ior");
                instructions.add("ifne");
            }
            if (ctx2.B_AND() != null){
                instructions.add("iand");
                instructions.add("ifne");
            }
        }
        return instructions;
    }

    private ArrayList<String> generateFloatCompare(ArcLangParser.BooleanComparatorContext ctx){
        ArrayList<String> instructions = new ArrayList<>();
        if (ctx.B_GRATER() != null) {
            instructions.add("fcmpl");
            instructions.add("ifge");
        }
        if (ctx.B_EQUAL() != null) {
            instructions.add("fcmpl");
            instructions.add("ifeq");
        }
        if (ctx.B_LESS() != null) {
            instructions.add("fcmpg");
            instructions.add("ifle");
        }
        if (ctx.B_GREATER_EQUAL() != null) {
            instructions.add("fcmpl");
            instructions.add("ifgt");
        }
        if (ctx.B_LESS_EQUAL() != null) {
            instructions.add("fcmpg");
            instructions.add("iflt");
        }
        if (ctx.B_NOT_EQUAL() != null) {
            instructions.add("fcmpl");
            instructions.add("ifne");
        }
        return instructions;
    }

    private void generateMethodHandleText(String name, Symbol functionSymbol) {
        //Generate method signature
        StringBuilder methodSignature = new StringBuilder();
        methodSignature.append("(");
        FunctionSymbolInterface symbol;
        symbol = (FunctionSymbolInterface) functionSymbol;
        if (symbol.getParams() != null) {
            for (DataType param : symbol.getParams()) {
                methodSignature.append(param.getDescriptor());
            }
        }
        methodSignature.append(")")
                .append(symbol.getReturnType().getDescriptor());

        //Some really fun code to generate a handle
        addJasminCode("invokestatic java/lang/invoke/MethodHandles.lookup()Ljava/lang/invoke/MethodHandles$Lookup;");
        addJasminCode("ldc " + this.className);
        addJasminCode("ldc \"" + name + "\"");
        addJasminCode("ldc \"" + methodSignature + "\"");
        addJasminCode("invokestatic java/lang/ClassLoader.getSystemClassLoader()Ljava/lang/ClassLoader;");
        addJasminCode("invokestatic java/lang/invoke/MethodType.fromMethodDescriptorString(Ljava/lang/String;Ljava/lang/ClassLoader;)Ljava/lang/invoke/MethodType;");
        addJasminCode("invokevirtual java/lang/invoke/MethodHandles$Lookup.findStatic(Ljava/lang/Class;Ljava/lang/String;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/MethodHandle;");
    }

    private void addJasminCode(String codeToAdd) {
        if(writeToMethod) jasminMethodCode.get(functionNameStack.peek()).add(codeToAdd);
        else jasminMainCode.add(codeToAdd);
    }

    public List<String> getCode(){
        return jasminMainCode;
    }

    public List<String> getMethodCode() {
        List<String> returnCode = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> e : jasminMethodCode.entrySet()){
            returnCode.add(" ");
            returnCode.add("; ---Method: '"+e.getKey()+"' ---");
            returnCode.addAll(e.getValue());
        }
        return returnCode;
    }
}
