package org.apache.harmony.xml.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class TextImpl extends CharacterDataImpl implements Text {
    public String getNodeName() {
        return "#text";
    }

    public short getNodeType() {
        return 3;
    }

    public final boolean isElementContentWhitespace() {
        return false;
    }

    public TextImpl(DocumentImpl documentImpl, String str) {
        super(documentImpl, str);
    }

    public final Text splitText(int i) throws DOMException {
        TextImpl createTextNode = this.document.createTextNode(substringData(i, getLength() - i));
        deleteData(0, i);
        Node nextSibling = getNextSibling();
        if (nextSibling == null) {
            getParentNode().appendChild(createTextNode);
        } else {
            getParentNode().insertBefore(createTextNode, nextSibling);
        }
        return this;
    }

    public final String getWholeText() {
        StringBuilder sb = new StringBuilder();
        for (TextImpl firstTextNodeInCurrentRun = firstTextNodeInCurrentRun(); firstTextNodeInCurrentRun != null; firstTextNodeInCurrentRun = firstTextNodeInCurrentRun.nextTextNode()) {
            firstTextNodeInCurrentRun.appendDataTo(sb);
        }
        return sb.toString();
    }

    public final Text replaceWholeText(String str) throws DOMException {
        Node parentNode = getParentNode();
        TextImpl firstTextNodeInCurrentRun = firstTextNodeInCurrentRun();
        TextImpl textImpl = null;
        while (firstTextNodeInCurrentRun != null) {
            if (firstTextNodeInCurrentRun != this || str == null || str.length() <= 0) {
                TextImpl nextTextNode = firstTextNodeInCurrentRun.nextTextNode();
                parentNode.removeChild(firstTextNodeInCurrentRun);
                firstTextNodeInCurrentRun = nextTextNode;
            } else {
                setData(str);
                firstTextNodeInCurrentRun = firstTextNodeInCurrentRun.nextTextNode();
                textImpl = this;
            }
        }
        return textImpl;
    }

    private TextImpl firstTextNodeInCurrentRun() {
        for (Node previousSibling = getPreviousSibling(); previousSibling != null; previousSibling = previousSibling.getPreviousSibling()) {
            short nodeType = previousSibling.getNodeType();
            if (nodeType != 3 && nodeType != 4) {
                break;
            }
            this = (TextImpl) previousSibling;
        }
        return this;
    }

    private TextImpl nextTextNode() {
        Node nextSibling = getNextSibling();
        if (nextSibling == null) {
            return null;
        }
        short nodeType = nextSibling.getNodeType();
        if (nodeType == 3 || nodeType == 4) {
            return (TextImpl) nextSibling;
        }
        return null;
    }

    public final TextImpl minimize() {
        if (getLength() == 0) {
            this.parent.removeChild(this);
            return null;
        }
        Node previousSibling = getPreviousSibling();
        if (previousSibling == null || previousSibling.getNodeType() != 3) {
            return this;
        }
        TextImpl textImpl = (TextImpl) previousSibling;
        textImpl.buffer.append(this.buffer);
        this.parent.removeChild(this);
        return textImpl;
    }
}
