package com.android.systemui.controls.dagger;

import android.app.Activity;
import android.content.pm.PackageManager;
import com.android.systemui.controls.ControlsMetricsLogger;
import com.android.systemui.controls.ControlsMetricsLoggerImpl;
import com.android.systemui.controls.controller.ControlsBindingController;
import com.android.systemui.controls.controller.ControlsBindingControllerImpl;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.ControlsControllerImpl;
import com.android.systemui.controls.controller.ControlsFavoritePersistenceWrapper;
import com.android.systemui.controls.controller.ControlsTileResourceConfiguration;
import com.android.systemui.controls.management.ControlsEditingActivity;
import com.android.systemui.controls.management.ControlsFavoritingActivity;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.management.ControlsListingControllerImpl;
import com.android.systemui.controls.management.ControlsProviderSelectorActivity;
import com.android.systemui.controls.management.ControlsRequestDialog;
import com.android.systemui.controls.p010ui.ControlActionCoordinator;
import com.android.systemui.controls.p010ui.ControlActionCoordinatorImpl;
import com.android.systemui.controls.p010ui.ControlsActivity;
import com.android.systemui.controls.p010ui.ControlsUiController;
import com.android.systemui.controls.p010ui.ControlsUiControllerImpl;
import com.android.systemui.dagger.SysUISingleton;
import dagger.Binds;
import dagger.BindsOptionalOf;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b'\u0018\u0000 (2\u00020\u0001:\u0001(B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H'J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH'J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH'J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H'J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000f\u001a\u00020\u0013H'J\u0010\u0010\u0014\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0015H'J\u0010\u0010\u0016\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0017H'J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u000f\u001a\u00020\u001aH'J\u0010\u0010\u001b\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u001cH'J\u0010\u0010\u001d\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u001eH'J\b\u0010\u001f\u001a\u00020 H'J\u0010\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020$H'J\u0010\u0010%\u001a\u00020&2\u0006\u0010\u000f\u001a\u00020'H'¨\u0006)"}, mo64987d2 = {"Lcom/android/systemui/controls/dagger/ControlsModule;", "", "()V", "optionalPersistenceWrapper", "Lcom/android/systemui/controls/controller/ControlsFavoritePersistenceWrapper;", "provideControlActionCoordinator", "Lcom/android/systemui/controls/ui/ControlActionCoordinator;", "coordinator", "Lcom/android/systemui/controls/ui/ControlActionCoordinatorImpl;", "provideControlsActivity", "Landroid/app/Activity;", "activity", "Lcom/android/systemui/controls/ui/ControlsActivity;", "provideControlsBindingController", "Lcom/android/systemui/controls/controller/ControlsBindingController;", "controller", "Lcom/android/systemui/controls/controller/ControlsBindingControllerImpl;", "provideControlsController", "Lcom/android/systemui/controls/controller/ControlsController;", "Lcom/android/systemui/controls/controller/ControlsControllerImpl;", "provideControlsEditingActivity", "Lcom/android/systemui/controls/management/ControlsEditingActivity;", "provideControlsFavoritingActivity", "Lcom/android/systemui/controls/management/ControlsFavoritingActivity;", "provideControlsListingController", "Lcom/android/systemui/controls/management/ControlsListingController;", "Lcom/android/systemui/controls/management/ControlsListingControllerImpl;", "provideControlsProviderActivity", "Lcom/android/systemui/controls/management/ControlsProviderSelectorActivity;", "provideControlsRequestDialog", "Lcom/android/systemui/controls/management/ControlsRequestDialog;", "provideControlsTileResourceConfiguration", "Lcom/android/systemui/controls/controller/ControlsTileResourceConfiguration;", "provideMetricsLogger", "Lcom/android/systemui/controls/ControlsMetricsLogger;", "logger", "Lcom/android/systemui/controls/ControlsMetricsLoggerImpl;", "provideUiController", "Lcom/android/systemui/controls/ui/ControlsUiController;", "Lcom/android/systemui/controls/ui/ControlsUiControllerImpl;", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@Module
/* compiled from: ControlsModule.kt */
public abstract class ControlsModule {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    @SysUISingleton
    @JvmStatic
    @ControlsFeatureEnabled
    @Provides
    public static final boolean providesControlsFeatureEnabled(PackageManager packageManager) {
        return Companion.providesControlsFeatureEnabled(packageManager);
    }

    @BindsOptionalOf
    public abstract ControlsFavoritePersistenceWrapper optionalPersistenceWrapper();

    @Binds
    public abstract ControlActionCoordinator provideControlActionCoordinator(ControlActionCoordinatorImpl controlActionCoordinatorImpl);

    @IntoMap
    @ClassKey(ControlsActivity.class)
    @Binds
    public abstract Activity provideControlsActivity(ControlsActivity controlsActivity);

    @Binds
    public abstract ControlsBindingController provideControlsBindingController(ControlsBindingControllerImpl controlsBindingControllerImpl);

    @Binds
    public abstract ControlsController provideControlsController(ControlsControllerImpl controlsControllerImpl);

    @IntoMap
    @ClassKey(ControlsEditingActivity.class)
    @Binds
    public abstract Activity provideControlsEditingActivity(ControlsEditingActivity controlsEditingActivity);

    @IntoMap
    @ClassKey(ControlsFavoritingActivity.class)
    @Binds
    public abstract Activity provideControlsFavoritingActivity(ControlsFavoritingActivity controlsFavoritingActivity);

    @Binds
    public abstract ControlsListingController provideControlsListingController(ControlsListingControllerImpl controlsListingControllerImpl);

    @IntoMap
    @ClassKey(ControlsProviderSelectorActivity.class)
    @Binds
    public abstract Activity provideControlsProviderActivity(ControlsProviderSelectorActivity controlsProviderSelectorActivity);

    @IntoMap
    @ClassKey(ControlsRequestDialog.class)
    @Binds
    public abstract Activity provideControlsRequestDialog(ControlsRequestDialog controlsRequestDialog);

    @BindsOptionalOf
    public abstract ControlsTileResourceConfiguration provideControlsTileResourceConfiguration();

    @Binds
    public abstract ControlsMetricsLogger provideMetricsLogger(ControlsMetricsLoggerImpl controlsMetricsLoggerImpl);

    @Binds
    public abstract ControlsUiController provideUiController(ControlsUiControllerImpl controlsUiControllerImpl);

    @Module
    @Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/controls/dagger/ControlsModule$Companion;", "", "()V", "providesControlsFeatureEnabled", "", "pm", "Landroid/content/pm/PackageManager;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ControlsModule.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @SysUISingleton
        @JvmStatic
        @ControlsFeatureEnabled
        @Provides
        public final boolean providesControlsFeatureEnabled(PackageManager packageManager) {
            Intrinsics.checkNotNullParameter(packageManager, "pm");
            return packageManager.hasSystemFeature("android.software.controls");
        }
    }
}
