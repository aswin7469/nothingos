package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
/* loaded from: classes2.dex */
public class PrimaryCheckBoxDeletePreference extends Preference {
    private boolean mChecked;
    private OnSelectedListener mListener;
    private RadioButton mRadioButton;
    private View mRightLayout;

    /* loaded from: classes2.dex */
    public interface OnSelectedListener {
        void onCheckChange(Preference preference, boolean z);

        void onClickDelete(Preference preference);
    }

    public PrimaryCheckBoxDeletePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public PrimaryCheckBoxDeletePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public PrimaryCheckBoxDeletePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public PrimaryCheckBoxDeletePreference(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setLayoutResource(R.layout.preference_checkbox_delete);
        int secondTargetResId = getSecondTargetResId();
        if (secondTargetResId != 0) {
            setWidgetLayoutResource(secondTargetResId);
        }
    }

    protected int getSecondTargetResId() {
        return R.layout.restricted_preference_widget_primary_delete;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(R.id.layout_left);
        if (findViewById != null) {
            findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.glyphs.widget.PrimaryCheckBoxDeletePreference.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (PrimaryCheckBoxDeletePreference.this.mRadioButton == null || !PrimaryCheckBoxDeletePreference.this.mRadioButton.isChecked()) {
                        PrimaryCheckBoxDeletePreference.this.setChecked(true);
                        if (PrimaryCheckBoxDeletePreference.this.mListener == null) {
                            return;
                        }
                        OnSelectedListener onSelectedListener = PrimaryCheckBoxDeletePreference.this.mListener;
                        PrimaryCheckBoxDeletePreference primaryCheckBoxDeletePreference = PrimaryCheckBoxDeletePreference.this;
                        onSelectedListener.onCheckChange(primaryCheckBoxDeletePreference, primaryCheckBoxDeletePreference.mRadioButton.isChecked());
                    }
                }
            });
        }
        View findViewById2 = preferenceViewHolder.findViewById(R.id.fl_delete);
        this.mRightLayout = preferenceViewHolder.findViewById(16908312);
        if (findViewById2 != null) {
            findViewById2.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.glyphs.widget.PrimaryCheckBoxDeletePreference.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (PrimaryCheckBoxDeletePreference.this.mListener != null) {
                        PrimaryCheckBoxDeletePreference.this.mListener.onClickDelete(PrimaryCheckBoxDeletePreference.this);
                    }
                }
            });
        }
        RadioButton radioButton = (RadioButton) preferenceViewHolder.findViewById(R.id.radioBtn);
        this.mRadioButton = radioButton;
        if (radioButton != null) {
            radioButton.setContentDescription(getTitle());
            this.mRadioButton.setChecked(this.mChecked);
        }
    }

    @Override // androidx.preference.Preference
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        setCheckBoxEnabled(z);
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

    public void deleteEnable(boolean z) {
        View view = this.mRightLayout;
        if (view != null) {
            view.setVisibility(z ? 0 : 8);
        }
        notifyChanged();
    }

    public void setCheckBoxEnabled(boolean z) {
        RadioButton radioButton = this.mRadioButton;
        if (radioButton != null) {
            radioButton.setEnabled(z);
        }
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mListener = onSelectedListener;
    }
}
