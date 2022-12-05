package com.android.internal.util;

import java.io.Writer;
@Deprecated
/* loaded from: classes4.dex */
public class IndentingPrintWriter extends android.util.IndentingPrintWriter {
    public IndentingPrintWriter(Writer writer, String singleIndent) {
        super(writer, singleIndent, -1);
    }

    public IndentingPrintWriter(Writer writer, String singleIndent, int wrapLength) {
        super(writer, singleIndent, wrapLength);
    }

    public IndentingPrintWriter(Writer writer, String singleIndent, String prefix, int wrapLength) {
        super(writer, singleIndent, prefix, wrapLength);
    }

    @Override // android.util.IndentingPrintWriter
    /* renamed from: setIndent  reason: collision with other method in class */
    public IndentingPrintWriter mo3457setIndent(String indent) {
        super.mo3457setIndent(indent);
        return this;
    }

    @Override // android.util.IndentingPrintWriter
    /* renamed from: setIndent  reason: collision with other method in class */
    public IndentingPrintWriter mo3456setIndent(int indent) {
        super.mo3456setIndent(indent);
        return this;
    }

    @Override // android.util.IndentingPrintWriter
    /* renamed from: increaseIndent  reason: collision with other method in class */
    public IndentingPrintWriter mo3455increaseIndent() {
        super.mo3455increaseIndent();
        return this;
    }

    @Override // android.util.IndentingPrintWriter
    /* renamed from: decreaseIndent  reason: collision with other method in class */
    public IndentingPrintWriter mo3454decreaseIndent() {
        super.mo3454decreaseIndent();
        return this;
    }

    public IndentingPrintWriter printPair(String key, Object value) {
        super.print(key, value);
        return this;
    }

    public IndentingPrintWriter printPair(String key, Object[] value) {
        super.print(key, value);
        return this;
    }

    public IndentingPrintWriter printHexPair(String key, int value) {
        super.printHexInt(key, value);
        return this;
    }
}
