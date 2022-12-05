package com.nt.settings.about;

import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R;
import com.android.settings.deviceinfo.storage.StorageEntry;
import com.android.settingslib.utils.ThreadUtils;
import java.io.File;
import java.io.IOException;
/* loaded from: classes2.dex */
public class NtAboutPhoneBasicInfoPreference extends Preference {
    private ConstraintLayout mConstraintLayoutDevice;
    private ConstraintLayout mConstraintLayoutSoftware;
    private Context mContext;
    private TextView mDeviceNameSummary;
    private OnClickListener mOnClickListener;
    private TextView mSoftwareInfoSummary;
    private StorageEntry mStorageEntry;
    private StorageStatsManager mStorageStatsManager;
    private TextView mStorageSummary;
    long mTotalBytes;
    long mUsedBytes;
    private Validator mValidator;

    /* loaded from: classes2.dex */
    public interface OnClickListener {
        void onClick(Preference preference, int i);
    }

    /* loaded from: classes2.dex */
    public interface Validator {
        boolean isTextValid(String str);
    }

    public NtAboutPhoneBasicInfoPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet, i, i2);
    }

    public NtAboutPhoneBasicInfoPreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public NtAboutPhoneBasicInfoPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NtAboutPhoneBasicInfoPreference(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2) {
        setLayoutResource(R.layout.nt_about_phone_basic_info);
        this.mContext = context;
        this.mStorageEntry = StorageEntry.getDefaultInternalStorageEntry(getContext());
        this.mStorageStatsManager = (StorageStatsManager) context.getSystemService(StorageStatsManager.class);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        String str;
        super.onBindViewHolder(preferenceViewHolder);
        ImageView imageView = (ImageView) preferenceViewHolder.findViewById(R.id.nt_about_phone_basic_info_img);
        if (TextUtils.equals(SystemProperties.get("ro.phone.shell.color"), "white")) {
            imageView.setImageResource(R.drawable.nt_about_phone_white);
        }
        TextView textView = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_device_name_summary);
        this.mDeviceNameSummary = textView;
        textView.setText(Settings.Global.getString(getContext().getContentResolver(), "device_name"));
        this.mSoftwareInfoSummary = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_software_info_summary);
        String str2 = SystemProperties.get("ro.nothing.version.id");
        if (!TextUtils.isEmpty(str2)) {
            str = this.mContext.getString(R.string.nt_nothing_os_version).trim() + " " + str2;
        } else {
            str = "Nothing OS 1.0.0";
        }
        Log.d("NtAboutPhoneBasicInfo", "@_@ ------ version: " + str);
        this.mSoftwareInfoSummary.setText(str);
        this.mStorageSummary = (TextView) preferenceViewHolder.findViewById(R.id.nt_about_phone_storage_summary);
        ConstraintLayout constraintLayout = (ConstraintLayout) preferenceViewHolder.findViewById(R.id.nt_about_phone_basic_info_device_name);
        this.mConstraintLayoutDevice = constraintLayout;
        constraintLayout.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.about.NtAboutPhoneBasicInfoPreference.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (NtAboutPhoneBasicInfoPreference.this.mOnClickListener != null) {
                    NtAboutPhoneBasicInfoPreference.this.mOnClickListener.onClick(NtAboutPhoneBasicInfoPreference.this, 10);
                }
            }
        });
        ConstraintLayout constraintLayout2 = (ConstraintLayout) preferenceViewHolder.findViewById(R.id.nt_about_phone_basic_info_software_info);
        this.mConstraintLayoutSoftware = constraintLayout2;
        constraintLayout2.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.about.NtAboutPhoneBasicInfoPreference.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (NtAboutPhoneBasicInfoPreference.this.mOnClickListener != null) {
                    NtAboutPhoneBasicInfoPreference.this.mOnClickListener.onClick(NtAboutPhoneBasicInfoPreference.this, 11);
                }
            }
        });
        getStorageStatsAndUpdateUi();
    }

    public void setDeviceName(String str) {
        TextView textView = this.mDeviceNameSummary;
        if (textView != null) {
            textView.setText(str);
        }
    }

    private void getStorageStatsAndUpdateUi() {
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.nt.settings.about.NtAboutPhoneBasicInfoPreference$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NtAboutPhoneBasicInfoPreference.this.lambda$getStorageStatsAndUpdateUi$1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getStorageStatsAndUpdateUi$1() {
        StorageEntry storageEntry;
        try {
            storageEntry = this.mStorageEntry;
        } catch (IOException unused) {
            this.mTotalBytes = 0L;
            this.mUsedBytes = 0L;
        }
        if (storageEntry == null || !storageEntry.isMounted()) {
            throw new IOException();
        }
        if (this.mStorageEntry.isPrivate()) {
            long totalBytes = this.mStorageStatsManager.getTotalBytes(this.mStorageEntry.getFsUuid());
            this.mTotalBytes = totalBytes;
            this.mUsedBytes = totalBytes - this.mStorageStatsManager.getFreeBytes(this.mStorageEntry.getFsUuid());
        } else {
            File path = this.mStorageEntry.getPath();
            if (path == null) {
                Log.d("NtAboutPhoneBasicInfo", "Mounted public storage has null root path: " + this.mStorageEntry);
                throw new IOException();
            }
            long totalSpace = path.getTotalSpace();
            this.mTotalBytes = totalSpace;
            this.mUsedBytes = totalSpace - path.getFreeSpace();
        }
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.about.NtAboutPhoneBasicInfoPreference$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                NtAboutPhoneBasicInfoPreference.this.lambda$getStorageStatsAndUpdateUi$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: updateState */
    public void lambda$getStorageStatsAndUpdateUi$0() {
        if (this.mStorageSummary != null) {
            this.mStorageSummary.setText((getStorageSummary(R.string.storage_usage_summary, this.mUsedBytes) + " / ") + getStorageSummary(R.string.nt_storage_total_summary, this.mTotalBytes));
        }
    }

    private String getStorageSummary(int i, long j) {
        Formatter.BytesResult formatBytes = Formatter.formatBytes(this.mContext.getResources(), j, 1);
        return this.mContext.getString(i, formatBytes.value, formatBytes.units);
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
}
