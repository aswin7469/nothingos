package com.google.protobuf.nano;

import com.google.protobuf.nano.ExtendableMessageNano;
import java.lang.reflect.Array;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Extension<M extends ExtendableMessageNano<M>, T> {
    public static final int TYPE_BOOL = 8;
    public static final int TYPE_BYTES = 12;
    public static final int TYPE_DOUBLE = 1;
    public static final int TYPE_ENUM = 14;
    public static final int TYPE_FIXED32 = 7;
    public static final int TYPE_FIXED64 = 6;
    public static final int TYPE_FLOAT = 2;
    public static final int TYPE_GROUP = 10;
    public static final int TYPE_INT32 = 5;
    public static final int TYPE_INT64 = 3;
    public static final int TYPE_MESSAGE = 11;
    public static final int TYPE_SFIXED32 = 15;
    public static final int TYPE_SFIXED64 = 16;
    public static final int TYPE_SINT32 = 17;
    public static final int TYPE_SINT64 = 18;
    public static final int TYPE_STRING = 9;
    public static final int TYPE_UINT32 = 13;
    public static final int TYPE_UINT64 = 4;
    protected final Class<T> clazz;
    protected final boolean repeated;
    public final int tag;
    protected final int type;

    @Deprecated
    public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(int i, Class<T> cls, int i2) {
        return new Extension<>(i, cls, i2, false);
    }

    public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T> createMessageTyped(int i, Class<T> cls, long j) {
        return new Extension<>(i, cls, (int) j, false);
    }

    public static <M extends ExtendableMessageNano<M>, T extends MessageNano> Extension<M, T[]> createRepeatedMessageTyped(int i, Class<T[]> cls, long j) {
        return new Extension<>(i, cls, (int) j, true);
    }

    public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createPrimitiveTyped(int i, Class<T> cls, long j) {
        return new PrimitiveExtension(i, cls, (int) j, false, 0, 0);
    }

    public static <M extends ExtendableMessageNano<M>, T> Extension<M, T> createRepeatedPrimitiveTyped(int i, Class<T> cls, long j, long j2, long j3) {
        return new PrimitiveExtension(i, cls, (int) j, true, (int) j2, (int) j3);
    }

    private Extension(int i, Class<T> cls, int i2, boolean z) {
        this.type = i;
        this.clazz = cls;
        this.tag = i2;
        this.repeated = z;
    }

    /* access modifiers changed from: package-private */
    public final T getValueFrom(List<UnknownFieldData> list) {
        if (list == null) {
            return null;
        }
        return this.repeated ? getRepeatedValueFrom(list) : getSingularValueFrom(list);
    }

    private T getRepeatedValueFrom(List<UnknownFieldData> list) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            UnknownFieldData unknownFieldData = list.get(i);
            if (unknownFieldData.bytes.length != 0) {
                readDataInto(unknownFieldData, arrayList);
            }
        }
        int size = arrayList.size();
        if (size == 0) {
            return null;
        }
        Class<T> cls = this.clazz;
        T cast = cls.cast(Array.newInstance(cls.getComponentType(), size));
        for (int i2 = 0; i2 < size; i2++) {
            Array.set(cast, i2, arrayList.get(i2));
        }
        return cast;
    }

    private T getSingularValueFrom(List<UnknownFieldData> list) {
        if (list.isEmpty()) {
            return null;
        }
        return this.clazz.cast(readData(CodedInputByteBufferNano.newInstance(list.get(list.size() - 1).bytes)));
    }

    /* access modifiers changed from: protected */
    public Object readData(CodedInputByteBufferNano codedInputByteBufferNano) {
        Class componentType = this.repeated ? this.clazz.getComponentType() : this.clazz;
        try {
            int i = this.type;
            if (i == 10) {
                MessageNano messageNano = (MessageNano) componentType.newInstance();
                codedInputByteBufferNano.readGroup(messageNano, WireFormatNano.getTagFieldNumber(this.tag));
                return messageNano;
            } else if (i == 11) {
                MessageNano messageNano2 = (MessageNano) componentType.newInstance();
                codedInputByteBufferNano.readMessage(messageNano2);
                return messageNano2;
            } else {
                throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType, e);
        } catch (IllegalAccessException e2) {
            throw new IllegalArgumentException("Error creating instance of class " + componentType, e2);
        } catch (IOException e3) {
            throw new IllegalArgumentException("Error reading extension field", e3);
        }
    }

    /* access modifiers changed from: protected */
    public void readDataInto(UnknownFieldData unknownFieldData, List<Object> list) {
        list.add(readData(CodedInputByteBufferNano.newInstance(unknownFieldData.bytes)));
    }

    /* access modifiers changed from: package-private */
    public void writeTo(Object obj, CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.repeated) {
            writeRepeatedData(obj, codedOutputByteBufferNano);
        } else {
            writeSingularData(obj, codedOutputByteBufferNano);
        }
    }

    /* access modifiers changed from: protected */
    public void writeSingularData(Object obj, CodedOutputByteBufferNano codedOutputByteBufferNano) {
        try {
            codedOutputByteBufferNano.writeRawVarint32(this.tag);
            int i = this.type;
            if (i == 10) {
                int tagFieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
                codedOutputByteBufferNano.writeGroupNoTag((MessageNano) obj);
                codedOutputByteBufferNano.writeTag(tagFieldNumber, 4);
            } else if (i == 11) {
                codedOutputByteBufferNano.writeMessageNoTag((MessageNano) obj);
            } else {
                throw new IllegalArgumentException("Unknown type " + this.type);
            }
        } catch (IOException e) {
            throw new IllegalStateException((Throwable) e);
        }
    }

    /* access modifiers changed from: protected */
    public void writeRepeatedData(Object obj, CodedOutputByteBufferNano codedOutputByteBufferNano) {
        int length = Array.getLength(obj);
        for (int i = 0; i < length; i++) {
            Object obj2 = Array.get(obj, i);
            if (obj2 != null) {
                writeSingularData(obj2, codedOutputByteBufferNano);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public int computeSerializedSize(Object obj) {
        if (this.repeated) {
            return computeRepeatedSerializedSize(obj);
        }
        return computeSingularSerializedSize(obj);
    }

    /* access modifiers changed from: protected */
    public int computeRepeatedSerializedSize(Object obj) {
        int length = Array.getLength(obj);
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (Array.get(obj, i2) != null) {
                i += computeSingularSerializedSize(Array.get(obj, i2));
            }
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public int computeSingularSerializedSize(Object obj) {
        int tagFieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
        int i = this.type;
        if (i == 10) {
            return CodedOutputByteBufferNano.computeGroupSize(tagFieldNumber, (MessageNano) obj);
        }
        if (i == 11) {
            return CodedOutputByteBufferNano.computeMessageSize(tagFieldNumber, (MessageNano) obj);
        }
        throw new IllegalArgumentException("Unknown type " + this.type);
    }

    private static class PrimitiveExtension<M extends ExtendableMessageNano<M>, T> extends Extension<M, T> {
        private final int nonPackedTag;
        private final int packedTag;

        public PrimitiveExtension(int i, Class<T> cls, int i2, boolean z, int i3, int i4) {
            super(i, cls, i2, z);
            this.nonPackedTag = i3;
            this.packedTag = i4;
        }

        /* access modifiers changed from: protected */
        public Object readData(CodedInputByteBufferNano codedInputByteBufferNano) {
            try {
                return codedInputByteBufferNano.readPrimitiveField(this.type);
            } catch (IOException e) {
                throw new IllegalArgumentException("Error reading extension field", e);
            }
        }

        /* access modifiers changed from: protected */
        public void readDataInto(UnknownFieldData unknownFieldData, List<Object> list) {
            if (unknownFieldData.tag == this.nonPackedTag) {
                list.add(readData(CodedInputByteBufferNano.newInstance(unknownFieldData.bytes)));
                return;
            }
            CodedInputByteBufferNano newInstance = CodedInputByteBufferNano.newInstance(unknownFieldData.bytes);
            try {
                newInstance.pushLimit(newInstance.readRawVarint32());
                while (!newInstance.isAtEnd()) {
                    list.add(readData(newInstance));
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("Error reading extension field", e);
            }
        }

        /* access modifiers changed from: protected */
        public final void writeSingularData(Object obj, CodedOutputByteBufferNano codedOutputByteBufferNano) {
            try {
                codedOutputByteBufferNano.writeRawVarint32(this.tag);
                switch (this.type) {
                    case 1:
                        codedOutputByteBufferNano.writeDoubleNoTag(((Double) obj).doubleValue());
                        return;
                    case 2:
                        codedOutputByteBufferNano.writeFloatNoTag(((Float) obj).floatValue());
                        return;
                    case 3:
                        codedOutputByteBufferNano.writeInt64NoTag(((Long) obj).longValue());
                        return;
                    case 4:
                        codedOutputByteBufferNano.writeUInt64NoTag(((Long) obj).longValue());
                        return;
                    case 5:
                        codedOutputByteBufferNano.writeInt32NoTag(((Integer) obj).intValue());
                        return;
                    case 6:
                        codedOutputByteBufferNano.writeFixed64NoTag(((Long) obj).longValue());
                        return;
                    case 7:
                        codedOutputByteBufferNano.writeFixed32NoTag(((Integer) obj).intValue());
                        return;
                    case 8:
                        codedOutputByteBufferNano.writeBoolNoTag(((Boolean) obj).booleanValue());
                        return;
                    case 9:
                        codedOutputByteBufferNano.writeStringNoTag((String) obj);
                        return;
                    case 12:
                        codedOutputByteBufferNano.writeBytesNoTag((byte[]) obj);
                        return;
                    case 13:
                        codedOutputByteBufferNano.writeUInt32NoTag(((Integer) obj).intValue());
                        return;
                    case 14:
                        codedOutputByteBufferNano.writeEnumNoTag(((Integer) obj).intValue());
                        return;
                    case 15:
                        codedOutputByteBufferNano.writeSFixed32NoTag(((Integer) obj).intValue());
                        return;
                    case 16:
                        codedOutputByteBufferNano.writeSFixed64NoTag(((Long) obj).longValue());
                        return;
                    case 17:
                        codedOutputByteBufferNano.writeSInt32NoTag(((Integer) obj).intValue());
                        return;
                    case 18:
                        codedOutputByteBufferNano.writeSInt64NoTag(((Long) obj).longValue());
                        return;
                    default:
                        throw new IllegalArgumentException("Unknown type " + this.type);
                }
            } catch (IOException e) {
                throw new IllegalStateException((Throwable) e);
            }
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:10:0x002d, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x002f, code lost:
            r8.writeSInt64NoTag(java.lang.reflect.Array.getLong(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x0039, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x003b, code lost:
            r8.writeSInt32NoTag(java.lang.reflect.Array.getInt(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:14:0x0045, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:15:0x0047, code lost:
            r8.writeSFixed64NoTag(java.lang.reflect.Array.getLong(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:16:0x0051, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:17:0x0053, code lost:
            r8.writeSFixed32NoTag(java.lang.reflect.Array.getInt(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:18:0x005d, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x005f, code lost:
            r8.writeEnumNoTag(java.lang.reflect.Array.getInt(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0069, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:21:0x006b, code lost:
            r8.writeUInt32NoTag(java.lang.reflect.Array.getInt(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x0075, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x0077, code lost:
            r8.writeBoolNoTag(java.lang.reflect.Array.getBoolean(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0081, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0083, code lost:
            r8.writeFixed32NoTag(java.lang.reflect.Array.getInt(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:26:0x008d, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:27:0x008f, code lost:
            r8.writeFixed64NoTag(java.lang.reflect.Array.getLong(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:28:0x0099, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:29:0x009b, code lost:
            r8.writeInt32NoTag(java.lang.reflect.Array.getInt(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:30:0x00a5, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:31:0x00a7, code lost:
            r8.writeUInt64NoTag(java.lang.reflect.Array.getLong(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:32:0x00b1, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:33:0x00b3, code lost:
            r8.writeInt64NoTag(java.lang.reflect.Array.getLong(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:34:0x00bd, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:35:0x00bf, code lost:
            r8.writeFloatNoTag(java.lang.reflect.Array.getFloat(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:36:0x00c9, code lost:
            if (r3 >= r1) goto L_0x00d5;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:37:0x00cb, code lost:
            r8.writeDoubleNoTag(java.lang.reflect.Array.getDouble(r7, r3));
            r3 = r3 + 1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:60:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:61:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:62:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:63:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:64:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:66:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:70:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:71:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:72:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:73:?, code lost:
            return;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void writeRepeatedData(java.lang.Object r7, com.google.protobuf.nano.CodedOutputByteBufferNano r8) {
            /*
                r6 = this;
                java.lang.String r0 = "Unpackable type "
                int r1 = r6.tag
                int r2 = r6.nonPackedTag
                if (r1 != r2) goto L_0x000d
                com.google.protobuf.nano.Extension.super.writeRepeatedData(r7, r8)
                goto L_0x00d5
            L_0x000d:
                int r1 = r6.tag
                int r2 = r6.packedTag
                if (r1 != r2) goto L_0x00f0
                int r1 = java.lang.reflect.Array.getLength(r7)
                int r2 = r6.computePackedDataSize(r7)
                int r3 = r6.tag     // Catch:{ IOException -> 0x00e9 }
                r8.writeRawVarint32(r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeRawVarint32(r2)     // Catch:{ IOException -> 0x00e9 }
                int r2 = r6.type     // Catch:{ IOException -> 0x00e9 }
                r3 = 0
                switch(r2) {
                    case 1: goto L_0x00c9;
                    case 2: goto L_0x00bd;
                    case 3: goto L_0x00b1;
                    case 4: goto L_0x00a5;
                    case 5: goto L_0x0099;
                    case 6: goto L_0x008d;
                    case 7: goto L_0x0081;
                    case 8: goto L_0x0075;
                    case 9: goto L_0x0029;
                    case 10: goto L_0x0029;
                    case 11: goto L_0x0029;
                    case 12: goto L_0x0029;
                    case 13: goto L_0x0069;
                    case 14: goto L_0x005d;
                    case 15: goto L_0x0051;
                    case 16: goto L_0x0045;
                    case 17: goto L_0x0039;
                    case 18: goto L_0x002d;
                    default: goto L_0x0029;
                }     // Catch:{ IOException -> 0x00e9 }
            L_0x0029:
                java.lang.IllegalArgumentException r7 = new java.lang.IllegalArgumentException     // Catch:{ IOException -> 0x00e9 }
                goto L_0x00d6
            L_0x002d:
                if (r3 >= r1) goto L_0x00d5
                long r4 = java.lang.reflect.Array.getLong(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeSInt64NoTag(r4)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x002d
            L_0x0039:
                if (r3 >= r1) goto L_0x00d5
                int r6 = java.lang.reflect.Array.getInt(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeSInt32NoTag(r6)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x0039
            L_0x0045:
                if (r3 >= r1) goto L_0x00d5
                long r4 = java.lang.reflect.Array.getLong(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeSFixed64NoTag(r4)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x0045
            L_0x0051:
                if (r3 >= r1) goto L_0x00d5
                int r6 = java.lang.reflect.Array.getInt(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeSFixed32NoTag(r6)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x0051
            L_0x005d:
                if (r3 >= r1) goto L_0x00d5
                int r6 = java.lang.reflect.Array.getInt(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeEnumNoTag(r6)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x005d
            L_0x0069:
                if (r3 >= r1) goto L_0x00d5
                int r6 = java.lang.reflect.Array.getInt(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeUInt32NoTag(r6)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x0069
            L_0x0075:
                if (r3 >= r1) goto L_0x00d5
                boolean r6 = java.lang.reflect.Array.getBoolean(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeBoolNoTag(r6)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x0075
            L_0x0081:
                if (r3 >= r1) goto L_0x00d5
                int r6 = java.lang.reflect.Array.getInt(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeFixed32NoTag(r6)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x0081
            L_0x008d:
                if (r3 >= r1) goto L_0x00d5
                long r4 = java.lang.reflect.Array.getLong(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeFixed64NoTag(r4)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x008d
            L_0x0099:
                if (r3 >= r1) goto L_0x00d5
                int r6 = java.lang.reflect.Array.getInt(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeInt32NoTag(r6)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x0099
            L_0x00a5:
                if (r3 >= r1) goto L_0x00d5
                long r4 = java.lang.reflect.Array.getLong(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeUInt64NoTag(r4)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x00a5
            L_0x00b1:
                if (r3 >= r1) goto L_0x00d5
                long r4 = java.lang.reflect.Array.getLong(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeInt64NoTag(r4)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x00b1
            L_0x00bd:
                if (r3 >= r1) goto L_0x00d5
                float r6 = java.lang.reflect.Array.getFloat(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeFloatNoTag(r6)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x00bd
            L_0x00c9:
                if (r3 >= r1) goto L_0x00d5
                double r4 = java.lang.reflect.Array.getDouble(r7, r3)     // Catch:{ IOException -> 0x00e9 }
                r8.writeDoubleNoTag(r4)     // Catch:{ IOException -> 0x00e9 }
                int r3 = r3 + 1
                goto L_0x00c9
            L_0x00d5:
                return
            L_0x00d6:
                java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x00e9 }
                r8.<init>((java.lang.String) r0)     // Catch:{ IOException -> 0x00e9 }
                int r6 = r6.type     // Catch:{ IOException -> 0x00e9 }
                java.lang.StringBuilder r6 = r8.append((int) r6)     // Catch:{ IOException -> 0x00e9 }
                java.lang.String r6 = r6.toString()     // Catch:{ IOException -> 0x00e9 }
                r7.<init>((java.lang.String) r6)     // Catch:{ IOException -> 0x00e9 }
                throw r7     // Catch:{ IOException -> 0x00e9 }
            L_0x00e9:
                r6 = move-exception
                java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
                r7.<init>((java.lang.Throwable) r6)
                throw r7
            L_0x00f0:
                java.lang.IllegalArgumentException r7 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r8 = new java.lang.StringBuilder
                java.lang.String r0 = "Unexpected repeated extension tag "
                r8.<init>((java.lang.String) r0)
                int r0 = r6.tag
                java.lang.StringBuilder r8 = r8.append((int) r0)
                java.lang.String r0 = ", unequal to both non-packed variant "
                java.lang.StringBuilder r8 = r8.append((java.lang.String) r0)
                int r0 = r6.nonPackedTag
                java.lang.StringBuilder r8 = r8.append((int) r0)
                java.lang.String r0 = " and packed variant "
                java.lang.StringBuilder r8 = r8.append((java.lang.String) r0)
                int r6 = r6.packedTag
                java.lang.StringBuilder r6 = r8.append((int) r6)
                java.lang.String r6 = r6.toString()
                r7.<init>((java.lang.String) r6)
                throw r7
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.protobuf.nano.Extension.PrimitiveExtension.writeRepeatedData(java.lang.Object, com.google.protobuf.nano.CodedOutputByteBufferNano):void");
        }

        private int computePackedDataSize(Object obj) {
            int i;
            int length = Array.getLength(obj);
            int i2 = 0;
            switch (this.type) {
                case 1:
                case 6:
                case 16:
                    return length * 8;
                case 2:
                case 7:
                case 15:
                    return length * 4;
                case 3:
                    int i3 = 0;
                    while (i2 < length) {
                        i3 = i + CodedOutputByteBufferNano.computeInt64SizeNoTag(Array.getLong(obj, i2));
                        i2++;
                    }
                    break;
                case 4:
                    int i4 = 0;
                    while (i2 < length) {
                        i4 = i + CodedOutputByteBufferNano.computeUInt64SizeNoTag(Array.getLong(obj, i2));
                        i2++;
                    }
                    break;
                case 5:
                    int i5 = 0;
                    while (i2 < length) {
                        i5 = i + CodedOutputByteBufferNano.computeInt32SizeNoTag(Array.getInt(obj, i2));
                        i2++;
                    }
                    break;
                case 8:
                    return length;
                case 13:
                    int i6 = 0;
                    while (i2 < length) {
                        i6 = i + CodedOutputByteBufferNano.computeUInt32SizeNoTag(Array.getInt(obj, i2));
                        i2++;
                    }
                    break;
                case 14:
                    int i7 = 0;
                    while (i2 < length) {
                        i7 = i + CodedOutputByteBufferNano.computeEnumSizeNoTag(Array.getInt(obj, i2));
                        i2++;
                    }
                    break;
                case 17:
                    int i8 = 0;
                    while (i2 < length) {
                        i8 = i + CodedOutputByteBufferNano.computeSInt32SizeNoTag(Array.getInt(obj, i2));
                        i2++;
                    }
                    break;
                case 18:
                    i = 0;
                    while (i2 < length) {
                        i += CodedOutputByteBufferNano.computeSInt64SizeNoTag(Array.getLong(obj, i2));
                        i2++;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Unexpected non-packable type " + this.type);
            }
            return i;
        }

        /* access modifiers changed from: protected */
        public int computeRepeatedSerializedSize(Object obj) {
            if (this.tag == this.nonPackedTag) {
                return Extension.super.computeRepeatedSerializedSize(obj);
            }
            if (this.tag == this.packedTag) {
                int computePackedDataSize = computePackedDataSize(obj);
                return computePackedDataSize + CodedOutputByteBufferNano.computeRawVarint32Size(computePackedDataSize) + CodedOutputByteBufferNano.computeRawVarint32Size(this.tag);
            }
            throw new IllegalArgumentException("Unexpected repeated extension tag " + this.tag + ", unequal to both non-packed variant " + this.nonPackedTag + " and packed variant " + this.packedTag);
        }

        /* access modifiers changed from: protected */
        public final int computeSingularSerializedSize(Object obj) {
            int tagFieldNumber = WireFormatNano.getTagFieldNumber(this.tag);
            switch (this.type) {
                case 1:
                    return CodedOutputByteBufferNano.computeDoubleSize(tagFieldNumber, ((Double) obj).doubleValue());
                case 2:
                    return CodedOutputByteBufferNano.computeFloatSize(tagFieldNumber, ((Float) obj).floatValue());
                case 3:
                    return CodedOutputByteBufferNano.computeInt64Size(tagFieldNumber, ((Long) obj).longValue());
                case 4:
                    return CodedOutputByteBufferNano.computeUInt64Size(tagFieldNumber, ((Long) obj).longValue());
                case 5:
                    return CodedOutputByteBufferNano.computeInt32Size(tagFieldNumber, ((Integer) obj).intValue());
                case 6:
                    return CodedOutputByteBufferNano.computeFixed64Size(tagFieldNumber, ((Long) obj).longValue());
                case 7:
                    return CodedOutputByteBufferNano.computeFixed32Size(tagFieldNumber, ((Integer) obj).intValue());
                case 8:
                    return CodedOutputByteBufferNano.computeBoolSize(tagFieldNumber, ((Boolean) obj).booleanValue());
                case 9:
                    return CodedOutputByteBufferNano.computeStringSize(tagFieldNumber, (String) obj);
                case 12:
                    return CodedOutputByteBufferNano.computeBytesSize(tagFieldNumber, (byte[]) obj);
                case 13:
                    return CodedOutputByteBufferNano.computeUInt32Size(tagFieldNumber, ((Integer) obj).intValue());
                case 14:
                    return CodedOutputByteBufferNano.computeEnumSize(tagFieldNumber, ((Integer) obj).intValue());
                case 15:
                    return CodedOutputByteBufferNano.computeSFixed32Size(tagFieldNumber, ((Integer) obj).intValue());
                case 16:
                    return CodedOutputByteBufferNano.computeSFixed64Size(tagFieldNumber, ((Long) obj).longValue());
                case 17:
                    return CodedOutputByteBufferNano.computeSInt32Size(tagFieldNumber, ((Integer) obj).intValue());
                case 18:
                    return CodedOutputByteBufferNano.computeSInt64Size(tagFieldNumber, ((Long) obj).longValue());
                default:
                    throw new IllegalArgumentException("Unknown type " + this.type);
            }
        }
    }
}
