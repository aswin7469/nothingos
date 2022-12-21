package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderListListener;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import java.util.List;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class DataStoreCoordinator$$ExternalSyntheticLambda0 implements OnAfterRenderListListener {
    public final /* synthetic */ DataStoreCoordinator f$0;

    public /* synthetic */ DataStoreCoordinator$$ExternalSyntheticLambda0(DataStoreCoordinator dataStoreCoordinator) {
        this.f$0 = dataStoreCoordinator;
    }

    public final void onAfterRenderList(List list, NotifStackController notifStackController) {
        DataStoreCoordinator.m3102attach$lambda0(this.f$0, list, notifStackController);
    }
}
