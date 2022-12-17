package com.android.settings.datausage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.datausage.CycleAdapter;

public class SpinnerPreference extends Preference implements CycleAdapter.SpinnerInterface {
    /* access modifiers changed from: private */
    public CycleAdapter mAdapter;
    /* access modifiers changed from: private */
    public Object mCurrentObject;
    private View mItemView;
    private boolean mItemViewVisible = false;
    /* access modifiers changed from: private */
    public AdapterView.OnItemSelectedListener mListener;
    private final AdapterView.OnItemSelectedListener mOnSelectedListener = new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (SpinnerPreference.this.mPosition != i) {
                SpinnerPreference.this.mPosition = i;
                SpinnerPreference spinnerPreference = SpinnerPreference.this;
                spinnerPreference.mCurrentObject = spinnerPreference.mAdapter.getItem(i);
                if (SpinnerPreference.this.mListener != null) {
                    SpinnerPreference.this.mListener.onItemSelected(adapterView, view, i, j);
                }
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            if (SpinnerPreference.this.mListener != null) {
                SpinnerPreference.this.mListener.onNothingSelected(adapterView);
            }
        }
    };
    /* access modifiers changed from: private */
    public int mPosition;

    public SpinnerPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R$layout.data_usage_cycles);
    }

    public void setAdapter(CycleAdapter cycleAdapter) {
        this.mAdapter = cycleAdapter;
        notifyChanged();
    }

    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.mListener = onItemSelectedListener;
    }

    public Object getSelectedItem() {
        return this.mCurrentObject;
    }

    public void setSelection(int i) {
        this.mPosition = i;
        this.mCurrentObject = this.mAdapter.getItem(i);
        notifyChanged();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        this.mItemView = view;
        view.setVisibility(this.mItemViewVisible ? 0 : 4);
        Spinner spinner = (Spinner) preferenceViewHolder.findViewById(R$id.cycles_spinner);
        spinner.setAdapter(this.mAdapter);
        spinner.setSelection(this.mPosition);
        spinner.setOnItemSelectedListener(this.mOnSelectedListener);
    }

    /* access modifiers changed from: package-private */
    public void setHasCycles(boolean z) {
        setVisible(z);
        if (z) {
            this.mItemViewVisible = true;
            View view = this.mItemView;
            if (view != null) {
                view.setVisibility(0);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void performClick(View view) {
        view.findViewById(R$id.cycles_spinner).performClick();
    }
}
