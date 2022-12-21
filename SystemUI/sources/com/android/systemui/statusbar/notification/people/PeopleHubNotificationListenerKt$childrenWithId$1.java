package com.android.systemui.statusbar.notification.people;

import android.view.View;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0004\b\u0004\u0010\u0005"}, mo64987d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "invoke", "(Landroid/view/View;)Ljava/lang/Boolean;"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PeopleHubNotificationListener.kt */
final class PeopleHubNotificationListenerKt$childrenWithId$1 extends Lambda implements Function1<View, Boolean> {
    final /* synthetic */ int $id;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    PeopleHubNotificationListenerKt$childrenWithId$1(int i) {
        super(1);
        this.$id = i;
    }

    public final Boolean invoke(View view) {
        Intrinsics.checkNotNullParameter(view, "it");
        return Boolean.valueOf(view.getId() == this.$id);
    }
}