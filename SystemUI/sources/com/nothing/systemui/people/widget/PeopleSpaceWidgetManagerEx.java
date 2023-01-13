package com.nothing.systemui.people.widget;

import android.os.Handler;
import android.service.notification.StatusBarNotification;
import com.android.systemui.people.PeopleSpaceUtils;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0016\u0018\u0000 \r2\u00020\u0001:\u0001\rB\u000f\b\u0017\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u001a\u0010\u0007\u001a\u00020\b2\b\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u000e"}, mo65043d2 = {"Lcom/nothing/systemui/people/widget/PeopleSpaceWidgetManagerEx;", "", "manager", "Lcom/android/systemui/people/widget/PeopleSpaceWidgetManager;", "(Lcom/android/systemui/people/widget/PeopleSpaceWidgetManager;)V", "getManager", "()Lcom/android/systemui/people/widget/PeopleSpaceWidgetManager;", "updateWidgetsWithNotificationChangedDelay", "", "sbn", "Landroid/service/notification/StatusBarNotification;", "notificationAction", "Lcom/android/systemui/people/PeopleSpaceUtils$NotificationAction;", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleSpaceWidgetManagerEx.kt */
public class PeopleSpaceWidgetManagerEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int DELAY_NOTIFICATION_CHANGE_WIDGET_WIDGET_DURATION = 500;
    private final PeopleSpaceWidgetManager manager;

    public final PeopleSpaceWidgetManager getManager() {
        return this.manager;
    }

    @Inject
    public PeopleSpaceWidgetManagerEx(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        Intrinsics.checkNotNullParameter(peopleSpaceWidgetManager, "manager");
        this.manager = peopleSpaceWidgetManager;
    }

    /* access modifiers changed from: private */
    /* renamed from: updateWidgetsWithNotificationChangedDelay$lambda-0  reason: not valid java name */
    public static final void m3507updateWidgetsWithNotificationChangedDelay$lambda0(PeopleSpaceWidgetManagerEx peopleSpaceWidgetManagerEx, StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction) {
        Intrinsics.checkNotNullParameter(peopleSpaceWidgetManagerEx, "this$0");
        peopleSpaceWidgetManagerEx.manager.updateWidgetsWithNotificationChanged(statusBarNotification, notificationAction);
    }

    public final void updateWidgetsWithNotificationChangedDelay(StatusBarNotification statusBarNotification, PeopleSpaceUtils.NotificationAction notificationAction) {
        Handler.getMain().postDelayed(new PeopleSpaceWidgetManagerEx$$ExternalSyntheticLambda0(this, statusBarNotification, notificationAction), 500);
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/nothing/systemui/people/widget/PeopleSpaceWidgetManagerEx$Companion;", "", "()V", "DELAY_NOTIFICATION_CHANGE_WIDGET_WIDGET_DURATION", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PeopleSpaceWidgetManagerEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
