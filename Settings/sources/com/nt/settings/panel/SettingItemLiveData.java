package com.nt.settings.panel;

import androidx.lifecycle.LiveData;
import java.util.List;
/* loaded from: classes2.dex */
public class SettingItemLiveData extends LiveData<List<SettingItemData>> {
    public void setDataChange(List<SettingItemData> list) {
        setValue(list);
    }
}
