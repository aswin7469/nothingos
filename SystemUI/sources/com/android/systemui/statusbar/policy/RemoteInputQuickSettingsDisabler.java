package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.LargeScreenUtils;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u00002\u00020\u0001B\u001f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000eJ\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\b\u0010\u0014\u001a\u00020\u0011H\u0002J\u000e\u0010\u0015\u001a\u00020\u00112\u0006\u0010\u0016\u001a\u00020\nR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000¨\u0006\u0017"}, mo64987d2 = {"Lcom/android/systemui/statusbar/policy/RemoteInputQuickSettingsDisabler;", "Lcom/android/systemui/statusbar/policy/ConfigurationController$ConfigurationListener;", "context", "Landroid/content/Context;", "commandQueue", "Lcom/android/systemui/statusbar/CommandQueue;", "configController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "(Landroid/content/Context;Lcom/android/systemui/statusbar/CommandQueue;Lcom/android/systemui/statusbar/policy/ConfigurationController;)V", "isLandscape", "", "remoteInputActive", "shouldUseSplitNotificationShade", "adjustDisableFlags", "", "state", "onConfigChanged", "", "newConfig", "Landroid/content/res/Configuration;", "recomputeDisableFlags", "setRemoteInputActive", "active", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RemoteInputQuickSettingsDisabler.kt */
public final class RemoteInputQuickSettingsDisabler implements ConfigurationController.ConfigurationListener {
    private final CommandQueue commandQueue;
    private final Context context;
    private boolean isLandscape;
    private boolean remoteInputActive;
    private boolean shouldUseSplitNotificationShade;

    @Inject
    public RemoteInputQuickSettingsDisabler(Context context2, CommandQueue commandQueue2, ConfigurationController configurationController) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(commandQueue2, "commandQueue");
        Intrinsics.checkNotNullParameter(configurationController, "configController");
        this.context = context2;
        this.commandQueue = commandQueue2;
        this.isLandscape = context2.getResources().getConfiguration().orientation == 2;
        Resources resources = context2.getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "context.resources");
        this.shouldUseSplitNotificationShade = LargeScreenUtils.shouldUseSplitNotificationShade(resources);
        configurationController.addCallback(this);
    }

    public final int adjustDisableFlags(int i) {
        return (!this.remoteInputActive || !this.isLandscape || this.shouldUseSplitNotificationShade) ? i : i | 1;
    }

    public final void setRemoteInputActive(boolean z) {
        if (this.remoteInputActive != z) {
            this.remoteInputActive = z;
            recomputeDisableFlags();
        }
    }

    public void onConfigChanged(Configuration configuration) {
        Intrinsics.checkNotNullParameter(configuration, "newConfig");
        boolean z = true;
        boolean z2 = false;
        boolean z3 = configuration.orientation == 2;
        if (z3 != this.isLandscape) {
            this.isLandscape = z3;
            z2 = true;
        }
        Resources resources = this.context.getResources();
        Intrinsics.checkNotNullExpressionValue(resources, "context.resources");
        boolean shouldUseSplitNotificationShade2 = LargeScreenUtils.shouldUseSplitNotificationShade(resources);
        if (shouldUseSplitNotificationShade2 != this.shouldUseSplitNotificationShade) {
            this.shouldUseSplitNotificationShade = shouldUseSplitNotificationShade2;
        } else {
            z = z2;
        }
        if (z) {
            recomputeDisableFlags();
        }
    }

    private final void recomputeDisableFlags() {
        this.commandQueue.recomputeDisableFlags(this.context.getDisplayId(), true);
    }
}
