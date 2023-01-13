package com.android.systemui.statusbar.policy;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.slice.compat.SliceProviderCompat;
import com.android.systemui.C1894R;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.SeedResponse;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000Y\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u000e\b\u0007\u0018\u0000 %2\u00020\u0001:\u0001%B'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0018\u0010\u0017\u001a\u00020\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\b\u0010\u001d\u001a\u00020\u0018H\u0002J\b\u0010\u001e\u001a\u00020\u0018H\u0002J\b\u0010\u001f\u001a\u00020\u0018H\u0016J\u0016\u0010 \u001a\u00020\u00182\f\u0010!\u001a\b\u0012\u0004\u0012\u00020#0\"H\u0002J\u0010\u0010$\u001a\u00020\u00182\u0006\u0010\u000b\u001a\u00020\fH\u0016R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0004\n\u0002\u0010\u000fR\u001e\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u000e¢\u0006\u0010\n\u0002\u0010\u0016\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006&"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/DeviceControlsControllerImpl;", "Lcom/android/systemui/statusbar/policy/DeviceControlsController;", "context", "Landroid/content/Context;", "controlsComponent", "Lcom/android/systemui/controls/dagger/ControlsComponent;", "userContextProvider", "Lcom/android/systemui/settings/UserContextProvider;", "secureSettings", "Lcom/android/systemui/util/settings/SecureSettings;", "(Landroid/content/Context;Lcom/android/systemui/controls/dagger/ControlsComponent;Lcom/android/systemui/settings/UserContextProvider;Lcom/android/systemui/util/settings/SecureSettings;)V", "callback", "Lcom/android/systemui/statusbar/policy/DeviceControlsController$Callback;", "listingCallback", "com/android/systemui/statusbar/policy/DeviceControlsControllerImpl$listingCallback$1", "Lcom/android/systemui/statusbar/policy/DeviceControlsControllerImpl$listingCallback$1;", "position", "", "getPosition$SystemUI_nothingRelease", "()Ljava/lang/Integer;", "setPosition$SystemUI_nothingRelease", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "addPackageToSeededSet", "", "prefs", "Landroid/content/SharedPreferences;", "pkg", "", "checkMigrationToQs", "fireControlsUpdate", "removeCallback", "seedFavorites", "serviceInfos", "", "Lcom/android/systemui/controls/ControlsServiceInfo;", "setCallback", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DeviceControlsControllerImpl.kt */
public final class DeviceControlsControllerImpl implements DeviceControlsController {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String PREFS_CONTROLS_FILE = "controls_prefs";
    public static final String PREFS_CONTROLS_SEEDING_COMPLETED = "SeedingCompleted";
    public static final String PREFS_SETTINGS_DIALOG_ATTEMPTS = "show_settings_attempts";
    public static final int QS_DEFAULT_POSITION = 7;
    public static final int QS_PRIORITY_POSITION = 3;
    private static final int SEEDING_MAX = 2;
    private static final String TAG = "DeviceControlsControllerImpl";
    private DeviceControlsController.Callback callback;
    private final Context context;
    private final ControlsComponent controlsComponent;
    private final DeviceControlsControllerImpl$listingCallback$1 listingCallback = new DeviceControlsControllerImpl$listingCallback$1(this);
    private Integer position;
    private final SecureSettings secureSettings;
    private final UserContextProvider userContextProvider;

    @Inject
    public DeviceControlsControllerImpl(Context context2, ControlsComponent controlsComponent2, UserContextProvider userContextProvider2, SecureSettings secureSettings2) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(controlsComponent2, "controlsComponent");
        Intrinsics.checkNotNullParameter(userContextProvider2, "userContextProvider");
        Intrinsics.checkNotNullParameter(secureSettings2, "secureSettings");
        this.context = context2;
        this.controlsComponent = controlsComponent2;
        this.userContextProvider = userContextProvider2;
        this.secureSettings = secureSettings2;
    }

    public final Integer getPosition$SystemUI_nothingRelease() {
        return this.position;
    }

    public final void setPosition$SystemUI_nothingRelease(Integer num) {
        this.position = num;
    }

    @Metadata(mo65042d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0004\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\bXT¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\f"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/DeviceControlsControllerImpl$Companion;", "", "()V", "PREFS_CONTROLS_FILE", "", "PREFS_CONTROLS_SEEDING_COMPLETED", "PREFS_SETTINGS_DIALOG_ATTEMPTS", "QS_DEFAULT_POSITION", "", "QS_PRIORITY_POSITION", "SEEDING_MAX", "TAG", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: DeviceControlsControllerImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final void checkMigrationToQs() {
        this.controlsComponent.getControlsController().ifPresent(new DeviceControlsControllerImpl$$ExternalSyntheticLambda3(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: checkMigrationToQs$lambda-0  reason: not valid java name */
    public static final void m3237checkMigrationToQs$lambda0(DeviceControlsControllerImpl deviceControlsControllerImpl, ControlsController controlsController) {
        Intrinsics.checkNotNullParameter(deviceControlsControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(controlsController, "it");
        if (!controlsController.getFavorites().isEmpty()) {
            deviceControlsControllerImpl.position = 3;
            deviceControlsControllerImpl.fireControlsUpdate();
        }
    }

    public void setCallback(DeviceControlsController.Callback callback2) {
        Intrinsics.checkNotNullParameter(callback2, "callback");
        removeCallback();
        this.callback = callback2;
        if (this.secureSettings.getInt("controls_enabled", 1) == 0) {
            fireControlsUpdate();
            return;
        }
        checkMigrationToQs();
        this.controlsComponent.getControlsListingController().ifPresent(new DeviceControlsControllerImpl$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: setCallback$lambda-1  reason: not valid java name */
    public static final void m3241setCallback$lambda1(DeviceControlsControllerImpl deviceControlsControllerImpl, ControlsListingController controlsListingController) {
        Intrinsics.checkNotNullParameter(deviceControlsControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(controlsListingController, "it");
        controlsListingController.addCallback(deviceControlsControllerImpl.listingCallback);
    }

    public void removeCallback() {
        this.position = null;
        this.callback = null;
        this.controlsComponent.getControlsListingController().ifPresent(new DeviceControlsControllerImpl$$ExternalSyntheticLambda4(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: removeCallback$lambda-2  reason: not valid java name */
    public static final void m3238removeCallback$lambda2(DeviceControlsControllerImpl deviceControlsControllerImpl, ControlsListingController controlsListingController) {
        Intrinsics.checkNotNullParameter(deviceControlsControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(controlsListingController, "it");
        controlsListingController.removeCallback(deviceControlsControllerImpl.listingCallback);
    }

    private final void fireControlsUpdate() {
        Log.i(TAG, "Setting DeviceControlsTile position: " + this.position);
        DeviceControlsController.Callback callback2 = this.callback;
        if (callback2 != null) {
            callback2.onControlsUpdate(this.position);
        }
    }

    /* access modifiers changed from: private */
    public final void seedFavorites(List<ControlsServiceInfo> list) {
        String[] stringArray = this.context.getResources().getStringArray(C1894R.array.config_controlsPreferredPackages);
        SharedPreferences sharedPreferences = this.userContextProvider.getUserContext().getSharedPreferences(PREFS_CONTROLS_FILE, 0);
        Set<String> stringSet = sharedPreferences.getStringSet(PREFS_CONTROLS_SEEDING_COMPLETED, SetsKt.emptySet());
        ControlsController controlsController = this.controlsComponent.getControlsController().get();
        Intrinsics.checkNotNullExpressionValue(controlsController, "controlsComponent.getControlsController().get()");
        ControlsController controlsController2 = controlsController;
        List arrayList = new ArrayList();
        for (int i = 0; i < Math.min(2, stringArray.length); i++) {
            String str = stringArray[i];
            for (ControlsServiceInfo controlsServiceInfo : list) {
                if (str.equals(controlsServiceInfo.componentName.getPackageName()) && !stringSet.contains(str)) {
                    ComponentName componentName = controlsServiceInfo.componentName;
                    Intrinsics.checkNotNullExpressionValue(componentName, "it.componentName");
                    if (controlsController2.countFavoritesForComponent(componentName) > 0) {
                        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "prefs");
                        Intrinsics.checkNotNullExpressionValue(str, SliceProviderCompat.EXTRA_PKG);
                        addPackageToSeededSet(sharedPreferences, str);
                    } else {
                        ComponentName componentName2 = controlsServiceInfo.componentName;
                        Intrinsics.checkNotNullExpressionValue(componentName2, "it.componentName");
                        arrayList.add(componentName2);
                    }
                }
            }
        }
        if (!arrayList.isEmpty()) {
            controlsController2.seedFavoritesForComponents(arrayList, new DeviceControlsControllerImpl$$ExternalSyntheticLambda1(this, sharedPreferences));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: seedFavorites$lambda-5  reason: not valid java name */
    public static final void m3239seedFavorites$lambda5(DeviceControlsControllerImpl deviceControlsControllerImpl, SharedPreferences sharedPreferences, SeedResponse seedResponse) {
        Intrinsics.checkNotNullParameter(deviceControlsControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(seedResponse, "response");
        Log.d(TAG, "Controls seeded: " + seedResponse);
        if (seedResponse.getAccepted()) {
            Intrinsics.checkNotNullExpressionValue(sharedPreferences, "prefs");
            deviceControlsControllerImpl.addPackageToSeededSet(sharedPreferences, seedResponse.getPackageName());
            if (deviceControlsControllerImpl.position == null) {
                deviceControlsControllerImpl.position = 7;
            }
            deviceControlsControllerImpl.fireControlsUpdate();
            deviceControlsControllerImpl.controlsComponent.getControlsListingController().ifPresent(new DeviceControlsControllerImpl$$ExternalSyntheticLambda0(deviceControlsControllerImpl));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: seedFavorites$lambda-5$lambda-4  reason: not valid java name */
    public static final void m3240seedFavorites$lambda5$lambda4(DeviceControlsControllerImpl deviceControlsControllerImpl, ControlsListingController controlsListingController) {
        Intrinsics.checkNotNullParameter(deviceControlsControllerImpl, "this$0");
        Intrinsics.checkNotNullParameter(controlsListingController, "it");
        controlsListingController.removeCallback(deviceControlsControllerImpl.listingCallback);
    }

    private final void addPackageToSeededSet(SharedPreferences sharedPreferences, String str) {
        Set<String> stringSet = sharedPreferences.getStringSet(PREFS_CONTROLS_SEEDING_COMPLETED, SetsKt.emptySet());
        Intrinsics.checkNotNullExpressionValue(stringSet, "seededPackages");
        Set mutableSet = CollectionsKt.toMutableSet(stringSet);
        mutableSet.add(str);
        sharedPreferences.edit().putStringSet(PREFS_CONTROLS_SEEDING_COMPLETED, mutableSet).apply();
    }
}
