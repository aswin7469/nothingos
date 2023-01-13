package com.nothing.systemui.p024qs.tiles.settings.panel;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CompoundButton;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.SettingItemData */
public class SettingItemData {
    public View.OnClickListener actionClickListener;
    public Drawable actionDrawable;
    public CachedBluetoothDevice cachedDevice;
    public boolean canForward;
    public View.OnClickListener contentClickListener;
    public boolean hasToggle;
    public boolean isChecked;
    public boolean isConnected;
    public boolean isForceUpdate;
    public boolean isNothingEarDevice;
    public boolean isSwithEnabled;
    public String macAddress;
    public String subTitle;
    public boolean supportAirpods;
    public boolean supportAnc;
    public CompoundButton.OnCheckedChangeListener switchListener;
    public String title;
    public Drawable titleDrawable;
}
