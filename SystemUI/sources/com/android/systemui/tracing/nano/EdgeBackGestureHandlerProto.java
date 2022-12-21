package com.android.systemui.tracing.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.p026io.IOException;

public final class EdgeBackGestureHandlerProto extends MessageNano {
    private static volatile EdgeBackGestureHandlerProto[] _emptyArray;
    public boolean allowGesture;

    public static EdgeBackGestureHandlerProto[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new EdgeBackGestureHandlerProto[0];
                }
            }
        }
        return _emptyArray;
    }

    public EdgeBackGestureHandlerProto() {
        clear();
    }

    public EdgeBackGestureHandlerProto clear() {
        this.allowGesture = false;
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        boolean z = this.allowGesture;
        if (z) {
            codedOutputByteBufferNano.writeBool(1, z);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        boolean z = this.allowGesture;
        return z ? computeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(1, z) : computeSerializedSize;
    }

    public EdgeBackGestureHandlerProto mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 8) {
                this.allowGesture = codedInputByteBufferNano.readBool();
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static EdgeBackGestureHandlerProto parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (EdgeBackGestureHandlerProto) MessageNano.mergeFrom(new EdgeBackGestureHandlerProto(), bArr);
    }

    public static EdgeBackGestureHandlerProto parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new EdgeBackGestureHandlerProto().mergeFrom(codedInputByteBufferNano);
    }
}
