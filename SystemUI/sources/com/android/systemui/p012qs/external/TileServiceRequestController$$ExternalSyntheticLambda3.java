package com.android.systemui.p012qs.external;

import android.content.ComponentName;
import com.android.internal.logging.InstanceId;
import java.util.function.Consumer;

/* renamed from: com.android.systemui.qs.external.TileServiceRequestController$$ExternalSyntheticLambda3 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class TileServiceRequestController$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ TileServiceRequestController f$0;
    public final /* synthetic */ ComponentName f$1;
    public final /* synthetic */ String f$2;
    public final /* synthetic */ InstanceId f$3;
    public final /* synthetic */ Consumer f$4;

    public /* synthetic */ TileServiceRequestController$$ExternalSyntheticLambda3(TileServiceRequestController tileServiceRequestController, ComponentName componentName, String str, InstanceId instanceId, Consumer consumer) {
        this.f$0 = tileServiceRequestController;
        this.f$1 = componentName;
        this.f$2 = str;
        this.f$3 = instanceId;
        this.f$4 = consumer;
    }

    public final void accept(Object obj) {
        TileServiceRequestController.m2958requestTileAdd$lambda0(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, (Integer) obj);
    }
}
