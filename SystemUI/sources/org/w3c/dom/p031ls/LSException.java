package org.w3c.dom.p031ls;

/* renamed from: org.w3c.dom.ls.LSException */
public class LSException extends RuntimeException {
    public static final short PARSE_ERR = 81;
    public static final short SERIALIZE_ERR = 82;
    public short code;

    public LSException(short s, String str) {
        super(str);
        this.code = s;
    }
}
