package com.android.systemui.media;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo65043d2 = {"<anonymous>", "", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaHost.kt */
final class MediaHost$init$3 extends Lambda implements Function0<Unit> {
    final /* synthetic */ int $location;
    final /* synthetic */ MediaHost this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    MediaHost$init$3(MediaHost mediaHost, int i) {
        super(0);
        this.this$0 = mediaHost;
        this.$location = i;
    }

    public final void invoke() {
        this.this$0.mediaHostStatesManager.updateHostState(this.$location, this.this$0.state);
    }
}
