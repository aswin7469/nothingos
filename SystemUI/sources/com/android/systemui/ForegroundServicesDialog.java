package com.android.systemui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
import com.android.internal.logging.MetricsLogger;
import java.util.ArrayList;
import javax.inject.Inject;

public final class ForegroundServicesDialog extends AlertActivity implements AdapterView.OnItemSelectedListener, DialogInterface.OnClickListener, AlertController.AlertParams.OnPrepareListViewListener {
    private static final String TAG = "ForegroundServicesDialog";
    /* access modifiers changed from: private */
    public PackageItemAdapter mAdapter;
    private DialogInterface.OnClickListener mAppClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialogInterface, int i) {
            String str = ((ApplicationInfo) ForegroundServicesDialog.this.mAdapter.getItem(i)).packageName;
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", str, (String) null));
            ForegroundServicesDialog.this.startActivity(intent);
            ForegroundServicesDialog.this.finish();
        }
    };
    LayoutInflater mInflater;
    private final MetricsLogger mMetricsLogger;
    private String[] mPackages;

    public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
    }

    public void onNothingSelected(AdapterView adapterView) {
    }

    public void onPrepareListView(ListView listView) {
    }

    @Inject
    ForegroundServicesDialog(MetricsLogger metricsLogger) {
        this.mMetricsLogger = metricsLogger;
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [android.content.Context, android.content.DialogInterface$OnClickListener, com.android.internal.app.AlertActivity, com.android.systemui.ForegroundServicesDialog, com.android.internal.app.AlertController$AlertParams$OnPrepareListViewListener, android.widget.AdapterView$OnItemSelectedListener] */
    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        ForegroundServicesDialog.super.onCreate(bundle);
        this.mInflater = LayoutInflater.from(this);
        this.mAdapter = new PackageItemAdapter(this);
        AlertController.AlertParams alertParams = this.mAlertParams;
        alertParams.mAdapter = this.mAdapter;
        alertParams.mOnClickListener = this.mAppClickListener;
        alertParams.mCustomTitleView = this.mInflater.inflate(C1893R.layout.foreground_service_title, (ViewGroup) null);
        alertParams.mIsSingleChoice = true;
        alertParams.mOnItemSelectedListener = this;
        alertParams.mPositiveButtonText = getString(17040167);
        alertParams.mPositiveButtonListener = this;
        alertParams.mOnPrepareListViewListener = this;
        updateApps(getIntent());
        if (this.mPackages == null) {
            Log.w(TAG, "No packages supplied");
            finish();
            return;
        }
        setupAlert();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        ForegroundServicesDialog.super.onResume();
        this.mMetricsLogger.visible(944);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        ForegroundServicesDialog.super.onPause();
        this.mMetricsLogger.hidden(944);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        ForegroundServicesDialog.super.onNewIntent(intent);
        updateApps(intent);
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        ForegroundServicesDialog.super.onStop();
        if (!isChangingConfigurations()) {
            finish();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateApps(Intent intent) {
        String[] stringArrayExtra = intent.getStringArrayExtra("packages");
        this.mPackages = stringArrayExtra;
        if (stringArrayExtra != null) {
            this.mAdapter.setPackages(stringArrayExtra);
        }
    }

    public void onClick(DialogInterface dialogInterface, int i) {
        finish();
    }

    private static class PackageItemAdapter extends ArrayAdapter<ApplicationInfo> {
        final IconDrawableFactory mIconDrawableFactory;
        final LayoutInflater mInflater;
        final PackageManager mPm;

        public PackageItemAdapter(Context context) {
            super(context, C1893R.layout.foreground_service_item);
            this.mPm = context.getPackageManager();
            this.mInflater = LayoutInflater.from(context);
            this.mIconDrawableFactory = IconDrawableFactory.newInstance(context, true);
        }

        public void setPackages(String[] strArr) {
            clear();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < strArr.length; i++) {
                try {
                    arrayList.add(this.mPm.getApplicationInfo(strArr[i], 4202496));
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
            arrayList.sort(new ApplicationInfo.DisplayNameComparator(this.mPm));
            addAll(arrayList);
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = this.mInflater.inflate(C1893R.layout.foreground_service_item, viewGroup, false);
            }
            ((ImageView) view.findViewById(C1893R.C1897id.app_icon)).setImageDrawable(this.mIconDrawableFactory.getBadgedIcon((ApplicationInfo) getItem(i)));
            ((TextView) view.findViewById(C1893R.C1897id.app_name)).setText(((ApplicationInfo) getItem(i)).loadLabel(this.mPm));
            return view;
        }
    }
}
