package com.android.settingslib.widget;

import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceDialogFragmentCompat;
import com.android.internal.R;
import com.android.settingslib.core.instrumentation.Instrumentable;
import java.util.ArrayList;

public class UpdatableListPreferenceDialogFragment extends PreferenceDialogFragmentCompat implements Instrumentable {
    private static final String METRICS_CATEGORY_KEY = "metrics_category_key";
    private static final String SAVE_STATE_ENTRIES = "UpdatableListPreferenceDialogFragment.entries";
    private static final String SAVE_STATE_ENTRY_VALUES = "UpdatableListPreferenceDialogFragment.entryValues";
    private static final String SAVE_STATE_INDEX = "UpdatableListPreferenceDialogFragment.index";
    private ArrayAdapter mAdapter;
    private int mClickedDialogEntryIndex;
    private ArrayList<CharSequence> mEntries;
    private CharSequence[] mEntryValues;
    private int mMetricsCategory = 0;

    public static UpdatableListPreferenceDialogFragment newInstance(String str, int i) {
        UpdatableListPreferenceDialogFragment updatableListPreferenceDialogFragment = new UpdatableListPreferenceDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", str);
        bundle.putInt(METRICS_CATEGORY_KEY, i);
        updatableListPreferenceDialogFragment.setArguments(bundle);
        return updatableListPreferenceDialogFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mMetricsCategory = getArguments().getInt(METRICS_CATEGORY_KEY, 0);
        if (bundle == null) {
            this.mEntries = new ArrayList<>();
            setPreferenceData(getListPreference());
            return;
        }
        this.mClickedDialogEntryIndex = bundle.getInt(SAVE_STATE_INDEX, 0);
        this.mEntries = bundle.getCharSequenceArrayList(SAVE_STATE_ENTRIES);
        this.mEntryValues = bundle.getCharSequenceArray(SAVE_STATE_ENTRY_VALUES);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(SAVE_STATE_INDEX, this.mClickedDialogEntryIndex);
        bundle.putCharSequenceArrayList(SAVE_STATE_ENTRIES, this.mEntries);
        bundle.putCharSequenceArray(SAVE_STATE_ENTRY_VALUES, this.mEntryValues);
    }

    public void onDialogClosed(boolean z) {
        if (z && this.mClickedDialogEntryIndex >= 0) {
            ListPreference listPreference = getListPreference();
            String charSequence = this.mEntryValues[this.mClickedDialogEntryIndex].toString();
            if (listPreference.callChangeListener(charSequence)) {
                listPreference.setValue(charSequence);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setAdapter(ArrayAdapter arrayAdapter) {
        this.mAdapter = arrayAdapter;
    }

    /* access modifiers changed from: package-private */
    public void setEntries(ArrayList<CharSequence> arrayList) {
        this.mEntries = arrayList;
    }

    /* access modifiers changed from: package-private */
    public ArrayAdapter getAdapter() {
        return this.mAdapter;
    }

    /* access modifiers changed from: package-private */
    public void setMetricsCategory(Bundle bundle) {
        this.mMetricsCategory = bundle.getInt(METRICS_CATEGORY_KEY, 0);
    }

    /* access modifiers changed from: protected */
    public void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes((AttributeSet) null, R.styleable.AlertDialog, 16842845, 0);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), obtainStyledAttributes.getResourceId(21, 17367058), this.mEntries);
        this.mAdapter = arrayAdapter;
        builder.setSingleChoiceItems((ListAdapter) arrayAdapter, this.mClickedDialogEntryIndex, (DialogInterface.OnClickListener) new UpdatableListPreferenceDialogFragment$$ExternalSyntheticLambda0(this));
        builder.setPositiveButton((CharSequence) null, (DialogInterface.OnClickListener) null);
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onPrepareDialogBuilder$0$com-android-settingslib-widget-UpdatableListPreferenceDialogFragment */
    public /* synthetic */ void mo29295x10c94463(DialogInterface dialogInterface, int i) {
        this.mClickedDialogEntryIndex = i;
        onClick(dialogInterface, -1);
        dialogInterface.dismiss();
    }

    public int getMetricsCategory() {
        return this.mMetricsCategory;
    }

    /* access modifiers changed from: package-private */
    public ListPreference getListPreference() {
        return (ListPreference) getPreference();
    }

    private void setPreferenceData(ListPreference listPreference) {
        this.mEntries.clear();
        this.mClickedDialogEntryIndex = listPreference.findIndexOfValue(listPreference.getValue());
        for (CharSequence add : listPreference.getEntries()) {
            this.mEntries.add(add);
        }
        this.mEntryValues = listPreference.getEntryValues();
    }

    public void onListPreferenceUpdated(ListPreference listPreference) {
        if (this.mAdapter != null) {
            setPreferenceData(listPreference);
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
