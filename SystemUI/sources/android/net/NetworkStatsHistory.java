package android.net;

import android.annotation.SystemApi;
import android.net.NetworkStats;
import android.net.connectivity.android.service.NetworkStatsHistoryBucketProto;
import android.net.connectivity.android.util.IndentingPrintWriter;
import android.net.connectivity.com.android.net.module.util.CollectionUtils;
import android.net.connectivity.com.android.net.module.util.NetworkStatsUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.proto.ProtoOutputStream;
import java.net.ProtocolException;
import java.p026io.CharArrayWriter;
import java.p026io.DataInput;
import java.p026io.DataOutput;
import java.p026io.IOException;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import libcore.util.EmptyArray;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class NetworkStatsHistory implements Parcelable {
    public static final Parcelable.Creator<NetworkStatsHistory> CREATOR = new Parcelable.Creator<NetworkStatsHistory>() {
        public NetworkStatsHistory createFromParcel(Parcel parcel) {
            return new NetworkStatsHistory(parcel);
        }

        public NetworkStatsHistory[] newArray(int i) {
            return new NetworkStatsHistory[i];
        }
    };
    public static final int FIELD_ACTIVE_TIME = 1;
    public static final int FIELD_ALL = -1;
    public static final int FIELD_OPERATIONS = 32;
    public static final int FIELD_RX_BYTES = 2;
    public static final int FIELD_RX_PACKETS = 4;
    public static final int FIELD_TX_BYTES = 8;
    public static final int FIELD_TX_PACKETS = 16;
    private static final int VERSION_ADD_ACTIVE = 3;
    private static final int VERSION_ADD_PACKETS = 2;
    private static final int VERSION_INIT = 1;
    private long[] activeTime;
    private int bucketCount;
    private long bucketDuration;
    private long[] bucketStart;
    private long[] operations;
    private long[] rxBytes;
    private long[] rxPackets;
    private long totalBytes;
    private long[] txBytes;
    private long[] txPackets;

    public int describeContents() {
        return 0;
    }

    public NetworkStatsHistory(long j, long[] jArr, long[] jArr2, long[] jArr3, long[] jArr4, long[] jArr5, long[] jArr6, long[] jArr7, int i, long j2) {
        this.bucketDuration = j;
        this.bucketStart = jArr;
        this.activeTime = jArr2;
        this.rxBytes = jArr3;
        this.rxPackets = jArr4;
        this.txBytes = jArr5;
        this.txPackets = jArr6;
        this.operations = jArr7;
        this.bucketCount = i;
        this.totalBytes = j2;
    }

    public static final class Entry {
        public static final long UNKNOWN = -1;
        public long activeTime;
        public long bucketDuration;
        public long bucketStart;
        public long operations;
        public long rxBytes;
        public long rxPackets;
        public long txBytes;
        public long txPackets;

        Entry() {
        }

        public Entry(long j, long j2, long j3, long j4, long j5, long j6, long j7) {
            this.bucketStart = j;
            this.activeTime = j2;
            this.rxBytes = j3;
            this.rxPackets = j4;
            this.txBytes = j5;
            this.txPackets = j6;
            this.operations = j7;
        }

        public long getBucketStart() {
            return this.bucketStart;
        }

        public long getActiveTime() {
            return this.activeTime;
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

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj.getClass() != getClass()) {
                return false;
            }
            Entry entry = (Entry) obj;
            if (this.bucketStart == entry.bucketStart && this.activeTime == entry.activeTime && this.rxBytes == entry.rxBytes && this.rxPackets == entry.rxPackets && this.txBytes == entry.txBytes && this.txPackets == entry.txPackets && this.operations == entry.operations) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (int) ((this.bucketStart * 2) + (this.activeTime * 3) + (this.rxBytes * 5) + (this.rxPackets * 7) + (this.txBytes * 11) + (this.txPackets * 13) + (this.operations * 17));
        }

        public String toString() {
            return "Entry{bucketStart=" + this.bucketStart + ", activeTime=" + this.activeTime + ", rxBytes=" + this.rxBytes + ", rxPackets=" + this.rxPackets + ", txBytes=" + this.txBytes + ", txPackets=" + this.txPackets + ", operations=" + this.operations + "}";
        }

        public Entry plus(Entry entry, long j) {
            Entry entry2 = entry;
            long j2 = this.bucketStart;
            if (j2 == entry2.bucketStart) {
                long min = Math.min(this.activeTime + entry2.activeTime, j);
                long j3 = this.rxBytes + entry2.rxBytes;
                long j4 = this.rxPackets + entry2.rxPackets;
                return new Entry(j2, min, j3, j4, this.txBytes + entry2.txBytes, this.txPackets + entry2.txPackets, this.operations + entry2.operations);
            }
            throw new IllegalArgumentException("bucketStart " + this.bucketStart + " is not equal to " + entry2.bucketStart);
        }
    }

    public NetworkStatsHistory(long j) {
        this(j, 10, -1);
    }

    public NetworkStatsHistory(long j, int i) {
        this(j, i, -1);
    }

    public NetworkStatsHistory(long j, int i, int i2) {
        this.bucketDuration = j;
        this.bucketStart = new long[i];
        if ((i2 & 1) != 0) {
            this.activeTime = new long[i];
        }
        if ((i2 & 2) != 0) {
            this.rxBytes = new long[i];
        }
        if ((i2 & 4) != 0) {
            this.rxPackets = new long[i];
        }
        if ((i2 & 8) != 0) {
            this.txBytes = new long[i];
        }
        if ((i2 & 16) != 0) {
            this.txPackets = new long[i];
        }
        if ((i2 & 32) != 0) {
            this.operations = new long[i];
        }
        this.bucketCount = 0;
        this.totalBytes = 0;
    }

    public NetworkStatsHistory(NetworkStatsHistory networkStatsHistory, long j) {
        this(j, networkStatsHistory.estimateResizeBuckets(j));
        recordEntireHistory(networkStatsHistory);
    }

    public NetworkStatsHistory(Parcel parcel) {
        this.bucketDuration = parcel.readLong();
        this.bucketStart = ParcelUtils.readLongArray(parcel);
        this.activeTime = ParcelUtils.readLongArray(parcel);
        this.rxBytes = ParcelUtils.readLongArray(parcel);
        this.rxPackets = ParcelUtils.readLongArray(parcel);
        this.txBytes = ParcelUtils.readLongArray(parcel);
        this.txPackets = ParcelUtils.readLongArray(parcel);
        this.operations = ParcelUtils.readLongArray(parcel);
        this.bucketCount = this.bucketStart.length;
        this.totalBytes = parcel.readLong();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.bucketDuration);
        ParcelUtils.writeLongArray(parcel, this.bucketStart, this.bucketCount);
        ParcelUtils.writeLongArray(parcel, this.activeTime, this.bucketCount);
        ParcelUtils.writeLongArray(parcel, this.rxBytes, this.bucketCount);
        ParcelUtils.writeLongArray(parcel, this.rxPackets, this.bucketCount);
        ParcelUtils.writeLongArray(parcel, this.txBytes, this.bucketCount);
        ParcelUtils.writeLongArray(parcel, this.txPackets, this.bucketCount);
        ParcelUtils.writeLongArray(parcel, this.operations, this.bucketCount);
        parcel.writeLong(this.totalBytes);
    }

    public NetworkStatsHistory(DataInput dataInput) throws IOException {
        long[] jArr;
        int readInt = dataInput.readInt();
        if (readInt == 1) {
            this.bucketDuration = dataInput.readLong();
            this.bucketStart = DataStreamUtils.readFullLongArray(dataInput);
            this.rxBytes = DataStreamUtils.readFullLongArray(dataInput);
            this.rxPackets = new long[this.bucketStart.length];
            this.txBytes = DataStreamUtils.readFullLongArray(dataInput);
            long[] jArr2 = this.bucketStart;
            this.txPackets = new long[jArr2.length];
            this.operations = new long[jArr2.length];
            this.bucketCount = jArr2.length;
            this.totalBytes = CollectionUtils.total(this.rxBytes) + CollectionUtils.total(this.txBytes);
        } else if (readInt == 2 || readInt == 3) {
            this.bucketDuration = dataInput.readLong();
            long[] readVarLongArray = DataStreamUtils.readVarLongArray(dataInput);
            this.bucketStart = readVarLongArray;
            if (readInt >= 3) {
                jArr = DataStreamUtils.readVarLongArray(dataInput);
            } else {
                jArr = new long[readVarLongArray.length];
            }
            this.activeTime = jArr;
            this.rxBytes = DataStreamUtils.readVarLongArray(dataInput);
            this.rxPackets = DataStreamUtils.readVarLongArray(dataInput);
            this.txBytes = DataStreamUtils.readVarLongArray(dataInput);
            this.txPackets = DataStreamUtils.readVarLongArray(dataInput);
            this.operations = DataStreamUtils.readVarLongArray(dataInput);
            this.bucketCount = this.bucketStart.length;
            this.totalBytes = CollectionUtils.total(this.rxBytes) + CollectionUtils.total(this.txBytes);
        } else {
            throw new ProtocolException("unexpected version: " + readInt);
        }
        int length = this.bucketStart.length;
        int i = this.bucketCount;
        if (length != i || this.rxBytes.length != i || this.rxPackets.length != i || this.txBytes.length != i || this.txPackets.length != i || this.operations.length != i) {
            throw new ProtocolException("Mismatched history lengths");
        }
    }

    public void writeToStream(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(3);
        dataOutput.writeLong(this.bucketDuration);
        DataStreamUtils.writeVarLongArray(dataOutput, this.bucketStart, this.bucketCount);
        DataStreamUtils.writeVarLongArray(dataOutput, this.activeTime, this.bucketCount);
        DataStreamUtils.writeVarLongArray(dataOutput, this.rxBytes, this.bucketCount);
        DataStreamUtils.writeVarLongArray(dataOutput, this.rxPackets, this.bucketCount);
        DataStreamUtils.writeVarLongArray(dataOutput, this.txBytes, this.bucketCount);
        DataStreamUtils.writeVarLongArray(dataOutput, this.txPackets, this.bucketCount);
        DataStreamUtils.writeVarLongArray(dataOutput, this.operations, this.bucketCount);
    }

    public int size() {
        return this.bucketCount;
    }

    public long getBucketDuration() {
        return this.bucketDuration;
    }

    public long getStart() {
        if (this.bucketCount > 0) {
            return this.bucketStart[0];
        }
        return Long.MAX_VALUE;
    }

    public long getEnd() {
        int i = this.bucketCount;
        if (i > 0) {
            return this.bucketStart[i - 1] + this.bucketDuration;
        }
        return Long.MIN_VALUE;
    }

    public long getTotalBytes() {
        return this.totalBytes;
    }

    public int getIndexBefore(long j) {
        int binarySearch = Arrays.binarySearch(this.bucketStart, 0, this.bucketCount, j);
        return NetworkStatsUtils.constrain(binarySearch < 0 ? (~binarySearch) - 1 : binarySearch - 1, 0, this.bucketCount - 1);
    }

    public int getIndexAfter(long j) {
        int binarySearch = Arrays.binarySearch(this.bucketStart, 0, this.bucketCount, j);
        return NetworkStatsUtils.constrain(binarySearch < 0 ? ~binarySearch : binarySearch + 1, 0, this.bucketCount - 1);
    }

    public Entry getValues(int i, Entry entry) {
        if (entry == null) {
            entry = new Entry();
        }
        entry.bucketStart = this.bucketStart[i];
        entry.bucketDuration = this.bucketDuration;
        entry.activeTime = getLong(this.activeTime, i, -1);
        entry.rxBytes = getLong(this.rxBytes, i, -1);
        entry.rxPackets = getLong(this.rxPackets, i, -1);
        entry.txBytes = getLong(this.txBytes, i, -1);
        entry.txPackets = getLong(this.txPackets, i, -1);
        entry.operations = getLong(this.operations, i, -1);
        return entry;
    }

    public List<Entry> getEntries() {
        ArrayList arrayList = new ArrayList(size());
        for (int i = 0; i < size(); i++) {
            arrayList.add(getValues(i, (Entry) null));
        }
        return arrayList;
    }

    public void setValues(int i, Entry entry) {
        long[] jArr = this.rxBytes;
        if (jArr != null) {
            this.totalBytes -= jArr[i];
        }
        long[] jArr2 = this.txBytes;
        if (jArr2 != null) {
            this.totalBytes -= jArr2[i];
        }
        this.bucketStart[i] = entry.bucketStart;
        setLong(this.activeTime, i, entry.activeTime);
        setLong(this.rxBytes, i, entry.rxBytes);
        setLong(this.rxPackets, i, entry.rxPackets);
        setLong(this.txBytes, i, entry.txBytes);
        setLong(this.txPackets, i, entry.txPackets);
        setLong(this.operations, i, entry.operations);
        long[] jArr3 = this.rxBytes;
        if (jArr3 != null) {
            this.totalBytes += jArr3[i];
        }
        long[] jArr4 = this.txBytes;
        if (jArr4 != null) {
            this.totalBytes += jArr4[i];
        }
    }

    @Deprecated
    public void recordData(long j, long j2, long j3, long j4) {
        recordData(j, j2, new NetworkStats.Entry(NetworkStats.IFACE_ALL, -1, 0, 0, j3, 0, j4, 0, 0));
    }

    public void recordData(long j, long j2, NetworkStats.Entry entry) {
        int i;
        long j3 = j;
        long j4 = j2;
        NetworkStats.Entry entry2 = entry;
        long j5 = entry2.rxBytes;
        long j6 = entry2.rxPackets;
        long j7 = entry2.txBytes;
        long j8 = entry2.txPackets;
        long j9 = entry2.operations;
        if (entry.isNegative()) {
            throw new IllegalArgumentException("tried recording negative data");
        } else if (!entry.isEmpty()) {
            ensureBuckets(j, j2);
            long j10 = j5;
            if (this.bucketCount != 0) {
                long j11 = j7;
                long j12 = j8;
                long j13 = j9;
                int indexAfter = getIndexAfter(j4);
                long j14 = j4 - j3;
                long j15 = j10;
                long j16 = j6;
                while (indexAfter >= 0) {
                    long j17 = this.bucketStart[indexAfter];
                    long j18 = this.bucketDuration + j17;
                    if (j18 < j3) {
                        break;
                    }
                    if (j17 <= j4) {
                        long min = Math.min(j18, j4) - Math.max(j17, j3);
                        if (min > 0) {
                            long multiplySafeByRational = NetworkStatsUtils.multiplySafeByRational(j15, min, j14);
                            i = indexAfter;
                            long multiplySafeByRational2 = NetworkStatsUtils.multiplySafeByRational(j16, min, j14);
                            long multiplySafeByRational3 = NetworkStatsUtils.multiplySafeByRational(j11, min, j14);
                            long multiplySafeByRational4 = NetworkStatsUtils.multiplySafeByRational(j13, min, j14);
                            long j19 = min;
                            addLong(this.activeTime, i, j19);
                            addLong(this.rxBytes, i, multiplySafeByRational);
                            j15 -= multiplySafeByRational;
                            addLong(this.rxPackets, i, multiplySafeByRational2);
                            j16 -= multiplySafeByRational2;
                            long j20 = multiplySafeByRational3;
                            addLong(this.txBytes, i, j20);
                            j11 -= j20;
                            long multiplySafeByRational5 = NetworkStatsUtils.multiplySafeByRational(j12, min, j14);
                            addLong(this.txPackets, i, multiplySafeByRational5);
                            j12 -= multiplySafeByRational5;
                            addLong(this.operations, i, multiplySafeByRational4);
                            j13 -= multiplySafeByRational4;
                            j14 -= j19;
                            indexAfter = i - 1;
                            j3 = j;
                            j4 = j2;
                            NetworkStats.Entry entry3 = entry;
                        }
                    }
                    i = indexAfter;
                    indexAfter = i - 1;
                    j3 = j;
                    j4 = j2;
                    NetworkStats.Entry entry32 = entry;
                }
                NetworkStats.Entry entry4 = entry;
                this.totalBytes += entry4.rxBytes + entry4.txBytes;
            }
        }
    }

    public void recordEntireHistory(NetworkStatsHistory networkStatsHistory) {
        recordHistory(networkStatsHistory, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public void recordHistory(NetworkStatsHistory networkStatsHistory, long j, long j2) {
        NetworkStats.Entry entry;
        NetworkStatsHistory networkStatsHistory2 = networkStatsHistory;
        NetworkStats.Entry entry2 = r1;
        NetworkStats.Entry entry3 = new NetworkStats.Entry(NetworkStats.IFACE_ALL, -1, 0, 0, 0, 0, 0, 0, 0);
        int i = 0;
        while (i < networkStatsHistory2.bucketCount) {
            long j3 = networkStatsHistory2.bucketStart[i];
            long j4 = networkStatsHistory2.bucketDuration + j3;
            if (j3 < j || j4 > j2) {
                entry = entry2;
            } else {
                entry = entry2;
                entry.rxBytes = getLong(networkStatsHistory2.rxBytes, i, 0);
                entry.rxPackets = getLong(networkStatsHistory2.rxPackets, i, 0);
                entry.txBytes = getLong(networkStatsHistory2.txBytes, i, 0);
                entry.txPackets = getLong(networkStatsHistory2.txPackets, i, 0);
                entry.operations = getLong(networkStatsHistory2.operations, i, 0);
                recordData(j3, j4, entry);
            }
            i++;
            entry2 = entry;
        }
    }

    private void ensureBuckets(long j, long j2) {
        long j3 = this.bucketDuration;
        long j4 = j - (j % j3);
        long j5 = j2 + ((j3 - (j2 % j3)) % j3);
        while (j4 < j5) {
            int binarySearch = Arrays.binarySearch(this.bucketStart, 0, this.bucketCount, j4);
            if (binarySearch < 0) {
                insertBucket(~binarySearch, j4);
            }
            j4 += this.bucketDuration;
        }
    }

    private void insertBucket(int i, long j) {
        int i2 = this.bucketCount;
        long[] jArr = this.bucketStart;
        if (i2 >= jArr.length) {
            int max = (Math.max(jArr.length, 10) * 3) / 2;
            this.bucketStart = Arrays.copyOf(this.bucketStart, max);
            long[] jArr2 = this.activeTime;
            if (jArr2 != null) {
                this.activeTime = Arrays.copyOf(jArr2, max);
            }
            long[] jArr3 = this.rxBytes;
            if (jArr3 != null) {
                this.rxBytes = Arrays.copyOf(jArr3, max);
            }
            long[] jArr4 = this.rxPackets;
            if (jArr4 != null) {
                this.rxPackets = Arrays.copyOf(jArr4, max);
            }
            long[] jArr5 = this.txBytes;
            if (jArr5 != null) {
                this.txBytes = Arrays.copyOf(jArr5, max);
            }
            long[] jArr6 = this.txPackets;
            if (jArr6 != null) {
                this.txPackets = Arrays.copyOf(jArr6, max);
            }
            long[] jArr7 = this.operations;
            if (jArr7 != null) {
                this.operations = Arrays.copyOf(jArr7, max);
            }
        }
        int i3 = this.bucketCount;
        if (i < i3) {
            int i4 = i + 1;
            int i5 = i3 - i;
            long[] jArr8 = this.bucketStart;
            System.arraycopy((Object) jArr8, i, (Object) jArr8, i4, i5);
            long[] jArr9 = this.activeTime;
            if (jArr9 != null) {
                System.arraycopy((Object) jArr9, i, (Object) jArr9, i4, i5);
            }
            long[] jArr10 = this.rxBytes;
            if (jArr10 != null) {
                System.arraycopy((Object) jArr10, i, (Object) jArr10, i4, i5);
            }
            long[] jArr11 = this.rxPackets;
            if (jArr11 != null) {
                System.arraycopy((Object) jArr11, i, (Object) jArr11, i4, i5);
            }
            long[] jArr12 = this.txBytes;
            if (jArr12 != null) {
                System.arraycopy((Object) jArr12, i, (Object) jArr12, i4, i5);
            }
            long[] jArr13 = this.txPackets;
            if (jArr13 != null) {
                System.arraycopy((Object) jArr13, i, (Object) jArr13, i4, i5);
            }
            long[] jArr14 = this.operations;
            if (jArr14 != null) {
                System.arraycopy((Object) jArr14, i, (Object) jArr14, i4, i5);
            }
        }
        this.bucketStart[i] = j;
        setLong(this.activeTime, i, 0);
        setLong(this.rxBytes, i, 0);
        setLong(this.rxPackets, i, 0);
        setLong(this.txBytes, i, 0);
        setLong(this.txPackets, i, 0);
        setLong(this.operations, i, 0);
        this.bucketCount++;
    }

    public void clear() {
        this.bucketStart = EmptyArray.LONG;
        if (this.activeTime != null) {
            this.activeTime = EmptyArray.LONG;
        }
        if (this.rxBytes != null) {
            this.rxBytes = EmptyArray.LONG;
        }
        if (this.rxPackets != null) {
            this.rxPackets = EmptyArray.LONG;
        }
        if (this.txBytes != null) {
            this.txBytes = EmptyArray.LONG;
        }
        if (this.txPackets != null) {
            this.txPackets = EmptyArray.LONG;
        }
        if (this.operations != null) {
            this.operations = EmptyArray.LONG;
        }
        this.bucketCount = 0;
        this.totalBytes = 0;
    }

    public void removeBucketsStartingBefore(long j) {
        int i = 0;
        while (i < this.bucketCount && this.bucketStart[i] < j) {
            i++;
        }
        if (i > 0) {
            long[] jArr = this.bucketStart;
            int length = jArr.length;
            this.bucketStart = Arrays.copyOfRange(jArr, i, length);
            long[] jArr2 = this.activeTime;
            if (jArr2 != null) {
                this.activeTime = Arrays.copyOfRange(jArr2, i, length);
            }
            long[] jArr3 = this.rxBytes;
            if (jArr3 != null) {
                this.rxBytes = Arrays.copyOfRange(jArr3, i, length);
            }
            long[] jArr4 = this.rxPackets;
            if (jArr4 != null) {
                this.rxPackets = Arrays.copyOfRange(jArr4, i, length);
            }
            long[] jArr5 = this.txBytes;
            if (jArr5 != null) {
                this.txBytes = Arrays.copyOfRange(jArr5, i, length);
            }
            long[] jArr6 = this.txPackets;
            if (jArr6 != null) {
                this.txPackets = Arrays.copyOfRange(jArr6, i, length);
            }
            long[] jArr7 = this.operations;
            if (jArr7 != null) {
                this.operations = Arrays.copyOfRange(jArr7, i, length);
            }
            this.bucketCount -= i;
            this.totalBytes = 0;
            long[] jArr8 = this.rxBytes;
            if (jArr8 != null) {
                this.totalBytes = 0 + CollectionUtils.total(jArr8);
            }
            long[] jArr9 = this.txBytes;
            if (jArr9 != null) {
                this.totalBytes += CollectionUtils.total(jArr9);
            }
        }
    }

    public Entry getValues(long j, long j2, Entry entry) {
        return getValues(j, j2, Long.MAX_VALUE, entry);
    }

    public Entry getValues(long j, long j2, long j3, Entry entry) {
        long j4 = j;
        long j5 = j2;
        Entry entry2 = entry != null ? entry : new Entry();
        entry2.bucketDuration = j5 - j4;
        entry2.bucketStart = j4;
        long j6 = -1;
        entry2.activeTime = this.activeTime != null ? 0 : -1;
        entry2.rxBytes = this.rxBytes != null ? 0 : -1;
        entry2.rxPackets = this.rxPackets != null ? 0 : -1;
        entry2.txBytes = this.txBytes != null ? 0 : -1;
        entry2.txPackets = this.txPackets != null ? 0 : -1;
        if (this.operations != null) {
            j6 = 0;
        }
        entry2.operations = j6;
        if (this.bucketCount == 0) {
            return entry2;
        }
        for (int indexAfter = getIndexAfter(j5); indexAfter >= 0; indexAfter--) {
            long j7 = this.bucketStart[indexAfter];
            long j8 = this.bucketDuration + j7;
            if (j8 <= j4) {
                break;
            }
            if (j7 < j5) {
                if (j8 > j3) {
                    j8 = j3;
                }
                long j9 = j8 - j7;
                if (j9 > 0) {
                    if (j8 >= j5) {
                        j8 = j5;
                    }
                    if (j7 <= j4) {
                        j7 = j4;
                    }
                    long j10 = j8 - j7;
                    if (j10 > 0) {
                        if (this.activeTime != null) {
                            entry2.activeTime += NetworkStatsUtils.multiplySafeByRational(this.activeTime[indexAfter], j10, j9);
                        }
                        if (this.rxBytes != null) {
                            entry2.rxBytes += NetworkStatsUtils.multiplySafeByRational(this.rxBytes[indexAfter], j10, j9);
                        }
                        if (this.rxPackets != null) {
                            entry2.rxPackets += NetworkStatsUtils.multiplySafeByRational(this.rxPackets[indexAfter], j10, j9);
                        }
                        if (this.txBytes != null) {
                            entry2.txBytes += NetworkStatsUtils.multiplySafeByRational(this.txBytes[indexAfter], j10, j9);
                        }
                        if (this.txPackets != null) {
                            entry2.txPackets += NetworkStatsUtils.multiplySafeByRational(this.txPackets[indexAfter], j10, j9);
                        }
                        if (this.operations != null) {
                            entry2.operations += NetworkStatsUtils.multiplySafeByRational(this.operations[indexAfter], j10, j9);
                        }
                    }
                }
            }
        }
        return entry2;
    }

    @Deprecated
    public void generateRandom(long j, long j2, long j3) {
        Random random = new Random();
        float nextFloat = random.nextFloat();
        float f = (float) j3;
        long j4 = (long) (f * nextFloat);
        long j5 = (long) (f * (1.0f - nextFloat));
        generateRandom(j, j2, j4, j4 / 1024, j5, j5 / 1024, j4 / 2048, random);
    }

    @Deprecated
    public void generateRandom(long j, long j2, long j3, long j4, long j5, long j6, long j7, Random random) {
        long j8 = j2;
        Random random2 = random;
        ensureBuckets(j, j2);
        NetworkStats.Entry entry = r3;
        NetworkStats.Entry entry2 = new NetworkStats.Entry(NetworkStats.IFACE_ALL, -1, 0, 0, 0, 0, 0, 0, 0);
        long j9 = j3;
        long j10 = j4;
        long j11 = j5;
        long j12 = j6;
        long j13 = j7;
        while (true) {
            if (j9 > 1024 || j10 > 128 || j11 > 1024 || j12 > 128 || j13 > 32) {
                long randomLong = randomLong(random2, j, j8);
                long randomLong2 = randomLong(random2, 0, (j8 - randomLong) / 2) + randomLong;
                long j14 = randomLong;
                NetworkStats.Entry entry3 = entry;
                entry3.rxBytes = randomLong(random2, 0, j9);
                entry3.rxPackets = randomLong(random2, 0, j10);
                entry3.txBytes = randomLong(random2, 0, j11);
                entry3.txPackets = randomLong(random2, 0, j12);
                entry3.operations = randomLong(random2, 0, j13);
                j9 -= entry3.rxBytes;
                j10 -= entry3.rxPackets;
                j11 -= entry3.txBytes;
                j12 -= entry3.txPackets;
                j13 -= entry3.operations;
                recordData(j14, randomLong2, entry3);
                j8 = j2;
            } else {
                return;
            }
        }
    }

    public static long randomLong(Random random, long j, long j2) {
        return (long) (((float) j) + (random.nextFloat() * ((float) (j2 - j))));
    }

    public boolean intersects(long j, long j2) {
        long start = getStart();
        long end = getEnd();
        if (j >= start && j <= end) {
            return true;
        }
        if (j2 >= start && j2 <= end) {
            return true;
        }
        if (start < j || start > j2) {
            return end >= j && end <= j2;
        }
        return true;
    }

    public void dump(IndentingPrintWriter indentingPrintWriter, boolean z) {
        indentingPrintWriter.print("NetworkStatsHistory: bucketDuration=");
        indentingPrintWriter.println(this.bucketDuration / 1000);
        indentingPrintWriter.increaseIndent();
        int i = 0;
        if (!z) {
            i = Math.max(0, this.bucketCount - 32);
        }
        if (i > 0) {
            indentingPrintWriter.print("(omitting ");
            indentingPrintWriter.print(i);
            indentingPrintWriter.println(" buckets)");
        }
        while (i < this.bucketCount) {
            indentingPrintWriter.print("st=");
            indentingPrintWriter.print(this.bucketStart[i] / 1000);
            if (this.rxBytes != null) {
                indentingPrintWriter.print(" rb=");
                indentingPrintWriter.print(this.rxBytes[i]);
            }
            if (this.rxPackets != null) {
                indentingPrintWriter.print(" rp=");
                indentingPrintWriter.print(this.rxPackets[i]);
            }
            if (this.txBytes != null) {
                indentingPrintWriter.print(" tb=");
                indentingPrintWriter.print(this.txBytes[i]);
            }
            if (this.txPackets != null) {
                indentingPrintWriter.print(" tp=");
                indentingPrintWriter.print(this.txPackets[i]);
            }
            if (this.operations != null) {
                indentingPrintWriter.print(" op=");
                indentingPrintWriter.print(this.operations[i]);
            }
            indentingPrintWriter.println();
            i++;
        }
        indentingPrintWriter.decreaseIndent();
    }

    public void dumpCheckin(PrintWriter printWriter) {
        printWriter.print("d,");
        printWriter.print(this.bucketDuration / 1000);
        printWriter.println();
        for (int i = 0; i < this.bucketCount; i++) {
            printWriter.print("b,");
            printWriter.print(this.bucketStart[i] / 1000);
            printWriter.print(',');
            long[] jArr = this.rxBytes;
            if (jArr != null) {
                printWriter.print(jArr[i]);
            } else {
                printWriter.print("*");
            }
            printWriter.print(',');
            long[] jArr2 = this.rxPackets;
            if (jArr2 != null) {
                printWriter.print(jArr2[i]);
            } else {
                printWriter.print("*");
            }
            printWriter.print(',');
            long[] jArr3 = this.txBytes;
            if (jArr3 != null) {
                printWriter.print(jArr3[i]);
            } else {
                printWriter.print("*");
            }
            printWriter.print(',');
            long[] jArr4 = this.txPackets;
            if (jArr4 != null) {
                printWriter.print(jArr4[i]);
            } else {
                printWriter.print("*");
            }
            printWriter.print(',');
            long[] jArr5 = this.operations;
            if (jArr5 != null) {
                printWriter.print(jArr5[i]);
            } else {
                printWriter.print("*");
            }
            printWriter.println();
        }
    }

    public void dumpDebug(ProtoOutputStream protoOutputStream, long j) {
        long start = protoOutputStream.start(j);
        protoOutputStream.write(1112396529665L, this.bucketDuration);
        for (int i = 0; i < this.bucketCount; i++) {
            long start2 = protoOutputStream.start(2246267895810L);
            protoOutputStream.write(1112396529665L, this.bucketStart[i]);
            dumpDebug(protoOutputStream, NetworkStatsHistoryBucketProto.RX_BYTES, this.rxBytes, i);
            dumpDebug(protoOutputStream, NetworkStatsHistoryBucketProto.RX_PACKETS, this.rxPackets, i);
            dumpDebug(protoOutputStream, NetworkStatsHistoryBucketProto.TX_BYTES, this.txBytes, i);
            dumpDebug(protoOutputStream, NetworkStatsHistoryBucketProto.TX_PACKETS, this.txPackets, i);
            dumpDebug(protoOutputStream, NetworkStatsHistoryBucketProto.OPERATIONS, this.operations, i);
            protoOutputStream.end(start2);
        }
        protoOutputStream.end(start);
    }

    private static void dumpDebug(ProtoOutputStream protoOutputStream, long j, long[] jArr, int i) {
        if (jArr != null) {
            protoOutputStream.write(j, jArr[i]);
        }
    }

    public String toString() {
        CharArrayWriter charArrayWriter = new CharArrayWriter();
        dump(new IndentingPrintWriter(charArrayWriter, "  "), false);
        return charArrayWriter.toString();
    }

    public boolean isSameAs(NetworkStatsHistory networkStatsHistory) {
        return this.bucketCount == networkStatsHistory.bucketCount && Arrays.equals(this.bucketStart, networkStatsHistory.bucketStart) && Arrays.equals(this.rxBytes, networkStatsHistory.rxBytes) && Arrays.equals(this.rxPackets, networkStatsHistory.rxPackets) && Arrays.equals(this.txBytes, networkStatsHistory.txBytes) && Arrays.equals(this.txPackets, networkStatsHistory.txPackets) && Arrays.equals(this.operations, networkStatsHistory.operations) && this.totalBytes == networkStatsHistory.totalBytes;
    }

    private static long getLong(long[] jArr, int i, long j) {
        return jArr != null ? jArr[i] : j;
    }

    private static void setLong(long[] jArr, int i, long j) {
        if (jArr != null) {
            jArr[i] = j;
        }
    }

    private static void addLong(long[] jArr, int i, long j) {
        if (jArr != null) {
            jArr[i] = jArr[i] + j;
        }
    }

    public int estimateResizeBuckets(long j) {
        return (int) ((((long) size()) * getBucketDuration()) / j);
    }

    public static class DataStreamUtils {
        @Deprecated
        public static long[] readFullLongArray(DataInput dataInput) throws IOException {
            int readInt = dataInput.readInt();
            if (readInt >= 0) {
                long[] jArr = new long[readInt];
                for (int i = 0; i < readInt; i++) {
                    jArr[i] = dataInput.readLong();
                }
                return jArr;
            }
            throw new ProtocolException("negative array size");
        }

        public static long readVarLong(DataInput dataInput) throws IOException {
            long j = 0;
            for (int i = 0; i < 64; i += 7) {
                byte readByte = dataInput.readByte();
                j |= ((long) (readByte & Byte.MAX_VALUE)) << i;
                if ((readByte & 128) == 0) {
                    return j;
                }
            }
            throw new ProtocolException("malformed long");
        }

        public static void writeVarLong(DataOutput dataOutput, long j) throws IOException {
            while ((-128 & j) != 0) {
                dataOutput.writeByte((((int) j) & 127) | 128);
                j >>>= 7;
            }
            dataOutput.writeByte((int) j);
        }

        public static long[] readVarLongArray(DataInput dataInput) throws IOException {
            int readInt = dataInput.readInt();
            if (readInt == -1) {
                return null;
            }
            if (readInt >= 0) {
                long[] jArr = new long[readInt];
                for (int i = 0; i < readInt; i++) {
                    jArr[i] = readVarLong(dataInput);
                }
                return jArr;
            }
            throw new ProtocolException("negative array size");
        }

        public static void writeVarLongArray(DataOutput dataOutput, long[] jArr, int i) throws IOException {
            if (jArr == null) {
                dataOutput.writeInt(-1);
            } else if (i <= jArr.length) {
                dataOutput.writeInt(i);
                for (int i2 = 0; i2 < i; i2++) {
                    writeVarLong(dataOutput, jArr[i2]);
                }
            } else {
                throw new IllegalArgumentException("size larger than length");
            }
        }
    }

    public static class ParcelUtils {
        public static long[] readLongArray(Parcel parcel) {
            int readInt = parcel.readInt();
            if (readInt == -1) {
                return null;
            }
            long[] jArr = new long[readInt];
            for (int i = 0; i < readInt; i++) {
                jArr[i] = parcel.readLong();
            }
            return jArr;
        }

        public static void writeLongArray(Parcel parcel, long[] jArr, int i) {
            if (jArr == null) {
                parcel.writeInt(-1);
            } else if (i <= jArr.length) {
                parcel.writeInt(i);
                for (int i2 = 0; i2 < i; i2++) {
                    parcel.writeLong(jArr[i2]);
                }
            } else {
                throw new IllegalArgumentException("size larger than length");
            }
        }
    }

    public static final class Builder {
        private final long mBucketDuration;
        private final TreeMap<Long, Entry> mEntries = new TreeMap<>();

        public Builder(long j, int i) {
            this.mBucketDuration = j;
        }

        public Builder addEntry(Entry entry) {
            Entry entry2 = this.mEntries.get(Long.valueOf(entry.bucketStart));
            if (entry2 != null) {
                this.mEntries.put(Long.valueOf(entry.bucketStart), entry2.plus(entry, this.mBucketDuration));
            } else {
                this.mEntries.put(Long.valueOf(entry.bucketStart), entry);
            }
            return this;
        }

        private static long sum(long[] jArr) {
            long j = 0;
            for (long j2 : jArr) {
                j += j2;
            }
            return j;
        }

        public NetworkStatsHistory build() {
            int size = this.mEntries.size();
            long[] jArr = new long[size];
            long[] jArr2 = new long[size];
            long[] jArr3 = new long[size];
            long[] jArr4 = new long[size];
            long[] jArr5 = new long[size];
            long[] jArr6 = new long[size];
            long[] jArr7 = new long[size];
            int i = 0;
            for (Entry next : this.mEntries.values()) {
                jArr[i] = next.bucketStart;
                jArr2[i] = next.activeTime;
                jArr3[i] = next.rxBytes;
                jArr4[i] = next.rxPackets;
                jArr5[i] = next.txBytes;
                jArr6[i] = next.txPackets;
                jArr7[i] = next.operations;
                i++;
            }
            return new NetworkStatsHistory(this.mBucketDuration, jArr, jArr2, jArr3, jArr4, jArr5, jArr6, jArr7, size, sum(jArr3) + sum(jArr5));
        }
    }
}
