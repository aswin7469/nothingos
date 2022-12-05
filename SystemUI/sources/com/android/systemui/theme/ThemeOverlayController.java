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
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.SystemUI;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.monet.ColorScheme;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.settings.SecureSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes2.dex */
public class ThemeOverlayController extends SystemUI {
    private final Executor mBgExecutor;
    private final Handler mBgHandler;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private ColorScheme mColorScheme;
    private WallpaperColors mCurrentColors;
    private boolean mDeferredThemeEvaluation;
    private WallpaperColors mDeferredWallpaperColors;
    private int mDeferredWallpaperColorsFlags;
    private DeviceProvisionedController mDeviceProvisionedController;
    private final boolean mIsMonetEnabled;
    private final Executor mMainExecutor;
    private boolean mNeedsOverlayCreation;
    private FabricatedOverlay mNeutralOverlay;
    private FabricatedOverlay mSecondaryOverlay;
    private SecureSettings mSecureSettings;
    private boolean mSkipSettingChange;
    private final ThemeOverlayApplier mThemeManager;
    private final UserManager mUserManager;
    private UserTracker mUserTracker;
    private WakefulnessLifecycle mWakefulnessLifecycle;
    private WallpaperManager mWallpaperManager;
    protected int mMainWallpaperColor = 0;
    protected int mWallpaperAccentColor = 0;
    private boolean mAcceptColorEvents = true;
    private final DeviceProvisionedController.DeviceProvisionedListener mDeviceProvisionedListener = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.android.systemui.theme.ThemeOverlayController.1
        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public void onUserSetupChanged() {
            if (ThemeOverlayController.this.mDeviceProvisionedController.isCurrentUserSetup() && ThemeOverlayController.this.mDeferredThemeEvaluation) {
                Log.i("ThemeOverlayController", "Applying deferred theme");
                ThemeOverlayController.this.mDeferredThemeEvaluation = false;
                ThemeOverlayController.this.reevaluateSystemTheme(true);
            }
        }
    };
    private final WallpaperManager.OnColorsChangedListener mOnColorsChangedListener = new WallpaperManager.OnColorsChangedListener() { // from class: com.android.systemui.theme.ThemeOverlayController$$ExternalSyntheticLambda0
        @Override // android.app.WallpaperManager.OnColorsChangedListener
        public final void onColorsChanged(WallpaperColors wallpaperColors, int i) {
            ThemeOverlayController.this.lambda$new$0(wallpaperColors, i);
        }
    };
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.theme.ThemeOverlayController.2
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            boolean equals = "android.intent.action.MANAGED_PROFILE_ADDED".equals(intent.getAction());
            boolean equals2 = "android.intent.action.USER_SWITCHED".equals(intent.getAction());
            boolean isManagedProfile = ThemeOverlayController.this.mUserManager.isManagedProfile(intent.getIntExtra("android.intent.extra.user_handle", 0));
            if (equals2 || equals) {
                if (!ThemeOverlayController.this.mDeviceProvisionedController.isCurrentUserSetup() && isManagedProfile) {
                    Log.i("ThemeOverlayController", "User setup not finished when " + intent.getAction() + " was received. Deferring... Managed profile? " + isManagedProfile);
                    return;
                }
                Log.d("ThemeOverlayController", "Updating overlays for user switch / profile added.");
                ThemeOverlayController.this.reevaluateSystemTheme(true);
            } else if (!"android.intent.action.WALLPAPER_CHANGED".equals(intent.getAction())) {
            } else {
                ThemeOverlayController.this.mAcceptColorEvents = true;
                Log.i("ThemeOverlayController", "Allowing color events again");
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(WallpaperColors wallpaperColors, int i) {
        if (!this.mAcceptColorEvents && this.mWakefulnessLifecycle.getWakefulness() != 0) {
            this.mDeferredWallpaperColors = wallpaperColors;
            this.mDeferredWallpaperColorsFlags = i;
            Log.i("ThemeOverlayController", "colors received; processing deferred until screen off: " + wallpaperColors);
            return;
        }
        if (wallpaperColors != null) {
            this.mAcceptColorEvents = false;
            this.mDeferredWallpaperColors = null;
            this.mDeferredWallpaperColorsFlags = 0;
        }
        handleWallpaperColors(wallpaperColors, i);
    }

    private int getLatestWallpaperType() {
        return this.mWallpaperManager.getWallpaperId(2) > this.mWallpaperManager.getWallpaperId(1) ? 2 : 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleWallpaperColors(WallpaperColors wallpaperColors, int i) {
        JSONObject jSONObject;
        boolean z = this.mCurrentColors != null;
        int latestWallpaperType = getLatestWallpaperType() & i;
        if (latestWallpaperType != 0) {
            this.mCurrentColors = wallpaperColors;
            Log.d("ThemeOverlayController", "got new colors: " + wallpaperColors + " where: " + i);
        }
        DeviceProvisionedController deviceProvisionedController = this.mDeviceProvisionedController;
        if (deviceProvisionedController != null && !deviceProvisionedController.isCurrentUserSetup()) {
            if (z) {
                Log.i("ThemeOverlayController", "Wallpaper color event deferred until setup is finished: " + wallpaperColors);
                this.mDeferredThemeEvaluation = true;
                return;
            } else if (this.mDeferredThemeEvaluation) {
                Log.i("ThemeOverlayController", "Wallpaper color event received, but we already were deferring eval: " + wallpaperColors);
                return;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("During user setup, but allowing first color event: had? ");
                sb.append(z);
                sb.append(" has? ");
                sb.append(this.mCurrentColors != null);
                Log.i("ThemeOverlayController", sb.toString());
            }
        }
        String stringForUser = this.mSecureSettings.getStringForUser("theme_customization_overlay_packages", this.mUserTracker.getUserId());
        boolean z2 = i == 3;
        try {
            if (stringForUser == null) {
                jSONObject = new JSONObject();
            } else {
                jSONObject = new JSONObject(stringForUser);
            }
            if (!"preset".equals(jSONObject.optString("android.theme.customization.color_source")) && latestWallpaperType != 0) {
                this.mSkipSettingChange = true;
                if (jSONObject.has("android.theme.customization.accent_color") || jSONObject.has("android.theme.customization.system_palette")) {
                    jSONObject.remove("android.theme.customization.accent_color");
                    jSONObject.remove("android.theme.customization.system_palette");
                    jSONObject.remove("android.theme.customization.color_index");
                }
                jSONObject.put("android.theme.customization.color_both", z2 ? "1" : "0");
                jSONObject.put("android.theme.customization.color_source", i == 2 ? "lock_wallpaper" : "home_wallpaper");
                jSONObject.put("_applied_timestamp", System.currentTimeMillis());
                Log.d("ThemeOverlayController", "Updating theme setting from " + stringForUser + " to " + jSONObject.toString());
                this.mSecureSettings.putString("theme_customization_overlay_packages", jSONObject.toString());
            }
        } catch (JSONException e) {
            Log.i("ThemeOverlayController", "Failed to parse THEME_CUSTOMIZATION_OVERLAY_PACKAGES.", e);
        }
        reevaluateSystemTheme(false);
    }

    public ThemeOverlayController(Context context, BroadcastDispatcher broadcastDispatcher, Handler handler, Executor executor, Executor executor2, ThemeOverlayApplier themeOverlayApplier, SecureSettings secureSettings, WallpaperManager wallpaperManager, UserManager userManager, DeviceProvisionedController deviceProvisionedController, UserTracker userTracker, DumpManager dumpManager, FeatureFlags featureFlags, WakefulnessLifecycle wakefulnessLifecycle) {
        super(context);
        this.mIsMonetEnabled = featureFlags.isMonetEnabled();
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
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        dumpManager.registerDumpable("ThemeOverlayController", this);
    }

    @Override // com.android.systemui.SystemUI
    public void start() {
        Log.d("ThemeOverlayController", "Start");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_ADDED");
        intentFilter.addAction("android.intent.action.WALLPAPER_CHANGED");
        this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter, this.mMainExecutor, UserHandle.ALL);
        this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("theme_customization_overlay_packages"), false, new ContentObserver(this.mBgHandler) { // from class: com.android.systemui.theme.ThemeOverlayController.3
            public void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
                Log.d("ThemeOverlayController", "Overlay changed for user: " + i2);
                if (ThemeOverlayController.this.mUserTracker.getUserId() != i2) {
                    return;
                }
                if (ThemeOverlayController.this.mDeviceProvisionedController.isUserSetup(i2)) {
                    if (!ThemeOverlayController.this.mSkipSettingChange) {
                        ThemeOverlayController.this.reevaluateSystemTheme(true);
                        return;
                    }
                    Log.d("ThemeOverlayController", "Skipping setting change");
                    ThemeOverlayController.this.mSkipSettingChange = false;
                    return;
                }
                Log.i("ThemeOverlayController", "Theme application deferred when setting changed.");
                ThemeOverlayController.this.mDeferredThemeEvaluation = true;
            }
        }, -1);
        if (!this.mIsMonetEnabled) {
            return;
        }
        this.mDeviceProvisionedController.addCallback(this.mDeviceProvisionedListener);
        Runnable runnable = new Runnable() { // from class: com.android.systemui.theme.ThemeOverlayController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                ThemeOverlayController.this.lambda$start$2();
            }
        };
        if (!this.mDeviceProvisionedController.isCurrentUserSetup()) {
            runnable.run();
        } else {
            this.mBgExecutor.execute(runnable);
        }
        this.mWallpaperManager.addOnColorsChangedListener(this.mOnColorsChangedListener, null, -1);
        this.mWakefulnessLifecycle.addObserver(new WakefulnessLifecycle.Observer() { // from class: com.android.systemui.theme.ThemeOverlayController.4
            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public void onFinishedGoingToSleep() {
                if (ThemeOverlayController.this.mDeferredWallpaperColors != null) {
                    WallpaperColors wallpaperColors = ThemeOverlayController.this.mDeferredWallpaperColors;
                    int i = ThemeOverlayController.this.mDeferredWallpaperColorsFlags;
                    ThemeOverlayController.this.mDeferredWallpaperColors = null;
                    ThemeOverlayController.this.mDeferredWallpaperColorsFlags = 0;
                    ThemeOverlayController.this.handleWallpaperColors(wallpaperColors, i);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$2() {
        final WallpaperColors wallpaperColors = this.mWallpaperManager.getWallpaperColors(getLatestWallpaperType());
        Runnable runnable = new Runnable() { // from class: com.android.systemui.theme.ThemeOverlayController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ThemeOverlayController.this.lambda$start$1(wallpaperColors);
            }
        };
        if (this.mDeviceProvisionedController.isCurrentUserSetup()) {
            this.mMainExecutor.execute(runnable);
        } else {
            runnable.run();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$start$1(WallpaperColors wallpaperColors) {
        Log.d("ThemeOverlayController", "Boot colors: " + wallpaperColors);
        this.mCurrentColors = wallpaperColors;
        reevaluateSystemTheme(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reevaluateSystemTheme(boolean z) {
        int neutralColor;
        int accentColor;
        WallpaperColors wallpaperColors = this.mCurrentColors;
        if (wallpaperColors == null) {
            accentColor = 0;
            neutralColor = 0;
        } else {
            neutralColor = getNeutralColor(wallpaperColors);
            accentColor = getAccentColor(wallpaperColors);
        }
        if (this.mMainWallpaperColor == neutralColor && this.mWallpaperAccentColor == accentColor && !z) {
            return;
        }
        this.mMainWallpaperColor = neutralColor;
        this.mWallpaperAccentColor = accentColor;
        if (this.mIsMonetEnabled) {
            this.mSecondaryOverlay = getOverlay(accentColor, 1);
            this.mNeutralOverlay = getOverlay(this.mMainWallpaperColor, 0);
            this.mNeedsOverlayCreation = true;
            Log.d("ThemeOverlayController", "fetched overlays. accent: " + this.mSecondaryOverlay + " neutral: " + this.mNeutralOverlay);
        }
        updateThemeOverlays();
    }

    protected int getNeutralColor(WallpaperColors wallpaperColors) {
        return ColorScheme.getSeedColor(wallpaperColors);
    }

    protected int getAccentColor(WallpaperColors wallpaperColors) {
        return ColorScheme.getSeedColor(wallpaperColors);
    }

    protected FabricatedOverlay getOverlay(int i, int i2) {
        String str;
        ColorScheme colorScheme = new ColorScheme(i, (this.mContext.getResources().getConfiguration().uiMode & 48) == 32);
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
            } else if (i4 == 1) {
                str = "android:color/system_" + str2 + i5 + "_50";
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("android:color/system_");
                sb.append(str2);
                sb.append(i5);
                sb.append("_");
                sb.append(i4 - 1);
                sb.append("00");
                str = sb.toString();
            }
            builder.setResourceValue(str, 28, ColorUtils.setAlphaComponent(allAccentColors.get(i3).intValue(), 255));
        }
        return builder.build();
    }

    private void updateThemeOverlays() {
        FabricatedOverlay fabricatedOverlay;
        FabricatedOverlay fabricatedOverlay2;
        int userId = this.mUserTracker.getUserId();
        String stringForUser = this.mSecureSettings.getStringForUser("theme_customization_overlay_packages", userId);
        Log.d("ThemeOverlayController", "updateThemeOverlays. Setting: " + stringForUser);
        final ArrayMap arrayMap = new ArrayMap();
        if (!TextUtils.isEmpty(stringForUser)) {
            try {
                JSONObject jSONObject = new JSONObject(stringForUser);
                for (String str : ThemeOverlayApplier.THEME_CATEGORIES) {
                    if (jSONObject.has(str)) {
                        arrayMap.put(str, new OverlayIdentifier(jSONObject.getString(str)));
                    }
                }
            } catch (JSONException e) {
                Log.i("ThemeOverlayController", "Failed to parse THEME_CUSTOMIZATION_OVERLAY_PACKAGES.", e);
            }
        }
        OverlayIdentifier overlayIdentifier = (OverlayIdentifier) arrayMap.get("android.theme.customization.system_palette");
        if (this.mIsMonetEnabled && overlayIdentifier != null && overlayIdentifier.getPackageName() != null) {
            try {
                String lowerCase = overlayIdentifier.getPackageName().toLowerCase();
                if (!lowerCase.startsWith("#")) {
                    lowerCase = "#" + lowerCase;
                }
                this.mNeutralOverlay = getOverlay(Color.parseColor(lowerCase), 0);
                this.mNeedsOverlayCreation = true;
                arrayMap.remove("android.theme.customization.system_palette");
            } catch (Exception e2) {
                Log.w("ThemeOverlayController", "Invalid color definition: " + overlayIdentifier.getPackageName(), e2);
            }
        } else if (!this.mIsMonetEnabled && overlayIdentifier != null) {
            try {
                arrayMap.remove("android.theme.customization.system_palette");
            } catch (NumberFormatException unused) {
            }
        }
        OverlayIdentifier overlayIdentifier2 = (OverlayIdentifier) arrayMap.get("android.theme.customization.accent_color");
        if (this.mIsMonetEnabled && overlayIdentifier2 != null && overlayIdentifier2.getPackageName() != null) {
            try {
                String lowerCase2 = overlayIdentifier2.getPackageName().toLowerCase();
                if (!lowerCase2.startsWith("#")) {
                    lowerCase2 = "#" + lowerCase2;
                }
                this.mSecondaryOverlay = getOverlay(Color.parseColor(lowerCase2), 1);
                this.mNeedsOverlayCreation = true;
                arrayMap.remove("android.theme.customization.accent_color");
            } catch (Exception e3) {
                Log.w("ThemeOverlayController", "Invalid color definition: " + overlayIdentifier2.getPackageName(), e3);
            }
        } else if (!this.mIsMonetEnabled && overlayIdentifier2 != null) {
            try {
                Integer.parseInt(overlayIdentifier2.getPackageName().toLowerCase(), 16);
                arrayMap.remove("android.theme.customization.accent_color");
            } catch (NumberFormatException unused2) {
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
        Log.d("ThemeOverlayController", "Applying overlays: " + ((String) arrayMap.keySet().stream().map(new Function() { // from class: com.android.systemui.theme.ThemeOverlayController$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                String lambda$updateThemeOverlays$3;
                lambda$updateThemeOverlays$3 = ThemeOverlayController.lambda$updateThemeOverlays$3(arrayMap, (String) obj);
                return lambda$updateThemeOverlays$3;
            }
        }).collect(Collectors.joining(", "))));
        if (this.mNeedsOverlayCreation) {
            this.mNeedsOverlayCreation = false;
            this.mThemeManager.applyCurrentUserOverlays(arrayMap, new FabricatedOverlay[]{this.mSecondaryOverlay, this.mNeutralOverlay}, userId, hashSet);
            return;
        }
        this.mThemeManager.applyCurrentUserOverlays(arrayMap, null, userId, hashSet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ String lambda$updateThemeOverlays$3(Map map, String str) {
        return str + " -> " + map.get(str);
    }

    @Override // com.android.systemui.SystemUI, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("mSystemColors=" + this.mCurrentColors);
        printWriter.println("mMainWallpaperColor=" + Integer.toHexString(this.mMainWallpaperColor));
        printWriter.println("mWallpaperAccentColor=" + Integer.toHexString(this.mWallpaperAccentColor));
        printWriter.println("mSecondaryOverlay=" + this.mSecondaryOverlay);
        printWriter.println("mNeutralOverlay=" + this.mNeutralOverlay);
        printWriter.println("mIsMonetEnabled=" + this.mIsMonetEnabled);
        printWriter.println("mColorScheme=" + this.mColorScheme);
        printWriter.println("mNeedsOverlayCreation=" + this.mNeedsOverlayCreation);
        printWriter.println("mAcceptColorEvents=" + this.mAcceptColorEvents);
        printWriter.println("mDeferredThemeEvaluation=" + this.mDeferredThemeEvaluation);
    }
}
