package com.android.settings.display.darkmode;

import android.app.UiModeManager;
import android.content.Context;
import android.content.IntentFilter;
import android.widget.Switch;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$string;
import com.android.settings.core.BasePreferenceController;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.core.instrumentation.MetricsFeatureProvider;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;
import java.time.LocalTime;

public class DarkModeActivationPreferenceController extends BasePreferenceController implements OnMainSwitchChangeListener {
    private TimeFormatter mFormat;
    private final MetricsFeatureProvider mMetricsFeatureProvider;
    private MainSwitchPreference mPreference;
    private final UiModeManager mUiModeManager;

    public int getAvailabilityStatus() {
        return 1;
    }

    public /* bridge */ /* synthetic */ Class getBackgroundWorkerClass() {
        return super.getBackgroundWorkerClass();
    }

    public /* bridge */ /* synthetic */ IntentFilter getIntentFilter() {
        return super.getIntentFilter();
    }

    public /* bridge */ /* synthetic */ int getSliceHighlightMenuRes() {
        return super.getSliceHighlightMenuRes();
    }

    public /* bridge */ /* synthetic */ boolean hasAsyncUpdate() {
        return super.hasAsyncUpdate();
    }

    public /* bridge */ /* synthetic */ boolean isPublicSlice() {
        return super.isPublicSlice();
    }

    public /* bridge */ /* synthetic */ boolean isSliceable() {
        return super.isSliceable();
    }

    public /* bridge */ /* synthetic */ boolean useDynamicSliceSummary() {
        return super.useDynamicSliceSummary();
    }

    public DarkModeActivationPreferenceController(Context context, String str) {
        super(context, str);
        this.mUiModeManager = (UiModeManager) context.getSystemService(UiModeManager.class);
        this.mFormat = new TimeFormatter(context);
        this.mMetricsFeatureProvider = FeatureFactory.getFactory(context).getMetricsFeatureProvider();
    }

    public DarkModeActivationPreferenceController(Context context, String str, TimeFormatter timeFormatter) {
        this(context, str);
        this.mFormat = timeFormatter;
    }

    public final void updateState(Preference preference) {
        this.mPreference.updateStatus((this.mContext.getResources().getConfiguration().uiMode & 32) != 0);
    }

    public CharSequence getSummary() {
        int i;
        LocalTime localTime;
        int i2;
        int i3;
        int i4;
        boolean z = (this.mContext.getResources().getConfiguration().uiMode & 32) != 0;
        int nightMode = this.mUiModeManager.getNightMode();
        if (nightMode == 0) {
            Context context = this.mContext;
            if (z) {
                i4 = R$string.dark_ui_summary_on_auto_mode_auto;
            } else {
                i4 = R$string.dark_ui_summary_off_auto_mode_auto;
            }
            return context.getString(i4);
        } else if (nightMode != 3) {
            Context context2 = this.mContext;
            if (z) {
                i = R$string.dark_ui_summary_on_auto_mode_never;
            } else {
                i = R$string.dark_ui_summary_off_auto_mode_never;
            }
            return context2.getString(i);
        } else if (this.mUiModeManager.getNightModeCustomType() == 1) {
            Context context3 = this.mContext;
            if (z) {
                i3 = R$string.dark_ui_summary_on_auto_mode_custom_bedtime;
            } else {
                i3 = R$string.dark_ui_summary_off_auto_mode_custom_bedtime;
            }
            return context3.getString(i3);
        } else {
            if (z) {
                localTime = this.mUiModeManager.getCustomNightModeEnd();
            } else {
                localTime = this.mUiModeManager.getCustomNightModeStart();
            }
            String of = this.mFormat.mo12819of(localTime);
            Context context4 = this.mContext;
            if (z) {
                i2 = R$string.dark_ui_summary_on_auto_mode_custom;
            } else {
                i2 = R$string.dark_ui_summary_off_auto_mode_custom;
            }
            return context4.getString(i2, new Object[]{of});
        }
    }

    public void onSwitchChanged(Switch switchR, boolean z) {
        this.mMetricsFeatureProvider.logClickedPreference(this.mPreference, getMetricsCategory());
        this.mUiModeManager.setNightModeActivated(!((this.mContext.getResources().getConfiguration().uiMode & 32) != 0));
    }

    public void displayPreference(PreferenceScreen preferenceScreen) {
        super.displayPreference(preferenceScreen);
        MainSwitchPreference mainSwitchPreference = (MainSwitchPreference) preferenceScreen.findPreference(getPreferenceKey());
        this.mPreference = mainSwitchPreference;
        mainSwitchPreference.addOnSwitchChangeListener(this);
    }
}
