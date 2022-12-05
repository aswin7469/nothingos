package com.nt.settings.panel;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.nothing.tesla.service.CmdObjectList;
import java.util.List;
/* loaded from: classes2.dex */
public class SettingsListAdapter extends RecyclerView.Adapter<SettingRowViewHolder> {
    private SettingContentRegistry mContentRegistry;
    private final List<String> mSettingListsData;
    private CmdObjectList mTeslaDataList;
    private TeslaConnectPanelPlugin mTeslaPlugin;
    private boolean showTeslaView = false;

    public SettingsListAdapter(List<String> list) {
        this.mSettingListsData = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public SettingRowViewHolder mo960onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new SettingRowViewHolder(viewGroup);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(SettingRowViewHolder settingRowViewHolder, int i) {
        settingRowViewHolder.mSettingView.setContentRegistry(this.mContentRegistry);
        settingRowViewHolder.mSettingView.setType(this.mSettingListsData.get(i));
        settingRowViewHolder.bindTesla(this.mTeslaDataList);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mSettingListsData.size();
    }

    public void setContentRegistry(SettingContentRegistry settingContentRegistry) {
        this.mContentRegistry = settingContentRegistry;
    }

    /* loaded from: classes2.dex */
    public class SettingRowViewHolder extends RecyclerView.ViewHolder {
        private SettingView mSettingView;

        public SettingRowViewHolder(ViewGroup viewGroup) {
            super(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.nt_panel_setting_row, viewGroup, false));
            SettingView settingView = (SettingView) this.itemView.findViewById(R.id.setting_view);
            this.mSettingView = settingView;
            settingView.setTeslaView(this.itemView.findViewById(R.id.pinned_tesla_header), SettingsListAdapter.this.showTeslaView);
            this.mSettingView.setupTeslaPlugin(SettingsListAdapter.this.mTeslaPlugin);
            this.mSettingView.setPinnedHeader(this.itemView.findViewById(R.id.pinned_header));
        }

        public void bindTesla(CmdObjectList cmdObjectList) {
            this.mSettingView.updateTeslaInfo(cmdObjectList);
        }
    }

    public void initialTeslaView(boolean z) {
        this.showTeslaView = z;
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
