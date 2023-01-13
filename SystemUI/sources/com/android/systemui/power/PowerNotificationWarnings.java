package com.android.systemui.power;

import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.Annotation;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.Slog;
import android.view.View;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.Utils;
import com.android.settingslib.fuelgauge.BatterySaverUtils;
import com.android.settingslib.media.MediaOutputConstants;
import com.android.settingslib.utils.PowerUtil;
import com.android.systemui.C1894R;
import com.android.systemui.SystemUIApplication;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.power.BatteryWarningEvents;
import com.android.systemui.power.PowerUI;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.NotificationChannels;
import com.android.systemui.volume.Events;
import com.nothing.systemui.power.PowerNotificationWarningsEx;
import dagger.Lazy;
import java.lang.ref.WeakReference;
import java.p026io.PrintWriter;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;
import javax.inject.Inject;

@SysUISingleton
public class PowerNotificationWarnings implements PowerUI.WarningsUI {
    private static final String ACTION_AUTO_SAVER_NO_THANKS = "PNW.autoSaverNoThanks";
    private static final String ACTION_CLICKED_TEMP_WARNING = "PNW.clickedTempWarning";
    private static final String ACTION_CLICKED_THERMAL_SHUTDOWN_WARNING = "PNW.clickedThermalShutdownWarning";
    private static final String ACTION_DISMISSED_TEMP_WARNING = "PNW.dismissedTempWarning";
    private static final String ACTION_DISMISSED_THERMAL_SHUTDOWN_WARNING = "PNW.dismissedThermalShutdownWarning";
    private static final String ACTION_DISMISSED_WARNING = "PNW.dismissedWarning";
    private static final String ACTION_DISMISS_AUTO_SAVER_SUGGESTION = "PNW.dismissAutoSaverSuggestion";
    private static final String ACTION_ENABLE_AUTO_SAVER = "PNW.enableAutoSaver";
    private static final String ACTION_ENABLE_SEVERE_BATTERY_DIALOG = "PNW.enableSevereDialog";
    private static final String ACTION_SHOW_AUTO_SAVER_SUGGESTION = "PNW.autoSaverSuggestion";
    private static final String ACTION_SHOW_BATTERY_SAVER_SETTINGS = "PNW.batterySaverSettings";
    private static final String ACTION_SHOW_START_SAVER_CONFIRMATION = "PNW.startSaverConfirmation";
    private static final String ACTION_START_SAVER = "PNW.startSaver";
    private static final AudioAttributes AUDIO_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    private static final String BATTERY_SAVER_DESCRIPTION_URL_KEY = "url";
    public static final String BATTERY_SAVER_SCHEDULE_SCREEN_INTENT_ACTION = "com.android.settings.BATTERY_SAVER_SCHEDULE_SETTINGS";
    private static final boolean DEBUG = PowerUI.DEBUG;
    public static final String EXTRA_CONFIRM_ONLY = "extra_confirm_only";
    private static final String EXTRA_SCHEDULED_BY_PERCENTAGE = "extra_scheduled_by_percentage";
    private static final int SHOWING_AUTO_SAVER_SUGGESTION = 4;
    private static final int SHOWING_INVALID_CHARGER = 3;
    private static final int SHOWING_NOTHING = 0;
    private static final String[] SHOWING_STRINGS = {"SHOWING_NOTHING", "SHOWING_WARNING", "SHOWING_SAVER", "SHOWING_INVALID_CHARGER", "SHOWING_AUTO_SAVER_SUGGESTION"};
    private static final int SHOWING_WARNING = 1;
    private static final String TAG = "PowerUI.Notification";
    private static final String TAG_AUTO_SAVER = "auto_saver";
    private static final String TAG_BATTERY = "low_battery";
    private static final String TAG_TEMPERATURE = "high_temp";
    /* access modifiers changed from: private */
    public ActivityStarter mActivityStarter;
    private final Lazy<BatteryController> mBatteryControllerLazy;
    private int mBatteryLevel;
    /* access modifiers changed from: private */
    public final BroadcastSender mBroadcastSender;
    private int mBucket;
    /* access modifiers changed from: private */
    public final Context mContext;
    private BatteryStateSnapshot mCurrentBatterySnapshot;
    private final DialogLaunchAnimator mDialogLaunchAnimator;
    private final PowerNotificationWarningsEx mEx;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    /* access modifiers changed from: private */
    public SystemUIDialog mHighTempDialog;
    private boolean mHighTempWarning;
    private boolean mInvalidCharger;
    private final KeyguardManager mKeyguard;
    private final NotificationManager mNoMan;
    /* access modifiers changed from: private */
    public final Intent mOpenBatterySaverSettings = settings("android.settings.BATTERY_SAVER_SETTINGS");
    private final Intent mOpenBatterySettings = settings("android.intent.action.POWER_USAGE_SUMMARY");
    private boolean mPlaySound;
    private final PowerManager mPowerMan;
    private final Receiver mReceiver;
    /* access modifiers changed from: private */
    public SystemUIDialog mSaverConfirmation;
    private SystemUIDialog mSaverEnabledConfirmation;
    private long mScreenOffTime;
    private boolean mShowAutoSaverSuggestion;
    private int mShowing;
    /* access modifiers changed from: private */
    public SystemUIDialog mThermalShutdownDialog;
    private final UiEventLogger mUiEventLogger;
    SystemUIDialog mUsbHighTempDialog;
    private final boolean mUseSevereDialog;
    private boolean mWarning;
    private long mWarningTriggerTimeMs;

    @Inject
    public PowerNotificationWarnings(Context context, ActivityStarter activityStarter, BroadcastSender broadcastSender, Lazy<BatteryController> lazy, DialogLaunchAnimator dialogLaunchAnimator, UiEventLogger uiEventLogger) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mHandler = handler;
        Receiver receiver = new Receiver();
        this.mReceiver = receiver;
        this.mContext = context;
        this.mNoMan = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mPowerMan = (PowerManager) context.getSystemService("power");
        this.mKeyguard = (KeyguardManager) context.getSystemService(KeyguardManager.class);
        receiver.init();
        this.mActivityStarter = activityStarter;
        this.mBroadcastSender = broadcastSender;
        this.mBatteryControllerLazy = lazy;
        this.mDialogLaunchAnimator = dialogLaunchAnimator;
        this.mUseSevereDialog = context.getResources().getBoolean(C1894R.bool.config_severe_battery_dialog);
        this.mUiEventLogger = uiEventLogger;
        this.mEx = new PowerNotificationWarningsEx(context, handler);
    }

    public void dump(PrintWriter printWriter) {
        printWriter.print("mWarning=");
        printWriter.println(this.mWarning);
        printWriter.print("mPlaySound=");
        printWriter.println(this.mPlaySound);
        printWriter.print("mInvalidCharger=");
        printWriter.println(this.mInvalidCharger);
        printWriter.print("mShowing=");
        printWriter.println(SHOWING_STRINGS[this.mShowing]);
        printWriter.print("mSaverConfirmation=");
        String str = "not null";
        printWriter.println(this.mSaverConfirmation != null ? str : null);
        printWriter.print("mSaverEnabledConfirmation=");
        printWriter.print("mHighTempWarning=");
        printWriter.println(this.mHighTempWarning);
        printWriter.print("mHighTempDialog=");
        printWriter.println(this.mHighTempDialog != null ? str : null);
        printWriter.print("mThermalShutdownDialog=");
        printWriter.println(this.mThermalShutdownDialog != null ? str : null);
        printWriter.print("mUsbHighTempDialog=");
        if (this.mUsbHighTempDialog == null) {
            str = null;
        }
        printWriter.println(str);
    }

    private int getLowBatteryAutoTriggerDefaultLevel() {
        return this.mContext.getResources().getInteger(17694857);
    }

    public void update(int i, int i2, long j) {
        this.mBatteryLevel = i;
        if (i2 >= 0) {
            this.mWarningTriggerTimeMs = 0;
        } else if (i2 < this.mBucket) {
            this.mWarningTriggerTimeMs = System.currentTimeMillis();
        }
        this.mBucket = i2;
        this.mScreenOffTime = j;
    }

    public void updateSnapshot(BatteryStateSnapshot batteryStateSnapshot) {
        this.mCurrentBatterySnapshot = batteryStateSnapshot;
    }

    private void updateNotification() {
        if (DEBUG) {
            Slog.d(TAG, "updateNotification mWarning=" + this.mWarning + " mPlaySound=" + this.mPlaySound + " mInvalidCharger=" + this.mInvalidCharger);
        }
        if (this.mInvalidCharger) {
            showInvalidChargerNotification();
            this.mShowing = 3;
        } else if (this.mWarning) {
            showWarningNotification();
            this.mShowing = 1;
        } else if (this.mShowAutoSaverSuggestion) {
            if (this.mShowing != 4) {
                showAutoSaverSuggestionNotification();
            }
            this.mShowing = 4;
        } else {
            this.mNoMan.cancelAsUser(TAG_BATTERY, 2, UserHandle.ALL);
            this.mNoMan.cancelAsUser(TAG_BATTERY, 3, UserHandle.ALL);
            this.mNoMan.cancelAsUser(TAG_AUTO_SAVER, 49, UserHandle.ALL);
            this.mShowing = 0;
        }
    }

    private void showInvalidChargerNotification() {
        Notification.Builder color = new Notification.Builder(this.mContext, NotificationChannels.ALERTS).setSmallIcon(C1894R.C1896drawable.ic_power_low).setWhen(0).setShowWhen(false).setOngoing(true).setContentTitle(this.mContext.getString(C1894R.string.invalid_charger_title)).setContentText(this.mContext.getString(C1894R.string.invalid_charger_text)).setColor(this.mContext.getColor(17170460));
        SystemUIApplication.overrideNotificationAppName(this.mContext, color, false);
        Notification build = color.build();
        this.mNoMan.cancelAsUser(TAG_BATTERY, 3, UserHandle.ALL);
        this.mNoMan.notifyAsUser(TAG_BATTERY, 2, build, UserHandle.ALL);
    }

    /* access modifiers changed from: protected */
    public void showWarningNotification() {
        if (showSevereLowBatteryDialog()) {
            this.mBroadcastSender.sendBroadcast(new Intent(ACTION_ENABLE_SEVERE_BATTERY_DIALOG).setPackage(this.mContext.getPackageName()).putExtra(EXTRA_SCHEDULED_BY_PERCENTAGE, isScheduledByPercentage()).addFlags(1342177280));
            dismissLowBatteryNotification();
            this.mPlaySound = false;
        } else if (!isScheduledByPercentage()) {
            String format = NumberFormat.getPercentInstance().format(((double) this.mCurrentBatterySnapshot.getBatteryLevel()) / 100.0d);
            String string = this.mContext.getString(C1894R.string.battery_low_title);
            String string2 = this.mContext.getString(C1894R.string.battery_low_description, new Object[]{format});
            Notification.Builder visibility = new Notification.Builder(this.mContext, NotificationChannels.BATTERY).setSmallIcon(C1894R.C1896drawable.ic_power_low).setWhen(this.mWarningTriggerTimeMs).setShowWhen(false).setContentText(string2).setContentTitle(string).setOnlyAlertOnce(true).setDeleteIntent(pendingBroadcast(ACTION_DISMISSED_WARNING)).setStyle(new Notification.BigTextStyle().bigText(string2)).setVisibility(1);
            if (hasBatterySettings()) {
                visibility.setContentIntent(pendingBroadcast(ACTION_SHOW_BATTERY_SAVER_SETTINGS));
            }
            if (!this.mCurrentBatterySnapshot.isHybrid() || this.mBucket < -1 || this.mCurrentBatterySnapshot.getTimeRemainingMillis() < this.mCurrentBatterySnapshot.getSevereThresholdMillis()) {
                visibility.setColor(Utils.getColorAttrDefaultColor(this.mContext, 16844099));
            }
            if (!this.mPowerMan.isPowerSaveMode()) {
                visibility.addAction(0, this.mContext.getString(C1894R.string.battery_saver_dismiss_action), pendingBroadcast(ACTION_DISMISSED_WARNING));
                visibility.addAction(0, this.mContext.getString(C1894R.string.battery_saver_start_action), pendingBroadcast(ACTION_START_SAVER));
            }
            visibility.setOnlyAlertOnce(!this.mPlaySound);
            this.mPlaySound = false;
            SystemUIApplication.overrideNotificationAppName(this.mContext, visibility, false);
            Notification build = visibility.build();
            this.mNoMan.cancelAsUser(TAG_BATTERY, 2, UserHandle.ALL);
            this.mNoMan.notifyAsUser(TAG_BATTERY, 3, build, UserHandle.ALL);
        }
    }

    private boolean showSevereLowBatteryDialog() {
        return this.mBucket < -1 && this.mUseSevereDialog;
    }

    private boolean isScheduledByPercentage() {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (Settings.Global.getInt(contentResolver, "automatic_power_save_mode", 0) != 0 || Settings.Global.getInt(contentResolver, "low_power_trigger_level", 0) <= 0) {
            return false;
        }
        return true;
    }

    private void showAutoSaverSuggestionNotification() {
        String string = this.mContext.getString(C1894R.string.auto_saver_text);
        Notification.Builder contentText = new Notification.Builder(this.mContext, NotificationChannels.HINTS).setSmallIcon(C1894R.C1896drawable.ic_power_saver).setWhen(0).setShowWhen(false).setContentTitle(this.mContext.getString(C1894R.string.auto_saver_title)).setStyle(new Notification.BigTextStyle().bigText(string)).setContentText(string);
        contentText.setContentIntent(pendingBroadcast(ACTION_ENABLE_AUTO_SAVER));
        contentText.setDeleteIntent(pendingBroadcast(ACTION_DISMISS_AUTO_SAVER_SUGGESTION));
        contentText.addAction(0, this.mContext.getString(C1894R.string.no_auto_saver_action), pendingBroadcast(ACTION_AUTO_SAVER_NO_THANKS));
        SystemUIApplication.overrideNotificationAppName(this.mContext, contentText, false);
        this.mNoMan.notifyAsUser(TAG_AUTO_SAVER, 49, contentText.build(), UserHandle.ALL);
    }

    private String getHybridContentString(String str) {
        return PowerUtil.getBatteryRemainingStringFormatted(this.mContext, this.mCurrentBatterySnapshot.getTimeRemainingMillis(), str, this.mCurrentBatterySnapshot.isBasedOnUsage());
    }

    private PendingIntent pendingBroadcast(String str) {
        return PendingIntent.getBroadcastAsUser(this.mContext, 0, new Intent(str).setPackage(this.mContext.getPackageName()).setFlags(268435456), 67108864, UserHandle.CURRENT);
    }

    private static Intent settings(String str) {
        return new Intent(str).setFlags(1551892480);
    }

    public boolean isInvalidChargerWarningShowing() {
        return this.mInvalidCharger;
    }

    public void dismissHighTemperatureWarning() {
        if (this.mHighTempWarning) {
            dismissHighTemperatureWarningInternal();
        }
    }

    /* access modifiers changed from: private */
    public void dismissHighTemperatureWarningInternal() {
        this.mNoMan.cancelAsUser(TAG_TEMPERATURE, 4, UserHandle.ALL);
        this.mHighTempWarning = false;
    }

    public void showHighTemperatureWarning() {
        if (!this.mHighTempWarning) {
            this.mHighTempWarning = true;
            String string = this.mContext.getString(C1894R.string.high_temp_notif_message);
            Notification.Builder color = new Notification.Builder(this.mContext, NotificationChannels.ALERTS).setSmallIcon(C1894R.C1896drawable.ic_device_thermostat_24).setWhen(0).setShowWhen(false).setContentTitle(this.mContext.getString(C1894R.string.high_temp_title)).setContentText(string).setStyle(new Notification.BigTextStyle().bigText(string)).setVisibility(1).setContentIntent(pendingBroadcast(ACTION_CLICKED_TEMP_WARNING)).setDeleteIntent(pendingBroadcast(ACTION_DISMISSED_TEMP_WARNING)).setColor(Utils.getColorAttrDefaultColor(this.mContext, 16844099));
            SystemUIApplication.overrideNotificationAppName(this.mContext, color, false);
            this.mNoMan.notifyAsUser(TAG_TEMPERATURE, 4, color.build(), UserHandle.ALL);
        }
    }

    /* access modifiers changed from: private */
    public void showHighTemperatureDialog() {
        if (this.mHighTempDialog == null) {
            SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext);
            systemUIDialog.setIconAttribute(16843605);
            systemUIDialog.setTitle(C1894R.string.high_temp_title);
            systemUIDialog.setMessage(C1894R.string.high_temp_dialog_message);
            systemUIDialog.setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
            systemUIDialog.setShowForAllUsers(true);
            systemUIDialog.setOnDismissListener(new PowerNotificationWarnings$$ExternalSyntheticLambda10(this));
            final String string = this.mContext.getString(C1894R.string.high_temp_dialog_help_url);
            if (!string.isEmpty()) {
                systemUIDialog.setNeutralButton(C1894R.string.high_temp_dialog_help_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PowerNotificationWarnings.this.mActivityStarter.startActivity(new Intent("android.intent.action.VIEW").setData(Uri.parse(string)).setFlags(268435456), true, (ActivityStarter.Callback) new PowerNotificationWarnings$1$$ExternalSyntheticLambda0(this));
                    }

                    /* access modifiers changed from: package-private */
                    /* renamed from: lambda$onClick$0$com-android-systemui-power-PowerNotificationWarnings$1 */
                    public /* synthetic */ void mo35579x7a7caa18(int i) {
                        SystemUIDialog unused = PowerNotificationWarnings.this.mHighTempDialog = null;
                    }
                });
            }
            systemUIDialog.show();
            this.mHighTempDialog = systemUIDialog;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showHighTemperatureDialog$0$com-android-systemui-power-PowerNotificationWarnings */
    public /* synthetic */ void mo35557x3be3e817(DialogInterface dialogInterface) {
        this.mHighTempDialog = null;
    }

    /* access modifiers changed from: package-private */
    public void dismissThermalShutdownWarning() {
        this.mNoMan.cancelAsUser(TAG_TEMPERATURE, 39, UserHandle.ALL);
    }

    /* access modifiers changed from: private */
    public void showThermalShutdownDialog() {
        if (this.mThermalShutdownDialog == null) {
            SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext);
            systemUIDialog.setIconAttribute(16843605);
            systemUIDialog.setTitle(C1894R.string.thermal_shutdown_title);
            systemUIDialog.setMessage(C1894R.string.thermal_shutdown_dialog_message);
            systemUIDialog.setPositiveButton(17039370, (DialogInterface.OnClickListener) null);
            systemUIDialog.setShowForAllUsers(true);
            systemUIDialog.setOnDismissListener(new PowerNotificationWarnings$$ExternalSyntheticLambda5(this));
            final String string = this.mContext.getString(C1894R.string.thermal_shutdown_dialog_help_url);
            if (!string.isEmpty()) {
                systemUIDialog.setNeutralButton(C1894R.string.thermal_shutdown_dialog_help_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PowerNotificationWarnings.this.mActivityStarter.startActivity(new Intent("android.intent.action.VIEW").setData(Uri.parse(string)).setFlags(268435456), true, (ActivityStarter.Callback) new PowerNotificationWarnings$2$$ExternalSyntheticLambda0(this));
                    }

                    /* access modifiers changed from: package-private */
                    /* renamed from: lambda$onClick$0$com-android-systemui-power-PowerNotificationWarnings$2 */
                    public /* synthetic */ void mo35581x7a7caa19(int i) {
                        SystemUIDialog unused = PowerNotificationWarnings.this.mThermalShutdownDialog = null;
                    }
                });
            }
            systemUIDialog.show();
            this.mThermalShutdownDialog = systemUIDialog;
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showThermalShutdownDialog$1$com-android-systemui-power-PowerNotificationWarnings */
    public /* synthetic */ void mo35562xda5317fb(DialogInterface dialogInterface) {
        this.mThermalShutdownDialog = null;
    }

    public void showThermalShutdownWarning() {
        String string = this.mContext.getString(C1894R.string.thermal_shutdown_message);
        Notification.Builder color = new Notification.Builder(this.mContext, NotificationChannels.ALERTS).setSmallIcon(C1894R.C1896drawable.ic_device_thermostat_24).setWhen(0).setShowWhen(false).setContentTitle(this.mContext.getString(C1894R.string.thermal_shutdown_title)).setContentText(string).setStyle(new Notification.BigTextStyle().bigText(string)).setVisibility(1).setContentIntent(pendingBroadcast(ACTION_CLICKED_THERMAL_SHUTDOWN_WARNING)).setDeleteIntent(pendingBroadcast(ACTION_DISMISSED_THERMAL_SHUTDOWN_WARNING)).setColor(Utils.getColorAttrDefaultColor(this.mContext, 16844099));
        SystemUIApplication.overrideNotificationAppName(this.mContext, color, false);
        this.mNoMan.notifyAsUser(TAG_TEMPERATURE, 39, color.build(), UserHandle.ALL);
    }

    public void showUsbHighTemperatureAlarm() {
        this.mHandler.post(new PowerNotificationWarnings$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: showUsbHighTemperatureAlarmInternal */
    public void mo35563x55f2606c() {
        if (this.mUsbHighTempDialog == null) {
            SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext, (int) C1894R.style.Theme_SystemUI_Dialog_Alert);
            systemUIDialog.setCancelable(false);
            systemUIDialog.setIconAttribute(16843605);
            systemUIDialog.setTitle(C1894R.string.high_temp_alarm_title);
            systemUIDialog.setShowForAllUsers(true);
            systemUIDialog.setMessage(this.mContext.getString(C1894R.string.high_temp_alarm_notify_message, new Object[]{""}));
            systemUIDialog.setPositiveButton(17039370, new PowerNotificationWarnings$$ExternalSyntheticLambda2(this));
            systemUIDialog.setNegativeButton(C1894R.string.high_temp_alarm_help_care_steps, new PowerNotificationWarnings$$ExternalSyntheticLambda3(this));
            systemUIDialog.setOnDismissListener(new PowerNotificationWarnings$$ExternalSyntheticLambda4(this));
            systemUIDialog.getWindow().addFlags(2097280);
            systemUIDialog.show();
            this.mUsbHighTempDialog = systemUIDialog;
            Events.writeEvent(19, 3, Boolean.valueOf(this.mKeyguard.isKeyguardLocked()));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showUsbHighTemperatureAlarmInternal$3$com-android-systemui-power-PowerNotificationWarnings */
    public /* synthetic */ void mo35564xaf41b88e(DialogInterface dialogInterface, int i) {
        this.mUsbHighTempDialog = null;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showUsbHighTemperatureAlarmInternal$5$com-android-systemui-power-PowerNotificationWarnings */
    public /* synthetic */ void mo35566xe378b5cc(DialogInterface dialogInterface, int i) {
        String string = this.mContext.getString(C1894R.string.high_temp_alarm_help_url);
        Intent intent = new Intent();
        intent.setClassName(MediaOutputConstants.SETTINGS_PACKAGE_NAME, "com.android.settings.HelpTrampoline");
        intent.putExtra("android.intent.extra.TEXT", string);
        this.mActivityStarter.startActivity(intent, true, (ActivityStarter.Callback) new PowerNotificationWarnings$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showUsbHighTemperatureAlarmInternal$4$com-android-systemui-power-PowerNotificationWarnings */
    public /* synthetic */ void mo35565xc95d372d(int i) {
        this.mUsbHighTempDialog = null;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showUsbHighTemperatureAlarmInternal$6$com-android-systemui-power-PowerNotificationWarnings */
    public /* synthetic */ void mo35567xfd94346b(DialogInterface dialogInterface) {
        this.mUsbHighTempDialog = null;
        Events.writeEvent(20, 9, Boolean.valueOf(this.mKeyguard.isKeyguardLocked()));
    }

    public void updateLowBatteryWarning() {
        updateNotification();
    }

    public void dismissLowBatteryWarning() {
        if (DEBUG) {
            Slog.d(TAG, "dismissing low battery warning: level=" + this.mBatteryLevel);
        }
        dismissLowBatteryNotification();
    }

    /* access modifiers changed from: private */
    public void dismissLowBatteryNotification() {
        if (this.mWarning) {
            Slog.i(TAG, "dismissing low battery notification");
        }
        this.mWarning = false;
        updateNotification();
    }

    private boolean hasBatterySettings() {
        return this.mOpenBatterySettings.resolveActivity(this.mContext.getPackageManager()) != null;
    }

    public void showLowBatteryWarning(boolean z) {
        Slog.i(TAG, "show low battery warning: level=" + this.mBatteryLevel + " [" + this.mBucket + "] playSound=" + z);
        logEvent(BatteryWarningEvents.LowBatteryWarningEvent.LOW_BATTERY_NOTIFICATION);
        this.mPlaySound = z;
        this.mWarning = true;
        updateNotification();
    }

    public void dismissInvalidChargerWarning() {
        dismissInvalidChargerNotification();
    }

    private void dismissInvalidChargerNotification() {
        if (this.mInvalidCharger) {
            Slog.i(TAG, "dismissing invalid charger notification");
        }
        this.mInvalidCharger = false;
        updateNotification();
    }

    public void showInvalidChargerWarning() {
        this.mInvalidCharger = true;
        updateNotification();
    }

    /* access modifiers changed from: private */
    public void showAutoSaverSuggestion() {
        this.mShowAutoSaverSuggestion = true;
        updateNotification();
    }

    /* access modifiers changed from: private */
    public void dismissAutoSaverSuggestion() {
        this.mShowAutoSaverSuggestion = false;
        updateNotification();
    }

    public void userSwitched() {
        updateNotification();
    }

    /* access modifiers changed from: private */
    public void showStartSaverConfirmation(Bundle bundle) {
        if (this.mSaverConfirmation == null) {
            SystemUIDialog systemUIDialog = new SystemUIDialog(this.mContext);
            boolean z = bundle.getBoolean("extra_confirm_only");
            int i = bundle.getInt(BatterySaverUtils.EXTRA_POWER_SAVE_MODE_TRIGGER, 0);
            int i2 = bundle.getInt(BatterySaverUtils.EXTRA_POWER_SAVE_MODE_TRIGGER_LEVEL, 0);
            systemUIDialog.setMessage(getBatterySaverDescription());
            if (isEnglishLocale()) {
                systemUIDialog.setMessageHyphenationFrequency(0);
            }
            systemUIDialog.setMessageMovementMethod(LinkMovementMethod.getInstance());
            if (z) {
                systemUIDialog.setTitle(C1894R.string.battery_saver_confirmation_title_generic);
                systemUIDialog.setPositiveButton(17040078, new PowerNotificationWarnings$$ExternalSyntheticLambda6(this, i, i2));
            } else {
                systemUIDialog.setTitle(C1894R.string.battery_saver_confirmation_title);
                systemUIDialog.setPositiveButton(C1894R.string.battery_saver_confirmation_ok, new PowerNotificationWarnings$$ExternalSyntheticLambda7(this));
                systemUIDialog.setNegativeButton(17039360, new PowerNotificationWarnings$$ExternalSyntheticLambda8(this));
            }
            systemUIDialog.setShowForAllUsers(true);
            systemUIDialog.setOnDismissListener(new PowerNotificationWarnings$$ExternalSyntheticLambda9(this));
            WeakReference<View> lastPowerSaverStartView = this.mBatteryControllerLazy.get().getLastPowerSaverStartView();
            if (lastPowerSaverStartView == null || lastPowerSaverStartView.get() == null || !lastPowerSaverStartView.get().isAggregatedVisible()) {
                systemUIDialog.show();
            } else {
                this.mDialogLaunchAnimator.showFromView(systemUIDialog, lastPowerSaverStartView.get());
            }
            logEvent(BatteryWarningEvents.LowBatteryWarningEvent.SAVER_CONFIRM_DIALOG);
            this.mSaverConfirmation = systemUIDialog;
            this.mBatteryControllerLazy.get().clearLastPowerSaverStartView();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showStartSaverConfirmation$7$com-android-systemui-power-PowerNotificationWarnings */
    public /* synthetic */ void mo35559x58f07b6e(int i, int i2, DialogInterface dialogInterface, int i3) {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Settings.Global.putInt(contentResolver, "automatic_power_save_mode", i);
        Settings.Global.putInt(contentResolver, "low_power_trigger_level", i2);
        Settings.Secure.putIntForUser(contentResolver, "low_power_warning_acknowledged", 1, -2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showStartSaverConfirmation$8$com-android-systemui-power-PowerNotificationWarnings */
    public /* synthetic */ void mo35560x730bfa0d(DialogInterface dialogInterface, int i) {
        setSaverMode(true, false);
        logEvent(BatteryWarningEvents.LowBatteryWarningEvent.SAVER_CONFIRM_OK);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showStartSaverConfirmation$9$com-android-systemui-power-PowerNotificationWarnings */
    public /* synthetic */ void mo35561x8d2778ac(DialogInterface dialogInterface, int i) {
        logEvent(BatteryWarningEvents.LowBatteryWarningEvent.SAVER_CONFIRM_CANCEL);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showStartSaverConfirmation$10$com-android-systemui-power-PowerNotificationWarnings */
    public /* synthetic */ void mo35558xcbaca002(DialogInterface dialogInterface) {
        this.mSaverConfirmation = null;
        logEvent(BatteryWarningEvents.LowBatteryWarningEvent.SAVER_CONFIRM_DISMISS);
    }

    /* access modifiers changed from: package-private */
    public Dialog getSaverConfirmationDialog() {
        return this.mSaverConfirmation;
    }

    private boolean isEnglishLocale() {
        return Objects.equals(Locale.getDefault().getLanguage(), Locale.ENGLISH.getLanguage());
    }

    private CharSequence getBatterySaverDescription() {
        String charSequence = this.mContext.getText(C1894R.string.help_uri_battery_saver_learn_more_link_target).toString();
        if (TextUtils.isEmpty(charSequence)) {
            return this.mContext.getText(C1894R.string.battery_low_intro);
        }
        SpannableString spannableString = new SpannableString(this.mContext.getText(17039798));
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(spannableString);
        for (Annotation annotation : (Annotation[]) spannableString.getSpans(0, spannableString.length(), Annotation.class)) {
            if (BATTERY_SAVER_DESCRIPTION_URL_KEY.equals(annotation.getValue())) {
                int spanStart = spannableString.getSpanStart(annotation);
                int spanEnd = spannableString.getSpanEnd(annotation);
                C23083 r8 = new URLSpan(charSequence) {
                    public void updateDrawState(TextPaint textPaint) {
                        super.updateDrawState(textPaint);
                        textPaint.setUnderlineText(false);
                    }

                    public void onClick(View view) {
                        if (PowerNotificationWarnings.this.mSaverConfirmation != null) {
                            PowerNotificationWarnings.this.mSaverConfirmation.dismiss();
                        }
                        PowerNotificationWarnings.this.mBroadcastSender.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS").setFlags(268435456));
                        Uri parse = Uri.parse(getURL());
                        Context context = view.getContext();
                        Intent flags = new Intent("android.intent.action.VIEW", parse).setFlags(268435456);
                        try {
                            context.startActivity(flags);
                        } catch (ActivityNotFoundException unused) {
                            Log.w(PowerNotificationWarnings.TAG, "Activity was not found for intent, " + flags.toString());
                        }
                    }
                };
                spannableStringBuilder.setSpan(r8, spanStart, spanEnd, spannableString.getSpanFlags(r8));
            }
        }
        return spannableStringBuilder;
    }

    /* access modifiers changed from: private */
    public void setSaverMode(boolean z, boolean z2) {
        BatterySaverUtils.setPowerSaveMode(this.mContext, z, z2);
    }

    /* access modifiers changed from: private */
    public void startBatterySaverSchedulePage() {
        Intent intent = new Intent(BATTERY_SAVER_SCHEDULE_SCREEN_INTENT_ACTION);
        intent.setFlags(268468224);
        this.mActivityStarter.startActivity(intent, true);
    }

    /* access modifiers changed from: private */
    public void logEvent(BatteryWarningEvents.LowBatteryWarningEvent lowBatteryWarningEvent) {
        UiEventLogger uiEventLogger = this.mUiEventLogger;
        if (uiEventLogger != null) {
            uiEventLogger.log(lowBatteryWarningEvent);
        }
    }

    private final class Receiver extends BroadcastReceiver {
        private Receiver() {
        }

        public void init() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(PowerNotificationWarnings.ACTION_SHOW_BATTERY_SAVER_SETTINGS);
            intentFilter.addAction(PowerNotificationWarnings.ACTION_START_SAVER);
            intentFilter.addAction(PowerNotificationWarnings.ACTION_DISMISSED_WARNING);
            intentFilter.addAction(PowerNotificationWarnings.ACTION_CLICKED_TEMP_WARNING);
            intentFilter.addAction(PowerNotificationWarnings.ACTION_DISMISSED_TEMP_WARNING);
            intentFilter.addAction(PowerNotificationWarnings.ACTION_CLICKED_THERMAL_SHUTDOWN_WARNING);
            intentFilter.addAction(PowerNotificationWarnings.ACTION_DISMISSED_THERMAL_SHUTDOWN_WARNING);
            intentFilter.addAction("PNW.startSaverConfirmation");
            intentFilter.addAction("PNW.autoSaverSuggestion");
            intentFilter.addAction(PowerNotificationWarnings.ACTION_ENABLE_AUTO_SAVER);
            intentFilter.addAction(PowerNotificationWarnings.ACTION_AUTO_SAVER_NO_THANKS);
            intentFilter.addAction(PowerNotificationWarnings.ACTION_DISMISS_AUTO_SAVER_SUGGESTION);
            PowerNotificationWarnings.this.mContext.registerReceiverAsUser(this, UserHandle.ALL, intentFilter, "android.permission.DEVICE_POWER", PowerNotificationWarnings.this.mHandler, 2);
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Slog.i(PowerNotificationWarnings.TAG, "Received " + action);
            if (action.equals(PowerNotificationWarnings.ACTION_SHOW_BATTERY_SAVER_SETTINGS)) {
                PowerNotificationWarnings.this.logEvent(BatteryWarningEvents.LowBatteryWarningEvent.LOW_BATTERY_NOTIFICATION_SETTINGS);
                PowerNotificationWarnings.this.dismissLowBatteryNotification();
                PowerNotificationWarnings.this.mContext.startActivityAsUser(PowerNotificationWarnings.this.mOpenBatterySaverSettings, UserHandle.CURRENT);
            } else if (action.equals(PowerNotificationWarnings.ACTION_START_SAVER)) {
                PowerNotificationWarnings.this.logEvent(BatteryWarningEvents.LowBatteryWarningEvent.LOW_BATTERY_NOTIFICATION_TURN_ON);
                PowerNotificationWarnings.this.setSaverMode(true, true);
                PowerNotificationWarnings.this.dismissLowBatteryNotification();
            } else if (action.equals("PNW.startSaverConfirmation")) {
                PowerNotificationWarnings.this.dismissLowBatteryNotification();
                PowerNotificationWarnings.this.showStartSaverConfirmation(intent.getExtras());
            } else if (action.equals(PowerNotificationWarnings.ACTION_DISMISSED_WARNING)) {
                PowerNotificationWarnings.this.logEvent(BatteryWarningEvents.LowBatteryWarningEvent.LOW_BATTERY_NOTIFICATION_CANCEL);
                PowerNotificationWarnings.this.dismissLowBatteryWarning();
            } else if (PowerNotificationWarnings.ACTION_CLICKED_TEMP_WARNING.equals(action)) {
                PowerNotificationWarnings.this.dismissHighTemperatureWarningInternal();
                PowerNotificationWarnings.this.showHighTemperatureDialog();
            } else if (PowerNotificationWarnings.ACTION_DISMISSED_TEMP_WARNING.equals(action)) {
                PowerNotificationWarnings.this.dismissHighTemperatureWarningInternal();
            } else if (PowerNotificationWarnings.ACTION_CLICKED_THERMAL_SHUTDOWN_WARNING.equals(action)) {
                PowerNotificationWarnings.this.dismissThermalShutdownWarning();
                PowerNotificationWarnings.this.showThermalShutdownDialog();
            } else if (PowerNotificationWarnings.ACTION_DISMISSED_THERMAL_SHUTDOWN_WARNING.equals(action)) {
                PowerNotificationWarnings.this.dismissThermalShutdownWarning();
            } else if ("PNW.autoSaverSuggestion".equals(action)) {
                PowerNotificationWarnings.this.showAutoSaverSuggestion();
            } else if (PowerNotificationWarnings.ACTION_DISMISS_AUTO_SAVER_SUGGESTION.equals(action)) {
                PowerNotificationWarnings.this.dismissAutoSaverSuggestion();
            } else if (PowerNotificationWarnings.ACTION_ENABLE_AUTO_SAVER.equals(action)) {
                PowerNotificationWarnings.this.dismissAutoSaverSuggestion();
                PowerNotificationWarnings.this.startBatterySaverSchedulePage();
            } else if (PowerNotificationWarnings.ACTION_AUTO_SAVER_NO_THANKS.equals(action)) {
                PowerNotificationWarnings.this.dismissAutoSaverSuggestion();
                BatterySaverUtils.suppressAutoBatterySaver(context);
            }
        }
    }

    public void showCriticalTemperatureWarning() {
        this.mEx.showCriticalTemperatureWarning();
    }

    public void dismissCriticaTemperatureWarning() {
        this.mEx.dismissCriticaTemperatureWarning();
    }
}
