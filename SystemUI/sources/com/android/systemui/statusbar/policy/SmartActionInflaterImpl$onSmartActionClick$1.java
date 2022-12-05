package com.android.systemui.statusbar.policy;

import android.app.Notification;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes2.dex */
public final class SmartActionInflaterImpl$onSmartActionClick$1 extends Lambda implements Function0<Unit> {
    final /* synthetic */ Notification.Action $action;
    final /* synthetic */ int $actionIndex;
    final /* synthetic */ NotificationEntry $entry;
    final /* synthetic */ SmartReplyView.SmartActions $smartActions;
    final /* synthetic */ SmartActionInflaterImpl this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SmartActionInflaterImpl$onSmartActionClick$1(SmartActionInflaterImpl smartActionInflaterImpl, NotificationEntry notificationEntry, int i, Notification.Action action, SmartReplyView.SmartActions smartActions) {
        super(0);
        this.this$0 = smartActionInflaterImpl;
        this.$entry = notificationEntry;
        this.$actionIndex = i;
        this.$action = action;
        this.$smartActions = smartActions;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Unit mo1951invoke() {
        mo1951invoke();
        return Unit.INSTANCE;
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public final void mo1951invoke() {
        SmartReplyController smartReplyController;
        smartReplyController = this.this$0.smartReplyController;
        smartReplyController.smartActionClicked(this.$entry, this.$actionIndex, this.$action, this.$smartActions.fromAssistant);
    }
}
