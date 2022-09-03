package nl.cos.typing.symbols;

import nl.cos.jasmin.DataType;

public class VariableSymbol extends Symbol{
    private final int index;
    private DataType type;

    public VariableSymbol(String name, int index, DataType type) {
        super(name);
        this.index = index;
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }
}
