package nl.cos.jasmin;

public enum MathInstr {
    ADD("add"),
    SUB("sub"),
    MUL("mul"),
    DIV("div"),
    REM("rem");

    private final String code;

    MathInstr(String c) {
        this.code = c;
    }

    public String getCode() {
        return code;
    }
}

