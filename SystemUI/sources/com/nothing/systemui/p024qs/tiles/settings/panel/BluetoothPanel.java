package com.nothing.systemui.p024qs.tiles.settings.panel;

import android.content.Context;
import androidx.core.graphics.drawable.IconCompat;
import com.android.systemui.C1893R;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.BluetoothPanel */
public class BluetoothPanel extends SettingsLifecycleObserver implements PanelContent {
    private static List<String> SETTINGS;
    private SettingContentRegistry mContentRegistry;
    private Context mContext;
    private BluetoothPanelDialog mDialog;

    public IconCompat getIcon() {
        return null;
    }

    public CharSequence getSubTitle() {
        return "Control your all devices";
    }

    static {
        ArrayList arrayList = new ArrayList();
        SETTINGS = arrayList;
        arrayList.add(SettingContentRegistry.BLUETOOTH_ITEM);
    }

    public BluetoothPanel(Context context, SettingContentRegistry settingContentRegistry, BluetoothPanelDialog bluetoothPanelDialog) {
        this.mDialog = bluetoothPanelDialog;
        this.mContext = context;
        this.mContentRegistry = settingContentRegistry;
    }

    public List<String> getLists() {
        return SETTINGS;
    }

    public CharSequence getTitle() {
        return this.mContext.getText(C1893R.string.bluetooth_device_context_connect);
    }

    public void onStart() {
        for (String contentProvider : SETTINGS) {
            this.mContentRegistry.getContentProvider(this.mContext, contentProvider, this.mDialog).onStart();
        }
    }

    public void onResume() {
        for (String contentProvider : SETTINGS) {
            this.mContentRegistry.getContentProvider(this.mContext, contentProvider, this.mDialog).onResume();
        }
    }

    public void onPause() {
        for (String contentProvider : SETTINGS) {
            this.mContentRegistry.getContentProvider(this.mContext, contentProvider, this.mDialog).onPause();
        }
    }

    public void onStop() {
        for (String contentProvider : SETTINGS) {
            this.mContentRegistry.getContentProvider(this.mContext, contentProvider, this.mDialog).onStop();
        }
    }

    public boolean isProgressBarVisible() {
        return ((BluetoothItem) this.mContentRegistry.getContentProvider(this.mContext, SETTINGS.get(0), this.mDialog)).isProgressBarVisible();
    }
}
