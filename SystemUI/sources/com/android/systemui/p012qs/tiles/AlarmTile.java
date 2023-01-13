package com.android.systemui.p012qs.tiles;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import androidx.lifecycle.LifecycleOwner;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.C1894R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.p012qs.QSHost;
import com.android.systemui.p012qs.logging.QSLogger;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.NextAlarmController;
import java.util.Locale;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B[\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0001\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016¢\u0006\u0002\u0010\u0017J\u0010\u0010%\u001a\u00020&2\u0006\u0010'\u001a\u00020$H\u0002J\n\u0010(\u001a\u0004\u0018\u00010\u001bH\u0016J\b\u0010)\u001a\u00020*H\u0016J\b\u0010+\u001a\u00020,H\u0016J\u0012\u0010-\u001a\u00020.2\b\u0010/\u001a\u0004\u0018\u000100H\u0014J\u001a\u00101\u001a\u00020.2\u0006\u00102\u001a\u00020\u00022\b\u00103\u001a\u0004\u0018\u000104H\u0014J\b\u00105\u001a\u00020\u0002H\u0016J\b\u00106\u001a\u000207H\u0002R\u000e\u0010\u0018\u001a\u00020\u0019X\u0004¢\u0006\u0002\n\u0000R\u001c\u0010\u001a\u001a\u00020\u001b8\u0000X\u0004¢\u0006\u000e\n\u0000\u0012\u0004\b\u001c\u0010\u001d\u001a\u0004\b\u001e\u0010\u001fR\u0016\u0010 \u001a\n \"*\u0004\u0018\u00010!0!X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010#\u001a\u0004\u0018\u00010$X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000¨\u00068"}, mo65043d2 = {"Lcom/android/systemui/qs/tiles/AlarmTile;", "Lcom/android/systemui/qs/tileimpl/QSTileImpl;", "Lcom/android/systemui/plugins/qs/QSTile$State;", "host", "Lcom/android/systemui/qs/QSHost;", "backgroundLooper", "Landroid/os/Looper;", "mainHandler", "Landroid/os/Handler;", "falsingManager", "Lcom/android/systemui/plugins/FalsingManager;", "metricsLogger", "Lcom/android/internal/logging/MetricsLogger;", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "qsLogger", "Lcom/android/systemui/qs/logging/QSLogger;", "userTracker", "Lcom/android/systemui/settings/UserTracker;", "nextAlarmController", "Lcom/android/systemui/statusbar/policy/NextAlarmController;", "(Lcom/android/systemui/qs/QSHost;Landroid/os/Looper;Landroid/os/Handler;Lcom/android/systemui/plugins/FalsingManager;Lcom/android/internal/logging/MetricsLogger;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/qs/logging/QSLogger;Lcom/android/systemui/settings/UserTracker;Lcom/android/systemui/statusbar/policy/NextAlarmController;)V", "callback", "Lcom/android/systemui/statusbar/policy/NextAlarmController$NextAlarmChangeCallback;", "defaultIntent", "Landroid/content/Intent;", "getDefaultIntent$SystemUI_nothingRelease$annotations", "()V", "getDefaultIntent$SystemUI_nothingRelease", "()Landroid/content/Intent;", "icon", "Lcom/android/systemui/plugins/qs/QSTile$Icon;", "kotlin.jvm.PlatformType", "lastAlarmInfo", "Landroid/app/AlarmManager$AlarmClockInfo;", "formatNextAlarm", "", "info", "getLongClickIntent", "getMetricsCategory", "", "getTileLabel", "", "handleClick", "", "view", "Landroid/view/View;", "handleUpdateState", "state", "arg", "", "newTileState", "use24HourFormat", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.tiles.AlarmTile */
/* compiled from: AlarmTile.kt */
public final class AlarmTile extends QSTileImpl<QSTile.State> {
    private final NextAlarmController.NextAlarmChangeCallback callback;
    private final Intent defaultIntent = new Intent("android.intent.action.SHOW_ALARMS");
    private final QSTile.Icon icon = QSTileImpl.ResourceIcon.get(C1894R.C1896drawable.ic_alarm);
    private AlarmManager.AlarmClockInfo lastAlarmInfo;
    private final UserTracker userTracker;

    public static /* synthetic */ void getDefaultIntent$SystemUI_nothingRelease$annotations() {
    }

    public Intent getLongClickIntent() {
        return null;
    }

    public int getMetricsCategory() {
        return 0;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public AlarmTile(QSHost qSHost, @Background Looper looper, @Main Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, UserTracker userTracker2, NextAlarmController nextAlarmController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        Intrinsics.checkNotNullParameter(qSHost, "host");
        Intrinsics.checkNotNullParameter(looper, "backgroundLooper");
        Intrinsics.checkNotNullParameter(handler, "mainHandler");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(metricsLogger, "metricsLogger");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(qSLogger, "qsLogger");
        Intrinsics.checkNotNullParameter(userTracker2, "userTracker");
        Intrinsics.checkNotNullParameter(nextAlarmController, "nextAlarmController");
        this.userTracker = userTracker2;
        AlarmTile$$ExternalSyntheticLambda0 alarmTile$$ExternalSyntheticLambda0 = new AlarmTile$$ExternalSyntheticLambda0(this);
        this.callback = alarmTile$$ExternalSyntheticLambda0;
        nextAlarmController.observe((LifecycleOwner) this, alarmTile$$ExternalSyntheticLambda0);
    }

    public final Intent getDefaultIntent$SystemUI_nothingRelease() {
        return this.defaultIntent;
    }

    /* access modifiers changed from: private */
    /* renamed from: callback$lambda-0  reason: not valid java name */
    public static final void m2972callback$lambda0(AlarmTile alarmTile, AlarmManager.AlarmClockInfo alarmClockInfo) {
        Intrinsics.checkNotNullParameter(alarmTile, "this$0");
        alarmTile.lastAlarmInfo = alarmClockInfo;
        alarmTile.refreshState();
    }

    public QSTile.State newTileState() {
        QSTile.State state = new QSTile.State();
        state.handlesLongClick = false;
        return state;
    }

    /* access modifiers changed from: protected */
    public void handleClick(View view) {
        PendingIntent pendingIntent = null;
        ActivityLaunchAnimator.Controller fromView = view != null ? ActivityLaunchAnimator.Controller.Companion.fromView(view, 32) : null;
        AlarmManager.AlarmClockInfo alarmClockInfo = this.lastAlarmInfo;
        if (alarmClockInfo != null) {
            pendingIntent = alarmClockInfo.getShowIntent();
        }
        if (pendingIntent != null) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(pendingIntent, fromView);
        } else {
            this.mActivityStarter.postStartActivityDismissingKeyguard(this.defaultIntent, 0, fromView);
        }
    }

    /* access modifiers changed from: protected */
    public void handleUpdateState(QSTile.State state, Object obj) {
        Unit unit;
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        state.icon = this.icon;
        state.label = getTileLabel();
        AlarmManager.AlarmClockInfo alarmClockInfo = this.lastAlarmInfo;
        if (alarmClockInfo != null) {
            state.secondaryLabel = formatNextAlarm(alarmClockInfo);
            state.state = 2;
            unit = Unit.INSTANCE;
        } else {
            unit = null;
        }
        if (unit == null) {
            AlarmTile alarmTile = this;
            state.secondaryLabel = this.mContext.getString(C1894R.string.qs_alarm_tile_no_alarm);
            state.state = 1;
        }
        state.contentDescription = TextUtils.concat(new CharSequence[]{state.label, ", ", state.secondaryLabel});
    }

    public CharSequence getTileLabel() {
        String string = this.mContext.getString(C1894R.string.status_bar_alarm);
        Intrinsics.checkNotNullExpressionValue(string, "mContext.getString(R.string.status_bar_alarm)");
        return string;
    }

    private final String formatNextAlarm(AlarmManager.AlarmClockInfo alarmClockInfo) {
        return DateFormat.format(DateFormat.getBestDateTimePattern(Locale.getDefault(), use24HourFormat() ? "EHm" : "Ehma"), alarmClockInfo.getTriggerTime()).toString();
    }

    private final boolean use24HourFormat() {
        return DateFormat.is24HourFormat(this.mContext, this.userTracker.getUserId());
    }
}
