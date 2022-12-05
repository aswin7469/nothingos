package com.android.settings.applications.specialaccess.deviceadmin;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.Dialog;
import android.app.admin.DeviceAdminInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import com.android.settings.R;
import com.android.settings.fuelgauge.BatteryUtils;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.users.UserDialogs;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
public class DeviceAdminAdd extends CollapsingToolbarBaseActivity {
    Button mActionButton;
    TextView mAddMsg;
    ImageView mAddMsgExpander;
    String mAddMsgText;
    boolean mAdding;
    boolean mAddingProfileOwner;
    TextView mAdminDescription;
    ImageView mAdminIcon;
    TextView mAdminName;
    ViewGroup mAdminPolicies;
    boolean mAdminPoliciesInitialized;
    TextView mAdminWarning;
    AppOpsManager mAppOps;
    Button mCancelButton;
    DevicePolicyManager mDPM;
    DeviceAdminInfo mDeviceAdmin;
    Handler mHandler;
    private LayoutInflater mLayoutInflaternflater;
    String mProfileOwnerName;
    TextView mProfileOwnerWarning;
    boolean mRefreshing;
    TextView mSupportMessage;
    Button mUninstallButton;
    boolean mWaitingForRemoveMsg;
    private final IBinder mToken = new Binder();
    boolean mAddMsgEllipsized = true;
    boolean mUninstalling = false;
    boolean mIsCalledFromSupportDialog = false;

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x014d, code lost:
        r9.activityInfo = r2;
        new android.app.admin.DeviceAdminInfo(r12, r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0154, code lost:
        r13 = true;
     */
    @Override // com.android.settingslib.collapsingtoolbar.CollapsingToolbarBaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mHandler = new Handler(getMainLooper());
        this.mDPM = (DevicePolicyManager) getSystemService("device_policy");
        this.mAppOps = (AppOpsManager) getSystemService("appops");
        this.mLayoutInflaternflater = (LayoutInflater) getSystemService("layout_inflater");
        PackageManager packageManager = getPackageManager();
        if ((getIntent().getFlags() & 268435456) != 0) {
            Log.w("DeviceAdminAdd", "Cannot start ADD_DEVICE_ADMIN as a new task");
            finish();
            return;
        }
        this.mIsCalledFromSupportDialog = getIntent().getBooleanExtra("android.app.extra.CALLED_FROM_SUPPORT_DIALOG", false);
        String action = getIntent().getAction();
        ComponentName componentName = (ComponentName) getIntent().getParcelableExtra("android.app.extra.DEVICE_ADMIN");
        if (componentName == null) {
            Optional<ComponentName> findAdminWithPackageName = findAdminWithPackageName(getIntent().getStringExtra("android.app.extra.DEVICE_ADMIN_PACKAGE_NAME"));
            if (!findAdminWithPackageName.isPresent()) {
                Log.w("DeviceAdminAdd", "No component specified in " + action);
                finish();
                return;
            }
            componentName = findAdminWithPackageName.get();
            this.mUninstalling = true;
        }
        if (action != null && action.equals("android.app.action.SET_PROFILE_OWNER")) {
            setResult(0);
            setFinishOnTouchOutside(true);
            this.mAddingProfileOwner = true;
            this.mProfileOwnerName = getIntent().getStringExtra("android.app.extra.PROFILE_OWNER_NAME");
            String callingPackage = getCallingPackage();
            if (callingPackage == null || !callingPackage.equals(componentName.getPackageName())) {
                Log.e("DeviceAdminAdd", "Unknown or incorrect caller");
                finish();
                return;
            }
            try {
                if ((packageManager.getPackageInfo(callingPackage, 0).applicationInfo.flags & 1) == 0) {
                    Log.e("DeviceAdminAdd", "Cannot set a non-system app as a profile owner");
                    finish();
                    return;
                }
            } catch (PackageManager.NameNotFoundException unused) {
                Log.e("DeviceAdminAdd", "Cannot find the package " + callingPackage);
                finish();
                return;
            }
        }
        try {
            ActivityInfo receiverInfo = packageManager.getReceiverInfo(componentName, 128);
            if (!this.mDPM.isAdminActive(componentName)) {
                List<ResolveInfo> queryBroadcastReceivers = packageManager.queryBroadcastReceivers(new Intent("android.app.action.DEVICE_ADMIN_ENABLED"), 32768);
                int size = queryBroadcastReceivers == null ? 0 : queryBroadcastReceivers.size();
                int i = 0;
                while (true) {
                    if (i >= size) {
                        break;
                    }
                    ResolveInfo resolveInfo = queryBroadcastReceivers.get(i);
                    if (!receiverInfo.packageName.equals(resolveInfo.activityInfo.packageName) || !receiverInfo.name.equals(resolveInfo.activityInfo.name)) {
                        i++;
                    } else {
                        try {
                            break;
                        } catch (IOException e) {
                            Log.w("DeviceAdminAdd", "Bad " + resolveInfo.activityInfo, e);
                        } catch (XmlPullParserException e2) {
                            Log.w("DeviceAdminAdd", "Bad " + resolveInfo.activityInfo, e2);
                        }
                    }
                }
                boolean z = false;
                if (!z) {
                    Log.w("DeviceAdminAdd", "Request to add invalid device admin: " + componentName);
                    finish();
                    return;
                }
            }
            ResolveInfo resolveInfo2 = new ResolveInfo();
            resolveInfo2.activityInfo = receiverInfo;
            try {
                this.mDeviceAdmin = new DeviceAdminInfo(this, resolveInfo2);
                if ("android.app.action.ADD_DEVICE_ADMIN".equals(getIntent().getAction())) {
                    this.mRefreshing = false;
                    if (this.mDPM.isAdminActive(componentName)) {
                        if (this.mDPM.isRemovingAdmin(componentName, Process.myUserHandle().getIdentifier())) {
                            Log.w("DeviceAdminAdd", "Requested admin is already being removed: " + componentName);
                            finish();
                            return;
                        }
                        ArrayList usedPolicies = this.mDeviceAdmin.getUsedPolicies();
                        int i2 = 0;
                        while (true) {
                            if (i2 >= usedPolicies.size()) {
                                break;
                            } else if (!this.mDPM.hasGrantedPolicy(componentName, ((DeviceAdminInfo.PolicyInfo) usedPolicies.get(i2)).ident)) {
                                this.mRefreshing = true;
                                break;
                            } else {
                                i2++;
                            }
                        }
                        if (!this.mRefreshing) {
                            setResult(-1);
                            finish();
                            return;
                        }
                    }
                }
                CharSequence charSequenceExtra = getIntent().getCharSequenceExtra("android.app.extra.ADD_EXPLANATION");
                if (charSequenceExtra != null) {
                    this.mAddMsgText = charSequenceExtra.toString();
                }
                if (this.mAddingProfileOwner) {
                    if (!this.mDPM.hasUserSetupCompleted()) {
                        addAndFinish();
                        return;
                    }
                    String string = getString(17039914);
                    if (TextUtils.isEmpty(string)) {
                        Log.w("DeviceAdminAdd", "Unable to set profile owner post-setup, no default supervisorprofile owner defined");
                        finish();
                        return;
                    }
                    ComponentName unflattenFromString = ComponentName.unflattenFromString(string);
                    if (unflattenFromString == null || componentName.compareTo(unflattenFromString) != 0) {
                        Log.w("DeviceAdminAdd", "Unable to set non-default profile owner post-setup " + componentName);
                        finish();
                        return;
                    }
                    AlertDialog create = new AlertDialog.Builder(this).setTitle(getText(R.string.profile_owner_add_title_simplified)).setView(R.layout.profile_owner_add).setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() { // from class: com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd.2
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i3) {
                            DeviceAdminAdd.this.addAndFinish();
                        }
                    }).setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) null).setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd.1
                        @Override // android.content.DialogInterface.OnDismissListener
                        public void onDismiss(DialogInterface dialogInterface) {
                            DeviceAdminAdd.this.finish();
                        }
                    }).create();
                    create.show();
                    Button button = create.getButton(-1);
                    this.mActionButton = button;
                    button.setFilterTouchesWhenObscured(true);
                    TextView textView = (TextView) create.findViewById(R.id.add_msg_simplified);
                    this.mAddMsg = textView;
                    textView.setText(this.mAddMsgText);
                    TextView textView2 = (TextView) create.findViewById(R.id.admin_warning_simplified);
                    this.mAdminWarning = textView2;
                    textView2.setText(getString(R.string.device_admin_warning_simplified, new Object[]{this.mProfileOwnerName}));
                    return;
                }
                setContentView(R.layout.device_admin_add);
                this.mAdminIcon = (ImageView) findViewById(R.id.admin_icon);
                this.mAdminName = (TextView) findViewById(R.id.admin_name);
                this.mAdminDescription = (TextView) findViewById(R.id.admin_description);
                this.mProfileOwnerWarning = (TextView) findViewById(R.id.profile_owner_warning);
                this.mAddMsg = (TextView) findViewById(R.id.add_msg);
                this.mAddMsgExpander = (ImageView) findViewById(R.id.add_msg_expander);
                this.mAddMsgExpander.setOnClickListener(new View.OnClickListener() { // from class: com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        DeviceAdminAdd deviceAdminAdd = DeviceAdminAdd.this;
                        deviceAdminAdd.toggleMessageEllipsis(deviceAdminAdd.mAddMsg);
                    }
                });
                this.mAddMsg.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd.4
                    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                    public void onGlobalLayout() {
                        int i3 = 0;
                        boolean z2 = DeviceAdminAdd.this.mAddMsg.getLineCount() <= DeviceAdminAdd.this.getEllipsizedLines();
                        ImageView imageView = DeviceAdminAdd.this.mAddMsgExpander;
                        if (z2) {
                            i3 = 8;
                        }
                        imageView.setVisibility(i3);
                        if (z2) {
                            ((View) DeviceAdminAdd.this.mAddMsgExpander.getParent()).invalidate();
                        }
                        DeviceAdminAdd.this.mAddMsg.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
                toggleMessageEllipsis(this.mAddMsg);
                this.mAdminWarning = (TextView) findViewById(R.id.admin_warning);
                this.mAdminPolicies = (ViewGroup) findViewById(R.id.admin_policies);
                this.mSupportMessage = (TextView) findViewById(R.id.admin_support_message);
                Button button2 = (Button) findViewById(R.id.cancel_button);
                this.mCancelButton = button2;
                button2.setFilterTouchesWhenObscured(true);
                this.mCancelButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd.5
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        EventLog.writeEvent(90202, DeviceAdminAdd.this.mDeviceAdmin.getActivityInfo().applicationInfo.uid);
                        DeviceAdminAdd.this.finish();
                    }
                });
                Button button3 = (Button) findViewById(R.id.uninstall_button);
                this.mUninstallButton = button3;
                button3.setFilterTouchesWhenObscured(true);
                this.mUninstallButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd.6
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        EventLog.writeEvent(90203, DeviceAdminAdd.this.mDeviceAdmin.getActivityInfo().applicationInfo.uid);
                        DeviceAdminAdd deviceAdminAdd = DeviceAdminAdd.this;
                        deviceAdminAdd.mDPM.uninstallPackageWithActiveAdmins(deviceAdminAdd.mDeviceAdmin.getPackageName());
                        DeviceAdminAdd.this.finish();
                    }
                });
                this.mActionButton = (Button) findViewById(R.id.action_button);
                View findViewById = findViewById(R.id.restricted_action);
                findViewById.setFilterTouchesWhenObscured(true);
                findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd.7
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        if (!DeviceAdminAdd.this.mActionButton.isEnabled()) {
                            DeviceAdminAdd.this.showPolicyTransparencyDialogIfRequired();
                            return;
                        }
                        DeviceAdminAdd deviceAdminAdd = DeviceAdminAdd.this;
                        if (!deviceAdminAdd.mAdding) {
                            if (deviceAdminAdd.isManagedProfile(deviceAdminAdd.mDeviceAdmin) && DeviceAdminAdd.this.mDeviceAdmin.getComponent().equals(DeviceAdminAdd.this.mDPM.getProfileOwner())) {
                                final int myUserId = UserHandle.myUserId();
                                UserDialogs.createRemoveDialog(DeviceAdminAdd.this, myUserId, new DialogInterface.OnClickListener() { // from class: com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd.7.1
                                    @Override // android.content.DialogInterface.OnClickListener
                                    public void onClick(DialogInterface dialogInterface, int i3) {
                                        UserManager.get(DeviceAdminAdd.this).removeUser(myUserId);
                                        DeviceAdminAdd.this.finish();
                                    }
                                }).show();
                                return;
                            }
                            DeviceAdminAdd deviceAdminAdd2 = DeviceAdminAdd.this;
                            if (deviceAdminAdd2.mUninstalling) {
                                deviceAdminAdd2.mDPM.uninstallPackageWithActiveAdmins(deviceAdminAdd2.mDeviceAdmin.getPackageName());
                                DeviceAdminAdd.this.finish();
                                return;
                            } else if (deviceAdminAdd2.mWaitingForRemoveMsg) {
                                return;
                            } else {
                                try {
                                    ActivityManager.getService().stopAppSwitches();
                                } catch (RemoteException unused2) {
                                }
                                DeviceAdminAdd deviceAdminAdd3 = DeviceAdminAdd.this;
                                deviceAdminAdd3.mWaitingForRemoveMsg = true;
                                deviceAdminAdd3.mDPM.getRemoveWarning(deviceAdminAdd3.mDeviceAdmin.getComponent(), new RemoteCallback(new RemoteCallback.OnResultListener() { // from class: com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd.7.2
                                    public void onResult(Bundle bundle2) {
                                        DeviceAdminAdd.this.continueRemoveAction(bundle2 != null ? bundle2.getCharSequence("android.app.extra.DISABLE_WARNING") : null);
                                    }
                                }, DeviceAdminAdd.this.mHandler));
                                DeviceAdminAdd.this.getWindow().getDecorView().getHandler().postDelayed(new Runnable() { // from class: com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd.7.3
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        DeviceAdminAdd.this.continueRemoveAction(null);
                                    }
                                }, 2000L);
                                return;
                            }
                        }
                        deviceAdminAdd.addAndFinish();
                    }
                });
            } catch (IOException e3) {
                Log.w("DeviceAdminAdd", "Unable to retrieve device policy " + componentName, e3);
                finish();
            } catch (XmlPullParserException e4) {
                Log.w("DeviceAdminAdd", "Unable to retrieve device policy " + componentName, e4);
                finish();
            }
        } catch (PackageManager.NameNotFoundException e5) {
            Log.w("DeviceAdminAdd", "Unable to retrieve device policy " + componentName, e5);
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPolicyTransparencyDialogIfRequired() {
        RestrictedLockUtils.EnforcedAdmin adminEnforcingCantRemoveProfile;
        if (!isManagedProfile(this.mDeviceAdmin) || !this.mDeviceAdmin.getComponent().equals(this.mDPM.getProfileOwner())) {
            return;
        }
        ComponentName profileOwnerAsUser = this.mDPM.getProfileOwnerAsUser(getUserId());
        if (profileOwnerAsUser != null && this.mDPM.isOrganizationOwnedDeviceWithManagedProfile()) {
            adminEnforcingCantRemoveProfile = new RestrictedLockUtils.EnforcedAdmin(profileOwnerAsUser, "no_remove_managed_profile", UserHandle.of(getUserId()));
        } else if (hasBaseCantRemoveProfileRestriction()) {
            return;
        } else {
            adminEnforcingCantRemoveProfile = getAdminEnforcingCantRemoveProfile();
        }
        if (adminEnforcingCantRemoveProfile == null) {
            return;
        }
        RestrictedLockUtils.sendShowAdminSupportDetailsIntent(this, adminEnforcingCantRemoveProfile);
    }

    void addAndFinish() {
        try {
            logSpecialPermissionChange(true, this.mDeviceAdmin.getComponent().getPackageName());
            this.mDPM.setActiveAdmin(this.mDeviceAdmin.getComponent(), this.mRefreshing);
            EventLog.writeEvent(90201, this.mDeviceAdmin.getActivityInfo().applicationInfo.uid);
            unrestrictAppIfPossible(BatteryUtils.getInstance(this));
            setResult(-1);
        } catch (RuntimeException e) {
            Log.w("DeviceAdminAdd", "Exception trying to activate admin " + this.mDeviceAdmin.getComponent(), e);
            if (this.mDPM.isAdminActive(this.mDeviceAdmin.getComponent())) {
                setResult(-1);
            }
        }
        if (this.mAddingProfileOwner) {
            try {
                this.mDPM.setProfileOwner(this.mDeviceAdmin.getComponent(), this.mProfileOwnerName, UserHandle.myUserId());
            } catch (RuntimeException unused) {
                setResult(0);
            }
        }
        finish();
    }

    void unrestrictAppIfPossible(BatteryUtils batteryUtils) {
        batteryUtils.clearForceAppStandby(this.mDeviceAdmin.getComponent().getPackageName());
    }

    void continueRemoveAction(CharSequence charSequence) {
        if (!this.mWaitingForRemoveMsg) {
            return;
        }
        this.mWaitingForRemoveMsg = false;
        if (charSequence == null) {
            try {
                ActivityManager.getService().resumeAppSwitches();
            } catch (RemoteException unused) {
            }
            logSpecialPermissionChange(false, this.mDeviceAdmin.getComponent().getPackageName());
            this.mDPM.removeActiveAdmin(this.mDeviceAdmin.getComponent());
            finish();
            return;
        }
        try {
            ActivityManager.getService().stopAppSwitches();
        } catch (RemoteException unused2) {
        }
        Bundle bundle = new Bundle();
        bundle.putCharSequence("android.app.extra.DISABLE_WARNING", charSequence);
        showDialog(1, bundle);
    }

    void logSpecialPermissionChange(boolean z, String str) {
        FeatureFactory.getFactory(this).getMetricsFeatureProvider().action(0, z ? 766 : 767, 0, str, 0);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mActionButton.setEnabled(true);
        if (!this.mAddingProfileOwner) {
            updateInterface();
        }
        this.mAppOps.setUserRestriction(24, true, this.mToken);
        this.mAppOps.setUserRestriction(45, true, this.mToken);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.mActionButton.setEnabled(false);
        this.mAppOps.setUserRestriction(24, false, this.mToken);
        this.mAppOps.setUserRestriction(45, false, this.mToken);
        try {
            ActivityManager.getService().resumeAppSwitches();
        } catch (RemoteException unused) {
        }
    }

    @Override // android.app.Activity
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        if (this.mIsCalledFromSupportDialog) {
            finish();
        }
    }

    @Override // android.app.Activity
    protected Dialog onCreateDialog(int i, Bundle bundle) {
        if (i == 1) {
            CharSequence charSequence = bundle.getCharSequence("android.app.extra.DISABLE_WARNING");
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(charSequence);
            builder.setPositiveButton(R.string.dlg_ok, new DialogInterface.OnClickListener() { // from class: com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd.8
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    try {
                        ActivityManager.getService().resumeAppSwitches();
                    } catch (RemoteException unused) {
                    }
                    DeviceAdminAdd deviceAdminAdd = DeviceAdminAdd.this;
                    deviceAdminAdd.mDPM.removeActiveAdmin(deviceAdminAdd.mDeviceAdmin.getComponent());
                    DeviceAdminAdd.this.finish();
                }
            });
            builder.setNegativeButton(R.string.dlg_cancel, (DialogInterface.OnClickListener) null);
            return builder.create();
        }
        return super.onCreateDialog(i, bundle);
    }

    void updateInterface() {
        findViewById(R.id.restricted_icon).setVisibility(8);
        this.mAdminIcon.setImageDrawable(this.mDeviceAdmin.loadIcon(getPackageManager()));
        this.mAdminName.setText(this.mDeviceAdmin.loadLabel(getPackageManager()));
        try {
            this.mAdminDescription.setText(this.mDeviceAdmin.loadDescription(getPackageManager()));
            this.mAdminDescription.setVisibility(0);
        } catch (Resources.NotFoundException unused) {
            this.mAdminDescription.setVisibility(8);
        }
        if (!TextUtils.isEmpty(this.mAddMsgText)) {
            this.mAddMsg.setText(this.mAddMsgText);
            this.mAddMsg.setVisibility(0);
        } else {
            this.mAddMsg.setVisibility(8);
            this.mAddMsgExpander.setVisibility(8);
        }
        boolean z = true;
        if (!this.mRefreshing && !this.mAddingProfileOwner && this.mDPM.isAdminActive(this.mDeviceAdmin.getComponent())) {
            this.mAdding = false;
            boolean equals = this.mDeviceAdmin.getComponent().equals(this.mDPM.getProfileOwner());
            boolean isManagedProfile = isManagedProfile(this.mDeviceAdmin);
            if (equals && isManagedProfile) {
                this.mAdminWarning.setText(R.string.admin_profile_owner_message);
                this.mActionButton.setText(R.string.remove_managed_profile_label);
                RestrictedLockUtils.EnforcedAdmin adminEnforcingCantRemoveProfile = getAdminEnforcingCantRemoveProfile();
                boolean hasBaseCantRemoveProfileRestriction = hasBaseCantRemoveProfileRestriction();
                if ((hasBaseCantRemoveProfileRestriction && this.mDPM.isOrganizationOwnedDeviceWithManagedProfile()) || (adminEnforcingCantRemoveProfile != null && !hasBaseCantRemoveProfileRestriction)) {
                    findViewById(R.id.restricted_icon).setVisibility(0);
                }
                Button button = this.mActionButton;
                if (adminEnforcingCantRemoveProfile != null || hasBaseCantRemoveProfileRestriction) {
                    z = false;
                }
                button.setEnabled(z);
            } else if (equals || this.mDeviceAdmin.getComponent().equals(this.mDPM.getDeviceOwnerComponentOnCallingUser())) {
                if (equals) {
                    this.mAdminWarning.setText(R.string.admin_profile_owner_user_message);
                } else if (isFinancedDevice()) {
                    this.mAdminWarning.setText(R.string.admin_financed_message);
                } else {
                    this.mAdminWarning.setText(R.string.admin_device_owner_message);
                }
                this.mActionButton.setText(R.string.remove_device_admin);
                this.mActionButton.setEnabled(false);
            } else {
                addDeviceAdminPolicies(false);
                this.mAdminWarning.setText(getString(R.string.device_admin_status, new Object[]{this.mDeviceAdmin.getActivityInfo().applicationInfo.loadLabel(getPackageManager())}));
                setTitle(R.string.active_device_admin_msg);
                if (this.mUninstalling) {
                    this.mActionButton.setText(R.string.remove_and_uninstall_device_admin);
                } else {
                    this.mActionButton.setText(R.string.remove_device_admin);
                }
            }
            CharSequence longSupportMessageForUser = this.mDPM.getLongSupportMessageForUser(this.mDeviceAdmin.getComponent(), UserHandle.myUserId());
            if (!TextUtils.isEmpty(longSupportMessageForUser)) {
                this.mSupportMessage.setText(longSupportMessageForUser);
                this.mSupportMessage.setVisibility(0);
                return;
            }
            this.mSupportMessage.setVisibility(8);
            return;
        }
        addDeviceAdminPolicies(true);
        this.mAdminWarning.setText(getString(R.string.device_admin_warning, new Object[]{this.mDeviceAdmin.getActivityInfo().applicationInfo.loadLabel(getPackageManager())}));
        setTitle(getText(R.string.add_device_admin_msg));
        this.mActionButton.setText(getText(R.string.add_device_admin));
        if (isAdminUninstallable()) {
            this.mUninstallButton.setVisibility(0);
        }
        this.mSupportMessage.setVisibility(8);
        this.mAdding = true;
    }

    private RestrictedLockUtils.EnforcedAdmin getAdminEnforcingCantRemoveProfile() {
        return RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this, "no_remove_managed_profile", getParentUserId());
    }

    private boolean hasBaseCantRemoveProfileRestriction() {
        return RestrictedLockUtilsInternal.hasBaseUserRestriction(this, "no_remove_managed_profile", getParentUserId());
    }

    private int getParentUserId() {
        return UserManager.get(this).getProfileParent(UserHandle.myUserId()).id;
    }

    private void addDeviceAdminPolicies(boolean z) {
        if (!this.mAdminPoliciesInitialized) {
            boolean isAdminUser = UserManager.get(this).isAdminUser();
            Iterator it = this.mDeviceAdmin.getUsedPolicies().iterator();
            while (it.hasNext()) {
                DeviceAdminInfo.PolicyInfo policyInfo = (DeviceAdminInfo.PolicyInfo) it.next();
                this.mAdminPolicies.addView(getPermissionItemView(getText(isAdminUser ? policyInfo.label : policyInfo.labelForSecondaryUsers), z ? getText(isAdminUser ? policyInfo.description : policyInfo.descriptionForSecondaryUsers) : ""));
            }
            this.mAdminPoliciesInitialized = true;
        }
    }

    private View getPermissionItemView(CharSequence charSequence, CharSequence charSequence2) {
        Drawable drawable = getDrawable(17302871);
        View inflate = this.mLayoutInflaternflater.inflate(R.layout.app_permission_item, (ViewGroup) null);
        TextView textView = (TextView) inflate.findViewById(R.id.permission_group);
        TextView textView2 = (TextView) inflate.findViewById(R.id.permission_list);
        ((ImageView) inflate.findViewById(R.id.perm_icon)).setImageDrawable(drawable);
        if (charSequence != null) {
            textView.setText(charSequence);
            textView2.setText(charSequence2);
        } else {
            textView.setText(charSequence2);
            textView2.setVisibility(8);
        }
        return inflate;
    }

    void toggleMessageEllipsis(View view) {
        TextView textView = (TextView) view;
        boolean z = !this.mAddMsgEllipsized;
        this.mAddMsgEllipsized = z;
        textView.setEllipsize(z ? TextUtils.TruncateAt.END : null);
        textView.setMaxLines(this.mAddMsgEllipsized ? getEllipsizedLines() : 15);
        this.mAddMsgExpander.setImageResource(this.mAddMsgEllipsized ? 17302238 : 17302237);
    }

    int getEllipsizedLines() {
        Display defaultDisplay = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        return defaultDisplay.getHeight() > defaultDisplay.getWidth() ? 5 : 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isManagedProfile(DeviceAdminInfo deviceAdminInfo) {
        UserInfo userInfo = UserManager.get(this).getUserInfo(UserHandle.getUserId(deviceAdminInfo.getActivityInfo().applicationInfo.uid));
        if (userInfo != null) {
            return userInfo.isManagedProfile();
        }
        return false;
    }

    private boolean isFinancedDevice() {
        if (this.mDPM.isDeviceManaged()) {
            DevicePolicyManager devicePolicyManager = this.mDPM;
            if (devicePolicyManager.getDeviceOwnerType(devicePolicyManager.getDeviceOwnerComponentOnAnyUser()) == 1) {
                return true;
            }
        }
        return false;
    }

    private Optional<ComponentName> findAdminWithPackageName(final String str) {
        List<ComponentName> activeAdmins = this.mDPM.getActiveAdmins();
        if (activeAdmins == null) {
            return Optional.empty();
        }
        return activeAdmins.stream().filter(new Predicate() { // from class: com.android.settings.applications.specialaccess.deviceadmin.DeviceAdminAdd$$ExternalSyntheticLambda0
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$findAdminWithPackageName$0;
                lambda$findAdminWithPackageName$0 = DeviceAdminAdd.lambda$findAdminWithPackageName$0(str, (ComponentName) obj);
                return lambda$findAdminWithPackageName$0;
            }
        }).findAny();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$findAdminWithPackageName$0(String str, ComponentName componentName) {
        return componentName.getPackageName().equals(str);
    }

    private boolean isAdminUninstallable() {
        return !this.mDeviceAdmin.getActivityInfo().applicationInfo.isSystemApp();
    }
}
