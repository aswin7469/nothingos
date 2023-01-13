package com.android.systemui.statusbar.phone.shade.transition;

import android.content.Context;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.p011qs.C2304QS;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import javax.inject.Provider;

/* renamed from: com.android.systemui.statusbar.phone.shade.transition.SplitShadeOverScroller_Factory  reason: case insensitive filesystem */
public final class C4841SplitShadeOverScroller_Factory {
    private final Provider<ConfigurationController> configurationControllerProvider;
    private final Provider<Context> contextProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<ScrimController> scrimControllerProvider;

    public C4841SplitShadeOverScroller_Factory(Provider<ConfigurationController> provider, Provider<DumpManager> provider2, Provider<Context> provider3, Provider<ScrimController> provider4) {
        this.configurationControllerProvider = provider;
        this.dumpManagerProvider = provider2;
        this.contextProvider = provider3;
        this.scrimControllerProvider = provider4;
    }

    public SplitShadeOverScroller get(C2304QS qs, NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return newInstance(this.configurationControllerProvider.get(), this.dumpManagerProvider.get(), this.contextProvider.get(), this.scrimControllerProvider.get(), qs, notificationStackScrollLayoutController);
    }

    public static C4841SplitShadeOverScroller_Factory create(Provider<ConfigurationController> provider, Provider<DumpManager> provider2, Provider<Context> provider3, Provider<ScrimController> provider4) {
        return new C4841SplitShadeOverScroller_Factory(provider, provider2, provider3, provider4);
    }

    public static SplitShadeOverScroller newInstance(ConfigurationController configurationController, DumpManager dumpManager, Context context, ScrimController scrimController, C2304QS qs, NotificationStackScrollLayoutController notificationStackScrollLayoutController) {
        return new SplitShadeOverScroller(configurationController, dumpManager, context, scrimController, qs, notificationStackScrollLayoutController);
    }
}
