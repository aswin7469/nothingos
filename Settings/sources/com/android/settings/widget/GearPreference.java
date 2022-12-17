package com.android.settings.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settingslib.RestrictedPreference;

public class GearPreference extends RestrictedPreference implements View.OnClickListener {
    protected boolean mGearState = true;
    private OnGearClickListener mOnGearClickListener;

    public interface OnGearClickListener {
        void onGearClick(GearPreference gearPreference);
    }

    public GearPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setOnGearClickListener(OnGearClickListener onGearClickListener) {
        this.mOnGearClickListener = onGearClickListener;
        notifyChanged();
    }

    /* access modifiers changed from: protected */
    public int getSecondTargetResId() {
        return R$layout.preference_widget_gear;
    }

    /* access modifiers changed from: protected */
    public boolean shouldHideSecondTarget() {
        return this.mOnGearClickListener == null;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(R$id.settings_button);
        if (this.mOnGearClickListener != null) {
            findViewById.setVisibility(0);
            findViewById.setOnClickListener(this);
        } else {
            findViewById.setVisibility(8);
            findViewById.setOnClickListener((View.OnClickListener) null);
        }
        findViewById.setEnabled(this.mGearState);
    }

    public void onClick(View view) {
        OnGearClickListener onGearClickListener;
        if (view.getId() == R$id.settings_button && (onGearClickListener = this.mOnGearClickListener) != null) {
            onGearClickListener.onGearClick(this);
        }
    }
}
