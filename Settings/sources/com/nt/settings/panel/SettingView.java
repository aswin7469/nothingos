package com.nt.settings.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nothing.tesla.service.CmdObjectList;
import java.util.List;
/* loaded from: classes2.dex */
public class SettingView extends LinearLayout {
    private SettingItemAdapter mAdapter;
    private SettingContentRegistry mContentRegistry;
    private List<SettingItemData> mData;
    private SettingItemLiveData mLiveData;
    private RecyclerView mRecyclerView;
    private CmdObjectList mTeslaDataList;
    private String mType;

    public SettingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        RecyclerView recyclerView = new RecyclerView(context, attributeSet);
        this.mRecyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SettingItemAdapter settingItemAdapter = new SettingItemAdapter();
        this.mAdapter = settingItemAdapter;
        this.mRecyclerView.setAdapter(settingItemAdapter);
        addView(this.mRecyclerView);
    }

    public void setPinnedHeader(View view) {
        this.mAdapter.setPinnedHeader(view);
    }

    public void setData(List<SettingItemData> list) {
        this.mData = list;
        this.mLiveData.setDataChange(list);
    }

    public void setContentRegistry(SettingContentRegistry settingContentRegistry) {
        this.mContentRegistry = settingContentRegistry;
    }

    public void setLiveData() {
        SettingContentRegistry settingContentRegistry = this.mContentRegistry;
        if (settingContentRegistry != null) {
            SettingItemContent contentProvider = settingContentRegistry.getContentProvider(getContext(), this.mType);
            contentProvider.loadData();
            this.mAdapter.setLiveData(getContext(), contentProvider.getLiveDates());
            this.mAdapter.setPinnedLiveData((NtSettingsPanelActivity) getContext(), contentProvider.getPinnedHeaderLiveDates());
        }
    }

    public void setType(String str) {
        this.mType = str;
        setLiveData();
    }

    public void setTeslaView(View view, boolean z) {
        this.mAdapter.setTeslaView(view, z);
    }

    public void setupTeslaPlugin(TeslaConnectPanelPlugin teslaConnectPanelPlugin) {
        this.mAdapter.setupTeslaPlugin(teslaConnectPanelPlugin);
    }

    public void updateTeslaInfo(CmdObjectList cmdObjectList) {
        this.mTeslaDataList = cmdObjectList;
        this.mAdapter.bindTeslaData(cmdObjectList);
    }
}
