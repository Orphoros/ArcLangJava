package nl.cos.typing.symbols;

import nl.cos.jasmin.DataType;

public class FunctionParamVariableSymbol extends VariableSymbol{
    private final int paramIndex;
    public FunctionParamVariableSymbol(String name, int index, DataType type, int paramIndex) {
        super(name, index, type);
        this.paramIndex = paramIndex;
    }

    public int getParamIndex() {
        return paramIndex;
    }
}
