package org.apache.harmony.xml.dom;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Node;

public final class CDATASectionImpl extends TextImpl implements CDATASection {
    public String getNodeName() {
        return "#cdata-section";
    }

    public short getNodeType() {
        return 4;
    }

    public CDATASectionImpl(DocumentImpl documentImpl, String str) {
        super(documentImpl, str);
    }

    public void split() {
        if (needsSplitting()) {
            Node parentNode = getParentNode();
            String[] split = getData().split("\\]\\]>");
            DocumentImpl documentImpl = this.document;
            parentNode.insertBefore(new CDATASectionImpl(documentImpl, split[0] + "]]"), this);
            for (int i = 1; i < split.length - 1; i++) {
                DocumentImpl documentImpl2 = this.document;
                parentNode.insertBefore(new CDATASectionImpl(documentImpl2, ">" + split[i] + "]]"), this);
            }
            setData(">" + split[split.length - 1]);
        }
    }

    public boolean needsSplitting() {
        return this.buffer.indexOf("]]>") != -1;
    }

    public TextImpl replaceWithText() {
        TextImpl textImpl = new TextImpl(this.document, getData());
        this.parent.insertBefore(textImpl, this);
        this.parent.removeChild(this);
        return textImpl;
    }
}
