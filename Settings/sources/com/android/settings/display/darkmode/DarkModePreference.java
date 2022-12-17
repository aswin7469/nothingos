package com.android.settings.display.darkmode;

import android.app.UiModeManager;
import android.content.Context;
import android.os.PowerManager;
import android.util.AttributeSet;
import com.android.settings.R$string;
import com.android.settingslib.PrimarySwitchPreference;
import java.time.LocalTime;

public class DarkModePreference extends PrimarySwitchPreference {
    private Runnable mCallback;
    private DarkModeObserver mDarkModeObserver;
    private TimeFormatter mFormat;
    private PowerManager mPowerManager;
    private UiModeManager mUiModeManager;

    public DarkModePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDarkModeObserver = new DarkModeObserver(context);
        this.mUiModeManager = (UiModeManager) context.getSystemService(UiModeManager.class);
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        this.mFormat = new TimeFormatter(context);
        DarkModePreference$$ExternalSyntheticLambda0 darkModePreference$$ExternalSyntheticLambda0 = new DarkModePreference$$ExternalSyntheticLambda0(this);
        this.mCallback = darkModePreference$$ExternalSyntheticLambda0;
        this.mDarkModeObserver.subscribe(darkModePreference$$ExternalSyntheticLambda0);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        boolean isPowerSaveMode = this.mPowerManager.isPowerSaveMode();
        boolean z = (getContext().getResources().getConfiguration().uiMode & 32) != 0;
        setSwitchEnabled(!isPowerSaveMode);
        updateSummary(isPowerSaveMode, z);
    }

    public void onAttached() {
        super.onAttached();
        this.mDarkModeObserver.subscribe(this.mCallback);
    }

    public void onDetached() {
        super.onDetached();
        this.mDarkModeObserver.unsubscribe();
    }

    private void updateSummary(boolean z, boolean z2) {
        String str;
        int i;
        LocalTime localTime;
        int i2;
        int i3;
        int i4;
        int i5;
        if (z) {
            if (z2) {
                i5 = R$string.dark_ui_mode_disabled_summary_dark_theme_on;
            } else {
                i5 = R$string.dark_ui_mode_disabled_summary_dark_theme_off;
            }
            setSummary((CharSequence) getContext().getString(i5));
            return;
        }
        int nightMode = this.mUiModeManager.getNightMode();
        if (nightMode == 0) {
            Context context = getContext();
            if (z2) {
                i4 = R$string.dark_ui_summary_on_auto_mode_auto;
            } else {
                i4 = R$string.dark_ui_summary_off_auto_mode_auto;
            }
            str = context.getString(i4);
        } else if (nightMode != 3) {
            Context context2 = getContext();
            if (z2) {
                i = R$string.dark_ui_summary_on_auto_mode_never;
            } else {
                i = R$string.dark_ui_summary_off_auto_mode_never;
            }
            str = context2.getString(i);
        } else if (this.mUiModeManager.getNightModeCustomType() == 1) {
            Context context3 = getContext();
            if (z2) {
                i3 = R$string.dark_ui_summary_on_auto_mode_custom_bedtime;
            } else {
                i3 = R$string.dark_ui_summary_off_auto_mode_custom_bedtime;
            }
            str = context3.getString(i3);
        } else {
            if (z2) {
                localTime = this.mUiModeManager.getCustomNightModeEnd();
            } else {
                localTime = this.mUiModeManager.getCustomNightModeStart();
            }
            String of = this.mFormat.mo12819of(localTime);
            Context context4 = getContext();
            if (z2) {
                i2 = R$string.dark_ui_summary_on_auto_mode_custom;
            } else {
                i2 = R$string.dark_ui_summary_off_auto_mode_custom;
            }
            str = context4.getString(i2, new Object[]{of});
        }
        setSummary((CharSequence) str);
    }
}
