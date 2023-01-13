package sun.misc;

public abstract class LRUCache<N, V> {

    /* renamed from: oa */
    private V[] f858oa = null;
    private final int size;

    /* access modifiers changed from: protected */
    public abstract V create(N n);

    /* access modifiers changed from: protected */
    public abstract boolean hasName(V v, N n);

    public LRUCache(int i) {
        this.size = i;
    }

    public static void moveToFront(Object[] objArr, int i) {
        Object obj = objArr[i];
        while (i > 0) {
            objArr[i] = objArr[i - 1];
            i--;
        }
        objArr[0] = obj;
    }

    public V forName(N n) {
        if (this.f858oa != null) {
            int i = 0;
            while (true) {
                V[] vArr = this.f858oa;
                if (i >= vArr.length) {
                    break;
                }
                V v = vArr[i];
                if (v != null && hasName(v, n)) {
                    if (i > 0) {
                        moveToFront(this.f858oa, i);
                    }
                    return v;
                }
                i++;
            }
        } else {
            this.f858oa = new Object[this.size];
        }
        V create = create(n);
        V[] vArr2 = this.f858oa;
        vArr2[vArr2.length - 1] = create;
        moveToFront(vArr2, vArr2.length - 1);
        return create;
    }
}
