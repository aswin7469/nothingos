package com.android.systemui.statusbar.phone.fragment;

import com.android.systemui.log.LogMessage;
import com.android.systemui.statusbar.DisableFlagsLogger;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\n¢\u0006\u0002\b\u0003"}, mo64987d2 = {"<anonymous>", "", "Lcom/android/systemui/log/LogMessage;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: CollapsedStatusBarFragmentLogger.kt */
final class CollapsedStatusBarFragmentLogger$logDisableFlagChange$2 extends Lambda implements Function1<LogMessage, String> {
    final /* synthetic */ CollapsedStatusBarFragmentLogger this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    CollapsedStatusBarFragmentLogger$logDisableFlagChange$2(CollapsedStatusBarFragmentLogger collapsedStatusBarFragmentLogger) {
        super(1);
        this.this$0 = collapsedStatusBarFragmentLogger;
    }

    public final String invoke(LogMessage logMessage) {
        Intrinsics.checkNotNullParameter(logMessage, "$this$log");
        return this.this$0.disableFlagsLogger.getDisableFlagsString((DisableFlagsLogger.DisableState) null, new DisableFlagsLogger.DisableState(logMessage.getInt1(), logMessage.getInt2()), new DisableFlagsLogger.DisableState((int) logMessage.getLong1(), (int) logMessage.getLong2()));
    }
}
