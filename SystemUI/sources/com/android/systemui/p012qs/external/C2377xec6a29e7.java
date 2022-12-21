package com.android.systemui.p012qs.external;

import com.android.internal.statusbar.IAddTileResultCallback;
import java.util.function.Consumer;

/* renamed from: com.android.systemui.qs.external.TileServiceRequestController$commandQueueCallback$1$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2377xec6a29e7 implements Consumer {
    public final /* synthetic */ IAddTileResultCallback f$0;

    public /* synthetic */ C2377xec6a29e7(IAddTileResultCallback iAddTileResultCallback) {
        this.f$0 = iAddTileResultCallback;
    }

    public final void accept(Object obj) {
        TileServiceRequestController$commandQueueCallback$1.m2956requestAddTile$lambda0(this.f$0, (Integer) obj);
    }
}
