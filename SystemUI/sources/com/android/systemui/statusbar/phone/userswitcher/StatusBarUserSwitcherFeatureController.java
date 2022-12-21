package com.android.systemui.statusbar.phone.userswitcher;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.FlagListenable;
import com.android.systemui.flags.Flags;
import com.android.systemui.flags.ResourceBooleanFlag;
import com.android.systemui.statusbar.policy.CallbackController;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0002H\u0016J\u0006\u0010\u000b\u001a\u00020\fJ\b\u0010\r\u001a\u00020\tH\u0002J\u0010\u0010\u000e\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0002H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00020\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000f"}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/userswitcher/StatusBarUserSwitcherFeatureController;", "Lcom/android/systemui/statusbar/policy/CallbackController;", "Lcom/android/systemui/statusbar/phone/userswitcher/OnUserSwitcherPreferenceChangeListener;", "flags", "Lcom/android/systemui/flags/FeatureFlags;", "(Lcom/android/systemui/flags/FeatureFlags;)V", "listeners", "", "addCallback", "", "listener", "isStatusBarUserSwitcherFeatureEnabled", "", "notifyListeners", "removeCallback", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: StatusBarUserSwitcherFeatureController.kt */
public final class StatusBarUserSwitcherFeatureController implements CallbackController<OnUserSwitcherPreferenceChangeListener> {
    private final FeatureFlags flags;
    private final List<OnUserSwitcherPreferenceChangeListener> listeners = new ArrayList();

    @Inject
    public StatusBarUserSwitcherFeatureController(FeatureFlags featureFlags) {
        Intrinsics.checkNotNullParameter(featureFlags, "flags");
        this.flags = featureFlags;
        ResourceBooleanFlag resourceBooleanFlag = Flags.STATUS_BAR_USER_SWITCHER;
        Intrinsics.checkNotNullExpressionValue(resourceBooleanFlag, "STATUS_BAR_USER_SWITCHER");
        featureFlags.addListener(resourceBooleanFlag, new StatusBarUserSwitcherFeatureController$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: _init_$lambda-0  reason: not valid java name */
    public static final void m3222_init_$lambda0(StatusBarUserSwitcherFeatureController statusBarUserSwitcherFeatureController, FlagListenable.FlagEvent flagEvent) {
        Intrinsics.checkNotNullParameter(statusBarUserSwitcherFeatureController, "this$0");
        Intrinsics.checkNotNullParameter(flagEvent, "it");
        flagEvent.requestNoRestart();
        statusBarUserSwitcherFeatureController.notifyListeners();
    }

    public final boolean isStatusBarUserSwitcherFeatureEnabled() {
        FeatureFlags featureFlags = this.flags;
        ResourceBooleanFlag resourceBooleanFlag = Flags.STATUS_BAR_USER_SWITCHER;
        Intrinsics.checkNotNullExpressionValue(resourceBooleanFlag, "STATUS_BAR_USER_SWITCHER");
        return featureFlags.isEnabled(resourceBooleanFlag);
    }

    public void addCallback(OnUserSwitcherPreferenceChangeListener onUserSwitcherPreferenceChangeListener) {
        Intrinsics.checkNotNullParameter(onUserSwitcherPreferenceChangeListener, "listener");
        if (!this.listeners.contains(onUserSwitcherPreferenceChangeListener)) {
            this.listeners.add(onUserSwitcherPreferenceChangeListener);
        }
    }

    public void removeCallback(OnUserSwitcherPreferenceChangeListener onUserSwitcherPreferenceChangeListener) {
        Intrinsics.checkNotNullParameter(onUserSwitcherPreferenceChangeListener, "listener");
        this.listeners.remove((Object) onUserSwitcherPreferenceChangeListener);
    }

    private final void notifyListeners() {
        FeatureFlags featureFlags = this.flags;
        ResourceBooleanFlag resourceBooleanFlag = Flags.STATUS_BAR_USER_SWITCHER;
        Intrinsics.checkNotNullExpressionValue(resourceBooleanFlag, "STATUS_BAR_USER_SWITCHER");
        boolean isEnabled = featureFlags.isEnabled(resourceBooleanFlag);
        for (OnUserSwitcherPreferenceChangeListener onUserSwitcherPreferenceChange : this.listeners) {
            onUserSwitcherPreferenceChange.onUserSwitcherPreferenceChange(isEnabled);
        }
    }
}
