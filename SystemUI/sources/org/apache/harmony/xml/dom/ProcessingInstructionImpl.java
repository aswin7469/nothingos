package org.apache.harmony.xml.dom;

import org.w3c.dom.DOMException;
import org.w3c.dom.ProcessingInstruction;

public final class ProcessingInstructionImpl extends LeafNodeImpl implements ProcessingInstruction {
    private String data;
    private String target;

    public short getNodeType() {
        return 7;
    }

    ProcessingInstructionImpl(DocumentImpl documentImpl, String str, String str2) {
        super(documentImpl);
        this.target = str;
        this.data = str2;
    }

    public String getData() {
        return this.data;
    }

    public String getNodeName() {
        return this.target;
    }

    public String getNodeValue() {
        return this.data;
    }

    public String getTarget() {
        return this.target;
    }

    public void setData(String str) throws DOMException {
        this.data = str;
    }
}
