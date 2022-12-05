package com.nt.settings.panel;

import android.content.Context;
import android.util.ArrayMap;
/* loaded from: classes2.dex */
public class SettingContentRegistry {
    private ArrayMap<String, SettingItemContent> mItemContents = new ArrayMap<>();

    public SettingItemContent getContentProvider(Context context, String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1314247385:
                if (str.equals("mobile_data")) {
                    c = 0;
                    break;
                }
                break;
            case 3649301:
                if (str.equals("wifi")) {
                    c = 1;
                    break;
                }
                break;
            case 309211811:
                if (str.equals("blue_tooth")) {
                    c = 2;
                    break;
                }
                break;
            case 1099007843:
                if (str.equals("hot_pot")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                SettingItemContent settingItemContent = this.mItemContents.get("mobile_data");
                if (settingItemContent != null) {
                    return settingItemContent;
                }
                MobileDataItem mobileDataItem = new MobileDataItem(context);
                this.mItemContents.put("mobile_data", mobileDataItem);
                return mobileDataItem;
            case 1:
                SettingItemContent settingItemContent2 = this.mItemContents.get("wifi");
                if (settingItemContent2 != null) {
                    return settingItemContent2;
                }
                WifiItem wifiItem = new WifiItem(context);
                this.mItemContents.put("wifi", wifiItem);
                return wifiItem;
            case 2:
                SettingItemContent settingItemContent3 = this.mItemContents.get("blue_tooth");
                if (settingItemContent3 != null) {
                    return settingItemContent3;
                }
                BluetoothItem bluetoothItem = new BluetoothItem(context);
                this.mItemContents.put("blue_tooth", bluetoothItem);
                return bluetoothItem;
            case 3:
                SettingItemContent settingItemContent4 = this.mItemContents.get("hot_pot");
                if (settingItemContent4 != null) {
                    return settingItemContent4;
                }
                HotPotItem hotPotItem = new HotPotItem(context);
                this.mItemContents.put("hot_pot", hotPotItem);
                return hotPotItem;
            default:
                return null;
        }
    }

    public void reset() {
        this.mItemContents.clear();
    }
}
