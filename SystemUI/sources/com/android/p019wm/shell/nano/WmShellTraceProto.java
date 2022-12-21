package com.android.p019wm.shell.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.p026io.IOException;

/* renamed from: com.android.wm.shell.nano.WmShellTraceProto */
public final class WmShellTraceProto extends MessageNano {
    private static volatile WmShellTraceProto[] _emptyArray;
    public boolean testValue;

    public static WmShellTraceProto[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new WmShellTraceProto[0];
                }
            }
        }
        return _emptyArray;
    }

    public WmShellTraceProto() {
        clear();
    }

    public WmShellTraceProto clear() {
        this.testValue = false;
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        boolean z = this.testValue;
        if (z) {
            codedOutputByteBufferNano.writeBool(1, z);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        boolean z = this.testValue;
        return z ? computeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(1, z) : computeSerializedSize;
    }

    public WmShellTraceProto mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 8) {
                this.testValue = codedInputByteBufferNano.readBool();
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static WmShellTraceProto parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (WmShellTraceProto) MessageNano.mergeFrom(new WmShellTraceProto(), bArr);
    }

    public static WmShellTraceProto parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new WmShellTraceProto().mergeFrom(codedInputByteBufferNano);
    }
}
