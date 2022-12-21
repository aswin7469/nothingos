package com.nothing.experience;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import com.nothing.experience.datas.DataHelper;
import com.nothing.experience.datas.EventProvider;
import com.nothing.experience.datas.LocalData;
import com.nothing.experience.internalapi.OSBuildWrapper;
import com.nothing.experience.network.ThreadPoolManager;
import com.nothing.experience.service.DataQuery;
import com.nothing.experience.service.DataQueryListener;
import com.nothing.experience.service.NothingExperience;
import com.nothing.experience.utils.DataSyncTask;
import com.nothing.experience.utils.DeviceIdFactory;
import com.nothing.experience.utils.LogUtil;
import com.nothing.experience.utils.NetworkUtils;
import com.nothing.experience.utils.SharedPreferencesUtils;
import com.nothing.experience.utils.SoftwareInfoUtil;

public class App extends Application {
    public static String DEVICE_COLOR = null;
    public static SharedPreferencesUtils GLOBAL_SETTINGS = null;
    public static String RAM_SIZE = null;
    public static String ROM_SIZE = null;
    private static final long SYNC_DURATION = 7200000;
    public static volatile int UPLOAD_TYPE = 0;
    public static int VERSION_CODE = 0;
    public static final int VERSION_HAS_BUILD_INFO = 500;
    public static boolean debug = OSBuildWrapper.isDebuggable();
    public static boolean startCompleted = false;
    private ExperienceChange mChange;
    /* access modifiers changed from: private */
    public NothingExperience mExperience;
    /* access modifiers changed from: private */
    public boolean mLowBattery = false;
    /* access modifiers changed from: private */
    public DataQuery mQuery;
    /* access modifiers changed from: private */
    public DataSyncTask mSyncTask;
    private SystemEventReceiver mSystemEventReceiver;

    public void onCreate() {
        super.onCreate();
        RAM_SIZE = SoftwareInfoUtil.getRamSize(this);
        ROM_SIZE = SoftwareInfoUtil.getRomSize(this);
        DEVICE_COLOR = SoftwareInfoUtil.getDeviceColor();
        this.mSystemEventReceiver = new SystemEventReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(this.mSystemEventReceiver, intentFilter);
        try {
            VERSION_CODE = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        this.mChange = new ExperienceChange(new Handler());
        getContentResolver().registerContentObserver(EventProvider.CONTENT_PRODUCT_URI, true, this.mChange);
        getContentResolver().registerContentObserver(EventProvider.CONTENT_QUALITY_URI, true, this.mChange);
        getContentResolver().registerContentObserver(EventProvider.CONTENT_ACTIVATION_URI, true, this.mChange);
        SharedPreferencesUtils instance = SharedPreferencesUtils.getInstance(this);
        GLOBAL_SETTINGS = instance;
        UPLOAD_TYPE = ((Integer) instance.get(SharedPreferencesUtils.KEY_REPORT_TYPE, 0)).intValue();
        this.mSyncTask = new DataSyncTask(this);
        this.mQuery = new DataQuery(this);
        this.mExperience = NothingExperience.getInstance(this);
        loadDeviceId();
    }

    private void loadDeviceId() {
        DeviceIdFactory.loadDeviceid(this, this.mQuery);
    }

    public void onLowMemory() {
        super.onLowMemory();
        ThreadPoolManager.release();
    }

    public void onTerminate() {
        super.onTerminate();
        SystemEventReceiver systemEventReceiver = this.mSystemEventReceiver;
        if (systemEventReceiver != null) {
            unregisterReceiver(systemEventReceiver);
        }
        if (this.mChange != null) {
            getContentResolver().unregisterContentObserver(this.mChange);
        }
    }

    /* access modifiers changed from: private */
    public boolean needSync() {
        boolean z = System.currentTimeMillis() - ((Long) GLOBAL_SETTINGS.get(SharedPreferencesUtils.KEY_SCHEDULE_TIME, 0L)).longValue() >= SYNC_DURATION;
        if (this.mLowBattery || !z) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public boolean needSyncViaWifi() {
        return NetworkUtils.isNetworkAvailable(this) && NetworkUtils.isWifi(this);
    }

    /* access modifiers changed from: private */
    public void syncActivation() {
        this.mSyncTask.syncActivation();
    }

    /* access modifiers changed from: private */
    public void sync() {
        if (this.mSyncTask == null) {
            this.mSyncTask = new DataSyncTask(this);
        }
        this.mSyncTask.uploadWhenReady();
    }

    /* access modifiers changed from: private */
    public boolean networkAvailable() {
        return NetworkUtils.isNetworkAvailable(this);
    }

    private class SystemEventReceiver extends BroadcastReceiver {
        private SystemEventReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            action.hashCode();
            if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                boolean z = false;
                int intExtra = (intent.getIntExtra("level", 0) * 100) / intent.getIntExtra("scale", 100);
                App app = App.this;
                if (intExtra < 10) {
                    z = true;
                }
                boolean unused = app.mLowBattery = z;
                if (NetworkUtils.isNetworkAvailable(context)) {
                    App.this.syncActivation();
                }
            } else if (action.equals("android.net.conn.CONNECTIVITY_CHANGE") && !App.this.mLowBattery) {
                if (NetworkUtils.isNetworkAvailable(context)) {
                    App.this.mQuery.queryActivationItemByLimited(new App$SystemEventReceiver$$ExternalSyntheticLambda0(this), "1");
                }
                if (App.this.needSyncViaWifi() && App.this.needSync()) {
                    App.this.sync();
                }
            }
        }

        /* renamed from: lambda$onReceive$0$com-nothing-experience-App$SystemEventReceiver */
        public /* synthetic */ void mo13891x78457e71(LocalData localData) {
            boolean z = localData == null || localData.events == null || localData.events.size() == 0;
            LogUtil.m42d("needActiveData:" + z);
            if (z) {
                Bundle bundle = new Bundle();
                bundle.putString(SharedPreferencesUtils.KEY_ACTIVE_DONE, "1");
                App.this.mExperience.logActivationEvent("Device_Active", bundle);
            }
        }
    }

    private class ExperienceChange extends ContentObserver {
        public ExperienceChange(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z, Uri uri) {
            super.onChange(z, uri);
            final boolean access$800 = App.this.networkAvailable();
            boolean access$400 = App.this.needSyncViaWifi();
            if (tableMatch(uri, DataHelper.TABLE_ACTIVATION)) {
                App.this.mQuery.queryActivationLastItem(new DataQueryListener<LocalData>() {
                    public /* synthetic */ void onFail(String str) {
                        DataQueryListener.CC.$default$onFail(this, str);
                    }

                    public void onSuccess(LocalData localData) {
                        if (access$800) {
                            App.this.mSyncTask.mo13967xbe4845d2(localData);
                        }
                        DeviceIdFactory.updateDeviceIdWhenActive(App.this, localData.user_pseudo_id);
                    }
                });
            }
            boolean z2 = App.this.needSync() && access$400;
            if (LogUtil.isTestEnv() && access$400) {
                if (tableMatch(uri, DataHelper.TABLE_PRODUCT)) {
                    App.this.mQuery.queryProductLastItem(new App$ExperienceChange$$ExternalSyntheticLambda0(this));
                } else if (tableMatch(uri, DataHelper.TABLE_QUALITY)) {
                    App.this.mQuery.queryQualityLastItem(new App$ExperienceChange$$ExternalSyntheticLambda1(this));
                }
            }
            if (z2) {
                App.this.sync();
                App.GLOBAL_SETTINGS.set(SharedPreferencesUtils.KEY_SCHEDULE_TIME, Long.valueOf(System.currentTimeMillis()));
            }
        }

        /* renamed from: lambda$onChange$0$com-nothing-experience-App$ExperienceChange  reason: not valid java name */
        public /* synthetic */ void m88lambda$onChange$0$comnothingexperienceApp$ExperienceChange(LocalData localData) {
            App.this.mSyncTask.mo13967xbe4845d2(localData);
        }

        /* renamed from: lambda$onChange$1$com-nothing-experience-App$ExperienceChange  reason: not valid java name */
        public /* synthetic */ void m89lambda$onChange$1$comnothingexperienceApp$ExperienceChange(LocalData localData) {
            App.this.mSyncTask.mo13967xbe4845d2(localData);
        }

        private boolean tableMatch(Uri uri, String str) {
            if (uri == null) {
                return false;
            }
            return uri.getPath().contains(str);
        }
    }
}
