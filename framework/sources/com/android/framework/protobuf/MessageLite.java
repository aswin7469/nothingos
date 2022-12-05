package com.android.framework.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: classes3.dex */
public interface MessageLite extends MessageLiteOrBuilder {

    /* loaded from: classes3.dex */
    public interface Builder extends MessageLiteOrBuilder, Cloneable {
        /* renamed from: build */
        MessageLite mo3225build();

        /* renamed from: buildPartial */
        MessageLite mo3236buildPartial();

        /* renamed from: clear */
        Builder mo3227clear();

        /* renamed from: clone */
        Builder mo3229clone();

        boolean mergeDelimitedFrom(InputStream inputStream) throws IOException;

        boolean mergeDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;

        /* renamed from: mergeFrom */
        Builder mo3178mergeFrom(ByteString byteString) throws InvalidProtocolBufferException;

        /* renamed from: mergeFrom */
        Builder mo3179mergeFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

        /* renamed from: mergeFrom */
        Builder mo3180mergeFrom(CodedInputStream codedInputStream) throws IOException;

        /* renamed from: mergeFrom */
        Builder mo3231mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;

        /* renamed from: mergeFrom */
        Builder mo3182mergeFrom(MessageLite messageLite);

        /* renamed from: mergeFrom */
        Builder mo3183mergeFrom(InputStream inputStream) throws IOException;

        /* renamed from: mergeFrom */
        Builder mo3184mergeFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;

        /* renamed from: mergeFrom */
        Builder mo3185mergeFrom(byte[] bArr) throws InvalidProtocolBufferException;

        /* renamed from: mergeFrom */
        Builder mo3232mergeFrom(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException;

        /* renamed from: mergeFrom */
        Builder mo3233mergeFrom(byte[] bArr, int i, int i2, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

        /* renamed from: mergeFrom */
        Builder mo3188mergeFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;
    }

    Parser<? extends MessageLite> getParserForType();

    int getSerializedSize();

    /* renamed from: newBuilderForType */
    Builder mo3223newBuilderForType();

    /* renamed from: toBuilder */
    Builder mo3224toBuilder();

    byte[] toByteArray();

    ByteString toByteString();

    void writeDelimitedTo(OutputStream outputStream) throws IOException;

    void writeTo(CodedOutputStream codedOutputStream) throws IOException;

    void writeTo(OutputStream outputStream) throws IOException;
}
