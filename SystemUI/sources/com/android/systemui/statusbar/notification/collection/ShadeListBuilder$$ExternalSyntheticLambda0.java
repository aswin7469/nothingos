package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifStabilityManager;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ShadeListBuilder$$ExternalSyntheticLambda0 implements Pluggable.PluggableListener {
    public final /* synthetic */ ShadeListBuilder f$0;

    public /* synthetic */ ShadeListBuilder$$ExternalSyntheticLambda0(ShadeListBuilder shadeListBuilder) {
        this.f$0 = shadeListBuilder;
    }

    public final void onPluggableInvalidated(Object obj) {
        this.f$0.onReorderingAllowedInvalidated((NotifStabilityManager) obj);
    }
}