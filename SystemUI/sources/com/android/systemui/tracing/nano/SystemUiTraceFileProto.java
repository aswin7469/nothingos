package com.android.systemui.tracing.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.p026io.IOException;

public final class SystemUiTraceFileProto extends MessageNano {
    public static final int INVALID = 0;
    public static final int MAGIC_NUMBER_H = 1129469001;
    public static final int MAGIC_NUMBER_L = 1431525715;
    private static volatile SystemUiTraceFileProto[] _emptyArray;
    public SystemUiTraceEntryProto[] entry;
    public long magicNumber;

    public static SystemUiTraceFileProto[] emptyArray() {
        if (_emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (_emptyArray == null) {
                    _emptyArray = new SystemUiTraceFileProto[0];
                }
            }
        }
        return _emptyArray;
    }

    public SystemUiTraceFileProto() {
        clear();
    }

    public SystemUiTraceFileProto clear() {
        this.magicNumber = 0;
        this.entry = SystemUiTraceEntryProto.emptyArray();
        this.cachedSize = -1;
        return this;
    }

    public void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        long j = this.magicNumber;
        if (j != 0) {
            codedOutputByteBufferNano.writeFixed64(1, j);
        }
        SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr = this.entry;
        if (systemUiTraceEntryProtoArr != null && systemUiTraceEntryProtoArr.length > 0) {
            int i = 0;
            while (true) {
                SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr2 = this.entry;
                if (i >= systemUiTraceEntryProtoArr2.length) {
                    break;
                }
                SystemUiTraceEntryProto systemUiTraceEntryProto = systemUiTraceEntryProtoArr2[i];
                if (systemUiTraceEntryProto != null) {
                    codedOutputByteBufferNano.writeMessage(2, systemUiTraceEntryProto);
                }
                i++;
            }
        }
        super.writeTo(codedOutputByteBufferNano);
    }

    /* access modifiers changed from: protected */
    public int computeSerializedSize() {
        int computeSerializedSize = super.computeSerializedSize();
        long j = this.magicNumber;
        if (j != 0) {
            computeSerializedSize += CodedOutputByteBufferNano.computeFixed64Size(1, j);
        }
        SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr = this.entry;
        if (systemUiTraceEntryProtoArr != null && systemUiTraceEntryProtoArr.length > 0) {
            int i = 0;
            while (true) {
                SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr2 = this.entry;
                if (i >= systemUiTraceEntryProtoArr2.length) {
                    break;
                }
                SystemUiTraceEntryProto systemUiTraceEntryProto = systemUiTraceEntryProtoArr2[i];
                if (systemUiTraceEntryProto != null) {
                    computeSerializedSize += CodedOutputByteBufferNano.computeMessageSize(2, systemUiTraceEntryProto);
                }
                i++;
            }
        }
        return computeSerializedSize;
    }

    public SystemUiTraceFileProto mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                return this;
            }
            if (readTag == 9) {
                this.magicNumber = codedInputByteBufferNano.readFixed64();
            } else if (readTag == 18) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr = this.entry;
                int length = systemUiTraceEntryProtoArr == null ? 0 : systemUiTraceEntryProtoArr.length;
                int i = repeatedFieldArrayLength + length;
                SystemUiTraceEntryProto[] systemUiTraceEntryProtoArr2 = new SystemUiTraceEntryProto[i];
                if (length != 0) {
                    System.arraycopy((Object) systemUiTraceEntryProtoArr, 0, (Object) systemUiTraceEntryProtoArr2, 0, length);
                }
                while (length < i - 1) {
                    SystemUiTraceEntryProto systemUiTraceEntryProto = new SystemUiTraceEntryProto();
                    systemUiTraceEntryProtoArr2[length] = systemUiTraceEntryProto;
                    codedInputByteBufferNano.readMessage(systemUiTraceEntryProto);
                    codedInputByteBufferNano.readTag();
                    length++;
                }
                SystemUiTraceEntryProto systemUiTraceEntryProto2 = new SystemUiTraceEntryProto();
                systemUiTraceEntryProtoArr2[length] = systemUiTraceEntryProto2;
                codedInputByteBufferNano.readMessage(systemUiTraceEntryProto2);
                this.entry = systemUiTraceEntryProtoArr2;
            } else if (!WireFormatNano.parseUnknownField(codedInputByteBufferNano, readTag)) {
                return this;
            }
        }
    }

    public static SystemUiTraceFileProto parseFrom(byte[] bArr) throws InvalidProtocolBufferNanoException {
        return (SystemUiTraceFileProto) MessageNano.mergeFrom(new SystemUiTraceFileProto(), bArr);
    }

    public static SystemUiTraceFileProto parseFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        return new SystemUiTraceFileProto().mergeFrom(codedInputByteBufferNano);
    }
}
