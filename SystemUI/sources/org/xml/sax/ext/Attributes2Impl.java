package org.xml.sax.ext;

import libcore.util.EmptyArray;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;

public class Attributes2Impl extends AttributesImpl implements Attributes2 {
    private boolean[] declared;
    private boolean[] specified;

    public Attributes2Impl() {
        this.declared = EmptyArray.BOOLEAN;
        this.specified = EmptyArray.BOOLEAN;
    }

    public Attributes2Impl(Attributes attributes) {
        super(attributes);
    }

    public boolean isDeclared(int i) {
        if (i >= 0 && i < getLength()) {
            return this.declared[i];
        }
        throw new ArrayIndexOutOfBoundsException("No attribute at index: " + i);
    }

    public boolean isDeclared(String str, String str2) {
        int index = getIndex(str, str2);
        if (index >= 0) {
            return this.declared[index];
        }
        throw new IllegalArgumentException("No such attribute: local=" + str2 + ", namespace=" + str);
    }

    public boolean isDeclared(String str) {
        int index = getIndex(str);
        if (index >= 0) {
            return this.declared[index];
        }
        throw new IllegalArgumentException("No such attribute: " + str);
    }

    public boolean isSpecified(int i) {
        if (i >= 0 && i < getLength()) {
            return this.specified[i];
        }
        throw new ArrayIndexOutOfBoundsException("No attribute at index: " + i);
    }

    public boolean isSpecified(String str, String str2) {
        int index = getIndex(str, str2);
        if (index >= 0) {
            return this.specified[index];
        }
        throw new IllegalArgumentException("No such attribute: local=" + str2 + ", namespace=" + str);
    }

    public boolean isSpecified(String str) {
        int index = getIndex(str);
        if (index >= 0) {
            return this.specified[index];
        }
        throw new IllegalArgumentException("No such attribute: " + str);
    }

    public void setAttributes(Attributes attributes) {
        int length = attributes.getLength();
        super.setAttributes(attributes);
        this.declared = new boolean[length];
        this.specified = new boolean[length];
        int i = 0;
        if (attributes instanceof Attributes2) {
            Attributes2 attributes2 = (Attributes2) attributes;
            while (i < length) {
                this.declared[i] = attributes2.isDeclared(i);
                this.specified[i] = attributes2.isSpecified(i);
                i++;
            }
            return;
        }
        while (i < length) {
            this.declared[i] = !"CDATA".equals(attributes.getType(i));
            this.specified[i] = true;
            i++;
        }
    }

    public void addAttribute(String str, String str2, String str3, String str4, String str5) {
        super.addAttribute(str, str2, str3, str4, str5);
        int length = getLength();
        if (length > this.specified.length) {
            boolean[] zArr = new boolean[length];
            boolean[] zArr2 = this.declared;
            System.arraycopy((Object) zArr2, 0, (Object) zArr, 0, zArr2.length);
            this.declared = zArr;
            boolean[] zArr3 = new boolean[length];
            boolean[] zArr4 = this.specified;
            System.arraycopy((Object) zArr4, 0, (Object) zArr3, 0, zArr4.length);
            this.specified = zArr3;
        }
        int i = length - 1;
        this.specified[i] = true;
        this.declared[i] = !"CDATA".equals(str4);
    }

    public void removeAttribute(int i) {
        int length = getLength() - 1;
        super.removeAttribute(i);
        if (i != length) {
            boolean[] zArr = this.declared;
            int i2 = i + 1;
            int i3 = length - i;
            System.arraycopy((Object) zArr, i2, (Object) zArr, i, i3);
            boolean[] zArr2 = this.specified;
            System.arraycopy((Object) zArr2, i2, (Object) zArr2, i, i3);
        }
    }

    public void setDeclared(int i, boolean z) {
        if (i < 0 || i >= getLength()) {
            throw new ArrayIndexOutOfBoundsException("No attribute at index: " + i);
        }
        this.declared[i] = z;
    }

    public void setSpecified(int i, boolean z) {
        if (i < 0 || i >= getLength()) {
            throw new ArrayIndexOutOfBoundsException("No attribute at index: " + i);
        }
        this.specified[i] = z;
    }
}
