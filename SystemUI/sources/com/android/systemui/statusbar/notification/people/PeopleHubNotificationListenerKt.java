package com.android.systemui.statusbar.notification.people;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@Metadata(mo65042d1 = {"\u00006\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0010\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000b\u001a\u001a\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003*\u00020\u00052\u0006\u0010\r\u001a\u00020\u0001H\u0002\u001a\u0014\u0010\u000e\u001a\u00020\u000f*\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012H\u0002\"\u000e\u0010\u0000\u001a\u00020\u0001XT¢\u0006\u0002\n\u0000\"\u001e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003*\u00020\u00058BX\u0004¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0013"}, mo65043d2 = {"MAX_STORED_INACTIVE_PEOPLE", "", "children", "Lkotlin/sequences/Sequence;", "Landroid/view/View;", "Landroid/view/ViewGroup;", "getChildren", "(Landroid/view/ViewGroup;)Lkotlin/sequences/Sequence;", "extractAvatarFromRow", "Landroid/graphics/drawable/Drawable;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "childrenWithId", "id", "registerListener", "Lcom/android/systemui/statusbar/notification/people/Subscription;", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;", "listener", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager$UserChangedListener;", "SystemUI_nothingRelease"}, mo65044k = 2, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubNotificationListener.kt */
public final class PeopleHubNotificationListenerKt {
    private static final int MAX_STORED_INACTIVE_PEOPLE = 10;

    /* access modifiers changed from: private */
    public static final Subscription registerListener(NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationLockscreenUserManager.UserChangedListener userChangedListener) {
        notificationLockscreenUserManager.addUserChangedListener(userChangedListener);
        return new PeopleHubNotificationListenerKt$registerListener$1(notificationLockscreenUserManager, userChangedListener);
    }

    /* access modifiers changed from: private */
    public static final Sequence<View> getChildren(ViewGroup viewGroup) {
        return SequencesKt.sequence(new PeopleHubNotificationListenerKt$children$1(viewGroup, (Continuation<? super PeopleHubNotificationListenerKt$children$1>) null));
    }

    /* access modifiers changed from: private */
    public static final Sequence<View> childrenWithId(ViewGroup viewGroup, int i) {
        return SequencesKt.filter(getChildren(viewGroup), new PeopleHubNotificationListenerKt$childrenWithId$1(i));
    }

    public static final Drawable extractAvatarFromRow(NotificationEntry notificationEntry) {
        Sequence<View> childrenWithId;
        Sequence<R> mapNotNull;
        Sequence<R> flatMap;
        Sequence<R> mapNotNull2;
        Sequence<R> mapNotNull3;
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        ExpandableNotificationRow row = notificationEntry.getRow();
        if (row == null || (childrenWithId = childrenWithId(row, C1894R.C1898id.expanded)) == null || (mapNotNull = SequencesKt.mapNotNull(childrenWithId, PeopleHubNotificationListenerKt$extractAvatarFromRow$1.INSTANCE)) == null || (flatMap = SequencesKt.flatMap(mapNotNull, PeopleHubNotificationListenerKt$extractAvatarFromRow$2.INSTANCE)) == null || (mapNotNull2 = SequencesKt.mapNotNull(flatMap, PeopleHubNotificationListenerKt$extractAvatarFromRow$3.INSTANCE)) == null || (mapNotNull3 = SequencesKt.mapNotNull(mapNotNull2, PeopleHubNotificationListenerKt$extractAvatarFromRow$4.INSTANCE)) == null) {
            return null;
        }
        return (Drawable) SequencesKt.firstOrNull(mapNotNull3);
    }
}
