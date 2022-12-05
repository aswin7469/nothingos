package com.android.systemui.controls.dagger;

import android.content.Context;
import android.database.ContentObserver;
import android.provider.Settings;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.controls.ui.ControlsUiController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.settings.SecureSettings;
import dagger.Lazy;
import java.util.Optional;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ControlsComponent.kt */
/* loaded from: classes.dex */
public final class ControlsComponent {
    private boolean canShowWhileLockedSetting;
    @NotNull
    private final Context context;
    private final boolean featureEnabled;
    @NotNull
    private final KeyguardStateController keyguardStateController;
    @NotNull
    private final Lazy<ControlsController> lazyControlsController;
    @NotNull
    private final Lazy<ControlsListingController> lazyControlsListingController;
    @NotNull
    private final Lazy<ControlsUiController> lazyControlsUiController;
    @NotNull
    private final LockPatternUtils lockPatternUtils;
    @NotNull
    private final SecureSettings secureSettings;
    @NotNull
    private final ContentObserver showWhileLockedObserver;
    @NotNull
    private final UserTracker userTracker;

    /* compiled from: ControlsComponent.kt */
    /* loaded from: classes.dex */
    public enum Visibility {
        AVAILABLE,
        AVAILABLE_AFTER_UNLOCK,
        UNAVAILABLE;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Visibility[] valuesCustom() {
            Visibility[] valuesCustom = values();
            Visibility[] visibilityArr = new Visibility[valuesCustom.length];
            System.arraycopy(valuesCustom, 0, visibilityArr, 0, valuesCustom.length);
            return visibilityArr;
        }
    }

    public ControlsComponent(boolean z, @NotNull Context context, @NotNull Lazy<ControlsController> lazyControlsController, @NotNull Lazy<ControlsUiController> lazyControlsUiController, @NotNull Lazy<ControlsListingController> lazyControlsListingController, @NotNull LockPatternUtils lockPatternUtils, @NotNull KeyguardStateController keyguardStateController, @NotNull UserTracker userTracker, @NotNull SecureSettings secureSettings) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(lazyControlsController, "lazyControlsController");
        Intrinsics.checkNotNullParameter(lazyControlsUiController, "lazyControlsUiController");
        Intrinsics.checkNotNullParameter(lazyControlsListingController, "lazyControlsListingController");
        Intrinsics.checkNotNullParameter(lockPatternUtils, "lockPatternUtils");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        Intrinsics.checkNotNullParameter(secureSettings, "secureSettings");
        this.featureEnabled = z;
        this.context = context;
        this.lazyControlsController = lazyControlsController;
        this.lazyControlsUiController = lazyControlsUiController;
        this.lazyControlsListingController = lazyControlsListingController;
        this.lockPatternUtils = lockPatternUtils;
        this.keyguardStateController = keyguardStateController;
        this.userTracker = userTracker;
        this.secureSettings = secureSettings;
        ContentObserver contentObserver = new ContentObserver() { // from class: com.android.systemui.controls.dagger.ControlsComponent$showWhileLockedObserver$1
            /* JADX INFO: Access modifiers changed from: package-private */
            {
                super(null);
            }

            @Override // android.database.ContentObserver
            public void onChange(boolean z2) {
                ControlsComponent.this.updateShowWhileLocked();
            }
        };
        this.showWhileLockedObserver = contentObserver;
        if (z) {
            secureSettings.registerContentObserver(Settings.Secure.getUriFor("lockscreen_show_controls"), false, contentObserver);
            updateShowWhileLocked();
        }
    }

    @NotNull
    public final Optional<ControlsController> getControlsController() {
        Optional<ControlsController> empty;
        String str;
        if (this.featureEnabled) {
            empty = Optional.of(this.lazyControlsController.get());
            str = "of(lazyControlsController.get())";
        } else {
            empty = Optional.empty();
            str = "empty()";
        }
        Intrinsics.checkNotNullExpressionValue(empty, str);
        return empty;
    }

    @NotNull
    public final Optional<ControlsListingController> getControlsListingController() {
        if (this.featureEnabled) {
            Optional<ControlsListingController> of = Optional.of(this.lazyControlsListingController.get());
            Intrinsics.checkNotNullExpressionValue(of, "{\n            Optional.of(lazyControlsListingController.get())\n        }");
            return of;
        }
        Optional<ControlsListingController> empty = Optional.empty();
        Intrinsics.checkNotNullExpressionValue(empty, "{\n            Optional.empty()\n        }");
        return empty;
    }

    public final boolean isEnabled() {
        return this.featureEnabled;
    }

    @NotNull
    public final Visibility getVisibility() {
        if (!isEnabled()) {
            return Visibility.UNAVAILABLE;
        }
        if (this.lockPatternUtils.getStrongAuthForUser(this.userTracker.getUserHandle().getIdentifier()) == 1) {
            return Visibility.AVAILABLE_AFTER_UNLOCK;
        }
        if (!this.canShowWhileLockedSetting && !this.keyguardStateController.isUnlocked()) {
            return Visibility.AVAILABLE_AFTER_UNLOCK;
        }
        return Visibility.AVAILABLE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateShowWhileLocked() {
        boolean z = false;
        if (this.secureSettings.getInt("lockscreen_show_controls", 0) != 0) {
            z = true;
        }
        this.canShowWhileLockedSetting = z;
    }
}
