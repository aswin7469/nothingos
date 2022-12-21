package java.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

abstract class ChangeListenerMap<L extends EventListener> {
    private Map<String, L[]> map;

    public abstract L extract(L l);

    /* access modifiers changed from: protected */
    public abstract L[] newArray(int i);

    /* access modifiers changed from: protected */
    public abstract L newProxy(String str, L l);

    ChangeListenerMap() {
    }

    public final synchronized void add(String str, L l) {
        if (this.map == null) {
            this.map = new HashMap();
        }
        EventListener[] eventListenerArr = (EventListener[]) this.map.get(str);
        int length = eventListenerArr != null ? eventListenerArr.length : 0;
        EventListener[] newArray = newArray(length + 1);
        newArray[length] = l;
        if (eventListenerArr != null) {
            System.arraycopy((Object) eventListenerArr, 0, (Object) newArray, 0, length);
        }
        this.map.put(str, newArray);
    }

    public final synchronized void remove(String str, L l) {
        EventListener[] eventListenerArr;
        Map<String, L[]> map2 = this.map;
        if (map2 != null && (eventListenerArr = (EventListener[]) map2.get(str)) != null) {
            int i = 0;
            while (true) {
                if (i >= eventListenerArr.length) {
                    break;
                } else if (l.equals(eventListenerArr[i])) {
                    int length = eventListenerArr.length - 1;
                    if (length > 0) {
                        EventListener[] newArray = newArray(length);
                        System.arraycopy((Object) eventListenerArr, 0, (Object) newArray, 0, i);
                        System.arraycopy((Object) eventListenerArr, i + 1, (Object) newArray, i, length - i);
                        this.map.put(str, newArray);
                    } else {
                        this.map.remove(str);
                        if (this.map.isEmpty()) {
                            this.map = null;
                        }
                    }
                } else {
                    i++;
                }
            }
        }
    }

    public final synchronized L[] get(String str) {
        Map<String, L[]> map2;
        map2 = this.map;
        return map2 != null ? (EventListener[]) map2.get(str) : null;
    }

    public final void set(String str, L[] lArr) {
        if (lArr != null) {
            if (this.map == null) {
                this.map = new HashMap();
            }
            this.map.put(str, lArr);
            return;
        }
        Map<String, L[]> map2 = this.map;
        if (map2 != null) {
            map2.remove(str);
            if (this.map.isEmpty()) {
                this.map = null;
            }
        }
    }

    public final synchronized L[] getListeners() {
        if (this.map == null) {
            return newArray(0);
        }
        ArrayList arrayList = new ArrayList();
        EventListener[] eventListenerArr = (EventListener[]) this.map.get((Object) null);
        if (eventListenerArr != null) {
            for (EventListener add : eventListenerArr) {
                arrayList.add(add);
            }
        }
        for (Map.Entry next : this.map.entrySet()) {
            String str = (String) next.getKey();
            if (str != null) {
                for (EventListener newProxy : (EventListener[]) next.getValue()) {
                    arrayList.add(newProxy(str, newProxy));
                }
            }
        }
        return (EventListener[]) arrayList.toArray(newArray(arrayList.size()));
    }

    public final L[] getListeners(String str) {
        EventListener[] eventListenerArr;
        if (str == null || (eventListenerArr = get(str)) == null) {
            return newArray(0);
        }
        return (EventListener[]) eventListenerArr.clone();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x001d, code lost:
        return r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final synchronized boolean hasListeners(java.lang.String r4) {
        /*
            r3 = this;
            monitor-enter(r3)
            java.util.Map<java.lang.String, L[]> r0 = r3.map     // Catch:{ all -> 0x001e }
            r1 = 0
            if (r0 != 0) goto L_0x0008
            monitor-exit(r3)
            return r1
        L_0x0008:
            r2 = 0
            java.lang.Object r0 = r0.get(r2)     // Catch:{ all -> 0x001e }
            java.util.EventListener[] r0 = (java.util.EventListener[]) r0     // Catch:{ all -> 0x001e }
            if (r0 != 0) goto L_0x001b
            if (r4 == 0) goto L_0x001c
            java.util.Map<java.lang.String, L[]> r0 = r3.map     // Catch:{ all -> 0x001e }
            java.lang.Object r4 = r0.get(r4)     // Catch:{ all -> 0x001e }
            if (r4 == 0) goto L_0x001c
        L_0x001b:
            r1 = 1
        L_0x001c:
            monitor-exit(r3)
            return r1
        L_0x001e:
            r4 = move-exception
            monitor-exit(r3)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: java.beans.ChangeListenerMap.hasListeners(java.lang.String):boolean");
    }

    public final Set<Map.Entry<String, L[]>> getEntries() {
        Map<String, L[]> map2 = this.map;
        if (map2 != null) {
            return map2.entrySet();
        }
        return Collections.emptySet();
    }
}
