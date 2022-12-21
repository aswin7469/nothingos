package org.apache.harmony.xml.dom;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.xml.XMLConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;

public abstract class NodeImpl implements Node {
    private static final NodeList EMPTY_LIST = new NodeListImpl();
    static final TypeInfo NULL_TYPE_INFO = new TypeInfo() {
        public String getTypeName() {
            return null;
        }

        public String getTypeNamespace() {
            return null;
        }

        public boolean isDerivedFrom(String str, String str2, int i) {
            return false;
        }
    };
    DocumentImpl document;

    public NamedNodeMap getAttributes() {
        return null;
    }

    public Node getFirstChild() {
        return null;
    }

    public Node getLastChild() {
        return null;
    }

    public String getLocalName() {
        return null;
    }

    public String getNamespaceURI() {
        return null;
    }

    public Node getNextSibling() {
        return null;
    }

    public String getNodeName() {
        return null;
    }

    public abstract short getNodeType();

    public String getNodeValue() throws DOMException {
        return null;
    }

    public Node getParentNode() {
        return null;
    }

    public String getPrefix() {
        return null;
    }

    public Node getPreviousSibling() {
        return null;
    }

    public boolean hasAttributes() {
        return false;
    }

    public boolean hasChildNodes() {
        return false;
    }

    public boolean isSameNode(Node node) {
        return this == node;
    }

    public void normalize() {
    }

    public void setPrefix(String str) throws DOMException {
    }

    NodeImpl(DocumentImpl documentImpl) {
        this.document = documentImpl;
    }

    public Node appendChild(Node node) throws DOMException {
        throw new DOMException(3, (String) null);
    }

    public final Node cloneNode(boolean z) {
        return this.document.cloneOrImportNode(1, this, z);
    }

    public NodeList getChildNodes() {
        return EMPTY_LIST;
    }

    public final Document getOwnerDocument() {
        DocumentImpl documentImpl = this.document;
        if (documentImpl == this) {
            return null;
        }
        return documentImpl;
    }

    public Node insertBefore(Node node, Node node2) throws DOMException {
        throw new DOMException(3, (String) null);
    }

    public boolean isSupported(String str, String str2) {
        return DOMImplementationImpl.getInstance().hasFeature(str, str2);
    }

    public Node removeChild(Node node) throws DOMException {
        throw new DOMException(3, (String) null);
    }

    public Node replaceChild(Node node, Node node2) throws DOMException {
        throw new DOMException(3, (String) null);
    }

    public final void setNodeValue(String str) throws DOMException {
        switch (getNodeType()) {
            case 1:
            case 5:
            case 6:
            case 9:
            case 10:
            case 11:
            case 12:
                return;
            case 2:
                ((Attr) this).setValue(str);
                return;
            case 3:
            case 4:
            case 8:
                ((CharacterData) this).setData(str);
                return;
            case 7:
                ((ProcessingInstruction) this).setData(str);
                return;
            default:
                throw new DOMException(9, "Unsupported node type " + getNodeType());
        }
    }

    static String validatePrefix(String str, boolean z, String str2) {
        if (!z) {
            throw new DOMException(14, str);
        } else if (str == null || (str2 != null && DocumentImpl.isXMLIdentifier(str) && ((!XMLConstants.XML_NS_PREFIX.equals(str) || "http://www.w3.org/XML/1998/namespace".equals(str2)) && (!XMLConstants.XMLNS_ATTRIBUTE.equals(str) || XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(str2))))) {
            return str;
        } else {
            throw new DOMException(14, str);
        }
    }

    static void setNameNS(NodeImpl nodeImpl, String str, String str2) {
        String str3;
        if (str2 != null) {
            int lastIndexOf = str2.lastIndexOf(":");
            if (lastIndexOf != -1) {
                str3 = validatePrefix(str2.substring(0, lastIndexOf), true, str);
                str2 = str2.substring(lastIndexOf + 1);
            } else {
                str3 = null;
            }
            if (DocumentImpl.isXMLIdentifier(str2)) {
                short nodeType = nodeImpl.getNodeType();
                if (nodeType == 1) {
                    ElementImpl elementImpl = (ElementImpl) nodeImpl;
                    elementImpl.namespaceAware = true;
                    elementImpl.namespaceURI = str;
                    elementImpl.prefix = str3;
                    elementImpl.localName = str2;
                } else if (nodeType != 2) {
                    throw new DOMException(9, "Cannot rename nodes of type " + nodeImpl.getNodeType());
                } else if (!XMLConstants.XMLNS_ATTRIBUTE.equals(str2) || XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(str)) {
                    AttrImpl attrImpl = (AttrImpl) nodeImpl;
                    attrImpl.namespaceAware = true;
                    attrImpl.namespaceURI = str;
                    attrImpl.prefix = str3;
                    attrImpl.localName = str2;
                } else {
                    throw new DOMException(14, str2);
                }
            } else {
                throw new DOMException(5, str2);
            }
        } else {
            throw new DOMException(14, str2);
        }
    }

    static void setName(NodeImpl nodeImpl, String str) {
        int lastIndexOf = str.lastIndexOf(":");
        if (lastIndexOf != -1) {
            String substring = str.substring(0, lastIndexOf);
            String substring2 = str.substring(lastIndexOf + 1);
            if (!DocumentImpl.isXMLIdentifier(substring) || !DocumentImpl.isXMLIdentifier(substring2)) {
                throw new DOMException(5, str);
            }
        } else if (!DocumentImpl.isXMLIdentifier(str)) {
            throw new DOMException(5, str);
        }
        short nodeType = nodeImpl.getNodeType();
        if (nodeType == 1) {
            ElementImpl elementImpl = (ElementImpl) nodeImpl;
            elementImpl.namespaceAware = false;
            elementImpl.localName = str;
        } else if (nodeType == 2) {
            AttrImpl attrImpl = (AttrImpl) nodeImpl;
            attrImpl.namespaceAware = false;
            attrImpl.localName = str;
        } else {
            throw new DOMException(9, "Cannot rename nodes of type " + nodeImpl.getNodeType());
        }
    }

    public final String getBaseURI() {
        switch (getNodeType()) {
            case 1:
                String attributeNS = ((Element) this).getAttributeNS("http://www.w3.org/XML/1998/namespace", "base");
                if (attributeNS != null) {
                    try {
                        if (!attributeNS.isEmpty()) {
                            if (new URI(attributeNS).isAbsolute()) {
                                return attributeNS;
                            }
                            String parentBaseUri = getParentBaseUri();
                            if (parentBaseUri == null) {
                                return null;
                            }
                            return new URI(parentBaseUri).resolve(attributeNS).toString();
                        }
                    } catch (URISyntaxException unused) {
                        return null;
                    }
                }
                return getParentBaseUri();
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 8:
            case 10:
            case 11:
            case 12:
                return null;
            case 7:
                return getParentBaseUri();
            case 9:
                return sanitizeUri(((Document) this).getDocumentURI());
            default:
                throw new DOMException(9, "Unsupported node type " + getNodeType());
        }
    }

    private String getParentBaseUri() {
        Node parentNode = getParentNode();
        if (parentNode != null) {
            return parentNode.getBaseURI();
        }
        return null;
    }

    private String sanitizeUri(String str) {
        if (str == null || str.length() == 0) {
            return null;
        }
        try {
            return new URI(str).toString();
        } catch (URISyntaxException unused) {
            return null;
        }
    }

    public short compareDocumentPosition(Node node) throws DOMException {
        throw new UnsupportedOperationException();
    }

    public String getTextContent() throws DOMException {
        return getNodeValue();
    }

    /* access modifiers changed from: package-private */
    public void getTextContent(StringBuilder sb) throws DOMException {
        String nodeValue = getNodeValue();
        if (nodeValue != null) {
            sb.append(nodeValue);
        }
    }

    public final void setTextContent(String str) throws DOMException {
        switch (getNodeType()) {
            case 1:
            case 5:
            case 6:
            case 11:
                break;
            case 2:
            case 3:
            case 4:
            case 7:
            case 8:
            case 12:
                setNodeValue(str);
                return;
            case 9:
            case 10:
                return;
            default:
                throw new DOMException(9, "Unsupported node type " + getNodeType());
        }
        while (true) {
            Node firstChild = getFirstChild();
            if (firstChild != null) {
                removeChild(firstChild);
            } else if (str != null && str.length() != 0) {
                appendChild(this.document.createTextNode(str));
                return;
            } else {
                return;
            }
        }
    }

    private NodeImpl getNamespacingElement() {
        switch (getNodeType()) {
            case 1:
                return this;
            case 2:
                return (NodeImpl) ((Attr) this).getOwnerElement();
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
                return getContainingElement();
            case 6:
            case 10:
            case 11:
            case 12:
                return null;
            case 9:
                return (NodeImpl) ((Document) this).getDocumentElement();
            default:
                throw new DOMException(9, "Unsupported node type " + getNodeType());
        }
    }

    private NodeImpl getContainingElement() {
        for (Node parentNode = getParentNode(); parentNode != null; parentNode = parentNode.getParentNode()) {
            if (parentNode.getNodeType() == 1) {
                return (NodeImpl) parentNode;
            }
        }
        return null;
    }

    public final String lookupPrefix(String str) {
        if (str == null) {
            return null;
        }
        NodeImpl namespacingElement = getNamespacingElement();
        for (NodeImpl nodeImpl = namespacingElement; nodeImpl != null; nodeImpl = nodeImpl.getContainingElement()) {
            if (str.equals(nodeImpl.getNamespaceURI()) && namespacingElement.isPrefixMappedToUri(nodeImpl.getPrefix(), str)) {
                return nodeImpl.getPrefix();
            }
            if (nodeImpl.hasAttributes()) {
                NamedNodeMap attributes = nodeImpl.getAttributes();
                int length = attributes.getLength();
                for (int i = 0; i < length; i++) {
                    Node item = attributes.item(i);
                    if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(item.getNamespaceURI()) && XMLConstants.XMLNS_ATTRIBUTE.equals(item.getPrefix()) && str.equals(item.getNodeValue()) && namespacingElement.isPrefixMappedToUri(item.getLocalName(), str)) {
                        return item.getLocalName();
                    }
                }
                continue;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public boolean isPrefixMappedToUri(String str, String str2) {
        if (str == null) {
            return false;
        }
        return str2.equals(lookupNamespaceURI(str));
    }

    public final boolean isDefaultNamespace(String str) {
        String lookupNamespaceURI = lookupNamespaceURI((String) null);
        if (str == null) {
            return lookupNamespaceURI == null;
        }
        return str.equals(lookupNamespaceURI);
    }

    public final String lookupNamespaceURI(String str) {
        for (NodeImpl namespacingElement = getNamespacingElement(); namespacingElement != null; namespacingElement = namespacingElement.getContainingElement()) {
            String prefix = namespacingElement.getPrefix();
            if (namespacingElement.getNamespaceURI() != null) {
                if (str == null) {
                    if (prefix == null) {
                    }
                } else if (str.equals(prefix)) {
                }
                return namespacingElement.getNamespaceURI();
            }
            if (namespacingElement.hasAttributes()) {
                NamedNodeMap attributes = namespacingElement.getAttributes();
                int length = attributes.getLength();
                for (int i = 0; i < length; i++) {
                    Node item = attributes.item(i);
                    if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(item.getNamespaceURI())) {
                        if (str == null) {
                            if (XMLConstants.XMLNS_ATTRIBUTE.equals(item.getNodeName())) {
                            }
                        } else if (XMLConstants.XMLNS_ATTRIBUTE.equals(item.getPrefix()) && str.equals(item.getLocalName())) {
                        }
                        String nodeValue = item.getNodeValue();
                        if (nodeValue.length() > 0) {
                            return nodeValue;
                        }
                        return null;
                    }
                }
                continue;
            }
        }
        return null;
    }

    private static List<Object> createEqualityKey(Node node) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Short.valueOf(node.getNodeType()));
        arrayList.add(node.getNodeName());
        arrayList.add(node.getLocalName());
        arrayList.add(node.getNamespaceURI());
        arrayList.add(node.getPrefix());
        arrayList.add(node.getNodeValue());
        for (Node firstChild = node.getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            arrayList.add(firstChild);
        }
        short nodeType = node.getNodeType();
        if (nodeType == 1) {
            arrayList.add(((Element) node).getAttributes());
        } else if (nodeType == 10) {
            DocumentTypeImpl documentTypeImpl = (DocumentTypeImpl) node;
            arrayList.add(documentTypeImpl.getPublicId());
            arrayList.add(documentTypeImpl.getSystemId());
            arrayList.add(documentTypeImpl.getInternalSubset());
            arrayList.add(documentTypeImpl.getEntities());
            arrayList.add(documentTypeImpl.getNotations());
        }
        return arrayList;
    }

    public final boolean isEqualNode(Node node) {
        if (node == this) {
            return true;
        }
        List<Object> createEqualityKey = createEqualityKey(this);
        List<Object> createEqualityKey2 = createEqualityKey(node);
        if (createEqualityKey.size() != createEqualityKey2.size()) {
            return false;
        }
        for (int i = 0; i < createEqualityKey.size(); i++) {
            Object obj = createEqualityKey.get(i);
            Object obj2 = createEqualityKey2.get(i);
            if (obj != obj2) {
                if (obj == null || obj2 == null) {
                    return false;
                }
                if ((obj instanceof String) || (obj instanceof Short)) {
                    if (!obj.equals(obj2)) {
                        return false;
                    }
                } else if (obj instanceof NamedNodeMap) {
                    if (!(obj2 instanceof NamedNodeMap) || !namedNodeMapsEqual((NamedNodeMap) obj, (NamedNodeMap) obj2)) {
                        return false;
                    }
                } else if (!(obj instanceof Node)) {
                    throw new AssertionError();
                } else if (!(obj2 instanceof Node) || !((Node) obj).isEqualNode((Node) obj2)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean namedNodeMapsEqual(NamedNodeMap namedNodeMap, NamedNodeMap namedNodeMap2) {
        Node node;
        if (namedNodeMap.getLength() != namedNodeMap2.getLength()) {
            return false;
        }
        for (int i = 0; i < namedNodeMap.getLength(); i++) {
            Node item = namedNodeMap.item(i);
            if (item.getLocalName() == null) {
                node = namedNodeMap2.getNamedItem(item.getNodeName());
            } else {
                node = namedNodeMap2.getNamedItemNS(item.getNamespaceURI(), item.getLocalName());
            }
            if (node == null || !item.isEqualNode(node)) {
                return false;
            }
        }
        return true;
    }

    public final Object getFeature(String str, String str2) {
        if (isSupported(str, str2)) {
            return this;
        }
        return null;
    }

    public final Object setUserData(String str, Object obj, UserDataHandler userDataHandler) {
        UserData userData;
        if (str != null) {
            Map<String, UserData> userDataMap = this.document.getUserDataMap(this);
            if (obj == null) {
                userData = userDataMap.remove(str);
            } else {
                userData = userDataMap.put(str, new UserData(obj, userDataHandler));
            }
            if (userData != null) {
                return userData.value;
            }
            return null;
        }
        throw new NullPointerException("key == null");
    }

    public final Object getUserData(String str) {
        if (str != null) {
            UserData userData = this.document.getUserDataMapForRead(this).get(str);
            if (userData != null) {
                return userData.value;
            }
            return null;
        }
        throw new NullPointerException("key == null");
    }

    static class UserData {
        final UserDataHandler handler;
        final Object value;

        UserData(Object obj, UserDataHandler userDataHandler) {
            this.value = obj;
            this.handler = userDataHandler;
        }
    }
}
