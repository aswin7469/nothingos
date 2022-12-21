package org.apache.harmony.xml.dom;

import org.w3c.dom.Comment;

public final class CommentImpl extends CharacterDataImpl implements Comment {
    public String getNodeName() {
        return "#comment";
    }

    public short getNodeType() {
        return 8;
    }

    CommentImpl(DocumentImpl documentImpl, String str) {
        super(documentImpl, str);
    }

    public boolean containsDashDash() {
        return this.buffer.indexOf("--") != -1;
    }
}
