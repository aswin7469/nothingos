package com.google.android.setupdesign.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.util.TypedValue;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Partner {
    private static final String ACTION_PARTNER_CUSTOMIZATION = "com.android.setupwizard.action.PARTNER_CUSTOMIZATION";
    private static final String TAG = "(setupdesign) Partner";
    private static Partner partner = null;
    private static boolean searched = false;
    private final String packageName;
    private final Resources resources;

    public static Set<String> getStringArray(Context context, int i) {
        ResourceEntry resourceEntry = getResourceEntry(context, i);
        return new HashSet(Arrays.asList(resourceEntry.resources.getStringArray(resourceEntry.f458id)));
    }

    public static boolean getBoolean(Context context, int i) {
        ResourceEntry resourceEntry = getResourceEntry(context, i);
        return resourceEntry.resources.getBoolean(resourceEntry.f458id);
    }

    public static Drawable getDrawable(Context context, int i) {
        ResourceEntry resourceEntry = getResourceEntry(context, i);
        return resourceEntry.resources.getDrawable(resourceEntry.f458id);
    }

    public static String getString(Context context, int i) {
        ResourceEntry resourceEntry = getResourceEntry(context, i);
        return resourceEntry.resources.getString(resourceEntry.f458id);
    }

    public static int getColor(Context context, int i) {
        ResourceEntry resourceEntry = getResourceEntry(context, i);
        return resourceEntry.resources.getColor(resourceEntry.f458id);
    }

    public static CharSequence getText(Context context, int i) {
        ResourceEntry resourceEntry = getResourceEntry(context, i);
        return resourceEntry.resources.getText(resourceEntry.f458id);
    }

    public static Icon getIcon(Context context, int i) {
        ResourceEntry resourceEntry = getResourceEntry(context, i);
        if (getTypedValue(resourceEntry).data == 0) {
            return null;
        }
        return Icon.createWithResource(resourceEntry.packageName, resourceEntry.f458id);
    }

    public static ResourceEntry getResourceEntry(Context context, int i) {
        Partner partner2 = get(context);
        if (partner2 != null) {
            Resources resources2 = context.getResources();
            int identifier = partner2.getIdentifier(resources2.getResourceEntryName(i), resources2.getResourceTypeName(i));
            if (identifier != 0) {
                return new ResourceEntry(partner2.getPackageName(), partner2.resources, identifier, true);
            }
        }
        return new ResourceEntry(context.getPackageName(), context.getResources(), i, false);
    }

    public static class ResourceEntry {

        /* renamed from: id */
        public int f458id;
        public boolean isOverlay;
        public String packageName;
        public Resources resources;

        ResourceEntry(String str, Resources resources2, int i, boolean z) {
            this.packageName = str;
            this.resources = resources2;
            this.f458id = i;
            this.isOverlay = z;
        }
    }

    public static synchronized Partner get(Context context) {
        Partner partner2;
        synchronized (Partner.class) {
            if (!searched) {
                PackageManager packageManager = context.getPackageManager();
                for (ResolveInfo next : packageManager.queryBroadcastReceivers(new Intent(ACTION_PARTNER_CUSTOMIZATION), 1835520)) {
                    if (next.activityInfo != null) {
                        ApplicationInfo applicationInfo = next.activityInfo.applicationInfo;
                        if ((applicationInfo.flags & 1) != 0) {
                            try {
                                partner = new Partner(applicationInfo.packageName, packageManager.getResourcesForApplication(applicationInfo));
                                break;
                            } catch (PackageManager.NameNotFoundException unused) {
                                Log.w(TAG, "Failed to find resources for " + applicationInfo.packageName);
                            }
                        } else {
                            continue;
                        }
                    }
                }
                searched = true;
            }
            partner2 = partner;
        }
        return partner2;
    }

    public static synchronized void resetForTesting() {
        synchronized (Partner.class) {
            searched = false;
            partner = null;
        }
    }

    private Partner(String str, Resources resources2) {
        this.packageName = str;
        this.resources = resources2;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public Resources getResources() {
        return this.resources;
    }

    public int getIdentifier(String str, String str2) {
        return this.resources.getIdentifier(str, str2, this.packageName);
    }

    private static TypedValue getTypedValue(ResourceEntry resourceEntry) {
        TypedValue typedValue = new TypedValue();
        resourceEntry.resources.getValue(resourceEntry.f458id, typedValue, true);
        return typedValue;
    }
}
