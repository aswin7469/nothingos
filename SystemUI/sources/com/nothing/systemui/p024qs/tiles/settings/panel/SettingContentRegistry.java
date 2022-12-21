package com.nothing.systemui.p024qs.tiles.settings.panel;

import android.content.Context;
import android.util.ArrayMap;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.SettingContentRegistry */
public class SettingContentRegistry {
    public static final String BLUETOOTH_ITEM = "bluetooth";
    private ArrayMap<String, SettingItemContent> mItemContents = new ArrayMap<>();

    public SettingItemContent getContentProvider(Context context, String str, BluetoothPanelDialog bluetoothPanelDialog) {
        str.hashCode();
        if (!str.equals(BLUETOOTH_ITEM)) {
            return null;
        }
        SettingItemContent settingItemContent = this.mItemContents.get(BLUETOOTH_ITEM);
        if (settingItemContent != null) {
            return settingItemContent;
        }
        BluetoothItem bluetoothItem = new BluetoothItem(context, bluetoothPanelDialog);
        this.mItemContents.put(BLUETOOTH_ITEM, bluetoothItem);
        return bluetoothItem;
    }

    public void reset() {
        this.mItemContents.clear();
    }
}
