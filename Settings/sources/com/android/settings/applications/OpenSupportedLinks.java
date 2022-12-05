package com.android.settings.applications;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceCategory;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settingslib.widget.FooterPreference;
import com.android.settingslib.widget.RadioButtonPreference;
import java.util.Iterator;
/* loaded from: classes.dex */
public class OpenSupportedLinks extends AppInfoWithHeader implements RadioButtonPreference.OnClickListener {
    RadioButtonPreference mAllowOpening;
    RadioButtonPreference mAskEveryTime;
    private int mCurrentIndex;
    RadioButtonPreference mNotAllowed;
    PackageManager mPackageManager;
    PreferenceCategory mPreferenceCategory;
    private String[] mRadioKeys = {"app_link_open_always", "app_link_open_ask", "app_link_open_never"};

    private int indexToLinkState(int i) {
        if (i != 0) {
            return i != 2 ? 1 : 3;
        }
        return 2;
    }

    private int linkStateToIndex(int i) {
        if (i != 2) {
            return i != 3 ? 1 : 2;
        }
        return 0;
    }

    @Override // com.android.settings.applications.AppInfoBase
    protected AlertDialog createDialog(int i, int i2) {
        return null;
    }

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 1824;
    }

    @Override // com.android.settings.applications.AppInfoBase
    protected boolean refreshUi() {
        return true;
    }

    @Override // com.android.settings.applications.AppInfoBase, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPackageManager = getPackageManager();
        addPreferencesFromResource(R.xml.open_supported_links);
        initRadioPreferencesGroup();
        updateFooterPreference();
    }

    void initRadioPreferencesGroup() {
        this.mPreferenceCategory = (PreferenceCategory) findPreference("supported_links_radio_group");
        this.mAllowOpening = makeRadioPreference("app_link_open_always", R.string.app_link_open_always);
        int entriesNo = getEntriesNo();
        this.mAllowOpening.setAppendixVisibility(8);
        this.mAllowOpening.setSummary(getResources().getQuantityString(R.plurals.app_link_open_always_summary, entriesNo, Integer.valueOf(entriesNo)));
        this.mAskEveryTime = makeRadioPreference("app_link_open_ask", R.string.app_link_open_ask);
        this.mNotAllowed = makeRadioPreference("app_link_open_never", R.string.app_link_open_never);
        int linkStateToIndex = linkStateToIndex(this.mPackageManager.getIntentVerificationStatusAsUser(this.mPackageName, this.mUserId));
        this.mCurrentIndex = linkStateToIndex;
        setRadioStatus(linkStateToIndex);
    }

    @Override // com.android.settingslib.widget.RadioButtonPreference.OnClickListener
    public void onRadioButtonClicked(RadioButtonPreference radioButtonPreference) {
        int preferenceKeyToIndex = preferenceKeyToIndex(radioButtonPreference.getKey());
        if (this.mCurrentIndex != preferenceKeyToIndex) {
            this.mCurrentIndex = preferenceKeyToIndex;
            setRadioStatus(preferenceKeyToIndex);
            updateAppLinkState(indexToLinkState(this.mCurrentIndex));
        }
    }

    private RadioButtonPreference makeRadioPreference(String str, int i) {
        RadioButtonPreference radioButtonPreference = new RadioButtonPreference(this.mPreferenceCategory.getContext());
        radioButtonPreference.setKey(str);
        radioButtonPreference.setTitle(i);
        radioButtonPreference.setOnClickListener(this);
        this.mPreferenceCategory.addPreference(radioButtonPreference);
        return radioButtonPreference;
    }

    int getEntriesNo() {
        return Utils.getHandledDomains(this.mPackageManager, this.mPackageName).size();
    }

    private void setRadioStatus(int i) {
        boolean z = false;
        this.mAllowOpening.setChecked(i == 0);
        this.mAskEveryTime.setChecked(i == 1);
        RadioButtonPreference radioButtonPreference = this.mNotAllowed;
        if (i == 2) {
            z = true;
        }
        radioButtonPreference.setChecked(z);
    }

    private int preferenceKeyToIndex(String str) {
        int i = 0;
        while (true) {
            String[] strArr = this.mRadioKeys;
            if (i < strArr.length) {
                if (TextUtils.equals(str, strArr[i])) {
                    return i;
                }
                i++;
            } else {
                return 1;
            }
        }
    }

    private void updateAppLinkState(int i) {
        if (this.mPackageManager.getIntentVerificationStatusAsUser(this.mPackageName, this.mUserId) == i) {
            return;
        }
        if (this.mPackageManager.updateIntentVerificationStatusAsUser(this.mPackageName, i, this.mUserId)) {
            this.mPackageManager.getIntentVerificationStatusAsUser(this.mPackageName, this.mUserId);
        } else {
            Log.e("OpenSupportedLinks", "Couldn't update intent verification status!");
        }
    }

    private void updateFooterPreference() {
        FooterPreference footerPreference = (FooterPreference) findPreference("supported_links_footer");
        if (footerPreference == null) {
            Log.w("OpenSupportedLinks", "Can't find the footer preference.");
        } else {
            addLinksToFooter(footerPreference);
        }
    }

    void addLinksToFooter(FooterPreference footerPreference) {
        Iterator<String> it;
        ArraySet<String> handledDomains = Utils.getHandledDomains(this.mPackageManager, this.mPackageName);
        if (handledDomains.isEmpty()) {
            Log.w("OpenSupportedLinks", "Can't find any app links.");
            return;
        }
        String str = ((Object) footerPreference.getTitle()) + System.lineSeparator();
        while (handledDomains.iterator().hasNext()) {
            str = ((Object) str) + System.lineSeparator() + it.next();
        }
        footerPreference.setTitle(str);
    }
}
