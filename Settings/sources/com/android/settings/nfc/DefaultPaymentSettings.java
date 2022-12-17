package com.android.settings.nfc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AlignmentSpan;
import android.text.style.BulletSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import androidx.preference.PreferenceScreen;
import com.android.settings.R$drawable;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.applications.defaultapps.DefaultAppPickerFragment;
import com.android.settings.nfc.PaymentBackend;
import com.android.settingslib.widget.CandidateInfo;
import com.android.settingslib.widget.FooterPreference;
import com.android.settingslib.widget.SelectorWithWidgetPreference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DefaultPaymentSettings extends DefaultAppPickerFragment {
    private List<PaymentBackend.PaymentAppInfo> mAppInfos;
    private FooterPreference mFooterPreference;
    private PaymentBackend mPaymentBackend;

    public int getMetricsCategory() {
        return 70;
    }

    /* access modifiers changed from: protected */
    public int getPreferenceScreenResId() {
        return R$xml.nfc_default_payment_settings;
    }

    /* access modifiers changed from: protected */
    public String getDefaultKey() {
        PaymentBackend.PaymentAppInfo defaultApp = this.mPaymentBackend.getDefaultApp();
        if (defaultApp == null) {
            return null;
        }
        return defaultApp.componentName.flattenToString() + " " + defaultApp.userHandle.getIdentifier();
    }

    /* access modifiers changed from: protected */
    public boolean setDefaultKey(String str) {
        String[] split = str.split(" ");
        if (split.length >= 2) {
            this.mPaymentBackend.setDefaultPaymentApp(ComponentName.unflattenFromString(split[0]), Integer.parseInt(split[1]));
        }
        return true;
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        PaymentBackend paymentBackend = new PaymentBackend(getActivity());
        this.mPaymentBackend = paymentBackend;
        this.mAppInfos = paymentBackend.getPaymentAppInfos();
    }

    /* access modifiers changed from: protected */
    public void addStaticPreferences(PreferenceScreen preferenceScreen) {
        if (this.mFooterPreference == null) {
            setupFooterPreference();
        }
        preferenceScreen.addPreference(this.mFooterPreference);
    }

    public void onResume() {
        super.onResume();
        this.mPaymentBackend.onResume();
    }

    public void onPause() {
        super.onPause();
        this.mPaymentBackend.onPause();
    }

    public class NfcPaymentCandidateInfoComparator implements Comparator<NfcPaymentCandidateInfo> {
        public NfcPaymentCandidateInfoComparator() {
        }

        public int compare(NfcPaymentCandidateInfo nfcPaymentCandidateInfo, NfcPaymentCandidateInfo nfcPaymentCandidateInfo2) {
            if (nfcPaymentCandidateInfo.loadLabel() == nfcPaymentCandidateInfo2.loadLabel()) {
                return 0;
            }
            if (nfcPaymentCandidateInfo.loadLabel() == null) {
                return -1;
            }
            if (nfcPaymentCandidateInfo2.loadLabel() == null) {
                return 1;
            }
            return nfcPaymentCandidateInfo.loadLabel().toString().compareTo(nfcPaymentCandidateInfo2.loadLabel().toString());
        }
    }

    public void bindPreferenceExtra(SelectorWithWidgetPreference selectorWithWidgetPreference, String str, CandidateInfo candidateInfo, String str2, String str3) {
        if (((NfcPaymentCandidateInfo) candidateInfo).isManagedProfile()) {
            selectorWithWidgetPreference.setSummary((CharSequence) getContext().getString(R$string.nfc_work_text));
        }
    }

    /* access modifiers changed from: protected */
    public List<? extends CandidateInfo> getCandidates() {
        ArrayList arrayList = new ArrayList();
        for (PaymentBackend.PaymentAppInfo next : this.mAppInfos) {
            boolean isManagedProfile = ((UserManager) getContext().createContextAsUser(next.userHandle, 0).getSystemService(UserManager.class)).isManagedProfile(next.userHandle.getIdentifier());
            arrayList.add(new NfcPaymentCandidateInfo(next.componentName.flattenToString(), next.label, next.icon, next.userHandle.getIdentifier(), isManagedProfile));
        }
        Collections.sort(arrayList, new NfcPaymentCandidateInfoComparator());
        return arrayList;
    }

    class NfcPaymentCandidateInfo extends CandidateInfo {
        private final Drawable mDrawable;
        private final boolean mIsManagedProfile;
        private final String mKey;
        private final CharSequence mLabel;
        /* access modifiers changed from: private */
        public final int mUserId;

        NfcPaymentCandidateInfo(String str, CharSequence charSequence, Drawable drawable, int i, boolean z) {
            super(true);
            this.mKey = str;
            this.mLabel = charSequence;
            this.mDrawable = drawable;
            this.mUserId = i;
            this.mIsManagedProfile = z;
        }

        public CharSequence loadLabel() {
            return this.mLabel;
        }

        public Drawable loadIcon() {
            return this.mDrawable;
        }

        public String getKey() {
            return this.mKey + " " + this.mUserId;
        }

        public boolean isManagedProfile() {
            return this.mIsManagedProfile;
        }
    }

    /* access modifiers changed from: protected */
    public CharSequence getConfirmationMessage(CandidateInfo candidateInfo) {
        if (candidateInfo == null) {
            return null;
        }
        NfcPaymentCandidateInfo nfcPaymentCandidateInfo = (NfcPaymentCandidateInfo) candidateInfo;
        if (!((UserManager) getContext().createContextAsUser(UserHandle.of(nfcPaymentCandidateInfo.mUserId), 0).getSystemService(UserManager.class)).isManagedProfile(nfcPaymentCandidateInfo.mUserId)) {
            return null;
        }
        String string = getContext().getString(R$string.nfc_default_payment_workapp_confirmation_title);
        String string2 = getContext().getString(R$string.nfc_default_payment_workapp_confirmation_message_title);
        String string3 = getContext().getString(R$string.nfc_default_payment_workapp_confirmation_message_1);
        String string4 = getContext().getString(R$string.nfc_default_payment_workapp_confirmation_message_2);
        SpannableString spannableString = new SpannableString(string);
        SpannableString spannableString2 = new SpannableString(string2);
        SpannableString spannableString3 = new SpannableString(string3);
        SpannableString spannableString4 = new SpannableString(string4);
        spannableString.setSpan(new RelativeSizeSpan(1.5f), 0, string.length(), 33);
        spannableString.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, string.length(), 33);
        spannableString2.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, string2.length(), 33);
        spannableString3.setSpan(new BulletSpan(20), 0, string3.length(), 33);
        spannableString4.setSpan(new BulletSpan(20), 0, string4.length(), 33);
        return TextUtils.concat(new CharSequence[]{spannableString, "\n\n", spannableString2, "\n\n", spannableString3, "\n", spannableString4});
    }

    private void setupFooterPreference() {
        FooterPreference footerPreference = new FooterPreference(getContext());
        this.mFooterPreference = footerPreference;
        footerPreference.setTitle((CharSequence) getResources().getString(R$string.nfc_default_payment_footer));
        this.mFooterPreference.setIcon(R$drawable.ic_info_outline_24dp);
        this.mFooterPreference.setLearnMoreAction(new DefaultPaymentSettings$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupFooterPreference$0(View view) {
        getContext().startActivity(new Intent(getActivity(), HowItWorks.class));
    }
}
