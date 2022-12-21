package com.android.systemui.tracing.nano;

import com.android.p019wm.shell.nano.WmShellTraceProto;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.p026io.IOException;

public final class SystemUiTraceProto extends MessageNano {
    private static volatile SystemUiTraceProto[] _emptyArray;
    public EdgeBackGestureHandlerProto edgeBackGestureHandler;
    public WmShellTraceProto wmShell;

    public static SystemUiTraceProto[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new SystemUiTraceProto[0];
                }
            }
        }
        return _emptyArray;
    }

    public SystemUiTraceProto() {
        clear();
    }

    public SystemUiTraceProto clear() {
        this.edgeBackGestureHandler = null;
        this.wmShell = null;
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        EdgeBackGestureHandlerProto edgeBackGestureHandlerProto = this.edgeBackGestureHandler;
        if (edgeBackGestureHandlerProto != null) {
            codedOutputByteBufferNano.writeMessage(1, edgeBackGestureHandlerProto);
        }
        WmShellTraceProto wmShellTraceProto = this.wmShell;
        if (wmShellTraceProto != null) {
            codedOutputByteBufferNano.writeMessage(2, wmShellTraceProto);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        EdgeBackGestureHandlerProto edgeBackGestureHandlerProto = this.edgeBackGestureHandler;
        if (edgeBackGestureHandlerProto != null) {
            computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, edgeBackGestureHandlerProto);
        }
        WmShellTraceProto wmShellTraceProto = this.wmShell;
        return wmShellTraceProto != null ? computeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(2, wmShellTraceProto) : computeSerializedSize;
    }

    public SystemUiTraceProto mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 10) {
                if (this.edgeBackGestureHandler == null) {
                    this.edgeBackGestureHandler = new EdgeBackGestureHandlerProto();
                }
                codedInputByteBufferNano.readMessage(this.edgeBackGestureHandler);
            } else if (readTag == 18) {
                if (this.wmShell == null) {
                    this.wmShell = new WmShellTraceProto();
                }
                codedInputByteBufferNano.readMessage(this.wmShell);
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static SystemUiTraceProto parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (SystemUiTraceProto) MessageNano.mergeFrom(new SystemUiTraceProto(), bArr);
    }

    public static SystemUiTraceProto parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new SystemUiTraceProto().mergeFrom(codedInputByteBufferNano);
    }
}
