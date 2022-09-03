package nl.cos.typing;

import nl.cos.exceptions.CompilerException;
import nl.cos.jasmin.DataType;
import nl.cos.typing.symbols.*;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SymbolTable {
    private int lastUsedIndex;
    private SymbolTable parentScope;
    private final boolean isFunctionScope;
    private final HashMap<String, Symbol> symbolTable;

    public SymbolTable(int offset, boolean isFunctionScope) {
        symbolTable = new HashMap<>();
        this.lastUsedIndex = offset;
        this.isFunctionScope = isFunctionScope;
    }

    public SymbolTable closeScope() {
        return parentScope;
    }

    public void addVariable(String name, DataType type){
        Symbol s = lookup(name);
        if(s != null) throw new CompilerException("Variable '" + name + "' already exists in current scope and cannot be added!");
        this.symbolTable.put(name, new VariableSymbol(name, ++lastUsedIndex, type));
    }

    public void addFuncParamVariable(String name, DataType type, int index){
        Symbol s = lookup(name);
        if(s != null) throw new CompilerException("Function param variable '" + name + "' already exists in current scope and cannot be added!");
        this.symbolTable.put(name, new FunctionParamVariableSymbol(name, ++lastUsedIndex, type,index));
    }

    public void addFuncParamFuncVar(String name, DataType type, int index, DataType returnType, DataType[] params){
        Symbol s = lookup(name);
        if(s != null) throw new CompilerException("Function param variable '" + name + "' already exists in current scope and cannot be added!");
        this.symbolTable.put(name, new FunctionParamFuncSymbol(name, ++lastUsedIndex, type, index, returnType, params));
    }

    public void addFunction(String name, DataType returnType, DataType[] params){
        Symbol s = lookup(name);
        if(s != null) throw new CompilerException("Variable '" + name + "' already exists in current scope and cannot be added!");
        this.symbolTable.put(name, new FunctionSymbol(name, ++lastUsedIndex, returnType, params));
    }

    public void updateFunctionDeclaration(String name, DataType returnType, DataType[] params){
        FunctionSymbol s = (FunctionSymbol) lookup(name);
        if(s == null) throw new CompilerException("Function variable '" + name + "' does not exist in the current scope!");
        this.symbolTable.replace(name, new FunctionSymbol(name, s.getIndex(), returnType, params));
    }

    public Symbol lookup(String name){
        if(!this.symbolTable.containsKey(name)){
            if(parentScope != null && !isFunctionScope) {
                return parentScope.lookup(name);
            }else return null;
        }else return this.symbolTable.get(name);
    }

    public ArrayList<Symbol> getCurrentScopeSymbols(){
        return new ArrayList<>(symbolTable.values());
    }

    public SymbolTable newFunctionScope() {
        return createNewScope(-1, true);
    }

    public SymbolTable newScope() {
        return createNewScope(lastUsedIndex, false);
    }

    private SymbolTable createNewScope(int offset, boolean isFunctionScope) {
        SymbolTable childScope = new SymbolTable(offset, isFunctionScope);
        childScope.parentScope = this;

        return childScope;
    }
}
