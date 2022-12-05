package com.android.settings.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Switch;
import com.android.internal.widget.LockPatternUtils;
import com.android.settings.R;
import com.android.settings.accessibility.AccessibilityServiceWarning;
import com.android.settings.password.ConfirmDeviceCredentialActivity;
import com.android.settings.widget.SettingsMainSwitchPreference;
import com.android.settingslib.accessibility.AccessibilityUtils;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public class ToggleAccessibilityServicePreferenceFragment extends ToggleFeaturePreferenceFragment {
    private Dialog mDialog;
    private LockPatternUtils mLockPatternUtils;
    private BroadcastReceiver mPackageRemovedReceiver;
    private AtomicBoolean mIsDialogShown = new AtomicBoolean(false);
    private final SettingsContentObserver mSettingsContentObserver = new SettingsContentObserver(new Handler()) { // from class: com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z, Uri uri) {
            ToggleAccessibilityServicePreferenceFragment.this.updateSwitchBarToggleSwitch();
        }
    };

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment, com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 4;
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment, com.android.settings.SettingsPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mLockPatternUtils = new LockPatternUtils(getPrefContext());
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        AccessibilityServiceInfo accessibilityServiceInfo = getAccessibilityServiceInfo();
        if (accessibilityServiceInfo == null) {
            getActivity().finishAndRemoveTask();
        } else if (AccessibilityUtil.isSystemApp(accessibilityServiceInfo)) {
        } else {
            registerPackageRemoveReceiver();
        }
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.core.InstrumentedPreferenceFragment, com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        updateSwitchBarToggleSwitch();
        this.mSettingsContentObserver.register(getContentResolver());
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment
    public void onPreferenceToggled(String str, boolean z) {
        ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
        AccessibilityStatsLogUtils.logAccessibilityServiceEnabled(unflattenFromString, z);
        AccessibilityUtils.setAccessibilityServiceState(getPrefContext(), unflattenFromString, z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AccessibilityServiceInfo getAccessibilityServiceInfo() {
        List<AccessibilityServiceInfo> installedAccessibilityServiceList = AccessibilityManager.getInstance(getPrefContext()).getInstalledAccessibilityServiceList();
        int size = installedAccessibilityServiceList.size();
        for (int i = 0; i < size; i++) {
            AccessibilityServiceInfo accessibilityServiceInfo = installedAccessibilityServiceList.get(i);
            ResolveInfo resolveInfo = accessibilityServiceInfo.getResolveInfo();
            if (this.mComponentName.getPackageName().equals(resolveInfo.serviceInfo.packageName) && this.mComponentName.getClassName().equals(resolveInfo.serviceInfo.name)) {
                return accessibilityServiceInfo;
            }
        }
        return null;
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.DialogCreatable
    public Dialog onCreateDialog(int i) {
        switch (i) {
            case 1002:
                AccessibilityServiceInfo accessibilityServiceInfo = getAccessibilityServiceInfo();
                if (accessibilityServiceInfo != null) {
                    this.mDialog = AccessibilityServiceWarning.createCapabilitiesDialog(getPrefContext(), accessibilityServiceInfo, new View.OnClickListener() { // from class: com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment$$ExternalSyntheticLambda4
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            ToggleAccessibilityServicePreferenceFragment.this.onDialogButtonFromEnableToggleClicked(view);
                        }
                    }, new AccessibilityServiceWarning.UninstallActionPerformer() { // from class: com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment$$ExternalSyntheticLambda5
                        @Override // com.android.settings.accessibility.AccessibilityServiceWarning.UninstallActionPerformer
                        public final void uninstallPackage() {
                            ToggleAccessibilityServicePreferenceFragment.this.onDialogButtonFromUninstallClicked();
                        }
                    });
                    break;
                } else {
                    return null;
                }
            case 1003:
                AccessibilityServiceInfo accessibilityServiceInfo2 = getAccessibilityServiceInfo();
                if (accessibilityServiceInfo2 != null) {
                    this.mDialog = AccessibilityServiceWarning.createCapabilitiesDialog(getPrefContext(), accessibilityServiceInfo2, new View.OnClickListener() { // from class: com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            ToggleAccessibilityServicePreferenceFragment.this.onDialogButtonFromShortcutClicked(view);
                        }
                    }, new AccessibilityServiceWarning.UninstallActionPerformer() { // from class: com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment$$ExternalSyntheticLambda5
                        @Override // com.android.settings.accessibility.AccessibilityServiceWarning.UninstallActionPerformer
                        public final void uninstallPackage() {
                            ToggleAccessibilityServicePreferenceFragment.this.onDialogButtonFromUninstallClicked();
                        }
                    });
                    break;
                } else {
                    return null;
                }
            case 1004:
                AccessibilityServiceInfo accessibilityServiceInfo3 = getAccessibilityServiceInfo();
                if (accessibilityServiceInfo3 != null) {
                    this.mDialog = AccessibilityServiceWarning.createCapabilitiesDialog(getPrefContext(), accessibilityServiceInfo3, new View.OnClickListener() { // from class: com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view) {
                            ToggleAccessibilityServicePreferenceFragment.this.onDialogButtonFromShortcutToggleClicked(view);
                        }
                    }, new AccessibilityServiceWarning.UninstallActionPerformer() { // from class: com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment$$ExternalSyntheticLambda5
                        @Override // com.android.settings.accessibility.AccessibilityServiceWarning.UninstallActionPerformer
                        public final void uninstallPackage() {
                            ToggleAccessibilityServicePreferenceFragment.this.onDialogButtonFromUninstallClicked();
                        }
                    });
                    break;
                } else {
                    return null;
                }
            case 1005:
                AccessibilityServiceInfo accessibilityServiceInfo4 = getAccessibilityServiceInfo();
                if (accessibilityServiceInfo4 != null) {
                    this.mDialog = AccessibilityServiceWarning.createDisableDialog(getPrefContext(), accessibilityServiceInfo4, new DialogInterface.OnClickListener() { // from class: com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment$$ExternalSyntheticLambda0
                        @Override // android.content.DialogInterface.OnClickListener
                        public final void onClick(DialogInterface dialogInterface, int i2) {
                            ToggleAccessibilityServicePreferenceFragment.this.onDialogButtonFromDisableToggleClicked(dialogInterface, i2);
                        }
                    });
                    break;
                } else {
                    return null;
                }
            default:
                this.mDialog = super.onCreateDialog(i);
                break;
        }
        return this.mDialog;
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment, com.android.settings.SettingsPreferenceFragment, com.android.settings.DialogCreatable
    public int getDialogMetricsCategory(int i) {
        if (i != 1008) {
            switch (i) {
                case 1002:
                case 1003:
                case 1004:
                    return 583;
                case 1005:
                    return 584;
                default:
                    return super.getDialogMetricsCategory(i);
            }
        }
        return 1810;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment
    public int getUserShortcutTypes() {
        return AccessibilityUtil.getUserShortcutTypesFromSettings(getPrefContext(), this.mComponentName);
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment
    protected void updateToggleServiceTitle(SettingsMainSwitchPreference settingsMainSwitchPreference) {
        AccessibilityServiceInfo accessibilityServiceInfo = getAccessibilityServiceInfo();
        settingsMainSwitchPreference.setTitle(accessibilityServiceInfo == null ? "" : getString(R.string.accessibility_service_primary_switch_title, accessibilityServiceInfo.getResolveInfo().loadLabel(getPackageManager())));
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment
    protected void updateSwitchBarToggleSwitch() {
        boolean isAccessibilityServiceEnabled = isAccessibilityServiceEnabled();
        if (this.mToggleServiceSwitchPreference.isChecked() == isAccessibilityServiceEnabled) {
            return;
        }
        this.mToggleServiceSwitchPreference.setChecked(isAccessibilityServiceEnabled);
    }

    private boolean isAccessibilityServiceEnabled() {
        return AccessibilityUtils.getEnabledServicesFromSettings(getPrefContext()).contains(this.mComponentName);
    }

    private boolean isFullDiskEncrypted() {
        return StorageManager.isNonDefaultBlockEncrypted();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 1) {
            if (i2 == -1) {
                handleConfirmServiceEnabled(true);
                if (!isFullDiskEncrypted()) {
                    return;
                }
                this.mLockPatternUtils.clearEncryptionPassword();
                Settings.Global.putInt(getContentResolver(), "require_password_to_decrypt", 0);
                return;
            }
            handleConfirmServiceEnabled(false);
        }
    }

    private void registerPackageRemoveReceiver() {
        if (this.mPackageRemovedReceiver != null || getContext() == null) {
            return;
        }
        this.mPackageRemovedReceiver = new BroadcastReceiver() { // from class: com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (TextUtils.equals(ToggleAccessibilityServicePreferenceFragment.this.mComponentName.getPackageName(), intent.getData().getSchemeSpecificPart())) {
                    ToggleAccessibilityServicePreferenceFragment.this.getActivity().finishAndRemoveTask();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme("package");
        getContext().registerReceiver(this.mPackageRemovedReceiver, intentFilter);
    }

    private void unregisterPackageRemoveReceiver() {
        if (this.mPackageRemovedReceiver == null || getContext() == null) {
            return;
        }
        getContext().unregisterReceiver(this.mPackageRemovedReceiver);
        this.mPackageRemovedReceiver = null;
    }

    private boolean isServiceSupportAccessibilityButton() {
        ServiceInfo serviceInfo;
        for (AccessibilityServiceInfo accessibilityServiceInfo : ((AccessibilityManager) getPrefContext().getSystemService(AccessibilityManager.class)).getInstalledAccessibilityServiceList()) {
            if ((accessibilityServiceInfo.flags & 256) != 0 && (serviceInfo = accessibilityServiceInfo.getResolveInfo().serviceInfo) != null && TextUtils.equals(serviceInfo.name, getAccessibilityServiceInfo().getResolveInfo().serviceInfo.name)) {
                return true;
            }
        }
        return false;
    }

    private void handleConfirmServiceEnabled(boolean z) {
        getArguments().putBoolean("checked", z);
        onPreferenceToggled(this.mPreferenceKey, z);
    }

    private String createConfirmCredentialReasonMessage() {
        int i = R.string.enable_service_password_reason;
        int keyguardStoredPasswordQuality = this.mLockPatternUtils.getKeyguardStoredPasswordQuality(UserHandle.myUserId());
        if (keyguardStoredPasswordQuality == 65536) {
            i = R.string.enable_service_pattern_reason;
        } else if (keyguardStoredPasswordQuality == 131072 || keyguardStoredPasswordQuality == 196608) {
            i = R.string.enable_service_pin_reason;
        }
        return getString(i, getAccessibilityServiceInfo().getResolveInfo().loadLabel(getPackageManager()));
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment, com.android.settingslib.widget.OnMainSwitchChangeListener
    public void onSwitchChanged(Switch r1, boolean z) {
        if (z != isAccessibilityServiceEnabled()) {
            onPreferenceClick(z);
        }
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment, com.android.settings.accessibility.ShortcutPreference.OnClickCallback
    public void onToggleClicked(ShortcutPreference shortcutPreference) {
        int retrieveUserShortcutType = PreferredShortcuts.retrieveUserShortcutType(getPrefContext(), this.mComponentName.flattenToString(), 1);
        if (shortcutPreference.isChecked()) {
            if (!this.mToggleServiceSwitchPreference.isChecked()) {
                shortcutPreference.setChecked(false);
                showPopupDialog(1004);
            } else {
                AccessibilityUtil.optInAllValuesToSettings(getPrefContext(), retrieveUserShortcutType, this.mComponentName);
                showPopupDialog(1008);
            }
        } else {
            AccessibilityUtil.optOutAllValuesFromSettings(getPrefContext(), retrieveUserShortcutType, this.mComponentName);
        }
        this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment, com.android.settings.accessibility.ShortcutPreference.OnClickCallback
    public void onSettingsClicked(ShortcutPreference shortcutPreference) {
        int i = 1;
        if (!(this.mShortcutPreference.isChecked() || this.mToggleServiceSwitchPreference.isChecked())) {
            i = 1003;
        }
        showPopupDialog(i);
    }

    @Override // com.android.settings.accessibility.ToggleFeaturePreferenceFragment
    protected void onProcessArguments(Bundle bundle) {
        super.onProcessArguments(bundle);
        String string = bundle.getString("settings_title");
        String string2 = bundle.getString("settings_component_name");
        if (!TextUtils.isEmpty(string) && !TextUtils.isEmpty(string2)) {
            Intent component = new Intent("android.intent.action.MAIN").setComponent(ComponentName.unflattenFromString(string2.toString()));
            if (!getPackageManager().queryIntentActivities(component, 0).isEmpty()) {
                this.mSettingsTitle = string;
                this.mSettingsIntent = component;
                setHasOptionsMenu(true);
            }
        }
        this.mComponentName = (ComponentName) bundle.getParcelable("component_name");
        int i = bundle.getInt("animated_image_res");
        if (i > 0) {
            this.mImageUri = new Uri.Builder().scheme("android.resource").authority(this.mComponentName.getPackageName()).appendPath(String.valueOf(i)).build();
        }
        this.mPackageName = getAccessibilityServiceInfo().getResolveInfo().loadLabel(getPackageManager());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDialogButtonFromDisableToggleClicked(DialogInterface dialogInterface, int i) {
        if (i == -2) {
            handleConfirmServiceEnabled(true);
        } else if (i == -1) {
            handleConfirmServiceEnabled(false);
        } else {
            throw new IllegalArgumentException("Unexpected button identifier");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDialogButtonFromEnableToggleClicked(View view) {
        int id = view.getId();
        if (id == R.id.permission_enable_allow_button) {
            onAllowButtonFromEnableToggleClicked();
        } else if (id == R.id.permission_enable_deny_button) {
            onDenyButtonFromEnableToggleClicked();
        } else {
            throw new IllegalArgumentException("Unexpected view id");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDialogButtonFromUninstallClicked() {
        this.mDialog.dismiss();
        Intent createUninstallPackageActivityIntent = createUninstallPackageActivityIntent();
        if (createUninstallPackageActivityIntent == null) {
            return;
        }
        startActivity(createUninstallPackageActivityIntent);
    }

    private Intent createUninstallPackageActivityIntent() {
        AccessibilityServiceInfo accessibilityServiceInfo = getAccessibilityServiceInfo();
        if (accessibilityServiceInfo == null) {
            Log.w("ToggleAccessibilityServicePreferenceFragment", "createUnInstallIntent -- invalid a11yServiceInfo");
            return null;
        }
        ApplicationInfo applicationInfo = accessibilityServiceInfo.getResolveInfo().serviceInfo.applicationInfo;
        return new Intent("android.intent.action.UNINSTALL_PACKAGE", Uri.parse("package:" + applicationInfo.packageName));
    }

    @Override // com.android.settingslib.core.lifecycle.ObservablePreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        unregisterPackageRemoveReceiver();
    }

    private void onAllowButtonFromEnableToggleClicked() {
        if (isFullDiskEncrypted()) {
            startActivityForResult(ConfirmDeviceCredentialActivity.createIntent(createConfirmCredentialReasonMessage(), null), 1);
        } else {
            handleConfirmServiceEnabled(true);
            if (isServiceSupportAccessibilityButton()) {
                this.mIsDialogShown.set(false);
                showPopupDialog(1008);
            }
        }
        this.mDialog.dismiss();
    }

    private void onDenyButtonFromEnableToggleClicked() {
        handleConfirmServiceEnabled(false);
        this.mDialog.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDialogButtonFromShortcutToggleClicked(View view) {
        int id = view.getId();
        if (id == R.id.permission_enable_allow_button) {
            onAllowButtonFromShortcutToggleClicked();
        } else if (id == R.id.permission_enable_deny_button) {
            onDenyButtonFromShortcutToggleClicked();
        } else {
            throw new IllegalArgumentException("Unexpected view id");
        }
    }

    private void onAllowButtonFromShortcutToggleClicked() {
        this.mShortcutPreference.setChecked(true);
        AccessibilityUtil.optInAllValuesToSettings(getPrefContext(), PreferredShortcuts.retrieveUserShortcutType(getPrefContext(), this.mComponentName.flattenToString(), 1), this.mComponentName);
        this.mIsDialogShown.set(false);
        showPopupDialog(1008);
        this.mDialog.dismiss();
        this.mShortcutPreference.setSummary(getShortcutTypeSummary(getPrefContext()));
    }

    private void onDenyButtonFromShortcutToggleClicked() {
        this.mShortcutPreference.setChecked(false);
        this.mDialog.dismiss();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onDialogButtonFromShortcutClicked(View view) {
        int id = view.getId();
        if (id == R.id.permission_enable_allow_button) {
            onAllowButtonFromShortcutClicked();
        } else if (id == R.id.permission_enable_deny_button) {
            onDenyButtonFromShortcutClicked();
        } else {
            throw new IllegalArgumentException("Unexpected view id");
        }
    }

    private void onAllowButtonFromShortcutClicked() {
        this.mIsDialogShown.set(false);
        showPopupDialog(1);
        this.mDialog.dismiss();
    }

    private void onDenyButtonFromShortcutClicked() {
        this.mDialog.dismiss();
    }

    private boolean onPreferenceClick(boolean z) {
        if (z) {
            this.mToggleServiceSwitchPreference.setChecked(false);
            getArguments().putBoolean("checked", false);
            if (!this.mShortcutPreference.isChecked()) {
                showPopupDialog(1002);
            } else {
                handleConfirmServiceEnabled(true);
                if (isServiceSupportAccessibilityButton()) {
                    showPopupDialog(1008);
                }
            }
        } else {
            this.mToggleServiceSwitchPreference.setChecked(true);
            getArguments().putBoolean("checked", true);
            showDialog(1005);
        }
        return true;
    }

    private void showPopupDialog(int i) {
        if (this.mIsDialogShown.compareAndSet(false, true)) {
            showDialog(i);
            setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.settings.accessibility.ToggleAccessibilityServicePreferenceFragment$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    ToggleAccessibilityServicePreferenceFragment.this.lambda$showPopupDialog$0(dialogInterface);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showPopupDialog$0(DialogInterface dialogInterface) {
        this.mIsDialogShown.compareAndSet(true, false);
    }
}
