package com.android.settings.localepicker;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LocaleManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.android.internal.app.LocalePickerWithRegion;
import com.android.internal.app.LocaleStore;
import com.android.settings.R$id;
import com.android.settings.R$string;
import com.android.settings.applications.appinfo.AppLocaleDetails;
import com.android.settings.core.SettingsBaseActivity;

public class AppLocalePickerActivity extends SettingsBaseActivity implements LocalePickerWithRegion.LocaleSelectedListener, MenuItem.OnActionExpandListener {
    private static final String TAG = AppLocalePickerActivity.class.getSimpleName();
    /* access modifiers changed from: private */
    public View mAppLocaleDetailContainer;
    private AppLocaleDetails mAppLocaleDetails;
    private Context mContextAsUser;
    private LocalePickerWithRegion mLocalePickerWithRegion;
    private String mPackageName;

    public void onCreate(Bundle bundle) {
        int intExtra;
        super.onCreate(bundle);
        Uri data = getIntent().getData();
        if (data == null) {
            Log.d(TAG, "There is no uri data.");
            finish();
            return;
        }
        String schemeSpecificPart = data.getSchemeSpecificPart();
        this.mPackageName = schemeSpecificPart;
        if (TextUtils.isEmpty(schemeSpecificPart)) {
            Log.d(TAG, "There is no package name.");
            finish();
            return;
        }
        this.mContextAsUser = this;
        if (getIntent().hasExtra("uid") && (intExtra = getIntent().getIntExtra("uid", -1)) != -1) {
            this.mContextAsUser = createContextAsUser(UserHandle.getUserHandleForUid(intExtra), 0);
        }
        setTitle(R$string.app_locale_picker_title);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        this.mLocalePickerWithRegion = LocalePickerWithRegion.createLanguagePicker(this.mContextAsUser, this, false, this.mPackageName, this);
        this.mAppLocaleDetails = AppLocaleDetails.newInstance(this.mPackageName, this.mContextAsUser.getUserId());
        this.mAppLocaleDetailContainer = launchAppLocaleDetailsPage();
        launchLocalePickerPage();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        super.onBackPressed();
        return true;
    }

    public void onLocaleSelected(LocaleStore.LocaleInfo localeInfo) {
        if (localeInfo == null || localeInfo.getLocale() == null || localeInfo.isSystemLocale()) {
            setAppDefaultLocale("");
        } else {
            setAppDefaultLocale(localeInfo.getLocale().toLanguageTag());
        }
        finish();
    }

    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
        this.mAppBarLayout.setExpanded(false, false);
        return true;
    }

    public boolean onMenuItemActionExpand(MenuItem menuItem) {
        this.mAppBarLayout.setExpanded(false, false);
        return true;
    }

    private void setAppDefaultLocale(String str) {
        String str2 = TAG;
        Log.d(str2, "setAppDefaultLocale: " + str);
        LocaleManager localeManager = (LocaleManager) this.mContextAsUser.getSystemService(LocaleManager.class);
        if (localeManager == null) {
            Log.w(str2, "LocaleManager is null, cannot set default app locale");
        } else {
            localeManager.setApplicationLocales(this.mPackageName, LocaleList.forLanguageTags(str));
        }
    }

    private View launchAppLocaleDetailsPage() {
        FrameLayout frameLayout = new FrameLayout(this);
        int i = R$id.layout_app_locale_details;
        frameLayout.setId(i);
        getSupportFragmentManager().beginTransaction().replace(i, this.mAppLocaleDetails).commit();
        return frameLayout;
    }

    private void launchLocalePickerPage() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            public void onFragmentViewCreated(FragmentManager fragmentManager, Fragment fragment, View view, Bundle bundle) {
                super.onFragmentViewCreated(fragmentManager, fragment, view, bundle);
                ListView listView = (ListView) view.findViewById(16908298);
                if (listView != null) {
                    listView.addHeaderView(AppLocalePickerActivity.this.mAppLocaleDetailContainer);
                }
            }
        }, true);
        fragmentManager.beginTransaction().setTransition(4097).replace(R$id.content_frame, this.mLocalePickerWithRegion).commit();
    }
}
