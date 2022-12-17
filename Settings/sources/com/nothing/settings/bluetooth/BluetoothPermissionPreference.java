package com.nothing.settings.bluetooth;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settings.R$id;
import com.android.settings.R$layout;

public class BluetoothPermissionPreference extends Preference implements View.OnClickListener {
    private Context mContext;
    private View mPermissionContent;
    private String mPermissionSummary;
    private String mPermissionTitle;
    private TextView mSummaryTextView;
    private TextView mTitleTextView;

    public BluetoothPermissionPreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mContext = context;
        setLayoutResource(R$layout.nt_preference_permission);
    }

    public BluetoothPermissionPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R$layout.nt_preference_permission);
        this.mContext = context;
    }

    public BluetoothPermissionPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R$layout.nt_preference_permission);
        this.mContext = context;
    }

    public BluetoothPermissionPreference(Context context) {
        super(context);
        setLayoutResource(R$layout.nt_preference_permission);
        this.mContext = context;
    }

    public void setPermissionTitle(String str) {
        this.mPermissionTitle = str;
    }

    public void setPermissionSummary(String str) {
        this.mPermissionSummary = str;
    }

    public void updateUI() {
        if (this.mTitleTextView != null && !TextUtils.isEmpty(this.mPermissionTitle)) {
            this.mTitleTextView.setText(this.mPermissionTitle);
        }
        if (this.mSummaryTextView != null && !TextUtils.isEmpty(this.mPermissionSummary)) {
            this.mSummaryTextView.setText(this.mPermissionSummary);
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View findViewById = preferenceViewHolder.findViewById(R$id.permission);
        this.mPermissionContent = findViewById;
        findViewById.setOnClickListener(this);
        this.mTitleTextView = (TextView) preferenceViewHolder.findViewById(R$id.permission_title);
        this.mSummaryTextView = (TextView) preferenceViewHolder.findViewById(R$id.permission_summary);
        updateUI();
    }

    public void onClick(View view) {
        try {
            if (view.getId() == R$id.permission) {
                startManagePermissionsActivity();
            }
        } catch (Exception unused) {
        }
    }

    private void startManagePermissionsActivity() {
        try {
            Intent intent = new Intent("android.intent.action.MANAGE_APP_PERMISSIONS");
            intent.putExtra("android.intent.extra.PACKAGE_NAME", "com.nothing.smartcenter");
            intent.putExtra("hideInfoButton", true);
            Context context = this.mContext;
            if (context != null) {
                context.startActivity(intent);
            }
        } catch (ActivityNotFoundException unused) {
            Log.w("BluetoothPermissionPreference", "No app can handle android.intent.action.MANAGE_APP_PERMISSIONS");
        }
    }
}
