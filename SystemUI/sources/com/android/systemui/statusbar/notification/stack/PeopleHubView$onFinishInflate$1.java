package com.android.systemui.statusbar.notification.stack;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.systemui.statusbar.notification.stack.PeopleHubView;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.jetbrains.annotations.Nullable;
/* compiled from: PeopleHubView.kt */
/* loaded from: classes.dex */
final class PeopleHubView$onFinishInflate$1 extends Lambda implements Function1<Integer, PeopleHubView.PersonDataListenerImpl> {
    final /* synthetic */ PeopleHubView this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PeopleHubView$onFinishInflate$1(PeopleHubView peopleHubView) {
        super(1);
        this.this$0 = peopleHubView;
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ PeopleHubView.PersonDataListenerImpl mo1949invoke(Integer num) {
        return invoke(num.intValue());
    }

    @Nullable
    public final PeopleHubView.PersonDataListenerImpl invoke(int i) {
        ViewGroup viewGroup;
        viewGroup = this.this$0.contents;
        if (viewGroup == null) {
            Intrinsics.throwUninitializedPropertyAccessException("contents");
            throw null;
        }
        View childAt = viewGroup.getChildAt(i);
        ImageView imageView = childAt instanceof ImageView ? (ImageView) childAt : null;
        if (imageView != null) {
            return new PeopleHubView.PersonDataListenerImpl(this.this$0, imageView);
        }
        return null;
    }
}
