package com.nothing.settings.deviceinfo.aboutphone;

import android.content.Context;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;

public class DeviceBasicInfoPreference extends Preference {
    private Context mContext;
    private String mDeviceName;
    private View mDeviceNameParent;
    private OnClickListener mOnClickListener;
    private View mSoftwareInfoParent;
    private String mStorageInfo;
    private View mStorageInfoParent;
    private Validator mValidator;

    public interface OnClickListener {
        void onClick(Preference preference, int i);
    }

    public interface Validator {
        boolean isTextValid(String str);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public Validator getValidator() {
        return this.mValidator;
    }

    public void setValidator(Validator validator) {
        this.mValidator = validator;
    }

    public void setDeviceName(String str) {
        if (!TextUtils.equals(this.mDeviceName, str)) {
            this.mDeviceName = str;
            notifyChanged();
        }
    }

    public void setStorageInfo(String str) {
        if (!TextUtils.equals(this.mStorageInfo, str)) {
            this.mStorageInfo = str;
            notifyChanged();
        }
    }

    public DeviceBasicInfoPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    public DeviceBasicInfoPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public DeviceBasicInfoPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DeviceBasicInfoPreference(Context context) {
        this(context, (AttributeSet) null);
    }

    private void init(Context context) {
        setLayoutResource(R$layout.about_phone_basic_info);
        this.mContext = context;
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        String str;
        super.onBindViewHolder(preferenceViewHolder);
        ImageView imageView = (ImageView) preferenceViewHolder.findViewById(R$id.about_info_device_image);
        if (TextUtils.equals(SystemProperties.get("ro.phone.shell.color"), "white")) {
            imageView.setImageResource(R$drawable.nt_about_phone_white);
        } else {
            imageView.setImageResource(R$drawable.nt_about_phone_black);
        }
        this.mDeviceNameParent = preferenceViewHolder.findViewById(R$id.about_info_device_name_parent);
        this.mSoftwareInfoParent = preferenceViewHolder.findViewById(R$id.about_info_software_info);
        this.mStorageInfoParent = preferenceViewHolder.findViewById(R$id.about_info_storage_parent);
        ((TextView) preferenceViewHolder.findViewById(R$id.about_info_device_name_summary)).setText(this.mDeviceName);
        TextView textView = (TextView) preferenceViewHolder.findViewById(R$id.about_info_software_summary);
        String str2 = SystemProperties.get("ro.nothing.version.id");
        if (!TextUtils.isEmpty(str2)) {
            str = this.mContext.getString(R$string.nt_nothing_os_version).trim() + " " + str2;
        } else {
            str = "Nothing OS 1.0.0";
        }
        textView.setText(str);
        ((TextView) preferenceViewHolder.findViewById(R$id.about_info_storage_summary)).setText(this.mStorageInfo);
        setupClickEvent();
    }

    private void setupClickEvent() {
        this.mDeviceNameParent.setOnClickListener(new DeviceBasicInfoPreference$$ExternalSyntheticLambda0(this));
        this.mSoftwareInfoParent.setOnClickListener(new DeviceBasicInfoPreference$$ExternalSyntheticLambda1(this));
        this.mStorageInfoParent.setOnClickListener(new DeviceBasicInfoPreference$$ExternalSyntheticLambda2(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickEvent$0(View view) {
        OnClickListener onClickListener = this.mOnClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, 0);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickEvent$1(View view) {
        OnClickListener onClickListener = this.mOnClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, 1);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setupClickEvent$2(View view) {
        OnClickListener onClickListener = this.mOnClickListener;
        if (onClickListener != null) {
            onClickListener.onClick(this, 2);
        }
    }
}
