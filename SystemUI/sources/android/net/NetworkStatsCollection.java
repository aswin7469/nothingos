package android.net;

import android.annotation.SystemApi;
import android.net.NetworkStats;
import android.net.NetworkStatsHistory;
import android.net.connectivity.android.service.NetworkStatsCollectionKeyProto;
import android.net.connectivity.android.util.IndentingPrintWriter;
import android.net.connectivity.com.android.internal.util.FileRotator;
import android.net.connectivity.com.android.net.module.util.CollectionUtils;
import android.net.connectivity.com.android.net.module.util.NetworkStatsUtils;
import android.os.Binder;
import android.telephony.SubscriptionPlan;
import android.util.ArrayMap;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Range;
import android.util.proto.ProtoOutputStream;
import java.net.ProtocolException;
import java.p026io.BufferedInputStream;
import java.p026io.DataInput;
import java.p026io.DataInputStream;
import java.p026io.DataOutput;
import java.p026io.DataOutputStream;
import java.p026io.File;
import java.p026io.FileNotFoundException;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.p026io.PrintWriter;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import libcore.p030io.IoUtils;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public class NetworkStatsCollection implements FileRotator.Reader, FileRotator.Writer {
    private static final int FILE_MAGIC = 1095648596;
    private static final String TAG = "NetworkStatsCollection";
    private static final int VERSION_NETWORK_INIT = 1;
    private static final int VERSION_UID_INIT = 1;
    private static final int VERSION_UID_WITH_IDENT = 2;
    private static final int VERSION_UID_WITH_SET = 4;
    private static final int VERSION_UID_WITH_TAG = 3;
    private static final int VERSION_UNIFIED_INIT = 16;
    private final long mBucketDurationMillis;
    private boolean mDirty;
    private long mEndMillis;
    private long mStartMillis;
    private ArrayMap<Key, NetworkStatsHistory> mStats = new ArrayMap<>();
    private long mTotalBytes;

    public NetworkStatsCollection(long j) {
        this.mBucketDurationMillis = j;
        reset();
    }

    public void clear() {
        reset();
    }

    public void reset() {
        this.mStats.clear();
        this.mStartMillis = Long.MAX_VALUE;
        this.mEndMillis = Long.MIN_VALUE;
        this.mTotalBytes = 0;
        this.mDirty = false;
    }

    public long getStartMillis() {
        return this.mStartMillis;
    }

    public long getFirstAtomicBucketMillis() {
        long j = this.mStartMillis;
        if (j == Long.MAX_VALUE) {
            return Long.MAX_VALUE;
        }
        return j + this.mBucketDurationMillis;
    }

    public long getEndMillis() {
        return this.mEndMillis;
    }

    public long getTotalBytes() {
        return this.mTotalBytes;
    }

    public boolean isDirty() {
        return this.mDirty;
    }

    public void clearDirty() {
        this.mDirty = false;
    }

    public boolean isEmpty() {
        return this.mStartMillis == Long.MAX_VALUE && this.mEndMillis == Long.MIN_VALUE;
    }

    public long roundUp(long j) {
        if (j == Long.MIN_VALUE || j == Long.MAX_VALUE || j == -1) {
            return j;
        }
        long j2 = this.mBucketDurationMillis;
        long j3 = j % j2;
        return j3 > 0 ? (j - j3) + j2 : j;
    }

    public long roundDown(long j) {
        if (j == Long.MIN_VALUE || j == Long.MAX_VALUE || j == -1) {
            return j;
        }
        long j2 = j % this.mBucketDurationMillis;
        return j2 > 0 ? j - j2 : j;
    }

    public int[] getRelevantUids(int i) {
        return getRelevantUids(i, Binder.getCallingUid());
    }

    public int[] getRelevantUids(int i, int i2) {
        int binarySearch;
        ArrayList arrayList = new ArrayList();
        for (int i3 = 0; i3 < this.mStats.size(); i3++) {
            Key keyAt = this.mStats.keyAt(i3);
            if (NetworkStatsAccess.isAccessibleToUser(keyAt.uid, i2, i) && (binarySearch = Collections.binarySearch(arrayList, new Integer(keyAt.uid))) < 0) {
                arrayList.add(~binarySearch, Integer.valueOf(keyAt.uid));
            }
        }
        return CollectionUtils.toIntArray(arrayList);
    }

    public NetworkStatsHistory getHistory(NetworkTemplate networkTemplate, SubscriptionPlan subscriptionPlan, int i, int i2, int i3, int i4, long j, long j2, int i5, int i6) {
        long j3;
        long j4;
        long j5;
        long j6;
        long j7;
        NetworkStatsHistory networkStatsHistory;
        long j8;
        int i7;
        int i8 = i;
        int i9 = i4;
        long j9 = j;
        long j10 = j2;
        int i10 = i6;
        if (NetworkStatsAccess.isAccessibleToUser(i8, i10, i5)) {
            long j11 = this.mBucketDurationMillis;
            int constrain = (int) NetworkStatsUtils.constrain((j10 - j9) / j11, 0, 15552000000L / j11);
            NetworkStatsHistory networkStatsHistory2 = new NetworkStatsHistory(this.mBucketDurationMillis, constrain, i9);
            if (j9 == j10) {
                return networkStatsHistory2;
            }
            long dataUsageTime = subscriptionPlan != null ? subscriptionPlan.getDataUsageTime() : -1;
            if (dataUsageTime != -1) {
                Iterator<Range<ZonedDateTime>> cycleIterator = subscriptionPlan.cycleIterator();
                while (true) {
                    if (!cycleIterator.hasNext()) {
                        break;
                    }
                    Range next = cycleIterator.next();
                    j3 = ((ZonedDateTime) next.getLower()).toInstant().toEpochMilli();
                    long epochMilli = ((ZonedDateTime) next.getUpper()).toInstant().toEpochMilli();
                    if (j3 <= dataUsageTime && dataUsageTime < epochMilli) {
                        j4 = Long.min(j9, j3);
                        j5 = Long.max(j10, dataUsageTime);
                        break;
                    }
                }
            }
            j4 = j9;
            j5 = j10;
            j3 = -1;
            if (j3 != -1) {
                j3 = roundUp(j3);
                dataUsageTime = roundDown(dataUsageTime);
                j4 = roundDown(j4);
                j5 = roundUp(j5);
            }
            long j12 = dataUsageTime;
            long j13 = j4;
            long j14 = j3;
            int i11 = 0;
            while (i11 < this.mStats.size()) {
                Key keyAt = this.mStats.keyAt(i11);
                if (keyAt.uid == i8) {
                    if (NetworkStats.setMatches(i2, keyAt.set) && keyAt.tag == i3) {
                        if (templateMatches(networkTemplate, keyAt.ident)) {
                            i7 = i11;
                            networkStatsHistory2.recordHistory(this.mStats.valueAt(i11), j13, j5);
                            i11 = i7 + 1;
                        }
                    }
                }
                i7 = i11;
                i11 = i7 + 1;
            }
            if (j14 == -1) {
                return networkStatsHistory2;
            }
            NetworkStatsHistory.Entry values = networkStatsHistory2.getValues(j14, j12, (NetworkStatsHistory.Entry) null);
            if (values.rxBytes == 0 || values.txBytes == 0) {
                NetworkStatsHistory networkStatsHistory3 = networkStatsHistory2;
                long j15 = j14;
                j6 = 0;
                long j16 = j12;
                networkStatsHistory3.recordData(j15, j16, new NetworkStats.Entry(1, 0, 1, 0, 0));
                networkStatsHistory3.getValues(j15, j16, values);
            } else {
                j6 = 0;
            }
            if (values.rxBytes + values.txBytes == j6) {
                j7 = 1;
            } else {
                j7 = values.rxBytes + values.txBytes;
            }
            long j17 = values.rxBytes == j6 ? 1 : values.rxBytes;
            long j18 = values.txBytes == j6 ? 1 : values.txBytes;
            long dataUsageBytes = subscriptionPlan.getDataUsageBytes();
            long j19 = j7;
            long multiplySafeByRational = NetworkStatsUtils.multiplySafeByRational(dataUsageBytes, j17, j19);
            long multiplySafeByRational2 = NetworkStatsUtils.multiplySafeByRational(dataUsageBytes, j18, j19);
            long totalBytes = networkStatsHistory2.getTotalBytes();
            int i12 = 0;
            while (i12 < networkStatsHistory2.size()) {
                networkStatsHistory2.getValues(i12, values);
                if (values.bucketStart >= j14) {
                    NetworkStatsHistory networkStatsHistory4 = networkStatsHistory2;
                    if (values.bucketStart + values.bucketDuration <= j12) {
                        values.rxBytes = NetworkStatsUtils.multiplySafeByRational(multiplySafeByRational, values.rxBytes, j17);
                        values.txBytes = NetworkStatsUtils.multiplySafeByRational(multiplySafeByRational2, values.txBytes, j18);
                        j8 = 0;
                        values.rxPackets = 0;
                        values.txPackets = 0;
                        networkStatsHistory = networkStatsHistory4;
                        networkStatsHistory.setValues(i12, values);
                        i12++;
                        networkStatsHistory2 = networkStatsHistory;
                    } else {
                        networkStatsHistory = networkStatsHistory4;
                    }
                } else {
                    networkStatsHistory = networkStatsHistory2;
                }
                j8 = 0;
                i12++;
                networkStatsHistory2 = networkStatsHistory;
            }
            NetworkStatsHistory networkStatsHistory5 = networkStatsHistory2;
            long totalBytes2 = networkStatsHistory5.getTotalBytes() - totalBytes;
            if (totalBytes2 != j6) {
                Log.d(TAG, "Augmented network usage by " + totalBytes2 + " bytes");
            }
            NetworkStatsHistory networkStatsHistory6 = new NetworkStatsHistory(this.mBucketDurationMillis, constrain, i9);
            networkStatsHistory6.recordHistory(networkStatsHistory5, j, j2);
            return networkStatsHistory6;
        }
        throw new SecurityException("Network stats history of uid " + i8 + " is forbidden for caller " + i10);
    }

    public NetworkStats getSummary(NetworkTemplate networkTemplate, long j, long j2, int i, int i2) {
        long currentTimeMillis = System.currentTimeMillis();
        NetworkStats networkStats = new NetworkStats(j2 - j, 24);
        if (j == j2) {
            return networkStats;
        }
        NetworkStats.Entry entry = new NetworkStats.Entry();
        NetworkStatsHistory.Entry entry2 = null;
        for (int i3 = 0; i3 < this.mStats.size(); i3++) {
            Key keyAt = this.mStats.keyAt(i3);
            if (templateMatches(networkTemplate, keyAt.ident)) {
                if (NetworkStatsAccess.isAccessibleToUser(keyAt.uid, i2, i) && keyAt.set < 1000) {
                    entry2 = this.mStats.valueAt(i3).getValues(j, j2, currentTimeMillis, entry2);
                    entry.iface = NetworkStats.IFACE_ALL;
                    entry.uid = keyAt.uid;
                    entry.set = keyAt.set;
                    entry.tag = keyAt.tag;
                    entry.defaultNetwork = keyAt.ident.areAllMembersOnDefaultNetwork() ? 1 : 0;
                    entry.metered = keyAt.ident.isAnyMemberMetered() ? 1 : 0;
                    entry.roaming = keyAt.ident.isAnyMemberRoaming() ? 1 : 0;
                    entry.rxBytes = entry2.rxBytes;
                    entry.rxPackets = entry2.rxPackets;
                    entry.txBytes = entry2.txBytes;
                    entry.txPackets = entry2.txPackets;
                    entry.operations = entry2.operations;
                    if (!entry.isEmpty()) {
                        networkStats.combineValues(entry);
                    }
                }
            }
        }
        return networkStats;
    }

    public void recordData(NetworkIdentitySet networkIdentitySet, int i, int i2, int i3, long j, long j2, NetworkStats.Entry entry) {
        NetworkStatsHistory findOrCreateHistory = findOrCreateHistory(networkIdentitySet, i, i2, i3);
        findOrCreateHistory.recordData(j, j2, entry);
        noteRecordedHistory(findOrCreateHistory.getStart(), findOrCreateHistory.getEnd(), entry.txBytes + entry.rxBytes);
    }

    public void recordHistory(Key key, NetworkStatsHistory networkStatsHistory) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(networkStatsHistory);
        if (networkStatsHistory.size() != 0) {
            noteRecordedHistory(networkStatsHistory.getStart(), networkStatsHistory.getEnd(), networkStatsHistory.getTotalBytes());
            NetworkStatsHistory networkStatsHistory2 = this.mStats.get(key);
            if (networkStatsHistory2 == null) {
                networkStatsHistory2 = new NetworkStatsHistory(networkStatsHistory.getBucketDuration());
                this.mStats.put(key, networkStatsHistory2);
            }
            networkStatsHistory2.recordEntireHistory(networkStatsHistory);
        }
    }

    public void recordCollection(NetworkStatsCollection networkStatsCollection) {
        Objects.requireNonNull(networkStatsCollection);
        for (int i = 0; i < networkStatsCollection.mStats.size(); i++) {
            recordHistory(networkStatsCollection.mStats.keyAt(i), networkStatsCollection.mStats.valueAt(i));
        }
    }

    private NetworkStatsHistory findOrCreateHistory(NetworkIdentitySet networkIdentitySet, int i, int i2, int i3) {
        NetworkStatsHistory networkStatsHistory;
        Key key = new Key(networkIdentitySet, i, i2, i3);
        NetworkStatsHistory networkStatsHistory2 = this.mStats.get(key);
        if (networkStatsHistory2 == null) {
            networkStatsHistory = new NetworkStatsHistory(this.mBucketDurationMillis, 10);
        } else {
            networkStatsHistory = networkStatsHistory2.getBucketDuration() != this.mBucketDurationMillis ? new NetworkStatsHistory(networkStatsHistory2, this.mBucketDurationMillis) : null;
        }
        if (networkStatsHistory == null) {
            return networkStatsHistory2;
        }
        this.mStats.put(key, networkStatsHistory);
        return networkStatsHistory;
    }

    public void read(InputStream inputStream) throws IOException {
        read((DataInput) new DataInputStream(inputStream));
    }

    private void read(DataInput dataInput) throws IOException {
        int readInt = dataInput.readInt();
        if (readInt == FILE_MAGIC) {
            int readInt2 = dataInput.readInt();
            if (readInt2 == 16) {
                int readInt3 = dataInput.readInt();
                for (int i = 0; i < readInt3; i++) {
                    NetworkIdentitySet networkIdentitySet = new NetworkIdentitySet(dataInput);
                    int readInt4 = dataInput.readInt();
                    for (int i2 = 0; i2 < readInt4; i2++) {
                        recordHistory(new Key(networkIdentitySet, dataInput.readInt(), dataInput.readInt(), dataInput.readInt()), new NetworkStatsHistory(dataInput));
                    }
                }
                return;
            }
            throw new ProtocolException("unexpected version: " + readInt2);
        }
        throw new ProtocolException("unexpected magic: " + readInt);
    }

    public void write(OutputStream outputStream) throws IOException {
        write((DataOutput) new DataOutputStream(outputStream));
        outputStream.flush();
    }

    private void write(DataOutput dataOutput) throws IOException {
        HashMap hashMap = new HashMap();
        for (Key next : this.mStats.keySet()) {
            ArrayList arrayList = (ArrayList) hashMap.get(next.ident);
            if (arrayList == null) {
                arrayList = new ArrayList();
                hashMap.put(next.ident, arrayList);
            }
            arrayList.add(next);
        }
        dataOutput.writeInt(FILE_MAGIC);
        dataOutput.writeInt(16);
        dataOutput.writeInt(hashMap.size());
        for (NetworkIdentitySet networkIdentitySet : hashMap.keySet()) {
            ArrayList arrayList2 = (ArrayList) hashMap.get(networkIdentitySet);
            networkIdentitySet.writeToStream(dataOutput);
            dataOutput.writeInt(arrayList2.size());
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                Key key = (Key) it.next();
                dataOutput.writeInt(key.uid);
                dataOutput.writeInt(key.set);
                dataOutput.writeInt(key.tag);
                this.mStats.get(key).writeToStream(dataOutput);
            }
        }
    }

    @Deprecated
    public void readLegacyNetwork(File file) throws IOException {
        AtomicFile atomicFile = new AtomicFile(file);
        DataInputStream dataInputStream = null;
        try {
            DataInputStream dataInputStream2 = new DataInputStream(new BufferedInputStream(atomicFile.openRead()));
            try {
                int readInt = dataInputStream2.readInt();
                if (readInt == FILE_MAGIC) {
                    int readInt2 = dataInputStream2.readInt();
                    if (readInt2 == 1) {
                        int readInt3 = dataInputStream2.readInt();
                        for (int i = 0; i < readInt3; i++) {
                            recordHistory(new Key(new NetworkIdentitySet((DataInput) dataInputStream2), -1, -1, 0), new NetworkStatsHistory((DataInput) dataInputStream2));
                        }
                        IoUtils.closeQuietly((AutoCloseable) dataInputStream2);
                        return;
                    }
                    throw new ProtocolException("unexpected version: " + readInt2);
                }
                throw new ProtocolException("unexpected magic: " + readInt);
            } catch (FileNotFoundException unused) {
                dataInputStream = dataInputStream2;
                IoUtils.closeQuietly((AutoCloseable) dataInputStream);
            } catch (Throwable th) {
                th = th;
                dataInputStream = dataInputStream2;
                IoUtils.closeQuietly((AutoCloseable) dataInputStream);
                throw th;
            }
        } catch (FileNotFoundException unused2) {
            IoUtils.closeQuietly((AutoCloseable) dataInputStream);
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly((AutoCloseable) dataInputStream);
            throw th;
        }
    }

    @Deprecated
    public void readLegacyUid(File file, boolean z) throws IOException {
        AtomicFile atomicFile = new AtomicFile(file);
        DataInputStream dataInputStream = null;
        try {
            DataInputStream dataInputStream2 = new DataInputStream(new BufferedInputStream(atomicFile.openRead()));
            try {
                int readInt = dataInputStream2.readInt();
                if (readInt == FILE_MAGIC) {
                    int readInt2 = dataInputStream2.readInt();
                    if (!(readInt2 == 1 || readInt2 == 2)) {
                        if (readInt2 != 3) {
                            if (readInt2 != 4) {
                                throw new ProtocolException("unexpected version: " + readInt2);
                            }
                        }
                        int readInt3 = dataInputStream2.readInt();
                        for (int i = 0; i < readInt3; i++) {
                            NetworkIdentitySet networkIdentitySet = new NetworkIdentitySet((DataInput) dataInputStream2);
                            int readInt4 = dataInputStream2.readInt();
                            for (int i2 = 0; i2 < readInt4; i2++) {
                                int readInt5 = dataInputStream2.readInt();
                                int readInt6 = readInt2 >= 4 ? dataInputStream2.readInt() : 0;
                                int readInt7 = dataInputStream2.readInt();
                                Key key = new Key(networkIdentitySet, readInt5, readInt6, readInt7);
                                NetworkStatsHistory networkStatsHistory = new NetworkStatsHistory((DataInput) dataInputStream2);
                                if ((readInt7 == 0) != z) {
                                    recordHistory(key, networkStatsHistory);
                                }
                            }
                        }
                    }
                    IoUtils.closeQuietly((AutoCloseable) dataInputStream2);
                    return;
                }
                throw new ProtocolException("unexpected magic: " + readInt);
            } catch (FileNotFoundException unused) {
                dataInputStream = dataInputStream2;
                IoUtils.closeQuietly((AutoCloseable) dataInputStream);
            } catch (Throwable th) {
                th = th;
                dataInputStream = dataInputStream2;
                IoUtils.closeQuietly((AutoCloseable) dataInputStream);
                throw th;
            }
        } catch (FileNotFoundException unused2) {
            IoUtils.closeQuietly((AutoCloseable) dataInputStream);
        } catch (Throwable th2) {
            th = th2;
            IoUtils.closeQuietly((AutoCloseable) dataInputStream);
            throw th;
        }
    }

    public void removeUids(int[] iArr) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.mStats.keySet());
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Key key = (Key) it.next();
            if (CollectionUtils.contains(iArr, key.uid)) {
                if (key.tag == 0) {
                    findOrCreateHistory(key.ident, -4, 0, 0).recordEntireHistory(this.mStats.get(key));
                }
                this.mStats.remove(key);
                this.mDirty = true;
            }
        }
    }

    public void removeHistoryBefore(long j) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(this.mStats.keySet());
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Key key = (Key) it.next();
            NetworkStatsHistory networkStatsHistory = this.mStats.get(key);
            if (networkStatsHistory.getStart() <= j) {
                networkStatsHistory.removeBucketsStartingBefore(j);
                if (networkStatsHistory.size() == 0) {
                    this.mStats.remove(key);
                }
                this.mDirty = true;
            }
        }
    }

    private void noteRecordedHistory(long j, long j2, long j3) {
        if (j < this.mStartMillis) {
            this.mStartMillis = j;
        }
        if (j2 > this.mEndMillis) {
            this.mEndMillis = j2;
        }
        this.mTotalBytes += j3;
        this.mDirty = true;
    }

    private int estimateBuckets() {
        return (int) (Math.min(this.mEndMillis - this.mStartMillis, 3024000000L) / this.mBucketDurationMillis);
    }

    private ArrayList<Key> getSortedKeys() {
        ArrayList<Key> arrayList = new ArrayList<>();
        arrayList.addAll(this.mStats.keySet());
        Collections.sort(arrayList, new NetworkStatsCollection$$ExternalSyntheticLambda0());
        return arrayList;
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        Iterator<Key> it = getSortedKeys().iterator();
        while (it.hasNext()) {
            Key next = it.next();
            indentingPrintWriter.print("ident=");
            indentingPrintWriter.print(next.ident.toString());
            indentingPrintWriter.print(" uid=");
            indentingPrintWriter.print(next.uid);
            indentingPrintWriter.print(" set=");
            indentingPrintWriter.print(NetworkStats.setToString(next.set));
            indentingPrintWriter.print(" tag=");
            indentingPrintWriter.println(NetworkStats.tagToString(next.tag));
            indentingPrintWriter.increaseIndent();
            this.mStats.get(next).dump(indentingPrintWriter, true);
            indentingPrintWriter.decreaseIndent();
        }
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        Iterator<Key> it = getSortedKeys().iterator();
        while (it.hasNext()) {
            Key next = it.next();
            long start2 = protoOutputStream.start(2246267895809L);
            long start3 = protoOutputStream.start(1146756268033L);
            next.ident.dumpDebug(protoOutputStream, 1146756268033L);
            protoOutputStream.write(NetworkStatsCollectionKeyProto.UID, next.uid);
            protoOutputStream.write(NetworkStatsCollectionKeyProto.SET, next.set);
            protoOutputStream.write(NetworkStatsCollectionKeyProto.TAG, next.tag);
            protoOutputStream.end(start3);
            this.mStats.get(next).dumpDebug(protoOutputStream, 1146756268034L);
            protoOutputStream.end(start2);
        }
        protoOutputStream.end(start);
    }

    public void dumpCheckin(PrintWriter printWriter, long j, long j2) {
        PrintWriter printWriter2 = printWriter;
        long j3 = j;
        long j4 = j2;
        dumpCheckin(printWriter2, j3, j4, NetworkTemplate.buildTemplateMobileWildcard(), "cell");
        PrintWriter printWriter3 = printWriter;
        long j5 = j;
        long j6 = j2;
        dumpCheckin(printWriter3, j5, j6, NetworkTemplate.buildTemplateWifiWildcard(), "wifi");
        dumpCheckin(printWriter2, j3, j4, NetworkTemplate.buildTemplateEthernet(), "eth");
        dumpCheckin(printWriter3, j5, j6, NetworkTemplate.buildTemplateBluetooth(), "bt");
    }

    private void dumpCheckin(PrintWriter printWriter, long j, long j2, NetworkTemplate networkTemplate, String str) {
        PrintWriter printWriter2 = printWriter;
        ArrayMap arrayMap = new ArrayMap();
        for (int i = 0; i < this.mStats.size(); i++) {
            Key keyAt = this.mStats.keyAt(i);
            NetworkStatsHistory valueAt = this.mStats.valueAt(i);
            if (templateMatches(networkTemplate, keyAt.ident) && keyAt.set < 1000) {
                Key key = new Key(new NetworkIdentitySet(), keyAt.uid, keyAt.set, keyAt.tag);
                NetworkStatsHistory networkStatsHistory = (NetworkStatsHistory) arrayMap.get(key);
                if (networkStatsHistory == null) {
                    networkStatsHistory = new NetworkStatsHistory(valueAt.getBucketDuration());
                    arrayMap.put(key, networkStatsHistory);
                }
                networkStatsHistory.recordHistory(valueAt, j, j2);
            }
        }
        for (int i2 = 0; i2 < arrayMap.size(); i2++) {
            Key key2 = (Key) arrayMap.keyAt(i2);
            NetworkStatsHistory networkStatsHistory2 = (NetworkStatsHistory) arrayMap.valueAt(i2);
            if (networkStatsHistory2.size() == 0) {
                String str2 = str;
            } else {
                printWriter.print("c,");
                printWriter.print(str);
                printWriter.print(',');
                printWriter.print(key2.uid);
                printWriter.print(',');
                printWriter.print(NetworkStats.setToCheckinString(key2.set));
                printWriter.print(',');
                printWriter.print(key2.tag);
                printWriter.println();
                networkStatsHistory2.dumpCheckin(printWriter);
            }
        }
    }

    private static boolean templateMatches(NetworkTemplate networkTemplate, NetworkIdentitySet networkIdentitySet) {
        Iterator it = networkIdentitySet.iterator();
        while (it.hasNext()) {
            if (networkTemplate.matches((NetworkIdentity) it.next())) {
                return true;
            }
        }
        return false;
    }

    public Map<Key, NetworkStatsHistory> getEntries() {
        return new ArrayMap(this.mStats);
    }

    public static final class Builder {
        private final long mBucketDurationMillis;
        private final ArrayMap<Key, NetworkStatsHistory> mEntries = new ArrayMap<>();

        public Builder(long j) {
            this.mBucketDurationMillis = j;
        }

        public Builder addEntry(Key key, NetworkStatsHistory networkStatsHistory) {
            Objects.requireNonNull(key);
            Objects.requireNonNull(networkStatsHistory);
            List<NetworkStatsHistory.Entry> entries = networkStatsHistory.getEntries();
            NetworkStatsHistory networkStatsHistory2 = this.mEntries.get(key);
            NetworkStatsHistory.Builder builder = new NetworkStatsHistory.Builder(this.mBucketDurationMillis, entries.size() + (networkStatsHistory2 != null ? networkStatsHistory2.size() : 0));
            if (networkStatsHistory2 != null) {
                for (NetworkStatsHistory.Entry addEntry : networkStatsHistory2.getEntries()) {
                    builder.addEntry(addEntry);
                }
            }
            for (NetworkStatsHistory.Entry addEntry2 : entries) {
                builder.addEntry(addEntry2);
            }
            this.mEntries.put(key, builder.build());
            return this;
        }

        public NetworkStatsCollection build() {
            NetworkStatsCollection networkStatsCollection = new NetworkStatsCollection(this.mBucketDurationMillis);
            for (int i = 0; i < this.mEntries.size(); i++) {
                networkStatsCollection.recordHistory(this.mEntries.keyAt(i), this.mEntries.valueAt(i));
            }
            return networkStatsCollection;
        }
    }

    public static final class Key {
        public final NetworkIdentitySet ident;
        private final int mHashCode;
        public final int set;
        public final int tag;
        public final int uid;

        public Key(Set<NetworkIdentity> set2, int i, int i2, int i3) {
            this(new NetworkIdentitySet((Set<NetworkIdentity>) (Set) Objects.requireNonNull(set2)), i, i2, i3);
        }

        public Key(NetworkIdentitySet networkIdentitySet, int i, int i2, int i3) {
            this.ident = (NetworkIdentitySet) Objects.requireNonNull(networkIdentitySet);
            this.uid = i;
            this.set = i2;
            this.tag = i3;
            this.mHashCode = Objects.hash(networkIdentitySet, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
        }

        public int hashCode() {
            return this.mHashCode;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Key)) {
                return false;
            }
            Key key = (Key) obj;
            if (this.uid == key.uid && this.set == key.set && this.tag == key.tag && Objects.equals(this.ident, key.ident)) {
                return true;
            }
            return false;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:2:0x000a, code lost:
            r1 = r3.ident;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static int compare(android.net.NetworkStatsCollection.Key r2, android.net.NetworkStatsCollection.Key r3) {
            /*
                java.util.Objects.requireNonNull(r2)
                java.util.Objects.requireNonNull(r3)
                android.net.NetworkIdentitySet r0 = r2.ident
                if (r0 == 0) goto L_0x0013
                android.net.NetworkIdentitySet r1 = r3.ident
                if (r1 == 0) goto L_0x0013
                int r0 = android.net.NetworkIdentitySet.compare(r0, r1)
                goto L_0x0014
            L_0x0013:
                r0 = 0
            L_0x0014:
                if (r0 != 0) goto L_0x001e
                int r0 = r2.uid
                int r1 = r3.uid
                int r0 = java.lang.Integer.compare(r0, r1)
            L_0x001e:
                if (r0 != 0) goto L_0x0028
                int r0 = r2.set
                int r1 = r3.set
                int r0 = java.lang.Integer.compare(r0, r1)
            L_0x0028:
                if (r0 != 0) goto L_0x0032
                int r2 = r2.tag
                int r3 = r3.tag
                int r0 = java.lang.Integer.compare(r2, r3)
            L_0x0032:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.NetworkStatsCollection.Key.compare(android.net.NetworkStatsCollection$Key, android.net.NetworkStatsCollection$Key):int");
        }
    }
}
