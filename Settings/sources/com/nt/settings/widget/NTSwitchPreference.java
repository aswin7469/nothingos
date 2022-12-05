package com.nt.settings.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R$attr;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class NTSwitchPreference extends SwitchPreference {
    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
    }

    public NTSwitchPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet, i, i2);
    }

    public NTSwitchPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i, 0);
    }

    public NTSwitchPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, R$attr.switchPreferenceStyle, 16843629);
    }

    @Override // androidx.preference.SwitchPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.itemView.setBackgroundResource(R.drawable.bg_switch);
        ((TextView) preferenceViewHolder.itemView.findViewById(16908310)).setTextColor(-1);
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) preferenceViewHolder.itemView.getLayoutParams();
        int dimensionPixelOffset = preferenceViewHolder.itemView.getContext().getResources().getDimensionPixelOffset(R.dimen.nt_preference_horizontal_margin);
        layoutParams.setMargins(dimensionPixelOffset, 0, dimensionPixelOffset, 0);
        preferenceViewHolder.itemView.setLayoutParams(layoutParams);
    }
}
