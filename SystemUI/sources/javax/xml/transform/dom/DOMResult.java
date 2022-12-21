package javax.xml.transform.dom;

import javax.xml.transform.Result;
import org.w3c.dom.Node;

public class DOMResult implements Result {
    public static final String FEATURE = "http://javax.xml.transform.dom.DOMResult/feature";
    private Node nextSibling = null;
    private Node node = null;
    private String systemId = null;

    public DOMResult() {
        setNode((Node) null);
        setNextSibling((Node) null);
        setSystemId((String) null);
    }

    public DOMResult(Node node2) {
        setNode(node2);
        setNextSibling((Node) null);
        setSystemId((String) null);
    }

    public DOMResult(Node node2, String str) {
        setNode(node2);
        setNextSibling((Node) null);
        setSystemId(str);
    }

    public DOMResult(Node node2, Node node3) {
        if (node3 != null) {
            if (node2 == null) {
                throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
            } else if ((node2.compareDocumentPosition(node3) & 16) == 0) {
                throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
            }
        }
        setNode(node2);
        setNextSibling(node3);
        setSystemId((String) null);
    }

    public DOMResult(Node node2, Node node3, String str) {
        if (node3 != null) {
            if (node2 == null) {
                throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
            } else if ((node2.compareDocumentPosition(node3) & 16) == 0) {
                throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
            }
        }
        setNode(node2);
        setNextSibling(node3);
        setSystemId(str);
    }

    public void setNode(Node node2) {
        Node node3 = this.nextSibling;
        if (node3 != null) {
            if (node2 == null) {
                throw new IllegalStateException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
            } else if ((node2.compareDocumentPosition(node3) & 16) == 0) {
                throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
            }
        }
        this.node = node2;
    }

    public Node getNode() {
        return this.node;
    }

    public void setNextSibling(Node node2) {
        if (node2 != null) {
            Node node3 = this.node;
            if (node3 == null) {
                throw new IllegalStateException("Cannot create a DOMResult when the nextSibling is contained by the \"null\" node.");
            } else if ((node3.compareDocumentPosition(node2) & 16) == 0) {
                throw new IllegalArgumentException("Cannot create a DOMResult when the nextSibling is not contained by the node.");
            }
        }
        this.nextSibling = node2;
    }

    public Node getNextSibling() {
        return this.nextSibling;
    }

    public void setSystemId(String str) {
        this.systemId = str;
    }

    public String getSystemId() {
        return this.systemId;
    }
}
