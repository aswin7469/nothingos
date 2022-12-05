package com.android.settings.fuelgauge.batterytip;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.app.job.JobWorkItem;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StatsDimensionsValue;
import android.os.UserManager;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.util.ArrayUtils;
import com.android.settings.R;
import com.android.settings.fuelgauge.BatteryUtils;
import com.android.settings.fuelgauge.PowerUsageFeatureProvider;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.fuelgauge.PowerAllowlistBackend;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public class AnomalyDetectionJobService extends JobService {
    static final long MAX_DELAY_MS = TimeUnit.MINUTES.toMillis(30);
    static final int STATSD_UID_FILED = 1;
    static final int UID_NULL = -1;
    private final Object mLock = new Object();
    boolean mIsJobCanceled = false;

    public static void scheduleAnomalyDetection(Context context, Intent intent) {
        if (((JobScheduler) context.getSystemService(JobScheduler.class)).enqueue(new JobInfo.Builder(R.integer.job_anomaly_detection, new ComponentName(context, AnomalyDetectionJobService.class)).setOverrideDeadline(MAX_DELAY_MS).build(), new JobWorkItem(intent)) != 1) {
            Log.i("AnomalyDetectionService", "Anomaly detection job service enqueue failed.");
        }
    }

    @Override // android.app.job.JobService
    public boolean onStartJob(final JobParameters jobParameters) {
        synchronized (this.mLock) {
            this.mIsJobCanceled = false;
        }
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.android.settings.fuelgauge.batterytip.AnomalyDetectionJobService$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                AnomalyDetectionJobService.this.lambda$onStartJob$0(jobParameters);
            }
        });
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onStartJob$0(JobParameters jobParameters) {
        BatteryDatabaseManager batteryDatabaseManager = BatteryDatabaseManager.getInstance(this);
        BatteryTipPolicy batteryTipPolicy = new BatteryTipPolicy(this);
        BatteryUtils batteryUtils = BatteryUtils.getInstance(this);
        ContentResolver contentResolver = getContentResolver();
        UserManager userManager = (UserManager) getSystemService(UserManager.class);
        PowerAllowlistBackend powerAllowlistBackend = PowerAllowlistBackend.getInstance(this);
        PowerUsageFeatureProvider powerUsageFeatureProvider = FeatureFactory.getFactory(this).getPowerUsageFeatureProvider(this);
        MetricsFeatureProvider metricsFeatureProvider = FeatureFactory.getFactory(this).getMetricsFeatureProvider();
        JobWorkItem dequeueWork = dequeueWork(jobParameters);
        while (dequeueWork != null) {
            saveAnomalyToDatabase(this, userManager, batteryDatabaseManager, batteryUtils, batteryTipPolicy, powerAllowlistBackend, contentResolver, powerUsageFeatureProvider, metricsFeatureProvider, dequeueWork.getIntent().getExtras());
            completeWork(jobParameters, dequeueWork);
            dequeueWork = dequeueWork(jobParameters);
            batteryDatabaseManager = batteryDatabaseManager;
        }
    }

    @Override // android.app.job.JobService
    public boolean onStopJob(JobParameters jobParameters) {
        synchronized (this.mLock) {
            this.mIsJobCanceled = true;
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:25:0x0068, code lost:
        if (android.provider.Settings.Global.getInt(r20, "app_auto_restriction_enabled", 1) == 1) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void saveAnomalyToDatabase(Context context, UserManager userManager, BatteryDatabaseManager batteryDatabaseManager, BatteryUtils batteryUtils, BatteryTipPolicy batteryTipPolicy, PowerAllowlistBackend powerAllowlistBackend, ContentResolver contentResolver, PowerUsageFeatureProvider powerUsageFeatureProvider, MetricsFeatureProvider metricsFeatureProvider, Bundle bundle) {
        StatsDimensionsValue statsDimensionsValue = (StatsDimensionsValue) bundle.getParcelable("android.app.extra.STATS_DIMENSIONS_VALUE");
        long j = bundle.getLong("key_anomaly_timestamp", System.currentTimeMillis());
        ArrayList<String> stringArrayList = bundle.getStringArrayList("android.app.extra.STATS_BROADCAST_SUBSCRIBER_COOKIES");
        boolean z = false;
        AnomalyInfo anomalyInfo = new AnomalyInfo(!ArrayUtils.isEmpty(stringArrayList) ? stringArrayList.get(0) : "");
        Log.i("AnomalyDetectionService", "Extra stats value: " + statsDimensionsValue.toString());
        try {
            int extractUidFromStatsDimensionsValue = extractUidFromStatsDimensionsValue(statsDimensionsValue);
            if (powerUsageFeatureProvider.isSmartBatterySupported()) {
                if (Settings.Global.getInt(contentResolver, "adaptive_battery_management_enabled", 1) == 1) {
                    z = true;
                }
                String packageName = batteryUtils.getPackageName(extractUidFromStatsDimensionsValue);
                String str = packageName + "/" + batteryUtils.getAppLongVersionCode(packageName);
                if (batteryUtils.shouldHideAnomaly(powerAllowlistBackend, extractUidFromStatsDimensionsValue, anomalyInfo)) {
                    metricsFeatureProvider.action(0, 1387, 0, str, anomalyInfo.anomalyType.intValue());
                    return;
                }
                if (z && anomalyInfo.autoRestriction) {
                    batteryUtils.setForceAppStandby(extractUidFromStatsDimensionsValue, packageName, 1);
                    batteryDatabaseManager.insertAnomaly(extractUidFromStatsDimensionsValue, packageName, anomalyInfo.anomalyType.intValue(), 2, j);
                } else {
                    batteryDatabaseManager.insertAnomaly(extractUidFromStatsDimensionsValue, packageName, anomalyInfo.anomalyType.intValue(), 0, j);
                }
                metricsFeatureProvider.action(0, 1367, 0, str, anomalyInfo.anomalyType.intValue());
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            Log.e("AnomalyDetectionService", "Parse stats dimensions value error.", e);
        }
    }

    int extractUidFromStatsDimensionsValue(StatsDimensionsValue statsDimensionsValue) {
        if (statsDimensionsValue == null) {
            return UID_NULL;
        }
        if (statsDimensionsValue.isValueType(3) && statsDimensionsValue.getField() == 1) {
            return statsDimensionsValue.getIntValue();
        }
        if (statsDimensionsValue.isValueType(7)) {
            List tupleValueList = statsDimensionsValue.getTupleValueList();
            int size = tupleValueList.size();
            for (int i = 0; i < size; i++) {
                int extractUidFromStatsDimensionsValue = extractUidFromStatsDimensionsValue((StatsDimensionsValue) tupleValueList.get(i));
                if (extractUidFromStatsDimensionsValue != UID_NULL) {
                    return extractUidFromStatsDimensionsValue;
                }
            }
        }
        return UID_NULL;
    }

    JobWorkItem dequeueWork(JobParameters jobParameters) {
        synchronized (this.mLock) {
            if (this.mIsJobCanceled) {
                return null;
            }
            return jobParameters.dequeueWork();
        }
    }

    void completeWork(JobParameters jobParameters, JobWorkItem jobWorkItem) {
        synchronized (this.mLock) {
            if (this.mIsJobCanceled) {
                return;
            }
            jobParameters.completeWork(jobWorkItem);
        }
    }
}
