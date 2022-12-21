package sun.util;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

public class ResourceBundleEnumeration implements Enumeration<String> {
    Enumeration<String> enumeration;
    Iterator<String> iterator;
    String next = null;
    Set<String> set;

    public ResourceBundleEnumeration(Set<String> set2, Enumeration<String> enumeration2) {
        this.set = set2;
        this.iterator = set2.iterator();
        this.enumeration = enumeration2;
    }

    public boolean hasMoreElements() {
        if (this.next == null) {
            if (this.iterator.hasNext()) {
                this.next = this.iterator.next();
            } else if (this.enumeration != null) {
                while (this.next == null && this.enumeration.hasMoreElements()) {
                    String nextElement = this.enumeration.nextElement();
                    this.next = nextElement;
                    if (this.set.contains(nextElement)) {
                        this.next = null;
                    }
                }
            }
        }
        return this.next != null;
    }

    public String nextElement() {
        if (hasMoreElements()) {
            String str = this.next;
            this.next = null;
            return str;
        }
        throw new NoSuchElementException();
    }
}
