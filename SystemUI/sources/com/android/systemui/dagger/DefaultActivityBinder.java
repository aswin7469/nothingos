package com.android.systemui.dagger;

import android.app.Activity;
import com.android.systemui.ForegroundServicesDialog;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageActivity;
import com.android.systemui.keyguard.WorkLockActivity;
import com.android.systemui.people.PeopleSpaceActivity;
import com.android.systemui.people.widget.LaunchConversationActivity;
import com.android.systemui.screenshot.LongScreenshotActivity;
import com.android.systemui.sensorprivacy.SensorUseStartedActivity;
import com.android.systemui.sensorprivacy.television.TvUnblockSensorActivity;
import com.android.systemui.settings.brightness.BrightnessDialog;
import com.android.systemui.statusbar.p013tv.notifications.TvNotificationPanelActivity;
import com.android.systemui.tuner.TunerActivity;
import com.android.systemui.usb.UsbConfirmActivity;
import com.android.systemui.usb.UsbDebuggingActivity;
import com.android.systemui.usb.UsbDebuggingSecondaryUserActivity;
import com.android.systemui.usb.UsbPermissionActivity;
import com.android.systemui.user.CreateUserActivity;
import com.nothing.systemui.power.NTCriticalTemperatureWarning;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
public abstract class DefaultActivityBinder {
    @IntoMap
    @ClassKey(BrightnessDialog.class)
    @Binds
    public abstract Activity bindBrightnessDialog(BrightnessDialog brightnessDialog);

    @IntoMap
    @ClassKey(CreateUserActivity.class)
    @Binds
    public abstract Activity bindCreateUserActivity(CreateUserActivity createUserActivity);

    @IntoMap
    @ClassKey(NTCriticalTemperatureWarning.class)
    @Binds
    public abstract Activity bindCriticalTemperatureWarning(NTCriticalTemperatureWarning nTCriticalTemperatureWarning);

    @IntoMap
    @ClassKey(ForegroundServicesDialog.class)
    @Binds
    public abstract Activity bindForegroundServicesDialog(ForegroundServicesDialog foregroundServicesDialog);

    @IntoMap
    @ClassKey(HdmiCecSetMenuLanguageActivity.class)
    @Binds
    public abstract Activity bindHdmiCecSetMenuLanguageActivity(HdmiCecSetMenuLanguageActivity hdmiCecSetMenuLanguageActivity);

    @IntoMap
    @ClassKey(LaunchConversationActivity.class)
    @Binds
    public abstract Activity bindLaunchConversationActivity(LaunchConversationActivity launchConversationActivity);

    @IntoMap
    @ClassKey(LongScreenshotActivity.class)
    @Binds
    public abstract Activity bindLongScreenshotActivity(LongScreenshotActivity longScreenshotActivity);

    @IntoMap
    @ClassKey(PeopleSpaceActivity.class)
    @Binds
    public abstract Activity bindPeopleSpaceActivity(PeopleSpaceActivity peopleSpaceActivity);

    @IntoMap
    @ClassKey(SensorUseStartedActivity.class)
    @Binds
    public abstract Activity bindSensorUseStartedActivity(SensorUseStartedActivity sensorUseStartedActivity);

    @IntoMap
    @ClassKey(TunerActivity.class)
    @Binds
    public abstract Activity bindTunerActivity(TunerActivity tunerActivity);

    @IntoMap
    @ClassKey(TvNotificationPanelActivity.class)
    @Binds
    public abstract Activity bindTvNotificationPanelActivity(TvNotificationPanelActivity tvNotificationPanelActivity);

    @IntoMap
    @ClassKey(TvUnblockSensorActivity.class)
    @Binds
    public abstract Activity bindTvUnblockSensorActivity(TvUnblockSensorActivity tvUnblockSensorActivity);

    @IntoMap
    @ClassKey(UsbConfirmActivity.class)
    @Binds
    public abstract Activity bindUsbConfirmActivity(UsbConfirmActivity usbConfirmActivity);

    @IntoMap
    @ClassKey(UsbDebuggingActivity.class)
    @Binds
    public abstract Activity bindUsbDebuggingActivity(UsbDebuggingActivity usbDebuggingActivity);

    @IntoMap
    @ClassKey(UsbDebuggingSecondaryUserActivity.class)
    @Binds
    public abstract Activity bindUsbDebuggingSecondaryUserActivity(UsbDebuggingSecondaryUserActivity usbDebuggingSecondaryUserActivity);

    @IntoMap
    @ClassKey(UsbPermissionActivity.class)
    @Binds
    public abstract Activity bindUsbPermissionActivity(UsbPermissionActivity usbPermissionActivity);

    @IntoMap
    @ClassKey(WorkLockActivity.class)
    @Binds
    public abstract Activity bindWorkLockActivity(WorkLockActivity workLockActivity);
}
