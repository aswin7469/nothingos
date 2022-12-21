package com.android.systemui.qrcodescanner.controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.DeviceConfig;
import android.util.Log;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.settings.SecureSettings;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import javax.inject.Inject;

@SysUISingleton
public class QRCodeScannerController implements CallbackController<Callback> {
    public static final int DEFAULT_QR_CODE_SCANNER_CHANGE = 0;
    public static final int QR_CODE_SCANNER_PREFERENCE_CHANGE = 1;
    private static final String TAG = "QRCodeScannerController";
    private final ArrayList<Callback> mCallbacks = new ArrayList<>();
    private ComponentName mComponentName = null;
    private final boolean mConfigEnableLockScreenButton;
    private final Context mContext;
    private AtomicInteger mDefaultQRCodeScannerChangeEvents = new AtomicInteger(0);
    private final DeviceConfigProxy mDeviceConfigProxy;
    /* access modifiers changed from: private */
    public final Executor mExecutor;
    private Intent mIntent = null;
    private Boolean mIsCameraAvailable = null;
    private DeviceConfig.OnPropertiesChangedListener mOnDefaultQRCodeScannerChangedListener = null;
    private String mQRCodeScannerActivity = null;
    private boolean mQRCodeScannerEnabled;
    private AtomicInteger mQRCodeScannerPreferenceChangeEvents = new AtomicInteger(0);
    private HashMap<Integer, ContentObserver> mQRCodeScannerPreferenceObserver = new HashMap<>();
    private final SecureSettings mSecureSettings;
    private UserTracker.Callback mUserChangedListener = null;
    private final UserTracker mUserTracker;

    public interface Callback {
        void onQRCodeScannerActivityChanged() {
        }

        void onQRCodeScannerPreferenceChanged() {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface QRCodeScannerChangeEvent {
    }

    @Inject
    public QRCodeScannerController(Context context, @Background Executor executor, SecureSettings secureSettings, DeviceConfigProxy deviceConfigProxy, UserTracker userTracker) {
        this.mContext = context;
        this.mExecutor = executor;
        this.mSecureSettings = secureSettings;
        this.mDeviceConfigProxy = deviceConfigProxy;
        this.mUserTracker = userTracker;
        this.mConfigEnableLockScreenButton = context.getResources().getBoolean(17891336);
    }

    public void addCallback(Callback callback) {
        if (isCameraAvailable()) {
            synchronized (this.mCallbacks) {
                this.mCallbacks.add(callback);
            }
        }
    }

    public void removeCallback(Callback callback) {
        if (isCameraAvailable()) {
            synchronized (this.mCallbacks) {
                this.mCallbacks.remove((Object) callback);
            }
        }
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0004, code lost:
        r0 = r2.mIntent;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isEnabledForLockScreenButton() {
        /*
            r2 = this;
            boolean r0 = r2.mQRCodeScannerEnabled
            if (r0 == 0) goto L_0x0014
            android.content.Intent r0 = r2.mIntent
            if (r0 == 0) goto L_0x0014
            boolean r1 = r2.mConfigEnableLockScreenButton
            if (r1 == 0) goto L_0x0014
            boolean r2 = r2.isActivityCallable(r0)
            if (r2 == 0) goto L_0x0014
            r2 = 1
            goto L_0x0015
        L_0x0014:
            r2 = 0
        L_0x0015:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qrcodescanner.controller.QRCodeScannerController.isEnabledForLockScreenButton():boolean");
    }

    public boolean isEnabledForQuickSettings() {
        Intent intent = this.mIntent;
        return intent != null && isActivityCallable(intent);
    }

    public void registerQRCodeScannerChangeObservers(int... iArr) {
        if (isCameraAvailable()) {
            for (int i : iArr) {
                if (i == 0) {
                    this.mDefaultQRCodeScannerChangeEvents.incrementAndGet();
                    registerDefaultQRCodeScannerObserver();
                } else if (i != 1) {
                    Log.e(TAG, "Unrecognised event: " + i);
                } else {
                    this.mQRCodeScannerPreferenceChangeEvents.incrementAndGet();
                    registerQRCodePreferenceObserver();
                    registerUserChangeObservers();
                }
            }
        }
    }

    public void unregisterQRCodeScannerChangeObservers(int... iArr) {
        if (isCameraAvailable()) {
            for (int i : iArr) {
                if (i != 0) {
                    if (i != 1) {
                        Log.e(TAG, "Unrecognised event: " + i);
                    } else if (this.mUserTracker != null && this.mQRCodeScannerPreferenceChangeEvents.decrementAndGet() == 0) {
                        unregisterQRCodePreferenceObserver();
                        unregisterUserChangeObservers();
                    }
                } else if (this.mOnDefaultQRCodeScannerChangedListener != null && this.mDefaultQRCodeScannerChangeEvents.decrementAndGet() == 0) {
                    unregisterDefaultQRCodeScannerObserver();
                }
            }
        }
    }

    public boolean isCameraAvailable() {
        if (this.mIsCameraAvailable == null) {
            this.mIsCameraAvailable = Boolean.valueOf(this.mContext.getPackageManager().hasSystemFeature("android.hardware.camera"));
        }
        return this.mIsCameraAvailable.booleanValue();
    }

    /* access modifiers changed from: private */
    public void updateQRCodeScannerPreferenceDetails(boolean z) {
        if (this.mConfigEnableLockScreenButton) {
            boolean z2 = this.mQRCodeScannerEnabled;
            boolean z3 = false;
            if (this.mSecureSettings.getIntForUser("lock_screen_show_qr_code_scanner", 0, this.mUserTracker.getUserId()) != 0) {
                z3 = true;
            }
            this.mQRCodeScannerEnabled = z3;
            if (z) {
                this.mSecureSettings.putStringForUser("show_qr_code_scanner_setting", this.mQRCodeScannerActivity, this.mUserTracker.getUserId());
            }
            if (!Objects.equals(Boolean.valueOf(this.mQRCodeScannerEnabled), Boolean.valueOf(z2))) {
                notifyQRCodeScannerPreferenceChanged();
            }
        }
    }

    private String getDefaultScannerActivity() {
        return this.mContext.getResources().getString(17039940);
    }

    /* access modifiers changed from: private */
    /* renamed from: updateQRCodeScannerActivityDetails */
    public void mo35803x5f47bf08() {
        ComponentName componentName;
        String string = this.mDeviceConfigProxy.getString("systemui", "default_qr_code_scanner", "");
        if (Objects.equals(string, "")) {
            string = getDefaultScannerActivity();
        }
        String str = this.mQRCodeScannerActivity;
        Intent intent = new Intent();
        if (string != null) {
            componentName = ComponentName.unflattenFromString(string);
            intent.setComponent(componentName);
            intent.addFlags(335544320);
        } else {
            componentName = null;
        }
        if (isActivityAvailable(intent)) {
            this.mQRCodeScannerActivity = string;
            this.mComponentName = componentName;
            this.mIntent = intent;
        } else {
            this.mQRCodeScannerActivity = null;
            this.mComponentName = null;
            this.mIntent = null;
        }
        if (!Objects.equals(this.mQRCodeScannerActivity, str)) {
            notifyQRCodeScannerActivityChanged();
        }
    }

    private boolean isActivityAvailable(Intent intent) {
        if (intent.getComponent() == null) {
            return false;
        }
        return !this.mContext.getPackageManager().queryIntentActivities(intent, 537698816).isEmpty();
    }

    private boolean isActivityCallable(Intent intent) {
        if (intent.getComponent() == null) {
            return false;
        }
        return !this.mContext.getPackageManager().queryIntentActivities(intent, 819200).isEmpty();
    }

    private void unregisterUserChangeObservers() {
        this.mUserTracker.removeCallback(this.mUserChangedListener);
        this.mUserChangedListener = null;
        this.mQRCodeScannerEnabled = false;
    }

    private void unregisterQRCodePreferenceObserver() {
        if (this.mConfigEnableLockScreenButton) {
            this.mQRCodeScannerPreferenceObserver.forEach(new QRCodeScannerController$$ExternalSyntheticLambda0(this));
            this.mQRCodeScannerPreferenceObserver = new HashMap<>();
            this.mSecureSettings.putStringForUser("show_qr_code_scanner_setting", (String) null, this.mUserTracker.getUserId());
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$unregisterQRCodePreferenceObserver$0$com-android-systemui-qrcodescanner-controller-QRCodeScannerController */
    public /* synthetic */ void mo35806xd44db0d8(Integer num, ContentObserver contentObserver) {
        this.mSecureSettings.unregisterContentObserver(contentObserver);
    }

    private void unregisterDefaultQRCodeScannerObserver() {
        this.mDeviceConfigProxy.removeOnPropertiesChangedListener(this.mOnDefaultQRCodeScannerChangedListener);
        this.mOnDefaultQRCodeScannerChangedListener = null;
        this.mQRCodeScannerActivity = null;
        this.mIntent = null;
        this.mComponentName = null;
    }

    private void notifyQRCodeScannerActivityChanged() {
        ArrayList arrayList;
        synchronized (this.mCallbacks) {
            arrayList = (ArrayList) this.mCallbacks.clone();
        }
        arrayList.forEach(new QRCodeScannerController$$ExternalSyntheticLambda1());
    }

    private void notifyQRCodeScannerPreferenceChanged() {
        ArrayList arrayList;
        synchronized (this.mCallbacks) {
            arrayList = (ArrayList) this.mCallbacks.clone();
        }
        arrayList.forEach(new QRCodeScannerController$$ExternalSyntheticLambda2());
    }

    private void registerDefaultQRCodeScannerObserver() {
        if (this.mOnDefaultQRCodeScannerChangedListener == null) {
            this.mExecutor.execute(new QRCodeScannerController$$ExternalSyntheticLambda3(this));
            QRCodeScannerController$$ExternalSyntheticLambda4 qRCodeScannerController$$ExternalSyntheticLambda4 = new QRCodeScannerController$$ExternalSyntheticLambda4(this);
            this.mOnDefaultQRCodeScannerChangedListener = qRCodeScannerController$$ExternalSyntheticLambda4;
            this.mDeviceConfigProxy.addOnPropertiesChangedListener("systemui", this.mExecutor, qRCodeScannerController$$ExternalSyntheticLambda4);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$registerDefaultQRCodeScannerObserver$4$com-android-systemui-qrcodescanner-controller-QRCodeScannerController */
    public /* synthetic */ void mo35804x42737249(DeviceConfig.Properties properties) {
        if ("systemui".equals(properties.getNamespace()) && properties.getKeyset().contains("default_qr_code_scanner")) {
            mo35803x5f47bf08();
            updateQRCodeScannerPreferenceDetails(true);
        }
    }

    /* access modifiers changed from: private */
    public void registerQRCodePreferenceObserver() {
        if (this.mConfigEnableLockScreenButton) {
            int userId = this.mUserTracker.getUserId();
            if (this.mQRCodeScannerPreferenceObserver.getOrDefault(Integer.valueOf(userId), null) == null) {
                this.mExecutor.execute(new QRCodeScannerController$$ExternalSyntheticLambda5(this));
                this.mQRCodeScannerPreferenceObserver.put(Integer.valueOf(userId), new ContentObserver((Handler) null) {
                    public void onChange(boolean z) {
                        QRCodeScannerController.this.mExecutor.execute(new QRCodeScannerController$1$$ExternalSyntheticLambda0(this));
                    }

                    /* access modifiers changed from: package-private */
                    /* renamed from: lambda$onChange$0$com-android-systemui-qrcodescanner-controller-QRCodeScannerController$1 */
                    public /* synthetic */ void mo35810x4f8469b9() {
                        QRCodeScannerController.this.updateQRCodeScannerPreferenceDetails(false);
                    }
                });
                SecureSettings secureSettings = this.mSecureSettings;
                secureSettings.registerContentObserverForUser(secureSettings.getUriFor("lock_screen_show_qr_code_scanner"), false, this.mQRCodeScannerPreferenceObserver.get(Integer.valueOf(userId)), userId);
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$registerQRCodePreferenceObserver$5$com-android-systemui-qrcodescanner-controller-QRCodeScannerController */
    public /* synthetic */ void mo35805x52bbd2c4() {
        updateQRCodeScannerPreferenceDetails(true);
    }

    private void registerUserChangeObservers() {
        if (this.mUserChangedListener == null) {
            C23182 r0 = new UserTracker.Callback() {
                public void onUserChanged(int i, Context context) {
                    QRCodeScannerController.this.registerQRCodePreferenceObserver();
                    QRCodeScannerController.this.updateQRCodeScannerPreferenceDetails(true);
                }
            };
            this.mUserChangedListener = r0;
            this.mUserTracker.addCallback(r0, this.mExecutor);
        }
    }
}
