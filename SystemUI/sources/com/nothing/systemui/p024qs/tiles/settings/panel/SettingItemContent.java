package com.nothing.systemui.p024qs.tiles.settings.panel;

import java.util.List;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.SettingItemContent */
public interface SettingItemContent {
    List<SettingItemData> getDates();

    SettingItemLiveData getLiveDates();

    SettingItemLiveData getPinnedHeaderLiveDates() {
        return null;
    }

    void loadData();

    void onCreate() {
    }

    void onDestroy() {
    }

    void onPause() {
    }

    void onResume() {
    }

    void onStart() {
    }

    void onStop() {
    }
}
