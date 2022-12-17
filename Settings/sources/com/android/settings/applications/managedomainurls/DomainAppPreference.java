package com.android.settings.applications.managedomainurls;

import android.content.Context;
import android.content.pm.verify.domain.DomainVerificationManager;
import android.content.pm.verify.domain.DomainVerificationUserState;
import android.graphics.drawable.Drawable;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$drawable;
import com.android.settings.R$string;
import com.android.settings.applications.intentpicker.IntentPickerUtils;
import com.android.settingslib.applications.AppUtils;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.AppPreference;

public class DomainAppPreference extends AppPreference {
    private Drawable mCacheIcon;
    private final DomainVerificationManager mDomainVerificationManager;
    private final ApplicationsState.AppEntry mEntry;

    public DomainAppPreference(Context context, ApplicationsState.AppEntry appEntry) {
        super(context);
        this.mDomainVerificationManager = (DomainVerificationManager) context.getSystemService(DomainVerificationManager.class);
        this.mEntry = appEntry;
        appEntry.ensureLabel(getContext());
        this.mCacheIcon = AppUtils.getIconFromCache(appEntry);
        setState();
    }

    public void reuse() {
        setState();
        notifyChanged();
    }

    public ApplicationsState.AppEntry getEntry() {
        return this.mEntry;
    }

    private void setState() {
        setTitle((CharSequence) this.mEntry.label);
        Drawable drawable = this.mCacheIcon;
        if (drawable != null) {
            setIcon(drawable);
        } else {
            setIcon(R$drawable.empty_icon);
        }
        setSummary(getDomainsSummary(this.mEntry.info.packageName));
    }

    private CharSequence getDomainsSummary(String str) {
        return getContext().getText(isLinkHandlingAllowed(str) ? R$string.app_link_open_always : R$string.app_link_open_never);
    }

    private boolean isLinkHandlingAllowed(String str) {
        DomainVerificationUserState domainVerificationUserState = IntentPickerUtils.getDomainVerificationUserState(this.mDomainVerificationManager, str);
        if (domainVerificationUserState == null) {
            return false;
        }
        return domainVerificationUserState.isLinkHandlingAllowed();
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        if (this.mCacheIcon == null) {
            ThreadUtils.postOnBackgroundThread((Runnable) new DomainAppPreference$$ExternalSyntheticLambda0(this));
        }
        super.onBindViewHolder(preferenceViewHolder);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$1() {
        ThreadUtils.postOnMainThread(new DomainAppPreference$$ExternalSyntheticLambda1(this, AppUtils.getIcon(getContext(), this.mEntry)));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onBindViewHolder$0(Drawable drawable) {
        setIcon(drawable);
        this.mCacheIcon = drawable;
    }
}
