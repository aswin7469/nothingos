package com.android.systemui.qs;

import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import org.jetbrains.annotations.NotNull;
/* compiled from: QSEvents.kt */
/* loaded from: classes.dex */
public final class QSEvents {
    @NotNull
    public static final QSEvents INSTANCE = new QSEvents();
    @NotNull
    private static UiEventLogger qsUiEventsLogger = new UiEventLoggerImpl();

    private QSEvents() {
    }

    @NotNull
    public final UiEventLogger getQsUiEventsLogger() {
        return qsUiEventsLogger;
    }
}
