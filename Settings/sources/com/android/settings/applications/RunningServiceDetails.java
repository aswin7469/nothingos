package com.android.settings.applications;

import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.Debug;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.settings.applications.RunningProcessesView;
import com.android.settings.applications.RunningState;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;
import com.android.settingslib.utils.ThreadUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
/* loaded from: classes.dex */
public class RunningServiceDetails extends InstrumentedFragment implements RunningState.OnRefreshUiListener {
    ViewGroup mAllDetails;
    ActivityManager mAm;
    boolean mHaveData;
    LayoutInflater mInflater;
    RunningState.MergedItem mMergedItem;
    int mNumProcesses;
    int mNumServices;
    String mProcessName;
    TextView mProcessesHeader;
    View mRootView;
    TextView mServicesHeader;
    boolean mShowBackground;
    ViewGroup mSnippet;
    RunningProcessesView.ActiveItem mSnippetActiveItem;
    RunningProcessesView.ViewHolder mSnippetViewHolder;
    RunningState mState;
    int mUid;
    int mUserId;
    final ArrayList<ActiveDetail> mActiveDetails = new ArrayList<>();
    StringBuilder mBuilder = new StringBuilder(128);

    @Override // com.android.settingslib.core.instrumentation.Instrumentable
    public int getMetricsCategory() {
        return 85;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class ActiveDetail implements View.OnClickListener {
        RunningProcessesView.ActiveItem mActiveItem;
        ComponentName mInstaller;
        PendingIntent mManageIntent;
        Button mReportButton;
        View mRootView;
        RunningState.ServiceItem mServiceItem;
        Button mStopButton;
        RunningProcessesView.ViewHolder mViewHolder;

        ActiveDetail() {
        }

        void stopActiveService(boolean z) {
            RunningState.ServiceItem serviceItem = this.mServiceItem;
            if (!z && (serviceItem.mServiceInfo.applicationInfo.flags & 1) != 0) {
                RunningServiceDetails.this.showConfirmStopDialog(serviceItem.mRunningService.service);
                return;
            }
            RunningServiceDetails.this.getActivity().stopService(new Intent().setComponent(serviceItem.mRunningService.service));
            RunningServiceDetails runningServiceDetails = RunningServiceDetails.this;
            RunningState.MergedItem mergedItem = runningServiceDetails.mMergedItem;
            if (mergedItem == null) {
                runningServiceDetails.mState.updateNow();
                RunningServiceDetails.this.finish();
            } else if (!runningServiceDetails.mShowBackground && mergedItem.mServices.size() <= 1) {
                RunningServiceDetails.this.mState.updateNow();
                RunningServiceDetails.this.finish();
            } else {
                RunningServiceDetails.this.mState.updateNow();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:50:0x00ba, code lost:
            if (r7 == null) goto L19;
         */
        /* JADX WARN: Removed duplicated region for block: B:56:0x0138 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // android.view.View.OnClickListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public void onClick(View view) {
            FileOutputStream fileOutputStream;
            FileInputStream fileInputStream;
            if (view == this.mReportButton) {
                ApplicationErrorReport applicationErrorReport = new ApplicationErrorReport();
                applicationErrorReport.type = 5;
                applicationErrorReport.packageName = this.mServiceItem.mServiceInfo.packageName;
                applicationErrorReport.installerPackageName = this.mInstaller.getPackageName();
                applicationErrorReport.processName = this.mServiceItem.mRunningService.process;
                applicationErrorReport.time = System.currentTimeMillis();
                applicationErrorReport.systemApp = (this.mServiceItem.mServiceInfo.applicationInfo.flags & 1) != 0;
                ApplicationErrorReport.RunningServiceInfo runningServiceInfo = new ApplicationErrorReport.RunningServiceInfo();
                if (this.mActiveItem.mFirstRunTime >= 0) {
                    runningServiceInfo.durationMillis = SystemClock.elapsedRealtime() - this.mActiveItem.mFirstRunTime;
                } else {
                    runningServiceInfo.durationMillis = -1L;
                }
                ServiceInfo serviceInfo = this.mServiceItem.mServiceInfo;
                ComponentName componentName = new ComponentName(serviceInfo.packageName, serviceInfo.name);
                File fileStreamPath = RunningServiceDetails.this.getActivity().getFileStreamPath("service_dump.txt");
                FileOutputStream fileOutputStream2 = null;
                r6 = null;
                FileInputStream fileInputStream2 = null;
                try {
                    fileOutputStream = new FileOutputStream(fileStreamPath);
                    try {
                        try {
                            Debug.dumpService("activity", fileOutputStream.getFD(), new String[]{"-a", "service", componentName.flattenToString()});
                        } catch (IOException e) {
                            e = e;
                            Log.w("RunningServicesDetails", "Can't dump service: " + componentName, e);
                        }
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream2 = fileOutputStream;
                        if (fileOutputStream2 != null) {
                            try {
                                fileOutputStream2.close();
                            } catch (IOException unused) {
                            }
                        }
                        throw th;
                    }
                } catch (IOException e2) {
                    e = e2;
                    fileOutputStream = null;
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream2 != null) {
                    }
                    throw th;
                }
                try {
                    fileOutputStream.close();
                } catch (IOException unused2) {
                    try {
                        try {
                            try {
                                fileInputStream = new FileInputStream(fileStreamPath);
                            } catch (IOException unused3) {
                                fileStreamPath.delete();
                                Log.i("RunningServicesDetails", "Details: " + runningServiceInfo.serviceDetails);
                                applicationErrorReport.runningServiceInfo = runningServiceInfo;
                                Intent intent = new Intent("android.intent.action.APP_ERROR");
                                intent.setComponent(this.mInstaller);
                                intent.putExtra("android.intent.extra.BUG_REPORT", applicationErrorReport);
                                intent.addFlags(268435456);
                                RunningServiceDetails.this.startActivity(intent);
                            }
                        } catch (IOException e3) {
                            e = e3;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                    }
                    try {
                        byte[] bArr = new byte[(int) fileStreamPath.length()];
                        fileInputStream.read(bArr);
                        runningServiceInfo.serviceDetails = new String(bArr);
                        fileInputStream.close();
                    } catch (IOException e4) {
                        e = e4;
                        fileInputStream2 = fileInputStream;
                        Log.w("RunningServicesDetails", "Can't read service dump: " + componentName, e);
                        if (fileInputStream2 != null) {
                            fileInputStream2.close();
                        }
                        fileStreamPath.delete();
                        Log.i("RunningServicesDetails", "Details: " + runningServiceInfo.serviceDetails);
                        applicationErrorReport.runningServiceInfo = runningServiceInfo;
                        Intent intent2 = new Intent("android.intent.action.APP_ERROR");
                        intent2.setComponent(this.mInstaller);
                        intent2.putExtra("android.intent.extra.BUG_REPORT", applicationErrorReport);
                        intent2.addFlags(268435456);
                        RunningServiceDetails.this.startActivity(intent2);
                    } catch (Throwable th4) {
                        th = th4;
                        fileInputStream2 = fileInputStream;
                        if (fileInputStream2 != null) {
                            try {
                                fileInputStream2.close();
                            } catch (IOException unused4) {
                            }
                        }
                        throw th;
                    }
                    fileStreamPath.delete();
                    Log.i("RunningServicesDetails", "Details: " + runningServiceInfo.serviceDetails);
                    applicationErrorReport.runningServiceInfo = runningServiceInfo;
                    Intent intent22 = new Intent("android.intent.action.APP_ERROR");
                    intent22.setComponent(this.mInstaller);
                    intent22.putExtra("android.intent.extra.BUG_REPORT", applicationErrorReport);
                    intent22.addFlags(268435456);
                    RunningServiceDetails.this.startActivity(intent22);
                }
            } else if (this.mManageIntent != null) {
                try {
                    RunningServiceDetails.this.getActivity().startIntentSender(this.mManageIntent.getIntentSender(), null, 268959744, 524288, 0);
                } catch (ActivityNotFoundException e5) {
                    Log.w("RunningServicesDetails", e5);
                } catch (IntentSender.SendIntentException e6) {
                    Log.w("RunningServicesDetails", e6);
                } catch (IllegalArgumentException e7) {
                    Log.w("RunningServicesDetails", e7);
                }
            } else if (this.mServiceItem != null) {
                stopActiveService(false);
            } else {
                RunningState.BaseItem baseItem = this.mActiveItem.mItem;
                if (baseItem.mBackground) {
                    RunningServiceDetails.this.mAm.killBackgroundProcesses(baseItem.mPackageInfo.packageName);
                    RunningServiceDetails.this.finish();
                    return;
                }
                RunningServiceDetails.this.mAm.forceStopPackage(baseItem.mPackageInfo.packageName);
                RunningServiceDetails.this.finish();
            }
        }
    }

    boolean findMergedItem() {
        RunningState.MergedItem mergedItem;
        int i;
        String str;
        RunningState.ProcessItem processItem;
        RunningState.ProcessItem processItem2;
        ArrayList<RunningState.MergedItem> currentBackgroundItems = this.mShowBackground ? this.mState.getCurrentBackgroundItems() : this.mState.getCurrentMergedItems();
        if (currentBackgroundItems != null) {
            for (int i2 = 0; i2 < currentBackgroundItems.size(); i2++) {
                mergedItem = currentBackgroundItems.get(i2);
                if (mergedItem.mUserId == this.mUserId && (((i = this.mUid) < 0 || (processItem2 = mergedItem.mProcess) == null || processItem2.mUid == i) && ((str = this.mProcessName) == null || ((processItem = mergedItem.mProcess) != null && str.equals(processItem.mProcessName))))) {
                    break;
                }
            }
        }
        mergedItem = null;
        if (this.mMergedItem != mergedItem) {
            this.mMergedItem = mergedItem;
            return true;
        }
        return false;
    }

    void addServicesHeader() {
        if (this.mNumServices == 0) {
            TextView textView = (TextView) this.mInflater.inflate(R.layout.preference_category, this.mAllDetails, false);
            this.mServicesHeader = textView;
            textView.setText(R.string.runningservicedetails_services_title);
            this.mAllDetails.addView(this.mServicesHeader);
        }
        this.mNumServices++;
    }

    void addProcessesHeader() {
        if (this.mNumProcesses == 0) {
            TextView textView = (TextView) this.mInflater.inflate(R.layout.preference_category, this.mAllDetails, false);
            this.mProcessesHeader = textView;
            textView.setText(R.string.runningservicedetails_processes_title);
            this.mAllDetails.addView(this.mProcessesHeader);
        }
        this.mNumProcesses++;
    }

    /* JADX WARN: Multi-variable type inference failed */
    void addServiceDetailsView(RunningState.ServiceItem serviceItem, RunningState.MergedItem mergedItem, boolean z, boolean z2) {
        int i;
        if (z) {
            addServicesHeader();
        } else if (mergedItem.mUserId != UserHandle.myUserId()) {
            addProcessesHeader();
        }
        RunningState.ServiceItem serviceItem2 = serviceItem != null ? serviceItem : mergedItem;
        ActiveDetail activeDetail = new ActiveDetail();
        boolean z3 = false;
        View inflate = this.mInflater.inflate(R.layout.running_service_details_service, this.mAllDetails, false);
        this.mAllDetails.addView(inflate);
        activeDetail.mRootView = inflate;
        activeDetail.mServiceItem = serviceItem;
        RunningProcessesView.ViewHolder viewHolder = new RunningProcessesView.ViewHolder(inflate);
        activeDetail.mViewHolder = viewHolder;
        activeDetail.mActiveItem = viewHolder.bind(this.mState, serviceItem2, this.mBuilder);
        if (!z2) {
            inflate.findViewById(R.id.service).setVisibility(8);
        }
        if (serviceItem != null) {
            ActivityManager.RunningServiceInfo runningServiceInfo = serviceItem.mRunningService;
            if (runningServiceInfo.clientLabel != 0) {
                activeDetail.mManageIntent = this.mAm.getRunningServiceControlPanel(runningServiceInfo.service);
            }
        }
        TextView textView = (TextView) inflate.findViewById(R.id.comp_description);
        activeDetail.mStopButton = (Button) inflate.findViewById(R.id.left_button);
        activeDetail.mReportButton = (Button) inflate.findViewById(R.id.right_button);
        if (z && mergedItem.mUserId != UserHandle.myUserId()) {
            textView.setVisibility(8);
            inflate.findViewById(R.id.control_buttons_panel).setVisibility(8);
        } else {
            if (serviceItem != null && serviceItem.mServiceInfo.descriptionRes != 0) {
                PackageManager packageManager = getActivity().getPackageManager();
                ServiceInfo serviceInfo = serviceItem.mServiceInfo;
                textView.setText(packageManager.getText(serviceInfo.packageName, serviceInfo.descriptionRes, serviceInfo.applicationInfo));
            } else if (mergedItem.mBackground) {
                textView.setText(R.string.background_process_stop_description);
            } else if (activeDetail.mManageIntent != null) {
                try {
                    textView.setText(getActivity().getString(R.string.service_manage_description, new Object[]{getActivity().getPackageManager().getResourcesForApplication(serviceItem.mRunningService.clientPackage).getString(serviceItem.mRunningService.clientLabel)}));
                } catch (PackageManager.NameNotFoundException unused) {
                }
            } else {
                FragmentActivity activity = getActivity();
                if (serviceItem != null) {
                    i = R.string.service_stop_description;
                } else {
                    i = R.string.heavy_weight_stop_description;
                }
                textView.setText(activity.getText(i));
            }
            activeDetail.mStopButton.setOnClickListener(activeDetail);
            activeDetail.mStopButton.setText(getActivity().getText(activeDetail.mManageIntent != null ? R.string.service_manage : R.string.service_stop));
            activeDetail.mReportButton.setOnClickListener(activeDetail);
            activeDetail.mReportButton.setText(17041271);
            if (Settings.Global.getInt(getActivity().getContentResolver(), "send_action_app_error", 0) != 0 && serviceItem != null) {
                FragmentActivity activity2 = getActivity();
                ServiceInfo serviceInfo2 = serviceItem.mServiceInfo;
                ComponentName errorReportReceiver = ApplicationErrorReport.getErrorReportReceiver(activity2, serviceInfo2.packageName, serviceInfo2.applicationInfo.flags);
                activeDetail.mInstaller = errorReportReceiver;
                Button button = activeDetail.mReportButton;
                if (errorReportReceiver != null) {
                    z3 = true;
                }
                button.setEnabled(z3);
            } else {
                activeDetail.mReportButton.setEnabled(false);
            }
        }
        this.mActiveDetails.add(activeDetail);
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x009b A[ADDED_TO_REGION] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void addProcessDetailsView(RunningState.ProcessItem processItem, boolean z) {
        addProcessesHeader();
        ActiveDetail activeDetail = new ActiveDetail();
        View inflate = this.mInflater.inflate(R.layout.running_service_details_process, this.mAllDetails, false);
        this.mAllDetails.addView(inflate);
        activeDetail.mRootView = inflate;
        RunningProcessesView.ViewHolder viewHolder = new RunningProcessesView.ViewHolder(inflate);
        activeDetail.mViewHolder = viewHolder;
        activeDetail.mActiveItem = viewHolder.bind(this.mState, processItem, this.mBuilder);
        TextView textView = (TextView) inflate.findViewById(R.id.comp_description);
        if (processItem.mUserId != UserHandle.myUserId()) {
            textView.setVisibility(8);
        } else if (z) {
            textView.setText(R.string.main_running_process_description);
        } else {
            CharSequence charSequence = null;
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = processItem.mRunningProcessInfo;
            ComponentName componentName = runningAppProcessInfo.importanceReasonComponent;
            int i = runningAppProcessInfo.importanceReasonCode;
            if (i != 1) {
                if (i != 2) {
                    i = 0;
                } else {
                    i = R.string.process_service_in_use_description;
                    if (componentName != null) {
                        ServiceInfo serviceInfo = getActivity().getPackageManager().getServiceInfo(runningAppProcessInfo.importanceReasonComponent, 0);
                        charSequence = RunningState.makeLabel(getActivity().getPackageManager(), serviceInfo.name, serviceInfo);
                    }
                }
                if (i != 0 && charSequence != null) {
                    textView.setText(getActivity().getString(i, new Object[]{charSequence}));
                }
            } else {
                i = R.string.process_provider_in_use_description;
                if (componentName != null) {
                    ProviderInfo providerInfo = getActivity().getPackageManager().getProviderInfo(runningAppProcessInfo.importanceReasonComponent, 0);
                    charSequence = RunningState.makeLabel(getActivity().getPackageManager(), providerInfo.name, providerInfo);
                }
                if (i != 0) {
                    textView.setText(getActivity().getString(i, new Object[]{charSequence}));
                }
            }
            if (i != 0) {
            }
        }
        this.mActiveDetails.add(activeDetail);
    }

    void addDetailsViews(RunningState.MergedItem mergedItem, boolean z, boolean z2) {
        RunningState.ProcessItem processItem;
        if (mergedItem != null) {
            boolean z3 = true;
            if (z) {
                for (int i = 0; i < mergedItem.mServices.size(); i++) {
                    addServiceDetailsView(mergedItem.mServices.get(i), mergedItem, true, true);
                }
            }
            if (!z2) {
                return;
            }
            if (mergedItem.mServices.size() <= 0) {
                if (mergedItem.mUserId == UserHandle.myUserId()) {
                    z3 = false;
                }
                addServiceDetailsView(null, mergedItem, false, z3);
                return;
            }
            int i2 = -1;
            while (i2 < mergedItem.mOtherProcesses.size()) {
                if (i2 < 0) {
                    processItem = mergedItem.mProcess;
                } else {
                    processItem = mergedItem.mOtherProcesses.get(i2);
                }
                if (processItem == null || processItem.mPid > 0) {
                    addProcessDetailsView(processItem, i2 < 0);
                }
                i2++;
            }
        }
    }

    void addDetailViews() {
        ArrayList<RunningState.MergedItem> arrayList;
        for (int size = this.mActiveDetails.size() - 1; size >= 0; size--) {
            this.mAllDetails.removeView(this.mActiveDetails.get(size).mRootView);
        }
        this.mActiveDetails.clear();
        TextView textView = this.mServicesHeader;
        if (textView != null) {
            this.mAllDetails.removeView(textView);
            this.mServicesHeader = null;
        }
        TextView textView2 = this.mProcessesHeader;
        if (textView2 != null) {
            this.mAllDetails.removeView(textView2);
            this.mProcessesHeader = null;
        }
        this.mNumProcesses = 0;
        this.mNumServices = 0;
        RunningState.MergedItem mergedItem = this.mMergedItem;
        if (mergedItem != null) {
            if (mergedItem.mUser != null) {
                if (this.mShowBackground) {
                    arrayList = new ArrayList<>(this.mMergedItem.mChildren);
                    Collections.sort(arrayList, this.mState.mBackgroundComparator);
                } else {
                    arrayList = mergedItem.mChildren;
                }
                for (int i = 0; i < arrayList.size(); i++) {
                    addDetailsViews(arrayList.get(i), true, false);
                }
                for (int i2 = 0; i2 < arrayList.size(); i2++) {
                    addDetailsViews(arrayList.get(i2), false, true);
                }
                return;
            }
            addDetailsViews(mergedItem, true, true);
        }
    }

    void refreshUi(boolean z) {
        if (findMergedItem()) {
            z = true;
        }
        if (z) {
            RunningState.MergedItem mergedItem = this.mMergedItem;
            if (mergedItem != null) {
                this.mSnippetActiveItem = this.mSnippetViewHolder.bind(this.mState, mergedItem, this.mBuilder);
            } else {
                RunningProcessesView.ActiveItem activeItem = this.mSnippetActiveItem;
                if (activeItem != null) {
                    activeItem.mHolder.size.setText("");
                    this.mSnippetActiveItem.mHolder.uptime.setText("");
                    this.mSnippetActiveItem.mHolder.description.setText(R.string.no_services);
                } else {
                    finish();
                    return;
                }
            }
            addDetailViews();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finish() {
        ThreadUtils.postOnMainThread(new Runnable() { // from class: com.android.settings.applications.RunningServiceDetails$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RunningServiceDetails.this.lambda$finish$0();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finish$0() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.onBackPressed();
        }
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
        this.mUid = getArguments().getInt("uid", -1);
        this.mUserId = getArguments().getInt("user_id", 0);
        this.mProcessName = getArguments().getString("process", null);
        this.mShowBackground = getArguments().getBoolean("background", false);
        this.mAm = (ActivityManager) getActivity().getSystemService("activity");
        this.mInflater = (LayoutInflater) getActivity().getSystemService("layout_inflater");
        this.mState = RunningState.getInstance(getActivity());
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.running_service_details, viewGroup, false);
        Utils.prepareCustomPreferencesList(viewGroup, inflate, inflate, false);
        this.mRootView = inflate;
        this.mAllDetails = (ViewGroup) inflate.findViewById(R.id.all_details);
        ViewGroup viewGroup2 = (ViewGroup) inflate.findViewById(R.id.snippet);
        this.mSnippet = viewGroup2;
        this.mSnippetViewHolder = new RunningProcessesView.ViewHolder(viewGroup2);
        ensureData();
        return inflate;
    }

    @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        this.mHaveData = false;
        this.mState.pause();
    }

    @Override // com.android.settings.core.InstrumentedFragment, com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        ensureData();
    }

    ActiveDetail activeDetailForService(ComponentName componentName) {
        ActivityManager.RunningServiceInfo runningServiceInfo;
        for (int i = 0; i < this.mActiveDetails.size(); i++) {
            ActiveDetail activeDetail = this.mActiveDetails.get(i);
            RunningState.ServiceItem serviceItem = activeDetail.mServiceItem;
            if (serviceItem != null && (runningServiceInfo = serviceItem.mRunningService) != null && componentName.equals(runningServiceInfo.service)) {
                return activeDetail;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showConfirmStopDialog(ComponentName componentName) {
        MyAlertDialogFragment newConfirmStop = MyAlertDialogFragment.newConfirmStop(1, componentName);
        newConfirmStop.setTargetFragment(this, 0);
        newConfirmStop.show(getFragmentManager(), "confirmstop");
    }

    /* loaded from: classes.dex */
    public static class MyAlertDialogFragment extends InstrumentedDialogFragment {
        @Override // com.android.settingslib.core.instrumentation.Instrumentable
        public int getMetricsCategory() {
            return 536;
        }

        public static MyAlertDialogFragment newConfirmStop(int i, ComponentName componentName) {
            MyAlertDialogFragment myAlertDialogFragment = new MyAlertDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", i);
            bundle.putParcelable("comp", componentName);
            myAlertDialogFragment.setArguments(bundle);
            return myAlertDialogFragment;
        }

        RunningServiceDetails getOwner() {
            return (RunningServiceDetails) getTargetFragment();
        }

        @Override // androidx.fragment.app.DialogFragment
        public Dialog onCreateDialog(Bundle bundle) {
            int i = getArguments().getInt("id");
            if (i == 1) {
                final ComponentName componentName = (ComponentName) getArguments().getParcelable("comp");
                if (getOwner().activeDetailForService(componentName) != null) {
                    return new AlertDialog.Builder(getActivity()).setTitle(getActivity().getString(R.string.runningservicedetails_stop_dlg_title)).setMessage(getActivity().getString(R.string.runningservicedetails_stop_dlg_text)).setPositiveButton(R.string.dlg_ok, new DialogInterface.OnClickListener() { // from class: com.android.settings.applications.RunningServiceDetails.MyAlertDialogFragment.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i2) {
                            ActiveDetail activeDetailForService = MyAlertDialogFragment.this.getOwner().activeDetailForService(componentName);
                            if (activeDetailForService != null) {
                                activeDetailForService.stopActiveService(true);
                            }
                        }
                    }).setNegativeButton(R.string.dlg_cancel, (DialogInterface.OnClickListener) null).create();
                }
                return null;
            }
            throw new IllegalArgumentException("unknown id " + i);
        }
    }

    void ensureData() {
        if (!this.mHaveData) {
            this.mHaveData = true;
            this.mState.resume(this);
            this.mState.waitForData();
            refreshUi(true);
        }
    }

    void updateTimes() {
        RunningProcessesView.ActiveItem activeItem = this.mSnippetActiveItem;
        if (activeItem != null) {
            activeItem.updateTime(getActivity(), this.mBuilder);
        }
        for (int i = 0; i < this.mActiveDetails.size(); i++) {
            this.mActiveDetails.get(i).mActiveItem.updateTime(getActivity(), this.mBuilder);
        }
    }

    @Override // com.android.settings.applications.RunningState.OnRefreshUiListener
    public void onRefreshUi(int i) {
        if (getActivity() == null) {
            return;
        }
        if (i == 0) {
            updateTimes();
        } else if (i == 1) {
            refreshUi(false);
            updateTimes();
        } else if (i != 2) {
        } else {
            refreshUi(true);
            updateTimes();
        }
    }
}
