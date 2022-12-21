package java.util;

import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.Serializable;
import java.p026io.StreamCorruptedException;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Hashtable<K, V> extends Dictionary<K, V> implements Map<K, V>, Cloneable, Serializable {
    private static final int ENTRIES = 2;
    private static final int KEYS = 0;
    private static final int MAX_ARRAY_SIZE = 2147483639;
    private static final int VALUES = 1;
    private static final long serialVersionUID = 1421746759512286392L;
    /* access modifiers changed from: private */
    public transient int count;
    private volatile transient Set<Map.Entry<K, V>> entrySet;
    private volatile transient Set<K> keySet;
    private float loadFactor;
    /* access modifiers changed from: private */
    public transient int modCount;
    /* access modifiers changed from: private */
    public transient HashtableEntry<?, ?>[] table;
    private int threshold;
    private volatile transient Collection<V> values;

    public Hashtable(int i, float f) {
        this.modCount = 0;
        if (i < 0) {
            throw new IllegalArgumentException("Illegal Capacity: " + i);
        } else if (f <= 0.0f || Float.isNaN(f)) {
            throw new IllegalArgumentException("Illegal Load: " + f);
        } else {
            i = i == 0 ? 1 : i;
            this.loadFactor = f;
            this.table = new HashtableEntry[i];
            this.threshold = Math.min(i, 2147483640);
        }
    }

    public Hashtable(int i) {
        this(i, 0.75f);
    }

    public Hashtable() {
        this(11, 0.75f);
    }

    public Hashtable(Map<? extends K, ? extends V> map) {
        this(Math.max(map.size() * 2, 11), 0.75f);
        putAll(map);
    }

    public synchronized int size() {
        return this.count;
    }

    public synchronized boolean isEmpty() {
        return this.count == 0;
    }

    public synchronized Enumeration<K> keys() {
        return getEnumeration(0);
    }

    public synchronized Enumeration<V> elements() {
        return getEnumeration(1);
    }

    public synchronized boolean contains(Object obj) {
        if (obj != null) {
            HashtableEntry[] hashtableEntryArr = this.table;
            int length = hashtableEntryArr.length;
            while (true) {
                int i = length - 1;
                if (length <= 0) {
                    return false;
                }
                for (HashtableEntry hashtableEntry = hashtableEntryArr[i]; hashtableEntry != null; hashtableEntry = hashtableEntry.next) {
                    if (hashtableEntry.value.equals(obj)) {
                        return true;
                    }
                }
                length = i;
            }
        } else {
            throw new NullPointerException();
        }
    }

    public boolean containsValue(Object obj) {
        return contains(obj);
    }

    public synchronized boolean containsKey(Object obj) {
        HashtableEntry[] hashtableEntryArr = this.table;
        int hashCode = obj.hashCode();
        for (HashtableEntry hashtableEntry = hashtableEntryArr[(Integer.MAX_VALUE & hashCode) % hashtableEntryArr.length]; hashtableEntry != null; hashtableEntry = hashtableEntry.next) {
            if (hashtableEntry.hash == hashCode && hashtableEntry.key.equals(obj)) {
                return true;
            }
        }
        return false;
    }

    public synchronized V get(Object obj) {
        HashtableEntry[] hashtableEntryArr = this.table;
        int hashCode = obj.hashCode();
        HashtableEntry hashtableEntry = hashtableEntryArr[(Integer.MAX_VALUE & hashCode) % hashtableEntryArr.length];
        while (hashtableEntry != null) {
            if (hashtableEntry.hash != hashCode || !hashtableEntry.key.equals(obj)) {
                hashtableEntry = hashtableEntry.next;
            } else {
                return hashtableEntry.value;
            }
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void rehash() {
        HashtableEntry<?, ?>[] hashtableEntryArr = this.table;
        int length = hashtableEntryArr.length;
        int i = (length << 1) + 1;
        if (i - MAX_ARRAY_SIZE > 0) {
            if (length != MAX_ARRAY_SIZE) {
                i = MAX_ARRAY_SIZE;
            } else {
                return;
            }
        }
        HashtableEntry<?, ?>[] hashtableEntryArr2 = new HashtableEntry[i];
        this.modCount++;
        this.threshold = (int) Math.min(((float) i) * this.loadFactor, 2.14748365E9f);
        this.table = hashtableEntryArr2;
        while (true) {
            int i2 = length - 1;
            if (length > 0) {
                HashtableEntry<K, V> hashtableEntry = hashtableEntryArr[i2];
                while (hashtableEntry != null) {
                    HashtableEntry<K, V> hashtableEntry2 = hashtableEntry.next;
                    int i3 = (hashtableEntry.hash & Integer.MAX_VALUE) % i;
                    hashtableEntry.next = hashtableEntryArr2[i3];
                    hashtableEntryArr2[i3] = hashtableEntry;
                    hashtableEntry = hashtableEntry2;
                }
                length = i2;
            } else {
                return;
            }
        }
    }

    private void addEntry(int i, K k, V v, int i2) {
        this.modCount++;
        HashtableEntry<?, ?>[] hashtableEntryArr = this.table;
        if (this.count >= this.threshold) {
            rehash();
            hashtableEntryArr = this.table;
            i = k.hashCode();
            i2 = (Integer.MAX_VALUE & i) % hashtableEntryArr.length;
        }
        hashtableEntryArr[i2] = new HashtableEntry<>(i, k, v, hashtableEntryArr[i2]);
        this.count++;
    }

    public synchronized V put(K k, V v) {
        if (v != null) {
            HashtableEntry[] hashtableEntryArr = this.table;
            int hashCode = k.hashCode();
            int length = (Integer.MAX_VALUE & hashCode) % hashtableEntryArr.length;
            HashtableEntry hashtableEntry = hashtableEntryArr[length];
            while (hashtableEntry != null) {
                if (hashtableEntry.hash != hashCode || !hashtableEntry.key.equals(k)) {
                    hashtableEntry = hashtableEntry.next;
                } else {
                    V v2 = hashtableEntry.value;
                    hashtableEntry.value = v;
                    return v2;
                }
            }
            addEntry(hashCode, k, v, length);
            return null;
        }
        throw new NullPointerException();
    }

    public synchronized V remove(Object obj) {
        HashtableEntry[] hashtableEntryArr = this.table;
        int hashCode = obj.hashCode();
        int length = (Integer.MAX_VALUE & hashCode) % hashtableEntryArr.length;
        HashtableEntry<K, V> hashtableEntry = hashtableEntryArr[length];
        HashtableEntry hashtableEntry2 = null;
        while (hashtableEntry != null) {
            if (hashtableEntry.hash != hashCode || !hashtableEntry.key.equals(obj)) {
                hashtableEntry2 = hashtableEntry;
                hashtableEntry = hashtableEntry.next;
            } else {
                this.modCount++;
                if (hashtableEntry2 != null) {
                    hashtableEntry2.next = hashtableEntry.next;
                } else {
                    hashtableEntryArr[length] = hashtableEntry.next;
                }
                this.count--;
                V v = hashtableEntry.value;
                hashtableEntry.value = null;
                return v;
            }
        }
        return null;
    }

    public synchronized void putAll(Map<? extends K, ? extends V> map) {
        for (Map.Entry next : map.entrySet()) {
            put(next.getKey(), next.getValue());
        }
    }

    public synchronized void clear() {
        HashtableEntry<?, ?>[] hashtableEntryArr = this.table;
        this.modCount++;
        int length = hashtableEntryArr.length;
        while (true) {
            length--;
            if (length >= 0) {
                hashtableEntryArr[length] = null;
            } else {
                this.count = 0;
            }
        }
    }

    public synchronized Object clone() {
        Hashtable hashtable;
        try {
            hashtable = (Hashtable) super.clone();
            hashtable.table = new HashtableEntry[this.table.length];
            int length = this.table.length;
            while (true) {
                int i = length - 1;
                HashtableEntry<?, ?> hashtableEntry = null;
                if (length > 0) {
                    HashtableEntry<?, ?>[] hashtableEntryArr = hashtable.table;
                    HashtableEntry<?, ?> hashtableEntry2 = this.table[i];
                    if (hashtableEntry2 != null) {
                        hashtableEntry = (HashtableEntry) hashtableEntry2.clone();
                    }
                    hashtableEntryArr[i] = hashtableEntry;
                    length = i;
                } else {
                    hashtable.keySet = null;
                    hashtable.entrySet = null;
                    hashtable.values = null;
                    hashtable.modCount = 0;
                }
            }
        } catch (CloneNotSupportedException e) {
            throw new InternalError((Throwable) e);
        }
        return hashtable;
    }

    public synchronized String toString() {
        String str;
        String str2;
        int size = size() - 1;
        if (size == -1) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        Iterator it = entrySet().iterator();
        sb.append('{');
        int i = 0;
        while (true) {
            Map.Entry entry = (Map.Entry) it.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key == this) {
                str = "(this Map)";
            } else {
                str = key.toString();
            }
            sb.append(str);
            sb.append('=');
            if (value == this) {
                str2 = "(this Map)";
            } else {
                str2 = value.toString();
            }
            sb.append(str2);
            if (i == size) {
                sb.append('}');
                return sb.toString();
            }
            sb.append(", ");
            i++;
        }
    }

    private <T> Enumeration<T> getEnumeration(int i) {
        if (this.count == 0) {
            return Collections.emptyEnumeration();
        }
        return new Enumerator(i, false);
    }

    /* access modifiers changed from: private */
    public <T> Iterator<T> getIterator(int i) {
        if (this.count == 0) {
            return Collections.emptyIterator();
        }
        return new Enumerator(i, true);
    }

    public Set<K> keySet() {
        if (this.keySet == null) {
            this.keySet = Collections.synchronizedSet(new KeySet(), this);
        }
        return this.keySet;
    }

    private class KeySet extends AbstractSet<K> {
        private KeySet() {
        }

        public Iterator<K> iterator() {
            return Hashtable.this.getIterator(0);
        }

        public int size() {
            return Hashtable.this.count;
        }

        public boolean contains(Object obj) {
            return Hashtable.this.containsKey(obj);
        }

        public boolean remove(Object obj) {
            return Hashtable.this.remove(obj) != null;
        }

        public void clear() {
            Hashtable.this.clear();
        }
    }

    public Set<Map.Entry<K, V>> entrySet() {
        if (this.entrySet == null) {
            this.entrySet = Collections.synchronizedSet(new EntrySet(), this);
        }
        return this.entrySet;
    }

    private class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        private EntrySet() {
        }

        public Iterator<Map.Entry<K, V>> iterator() {
            return Hashtable.this.getIterator(2);
        }

        public boolean add(Map.Entry<K, V> entry) {
            return super.add(entry);
        }

        public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            HashtableEntry<K, V>[] r4 = Hashtable.this.table;
            int hashCode = key.hashCode();
            for (HashtableEntry<K, V> hashtableEntry = r4[(Integer.MAX_VALUE & hashCode) % r4.length]; hashtableEntry != null; hashtableEntry = hashtableEntry.next) {
                if (hashtableEntry.hash == hashCode && hashtableEntry.equals(entry)) {
                    return true;
                }
            }
            return false;
        }

        public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry)) {
                return false;
            }
            Map.Entry entry = (Map.Entry) obj;
            Object key = entry.getKey();
            HashtableEntry<K, V>[] r2 = Hashtable.this.table;
            int hashCode = key.hashCode();
            int length = (Integer.MAX_VALUE & hashCode) % r2.length;
            HashtableEntry<K, V> hashtableEntry = r2[length];
            HashtableEntry<K, V> hashtableEntry2 = null;
            while (hashtableEntry != null) {
                if (hashtableEntry.hash != hashCode || !hashtableEntry.equals(entry)) {
                    hashtableEntry2 = hashtableEntry;
                    hashtableEntry = hashtableEntry.next;
                } else {
                    Hashtable hashtable = Hashtable.this;
                    hashtable.modCount = hashtable.modCount + 1;
                    if (hashtableEntry2 != null) {
                        hashtableEntry2.next = hashtableEntry.next;
                    } else {
                        r2[length] = hashtableEntry.next;
                    }
                    Hashtable hashtable2 = Hashtable.this;
                    hashtable2.count = hashtable2.count - 1;
                    hashtableEntry.value = null;
                    return true;
                }
            }
            return false;
        }

        public int size() {
            return Hashtable.this.count;
        }

        public void clear() {
            Hashtable.this.clear();
        }
    }

    public Collection<V> values() {
        if (this.values == null) {
            this.values = Collections.synchronizedCollection(new ValueCollection(), this);
        }
        return this.values;
    }

    private class ValueCollection extends AbstractCollection<V> {
        private ValueCollection() {
        }

        public Iterator<V> iterator() {
            return Hashtable.this.getIterator(1);
        }

        public int size() {
            return Hashtable.this.count;
        }

        public boolean contains(Object obj) {
            return Hashtable.this.containsValue(obj);
        }

        public void clear() {
            Hashtable.this.clear();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0046, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean equals(java.lang.Object r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            r0 = 1
            if (r6 != r5) goto L_0x0006
            monitor-exit(r5)
            return r0
        L_0x0006:
            boolean r1 = r6 instanceof java.util.Map     // Catch:{ all -> 0x0059 }
            r2 = 0
            if (r1 != 0) goto L_0x000d
            monitor-exit(r5)
            return r2
        L_0x000d:
            java.util.Map r6 = (java.util.Map) r6     // Catch:{ all -> 0x0059 }
            int r1 = r6.size()     // Catch:{ all -> 0x0059 }
            int r3 = r5.size()     // Catch:{ all -> 0x0059 }
            if (r1 == r3) goto L_0x001b
            monitor-exit(r5)
            return r2
        L_0x001b:
            java.util.Set r1 = r5.entrySet()     // Catch:{ ClassCastException -> 0x0057, NullPointerException -> 0x0055 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ ClassCastException -> 0x0057, NullPointerException -> 0x0055 }
        L_0x0023:
            boolean r3 = r1.hasNext()     // Catch:{ ClassCastException -> 0x0057, NullPointerException -> 0x0055 }
            if (r3 == 0) goto L_0x0053
            java.lang.Object r3 = r1.next()     // Catch:{ ClassCastException -> 0x0057, NullPointerException -> 0x0055 }
            java.util.Map$Entry r3 = (java.util.Map.Entry) r3     // Catch:{ ClassCastException -> 0x0057, NullPointerException -> 0x0055 }
            java.lang.Object r4 = r3.getKey()     // Catch:{ ClassCastException -> 0x0057, NullPointerException -> 0x0055 }
            java.lang.Object r3 = r3.getValue()     // Catch:{ ClassCastException -> 0x0057, NullPointerException -> 0x0055 }
            if (r3 != 0) goto L_0x0047
            java.lang.Object r3 = r6.get(r4)     // Catch:{ ClassCastException -> 0x0057, NullPointerException -> 0x0055 }
            if (r3 != 0) goto L_0x0045
            boolean r3 = r6.containsKey(r4)     // Catch:{ ClassCastException -> 0x0057, NullPointerException -> 0x0055 }
            if (r3 != 0) goto L_0x0023
        L_0x0045:
            monitor-exit(r5)
            return r2
        L_0x0047:
            java.lang.Object r4 = r6.get(r4)     // Catch:{ ClassCastException -> 0x0057, NullPointerException -> 0x0055 }
            boolean r3 = r3.equals(r4)     // Catch:{ ClassCastException -> 0x0057, NullPointerException -> 0x0055 }
            if (r3 != 0) goto L_0x0023
            monitor-exit(r5)
            return r2
        L_0x0053:
            monitor-exit(r5)
            return r0
        L_0x0055:
            monitor-exit(r5)
            return r2
        L_0x0057:
            monitor-exit(r5)
            return r2
        L_0x0059:
            r6 = move-exception
            monitor-exit(r5)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Hashtable.equals(java.lang.Object):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002e, code lost:
        return 0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized int hashCode() {
        /*
            r6 = this;
            monitor-enter(r6)
            int r0 = r6.count     // Catch:{ all -> 0x002f }
            r1 = 0
            if (r0 == 0) goto L_0x002d
            float r0 = r6.loadFactor     // Catch:{ all -> 0x002f }
            r2 = 0
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 >= 0) goto L_0x000e
            goto L_0x002d
        L_0x000e:
            float r0 = -r0
            r6.loadFactor = r0     // Catch:{ all -> 0x002f }
            java.util.Hashtable$HashtableEntry<?, ?>[] r0 = r6.table     // Catch:{ all -> 0x002f }
            int r2 = r0.length     // Catch:{ all -> 0x002f }
            r3 = r1
        L_0x0015:
            if (r1 >= r2) goto L_0x0026
            r4 = r0[r1]     // Catch:{ all -> 0x002f }
        L_0x0019:
            if (r4 == 0) goto L_0x0023
            int r5 = r4.hashCode()     // Catch:{ all -> 0x002f }
            int r3 = r3 + r5
            java.util.Hashtable$HashtableEntry<K, V> r4 = r4.next     // Catch:{ all -> 0x002f }
            goto L_0x0019
        L_0x0023:
            int r1 = r1 + 1
            goto L_0x0015
        L_0x0026:
            float r0 = r6.loadFactor     // Catch:{ all -> 0x002f }
            float r0 = -r0
            r6.loadFactor = r0     // Catch:{ all -> 0x002f }
            monitor-exit(r6)
            return r3
        L_0x002d:
            monitor-exit(r6)
            return r1
        L_0x002f:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Hashtable.hashCode():int");
    }

    public synchronized V getOrDefault(Object obj, V v) {
        V v2 = get(obj);
        if (v2 != null) {
            v = v2;
        }
        return v;
    }

    public synchronized void forEach(BiConsumer<? super K, ? super V> biConsumer) {
        Objects.requireNonNull(biConsumer);
        int i = this.modCount;
        for (HashtableEntry hashtableEntry : this.table) {
            while (hashtableEntry != null) {
                biConsumer.accept(hashtableEntry.key, hashtableEntry.value);
                hashtableEntry = hashtableEntry.next;
                if (i != this.modCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    public synchronized void replaceAll(BiFunction<? super K, ? super V, ? extends V> biFunction) {
        Objects.requireNonNull(biFunction);
        int i = this.modCount;
        for (HashtableEntry hashtableEntry : this.table) {
            while (hashtableEntry != null) {
                hashtableEntry.value = Objects.requireNonNull(biFunction.apply(hashtableEntry.key, hashtableEntry.value));
                hashtableEntry = hashtableEntry.next;
                if (i != this.modCount) {
                    throw new ConcurrentModificationException();
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0027, code lost:
        return r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized V putIfAbsent(K r5, V r6) {
        /*
            r4 = this;
            monitor-enter(r4)
            java.util.Objects.requireNonNull(r6)     // Catch:{ all -> 0x0031 }
            java.util.Hashtable$HashtableEntry<?, ?>[] r0 = r4.table     // Catch:{ all -> 0x0031 }
            int r1 = r5.hashCode()     // Catch:{ all -> 0x0031 }
            r2 = 2147483647(0x7fffffff, float:NaN)
            r2 = r2 & r1
            int r3 = r0.length     // Catch:{ all -> 0x0031 }
            int r2 = r2 % r3
            r0 = r0[r2]     // Catch:{ all -> 0x0031 }
        L_0x0012:
            if (r0 == 0) goto L_0x002b
            int r3 = r0.hash     // Catch:{ all -> 0x0031 }
            if (r3 != r1) goto L_0x0028
            K r3 = r0.key     // Catch:{ all -> 0x0031 }
            boolean r3 = r3.equals(r5)     // Catch:{ all -> 0x0031 }
            if (r3 == 0) goto L_0x0028
            V r5 = r0.value     // Catch:{ all -> 0x0031 }
            if (r5 != 0) goto L_0x0026
            r0.value = r6     // Catch:{ all -> 0x0031 }
        L_0x0026:
            monitor-exit(r4)
            return r5
        L_0x0028:
            java.util.Hashtable$HashtableEntry<K, V> r0 = r0.next     // Catch:{ all -> 0x0031 }
            goto L_0x0012
        L_0x002b:
            r4.addEntry(r1, r5, r6, r2)     // Catch:{ all -> 0x0031 }
            monitor-exit(r4)
            r4 = 0
            return r4
        L_0x0031:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Hashtable.putIfAbsent(java.lang.Object, java.lang.Object):java.lang.Object");
    }

    public synchronized boolean remove(Object obj, Object obj2) {
        Objects.requireNonNull(obj2);
        HashtableEntry[] hashtableEntryArr = this.table;
        int hashCode = obj.hashCode();
        int length = (Integer.MAX_VALUE & hashCode) % hashtableEntryArr.length;
        HashtableEntry<K, V> hashtableEntry = hashtableEntryArr[length];
        HashtableEntry hashtableEntry2 = null;
        while (hashtableEntry != null) {
            if (hashtableEntry.hash != hashCode || !hashtableEntry.key.equals(obj) || !hashtableEntry.value.equals(obj2)) {
                hashtableEntry2 = hashtableEntry;
                hashtableEntry = hashtableEntry.next;
            } else {
                this.modCount++;
                if (hashtableEntry2 != null) {
                    hashtableEntry2.next = hashtableEntry.next;
                } else {
                    hashtableEntryArr[length] = hashtableEntry.next;
                }
                this.count--;
                hashtableEntry.value = null;
                return true;
            }
        }
        return false;
    }

    public synchronized boolean replace(K k, V v, V v2) {
        Objects.requireNonNull(v);
        Objects.requireNonNull(v2);
        HashtableEntry[] hashtableEntryArr = this.table;
        int hashCode = k.hashCode();
        HashtableEntry hashtableEntry = hashtableEntryArr[(Integer.MAX_VALUE & hashCode) % hashtableEntryArr.length];
        while (hashtableEntry != null) {
            if (hashtableEntry.hash != hashCode || !hashtableEntry.key.equals(k)) {
                hashtableEntry = hashtableEntry.next;
            } else if (!hashtableEntry.value.equals(v)) {
                return false;
            } else {
                hashtableEntry.value = v2;
                return true;
            }
        }
        return false;
    }

    public synchronized V replace(K k, V v) {
        Objects.requireNonNull(v);
        HashtableEntry[] hashtableEntryArr = this.table;
        int hashCode = k.hashCode();
        HashtableEntry hashtableEntry = hashtableEntryArr[(Integer.MAX_VALUE & hashCode) % hashtableEntryArr.length];
        while (hashtableEntry != null) {
            if (hashtableEntry.hash != hashCode || !hashtableEntry.key.equals(k)) {
                hashtableEntry = hashtableEntry.next;
            } else {
                V v2 = hashtableEntry.value;
                hashtableEntry.value = v;
                return v2;
            }
        }
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0031, code lost:
        return r6;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized V computeIfAbsent(K r5, java.util.function.Function<? super K, ? extends V> r6) {
        /*
            r4 = this;
            monitor-enter(r4)
            java.util.Objects.requireNonNull(r6)     // Catch:{ all -> 0x0032 }
            java.util.Hashtable$HashtableEntry<?, ?>[] r0 = r4.table     // Catch:{ all -> 0x0032 }
            int r1 = r5.hashCode()     // Catch:{ all -> 0x0032 }
            r2 = 2147483647(0x7fffffff, float:NaN)
            r2 = r2 & r1
            int r3 = r0.length     // Catch:{ all -> 0x0032 }
            int r2 = r2 % r3
            r0 = r0[r2]     // Catch:{ all -> 0x0032 }
        L_0x0012:
            if (r0 == 0) goto L_0x0027
            int r3 = r0.hash     // Catch:{ all -> 0x0032 }
            if (r3 != r1) goto L_0x0024
            K r3 = r0.key     // Catch:{ all -> 0x0032 }
            boolean r3 = r3.equals(r5)     // Catch:{ all -> 0x0032 }
            if (r3 == 0) goto L_0x0024
            V r5 = r0.value     // Catch:{ all -> 0x0032 }
            monitor-exit(r4)
            return r5
        L_0x0024:
            java.util.Hashtable$HashtableEntry<K, V> r0 = r0.next     // Catch:{ all -> 0x0032 }
            goto L_0x0012
        L_0x0027:
            java.lang.Object r6 = r6.apply(r5)     // Catch:{ all -> 0x0032 }
            if (r6 == 0) goto L_0x0030
            r4.addEntry(r1, r5, r6, r2)     // Catch:{ all -> 0x0032 }
        L_0x0030:
            monitor-exit(r4)
            return r6
        L_0x0032:
            r5 = move-exception
            monitor-exit(r4)
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Hashtable.computeIfAbsent(java.lang.Object, java.util.function.Function):java.lang.Object");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0045, code lost:
        return r9;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized V computeIfPresent(K r9, java.util.function.BiFunction<? super K, ? super V, ? extends V> r10) {
        /*
            r8 = this;
            monitor-enter(r8)
            java.util.Objects.requireNonNull(r10)     // Catch:{ all -> 0x004e }
            java.util.Hashtable$HashtableEntry<?, ?>[] r0 = r8.table     // Catch:{ all -> 0x004e }
            int r1 = r9.hashCode()     // Catch:{ all -> 0x004e }
            r2 = 2147483647(0x7fffffff, float:NaN)
            r2 = r2 & r1
            int r3 = r0.length     // Catch:{ all -> 0x004e }
            int r2 = r2 % r3
            r3 = r0[r2]     // Catch:{ all -> 0x004e }
            r4 = 0
            r5 = r4
        L_0x0014:
            if (r3 == 0) goto L_0x004c
            int r6 = r3.hash     // Catch:{ all -> 0x004e }
            if (r6 != r1) goto L_0x0046
            K r6 = r3.key     // Catch:{ all -> 0x004e }
            boolean r6 = r6.equals(r9)     // Catch:{ all -> 0x004e }
            if (r6 == 0) goto L_0x0046
            V r1 = r3.value     // Catch:{ all -> 0x004e }
            java.lang.Object r9 = r10.apply(r9, r1)     // Catch:{ all -> 0x004e }
            if (r9 != 0) goto L_0x0042
            int r10 = r8.modCount     // Catch:{ all -> 0x004e }
            int r10 = r10 + 1
            r8.modCount = r10     // Catch:{ all -> 0x004e }
            if (r5 == 0) goto L_0x0037
            java.util.Hashtable$HashtableEntry<K, V> r10 = r3.next     // Catch:{ all -> 0x004e }
            r5.next = r10     // Catch:{ all -> 0x004e }
            goto L_0x003b
        L_0x0037:
            java.util.Hashtable$HashtableEntry<K, V> r10 = r3.next     // Catch:{ all -> 0x004e }
            r0[r2] = r10     // Catch:{ all -> 0x004e }
        L_0x003b:
            int r10 = r8.count     // Catch:{ all -> 0x004e }
            int r10 = r10 + -1
            r8.count = r10     // Catch:{ all -> 0x004e }
            goto L_0x0044
        L_0x0042:
            r3.value = r9     // Catch:{ all -> 0x004e }
        L_0x0044:
            monitor-exit(r8)
            return r9
        L_0x0046:
            java.util.Hashtable$HashtableEntry<K, V> r5 = r3.next     // Catch:{ all -> 0x004e }
            r7 = r5
            r5 = r3
            r3 = r7
            goto L_0x0014
        L_0x004c:
            monitor-exit(r8)
            return r4
        L_0x004e:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Hashtable.computeIfPresent(java.lang.Object, java.util.function.BiFunction):java.lang.Object");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0045, code lost:
        return r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0056, code lost:
        return r10;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized V compute(K r9, java.util.function.BiFunction<? super K, ? super V, ? extends V> r10) {
        /*
            r8 = this;
            monitor-enter(r8)
            java.util.Objects.requireNonNull(r10)     // Catch:{ all -> 0x0057 }
            java.util.Hashtable$HashtableEntry<?, ?>[] r0 = r8.table     // Catch:{ all -> 0x0057 }
            int r1 = r9.hashCode()     // Catch:{ all -> 0x0057 }
            r2 = 2147483647(0x7fffffff, float:NaN)
            r2 = r2 & r1
            int r3 = r0.length     // Catch:{ all -> 0x0057 }
            int r2 = r2 % r3
            r3 = r0[r2]     // Catch:{ all -> 0x0057 }
            r4 = 0
            r5 = r4
        L_0x0014:
            if (r3 == 0) goto L_0x004c
            int r6 = r3.hash     // Catch:{ all -> 0x0057 }
            if (r6 != r1) goto L_0x0046
            K r6 = r3.key     // Catch:{ all -> 0x0057 }
            boolean r6 = java.util.Objects.equals(r6, r9)     // Catch:{ all -> 0x0057 }
            if (r6 == 0) goto L_0x0046
            V r1 = r3.value     // Catch:{ all -> 0x0057 }
            java.lang.Object r9 = r10.apply(r9, r1)     // Catch:{ all -> 0x0057 }
            if (r9 != 0) goto L_0x0042
            int r10 = r8.modCount     // Catch:{ all -> 0x0057 }
            int r10 = r10 + 1
            r8.modCount = r10     // Catch:{ all -> 0x0057 }
            if (r5 == 0) goto L_0x0037
            java.util.Hashtable$HashtableEntry<K, V> r10 = r3.next     // Catch:{ all -> 0x0057 }
            r5.next = r10     // Catch:{ all -> 0x0057 }
            goto L_0x003b
        L_0x0037:
            java.util.Hashtable$HashtableEntry<K, V> r10 = r3.next     // Catch:{ all -> 0x0057 }
            r0[r2] = r10     // Catch:{ all -> 0x0057 }
        L_0x003b:
            int r10 = r8.count     // Catch:{ all -> 0x0057 }
            int r10 = r10 + -1
            r8.count = r10     // Catch:{ all -> 0x0057 }
            goto L_0x0044
        L_0x0042:
            r3.value = r9     // Catch:{ all -> 0x0057 }
        L_0x0044:
            monitor-exit(r8)
            return r9
        L_0x0046:
            java.util.Hashtable$HashtableEntry<K, V> r5 = r3.next     // Catch:{ all -> 0x0057 }
            r7 = r5
            r5 = r3
            r3 = r7
            goto L_0x0014
        L_0x004c:
            java.lang.Object r10 = r10.apply(r9, r4)     // Catch:{ all -> 0x0057 }
            if (r10 == 0) goto L_0x0055
            r8.addEntry(r1, r9, r10, r2)     // Catch:{ all -> 0x0057 }
        L_0x0055:
            monitor-exit(r8)
            return r10
        L_0x0057:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Hashtable.compute(java.lang.Object, java.util.function.BiFunction):java.lang.Object");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0044, code lost:
        return r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0051, code lost:
        return r9;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized V merge(K r8, V r9, java.util.function.BiFunction<? super V, ? super V, ? extends V> r10) {
        /*
            r7 = this;
            monitor-enter(r7)
            java.util.Objects.requireNonNull(r10)     // Catch:{ all -> 0x0052 }
            java.util.Hashtable$HashtableEntry<?, ?>[] r0 = r7.table     // Catch:{ all -> 0x0052 }
            int r1 = r8.hashCode()     // Catch:{ all -> 0x0052 }
            r2 = 2147483647(0x7fffffff, float:NaN)
            r2 = r2 & r1
            int r3 = r0.length     // Catch:{ all -> 0x0052 }
            int r2 = r2 % r3
            r3 = r0[r2]     // Catch:{ all -> 0x0052 }
            r4 = 0
        L_0x0013:
            if (r3 == 0) goto L_0x004b
            int r5 = r3.hash     // Catch:{ all -> 0x0052 }
            if (r5 != r1) goto L_0x0045
            K r5 = r3.key     // Catch:{ all -> 0x0052 }
            boolean r5 = r5.equals(r8)     // Catch:{ all -> 0x0052 }
            if (r5 == 0) goto L_0x0045
            V r8 = r3.value     // Catch:{ all -> 0x0052 }
            java.lang.Object r8 = r10.apply(r8, r9)     // Catch:{ all -> 0x0052 }
            if (r8 != 0) goto L_0x0041
            int r9 = r7.modCount     // Catch:{ all -> 0x0052 }
            int r9 = r9 + 1
            r7.modCount = r9     // Catch:{ all -> 0x0052 }
            if (r4 == 0) goto L_0x0036
            java.util.Hashtable$HashtableEntry<K, V> r9 = r3.next     // Catch:{ all -> 0x0052 }
            r4.next = r9     // Catch:{ all -> 0x0052 }
            goto L_0x003a
        L_0x0036:
            java.util.Hashtable$HashtableEntry<K, V> r9 = r3.next     // Catch:{ all -> 0x0052 }
            r0[r2] = r9     // Catch:{ all -> 0x0052 }
        L_0x003a:
            int r9 = r7.count     // Catch:{ all -> 0x0052 }
            int r9 = r9 + -1
            r7.count = r9     // Catch:{ all -> 0x0052 }
            goto L_0x0043
        L_0x0041:
            r3.value = r8     // Catch:{ all -> 0x0052 }
        L_0x0043:
            monitor-exit(r7)
            return r8
        L_0x0045:
            java.util.Hashtable$HashtableEntry<K, V> r4 = r3.next     // Catch:{ all -> 0x0052 }
            r6 = r4
            r4 = r3
            r3 = r6
            goto L_0x0013
        L_0x004b:
            if (r9 == 0) goto L_0x0050
            r7.addEntry(r1, r8, r9, r2)     // Catch:{ all -> 0x0052 }
        L_0x0050:
            monitor-exit(r7)
            return r9
        L_0x0052:
            r8 = move-exception
            monitor-exit(r7)
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.Hashtable.merge(java.lang.Object, java.lang.Object, java.util.function.BiFunction):java.lang.Object");
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        HashtableEntry<K, V> hashtableEntry;
        synchronized (this) {
            objectOutputStream.defaultWriteObject();
            objectOutputStream.writeInt(this.table.length);
            objectOutputStream.writeInt(this.count);
            hashtableEntry = null;
            int i = 0;
            while (true) {
                HashtableEntry[] hashtableEntryArr = this.table;
                if (i >= hashtableEntryArr.length) {
                    break;
                }
                HashtableEntry hashtableEntry2 = hashtableEntryArr[i];
                while (hashtableEntry2 != null) {
                    HashtableEntry<K, V> hashtableEntry3 = new HashtableEntry<>(0, hashtableEntry2.key, hashtableEntry2.value, hashtableEntry);
                    hashtableEntry2 = hashtableEntry2.next;
                    hashtableEntry = hashtableEntry3;
                }
                i++;
            }
        }
        while (hashtableEntry != null) {
            objectOutputStream.writeObject(hashtableEntry.key);
            objectOutputStream.writeObject(hashtableEntry.value);
            hashtableEntry = hashtableEntry.next;
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        float f = this.loadFactor;
        if (f <= 0.0f || Float.isNaN(f)) {
            throw new StreamCorruptedException("Illegal Load: " + this.loadFactor);
        }
        int readInt = objectInputStream.readInt();
        int readInt2 = objectInputStream.readInt();
        if (readInt2 >= 0) {
            int max = Math.max(readInt, ((int) (((float) readInt2) / this.loadFactor)) + 1);
            int i = ((int) (((float) ((readInt2 / 20) + readInt2)) / this.loadFactor)) + 3;
            if (i > readInt2 && (i & 1) == 0) {
                i--;
            }
            int min = Math.min(i, max);
            this.table = new HashtableEntry[min];
            this.threshold = (int) Math.min(((float) min) * this.loadFactor, 2.14748365E9f);
            this.count = 0;
            while (readInt2 > 0) {
                reconstitutionPut(this.table, objectInputStream.readObject(), objectInputStream.readObject());
                readInt2--;
            }
            return;
        }
        throw new StreamCorruptedException("Illegal # of Elements: " + readInt2);
    }

    private void reconstitutionPut(HashtableEntry<?, ?>[] hashtableEntryArr, K k, V v) throws StreamCorruptedException {
        if (v != null) {
            int hashCode = k.hashCode();
            int length = (Integer.MAX_VALUE & hashCode) % hashtableEntryArr.length;
            HashtableEntry<K, V> hashtableEntry = hashtableEntryArr[length];
            while (hashtableEntry != null) {
                if (hashtableEntry.hash != hashCode || !hashtableEntry.key.equals(k)) {
                    hashtableEntry = hashtableEntry.next;
                } else {
                    throw new StreamCorruptedException();
                }
            }
            hashtableEntryArr[length] = new HashtableEntry<>(hashCode, k, v, hashtableEntryArr[length]);
            this.count++;
            return;
        }
        throw new StreamCorruptedException();
    }

    private static class HashtableEntry<K, V> implements Map.Entry<K, V> {
        final int hash;
        final K key;
        HashtableEntry<K, V> next;
        V value;

        protected HashtableEntry(int i, K k, V v, HashtableEntry<K, V> hashtableEntry) {
            this.hash = i;
            this.key = k;
            this.value = v;
            this.next = hashtableEntry;
        }

        /* access modifiers changed from: protected */
        public Object clone() {
            int i = this.hash;
            K k = this.key;
            V v = this.value;
            HashtableEntry<K, V> hashtableEntry = this.next;
            return new HashtableEntry(i, k, v, hashtableEntry == null ? null : (HashtableEntry) hashtableEntry.clone());
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public V setValue(V v) {
            v.getClass();
            V v2 = this.value;
            this.value = v;
            return v2;
        }

        /* JADX WARNING: Removed duplicated region for block: B:15:0x0032 A[ORIG_RETURN, RETURN, SYNTHETIC] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean equals(java.lang.Object r4) {
            /*
                r3 = this;
                boolean r0 = r4 instanceof java.util.Map.Entry
                r1 = 0
                if (r0 != 0) goto L_0x0006
                return r1
            L_0x0006:
                java.util.Map$Entry r4 = (java.util.Map.Entry) r4
                K r0 = r3.key
                if (r0 != 0) goto L_0x0013
                java.lang.Object r0 = r4.getKey()
                if (r0 != 0) goto L_0x0033
                goto L_0x001d
            L_0x0013:
                java.lang.Object r2 = r4.getKey()
                boolean r0 = r0.equals(r2)
                if (r0 == 0) goto L_0x0033
            L_0x001d:
                V r3 = r3.value
                if (r3 != 0) goto L_0x0028
                java.lang.Object r3 = r4.getValue()
                if (r3 != 0) goto L_0x0033
                goto L_0x0032
            L_0x0028:
                java.lang.Object r4 = r4.getValue()
                boolean r3 = r3.equals(r4)
                if (r3 == 0) goto L_0x0033
            L_0x0032:
                r1 = 1
            L_0x0033:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.Hashtable.HashtableEntry.equals(java.lang.Object):boolean");
        }

        public int hashCode() {
            return Objects.hashCode(this.value) ^ this.hash;
        }

        public String toString() {
            return this.key.toString() + "=" + this.value.toString();
        }
    }

    private class Enumerator<T> implements Enumeration<T>, Iterator<T> {
        HashtableEntry<?, ?> entry;
        protected int expectedModCount;
        int index;
        boolean iterator;
        HashtableEntry<?, ?> lastReturned;
        HashtableEntry<?, ?>[] table;
        int type;

        Enumerator(int i, boolean z) {
            HashtableEntry<?, ?>[] r0 = Hashtable.this.table;
            this.table = r0;
            this.index = r0.length;
            this.expectedModCount = Hashtable.this.modCount;
            this.type = i;
            this.iterator = z;
        }

        public boolean hasMoreElements() {
            HashtableEntry<?, ?> hashtableEntry = this.entry;
            int i = this.index;
            HashtableEntry<?, ?>[] hashtableEntryArr = this.table;
            while (hashtableEntry == null && i > 0) {
                i--;
                hashtableEntry = hashtableEntryArr[i];
            }
            this.entry = hashtableEntry;
            this.index = i;
            return hashtableEntry != null;
        }

        public T nextElement() {
            T t = this.entry;
            int i = this.index;
            T[] tArr = this.table;
            while (t == null && i > 0) {
                i--;
                t = tArr[i];
            }
            this.entry = t;
            this.index = i;
            if (t != null) {
                this.lastReturned = t;
                this.entry = t.next;
                int i2 = this.type;
                if (i2 == 0) {
                    return t.key;
                }
                return i2 == 1 ? t.value : t;
            }
            throw new NoSuchElementException("Hashtable Enumerator");
        }

        public boolean hasNext() {
            return hasMoreElements();
        }

        public T next() {
            if (Hashtable.this.modCount == this.expectedModCount) {
                return nextElement();
            }
            throw new ConcurrentModificationException();
        }

        public void remove() {
            if (!this.iterator) {
                throw new UnsupportedOperationException();
            } else if (this.lastReturned == null) {
                throw new IllegalStateException("Hashtable Enumerator");
            } else if (Hashtable.this.modCount == this.expectedModCount) {
                synchronized (Hashtable.this) {
                    HashtableEntry<K, V>[] r1 = Hashtable.this.table;
                    int length = (this.lastReturned.hash & Integer.MAX_VALUE) % r1.length;
                    HashtableEntry<K, V> hashtableEntry = r1[length];
                    HashtableEntry<K, V> hashtableEntry2 = null;
                    while (hashtableEntry != null) {
                        if (hashtableEntry == this.lastReturned) {
                            Hashtable hashtable = Hashtable.this;
                            hashtable.modCount = hashtable.modCount + 1;
                            this.expectedModCount++;
                            if (hashtableEntry2 == null) {
                                r1[length] = hashtableEntry.next;
                            } else {
                                hashtableEntry2.next = hashtableEntry.next;
                            }
                            Hashtable hashtable2 = Hashtable.this;
                            hashtable2.count = hashtable2.count - 1;
                            this.lastReturned = null;
                        } else {
                            hashtableEntry2 = hashtableEntry;
                            hashtableEntry = hashtableEntry.next;
                        }
                    }
                    throw new ConcurrentModificationException();
                }
            } else {
                throw new ConcurrentModificationException();
            }
        }
    }
}
