package nl.cos.typing.symbols;

import nl.cos.jasmin.DataType;

public interface FunctionSymbolInterface {
    public DataType getReturnType();
    public DataType[] getParams();
}
