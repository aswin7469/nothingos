package com.android.framework.protobuf;

import java.io.IOException;
/* loaded from: classes3.dex */
class UnknownFieldSetLiteSchema extends UnknownFieldSchema<UnknownFieldSetLite, UnknownFieldSetLite> {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public boolean shouldDiscardUnknownFields(Reader reader) {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    /* renamed from: newBuilder */
    public UnknownFieldSetLite mo3258newBuilder() {
        return UnknownFieldSetLite.newInstance();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public void addVarint(UnknownFieldSetLite fields, int number, long value) {
        fields.storeField(WireFormat.makeTag(number, 0), Long.valueOf(value));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public void addFixed32(UnknownFieldSetLite fields, int number, int value) {
        fields.storeField(WireFormat.makeTag(number, 5), Integer.valueOf(value));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public void addFixed64(UnknownFieldSetLite fields, int number, long value) {
        fields.storeField(WireFormat.makeTag(number, 1), Long.valueOf(value));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public void addLengthDelimited(UnknownFieldSetLite fields, int number, ByteString value) {
        fields.storeField(WireFormat.makeTag(number, 2), value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public void addGroup(UnknownFieldSetLite fields, int number, UnknownFieldSetLite subFieldSet) {
        fields.storeField(WireFormat.makeTag(number, 3), subFieldSet);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public UnknownFieldSetLite toImmutable(UnknownFieldSetLite fields) {
        fields.makeImmutable();
        return fields;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public void setToMessage(Object message, UnknownFieldSetLite fields) {
        ((GeneratedMessageLite) message).unknownFields = fields;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    /* renamed from: getFromMessage */
    public UnknownFieldSetLite mo3257getFromMessage(Object message) {
        return ((GeneratedMessageLite) message).unknownFields;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    /* renamed from: getBuilderFromMessage */
    public UnknownFieldSetLite mo3256getBuilderFromMessage(Object message) {
        UnknownFieldSetLite unknownFields = mo3257getFromMessage(message);
        if (unknownFields == UnknownFieldSetLite.getDefaultInstance()) {
            UnknownFieldSetLite unknownFields2 = UnknownFieldSetLite.newInstance();
            setToMessage(message, unknownFields2);
            return unknownFields2;
        }
        return unknownFields;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public void setBuilderToMessage(Object message, UnknownFieldSetLite fields) {
        setToMessage(message, fields);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public void makeImmutable(Object message) {
        mo3257getFromMessage(message).makeImmutable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public void writeTo(UnknownFieldSetLite fields, Writer writer) throws IOException {
        fields.writeTo(writer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public void writeAsMessageSetTo(UnknownFieldSetLite fields, Writer writer) throws IOException {
        fields.writeAsMessageSetTo(writer);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public UnknownFieldSetLite merge(UnknownFieldSetLite message, UnknownFieldSetLite other) {
        if (other.equals(UnknownFieldSetLite.getDefaultInstance())) {
            return message;
        }
        return UnknownFieldSetLite.mutableCopyOf(message, other);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public int getSerializedSize(UnknownFieldSetLite unknowns) {
        return unknowns.getSerializedSize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.framework.protobuf.UnknownFieldSchema
    public int getSerializedSizeAsMessageSet(UnknownFieldSetLite unknowns) {
        return unknowns.getSerializedSizeAsMessageSet();
    }
}
