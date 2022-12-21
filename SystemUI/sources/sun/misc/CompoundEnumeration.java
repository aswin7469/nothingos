package sun.misc;

import java.util.Enumeration;
import java.util.NoSuchElementException;

public class CompoundEnumeration<E> implements Enumeration<E> {
    private Enumeration<E>[] enums;
    private int index = 0;

    public CompoundEnumeration(Enumeration<E>[] enumerationArr) {
        this.enums = enumerationArr;
    }

    private boolean next() {
        while (true) {
            int i = this.index;
            Enumeration<E>[] enumerationArr = this.enums;
            if (i >= enumerationArr.length) {
                return false;
            }
            Enumeration<E> enumeration = enumerationArr[i];
            if (enumeration != null && enumeration.hasMoreElements()) {
                return true;
            }
            this.index++;
        }
    }

    public boolean hasMoreElements() {
        return next();
    }

    public E nextElement() {
        if (next()) {
            return this.enums[this.index].nextElement();
        }
        throw new NoSuchElementException();
    }
}
