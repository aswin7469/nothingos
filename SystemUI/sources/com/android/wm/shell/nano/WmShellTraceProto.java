package com.android.wm.shell.nano;

import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
/* loaded from: classes2.dex */
public final class WmShellTraceProto extends MessageNano {
    public boolean testValue;

    public WmShellTraceProto() {
        clear();
    }

    public WmShellTraceProto clear() {
        this.testValue = false;
        this.cachedSize = -1;
        return this;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        boolean z = this.testValue;
        if (z) {
            codedOutputByteBufferNano.writeBool(1, z);
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.protobuf.nano.MessageNano
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        boolean z = this.testValue;
        return z ? computeSerializedSize + CodedOutputByteBufferNano.computeBoolSize(1, z) : computeSerializedSize;
    }
}
