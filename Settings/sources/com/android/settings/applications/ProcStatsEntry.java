package com.android.settings.applications;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.LongSparseArray;
import com.android.internal.app.procstats.ProcessState;
import com.android.internal.app.procstats.ProcessStats;
import com.android.internal.app.procstats.ServiceState;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
/* loaded from: classes.dex */
public final class ProcStatsEntry implements Parcelable {
    public static final Parcelable.Creator<ProcStatsEntry> CREATOR = new Parcelable.Creator<ProcStatsEntry>() { // from class: com.android.settings.applications.ProcStatsEntry.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ProcStatsEntry mo231createFromParcel(Parcel parcel) {
            return new ProcStatsEntry(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ProcStatsEntry[] mo232newArray(int i) {
            return new ProcStatsEntry[i];
        }
    };
    private static boolean DEBUG = false;
    final long mAvgBgMem;
    final long mAvgRunMem;
    String mBestTargetPackage;
    final long mBgDuration;
    final double mBgWeight;
    public CharSequence mLabel;
    final long mMaxBgMem;
    final long mMaxRunMem;
    final String mName;
    final String mPackage;
    final ArrayList<String> mPackages;
    final long mRunDuration;
    final double mRunWeight;
    ArrayMap<String, ArrayList<Service>> mServices;
    final int mUid;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public ProcStatsEntry(ProcessState processState, String str, ProcessStats.ProcessDataCollection processDataCollection, ProcessStats.ProcessDataCollection processDataCollection2, boolean z) {
        ArrayList<String> arrayList = new ArrayList<>();
        this.mPackages = arrayList;
        this.mServices = new ArrayMap<>(1);
        processState.computeProcessData(processDataCollection, 0L);
        processState.computeProcessData(processDataCollection2, 0L);
        this.mPackage = processState.getPackage();
        this.mUid = processState.getUid();
        this.mName = processState.getName();
        arrayList.add(str);
        long j = processDataCollection.totalTime;
        this.mBgDuration = j;
        long j2 = z ? processDataCollection.avgUss : processDataCollection.avgPss;
        this.mAvgBgMem = j2;
        this.mMaxBgMem = z ? processDataCollection.maxUss : processDataCollection.maxPss;
        double d = j2 * j;
        this.mBgWeight = d;
        long j3 = processDataCollection2.totalTime;
        this.mRunDuration = j3;
        long j4 = z ? processDataCollection2.avgUss : processDataCollection2.avgPss;
        this.mAvgRunMem = j4;
        this.mMaxRunMem = z ? processDataCollection2.maxUss : processDataCollection2.maxPss;
        this.mRunWeight = j4 * j3;
        if (DEBUG) {
            Log.d("ProcStatsEntry", "New proc entry " + processState.getName() + ": dur=" + j + " avgpss=" + j2 + " weight=" + d);
        }
    }

    public ProcStatsEntry(String str, int i, String str2, long j, long j2, long j3) {
        this.mPackages = new ArrayList<>();
        this.mServices = new ArrayMap<>(1);
        this.mPackage = str;
        this.mUid = i;
        this.mName = str2;
        this.mRunDuration = j;
        this.mBgDuration = j;
        this.mMaxRunMem = j2;
        this.mAvgRunMem = j2;
        this.mMaxBgMem = j2;
        this.mAvgBgMem = j2;
        double d = j3 * j2;
        this.mRunWeight = d;
        this.mBgWeight = d;
        if (DEBUG) {
            Log.d("ProcStatsEntry", "New proc entry " + str2 + ": dur=" + j + " avgpss=" + j2 + " weight=" + d);
        }
    }

    public ProcStatsEntry(Parcel parcel) {
        ArrayList<String> arrayList = new ArrayList<>();
        this.mPackages = arrayList;
        this.mServices = new ArrayMap<>(1);
        this.mPackage = parcel.readString();
        this.mUid = parcel.readInt();
        this.mName = parcel.readString();
        parcel.readStringList(arrayList);
        this.mBgDuration = parcel.readLong();
        this.mAvgBgMem = parcel.readLong();
        this.mMaxBgMem = parcel.readLong();
        this.mBgWeight = parcel.readDouble();
        this.mRunDuration = parcel.readLong();
        this.mAvgRunMem = parcel.readLong();
        this.mMaxRunMem = parcel.readLong();
        this.mRunWeight = parcel.readDouble();
        this.mBestTargetPackage = parcel.readString();
        int readInt = parcel.readInt();
        if (readInt > 0) {
            this.mServices.ensureCapacity(readInt);
            for (int i = 0; i < readInt; i++) {
                String readString = parcel.readString();
                ArrayList arrayList2 = new ArrayList();
                parcel.readTypedList(arrayList2, Service.CREATOR);
                this.mServices.append(readString, arrayList2);
            }
        }
    }

    public void addPackage(String str) {
        this.mPackages.add(str);
    }

    /* JADX WARN: Removed duplicated region for block: B:128:0x0388  */
    /* JADX WARN: Removed duplicated region for block: B:133:0x03b6  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x03f1  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x0412 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void evaluateTargetPackage(PackageManager packageManager, ProcessStats processStats, ProcessStats.ProcessDataCollection processDataCollection, ProcessStats.ProcessDataCollection processDataCollection2, Comparator<ProcStatsEntry> comparator, boolean z) {
        int i;
        String str;
        double d;
        ApplicationInfo applicationInfo;
        ArrayList<Service> arrayList;
        long j;
        this.mBestTargetPackage = null;
        int i2 = 0;
        if (this.mPackages.size() == 1) {
            if (DEBUG) {
                Log.d("ProcStatsEntry", "Eval pkg of " + this.mName + ": single pkg " + this.mPackages.get(0));
            }
            this.mBestTargetPackage = this.mPackages.get(0);
            return;
        }
        for (int i3 = 0; i3 < this.mPackages.size(); i3++) {
            if ("android".equals(this.mPackages.get(i3))) {
                this.mBestTargetPackage = this.mPackages.get(i3);
                return;
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (int i4 = 0; i4 < this.mPackages.size(); i4++) {
            LongSparseArray longSparseArray = (LongSparseArray) processStats.mPackages.get(this.mPackages.get(i4), this.mUid);
            for (int i5 = 0; i5 < longSparseArray.size(); i5++) {
                ProcessStats.PackageState packageState = (ProcessStats.PackageState) longSparseArray.valueAt(i5);
                if (DEBUG) {
                    Log.d("ProcStatsEntry", "Eval pkg of " + this.mName + ", pkg " + packageState + ":");
                }
                if (packageState == null) {
                    Log.w("ProcStatsEntry", "No package state found for " + this.mPackages.get(i4) + "/" + this.mUid + " in process " + this.mName);
                } else {
                    ProcessState processState = (ProcessState) packageState.mProcesses.get(this.mName);
                    if (processState == null) {
                        Log.w("ProcStatsEntry", "No process " + this.mName + " found in package state " + this.mPackages.get(i4) + "/" + this.mUid);
                    } else {
                        arrayList2.add(new ProcStatsEntry(processState, packageState.mPackageName, processDataCollection, processDataCollection2, z));
                    }
                }
            }
        }
        if (arrayList2.size() > 1) {
            Collections.sort(arrayList2, comparator);
            String str2 = " weight ";
            if (((ProcStatsEntry) arrayList2.get(0)).mRunWeight > ((ProcStatsEntry) arrayList2.get(1)).mRunWeight * 3.0d) {
                if (DEBUG) {
                    Log.d("ProcStatsEntry", "Eval pkg of " + this.mName + ": best pkg " + ((ProcStatsEntry) arrayList2.get(0)).mPackage + str2 + ((ProcStatsEntry) arrayList2.get(0)).mRunWeight + " better than " + ((ProcStatsEntry) arrayList2.get(1)).mPackage + str2 + ((ProcStatsEntry) arrayList2.get(1)).mRunWeight);
                }
                this.mBestTargetPackage = ((ProcStatsEntry) arrayList2.get(0)).mPackage;
                return;
            }
            double d2 = ((ProcStatsEntry) arrayList2.get(0)).mRunWeight;
            long j2 = -1;
            int i6 = 0;
            boolean z2 = false;
            while (i6 < arrayList2.size()) {
                ProcStatsEntry procStatsEntry = (ProcStatsEntry) arrayList2.get(i6);
                ArrayList arrayList3 = arrayList2;
                if (procStatsEntry.mRunWeight < d2 / 2.0d) {
                    if (DEBUG) {
                        Log.d("ProcStatsEntry", "Eval pkg of " + this.mName + ": pkg " + procStatsEntry.mPackage + str2 + procStatsEntry.mRunWeight + " too small");
                    }
                } else {
                    try {
                        try {
                            applicationInfo = packageManager.getApplicationInfo(procStatsEntry.mPackage, i2);
                        } catch (PackageManager.NameNotFoundException unused) {
                            i = i6;
                            str = str2;
                            d = d2;
                            if (DEBUG) {
                            }
                            i6 = i + 1;
                            str2 = str;
                            arrayList2 = arrayList3;
                            d2 = d;
                            i2 = 0;
                        }
                    } catch (PackageManager.NameNotFoundException unused2) {
                    }
                    if (applicationInfo.icon == 0) {
                        if (DEBUG) {
                            Log.d("ProcStatsEntry", "Eval pkg of " + this.mName + ": pkg " + procStatsEntry.mPackage + " has no icon");
                        }
                    } else {
                        if ((applicationInfo.flags & 8) != 0) {
                            i = i6;
                            try {
                                long j3 = procStatsEntry.mRunDuration;
                                if (z2 && j3 <= j2) {
                                    if (DEBUG) {
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("Eval pkg of ");
                                        str = str2;
                                        try {
                                            sb.append(this.mName);
                                            sb.append(": pkg ");
                                            sb.append(procStatsEntry.mPackage);
                                            sb.append(" pers run time ");
                                            sb.append(j3);
                                            sb.append(" not as good as last ");
                                            sb.append(j2);
                                            Log.d("ProcStatsEntry", sb.toString());
                                        } catch (PackageManager.NameNotFoundException unused3) {
                                            d = d2;
                                            if (DEBUG) {
                                            }
                                            i6 = i + 1;
                                            str2 = str;
                                            arrayList2 = arrayList3;
                                            d2 = d;
                                            i2 = 0;
                                        }
                                    }
                                    str = str2;
                                }
                                str = str2;
                                if (DEBUG) {
                                    Log.d("ProcStatsEntry", "Eval pkg of " + this.mName + ": pkg " + procStatsEntry.mPackage + " new best pers run time " + j3);
                                }
                                j2 = j3;
                                z2 = true;
                            } catch (PackageManager.NameNotFoundException unused4) {
                                str = str2;
                                d = d2;
                                if (DEBUG) {
                                    Log.d("ProcStatsEntry", "Eval pkg of " + this.mName + ": pkg " + procStatsEntry.mPackage + " failed finding app info");
                                }
                                i6 = i + 1;
                                str2 = str;
                                arrayList2 = arrayList3;
                                d2 = d;
                                i2 = 0;
                            }
                        } else {
                            i = i6;
                            str = str2;
                            if (z2) {
                                if (DEBUG) {
                                    Log.d("ProcStatsEntry", "Eval pkg of " + this.mName + ": pkg " + procStatsEntry.mPackage + " is not persistent");
                                }
                            } else {
                                int size = this.mServices.size();
                                int i7 = 0;
                                while (true) {
                                    if (i7 >= size) {
                                        arrayList = null;
                                        break;
                                    }
                                    arrayList = this.mServices.valueAt(i7);
                                    int i8 = size;
                                    if (arrayList.get(0).mPackage.equals(procStatsEntry.mPackage)) {
                                        break;
                                    }
                                    i7++;
                                    size = i8;
                                }
                                if (arrayList != null) {
                                    int size2 = arrayList.size();
                                    int i9 = 0;
                                    while (i9 < size2) {
                                        Service service = arrayList.get(i9);
                                        d = d2;
                                        ArrayList<Service> arrayList4 = arrayList;
                                        if (service.mDuration > 0) {
                                            if (DEBUG) {
                                                Log.d("ProcStatsEntry", "Eval pkg of " + this.mName + ": pkg " + procStatsEntry.mPackage + " service " + service.mName + " run time is " + service.mDuration);
                                            }
                                            j = service.mDuration;
                                            if (j <= j2) {
                                                if (DEBUG) {
                                                    Log.d("ProcStatsEntry", "Eval pkg of " + this.mName + ": pkg " + procStatsEntry.mPackage + " new best run time " + j);
                                                }
                                                this.mBestTargetPackage = procStatsEntry.mPackage;
                                                j2 = j;
                                            } else if (DEBUG) {
                                                Log.d("ProcStatsEntry", "Eval pkg of " + this.mName + ": pkg " + procStatsEntry.mPackage + " run time " + j + " not as good as last " + j2);
                                            }
                                            i6 = i + 1;
                                            str2 = str;
                                            arrayList2 = arrayList3;
                                            d2 = d;
                                            i2 = 0;
                                        } else {
                                            i9++;
                                            arrayList = arrayList4;
                                            d2 = d;
                                        }
                                    }
                                }
                                d = d2;
                                j = 0;
                                if (j <= j2) {
                                }
                                i6 = i + 1;
                                str2 = str;
                                arrayList2 = arrayList3;
                                d2 = d;
                                i2 = 0;
                            }
                        }
                        d = d2;
                        i6 = i + 1;
                        str2 = str;
                        arrayList2 = arrayList3;
                        d2 = d;
                        i2 = 0;
                    }
                }
                i = i6;
                str = str2;
                d = d2;
                i6 = i + 1;
                str2 = str;
                arrayList2 = arrayList3;
                d2 = d;
                i2 = 0;
            }
            ArrayList arrayList5 = arrayList2;
            if (!TextUtils.isEmpty(this.mBestTargetPackage)) {
                return;
            }
            this.mBestTargetPackage = ((ProcStatsEntry) arrayList5.get(0)).mPackage;
        } else if (arrayList2.size() == 1) {
            this.mBestTargetPackage = ((ProcStatsEntry) arrayList2.get(0)).mPackage;
        }
    }

    public void addService(ServiceState serviceState) {
        ArrayList<Service> arrayList = this.mServices.get(serviceState.getPackage());
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.mServices.put(serviceState.getPackage(), arrayList);
        }
        arrayList.add(new Service(serviceState));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mPackage);
        parcel.writeInt(this.mUid);
        parcel.writeString(this.mName);
        parcel.writeStringList(this.mPackages);
        parcel.writeLong(this.mBgDuration);
        parcel.writeLong(this.mAvgBgMem);
        parcel.writeLong(this.mMaxBgMem);
        parcel.writeDouble(this.mBgWeight);
        parcel.writeLong(this.mRunDuration);
        parcel.writeLong(this.mAvgRunMem);
        parcel.writeLong(this.mMaxRunMem);
        parcel.writeDouble(this.mRunWeight);
        parcel.writeString(this.mBestTargetPackage);
        int size = this.mServices.size();
        parcel.writeInt(size);
        for (int i2 = 0; i2 < size; i2++) {
            parcel.writeString(this.mServices.keyAt(i2));
            parcel.writeTypedList(this.mServices.valueAt(i2));
        }
    }

    public int getUid() {
        return this.mUid;
    }

    /* loaded from: classes.dex */
    public static final class Service implements Parcelable {
        public static final Parcelable.Creator<Service> CREATOR = new Parcelable.Creator<Service>() { // from class: com.android.settings.applications.ProcStatsEntry.Service.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public Service mo233createFromParcel(Parcel parcel) {
                return new Service(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public Service[] mo234newArray(int i) {
                return new Service[i];
            }
        };
        final long mDuration;
        final String mName;
        final String mPackage;
        final String mProcess;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public Service(ServiceState serviceState) {
            this.mPackage = serviceState.getPackage();
            this.mName = serviceState.getName();
            this.mProcess = serviceState.getProcessName();
            this.mDuration = serviceState.dumpTime((PrintWriter) null, (String) null, 0, -1, 0L, 0L);
        }

        public Service(Parcel parcel) {
            this.mPackage = parcel.readString();
            this.mName = parcel.readString();
            this.mProcess = parcel.readString();
            this.mDuration = parcel.readLong();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mPackage);
            parcel.writeString(this.mName);
            parcel.writeString(this.mProcess);
            parcel.writeLong(this.mDuration);
        }
    }
}
