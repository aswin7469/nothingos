package java.p026io;

import com.android.systemui.power.TemperatureController;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/* renamed from: java.io.ExpiringCache */
class ExpiringCache {
    /* access modifiers changed from: private */
    public int MAX_ENTRIES;
    private Map<String, Entry> map;
    private long millisUntilExpiration;
    private int queryCount;
    private int queryOverflow;

    /* renamed from: java.io.ExpiringCache$Entry */
    static class Entry {
        private long timestamp;
        private String val;

        Entry(long j, String str) {
            this.timestamp = j;
            this.val = str;
        }

        /* access modifiers changed from: package-private */
        public long timestamp() {
            return this.timestamp;
        }

        /* access modifiers changed from: package-private */
        public void setTimestamp(long j) {
            this.timestamp = j;
        }

        /* access modifiers changed from: package-private */
        public String val() {
            return this.val;
        }

        /* access modifiers changed from: package-private */
        public void setVal(String str) {
            this.val = str;
        }
    }

    ExpiringCache() {
        this(TemperatureController.DELAY_MS);
    }

    ExpiringCache(long j) {
        this.queryOverflow = 300;
        this.MAX_ENTRIES = 200;
        this.millisUntilExpiration = j;
        this.map = new LinkedHashMap<String, Entry>() {
            /* access modifiers changed from: protected */
            public boolean removeEldestEntry(Map.Entry<String, Entry> entry) {
                return size() > ExpiringCache.this.MAX_ENTRIES;
            }
        };
    }

    /* access modifiers changed from: package-private */
    public synchronized String get(String str) {
        int i = this.queryCount + 1;
        this.queryCount = i;
        if (i >= this.queryOverflow) {
            cleanup();
        }
        Entry entryFor = entryFor(str);
        if (entryFor == null) {
            return null;
        }
        return entryFor.val();
    }

    /* access modifiers changed from: package-private */
    public synchronized void put(String str, String str2) {
        int i = this.queryCount + 1;
        this.queryCount = i;
        if (i >= this.queryOverflow) {
            cleanup();
        }
        Entry entryFor = entryFor(str);
        if (entryFor != null) {
            entryFor.setTimestamp(System.currentTimeMillis());
            entryFor.setVal(str2);
        } else {
            this.map.put(str, new Entry(System.currentTimeMillis(), str2));
        }
    }

    /* access modifiers changed from: package-private */
    public synchronized void clear() {
        this.map.clear();
    }

    private Entry entryFor(String str) {
        Entry entry = this.map.get(str);
        if (entry == null) {
            return entry;
        }
        long currentTimeMillis = System.currentTimeMillis() - entry.timestamp();
        if (currentTimeMillis >= 0 && currentTimeMillis < this.millisUntilExpiration) {
            return entry;
        }
        this.map.remove(str);
        return null;
    }

    private void cleanup() {
        Set<String> keySet = this.map.keySet();
        int size = keySet.size();
        String[] strArr = new String[size];
        int i = 0;
        for (String str : keySet) {
            strArr[i] = str;
            i++;
        }
        for (int i2 = 0; i2 < size; i2++) {
            entryFor(strArr[i2]);
        }
        this.queryCount = 0;
    }
}
