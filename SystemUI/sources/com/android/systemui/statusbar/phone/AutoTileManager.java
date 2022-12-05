package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.hardware.display.ColorDisplayManager;
import android.hardware.display.NightDisplayListener;
import android.os.Handler;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$array;
import com.android.systemui.qs.AutoAddTracker;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.ReduceBrightColorsController;
import com.android.systemui.qs.SecureSetting;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.ManagedProfileController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.statusbar.policy.HotspotController;
import com.android.systemui.statusbar.policy.WalletController;
import com.android.systemui.util.UserAwareController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class AutoTileManager implements UserAwareController {
    protected final AutoAddTracker mAutoTracker;
    private final CastController mCastController;
    protected final Context mContext;
    private UserHandle mCurrentUser;
    private final DataSaverController mDataSaverController;
    private final DeviceControlsController mDeviceControlsController;
    protected final Handler mHandler;
    protected final QSTileHost mHost;
    private final HotspotController mHotspotController;
    private boolean mInitialized;
    private final boolean mIsReduceBrightColorsAvailable;
    private final ManagedProfileController mManagedProfileController;
    private final NightDisplayListener mNightDisplayListener;
    private final ReduceBrightColorsController mReduceBrightColorsController;
    protected final SecureSettings mSecureSettings;
    private final WalletController mWalletController;
    private final ArrayList<AutoAddSetting> mAutoAddSettingList = new ArrayList<>();
    private final ManagedProfileController.Callback mProfileCallback = new ManagedProfileController.Callback() { // from class: com.android.systemui.statusbar.phone.AutoTileManager.1
        @Override // com.android.systemui.statusbar.phone.ManagedProfileController.Callback
        public void onManagedProfileRemoved() {
        }

        @Override // com.android.systemui.statusbar.phone.ManagedProfileController.Callback
        public void onManagedProfileChanged() {
            if (!AutoTileManager.this.mAutoTracker.isAdded("work") && AutoTileManager.this.mManagedProfileController.hasActiveProfile()) {
                AutoTileManager.this.mHost.addTile("work");
                AutoTileManager.this.mAutoTracker.setTileAdded("work");
            }
        }
    };
    private final DataSaverController.Listener mDataSaverListener = new AnonymousClass2();
    private final HotspotController.Callback mHotspotCallback = new AnonymousClass3();
    private final DeviceControlsController.Callback mDeviceControlsCallback = new AnonymousClass4();
    @VisibleForTesting
    final NightDisplayListener.Callback mNightDisplayCallback = new AnonymousClass5();
    @VisibleForTesting
    final ReduceBrightColorsController.Listener mReduceBrightColorsCallback = new AnonymousClass6();
    @VisibleForTesting
    final CastController.Callback mCastCallback = new AnonymousClass7();

    public AutoTileManager(Context context, AutoAddTracker.Builder builder, QSTileHost qSTileHost, Handler handler, SecureSettings secureSettings, HotspotController hotspotController, DataSaverController dataSaverController, ManagedProfileController managedProfileController, NightDisplayListener nightDisplayListener, CastController castController, ReduceBrightColorsController reduceBrightColorsController, DeviceControlsController deviceControlsController, WalletController walletController, boolean z) {
        this.mContext = context;
        this.mHost = qSTileHost;
        this.mSecureSettings = secureSettings;
        UserHandle user = qSTileHost.getUserContext().getUser();
        this.mCurrentUser = user;
        this.mAutoTracker = builder.setUserId(user.getIdentifier()).build();
        this.mHandler = handler;
        this.mHotspotController = hotspotController;
        this.mDataSaverController = dataSaverController;
        this.mManagedProfileController = managedProfileController;
        this.mNightDisplayListener = nightDisplayListener;
        this.mCastController = castController;
        this.mReduceBrightColorsController = reduceBrightColorsController;
        this.mIsReduceBrightColorsAvailable = z;
        this.mDeviceControlsController = deviceControlsController;
        this.mWalletController = walletController;
    }

    public void init() {
        if (this.mInitialized) {
            Log.w("AutoTileManager", "Trying to re-initialize");
            return;
        }
        this.mAutoTracker.initialize();
        populateSettingsList();
        startControllersAndSettingsListeners();
        this.mInitialized = true;
    }

    protected void startControllersAndSettingsListeners() {
        if (!this.mAutoTracker.isAdded("hotspot")) {
            this.mHotspotController.addCallback(this.mHotspotCallback);
        }
        if (!this.mAutoTracker.isAdded("saver")) {
            this.mDataSaverController.addCallback(this.mDataSaverListener);
        }
        if (!this.mAutoTracker.isAdded("work")) {
            this.mManagedProfileController.addCallback(this.mProfileCallback);
        }
        if (!this.mAutoTracker.isAdded("night") && ColorDisplayManager.isNightDisplayAvailable(this.mContext)) {
            this.mNightDisplayListener.setCallback(this.mNightDisplayCallback);
        }
        if (!this.mAutoTracker.isAdded("cast")) {
            this.mCastController.addCallback(this.mCastCallback);
        }
        if (!this.mAutoTracker.isAdded("reduce_brightness") && this.mIsReduceBrightColorsAvailable) {
            this.mReduceBrightColorsController.addCallback(this.mReduceBrightColorsCallback);
        }
        if (!this.mAutoTracker.isAdded("controls")) {
            this.mDeviceControlsController.setCallback(this.mDeviceControlsCallback);
        }
        if (!this.mAutoTracker.isAdded("wallet")) {
            initWalletController();
        }
        int size = this.mAutoAddSettingList.size();
        for (int i = 0; i < size; i++) {
            if (!this.mAutoTracker.isAdded(this.mAutoAddSettingList.get(i).mSpec)) {
                this.mAutoAddSettingList.get(i).setListening(true);
            }
        }
    }

    protected void stopListening() {
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
        int size = this.mAutoAddSettingList.size();
        for (int i = 0; i < size; i++) {
            this.mAutoAddSettingList.get(i).setListening(false);
        }
    }

    private void populateSettingsList() {
        String[] stringArray;
        try {
            for (String str : this.mContext.getResources().getStringArray(R$array.config_quickSettingsAutoAdd)) {
                String[] split = str.split(":");
                if (split.length == 2) {
                    this.mAutoAddSettingList.add(new AutoAddSetting(this.mSecureSettings, this.mHandler, split[0], this.mCurrentUser.getIdentifier(), split[1]));
                } else {
                    Log.w("AutoTileManager", "Malformed item in array: " + str);
                }
            }
        } catch (Resources.NotFoundException unused) {
            Log.w("AutoTileManager", "Missing config resource");
        }
    }

    @Override // com.android.systemui.util.UserAwareController
    /* renamed from: changeUser */
    public void lambda$changeUser$0(final UserHandle userHandle) {
        if (!this.mInitialized) {
            throw new IllegalStateException("AutoTileManager not initialized");
        }
        if (!Thread.currentThread().equals(this.mHandler.getLooper().getThread())) {
            this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.AutoTileManager$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AutoTileManager.this.lambda$changeUser$0(userHandle);
                }
            });
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

    public void unmarkTileAsAutoAdded(String str) {
        this.mAutoTracker.setTileRemoved(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.AutoTileManager$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements DataSaverController.Listener {
        AnonymousClass2() {
        }

        @Override // com.android.systemui.statusbar.policy.DataSaverController.Listener
        public void onDataSaverChanged(boolean z) {
            if (!AutoTileManager.this.mAutoTracker.isAdded("saver") && z) {
                AutoTileManager.this.mHost.addTile("saver");
                AutoTileManager.this.mAutoTracker.setTileAdded("saver");
                AutoTileManager.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.AutoTileManager$2$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutoTileManager.AnonymousClass2.this.lambda$onDataSaverChanged$0();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onDataSaverChanged$0() {
            AutoTileManager.this.mDataSaverController.removeCallback(AutoTileManager.this.mDataSaverListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.AutoTileManager$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements HotspotController.Callback {
        AnonymousClass3() {
        }

        @Override // com.android.systemui.statusbar.policy.HotspotController.Callback
        public void onHotspotChanged(boolean z, int i) {
            if (!AutoTileManager.this.mAutoTracker.isAdded("hotspot") && z) {
                AutoTileManager.this.mHost.addTile("hotspot");
                AutoTileManager.this.mAutoTracker.setTileAdded("hotspot");
                AutoTileManager.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.AutoTileManager$3$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutoTileManager.AnonymousClass3.this.lambda$onHotspotChanged$0();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHotspotChanged$0() {
            AutoTileManager.this.mHotspotController.removeCallback(AutoTileManager.this.mHotspotCallback);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.AutoTileManager$4  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass4 implements DeviceControlsController.Callback {
        AnonymousClass4() {
        }

        @Override // com.android.systemui.statusbar.policy.DeviceControlsController.Callback
        public void onControlsUpdate(Integer num) {
            if (AutoTileManager.this.mAutoTracker.isAdded("controls")) {
                return;
            }
            if (num != null) {
                AutoTileManager.this.mHost.addTile("controls", num.intValue());
            }
            AutoTileManager.this.mAutoTracker.setTileAdded("controls");
            AutoTileManager.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.AutoTileManager$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AutoTileManager.AnonymousClass4.this.lambda$onControlsUpdate$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onControlsUpdate$0() {
            AutoTileManager.this.mDeviceControlsController.removeCallback();
        }
    }

    private void initWalletController() {
        Integer walletPosition;
        if (!this.mAutoTracker.isAdded("wallet") && (walletPosition = this.mWalletController.getWalletPosition()) != null) {
            this.mHost.addTile("wallet", walletPosition.intValue());
            this.mAutoTracker.setTileAdded("wallet");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.AutoTileManager$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements NightDisplayListener.Callback {
        AnonymousClass5() {
        }

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
            if (AutoTileManager.this.mAutoTracker.isAdded("night")) {
                return;
            }
            AutoTileManager.this.mHost.addTile("night");
            AutoTileManager.this.mAutoTracker.setTileAdded("night");
            AutoTileManager.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.AutoTileManager$5$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AutoTileManager.AnonymousClass5.this.lambda$addNightTile$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addNightTile$0() {
            AutoTileManager.this.mNightDisplayListener.setCallback((NightDisplayListener.Callback) null);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.AutoTileManager$6  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass6 implements ReduceBrightColorsController.Listener {
        AnonymousClass6() {
        }

        @Override // com.android.systemui.qs.ReduceBrightColorsController.Listener
        public void onActivated(boolean z) {
            if (z) {
                addReduceBrightColorsTile();
            }
        }

        private void addReduceBrightColorsTile() {
            if (AutoTileManager.this.mAutoTracker.isAdded("reduce_brightness")) {
                return;
            }
            AutoTileManager.this.mHost.addTile("reduce_brightness");
            AutoTileManager.this.mAutoTracker.setTileAdded("reduce_brightness");
            AutoTileManager.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.AutoTileManager$6$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AutoTileManager.AnonymousClass6.this.lambda$addReduceBrightColorsTile$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$addReduceBrightColorsTile$0() {
            AutoTileManager.this.mReduceBrightColorsController.removeCallback((ReduceBrightColorsController.Listener) this);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.phone.AutoTileManager$7  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass7 implements CastController.Callback {
        AnonymousClass7() {
        }

        @Override // com.android.systemui.statusbar.policy.CastController.Callback
        public void onCastDevicesChanged() {
            if (AutoTileManager.this.mAutoTracker.isAdded("cast")) {
                return;
            }
            boolean z = false;
            for (CastController.CastDevice castDevice : AutoTileManager.this.mCastController.getCastDevices()) {
                int i = castDevice.state;
                if (i != 2) {
                    if (i == 1) {
                    }
                }
                z = true;
            }
            if (!z) {
                return;
            }
            AutoTileManager.this.mHost.addTile("cast");
            AutoTileManager.this.mAutoTracker.setTileAdded("cast");
            AutoTileManager.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.AutoTileManager$7$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    AutoTileManager.AnonymousClass7.this.lambda$onCastDevicesChanged$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onCastDevicesChanged$0() {
            AutoTileManager.this.mCastController.removeCallback(AutoTileManager.this.mCastCallback);
        }
    }

    @VisibleForTesting
    protected SecureSetting getSecureSettingForKey(String str) {
        Iterator<AutoAddSetting> it = this.mAutoAddSettingList.iterator();
        while (it.hasNext()) {
            AutoAddSetting next = it.next();
            if (Objects.equals(str, next.getKey())) {
                return next;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class AutoAddSetting extends SecureSetting {
        private final String mSpec;

        AutoAddSetting(SecureSettings secureSettings, Handler handler, String str, int i, String str2) {
            super(secureSettings, handler, str, i);
            this.mSpec = str2;
        }

        @Override // com.android.systemui.qs.SecureSetting
        protected void handleValueChanged(int i, boolean z) {
            if (AutoTileManager.this.mAutoTracker.isAdded(this.mSpec)) {
                AutoTileManager.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.AutoTileManager$AutoAddSetting$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutoTileManager.AutoAddSetting.this.lambda$handleValueChanged$0();
                    }
                });
            } else if (i == 0) {
            } else {
                if (this.mSpec.startsWith("custom(")) {
                    AutoTileManager.this.mHost.addTile(CustomTile.getComponentFromSpec(this.mSpec), true);
                } else {
                    AutoTileManager.this.mHost.addTile(this.mSpec);
                }
                AutoTileManager.this.mAutoTracker.setTileAdded(this.mSpec);
                AutoTileManager.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.phone.AutoTileManager$AutoAddSetting$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        AutoTileManager.AutoAddSetting.this.lambda$handleValueChanged$1();
                    }
                });
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleValueChanged$0() {
            setListening(false);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$handleValueChanged$1() {
            setListening(false);
        }
    }
}
