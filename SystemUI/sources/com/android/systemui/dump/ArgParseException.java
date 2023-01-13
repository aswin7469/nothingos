package com.android.systemui.dump;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u00002\u00060\u0001j\u0002`\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005¨\u0006\u0006"}, mo65043d2 = {"Lcom/android/systemui/dump/ArgParseException;", "Ljava/lang/Exception;", "Lkotlin/Exception;", "message", "", "(Ljava/lang/String;)V", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: DumpHandler.kt */
public final class ArgParseException extends Exception {
    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ArgParseException(String str) {
        super(str);
        Intrinsics.checkNotNullParameter(str, "message");
    }
}
