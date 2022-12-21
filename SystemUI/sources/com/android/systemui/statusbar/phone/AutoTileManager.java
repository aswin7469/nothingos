package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import android.os.UserHandle;
import android.util.Log;
import com.android.systemui.C1893R;
import com.android.systemui.p012qs.AutoAddTracker;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.p012qs.ReduceBrightColorsController;
import com.android.systemui.p012qs.SettingObserver;
import com.android.systemui.p012qs.external.CustomTile;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.SafetyController;
import com.android.systemui.statusbar.policy.WalletController;
import com.android.systemui.util.UserAwareController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class AutoTileManager implements UserAwareController {
    public static final String BRIGHTNESS = "reduce_brightness";
    public static final String CAST = "cast";
    public static final String DEVICE_CONTROLS = "controls";
    public static final String HOTSPOT = "hotspot";
    public static final String INVERSION = "inversion";
    public static final String NIGHT = "night";
    public static final String SAVER = "saver";
    static final String SETTING_SEPARATOR = ":";
    private static final String TAG = "AutoTileManager";
    public static final String WALLET = "wallet";
    public static final String WORK = "work";
    private final ArrayList<AutoAddSetting> mAutoAddSettingList = new ArrayList<>();
    protected final AutoAddTracker mAutoTracker;
    final CastController.Callback mCastCallback = new CastController.Callback() {
        public void onCastDevicesChanged() {
            boolean z;
            if (!AutoTileManager.this.mAutoTracker.isAdded(AutoTileManager.CAST)) {
                Iterator<CastController.CastDevice> it = AutoTileManager.this.mCastController.getCastDevices().iterator();
                while (true) {
                    if (!it.hasNext()) {
                        z = false;
                        break;
                    }
                    CastController.CastDevice next = it.next();
                    z = true;
                    if (next.state != 2) {
                        if (next.state == 1) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (z) {
                    AutoTileManager.this.mHost.addTile(AutoTileManager.CAST);
                    AutoTileManager.this.mAutoTracker.setTileAdded(AutoTileManager.CAST);
                    AutoTileManager.this.mHandler.post(new AutoTileManager$7$$ExternalSyntheticLambda0(this));
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onCastDevicesChanged$0$com-android-systemui-statusbar-phone-AutoTileManager$7 */
        public /* synthetic */ void mo43668x68cfcc9e() {
            AutoTileManager.this.mCastController.removeCallback(AutoTileManager.this.mCastCallback);
        }
    };
    /* access modifiers changed from: private */
    public final CastController mCastController;
    protected final Context mContext;
    private UserHandle mCurrentUser;
    /* access modifiers changed from: private */
    public final DataSaverController mDataSaverController;
    /* access modifiers changed from: private */
    public final DataSaverController.Listener mDataSaverListener = new DataSaverController.Listener() {
        public void onDataSaverChanged(boolean z) {
            if (!AutoTileManager.this.mAutoTracker.isAdded(AutoTileManager.SAVER) && z) {
                AutoTileManager.this.mHost.addTile(AutoTileManager.SAVER);
                AutoTileManager.this.mAutoTracker.setTileAdded(AutoTileManager.SAVER);
                AutoTileManager.this.mHandler.post(new AutoTileManager$2$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onDataSaverChanged$0$com-android-systemui-statusbar-phone-AutoTileManager$2 */
        public /* synthetic */ void mo43660x4ab10a06() {
            AutoTileManager.this.mDataSaverController.removeCallback(AutoTileManager.this.mDataSaverListener);
        }
    };
    private final DeviceControlsController.Callback mDeviceControlsCallback = new DeviceControlsController.Callback() {
        public void onControlsUpdate(Integer num) {
            if (!AutoTileManager.this.mAutoTracker.isAdded(AutoTileManager.DEVICE_CONTROLS)) {
                if (num != null) {
                    AutoTileManager.this.mHost.addTile(AutoTileManager.DEVICE_CONTROLS, num.intValue());
                }
                AutoTileManager.this.mAutoTracker.setTileAdded(AutoTileManager.DEVICE_CONTROLS);
                AutoTileManager.this.mHandler.post(new AutoTileManager$4$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onControlsUpdate$0$com-android-systemui-statusbar-phone-AutoTileManager$4 */
        public /* synthetic */ void mo43662x9f20a7d2() {
            AutoTileManager.this.mDeviceControlsController.removeCallback();
        }
    };
    /* access modifiers changed from: private */
    public final DeviceControlsController mDeviceControlsController;
    protected final Handler mHandler;
    protected final QSTileHost mHost;
    /* access modifiers changed from: private */
    public final HotspotController.Callback mHotspotCallback = new HotspotController.Callback() {
        public void onHotspotChanged(boolean z, int i) {
            if (!AutoTileManager.this.mAutoTracker.isAdded(AutoTileManager.HOTSPOT) && z) {
                AutoTileManager.this.mHost.addTile(AutoTileManager.HOTSPOT);
                AutoTileManager.this.mAutoTracker.setTileAdded(AutoTileManager.HOTSPOT);
                AutoTileManager.this.mHandler.post(new AutoTileManager$3$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onHotspotChanged$0$com-android-systemui-statusbar-phone-AutoTileManager$3 */
        public /* synthetic */ void mo43661x67d7d0cb() {
            AutoTileManager.this.mHotspotController.removeCallback(AutoTileManager.this.mHotspotCallback);
        }
    };
    /* access modifiers changed from: private */
    public final HotspotController mHotspotController;
    private boolean mInitialized;
    private final boolean mIsReduceBrightColorsAvailable;
    /* access modifiers changed from: private */
    public final ManagedProfileController mManagedProfileController;
    final NightDisplayListener.Callback mNightDisplayCallback = new NightDisplayListener.Callback() {
        public void onActivated(boolean z) {
            if (z) {
                addNightTile();
            }
        }

        public void onAutoModeChanged(int i) {
            if (i == 1 || i == 2) {
                addNightTile();
            }
        }

        private void addNightTile() {
            if (!AutoTileManager.this.mAutoTracker.isAdded(AutoTileManager.NIGHT)) {
                AutoTileManager.this.mHost.addTile(AutoTileManager.NIGHT);
                AutoTileManager.this.mAutoTracker.setTileAdded(AutoTileManager.NIGHT);
                AutoTileManager.this.mHandler.post(new AutoTileManager$5$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$addNightTile$0$com-android-systemui-statusbar-phone-AutoTileManager$5 */
        public /* synthetic */ void mo43664x8457a6cc() {
            AutoTileManager.this.mNightDisplayListener.setCallback((NightDisplayListener.Callback) null);
        }
    };
    /* access modifiers changed from: private */
    public final NightDisplayListener mNightDisplayListener;
    private final ManagedProfileController.Callback mProfileCallback = new ManagedProfileController.Callback() {
        public void onManagedProfileRemoved() {
        }

        public void onManagedProfileChanged() {
            if (!AutoTileManager.this.mAutoTracker.isAdded(AutoTileManager.WORK) && AutoTileManager.this.mManagedProfileController.hasActiveProfile()) {
                AutoTileManager.this.mHost.addTile(AutoTileManager.WORK);
                AutoTileManager.this.mAutoTracker.setTileAdded(AutoTileManager.WORK);
            }
        }
    };
    final ReduceBrightColorsController.Listener mReduceBrightColorsCallback = new ReduceBrightColorsController.Listener() {
        public void onActivated(boolean z) {
            if (z) {
                addReduceBrightColorsTile();
            }
        }

        private void addReduceBrightColorsTile() {
            if (!AutoTileManager.this.mAutoTracker.isAdded(AutoTileManager.BRIGHTNESS)) {
                AutoTileManager.this.mHost.addTile(AutoTileManager.BRIGHTNESS);
                AutoTileManager.this.mAutoTracker.setTileAdded(AutoTileManager.BRIGHTNESS);
                AutoTileManager.this.mHandler.post(new AutoTileManager$6$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$addReduceBrightColorsTile$0$com-android-systemui-statusbar-phone-AutoTileManager$6 */
        public /* synthetic */ void mo43667xb324b80d() {
            AutoTileManager.this.mReduceBrightColorsController.removeCallback((ReduceBrightColorsController.Listener) this);
        }
    };
    /* access modifiers changed from: private */
    public final ReduceBrightColorsController mReduceBrightColorsController;
    final SafetyController.Listener mSafetyCallback = new SafetyController.Listener() {
        public void onSafetyCenterEnableChanged(boolean z) {
            if (AutoTileManager.this.mSafetySpec != null) {
                if (z && !AutoTileManager.this.mAutoTracker.isAdded(AutoTileManager.this.mSafetySpec)) {
                    AutoTileManager.this.initSafetyTile();
                } else if (!z && AutoTileManager.this.mAutoTracker.isAdded(AutoTileManager.this.mSafetySpec)) {
                    AutoTileManager.this.mHost.removeTile(CustomTile.getComponentFromSpec(AutoTileManager.this.mSafetySpec));
                    AutoTileManager.this.mHost.unmarkTileAsAutoAdded(AutoTileManager.this.mSafetySpec);
                }
            }
        }
    };
    private final SafetyController mSafetyController;
    /* access modifiers changed from: private */
    public final String mSafetySpec;
    protected final SecureSettings mSecureSettings;
    private final WalletController mWalletController;

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x009a, code lost:
        if (r1.length() == 0) goto L_0x009c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public AutoTileManager(android.content.Context r5, com.android.systemui.p012qs.AutoAddTracker.Builder r6, com.android.systemui.p012qs.QSTileHost r7, @com.android.systemui.dagger.qualifiers.Background android.os.Handler r8, com.android.systemui.util.settings.SecureSettings r9, com.android.systemui.statusbar.policy.HotspotController r10, com.android.systemui.statusbar.policy.DataSaverController r11, com.android.systemui.statusbar.phone.ManagedProfileController r12, android.hardware.display.NightDisplayListener r13, com.android.systemui.statusbar.policy.CastController r14, com.android.systemui.p012qs.ReduceBrightColorsController r15, com.android.systemui.statusbar.policy.DeviceControlsController r16, com.android.systemui.statusbar.policy.WalletController r17, com.android.systemui.statusbar.policy.SafetyController r18, @javax.inject.Named("rbc_available") boolean r19) {
        /*
            r4 = this;
            r0 = r4
            r4.<init>()
            java.util.ArrayList r1 = new java.util.ArrayList
            r1.<init>()
            r0.mAutoAddSettingList = r1
            com.android.systemui.statusbar.phone.AutoTileManager$1 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$1
            r1.<init>()
            r0.mProfileCallback = r1
            com.android.systemui.statusbar.phone.AutoTileManager$2 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$2
            r1.<init>()
            r0.mDataSaverListener = r1
            com.android.systemui.statusbar.phone.AutoTileManager$3 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$3
            r1.<init>()
            r0.mHotspotCallback = r1
            com.android.systemui.statusbar.phone.AutoTileManager$4 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$4
            r1.<init>()
            r0.mDeviceControlsCallback = r1
            com.android.systemui.statusbar.phone.AutoTileManager$5 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$5
            r1.<init>()
            r0.mNightDisplayCallback = r1
            com.android.systemui.statusbar.phone.AutoTileManager$6 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$6
            r1.<init>()
            r0.mReduceBrightColorsCallback = r1
            com.android.systemui.statusbar.phone.AutoTileManager$7 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$7
            r1.<init>()
            r0.mCastCallback = r1
            com.android.systemui.statusbar.phone.AutoTileManager$8 r1 = new com.android.systemui.statusbar.phone.AutoTileManager$8
            r1.<init>()
            r0.mSafetyCallback = r1
            r1 = r5
            r0.mContext = r1
            r2 = r7
            r0.mHost = r2
            r3 = r9
            r0.mSecureSettings = r3
            android.content.Context r2 = r7.getUserContext()
            android.os.UserHandle r2 = r2.getUser()
            r0.mCurrentUser = r2
            int r2 = r2.getIdentifier()
            r3 = r6
            com.android.systemui.qs.AutoAddTracker$Builder r2 = r6.setUserId(r2)
            com.android.systemui.qs.AutoAddTracker r2 = r2.build()
            r0.mAutoTracker = r2
            r2 = r8
            r0.mHandler = r2
            r2 = r10
            r0.mHotspotController = r2
            r2 = r11
            r0.mDataSaverController = r2
            r2 = r12
            r0.mManagedProfileController = r2
            r2 = r13
            r0.mNightDisplayListener = r2
            r2 = r14
            r0.mCastController = r2
            r2 = r15
            r0.mReduceBrightColorsController = r2
            r2 = r19
            r0.mIsReduceBrightColorsAvailable = r2
            r2 = r16
            r0.mDeviceControlsController = r2
            r2 = r17
            r0.mWalletController = r2
            r2 = r18
            r0.mSafetyController = r2
            r2 = 0
            android.content.res.Resources r1 = r5.getResources()     // Catch:{ NotFoundException | NullPointerException -> 0x009c }
            r3 = 2131953202(0x7f130632, float:1.9542868E38)
            java.lang.String r1 = r1.getString(r3)     // Catch:{ NotFoundException | NullPointerException -> 0x009c }
            int r3 = r1.length()     // Catch:{ NotFoundException | NullPointerException -> 0x009c }
            if (r3 != 0) goto L_0x009d
        L_0x009c:
            r1 = r2
        L_0x009d:
            if (r1 == 0) goto L_0x00b2
            android.content.ComponentName r2 = new android.content.ComponentName
            android.content.Context r3 = r0.mContext
            android.content.pm.PackageManager r3 = r3.getPackageManager()
            java.lang.String r3 = r3.getPermissionControllerPackageName()
            r2.<init>(r3, r1)
            java.lang.String r2 = com.android.systemui.p012qs.external.CustomTile.toSpec(r2)
        L_0x00b2:
            r0.mSafetySpec = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.AutoTileManager.<init>(android.content.Context, com.android.systemui.qs.AutoAddTracker$Builder, com.android.systemui.qs.QSTileHost, android.os.Handler, com.android.systemui.util.settings.SecureSettings, com.android.systemui.statusbar.policy.HotspotController, com.android.systemui.statusbar.policy.DataSaverController, com.android.systemui.statusbar.phone.ManagedProfileController, android.hardware.display.NightDisplayListener, com.android.systemui.statusbar.policy.CastController, com.android.systemui.qs.ReduceBrightColorsController, com.android.systemui.statusbar.policy.DeviceControlsController, com.android.systemui.statusbar.policy.WalletController, com.android.systemui.statusbar.policy.SafetyController, boolean):void");
    }

    public void init() {
        if (this.mInitialized) {
            Log.w(TAG, "Trying to re-initialize");
            return;
        }
        this.mAutoTracker.initialize();
        populateSettingsList();
        startControllersAndSettingsListeners();
        this.mInitialized = true;
    }

    /* access modifiers changed from: protected */
    public void startControllersAndSettingsListeners() {
        if (!this.mAutoTracker.isAdded(HOTSPOT)) {
            this.mHotspotController.addCallback(this.mHotspotCallback);
        }
        if (!this.mAutoTracker.isAdded(SAVER)) {
            this.mDataSaverController.addCallback(this.mDataSaverListener);
        }
        if (!this.mAutoTracker.isAdded(WORK)) {
            this.mManagedProfileController.addCallback(this.mProfileCallback);
        }
        if (!this.mAutoTracker.isAdded(NIGHT) && ColorDisplayManager.isNightDisplayAvailable(this.mContext)) {
            this.mNightDisplayListener.setCallback(this.mNightDisplayCallback);
        }
        if (!this.mAutoTracker.isAdded(CAST)) {
            this.mCastController.addCallback(this.mCastCallback);
        }
        if (!this.mAutoTracker.isAdded(BRIGHTNESS) && this.mIsReduceBrightColorsAvailable) {
            this.mReduceBrightColorsController.addCallback(this.mReduceBrightColorsCallback);
        }
        if (!this.mAutoTracker.isAdded(DEVICE_CONTROLS)) {
            this.mDeviceControlsController.setCallback(this.mDeviceControlsCallback);
        }
        if (!this.mAutoTracker.isAdded(WALLET)) {
            initWalletController();
        }
        String str = this.mSafetySpec;
        if (str != null) {
            if (!this.mAutoTracker.isAdded(str)) {
                initSafetyTile();
            }
            this.mSafetyController.addCallback(this.mSafetyCallback);
        }
        int size = this.mAutoAddSettingList.size();
        for (int i = 0; i < size; i++) {
            if (!this.mAutoTracker.isAdded(this.mAutoAddSettingList.get(i).mSpec)) {
                this.mAutoAddSettingList.get(i).setListening(true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void stopListening() {
        this.mHotspotController.removeCallback(this.mHotspotCallback);
        this.mDataSaverController.removeCallback(this.mDataSaverListener);
        this.mManagedProfileController.removeCallback(this.mProfileCallback);
        if (ColorDisplayManager.isNightDisplayAvailable(this.mContext)) {
            this.mNightDisplayListener.setCallback((NightDisplayListener.Callback) null);
        }
        if (this.mIsReduceBrightColorsAvailable) {
            this.mReduceBrightColorsController.removeCallback(this.mReduceBrightColorsCallback);
        }
        this.mCastController.removeCallback(this.mCastCallback);
        this.mDeviceControlsController.removeCallback();
        if (this.mSafetySpec != null) {
            this.mSafetyController.removeCallback(this.mSafetyCallback);
        }
        int size = this.mAutoAddSettingList.size();
        for (int i = 0; i < size; i++) {
            this.mAutoAddSettingList.get(i).setListening(false);
        }
    }

    public void destroy() {
        stopListening();
        this.mAutoTracker.destroy();
    }

    private void populateSettingsList() {
        try {
            for (String str : this.mContext.getResources().getStringArray(C1893R.array.config_quickSettingsAutoAdd)) {
                String[] split = str.split(":");
                if (split.length == 2) {
                    this.mAutoAddSettingList.add(new AutoAddSetting(this.mSecureSettings, this.mHandler, split[0], this.mCurrentUser.getIdentifier(), split[1]));
                } else {
                    Log.w(TAG, "Malformed item in array: " + str);
                }
            }
        } catch (Resources.NotFoundException unused) {
            Log.w(TAG, "Missing config resource");
        }
    }

    /* renamed from: changeUser */
    public void mo43656xb844e8a5(UserHandle userHandle) {
        if (!this.mInitialized) {
            throw new IllegalStateException("AutoTileManager not initialized");
        } else if (!Thread.currentThread().equals(this.mHandler.getLooper().getThread())) {
            this.mHandler.post(new AutoTileManager$$ExternalSyntheticLambda0(this, userHandle));
        } else if (userHandle.getIdentifier() != this.mCurrentUser.getIdentifier()) {
            stopListening();
            this.mCurrentUser = userHandle;
            int size = this.mAutoAddSettingList.size();
            for (int i = 0; i < size; i++) {
                this.mAutoAddSettingList.get(i).setUserId(userHandle.getIdentifier());
            }
            this.mAutoTracker.changeUser(userHandle);
            startControllersAndSettingsListeners();
        }
    }

    public int getCurrentUserId() {
        return this.mCurrentUser.getIdentifier();
    }

    public void unmarkTileAsAutoAdded(String str) {
        this.mAutoTracker.setTileRemoved(str);
    }

    private void initWalletController() {
        Integer walletPosition;
        if (!this.mAutoTracker.isAdded(WALLET) && (walletPosition = this.mWalletController.getWalletPosition()) != null) {
            this.mHost.addTile(WALLET, walletPosition.intValue());
            this.mAutoTracker.setTileAdded(WALLET);
        }
    }

    /* access modifiers changed from: private */
    public void initSafetyTile() {
        String str = this.mSafetySpec;
        if (str != null && !this.mAutoTracker.isAdded(str)) {
            this.mHost.addTile(CustomTile.getComponentFromSpec(this.mSafetySpec), true);
            this.mAutoTracker.setTileAdded(this.mSafetySpec);
        }
    }

    /* access modifiers changed from: protected */
    public SettingObserver getSecureSettingForKey(String str) {
        Iterator<AutoAddSetting> it = this.mAutoAddSettingList.iterator();
        while (it.hasNext()) {
            SettingObserver next = it.next();
            if (Objects.equals(str, next.getKey())) {
                return next;
            }
        }
        return null;
    }

    private class AutoAddSetting extends SettingObserver {
        /* access modifiers changed from: private */
        public final String mSpec;

        AutoAddSetting(SecureSettings secureSettings, Handler handler, String str, int i, String str2) {
            super(secureSettings, handler, str, i);
            this.mSpec = str2;
        }

        /* access modifiers changed from: protected */
        public void handleValueChanged(int i, boolean z) {
            if (AutoTileManager.this.mAutoTracker.isAdded(this.mSpec)) {
                AutoTileManager.this.mHandler.post(new AutoTileManager$AutoAddSetting$$ExternalSyntheticLambda0(this));
            } else if (i != 0) {
                if (this.mSpec.startsWith(CustomTile.PREFIX)) {
                    AutoTileManager.this.mHost.addTile(CustomTile.getComponentFromSpec(this.mSpec), true);
                } else {
                    AutoTileManager.this.mHost.addTile(this.mSpec);
                }
                AutoTileManager.this.mAutoTracker.setTileAdded(this.mSpec);
                AutoTileManager.this.mHandler.post(new AutoTileManager$AutoAddSetting$$ExternalSyntheticLambda1(this));
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$handleValueChanged$0$com-android-systemui-statusbar-phone-AutoTileManager$AutoAddSetting */
        public /* synthetic */ void mo43670xad5dd92d() {
            setListening(false);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$handleValueChanged$1$com-android-systemui-statusbar-phone-AutoTileManager$AutoAddSetting */
        public /* synthetic */ void mo43671x9eaf68ae() {
            setListening(false);
        }
    }
}
