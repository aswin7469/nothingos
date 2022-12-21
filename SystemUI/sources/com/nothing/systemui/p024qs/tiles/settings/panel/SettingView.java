package com.nothing.systemui.p024qs.tiles.settings.panel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSFragmentEx;
import com.nothing.systemui.p024qs.tiles.BluetoothTileEx;
import com.nothing.systemui.p024qs.tiles.settings.tesla.TeslaConnectPanelPlugin;
import com.nothing.tesla.service.CmdObjectList;
import java.util.List;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.SettingView */
public class SettingView extends LinearLayout {
    private SettingItemAdapter mAdapter;
    private BluetoothTileEx mBluetoothTileEx = ((BluetoothTileEx) NTDependencyEx.get(BluetoothTileEx.class));
    private SettingContentRegistry mContentRegistry;
    private List<SettingItemData> mData;
    private BluetoothPanelDialog mDialog;
    private SettingItemLiveData mLiveData;
    private QSFragmentEx mQSEx = ((QSFragmentEx) NTDependencyEx.get(QSFragmentEx.class));
    private RecyclerView mRecyclerView;
    private CmdObjectList mTeslaDataList;
    private String mType;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public SettingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        RecyclerView recyclerView = new RecyclerView(context, attributeSet);
        this.mRecyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SettingItemAdapter settingItemAdapter = new SettingItemAdapter(context);
        this.mAdapter = settingItemAdapter;
        settingItemAdapter.setNothingAppHasPermission(this.mBluetoothTileEx.isNothingAppHasPermission("com.nothing.smartcenter") && this.mBluetoothTileEx.isNothingAppEnabled("com.nothing.smartcenter"));
        this.mRecyclerView.setAdapter(this.mAdapter);
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
            SettingItemContent contentProvider = settingContentRegistry.getContentProvider(getContext(), this.mType, this.mDialog);
            contentProvider.loadData();
            this.mAdapter.setLiveData(this.mQSEx.getQSFragment(), contentProvider.getLiveDates());
            this.mAdapter.setPinnedLiveData(this.mQSEx.getQSFragment(), contentProvider.getPinnedHeaderLiveDates());
            this.mBluetoothTileEx.setAncCallback(this.mAdapter.getAncCallback());
        }
    }

    public void setType(String str, BluetoothPanelDialog bluetoothPanelDialog) {
        this.mType = str;
        this.mDialog = bluetoothPanelDialog;
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
