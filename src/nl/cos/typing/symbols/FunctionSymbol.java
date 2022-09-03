package nl.cos.typing.symbols;

import nl.cos.jasmin.DataType;

public class FunctionSymbol extends VariableSymbol implements FunctionSymbolInterface{
    private final DataType returnType;
    private final DataType[] params;

    public FunctionSymbol(String name, int index, DataType returnType, DataType[] params) {
        super(name, index, DataType.FUNCTION);
        this.returnType = returnType;
        this.params = params;
    }

    public DataType getReturnType() {
        return returnType;
    }

    public DataType[] getParams() {
        return params;
    }
}
