package java.util;

import java.p026io.IOException;
import java.p026io.InvalidObjectException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.util.HashMap;

public class HashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, Serializable {
    private static final Object PRESENT = new Object();
    static final long serialVersionUID = -5024744406713321676L;
    private transient HashMap<E, Object> map;

    public HashSet() {
        this.map = new HashMap<>();
    }

    public HashSet(Collection<? extends E> collection) {
        this.map = new HashMap<>(Math.max(((int) (((float) collection.size()) / 0.75f)) + 1, 16));
        addAll(collection);
    }

    public HashSet(int i, float f) {
        this.map = new HashMap<>(i, f);
    }

    public HashSet(int i) {
        this.map = new HashMap<>(i);
    }

    HashSet(int i, float f, boolean z) {
        this.map = new LinkedHashMap(i, f);
    }

    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public boolean contains(Object obj) {
        return this.map.containsKey(obj);
    }

    public boolean add(E e) {
        return this.map.put(e, PRESENT) == null;
    }

    public boolean remove(Object obj) {
        return this.map.remove(obj) == PRESENT;
    }

    public void clear() {
        this.map.clear();
    }

    public Object clone() {
        try {
            HashSet hashSet = (HashSet) super.clone();
            hashSet.map = (HashMap) this.map.clone();
            return hashSet;
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeInt(this.map.capacity());
        objectOutputStream.writeFloat(this.map.loadFactor());
        objectOutputStream.writeInt(this.map.size());
        for (E writeObject : this.map.keySet()) {
            objectOutputStream.writeObject(writeObject);
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        HashMap<E, Object> hashMap;
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        if (readInt >= 0) {
            float readFloat = objectInputStream.readFloat();
            if (readFloat <= 0.0f || Float.isNaN(readFloat)) {
                throw new InvalidObjectException("Illegal load factor: " + readFloat);
            }
            int readInt2 = objectInputStream.readInt();
            if (readInt2 >= 0) {
                int min = (int) Math.min(((float) readInt2) * Math.min(1.0f / readFloat, 4.0f), 1.07374182E9f);
                if (this instanceof LinkedHashSet) {
                    hashMap = new LinkedHashMap<>(min, readFloat);
                } else {
                    hashMap = new HashMap<>(min, readFloat);
                }
                this.map = hashMap;
                for (int i = 0; i < readInt2; i++) {
                    this.map.put(objectInputStream.readObject(), PRESENT);
                }
                return;
            }
            throw new InvalidObjectException("Illegal size: " + readInt2);
        }
        throw new InvalidObjectException("Illegal capacity: " + readInt);
    }

    public Spliterator<E> spliterator() {
        return new HashMap.KeySpliterator(this.map, 0, -1, 0, 0);
    }
}
