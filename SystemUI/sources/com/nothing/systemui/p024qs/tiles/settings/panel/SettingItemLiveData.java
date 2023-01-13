package com.nothing.systemui.p024qs.tiles.settings.panel;

import androidx.lifecycle.LiveData;
import com.android.settingslib.utils.ThreadUtils;
import java.util.List;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.SettingItemLiveData */
public class SettingItemLiveData extends LiveData<List<SettingItemData>> {
    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setDataChange$0$com-nothing-systemui-qs-tiles-settings-panel-SettingItemLiveData */
    public /* synthetic */ void mo57967x80eaff34(List list) {
        setValue(list);
    }

    public void setDataChange(List<SettingItemData> list) {
        ThreadUtils.postOnMainThread(new SettingItemLiveData$$ExternalSyntheticLambda0(this, list));
    }
}
