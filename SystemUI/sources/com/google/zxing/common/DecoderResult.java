package com.google.zxing.common;

import java.util.List;

public final class DecoderResult {
    private final List<byte[]> byteSegments;
    private final String ecLevel;
    private Integer erasures;
    private Integer errorsCorrected;
    private int numBits;
    private Object other;
    private final byte[] rawBytes;
    private final int structuredAppendParity;
    private final int structuredAppendSequenceNumber;
    private final int symbologyModifier;
    private final String text;

    public DecoderResult(byte[] bArr, String str, List<byte[]> list, String str2) {
        this(bArr, str, list, str2, -1, -1, 0);
    }

    public DecoderResult(byte[] bArr, String str, List<byte[]> list, String str2, int i) {
        this(bArr, str, list, str2, -1, -1, i);
    }

    public DecoderResult(byte[] bArr, String str, List<byte[]> list, String str2, int i, int i2) {
        this(bArr, str, list, str2, i, i2, 0);
    }

    public DecoderResult(byte[] bArr, String str, List<byte[]> list, String str2, int i, int i2, int i3) {
        int i4;
        this.rawBytes = bArr;
        if (bArr == null) {
            i4 = 0;
        } else {
            i4 = bArr.length * 8;
        }
        this.numBits = i4;
        this.text = str;
        this.byteSegments = list;
        this.ecLevel = str2;
        this.structuredAppendParity = i2;
        this.structuredAppendSequenceNumber = i;
        this.symbologyModifier = i3;
    }

    public byte[] getRawBytes() {
        return this.rawBytes;
    }

    public int getNumBits() {
        return this.numBits;
    }

    public void setNumBits(int i) {
        this.numBits = i;
    }

    public String getText() {
        return this.text;
    }

    public List<byte[]> getByteSegments() {
        return this.byteSegments;
    }

    public String getECLevel() {
        return this.ecLevel;
    }

    public Integer getErrorsCorrected() {
        return this.errorsCorrected;
    }

    public void setErrorsCorrected(Integer num) {
        this.errorsCorrected = num;
    }

    public Integer getErasures() {
        return this.erasures;
    }

    public void setErasures(Integer num) {
        this.erasures = num;
    }

    public Object getOther() {
        return this.other;
    }

    public void setOther(Object obj) {
        this.other = obj;
    }

    public boolean hasStructuredAppend() {
        return this.structuredAppendParity >= 0 && this.structuredAppendSequenceNumber >= 0;
    }

    public int getStructuredAppendParity() {
        return this.structuredAppendParity;
    }

    public int getStructuredAppendSequenceNumber() {
        return this.structuredAppendSequenceNumber;
    }

    public int getSymbologyModifier() {
        return this.symbologyModifier;
    }
}
