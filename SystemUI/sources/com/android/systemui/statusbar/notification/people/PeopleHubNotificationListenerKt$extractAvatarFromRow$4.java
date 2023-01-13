package com.android.systemui.statusbar.notification.people;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.internal.widget.MessagingGroup;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.sequences.SequencesKt;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u0004\u0018\u00010\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "Landroid/graphics/drawable/Drawable;", "messagesView", "Landroid/view/ViewGroup;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubNotificationListener.kt */
final class PeopleHubNotificationListenerKt$extractAvatarFromRow$4 extends Lambda implements Function1<ViewGroup, Drawable> {
    public static final PeopleHubNotificationListenerKt$extractAvatarFromRow$4 INSTANCE = new PeopleHubNotificationListenerKt$extractAvatarFromRow$4();

    PeopleHubNotificationListenerKt$extractAvatarFromRow$4() {
        super(1);
    }

    public final Drawable invoke(ViewGroup viewGroup) {
        ImageView imageView;
        Intrinsics.checkNotNullParameter(viewGroup, "messagesView");
        MessagingGroup messagingGroup = (MessagingGroup) SequencesKt.lastOrNull(SequencesKt.mapNotNull(PeopleHubNotificationListenerKt.getChildren(viewGroup), C27371.INSTANCE));
        if (messagingGroup == null || (imageView = (ImageView) messagingGroup.findViewById(16909221)) == null) {
            return null;
        }
        return imageView.getDrawable();
    }
}
