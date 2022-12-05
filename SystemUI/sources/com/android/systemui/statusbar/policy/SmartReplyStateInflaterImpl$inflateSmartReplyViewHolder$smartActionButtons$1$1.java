package com.android.systemui.statusbar.policy;

import android.app.Notification;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes2.dex */
final class SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1 extends Lambda implements Function1<Notification.Action, Boolean> {
    public static final SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1 INSTANCE = new SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1();

    SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    /* renamed from: invoke */
    public /* bridge */ /* synthetic */ Boolean mo1949invoke(Notification.Action action) {
        return Boolean.valueOf(invoke2(action));
    }

    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final boolean invoke2(Notification.Action action) {
        return action.actionIntent != null;
    }
}
