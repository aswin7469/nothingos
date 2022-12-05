package com.android.systemui.tracing.nano;

import com.android.wm.shell.nano.WmShellTraceProto;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public final class SystemUiTraceProto extends MessageNano {
    public EdgeBackGestureHandlerProto edgeBackGestureHandler;
    public WmShellTraceProto wmShell;

    public SystemUiTraceProto() {
        clear();
    }

    public SystemUiTraceProto clear() {
        this.edgeBackGestureHandler = null;
        this.wmShell = null;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        EdgeBackGestureHandlerProto edgeBackGestureHandlerProto = this.edgeBackGestureHandler;
        if (edgeBackGestureHandlerProto != null) {
            computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(1, edgeBackGestureHandlerProto);
        }
        WmShellTraceProto wmShellTraceProto = this.wmShell;
        return wmShellTraceProto != null ? computeSerializedSize + CodedOutputByteBufferNano.computeMessageSize(2, wmShellTraceProto) : computeSerializedSize;
    }
}
