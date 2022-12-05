package com.nt.settings.panel;

import android.content.Context;
import androidx.core.graphics.drawable.IconCompat;
import com.android.settings.R;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class BluetoothPanel extends SettingsLifecycleObserver implements NtPanelContent {
    private static List<String> SETTINGS;
    private SettingContentRegistry mContentRegistry;
    private Context mContext;

    @Override // com.nt.settings.panel.NtPanelContent
    public IconCompat getIcon() {
        return null;
    }

    @Override // com.nt.settings.panel.NtPanelContent
    public CharSequence getSubTitle() {
        return "Control your all devices";
    }

    static {
        ArrayList arrayList = new ArrayList();
        SETTINGS = arrayList;
        arrayList.add("blue_tooth");
    }

    public BluetoothPanel(Context context, SettingContentRegistry settingContentRegistry) {
        this.mContext = context;
        this.mContentRegistry = settingContentRegistry;
    }

    @Override // com.nt.settings.panel.NtPanelContent
    public List<String> getLists() {
        return SETTINGS;
    }

    @Override // com.nt.settings.panel.NtPanelContent
    public CharSequence getTitle() {
        return this.mContext.getText(R.string.bluetooth_device_context_connect);
    }

    @Override // com.nt.settings.panel.SettingsLifecycleObserver
    public void onStart() {
        for (String str : SETTINGS) {
            this.mContentRegistry.getContentProvider(this.mContext, str).onStart();
        }
    }

    @Override // com.nt.settings.panel.SettingsLifecycleObserver
    public void onResume() {
        for (String str : SETTINGS) {
            this.mContentRegistry.getContentProvider(this.mContext, str).onResume();
        }
    }

    @Override // com.nt.settings.panel.SettingsLifecycleObserver
    public void onPause() {
        for (String str : SETTINGS) {
            this.mContentRegistry.getContentProvider(this.mContext, str).onPause();
        }
    }

    @Override // com.nt.settings.panel.SettingsLifecycleObserver
    public void onStop() {
        for (String str : SETTINGS) {
            this.mContentRegistry.getContentProvider(this.mContext, str).onStop();
        }
    }

    @Override // com.nt.settings.panel.NtPanelContent
    public boolean isProgressBarVisible() {
        return ((BluetoothItem) this.mContentRegistry.getContentProvider(this.mContext, SETTINGS.get(0))).isProgressBarVisible();
    }
}
