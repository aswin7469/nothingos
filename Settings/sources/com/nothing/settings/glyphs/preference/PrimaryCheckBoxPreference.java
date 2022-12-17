package com.nothing.settings.glyphs.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;

public class PrimaryCheckBoxPreference extends Preference {
    private boolean mChecked;
    private OnSelectedListener mListener;
    private RadioButton mRadioButton;

    public interface OnSelectedListener {
        void onCheckChange(Preference preference, boolean z);
    }

    public PrimaryCheckBoxPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R$layout.preference_checkbox);
    }

    public PrimaryCheckBoxPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R$layout.preference_checkbox);
    }

    public PrimaryCheckBoxPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R$layout.preference_checkbox);
    }

    public PrimaryCheckBoxPreference(Context context) {
        super(context);
        setLayoutResource(R$layout.preference_checkbox);
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

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.itemView.setOnClickListener(new PrimaryCheckBoxPreference$$ExternalSyntheticLambda0(this));
        RadioButton radioButton = (RadioButton) preferenceViewHolder.findViewById(R$id.radioBtn);
        this.mRadioButton = radioButton;
        if (radioButton != null) {
            radioButton.setContentDescription(getTitle());
            this.mRadioButton.setChecked(this.mChecked);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        this.mChecked = true;
        this.mRadioButton.setChecked(true);
        OnSelectedListener onSelectedListener = this.mListener;
        if (onSelectedListener != null) {
            onSelectedListener.onCheckChange(this, true);
        }
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mListener = onSelectedListener;
    }
}
