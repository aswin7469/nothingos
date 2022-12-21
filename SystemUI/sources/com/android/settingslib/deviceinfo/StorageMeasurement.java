package com.android.settingslib.deviceinfo;

import android.app.usage.ExternalStorageStats;
import android.app.usage.StorageStats;
import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.content.pm.UserInfo;
import android.icu.text.DateFormat;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.os.storage.VolumeInfo;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseLongArray;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.ref.WeakReference;
import java.p026io.IOException;
import java.util.HashMap;
import java.util.List;

public class StorageMeasurement {
    private static final String TAG = "StorageMeasurement";
    private final Context mContext;
    /* access modifiers changed from: private */
    public WeakReference<MeasurementReceiver> mReceiver;
    private final VolumeInfo mSharedVolume;
    private final StorageStatsManager mStats;
    private final UserManager mUser;
    private final VolumeInfo mVolume;

    public interface MeasurementReceiver {
        void onDetailsChanged(MeasurementDetails measurementDetails);
    }

    public static class MeasurementDetails {
        public SparseLongArray appsSize = new SparseLongArray();
        public long availSize;
        public long cacheSize;
        public SparseArray<HashMap<String, Long>> mediaSize = new SparseArray<>();
        public SparseLongArray miscSize = new SparseLongArray();
        public long totalSize;
        public SparseLongArray usersSize = new SparseLongArray();

        public String toString() {
            return "MeasurementDetails: [totalSize: " + this.totalSize + " availSize: " + this.availSize + " cacheSize: " + this.cacheSize + " mediaSize: " + this.mediaSize + " miscSize: " + this.miscSize + "usersSize: " + this.usersSize + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }

    public StorageMeasurement(Context context, VolumeInfo volumeInfo, VolumeInfo volumeInfo2) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mUser = (UserManager) applicationContext.getSystemService(UserManager.class);
        this.mStats = (StorageStatsManager) applicationContext.getSystemService(StorageStatsManager.class);
        this.mVolume = volumeInfo;
        this.mSharedVolume = volumeInfo2;
    }

    public void setReceiver(MeasurementReceiver measurementReceiver) {
        WeakReference<MeasurementReceiver> weakReference = this.mReceiver;
        if (weakReference == null || weakReference.get() == null) {
            this.mReceiver = new WeakReference<>(measurementReceiver);
        }
    }

    public void forceMeasure() {
        measure();
    }

    public void measure() {
        new MeasureTask().execute(new Void[0]);
    }

    public void onDestroy() {
        this.mReceiver = null;
    }

    private class MeasureTask extends AsyncTask<Void, Void, MeasurementDetails> {
        private MeasureTask() {
        }

        /* access modifiers changed from: protected */
        public MeasurementDetails doInBackground(Void... voidArr) {
            return StorageMeasurement.this.measureExactStorage();
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(MeasurementDetails measurementDetails) {
            MeasurementReceiver measurementReceiver = StorageMeasurement.this.mReceiver != null ? (MeasurementReceiver) StorageMeasurement.this.mReceiver.get() : null;
            if (measurementReceiver != null) {
                measurementReceiver.onDetailsChanged(measurementDetails);
            }
        }
    }

    /* access modifiers changed from: private */
    public MeasurementDetails measureExactStorage() {
        List<UserInfo> users = this.mUser.getUsers();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        MeasurementDetails measurementDetails = new MeasurementDetails();
        VolumeInfo volumeInfo = this.mVolume;
        if (volumeInfo == null) {
            return measurementDetails;
        }
        if (volumeInfo.getType() == 0 || this.mVolume.getType() == 5) {
            measurementDetails.totalSize = this.mVolume.getPath().getTotalSpace();
            measurementDetails.availSize = this.mVolume.getPath().getUsableSpace();
            return measurementDetails;
        }
        try {
            measurementDetails.totalSize = this.mStats.getTotalBytes(this.mVolume.fsUuid);
            measurementDetails.availSize = this.mStats.getFreeBytes(this.mVolume.fsUuid);
            long elapsedRealtime2 = SystemClock.elapsedRealtime();
            Log.d(TAG, "Measured total storage in " + (elapsedRealtime2 - elapsedRealtime) + DateFormat.MINUTE_SECOND);
            VolumeInfo volumeInfo2 = this.mSharedVolume;
            if (volumeInfo2 != null && volumeInfo2.isMountedReadable()) {
                for (UserInfo userInfo : users) {
                    HashMap hashMap = new HashMap();
                    measurementDetails.mediaSize.put(userInfo.id, hashMap);
                    try {
                        ExternalStorageStats queryExternalStatsForUser = this.mStats.queryExternalStatsForUser(this.mSharedVolume.fsUuid, UserHandle.of(userInfo.id));
                        addValue(measurementDetails.usersSize, userInfo.id, queryExternalStatsForUser.getTotalBytes());
                        hashMap.put(Environment.DIRECTORY_MUSIC, Long.valueOf(queryExternalStatsForUser.getAudioBytes()));
                        hashMap.put(Environment.DIRECTORY_MOVIES, Long.valueOf(queryExternalStatsForUser.getVideoBytes()));
                        hashMap.put(Environment.DIRECTORY_PICTURES, Long.valueOf(queryExternalStatsForUser.getImageBytes()));
                        addValue(measurementDetails.miscSize, userInfo.id, ((queryExternalStatsForUser.getTotalBytes() - queryExternalStatsForUser.getAudioBytes()) - queryExternalStatsForUser.getVideoBytes()) - queryExternalStatsForUser.getImageBytes());
                    } catch (IOException e) {
                        Log.w(TAG, e);
                    }
                }
            }
            long elapsedRealtime3 = SystemClock.elapsedRealtime();
            Log.d(TAG, "Measured shared storage in " + (elapsedRealtime3 - elapsedRealtime2) + DateFormat.MINUTE_SECOND);
            if (this.mVolume.getType() == 1 && this.mVolume.isMountedReadable()) {
                for (UserInfo userInfo2 : users) {
                    try {
                        StorageStats queryStatsForUser = this.mStats.queryStatsForUser(this.mVolume.fsUuid, UserHandle.of(userInfo2.id));
                        if (userInfo2.id == UserHandle.myUserId()) {
                            addValue(measurementDetails.usersSize, userInfo2.id, queryStatsForUser.getAppBytes());
                        }
                        addValue(measurementDetails.usersSize, userInfo2.id, queryStatsForUser.getDataBytes());
                        addValue(measurementDetails.appsSize, userInfo2.id, queryStatsForUser.getAppBytes() + queryStatsForUser.getDataBytes());
                        measurementDetails.cacheSize += queryStatsForUser.getCacheBytes();
                    } catch (IOException e2) {
                        Log.w(TAG, e2);
                    }
                }
            }
            Log.d(TAG, "Measured private storage in " + (SystemClock.elapsedRealtime() - elapsedRealtime3) + DateFormat.MINUTE_SECOND);
            return measurementDetails;
        } catch (IOException e3) {
            Log.w(TAG, e3);
            return measurementDetails;
        }
    }

    private static void addValue(SparseLongArray sparseLongArray, int i, long j) {
        sparseLongArray.put(i, sparseLongArray.get(i) + j);
    }
}
