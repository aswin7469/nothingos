package com.nothing.systemui.p024qs.tiles.settings.panel;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.C1894R;
import com.nothing.systemui.p024qs.tiles.settings.tesla.TeslaConnectPanelPlugin;
import com.nothing.tesla.service.CmdObjectList;
import java.util.List;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.SettingsListAdapter */
public class SettingsListAdapter extends RecyclerView.Adapter<SettingRowViewHolder> {
    private SettingContentRegistry mContentRegistry;
    private BluetoothPanelDialog mDialog;
    /* access modifiers changed from: private */
    public boolean mIsTeslaViewShowing;
    private final List<String> mSettingListsData;
    private CmdObjectList mTeslaDataList;
    /* access modifiers changed from: private */
    public TeslaConnectPanelPlugin mTeslaPlugin;

    public SettingsListAdapter(List<String> list, BluetoothPanelDialog bluetoothPanelDialog) {
        this.mDialog = bluetoothPanelDialog;
        this.mSettingListsData = list;
    }

    public SettingRowViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SettingRowViewHolder(viewGroup);
    }

    public void onBindViewHolder(SettingRowViewHolder settingRowViewHolder, int i) {
        settingRowViewHolder.mSettingView.setContentRegistry(this.mContentRegistry);
        settingRowViewHolder.mSettingView.setType(this.mSettingListsData.get(i), this.mDialog);
        settingRowViewHolder.bindTesla(this.mTeslaDataList);
    }

    public int getItemCount() {
        return this.mSettingListsData.size();
    }

    public void setContentRegistry(SettingContentRegistry settingContentRegistry) {
        this.mContentRegistry = settingContentRegistry;
    }

    /* renamed from: com.nothing.systemui.qs.tiles.settings.panel.SettingsListAdapter$SettingRowViewHolder */
    public class SettingRowViewHolder extends RecyclerView.ViewHolder {
        /* access modifiers changed from: private */
        public SettingView mSettingView;

        public SettingRowViewHolder(ViewGroup viewGroup) {
            super(LayoutInflater.from(viewGroup.getContext()).inflate(C1894R.layout.nt_panel_setting_row, viewGroup, false));
            SettingView settingView = (SettingView) this.itemView.findViewById(C1894R.C1898id.setting_view);
            this.mSettingView = settingView;
            settingView.setTeslaView(this.itemView.findViewById(C1894R.C1898id.pinned_tesla_header), SettingsListAdapter.this.mIsTeslaViewShowing);
            this.mSettingView.setPinnedHeader(this.itemView.findViewById(C1894R.C1898id.pinned_header));
            this.mSettingView.setupTeslaPlugin(SettingsListAdapter.this.mTeslaPlugin);
        }

        public void bindTesla(CmdObjectList cmdObjectList) {
            this.mSettingView.updateTeslaInfo(cmdObjectList);
        }
    }

    public void initialTeslaView(boolean z) {
        this.mIsTeslaViewShowing = z;
        notifyDataSetChanged();
    }

    public void setupTeslaPlugin(TeslaConnectPanelPlugin teslaConnectPanelPlugin) {
        this.mTeslaPlugin = teslaConnectPanelPlugin;
    }

    public void updateTeslaInfo(CmdObjectList cmdObjectList) {
        this.mTeslaDataList = cmdObjectList;
        notifyDataSetChanged();
    }

    public void updateTeslaItemStatus(CmdObjectList cmdObjectList) {
        this.mTeslaDataList = cmdObjectList;
        notifyDataSetChanged();
    }
}
