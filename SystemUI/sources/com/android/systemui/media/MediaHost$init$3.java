package com.android.systemui.media;

import com.android.systemui.media.MediaHost;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MediaHost.kt */
/* loaded from: classes.dex */
public final class MediaHost$init$3 extends Lambda implements Function0<Unit> {
    final /* synthetic */ int $location;
    final /* synthetic */ MediaHost this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MediaHost$init$3(MediaHost mediaHost, int i) {
        super(0);
        this.this$0 = mediaHost;
        this.$location = i;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1951invoke() {
        mo1951invoke();
        return Unit.INSTANCE;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final void mo1951invoke() {
        MediaHostStatesManager mediaHostStatesManager;
        MediaHost.MediaHostStateHolder mediaHostStateHolder;
        mediaHostStatesManager = this.this$0.mediaHostStatesManager;
        int i = this.$location;
        mediaHostStateHolder = this.this$0.state;
        mediaHostStatesManager.updateHostState(i, mediaHostStateHolder);
    }
}
