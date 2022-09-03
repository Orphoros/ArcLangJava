package nl.cos.typing.symbols;

import nl.cos.jasmin.DataType;

public class FunctionParamFuncSymbol extends FunctionParamVariableSymbol implements FunctionSymbolInterface{

    private final DataType returnType;
    private final DataType[] params;

    public FunctionParamFuncSymbol(String name, int index, DataType type, int paramIndex, DataType returnType, DataType[] params) {
        super(name, index, type, paramIndex);
        this.returnType = returnType;
        this.params = params;
    }

    @Override
    public DataType getReturnType() {
        return returnType;
    }

    @Override
    public DataType[] getParams() {
        return params;
    }
}
