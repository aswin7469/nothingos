package org.apache.harmony.xml.dom;

import org.w3c.dom.EntityReference;

public class EntityReferenceImpl extends LeafNodeImpl implements EntityReference {
    private String name;

    public short getNodeType() {
        return 5;
    }

    EntityReferenceImpl(DocumentImpl documentImpl, String str) {
        super(documentImpl);
        this.name = str;
    }

    public String getNodeName() {
        return this.name;
    }
}
