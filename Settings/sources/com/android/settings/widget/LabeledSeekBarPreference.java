package com.android.settings.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R$attr;
import com.android.settings.R;
import com.android.settings.R$styleable;
/* loaded from: classes.dex */
public class LabeledSeekBarPreference extends SeekBarPreference {
    private Preference.OnPreferenceChangeListener mStopListener;
    private CharSequence mSummary;
    private final int mTextEndId;
    private final int mTextStartId;
    private final int mTickMarkId;

    public LabeledSeekBarPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setLayoutResource(R.layout.preference_labeled_slider);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.LabeledSeekBarPreference);
        int i3 = R$styleable.LabeledSeekBarPreference_textStart;
        int i4 = R.string.summary_placeholder;
        this.mTextStartId = obtainStyledAttributes.getResourceId(i3, i4);
        this.mTextEndId = obtainStyledAttributes.getResourceId(R$styleable.LabeledSeekBarPreference_textEnd, i4);
        this.mTickMarkId = obtainStyledAttributes.getResourceId(R$styleable.LabeledSeekBarPreference_tickMark, 0);
        this.mSummary = obtainStyledAttributes.getText(R$styleable.Preference_android_summary);
        obtainStyledAttributes.recycle();
    }

    public LabeledSeekBarPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R$attr.seekBarPreferenceStyle, 17957080), 0);
    }

    @Override // com.android.settings.widget.SeekBarPreference, com.android.settingslib.RestrictedPreference, com.android.settingslib.widget.TwoTargetPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ((TextView) preferenceViewHolder.findViewById(16908308)).setText(this.mTextStartId);
        ((TextView) preferenceViewHolder.findViewById(16908309)).setText(this.mTextEndId);
        if (this.mTickMarkId != 0) {
            ((SeekBar) preferenceViewHolder.findViewById(16909419)).setTickMark(getContext().getDrawable(this.mTickMarkId));
        }
        TextView textView = (TextView) preferenceViewHolder.findViewById(16908304);
        CharSequence charSequence = this.mSummary;
        if (charSequence != null) {
            textView.setText(charSequence);
            textView.setVisibility(0);
            return;
        }
        textView.setText((CharSequence) null);
        textView.setVisibility(8);
    }

    public void setOnPreferenceChangeStopListener(Preference.OnPreferenceChangeListener onPreferenceChangeListener) {
        this.mStopListener = onPreferenceChangeListener;
    }

    @Override // com.android.settings.widget.SeekBarPreference, android.widget.SeekBar.OnSeekBarChangeListener
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        Preference.OnPreferenceChangeListener onPreferenceChangeListener = this.mStopListener;
        if (onPreferenceChangeListener != null) {
            onPreferenceChangeListener.onPreferenceChange(this, Integer.valueOf(seekBar.getProgress()));
        }
    }

    @Override // androidx.preference.Preference
    public void setSummary(CharSequence charSequence) {
        super.setSummary(charSequence);
        this.mSummary = charSequence;
        notifyChanged();
    }

    @Override // androidx.preference.Preference
    public void setSummary(int i) {
        super.setSummary(i);
        this.mSummary = getContext().getText(i);
        notifyChanged();
    }

    @Override // com.android.settings.widget.SeekBarPreference, androidx.preference.Preference
    public CharSequence getSummary() {
        return this.mSummary;
    }
}
