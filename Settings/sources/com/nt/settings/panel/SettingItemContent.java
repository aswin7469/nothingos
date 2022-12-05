package com.nt.settings.panel;

import java.util.List;
/* loaded from: classes2.dex */
public interface SettingItemContent {
    List<SettingItemData> getDates();

    SettingItemLiveData getLiveDates();

    default SettingItemLiveData getPinnedHeaderLiveDates() {
        return null;
    }

    void loadData();

    default void onCreate() {
    }

    default void onDestroy() {
    }

    default void onPause() {
    }

    default void onResume() {
    }

    default void onStart() {
    }

    default void onStop() {
    }
}
