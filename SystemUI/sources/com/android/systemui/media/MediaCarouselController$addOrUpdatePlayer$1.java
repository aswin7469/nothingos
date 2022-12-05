package com.android.systemui.media;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MediaCarouselController.kt */
/* loaded from: classes.dex */
public /* synthetic */ class MediaCarouselController$addOrUpdatePlayer$1 extends FunctionReferenceImpl implements Function0<Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public MediaCarouselController$addOrUpdatePlayer$1(MediaCarouselController mediaCarouselController) {
        super(0, mediaCarouselController, MediaCarouselController.class, "updateCarouselDimensions", "updateCarouselDimensions()V", 0);
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
        ((MediaCarouselController) this.receiver).updateCarouselDimensions();
    }
}
