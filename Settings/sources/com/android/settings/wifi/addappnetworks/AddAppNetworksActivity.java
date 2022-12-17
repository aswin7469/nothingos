package com.android.settings.wifi.addappnetworks;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settingslib.core.lifecycle.HideNonSystemOverlayMixin;
import com.android.settingslib.wifi.WifiEnterpriseRestrictionUtils;

public class AddAppNetworksActivity extends FragmentActivity {
    @VisibleForTesting
    IActivityManager mActivityManager = ActivityManager.getService();
    @VisibleForTesting
    final Bundle mBundle = new Bundle();

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.settings_panel);
        if (!showAddNetworksFragment()) {
            finish();
            return;
        }
        getLifecycle().addObserver(new HideNonSystemOverlayMixin(this));
        Window window = getWindow();
        window.setGravity(80);
        window.setLayout(-1, -2);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (!showAddNetworksFragment()) {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    @VisibleForTesting
    public boolean showAddNetworksFragment() {
        if (!isAddWifiConfigAllow()) {
            Log.d("AddAppNetworksActivity", "Not allowed by Enterprise Restriction");
            return false;
        }
        String callingAppPackageName = getCallingAppPackageName();
        if (TextUtils.isEmpty(callingAppPackageName)) {
            Log.d("AddAppNetworksActivity", "Package name is null");
            return false;
        }
        this.mBundle.putString("panel_calling_package_name", callingAppPackageName);
        this.mBundle.putParcelableArrayList("android.provider.extra.WIFI_NETWORK_LIST", getIntent().getParcelableArrayListExtra("android.provider.extra.WIFI_NETWORK_LIST"));
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment findFragmentByTag = supportFragmentManager.findFragmentByTag("AddAppNetworksActivity");
        if (findFragmentByTag == null) {
            AddAppNetworksFragment addAppNetworksFragment = new AddAppNetworksFragment();
            addAppNetworksFragment.setArguments(this.mBundle);
            supportFragmentManager.beginTransaction().add(R$id.main_content, (Fragment) addAppNetworksFragment, "AddAppNetworksActivity").commit();
            return true;
        }
        ((AddAppNetworksFragment) findFragmentByTag).createContent(this.mBundle);
        return true;
    }

    /* access modifiers changed from: protected */
    @VisibleForTesting
    public String getCallingAppPackageName() {
        try {
            return this.mActivityManager.getLaunchedFromPackage(getActivityToken());
        } catch (RemoteException unused) {
            Log.e("AddAppNetworksActivity", "Can not get the package from activity manager");
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public boolean isAddWifiConfigAllow() {
        return WifiEnterpriseRestrictionUtils.isAddWifiConfigAllowed(this);
    }
}
