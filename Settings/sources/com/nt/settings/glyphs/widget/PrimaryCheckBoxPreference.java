package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class PrimaryCheckBoxPreference extends Preference {
    private boolean mChecked;
    private OnSelectedListener mListener;
    private RadioButton mRadioButton;

    /* loaded from: classes2.dex */
    public interface OnSelectedListener {
        void onCheckChange(Preference preference, boolean z);
    }

    public PrimaryCheckBoxPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.preference_checkbox);
    }

    public PrimaryCheckBoxPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R.layout.preference_checkbox);
    }

    public PrimaryCheckBoxPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R.layout.preference_checkbox);
    }

    public PrimaryCheckBoxPreference(Context context) {
        super(context);
        setLayoutResource(R.layout.preference_checkbox);
    }

    public void setChecked(boolean z) {
        this.mChecked = z;
        RadioButton radioButton = this.mRadioButton;
        if (radioButton != null) {
            radioButton.setChecked(z);
        } else {
            notifyChanged();
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.glyphs.widget.PrimaryCheckBoxPreference.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                PrimaryCheckBoxPreference.this.mChecked = true;
                PrimaryCheckBoxPreference.this.mRadioButton.setChecked(true);
                if (PrimaryCheckBoxPreference.this.mListener != null) {
                    PrimaryCheckBoxPreference.this.mListener.onCheckChange(PrimaryCheckBoxPreference.this, true);
                }
            }
        });
        RadioButton radioButton = (RadioButton) preferenceViewHolder.findViewById(R.id.radioBtn);
        this.mRadioButton = radioButton;
        if (radioButton != null) {
            radioButton.setContentDescription(getTitle());
            this.mRadioButton.setChecked(this.mChecked);
        }
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mListener = onSelectedListener;
    }
}
