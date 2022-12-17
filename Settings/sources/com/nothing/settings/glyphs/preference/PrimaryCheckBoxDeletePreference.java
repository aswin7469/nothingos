package com.nothing.settings.glyphs.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;

public class PrimaryCheckBoxDeletePreference extends Preference {
    private boolean mChecked;
    private OnSelectedListener mListener;
    private RadioButton mRadioButton;
    private View mRightLayout;

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
        setLayoutResource(R$layout.preference_checkbox_delete);
        int secondTargetResId = getSecondTargetResId();
        if (secondTargetResId != 0) {
            setWidgetLayoutResource(secondTargetResId);
        }
    }

    /* access modifiers changed from: protected */
    public int getSecondTargetResId() {
        return R$layout.restricted_preference_widget_primary_delete;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(R$id.layout_left);
        if (findViewById != null) {
            findViewById.setOnClickListener(new PrimaryCheckBoxDeletePreference$$ExternalSyntheticLambda0(this));
        }
        View findViewById2 = preferenceViewHolder.findViewById(R$id.fl_delete);
        this.mRightLayout = preferenceViewHolder.findViewById(16908312);
        if (findViewById2 != null) {
            findViewById2.setOnClickListener(new PrimaryCheckBoxDeletePreference$$ExternalSyntheticLambda1(this));
        }
        RadioButton radioButton = (RadioButton) preferenceViewHolder.findViewById(R$id.radioBtn);
        this.mRadioButton = radioButton;
        if (radioButton != null) {
            radioButton.setContentDescription(getTitle());
            this.mRadioButton.setChecked(this.mChecked);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        RadioButton radioButton = this.mRadioButton;
        if (radioButton == null || !radioButton.isChecked()) {
            setChecked(true);
            OnSelectedListener onSelectedListener = this.mListener;
            if (onSelectedListener != null) {
                onSelectedListener.onCheckChange(this, this.mRadioButton.isChecked());
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$1(View view) {
        OnSelectedListener onSelectedListener = this.mListener;
        if (onSelectedListener != null) {
            onSelectedListener.onClickDelete(this);
        }
    }

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
