package com.android.systemui.unfold;

import com.android.internal.util.FrameworkStatsLog;
import com.android.systemui.unfold.FoldStateLoggingProvider;
import java.net.HttpURLConnection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0006\u0010\u0005\u001a\u00020\u0006J\u0010\u0010\u0007\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\n"}, mo65043d2 = {"Lcom/android/systemui/unfold/FoldStateLogger;", "Lcom/android/systemui/unfold/FoldStateLoggingProvider$FoldStateLoggingListener;", "foldStateLoggingProvider", "Lcom/android/systemui/unfold/FoldStateLoggingProvider;", "(Lcom/android/systemui/unfold/FoldStateLoggingProvider;)V", "init", "", "onFoldUpdate", "foldStateUpdate", "Lcom/android/systemui/unfold/FoldStateChange;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: FoldStateLogger.kt */
public final class FoldStateLogger implements FoldStateLoggingProvider.FoldStateLoggingListener {
    private final FoldStateLoggingProvider foldStateLoggingProvider;

    public FoldStateLogger(FoldStateLoggingProvider foldStateLoggingProvider2) {
        Intrinsics.checkNotNullParameter(foldStateLoggingProvider2, "foldStateLoggingProvider");
        this.foldStateLoggingProvider = foldStateLoggingProvider2;
    }

    public final void init() {
        this.foldStateLoggingProvider.addCallback(this);
    }

    public void onFoldUpdate(FoldStateChange foldStateChange) {
        Intrinsics.checkNotNullParameter(foldStateChange, "foldStateUpdate");
        FrameworkStatsLog.write(HttpURLConnection.HTTP_REQ_TOO_LONG, foldStateChange.getPrevious(), foldStateChange.getCurrent(), foldStateChange.getDtMillis());
    }
}
