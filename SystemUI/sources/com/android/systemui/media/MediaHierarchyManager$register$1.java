package com.android.systemui.media;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "", "it", "", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaHierarchyManager.kt */
final class MediaHierarchyManager$register$1 extends Lambda implements Function1<Boolean, Unit> {
    final /* synthetic */ MediaHost $mediaObject;
    final /* synthetic */ MediaHierarchyManager this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    MediaHierarchyManager$register$1(MediaHost mediaHost, MediaHierarchyManager mediaHierarchyManager) {
        super(1);
        this.$mediaObject = mediaHost;
        this.this$0 = mediaHierarchyManager;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke(((Boolean) obj).booleanValue());
        return Unit.INSTANCE;
    }

    public final void invoke(boolean z) {
        this.this$0.updateDesiredLocation(true, this.$mediaObject.getLocation() == 1);
    }
}
