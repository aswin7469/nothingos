package com.android.systemui.media;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: KeyguardMediaController.kt */
/* loaded from: classes.dex */
public /* synthetic */ class KeyguardMediaController$attachSinglePaneContainer$1 extends FunctionReferenceImpl implements Function1<Boolean, Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public KeyguardMediaController$attachSinglePaneContainer$1(KeyguardMediaController keyguardMediaController) {
        super(1, keyguardMediaController, KeyguardMediaController.class, "onMediaHostVisibilityChanged", "onMediaHostVisibilityChanged(Z)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1949invoke(Boolean bool) {
        invoke(bool.booleanValue());
        return Unit.INSTANCE;
    }

    public final void invoke(boolean z) {
        ((KeyguardMediaController) this.receiver).onMediaHostVisibilityChanged(z);
    }
}
