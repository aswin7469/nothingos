package com.nothing.settings.display.aod;

import android.app.Dialog;
import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R$xml;
import com.android.settings.dashboard.DashboardFragment;
import com.android.settingslib.core.AbstractPreferenceController;
import com.nothing.settings.display.aod.AodEndTimePreferenceController;
import com.nothing.settings.display.aod.AodStartTimePreferenceController;
import java.util.ArrayList;
import java.util.List;

public class AodDisplayModeFragment extends DashboardFragment implements AodStartTimePreferenceController.StartTimePreferenceHost, AodEndTimePreferenceController.EndTimePreferenceHost {
    public int getDialogMetricsCategory(int i) {
        return (i == 0 || i == 1) ? 608 : 0;
    }

    public int getHelpResource() {
        return 0;
    }

    public String getLogTag() {
        return "AodDisplayMode";
    }

    public int getMetricsCategory() {
        return 0;
    }

    public int getPreferenceScreenResId() {
        return R$xml.aod_display_mode_settings;
    }

    /* access modifiers changed from: protected */
    public List<AbstractPreferenceController> createPreferenceControllers(Context context) {
        ArrayList arrayList = new ArrayList();
        FragmentActivity activity = getActivity();
        AodScheduleSwitchPreferenceController aodScheduleSwitchPreferenceController = new AodScheduleSwitchPreferenceController(activity, AodScheduleSwitchPreferenceController.KEY_AOD_DISPLAY_MODE, this);
        AodStartTimePreferenceController aodStartTimePreferenceController = new AodStartTimePreferenceController(activity, this);
        AodEndTimePreferenceController aodEndTimePreferenceController = new AodEndTimePreferenceController(activity, this);
        arrayList.add(aodScheduleSwitchPreferenceController);
        arrayList.add(aodStartTimePreferenceController);
        arrayList.add(aodEndTimePreferenceController);
        return arrayList;
    }

    public void updateDisplayMode(Context context) {
        updatePreferenceStates();
    }

    public Dialog onCreateDialog(int i) {
        if (i == 0) {
            return ((AodStartTimePreferenceController) use(AodStartTimePreferenceController.class)).buildTimePicker(getActivity());
        }
        if (i == 1) {
            return ((AodEndTimePreferenceController) use(AodEndTimePreferenceController.class)).buildTimePicker(getActivity());
        }
        throw new IllegalArgumentException();
    }

    public void showEndTimePicker() {
        removeDialog(1);
        showDialog(1);
    }

    public void showStartTimePicker() {
        removeDialog(0);
        showDialog(0);
    }
}
