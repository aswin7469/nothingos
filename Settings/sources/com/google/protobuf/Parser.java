package com.google.protobuf;
/* loaded from: classes2.dex */
public interface Parser<MessageType> {
    /* renamed from: parseFrom */
    MessageType mo896parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

    /* renamed from: parsePartialFrom */
    MessageType mo912parsePartialFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;
}
