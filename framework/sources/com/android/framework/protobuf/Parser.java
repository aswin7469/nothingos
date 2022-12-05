package com.android.framework.protobuf;

import java.io.InputStream;
import java.nio.ByteBuffer;
/* loaded from: classes3.dex */
public interface Parser<MessageType> {
    /* renamed from: parseDelimitedFrom */
    MessageType mo3189parseDelimitedFrom(InputStream inputStream) throws InvalidProtocolBufferException;

    /* renamed from: parseDelimitedFrom */
    MessageType mo3190parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo3191parseFrom(ByteString byteString) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo3192parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo3193parseFrom(CodedInputStream codedInputStream) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo3194parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo3195parseFrom(InputStream inputStream) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo3196parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo3197parseFrom(ByteBuffer byteBuffer) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo3198parseFrom(ByteBuffer byteBuffer, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo3199parseFrom(byte[] bArr) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo3200parseFrom(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo3201parseFrom(byte[] bArr, int i, int i2, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parseFrom */
    MessageType mo3202parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialDelimitedFrom */
    MessageType mo3203parsePartialDelimitedFrom(InputStream inputStream) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialDelimitedFrom */
    MessageType mo3204parsePartialDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo3205parsePartialFrom(ByteString byteString) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo3206parsePartialFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo3207parsePartialFrom(CodedInputStream codedInputStream) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo3235parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo3208parsePartialFrom(InputStream inputStream) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo3209parsePartialFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo3210parsePartialFrom(byte[] bArr) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo3211parsePartialFrom(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo3234parsePartialFrom(byte[] bArr, int i, int i2, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo3213parsePartialFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;
}
