package com.android.systemui.log;

import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, mo64987d2 = {"<anonymous>", "Lcom/android/systemui/log/LogMessageImpl;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: LogBuffer.kt */
final class LogBuffer$buffer$1 extends Lambda implements Function0<LogMessageImpl> {
    public static final LogBuffer$buffer$1 INSTANCE = new LogBuffer$buffer$1();

    LogBuffer$buffer$1() {
        super(0);
    }

    public final LogMessageImpl invoke() {
        return LogMessageImpl.Factory.create();
    }
}
