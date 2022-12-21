package org.apache.harmony.xml.dom;

import org.w3c.dom.Node;

public abstract class LeafNodeImpl extends NodeImpl {
    int index;
    InnerNodeImpl parent;

    /* access modifiers changed from: package-private */
    public boolean isParentOf(Node node) {
        return false;
    }

    LeafNodeImpl(DocumentImpl documentImpl) {
        super(documentImpl);
    }

    public Node getNextSibling() {
        InnerNodeImpl innerNodeImpl = this.parent;
        if (innerNodeImpl == null || this.index + 1 >= innerNodeImpl.children.size()) {
            return null;
        }
        return this.parent.children.get(this.index + 1);
    }

    public Node getParentNode() {
        return this.parent;
    }

    public Node getPreviousSibling() {
        InnerNodeImpl innerNodeImpl = this.parent;
        if (innerNodeImpl == null || this.index == 0) {
            return null;
        }
        return innerNodeImpl.children.get(this.index - 1);
    }
}
