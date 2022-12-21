package org.apache.harmony.xml.dom;

import org.w3c.dom.CharacterData;
import org.w3c.dom.DOMException;

public abstract class CharacterDataImpl extends LeafNodeImpl implements CharacterData {
    protected StringBuffer buffer;

    CharacterDataImpl(DocumentImpl documentImpl, String str) {
        super(documentImpl);
        setData(str);
    }

    public void appendData(String str) throws DOMException {
        this.buffer.append(str);
    }

    public void deleteData(int i, int i2) throws DOMException {
        this.buffer.delete(i, i2 + i);
    }

    public String getData() throws DOMException {
        return this.buffer.toString();
    }

    public void appendDataTo(StringBuilder sb) {
        sb.append(this.buffer);
    }

    public int getLength() {
        return this.buffer.length();
    }

    public String getNodeValue() {
        return getData();
    }

    public void insertData(int i, String str) throws DOMException {
        try {
            this.buffer.insert(i, str);
        } catch (ArrayIndexOutOfBoundsException unused) {
            throw new DOMException(1, (String) null);
        }
    }

    public void replaceData(int i, int i2, String str) throws DOMException {
        try {
            this.buffer.replace(i, i2 + i, str);
        } catch (ArrayIndexOutOfBoundsException unused) {
            throw new DOMException(1, (String) null);
        }
    }

    public void setData(String str) throws DOMException {
        this.buffer = new StringBuffer(str);
    }

    public String substringData(int i, int i2) throws DOMException {
        try {
            return this.buffer.substring(i, i2 + i);
        } catch (ArrayIndexOutOfBoundsException unused) {
            throw new DOMException(1, (String) null);
        }
    }
}
