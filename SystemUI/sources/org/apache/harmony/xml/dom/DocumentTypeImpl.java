package org.apache.harmony.xml.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;

public final class DocumentTypeImpl extends LeafNodeImpl implements DocumentType {
    private String publicId;
    private String qualifiedName;
    private String systemId;

    public NamedNodeMap getEntities() {
        return null;
    }

    public String getInternalSubset() {
        return null;
    }

    public short getNodeType() {
        return 10;
    }

    public NamedNodeMap getNotations() {
        return null;
    }

    public String getTextContent() throws DOMException {
        return null;
    }

    public DocumentTypeImpl(DocumentImpl documentImpl, String str, String str2, String str3) {
        super(documentImpl);
        if (str == null || "".equals(str)) {
            throw new DOMException(14, str);
        }
        int lastIndexOf = str.lastIndexOf(":");
        if (lastIndexOf != -1) {
            String substring = str.substring(0, lastIndexOf);
            String substring2 = str.substring(lastIndexOf + 1);
            if (!DocumentImpl.isXMLIdentifier(substring)) {
                throw new DOMException(14, str);
            } else if (!DocumentImpl.isXMLIdentifier(substring2)) {
                throw new DOMException(5, str);
            }
        } else if (!DocumentImpl.isXMLIdentifier(str)) {
            throw new DOMException(5, str);
        }
        this.qualifiedName = str;
        this.publicId = str2;
        this.systemId = str3;
    }

    public String getNodeName() {
        return this.qualifiedName;
    }

    public String getName() {
        return this.qualifiedName;
    }

    public String getPublicId() {
        return this.publicId;
    }

    public String getSystemId() {
        return this.systemId;
    }
}
