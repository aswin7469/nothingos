package com.android.systemui.theme;

import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.om.FabricatedOverlay;
import android.content.om.OverlayIdentifier;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.CoreStartable;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.monet.ColorScheme;
import com.android.systemui.monet.Style;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.settings.SecureSettings;
import java.p026io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
import sun.util.locale.BaseLocale;

@SysUISingleton
public class ThemeOverlayController extends CoreStartable implements Dumpable {
    protected static final int ACCENT = 1;
    private static final boolean DEBUG = true;
    protected static final int NEUTRAL = 0;
    protected static final String TAG = "ThemeOverlayController";
    /* access modifiers changed from: private */
    public boolean mAcceptColorEvents = true;
    private final Executor mBgExecutor;
    private final Handler mBgHandler;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean equals = "android.intent.action.MANAGED_PROFILE_ADDED".equals(intent.getAction());
            boolean isManagedProfile = ThemeOverlayController.this.mUserManager.isManagedProfile(intent.getIntExtra("android.intent.extra.user_handle", 0));
            if (equals) {
                if (ThemeOverlayController.this.mDeviceProvisionedController.isCurrentUserSetup() || !isManagedProfile) {
                    Log.d(ThemeOverlayController.TAG, "Updating overlays for user switch / profile added.");
                    ThemeOverlayController.this.reevaluateSystemTheme(true);
                    return;
                }
                Log.i(ThemeOverlayController.TAG, "User setup not finished when " + intent.getAction() + " was received. Deferring... Managed profile? " + isManagedProfile);
            } else if (!"android.intent.action.WALLPAPER_CHANGED".equals(intent.getAction())) {
            } else {
                if (intent.getBooleanExtra("android.service.wallpaper.extra.FROM_FOREGROUND_APP", false)) {
                    boolean unused = ThemeOverlayController.this.mAcceptColorEvents = true;
                    Log.i(ThemeOverlayController.TAG, "Wallpaper changed, allowing color events again");
                    return;
                }
                Log.i(ThemeOverlayController.TAG, "Wallpaper changed from background app, keep deferring color events. Accepting: " + ThemeOverlayController.this.mAcceptColorEvents);
            }
        }
    };
    protected ColorScheme mColorScheme;
    private final SparseArray<WallpaperColors> mCurrentColors = new SparseArray<>();
    /* access modifiers changed from: private */
    public boolean mDeferredThemeEvaluation;
    /* access modifiers changed from: private */
    public final SparseArray<WallpaperColors> mDeferredWallpaperColors = new SparseArray<>();
    /* access modifiers changed from: private */
    public final SparseIntArray mDeferredWallpaperColorsFlags = new SparseIntArray();
    /* access modifiers changed from: private */
    public final DeviceProvisionedController mDeviceProvisionedController;
    private final DeviceProvisionedController.DeviceProvisionedListener mDeviceProvisionedListener = new DeviceProvisionedController.DeviceProvisionedListener() {
        public void onUserSetupChanged() {
            if (ThemeOverlayController.this.mDeviceProvisionedController.isCurrentUserSetup() && ThemeOverlayController.this.mDeferredThemeEvaluation) {
                Log.i(ThemeOverlayController.TAG, "Applying deferred theme");
                boolean unused = ThemeOverlayController.this.mDeferredThemeEvaluation = false;
                ThemeOverlayController.this.reevaluateSystemTheme(true);
            }
        }
    };
    private final boolean mIsMonetEnabled;
    private final Executor mMainExecutor;
    protected int mMainWallpaperColor = 0;
    private boolean mNeedsOverlayCreation;
    private FabricatedOverlay mNeutralOverlay;
    private final WallpaperManager.OnColorsChangedListener mOnColorsChangedListener = new WallpaperManager.OnColorsChangedListener() {
        public void onColorsChanged(WallpaperColors wallpaperColors, int i) {
            throw new IllegalStateException("This should never be invoked, all messages should arrive on the overload that has a user id");
        }

        public void onColorsChanged(WallpaperColors wallpaperColors, int i, int i2) {
            boolean z = i2 == ThemeOverlayController.this.mUserTracker.getUserId();
            if (!z || ThemeOverlayController.this.mAcceptColorEvents || ThemeOverlayController.this.mWakefulnessLifecycle.getWakefulness() == 0) {
                if (z && wallpaperColors != null) {
                    boolean unused = ThemeOverlayController.this.mAcceptColorEvents = false;
                    ThemeOverlayController.this.mDeferredWallpaperColors.put(i2, (Object) null);
                    ThemeOverlayController.this.mDeferredWallpaperColorsFlags.put(i2, 0);
                }
                ThemeOverlayController.this.handleWallpaperColors(wallpaperColors, i, i2);
                return;
            }
            ThemeOverlayController.this.mDeferredWallpaperColors.put(i2, wallpaperColors);
            ThemeOverlayController.this.mDeferredWallpaperColorsFlags.put(i2, i);
            Log.i(ThemeOverlayController.TAG, "colors received; processing deferred until screen off: " + wallpaperColors + " user: " + i2);
        }
    };
    private final Resources mResources;
    private FabricatedOverlay mSecondaryOverlay;
    private final SecureSettings mSecureSettings;
    /* access modifiers changed from: private */
    public boolean mSkipSettingChange;
    private final ThemeOverlayApplier mThemeManager;
    private Style mThemeStyle = Style.TONAL_SPOT;
    /* access modifiers changed from: private */
    public final UserManager mUserManager;
    /* access modifiers changed from: private */
    public final UserTracker mUserTracker;
    private final UserTracker.Callback mUserTrackerCallback = new UserTracker.Callback() {
        public void onUserChanged(int i, Context context) {
            boolean isManagedProfile = ThemeOverlayController.this.mUserManager.isManagedProfile(i);
            if (ThemeOverlayController.this.mDeviceProvisionedController.isCurrentUserSetup() || !isManagedProfile) {
                Log.d(ThemeOverlayController.TAG, "Updating overlays for user switch / profile added.");
                ThemeOverlayController.this.reevaluateSystemTheme(true);
                return;
            }
            Log.i(ThemeOverlayController.TAG, "User setup not finished when new user event was received. Deferring... Managed profile? " + isManagedProfile);
        }
    };
    /* access modifiers changed from: private */
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    private final WallpaperManager mWallpaperManager;

    private int getLatestWallpaperType(int i) {
        return this.mWallpaperManager.getWallpaperIdForUser(2, i) > this.mWallpaperManager.getWallpaperIdForUser(1, i) ? 2 : 1;
    }

    private boolean isSeedColorSet(JSONObject jSONObject, WallpaperColors wallpaperColors) {
        String str;
        if (wallpaperColors == null || (str = (String) jSONObject.opt("android.theme.customization.system_palette")) == null) {
            return false;
        }
        if (!str.startsWith("#")) {
            str = "#" + str;
        }
        int parseColor = Color.parseColor(str);
        for (Integer intValue : ColorScheme.getSeedColors(wallpaperColors)) {
            if (intValue.intValue() == parseColor) {
                Log.d(TAG, "Same as previous set system palette: " + str);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0109, code lost:
        if (r9.has("android.theme.customization.system_palette") != false) goto L_0x010b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleWallpaperColors(android.app.WallpaperColors r13, int r14, int r15) {
        /*
            r12 = this;
            java.lang.String r0 = "android.theme.customization.accent_color"
            java.lang.String r1 = "android.theme.customization.color_source"
            java.lang.String r2 = "Updating theme setting from "
            com.android.systemui.settings.UserTracker r3 = r12.mUserTracker
            int r3 = r3.getUserId()
            android.util.SparseArray<android.app.WallpaperColors> r4 = r12.mCurrentColors
            java.lang.Object r4 = r4.get(r15)
            r5 = 0
            r6 = 1
            if (r4 == 0) goto L_0x0018
            r4 = r6
            goto L_0x0019
        L_0x0018:
            r4 = r5
        L_0x0019:
            int r7 = r12.getLatestWallpaperType(r15)
            r7 = r7 & r14
            java.lang.String r8 = "ThemeOverlayController"
            if (r7 == 0) goto L_0x0043
            android.util.SparseArray<android.app.WallpaperColors> r9 = r12.mCurrentColors
            r9.put(r15, r13)
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r10 = "got new colors: "
            r9.<init>((java.lang.String) r10)
            java.lang.StringBuilder r9 = r9.append((java.lang.Object) r13)
            java.lang.String r10 = " where: "
            java.lang.StringBuilder r9 = r9.append((java.lang.String) r10)
            java.lang.StringBuilder r9 = r9.append((int) r14)
            java.lang.String r9 = r9.toString()
            android.util.Log.d(r8, r9)
        L_0x0043:
            if (r15 == r3) goto L_0x006c
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            java.lang.String r14 = "Colors "
            r12.<init>((java.lang.String) r14)
            java.lang.StringBuilder r12 = r12.append((java.lang.Object) r13)
            java.lang.String r13 = " for user "
            java.lang.StringBuilder r12 = r12.append((java.lang.String) r13)
            java.lang.StringBuilder r12 = r12.append((int) r15)
            java.lang.String r13 = ". Not for current user: "
            java.lang.StringBuilder r12 = r12.append((java.lang.String) r13)
            java.lang.StringBuilder r12 = r12.append((int) r3)
            java.lang.String r12 = r12.toString()
            android.util.Log.d(r8, r12)
            return
        L_0x006c:
            com.android.systemui.statusbar.policy.DeviceProvisionedController r9 = r12.mDeviceProvisionedController
            if (r9 == 0) goto L_0x00cb
            boolean r9 = r9.isCurrentUserSetup()
            if (r9 != 0) goto L_0x00cb
            if (r4 == 0) goto L_0x008d
            java.lang.StringBuilder r14 = new java.lang.StringBuilder
            java.lang.String r15 = "Wallpaper color event deferred until setup is finished: "
            r14.<init>((java.lang.String) r15)
            java.lang.StringBuilder r13 = r14.append((java.lang.Object) r13)
            java.lang.String r13 = r13.toString()
            android.util.Log.i(r8, r13)
            r12.mDeferredThemeEvaluation = r6
            return
        L_0x008d:
            boolean r9 = r12.mDeferredThemeEvaluation
            if (r9 == 0) goto L_0x00a4
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            java.lang.String r14 = "Wallpaper color event received, but we already were deferring eval: "
            r12.<init>((java.lang.String) r14)
            java.lang.StringBuilder r12 = r12.append((java.lang.Object) r13)
            java.lang.String r12 = r12.toString()
            android.util.Log.i(r8, r12)
            return
        L_0x00a4:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r10 = "During user setup, but allowing first color event: had? "
            r9.<init>((java.lang.String) r10)
            java.lang.StringBuilder r4 = r9.append((boolean) r4)
            java.lang.String r9 = " has? "
            java.lang.StringBuilder r4 = r4.append((java.lang.String) r9)
            android.util.SparseArray<android.app.WallpaperColors> r9 = r12.mCurrentColors
            java.lang.Object r15 = r9.get(r15)
            if (r15 == 0) goto L_0x00bf
            r15 = r6
            goto L_0x00c0
        L_0x00bf:
            r15 = r5
        L_0x00c0:
            java.lang.StringBuilder r15 = r4.append((boolean) r15)
            java.lang.String r15 = r15.toString()
            android.util.Log.i(r8, r15)
        L_0x00cb:
            com.android.systemui.util.settings.SecureSettings r15 = r12.mSecureSettings
            java.lang.String r4 = "theme_customization_overlay_packages"
            java.lang.String r15 = r15.getStringForUser(r4, r3)
            r3 = 3
            if (r14 != r3) goto L_0x00d9
            r3 = r6
            goto L_0x00da
        L_0x00d9:
            r3 = r5
        L_0x00da:
            if (r15 != 0) goto L_0x00e2
            org.json.JSONObject r9 = new org.json.JSONObject     // Catch:{ JSONException -> 0x015f }
            r9.<init>()     // Catch:{ JSONException -> 0x015f }
            goto L_0x00e7
        L_0x00e2:
            org.json.JSONObject r9 = new org.json.JSONObject     // Catch:{ JSONException -> 0x015f }
            r9.<init>((java.lang.String) r15)     // Catch:{ JSONException -> 0x015f }
        L_0x00e7:
            java.lang.String r10 = "preset"
            java.lang.String r11 = r9.optString(r1)     // Catch:{ JSONException -> 0x015f }
            boolean r10 = r10.equals(r11)     // Catch:{ JSONException -> 0x015f }
            if (r10 != 0) goto L_0x0165
            if (r7 == 0) goto L_0x0165
            boolean r13 = r12.isSeedColorSet(r9, r13)     // Catch:{ JSONException -> 0x015f }
            if (r13 != 0) goto L_0x0165
            r12.mSkipSettingChange = r6     // Catch:{ JSONException -> 0x015f }
            boolean r13 = r9.has(r0)     // Catch:{ JSONException -> 0x015f }
            java.lang.String r6 = "android.theme.customization.system_palette"
            if (r13 != 0) goto L_0x010b
            boolean r13 = r9.has(r6)     // Catch:{ JSONException -> 0x015f }
            if (r13 == 0) goto L_0x0116
        L_0x010b:
            r9.remove(r0)     // Catch:{ JSONException -> 0x015f }
            r9.remove(r6)     // Catch:{ JSONException -> 0x015f }
            java.lang.String r13 = "android.theme.customization.color_index"
            r9.remove(r13)     // Catch:{ JSONException -> 0x015f }
        L_0x0116:
            java.lang.String r13 = "android.theme.customization.color_both"
            if (r3 == 0) goto L_0x011d
            java.lang.String r0 = "1"
            goto L_0x011f
        L_0x011d:
            java.lang.String r0 = "0"
        L_0x011f:
            r9.put((java.lang.String) r13, (java.lang.Object) r0)     // Catch:{ JSONException -> 0x015f }
            r13 = 2
            if (r14 != r13) goto L_0x0128
            java.lang.String r13 = "lock_wallpaper"
            goto L_0x012a
        L_0x0128:
            java.lang.String r13 = "home_wallpaper"
        L_0x012a:
            r9.put((java.lang.String) r1, (java.lang.Object) r13)     // Catch:{ JSONException -> 0x015f }
            java.lang.String r13 = "_applied_timestamp"
            long r0 = java.lang.System.currentTimeMillis()     // Catch:{ JSONException -> 0x015f }
            r9.put((java.lang.String) r13, (long) r0)     // Catch:{ JSONException -> 0x015f }
            java.lang.StringBuilder r13 = new java.lang.StringBuilder     // Catch:{ JSONException -> 0x015f }
            r13.<init>((java.lang.String) r2)     // Catch:{ JSONException -> 0x015f }
            java.lang.StringBuilder r13 = r13.append((java.lang.String) r15)     // Catch:{ JSONException -> 0x015f }
            java.lang.String r14 = " to "
            java.lang.StringBuilder r13 = r13.append((java.lang.String) r14)     // Catch:{ JSONException -> 0x015f }
            java.lang.String r14 = r9.toString()     // Catch:{ JSONException -> 0x015f }
            java.lang.StringBuilder r13 = r13.append((java.lang.String) r14)     // Catch:{ JSONException -> 0x015f }
            java.lang.String r13 = r13.toString()     // Catch:{ JSONException -> 0x015f }
            android.util.Log.d(r8, r13)     // Catch:{ JSONException -> 0x015f }
            com.android.systemui.util.settings.SecureSettings r13 = r12.mSecureSettings     // Catch:{ JSONException -> 0x015f }
            java.lang.String r14 = r9.toString()     // Catch:{ JSONException -> 0x015f }
            r15 = -2
            r13.putStringForUser(r4, r14, r15)     // Catch:{ JSONException -> 0x015f }
            goto L_0x0165
        L_0x015f:
            r13 = move-exception
            java.lang.String r14 = "Failed to parse THEME_CUSTOMIZATION_OVERLAY_PACKAGES."
            android.util.Log.i(r8, r14, r13)
        L_0x0165:
            r12.reevaluateSystemTheme(r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.theme.ThemeOverlayController.handleWallpaperColors(android.app.WallpaperColors, int, int):void");
    }

    @Inject
    public ThemeOverlayController(Context context, BroadcastDispatcher broadcastDispatcher, @Background Handler handler, @Main Executor executor, @Background Executor executor2, ThemeOverlayApplier themeOverlayApplier, SecureSettings secureSettings, WallpaperManager wallpaperManager, UserManager userManager, DeviceProvisionedController deviceProvisionedController, UserTracker userTracker, DumpManager dumpManager, FeatureFlags featureFlags, @Main Resources resources, WakefulnessLifecycle wakefulnessLifecycle) {
        super(context);
        this.mIsMonetEnabled = featureFlags.isEnabled(Flags.MONET);
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mUserManager = userManager;
        this.mBgExecutor = executor2;
        this.mMainExecutor = executor;
        this.mBgHandler = handler;
        this.mThemeManager = themeOverlayApplier;
        this.mSecureSettings = secureSettings;
        this.mWallpaperManager = wallpaperManager;
        this.mUserTracker = userTracker;
        this.mResources = resources;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        dumpManager.registerDumpable(TAG, this);
    }

    public void start() {
        Log.d(TAG, "Start");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_ADDED");
        intentFilter.addAction("android.intent.action.WALLPAPER_CHANGED");
        this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter, this.mMainExecutor, UserHandle.ALL);
        this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("theme_customization_overlay_packages"), false, (ContentObserver) new ContentObserver(this.mBgHandler) {
            public void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
                Log.d(ThemeOverlayController.TAG, "Overlay changed for user: " + i2);
                if (ThemeOverlayController.this.mUserTracker.getUserId() == i2) {
                    if (!ThemeOverlayController.this.mDeviceProvisionedController.isUserSetup(i2)) {
                        Log.i(ThemeOverlayController.TAG, "Theme application deferred when setting changed.");
                        boolean unused = ThemeOverlayController.this.mDeferredThemeEvaluation = true;
                    } else if (ThemeOverlayController.this.mSkipSettingChange) {
                        Log.d(ThemeOverlayController.TAG, "Skipping setting change");
                        boolean unused2 = ThemeOverlayController.this.mSkipSettingChange = false;
                    } else {
                        ThemeOverlayController.this.reevaluateSystemTheme(true);
                    }
                }
            }
        }, -1);
        if (this.mIsMonetEnabled) {
            this.mUserTracker.addCallback(this.mUserTrackerCallback, this.mMainExecutor);
            this.mDeviceProvisionedController.addCallback(this.mDeviceProvisionedListener);
            if (this.mIsMonetEnabled) {
                ThemeOverlayController$$ExternalSyntheticLambda1 themeOverlayController$$ExternalSyntheticLambda1 = new ThemeOverlayController$$ExternalSyntheticLambda1(this);
                if (!this.mDeviceProvisionedController.isCurrentUserSetup()) {
                    themeOverlayController$$ExternalSyntheticLambda1.run();
                } else {
                    this.mBgExecutor.execute(themeOverlayController$$ExternalSyntheticLambda1);
                }
                this.mWallpaperManager.addOnColorsChangedListener(this.mOnColorsChangedListener, (Handler) null, -1);
                this.mWakefulnessLifecycle.addObserver(new WakefulnessLifecycle.Observer() {
                    public void onFinishedGoingToSleep() {
                        int userId = ThemeOverlayController.this.mUserTracker.getUserId();
                        WallpaperColors wallpaperColors = (WallpaperColors) ThemeOverlayController.this.mDeferredWallpaperColors.get(userId);
                        if (wallpaperColors != null) {
                            int i = ThemeOverlayController.this.mDeferredWallpaperColorsFlags.get(userId);
                            ThemeOverlayController.this.mDeferredWallpaperColors.put(userId, (Object) null);
                            ThemeOverlayController.this.mDeferredWallpaperColorsFlags.put(userId, 0);
                            ThemeOverlayController.this.handleWallpaperColors(wallpaperColors, i, userId);
                        }
                    }
                });
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$start$1$com-android-systemui-theme-ThemeOverlayController  reason: not valid java name */
    public /* synthetic */ void m3255lambda$start$1$comandroidsystemuithemeThemeOverlayController() {
        ThemeOverlayController$$ExternalSyntheticLambda0 themeOverlayController$$ExternalSyntheticLambda0 = new ThemeOverlayController$$ExternalSyntheticLambda0(this, this.mWallpaperManager.getWallpaperColors(getLatestWallpaperType(this.mUserTracker.getUserId())));
        if (this.mDeviceProvisionedController.isCurrentUserSetup()) {
            this.mMainExecutor.execute(themeOverlayController$$ExternalSyntheticLambda0);
        } else {
            themeOverlayController$$ExternalSyntheticLambda0.run();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$start$0$com-android-systemui-theme-ThemeOverlayController  reason: not valid java name */
    public /* synthetic */ void m3254lambda$start$0$comandroidsystemuithemeThemeOverlayController(WallpaperColors wallpaperColors) {
        Log.d(TAG, "Boot colors: " + wallpaperColors);
        this.mCurrentColors.put(this.mUserTracker.getUserId(), wallpaperColors);
        reevaluateSystemTheme(false);
    }

    /* access modifiers changed from: private */
    public void reevaluateSystemTheme(boolean z) {
        int i;
        WallpaperColors wallpaperColors = this.mCurrentColors.get(this.mUserTracker.getUserId());
        if (wallpaperColors == null) {
            i = 0;
        } else {
            i = getNeutralColor(wallpaperColors);
        }
        if (this.mMainWallpaperColor != i || z) {
            this.mMainWallpaperColor = i;
            if (this.mIsMonetEnabled) {
                Style fetchThemeStyleFromSetting = fetchThemeStyleFromSetting();
                this.mThemeStyle = fetchThemeStyleFromSetting;
                this.mSecondaryOverlay = getOverlay(this.mMainWallpaperColor, 1, fetchThemeStyleFromSetting);
                this.mNeutralOverlay = getOverlay(this.mMainWallpaperColor, 0, this.mThemeStyle);
                this.mNeedsOverlayCreation = true;
                Log.d(TAG, "fetched overlays. accent: " + this.mSecondaryOverlay + " neutral: " + this.mNeutralOverlay);
            }
            updateThemeOverlays();
        }
    }

    /* access modifiers changed from: protected */
    public int getNeutralColor(WallpaperColors wallpaperColors) {
        return ColorScheme.getSeedColor(wallpaperColors);
    }

    /* access modifiers changed from: protected */
    public int getAccentColor(WallpaperColors wallpaperColors) {
        return ColorScheme.getSeedColor(wallpaperColors);
    }

    /* access modifiers changed from: protected */
    public FabricatedOverlay getOverlay(int i, int i2, Style style) {
        String str;
        ColorScheme colorScheme = new ColorScheme(i, (this.mResources.getConfiguration().uiMode & 48) == 32, style);
        this.mColorScheme = colorScheme;
        List<Integer> allAccentColors = i2 == 1 ? colorScheme.getAllAccentColors() : colorScheme.getAllNeutralColors();
        String str2 = i2 == 1 ? "accent" : "neutral";
        int size = this.mColorScheme.getAccent1().size();
        FabricatedOverlay.Builder builder = new FabricatedOverlay.Builder("com.android.systemui", str2, "android");
        for (int i3 = 0; i3 < allAccentColors.size(); i3++) {
            int i4 = i3 % size;
            int i5 = (i3 / size) + 1;
            if (i4 == 0) {
                str = "android:color/system_" + str2 + i5 + "_10";
            } else if (i4 != 1) {
                str = "android:color/system_" + str2 + i5 + BaseLocale.SEP + (i4 - 1) + "00";
            } else {
                str = "android:color/system_" + str2 + i5 + "_50";
            }
            builder.setResourceValue(str, 28, ColorUtils.setAlphaComponent(allAccentColors.get(i3).intValue(), 255));
        }
        return builder.build();
    }

    /* JADX WARNING: Removed duplicated region for block: B:3:0x0014  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean colorSchemeIsApplied(java.util.Set<android.os.UserHandle> r6) {
        /*
            r5 = this;
            android.util.ArraySet r0 = new android.util.ArraySet
            r0.<init>(r6)
            android.os.UserHandle r6 = android.os.UserHandle.SYSTEM
            r0.add(r6)
            java.util.Iterator r6 = r0.iterator()
        L_0x000e:
            boolean r0 = r6.hasNext()
            if (r0 == 0) goto L_0x00cb
            java.lang.Object r0 = r6.next()
            android.os.UserHandle r0 = (android.os.UserHandle) r0
            boolean r1 = r0.isSystem()
            r2 = 0
            if (r1 == 0) goto L_0x0024
            android.content.res.Resources r0 = r5.mResources
            goto L_0x002e
        L_0x0024:
            android.content.Context r1 = r5.mContext
            android.content.Context r0 = r1.createContextAsUser(r0, r2)
            android.content.res.Resources r0 = r0.getResources()
        L_0x002e:
            android.content.Context r1 = r5.mContext
            android.content.res.Resources$Theme r1 = r1.getTheme()
            r3 = 17170494(0x106003e, float:2.4612087E-38)
            int r1 = r0.getColor(r3, r1)
            com.android.systemui.monet.ColorScheme r3 = r5.mColorScheme
            java.util.List r3 = r3.getAccent1()
            r4 = 6
            java.lang.Object r3 = r3.get(r4)
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r3 = r3.intValue()
            if (r1 != r3) goto L_0x00ca
            android.content.Context r1 = r5.mContext
            android.content.res.Resources$Theme r1 = r1.getTheme()
            r3 = 17170507(0x106004b, float:2.4612123E-38)
            int r1 = r0.getColor(r3, r1)
            com.android.systemui.monet.ColorScheme r3 = r5.mColorScheme
            java.util.List r3 = r3.getAccent2()
            java.lang.Object r3 = r3.get(r4)
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r3 = r3.intValue()
            if (r1 != r3) goto L_0x00ca
            android.content.Context r1 = r5.mContext
            android.content.res.Resources$Theme r1 = r1.getTheme()
            r3 = 17170520(0x1060058, float:2.461216E-38)
            int r1 = r0.getColor(r3, r1)
            com.android.systemui.monet.ColorScheme r3 = r5.mColorScheme
            java.util.List r3 = r3.getAccent3()
            java.lang.Object r3 = r3.get(r4)
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r3 = r3.intValue()
            if (r1 != r3) goto L_0x00ca
            android.content.Context r1 = r5.mContext
            android.content.res.Resources$Theme r1 = r1.getTheme()
            r3 = 17170468(0x1060024, float:2.4612014E-38)
            int r1 = r0.getColor(r3, r1)
            com.android.systemui.monet.ColorScheme r3 = r5.mColorScheme
            java.util.List r3 = r3.getNeutral1()
            java.lang.Object r3 = r3.get(r4)
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r3 = r3.intValue()
            if (r1 != r3) goto L_0x00ca
            android.content.Context r1 = r5.mContext
            android.content.res.Resources$Theme r1 = r1.getTheme()
            r3 = 17170481(0x1060031, float:2.461205E-38)
            int r0 = r0.getColor(r3, r1)
            com.android.systemui.monet.ColorScheme r1 = r5.mColorScheme
            java.util.List r1 = r1.getNeutral2()
            java.lang.Object r1 = r1.get(r4)
            java.lang.Integer r1 = (java.lang.Integer) r1
            int r1 = r1.intValue()
            if (r0 == r1) goto L_0x000e
        L_0x00ca:
            return r2
        L_0x00cb:
            r5 = 1
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.theme.ThemeOverlayController.colorSchemeIsApplied(java.util.Set):boolean");
    }

    private void updateThemeOverlays() {
        FabricatedOverlay fabricatedOverlay;
        FabricatedOverlay fabricatedOverlay2;
        int userId = this.mUserTracker.getUserId();
        String stringForUser = this.mSecureSettings.getStringForUser("theme_customization_overlay_packages", userId);
        Log.d(TAG, "updateThemeOverlays. Setting: " + stringForUser);
        ArrayMap arrayMap = new ArrayMap();
        if (!TextUtils.isEmpty(stringForUser)) {
            try {
                JSONObject jSONObject = new JSONObject(stringForUser);
                for (String next : ThemeOverlayApplier.THEME_CATEGORIES) {
                    if (jSONObject.has(next)) {
                        arrayMap.put(next, new OverlayIdentifier(jSONObject.getString(next)));
                    }
                }
            } catch (JSONException e) {
                Log.i(TAG, "Failed to parse THEME_CUSTOMIZATION_OVERLAY_PACKAGES.", e);
            }
        }
        OverlayIdentifier overlayIdentifier = (OverlayIdentifier) arrayMap.get("android.theme.customization.system_palette");
        if (this.mIsMonetEnabled && overlayIdentifier != null && overlayIdentifier.getPackageName() != null) {
            try {
                String lowerCase = overlayIdentifier.getPackageName().toLowerCase();
                if (!lowerCase.startsWith("#")) {
                    lowerCase = "#" + lowerCase;
                }
                int parseColor = Color.parseColor(lowerCase);
                this.mNeutralOverlay = getOverlay(parseColor, 0, this.mThemeStyle);
                this.mSecondaryOverlay = getOverlay(parseColor, 1, this.mThemeStyle);
                this.mNeedsOverlayCreation = true;
                arrayMap.remove("android.theme.customization.system_palette");
                arrayMap.remove("android.theme.customization.accent_color");
            } catch (Exception e2) {
                Log.w(TAG, "Invalid color definition: " + overlayIdentifier.getPackageName(), e2);
            }
        } else if (!this.mIsMonetEnabled && overlayIdentifier != null) {
            try {
                arrayMap.remove("android.theme.customization.system_palette");
                arrayMap.remove("android.theme.customization.accent_color");
            } catch (NumberFormatException unused) {
            }
        }
        if (!arrayMap.containsKey("android.theme.customization.system_palette") && (fabricatedOverlay2 = this.mNeutralOverlay) != null) {
            arrayMap.put("android.theme.customization.system_palette", fabricatedOverlay2.getIdentifier());
        }
        if (!arrayMap.containsKey("android.theme.customization.accent_color") && (fabricatedOverlay = this.mSecondaryOverlay) != null) {
            arrayMap.put("android.theme.customization.accent_color", fabricatedOverlay.getIdentifier());
        }
        HashSet hashSet = new HashSet();
        for (UserInfo userInfo : this.mUserManager.getEnabledProfiles(userId)) {
            if (userInfo.isManagedProfile()) {
                hashSet.add(userInfo.getUserHandle());
            }
        }
        if (colorSchemeIsApplied(hashSet)) {
            Log.d(TAG, "Skipping overlay creation. Theme was already: " + this.mColorScheme);
            return;
        }
        Log.d(TAG, "Applying overlays: " + ((String) arrayMap.keySet().stream().map(new ThemeOverlayController$$ExternalSyntheticLambda2(arrayMap)).collect(Collectors.joining(", "))));
        if (this.mNeedsOverlayCreation) {
            this.mNeedsOverlayCreation = false;
            this.mThemeManager.applyCurrentUserOverlays(arrayMap, new FabricatedOverlay[]{this.mSecondaryOverlay, this.mNeutralOverlay}, userId, hashSet);
            return;
        }
        this.mThemeManager.applyCurrentUserOverlays(arrayMap, (FabricatedOverlay[]) null, userId, hashSet);
    }

    static /* synthetic */ String lambda$updateThemeOverlays$2(Map map, String str) {
        return str + " -> " + map.get(str);
    }

    private Style fetchThemeStyleFromSetting() {
        List asList = Arrays.asList(Style.EXPRESSIVE, Style.SPRITZ, Style.TONAL_SPOT, Style.FRUIT_SALAD, Style.RAINBOW, Style.VIBRANT);
        Style style = this.mThemeStyle;
        String stringForUser = this.mSecureSettings.getStringForUser("theme_customization_overlay_packages", this.mUserTracker.getUserId());
        if (TextUtils.isEmpty(stringForUser)) {
            return style;
        }
        try {
            Style valueOf = Style.valueOf(new JSONObject(stringForUser).getString("android.theme.customization.theme_style"));
            if (!asList.contains(valueOf)) {
                valueOf = Style.TONAL_SPOT;
            }
            return valueOf;
        } catch (IllegalArgumentException | JSONException e) {
            Log.i(TAG, "Failed to parse THEME_CUSTOMIZATION_OVERLAY_PACKAGES.", e);
            return Style.TONAL_SPOT;
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("mSystemColors=" + this.mCurrentColors);
        printWriter.println("mMainWallpaperColor=" + Integer.toHexString(this.mMainWallpaperColor));
        printWriter.println("mSecondaryOverlay=" + this.mSecondaryOverlay);
        printWriter.println("mNeutralOverlay=" + this.mNeutralOverlay);
        printWriter.println("mIsMonetEnabled=" + this.mIsMonetEnabled);
        printWriter.println("mColorScheme=" + this.mColorScheme);
        printWriter.println("mNeedsOverlayCreation=" + this.mNeedsOverlayCreation);
        printWriter.println("mAcceptColorEvents=" + this.mAcceptColorEvents);
        printWriter.println("mDeferredThemeEvaluation=" + this.mDeferredThemeEvaluation);
        printWriter.println("mThemeStyle=" + this.mThemeStyle);
    }
}
