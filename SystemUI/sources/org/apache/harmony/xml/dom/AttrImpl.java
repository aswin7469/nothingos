package org.apache.harmony.xml.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.TypeInfo;

public final class AttrImpl extends NodeImpl implements Attr {
    boolean isId;
    String localName;
    boolean namespaceAware;
    String namespaceURI;
    ElementImpl ownerElement;
    String prefix;
    private String value = "";

    public short getNodeType() {
        return 2;
    }

    AttrImpl(DocumentImpl documentImpl, String str, String str2) {
        super(documentImpl);
        setNameNS(this, str, str2);
    }

    AttrImpl(DocumentImpl documentImpl, String str) {
        super(documentImpl);
        setName(this, str);
    }

    public String getLocalName() {
        if (this.namespaceAware) {
            return this.localName;
        }
        return null;
    }

    public String getName() {
        if (this.prefix == null) {
            return this.localName;
        }
        return this.prefix + ":" + this.localName;
    }

    public String getNamespaceURI() {
        return this.namespaceURI;
    }

    public String getNodeName() {
        return getName();
    }

    public String getNodeValue() {
        return getValue();
    }

    public Element getOwnerElement() {
        return this.ownerElement;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public boolean getSpecified() {
        return this.value != null;
    }

    public String getValue() {
        return this.value;
    }

    public void setPrefix(String str) {
        this.prefix = validatePrefix(str, this.namespaceAware, this.namespaceURI);
    }

    public void setValue(String str) throws DOMException {
        this.value = str;
    }

    public TypeInfo getSchemaTypeInfo() {
        return NULL_TYPE_INFO;
    }

    public boolean isId() {
        return this.isId;
    }
}
