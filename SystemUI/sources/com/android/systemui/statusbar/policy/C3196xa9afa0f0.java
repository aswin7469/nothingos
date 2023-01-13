package com.android.systemui.statusbar.policy;

import android.app.Notification;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0004\b\u0005\u0010\u0006"}, mo65043d2 = {"<anonymous>", "", "it", "Landroid/app/Notification$Action;", "kotlin.jvm.PlatformType", "invoke", "(Landroid/app/Notification$Action;)Ljava/lang/Boolean;"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1 */
/* compiled from: SmartReplyStateInflater.kt */
final class C3196xa9afa0f0 extends Lambda implements Function1<Notification.Action, Boolean> {
    public static final C3196xa9afa0f0 INSTANCE = new C3196xa9afa0f0();

    C3196xa9afa0f0() {
        super(1);
    }

    public final Boolean invoke(Notification.Action action) {
        return Boolean.valueOf(action.actionIntent != null);
    }
}
