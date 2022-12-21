package org.apache.harmony.xml.dom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class InnerNodeImpl extends LeafNodeImpl {
    List<LeafNodeImpl> children = new ArrayList();

    protected InnerNodeImpl(DocumentImpl documentImpl) {
        super(documentImpl);
    }

    public Node appendChild(Node node) throws DOMException {
        return insertChildAt(node, this.children.size());
    }

    public NodeList getChildNodes() {
        NodeListImpl nodeListImpl = new NodeListImpl();
        for (LeafNodeImpl add : this.children) {
            nodeListImpl.add(add);
        }
        return nodeListImpl;
    }

    public Node getFirstChild() {
        if (!this.children.isEmpty()) {
            return this.children.get(0);
        }
        return null;
    }

    public Node getLastChild() {
        if (this.children.isEmpty()) {
            return null;
        }
        List<LeafNodeImpl> list = this.children;
        return list.get(list.size() - 1);
    }

    public Node getNextSibling() {
        if (this.parent == null || this.index + 1 >= this.parent.children.size()) {
            return null;
        }
        return this.parent.children.get(this.index + 1);
    }

    public boolean hasChildNodes() {
        return this.children.size() != 0;
    }

    public Node insertBefore(Node node, Node node2) throws DOMException {
        LeafNodeImpl leafNodeImpl = (LeafNodeImpl) node2;
        if (leafNodeImpl == null) {
            return appendChild(node);
        }
        if (leafNodeImpl.document != this.document) {
            throw new DOMException(4, (String) null);
        } else if (leafNodeImpl.parent == this) {
            return insertChildAt(node, leafNodeImpl.index);
        } else {
            throw new DOMException(3, (String) null);
        }
    }

    /* access modifiers changed from: package-private */
    public Node insertChildAt(Node node, int i) throws DOMException {
        if (node instanceof DocumentFragment) {
            NodeList childNodes = node.getChildNodes();
            for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
                insertChildAt(childNodes.item(i2), i + i2);
            }
            return node;
        }
        LeafNodeImpl leafNodeImpl = (LeafNodeImpl) node;
        if (leafNodeImpl.document != null && this.document != null && leafNodeImpl.document != this.document) {
            throw new DOMException(4, (String) null);
        } else if (!leafNodeImpl.isParentOf(this)) {
            if (leafNodeImpl.parent != null) {
                int i3 = leafNodeImpl.index;
                leafNodeImpl.parent.children.remove(i3);
                leafNodeImpl.parent.refreshIndices(i3);
            }
            this.children.add(i, leafNodeImpl);
            leafNodeImpl.parent = this;
            refreshIndices(i);
            return node;
        } else {
            throw new DOMException(3, (String) null);
        }
    }

    public boolean isParentOf(Node node) {
        for (LeafNodeImpl leafNodeImpl = (LeafNodeImpl) node; leafNodeImpl != null; leafNodeImpl = leafNodeImpl.parent) {
            if (leafNodeImpl == this) {
                return true;
            }
        }
        return false;
    }

    public final void normalize() {
        Node firstChild = getFirstChild();
        while (firstChild != null) {
            Node nextSibling = firstChild.getNextSibling();
            firstChild.normalize();
            if (firstChild.getNodeType() == 3) {
                ((TextImpl) firstChild).minimize();
            }
            firstChild = nextSibling;
        }
    }

    private void refreshIndices(int i) {
        while (i < this.children.size()) {
            this.children.get(i).index = i;
            i++;
        }
    }

    public Node removeChild(Node node) throws DOMException {
        LeafNodeImpl leafNodeImpl = (LeafNodeImpl) node;
        if (leafNodeImpl.document != this.document) {
            throw new DOMException(4, (String) null);
        } else if (leafNodeImpl.parent == this) {
            int i = leafNodeImpl.index;
            this.children.remove(i);
            leafNodeImpl.parent = null;
            refreshIndices(i);
            return node;
        } else {
            throw new DOMException(3, (String) null);
        }
    }

    public Node replaceChild(Node node, Node node2) throws DOMException {
        int i = ((LeafNodeImpl) node2).index;
        removeChild(node2);
        insertChildAt(node, i);
        return node2;
    }

    public String getTextContent() throws DOMException {
        Node firstChild = getFirstChild();
        if (firstChild == null) {
            return "";
        }
        if (firstChild.getNextSibling() != null) {
            StringBuilder sb = new StringBuilder();
            getTextContent(sb);
            return sb.toString();
        } else if (hasTextContent(firstChild)) {
            return firstChild.getTextContent();
        } else {
            return "";
        }
    }

    /* access modifiers changed from: package-private */
    public void getTextContent(StringBuilder sb) throws DOMException {
        for (Node firstChild = getFirstChild(); firstChild != null; firstChild = firstChild.getNextSibling()) {
            if (hasTextContent(firstChild)) {
                ((NodeImpl) firstChild).getTextContent(sb);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean hasTextContent(Node node) {
        return (node.getNodeType() == 8 || node.getNodeType() == 7) ? false : true;
    }

    /* access modifiers changed from: package-private */
    public void getElementsByTagName(NodeListImpl nodeListImpl, String str) {
        for (NodeImpl next : this.children) {
            if (next.getNodeType() == 1) {
                ElementImpl elementImpl = (ElementImpl) next;
                if (matchesNameOrWildcard(str, elementImpl.getNodeName())) {
                    nodeListImpl.add(elementImpl);
                }
                elementImpl.getElementsByTagName(nodeListImpl, str);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void getElementsByTagNameNS(NodeListImpl nodeListImpl, String str, String str2) {
        for (NodeImpl next : this.children) {
            if (next.getNodeType() == 1) {
                ElementImpl elementImpl = (ElementImpl) next;
                if (matchesNameOrWildcard(str, elementImpl.getNamespaceURI()) && matchesNameOrWildcard(str2, elementImpl.getLocalName())) {
                    nodeListImpl.add(elementImpl);
                }
                elementImpl.getElementsByTagNameNS(nodeListImpl, str, str2);
            }
        }
    }

    private static boolean matchesNameOrWildcard(String str, String str2) {
        return "*".equals(str) || Objects.equals(str, str2);
    }
}
