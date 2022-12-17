package com.android.settings.bluetooth;

import android.companion.AssociationInfo;
import android.companion.CompanionDeviceManager;
import android.companion.ICompanionDeviceManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import com.android.internal.util.CollectionUtils;
import com.android.settings.R$drawable;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.core.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class BluetoothDetailsCompanionAppsController extends BluetoothDetailsController {
    private CachedBluetoothDevice mCachedDevice;
    CompanionDeviceManager mCompanionDeviceManager;
    PackageManager mPackageManager;
    PreferenceCategory mProfilesContainer;

    public String getPreferenceKey() {
        return "device_companion_apps";
    }

    public BluetoothDetailsCompanionAppsController(Context context, PreferenceFragmentCompat preferenceFragmentCompat, CachedBluetoothDevice cachedBluetoothDevice, Lifecycle lifecycle) {
        super(context, preferenceFragmentCompat, cachedBluetoothDevice, lifecycle);
        this.mCachedDevice = cachedBluetoothDevice;
        this.mCompanionDeviceManager = (CompanionDeviceManager) context.getSystemService(CompanionDeviceManager.class);
        this.mPackageManager = context.getPackageManager();
        lifecycle.addObserver(this);
    }

    /* access modifiers changed from: protected */
    public void init(PreferenceScreen preferenceScreen) {
        PreferenceCategory preferenceCategory = (PreferenceCategory) preferenceScreen.findPreference(getPreferenceKey());
        this.mProfilesContainer = preferenceCategory;
        preferenceCategory.setLayoutResource(R$layout.preference_companion_app);
    }

    private List<AssociationInfo> getAssociations(String str) {
        return CollectionUtils.filter(this.mCompanionDeviceManager.getAllAssociations(), new C0781x74fb60ba(str));
    }

    private static void removePreference(PreferenceCategory preferenceCategory, String str) {
        Preference findPreference = preferenceCategory.findPreference(str);
        if (findPreference != null) {
            preferenceCategory.removePreference(findPreference);
        }
    }

    private void removeAssociationDialog(String str, String str2, PreferenceCategory preferenceCategory, CharSequence charSequence, Context context) {
        C0780x74fb60b9 bluetoothDetailsCompanionAppsController$$ExternalSyntheticLambda4 = new C0780x74fb60b9(str, str2, preferenceCategory);
        new AlertDialog.Builder(context).setPositiveButton(R$string.bluetooth_companion_app_remove_association_confirm_button, (DialogInterface.OnClickListener) bluetoothDetailsCompanionAppsController$$ExternalSyntheticLambda4).setNegativeButton(17039360, (DialogInterface.OnClickListener) bluetoothDetailsCompanionAppsController$$ExternalSyntheticLambda4).setTitle(R$string.bluetooth_companion_app_remove_association_dialog_title).setMessage((CharSequence) this.mContext.getString(R$string.bluetooth_companion_app_body, new Object[]{charSequence, this.mCachedDevice.getName()})).show();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$removeAssociationDialog$1(String str, String str2, PreferenceCategory preferenceCategory, DialogInterface dialogInterface, int i) {
        if (i == -1) {
            removeAssociation(str, str2, preferenceCategory);
        }
    }

    private static void removeAssociation(String str, String str2, PreferenceCategory preferenceCategory) {
        try {
            ICompanionDeviceManager asInterface = ICompanionDeviceManager.Stub.asInterface(ServiceManager.getService("companiondevice"));
            Objects.requireNonNull(asInterface);
            asInterface.legacyDisassociate(str2, str, UserHandle.myUserId());
            removePreference(preferenceCategory, str);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private CharSequence getAppName(String str) {
        try {
            PackageManager packageManager = this.mPackageManager;
            return packageManager.getApplicationLabel(packageManager.getApplicationInfo(str, 0));
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("BTCompanionController", "Package Not Found", e);
            return null;
        }
    }

    private List<String> getPreferencesNeedToShow(String str, PreferenceCategory preferenceCategory) {
        ArrayList<String> arrayList = new ArrayList<>();
        Set set = (Set) getAssociations(str).stream().map(new C0778x74fb60b7()).collect(Collectors.toSet());
        for (int i = 0; i < preferenceCategory.getPreferenceCount(); i++) {
            String key = preferenceCategory.getPreference(i).getKey();
            if (set.isEmpty() || !set.contains(key)) {
                arrayList.add(key);
            }
        }
        for (String removePreference : arrayList) {
            removePreference(preferenceCategory, removePreference);
        }
        return (List) set.stream().filter(new C0779x74fb60b8(preferenceCategory)).collect(Collectors.toList());
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getPreferencesNeedToShow$2(PreferenceCategory preferenceCategory, String str) {
        return preferenceCategory.findPreference(str) == null;
    }

    /* access modifiers changed from: protected */
    public void refresh() {
        updatePreferences(this.mContext, this.mCachedDevice.getAddress(), this.mProfilesContainer);
    }

    public void updatePreferences(Context context, String str, PreferenceCategory preferenceCategory) {
        boolean z;
        Context context2 = context;
        PreferenceCategory preferenceCategory2 = preferenceCategory;
        BluetoothFeatureProvider bluetoothFeatureProvider = FeatureFactory.getFactory(context).getBluetoothFeatureProvider();
        boolean z2 = DeviceConfig.getBoolean("settings_ui", "bt_slice_settings_enabled", true);
        Uri bluetoothDeviceSettingsUri = bluetoothFeatureProvider.getBluetoothDeviceSettingsUri(this.mCachedDevice.getDevice());
        if (!z2 || bluetoothDeviceSettingsUri == null) {
            HashSet hashSet = new HashSet();
            for (String next : getPreferencesNeedToShow(str, preferenceCategory2)) {
                CharSequence appName = getAppName(next);
                if (TextUtils.isEmpty(appName)) {
                    String str2 = str;
                } else if (hashSet.add(next)) {
                    C0776x74fb60b5 bluetoothDetailsCompanionAppsController$$ExternalSyntheticLambda0 = r1;
                    HashSet hashSet2 = hashSet;
                    C0776x74fb60b5 bluetoothDetailsCompanionAppsController$$ExternalSyntheticLambda02 = new C0776x74fb60b5(this, next, str, preferenceCategory, appName, context);
                    CompanionAppWidgetPreference companionAppWidgetPreference = new CompanionAppWidgetPreference(context.getResources().getDrawable(R$drawable.ic_clear), bluetoothDetailsCompanionAppsController$$ExternalSyntheticLambda0, context2);
                    try {
                        Drawable applicationIcon = this.mPackageManager.getApplicationIcon(next);
                        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(next);
                        companionAppWidgetPreference.setIcon(applicationIcon);
                        companionAppWidgetPreference.setTitle((CharSequence) appName.toString());
                        companionAppWidgetPreference.setOnPreferenceClickListener(new C0777x74fb60b6(context2, launchIntentForPackage));
                        companionAppWidgetPreference.setKey(next);
                        z = true;
                        companionAppWidgetPreference.setVisible(true);
                        preferenceCategory2.addPreference(companionAppWidgetPreference);
                    } catch (PackageManager.NameNotFoundException e) {
                        z = true;
                        Log.e("BTCompanionController", "Icon Not Found", e);
                    }
                    String str3 = str;
                    boolean z3 = z;
                    hashSet = hashSet2;
                }
            }
            return;
        }
        preferenceCategory.removeAll();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePreferences$3(String str, String str2, PreferenceCategory preferenceCategory, CharSequence charSequence, Context context, View view) {
        removeAssociationDialog(str, str2, preferenceCategory, charSequence, context);
    }
}
