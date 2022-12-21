package com.android.systemui.keyguard;

import android.app.AlarmManager;
import android.content.ContentResolver;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.nothing.systemui.keyguard.calendar.CalendarManager;
import com.nothing.systemui.keyguard.weather.KeyguardWeatherController;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class KeyguardSliceProvider_MembersInjector implements MembersInjector<KeyguardSliceProvider> {
    private final Provider<AlarmManager> mAlarmManagerProvider;
    private final Provider<CalendarManager> mCalendarManagerProvider;
    private final Provider<ContentResolver> mContentResolverProvider;
    private final Provider<DozeParameters> mDozeParametersProvider;
    private final Provider<KeyguardBypassController> mKeyguardBypassControllerProvider;
    private final Provider<KeyguardUpdateMonitor> mKeyguardUpdateMonitorProvider;
    private final Provider<KeyguardWeatherController> mKeyguardWeatherControllerProvider;
    private final Provider<NotificationMediaManager> mMediaManagerProvider;
    private final Provider<NextAlarmController> mNextAlarmControllerProvider;
    private final Provider<StatusBarStateController> mStatusBarStateControllerProvider;
    private final Provider<ZenModeController> mZenModeControllerProvider;

    public KeyguardSliceProvider_MembersInjector(Provider<DozeParameters> provider, Provider<ZenModeController> provider2, Provider<NextAlarmController> provider3, Provider<AlarmManager> provider4, Provider<ContentResolver> provider5, Provider<NotificationMediaManager> provider6, Provider<StatusBarStateController> provider7, Provider<KeyguardBypassController> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<KeyguardWeatherController> provider10, Provider<CalendarManager> provider11) {
        this.mDozeParametersProvider = provider;
        this.mZenModeControllerProvider = provider2;
        this.mNextAlarmControllerProvider = provider3;
        this.mAlarmManagerProvider = provider4;
        this.mContentResolverProvider = provider5;
        this.mMediaManagerProvider = provider6;
        this.mStatusBarStateControllerProvider = provider7;
        this.mKeyguardBypassControllerProvider = provider8;
        this.mKeyguardUpdateMonitorProvider = provider9;
        this.mKeyguardWeatherControllerProvider = provider10;
        this.mCalendarManagerProvider = provider11;
    }

    public static MembersInjector<KeyguardSliceProvider> create(Provider<DozeParameters> provider, Provider<ZenModeController> provider2, Provider<NextAlarmController> provider3, Provider<AlarmManager> provider4, Provider<ContentResolver> provider5, Provider<NotificationMediaManager> provider6, Provider<StatusBarStateController> provider7, Provider<KeyguardBypassController> provider8, Provider<KeyguardUpdateMonitor> provider9, Provider<KeyguardWeatherController> provider10, Provider<CalendarManager> provider11) {
        return new KeyguardSliceProvider_MembersInjector(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    public void injectMembers(KeyguardSliceProvider keyguardSliceProvider) {
        injectMDozeParameters(keyguardSliceProvider, this.mDozeParametersProvider.get());
        injectMZenModeController(keyguardSliceProvider, this.mZenModeControllerProvider.get());
        injectMNextAlarmController(keyguardSliceProvider, this.mNextAlarmControllerProvider.get());
        injectMAlarmManager(keyguardSliceProvider, this.mAlarmManagerProvider.get());
        injectMContentResolver(keyguardSliceProvider, this.mContentResolverProvider.get());
        injectMMediaManager(keyguardSliceProvider, this.mMediaManagerProvider.get());
        injectMStatusBarStateController(keyguardSliceProvider, this.mStatusBarStateControllerProvider.get());
        injectMKeyguardBypassController(keyguardSliceProvider, this.mKeyguardBypassControllerProvider.get());
        injectMKeyguardUpdateMonitor(keyguardSliceProvider, this.mKeyguardUpdateMonitorProvider.get());
        injectMKeyguardWeatherController(keyguardSliceProvider, this.mKeyguardWeatherControllerProvider.get());
        injectMCalendarManager(keyguardSliceProvider, this.mCalendarManagerProvider.get());
    }

    public static void injectMDozeParameters(KeyguardSliceProvider keyguardSliceProvider, DozeParameters dozeParameters) {
        keyguardSliceProvider.mDozeParameters = dozeParameters;
    }

    public static void injectMZenModeController(KeyguardSliceProvider keyguardSliceProvider, ZenModeController zenModeController) {
        keyguardSliceProvider.mZenModeController = zenModeController;
    }

    public static void injectMNextAlarmController(KeyguardSliceProvider keyguardSliceProvider, NextAlarmController nextAlarmController) {
        keyguardSliceProvider.mNextAlarmController = nextAlarmController;
    }

    public static void injectMAlarmManager(KeyguardSliceProvider keyguardSliceProvider, AlarmManager alarmManager) {
        keyguardSliceProvider.mAlarmManager = alarmManager;
    }

    public static void injectMContentResolver(KeyguardSliceProvider keyguardSliceProvider, ContentResolver contentResolver) {
        keyguardSliceProvider.mContentResolver = contentResolver;
    }

    public static void injectMMediaManager(KeyguardSliceProvider keyguardSliceProvider, NotificationMediaManager notificationMediaManager) {
        keyguardSliceProvider.mMediaManager = notificationMediaManager;
    }

    public static void injectMStatusBarStateController(KeyguardSliceProvider keyguardSliceProvider, StatusBarStateController statusBarStateController) {
        keyguardSliceProvider.mStatusBarStateController = statusBarStateController;
    }

    public static void injectMKeyguardBypassController(KeyguardSliceProvider keyguardSliceProvider, KeyguardBypassController keyguardBypassController) {
        keyguardSliceProvider.mKeyguardBypassController = keyguardBypassController;
    }

    public static void injectMKeyguardUpdateMonitor(KeyguardSliceProvider keyguardSliceProvider, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        keyguardSliceProvider.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
    }

    public static void injectMKeyguardWeatherController(KeyguardSliceProvider keyguardSliceProvider, KeyguardWeatherController keyguardWeatherController) {
        keyguardSliceProvider.mKeyguardWeatherController = keyguardWeatherController;
    }

    public static void injectMCalendarManager(KeyguardSliceProvider keyguardSliceProvider, CalendarManager calendarManager) {
        keyguardSliceProvider.mCalendarManager = calendarManager;
    }
}
