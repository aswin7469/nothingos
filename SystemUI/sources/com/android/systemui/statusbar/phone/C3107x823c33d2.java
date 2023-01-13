package com.android.systemui.statusbar.phone;

import com.android.systemui.log.LogMessage;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n¢\u0006\u0002\b\u0003"}, mo65043d2 = {"<anonymous>", "", "Lcom/android/systemui/log/LogMessage;", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.statusbar.phone.StatusBarNotificationActivityStarterLogger$logNonClickableNotification$2 */
/* compiled from: StatusBarNotificationActivityStarterLogger.kt */
final class C3107x823c33d2 extends Lambda implements Function1<LogMessage, String> {
    public static final C3107x823c33d2 INSTANCE = new C3107x823c33d2();

    C3107x823c33d2() {
        super(1);
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return "onNotificationClicked called for non-clickable notification! " + logMessage.getStr1();
    }
}
