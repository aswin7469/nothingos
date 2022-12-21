package android.net;

import android.annotation.SystemApi;
import android.net.connectivity.com.android.net.module.util.CollectionUtils;
import android.net.connectivity.com.android.net.module.util.NetworkStatsUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.p026io.CharArrayWriter;
import java.p026io.PrintWriter;
import java.p026io.Writer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import libcore.util.EmptyArray;

@SystemApi
public final class NetworkStats implements Parcelable, Iterable<Entry> {
    private static final String CLATD_INTERFACE_PREFIX = "v4-";
    public static final Parcelable.Creator<NetworkStats> CREATOR = new Parcelable.Creator<NetworkStats>() {
        public NetworkStats createFromParcel(Parcel parcel) {
            return new NetworkStats(parcel);
        }

        public NetworkStats[] newArray(int i) {
            return new NetworkStats[i];
        }
    };
    public static final int DEFAULT_NETWORK_ALL = -1;
    public static final int DEFAULT_NETWORK_NO = 0;
    public static final int DEFAULT_NETWORK_YES = 1;
    public static final String IFACE_ALL = null;
    public static final String IFACE_VT = "vt_data0";
    public static final String[] INTERFACES_ALL = null;
    private static final int IPV4V6_HEADER_DELTA = 20;
    public static final int METERED_ALL = -1;
    public static final int METERED_NO = 0;
    public static final int METERED_YES = 1;
    public static final int ROAMING_ALL = -1;
    public static final int ROAMING_NO = 0;
    public static final int ROAMING_YES = 1;
    public static final int SET_ALL = -1;
    public static final int SET_DBG_VPN_IN = 1001;
    public static final int SET_DBG_VPN_OUT = 1002;
    public static final int SET_DEBUG_START = 1000;
    public static final int SET_DEFAULT = 0;
    public static final int SET_FOREGROUND = 1;
    public static final int STATS_PER_IFACE = 0;
    public static final int STATS_PER_UID = 1;
    private static final String TAG = "NetworkStats";
    public static final int TAG_ALL = -1;
    public static final int TAG_NONE = 0;
    public static final int UID_ALL = -1;
    public static final int UID_TETHERING = -5;
    private int capacity;
    private int[] defaultNetwork;
    private long elapsedRealtime;
    private String[] iface;
    private int[] metered;
    private long[] operations;
    private int[] roaming;
    private long[] rxBytes;
    private long[] rxPackets;
    private int[] set;
    /* access modifiers changed from: private */
    public int size;
    private int[] tag;
    private long[] txBytes;
    private long[] txPackets;
    private int[] uid;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DefaultNetwork {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Meteredness {
    }

    public interface NonMonotonicObserver<C> {
        void foundNonMonotonic(NetworkStats networkStats, int i, NetworkStats networkStats2, int i2, C c);

        void foundNonMonotonic(NetworkStats networkStats, int i, C c);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Roaming {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface StatsType {
    }

    public static String defaultNetworkToString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? "UNKNOWN" : "YES" : "NO" : "ALL";
    }

    public static String meteredToString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? "UNKNOWN" : "YES" : "NO" : "ALL";
    }

    public static String roamingToString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? "UNKNOWN" : "YES" : "NO" : "ALL";
    }

    public static boolean setMatches(int i, int i2) {
        if (i == i2) {
            return true;
        }
        return i == -1 && i2 < 1000;
    }

    public static String setToCheckinString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? i != 1001 ? i != 1002 ? "unk" : "vpnout" : "vpnin" : "fg" : "def" : "all";
    }

    public static String setToString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? i != 1001 ? i != 1002 ? "UNKNOWN" : "DBG_VPN_OUT" : "DBG_VPN_IN" : "FOREGROUND" : "DEFAULT" : "ALL";
    }

    public int describeContents() {
        return 0;
    }

    @SystemApi
    public static class Entry {
        public int defaultNetwork;
        public String iface;
        public int metered;
        public long operations;
        public int roaming;
        public long rxBytes;
        public long rxPackets;
        public int set;
        public int tag;
        public long txBytes;
        public long txPackets;
        public int uid;

        public Entry() {
            this(NetworkStats.IFACE_ALL, -1, 0, 0, 0, 0, 0, 0, 0);
        }

        public Entry(long j, long j2, long j3, long j4, long j5) {
            this(NetworkStats.IFACE_ALL, -1, 0, 0, j, j2, j3, j4, j5);
        }

        public Entry(String str, int i, int i2, int i3, long j, long j2, long j3, long j4, long j5) {
            this(str, i, i2, i3, 0, 0, 0, j, j2, j3, j4, j5);
        }

        public Entry(String str, int i, int i2, int i3, int i4, int i5, int i6, long j, long j2, long j3, long j4, long j5) {
            this.iface = str;
            this.uid = i;
            this.set = i2;
            this.tag = i3;
            this.metered = i4;
            this.roaming = i5;
            this.defaultNetwork = i6;
            this.rxBytes = j;
            this.rxPackets = j2;
            this.txBytes = j3;
            this.txPackets = j4;
            this.operations = j5;
        }

        public boolean isNegative() {
            return this.rxBytes < 0 || this.rxPackets < 0 || this.txBytes < 0 || this.txPackets < 0 || this.operations < 0;
        }

        public boolean isEmpty() {
            return this.rxBytes == 0 && this.rxPackets == 0 && this.txBytes == 0 && this.txPackets == 0 && this.operations == 0;
        }

        public void add(Entry entry) {
            this.rxBytes += entry.rxBytes;
            this.rxPackets += entry.rxPackets;
            this.txBytes += entry.txBytes;
            this.txPackets += entry.txPackets;
            this.operations += entry.operations;
        }

        public String getIface() {
            return this.iface;
        }

        public int getUid() {
            return this.uid;
        }

        public int getSet() {
            return this.set;
        }

        public int getTag() {
            return this.tag;
        }

        public int getMetered() {
            return this.metered;
        }

        public int getRoaming() {
            return this.roaming;
        }

        public int getDefaultNetwork() {
            return this.defaultNetwork;
        }

        public long getRxBytes() {
            return this.rxBytes;
        }

        public long getRxPackets() {
            return this.rxPackets;
        }

        public long getTxBytes() {
            return this.txBytes;
        }

        public long getTxPackets() {
            return this.txPackets;
        }

        public long getOperations() {
            return this.operations;
        }

        public String toString() {
            return "iface=" + this.iface + " uid=" + this.uid + " set=" + NetworkStats.setToString(this.set) + " tag=" + NetworkStats.tagToString(this.tag) + " metered=" + NetworkStats.meteredToString(this.metered) + " roaming=" + NetworkStats.roamingToString(this.roaming) + " defaultNetwork=" + NetworkStats.defaultNetworkToString(this.defaultNetwork) + " rxBytes=" + this.rxBytes + " rxPackets=" + this.rxPackets + " txBytes=" + this.txBytes + " txPackets=" + this.txPackets + " operations=" + this.operations;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            if (this.uid == entry.uid && this.set == entry.set && this.tag == entry.tag && this.metered == entry.metered && this.roaming == entry.roaming && this.defaultNetwork == entry.defaultNetwork && this.rxBytes == entry.rxBytes && this.rxPackets == entry.rxPackets && this.txBytes == entry.txBytes && this.txPackets == entry.txPackets && this.operations == entry.operations && TextUtils.equals(this.iface, entry.iface)) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return Objects.hash(Integer.valueOf(this.uid), Integer.valueOf(this.set), Integer.valueOf(this.tag), Integer.valueOf(this.metered), Integer.valueOf(this.roaming), Integer.valueOf(this.defaultNetwork), this.iface);
        }
    }

    public NetworkStats(long j, int i) {
        this.elapsedRealtime = j;
        this.size = 0;
        if (i > 0) {
            this.capacity = i;
            this.iface = new String[i];
            this.uid = new int[i];
            this.set = new int[i];
            this.tag = new int[i];
            this.metered = new int[i];
            this.roaming = new int[i];
            this.defaultNetwork = new int[i];
            this.rxBytes = new long[i];
            this.rxPackets = new long[i];
            this.txBytes = new long[i];
            this.txPackets = new long[i];
            this.operations = new long[i];
            return;
        }
        clear();
    }

    public NetworkStats(Parcel parcel) {
        this.elapsedRealtime = parcel.readLong();
        this.size = parcel.readInt();
        this.capacity = parcel.readInt();
        this.iface = parcel.createStringArray();
        this.uid = parcel.createIntArray();
        this.set = parcel.createIntArray();
        this.tag = parcel.createIntArray();
        this.metered = parcel.createIntArray();
        this.roaming = parcel.createIntArray();
        this.defaultNetwork = parcel.createIntArray();
        this.rxBytes = parcel.createLongArray();
        this.rxPackets = parcel.createLongArray();
        this.txBytes = parcel.createLongArray();
        this.txPackets = parcel.createLongArray();
        this.operations = parcel.createLongArray();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.elapsedRealtime);
        parcel.writeInt(this.size);
        parcel.writeInt(this.capacity);
        parcel.writeStringArray(this.iface);
        parcel.writeIntArray(this.uid);
        parcel.writeIntArray(this.set);
        parcel.writeIntArray(this.tag);
        parcel.writeIntArray(this.metered);
        parcel.writeIntArray(this.roaming);
        parcel.writeIntArray(this.defaultNetwork);
        parcel.writeLongArray(this.rxBytes);
        parcel.writeLongArray(this.rxPackets);
        parcel.writeLongArray(this.txBytes);
        parcel.writeLongArray(this.txPackets);
        parcel.writeLongArray(this.operations);
    }

    public NetworkStats clone() {
        NetworkStats networkStats = new NetworkStats(this.elapsedRealtime, this.size);
        Entry entry = null;
        for (int i = 0; i < this.size; i++) {
            entry = getValues(i, entry);
            networkStats.insertEntry(entry);
        }
        return networkStats;
    }

    public void clear() {
        this.capacity = 0;
        this.iface = EmptyArray.STRING;
        this.uid = EmptyArray.INT;
        this.set = EmptyArray.INT;
        this.tag = EmptyArray.INT;
        this.metered = EmptyArray.INT;
        this.roaming = EmptyArray.INT;
        this.defaultNetwork = EmptyArray.INT;
        this.rxBytes = EmptyArray.LONG;
        this.rxPackets = EmptyArray.LONG;
        this.txBytes = EmptyArray.LONG;
        this.txPackets = EmptyArray.LONG;
        this.operations = EmptyArray.LONG;
    }

    public NetworkStats insertEntry(String str, long j, long j2, long j3, long j4) {
        return insertEntry(str, -1, 0, 0, j, j2, j3, j4, 0);
    }

    public NetworkStats insertEntry(String str, int i, int i2, int i3, long j, long j2, long j3, long j4, long j5) {
        return insertEntry(new Entry(str, i, i2, i3, j, j2, j3, j4, j5));
    }

    public NetworkStats insertEntry(String str, int i, int i2, int i3, int i4, int i5, int i6, long j, long j2, long j3, long j4, long j5) {
        Entry entry = r0;
        Entry entry2 = new Entry(str, i, i2, i3, i4, i5, i6, j, j2, j3, j4, j5);
        return insertEntry(entry);
    }

    public NetworkStats insertEntry(Entry entry) {
        int i = this.size;
        if (i >= this.capacity) {
            int max = (Math.max(i, 10) * 3) / 2;
            this.iface = (String[]) Arrays.copyOf((T[]) this.iface, max);
            this.uid = Arrays.copyOf(this.uid, max);
            this.set = Arrays.copyOf(this.set, max);
            this.tag = Arrays.copyOf(this.tag, max);
            this.metered = Arrays.copyOf(this.metered, max);
            this.roaming = Arrays.copyOf(this.roaming, max);
            this.defaultNetwork = Arrays.copyOf(this.defaultNetwork, max);
            this.rxBytes = Arrays.copyOf(this.rxBytes, max);
            this.rxPackets = Arrays.copyOf(this.rxPackets, max);
            this.txBytes = Arrays.copyOf(this.txBytes, max);
            this.txPackets = Arrays.copyOf(this.txPackets, max);
            this.operations = Arrays.copyOf(this.operations, max);
            this.capacity = max;
        }
        setValues(this.size, entry);
        this.size++;
        return this;
    }

    private void setValues(int i, Entry entry) {
        this.iface[i] = entry.iface;
        this.uid[i] = entry.uid;
        this.set[i] = entry.set;
        this.tag[i] = entry.tag;
        this.metered[i] = entry.metered;
        this.roaming[i] = entry.roaming;
        this.defaultNetwork[i] = entry.defaultNetwork;
        this.rxBytes[i] = entry.rxBytes;
        this.rxPackets[i] = entry.rxPackets;
        this.txBytes[i] = entry.txBytes;
        this.txPackets[i] = entry.txPackets;
        this.operations[i] = entry.operations;
    }

    @SystemApi
    public Iterator<Entry> iterator() {
        return new Iterator<Entry>() {
            int mIndex = 0;

            public boolean hasNext() {
                return this.mIndex < NetworkStats.this.size;
            }

            public Entry next() {
                NetworkStats networkStats = NetworkStats.this;
                int i = this.mIndex;
                this.mIndex = i + 1;
                return networkStats.getValues(i, (Entry) null);
            }
        };
    }

    public Entry getValues(int i, Entry entry) {
        if (entry == null) {
            entry = new Entry();
        }
        entry.iface = this.iface[i];
        entry.uid = this.uid[i];
        entry.set = this.set[i];
        entry.tag = this.tag[i];
        entry.metered = this.metered[i];
        entry.roaming = this.roaming[i];
        entry.defaultNetwork = this.defaultNetwork[i];
        entry.rxBytes = this.rxBytes[i];
        entry.rxPackets = this.rxPackets[i];
        entry.txBytes = this.txBytes[i];
        entry.txPackets = this.txPackets[i];
        entry.operations = this.operations[i];
        return entry;
    }

    private void maybeCopyEntry(int i, int i2) {
        if (i != i2) {
            String[] strArr = this.iface;
            strArr[i] = strArr[i2];
            int[] iArr = this.uid;
            iArr[i] = iArr[i2];
            int[] iArr2 = this.set;
            iArr2[i] = iArr2[i2];
            int[] iArr3 = this.tag;
            iArr3[i] = iArr3[i2];
            int[] iArr4 = this.metered;
            iArr4[i] = iArr4[i2];
            int[] iArr5 = this.roaming;
            iArr5[i] = iArr5[i2];
            int[] iArr6 = this.defaultNetwork;
            iArr6[i] = iArr6[i2];
            long[] jArr = this.rxBytes;
            jArr[i] = jArr[i2];
            long[] jArr2 = this.rxPackets;
            jArr2[i] = jArr2[i2];
            long[] jArr3 = this.txBytes;
            jArr3[i] = jArr3[i2];
            long[] jArr4 = this.txPackets;
            jArr4[i] = jArr4[i2];
            long[] jArr5 = this.operations;
            jArr5[i] = jArr5[i2];
        }
    }

    public long getElapsedRealtime() {
        return this.elapsedRealtime;
    }

    public void setElapsedRealtime(long j) {
        this.elapsedRealtime = j;
    }

    public long getElapsedRealtimeAge() {
        return SystemClock.elapsedRealtime() - this.elapsedRealtime;
    }

    public int size() {
        return this.size;
    }

    public int internalSize() {
        return this.capacity;
    }

    @Deprecated
    public NetworkStats combineValues(String str, int i, int i2, long j, long j2, long j3, long j4, long j5) {
        return combineValues(str, i, 0, i2, j, j2, j3, j4, j5);
    }

    public NetworkStats combineValues(String str, int i, int i2, int i3, long j, long j2, long j3, long j4, long j5) {
        return combineValues(new Entry(str, i, i2, i3, j, j2, j3, j4, j5));
    }

    public NetworkStats combineValues(Entry entry) {
        int findIndex = findIndex(entry.iface, entry.uid, entry.set, entry.tag, entry.metered, entry.roaming, entry.defaultNetwork);
        if (findIndex == -1) {
            insertEntry(entry);
        } else {
            long[] jArr = this.rxBytes;
            jArr[findIndex] = jArr[findIndex] + entry.rxBytes;
            long[] jArr2 = this.rxPackets;
            jArr2[findIndex] = jArr2[findIndex] + entry.rxPackets;
            long[] jArr3 = this.txBytes;
            jArr3[findIndex] = jArr3[findIndex] + entry.txBytes;
            long[] jArr4 = this.txPackets;
            jArr4[findIndex] = jArr4[findIndex] + entry.txPackets;
            long[] jArr5 = this.operations;
            jArr5[findIndex] = jArr5[findIndex] + entry.operations;
        }
        return this;
    }

    public NetworkStats addEntry(Entry entry) {
        return clone().combineValues(entry);
    }

    public NetworkStats add(NetworkStats networkStats) {
        NetworkStats clone = clone();
        clone.combineAllValues(networkStats);
        return clone;
    }

    public void combineAllValues(NetworkStats networkStats) {
        Entry entry = null;
        for (int i = 0; i < networkStats.size; i++) {
            entry = networkStats.getValues(i, entry);
            combineValues(entry);
        }
    }

    public int findIndex(String str, int i, int i2, int i3, int i4, int i5, int i6) {
        for (int i7 = 0; i7 < this.size; i7++) {
            if (i == this.uid[i7] && i2 == this.set[i7] && i3 == this.tag[i7] && i4 == this.metered[i7] && i5 == this.roaming[i7] && i6 == this.defaultNetwork[i7] && Objects.equals(str, this.iface[i7])) {
                return i7;
            }
        }
        return -1;
    }

    public int findIndexHinted(String str, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        int i8;
        int i9 = 0;
        while (true) {
            int i10 = this.size;
            if (i9 >= i10) {
                return -1;
            }
            int i11 = i9 / 2;
            if (i9 % 2 == 0) {
                i8 = (i11 + i7) % i10;
            } else {
                i8 = (((i10 + i7) - i11) - 1) % i10;
            }
            if (i == this.uid[i8] && i2 == this.set[i8] && i3 == this.tag[i8] && i4 == this.metered[i8] && i5 == this.roaming[i8] && i6 == this.defaultNetwork[i8] && Objects.equals(str, this.iface[i8])) {
                return i8;
            }
            i9++;
        }
    }

    public void spliceOperationsFrom(NetworkStats networkStats) {
        for (int i = 0; i < this.size; i++) {
            int findIndex = networkStats.findIndex(this.iface[i], this.uid[i], this.set[i], this.tag[i], this.metered[i], this.roaming[i], this.defaultNetwork[i]);
            if (findIndex == -1) {
                this.operations[i] = 0;
            } else {
                this.operations[i] = networkStats.operations[findIndex];
            }
        }
    }

    public String[] getUniqueIfaces() {
        HashSet hashSet = new HashSet();
        for (String str : this.iface) {
            if (str != IFACE_ALL) {
                hashSet.add(str);
            }
        }
        return (String[]) hashSet.toArray(new String[hashSet.size()]);
    }

    public int[] getUniqueUids() {
        SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
        for (int put : this.uid) {
            sparseBooleanArray.put(put, true);
        }
        int size2 = sparseBooleanArray.size();
        int[] iArr = new int[size2];
        for (int i = 0; i < size2; i++) {
            iArr[i] = sparseBooleanArray.keyAt(i);
        }
        return iArr;
    }

    public long getTotalBytes() {
        Entry total = getTotal((Entry) null);
        return total.rxBytes + total.txBytes;
    }

    public Entry getTotal(Entry entry) {
        return getTotal(entry, (HashSet<String>) null, -1, false);
    }

    public Entry getTotal(Entry entry, int i) {
        return getTotal(entry, (HashSet<String>) null, i, false);
    }

    public Entry getTotal(Entry entry, HashSet<String> hashSet) {
        return getTotal(entry, hashSet, -1, false);
    }

    public Entry getTotalIncludingTags(Entry entry) {
        return getTotal(entry, (HashSet<String>) null, -1, true);
    }

    private Entry getTotal(Entry entry, HashSet<String> hashSet, int i, boolean z) {
        if (entry == null) {
            entry = new Entry();
        }
        entry.iface = IFACE_ALL;
        entry.uid = i;
        entry.set = -1;
        entry.tag = 0;
        entry.metered = -1;
        entry.roaming = -1;
        entry.defaultNetwork = -1;
        entry.rxBytes = 0;
        entry.rxPackets = 0;
        entry.txBytes = 0;
        entry.txPackets = 0;
        entry.operations = 0;
        for (int i2 = 0; i2 < this.size; i2++) {
            boolean z2 = true;
            boolean z3 = i == -1 || i == this.uid[i2];
            if (hashSet != null && !hashSet.contains(this.iface[i2])) {
                z2 = false;
            }
            if (z3 && z2 && (this.tag[i2] == 0 || z)) {
                entry.rxBytes += this.rxBytes[i2];
                entry.rxPackets += this.rxPackets[i2];
                entry.txBytes += this.txBytes[i2];
                entry.txPackets += this.txPackets[i2];
                entry.operations += this.operations[i2];
            }
        }
        return entry;
    }

    public long getTotalPackets() {
        long j = 0;
        for (int i = this.size - 1; i >= 0; i--) {
            j += this.rxPackets[i] + this.txPackets[i];
        }
        return j;
    }

    public NetworkStats subtract(NetworkStats networkStats) {
        return subtract(this, networkStats, (NonMonotonicObserver) null, (Object) null);
    }

    public static <C> NetworkStats subtract(NetworkStats networkStats, NetworkStats networkStats2, NonMonotonicObserver<C> nonMonotonicObserver, C c) {
        return subtract(networkStats, networkStats2, nonMonotonicObserver, c, (NetworkStats) null);
    }

    public static <C> NetworkStats subtract(NetworkStats networkStats, NetworkStats networkStats2, NonMonotonicObserver<C> nonMonotonicObserver, C c, NetworkStats networkStats3) {
        NetworkStats networkStats4;
        NetworkStats networkStats5;
        int i;
        Entry entry;
        long j;
        NetworkStats networkStats6 = networkStats;
        NetworkStats networkStats7 = networkStats2;
        NetworkStats networkStats8 = networkStats3;
        long j2 = networkStats6.elapsedRealtime - networkStats7.elapsedRealtime;
        if (j2 < 0) {
            if (nonMonotonicObserver != null) {
                nonMonotonicObserver.foundNonMonotonic(networkStats, -1, networkStats2, -1, c);
            }
            j2 = 0;
        }
        Entry entry2 = new Entry();
        if (networkStats8 == null || networkStats8.capacity < networkStats6.size) {
            networkStats4 = new NetworkStats(j2, networkStats6.size);
        } else {
            networkStats8.size = 0;
            networkStats8.elapsedRealtime = j2;
            networkStats4 = networkStats8;
        }
        int i2 = 0;
        while (i2 < networkStats6.size) {
            entry2.iface = networkStats6.iface[i2];
            entry2.uid = networkStats6.uid[i2];
            entry2.set = networkStats6.set[i2];
            entry2.tag = networkStats6.tag[i2];
            entry2.metered = networkStats6.metered[i2];
            entry2.roaming = networkStats6.roaming[i2];
            entry2.defaultNetwork = networkStats6.defaultNetwork[i2];
            entry2.rxBytes = networkStats6.rxBytes[i2];
            entry2.rxPackets = networkStats6.rxPackets[i2];
            entry2.txBytes = networkStats6.txBytes[i2];
            entry2.txPackets = networkStats6.txPackets[i2];
            entry2.operations = networkStats6.operations[i2];
            NetworkStats networkStats9 = networkStats7;
            int findIndexHinted = networkStats2.findIndexHinted(entry2.iface, entry2.uid, entry2.set, entry2.tag, entry2.metered, entry2.roaming, entry2.defaultNetwork, i2);
            if (findIndexHinted != -1) {
                entry2.rxBytes -= networkStats9.rxBytes[findIndexHinted];
                entry2.rxPackets -= networkStats9.rxPackets[findIndexHinted];
                entry2.txBytes -= networkStats9.txBytes[findIndexHinted];
                entry2.txPackets -= networkStats9.txPackets[findIndexHinted];
                entry2.operations -= networkStats9.operations[findIndexHinted];
            }
            if (entry2.isNegative()) {
                if (nonMonotonicObserver != null) {
                    i = i2;
                    networkStats5 = networkStats4;
                    int i3 = findIndexHinted;
                    entry = entry2;
                    nonMonotonicObserver.foundNonMonotonic(networkStats, i2, networkStats2, i3, c);
                } else {
                    i = i2;
                    networkStats5 = networkStats4;
                    entry = entry2;
                }
                j = 0;
                entry.rxBytes = Math.max(entry.rxBytes, 0);
                entry.rxPackets = Math.max(entry.rxPackets, 0);
                entry.txBytes = Math.max(entry.txBytes, 0);
                entry.txPackets = Math.max(entry.txPackets, 0);
                entry.operations = Math.max(entry.operations, 0);
            } else {
                i = i2;
                networkStats5 = networkStats4;
                entry = entry2;
                j = 0;
            }
            networkStats5.insertEntry(entry);
            networkStats7 = networkStats2;
            long j3 = j;
            entry2 = entry;
            networkStats4 = networkStats5;
            i2 = i + 1;
        }
        return networkStats4;
    }

    public static void apply464xlatAdjustments(NetworkStats networkStats, NetworkStats networkStats2, Map<String, String> map) {
        Entry entry = null;
        for (int i = 0; i < networkStats2.size; i++) {
            entry = networkStats2.getValues(i, entry);
            if (!(entry == null || entry.iface == null || !entry.iface.startsWith("v4-"))) {
                entry.rxBytes += entry.rxPackets * 20;
                entry.txBytes += entry.txPackets * 20;
                networkStats2.setValues(i, entry);
            }
        }
    }

    public void apply464xlatAdjustments(Map<String, String> map) {
        apply464xlatAdjustments(this, this, map);
    }

    public NetworkStats groupedByIface() {
        NetworkStats networkStats = new NetworkStats(this.elapsedRealtime, 10);
        Entry entry = new Entry();
        entry.uid = -1;
        entry.set = -1;
        entry.tag = 0;
        entry.metered = -1;
        entry.roaming = -1;
        entry.defaultNetwork = -1;
        entry.operations = 0;
        for (int i = 0; i < this.size; i++) {
            if (this.tag[i] == 0) {
                entry.iface = this.iface[i];
                entry.rxBytes = this.rxBytes[i];
                entry.rxPackets = this.rxPackets[i];
                entry.txBytes = this.txBytes[i];
                entry.txPackets = this.txPackets[i];
                networkStats.combineValues(entry);
            }
        }
        return networkStats;
    }

    public NetworkStats groupedByUid() {
        NetworkStats networkStats = new NetworkStats(this.elapsedRealtime, 10);
        Entry entry = new Entry();
        entry.iface = IFACE_ALL;
        entry.set = -1;
        entry.tag = 0;
        entry.metered = -1;
        entry.roaming = -1;
        entry.defaultNetwork = -1;
        for (int i = 0; i < this.size; i++) {
            if (this.tag[i] == 0) {
                entry.uid = this.uid[i];
                entry.rxBytes = this.rxBytes[i];
                entry.rxPackets = this.rxPackets[i];
                entry.txBytes = this.txBytes[i];
                entry.txPackets = this.txPackets[i];
                entry.operations = this.operations[i];
                networkStats.combineValues(entry);
            }
        }
        return networkStats;
    }

    static /* synthetic */ boolean lambda$removeUids$0(int[] iArr, Entry entry) {
        return !CollectionUtils.contains(iArr, entry.uid);
    }

    public void removeUids(int[] iArr) {
        filter(new NetworkStats$$ExternalSyntheticLambda1(iArr));
    }

    public NetworkStats removeEmptyEntries() {
        NetworkStats clone = clone();
        clone.filter(new NetworkStats$$ExternalSyntheticLambda2());
        return clone;
    }

    static /* synthetic */ boolean lambda$removeEmptyEntries$1(Entry entry) {
        return (entry.rxBytes == 0 && entry.rxPackets == 0 && entry.txBytes == 0 && entry.txPackets == 0 && entry.operations == 0) ? false : true;
    }

    public void clearInterfaces() {
        for (int i = 0; i < this.size; i++) {
            this.iface[i] = null;
        }
    }

    public void filter(int i, String[] strArr, int i2) {
        if (i != -1 || i2 != -1 || strArr != INTERFACES_ALL) {
            filter(new NetworkStats$$ExternalSyntheticLambda0(i, i2, strArr));
        }
    }

    static /* synthetic */ boolean lambda$filter$2(int i, int i2, String[] strArr, Entry entry) {
        return (i == -1 || i == entry.uid) && (i2 == -1 || i2 == entry.tag) && (strArr == INTERFACES_ALL || CollectionUtils.contains((T[]) strArr, entry.iface));
    }

    static /* synthetic */ boolean lambda$filterDebugEntries$3(Entry entry) {
        return entry.set < 1000;
    }

    public void filterDebugEntries() {
        filter(new NetworkStats$$ExternalSyntheticLambda3());
    }

    private void filter(Predicate<Entry> predicate) {
        Entry entry = new Entry();
        int i = 0;
        for (int i2 = 0; i2 < this.size; i2++) {
            entry = getValues(i2, entry);
            if (predicate.test(entry)) {
                if (i != i2) {
                    setValues(i, entry);
                }
                i++;
            }
        }
        this.size = i;
    }

    public void dump(String str, PrintWriter printWriter) {
        printWriter.print(str);
        printWriter.print("NetworkStats: elapsedRealtime=");
        printWriter.println(this.elapsedRealtime);
        for (int i = 0; i < this.size; i++) {
            printWriter.print(str);
            printWriter.print("  [");
            printWriter.print(i);
            printWriter.print(NavigationBarInflaterView.SIZE_MOD_END);
            printWriter.print(" iface=");
            printWriter.print(this.iface[i]);
            printWriter.print(" uid=");
            printWriter.print(this.uid[i]);
            printWriter.print(" set=");
            printWriter.print(setToString(this.set[i]));
            printWriter.print(" tag=");
            printWriter.print(tagToString(this.tag[i]));
            printWriter.print(" metered=");
            printWriter.print(meteredToString(this.metered[i]));
            printWriter.print(" roaming=");
            printWriter.print(roamingToString(this.roaming[i]));
            printWriter.print(" defaultNetwork=");
            printWriter.print(defaultNetworkToString(this.defaultNetwork[i]));
            printWriter.print(" rxBytes=");
            printWriter.print(this.rxBytes[i]);
            printWriter.print(" rxPackets=");
            printWriter.print(this.rxPackets[i]);
            printWriter.print(" txBytes=");
            printWriter.print(this.txBytes[i]);
            printWriter.print(" txPackets=");
            printWriter.print(this.txPackets[i]);
            printWriter.print(" operations=");
            printWriter.println(this.operations[i]);
        }
    }

    public static String tagToString(int i) {
        return "0x" + Integer.toHexString(i);
    }

    public String toString() {
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        dump("", new PrintWriter((Writer) charArrayWriter));
        return charArrayWriter.toString();
    }

    public void migrateTun(int i, String str, List<String> list) {
        Entry entry = new Entry();
        int size2 = list.size();
        Entry[] entryArr = new Entry[size2];
        Entry entry2 = new Entry();
        for (int i2 = 0; i2 < size2; i2++) {
            entryArr[i2] = new Entry();
        }
        int i3 = i;
        String str2 = str;
        List<String> list2 = list;
        Entry entry3 = entry;
        Entry[] entryArr2 = entryArr;
        Entry entry4 = entry2;
        tunAdjustmentInit(i3, str2, list2, entry3, entryArr2, entry4);
        deductTrafficFromVpnApp(i, list, addTrafficToApplications(i3, str2, list2, entry3, entryArr2, entry4));
    }

    private void tunAdjustmentInit(int i, String str, List<String> list, Entry entry, Entry[] entryArr, Entry entry2) {
        Entry entry3 = new Entry();
        int i2 = 0;
        while (i2 < this.size) {
            getValues(i2, entry3);
            if (entry3.uid == -1) {
                throw new IllegalStateException("Cannot adjust VPN accounting on an iface aggregated NetworkStats.");
            } else if (entry3.set == 1001 || entry3.set == 1002) {
                throw new IllegalStateException("Cannot adjust VPN accounting on a NetworkStats containing SET_DBG_VPN_*");
            } else {
                if (entry3.tag == 0) {
                    if (i == 1000) {
                        if (str.equals(entry3.iface)) {
                            entry.add(entry3);
                            entry2.add(entry3);
                            if (entryArr.length > 0) {
                                entryArr[0].add(entry3);
                            }
                        }
                    } else if (entry3.uid == i) {
                        int i3 = 0;
                        while (true) {
                            if (i3 >= list.size()) {
                                break;
                            } else if (Objects.equals(list.get(i3), entry3.iface)) {
                                entryArr[i3].add(entry3);
                                entry2.add(entry3);
                                break;
                            } else {
                                i3++;
                            }
                        }
                    } else if (str.equals(entry3.iface)) {
                        entry.add(entry3);
                    }
                }
                i2++;
            }
        }
    }

    private Entry[] addTrafficToApplications(int i, String str, List<String> list, Entry entry, Entry[] entryArr, Entry entry2) {
        int i2;
        int i3;
        Entry entry3;
        int i4;
        Entry entry4;
        long j;
        long j2;
        long j3;
        long j4;
        long j5;
        long j6;
        long j7;
        int i5;
        int i6;
        int i7;
        int i8 = i;
        Entry entry5 = entry;
        Entry entry6 = entry2;
        Entry[] entryArr2 = new Entry[list.size()];
        for (int i9 = 0; i9 < list.size(); i9++) {
            entryArr2[i9] = new Entry();
        }
        Entry entry7 = new Entry();
        int i10 = this.size;
        int i11 = 0;
        while (i11 < i10) {
            if (Objects.equals(this.iface[i11], str) && ((i4 = this.uid[i11]) != i8 || i8 == 1000)) {
                entry7.uid = i4;
                entry7.tag = this.tag[i11];
                entry7.metered = this.metered[i11];
                entry7.roaming = this.roaming[i11];
                entry7.defaultNetwork = this.defaultNetwork[i11];
                if (entry5.rxBytes > 0) {
                    entry4 = entry7;
                    j = Math.min(this.rxBytes[i11], NetworkStatsUtils.multiplySafeByRational(entry6.rxBytes, this.rxBytes[i11], entry5.rxBytes));
                } else {
                    entry4 = entry7;
                    j = 0;
                }
                if (entry5.rxPackets > 0) {
                    j2 = Math.min(this.rxPackets[i11], NetworkStatsUtils.multiplySafeByRational(entry6.rxPackets, this.rxPackets[i11], entry5.rxPackets));
                } else {
                    j2 = 0;
                }
                if (entry5.txBytes > 0) {
                    j3 = Math.min(this.txBytes[i11], NetworkStatsUtils.multiplySafeByRational(entry6.txBytes, this.txBytes[i11], entry5.txBytes));
                } else {
                    j3 = 0;
                }
                if (entry5.txPackets > 0) {
                    j4 = j3;
                    j5 = Math.min(this.txPackets[i11], NetworkStatsUtils.multiplySafeByRational(entry6.txPackets, this.txPackets[i11], entry5.txPackets));
                } else {
                    j4 = j3;
                    j5 = 0;
                }
                if (entry5.operations > 0) {
                    j6 = j5;
                    j7 = Math.min(this.operations[i11], NetworkStatsUtils.multiplySafeByRational(entry6.operations, this.operations[i11], entry5.operations));
                } else {
                    j6 = j5;
                    j7 = 0;
                }
                int i12 = 0;
                while (i12 < list.size()) {
                    Entry entry8 = entry4;
                    entry8.iface = list.get(i12);
                    entry8.rxBytes = 0;
                    entry8.set = this.set[i11];
                    if (entry6.rxBytes > 0) {
                        i6 = i10;
                        i5 = i11;
                        entry8.rxBytes = NetworkStatsUtils.multiplySafeByRational(j, entryArr[i12].rxBytes, entry6.rxBytes);
                    } else {
                        i6 = i10;
                        i5 = i11;
                    }
                    entry8.rxPackets = 0;
                    if (entry6.rxPackets > 0) {
                        entry8.rxPackets = NetworkStatsUtils.multiplySafeByRational(j2, entryArr[i12].rxPackets, entry6.rxPackets);
                    }
                    entry8.txBytes = 0;
                    if (entry6.txBytes > 0) {
                        i7 = i12;
                        entry8.txBytes = NetworkStatsUtils.multiplySafeByRational(j4, entryArr[i12].txBytes, entry6.txBytes);
                    } else {
                        i7 = i12;
                    }
                    entry8.txPackets = 0;
                    if (entry6.txPackets > 0) {
                        entry8.txPackets = NetworkStatsUtils.multiplySafeByRational(j6, entryArr[i7].txPackets, entry6.txPackets);
                    }
                    entry8.operations = 0;
                    if (entry6.operations > 0) {
                        entry8.operations = NetworkStatsUtils.multiplySafeByRational(j7, entryArr[i7].operations, entry6.operations);
                    }
                    combineValues(entry8);
                    if (this.tag[i5] == 0) {
                        entryArr2[i7].add(entry8);
                        entry8.set = 1001;
                        combineValues(entry8);
                    }
                    i12 = i7 + 1;
                    i10 = i6;
                    i11 = i5;
                    entry4 = entry8;
                    int i13 = i;
                }
                i2 = i11;
                entry3 = entry4;
                i3 = i10;
            } else {
                entry3 = entry7;
                i3 = i10;
                i2 = i11;
            }
            i11 = i2 + 1;
            entry7 = entry3;
            i10 = i3;
            i8 = i;
        }
        return entryArr2;
    }

    private void deductTrafficFromVpnApp(int i, List<String> list, Entry[] entryArr) {
        if (i != 1000) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                entryArr[i2].uid = i;
                entryArr[i2].set = 1002;
                entryArr[i2].tag = 0;
                entryArr[i2].iface = list.get(i2);
                entryArr[i2].metered = -1;
                entryArr[i2].roaming = -1;
                entryArr[i2].defaultNetwork = -1;
                combineValues(entryArr[i2]);
                int findIndex = findIndex(list.get(i2), i, 0, 0, 0, 0, 0);
                if (findIndex != -1) {
                    tunSubtract(findIndex, this, entryArr[i2]);
                }
                int findIndex2 = findIndex(list.get(i2), i, 1, 0, 0, 0, 0);
                if (findIndex2 != -1) {
                    tunSubtract(findIndex2, this, entryArr[i2]);
                }
            }
        }
    }

    private static void tunSubtract(int i, NetworkStats networkStats, Entry entry) {
        long min = Math.min(networkStats.rxBytes[i], entry.rxBytes);
        long[] jArr = networkStats.rxBytes;
        jArr[i] = jArr[i] - min;
        entry.rxBytes -= min;
        long min2 = Math.min(networkStats.rxPackets[i], entry.rxPackets);
        long[] jArr2 = networkStats.rxPackets;
        jArr2[i] = jArr2[i] - min2;
        entry.rxPackets -= min2;
        long min3 = Math.min(networkStats.txBytes[i], entry.txBytes);
        long[] jArr3 = networkStats.txBytes;
        jArr3[i] = jArr3[i] - min3;
        entry.txBytes -= min3;
        long min4 = Math.min(networkStats.txPackets[i], entry.txPackets);
        long[] jArr4 = networkStats.txPackets;
        jArr4[i] = jArr4[i] - min4;
        entry.txPackets -= min4;
    }
}
