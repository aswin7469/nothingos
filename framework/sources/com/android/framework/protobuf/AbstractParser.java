package com.android.framework.protobuf;

import com.android.framework.protobuf.AbstractMessageLite;
import com.android.framework.protobuf.MessageLite;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
/* loaded from: classes3.dex */
public abstract class AbstractParser<MessageType extends MessageLite> implements Parser<MessageType> {
    private static final ExtensionRegistryLite EMPTY_REGISTRY = ExtensionRegistryLite.getEmptyRegistry();

    private UninitializedMessageException newUninitializedMessageException(MessageType message) {
        if (message instanceof AbstractMessageLite) {
            return ((AbstractMessageLite) message).newUninitializedMessageException();
        }
        return new UninitializedMessageException(message);
    }

    private MessageType checkMessageInitialized(MessageType message) throws InvalidProtocolBufferException {
        if (message != null && !message.isInitialized()) {
            throw newUninitializedMessageException(message).asInvalidProtocolBufferException().setUnfinishedMessage(message);
        }
        return message;
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parsePartialFrom */
    public MessageType mo3207parsePartialFrom(CodedInputStream input) throws InvalidProtocolBufferException {
        return (MessageType) parsePartialFrom(input, EMPTY_REGISTRY);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseFrom */
    public MessageType mo3194parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return (MessageType) checkMessageInitialized((MessageLite) parsePartialFrom(input, extensionRegistry));
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseFrom */
    public MessageType mo3193parseFrom(CodedInputStream input) throws InvalidProtocolBufferException {
        return mo3194parseFrom(input, EMPTY_REGISTRY);
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parsePartialFrom */
    public MessageType mo3206parsePartialFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        try {
            CodedInputStream input = data.newCodedInput();
            MessageType message = (MessageType) parsePartialFrom(input, extensionRegistry);
            try {
                input.checkLastTagWas(0);
                return message;
            } catch (InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(message);
            }
        } catch (InvalidProtocolBufferException e2) {
            throw e2;
        }
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parsePartialFrom */
    public MessageType mo3205parsePartialFrom(ByteString data) throws InvalidProtocolBufferException {
        return mo3206parsePartialFrom(data, EMPTY_REGISTRY);
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseFrom */
    public MessageType mo3192parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized(mo3206parsePartialFrom(data, extensionRegistry));
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseFrom */
    public MessageType mo3191parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return mo3192parseFrom(data, EMPTY_REGISTRY);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseFrom */
    public MessageType mo3198parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        try {
            CodedInputStream input = CodedInputStream.newInstance(data);
            MessageLite messageLite = (MessageLite) parsePartialFrom(input, extensionRegistry);
            try {
                input.checkLastTagWas(0);
                return (MessageType) checkMessageInitialized(messageLite);
            } catch (InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(messageLite);
            }
        } catch (InvalidProtocolBufferException e2) {
            throw e2;
        }
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseFrom */
    public MessageType mo3197parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return mo3198parseFrom(data, EMPTY_REGISTRY);
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parsePartialFrom */
    public MessageType mo3234parsePartialFrom(byte[] data, int off, int len, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        try {
            CodedInputStream input = CodedInputStream.newInstance(data, off, len);
            MessageType message = (MessageType) parsePartialFrom(input, extensionRegistry);
            try {
                input.checkLastTagWas(0);
                return message;
            } catch (InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(message);
            }
        } catch (InvalidProtocolBufferException e2) {
            throw e2;
        }
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parsePartialFrom */
    public MessageType mo3211parsePartialFrom(byte[] data, int off, int len) throws InvalidProtocolBufferException {
        return mo3234parsePartialFrom(data, off, len, EMPTY_REGISTRY);
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parsePartialFrom */
    public MessageType mo3213parsePartialFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return mo3234parsePartialFrom(data, 0, data.length, extensionRegistry);
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parsePartialFrom */
    public MessageType mo3210parsePartialFrom(byte[] data) throws InvalidProtocolBufferException {
        return mo3234parsePartialFrom(data, 0, data.length, EMPTY_REGISTRY);
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseFrom */
    public MessageType mo3201parseFrom(byte[] data, int off, int len, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized(mo3234parsePartialFrom(data, off, len, extensionRegistry));
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseFrom */
    public MessageType mo3200parseFrom(byte[] data, int off, int len) throws InvalidProtocolBufferException {
        return mo3201parseFrom(data, off, len, EMPTY_REGISTRY);
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseFrom */
    public MessageType mo3202parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return mo3201parseFrom(data, 0, data.length, extensionRegistry);
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseFrom */
    public MessageType mo3199parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return mo3202parseFrom(data, EMPTY_REGISTRY);
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parsePartialFrom */
    public MessageType mo3209parsePartialFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        CodedInputStream codedInput = CodedInputStream.newInstance(input);
        MessageType message = (MessageType) parsePartialFrom(codedInput, extensionRegistry);
        try {
            codedInput.checkLastTagWas(0);
            return message;
        } catch (InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(message);
        }
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parsePartialFrom */
    public MessageType mo3208parsePartialFrom(InputStream input) throws InvalidProtocolBufferException {
        return mo3209parsePartialFrom(input, EMPTY_REGISTRY);
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseFrom */
    public MessageType mo3196parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized(mo3209parsePartialFrom(input, extensionRegistry));
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseFrom */
    public MessageType mo3195parseFrom(InputStream input) throws InvalidProtocolBufferException {
        return mo3196parseFrom(input, EMPTY_REGISTRY);
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parsePartialDelimitedFrom */
    public MessageType mo3204parsePartialDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        try {
            int firstByte = input.read();
            if (firstByte == -1) {
                return null;
            }
            int size = CodedInputStream.readRawVarint32(firstByte, input);
            InputStream limitedInput = new AbstractMessageLite.Builder.LimitedInputStream(input, size);
            return mo3209parsePartialFrom(limitedInput, extensionRegistry);
        } catch (IOException e) {
            throw new InvalidProtocolBufferException(e);
        }
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parsePartialDelimitedFrom */
    public MessageType mo3203parsePartialDelimitedFrom(InputStream input) throws InvalidProtocolBufferException {
        return mo3204parsePartialDelimitedFrom(input, EMPTY_REGISTRY);
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseDelimitedFrom */
    public MessageType mo3190parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return checkMessageInitialized(mo3204parsePartialDelimitedFrom(input, extensionRegistry));
    }

    @Override // com.android.framework.protobuf.Parser
    /* renamed from: parseDelimitedFrom */
    public MessageType mo3189parseDelimitedFrom(InputStream input) throws InvalidProtocolBufferException {
        return mo3190parseDelimitedFrom(input, EMPTY_REGISTRY);
    }
}
