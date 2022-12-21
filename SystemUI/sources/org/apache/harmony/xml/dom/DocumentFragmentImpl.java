package org.apache.harmony.xml.dom;

import org.w3c.dom.DocumentFragment;

public class DocumentFragmentImpl extends InnerNodeImpl implements DocumentFragment {
    public String getNodeName() {
        return "#document-fragment";
    }

    public short getNodeType() {
        return 11;
    }

    DocumentFragmentImpl(DocumentImpl documentImpl) {
        super(documentImpl);
    }
}
