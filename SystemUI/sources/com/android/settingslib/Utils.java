package com.android.settingslib;

import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.UserInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.NetworkCapabilities;
import android.net.TetheringManager;
import android.net.vcn.VcnTransportInfo;
import android.net.wifi.WifiInfo;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import com.android.internal.util.UserIcons;
import com.android.settingslib.drawable.UserIconDrawable;
import com.android.settingslib.fuelgauge.BatteryStatus;
import com.android.settingslib.utils.BuildCompatUtils;
import java.text.NumberFormat;

public class Utils {
    static final int[] SHOW_X_WIFI_PIE = {C1757R.C1759drawable.ic_show_x_wifi_signal_0, C1757R.C1759drawable.ic_show_x_wifi_signal_1, C1757R.C1759drawable.ic_show_x_wifi_signal_2, C1757R.C1759drawable.ic_show_x_wifi_signal_3, C1757R.C1759drawable.ic_show_x_wifi_signal_4};
    static final String STORAGE_MANAGER_ENABLED_PROPERTY = "ro.storage_manager.enabled";
    static final int[] WIFI_4_PIE = {17302898, 17302899, 17302900, 17302901, 17302902};
    static final int[] WIFI_5_PIE = {17302903, 17302904, 17302905, 17302906, 17302907};
    static final int[] WIFI_6_PIE = {17302908, 17302909, 17302910, 17302911, 17302912};
    static final int[] WIFI_PIE = {17302913, 17302914, 17302915, 17302916, 17302917};
    private static String sPermissionControllerPackageName;
    private static String sServicesSystemSharedLibPackageName;
    private static String sSharedSystemSharedLibPackageName;
    private static Signature[] sSystemSignature;

    public static Drawable getBadgedIcon(Context context, Drawable drawable, UserHandle userHandle) {
        return drawable;
    }

    public static void updateLocationEnabled(Context context, boolean z, int i, int i2) {
        Settings.Secure.putIntForUser(context.getContentResolver(), "location_changer", i2, i);
        ((LocationManager) context.getSystemService(LocationManager.class)).setLocationEnabledForUser(z, UserHandle.of(i));
    }

    public static int getTetheringLabel(TetheringManager tetheringManager) {
        String[] tetherableUsbRegexs = tetheringManager.getTetherableUsbRegexs();
        String[] tetherableWifiRegexs = tetheringManager.getTetherableWifiRegexs();
        String[] tetherableBluetoothRegexs = tetheringManager.getTetherableBluetoothRegexs();
        boolean z = true;
        boolean z2 = tetherableUsbRegexs.length != 0;
        boolean z3 = tetherableWifiRegexs.length != 0;
        if (tetherableBluetoothRegexs.length == 0) {
            z = false;
        }
        if (z3 && z2 && z) {
            return C1757R.string.tether_settings_title_all;
        }
        if (z3 && z2) {
            return C1757R.string.tether_settings_title_all;
        }
        if (z3 && z) {
            return C1757R.string.tether_settings_title_all;
        }
        if (z3) {
            return C1757R.string.tether_settings_title_wifi;
        }
        if (z2 && z) {
            return C1757R.string.tether_settings_title_usb_bluetooth;
        }
        if (z2) {
            return C1757R.string.tether_settings_title_usb;
        }
        return C1757R.string.tether_settings_title_bluetooth;
    }

    public static String getUserLabel(Context context, UserInfo userInfo) {
        String str = userInfo != null ? userInfo.name : null;
        if (!userInfo.isManagedProfile()) {
            if (userInfo.isGuest()) {
                str = context.getString(17040431);
            }
            if (str == null && userInfo != null) {
                str = Integer.toString(userInfo.id);
            } else if (userInfo == null) {
                str = context.getString(C1757R.string.unknown);
            }
            return context.getResources().getString(C1757R.string.running_process_item_user_label, new Object[]{str});
        } else if (BuildCompatUtils.isAtLeastT()) {
            return getUpdatableManagedUserTitle(context);
        } else {
            return context.getString(C1757R.string.managed_user_title);
        }
    }

    private static String getUpdatableManagedUserTitle(Context context) {
        return ((DevicePolicyManager) context.getSystemService(DevicePolicyManager.class)).getResources().getString("Settings.WORK_PROFILE_USER_LABEL", new Utils$$ExternalSyntheticLambda0(context));
    }

    public static Drawable getUserIcon(Context context, UserManager userManager, UserInfo userInfo) {
        Bitmap userIcon;
        int defaultSize = UserIconDrawable.getDefaultSize(context);
        if (userInfo.isManagedProfile()) {
            Drawable managedUserDrawable = UserIconDrawable.getManagedUserDrawable(context);
            managedUserDrawable.setBounds(0, 0, defaultSize, defaultSize);
            return managedUserDrawable;
        } else if (userInfo.iconPath == null || (userIcon = userManager.getUserIcon(userInfo.id)) == null) {
            return new UserIconDrawable(defaultSize).setIconDrawable(UserIcons.getDefaultUserIcon(context.getResources(), userInfo.id, false)).bake();
        } else {
            return new UserIconDrawable(defaultSize).setIcon(userIcon).bake();
        }
    }

    public static String formatPercentage(double d, boolean z) {
        return formatPercentage(z ? Math.round((float) d) : (int) d);
    }

    public static String formatPercentage(long j, long j2) {
        return formatPercentage(((double) j) / ((double) j2));
    }

    public static String formatPercentage(int i) {
        return formatPercentage(((double) i) / 100.0d);
    }

    public static String formatPercentage(double d) {
        return NumberFormat.getPercentInstance().format(d);
    }

    public static int getBatteryLevel(Intent intent) {
        return (intent.getIntExtra("level", 0) * 100) / intent.getIntExtra("scale", 100);
    }

    public static String getBatteryStatus(Context context, Intent intent, boolean z) {
        int i;
        int intExtra = intent.getIntExtra("status", 1);
        Resources resources = context.getResources();
        String string = resources.getString(C1757R.string.battery_info_status_unknown);
        BatteryStatus batteryStatus = new BatteryStatus(intent);
        if (batteryStatus.isCharged()) {
            if (z) {
                i = C1757R.string.battery_info_status_full_charged;
            } else {
                i = C1757R.string.battery_info_status_full;
            }
            return resources.getString(i);
        } else if (intExtra == 2) {
            if (z) {
                return resources.getString(C1757R.string.battery_info_status_charging);
            }
            if (!batteryStatus.isPluggedInWired()) {
                return resources.getString(C1757R.string.battery_info_status_charging_wireless);
            }
            int chargingSpeed = batteryStatus.getChargingSpeed(context);
            if (chargingSpeed != 0) {
                return chargingSpeed != 2 ? resources.getString(C1757R.string.battery_info_status_charging) : resources.getString(C1757R.string.battery_info_status_charging_fast);
            }
            return resources.getString(C1757R.string.battery_info_status_charging_slow);
        } else if (intExtra == 3) {
            return resources.getString(C1757R.string.battery_info_status_discharging);
        } else {
            return intExtra == 4 ? resources.getString(C1757R.string.battery_info_status_not_charging) : string;
        }
    }

    public static ColorStateList getColorAccent(Context context) {
        return getColorAttr(context, 16843829);
    }

    public static ColorStateList getColorError(Context context) {
        return getColorAttr(context, 16844099);
    }

    public static int getColorAccentDefaultColor(Context context) {
        return getColorAttrDefaultColor(context, 16843829);
    }

    public static int getColorErrorDefaultColor(Context context) {
        return getColorAttrDefaultColor(context, 16844099);
    }

    public static int getColorStateListDefaultColor(Context context, int i) {
        return context.getResources().getColorStateList(i, context.getTheme()).getDefaultColor();
    }

    public static int getDisabled(Context context, int i) {
        return applyAlphaAttr(context, 16842803, i);
    }

    public static int applyAlphaAttr(Context context, int i, int i2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        float f = obtainStyledAttributes.getFloat(0, 0.0f);
        obtainStyledAttributes.recycle();
        return applyAlpha(f, i2);
    }

    public static int applyAlpha(float f, int i) {
        return Color.argb((int) (f * ((float) Color.alpha(i))), Color.red(i), Color.green(i), Color.blue(i));
    }

    public static int getColorAttrDefaultColor(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        return color;
    }

    public static ColorStateList getColorAttr(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        try {
            return obtainStyledAttributes.getColorStateList(0);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public static int getThemeAttr(Context context, int i) {
        return getThemeAttr(context, i, 0);
    }

    public static int getThemeAttr(Context context, int i, int i2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        int resourceId = obtainStyledAttributes.getResourceId(0, i2);
        obtainStyledAttributes.recycle();
        return resourceId;
    }

    public static Drawable getDrawable(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        Drawable drawable = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
        return drawable;
    }

    public static ColorMatrix getAlphaInvariantColorMatrixForColor(int i) {
        return new ColorMatrix(new float[]{0.0f, 0.0f, 0.0f, 0.0f, (float) Color.red(i), 0.0f, 0.0f, 0.0f, 0.0f, (float) Color.green(i), 0.0f, 0.0f, 0.0f, 0.0f, (float) Color.blue(i), 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
    }

    public static ColorFilter getAlphaInvariantColorFilterForColor(int i) {
        return new ColorMatrixColorFilter(getAlphaInvariantColorMatrixForColor(i));
    }

    public static boolean isSystemPackage(Resources resources, PackageManager packageManager, PackageInfo packageInfo) {
        if (sSystemSignature == null) {
            sSystemSignature = new Signature[]{getSystemSignature(packageManager)};
        }
        if (sPermissionControllerPackageName == null) {
            sPermissionControllerPackageName = packageManager.getPermissionControllerPackageName();
        }
        if (sServicesSystemSharedLibPackageName == null) {
            sServicesSystemSharedLibPackageName = packageManager.getServicesSystemSharedLibraryPackageName();
        }
        if (sSharedSystemSharedLibPackageName == null) {
            sSharedSystemSharedLibPackageName = packageManager.getSharedSystemSharedLibraryPackageName();
        }
        Signature signature = sSystemSignature[0];
        if ((signature == null || !signature.equals(getFirstSignature(packageInfo))) && !packageInfo.packageName.equals(sPermissionControllerPackageName) && !packageInfo.packageName.equals(sServicesSystemSharedLibPackageName) && !packageInfo.packageName.equals(sSharedSystemSharedLibPackageName) && !packageInfo.packageName.equals("com.android.printspooler") && !isDeviceProvisioningPackage(resources, packageInfo.packageName)) {
            return false;
        }
        return true;
    }

    private static Signature getFirstSignature(PackageInfo packageInfo) {
        if (packageInfo == null || packageInfo.signatures == null || packageInfo.signatures.length <= 0) {
            return null;
        }
        return packageInfo.signatures[0];
    }

    private static Signature getSystemSignature(PackageManager packageManager) {
        try {
            return getFirstSignature(packageManager.getPackageInfo("android", 64));
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static boolean isDeviceProvisioningPackage(Resources resources, String str) {
        String string = resources.getString(17039955);
        return string != null && string.equals(str);
    }

    public static int getWifiIconResource(int i) {
        return getWifiIconResource(false, i, 0);
    }

    public static int getWifiIconResource(boolean z, int i) {
        return getWifiIconResource(z, i, 0);
    }

    public static int getWifiIconResource(int i, int i2) {
        return getWifiIconResource(false, i, i2);
    }

    public static int getWifiIconResource(boolean z, int i, int i2) {
        if (i >= 0) {
            int[] iArr = WIFI_PIE;
            if (i < iArr.length) {
                if (z) {
                    return SHOW_X_WIFI_PIE[i];
                }
                if (i2 == 4) {
                    return WIFI_4_PIE[i];
                }
                if (i2 == 5) {
                    return WIFI_5_PIE[i];
                }
                if (i2 != 6) {
                    return iArr[i];
                }
                return WIFI_6_PIE[i];
            }
        }
        throw new IllegalArgumentException("No Wifi icon found for level: " + i);
    }

    public static int getDefaultStorageManagerDaysToRetain(Resources resources) {
        try {
            return resources.getInteger(17694948);
        } catch (Resources.NotFoundException unused) {
            return 90;
        }
    }

    public static boolean isWifiOnly(Context context) {
        return !((TelephonyManager) context.getSystemService(TelephonyManager.class)).isDataCapable();
    }

    public static boolean isStorageManagerEnabled(Context context) {
        boolean z;
        try {
            z = SystemProperties.getBoolean(STORAGE_MANAGER_ENABLED_PROPERTY, false);
        } catch (Resources.NotFoundException unused) {
            z = false;
        }
        if (Settings.Secure.getInt(context.getContentResolver(), "automatic_storage_manager_enabled", z ? 1 : 0) != 0) {
            return true;
        }
        return false;
    }

    public static boolean isAudioModeOngoingCall(Context context) {
        int mode = ((AudioManager) context.getSystemService(AudioManager.class)).getMode();
        return mode == 1 || mode == 2 || mode == 3;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:3:0x0004, code lost:
        r3 = getCombinedServiceState(r3);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isInService(android.telephony.ServiceState r3) {
        /*
            r0 = 0
            if (r3 != 0) goto L_0x0004
            return r0
        L_0x0004:
            int r3 = getCombinedServiceState(r3)
            r1 = 3
            if (r3 == r1) goto L_0x0013
            r1 = 1
            if (r3 == r1) goto L_0x0013
            r2 = 2
            if (r3 != r2) goto L_0x0012
            goto L_0x0013
        L_0x0012:
            return r1
        L_0x0013:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.Utils.isInService(android.telephony.ServiceState):boolean");
    }

    public static int getCombinedServiceState(ServiceState serviceState) {
        if (serviceState == null) {
            return 1;
        }
        int state = serviceState.getState();
        int dataRegistrationState = serviceState.getDataRegistrationState();
        if ((state == 1 || state == 2) && dataRegistrationState == 0 && isNotInIwlan(serviceState)) {
            return 0;
        }
        return state;
    }

    public static Drawable getBadgedIcon(Context context, ApplicationInfo applicationInfo) {
        return getBadgedIcon(context, applicationInfo.loadUnbadgedIcon(context.getPackageManager()), UserHandle.getUserHandleForUid(applicationInfo.uid));
    }

    private static boolean isNotInIwlan(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 2);
        if (networkRegistrationInfo == null) {
            return true;
        }
        return !(networkRegistrationInfo.getRegistrationState() == 1 || networkRegistrationInfo.getRegistrationState() == 5);
    }

    public static Bitmap convertCornerRadiusBitmap(Context context, Bitmap bitmap, float f) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        create.setAntiAlias(true);
        create.setCornerRadius(f);
        Canvas canvas = new Canvas(createBitmap);
        create.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        create.draw(canvas);
        return createBitmap;
    }

    public static WifiInfo tryGetWifiInfoForVcn(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities.getTransportInfo() == null || !(networkCapabilities.getTransportInfo() instanceof VcnTransportInfo)) {
            return null;
        }
        return networkCapabilities.getTransportInfo().getWifiInfo();
    }
}
