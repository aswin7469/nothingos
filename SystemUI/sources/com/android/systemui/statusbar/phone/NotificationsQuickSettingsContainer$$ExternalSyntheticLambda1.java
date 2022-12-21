package com.android.systemui.statusbar.phone;

import android.view.View;
import java.util.function.ToIntFunction;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class NotificationsQuickSettingsContainer$$ExternalSyntheticLambda1 implements ToIntFunction {
    public final /* synthetic */ NotificationsQuickSettingsContainer f$0;

    public /* synthetic */ NotificationsQuickSettingsContainer$$ExternalSyntheticLambda1(NotificationsQuickSettingsContainer notificationsQuickSettingsContainer) {
        this.f$0 = notificationsQuickSettingsContainer;
    }

    public final int applyAsInt(Object obj) {
        return this.f$0.indexOfChild((View) obj);
    }
}
