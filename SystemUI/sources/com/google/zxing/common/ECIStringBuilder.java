package com.google.zxing.common;

import com.google.zxing.FormatException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class ECIStringBuilder {
    private StringBuilder currentBytes;
    private Charset currentCharset;
    private StringBuilder result;

    public ECIStringBuilder() {
        this.currentCharset = StandardCharsets.ISO_8859_1;
        this.currentBytes = new StringBuilder();
    }

    public ECIStringBuilder(int i) {
        this.currentCharset = StandardCharsets.ISO_8859_1;
        this.currentBytes = new StringBuilder(i);
    }

    public void append(char c) {
        this.currentBytes.append((char) (c & 255));
    }

    public void append(byte b) {
        this.currentBytes.append((char) (b & 255));
    }

    public void append(String str) {
        this.currentBytes.append(str);
    }

    public void append(int i) {
        append(String.valueOf(i));
    }

    public void appendECI(int i) throws FormatException {
        encodeCurrentBytesIfAny();
        CharacterSetECI characterSetECIByValue = CharacterSetECI.getCharacterSetECIByValue(i);
        if (characterSetECIByValue != null) {
            this.currentCharset = characterSetECIByValue.getCharset();
            return;
        }
        throw FormatException.getFormatInstance();
    }

    private void encodeCurrentBytesIfAny() {
        if (this.currentCharset.equals(StandardCharsets.ISO_8859_1)) {
            if (this.currentBytes.length() > 0) {
                StringBuilder sb = this.result;
                if (sb == null) {
                    this.result = this.currentBytes;
                    this.currentBytes = new StringBuilder();
                    return;
                }
                sb.append((CharSequence) this.currentBytes);
                this.currentBytes = new StringBuilder();
            }
        } else if (this.currentBytes.length() > 0) {
            byte[] bytes = this.currentBytes.toString().getBytes(StandardCharsets.ISO_8859_1);
            this.currentBytes = new StringBuilder();
            StringBuilder sb2 = this.result;
            if (sb2 == null) {
                this.result = new StringBuilder(new String(bytes, this.currentCharset));
            } else {
                sb2.append(new String(bytes, this.currentCharset));
            }
        }
    }

    public void appendCharacters(StringBuilder sb) {
        encodeCurrentBytesIfAny();
        this.result.append((CharSequence) sb);
    }

    public int length() {
        return toString().length();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0008, code lost:
        r1 = r1.result;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isEmpty() {
        /*
            r1 = this;
            java.lang.StringBuilder r0 = r1.currentBytes
            int r0 = r0.length()
            if (r0 != 0) goto L_0x0014
            java.lang.StringBuilder r1 = r1.result
            if (r1 == 0) goto L_0x0012
            int r1 = r1.length()
            if (r1 != 0) goto L_0x0014
        L_0x0012:
            r1 = 1
            goto L_0x0015
        L_0x0014:
            r1 = 0
        L_0x0015:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.zxing.common.ECIStringBuilder.isEmpty():boolean");
    }

    public String toString() {
        encodeCurrentBytesIfAny();
        StringBuilder sb = this.result;
        return sb == null ? "" : sb.toString();
    }
}
