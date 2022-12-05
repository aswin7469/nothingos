package com.nt.settings.glyphs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settingslib.widget.TwoTargetPreference;
import com.nt.settings.glyphs.utils.ContactsManager;
/* loaded from: classes2.dex */
public class PrimaryDeletePreference extends TwoTargetPreference implements Preference.OnPreferenceClickListener {
    public ContactsManager.Contact mContact;
    private OnSelectedListener mListener;

    /* loaded from: classes2.dex */
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

    @Override // com.android.settingslib.widget.TwoTargetPreference
    protected int getSecondTargetResId() {
        return R.layout.restricted_preference_widget_primary_delete;
    }

    @Override // com.android.settingslib.widget.TwoTargetPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(R.id.fl_delete);
        if (findViewById != null) {
            findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.glyphs.widget.PrimaryDeletePreference.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (PrimaryDeletePreference.this.mListener != null) {
                        OnSelectedListener onSelectedListener = PrimaryDeletePreference.this.mListener;
                        PrimaryDeletePreference primaryDeletePreference = PrimaryDeletePreference.this;
                        onSelectedListener.onClickDelete(primaryDeletePreference, primaryDeletePreference.mContact);
                    }
                }
            });
        }
    }

    @Override // com.android.settingslib.widget.TwoTargetPreference
    protected boolean shouldHideSecondTarget() {
        return getSecondTargetResId() == 0;
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mListener = onSelectedListener;
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        OnSelectedListener onSelectedListener = this.mListener;
        if (onSelectedListener != null) {
            onSelectedListener.onClick(preference, this.mContact);
            return true;
        }
        return true;
    }

    public void setData(ContactsManager.Contact contact) {
        this.mContact = contact;
        setTitle(contact.getDisplayName());
        if ("".equals(contact.getRingtoneUri())) {
            setSummary(17041302);
        } else {
            setSummary(contact.getRingtoneTitle());
        }
    }
}
