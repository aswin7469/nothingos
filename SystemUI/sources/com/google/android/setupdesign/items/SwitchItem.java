package com.google.android.setupdesign.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.setupdesign.C3953R;

public class SwitchItem extends Item implements CompoundButton.OnCheckedChangeListener {
    private boolean checked = false;
    private OnCheckedChangeListener listener;

    public interface OnCheckedChangeListener {
        void onCheckedChange(SwitchItem switchItem, boolean z);
    }

    public SwitchItem() {
    }

    public SwitchItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C3953R.styleable.SudSwitchItem);
        this.checked = obtainStyledAttributes.getBoolean(C3953R.styleable.SudSwitchItem_android_checked, false);
        obtainStyledAttributes.recycle();
    }

    public void setChecked(boolean z) {
        if (this.checked != z) {
            this.checked = z;
            notifyItemChanged();
            OnCheckedChangeListener onCheckedChangeListener = this.listener;
            if (onCheckedChangeListener != null) {
                onCheckedChangeListener.onCheckedChange(this, z);
            }
        }
    }

    public boolean isChecked() {
        return this.checked;
    }

    /* access modifiers changed from: protected */
    public int getDefaultLayoutResource() {
        return C3953R.layout.sud_items_switch;
    }

    public void toggle(View view) {
        this.checked = !this.checked;
        ((SwitchCompat) view.findViewById(C3953R.C3956id.sud_items_switch)).setChecked(this.checked);
    }

    public void onBindView(View view) {
        super.onBindView(view);
        SwitchCompat switchCompat = (SwitchCompat) view.findViewById(C3953R.C3956id.sud_items_switch);
        switchCompat.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        switchCompat.setChecked(this.checked);
        switchCompat.setOnCheckedChangeListener(this);
        switchCompat.setEnabled(isEnabled());
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.listener = onCheckedChangeListener;
    }

    public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        this.checked = z;
        OnCheckedChangeListener onCheckedChangeListener = this.listener;
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChange(this, z);
        }
    }
}
