package com.android.settingslib.dream;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.service.dreams.DreamService;
import android.service.dreams.IDreamManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.settingslib.C1757R;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DreamBackend {
    public static final int COMPLICATION_TYPE_AIR_QUALITY = 4;
    public static final int COMPLICATION_TYPE_CAST_INFO = 5;
    public static final int COMPLICATION_TYPE_DATE = 2;
    public static final int COMPLICATION_TYPE_TIME = 1;
    public static final int COMPLICATION_TYPE_WEATHER = 3;
    private static final boolean DEBUG = false;
    public static final int EITHER = 2;
    public static final int NEVER = 3;
    private static final String TAG = "DreamBackend";
    public static final int WHILE_CHARGING = 0;
    public static final int WHILE_DOCKED = 1;
    private static DreamBackend sInstance;
    private final DreamInfoComparator mComparator = new DreamInfoComparator(getDefaultDream());
    private final Context mContext;
    private final Set<Integer> mDefaultEnabledComplications;
    private final Set<ComponentName> mDisabledDreams;
    private final IDreamManager mDreamManager = IDreamManager.Stub.asInterface(ServiceManager.getService("dreams"));
    private final boolean mDreamsActivatedOnDockByDefault;
    private final boolean mDreamsActivatedOnSleepByDefault;
    private final boolean mDreamsEnabledByDefault;
    private final Set<Integer> mSupportedComplications;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ComplicationType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface WhenToDream {
    }

    private static void logd(String str, Object... objArr) {
    }

    public static class DreamInfo {
        public CharSequence caption;
        public ComponentName componentName;
        public CharSequence description;
        public Drawable icon;
        public boolean isActive;
        public Drawable previewImage;
        public ComponentName settingsComponentName;

        public String toString() {
            StringBuilder sb = new StringBuilder(DreamInfo.class.getSimpleName());
            sb.append('[').append(this.caption);
            if (this.isActive) {
                sb.append(",active");
            }
            sb.append(',').append((Object) this.componentName);
            if (this.settingsComponentName != null) {
                sb.append("settings=").append((Object) this.settingsComponentName);
            }
            return sb.append(']').toString();
        }
    }

    public static DreamBackend getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DreamBackend(context);
        }
        return sInstance;
    }

    public DreamBackend(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        Resources resources = applicationContext.getResources();
        this.mDreamsEnabledByDefault = resources.getBoolean(17891617);
        this.mDreamsActivatedOnSleepByDefault = resources.getBoolean(17891616);
        this.mDreamsActivatedOnDockByDefault = resources.getBoolean(17891615);
        this.mDisabledDreams = (Set) Arrays.stream((T[]) resources.getStringArray(17236032)).map(new DreamBackend$$ExternalSyntheticLambda1()).collect(Collectors.toSet());
        Set<Integer> set = (Set) Arrays.stream(resources.getIntArray(17236132)).boxed().collect(Collectors.toSet());
        this.mSupportedComplications = set;
        Stream<Integer> boxed = Arrays.stream(resources.getIntArray(17236056)).boxed();
        Objects.requireNonNull(set);
        this.mDefaultEnabledComplications = (Set) boxed.filter(new DreamBackend$$ExternalSyntheticLambda2(set)).collect(Collectors.toSet());
    }

    public List<DreamInfo> getDreamInfos() {
        logd("getDreamInfos()", new Object[0]);
        ComponentName activeDream = getActiveDream();
        PackageManager packageManager = this.mContext.getPackageManager();
        List<ResolveInfo> queryIntentServices = packageManager.queryIntentServices(new Intent("android.service.dreams.DreamService"), 128);
        ArrayList arrayList = new ArrayList(queryIntentServices.size());
        for (ResolveInfo next : queryIntentServices) {
            ComponentName dreamComponentName = getDreamComponentName(next);
            if (dreamComponentName != null && !this.mDisabledDreams.contains(dreamComponentName)) {
                DreamInfo dreamInfo = new DreamInfo();
                dreamInfo.caption = next.loadLabel(packageManager);
                dreamInfo.icon = next.loadIcon(packageManager);
                dreamInfo.description = getDescription(next, packageManager);
                dreamInfo.componentName = dreamComponentName;
                dreamInfo.isActive = dreamInfo.componentName.equals(activeDream);
                DreamService.DreamMetadata dreamMetadata = DreamService.getDreamMetadata(this.mContext, next.serviceInfo);
                if (dreamMetadata != null) {
                    dreamInfo.settingsComponentName = dreamMetadata.settingsActivity;
                    dreamInfo.previewImage = dreamMetadata.previewImage;
                }
                arrayList.add(dreamInfo);
            }
        }
        arrayList.sort(this.mComparator);
        return arrayList;
    }

    private static CharSequence getDescription(ResolveInfo resolveInfo, PackageManager packageManager) {
        ApplicationInfo applicationInfo;
        String str = resolveInfo.resolvePackageName;
        if (str == null) {
            str = resolveInfo.serviceInfo.packageName;
            applicationInfo = resolveInfo.serviceInfo.applicationInfo;
        } else {
            applicationInfo = null;
        }
        if (resolveInfo.serviceInfo.descriptionRes != 0) {
            return packageManager.getText(str, resolveInfo.serviceInfo.descriptionRes, applicationInfo);
        }
        return null;
    }

    public ComponentName getDefaultDream() {
        IDreamManager iDreamManager = this.mDreamManager;
        if (iDreamManager == null) {
            return null;
        }
        try {
            return iDreamManager.getDefaultDreamComponentForUser(this.mContext.getUserId());
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to get default dream", e);
            return null;
        }
    }

    public CharSequence getActiveDreamName() {
        ComponentName activeDream = getActiveDream();
        if (activeDream != null) {
            PackageManager packageManager = this.mContext.getPackageManager();
            try {
                ServiceInfo serviceInfo = packageManager.getServiceInfo(activeDream, 0);
                if (serviceInfo != null) {
                    return serviceInfo.loadLabel(packageManager);
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return null;
    }

    public Drawable getActiveIcon() {
        ComponentName activeDream = getActiveDream();
        if (activeDream != null) {
            PackageManager packageManager = this.mContext.getPackageManager();
            try {
                ServiceInfo serviceInfo = packageManager.getServiceInfo(activeDream, 0);
                if (serviceInfo != null) {
                    return serviceInfo.loadIcon(packageManager);
                }
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return null;
    }

    public int getWhenToDreamSetting() {
        if (isActivatedOnDock() && isActivatedOnSleep()) {
            return 2;
        }
        if (isActivatedOnDock()) {
            return 1;
        }
        return isActivatedOnSleep() ? 0 : 3;
    }

    public void setWhenToDream(int i) {
        setEnabled(i != 3);
        if (i == 0) {
            setActivatedOnDock(false);
            setActivatedOnSleep(true);
        } else if (i == 1) {
            setActivatedOnDock(true);
            setActivatedOnSleep(false);
        } else if (i == 2) {
            setActivatedOnDock(true);
            setActivatedOnSleep(true);
        }
    }

    public boolean isComplicationEnabled(int i) {
        return getEnabledComplications().contains(Integer.valueOf(i));
    }

    public Set<Integer> getEnabledComplications() {
        String string = Settings.Secure.getString(this.mContext.getContentResolver(), "screensaver_enabled_complications");
        if (string == null) {
            return this.mDefaultEnabledComplications;
        }
        return parseFromString(string);
    }

    public Set<Integer> getSupportedComplications() {
        return this.mSupportedComplications;
    }

    public void setComplicationEnabled(int i, boolean z) {
        if (this.mSupportedComplications.contains(Integer.valueOf(i))) {
            Set<Integer> enabledComplications = getEnabledComplications();
            if (z) {
                enabledComplications.add(Integer.valueOf(i));
            } else {
                enabledComplications.remove(Integer.valueOf(i));
            }
            Settings.Secure.putString(this.mContext.getContentResolver(), "screensaver_enabled_complications", convertToString(enabledComplications));
        }
    }

    public CharSequence getComplicationTitle(int i) {
        int i2;
        if (i == 1) {
            i2 = C1757R.string.dream_complication_title_time;
        } else if (i == 2) {
            i2 = C1757R.string.dream_complication_title_date;
        } else if (i == 3) {
            i2 = C1757R.string.dream_complication_title_weather;
        } else if (i == 4) {
            i2 = C1757R.string.dream_complication_title_aqi;
        } else if (i != 5) {
            return null;
        } else {
            i2 = C1757R.string.dream_complication_title_cast_info;
        }
        return this.mContext.getString(i2);
    }

    private static String convertToString(Set<Integer> set) {
        return (String) set.stream().map(new DreamBackend$$ExternalSyntheticLambda3()).collect(Collectors.joining(NavigationBarInflaterView.BUTTON_SEPARATOR));
    }

    private static Set<Integer> parseFromString(String str) {
        if (TextUtils.isEmpty(str)) {
            return new HashSet();
        }
        return (Set) Arrays.stream((T[]) str.split(NavigationBarInflaterView.BUTTON_SEPARATOR)).map(new DreamBackend$$ExternalSyntheticLambda0()).collect(Collectors.toSet());
    }

    public boolean isEnabled() {
        return getBoolean("screensaver_enabled", this.mDreamsEnabledByDefault);
    }

    public void setEnabled(boolean z) {
        logd("setEnabled(%s)", Boolean.valueOf(z));
        setBoolean("screensaver_enabled", z);
    }

    public boolean isActivatedOnDock() {
        return getBoolean("screensaver_activate_on_dock", this.mDreamsActivatedOnDockByDefault);
    }

    public void setActivatedOnDock(boolean z) {
        logd("setActivatedOnDock(%s)", Boolean.valueOf(z));
        setBoolean("screensaver_activate_on_dock", z);
    }

    public boolean isActivatedOnSleep() {
        return getBoolean("screensaver_activate_on_sleep", this.mDreamsActivatedOnSleepByDefault);
    }

    public void setActivatedOnSleep(boolean z) {
        logd("setActivatedOnSleep(%s)", Boolean.valueOf(z));
        setBoolean("screensaver_activate_on_sleep", z);
    }

    private boolean getBoolean(String str, boolean z) {
        return Settings.Secure.getInt(this.mContext.getContentResolver(), str, z ? 1 : 0) == 1;
    }

    private void setBoolean(String str, boolean z) {
        Settings.Secure.putInt(this.mContext.getContentResolver(), str, z ? 1 : 0);
    }

    public void setActiveDream(ComponentName componentName) {
        logd("setActiveDream(%s)", componentName);
        IDreamManager iDreamManager = this.mDreamManager;
        if (iDreamManager != null) {
            try {
                ComponentName[] componentNameArr = {componentName};
                if (componentName == null) {
                    componentNameArr = null;
                }
                iDreamManager.setDreamComponents(componentNameArr);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to set active dream to " + componentName, e);
            }
        }
    }

    public ComponentName getActiveDream() {
        IDreamManager iDreamManager = this.mDreamManager;
        if (iDreamManager == null) {
            return null;
        }
        try {
            ComponentName[] dreamComponents = iDreamManager.getDreamComponents();
            if (dreamComponents == null || dreamComponents.length <= 0) {
                return null;
            }
            return dreamComponents[0];
        } catch (RemoteException e) {
            Log.w(TAG, "Failed to get active dream", e);
            return null;
        }
    }

    public void launchSettings(Context context, DreamInfo dreamInfo) {
        logd("launchSettings(%s)", dreamInfo);
        if (dreamInfo != null && dreamInfo.settingsComponentName != null) {
            context.startActivity(new Intent().setComponent(dreamInfo.settingsComponentName).addFlags(276824064));
        }
    }

    public void preview(ComponentName componentName) {
        logd("preview(%s)", componentName);
        IDreamManager iDreamManager = this.mDreamManager;
        if (iDreamManager != null && componentName != null) {
            try {
                iDreamManager.testDream(this.mContext.getUserId(), componentName);
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to preview " + componentName, e);
            }
        }
    }

    public void startDreaming() {
        logd("startDreaming()", new Object[0]);
        IDreamManager iDreamManager = this.mDreamManager;
        if (iDreamManager != null) {
            try {
                iDreamManager.dream();
            } catch (RemoteException e) {
                Log.w(TAG, "Failed to dream", e);
            }
        }
    }

    private static ComponentName getDreamComponentName(ResolveInfo resolveInfo) {
        if (resolveInfo == null || resolveInfo.serviceInfo == null) {
            return null;
        }
        return new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
    }

    private static class DreamInfoComparator implements Comparator<DreamInfo> {
        private final ComponentName mDefaultDream;

        public DreamInfoComparator(ComponentName componentName) {
            this.mDefaultDream = componentName;
        }

        public int compare(DreamInfo dreamInfo, DreamInfo dreamInfo2) {
            return sortKey(dreamInfo).compareTo(sortKey(dreamInfo2));
        }

        private String sortKey(DreamInfo dreamInfo) {
            StringBuilder sb = new StringBuilder();
            sb.append(dreamInfo.componentName.equals(this.mDefaultDream) ? '0' : '1');
            sb.append(dreamInfo.caption);
            return sb.toString();
        }
    }
}
