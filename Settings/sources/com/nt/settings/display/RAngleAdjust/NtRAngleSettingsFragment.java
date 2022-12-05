package com.nt.settings.display.RAngleAdjust;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Switch;
import com.android.settings.R;
import com.nt.common.widget.SingleArrayPartitionAdapter;
import com.nt.settings.AppListBaseSettings;
import com.nt.settings.display.RAngleAdjust.NtRAngleAdjustAdapter;
import com.nt.settings.widget.NtAppEntry;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes2.dex */
public class NtRAngleSettingsFragment extends AppListBaseSettings {
    private static final String TAG = NtRAngleSettingsFragment.class.getSimpleName();
    private NtRAngleAdjustController mNtRAngleAdjustController;

    @Override // com.nt.settings.AppListBaseSettings, com.nt.settings.SettingsPreferenceFragment, android.preference.PreferenceFragment, android.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mListView.setOnScrollListener(null);
        this.mNtRAngleAdjustController = NtRAngleAdjustController.getInstance(getContext());
        getActivity().setTitle(R.string.r_angle_adjust);
    }

    @Override // com.nt.settings.AppListBaseSettings
    public SingleArrayPartitionAdapter<NtAppEntry> createNtSingleArrayGroupAdapter(List<NtAppEntry> list) {
        if (list.size() > 0) {
            NtAppEntry ntAppEntry = list.get(0);
            if (ntAppEntry == null || ntAppEntry.mInfo != null || ntAppEntry.mResolveInfo != null) {
                list.add(0, new NtAppEntry(getActivity(), null, null, null));
                this.mSwitchOffOnSizeArray[1] = list.size() - 1;
            }
        } else {
            list.add(0, new NtAppEntry(getActivity(), null, null, null));
            this.mSwitchOffOnSizeArray[1] = 0;
        }
        this.mSwitchOffOnSizeArray[0] = 1;
        return new NtRAngleAdjustAdapter(getActivity(), list, this.mSwitchOffOnSizeArray, this.mLaunchNames, this.mAppIconMemoryOptimizeHelper);
    }

    @Override // com.nt.settings.AppListBaseSettings
    protected AdapterView.OnItemClickListener createOnItemClickListener() {
        return new AdapterView.OnItemClickListener() { // from class: com.nt.settings.display.RAngleAdjust.NtRAngleSettingsFragment.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                String str = NtRAngleSettingsFragment.TAG;
                Log.d(str, "@_@ ------ pos:" + i);
                if (i > 2) {
                    NtRAngleAdjustAdapter.AppViewHolder appViewHolder = (NtRAngleAdjustAdapter.AppViewHolder) view.getTag();
                    if (NtRAngleSettingsFragment.this.needShowTipsDialog()) {
                        NtRAngleSettingsFragment.this.showTipsDialog(appViewHolder);
                        return;
                    }
                    Switch r0 = appViewHolder.rAngleAdustSwitch;
                    r0.setChecked(!r0.isChecked());
                }
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needShowTipsDialog() {
        return this.mNtRAngleAdjustController.needShowTipsDialog();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTipsDialog(final NtRAngleAdjustAdapter.AppViewHolder appViewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.r_angle_adjust_change_dlg_title);
        builder.setMessage(R.string.r_angle_adjust_change_dlg_summary);
        View inflate = View.inflate(getActivity(), R.layout.r_angle_dialog, null);
        final CheckBox checkBox = (CheckBox) inflate.findViewById(R.id.r_angle_state_tips_checkbox);
        builder.setView(inflate);
        builder.setPositiveButton(R.string.r_angle_adjust_change_btn_text, new DialogInterface.OnClickListener() { // from class: com.nt.settings.display.RAngleAdjust.NtRAngleSettingsFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (checkBox.isChecked()) {
                    NtRAngleSettingsFragment.this.mNtRAngleAdjustController.setShowTipsDialog(false);
                }
                Switch r0 = appViewHolder.rAngleAdustSwitch;
                r0.setChecked(!r0.isChecked());
            }
        });
        builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null);
        builder.create().show();
    }

    @Override // com.nt.settings.AppListBaseSettings
    public Loader<List<NtAppEntry>> onCreateAppLoader(int i, Bundle bundle) {
        return new NtRAngleAdjustAppLoader(getActivity(), new HashMap(), this.mLaunchNames, this.mSwitchOffOnSizeArray);
    }
}
