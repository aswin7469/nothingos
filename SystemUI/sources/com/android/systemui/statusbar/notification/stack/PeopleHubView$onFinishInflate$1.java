package com.android.systemui.statusbar.notification.stack;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.systemui.statusbar.notification.stack.PeopleHubView;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\b\u0018\u00010\u0001R\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\n¢\u0006\u0002\b\u0005"}, mo64987d2 = {"<anonymous>", "Lcom/android/systemui/statusbar/notification/stack/PeopleHubView$PersonDataListenerImpl;", "Lcom/android/systemui/statusbar/notification/stack/PeopleHubView;", "idx", "", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PeopleHubView.kt */
final class PeopleHubView$onFinishInflate$1 extends Lambda implements Function1<Integer, PeopleHubView.PersonDataListenerImpl> {
    final /* synthetic */ PeopleHubView this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    PeopleHubView$onFinishInflate$1(PeopleHubView peopleHubView) {
        super(1);
        this.this$0 = peopleHubView;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return invoke(((Number) obj).intValue());
    }

    public final PeopleHubView.PersonDataListenerImpl invoke(int i) {
        ViewGroup access$getContents$p = this.this$0.contents;
        if (access$getContents$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("contents");
            access$getContents$p = null;
        }
        View childAt = access$getContents$p.getChildAt(i);
        ImageView imageView = childAt instanceof ImageView ? (ImageView) childAt : null;
        if (imageView != null) {
            return new PeopleHubView.PersonDataListenerImpl(this.this$0, imageView);
        }
        return null;
    }
}
