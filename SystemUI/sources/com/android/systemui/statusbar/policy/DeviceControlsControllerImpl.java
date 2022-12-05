package com.android.systemui.statusbar.policy;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.systemui.R$array;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.SeedResponse;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: DeviceControlsControllerImpl.kt */
/* loaded from: classes2.dex */
public final class DeviceControlsControllerImpl implements DeviceControlsController {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @Nullable
    private DeviceControlsController.Callback callback;
    @NotNull
    private final Context context;
    @NotNull
    private final ControlsComponent controlsComponent;
    @NotNull
    private final DeviceControlsControllerImpl$listingCallback$1 listingCallback = new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$listingCallback$1
        @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
        public void onServicesUpdated(@NotNull List<ControlsServiceInfo> serviceInfos) {
            Intrinsics.checkNotNullParameter(serviceInfos, "serviceInfos");
            if (!serviceInfos.isEmpty()) {
                DeviceControlsControllerImpl.this.seedFavorites(serviceInfos);
            }
        }
    };
    @Nullable
    private Integer position;
    @NotNull
    private final SecureSettings secureSettings;
    @NotNull
    private final UserContextProvider userContextProvider;

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$listingCallback$1] */
    public DeviceControlsControllerImpl(@NotNull Context context, @NotNull ControlsComponent controlsComponent, @NotNull UserContextProvider userContextProvider, @NotNull SecureSettings secureSettings) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(controlsComponent, "controlsComponent");
        Intrinsics.checkNotNullParameter(userContextProvider, "userContextProvider");
        Intrinsics.checkNotNullParameter(secureSettings, "secureSettings");
        this.context = context;
        this.controlsComponent = controlsComponent;
        this.userContextProvider = userContextProvider;
        this.secureSettings = secureSettings;
    }

    @Nullable
    public final Integer getPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.position;
    }

    public final void setPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core(@Nullable Integer num) {
        this.position = num;
    }

    /* compiled from: DeviceControlsControllerImpl.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final void checkMigrationToQs() {
        this.controlsComponent.getControlsController().ifPresent(new Consumer<ControlsController>() { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$checkMigrationToQs$1
            @Override // java.util.function.Consumer
            public final void accept(@NotNull ControlsController it) {
                Intrinsics.checkNotNullParameter(it, "it");
                if (!it.getFavorites().isEmpty()) {
                    DeviceControlsControllerImpl.this.setPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core(3);
                    DeviceControlsControllerImpl.this.fireControlsUpdate();
                }
            }
        });
    }

    @Override // com.android.systemui.statusbar.policy.DeviceControlsController
    public void setCallback(@NotNull DeviceControlsController.Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        removeCallback();
        this.callback = callback;
        if (this.secureSettings.getInt("controls_enabled", 1) == 0) {
            fireControlsUpdate();
            return;
        }
        checkMigrationToQs();
        this.controlsComponent.getControlsListingController().ifPresent(new Consumer<ControlsListingController>() { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$setCallback$1
            @Override // java.util.function.Consumer
            public final void accept(@NotNull ControlsListingController it) {
                DeviceControlsControllerImpl$listingCallback$1 deviceControlsControllerImpl$listingCallback$1;
                Intrinsics.checkNotNullParameter(it, "it");
                deviceControlsControllerImpl$listingCallback$1 = DeviceControlsControllerImpl.this.listingCallback;
                it.addCallback(deviceControlsControllerImpl$listingCallback$1);
            }
        });
    }

    @Override // com.android.systemui.statusbar.policy.DeviceControlsController
    public void removeCallback() {
        this.position = null;
        this.callback = null;
        this.controlsComponent.getControlsListingController().ifPresent(new Consumer<ControlsListingController>() { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$removeCallback$1
            @Override // java.util.function.Consumer
            public final void accept(@NotNull ControlsListingController it) {
                DeviceControlsControllerImpl$listingCallback$1 deviceControlsControllerImpl$listingCallback$1;
                Intrinsics.checkNotNullParameter(it, "it");
                deviceControlsControllerImpl$listingCallback$1 = DeviceControlsControllerImpl.this.listingCallback;
                it.removeCallback(deviceControlsControllerImpl$listingCallback$1);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void fireControlsUpdate() {
        Log.i("DeviceControlsControllerImpl", Intrinsics.stringPlus("Setting DeviceControlsTile position: ", this.position));
        DeviceControlsController.Callback callback = this.callback;
        if (callback == null) {
            return;
        }
        callback.onControlsUpdate(this.position);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void seedFavorites(List<ControlsServiceInfo> list) {
        Set<String> emptySet;
        String[] stringArray = this.context.getResources().getStringArray(R$array.config_controlsPreferredPackages);
        final SharedPreferences prefs = this.userContextProvider.getUserContext().getSharedPreferences("controls_prefs", 0);
        emptySet = SetsKt__SetsKt.emptySet();
        Set<String> stringSet = prefs.getStringSet("SeedingCompleted", emptySet);
        ControlsController controlsController = this.controlsComponent.getControlsController().get();
        Intrinsics.checkNotNullExpressionValue(controlsController, "controlsComponent.getControlsController().get()");
        ControlsController controlsController2 = controlsController;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < Math.min(2, stringArray.length); i++) {
            String pkg = stringArray[i];
            for (ControlsServiceInfo controlsServiceInfo : list) {
                if (pkg.equals(controlsServiceInfo.componentName.getPackageName()) && !stringSet.contains(pkg)) {
                    ComponentName componentName = controlsServiceInfo.componentName;
                    Intrinsics.checkNotNullExpressionValue(componentName, "it.componentName");
                    if (controlsController2.countFavoritesForComponent(componentName) > 0) {
                        Intrinsics.checkNotNullExpressionValue(prefs, "prefs");
                        Intrinsics.checkNotNullExpressionValue(pkg, "pkg");
                        addPackageToSeededSet(prefs, pkg);
                    } else {
                        ComponentName componentName2 = controlsServiceInfo.componentName;
                        Intrinsics.checkNotNullExpressionValue(componentName2, "it.componentName");
                        arrayList.add(componentName2);
                    }
                }
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        controlsController2.seedFavoritesForComponents(arrayList, new Consumer<SeedResponse>() { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$seedFavorites$2
            @Override // java.util.function.Consumer
            public final void accept(@NotNull SeedResponse response) {
                ControlsComponent controlsComponent;
                Intrinsics.checkNotNullParameter(response, "response");
                Log.d("DeviceControlsControllerImpl", Intrinsics.stringPlus("Controls seeded: ", response));
                if (response.getAccepted()) {
                    DeviceControlsControllerImpl deviceControlsControllerImpl = DeviceControlsControllerImpl.this;
                    SharedPreferences prefs2 = prefs;
                    Intrinsics.checkNotNullExpressionValue(prefs2, "prefs");
                    deviceControlsControllerImpl.addPackageToSeededSet(prefs2, response.getPackageName());
                    if (DeviceControlsControllerImpl.this.getPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core() == null) {
                        DeviceControlsControllerImpl.this.setPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core(7);
                    }
                    DeviceControlsControllerImpl.this.fireControlsUpdate();
                    controlsComponent = DeviceControlsControllerImpl.this.controlsComponent;
                    Optional<ControlsListingController> controlsListingController = controlsComponent.getControlsListingController();
                    final DeviceControlsControllerImpl deviceControlsControllerImpl2 = DeviceControlsControllerImpl.this;
                    controlsListingController.ifPresent(new Consumer<ControlsListingController>() { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$seedFavorites$2.1
                        @Override // java.util.function.Consumer
                        public final void accept(@NotNull ControlsListingController it) {
                            DeviceControlsControllerImpl$listingCallback$1 deviceControlsControllerImpl$listingCallback$1;
                            Intrinsics.checkNotNullParameter(it, "it");
                            deviceControlsControllerImpl$listingCallback$1 = DeviceControlsControllerImpl.this.listingCallback;
                            it.removeCallback(deviceControlsControllerImpl$listingCallback$1);
                        }
                    });
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void addPackageToSeededSet(SharedPreferences sharedPreferences, String str) {
        Set<String> emptySet;
        Set<String> mutableSet;
        emptySet = SetsKt__SetsKt.emptySet();
        Set<String> seededPackages = sharedPreferences.getStringSet("SeedingCompleted", emptySet);
        Intrinsics.checkNotNullExpressionValue(seededPackages, "seededPackages");
        mutableSet = CollectionsKt___CollectionsKt.toMutableSet(seededPackages);
        mutableSet.add(str);
        sharedPreferences.edit().putStringSet("SeedingCompleted", mutableSet).apply();
    }
}
