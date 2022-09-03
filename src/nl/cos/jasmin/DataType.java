package nl.cos.jasmin;

public enum DataType {
    INT("I","i"),
    VOID("V","null"),
    STRING("Ljava/lang/String;","a"),
    BOOLEAN("Z","i"),
    FLOAT("F","f"),
    FUNCTION("Ljava/lang/invoke/MethodHandle;", "a"),
    PARAM_FUNCTION("Ljava/lang/invoke/MethodHandle;", "a");

    private final String descriptor;
    private final String mnemonic;

    DataType(String d, String m) {
        this.descriptor = d;
        this.mnemonic = m;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String getMnemonic() {
        return mnemonic;
    }
}
