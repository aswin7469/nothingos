package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.Pluggable;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ShadeListBuilder$$ExternalSyntheticLambda1 implements Pluggable.PluggableListener {
    public final /* synthetic */ ShadeListBuilder f$0;

    public /* synthetic */ ShadeListBuilder$$ExternalSyntheticLambda1(ShadeListBuilder shadeListBuilder) {
        this.f$0 = shadeListBuilder;
    }

    public final void onPluggableInvalidated(Object obj) {
        this.f$0.onNotifSectionInvalidated((NotifSectioner) obj);
    }
}
