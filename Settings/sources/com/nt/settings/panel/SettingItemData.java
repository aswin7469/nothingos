package com.nt.settings.panel;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.CompoundButton;
/* loaded from: classes2.dex */
public class SettingItemData {
    public View.OnClickListener actionClickListener;
    public Drawable actionDrawable;
    public boolean canForward;
    public View.OnClickListener contentClickListener;
    public boolean hasToggle;
    public boolean isChecked;
    public boolean isForceUpdate;
    public String subTitle;
    public CompoundButton.OnCheckedChangeListener switchListener;
    public String title;
    public Drawable titleDrawable;
}
