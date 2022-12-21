package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class ExpandableNotificationRowDragController_Factory implements Factory<ExpandableNotificationRowDragController> {
    private final Provider<Context> contextProvider;
    private final Provider<HeadsUpManager> headsUpManagerProvider;
    private final Provider<ShadeController> shadeControllerProvider;

    public ExpandableNotificationRowDragController_Factory(Provider<Context> provider, Provider<HeadsUpManager> provider2, Provider<ShadeController> provider3) {
        this.contextProvider = provider;
        this.headsUpManagerProvider = provider2;
        this.shadeControllerProvider = provider3;
    }

    public ExpandableNotificationRowDragController get() {
        return newInstance(this.contextProvider.get(), this.headsUpManagerProvider.get(), this.shadeControllerProvider.get());
    }

    public static ExpandableNotificationRowDragController_Factory create(Provider<Context> provider, Provider<HeadsUpManager> provider2, Provider<ShadeController> provider3) {
        return new ExpandableNotificationRowDragController_Factory(provider, provider2, provider3);
    }

    public static ExpandableNotificationRowDragController newInstance(Context context, HeadsUpManager headsUpManager, ShadeController shadeController) {
        return new ExpandableNotificationRowDragController(context, headsUpManager, shadeController);
    }
}
