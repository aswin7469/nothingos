package com.android.systemui.media;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MediaCarouselController.kt */
/* loaded from: classes.dex */
public final class MediaCarouselController$reorderAllPlayers$4 extends Lambda implements Function0<Unit> {
    final /* synthetic */ int $activeMediaIndex;
    final /* synthetic */ MediaCarouselController this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MediaCarouselController$reorderAllPlayers$4(MediaCarouselController mediaCarouselController, int i) {
        super(0);
        this.this$0 = mediaCarouselController;
        this.$activeMediaIndex = i;
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
        MediaCarouselScrollHandler.scrollToPlayer$default(this.this$0.getMediaCarouselScrollHandler(), 0, this.$activeMediaIndex, 1, null);
    }
}
