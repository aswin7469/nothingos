package com.google.protobuf;

import java.io.IOException;
/* loaded from: classes2.dex */
public interface MessageLite extends MessageLiteOrBuilder {

    /* loaded from: classes2.dex */
    public interface Builder extends MessageLiteOrBuilder, Cloneable {
        /* renamed from: build */
        MessageLite mo908build();

        /* renamed from: buildPartial */
        MessageLite mo909buildPartial();

        /* renamed from: mergeFrom */
        Builder mo895mergeFrom(MessageLite messageLite);
    }

    Parser<? extends MessageLite> getParserForType();

    int getSerializedSize();

    /* renamed from: newBuilderForType */
    Builder mo906newBuilderForType();

    /* renamed from: toBuilder */
    Builder mo907toBuilder();

    ByteString toByteString();

    void writeTo(CodedOutputStream codedOutputStream) throws IOException;
}
