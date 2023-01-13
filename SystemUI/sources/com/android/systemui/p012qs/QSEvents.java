package com.android.systemui.p012qs;

import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.logging.testing.UiEventLoggerFake;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0006\u0010\b\u001a\u00020\tJ\u0006\u0010\n\u001a\u00020\u000bR\u001e\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@BX\u000e¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\f"}, mo65043d2 = {"Lcom/android/systemui/qs/QSEvents;", "", "()V", "<set-?>", "Lcom/android/internal/logging/UiEventLogger;", "qsUiEventsLogger", "getQsUiEventsLogger", "()Lcom/android/internal/logging/UiEventLogger;", "resetLogger", "", "setLoggerForTesting", "Lcom/android/internal/logging/testing/UiEventLoggerFake;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.QSEvents */
/* compiled from: QSEvents.kt */
public final class QSEvents {
    public static final QSEvents INSTANCE = new QSEvents();
    private static UiEventLogger qsUiEventsLogger = new UiEventLoggerImpl();

    private QSEvents() {
    }

    public final UiEventLogger getQsUiEventsLogger() {
        return qsUiEventsLogger;
    }

    public final UiEventLoggerFake setLoggerForTesting() {
        UiEventLogger uiEventLoggerFake = new UiEventLoggerFake();
        qsUiEventsLogger = uiEventLoggerFake;
        return uiEventLoggerFake;
    }

    public final void resetLogger() {
        qsUiEventsLogger = new UiEventLoggerImpl();
    }
}
