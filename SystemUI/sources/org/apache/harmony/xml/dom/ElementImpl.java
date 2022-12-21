package org.apache.harmony.xml.dom;

import android.net.wifi.WifiEnterpriseConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;

public class ElementImpl extends InnerNodeImpl implements Element {
    /* access modifiers changed from: private */
    public List<AttrImpl> attributes = new ArrayList();
    String localName;
    boolean namespaceAware;
    String namespaceURI;
    String prefix;

    public short getNodeType() {
        return 1;
    }

    ElementImpl(DocumentImpl documentImpl, String str, String str2) {
        super(documentImpl);
        setNameNS(this, str, str2);
    }

    ElementImpl(DocumentImpl documentImpl, String str) {
        super(documentImpl);
        setName(this, str);
    }

    /* access modifiers changed from: private */
    public int indexOfAttribute(String str) {
        for (int i = 0; i < this.attributes.size(); i++) {
            if (Objects.equals(str, this.attributes.get(i).getNodeName())) {
                return i;
            }
        }
        return -1;
    }

    /* access modifiers changed from: private */
    public int indexOfAttributeNS(String str, String str2) {
        for (int i = 0; i < this.attributes.size(); i++) {
            AttrImpl attrImpl = this.attributes.get(i);
            if (Objects.equals(str, attrImpl.getNamespaceURI()) && Objects.equals(str2, attrImpl.getLocalName())) {
                return i;
            }
        }
        return -1;
    }

    public String getAttribute(String str) {
        AttrImpl attributeNode = getAttributeNode(str);
        if (attributeNode == null) {
            return "";
        }
        return attributeNode.getValue();
    }

    public String getAttributeNS(String str, String str2) {
        AttrImpl attributeNodeNS = getAttributeNodeNS(str, str2);
        if (attributeNodeNS == null) {
            return "";
        }
        return attributeNodeNS.getValue();
    }

    public AttrImpl getAttributeNode(String str) {
        int indexOfAttribute = indexOfAttribute(str);
        if (indexOfAttribute == -1) {
            return null;
        }
        return this.attributes.get(indexOfAttribute);
    }

    public AttrImpl getAttributeNodeNS(String str, String str2) {
        int indexOfAttributeNS = indexOfAttributeNS(str, str2);
        if (indexOfAttributeNS == -1) {
            return null;
        }
        return this.attributes.get(indexOfAttributeNS);
    }

    public NamedNodeMap getAttributes() {
        return new ElementAttrNamedNodeMapImpl();
    }

    /* access modifiers changed from: package-private */
    public Element getElementById(String str) {
        Element elementById;
        for (Attr next : this.attributes) {
            if (next.isId() && str.equals(next.getValue())) {
                return this;
            }
        }
        if (str.equals(getAttribute("id"))) {
            return this;
        }
        for (NodeImpl nodeImpl : this.children) {
            if (nodeImpl.getNodeType() == 1 && (elementById = ((ElementImpl) nodeImpl).getElementById(str)) != null) {
                return elementById;
            }
        }
        return null;
    }

    public NodeList getElementsByTagName(String str) {
        NodeListImpl nodeListImpl = new NodeListImpl();
        getElementsByTagName(nodeListImpl, str);
        return nodeListImpl;
    }

    public NodeList getElementsByTagNameNS(String str, String str2) {
        NodeListImpl nodeListImpl = new NodeListImpl();
        getElementsByTagNameNS(nodeListImpl, str, str2);
        return nodeListImpl;
    }

    public String getLocalName() {
        if (this.namespaceAware) {
            return this.localName;
        }
        return null;
    }

    public String getNamespaceURI() {
        return this.namespaceURI;
    }

    public String getNodeName() {
        return getTagName();
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getTagName() {
        if (this.prefix == null) {
            return this.localName;
        }
        return this.prefix + ":" + this.localName;
    }

    public boolean hasAttribute(String str) {
        return indexOfAttribute(str) != -1;
    }

    public boolean hasAttributeNS(String str, String str2) {
        return indexOfAttributeNS(str, str2) != -1;
    }

    public boolean hasAttributes() {
        return !this.attributes.isEmpty();
    }

    public void removeAttribute(String str) throws DOMException {
        int indexOfAttribute = indexOfAttribute(str);
        if (indexOfAttribute != -1) {
            this.attributes.remove(indexOfAttribute);
        }
    }

    public void removeAttributeNS(String str, String str2) throws DOMException {
        int indexOfAttributeNS = indexOfAttributeNS(str, str2);
        if (indexOfAttributeNS != -1) {
            this.attributes.remove(indexOfAttributeNS);
        }
    }

    public Attr removeAttributeNode(Attr attr) throws DOMException {
        AttrImpl attrImpl = (AttrImpl) attr;
        if (attrImpl.getOwnerElement() == this) {
            this.attributes.remove((Object) attrImpl);
            attrImpl.ownerElement = null;
            return attrImpl;
        }
        throw new DOMException(8, (String) null);
    }

    public void setAttribute(String str, String str2) throws DOMException {
        AttrImpl attributeNode = getAttributeNode(str);
        if (attributeNode == null) {
            attributeNode = this.document.createAttribute(str);
            setAttributeNode(attributeNode);
        }
        attributeNode.setValue(str2);
    }

    public void setAttributeNS(String str, String str2, String str3) throws DOMException {
        AttrImpl attributeNodeNS = getAttributeNodeNS(str, str2);
        if (attributeNodeNS == null) {
            attributeNodeNS = this.document.createAttributeNS(str, str2);
            setAttributeNodeNS(attributeNodeNS);
        }
        attributeNodeNS.setValue(str3);
    }

    public Attr setAttributeNode(Attr attr) throws DOMException {
        AttrImpl attrImpl = (AttrImpl) attr;
        AttrImpl attrImpl2 = null;
        if (attrImpl.document != this.document) {
            throw new DOMException(4, (String) null);
        } else if (attrImpl.getOwnerElement() == null) {
            int indexOfAttribute = indexOfAttribute(attr.getName());
            if (indexOfAttribute != -1) {
                attrImpl2 = this.attributes.get(indexOfAttribute);
                this.attributes.remove(indexOfAttribute);
            }
            this.attributes.add(attrImpl);
            attrImpl.ownerElement = this;
            return attrImpl2;
        } else {
            throw new DOMException(10, (String) null);
        }
    }

    public Attr setAttributeNodeNS(Attr attr) throws DOMException {
        AttrImpl attrImpl = (AttrImpl) attr;
        AttrImpl attrImpl2 = null;
        if (attrImpl.document != this.document) {
            throw new DOMException(4, (String) null);
        } else if (attrImpl.getOwnerElement() == null) {
            int indexOfAttributeNS = indexOfAttributeNS(attr.getNamespaceURI(), attr.getLocalName());
            if (indexOfAttributeNS != -1) {
                attrImpl2 = this.attributes.get(indexOfAttributeNS);
                this.attributes.remove(indexOfAttributeNS);
            }
            this.attributes.add(attrImpl);
            attrImpl.ownerElement = this;
            return attrImpl2;
        } else {
            throw new DOMException(10, (String) null);
        }
    }

    public void setPrefix(String str) {
        this.prefix = validatePrefix(str, this.namespaceAware, this.namespaceURI);
    }

    public class ElementAttrNamedNodeMapImpl implements NamedNodeMap {
        public ElementAttrNamedNodeMapImpl() {
        }

        public int getLength() {
            return ElementImpl.this.attributes.size();
        }

        private int indexOfItem(String str) {
            return ElementImpl.this.indexOfAttribute(str);
        }

        private int indexOfItemNS(String str, String str2) {
            return ElementImpl.this.indexOfAttributeNS(str, str2);
        }

        public Node getNamedItem(String str) {
            return ElementImpl.this.getAttributeNode(str);
        }

        public Node getNamedItemNS(String str, String str2) {
            return ElementImpl.this.getAttributeNodeNS(str, str2);
        }

        public Node item(int i) {
            return (Node) ElementImpl.this.attributes.get(i);
        }

        public Node removeNamedItem(String str) throws DOMException {
            int indexOfItem = indexOfItem(str);
            if (indexOfItem != -1) {
                return (Node) ElementImpl.this.attributes.remove(indexOfItem);
            }
            throw new DOMException(8, (String) null);
        }

        public Node removeNamedItemNS(String str, String str2) throws DOMException {
            int indexOfItemNS = indexOfItemNS(str, str2);
            if (indexOfItemNS != -1) {
                return (Node) ElementImpl.this.attributes.remove(indexOfItemNS);
            }
            throw new DOMException(8, (String) null);
        }

        public Node setNamedItem(Node node) throws DOMException {
            if (node instanceof Attr) {
                return ElementImpl.this.setAttributeNode((Attr) node);
            }
            throw new DOMException(3, (String) null);
        }

        public Node setNamedItemNS(Node node) throws DOMException {
            if (node instanceof Attr) {
                return ElementImpl.this.setAttributeNodeNS((Attr) node);
            }
            throw new DOMException(3, (String) null);
        }
    }

    public TypeInfo getSchemaTypeInfo() {
        return NULL_TYPE_INFO;
    }

    public void setIdAttribute(String str, boolean z) throws DOMException {
        AttrImpl attributeNode = getAttributeNode(str);
        if (attributeNode != null) {
            attributeNode.isId = z;
            return;
        }
        throw new DOMException(8, "No such attribute: " + str);
    }

    public void setIdAttributeNS(String str, String str2, boolean z) throws DOMException {
        AttrImpl attributeNodeNS = getAttributeNodeNS(str, str2);
        if (attributeNodeNS != null) {
            attributeNodeNS.isId = z;
            return;
        }
        throw new DOMException(8, "No such attribute: " + str + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + str2);
    }

    public void setIdAttributeNode(Attr attr, boolean z) throws DOMException {
        ((AttrImpl) attr).isId = z;
    }
}
