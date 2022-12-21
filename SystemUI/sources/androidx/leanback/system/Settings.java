package androidx.leanback.system;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import androidx.leanback.widget.ShadowOverlayContainer;
import androidx.slice.core.SliceHints;
import java.util.Iterator;

public class Settings {
    private static final String ACTION_PARTNER_CUSTOMIZATION = "android.support.v17.leanback.action.PARTNER_CUSTOMIZATION";
    private static final boolean DEBUG = false;
    public static final String OUTLINE_CLIPPING_DISABLED = "OUTLINE_CLIPPING_DISABLED";
    public static final String PREFER_STATIC_SHADOWS = "PREFER_STATIC_SHADOWS";
    private static final String TAG = "Settings";
    private static Settings sInstance;
    private boolean mOutlineClippingDisabled;
    private boolean mPreferStaticShadows;

    public static Settings getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new Settings(context);
        }
        return sInstance;
    }

    private Settings(Context context) {
        generateSetting(getCustomizations(context), context);
    }

    public boolean preferStaticShadows() {
        return this.mPreferStaticShadows;
    }

    public boolean isOutlineClippingDisabled() {
        return this.mOutlineClippingDisabled;
    }

    public boolean getBoolean(String str) {
        return getOrSetBoolean(str, false, false);
    }

    public void setBoolean(String str, boolean z) {
        getOrSetBoolean(str, true, z);
    }

    /* access modifiers changed from: package-private */
    public boolean getOrSetBoolean(String str, boolean z, boolean z2) {
        if (str.compareTo(PREFER_STATIC_SHADOWS) == 0) {
            if (!z) {
                return this.mPreferStaticShadows;
            }
            this.mPreferStaticShadows = z2;
            return z2;
        } else if (str.compareTo(OUTLINE_CLIPPING_DISABLED) != 0) {
            throw new IllegalArgumentException("Invalid key");
        } else if (!z) {
            return this.mOutlineClippingDisabled;
        } else {
            this.mOutlineClippingDisabled = z2;
            return z2;
        }
    }

    private void generateSetting(Customizations customizations, Context context) {
        if (ShadowOverlayContainer.supportsDynamicShadow()) {
            this.mPreferStaticShadows = false;
            if (customizations != null) {
                this.mPreferStaticShadows = customizations.getBoolean("leanback_prefer_static_shadows", false);
            }
        } else {
            this.mPreferStaticShadows = true;
        }
        boolean isLowRamDevice = ((ActivityManager) context.getSystemService(SliceHints.HINT_ACTIVITY)).isLowRamDevice();
        this.mOutlineClippingDisabled = isLowRamDevice;
        if (customizations != null) {
            this.mOutlineClippingDisabled = customizations.getBoolean("leanback_outline_clipping_disabled", isLowRamDevice);
        }
    }

    static class Customizations {
        String mPackageName;
        Resources mResources;

        public Customizations(Resources resources, String str) {
            this.mResources = resources;
            this.mPackageName = str;
        }

        public boolean getBoolean(String str, boolean z) {
            int identifier = this.mResources.getIdentifier(str, "bool", this.mPackageName);
            return identifier > 0 ? this.mResources.getBoolean(identifier) : z;
        }
    }

    private Customizations getCustomizations(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Iterator<ResolveInfo> it = packageManager.queryBroadcastReceivers(new Intent(ACTION_PARTNER_CUSTOMIZATION), 0).iterator();
        Resources resources = null;
        String str = null;
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ResolveInfo next = it.next();
            String str2 = next.activityInfo.packageName;
            if (str2 != null && isSystemApp(next)) {
                try {
                    resources = packageManager.getResourcesForApplication(str2);
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
            if (resources != null) {
                str = str2;
                break;
            }
            str = str2;
        }
        if (resources == null) {
            return null;
        }
        return new Customizations(resources, str);
    }

    private static boolean isSystemApp(ResolveInfo resolveInfo) {
        return (resolveInfo.activityInfo == null || (resolveInfo.activityInfo.applicationInfo.flags & 1) == 0) ? false : true;
    }
}
