package com.android.settings.applications.appinfo;

import android.app.Activity;
import android.app.LocaleManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.UserHandle;
import android.util.FeatureFlagUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.Preference;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.R$xml;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.applications.AppLocaleUtil;
import com.android.settings.widget.EntityHeaderController;
import com.android.settingslib.Utils;
import com.android.settingslib.applications.AppUtils;
import com.android.settingslib.applications.ApplicationsState;
import com.android.settingslib.widget.LayoutPreference;
import java.util.Locale;

public class AppLocaleDetails extends SettingsPreferenceFragment {
    private ApplicationInfo mApplicationInfo;
    private boolean mCreated = false;
    private String mPackageName;
    private LayoutPreference mPrefOfDescription;
    private Preference mPrefOfDisclaimer;

    public int getMetricsCategory() {
        return 1911;
    }

    public static AppLocaleDetails newInstance(String str, int i) {
        AppLocaleDetails appLocaleDetails = new AppLocaleDetails();
        Bundle bundle = new Bundle();
        bundle.putString("package", str);
        bundle.putInt("uid", i);
        appLocaleDetails.setArguments(bundle);
        return appLocaleDetails;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        String string = arguments.getString("package", "");
        this.mPackageName = string;
        if (string.isEmpty()) {
            Log.d("AppLocaleDetails", "There is no package name.");
            finish();
        }
        int i = arguments.getInt("uid", getContext().getUserId());
        addPreferencesFromResource(R$xml.app_locale_details);
        this.mPrefOfDescription = (LayoutPreference) getPreferenceScreen().findPreference("app_locale_description");
        this.mPrefOfDisclaimer = getPreferenceScreen().findPreference("app_locale_disclaimer");
        this.mApplicationInfo = getApplicationInfo(this.mPackageName, i);
        setDisclaimerPreference();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        if (this.mPackageName.isEmpty()) {
            return layoutInflater.inflate(R$layout.manage_applications_apps_unsupported, (ViewGroup) null);
        }
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }

    public void onResume() {
        super.onResume();
        refreshUi();
    }

    private void refreshUi() {
        setDescription();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (this.mCreated) {
            Log.w("AppLocaleDetails", "onActivityCreated: ignoring duplicate call");
            return;
        }
        this.mCreated = true;
        if (this.mPackageName != null) {
            FragmentActivity activity = getActivity();
            getPreferenceScreen().addPreference(EntityHeaderController.newInstance(activity, this, (View) null).setRecyclerView(getListView(), getSettingsLifecycle()).setIcon(Utils.getBadgedIcon(getContext(), this.mApplicationInfo)).setLabel(this.mApplicationInfo.loadLabel(getContext().getPackageManager())).setIsInstantApp(AppUtils.isInstant(this.mApplicationInfo)).setPackageName(this.mPackageName).setUid(this.mApplicationInfo.uid).setHasAppInfoLink(true).setButtonActions(0, 0).setOrder(10).done((Activity) activity, getPrefContext()));
        }
    }

    private void setDisclaimerPreference() {
        if (FeatureFlagUtils.isEnabled(getContext(), "settings_app_locale_opt_in_enabled")) {
            this.mPrefOfDisclaimer.setVisible(false);
        }
    }

    private void setDescription() {
        int appDescription = getAppDescription();
        if (appDescription != -1) {
            this.mPrefOfDescription.setVisible(true);
            ((TextView) this.mPrefOfDescription.findViewById(R$id.description)).setText(getContext().getString(appDescription));
        }
    }

    private ApplicationInfo getApplicationInfo(String str, int i) {
        try {
            return getContext().getPackageManager().getApplicationInfoAsUser(str, 0, i);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.w("AppLocaleDetails", "Application info not found for: " + str);
            return null;
        }
    }

    private int getAppDescription() {
        LocaleList packageLocales = AppLocaleUtil.getPackageLocales(getContext(), this.mPackageName);
        String[] assetLocales = AppLocaleUtil.getAssetLocales(getContext(), this.mPackageName);
        if ((packageLocales == null || !packageLocales.isEmpty()) && (packageLocales != null || assetLocales.length != 0)) {
            return -1;
        }
        return R$string.desc_no_available_supported_locale;
    }

    public static Locale getAppDefaultLocale(Context context, String str) {
        LocaleList localeList;
        LocaleManager localeManager = (LocaleManager) context.getSystemService(LocaleManager.class);
        if (localeManager == null) {
            localeList = null;
        } else {
            try {
                localeList = localeManager.getApplicationLocales(str);
            } catch (IllegalArgumentException e) {
                Log.w("AppLocaleDetails", "package name : " + str + " is not correct. " + e);
                return null;
            }
        }
        if (localeList == null) {
            return null;
        }
        return localeList.get(0);
    }

    public static CharSequence getSummary(Context context, ApplicationsState.AppEntry appEntry) {
        Locale appDefaultLocale = getAppDefaultLocale(context.createContextAsUser(UserHandle.getUserHandleForUid(appEntry.info.uid), 0), appEntry.info.packageName);
        if (appDefaultLocale == null) {
            return context.getString(R$string.preference_of_system_locale_summary);
        }
        return appDefaultLocale.getDisplayName(appDefaultLocale);
    }
}
