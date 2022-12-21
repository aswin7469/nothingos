package com.google.android.setupcompat.partnerconfig;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.util.BuildCompatUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;

public class PartnerConfigHelper {
    public static final String IS_DYNAMIC_COLOR_ENABLED_METHOD = "isDynamicColorEnabled";
    public static final String IS_EXTENDED_PARTNER_CONFIG_ENABLED_METHOD = "isExtendedPartnerConfigEnabled";
    public static final String IS_MATERIAL_YOU_STYLE_ENABLED_METHOD = "IsMaterialYouStyleEnabled";
    public static final String IS_NEUTRAL_BUTTON_STYLE_ENABLED_METHOD = "isNeutralButtonStyleEnabled";
    public static final String IS_SUW_DAY_NIGHT_ENABLED_METHOD = "isSuwDayNightEnabled";
    public static final String KEY_FALLBACK_CONFIG = "fallbackConfig";
    public static final String MATERIAL_YOU_RESOURCE_SUFFIX = "_material_you";
    public static final String SUW_AUTHORITY = "com.google.android.setupwizard.partner";
    public static final String SUW_GET_PARTNER_CONFIG_METHOD = "getOverlayConfig";
    public static final String SUW_PACKAGE_NAME = "com.google.android.setupwizard";
    private static final String TAG = "PartnerConfigHelper";
    public static Bundle applyDynamicColorBundle = null;
    public static Bundle applyExtendedPartnerConfigBundle = null;
    public static Bundle applyMaterialYouConfigBundle = null;
    public static Bundle applyNeutralButtonStyleBundle = null;
    private static ContentObserver contentObserver = null;
    private static PartnerConfigHelper instance = null;
    private static int savedConfigUiMode = 0;
    public static int savedOrientation = 1;
    public static int savedScreenHeight;
    public static int savedScreenWidth;
    static Bundle suwDayNightEnabledBundle;
    final EnumMap<PartnerConfig, Object> partnerResourceCache = new EnumMap<>(PartnerConfig.class);
    Bundle resultBundle = null;

    public static synchronized PartnerConfigHelper get(Context context) {
        PartnerConfigHelper partnerConfigHelper;
        synchronized (PartnerConfigHelper.class) {
            if (!isValidInstance(context)) {
                instance = new PartnerConfigHelper(context);
            }
            partnerConfigHelper = instance;
        }
        return partnerConfigHelper;
    }

    private static boolean isValidInstance(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        if (instance == null) {
            savedConfigUiMode = configuration.uiMode & 48;
            savedOrientation = configuration.orientation;
            savedScreenWidth = configuration.screenWidthDp;
            savedScreenHeight = configuration.screenHeightDp;
            return false;
        }
        if (!(isSetupWizardDayNightEnabled(context) && (configuration.uiMode & 48) != savedConfigUiMode) && configuration.orientation == savedOrientation && configuration.screenWidthDp == savedScreenWidth && configuration.screenHeightDp == savedScreenHeight) {
            return true;
        }
        savedConfigUiMode = configuration.uiMode & 48;
        savedOrientation = configuration.orientation;
        savedScreenHeight = configuration.screenHeightDp;
        savedScreenWidth = configuration.screenWidthDp;
        resetInstance();
        return false;
    }

    private PartnerConfigHelper(Context context) {
        getPartnerConfigBundle(context);
        registerContentObserver(context);
    }

    public boolean isAvailable() {
        Bundle bundle = this.resultBundle;
        return bundle != null && !bundle.isEmpty();
    }

    public boolean isPartnerConfigAvailable(PartnerConfig partnerConfig) {
        return isAvailable() && this.resultBundle.containsKey(partnerConfig.getResourceName());
    }

    public int getColor(Context context, PartnerConfig partnerConfig) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.COLOR) {
            throw new IllegalArgumentException("Not a color resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return ((Integer) this.partnerResourceCache.get(partnerConfig)).intValue();
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Resources resources = resourceEntryFromKey.getResources();
                int resourceId = resourceEntryFromKey.getResourceId();
                TypedValue typedValue = new TypedValue();
                resources.getValue(resourceId, typedValue, true);
                if (typedValue.type == 1 && typedValue.data == 0) {
                    return 0;
                }
                int color = resources.getColor(resourceId, (Resources.Theme) null);
                this.partnerResourceCache.put(partnerConfig, Integer.valueOf(color));
                return color;
            } catch (NullPointerException unused) {
                return 0;
            }
        }
    }

    public Drawable getDrawable(Context context, PartnerConfig partnerConfig) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.DRAWABLE) {
            throw new IllegalArgumentException("Not a drawable resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return (Drawable) this.partnerResourceCache.get(partnerConfig);
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Resources resources = resourceEntryFromKey.getResources();
                int resourceId = resourceEntryFromKey.getResourceId();
                TypedValue typedValue = new TypedValue();
                resources.getValue(resourceId, typedValue, true);
                if (typedValue.type == 1 && typedValue.data == 0) {
                    return null;
                }
                Drawable drawable = resources.getDrawable(resourceId, (Resources.Theme) null);
                this.partnerResourceCache.put(partnerConfig, drawable);
                return drawable;
            } catch (Resources.NotFoundException | NullPointerException unused) {
                return null;
            }
        }
    }

    public String getString(Context context, PartnerConfig partnerConfig) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.STRING) {
            throw new IllegalArgumentException("Not a string resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return (String) this.partnerResourceCache.get(partnerConfig);
        } else {
            String str = null;
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                str = resourceEntryFromKey.getResources().getString(resourceEntryFromKey.getResourceId());
                this.partnerResourceCache.put(partnerConfig, str);
                return str;
            } catch (NullPointerException unused) {
                return str;
            }
        }
    }

    public List<String> getStringArray(Context context, PartnerConfig partnerConfig) {
        if (partnerConfig.getResourceType() == PartnerConfig.ResourceType.STRING_ARRAY) {
            ArrayList arrayList = new ArrayList();
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Collections.addAll(arrayList, resourceEntryFromKey.getResources().getStringArray(resourceEntryFromKey.getResourceId()));
            } catch (NullPointerException unused) {
            }
            return arrayList;
        }
        throw new IllegalArgumentException("Not a string array resource");
    }

    public boolean getBoolean(Context context, PartnerConfig partnerConfig, boolean z) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.BOOL) {
            throw new IllegalArgumentException("Not a bool resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return ((Boolean) this.partnerResourceCache.get(partnerConfig)).booleanValue();
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                z = resourceEntryFromKey.getResources().getBoolean(resourceEntryFromKey.getResourceId());
                this.partnerResourceCache.put(partnerConfig, Boolean.valueOf(z));
                return z;
            } catch (Resources.NotFoundException | NullPointerException unused) {
                return z;
            }
        }
    }

    public float getDimension(Context context, PartnerConfig partnerConfig) {
        return getDimension(context, partnerConfig, 0.0f);
    }

    public float getDimension(Context context, PartnerConfig partnerConfig, float f) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.DIMENSION) {
            throw new IllegalArgumentException("Not a dimension resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return getDimensionFromTypedValue(context, (TypedValue) this.partnerResourceCache.get(partnerConfig));
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Resources resources = resourceEntryFromKey.getResources();
                int resourceId = resourceEntryFromKey.getResourceId();
                float dimension = resources.getDimension(resourceId);
                this.partnerResourceCache.put(partnerConfig, getTypedValueFromResource(resources, resourceId, 5));
                return getDimensionFromTypedValue(context, (TypedValue) this.partnerResourceCache.get(partnerConfig));
            } catch (Resources.NotFoundException | NullPointerException unused) {
                return f;
            }
        }
    }

    public float getFraction(Context context, PartnerConfig partnerConfig) {
        return getFraction(context, partnerConfig, 0.0f);
    }

    public float getFraction(Context context, PartnerConfig partnerConfig, float f) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.FRACTION) {
            throw new IllegalArgumentException("Not a fraction resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return ((Float) this.partnerResourceCache.get(partnerConfig)).floatValue();
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                f = resourceEntryFromKey.getResources().getFraction(resourceEntryFromKey.getResourceId(), 1, 1);
                this.partnerResourceCache.put(partnerConfig, Float.valueOf(f));
                return f;
            } catch (Resources.NotFoundException | NullPointerException unused) {
                return f;
            }
        }
    }

    public int getInteger(Context context, PartnerConfig partnerConfig, int i) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.INTEGER) {
            throw new IllegalArgumentException("Not a integer resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return ((Integer) this.partnerResourceCache.get(partnerConfig)).intValue();
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                i = resourceEntryFromKey.getResources().getInteger(resourceEntryFromKey.getResourceId());
                this.partnerResourceCache.put(partnerConfig, Integer.valueOf(i));
                return i;
            } catch (Resources.NotFoundException | NullPointerException unused) {
                return i;
            }
        }
    }

    public ResourceEntry getIllustrationResourceEntry(Context context, PartnerConfig partnerConfig) {
        if (partnerConfig.getResourceType() != PartnerConfig.ResourceType.ILLUSTRATION) {
            throw new IllegalArgumentException("Not a illustration resource");
        } else if (this.partnerResourceCache.containsKey(partnerConfig)) {
            return (ResourceEntry) this.partnerResourceCache.get(partnerConfig);
        } else {
            try {
                ResourceEntry resourceEntryFromKey = getResourceEntryFromKey(context, partnerConfig.getResourceName());
                Resources resources = resourceEntryFromKey.getResources();
                int resourceId = resourceEntryFromKey.getResourceId();
                TypedValue typedValue = new TypedValue();
                resources.getValue(resourceId, typedValue, true);
                if (typedValue.type == 1 && typedValue.data == 0) {
                    return null;
                }
                this.partnerResourceCache.put(partnerConfig, resourceEntryFromKey);
                return resourceEntryFromKey;
            } catch (NullPointerException unused) {
                return null;
            }
        }
    }

    private void getPartnerConfigBundle(Context context) {
        Bundle bundle = this.resultBundle;
        if (bundle == null || bundle.isEmpty()) {
            try {
                this.resultBundle = context.getContentResolver().call(getContentUri(), SUW_GET_PARTNER_CONFIG_METHOD, (String) null, (Bundle) null);
                this.partnerResourceCache.clear();
                String str = TAG;
                StringBuilder sb = new StringBuilder("PartnerConfigsBundle=");
                Bundle bundle2 = this.resultBundle;
                Log.i(str, sb.append(bundle2 != null ? Integer.valueOf(bundle2.size()) : "(null)").toString());
            } catch (IllegalArgumentException | SecurityException unused) {
                Log.w(TAG, "Fail to get config from suw provider");
            }
        }
    }

    /* access modifiers changed from: package-private */
    public ResourceEntry getResourceEntryFromKey(Context context, String str) {
        Bundle bundle = this.resultBundle.getBundle(str);
        Bundle bundle2 = this.resultBundle.getBundle(KEY_FALLBACK_CONFIG);
        if (bundle2 != null) {
            bundle.putBundle(KEY_FALLBACK_CONFIG, bundle2.getBundle(str));
        }
        return adjustResourceEntryDayNightMode(context, adjustResourceEntryDefaultValue(context, ResourceEntry.fromBundle(context, bundle)));
    }

    private static ResourceEntry adjustResourceEntryDayNightMode(Context context, ResourceEntry resourceEntry) {
        Resources resources = resourceEntry.getResources();
        Configuration configuration = resources.getConfiguration();
        if (!isSetupWizardDayNightEnabled(context) && Util.isNightMode(configuration)) {
            if (resourceEntry == null) {
                Log.w(TAG, "resourceEntry is null, skip to force day mode.");
                return resourceEntry;
            }
            configuration.uiMode = (configuration.uiMode & -49) | 16;
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        return resourceEntry;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001b, code lost:
        r4 = r5.getResources().getResourceTypeName(r5.getResourceId());
        r0 = r5.getResourceName().concat(MATERIAL_YOU_RESOURCE_SUFFIX);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.android.setupcompat.partnerconfig.ResourceEntry adjustResourceEntryDefaultValue(android.content.Context r4, com.google.android.setupcompat.partnerconfig.ResourceEntry r5) {
        /*
            r3 = this;
            java.lang.String r3 = "use material you resource:"
            boolean r0 = com.google.android.setupcompat.util.BuildCompatUtils.isAtLeastT()
            if (r0 == 0) goto L_0x005f
            boolean r4 = shouldApplyMaterialYouStyle(r4)
            if (r4 == 0) goto L_0x005f
            java.lang.String r4 = "com.google.android.setupwizard"
            java.lang.String r0 = r5.getPackageName()     // Catch:{ NotFoundException -> 0x005f }
            boolean r4 = r4.equals(r0)     // Catch:{ NotFoundException -> 0x005f }
            if (r4 == 0) goto L_0x005f
            android.content.res.Resources r4 = r5.getResources()     // Catch:{ NotFoundException -> 0x005f }
            int r0 = r5.getResourceId()     // Catch:{ NotFoundException -> 0x005f }
            java.lang.String r4 = r4.getResourceTypeName(r0)     // Catch:{ NotFoundException -> 0x005f }
            java.lang.String r0 = r5.getResourceName()     // Catch:{ NotFoundException -> 0x005f }
            java.lang.String r1 = "_material_you"
            java.lang.String r0 = r0.concat(r1)     // Catch:{ NotFoundException -> 0x005f }
            android.content.res.Resources r1 = r5.getResources()     // Catch:{ NotFoundException -> 0x005f }
            java.lang.String r2 = r5.getPackageName()     // Catch:{ NotFoundException -> 0x005f }
            int r4 = r1.getIdentifier(r0, r4, r2)     // Catch:{ NotFoundException -> 0x005f }
            if (r4 == 0) goto L_0x005f
            java.lang.String r1 = TAG     // Catch:{ NotFoundException -> 0x005f }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ NotFoundException -> 0x005f }
            r2.<init>((java.lang.String) r3)     // Catch:{ NotFoundException -> 0x005f }
            java.lang.StringBuilder r3 = r2.append((java.lang.String) r0)     // Catch:{ NotFoundException -> 0x005f }
            java.lang.String r3 = r3.toString()     // Catch:{ NotFoundException -> 0x005f }
            android.util.Log.i(r1, r3)     // Catch:{ NotFoundException -> 0x005f }
            com.google.android.setupcompat.partnerconfig.ResourceEntry r3 = new com.google.android.setupcompat.partnerconfig.ResourceEntry     // Catch:{ NotFoundException -> 0x005f }
            java.lang.String r1 = r5.getPackageName()     // Catch:{ NotFoundException -> 0x005f }
            android.content.res.Resources r2 = r5.getResources()     // Catch:{ NotFoundException -> 0x005f }
            r3.<init>(r1, r0, r4, r2)     // Catch:{ NotFoundException -> 0x005f }
            return r3
        L_0x005f:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.adjustResourceEntryDefaultValue(android.content.Context, com.google.android.setupcompat.partnerconfig.ResourceEntry):com.google.android.setupcompat.partnerconfig.ResourceEntry");
    }

    public static synchronized void resetInstance() {
        synchronized (PartnerConfigHelper.class) {
            instance = null;
            suwDayNightEnabledBundle = null;
            applyExtendedPartnerConfigBundle = null;
            applyMaterialYouConfigBundle = null;
            applyDynamicColorBundle = null;
            applyNeutralButtonStyleBundle = null;
        }
    }

    public static boolean isSetupWizardDayNightEnabled(Context context) {
        if (suwDayNightEnabledBundle == null) {
            try {
                suwDayNightEnabledBundle = context.getContentResolver().call(getContentUri(), IS_SUW_DAY_NIGHT_ENABLED_METHOD, (String) null, (Bundle) null);
            } catch (IllegalArgumentException | SecurityException unused) {
                Log.w(TAG, "SetupWizard DayNight supporting status unknown; return as false.");
                suwDayNightEnabledBundle = null;
                return false;
            }
        }
        Bundle bundle = suwDayNightEnabledBundle;
        if (bundle == null || !bundle.getBoolean(IS_SUW_DAY_NIGHT_ENABLED_METHOD, false)) {
            return false;
        }
        return true;
    }

    public static boolean shouldApplyExtendedPartnerConfig(Context context) {
        if (applyExtendedPartnerConfigBundle == null) {
            try {
                applyExtendedPartnerConfigBundle = context.getContentResolver().call(getContentUri(), IS_EXTENDED_PARTNER_CONFIG_ENABLED_METHOD, (String) null, (Bundle) null);
            } catch (IllegalArgumentException | SecurityException unused) {
                Log.w(TAG, "SetupWizard extended partner configs supporting status unknown; return as false.");
                applyExtendedPartnerConfigBundle = null;
                return false;
            }
        }
        Bundle bundle = applyExtendedPartnerConfigBundle;
        if (bundle == null || !bundle.getBoolean(IS_EXTENDED_PARTNER_CONFIG_ENABLED_METHOD, false)) {
            return false;
        }
        return true;
    }

    public static boolean shouldApplyMaterialYouStyle(Context context) {
        Bundle bundle = applyMaterialYouConfigBundle;
        if (bundle == null || bundle.isEmpty()) {
            try {
                Bundle call = context.getContentResolver().call(getContentUri(), IS_MATERIAL_YOU_STYLE_ENABLED_METHOD, (String) null, (Bundle) null);
                applyMaterialYouConfigBundle = call;
                if (call != null && call.isEmpty() && !BuildCompatUtils.isAtLeastT()) {
                    return shouldApplyExtendedPartnerConfig(context);
                }
            } catch (IllegalArgumentException | SecurityException unused) {
                Log.w(TAG, "SetupWizard Material You configs supporting status unknown; return as false.");
                applyMaterialYouConfigBundle = null;
                return false;
            }
        }
        Bundle bundle2 = applyMaterialYouConfigBundle;
        if (bundle2 == null || !bundle2.getBoolean(IS_MATERIAL_YOU_STYLE_ENABLED_METHOD, false)) {
            return false;
        }
        return true;
    }

    public static boolean isSetupWizardDynamicColorEnabled(Context context) {
        if (applyDynamicColorBundle == null) {
            try {
                applyDynamicColorBundle = context.getContentResolver().call(getContentUri(), IS_DYNAMIC_COLOR_ENABLED_METHOD, (String) null, (Bundle) null);
            } catch (IllegalArgumentException | SecurityException unused) {
                Log.w(TAG, "SetupWizard dynamic color supporting status unknown; return as false.");
                applyDynamicColorBundle = null;
                return false;
            }
        }
        Bundle bundle = applyDynamicColorBundle;
        if (bundle == null || !bundle.getBoolean(IS_DYNAMIC_COLOR_ENABLED_METHOD, false)) {
            return false;
        }
        return true;
    }

    public static boolean isNeutralButtonStyleEnabled(Context context) {
        if (applyNeutralButtonStyleBundle == null) {
            try {
                applyNeutralButtonStyleBundle = context.getContentResolver().call(getContentUri(), IS_NEUTRAL_BUTTON_STYLE_ENABLED_METHOD, (String) null, (Bundle) null);
            } catch (IllegalArgumentException | SecurityException unused) {
                Log.w(TAG, "Neutral button style supporting status unknown; return as false.");
                applyNeutralButtonStyleBundle = null;
                return false;
            }
        }
        Bundle bundle = applyNeutralButtonStyleBundle;
        if (bundle == null || !bundle.getBoolean(IS_NEUTRAL_BUTTON_STYLE_ENABLED_METHOD, false)) {
            return false;
        }
        return true;
    }

    static Uri getContentUri() {
        return new Uri.Builder().scheme("content").authority(SUW_AUTHORITY).build();
    }

    private static TypedValue getTypedValueFromResource(Resources resources, int i, int i2) {
        TypedValue typedValue = new TypedValue();
        resources.getValue(i, typedValue, true);
        if (typedValue.type == i2) {
            return typedValue;
        }
        throw new Resources.NotFoundException("Resource ID #0x" + Integer.toHexString(i) + " type #0x" + Integer.toHexString(typedValue.type) + " is not valid");
    }

    private static float getDimensionFromTypedValue(Context context, TypedValue typedValue) {
        return typedValue.getDimension(context.getResources().getDisplayMetrics());
    }

    private static void registerContentObserver(Context context) {
        if (isSetupWizardDayNightEnabled(context)) {
            if (contentObserver != null) {
                unregisterContentObserver(context);
            }
            Uri contentUri = getContentUri();
            try {
                contentObserver = new ContentObserver((Handler) null) {
                    public void onChange(boolean z) {
                        super.onChange(z);
                        PartnerConfigHelper.resetInstance();
                    }
                };
                context.getContentResolver().registerContentObserver(contentUri, true, contentObserver);
            } catch (IllegalArgumentException | NullPointerException | SecurityException e) {
                Log.w(TAG, "Failed to register content observer for " + contentUri + ": " + e);
            }
        }
    }

    private static void unregisterContentObserver(Context context) {
        try {
            context.getContentResolver().unregisterContentObserver(contentObserver);
            contentObserver = null;
        } catch (IllegalArgumentException | NullPointerException | SecurityException e) {
            Log.w(TAG, "Failed to unregister content observer: " + e);
        }
    }
}
