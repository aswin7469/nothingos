package com.android.settings.applications;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.accessibility.ListDialogPreference;

public class AppDomainsPreference extends ListDialogPreference {
    private int mNumEntries;

    public AppDomainsPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setDialogLayoutResource(R$layout.app_domains_dialog);
        setListItemLayoutResource(R$layout.app_domains_item);
    }

    public void setTitles(CharSequence[] charSequenceArr) {
        this.mNumEntries = charSequenceArr != null ? charSequenceArr.length : 0;
        super.setTitles(charSequenceArr);
    }

    public CharSequence getSummary() {
        int i;
        Context context = getContext();
        if (this.mNumEntries == 0) {
            return context.getString(R$string.domain_urls_summary_none);
        }
        CharSequence summary = super.getSummary();
        if (this.mNumEntries == 1) {
            i = R$string.domain_urls_summary_one;
        } else {
            i = R$string.domain_urls_summary_some;
        }
        return context.getString(i, new Object[]{summary});
    }

    /* access modifiers changed from: protected */
    public void onBindListItem(View view, int i) {
        CharSequence titleAt = getTitleAt(i);
        if (titleAt != null) {
            ((TextView) view.findViewById(R$id.domain_name)).setText(titleAt);
        }
    }
}
