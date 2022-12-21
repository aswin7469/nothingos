package com.android.systemui.unfold.updates;

import com.android.systemui.unfold.updates.DeviceFoldStateProvider;
import java.util.function.Consumer;

/* renamed from: com.android.systemui.unfold.updates.DeviceFoldStateProvider$FoldStateListener$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C3241x9a6fedba implements Consumer {
    public final /* synthetic */ DeviceFoldStateProvider f$0;

    public /* synthetic */ C3241x9a6fedba(DeviceFoldStateProvider deviceFoldStateProvider) {
        this.f$0 = deviceFoldStateProvider;
    }

    public final void accept(Object obj) {
        DeviceFoldStateProvider.FoldStateListener.m3286_init_$lambda0(this.f$0, ((Boolean) obj).booleanValue());
    }
}
