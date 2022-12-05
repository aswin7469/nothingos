package com.android.internal.app.procstats;

import android.content.ComponentName;
import android.os.Debug;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.SettingsStringUtil;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.DebugUtils;
import android.util.LongSparseArray;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.app.ProcessMap;
import com.android.internal.app.procstats.AssociationState;
import com.android.internal.app.procstats.IProcessStats;
import com.android.internal.content.NativeLibraryHelper;
import dalvik.system.VMRuntime;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: classes4.dex */
public final class ProcessStats implements Parcelable {
    public static final int ADD_PSS_EXTERNAL = 3;
    public static final int ADD_PSS_EXTERNAL_SLOW = 4;
    public static final int ADD_PSS_INTERNAL_ALL_MEM = 1;
    public static final int ADD_PSS_INTERNAL_ALL_POLL = 2;
    public static final int ADD_PSS_INTERNAL_SINGLE = 0;
    public static final int ADJ_COUNT = 8;
    public static final int ADJ_MEM_FACTOR_COUNT = 4;
    public static final int ADJ_MEM_FACTOR_CRITICAL = 3;
    public static final int ADJ_MEM_FACTOR_LOW = 2;
    public static final int ADJ_MEM_FACTOR_MODERATE = 1;
    public static final int ADJ_MEM_FACTOR_NORMAL = 0;
    public static final int ADJ_NOTHING = -1;
    public static final int ADJ_SCREEN_MOD = 4;
    public static final int ADJ_SCREEN_OFF = 0;
    public static final int ADJ_SCREEN_ON = 4;
    static final boolean DEBUG = false;
    static final boolean DEBUG_PARCEL = false;
    public static final int FLAG_COMPLETE = 1;
    public static final int FLAG_SHUTDOWN = 2;
    public static final int FLAG_SYSPROPS = 4;
    private static final long INVERSE_PROC_STATE_WARNING_MIN_INTERVAL_MS = 10000;
    private static final int MAGIC = 1347638356;
    private static final int PARCEL_VERSION = 40;
    public static final int PSS_AVERAGE = 2;
    public static final int PSS_COUNT = 10;
    public static final int PSS_MAXIMUM = 3;
    public static final int PSS_MINIMUM = 1;
    public static final int PSS_RSS_AVERAGE = 8;
    public static final int PSS_RSS_MAXIMUM = 9;
    public static final int PSS_RSS_MINIMUM = 7;
    public static final int PSS_SAMPLE_COUNT = 0;
    public static final int PSS_USS_AVERAGE = 5;
    public static final int PSS_USS_MAXIMUM = 6;
    public static final int PSS_USS_MINIMUM = 4;
    public static final int REPORT_ALL = 15;
    public static final int REPORT_PKG_ASC_STATS = 8;
    public static final int REPORT_PKG_PROC_STATS = 2;
    public static final int REPORT_PKG_STATS = 14;
    public static final int REPORT_PKG_SVC_STATS = 4;
    public static final int REPORT_PROC_STATS = 1;
    public static final String SERVICE_NAME = "procstats";
    public static final int STATE_BACKUP = 6;
    public static final int STATE_BOUND_TOP_OR_FGS = 2;
    public static final int STATE_CACHED_ACTIVITY = 13;
    public static final int STATE_CACHED_ACTIVITY_CLIENT = 14;
    public static final int STATE_CACHED_EMPTY = 15;
    public static final int STATE_COUNT = 16;
    public static final int STATE_FGS = 3;
    public static final int STATE_HEAVY_WEIGHT = 10;
    public static final int STATE_HOME = 11;
    public static final int STATE_IMPORTANT_BACKGROUND = 5;
    public static final int STATE_IMPORTANT_FOREGROUND = 4;
    public static final int STATE_LAST_ACTIVITY = 12;
    public static final int STATE_NOTHING = -1;
    public static final int STATE_PERSISTENT = 0;
    public static final int STATE_RECEIVER = 9;
    public static final int STATE_SERVICE = 7;
    public static final int STATE_SERVICE_RESTARTING = 8;
    public static final int STATE_TOP = 1;
    public static final int SYS_MEM_USAGE_CACHED_AVERAGE = 2;
    public static final int SYS_MEM_USAGE_CACHED_MAXIMUM = 3;
    public static final int SYS_MEM_USAGE_CACHED_MINIMUM = 1;
    public static final int SYS_MEM_USAGE_COUNT = 16;
    public static final int SYS_MEM_USAGE_FREE_AVERAGE = 5;
    public static final int SYS_MEM_USAGE_FREE_MAXIMUM = 6;
    public static final int SYS_MEM_USAGE_FREE_MINIMUM = 4;
    public static final int SYS_MEM_USAGE_KERNEL_AVERAGE = 11;
    public static final int SYS_MEM_USAGE_KERNEL_MAXIMUM = 12;
    public static final int SYS_MEM_USAGE_KERNEL_MINIMUM = 10;
    public static final int SYS_MEM_USAGE_NATIVE_AVERAGE = 14;
    public static final int SYS_MEM_USAGE_NATIVE_MAXIMUM = 15;
    public static final int SYS_MEM_USAGE_NATIVE_MINIMUM = 13;
    public static final int SYS_MEM_USAGE_SAMPLE_COUNT = 0;
    public static final int SYS_MEM_USAGE_ZRAM_AVERAGE = 8;
    public static final int SYS_MEM_USAGE_ZRAM_MAXIMUM = 9;
    public static final int SYS_MEM_USAGE_ZRAM_MINIMUM = 7;
    public static final String TAG = "ProcessStats";
    ArrayMap<String, Integer> mCommonStringToIndex;
    public long mExternalPssCount;
    public long mExternalPssTime;
    public long mExternalSlowPssCount;
    public long mExternalSlowPssTime;
    public int mFlags;
    boolean mHasSwappedOutPss;
    ArrayList<String> mIndexToCommonString;
    public long mInternalAllMemPssCount;
    public long mInternalAllMemPssTime;
    public long mInternalAllPollPssCount;
    public long mInternalAllPollPssTime;
    public long mInternalSinglePssCount;
    public long mInternalSinglePssTime;
    public int mMemFactor;
    public final long[] mMemFactorDurations;
    private long mNextInverseProcStateWarningUptime;
    public int mNumAggregated;
    public final ProcessMap<LongSparseArray<PackageState>> mPackages;
    private final ArrayList<String> mPageTypeLabels;
    private final ArrayList<Integer> mPageTypeNodes;
    private final ArrayList<int[]> mPageTypeSizes;
    private final ArrayList<String> mPageTypeZones;
    public final ProcessMap<ProcessState> mProcesses;
    public String mReadError;
    boolean mRunning;
    String mRuntime;
    private int mSkippedInverseProcStateWarningCount;
    public long mStartTime;
    public final SysMemUsageTable mSysMemUsage;
    public final long[] mSysMemUsageArgs;
    public final SparseMappingTable mTableData;
    public long mTimePeriodEndRealtime;
    public long mTimePeriodEndUptime;
    public long mTimePeriodStartClock;
    public String mTimePeriodStartClockStr;
    public long mTimePeriodStartRealtime;
    public long mTimePeriodStartUptime;
    public final ArrayList<AssociationState.SourceState> mTrackingAssociations;
    public static long COMMIT_PERIOD = 10800000;
    public static long COMMIT_UPTIME_PERIOD = 3600000;
    public static final int[] ALL_MEM_ADJ = {0, 1, 2, 3};
    public static final int[] ALL_SCREEN_ADJ = {0, 4};
    public static final int[] NON_CACHED_PROC_STATES = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    public static final int[] BACKGROUND_PROC_STATES = {4, 5, 6, 10, 7, 8, 9};
    public static final int[] ALL_PROC_STATES = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    public static final int[] OPTIONS = {1, 2, 4, 8, 14, 15};
    public static final String[] OPTIONS_STR = {"proc", "pkg-proc", "pkg-svc", "pkg-asc", "pkg-all", "all"};
    private static final Pattern sPageTypeRegex = Pattern.compile("^Node\\s+(\\d+),.* zone\\s+(\\w+),.* type\\s+(\\w+)\\s+([\\s\\d]+?)\\s*$");
    public static final Parcelable.Creator<ProcessStats> CREATOR = new Parcelable.Creator<ProcessStats>() { // from class: com.android.internal.app.procstats.ProcessStats.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ProcessStats mo3559createFromParcel(Parcel in) {
            return new ProcessStats(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ProcessStats[] mo3560newArray(int size) {
            return new ProcessStats[size];
        }
    };
    static final int[] BAD_TABLE = new int[0];
    static final Comparator<AssociationDumpContainer> ASSOCIATION_COMPARATOR = ProcessStats$$ExternalSyntheticLambda0.INSTANCE;

    public ProcessStats(boolean running) {
        this.mPackages = new ProcessMap<>();
        this.mProcesses = new ProcessMap<>();
        this.mTrackingAssociations = new ArrayList<>();
        this.mMemFactorDurations = new long[8];
        this.mMemFactor = -1;
        this.mNumAggregated = 1;
        SparseMappingTable sparseMappingTable = new SparseMappingTable();
        this.mTableData = sparseMappingTable;
        this.mSysMemUsageArgs = new long[16];
        this.mSysMemUsage = new SysMemUsageTable(sparseMappingTable);
        this.mPageTypeNodes = new ArrayList<>();
        this.mPageTypeZones = new ArrayList<>();
        this.mPageTypeLabels = new ArrayList<>();
        this.mPageTypeSizes = new ArrayList<>();
        this.mRunning = running;
        reset();
        if (running) {
            Debug.MemoryInfo info = new Debug.MemoryInfo();
            Debug.getMemoryInfo(Process.myPid(), info);
            this.mHasSwappedOutPss = info.hasSwappedOutPss();
        }
    }

    public ProcessStats(Parcel in) {
        this.mPackages = new ProcessMap<>();
        this.mProcesses = new ProcessMap<>();
        this.mTrackingAssociations = new ArrayList<>();
        this.mMemFactorDurations = new long[8];
        this.mMemFactor = -1;
        this.mNumAggregated = 1;
        SparseMappingTable sparseMappingTable = new SparseMappingTable();
        this.mTableData = sparseMappingTable;
        this.mSysMemUsageArgs = new long[16];
        this.mSysMemUsage = new SysMemUsageTable(sparseMappingTable);
        this.mPageTypeNodes = new ArrayList<>();
        this.mPageTypeZones = new ArrayList<>();
        this.mPageTypeLabels = new ArrayList<>();
        this.mPageTypeSizes = new ArrayList<>();
        reset();
        readFromParcel(in);
    }

    public ProcessStats() {
        this(false);
    }

    public void add(ProcessStats other) {
        ArrayMap<String, SparseArray<ProcessState>> procMap;
        ProcessState thisProc;
        int NPROCS;
        PackageState otherState;
        SparseArray<LongSparseArray<PackageState>> uids;
        int NSRVS;
        LongSparseArray<PackageState> versions;
        int NASCS;
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap;
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap2 = other.mPackages.getMap();
        for (int ip = 0; ip < pkgMap2.size(); ip++) {
            String pkgName = pkgMap2.keyAt(ip);
            SparseArray<LongSparseArray<PackageState>> uids2 = pkgMap2.valueAt(ip);
            for (int iu = 0; iu < uids2.size(); iu++) {
                int uid = uids2.keyAt(iu);
                LongSparseArray<PackageState> versions2 = uids2.valueAt(iu);
                int iv = 0;
                while (iv < versions2.size()) {
                    long vers = versions2.keyAt(iv);
                    PackageState otherState2 = versions2.valueAt(iv);
                    int NPROCS2 = otherState2.mProcesses.size();
                    int NSRVS2 = otherState2.mServices.size();
                    int NASCS2 = otherState2.mAssociations.size();
                    int iv2 = iv;
                    int iv3 = 0;
                    while (iv3 < NPROCS2) {
                        int NASCS3 = NASCS2;
                        ProcessState otherProc = otherState2.mProcesses.valueAt(iv3);
                        int NSRVS3 = NSRVS2;
                        if (otherProc.getCommonProcess() == otherProc) {
                            NPROCS = NPROCS2;
                            otherState = otherState2;
                            uids = uids2;
                            NSRVS = NSRVS3;
                            versions = versions2;
                            NASCS = NASCS3;
                            pkgMap = pkgMap2;
                        } else {
                            pkgMap = pkgMap2;
                            uids = uids2;
                            NSRVS = NSRVS3;
                            versions = versions2;
                            NASCS = NASCS3;
                            NPROCS = NPROCS2;
                            long vers2 = vers;
                            otherState = otherState2;
                            ProcessState thisProc2 = getProcessStateLocked(pkgName, uid, vers, otherProc.getName());
                            if (thisProc2.getCommonProcess() != thisProc2) {
                                vers = vers2;
                            } else {
                                thisProc2.setMultiPackage(true);
                                long now = SystemClock.uptimeMillis();
                                vers = vers2;
                                PackageState pkgState = getPackageStateLocked(pkgName, uid, vers);
                                thisProc2 = thisProc2.clone(now);
                                pkgState.mProcesses.put(thisProc2.getName(), thisProc2);
                            }
                            thisProc2.add(otherProc);
                        }
                        iv3++;
                        otherState2 = otherState;
                        NSRVS2 = NSRVS;
                        NASCS2 = NASCS;
                        pkgMap2 = pkgMap;
                        versions2 = versions;
                        uids2 = uids;
                        NPROCS2 = NPROCS;
                    }
                    PackageState otherState3 = otherState2;
                    ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap3 = pkgMap2;
                    SparseArray<LongSparseArray<PackageState>> uids3 = uids2;
                    LongSparseArray<PackageState> versions3 = versions2;
                    int NASCS4 = NASCS2;
                    int isvc = 0;
                    for (int NSRVS4 = NSRVS2; isvc < NSRVS4; NSRVS4 = NSRVS4) {
                        ServiceState otherSvc = otherState3.mServices.valueAt(isvc);
                        ServiceState thisSvc = getServiceStateLocked(pkgName, uid, vers, otherSvc.getProcessName(), otherSvc.getName());
                        thisSvc.add(otherSvc);
                        isvc++;
                    }
                    long vers3 = vers;
                    for (int iasc = 0; iasc < NASCS4; iasc++) {
                        AssociationState otherAsc = otherState3.mAssociations.valueAt(iasc);
                        AssociationState thisAsc = getAssociationStateLocked(pkgName, uid, vers3, otherAsc.getProcessName(), otherAsc.getName());
                        thisAsc.add(otherAsc);
                    }
                    iv = iv2 + 1;
                    pkgMap2 = pkgMap3;
                    versions2 = versions3;
                    uids2 = uids3;
                }
            }
        }
        ArrayMap<String, SparseArray<ProcessState>> procMap2 = other.mProcesses.getMap();
        for (int ip2 = 0; ip2 < procMap2.size(); ip2++) {
            SparseArray<ProcessState> uids4 = procMap2.valueAt(ip2);
            int iu2 = 0;
            while (iu2 < uids4.size()) {
                int uid2 = uids4.keyAt(iu2);
                ProcessState otherProc2 = uids4.valueAt(iu2);
                String name = otherProc2.getName();
                String pkg = otherProc2.getPackage();
                long vers4 = otherProc2.getVersion();
                ProcessState thisProc3 = this.mProcesses.get(name, uid2);
                if (thisProc3 == null) {
                    procMap = procMap2;
                    thisProc = new ProcessState(this, pkg, uid2, vers4, name);
                    this.mProcesses.put(name, uid2, thisProc);
                    PackageState thisState = getPackageStateLocked(pkg, uid2, vers4);
                    if (!thisState.mProcesses.containsKey(name)) {
                        thisState.mProcesses.put(name, thisProc);
                    }
                } else {
                    procMap = procMap2;
                    thisProc = thisProc3;
                }
                thisProc.add(otherProc2);
                iu2++;
                procMap2 = procMap;
            }
        }
        for (int i = 0; i < 8; i++) {
            long[] jArr = this.mMemFactorDurations;
            jArr[i] = jArr[i] + other.mMemFactorDurations[i];
        }
        this.mSysMemUsage.mergeStats(other.mSysMemUsage);
        this.mNumAggregated += other.mNumAggregated;
        long j = other.mTimePeriodStartClock;
        if (j < this.mTimePeriodStartClock) {
            this.mTimePeriodStartClock = j;
            this.mTimePeriodStartClockStr = other.mTimePeriodStartClockStr;
        }
        this.mTimePeriodEndRealtime += other.mTimePeriodEndRealtime - other.mTimePeriodStartRealtime;
        this.mTimePeriodEndUptime += other.mTimePeriodEndUptime - other.mTimePeriodStartUptime;
        this.mInternalSinglePssCount += other.mInternalSinglePssCount;
        this.mInternalSinglePssTime += other.mInternalSinglePssTime;
        this.mInternalAllMemPssCount += other.mInternalAllMemPssCount;
        this.mInternalAllMemPssTime += other.mInternalAllMemPssTime;
        this.mInternalAllPollPssCount += other.mInternalAllPollPssCount;
        this.mInternalAllPollPssTime += other.mInternalAllPollPssTime;
        this.mExternalPssCount += other.mExternalPssCount;
        this.mExternalPssTime += other.mExternalPssTime;
        this.mExternalSlowPssCount += other.mExternalSlowPssCount;
        this.mExternalSlowPssTime += other.mExternalSlowPssTime;
        this.mHasSwappedOutPss |= other.mHasSwappedOutPss;
    }

    public void addSysMemUsage(long cachedMem, long freeMem, long zramMem, long kernelMem, long nativeMem) {
        int i = this.mMemFactor;
        if (i != -1) {
            int state = i * 16;
            this.mSysMemUsageArgs[0] = 1;
            for (int i2 = 0; i2 < 3; i2++) {
                long[] jArr = this.mSysMemUsageArgs;
                jArr[i2 + 1] = cachedMem;
                jArr[i2 + 4] = freeMem;
                jArr[i2 + 7] = zramMem;
                jArr[i2 + 10] = kernelMem;
                jArr[i2 + 13] = nativeMem;
            }
            this.mSysMemUsage.mergeStats(state, this.mSysMemUsageArgs, 0);
        }
    }

    public void computeTotalMemoryUse(TotalMemoryUseCollection data, long now) {
        int i;
        long[] totalMemUsage;
        long j = now;
        data.totalTime = 0L;
        int i2 = 0;
        while (true) {
            i = 0;
            if (i2 >= 16) {
                break;
            }
            data.processStateWeight[i2] = 0.0d;
            data.processStatePss[i2] = 0;
            data.processStateTime[i2] = 0;
            data.processStateSamples[i2] = 0;
            i2++;
        }
        for (int i3 = 0; i3 < 16; i3++) {
            data.sysMemUsage[i3] = 0;
        }
        data.sysMemCachedWeight = 0.0d;
        data.sysMemFreeWeight = 0.0d;
        data.sysMemZRamWeight = 0.0d;
        data.sysMemKernelWeight = 0.0d;
        data.sysMemNativeWeight = 0.0d;
        data.sysMemSamples = 0;
        long[] totalMemUsage2 = this.mSysMemUsage.getTotalMemUsage();
        int is = 0;
        while (is < data.screenStates.length) {
            int im = 0;
            while (im < data.memStates.length) {
                int memBucket = data.screenStates[is] + data.memStates[im];
                int stateBucket = memBucket * 16;
                long memTime = this.mMemFactorDurations[memBucket];
                if (this.mMemFactor == memBucket) {
                    memTime += j - this.mStartTime;
                }
                data.totalTime += memTime;
                int sysKey = this.mSysMemUsage.getKey((byte) stateBucket);
                long[] longs = totalMemUsage2;
                int idx = 0;
                if (sysKey == -1) {
                    totalMemUsage = totalMemUsage2;
                } else {
                    long[] tmpLongs = this.mSysMemUsage.getArrayForKey(sysKey);
                    int tmpIndex = SparseMappingTable.getIndexFromKey(sysKey);
                    if (tmpLongs[tmpIndex + 0] < 3) {
                        totalMemUsage = totalMemUsage2;
                    } else {
                        totalMemUsage = totalMemUsage2;
                        long[] totalMemUsage3 = data.sysMemUsage;
                        SysMemUsageTable.mergeSysMemUsage(totalMemUsage3, i, longs, 0);
                        longs = tmpLongs;
                        idx = tmpIndex;
                    }
                }
                data.sysMemCachedWeight += longs[idx + 2] * memTime;
                data.sysMemFreeWeight += longs[idx + 5] * memTime;
                data.sysMemZRamWeight += longs[idx + 8] * memTime;
                data.sysMemKernelWeight += longs[idx + 11] * memTime;
                data.sysMemNativeWeight += longs[idx + 14] * memTime;
                data.sysMemSamples = (int) (data.sysMemSamples + longs[idx + 0]);
                im++;
                j = now;
                totalMemUsage2 = totalMemUsage;
                is = is;
                i = 0;
            }
            is++;
            j = now;
            i = 0;
        }
        data.hasSwappedOutPss = this.mHasSwappedOutPss;
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        for (int iproc = 0; iproc < procMap.size(); iproc++) {
            SparseArray<ProcessState> uids = procMap.valueAt(iproc);
            for (int iu = 0; iu < uids.size(); iu++) {
                ProcessState proc = uids.valueAt(iu);
                proc.aggregatePss(data, now);
            }
        }
    }

    public void reset() {
        resetCommon();
        this.mPackages.getMap().clear();
        this.mProcesses.getMap().clear();
        this.mMemFactor = -1;
        this.mStartTime = 0L;
    }

    public void resetSafely() {
        resetCommon();
        long now = SystemClock.uptimeMillis();
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        for (int ip = procMap.size() - 1; ip >= 0; ip--) {
            SparseArray<ProcessState> uids = procMap.valueAt(ip);
            for (int iu = uids.size() - 1; iu >= 0; iu--) {
                uids.valueAt(iu).tmpNumInUse = 0;
            }
        }
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = this.mPackages.getMap();
        for (int ip2 = pkgMap.size() - 1; ip2 >= 0; ip2--) {
            SparseArray<LongSparseArray<PackageState>> uids2 = pkgMap.valueAt(ip2);
            for (int iu2 = uids2.size() - 1; iu2 >= 0; iu2--) {
                LongSparseArray<PackageState> vpkgs = uids2.valueAt(iu2);
                for (int iv = vpkgs.size() - 1; iv >= 0; iv--) {
                    PackageState pkgState = vpkgs.valueAt(iv);
                    for (int iproc = pkgState.mProcesses.size() - 1; iproc >= 0; iproc--) {
                        ProcessState ps = pkgState.mProcesses.valueAt(iproc);
                        if (ps.isInUse()) {
                            ps.resetSafely(now);
                            ps.getCommonProcess().tmpNumInUse++;
                            ps.getCommonProcess().tmpFoundSubProc = ps;
                        } else {
                            pkgState.mProcesses.valueAt(iproc).makeDead();
                            pkgState.mProcesses.removeAt(iproc);
                        }
                    }
                    for (int isvc = pkgState.mServices.size() - 1; isvc >= 0; isvc--) {
                        ServiceState ss = pkgState.mServices.valueAt(isvc);
                        if (ss.isInUse()) {
                            ss.resetSafely(now);
                        } else {
                            pkgState.mServices.removeAt(isvc);
                        }
                    }
                    for (int iasc = pkgState.mAssociations.size() - 1; iasc >= 0; iasc--) {
                        AssociationState as = pkgState.mAssociations.valueAt(iasc);
                        if (as.isInUse()) {
                            as.resetSafely(now);
                        } else {
                            pkgState.mAssociations.removeAt(iasc);
                        }
                    }
                    if (pkgState.mProcesses.size() <= 0 && pkgState.mServices.size() <= 0 && pkgState.mAssociations.size() <= 0) {
                        vpkgs.removeAt(iv);
                    }
                }
                int iv2 = vpkgs.size();
                if (iv2 <= 0) {
                    uids2.removeAt(iu2);
                }
            }
            int iu3 = uids2.size();
            if (iu3 <= 0) {
                pkgMap.removeAt(ip2);
            }
        }
        int ip3 = procMap.size();
        for (int ip4 = ip3 - 1; ip4 >= 0; ip4--) {
            SparseArray<ProcessState> uids3 = procMap.valueAt(ip4);
            for (int iu4 = uids3.size() - 1; iu4 >= 0; iu4--) {
                ProcessState ps2 = uids3.valueAt(iu4);
                if (ps2.isInUse() || ps2.tmpNumInUse > 0) {
                    if (!ps2.isActive() && ps2.isMultiPackage() && ps2.tmpNumInUse == 1) {
                        ProcessState ps3 = ps2.tmpFoundSubProc;
                        ps3.makeStandalone();
                        uids3.setValueAt(iu4, ps3);
                    } else {
                        ps2.resetSafely(now);
                    }
                } else {
                    ps2.makeDead();
                    uids3.removeAt(iu4);
                }
            }
            int iu5 = uids3.size();
            if (iu5 <= 0) {
                procMap.removeAt(ip4);
            }
        }
        this.mStartTime = now;
    }

    private void resetCommon() {
        this.mNumAggregated = 1;
        this.mTimePeriodStartClock = System.currentTimeMillis();
        buildTimePeriodStartClockStr();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.mTimePeriodEndRealtime = elapsedRealtime;
        this.mTimePeriodStartRealtime = elapsedRealtime;
        long uptimeMillis = SystemClock.uptimeMillis();
        this.mTimePeriodEndUptime = uptimeMillis;
        this.mTimePeriodStartUptime = uptimeMillis;
        this.mInternalSinglePssCount = 0L;
        this.mInternalSinglePssTime = 0L;
        this.mInternalAllMemPssCount = 0L;
        this.mInternalAllMemPssTime = 0L;
        this.mInternalAllPollPssCount = 0L;
        this.mInternalAllPollPssTime = 0L;
        this.mExternalPssCount = 0L;
        this.mExternalPssTime = 0L;
        this.mExternalSlowPssCount = 0L;
        this.mExternalSlowPssTime = 0L;
        this.mTableData.reset();
        Arrays.fill(this.mMemFactorDurations, 0L);
        this.mSysMemUsage.resetTable();
        this.mStartTime = 0L;
        this.mReadError = null;
        this.mFlags = 0;
        evaluateSystemProperties(true);
        updateFragmentation();
    }

    public boolean evaluateSystemProperties(boolean update) {
        boolean changed = false;
        String runtime = SystemProperties.get("persist.sys.dalvik.vm.lib.2", VMRuntime.getRuntime().vmLibrary());
        if (!Objects.equals(runtime, this.mRuntime)) {
            changed = true;
            if (update) {
                this.mRuntime = runtime;
            }
        }
        return changed;
    }

    private void buildTimePeriodStartClockStr() {
        this.mTimePeriodStartClockStr = DateFormat.format("yyyy-MM-dd-HH-mm-ss", this.mTimePeriodStartClock).toString();
    }

    public void updateFragmentation() {
        Integer node;
        BufferedReader reader = null;
        try {
            try {
                reader = new BufferedReader(new FileReader("/proc/pagetypeinfo"));
                Matcher matcher = sPageTypeRegex.matcher("");
                this.mPageTypeNodes.clear();
                this.mPageTypeZones.clear();
                this.mPageTypeLabels.clear();
                this.mPageTypeSizes.clear();
                while (true) {
                    String line = reader.readLine();
                    if (line != null) {
                        matcher.reset(line);
                        if (matcher.matches() && (node = Integer.valueOf(matcher.group(1), 10)) != null) {
                            this.mPageTypeNodes.add(node);
                            this.mPageTypeZones.add(matcher.group(2));
                            this.mPageTypeLabels.add(matcher.group(3));
                            this.mPageTypeSizes.add(splitAndParseNumbers(matcher.group(4)));
                        }
                    } else {
                        try {
                            reader.close();
                            return;
                        } catch (IOException e) {
                            return;
                        }
                    }
                }
            } catch (IOException e2) {
                this.mPageTypeNodes.clear();
                this.mPageTypeZones.clear();
                this.mPageTypeLabels.clear();
                this.mPageTypeSizes.clear();
                if (reader == null) {
                    return;
                }
                try {
                    reader.close();
                } catch (IOException e3) {
                }
            }
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
    }

    private static int[] splitAndParseNumbers(String s) {
        boolean digit = false;
        int count = 0;
        int N = s.length();
        for (int i = 0; i < N; i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                if (!digit) {
                    digit = true;
                    count++;
                }
            } else {
                digit = false;
            }
        }
        int[] result = new int[count];
        int p = 0;
        int val = 0;
        for (int i2 = 0; i2 < N; i2++) {
            char c2 = s.charAt(i2);
            if (c2 >= '0' && c2 <= '9') {
                if (!digit) {
                    digit = true;
                    val = c2 - '0';
                } else {
                    val = (val * 10) + (c2 - '0');
                }
            } else if (digit) {
                digit = false;
                result[p] = val;
                p++;
            }
        }
        if (count > 0) {
            result[count - 1] = val;
        }
        return result;
    }

    private void writeCompactedLongArray(Parcel out, long[] array, int num) {
        for (int i = 0; i < num; i++) {
            long val = array[i];
            if (val < 0) {
                Slog.w(TAG, "Time val negative: " + val);
                val = 0;
            }
            if (val > 2147483647L) {
                int top = ~((int) (2147483647L & (val >> 32)));
                int bottom = (int) (4294967295L & val);
                out.writeInt(top);
                out.writeInt(bottom);
            } else {
                out.writeInt((int) val);
            }
        }
    }

    private void readCompactedLongArray(Parcel in, int version, long[] array, int num) {
        if (version <= 10) {
            in.readLongArray(array);
            return;
        }
        int alen = array.length;
        if (num > alen) {
            throw new RuntimeException("bad array lengths: got " + num + " array is " + alen);
        }
        int i = 0;
        while (i < num) {
            int val = in.readInt();
            if (val >= 0) {
                array[i] = val;
            } else {
                int bottom = in.readInt();
                array[i] = ((~val) << 32) | bottom;
            }
            i++;
        }
        while (i < alen) {
            array[i] = 0;
            i++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeCommonString(Parcel out, String name) {
        Integer index = this.mCommonStringToIndex.get(name);
        if (index != null) {
            out.writeInt(index.intValue());
            return;
        }
        Integer index2 = Integer.valueOf(this.mCommonStringToIndex.size());
        this.mCommonStringToIndex.put(name, index2);
        out.writeInt(~index2.intValue());
        out.writeString(name);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String readCommonString(Parcel in, int version) {
        if (version <= 9) {
            return in.readString();
        }
        int index = in.readInt();
        if (index >= 0) {
            return this.mIndexToCommonString.get(index);
        }
        int index2 = ~index;
        String name = in.readString();
        while (this.mIndexToCommonString.size() <= index2) {
            this.mIndexToCommonString.add(null);
        }
        this.mIndexToCommonString.set(index2, name);
        return name;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        writeToParcel(out, SystemClock.uptimeMillis(), flags);
    }

    public void writeToParcel(Parcel out, long now, int flags) {
        out.writeInt(MAGIC);
        out.writeInt(40);
        out.writeInt(16);
        out.writeInt(8);
        out.writeInt(10);
        out.writeInt(16);
        out.writeInt(4096);
        this.mCommonStringToIndex = new ArrayMap<>(this.mProcesses.size());
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        int NPROC = procMap.size();
        for (int ip = 0; ip < NPROC; ip++) {
            SparseArray<ProcessState> uids = procMap.valueAt(ip);
            int NUID = uids.size();
            for (int iu = 0; iu < NUID; iu++) {
                uids.valueAt(iu).commitStateTime(now);
            }
        }
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = this.mPackages.getMap();
        int NPKG = pkgMap.size();
        for (int ip2 = 0; ip2 < NPKG; ip2++) {
            SparseArray<LongSparseArray<PackageState>> uids2 = pkgMap.valueAt(ip2);
            int NUID2 = uids2.size();
            for (int iu2 = 0; iu2 < NUID2; iu2++) {
                LongSparseArray<PackageState> vpkgs = uids2.valueAt(iu2);
                int NVERS = vpkgs.size();
                int iv = 0;
                while (iv < NVERS) {
                    PackageState pkgState = vpkgs.valueAt(iv);
                    SparseArray<LongSparseArray<PackageState>> uids3 = uids2;
                    int NPROCS = pkgState.mProcesses.size();
                    int NUID3 = NUID2;
                    int NUID4 = 0;
                    while (NUID4 < NPROCS) {
                        int NPROCS2 = NPROCS;
                        ProcessState proc = pkgState.mProcesses.valueAt(NUID4);
                        LongSparseArray<PackageState> vpkgs2 = vpkgs;
                        if (proc.getCommonProcess() != proc) {
                            proc.commitStateTime(now);
                        }
                        NUID4++;
                        NPROCS = NPROCS2;
                        vpkgs = vpkgs2;
                    }
                    LongSparseArray<PackageState> vpkgs3 = vpkgs;
                    int NSRVS = pkgState.mServices.size();
                    for (int isvc = 0; isvc < NSRVS; isvc++) {
                        pkgState.mServices.valueAt(isvc).commitStateTime(now);
                    }
                    int NASCS = pkgState.mAssociations.size();
                    int iasc = 0;
                    while (iasc < NASCS) {
                        pkgState.mAssociations.valueAt(iasc).commitStateTime(now);
                        iasc++;
                        NSRVS = NSRVS;
                    }
                    iv++;
                    uids2 = uids3;
                    NUID2 = NUID3;
                    vpkgs = vpkgs3;
                }
            }
        }
        int ip3 = this.mNumAggregated;
        out.writeInt(ip3);
        out.writeLong(this.mTimePeriodStartClock);
        out.writeLong(this.mTimePeriodStartRealtime);
        out.writeLong(this.mTimePeriodEndRealtime);
        out.writeLong(this.mTimePeriodStartUptime);
        out.writeLong(this.mTimePeriodEndUptime);
        out.writeLong(this.mInternalSinglePssCount);
        out.writeLong(this.mInternalSinglePssTime);
        out.writeLong(this.mInternalAllMemPssCount);
        out.writeLong(this.mInternalAllMemPssTime);
        out.writeLong(this.mInternalAllPollPssCount);
        out.writeLong(this.mInternalAllPollPssTime);
        out.writeLong(this.mExternalPssCount);
        out.writeLong(this.mExternalPssTime);
        out.writeLong(this.mExternalSlowPssCount);
        out.writeLong(this.mExternalSlowPssTime);
        out.writeString(this.mRuntime);
        out.writeInt(this.mHasSwappedOutPss ? 1 : 0);
        out.writeInt(this.mFlags);
        this.mTableData.writeToParcel(out);
        int i = this.mMemFactor;
        if (i != -1) {
            long[] jArr = this.mMemFactorDurations;
            jArr[i] = jArr[i] + (now - this.mStartTime);
            this.mStartTime = now;
        }
        long[] jArr2 = this.mMemFactorDurations;
        writeCompactedLongArray(out, jArr2, jArr2.length);
        this.mSysMemUsage.writeToParcel(out);
        out.writeInt(NPROC);
        for (int ip4 = 0; ip4 < NPROC; ip4++) {
            writeCommonString(out, procMap.keyAt(ip4));
            SparseArray<ProcessState> uids4 = procMap.valueAt(ip4);
            int NUID5 = uids4.size();
            out.writeInt(NUID5);
            for (int iu3 = 0; iu3 < NUID5; iu3++) {
                out.writeInt(uids4.keyAt(iu3));
                ProcessState proc2 = uids4.valueAt(iu3);
                writeCommonString(out, proc2.getPackage());
                out.writeLong(proc2.getVersion());
                proc2.writeToParcel(out, now);
            }
        }
        out.writeInt(NPKG);
        for (int ip5 = 0; ip5 < NPKG; ip5++) {
            writeCommonString(out, pkgMap.keyAt(ip5));
            SparseArray<LongSparseArray<PackageState>> uids5 = pkgMap.valueAt(ip5);
            int NUID6 = uids5.size();
            out.writeInt(NUID6);
            for (int iu4 = 0; iu4 < NUID6; iu4++) {
                out.writeInt(uids5.keyAt(iu4));
                LongSparseArray<PackageState> vpkgs4 = uids5.valueAt(iu4);
                int NVERS2 = vpkgs4.size();
                out.writeInt(NVERS2);
                int iv2 = 0;
                while (iv2 < NVERS2) {
                    ArrayMap<String, SparseArray<ProcessState>> procMap2 = procMap;
                    int NPROC2 = NPROC;
                    out.writeLong(vpkgs4.keyAt(iv2));
                    PackageState pkgState2 = vpkgs4.valueAt(iv2);
                    int NPROCS3 = pkgState2.mProcesses.size();
                    out.writeInt(NPROCS3);
                    ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap2 = pkgMap;
                    int iproc = 0;
                    while (iproc < NPROCS3) {
                        int NPROCS4 = NPROCS3;
                        writeCommonString(out, pkgState2.mProcesses.keyAt(iproc));
                        ProcessState proc3 = pkgState2.mProcesses.valueAt(iproc);
                        int NPKG2 = NPKG;
                        if (proc3.getCommonProcess() == proc3) {
                            out.writeInt(0);
                        } else {
                            out.writeInt(1);
                            proc3.writeToParcel(out, now);
                        }
                        iproc++;
                        NPROCS3 = NPROCS4;
                        NPKG = NPKG2;
                    }
                    int NPKG3 = NPKG;
                    int NSRVS2 = pkgState2.mServices.size();
                    out.writeInt(NSRVS2);
                    int isvc2 = 0;
                    while (isvc2 < NSRVS2) {
                        out.writeString(pkgState2.mServices.keyAt(isvc2));
                        ServiceState svc = pkgState2.mServices.valueAt(isvc2);
                        writeCommonString(out, svc.getProcessName());
                        svc.writeToParcel(out, now);
                        isvc2++;
                        NSRVS2 = NSRVS2;
                    }
                    int NASCS2 = pkgState2.mAssociations.size();
                    out.writeInt(NASCS2);
                    int iasc2 = 0;
                    while (iasc2 < NASCS2) {
                        writeCommonString(out, pkgState2.mAssociations.keyAt(iasc2));
                        AssociationState asc = pkgState2.mAssociations.valueAt(iasc2);
                        writeCommonString(out, asc.getProcessName());
                        asc.writeToParcel(this, out, now);
                        iasc2++;
                        pkgState2 = pkgState2;
                    }
                    iv2++;
                    procMap = procMap2;
                    NPROC = NPROC2;
                    pkgMap = pkgMap2;
                    NPKG = NPKG3;
                }
            }
        }
        int NPAGETYPES = this.mPageTypeLabels.size();
        out.writeInt(NPAGETYPES);
        for (int i2 = 0; i2 < NPAGETYPES; i2++) {
            out.writeInt(this.mPageTypeNodes.get(i2).intValue());
            out.writeString(this.mPageTypeZones.get(i2));
            out.writeString(this.mPageTypeLabels.get(i2));
            out.writeIntArray(this.mPageTypeSizes.get(i2));
        }
        this.mCommonStringToIndex = null;
    }

    private boolean readCheckedInt(Parcel in, int val, String what) {
        int got = in.readInt();
        if (got != val) {
            this.mReadError = "bad " + what + ": " + got;
            return false;
        }
        return true;
    }

    static byte[] readFully(InputStream stream, int[] outLen) throws IOException {
        int pos = 0;
        int initialAvail = stream.available();
        byte[] data = new byte[initialAvail > 0 ? initialAvail + 1 : 16384];
        while (true) {
            int amt = stream.read(data, pos, data.length - pos);
            if (amt < 0) {
                outLen[0] = pos;
                return data;
            }
            pos += amt;
            if (pos >= data.length) {
                byte[] newData = new byte[pos + 16384];
                System.arraycopy(data, 0, newData, 0, pos);
                data = newData;
            }
        }
    }

    public void read(InputStream stream) {
        try {
            int[] len = new int[1];
            byte[] raw = readFully(stream, len);
            Parcel in = Parcel.obtain();
            in.unmarshall(raw, 0, len[0]);
            in.setDataPosition(0);
            stream.close();
            readFromParcel(in);
        } catch (IOException e) {
            this.mReadError = "caught exception: " + e;
        }
    }

    public void readFromParcel(Parcel in) {
        LongSparseArray<PackageState> vpkg;
        int uid;
        String associationName;
        AssociationState asc;
        long vers;
        String serviceName;
        int NSRVS;
        PackageState pkgState;
        ServiceState serv;
        int NPROC;
        int uid2;
        String procName;
        boolean hadData = this.mPackages.getMap().size() > 0 || this.mProcesses.getMap().size() > 0;
        if (hadData) {
            resetSafely();
        }
        if (!readCheckedInt(in, MAGIC, "magic number")) {
            return;
        }
        int version = in.readInt();
        if (version == 40) {
            if (readCheckedInt(in, 16, "state count") && readCheckedInt(in, 8, "adj count") && readCheckedInt(in, 10, "pss count") && readCheckedInt(in, 16, "sys mem usage count") && readCheckedInt(in, 4096, "longs size")) {
                this.mIndexToCommonString = new ArrayList<>();
                this.mNumAggregated = in.readInt();
                this.mTimePeriodStartClock = in.readLong();
                buildTimePeriodStartClockStr();
                this.mTimePeriodStartRealtime = in.readLong();
                this.mTimePeriodEndRealtime = in.readLong();
                this.mTimePeriodStartUptime = in.readLong();
                this.mTimePeriodEndUptime = in.readLong();
                this.mInternalSinglePssCount = in.readLong();
                this.mInternalSinglePssTime = in.readLong();
                this.mInternalAllMemPssCount = in.readLong();
                this.mInternalAllMemPssTime = in.readLong();
                this.mInternalAllPollPssCount = in.readLong();
                this.mInternalAllPollPssTime = in.readLong();
                this.mExternalPssCount = in.readLong();
                this.mExternalPssTime = in.readLong();
                this.mExternalSlowPssCount = in.readLong();
                this.mExternalSlowPssTime = in.readLong();
                this.mRuntime = in.readString();
                this.mHasSwappedOutPss = in.readInt() != 0;
                this.mFlags = in.readInt();
                this.mTableData.readFromParcel(in);
                long[] jArr = this.mMemFactorDurations;
                readCompactedLongArray(in, version, jArr, jArr.length);
                if (!this.mSysMemUsage.readFromParcel(in)) {
                    return;
                }
                int NPROC2 = in.readInt();
                if (NPROC2 >= 0) {
                    int NPROC3 = NPROC2;
                    while (NPROC3 > 0) {
                        int NPROC4 = NPROC3 - 1;
                        String procName2 = readCommonString(in, version);
                        if (procName2 == null) {
                            this.mReadError = "bad process name";
                            return;
                        }
                        int NUID = in.readInt();
                        if (NUID < 0) {
                            this.mReadError = "bad uid count: " + NUID;
                            return;
                        }
                        while (NUID > 0) {
                            int NUID2 = NUID - 1;
                            int uid3 = in.readInt();
                            if (uid3 < 0) {
                                this.mReadError = "bad uid: " + uid3;
                                return;
                            }
                            String pkgName = readCommonString(in, version);
                            if (pkgName == null) {
                                this.mReadError = "bad process package name";
                                return;
                            }
                            long vers2 = in.readLong();
                            ProcessState proc = hadData ? this.mProcesses.get(procName2, uid3) : null;
                            if (proc != null) {
                                if (proc.readFromParcel(in, version, false)) {
                                    uid2 = uid3;
                                    procName = procName2;
                                } else {
                                    return;
                                }
                            } else {
                                uid2 = uid3;
                                procName = procName2;
                                proc = new ProcessState(this, pkgName, uid3, vers2, procName2);
                                if (!proc.readFromParcel(in, version, true)) {
                                    return;
                                }
                            }
                            String procName3 = procName;
                            this.mProcesses.put(procName3, uid2, proc);
                            procName2 = procName3;
                            NUID = NUID2;
                        }
                        NPROC3 = NPROC4;
                    }
                    int NUID3 = in.readInt();
                    if (NUID3 < 0) {
                        this.mReadError = "bad package count: " + NUID3;
                        return;
                    }
                    while (NUID3 > 0) {
                        int NPKG = NUID3 - 1;
                        String pkgName2 = readCommonString(in, version);
                        if (pkgName2 == null) {
                            this.mReadError = "bad package name";
                            return;
                        }
                        int NVERS = in.readInt();
                        if (NVERS < 0) {
                            this.mReadError = "bad uid count: " + NVERS;
                            return;
                        }
                        while (NVERS > 0) {
                            int NUID4 = NVERS - 1;
                            int uid4 = in.readInt();
                            if (uid4 < 0) {
                                this.mReadError = "bad uid: " + uid4;
                                return;
                            }
                            int NVERS2 = in.readInt();
                            if (NVERS2 < 0) {
                                this.mReadError = "bad versions count: " + NVERS2;
                                return;
                            }
                            while (NVERS2 > 0) {
                                int NVERS3 = NVERS2 - 1;
                                long vers3 = in.readLong();
                                String pkgName3 = pkgName2;
                                int uid5 = uid4;
                                PackageState pkgState2 = new PackageState(this, pkgName2, uid4, vers3);
                                LongSparseArray<PackageState> vpkg2 = this.mPackages.get(pkgName3, uid5);
                                if (vpkg2 != null) {
                                    vpkg = vpkg2;
                                } else {
                                    LongSparseArray<PackageState> vpkg3 = new LongSparseArray<>();
                                    this.mPackages.put(pkgName3, uid5, vpkg3);
                                    vpkg = vpkg3;
                                }
                                long vers4 = vers3;
                                vpkg.put(vers4, pkgState2);
                                int NPROCS = in.readInt();
                                if (NPROCS < 0) {
                                    this.mReadError = "bad package process count: " + NPROCS;
                                    return;
                                }
                                int NPROCS2 = NPROCS;
                                while (NPROCS2 > 0) {
                                    NPROCS2--;
                                    String procName4 = readCommonString(in, version);
                                    if (procName4 == null) {
                                        this.mReadError = "bad package process name";
                                        return;
                                    }
                                    int hasProc = in.readInt();
                                    LongSparseArray<PackageState> vpkg4 = vpkg;
                                    ProcessState commonProc = this.mProcesses.get(procName4, uid5);
                                    if (commonProc == null) {
                                        this.mReadError = "no common proc: " + procName4;
                                        return;
                                    }
                                    if (hasProc != 0) {
                                        ProcessState proc2 = hadData ? pkgState2.mProcesses.get(procName4) : null;
                                        if (proc2 != null) {
                                            NPROC = NPROC3;
                                            if (!proc2.readFromParcel(in, version, false)) {
                                                return;
                                            }
                                        } else {
                                            NPROC = NPROC3;
                                            proc2 = new ProcessState(commonProc, pkgName3, uid5, vers4, procName4, 0L);
                                            if (!proc2.readFromParcel(in, version, true)) {
                                                return;
                                            }
                                        }
                                        pkgState2.mProcesses.put(procName4, proc2);
                                    } else {
                                        NPROC = NPROC3;
                                        pkgState2.mProcesses.put(procName4, commonProc);
                                    }
                                    vpkg = vpkg4;
                                    NPROC3 = NPROC;
                                }
                                int NPROC5 = NPROC3;
                                int NSRVS2 = in.readInt();
                                if (NSRVS2 < 0) {
                                    this.mReadError = "bad package service count: " + NSRVS2;
                                    return;
                                }
                                for (int NSRVS3 = NSRVS2; NSRVS3 > 0; NSRVS3 = NSRVS) {
                                    int NSRVS4 = NSRVS3 - 1;
                                    String serviceName2 = in.readString();
                                    if (serviceName2 == null) {
                                        this.mReadError = "bad package service name";
                                        return;
                                    }
                                    String processName = version > 9 ? readCommonString(in, version) : null;
                                    ServiceState serv2 = hadData ? pkgState2.mServices.get(serviceName2) : null;
                                    if (serv2 != null) {
                                        vers = vers4;
                                        serviceName = serviceName2;
                                        NSRVS = NSRVS4;
                                        pkgState = pkgState2;
                                        serv = serv2;
                                    } else {
                                        vers = vers4;
                                        serviceName = serviceName2;
                                        NSRVS = NSRVS4;
                                        pkgState = pkgState2;
                                        serv = new ServiceState(this, pkgName3, serviceName2, processName, null);
                                    }
                                    if (!serv.readFromParcel(in)) {
                                        return;
                                    }
                                    pkgState.mServices.put(serviceName, serv);
                                    pkgState2 = pkgState;
                                    vers4 = vers;
                                }
                                PackageState pkgState3 = pkgState2;
                                int NASCS = in.readInt();
                                if (NASCS < 0) {
                                    this.mReadError = "bad package association count: " + NASCS;
                                    return;
                                }
                                while (NASCS > 0) {
                                    int NASCS2 = NASCS - 1;
                                    String associationName2 = readCommonString(in, version);
                                    if (associationName2 == null) {
                                        this.mReadError = "bad package association name";
                                        return;
                                    }
                                    String processName2 = readCommonString(in, version);
                                    AssociationState asc2 = hadData ? pkgState3.mAssociations.get(associationName2) : null;
                                    if (asc2 != null) {
                                        uid = uid5;
                                        associationName = associationName2;
                                        asc = asc2;
                                    } else {
                                        uid = uid5;
                                        associationName = associationName2;
                                        asc = new AssociationState(this, pkgState3, associationName2, processName2, null);
                                    }
                                    String errorMsg = asc.readFromParcel(this, in, version);
                                    if (errorMsg != null) {
                                        this.mReadError = errorMsg;
                                        return;
                                    }
                                    pkgState3.mAssociations.put(associationName, asc);
                                    NASCS = NASCS2;
                                    uid5 = uid;
                                }
                                pkgName2 = pkgName3;
                                NVERS2 = NVERS3;
                                uid4 = uid5;
                                NPROC3 = NPROC5;
                            }
                            NVERS = NUID4;
                        }
                        NUID3 = NPKG;
                    }
                    int NPAGETYPES = in.readInt();
                    this.mPageTypeNodes.clear();
                    this.mPageTypeNodes.ensureCapacity(NPAGETYPES);
                    this.mPageTypeZones.clear();
                    this.mPageTypeZones.ensureCapacity(NPAGETYPES);
                    this.mPageTypeLabels.clear();
                    this.mPageTypeLabels.ensureCapacity(NPAGETYPES);
                    this.mPageTypeSizes.clear();
                    this.mPageTypeSizes.ensureCapacity(NPAGETYPES);
                    for (int i = 0; i < NPAGETYPES; i++) {
                        this.mPageTypeNodes.add(Integer.valueOf(in.readInt()));
                        this.mPageTypeZones.add(in.readString());
                        this.mPageTypeLabels.add(in.readString());
                        this.mPageTypeSizes.add(in.createIntArray());
                    }
                    this.mIndexToCommonString = null;
                    return;
                }
                this.mReadError = "bad process count: " + NPROC2;
                return;
            }
            return;
        }
        this.mReadError = "bad version: " + version;
    }

    public PackageState getPackageStateLocked(String packageName, int uid, long vers) {
        LongSparseArray<PackageState> vpkg = this.mPackages.get(packageName, uid);
        if (vpkg == null) {
            vpkg = new LongSparseArray<>();
            this.mPackages.put(packageName, uid, vpkg);
        }
        PackageState as = vpkg.get(vers);
        if (as != null) {
            return as;
        }
        PackageState as2 = new PackageState(this, packageName, uid, vers);
        vpkg.put(vers, as2);
        return as2;
    }

    public ProcessState getProcessStateLocked(String packageName, int uid, long vers, String processName) {
        return getProcessStateLocked(getPackageStateLocked(packageName, uid, vers), processName);
    }

    public ProcessState getProcessStateLocked(PackageState pkgState, String processName) {
        ProcessState commonProc;
        String str;
        ProcessState ps;
        ProcessState ps2 = pkgState.mProcesses.get(processName);
        if (ps2 != null) {
            return ps2;
        }
        ProcessState commonProc2 = this.mProcesses.get(processName, pkgState.mUid);
        if (commonProc2 != null) {
            commonProc = commonProc2;
        } else {
            ProcessState commonProc3 = new ProcessState(this, pkgState.mPackageName, pkgState.mUid, pkgState.mVersionCode, processName);
            this.mProcesses.put(processName, pkgState.mUid, commonProc3);
            commonProc = commonProc3;
        }
        if (!commonProc.isMultiPackage()) {
            if (pkgState.mPackageName.equals(commonProc.getPackage()) && pkgState.mVersionCode == commonProc.getVersion()) {
                ps = commonProc;
                str = processName;
            } else {
                commonProc.setMultiPackage(true);
                long now = SystemClock.uptimeMillis();
                PackageState commonPkgState = getPackageStateLocked(commonProc.getPackage(), pkgState.mUid, commonProc.getVersion());
                if (commonPkgState != null) {
                    ProcessState cloned = commonProc.clone(now);
                    commonPkgState.mProcesses.put(commonProc.getName(), cloned);
                    for (int i = commonPkgState.mServices.size() - 1; i >= 0; i--) {
                        ServiceState ss = commonPkgState.mServices.valueAt(i);
                        if (ss.getProcess() == commonProc) {
                            ss.setProcess(cloned);
                        }
                    }
                    for (int i2 = commonPkgState.mAssociations.size() - 1; i2 >= 0; i2--) {
                        AssociationState as = commonPkgState.mAssociations.valueAt(i2);
                        if (as.getProcess() == commonProc) {
                            as.setProcess(cloned);
                        }
                    }
                } else {
                    Slog.w(TAG, "Cloning proc state: no package state " + commonProc.getPackage() + "/" + pkgState.mUid + " for proc " + commonProc.getName());
                }
                str = processName;
                ps = new ProcessState(commonProc, pkgState.mPackageName, pkgState.mUid, pkgState.mVersionCode, processName, now);
            }
        } else {
            str = processName;
            ps = new ProcessState(commonProc, pkgState.mPackageName, pkgState.mUid, pkgState.mVersionCode, processName, SystemClock.uptimeMillis());
        }
        pkgState.mProcesses.put(str, ps);
        return ps;
    }

    public ServiceState getServiceStateLocked(String packageName, int uid, long vers, String processName, String className) {
        PackageState as = getPackageStateLocked(packageName, uid, vers);
        ServiceState ss = as.mServices.get(className);
        if (ss != null) {
            return ss;
        }
        ProcessState ps = processName != null ? getProcessStateLocked(packageName, uid, vers, processName) : null;
        ServiceState ss2 = new ServiceState(this, packageName, className, processName, ps);
        as.mServices.put(className, ss2);
        return ss2;
    }

    public AssociationState getAssociationStateLocked(String packageName, int uid, long vers, String processName, String className) {
        PackageState pkgs = getPackageStateLocked(packageName, uid, vers);
        AssociationState as = pkgs.mAssociations.get(className);
        if (as != null) {
            return as;
        }
        ProcessState procs = processName != null ? getProcessStateLocked(packageName, uid, vers, processName) : null;
        AssociationState as2 = new AssociationState(this, pkgs, className, processName, procs);
        pkgs.mAssociations.put(className, as2);
        return as2;
    }

    public void updateTrackingAssociationsLocked(int curSeq, long now) {
        int NUM = this.mTrackingAssociations.size();
        for (int i = NUM - 1; i >= 0; i--) {
            AssociationState.SourceState act = this.mTrackingAssociations.get(i);
            if (act.stopActiveIfNecessary(curSeq, now)) {
                this.mTrackingAssociations.remove(i);
            } else {
                AssociationState asc = act.getAssociationState();
                if (asc == null) {
                    Slog.wtf(TAG, act.toString() + " shouldn't be in the tracking list.");
                } else {
                    ProcessState proc = asc.getProcess();
                    if (proc == null) {
                        Slog.wtf(TAG, "Tracking association without process: " + act + " in " + asc);
                    } else {
                        int procState = proc.getCombinedState() % 16;
                        if (act.mProcState == procState) {
                            act.startActive(now);
                        } else {
                            act.stopActive(now);
                            if (act.mProcState < procState) {
                                long nowUptime = SystemClock.uptimeMillis();
                                if (this.mNextInverseProcStateWarningUptime > nowUptime) {
                                    this.mSkippedInverseProcStateWarningCount++;
                                } else {
                                    Slog.w(TAG, "Tracking association " + act + " whose proc state " + act.mProcState + " is better than process " + proc + " proc state " + procState + " (" + this.mSkippedInverseProcStateWarningCount + " skipped)");
                                    this.mSkippedInverseProcStateWarningCount = 0;
                                    this.mNextInverseProcStateWarningUptime = 10000 + nowUptime;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public final class AssociationDumpContainer {
        long mActiveTime;
        ArrayList<Pair<AssociationState.SourceKey, AssociationState.SourceDumpContainer>> mSources;
        final AssociationState mState;
        long mTotalTime;

        AssociationDumpContainer(AssociationState state) {
            this.mState = state;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ int lambda$static$0(AssociationDumpContainer o1, AssociationDumpContainer o2) {
        int diff = o1.mState.getProcessName().compareTo(o2.mState.getProcessName());
        if (diff != 0) {
            return diff;
        }
        if (o1.mActiveTime != o2.mActiveTime) {
            return o1.mActiveTime > o2.mActiveTime ? -1 : 1;
        } else if (o1.mTotalTime != o2.mTotalTime) {
            return o1.mTotalTime > o2.mTotalTime ? -1 : 1;
        } else {
            int diff2 = o1.mState.getName().compareTo(o2.mState.getName());
            if (diff2 != 0) {
                return diff2;
            }
            return 0;
        }
    }

    public void dumpLocked(PrintWriter pw, String reqPackage, long now, boolean dumpSummary, boolean dumpDetails, boolean dumpAll, boolean activeOnly, int section) {
        boolean partial;
        ProcessStats processStats;
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        long totalTime;
        ProcessStats processStats2;
        PrintWriter printWriter;
        int iu;
        String procName;
        SparseArray<ProcessState> uids;
        int ip;
        String str7;
        String str8;
        String str9;
        String str10;
        boolean printedHeader;
        int ip2;
        int iv;
        String str11;
        boolean printedHeader2;
        String pkgName;
        int uid;
        boolean printedHeader3;
        PackageState pkgState;
        String str12;
        int NSRVS;
        String str13;
        int NASCS;
        int ip3;
        String str14;
        long totalTime2;
        String str15;
        String str16;
        String str17;
        String str18;
        String str19;
        int iu2;
        ProcessStats processStats3;
        PackageState pkgState2;
        int NCONT;
        int iasc;
        ArrayList<AssociationDumpContainer> associations;
        int iu3;
        int NSRVS2;
        String str20;
        String str21;
        int isvc;
        String str22;
        String str23;
        String str24;
        int NPROCS;
        String str25;
        String str26;
        String str27;
        int iproc;
        int uid2;
        ProcessStats processStats4 = this;
        long totalTime3 = DumpUtils.dumpSingleTime(null, null, processStats4.mMemFactorDurations, processStats4.mMemFactor, processStats4.mStartTime, now);
        pw.print("          Start time: ");
        pw.print(DateFormat.format("yyyy-MM-dd HH:mm:ss", processStats4.mTimePeriodStartClock));
        pw.println();
        pw.print("        Total uptime: ");
        TimeUtils.formatDuration((processStats4.mRunning ? SystemClock.uptimeMillis() : processStats4.mTimePeriodEndUptime) - processStats4.mTimePeriodStartUptime, pw);
        pw.println();
        pw.print("  Total elapsed time: ");
        TimeUtils.formatDuration((processStats4.mRunning ? SystemClock.elapsedRealtime() : processStats4.mTimePeriodEndRealtime) - processStats4.mTimePeriodStartRealtime, pw);
        boolean partial2 = true;
        if ((processStats4.mFlags & 2) != 0) {
            pw.print(" (shutdown)");
            partial2 = false;
        }
        if ((processStats4.mFlags & 4) != 0) {
            pw.print(" (sysprops)");
            partial2 = false;
        }
        if ((processStats4.mFlags & 1) == 0) {
            partial = partial2;
        } else {
            pw.print(" (complete)");
            partial = false;
        }
        if (partial) {
            pw.print(" (partial)");
        }
        if (processStats4.mHasSwappedOutPss) {
            pw.print(" (swapped-out-pss)");
        }
        pw.print(' ');
        pw.print(processStats4.mRuntime);
        pw.println();
        pw.print("     Aggregated over: ");
        pw.println(processStats4.mNumAggregated);
        if (processStats4.mSysMemUsage.getKeyCount() > 0) {
            pw.println();
            pw.println("System memory usage:");
            processStats4.mSysMemUsage.dump(pw, "  ", ALL_SCREEN_ADJ, ALL_MEM_ADJ);
        }
        boolean printedHeader4 = false;
        int i = section & 14;
        String str28 = "      (Not active: ";
        String str29 = " entries)";
        String str30 = " / ";
        String str31 = "  * ";
        String str32 = ")";
        String str33 = SettingsStringUtil.DELIMITER;
        if (i == 0) {
            processStats = processStats4;
            str = str33;
            str2 = str32;
            str3 = str31;
            str4 = str30;
            str5 = str29;
            str6 = str28;
            totalTime = totalTime3;
        } else {
            ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = processStats4.mPackages.getMap();
            int ip4 = 0;
            while (true) {
                printedHeader = printedHeader4;
                if (ip4 >= pkgMap.size()) {
                    break;
                }
                String pkgName2 = pkgMap.keyAt(ip4);
                ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap2 = pkgMap;
                SparseArray<LongSparseArray<PackageState>> uids2 = pkgMap.valueAt(ip4);
                int iu4 = 0;
                while (iu4 < uids2.size()) {
                    int uid3 = uids2.keyAt(iu4);
                    int iu5 = iu4;
                    LongSparseArray<PackageState> vpkgs = uids2.valueAt(iu4);
                    SparseArray<LongSparseArray<PackageState>> uids3 = uids2;
                    int iv2 = 0;
                    while (true) {
                        ip2 = ip4;
                        int ip5 = vpkgs.size();
                        if (iv2 < ip5) {
                            long totalTime4 = totalTime3;
                            long vers = vpkgs.keyAt(iv2);
                            PackageState pkgState3 = vpkgs.valueAt(iv2);
                            LongSparseArray<PackageState> vpkgs2 = vpkgs;
                            int NPROCS2 = pkgState3.mProcesses.size();
                            String str34 = str32;
                            int NSRVS3 = pkgState3.mServices.size();
                            String str35 = str29;
                            int NASCS2 = pkgState3.mAssociations.size();
                            boolean pkgMatch = reqPackage == null || reqPackage.equals(pkgName2);
                            boolean onlyAssociations = false;
                            boolean procMatch = false;
                            if (pkgMatch) {
                                iv = iv2;
                                str11 = str28;
                            } else {
                                str11 = str28;
                                int iproc2 = 0;
                                while (true) {
                                    if (iproc2 >= NPROCS2) {
                                        iv = iv2;
                                        break;
                                    }
                                    iv = iv2;
                                    if (!reqPackage.equals(pkgState3.mProcesses.valueAt(iproc2).getName())) {
                                        iproc2++;
                                        iv2 = iv;
                                    } else {
                                        procMatch = true;
                                        break;
                                    }
                                }
                                if (!procMatch) {
                                    int iasc2 = 0;
                                    while (true) {
                                        if (iasc2 < NASCS2) {
                                            if (!pkgState3.mAssociations.valueAt(iasc2).hasProcessOrPackage(reqPackage)) {
                                                iasc2++;
                                            } else {
                                                onlyAssociations = true;
                                                break;
                                            }
                                        } else {
                                            break;
                                        }
                                    }
                                    if (!onlyAssociations) {
                                        processStats3 = this;
                                        pkgName = pkgName2;
                                        uid = uid3;
                                        str12 = str33;
                                        str13 = str31;
                                        ip3 = ip2;
                                        iu2 = iu5;
                                        str14 = str34;
                                        totalTime2 = totalTime4;
                                        str15 = str35;
                                        str18 = str11;
                                        str17 = str30;
                                        str32 = str14;
                                        str33 = str12;
                                        vpkgs = vpkgs2;
                                        str30 = str17;
                                        uid3 = uid;
                                        ip4 = ip3;
                                        str29 = str15;
                                        iu5 = iu2;
                                        str28 = str18;
                                        str31 = str13;
                                        totalTime3 = totalTime2;
                                        processStats4 = processStats3;
                                        iv2 = iv + 1;
                                        pkgName2 = pkgName;
                                    }
                                }
                            }
                            if (NPROCS2 > 0 || NSRVS3 > 0 || NASCS2 > 0) {
                                if (!printedHeader) {
                                    pw.println();
                                    pw.println("Per-Package Stats:");
                                    printedHeader = true;
                                }
                                pw.print(str31);
                                pw.print(pkgName2);
                                pw.print(str30);
                                UserHandle.formatUid(pw, uid3);
                                pw.print(" / v");
                                pw.print(vers);
                                pw.println(str33);
                                printedHeader2 = printedHeader;
                            } else {
                                printedHeader2 = printedHeader;
                            }
                            if ((section & 2) == 0 || onlyAssociations) {
                                pkgName = pkgName2;
                                uid = uid3;
                                printedHeader3 = printedHeader2;
                                pkgState = pkgState3;
                                str12 = str33;
                                NSRVS = NSRVS3;
                                str13 = str31;
                                NASCS = NASCS2;
                                ip3 = ip2;
                                str14 = str34;
                                totalTime2 = totalTime4;
                                str15 = str35;
                                str16 = str11;
                                str17 = str30;
                            } else {
                                if (!dumpSummary) {
                                    pkgName = pkgName2;
                                    uid = uid3;
                                    printedHeader3 = printedHeader2;
                                    pkgState = pkgState3;
                                    str22 = str33;
                                    NSRVS = NSRVS3;
                                    str13 = str31;
                                    NASCS = NASCS2;
                                    ip3 = ip2;
                                    str23 = str34;
                                    totalTime2 = totalTime4;
                                    str24 = str35;
                                    str16 = str11;
                                    str17 = str30;
                                } else if (dumpAll) {
                                    pkgName = pkgName2;
                                    uid = uid3;
                                    printedHeader3 = printedHeader2;
                                    pkgState = pkgState3;
                                    str22 = str33;
                                    NSRVS = NSRVS3;
                                    str13 = str31;
                                    NASCS = NASCS2;
                                    ip3 = ip2;
                                    str23 = str34;
                                    totalTime2 = totalTime4;
                                    str24 = str35;
                                    str16 = str11;
                                    str17 = str30;
                                } else {
                                    ArrayList<ProcessState> procs = new ArrayList<>();
                                    int iproc3 = 0;
                                    while (iproc3 < NPROCS2) {
                                        String pkgName3 = pkgName2;
                                        ProcessState proc = pkgState3.mProcesses.valueAt(iproc3);
                                        if (!pkgMatch) {
                                            uid2 = uid3;
                                            if (!reqPackage.equals(proc.getName())) {
                                                iproc3++;
                                                pkgName2 = pkgName3;
                                                uid3 = uid2;
                                            }
                                        } else {
                                            uid2 = uid3;
                                        }
                                        if (!activeOnly || proc.isInUse()) {
                                            procs.add(proc);
                                        }
                                        iproc3++;
                                        pkgName2 = pkgName3;
                                        uid3 = uid2;
                                    }
                                    pkgName = pkgName2;
                                    uid = uid3;
                                    printedHeader3 = printedHeader2;
                                    pkgState = pkgState3;
                                    ip3 = ip2;
                                    NSRVS = NSRVS3;
                                    str13 = str31;
                                    NASCS = NASCS2;
                                    str17 = str30;
                                    str16 = str11;
                                    totalTime2 = totalTime4;
                                    DumpUtils.dumpProcessSummaryLocked(pw, "      ", "Prc ", procs, ALL_SCREEN_ADJ, ALL_MEM_ADJ, NON_CACHED_PROC_STATES, now, totalTime2);
                                    str14 = str34;
                                    str15 = str35;
                                    str12 = str33;
                                }
                                int iproc4 = 0;
                                while (iproc4 < NPROCS2) {
                                    ProcessState proc2 = pkgState.mProcesses.valueAt(iproc4);
                                    if (!pkgMatch && !reqPackage.equals(proc2.getName())) {
                                        NPROCS = NPROCS2;
                                        iproc = iproc4;
                                        str26 = str23;
                                        String str36 = str22;
                                        str25 = str24;
                                        str27 = str36;
                                    } else if (!activeOnly || proc2.isInUse()) {
                                        String str37 = str23;
                                        pw.print("      Process ");
                                        pw.print(pkgState.mProcesses.keyAt(iproc4));
                                        if (proc2.getCommonProcess().isMultiPackage()) {
                                            pw.print(" (multi, ");
                                        } else {
                                            pw.print(" (unique, ");
                                        }
                                        pw.print(proc2.getDurationsBucketCount());
                                        pw.print(str24);
                                        String str38 = str22;
                                        pw.println(str38);
                                        int[] iArr = ALL_SCREEN_ADJ;
                                        int[] iArr2 = ALL_MEM_ADJ;
                                        int[] iArr3 = ALL_PROC_STATES;
                                        NPROCS = NPROCS2;
                                        str25 = str24;
                                        str26 = str37;
                                        str27 = str38;
                                        proc2.dumpProcessState(pw, "        ", iArr, iArr2, iArr3, now);
                                        proc2.dumpPss(pw, "        ", iArr, iArr2, iArr3, now);
                                        iproc = iproc4;
                                        proc2.dumpInternalLocked(pw, "        ", reqPackage, totalTime2, now, dumpAll);
                                    } else {
                                        pw.print(str16);
                                        pw.print(pkgState.mProcesses.keyAt(iproc4));
                                        String str39 = str23;
                                        pw.println(str39);
                                        NPROCS = NPROCS2;
                                        str26 = str39;
                                        iproc = iproc4;
                                        String str40 = str22;
                                        str25 = str24;
                                        str27 = str40;
                                    }
                                    iproc4 = iproc + 1;
                                    str23 = str26;
                                    NPROCS2 = NPROCS;
                                    String str41 = str25;
                                    str22 = str27;
                                    str24 = str41;
                                }
                                str14 = str23;
                                String str42 = str22;
                                str15 = str24;
                                str12 = str42;
                            }
                            String str43 = "        Process: ";
                            if ((section & 4) == 0 || onlyAssociations) {
                                str18 = str16;
                                str19 = str43;
                            } else {
                                int isvc2 = 0;
                                while (true) {
                                    int NSRVS4 = NSRVS;
                                    if (isvc2 >= NSRVS4) {
                                        break;
                                    }
                                    ServiceState svc = pkgState.mServices.valueAt(isvc2);
                                    if (!pkgMatch && !reqPackage.equals(svc.getProcessName())) {
                                        str20 = str16;
                                        NSRVS2 = NSRVS4;
                                        str21 = str43;
                                        isvc = isvc2;
                                    } else if (activeOnly && !svc.isInUse()) {
                                        pw.print("      (Not active service: ");
                                        pw.print(pkgState.mServices.keyAt(isvc2));
                                        pw.println(str14);
                                        str20 = str16;
                                        NSRVS2 = NSRVS4;
                                        str21 = str43;
                                        isvc = isvc2;
                                    } else {
                                        if (dumpAll) {
                                            pw.print("      Service ");
                                        } else {
                                            pw.print("      * Svc ");
                                        }
                                        pw.print(pkgState.mServices.keyAt(isvc2));
                                        pw.println(str12);
                                        pw.print(str43);
                                        pw.println(svc.getProcessName());
                                        NSRVS2 = NSRVS4;
                                        str20 = str16;
                                        str21 = str43;
                                        isvc = isvc2;
                                        svc.dumpStats(pw, "        ", "          ", "    ", now, totalTime2, dumpSummary, dumpAll);
                                    }
                                    isvc2 = isvc + 1;
                                    str43 = str21;
                                    NSRVS = NSRVS2;
                                    str16 = str20;
                                }
                                str18 = str16;
                                str19 = str43;
                            }
                            if ((section & 8) != 0) {
                                ArrayList<AssociationDumpContainer> associations2 = new ArrayList<>(NASCS);
                                int iasc3 = 0;
                                while (iasc3 < NASCS) {
                                    AssociationState asc = pkgState.mAssociations.valueAt(iasc3);
                                    if (!pkgMatch && !reqPackage.equals(asc.getProcessName())) {
                                        if (!onlyAssociations) {
                                            iu3 = iu5;
                                        } else if (!asc.hasProcessOrPackage(reqPackage)) {
                                            iu3 = iu5;
                                        }
                                        iasc3++;
                                        iu5 = iu3;
                                    }
                                    AssociationDumpContainer cont = new AssociationDumpContainer(asc);
                                    iu3 = iu5;
                                    cont.mSources = AssociationState.createSortedAssociations(now, totalTime2, asc.mSources);
                                    cont.mTotalTime = asc.getTotalDuration(now);
                                    cont.mActiveTime = asc.getActiveDuration(now);
                                    associations2.add(cont);
                                    iasc3++;
                                    iu5 = iu3;
                                }
                                iu2 = iu5;
                                Collections.sort(associations2, ASSOCIATION_COMPARATOR);
                                int NCONT2 = associations2.size();
                                int iasc4 = 0;
                                while (iasc4 < NCONT2) {
                                    AssociationDumpContainer cont2 = associations2.get(iasc4);
                                    AssociationState asc2 = cont2.mState;
                                    if (activeOnly && !asc2.isInUse()) {
                                        pw.print("      (Not active association: ");
                                        pw.print(pkgState.mAssociations.keyAt(iasc4));
                                        pw.println(str14);
                                        pkgState2 = pkgState;
                                        NCONT = NCONT2;
                                        iasc = iasc4;
                                        associations = associations2;
                                    } else {
                                        if (dumpAll) {
                                            pw.print("      Association ");
                                        } else {
                                            pw.print("      * Asc ");
                                        }
                                        pw.print(cont2.mState.getName());
                                        pw.println(str12);
                                        pw.print(str19);
                                        pw.println(asc2.getProcessName());
                                        pkgState2 = pkgState;
                                        NCONT = NCONT2;
                                        iasc = iasc4;
                                        associations = associations2;
                                        asc2.dumpStats(pw, "        ", "          ", "    ", cont2.mSources, now, totalTime2, (!onlyAssociations || pkgMatch || procMatch || asc2.getProcessName().equals(reqPackage)) ? null : reqPackage, dumpDetails, dumpAll);
                                    }
                                    iasc4 = iasc + 1;
                                    pkgState = pkgState2;
                                    NCONT2 = NCONT;
                                    associations2 = associations;
                                }
                                processStats3 = this;
                            } else {
                                iu2 = iu5;
                                processStats3 = this;
                            }
                            printedHeader = printedHeader3;
                            str32 = str14;
                            str33 = str12;
                            vpkgs = vpkgs2;
                            str30 = str17;
                            uid3 = uid;
                            ip4 = ip3;
                            str29 = str15;
                            iu5 = iu2;
                            str28 = str18;
                            str31 = str13;
                            totalTime3 = totalTime2;
                            processStats4 = processStats3;
                            iv2 = iv + 1;
                            pkgName2 = pkgName;
                        }
                    }
                    ip4 = ip2;
                    iu4 = iu5 + 1;
                    processStats4 = processStats4;
                    uids2 = uids3;
                    pkgName2 = pkgName2;
                }
                ip4++;
                printedHeader4 = printedHeader;
                processStats4 = processStats4;
                pkgMap = pkgMap2;
            }
            processStats = processStats4;
            str = str33;
            str2 = str32;
            str3 = str31;
            str4 = str30;
            str5 = str29;
            str6 = str28;
            totalTime = totalTime3;
            printedHeader4 = printedHeader;
        }
        if ((section & 1) != 0) {
            ArrayMap<String, SparseArray<ProcessState>> procMap = processStats.mProcesses.getMap();
            boolean printedHeader5 = false;
            int numShownProcs = 0;
            int numTotalProcs = 0;
            int ip6 = 0;
            while (ip6 < procMap.size()) {
                String procName2 = procMap.keyAt(ip6);
                SparseArray<ProcessState> uids4 = procMap.valueAt(ip6);
                int iu6 = 0;
                while (iu6 < uids4.size()) {
                    int uid4 = uids4.keyAt(iu6);
                    int numTotalProcs2 = numTotalProcs + 1;
                    ProcessState proc3 = uids4.valueAt(iu6);
                    if (!proc3.hasAnyData() || !proc3.isMultiPackage() || (reqPackage != null && !reqPackage.equals(procName2) && !reqPackage.equals(proc3.getPackage()))) {
                        iu = iu6;
                        procName = procName2;
                        uids = uids4;
                        ip = ip6;
                        str7 = str4;
                        str8 = str5;
                        str9 = str6;
                        str10 = str3;
                    } else {
                        int numShownProcs2 = numShownProcs + 1;
                        pw.println();
                        if (!printedHeader5) {
                            pw.println("Multi-Package Common Processes:");
                            printedHeader5 = true;
                        }
                        if (activeOnly && !proc3.isInUse()) {
                            String str44 = str6;
                            pw.print(str44);
                            pw.print(procName2);
                            pw.println(str2);
                            str9 = str44;
                            iu = iu6;
                            procName = procName2;
                            uids = uids4;
                            ip = ip6;
                            str7 = str4;
                            str8 = str5;
                            str10 = str3;
                        } else {
                            String str45 = str3;
                            pw.print(str45);
                            pw.print(procName2);
                            String str46 = str4;
                            pw.print(str46);
                            UserHandle.formatUid(pw, uid4);
                            pw.print(" (");
                            pw.print(proc3.getDurationsBucketCount());
                            String str47 = str5;
                            pw.print(str47);
                            pw.println(str);
                            int[] iArr4 = ALL_SCREEN_ADJ;
                            int[] iArr5 = ALL_MEM_ADJ;
                            int[] iArr6 = ALL_PROC_STATES;
                            str8 = str47;
                            str7 = str46;
                            str10 = str45;
                            str9 = str6;
                            iu = iu6;
                            procName = procName2;
                            uids = uids4;
                            proc3.dumpProcessState(pw, "        ", iArr4, iArr5, iArr6, now);
                            proc3.dumpPss(pw, "        ", iArr4, iArr5, iArr6, now);
                            ip = ip6;
                            proc3.dumpInternalLocked(pw, "        ", reqPackage, totalTime, now, dumpAll);
                        }
                        numShownProcs = numShownProcs2;
                    }
                    iu6 = iu + 1;
                    numTotalProcs = numTotalProcs2;
                    ip6 = ip;
                    str6 = str9;
                    str5 = str8;
                    str4 = str7;
                    str3 = str10;
                    procName2 = procName;
                    uids4 = uids;
                }
                ip6++;
            }
            pw.print("  Total procs: ");
            pw.print(numShownProcs);
            pw.print(" shown of ");
            pw.print(numTotalProcs);
            pw.println(" total");
        }
        if (dumpAll) {
            pw.println();
            if (processStats.mTrackingAssociations.size() > 0) {
                pw.println();
                pw.println("Tracking associations:");
                for (int i2 = 0; i2 < processStats.mTrackingAssociations.size(); i2++) {
                    AssociationState.SourceState src = processStats.mTrackingAssociations.get(i2);
                    AssociationState asc3 = src.getAssociationState();
                    if (asc3 == null) {
                        Slog.wtf(TAG, src.toString() + " shouldn't be in the tracking list.");
                    } else {
                        pw.print("  #");
                        pw.print(i2);
                        pw.print(": ");
                        pw.print(asc3.getProcessName());
                        pw.print("/");
                        UserHandle.formatUid(pw, asc3.getUid());
                        pw.print(" <- ");
                        pw.print(src.getProcessName());
                        pw.print("/");
                        UserHandle.formatUid(pw, src.getUid());
                        pw.println(str);
                        pw.print("    Tracking for: ");
                        TimeUtils.formatDuration(now - src.mTrackingUptime, pw);
                        pw.println();
                        pw.print("    Component: ");
                        pw.print(new ComponentName(asc3.getPackage(), asc3.getName()).flattenToShortString());
                        pw.println();
                        pw.print("    Proc state: ");
                        if (src.mProcState != -1) {
                            pw.print(DumpUtils.STATE_NAMES[src.mProcState]);
                        } else {
                            pw.print("--");
                        }
                        pw.print(" #");
                        pw.println(src.mProcStateSeq);
                        pw.print("    Process: ");
                        pw.println(asc3.getProcess());
                        if (src.mActiveCount > 0) {
                            pw.print("    Active count ");
                            pw.print(src.mActiveCount);
                            pw.print(": ");
                            AssociationState.dumpActiveDurationSummary(pw, src, totalTime, now, dumpAll);
                            pw.println();
                        }
                    }
                }
            }
        }
        pw.println();
        if (dumpSummary) {
            pw.println("Process summary:");
            processStats2 = processStats;
            printWriter = pw;
            dumpSummaryLocked(pw, reqPackage, now, activeOnly);
        } else {
            processStats2 = processStats;
            printWriter = pw;
            processStats2.dumpTotalsLocked(printWriter, now);
        }
        if (dumpAll) {
            pw.println();
            printWriter.println("Internal state:");
            printWriter.print("  mRunning=");
            printWriter.println(processStats2.mRunning);
        }
        if (reqPackage == null) {
            dumpFragmentationLocked(pw);
        }
    }

    public void dumpSummaryLocked(PrintWriter pw, String reqPackage, long now, boolean activeOnly) {
        long totalTime = DumpUtils.dumpSingleTime(null, null, this.mMemFactorDurations, this.mMemFactor, this.mStartTime, now);
        dumpFilteredSummaryLocked(pw, null, "  ", null, ALL_SCREEN_ADJ, ALL_MEM_ADJ, ALL_PROC_STATES, NON_CACHED_PROC_STATES, now, totalTime, reqPackage, activeOnly);
        pw.println();
        dumpTotalsLocked(pw, now);
    }

    private void dumpFragmentationLocked(PrintWriter pw) {
        pw.println();
        pw.println("Available pages by page size:");
        int NPAGETYPES = this.mPageTypeLabels.size();
        for (int i = 0; i < NPAGETYPES; i++) {
            pw.format("Node %3d Zone %7s  %14s ", this.mPageTypeNodes.get(i), this.mPageTypeZones.get(i), this.mPageTypeLabels.get(i));
            int[] sizes = this.mPageTypeSizes.get(i);
            int N = sizes == null ? 0 : sizes.length;
            for (int j = 0; j < N; j++) {
                pw.format("%6d", Integer.valueOf(sizes[j]));
            }
            pw.println();
        }
    }

    long printMemoryCategory(PrintWriter pw, String prefix, String label, double memWeight, long totalTime, long curTotalMem, int samples) {
        if (memWeight != 0.0d) {
            long mem = (long) ((1024.0d * memWeight) / totalTime);
            pw.print(prefix);
            pw.print(label);
            pw.print(": ");
            DebugUtils.printSizeValue(pw, mem);
            pw.print(" (");
            pw.print(samples);
            pw.print(" samples)");
            pw.println();
            return curTotalMem + mem;
        }
        return curTotalMem;
    }

    void dumpTotalsLocked(PrintWriter pw, long now) {
        int i;
        pw.println("Run time Stats:");
        DumpUtils.dumpSingleTime(pw, "  ", this.mMemFactorDurations, this.mMemFactor, this.mStartTime, now);
        pw.println();
        pw.println("Memory usage:");
        TotalMemoryUseCollection totalMem = new TotalMemoryUseCollection(ALL_SCREEN_ADJ, ALL_MEM_ADJ);
        computeTotalMemoryUse(totalMem, now);
        long totalPss = printMemoryCategory(pw, "  ", "Kernel ", totalMem.sysMemKernelWeight, totalMem.totalTime, 0L, totalMem.sysMemSamples);
        long totalPss2 = printMemoryCategory(pw, "  ", "Native ", totalMem.sysMemNativeWeight, totalMem.totalTime, totalPss, totalMem.sysMemSamples);
        int i2 = 0;
        while (i2 < 16) {
            if (i2 == 8) {
                i = i2;
            } else {
                i = i2;
                totalPss2 = printMemoryCategory(pw, "  ", DumpUtils.STATE_NAMES[i2], totalMem.processStateWeight[i2], totalMem.totalTime, totalPss2, totalMem.processStateSamples[i2]);
            }
            i2 = i + 1;
        }
        long totalPss3 = printMemoryCategory(pw, "  ", "Z-Ram  ", totalMem.sysMemZRamWeight, totalMem.totalTime, printMemoryCategory(pw, "  ", "Free   ", totalMem.sysMemFreeWeight, totalMem.totalTime, printMemoryCategory(pw, "  ", "Cached ", totalMem.sysMemCachedWeight, totalMem.totalTime, totalPss2, totalMem.sysMemSamples), totalMem.sysMemSamples), totalMem.sysMemSamples);
        pw.print("  TOTAL  : ");
        DebugUtils.printSizeValue(pw, totalPss3);
        pw.println();
        printMemoryCategory(pw, "  ", DumpUtils.STATE_NAMES[8], totalMem.processStateWeight[8], totalMem.totalTime, totalPss3, totalMem.processStateSamples[8]);
        pw.println();
        pw.println("PSS collection stats:");
        pw.print("  Internal Single: ");
        pw.print(this.mInternalSinglePssCount);
        pw.print("x over ");
        TimeUtils.formatDuration(this.mInternalSinglePssTime, pw);
        pw.println();
        pw.print("  Internal All Procs (Memory Change): ");
        pw.print(this.mInternalAllMemPssCount);
        pw.print("x over ");
        TimeUtils.formatDuration(this.mInternalAllMemPssTime, pw);
        pw.println();
        pw.print("  Internal All Procs (Polling): ");
        pw.print(this.mInternalAllPollPssCount);
        pw.print("x over ");
        TimeUtils.formatDuration(this.mInternalAllPollPssTime, pw);
        pw.println();
        pw.print("  External: ");
        pw.print(this.mExternalPssCount);
        pw.print("x over ");
        TimeUtils.formatDuration(this.mExternalPssTime, pw);
        pw.println();
        pw.print("  External Slow: ");
        pw.print(this.mExternalSlowPssCount);
        pw.print("x over ");
        TimeUtils.formatDuration(this.mExternalSlowPssTime, pw);
        pw.println();
    }

    void dumpFilteredSummaryLocked(PrintWriter pw, String header, String prefix, String prcLabel, int[] screenStates, int[] memStates, int[] procStates, int[] sortProcStates, long now, long totalTime, String reqPackage, boolean activeOnly) {
        ArrayList<ProcessState> procs = collectProcessesLocked(screenStates, memStates, procStates, sortProcStates, now, reqPackage, activeOnly);
        if (procs.size() > 0) {
            if (header != null) {
                pw.println();
                pw.println(header);
            }
            DumpUtils.dumpProcessSummaryLocked(pw, prefix, prcLabel, procs, screenStates, memStates, sortProcStates, now, totalTime);
        }
    }

    public ArrayList<ProcessState> collectProcessesLocked(int[] screenStates, int[] memStates, int[] procStates, int[] sortProcStates, long now, String reqPackage, boolean activeOnly) {
        ArraySet<ProcessState> foundProcs = new ArraySet<>();
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = this.mPackages.getMap();
        for (int ip = 0; ip < pkgMap.size(); ip++) {
            String pkgName = pkgMap.keyAt(ip);
            SparseArray<LongSparseArray<PackageState>> procs = pkgMap.valueAt(ip);
            for (int iu = 0; iu < procs.size(); iu++) {
                LongSparseArray<PackageState> vpkgs = procs.valueAt(iu);
                int NVERS = vpkgs.size();
                for (int iv = 0; iv < NVERS; iv++) {
                    PackageState state = vpkgs.valueAt(iv);
                    int NPROCS = state.mProcesses.size();
                    boolean pkgMatch = reqPackage == null || reqPackage.equals(pkgName);
                    for (int iproc = 0; iproc < NPROCS; iproc++) {
                        ProcessState proc = state.mProcesses.valueAt(iproc);
                        if ((pkgMatch || reqPackage.equals(proc.getName())) && (!activeOnly || proc.isInUse())) {
                            foundProcs.add(proc.getCommonProcess());
                        }
                    }
                }
            }
        }
        ArrayList<ProcessState> outProcs = new ArrayList<>(foundProcs.size());
        for (int i = 0; i < foundProcs.size(); i++) {
            ProcessState proc2 = foundProcs.valueAt(i);
            if (proc2.computeProcessTimeLocked(screenStates, memStates, procStates, now) > 0) {
                outProcs.add(proc2);
                if (procStates != sortProcStates) {
                    proc2.computeProcessTimeLocked(screenStates, memStates, sortProcStates, now);
                }
            }
        }
        Collections.sort(outProcs, ProcessState.COMPARATOR);
        return outProcs;
    }

    public void dumpCheckinLocked(PrintWriter pw, String reqPackage, int section) {
        boolean partial;
        String str;
        ProcessStats processStats;
        String str2;
        LongSparseArray<PackageState> vpkgs;
        SparseArray<LongSparseArray<PackageState>> uids;
        int iu;
        String pkgName;
        int NSRVS;
        int ip;
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap;
        String str3;
        PackageState pkgState;
        int NASCS;
        String str4 = reqPackage;
        long now = SystemClock.uptimeMillis();
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap2 = this.mPackages.getMap();
        pw.println("vers,5");
        pw.print("period,");
        pw.print(this.mTimePeriodStartClockStr);
        String str5 = ",";
        pw.print(str5);
        pw.print(this.mTimePeriodStartRealtime);
        pw.print(str5);
        pw.print(this.mRunning ? SystemClock.elapsedRealtime() : this.mTimePeriodEndRealtime);
        boolean partial2 = true;
        if ((this.mFlags & 2) != 0) {
            pw.print(",shutdown");
            partial2 = false;
        }
        if ((this.mFlags & 4) != 0) {
            pw.print(",sysprops");
            partial2 = false;
        }
        if ((this.mFlags & 1) == 0) {
            partial = partial2;
        } else {
            pw.print(",complete");
            partial = false;
        }
        if (partial) {
            pw.print(",partial");
        }
        if (this.mHasSwappedOutPss) {
            pw.print(",swapped-out-pss");
        }
        pw.println();
        pw.print("config,");
        pw.println(this.mRuntime);
        if ((section & 14) != 0) {
            int ip2 = 0;
            while (ip2 < pkgMap2.size()) {
                String pkgName2 = pkgMap2.keyAt(ip2);
                if (str4 == null || str4.equals(pkgName2)) {
                    SparseArray<LongSparseArray<PackageState>> uids2 = pkgMap2.valueAt(ip2);
                    int iu2 = 0;
                    while (iu2 < uids2.size()) {
                        int uid = uids2.keyAt(iu2);
                        LongSparseArray<PackageState> vpkgs2 = uids2.valueAt(iu2);
                        int iv = 0;
                        while (iv < vpkgs2.size()) {
                            long vers = vpkgs2.keyAt(iv);
                            PackageState pkgState2 = vpkgs2.valueAt(iv);
                            int NPROCS = pkgState2.mProcesses.size();
                            int NSRVS2 = pkgState2.mServices.size();
                            int iv2 = iv;
                            int NASCS2 = pkgState2.mAssociations.size();
                            if ((section & 2) != 0) {
                                int iproc = 0;
                                while (iproc < NPROCS) {
                                    ProcessState proc = pkgState2.mProcesses.valueAt(iproc);
                                    proc.dumpPackageProcCheckin(pw, pkgName2, uid, vers, pkgState2.mProcesses.keyAt(iproc), now);
                                    iproc++;
                                    NSRVS2 = NSRVS2;
                                    pkgName2 = pkgName2;
                                    pkgState2 = pkgState2;
                                    NASCS2 = NASCS2;
                                    ip2 = ip2;
                                    NPROCS = NPROCS;
                                    pkgMap2 = pkgMap2;
                                    str5 = str5;
                                    vpkgs2 = vpkgs2;
                                    uids2 = uids2;
                                    iu2 = iu2;
                                }
                                vpkgs = vpkgs2;
                                uids = uids2;
                                iu = iu2;
                                pkgName = pkgName2;
                                NSRVS = NSRVS2;
                                ip = ip2;
                                pkgMap = pkgMap2;
                                str3 = str5;
                                pkgState = pkgState2;
                                NASCS = NASCS2;
                            } else {
                                vpkgs = vpkgs2;
                                uids = uids2;
                                iu = iu2;
                                pkgName = pkgName2;
                                NSRVS = NSRVS2;
                                ip = ip2;
                                pkgMap = pkgMap2;
                                str3 = str5;
                                pkgState = pkgState2;
                                NASCS = NASCS2;
                            }
                            int NPROCS2 = section & 4;
                            if (NPROCS2 != 0) {
                                for (int isvc = 0; isvc < NSRVS; isvc++) {
                                    String serviceName = DumpUtils.collapseString(pkgName, pkgState.mServices.keyAt(isvc));
                                    ServiceState svc = pkgState.mServices.valueAt(isvc);
                                    svc.dumpTimesCheckin(pw, pkgName, uid, vers, serviceName, now);
                                }
                            }
                            if ((section & 8) != 0) {
                                for (int iasc = 0; iasc < NASCS; iasc++) {
                                    String associationName = DumpUtils.collapseString(pkgName, pkgState.mAssociations.keyAt(iasc));
                                    AssociationState asc = pkgState.mAssociations.valueAt(iasc);
                                    asc.dumpTimesCheckin(pw, pkgName, uid, vers, associationName, now);
                                }
                            }
                            iv = iv2 + 1;
                            pkgName2 = pkgName;
                            ip2 = ip;
                            pkgMap2 = pkgMap;
                            str5 = str3;
                            vpkgs2 = vpkgs;
                            uids2 = uids;
                            iu2 = iu;
                        }
                        iu2++;
                    }
                }
                ip2++;
                str4 = reqPackage;
                pkgMap2 = pkgMap2;
                str5 = str5;
            }
            str = str5;
        } else {
            str = str5;
        }
        if ((section & 1) == 0) {
            processStats = this;
        } else {
            processStats = this;
            ArrayMap<String, SparseArray<ProcessState>> procMap = processStats.mProcesses.getMap();
            for (int ip3 = 0; ip3 < procMap.size(); ip3++) {
                String procName = procMap.keyAt(ip3);
                SparseArray<ProcessState> uids3 = procMap.valueAt(ip3);
                for (int iu3 = 0; iu3 < uids3.size(); iu3++) {
                    int uid2 = uids3.keyAt(iu3);
                    ProcessState procState = uids3.valueAt(iu3);
                    procState.dumpProcCheckin(pw, procName, uid2, now);
                }
            }
        }
        pw.print("total");
        DumpUtils.dumpAdjTimesCheckin(pw, ",", processStats.mMemFactorDurations, processStats.mMemFactor, processStats.mStartTime, now);
        pw.println();
        int sysMemUsageCount = processStats.mSysMemUsage.getKeyCount();
        if (sysMemUsageCount <= 0) {
            str2 = str;
        } else {
            pw.print("sysmemusage");
            int i = 0;
            while (i < sysMemUsageCount) {
                int key = processStats.mSysMemUsage.getKeyAt(i);
                int type = SparseMappingTable.getIdFromKey(key);
                String str6 = str;
                pw.print(str6);
                DumpUtils.printProcStateTag(pw, type);
                for (int j = 0; j < 16; j++) {
                    if (j > 1) {
                        pw.print(SettingsStringUtil.DELIMITER);
                    }
                    pw.print(processStats.mSysMemUsage.getValue(key, j));
                }
                i++;
                str = str6;
            }
            str2 = str;
        }
        pw.println();
        TotalMemoryUseCollection totalMem = new TotalMemoryUseCollection(ALL_SCREEN_ADJ, ALL_MEM_ADJ);
        processStats.computeTotalMemoryUse(totalMem, now);
        pw.print("weights,");
        pw.print(totalMem.totalTime);
        pw.print(str2);
        pw.print(totalMem.sysMemCachedWeight);
        pw.print(SettingsStringUtil.DELIMITER);
        pw.print(totalMem.sysMemSamples);
        pw.print(str2);
        pw.print(totalMem.sysMemFreeWeight);
        pw.print(SettingsStringUtil.DELIMITER);
        pw.print(totalMem.sysMemSamples);
        pw.print(str2);
        pw.print(totalMem.sysMemZRamWeight);
        pw.print(SettingsStringUtil.DELIMITER);
        pw.print(totalMem.sysMemSamples);
        pw.print(str2);
        pw.print(totalMem.sysMemKernelWeight);
        pw.print(SettingsStringUtil.DELIMITER);
        pw.print(totalMem.sysMemSamples);
        pw.print(str2);
        pw.print(totalMem.sysMemNativeWeight);
        pw.print(SettingsStringUtil.DELIMITER);
        pw.print(totalMem.sysMemSamples);
        for (int i2 = 0; i2 < 16; i2++) {
            pw.print(str2);
            pw.print(totalMem.processStateWeight[i2]);
            pw.print(SettingsStringUtil.DELIMITER);
            pw.print(totalMem.processStateSamples[i2]);
        }
        pw.println();
        int NPAGETYPES = processStats.mPageTypeLabels.size();
        for (int i3 = 0; i3 < NPAGETYPES; i3++) {
            pw.print("availablepages,");
            pw.print(processStats.mPageTypeLabels.get(i3));
            pw.print(str2);
            pw.print(processStats.mPageTypeZones.get(i3));
            pw.print(str2);
            int[] sizes = processStats.mPageTypeSizes.get(i3);
            int N = sizes == null ? 0 : sizes.length;
            for (int j2 = 0; j2 < N; j2++) {
                if (j2 != 0) {
                    pw.print(str2);
                }
                pw.print(sizes[j2]);
            }
            pw.println();
        }
    }

    public void dumpDebug(ProtoOutputStream proto, long now, int section) {
        dumpProtoPreamble(proto);
        int NPAGETYPES = this.mPageTypeLabels.size();
        for (int i = 0; i < NPAGETYPES; i++) {
            long token = proto.start(2246267895818L);
            proto.write(1120986464257L, this.mPageTypeNodes.get(i).intValue());
            proto.write(1138166333442L, this.mPageTypeZones.get(i));
            proto.write(1138166333443L, this.mPageTypeLabels.get(i));
            int[] sizes = this.mPageTypeSizes.get(i);
            int N = sizes == null ? 0 : sizes.length;
            for (int j = 0; j < N; j++) {
                proto.write(2220498092036L, sizes[j]);
            }
            proto.end(token);
        }
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        if ((section & 1) != 0) {
            for (int ip = 0; ip < procMap.size(); ip++) {
                String procName = procMap.keyAt(ip);
                SparseArray<ProcessState> uids = procMap.valueAt(ip);
                for (int iu = 0; iu < uids.size(); iu++) {
                    int uid = uids.keyAt(iu);
                    ProcessState procState = uids.valueAt(iu);
                    procState.dumpDebug(proto, 2246267895816L, procName, uid, now);
                }
            }
        }
        if ((section & 14) != 0) {
            ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = this.mPackages.getMap();
            for (int ip2 = 0; ip2 < pkgMap.size(); ip2++) {
                SparseArray<LongSparseArray<PackageState>> uids2 = pkgMap.valueAt(ip2);
                for (int iu2 = 0; iu2 < uids2.size(); iu2++) {
                    LongSparseArray<PackageState> vers = uids2.valueAt(iu2);
                    for (int iv = 0; iv < vers.size(); iv++) {
                        PackageState pkgState = vers.valueAt(iv);
                        pkgState.dumpDebug(proto, 2246267895817L, now, section);
                    }
                }
            }
        }
    }

    public void dumpAggregatedProtoForStatsd(ProtoOutputStream[] protoStreams, long maxRawShardSizeBytes) {
        int shardIndex = 0;
        dumpProtoPreamble(protoStreams[0]);
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        ProcessMap<ArraySet<PackageState>> procToPkgMap = new ProcessMap<>();
        SparseArray<ArraySet<String>> uidToPkgMap = new SparseArray<>();
        char c = 0;
        collectProcessPackageMaps(null, false, procToPkgMap, uidToPkgMap);
        int ip = 0;
        while (true) {
            if (ip >= procMap.size()) {
                break;
            }
            String procName = procMap.keyAt(ip);
            if (protoStreams[shardIndex].getRawSize() > maxRawShardSizeBytes) {
                shardIndex++;
                if (shardIndex < protoStreams.length) {
                    dumpProtoPreamble(protoStreams[shardIndex]);
                } else {
                    Object[] objArr = new Object[2];
                    objArr[c] = Integer.valueOf(ip);
                    objArr[1] = Integer.valueOf(procMap.size());
                    Slog.d(TAG, String.format("Dropping process indices from %d to %d from statsd proto (too large)", objArr));
                    break;
                }
            }
            int iu = 0;
            for (SparseArray<ProcessState> uids = procMap.valueAt(ip); iu < uids.size(); uids = uids) {
                int uid = uids.keyAt(iu);
                ProcessState procState = uids.valueAt(iu);
                procState.dumpAggregatedProtoForStatsd(protoStreams[shardIndex], 2246267895816L, procName, uid, this.mTimePeriodEndRealtime, procToPkgMap, uidToPkgMap);
                iu++;
                ip = ip;
                c = c;
            }
            ip++;
        }
        for (int i = 0; i <= shardIndex; i++) {
            protoStreams[i].flush();
        }
    }

    private void dumpProtoPreamble(ProtoOutputStream proto) {
        proto.write(1112396529665L, this.mTimePeriodStartRealtime);
        proto.write(1112396529666L, this.mRunning ? SystemClock.elapsedRealtime() : this.mTimePeriodEndRealtime);
        proto.write(1112396529667L, this.mTimePeriodStartUptime);
        proto.write(1112396529668L, this.mTimePeriodEndUptime);
        proto.write(1138166333445L, this.mRuntime);
        proto.write(1133871366150L, this.mHasSwappedOutPss);
        boolean partial = true;
        if ((this.mFlags & 2) != 0) {
            proto.write(2259152797703L, 3);
            partial = false;
        }
        if ((this.mFlags & 4) != 0) {
            proto.write(2259152797703L, 4);
            partial = false;
        }
        if ((this.mFlags & 1) != 0) {
            proto.write(2259152797703L, 1);
            partial = false;
        }
        if (partial) {
            proto.write(2259152797703L, 2);
        }
    }

    private void collectProcessPackageMaps(String reqPackage, boolean activeOnly, ProcessMap<ArraySet<PackageState>> procToPkgMap, SparseArray<ArraySet<String>> uidToPkgMap) {
        ArraySet<PackageState> pkgStates;
        ArraySet<String> packages;
        String str = reqPackage;
        ProcessMap<ArraySet<PackageState>> processMap = procToPkgMap;
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = this.mPackages.getMap();
        int i = 1;
        int ip = pkgMap.size() - 1;
        while (ip >= 0) {
            String pkgName = pkgMap.keyAt(ip);
            SparseArray<LongSparseArray<PackageState>> procs = pkgMap.valueAt(ip);
            int iu = procs.size() - i;
            while (iu >= 0) {
                LongSparseArray<PackageState> vpkgs = procs.valueAt(iu);
                int iv = vpkgs.size() - i;
                while (iv >= 0) {
                    PackageState state = vpkgs.valueAt(iv);
                    int i2 = (str == null || str.equals(pkgName)) ? i : 0;
                    int iproc = state.mProcesses.size() - i;
                    while (iproc >= 0) {
                        ProcessState proc = state.mProcesses.valueAt(iproc);
                        if ((i2 != 0 || str.equals(proc.getName())) && (!activeOnly || proc.isInUse())) {
                            String name = proc.getName();
                            int uid = proc.getUid();
                            ArraySet<PackageState> pkgStates2 = processMap.get(name, uid);
                            if (pkgStates2 != null) {
                                pkgStates = pkgStates2;
                            } else {
                                pkgStates = new ArraySet<>();
                                processMap.put(name, uid, pkgStates);
                            }
                            pkgStates.add(state);
                            ArraySet<String> packages2 = uidToPkgMap.get(uid);
                            if (packages2 != null) {
                                packages = packages2;
                            } else {
                                packages = new ArraySet<>();
                                uidToPkgMap.put(uid, packages);
                            }
                            packages.add(state.mPackageName);
                        }
                        iproc--;
                        str = reqPackage;
                        processMap = procToPkgMap;
                    }
                    iv--;
                    str = reqPackage;
                    processMap = procToPkgMap;
                    i = 1;
                }
                iu--;
                str = reqPackage;
                processMap = procToPkgMap;
                i = 1;
            }
            ip--;
            str = reqPackage;
            processMap = procToPkgMap;
            i = 1;
        }
    }

    public void dumpFilteredAssociationStatesProtoForProc(ProtoOutputStream proto, long fieldId, long now, ProcessState procState, SparseArray<ArraySet<String>> uidToPkgMap) {
        ArrayMap<AssociationState.SourceKey, AssociationState.SourceState> sources;
        IProcessStats procStatsService;
        long duration;
        int idx;
        boolean z;
        int i;
        if ((!procState.isMultiPackage() || procState.getCommonProcess() == procState) && (sources = procState.mCommonSources) != null && !sources.isEmpty() && (procStatsService = IProcessStats.Stub.asInterface(ServiceManager.getService(SERVICE_NAME))) != null) {
            try {
                long minimum = procStatsService.getMinAssociationDumpDuration();
                int i2 = 1;
                int i3 = sources.size() - 1;
                while (i3 >= 0) {
                    AssociationState.SourceState src = sources.valueAt(i3);
                    long duration2 = src.mDuration;
                    if (src.mNesting <= 0) {
                        duration = duration2;
                    } else {
                        duration = duration2 + (now - src.mStartUptime);
                    }
                    if (duration < minimum) {
                        i = i2;
                    } else {
                        AssociationState.SourceKey key = sources.keyAt(i3);
                        long token = proto.start(fieldId);
                        int idx2 = uidToPkgMap.indexOfKey(key.mUid);
                        String str = key.mProcess;
                        String str2 = key.mPackage;
                        if (idx2 >= 0) {
                            idx = 1;
                            if (uidToPkgMap.valueAt(idx2).size() > 1) {
                                z = true;
                                i = idx;
                                ProcessState.writeCompressedProcessName(proto, 1138166333441L, str, str2, z);
                                proto.write(1120986464261L, key.mUid);
                                proto.write(1120986464259L, src.mCount);
                                proto.write(1120986464260L, (int) (duration / 1000));
                                proto.end(token);
                            }
                        } else {
                            idx = i2;
                        }
                        z = false;
                        i = idx;
                        ProcessState.writeCompressedProcessName(proto, 1138166333441L, str, str2, z);
                        proto.write(1120986464261L, key.mUid);
                        proto.write(1120986464259L, src.mCount);
                        proto.write(1120986464260L, (int) (duration / 1000));
                        proto.end(token);
                    }
                    i3--;
                    i2 = i;
                }
            } catch (RemoteException e) {
            }
        }
    }

    /* loaded from: classes4.dex */
    public static final class ProcessStateHolder {
        public final long appVersion;
        public PackageState pkg;
        public ProcessState state;

        public ProcessStateHolder(long _appVersion) {
            this.appVersion = _appVersion;
        }
    }

    /* loaded from: classes4.dex */
    public static final class PackageState {
        public final String mPackageName;
        public final ProcessStats mProcessStats;
        public final int mUid;
        public final long mVersionCode;
        public final ArrayMap<String, ProcessState> mProcesses = new ArrayMap<>();
        public final ArrayMap<String, ServiceState> mServices = new ArrayMap<>();
        public final ArrayMap<String, AssociationState> mAssociations = new ArrayMap<>();

        public PackageState(ProcessStats procStats, String packageName, int uid, long versionCode) {
            this.mProcessStats = procStats;
            this.mUid = uid;
            this.mPackageName = packageName;
            this.mVersionCode = versionCode;
        }

        public AssociationState getAssociationStateLocked(ProcessState proc, String className) {
            AssociationState as = this.mAssociations.get(className);
            if (as != null) {
                if (proc != null) {
                    as.setProcess(proc);
                }
                return as;
            }
            AssociationState as2 = new AssociationState(this.mProcessStats, this, className, proc.getName(), proc);
            this.mAssociations.put(className, as2);
            return as2;
        }

        public void dumpDebug(ProtoOutputStream proto, long fieldId, long now, int section) {
            long token = proto.start(fieldId);
            proto.write(1138166333441L, this.mPackageName);
            proto.write(1120986464258L, this.mUid);
            proto.write(1112396529667L, this.mVersionCode);
            if ((section & 2) != 0) {
                for (int ip = 0; ip < this.mProcesses.size(); ip++) {
                    String procName = this.mProcesses.keyAt(ip);
                    ProcessState procState = this.mProcesses.valueAt(ip);
                    procState.dumpDebug(proto, 2246267895812L, procName, this.mUid, now);
                }
            }
            if ((section & 4) != 0) {
                for (int is = 0; is < this.mServices.size(); is++) {
                    ServiceState serviceState = this.mServices.valueAt(is);
                    serviceState.dumpDebug(proto, 2246267895813L, now);
                }
            }
            if ((section & 8) != 0) {
                for (int ia = 0; ia < this.mAssociations.size(); ia++) {
                    AssociationState ascState = this.mAssociations.valueAt(ia);
                    ascState.dumpDebug(proto, 2246267895814L, now);
                }
            }
            proto.end(token);
        }
    }

    /* loaded from: classes4.dex */
    public static final class ProcessDataCollection {
        public long avgPss;
        public long avgRss;
        public long avgUss;
        public long maxPss;
        public long maxRss;
        public long maxUss;
        final int[] memStates;
        public long minPss;
        public long minRss;
        public long minUss;
        public long numPss;
        final int[] procStates;
        final int[] screenStates;
        public long totalTime;

        public ProcessDataCollection(int[] _screenStates, int[] _memStates, int[] _procStates) {
            this.screenStates = _screenStates;
            this.memStates = _memStates;
            this.procStates = _procStates;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void print(PrintWriter pw, long overallTime, boolean full) {
            if (this.totalTime > overallTime) {
                pw.print("*");
            }
            DumpUtils.printPercent(pw, this.totalTime / overallTime);
            if (this.numPss > 0) {
                pw.print(" (");
                DebugUtils.printSizeValue(pw, this.minPss * 1024);
                pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                DebugUtils.printSizeValue(pw, this.avgPss * 1024);
                pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                DebugUtils.printSizeValue(pw, this.maxPss * 1024);
                pw.print("/");
                DebugUtils.printSizeValue(pw, this.minUss * 1024);
                pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                DebugUtils.printSizeValue(pw, this.avgUss * 1024);
                pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                DebugUtils.printSizeValue(pw, this.maxUss * 1024);
                pw.print("/");
                DebugUtils.printSizeValue(pw, this.minRss * 1024);
                pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                DebugUtils.printSizeValue(pw, this.avgRss * 1024);
                pw.print(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
                DebugUtils.printSizeValue(pw, this.maxRss * 1024);
                if (full) {
                    pw.print(" over ");
                    pw.print(this.numPss);
                }
                pw.print(")");
            }
        }
    }

    /* loaded from: classes4.dex */
    public static class TotalMemoryUseCollection {
        public boolean hasSwappedOutPss;
        final int[] memStates;
        final int[] screenStates;
        public double sysMemCachedWeight;
        public double sysMemFreeWeight;
        public double sysMemKernelWeight;
        public double sysMemNativeWeight;
        public int sysMemSamples;
        public double sysMemZRamWeight;
        public long totalTime;
        public long[] processStatePss = new long[16];
        public double[] processStateWeight = new double[16];
        public long[] processStateTime = new long[16];
        public int[] processStateSamples = new int[16];
        public long[] sysMemUsage = new long[16];

        public TotalMemoryUseCollection(int[] _screenStates, int[] _memStates) {
            this.screenStates = _screenStates;
            this.memStates = _memStates;
        }
    }
}
