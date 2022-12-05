package com.android.systemui.media;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: MediaHierarchyManager.kt */
/* loaded from: classes.dex */
public final class MediaHierarchyManager$register$1 extends Lambda implements Function1<Boolean, Unit> {
    final /* synthetic */ MediaHost $mediaObject;
    final /* synthetic */ MediaHierarchyManager this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MediaHierarchyManager$register$1(MediaHost mediaHost, MediaHierarchyManager mediaHierarchyManager) {
        super(1);
        this.$mediaObject = mediaHost;
        this.this$0 = mediaHierarchyManager;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1949invoke(Boolean bool) {
        invoke(bool.booleanValue());
        return Unit.INSTANCE;
    }

    public final void invoke(boolean z) {
        this.this$0.updateDesiredLocation(true, this.$mediaObject.getLocation() == 1);
    }
}
