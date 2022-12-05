package com.android.systemui.statusbar.notification.collection;

import com.android.systemui.statusbar.NotificationMediaManager;
import dagger.Lazy;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: NotificationRankingManager.kt */
/* loaded from: classes.dex */
public final class NotificationRankingManager$mediaManager$2 extends Lambda implements Function0<NotificationMediaManager> {
    final /* synthetic */ NotificationRankingManager this$0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationRankingManager$mediaManager$2(NotificationRankingManager notificationRankingManager) {
        super(0);
        this.this$0 = notificationRankingManager;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final NotificationMediaManager mo1951invoke() {
        Lazy lazy;
        lazy = this.this$0.mediaManagerLazy;
        return (NotificationMediaManager) lazy.get();
    }
}
