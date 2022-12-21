package java.util;

import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamException;
import java.p026io.Serializable;
import java.util.ImmutableCollections;

/* compiled from: ImmutableCollections */
final class CollSer implements Serializable {
    static final int IMM_LIST = 1;
    static final int IMM_MAP = 3;
    static final int IMM_SET = 2;
    private static final long serialVersionUID = 6309168927139932177L;
    private transient Object[] array;
    private final int tag;

    CollSer(int i, Object... objArr) {
        this.tag = i;
        this.array = objArr;
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        if (readInt >= 0) {
            Object[] objArr = new Object[readInt];
            for (int i = 0; i < readInt; i++) {
                objArr[i] = objectInputStream.readObject();
            }
            this.array = objArr;
            return;
        }
        throw new InvalidObjectException("negative length " + readInt);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.array.length);
        int i = 0;
        while (true) {
            Object[] objArr = this.array;
            if (i < objArr.length) {
                objectOutputStream.writeObject(objArr[i]);
                i++;
            } else {
                return;
            }
        }
    }

    private Object readResolve() throws ObjectStreamException {
        try {
            Object[] objArr = this.array;
            if (objArr != null) {
                int i = this.tag;
                int i2 = i & 255;
                if (i2 == 1) {
                    return List.m1733of((E[]) objArr);
                }
                if (i2 == 2) {
                    return Set.m1761of((E[]) objArr);
                }
                if (i2 != 3) {
                    throw new InvalidObjectException(String.format("invalid flags 0x%x", Integer.valueOf(i)));
                } else if (objArr.length == 0) {
                    return ImmutableCollections.Map0.instance();
                } else {
                    if (objArr.length == 2) {
                        return new ImmutableCollections.Map1(objArr[0], objArr[1]);
                    }
                    return new ImmutableCollections.MapN(this.array);
                }
            } else {
                throw new InvalidObjectException("null array");
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            InvalidObjectException invalidObjectException = new InvalidObjectException("invalid object");
            invalidObjectException.initCause(e);
            throw invalidObjectException;
        }
    }
}
