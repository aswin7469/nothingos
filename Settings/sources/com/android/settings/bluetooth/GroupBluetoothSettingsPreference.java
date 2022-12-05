package com.android.settings.bluetooth;

import android.content.Context;
import android.content.res.Resources;
import android.os.UserManager;
import android.util.TypedValue;
import android.widget.ImageView;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.widget.GearPreference;
/* loaded from: classes.dex */
public class GroupBluetoothSettingsPreference extends GearPreference {
    private static int sDimAlpha = Integer.MIN_VALUE;
    private int mGroupId;
    private final UserManager mUserManager;
    private int mVisibleCount;
    private String contentDescription = null;
    private boolean mHideSecondTarget = false;
    private Resources mResources = getContext().getResources();

    public GroupBluetoothSettingsPreference(Context context, int i) {
        super(context, null);
        this.mGroupId = -1;
        this.mVisibleCount = 0;
        this.mGroupId = i;
        this.mUserManager = (UserManager) context.getSystemService(UserManager.class);
        if (sDimAlpha == Integer.MIN_VALUE) {
            TypedValue typedValue = new TypedValue();
            context.getTheme().resolveAttribute(16842803, typedValue, true);
            sDimAlpha = (int) (typedValue.getFloat() * 255.0f);
        }
        this.mVisibleCount = 0;
        onDeviceAttributesChanged();
    }

    @Override // com.android.settings.widget.GearPreference, com.android.settingslib.RestrictedPreference, com.android.settingslib.widget.TwoTargetPreference
    protected boolean shouldHideSecondTarget() {
        return this.mUserManager.hasUserRestriction("no_config_bluetooth") || this.mHideSecondTarget;
    }

    public void onDeviceAttributesChanged() {
        String string = getContext().getString(R.string.group_settings);
        setTitle(string + " " + (this.mGroupId + 1));
        setEnabled(true);
        setVisible(true);
    }

    @Override // com.android.settings.widget.GearPreference, com.android.settingslib.RestrictedPreference, com.android.settingslib.widget.TwoTargetPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        if (findPreferenceInHierarchy("bt_checkbox") != null) {
            setDependency("bt_checkbox");
        }
        ImageView imageView = (ImageView) preferenceViewHolder.findViewById(R.id.settings_button);
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

    @Override // com.android.settings.widget.GearPreference, com.android.settingslib.RestrictedPreference, com.android.settingslib.widget.TwoTargetPreference
    protected int getSecondTargetResId() {
        return R.layout.preference_widget_gear;
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
