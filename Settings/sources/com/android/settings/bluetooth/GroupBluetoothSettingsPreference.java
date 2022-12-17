package com.android.settings.bluetooth;

import android.content.Context;
import android.content.res.Resources;
import android.os.UserManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.widget.GearPreference;

public class GroupBluetoothSettingsPreference extends GearPreference {
    private static int sDimAlpha = Integer.MIN_VALUE;
    private String contentDescription = null;
    private int mGroupId;
    private boolean mHideSecondTarget = false;
    private Resources mResources;
    private final UserManager mUserManager;
    private int mVisibleCount = 0;

    public GroupBluetoothSettingsPreference(Context context, int i) {
        super(context, (AttributeSet) null);
        this.mGroupId = i;
        this.mResources = getContext().getResources();
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        if (sDimAlpha == Integer.MIN_VALUE) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(16842803, typedValue, true);
            sDimAlpha = (int) (typedValue.getFloat() * 255.0f);
        }
        this.mVisibleCount = 0;
        onDeviceAttributesChanged();
    }

    /* access modifiers changed from: protected */
    public boolean shouldHideSecondTarget() {
        return this.mUserManager.hasUserRestriction("no_config_bluetooth") || this.mHideSecondTarget;
    }

    public void onDeviceAttributesChanged() {
        String string = getContext().getString(R$string.group_settings);
        setTitle((CharSequence) string + " " + (this.mGroupId + 1));
        setEnabled(true);
        setVisible(true);
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        if (findPreferenceInHierarchy("bt_checkbox") != null) {
            setDependency("bt_checkbox");
        }
        ImageView imageView = (ImageView) preferenceViewHolder.findViewById(R$id.settings_button);
        if (imageView != null) {
            imageView.setOnClickListener(this);
        }
        ImageView imageView2 = (ImageView) preferenceViewHolder.findViewById(16908294);
        if (imageView2 != null) {
            imageView2.setContentDescription(this.contentDescription);
            imageView2.setImportantForAccessibility(2);
            imageView2.setVisibility(0);
        }
        super.onBindViewHolder(preferenceViewHolder);
    }

    /* access modifiers changed from: protected */
    public int getSecondTargetResId() {
        return R$layout.preference_widget_gear;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public int incrementChildCound() {
        int i = this.mVisibleCount + 1;
        this.mVisibleCount = i;
        return i;
    }

    public int decrementChildCount() {
        int i = this.mVisibleCount - 1;
        this.mVisibleCount = i;
        return i;
    }
}
