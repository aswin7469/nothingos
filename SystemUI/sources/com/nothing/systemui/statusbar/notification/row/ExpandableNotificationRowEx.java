package com.nothing.systemui.statusbar.notification.row;

import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\u0018\u0000 \f2\u00020\u0001:\u0001\fB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\b\u001a\u00020\tJ\u0006\u0010\n\u001a\u00020\tJ\u0006\u0010\u000b\u001a\u00020\tR\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/nothing/systemui/statusbar/notification/row/ExpandableNotificationRowEx;", "", "row", "Lcom/android/systemui/statusbar/notification/row/ExpandableNotificationRow;", "(Lcom/android/systemui/statusbar/notification/row/ExpandableNotificationRow;)V", "finishedInflated", "", "isDarkTheme", "onFinishInflate", "", "onUiModeChangedDelayCheck", "reInflateViews", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ExpandableNotificationRowEx.kt */
public final class ExpandableNotificationRowEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String TAG = "ExpandableNotificationRowEx";
    private boolean finishedInflated;
    private boolean isDarkTheme;
    private final ExpandableNotificationRow row;

    public ExpandableNotificationRowEx(ExpandableNotificationRow expandableNotificationRow) {
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "row");
        this.row = expandableNotificationRow;
    }

    public final void onFinishInflate() {
        this.finishedInflated = true;
        this.isDarkTheme = this.row.getContext().getResources().getConfiguration().isNightModeActive();
    }

    public final void reInflateViews() {
        this.isDarkTheme = this.row.getContext().getResources().getConfiguration().isNightModeActive();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0027, code lost:
        r1 = (r1 = r1.getEntry()).getSbn();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void onUiModeChangedDelayCheck() {
        /*
            r2 = this;
            boolean r0 = r2.isDarkTheme
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r1 = r2.row
            android.content.Context r1 = r1.getContext()
            android.content.res.Resources r1 = r1.getResources()
            android.content.res.Configuration r1 = r1.getConfiguration()
            boolean r1 = r1.isNightModeActive()
            if (r0 == r1) goto L_0x004b
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "Notification isDarkTheme not match, pkg = "
            r0.<init>((java.lang.String) r1)
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r1 = r2.row
            if (r1 == 0) goto L_0x0032
            com.android.systemui.statusbar.notification.collection.NotificationEntry r1 = r1.getEntry()
            if (r1 == 0) goto L_0x0032
            android.service.notification.StatusBarNotification r1 = r1.getSbn()
            if (r1 == 0) goto L_0x0032
            java.lang.String r1 = r1.getPackageName()
            goto L_0x0033
        L_0x0032:
            r1 = 0
        L_0x0033:
            java.lang.StringBuilder r0 = r0.append((java.lang.String) r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = "ExpandableNotificationRowEx"
            com.nothing.systemui.util.NTLogUtil.m1686d(r1, r0)
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = r2.row
            r1 = 1
            r0.setUpdateBackgroundOnUpdate(r1)
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r2 = r2.row
            r2.reInflateViews()
        L_0x004b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.statusbar.notification.row.ExpandableNotificationRowEx.onUiModeChangedDelayCheck():void");
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/nothing/systemui/statusbar/notification/row/ExpandableNotificationRowEx$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: ExpandableNotificationRowEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
