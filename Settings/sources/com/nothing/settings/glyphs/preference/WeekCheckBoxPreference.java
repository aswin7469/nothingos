package com.nothing.settings.glyphs.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.nothing.settings.glyphs.widget.WeekCheckBox;

public class WeekCheckBoxPreference extends Preference {
    private OnSelectedChangeListener listener;
    private int[] weekSelectedStatus = new int[7];

    public interface OnSelectedChangeListener {
        void onSelectedChange(int[] iArr);
    }

    public WeekCheckBoxPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public WeekCheckBoxPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public WeekCheckBoxPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0, 0);
        init(context);
    }

    public WeekCheckBoxPreference(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setLayoutResource(R$layout.preference_week_check_box);
        setSelectable(false);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        WeekCheckBox weekCheckBox = (WeekCheckBox) preferenceViewHolder.findViewById(R$id.wcb_weeks);
        weekCheckBox.setSelectStatus(this.weekSelectedStatus);
        weekCheckBox.setOnCheckChangeListener(new WeekCheckBoxPreference$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view, int i, boolean z) {
        int[] iArr = this.weekSelectedStatus;
        iArr[i] = z;
        OnSelectedChangeListener onSelectedChangeListener = this.listener;
        if (onSelectedChangeListener != null) {
            onSelectedChangeListener.onSelectedChange(iArr);
        }
    }

    public void setCheckedStatus(int[] iArr) {
        this.weekSelectedStatus = iArr;
        notifyChanged();
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener onSelectedChangeListener) {
        this.listener = onSelectedChangeListener;
    }
}
