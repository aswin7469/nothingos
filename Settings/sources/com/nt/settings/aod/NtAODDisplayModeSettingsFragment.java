package com.nt.settings.aod;

import android.app.Dialog;
import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.core.AbstractPreferenceController;
import com.nt.settings.aod.NtAODEndTimePreferenceController;
import com.nt.settings.aod.NtAODStartTimePreferenceController;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class NtAODDisplayModeSettingsFragment extends DashboardFragment implements NtAODStartTimePreferenceController.StartTimePreferenceHost, NtAODEndTimePreferenceController.EndTimePreferenceHost {
    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settings.DialogCreatable
    public int getDialogMetricsCategory(int i) {
        return (i == 0 || i == 1) ? 608 : 0;
    }

    @Override // com.android.settings.support.actionbar.HelpResourceProvider
    public int getHelpResource() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment
    public String getLogTag() {
        return "NtAODDisplayMode";
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.dashboard.DashboardFragment, com.android.settings.core.InstrumentedPreferenceFragment
    public int getPreferenceScreenResId() {
        return R.xml.nt_aod_display_mode_settings;
    }

    @Override // com.android.settings.dashboard.DashboardFragment
    protected List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        FragmentActivity activity = getActivity();
        NtAODDisplayModePreferenceController ntAODDisplayModePreferenceController = new NtAODDisplayModePreferenceController(activity, "aod_display_mode", this);
        NtAODStartTimePreferenceController ntAODStartTimePreferenceController = new NtAODStartTimePreferenceController(activity, this, ntAODDisplayModePreferenceController);
        NtAODEndTimePreferenceController ntAODEndTimePreferenceController = new NtAODEndTimePreferenceController(activity, this, ntAODDisplayModePreferenceController);
        arrayList.add(ntAODDisplayModePreferenceController);
        arrayList.add(ntAODStartTimePreferenceController);
        arrayList.add(ntAODEndTimePreferenceController);
        return arrayList;
    }

    @Override // com.nt.settings.aod.UpdateDisplayModeCallback
    public void updateDisplayMode(Context context) {
        updatePreferenceStates();
    }

    @Override // com.android.settings.SettingsPreferenceFragment, com.android.settings.DialogCreatable
    public Dialog onCreateDialog(int i) {
        if (i != 0) {
            if (i == 1) {
                return ((NtAODEndTimePreferenceController) use(NtAODEndTimePreferenceController.class)).buildTimePicker(getActivity());
            }
            throw new IllegalArgumentException();
        }
        return ((NtAODStartTimePreferenceController) use(NtAODStartTimePreferenceController.class)).buildTimePicker(getActivity());
    }

    @Override // com.nt.settings.aod.NtAODEndTimePreferenceController.EndTimePreferenceHost
    public void showEndTimePicker() {
        removeDialog(1);
        showDialog(1);
    }

    @Override // com.nt.settings.aod.NtAODStartTimePreferenceController.StartTimePreferenceHost
    public void showStartTimePicker() {
        removeDialog(0);
        showDialog(0);
    }
}
