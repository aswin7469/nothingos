package com.android.systemui.p012qs.tiles.dialog;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.notification.ZenModeDialogMetricsLogger;
import com.android.systemui.p012qs.QSDndEvent;
import com.android.systemui.p012qs.QSEvents;

/* renamed from: com.android.systemui.qs.tiles.dialog.QSZenModeDialogMetricsLogger */
public class QSZenModeDialogMetricsLogger extends ZenModeDialogMetricsLogger {
    private final UiEventLogger mUiEventLogger = QSEvents.INSTANCE.getQsUiEventsLogger();

    public QSZenModeDialogMetricsLogger(Context context) {
        super(context);
    }

    public void logOnEnableZenModeForever() {
        super.logOnEnableZenModeForever();
        this.mUiEventLogger.log(QSDndEvent.QS_DND_DIALOG_ENABLE_FOREVER);
    }

    public void logOnEnableZenModeUntilAlarm() {
        super.logOnEnableZenModeUntilAlarm();
        this.mUiEventLogger.log(QSDndEvent.QS_DND_DIALOG_ENABLE_UNTIL_ALARM);
    }

    public void logOnEnableZenModeUntilCountdown() {
        super.logOnEnableZenModeUntilCountdown();
        this.mUiEventLogger.log(QSDndEvent.QS_DND_DIALOG_ENABLE_UNTIL_COUNTDOWN);
    }

    public void logOnConditionSelected() {
        super.logOnConditionSelected();
        this.mUiEventLogger.log(QSDndEvent.QS_DND_CONDITION_SELECT);
    }

    public void logOnClickTimeButton(boolean z) {
        super.logOnClickTimeButton(z);
        this.mUiEventLogger.log(z ? QSDndEvent.QS_DND_TIME_UP : QSDndEvent.QS_DND_TIME_DOWN);
    }
}
