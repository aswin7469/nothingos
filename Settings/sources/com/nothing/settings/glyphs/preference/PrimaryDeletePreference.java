package com.nothing.settings.glyphs.preference;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settingslib.widget.TwoTargetPreference;
import com.nothing.settings.glyphs.utils.ContactsManager;

public class PrimaryDeletePreference extends TwoTargetPreference implements Preference.OnPreferenceClickListener {
    public ContactsManager.Contact mContact;
    private OnSelectedListener mListener;

    public interface OnSelectedListener {
        void onClick(Preference preference, ContactsManager.Contact contact);

        void onClickDelete(Preference preference, ContactsManager.Contact contact);
    }

    public PrimaryDeletePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setOnPreferenceClickListener(this);
    }

    public PrimaryDeletePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setOnPreferenceClickListener(this);
    }

    public PrimaryDeletePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnPreferenceClickListener(this);
    }

    public PrimaryDeletePreference(Context context) {
        super(context);
        setOnPreferenceClickListener(this);
    }

    /* access modifiers changed from: protected */
    public int getSecondTargetResId() {
        return R$layout.restricted_preference_widget_primary_delete;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(R$id.fl_delete);
        if (findViewById != null) {
            findViewById.setOnClickListener(new PrimaryDeletePreference$$ExternalSyntheticLambda0(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(View view) {
        OnSelectedListener onSelectedListener = this.mListener;
        if (onSelectedListener != null) {
            onSelectedListener.onClickDelete(this, this.mContact);
        }
    }

    /* access modifiers changed from: protected */
    public boolean shouldHideSecondTarget() {
        return getSecondTargetResId() == 0;
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mListener = onSelectedListener;
    }

    public boolean onPreferenceClick(Preference preference) {
        OnSelectedListener onSelectedListener = this.mListener;
        if (onSelectedListener == null) {
            return true;
        }
        onSelectedListener.onClick(preference, this.mContact);
        return true;
    }

    public void setData(ContactsManager.Contact contact) {
        this.mContact = contact;
        setTitle((CharSequence) contact.getDisplayName());
        Log.d("PrimaryDeletePreference", "contact.getRingtoneUri():" + contact.getRingtoneUri());
        if (TextUtils.isEmpty(contact.getRingtoneUri())) {
            setSummary((CharSequence) getContext().getString(17041421));
        } else {
            setSummary((CharSequence) contact.getRingtoneTitle());
        }
    }
}
