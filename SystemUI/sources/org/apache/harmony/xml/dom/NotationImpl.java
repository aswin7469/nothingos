package org.apache.harmony.xml.dom;

import org.w3c.dom.Notation;

public class NotationImpl extends LeafNodeImpl implements Notation {
    private String notationName;
    private String publicID;
    private String systemID;

    public short getNodeType() {
        return 12;
    }

    NotationImpl(DocumentImpl documentImpl, String str, String str2, String str3) {
        super(documentImpl);
    }

    public String getNodeName() {
        return this.notationName;
    }

    public String getPublicId() {
        return this.publicID;
    }

    public String getSystemId() {
        return this.systemID;
    }
}
