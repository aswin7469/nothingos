package com.android.settings.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.accessibilityservice.AccessibilityShortcutInfo;
import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import com.android.settings.R$drawable;
import com.android.settings.R$string;
import com.android.settings.Utils;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.RestrictedPreference;
import com.android.settingslib.accessibility.AccessibilityUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RestrictedPreferenceHelper {
    private final AppOpsManager mAppOps;
    private final Context mContext;
    private final DevicePolicyManager mDpm;
    private final PackageManager mPm;

    public RestrictedPreferenceHelper(Context context) {
        this.mContext = context;
        this.mDpm = (DevicePolicyManager) context.getSystemService(DevicePolicyManager.class);
        this.mPm = context.getPackageManager();
        this.mAppOps = (AppOpsManager) context.getSystemService(AppOpsManager.class);
    }

    public List<RestrictedPreference> createAccessibilityServicePreferenceList(List<AccessibilityServiceInfo> list) {
        Set<ComponentName> enabledServicesFromSettings = AccessibilityUtils.getEnabledServicesFromSettings(this.mContext);
        List permittedAccessibilityServices = this.mDpm.getPermittedAccessibilityServices(UserHandle.myUserId());
        int size = list.size();
        ArrayList arrayList = new ArrayList(size);
        int i = 0;
        while (i < size) {
            AccessibilityServiceInfo accessibilityServiceInfo = list.get(i);
            ResolveInfo resolveInfo = accessibilityServiceInfo.getResolveInfo();
            String str = resolveInfo.serviceInfo.packageName;
            ComponentName componentName = new ComponentName(str, resolveInfo.serviceInfo.name);
            String flattenToString = componentName.flattenToString();
            CharSequence loadLabel = resolveInfo.loadLabel(this.mPm);
            boolean contains = enabledServicesFromSettings.contains(componentName);
            CharSequence serviceSummary = AccessibilitySettings.getServiceSummary(this.mContext, accessibilityServiceInfo, contains);
            String accessibilityServiceFragmentTypeName = getAccessibilityServiceFragmentTypeName(accessibilityServiceInfo);
            Drawable loadIcon = resolveInfo.loadIcon(this.mPm);
            if (resolveInfo.getIconResource() == 0) {
                loadIcon = ContextCompat.getDrawable(this.mContext, R$drawable.ic_accessibility_generic);
            }
            Set<ComponentName> set = enabledServicesFromSettings;
            boolean z = contains;
            ComponentName componentName2 = componentName;
            String str2 = accessibilityServiceFragmentTypeName;
            String str3 = str;
            int i2 = size;
            ResolveInfo resolveInfo2 = resolveInfo;
            RestrictedPreference createRestrictedPreference = createRestrictedPreference(flattenToString, loadLabel, serviceSummary, loadIcon, str2, str, resolveInfo.serviceInfo.applicationInfo.uid);
            setRestrictedPreferenceEnabled(createRestrictedPreference, permittedAccessibilityServices, z);
            String key = createRestrictedPreference.getKey();
            int animatedImageRes = accessibilityServiceInfo.getAnimatedImageRes();
            CharSequence loadIntro = accessibilityServiceInfo.loadIntro(this.mPm);
            CharSequence serviceDescription = AccessibilitySettings.getServiceDescription(this.mContext, accessibilityServiceInfo, z);
            String loadHtmlDescription = accessibilityServiceInfo.loadHtmlDescription(this.mPm);
            RestrictedPreference restrictedPreference = createRestrictedPreference;
            int i3 = i;
            String tileServiceName = accessibilityServiceInfo.getTileServiceName();
            putBasicExtras(createRestrictedPreference, key, loadLabel, loadIntro, serviceDescription, animatedImageRes, loadHtmlDescription, componentName2);
            putServiceExtras(restrictedPreference, resolveInfo2, Boolean.valueOf(z));
            String str4 = str3;
            putSettingsExtras(restrictedPreference, str4, accessibilityServiceInfo.getSettingsActivityName());
            putTileServiceExtras(restrictedPreference, str4, tileServiceName);
            arrayList.add(restrictedPreference);
            i = i3 + 1;
            enabledServicesFromSettings = set;
            size = i2;
            permittedAccessibilityServices = permittedAccessibilityServices;
        }
        return arrayList;
    }

    public List<RestrictedPreference> createAccessibilityActivityPreferenceList(List<AccessibilityShortcutInfo> list) {
        Set<ComponentName> enabledServicesFromSettings = AccessibilityUtils.getEnabledServicesFromSettings(this.mContext);
        List permittedAccessibilityServices = this.mDpm.getPermittedAccessibilityServices(UserHandle.myUserId());
        int size = list.size();
        ArrayList arrayList = new ArrayList(size);
        int i = 0;
        while (i < size) {
            AccessibilityShortcutInfo accessibilityShortcutInfo = list.get(i);
            ActivityInfo activityInfo = accessibilityShortcutInfo.getActivityInfo();
            ComponentName componentName = accessibilityShortcutInfo.getComponentName();
            String flattenToString = componentName.flattenToString();
            CharSequence loadLabel = activityInfo.loadLabel(this.mPm);
            String loadSummary = accessibilityShortcutInfo.loadSummary(this.mPm);
            String name = LaunchAccessibilityActivityPreferenceFragment.class.getName();
            Drawable loadIcon = activityInfo.loadIcon(this.mPm);
            if (activityInfo.getIconResource() == 0) {
                loadIcon = ContextCompat.getDrawable(this.mContext, R$drawable.ic_accessibility_generic);
            }
            int i2 = size;
            ComponentName componentName2 = componentName;
            RestrictedPreference createRestrictedPreference = createRestrictedPreference(flattenToString, loadLabel, loadSummary, loadIcon, name, componentName.getPackageName(), activityInfo.applicationInfo.uid);
            setRestrictedPreferenceEnabled(createRestrictedPreference, permittedAccessibilityServices, enabledServicesFromSettings.contains(componentName2));
            String key = createRestrictedPreference.getKey();
            String loadIntro = accessibilityShortcutInfo.loadIntro(this.mPm);
            String loadDescription = accessibilityShortcutInfo.loadDescription(this.mPm);
            int animatedImageRes = accessibilityShortcutInfo.getAnimatedImageRes();
            String loadHtmlDescription = accessibilityShortcutInfo.loadHtmlDescription(this.mPm);
            Set<ComponentName> set = enabledServicesFromSettings;
            String settingsActivityName = accessibilityShortcutInfo.getSettingsActivityName();
            CharSequence charSequence = loadLabel;
            List list2 = permittedAccessibilityServices;
            RestrictedPreference restrictedPreference = createRestrictedPreference;
            String tileServiceName = accessibilityShortcutInfo.getTileServiceName();
            putBasicExtras(createRestrictedPreference, key, charSequence, loadIntro, loadDescription, animatedImageRes, loadHtmlDescription, componentName2);
            putSettingsExtras(restrictedPreference, componentName2.getPackageName(), settingsActivityName);
            putTileServiceExtras(restrictedPreference, componentName2.getPackageName(), tileServiceName);
            arrayList.add(restrictedPreference);
            i++;
            permittedAccessibilityServices = list2;
            size = i2;
            enabledServicesFromSettings = set;
        }
        return arrayList;
    }

    private String getAccessibilityServiceFragmentTypeName(AccessibilityServiceInfo accessibilityServiceInfo) {
        int accessibilityServiceFragmentType = AccessibilityUtil.getAccessibilityServiceFragmentType(accessibilityServiceInfo);
        if (accessibilityServiceFragmentType == 0) {
            return VolumeShortcutToggleAccessibilityServicePreferenceFragment.class.getName();
        }
        if (accessibilityServiceFragmentType == 1) {
            return InvisibleToggleAccessibilityServicePreferenceFragment.class.getName();
        }
        if (accessibilityServiceFragmentType == 2) {
            return ToggleAccessibilityServicePreferenceFragment.class.getName();
        }
        throw new IllegalArgumentException("Unsupported accessibility fragment type " + accessibilityServiceFragmentType);
    }

    private RestrictedPreference createRestrictedPreference(String str, CharSequence charSequence, CharSequence charSequence2, Drawable drawable, String str2, String str3, int i) {
        RestrictedPreference restrictedPreference = new RestrictedPreference(this.mContext, str3, i);
        restrictedPreference.setKey(str);
        restrictedPreference.setTitle(charSequence);
        restrictedPreference.setSummary(charSequence2);
        restrictedPreference.setIcon(Utils.getAdaptiveIcon(this.mContext, drawable, -1));
        restrictedPreference.setFragment(str2);
        restrictedPreference.setIconSize(1);
        restrictedPreference.setPersistent(false);
        restrictedPreference.setOrder(-1);
        return restrictedPreference;
    }

    private void setRestrictedPreferenceEnabled(RestrictedPreference restrictedPreference, List<String> list, boolean z) {
        boolean z2;
        boolean z3 = list == null || list.contains(restrictedPreference.getPackageName());
        if (z3) {
            try {
                z3 = !this.mContext.getResources().getBoolean(17891657) || this.mAppOps.noteOpNoThrow(119, restrictedPreference.getUid(), restrictedPreference.getPackageName()) == 0;
                z2 = z3;
            } catch (Exception unused) {
                z2 = true;
            }
        } else {
            z2 = false;
        }
        if (z3 || z) {
            restrictedPreference.setEnabled(true);
            return;
        }
        RestrictedLockUtils.EnforcedAdmin checkIfAccessibilityServiceDisallowed = RestrictedLockUtilsInternal.checkIfAccessibilityServiceDisallowed(this.mContext, restrictedPreference.getPackageName(), UserHandle.myUserId());
        if (checkIfAccessibilityServiceDisallowed != null) {
            restrictedPreference.setDisabledByAdmin(checkIfAccessibilityServiceDisallowed);
        } else if (!z2) {
            restrictedPreference.setDisabledByAppOps(true);
        } else {
            restrictedPreference.setEnabled(false);
        }
    }

    private void putBasicExtras(RestrictedPreference restrictedPreference, String str, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, String str2, ComponentName componentName) {
        Bundle extras = restrictedPreference.getExtras();
        extras.putString("preference_key", str);
        extras.putCharSequence("title", charSequence);
        extras.putCharSequence("intro", charSequence2);
        extras.putCharSequence("summary", charSequence3);
        extras.putParcelable("component_name", componentName);
        extras.putInt("animated_image_res", i);
        extras.putString("html_description", str2);
    }

    private void putServiceExtras(RestrictedPreference restrictedPreference, ResolveInfo resolveInfo, Boolean bool) {
        Bundle extras = restrictedPreference.getExtras();
        extras.putParcelable("resolve_info", resolveInfo);
        extras.putBoolean("checked", bool.booleanValue());
    }

    private void putSettingsExtras(RestrictedPreference restrictedPreference, String str, String str2) {
        Bundle extras = restrictedPreference.getExtras();
        if (!TextUtils.isEmpty(str2)) {
            extras.putString("settings_title", this.mContext.getText(R$string.accessibility_menu_item_settings).toString());
            extras.putString("settings_component_name", new ComponentName(str, str2).flattenToString());
        }
    }

    private void putTileServiceExtras(RestrictedPreference restrictedPreference, String str, String str2) {
        Bundle extras = restrictedPreference.getExtras();
        if (!TextUtils.isEmpty(str2)) {
            extras.putString("tile_service_component_name", new ComponentName(str, str2).flattenToString());
        }
    }
}
