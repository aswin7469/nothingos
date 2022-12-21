package com.android.systemui.statusbar.notification.people;

import android.view.View;
import android.view.ViewGroup;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo64987d2 = {"<anonymous>", "Landroid/view/ViewGroup;", "it", "Landroid/view/View;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PeopleHubNotificationListener.kt */
final class PeopleHubNotificationListenerKt$extractAvatarFromRow$3 extends Lambda implements Function1<View, ViewGroup> {
    public static final PeopleHubNotificationListenerKt$extractAvatarFromRow$3 INSTANCE = new PeopleHubNotificationListenerKt$extractAvatarFromRow$3();

    PeopleHubNotificationListenerKt$extractAvatarFromRow$3() {
        super(1);
    }

    public final ViewGroup invoke(View view) {
        Intrinsics.checkNotNullParameter(view, "it");
        return (ViewGroup) view.findViewById(16909294);
    }
}
