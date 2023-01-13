package com.android.settingslib;

import android.app.AppOpsManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.utils.BuildCompatUtils;
import com.nothing.ui.support.NtCustSwitchPreference;

public class RestrictedSwitchPreference extends NtCustSwitchPreference {
    AppOpsManager mAppOpsManager;
    RestrictedPreferenceHelper mHelper;
    private int mIconSize;
    CharSequence mRestrictedSwitchSummary;
    boolean mUseAdditionalSummary;

    /* JADX WARNING: type inference failed for: r2v0, types: [com.android.settingslib.RestrictedSwitchPreference, androidx.preference.Preference] */
    public RestrictedSwitchPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mUseAdditionalSummary = false;
        this.mHelper = new RestrictedPreferenceHelper(context, this, attributeSet);
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1757R.styleable.RestrictedSwitchPreference);
            TypedValue peekValue = obtainStyledAttributes.peekValue(C1757R.styleable.RestrictedSwitchPreference_useAdditionalSummary);
            if (peekValue != null) {
                this.mUseAdditionalSummary = peekValue.type == 18 && peekValue.data != 0;
            }
            TypedValue peekValue2 = obtainStyledAttributes.peekValue(C1757R.styleable.RestrictedSwitchPreference_restrictedSwitchSummary);
            obtainStyledAttributes.recycle();
            if (peekValue2 != null && peekValue2.type == 3) {
                if (peekValue2.resourceId != 0) {
                    this.mRestrictedSwitchSummary = context.getText(peekValue2.resourceId);
                } else {
                    this.mRestrictedSwitchSummary = peekValue2.string;
                }
            }
        }
        if (this.mUseAdditionalSummary) {
            setLayoutResource(C1757R.layout.restricted_switch_preference);
            useAdminDisabledSummary(false);
        }
    }

    public RestrictedSwitchPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public RestrictedSwitchPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, C1757R.attr.switchPreferenceStyle, 16843629));
    }

    public RestrictedSwitchPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    public void setAppOps(AppOpsManager appOpsManager) {
        this.mAppOpsManager = appOpsManager;
    }

    public void setIconSize(int i) {
        this.mIconSize = i;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        RestrictedSwitchPreference.super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(16908352);
        if (findViewById != null) {
            findViewById.getRootView().setFilterTouchesWhenObscured(true);
        }
        this.mHelper.onBindViewHolder(preferenceViewHolder);
        CharSequence charSequence = this.mRestrictedSwitchSummary;
        if (charSequence == null) {
            if (isChecked()) {
                charSequence = getUpdatableEnterpriseString(getContext(), "Settings.ENABLED_BY_ADMIN_SWITCH_SUMMARY", C1757R.string.enabled_by_admin);
            } else {
                charSequence = getUpdatableEnterpriseString(getContext(), "Settings.DISABLED_BY_ADMIN_SWITCH_SUMMARY", C1757R.string.disabled_by_admin);
            }
        }
        ImageView imageView = (ImageView) preferenceViewHolder.itemView.findViewById(16908294);
        if (this.mIconSize > 0) {
            int i = this.mIconSize;
            imageView.setLayoutParams(new LinearLayout.LayoutParams(i, i));
        }
        if (this.mUseAdditionalSummary) {
            TextView textView = (TextView) preferenceViewHolder.findViewById(C1757R.C1760id.additional_summary);
            if (textView == null) {
                return;
            }
            if (isDisabledByAdmin()) {
                textView.setText(charSequence);
                textView.setVisibility(0);
                return;
            }
            textView.setVisibility(8);
            return;
        }
        TextView textView2 = (TextView) preferenceViewHolder.findViewById(16908304);
        if (textView2 != null && isDisabledByAdmin()) {
            textView2.setText(charSequence);
            textView2.setVisibility(0);
        }
    }

    private static String getUpdatableEnterpriseString(Context context, String str, int i) {
        if (!BuildCompatUtils.isAtLeastT()) {
            return context.getString(i);
        }
        return ((DevicePolicyManager) context.getSystemService(DevicePolicyManager.class)).getResources().getString(str, new RestrictedSwitchPreference$$ExternalSyntheticLambda0(context, i));
    }

    public void performClick() {
        if (!this.mHelper.performClick()) {
            RestrictedSwitchPreference.super.performClick();
        }
    }

    public void useAdminDisabledSummary(boolean z) {
        this.mHelper.useAdminDisabledSummary(z);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        this.mHelper.onAttachedToHierarchy();
        RestrictedSwitchPreference.super.onAttachedToHierarchy(preferenceManager);
    }

    public void checkRestrictionAndSetDisabled(String str) {
        this.mHelper.checkRestrictionAndSetDisabled(str, UserHandle.myUserId());
    }

    public void checkRestrictionAndSetDisabled(String str, int i) {
        this.mHelper.checkRestrictionAndSetDisabled(str, i);
    }

    public void setEnabled(boolean z) {
        boolean z2;
        boolean z3 = true;
        if (!z || !isDisabledByAdmin()) {
            z2 = false;
        } else {
            this.mHelper.setDisabledByAdmin((RestrictedLockUtils.EnforcedAdmin) null);
            z2 = true;
        }
        if (!z || !isDisabledByAppOps()) {
            z3 = z2;
        } else {
            this.mHelper.setDisabledByAppOps(false);
        }
        if (!z3) {
            RestrictedSwitchPreference.super.setEnabled(z);
        }
    }

    public void setDisabledByAdmin(RestrictedLockUtils.EnforcedAdmin enforcedAdmin) {
        if (this.mHelper.setDisabledByAdmin(enforcedAdmin)) {
            notifyChanged();
        }
    }

    public boolean isDisabledByAdmin() {
        return this.mHelper.isDisabledByAdmin();
    }

    private void setDisabledByAppOps(boolean z) {
        if (this.mHelper.setDisabledByAppOps(z)) {
            notifyChanged();
        }
    }

    public boolean isDisabledByAppOps() {
        return this.mHelper.isDisabledByAppOps();
    }

    public int getUid() {
        RestrictedPreferenceHelper restrictedPreferenceHelper = this.mHelper;
        if (restrictedPreferenceHelper != null) {
            return restrictedPreferenceHelper.uid;
        }
        return -1;
    }

    public String getPackageName() {
        RestrictedPreferenceHelper restrictedPreferenceHelper = this.mHelper;
        if (restrictedPreferenceHelper != null) {
            return restrictedPreferenceHelper.packageName;
        }
        return null;
    }

    public void updateState(String str, int i, boolean z) {
        this.mHelper.updatePackageDetails(str, i);
        if (this.mAppOpsManager == null) {
            this.mAppOpsManager = (AppOpsManager) getContext().getSystemService(AppOpsManager.class);
        }
        boolean z2 = !getContext().getResources().getBoolean(17891657) || this.mAppOpsManager.noteOpNoThrow(119, i, str) == 0;
        if (z) {
            setEnabled(true);
        } else if (z2 && isDisabledByAppOps()) {
            setEnabled(true);
        } else if (!z2) {
            setDisabledByAppOps(true);
        }
    }
}
