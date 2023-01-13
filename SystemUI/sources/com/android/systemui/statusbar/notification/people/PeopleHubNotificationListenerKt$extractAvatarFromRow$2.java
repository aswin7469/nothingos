package com.android.systemui.statusbar.notification.people;

import android.view.View;
import android.view.ViewGroup;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.Sequence;

@Metadata(mo65042d1 = {"\u0000\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\n¢\u0006\u0002\b\u0005"}, mo65043d2 = {"<anonymous>", "Lkotlin/sequences/Sequence;", "Landroid/view/View;", "it", "Landroid/view/ViewGroup;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubNotificationListener.kt */
final class PeopleHubNotificationListenerKt$extractAvatarFromRow$2 extends Lambda implements Function1<ViewGroup, Sequence<? extends View>> {
    public static final PeopleHubNotificationListenerKt$extractAvatarFromRow$2 INSTANCE = new PeopleHubNotificationListenerKt$extractAvatarFromRow$2();

    PeopleHubNotificationListenerKt$extractAvatarFromRow$2() {
        super(1);
    }

    public final Sequence<View> invoke(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "it");
        return PeopleHubNotificationListenerKt.childrenWithId(viewGroup, 16909547);
    }
}
