package com.android.systemui.controls.dagger;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.provider.Settings;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.ControlsTileResourceConfiguration;
import com.android.systemui.controls.controller.ControlsTileResourceConfigurationImpl;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.p010ui.ControlsUiController;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.Lazy;
import java.util.Optional;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001:\u0001.Bq\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007\u0012\f\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0007\u0012\f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0007\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016¢\u0006\u0002\u0010\u0018J\f\u0010#\u001a\b\u0012\u0004\u0012\u00020\b0\u0016J\f\u0010$\u001a\b\u0012\u0004\u0012\u00020\f0\u0016J\f\u0010%\u001a\b\u0012\u0004\u0012\u00020\n0\u0016J\u0006\u0010&\u001a\u00020'J\u0006\u0010(\u001a\u00020'J\u0006\u0010)\u001a\u00020*J\u0006\u0010+\u001a\u00020\u0003J\b\u0010,\u001a\u00020-H\u0002R\u000e\u0010\u0019\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u001a\u001a\u00020\u001b8BX\u0004¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u001dR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0007X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\b\u0012\u0004\u0012\u00020\n0\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u001f\u001a\u00020 ¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\"R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000¨\u0006/"}, mo65043d2 = {"Lcom/android/systemui/controls/dagger/ControlsComponent;", "", "featureEnabled", "", "context", "Landroid/content/Context;", "lazyControlsController", "Ldagger/Lazy;", "Lcom/android/systemui/controls/controller/ControlsController;", "lazyControlsUiController", "Lcom/android/systemui/controls/ui/ControlsUiController;", "lazyControlsListingController", "Lcom/android/systemui/controls/management/ControlsListingController;", "lockPatternUtils", "Lcom/android/internal/widget/LockPatternUtils;", "keyguardStateController", "Lcom/android/systemui/statusbar/policy/KeyguardStateController;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "secureSettings", "Lcom/android/systemui/util/settings/SecureSettings;", "optionalControlsTileResourceConfiguration", "Ljava/util/Optional;", "Lcom/android/systemui/controls/controller/ControlsTileResourceConfiguration;", "(ZLandroid/content/Context;Ldagger/Lazy;Ldagger/Lazy;Ldagger/Lazy;Lcom/android/internal/widget/LockPatternUtils;Lcom/android/systemui/statusbar/policy/KeyguardStateController;Lcom/android/systemui/settings/UserTracker;Lcom/android/systemui/util/settings/SecureSettings;Ljava/util/Optional;)V", "canShowWhileLockedSetting", "contentResolver", "Landroid/content/ContentResolver;", "getContentResolver", "()Landroid/content/ContentResolver;", "controlsTileResourceConfiguration", "showWhileLockedObserver", "Landroid/database/ContentObserver;", "getShowWhileLockedObserver", "()Landroid/database/ContentObserver;", "getControlsController", "getControlsListingController", "getControlsUiController", "getTileImageId", "", "getTileTitleId", "getVisibility", "Lcom/android/systemui/controls/dagger/ControlsComponent$Visibility;", "isEnabled", "updateShowWhileLocked", "", "Visibility", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsComponent.kt */
public final class ControlsComponent {
    private boolean canShowWhileLockedSetting;
    private final Context context;
    private final ControlsTileResourceConfiguration controlsTileResourceConfiguration;
    private final boolean featureEnabled;
    private final KeyguardStateController keyguardStateController;
    private final Lazy<ControlsController> lazyControlsController;
    private final Lazy<ControlsListingController> lazyControlsListingController;
    private final Lazy<ControlsUiController> lazyControlsUiController;
    private final LockPatternUtils lockPatternUtils;
    private final Optional<ControlsTileResourceConfiguration> optionalControlsTileResourceConfiguration;
    private final SecureSettings secureSettings;
    private final ContentObserver showWhileLockedObserver;
    private final UserTracker userTracker;

    @Metadata(mo65042d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005¨\u0006\u0006"}, mo65043d2 = {"Lcom/android/systemui/controls/dagger/ControlsComponent$Visibility;", "", "(Ljava/lang/String;I)V", "AVAILABLE", "AVAILABLE_AFTER_UNLOCK", "UNAVAILABLE", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ControlsComponent.kt */
    public enum Visibility {
        AVAILABLE,
        AVAILABLE_AFTER_UNLOCK,
        UNAVAILABLE
    }

    @Inject
    public ControlsComponent(@ControlsFeatureEnabled boolean z, Context context2, Lazy<ControlsController> lazy, Lazy<ControlsUiController> lazy2, Lazy<ControlsListingController> lazy3, LockPatternUtils lockPatternUtils2, KeyguardStateController keyguardStateController2, UserTracker userTracker2, SecureSettings secureSettings2, Optional<ControlsTileResourceConfiguration> optional) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(lazy, "lazyControlsController");
        Intrinsics.checkNotNullParameter(lazy2, "lazyControlsUiController");
        Intrinsics.checkNotNullParameter(lazy3, "lazyControlsListingController");
        Intrinsics.checkNotNullParameter(lockPatternUtils2, "lockPatternUtils");
        Intrinsics.checkNotNullParameter(keyguardStateController2, "keyguardStateController");
        Intrinsics.checkNotNullParameter(userTracker2, "userTracker");
        Intrinsics.checkNotNullParameter(secureSettings2, "secureSettings");
        Intrinsics.checkNotNullParameter(optional, "optionalControlsTileResourceConfiguration");
        this.featureEnabled = z;
        this.context = context2;
        this.lazyControlsController = lazy;
        this.lazyControlsUiController = lazy2;
        this.lazyControlsListingController = lazy3;
        this.lockPatternUtils = lockPatternUtils2;
        this.keyguardStateController = keyguardStateController2;
        this.userTracker = userTracker2;
        this.secureSettings = secureSettings2;
        this.optionalControlsTileResourceConfiguration = optional;
        ControlsTileResourceConfiguration orElse = optional.orElse(new ControlsTileResourceConfigurationImpl());
        Intrinsics.checkNotNullExpressionValue(orElse, "optionalControlsTileReso…igurationImpl()\n        )");
        this.controlsTileResourceConfiguration = orElse;
        ContentObserver controlsComponent$showWhileLockedObserver$1 = new ControlsComponent$showWhileLockedObserver$1(this);
        this.showWhileLockedObserver = controlsComponent$showWhileLockedObserver$1;
        if (z) {
            secureSettings2.registerContentObserverForUser(Settings.Secure.getUriFor("lockscreen_show_controls"), false, controlsComponent$showWhileLockedObserver$1, -1);
            updateShowWhileLocked();
        }
    }

    private final ContentResolver getContentResolver() {
        ContentResolver contentResolver = this.context.getContentResolver();
        Intrinsics.checkNotNullExpressionValue(contentResolver, "context.contentResolver");
        return contentResolver;
    }

    public final ContentObserver getShowWhileLockedObserver() {
        return this.showWhileLockedObserver;
    }

    public final Optional<ControlsController> getControlsController() {
        Optional<ControlsController> optional;
        String str;
        if (this.featureEnabled) {
            optional = Optional.m1751of(this.lazyControlsController.get());
            str = "of(lazyControlsController.get())";
        } else {
            optional = Optional.empty();
            str = "empty()";
        }
        Intrinsics.checkNotNullExpressionValue(optional, str);
        return optional;
    }

    public final Optional<ControlsUiController> getControlsUiController() {
        Optional<ControlsUiController> optional;
        String str;
        if (this.featureEnabled) {
            optional = Optional.m1751of(this.lazyControlsUiController.get());
            str = "of(lazyControlsUiController.get())";
        } else {
            optional = Optional.empty();
            str = "empty()";
        }
        Intrinsics.checkNotNullExpressionValue(optional, str);
        return optional;
    }

    public final Optional<ControlsListingController> getControlsListingController() {
        if (this.featureEnabled) {
            Optional<ControlsListingController> of = Optional.m1751of(this.lazyControlsListingController.get());
            Intrinsics.checkNotNullExpressionValue(of, "{\n            Optional.o…ntroller.get())\n        }");
            return of;
        }
        Optional<ControlsListingController> empty = Optional.empty();
        Intrinsics.checkNotNullExpressionValue(empty, "{\n            Optional.empty()\n        }");
        return empty;
    }

    public final boolean isEnabled() {
        return this.featureEnabled;
    }

    public final Visibility getVisibility() {
        if (!isEnabled()) {
            return Visibility.UNAVAILABLE;
        }
        if (this.lockPatternUtils.getStrongAuthForUser(this.userTracker.getUserHandle().getIdentifier()) == 1) {
            return Visibility.AVAILABLE_AFTER_UNLOCK;
        }
        if (this.canShowWhileLockedSetting || this.keyguardStateController.isUnlocked()) {
            return Visibility.AVAILABLE;
        }
        return Visibility.AVAILABLE_AFTER_UNLOCK;
    }

    /* access modifiers changed from: private */
    public final void updateShowWhileLocked() {
        boolean z = false;
        if (this.secureSettings.getIntForUser("lockscreen_show_controls", 0, -2) != 0) {
            z = true;
        }
        this.canShowWhileLockedSetting = z;
    }

    public final int getTileTitleId() {
        return this.controlsTileResourceConfiguration.getTileTitleId();
    }

    public final int getTileImageId() {
        return this.controlsTileResourceConfiguration.getTileImageId();
    }
}
