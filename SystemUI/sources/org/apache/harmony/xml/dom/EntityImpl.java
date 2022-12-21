package org.apache.harmony.xml.dom;

import org.w3c.dom.Entity;

public class EntityImpl extends NodeImpl implements Entity {
    private String notationName;
    private String publicID;
    private String systemID;

    public short getNodeType() {
        return 6;
    }

    EntityImpl(DocumentImpl documentImpl, String str, String str2, String str3) {
        super(documentImpl);
        this.notationName = str;
        this.publicID = str2;
        this.systemID = str3;
    }

    public String getNodeName() {
        return getNotationName();
    }

    public String getNotationName() {
        return this.notationName;
    }

    public String getPublicId() {
        return this.publicID;
    }

    public String getSystemId() {
        return this.systemID;
    }

    public String getInputEncoding() {
        throw new UnsupportedOperationException();
    }

    public String getXmlEncoding() {
        throw new UnsupportedOperationException();
    }

    public String getXmlVersion() {
        throw new UnsupportedOperationException();
    }
}
