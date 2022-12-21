package com.google.android.setupdesign.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import com.google.android.setupcompat.C3931R;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.google.android.setupdesign.C3953R;
import com.google.android.setupdesign.GlifLayout;
import java.util.Locale;

public final class PartnerStyleHelper {
    private static final String TAG = "PartnerStyleHelper";

    public static int getLayoutGravity(Context context) {
        String string = PartnerConfigHelper.get(context).getString(context, PartnerConfig.CONFIG_LAYOUT_GRAVITY);
        if (string == null) {
            return 0;
        }
        String lowerCase = string.toLowerCase(Locale.ROOT);
        lowerCase.hashCode();
        if (lowerCase.equals("center")) {
            return 17;
        }
        if (!lowerCase.equals("start")) {
            return 0;
        }
        return 8388611;
    }

    public static boolean isPartnerHeavyThemeLayout(TemplateLayout templateLayout) {
        if (!(templateLayout instanceof GlifLayout)) {
            return false;
        }
        return ((GlifLayout) templateLayout).shouldApplyPartnerHeavyThemeResource();
    }

    public static boolean isPartnerLightThemeLayout(TemplateLayout templateLayout) {
        if (!(templateLayout instanceof PartnerCustomizationLayout)) {
            return false;
        }
        return ((PartnerCustomizationLayout) templateLayout).shouldApplyPartnerResource();
    }

    public static boolean shouldApplyPartnerResource(View view) {
        if (view == null) {
            return false;
        }
        if (view instanceof PartnerCustomizationLayout) {
            return isPartnerLightThemeLayout((PartnerCustomizationLayout) view);
        }
        return shouldApplyPartnerResource(view.getContext());
    }

    private static boolean shouldApplyPartnerResource(Context context) {
        if (!PartnerConfigHelper.get(context).isAvailable()) {
            return false;
        }
        Activity activity = null;
        try {
            activity = PartnerCustomizationLayout.lookupActivityFromContext(context);
            if (activity != null) {
                TemplateLayout findLayoutFromActivity = findLayoutFromActivity(activity);
                if (findLayoutFromActivity instanceof PartnerCustomizationLayout) {
                    return ((PartnerCustomizationLayout) findLayoutFromActivity).shouldApplyPartnerResource();
                }
            }
        } catch (ClassCastException | IllegalArgumentException unused) {
        }
        boolean isAnySetupWizard = activity != null ? WizardManagerHelper.isAnySetupWizard(activity.getIntent()) : false;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{C3953R.attr.sucUsePartnerResource});
        boolean z = obtainStyledAttributes.getBoolean(0, true);
        obtainStyledAttributes.recycle();
        if (isAnySetupWizard || z) {
            return true;
        }
        return false;
    }

    public static boolean shouldApplyPartnerHeavyThemeResource(View view) {
        if (view == null) {
            return false;
        }
        if (view instanceof GlifLayout) {
            return isPartnerHeavyThemeLayout((GlifLayout) view);
        }
        return shouldApplyPartnerHeavyThemeResource(view.getContext());
    }

    static boolean shouldApplyPartnerHeavyThemeResource(Context context) {
        try {
            TemplateLayout findLayoutFromActivity = findLayoutFromActivity(PartnerCustomizationLayout.lookupActivityFromContext(context));
            if (findLayoutFromActivity instanceof GlifLayout) {
                return ((GlifLayout) findLayoutFromActivity).shouldApplyPartnerHeavyThemeResource();
            }
        } catch (ClassCastException | IllegalArgumentException unused) {
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{C3953R.attr.sudUsePartnerHeavyTheme});
        boolean z = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
        boolean z2 = z || PartnerConfigHelper.shouldApplyExtendedPartnerConfig(context);
        if (!shouldApplyPartnerResource(context) || !z2) {
            return false;
        }
        return true;
    }

    public static boolean useDynamicColor(View view) {
        if (view == null) {
            return false;
        }
        return getDynamicColorAttributeFromTheme(view.getContext());
    }

    static boolean getDynamicColorAttributeFromTheme(Context context) {
        try {
            TemplateLayout findLayoutFromActivity = findLayoutFromActivity(PartnerCustomizationLayout.lookupActivityFromContext(context));
            if (findLayoutFromActivity instanceof GlifLayout) {
                return ((GlifLayout) findLayoutFromActivity).shouldApplyDynamicColor();
            }
        } catch (ClassCastException | IllegalArgumentException unused) {
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{C3953R.attr.sucFullDynamicColor});
        boolean hasValue = obtainStyledAttributes.hasValue(C3931R.styleable.SucPartnerCustomizationLayout_sucFullDynamicColor);
        obtainStyledAttributes.recycle();
        return hasValue;
    }

    private static TemplateLayout findLayoutFromActivity(Activity activity) {
        View findViewById;
        if (activity == null || (findViewById = activity.findViewById(C3953R.C3956id.suc_layout_status)) == null) {
            return null;
        }
        return (TemplateLayout) findViewById.getParent();
    }

    private PartnerStyleHelper() {
    }
}
