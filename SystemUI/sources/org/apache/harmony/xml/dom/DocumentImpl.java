package org.apache.harmony.xml.dom;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.apache.harmony.xml.dom.NodeImpl;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

public final class DocumentImpl extends InnerNodeImpl implements Document {
    private String documentUri;
    private DOMConfigurationImpl domConfiguration;
    private DOMImplementation domImplementation;
    private String inputEncoding;
    private WeakHashMap<NodeImpl, Map<String, NodeImpl.UserData>> nodeToUserData;
    private boolean strictErrorChecking = true;
    private boolean xmlStandalone = false;
    private String xmlVersion = "1.0";

    private static boolean isXMLIdentifierStart(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || c == '_';
    }

    public String getNodeName() {
        return "#document";
    }

    public short getNodeType() {
        return 9;
    }

    public String getTextContent() {
        return null;
    }

    public String getXmlEncoding() {
        return null;
    }

    public DocumentImpl(DOMImplementationImpl dOMImplementationImpl, String str, String str2, DocumentType documentType, String str3) {
        super((DocumentImpl) null);
        this.document = this;
        this.domImplementation = dOMImplementationImpl;
        this.inputEncoding = str3;
        if (documentType != null) {
            appendChild(documentType);
        }
        if (str2 != null) {
            appendChild(createElementNS(str, str2));
        }
    }

    private static boolean isXMLIdentifierPart(char c) {
        return isXMLIdentifierStart(c) || (c >= '0' && c <= '9') || c == '-' || c == '.';
    }

    static boolean isXMLIdentifier(String str) {
        if (str.length() == 0 || !isXMLIdentifierStart(str.charAt(0))) {
            return false;
        }
        for (int i = 1; i < str.length(); i++) {
            if (!isXMLIdentifierPart(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private NodeImpl shallowCopy(short s, Node node) {
        ElementImpl elementImpl;
        AttrImpl attrImpl;
        switch (node.getNodeType()) {
            case 1:
                ElementImpl elementImpl2 = (ElementImpl) node;
                if (elementImpl2.namespaceAware) {
                    elementImpl = createElementNS(elementImpl2.getNamespaceURI(), elementImpl2.getLocalName());
                    elementImpl.setPrefix(elementImpl2.getPrefix());
                } else {
                    elementImpl = createElement(elementImpl2.getTagName());
                }
                NamedNodeMap attributes = elementImpl2.getAttributes();
                for (int i = 0; i < attributes.getLength(); i++) {
                    AttrImpl attrImpl2 = (AttrImpl) attributes.item(i);
                    AttrImpl attrImpl3 = (AttrImpl) shallowCopy(s, attrImpl2);
                    notifyUserDataHandlers(s, attrImpl2, attrImpl3);
                    if (attrImpl2.namespaceAware) {
                        elementImpl.setAttributeNodeNS(attrImpl3);
                    } else {
                        elementImpl.setAttributeNode(attrImpl3);
                    }
                }
                return elementImpl;
            case 2:
                AttrImpl attrImpl4 = (AttrImpl) node;
                if (attrImpl4.namespaceAware) {
                    attrImpl = createAttributeNS(attrImpl4.getNamespaceURI(), attrImpl4.getLocalName());
                    attrImpl.setPrefix(attrImpl4.getPrefix());
                } else {
                    attrImpl = createAttribute(attrImpl4.getName());
                }
                attrImpl.setNodeValue(attrImpl4.getValue());
                return attrImpl;
            case 3:
                return createTextNode(((Text) node).getData());
            case 4:
                return createCDATASection(((CharacterData) node).getData());
            case 5:
                return createEntityReference(node.getNodeName());
            case 6:
            case 12:
                throw new UnsupportedOperationException();
            case 7:
                ProcessingInstruction processingInstruction = (ProcessingInstruction) node;
                return createProcessingInstruction(processingInstruction.getTarget(), processingInstruction.getData());
            case 8:
                return createComment(((Comment) node).getData());
            case 9:
            case 10:
                throw new DOMException(9, "Cannot copy node of type " + node.getNodeType());
            case 11:
                return createDocumentFragment();
            default:
                throw new DOMException(9, "Unsupported node type " + node.getNodeType());
        }
    }

    /* access modifiers changed from: package-private */
    public Node cloneOrImportNode(short s, Node node, boolean z) {
        NodeImpl shallowCopy = shallowCopy(s, node);
        if (z) {
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                shallowCopy.appendChild(cloneOrImportNode(s, childNodes.item(i), z));
            }
        }
        notifyUserDataHandlers(s, node, shallowCopy);
        return shallowCopy;
    }

    public Node importNode(Node node, boolean z) {
        return cloneOrImportNode(2, node, z);
    }

    public Node adoptNode(Node node) {
        if (!(node instanceof NodeImpl)) {
            return null;
        }
        NodeImpl nodeImpl = (NodeImpl) node;
        switch (nodeImpl.getNodeType()) {
            case 1:
            case 3:
            case 4:
            case 5:
            case 7:
            case 8:
            case 11:
                break;
            case 2:
                AttrImpl attrImpl = (AttrImpl) node;
                if (attrImpl.ownerElement != null) {
                    attrImpl.ownerElement.removeAttributeNode(attrImpl);
                    break;
                }
                break;
            case 6:
            case 9:
            case 10:
            case 12:
                throw new DOMException(9, "Cannot adopt nodes of type " + nodeImpl.getNodeType());
            default:
                throw new DOMException(9, "Unsupported node type " + node.getNodeType());
        }
        Node parentNode = nodeImpl.getParentNode();
        if (parentNode != null) {
            parentNode.removeChild(nodeImpl);
        }
        changeDocumentToThis(nodeImpl);
        notifyUserDataHandlers(5, node, (NodeImpl) null);
        return nodeImpl;
    }

    private void changeDocumentToThis(NodeImpl nodeImpl) {
        Map<String, NodeImpl.UserData> userDataMapForRead = nodeImpl.document.getUserDataMapForRead(nodeImpl);
        if (!userDataMapForRead.isEmpty()) {
            getUserDataMap(nodeImpl).putAll(userDataMapForRead);
        }
        nodeImpl.document = this;
        NodeList childNodes = nodeImpl.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            changeDocumentToThis((NodeImpl) childNodes.item(i));
        }
        if (nodeImpl.getNodeType() == 1) {
            NamedNodeMap attributes = nodeImpl.getAttributes();
            for (int i2 = 0; i2 < attributes.getLength(); i2++) {
                changeDocumentToThis((AttrImpl) attributes.item(i2));
            }
        }
    }

    public Node renameNode(Node node, String str, String str2) {
        if (node.getOwnerDocument() == this) {
            setNameNS((NodeImpl) node, str, str2);
            notifyUserDataHandlers(4, node, (NodeImpl) null);
            return node;
        }
        throw new DOMException(4, (String) null);
    }

    public AttrImpl createAttribute(String str) {
        return new AttrImpl(this, str);
    }

    public AttrImpl createAttributeNS(String str, String str2) {
        return new AttrImpl(this, str, str2);
    }

    public CDATASectionImpl createCDATASection(String str) {
        return new CDATASectionImpl(this, str);
    }

    public CommentImpl createComment(String str) {
        return new CommentImpl(this, str);
    }

    public DocumentFragmentImpl createDocumentFragment() {
        return new DocumentFragmentImpl(this);
    }

    public ElementImpl createElement(String str) {
        return new ElementImpl(this, str);
    }

    public ElementImpl createElementNS(String str, String str2) {
        return new ElementImpl(this, str, str2);
    }

    public EntityReferenceImpl createEntityReference(String str) {
        return new EntityReferenceImpl(this, str);
    }

    public ProcessingInstructionImpl createProcessingInstruction(String str, String str2) {
        return new ProcessingInstructionImpl(this, str, str2);
    }

    public TextImpl createTextNode(String str) {
        return new TextImpl(this, str);
    }

    public DocumentType getDoctype() {
        for (LeafNodeImpl leafNodeImpl : this.children) {
            if (leafNodeImpl instanceof DocumentType) {
                return (DocumentType) leafNodeImpl;
            }
        }
        return null;
    }

    public Element getDocumentElement() {
        for (LeafNodeImpl leafNodeImpl : this.children) {
            if (leafNodeImpl instanceof Element) {
                return (Element) leafNodeImpl;
            }
        }
        return null;
    }

    public Element getElementById(String str) {
        ElementImpl elementImpl = (ElementImpl) getDocumentElement();
        if (elementImpl == null) {
            return null;
        }
        return elementImpl.getElementById(str);
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

    public DOMImplementation getImplementation() {
        return this.domImplementation;
    }

    public Node insertChildAt(Node node, int i) {
        if ((node instanceof Element) && getDocumentElement() != null) {
            throw new DOMException(3, "Only one root element allowed");
        } else if (!(node instanceof DocumentType) || getDoctype() == null) {
            return super.insertChildAt(node, i);
        } else {
            throw new DOMException(3, "Only one DOCTYPE element allowed");
        }
    }

    public String getInputEncoding() {
        return this.inputEncoding;
    }

    public boolean getXmlStandalone() {
        return this.xmlStandalone;
    }

    public void setXmlStandalone(boolean z) {
        this.xmlStandalone = z;
    }

    public String getXmlVersion() {
        return this.xmlVersion;
    }

    public void setXmlVersion(String str) {
        this.xmlVersion = str;
    }

    public boolean getStrictErrorChecking() {
        return this.strictErrorChecking;
    }

    public void setStrictErrorChecking(boolean z) {
        this.strictErrorChecking = z;
    }

    public String getDocumentURI() {
        return this.documentUri;
    }

    public void setDocumentURI(String str) {
        this.documentUri = str;
    }

    public DOMConfiguration getDomConfig() {
        if (this.domConfiguration == null) {
            this.domConfiguration = new DOMConfigurationImpl();
        }
        return this.domConfiguration;
    }

    public void normalizeDocument() {
        Element documentElement = getDocumentElement();
        if (documentElement != null) {
            ((DOMConfigurationImpl) getDomConfig()).normalize(documentElement);
        }
    }

    /* access modifiers changed from: package-private */
    public Map<String, NodeImpl.UserData> getUserDataMap(NodeImpl nodeImpl) {
        if (this.nodeToUserData == null) {
            this.nodeToUserData = new WeakHashMap<>();
        }
        Map<String, NodeImpl.UserData> map = this.nodeToUserData.get(nodeImpl);
        if (map != null) {
            return map;
        }
        HashMap hashMap = new HashMap();
        this.nodeToUserData.put(nodeImpl, hashMap);
        return hashMap;
    }

    /* access modifiers changed from: package-private */
    public Map<String, NodeImpl.UserData> getUserDataMapForRead(NodeImpl nodeImpl) {
        WeakHashMap<NodeImpl, Map<String, NodeImpl.UserData>> weakHashMap = this.nodeToUserData;
        if (weakHashMap == null) {
            return Collections.emptyMap();
        }
        Map<String, NodeImpl.UserData> map = weakHashMap.get(nodeImpl);
        return map == null ? Collections.emptyMap() : map;
    }

    private static void notifyUserDataHandlers(short s, Node node, NodeImpl nodeImpl) {
        if (node instanceof NodeImpl) {
            NodeImpl nodeImpl2 = (NodeImpl) node;
            if (nodeImpl2.document != null) {
                for (Map.Entry next : nodeImpl2.document.getUserDataMapForRead(nodeImpl2).entrySet()) {
                    NodeImpl.UserData userData = (NodeImpl.UserData) next.getValue();
                    if (userData.handler != null) {
                        userData.handler.handle(s, (String) next.getKey(), userData.value, node, nodeImpl);
                    }
                }
            }
        }
    }
}
