package com.android.settings;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.sysprop.VoldProperties;
import android.telephony.euicc.EuiccManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.android.settings.Settings;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.enterprise.ActionDisabledByAdminDialogHelper;
import com.android.settings.password.ChooseLockSettingsHelper;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.settingslib.development.DevelopmentSettingsEnabler;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;

public class MainClear extends InstrumentedFragment implements ViewTreeObserver.OnGlobalLayoutListener {
    static final int CREDENTIAL_CONFIRM_REQUEST = 56;
    static final int KEYGUARD_REQUEST = 55;
    private View mContentView;
    CheckBox mEsimStorage;
    View mEsimStorageContainer;
    CheckBox mExternalStorage;
    private View mExternalStorageContainer;
    FooterButton mInitiateButton;
    protected final View.OnClickListener mInitiateListener = new View.OnClickListener() {
        public void onClick(View view) {
            Context context = view.getContext();
            if (Utils.isDemoUser(context)) {
                ComponentName deviceOwnerComponent = Utils.getDeviceOwnerComponent(context);
                if (deviceOwnerComponent != null) {
                    context.startActivity(new Intent().setPackage(deviceOwnerComponent.getPackageName()).setAction("android.intent.action.FACTORY_RESET"));
                }
            } else if (!MainClear.this.runKeyguardConfirmation(55)) {
                Intent accountConfirmationIntent = MainClear.this.getAccountConfirmationIntent();
                if (accountConfirmationIntent != null) {
                    MainClear.this.showAccountCredentialConfirmation(accountConfirmationIntent);
                } else {
                    MainClear.this.showFinalConfirmation();
                }
            }
        }
    };
    ScrollView mScrollView;

    public int getMetricsCategory() {
        return 66;
    }

    /* access modifiers changed from: package-private */
    public boolean isValidRequestCode(int i) {
        return i == 55 || i == 56;
    }

    public void onGlobalLayout() {
        this.mInitiateButton.setEnabled(hasReachedBottom(this.mScrollView));
    }

    private void setUpActionBarAndTitle() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Log.e("MainClear", "No activity attached, skipping setUpActionBarAndTitle");
            return;
        }
        ActionBar actionBar = activity.getActionBar();
        if (actionBar == null) {
            Log.e("MainClear", "No actionbar, skipping setUpActionBarAndTitle");
            return;
        }
        actionBar.hide();
        activity.getWindow().setStatusBarColor(0);
    }

    /* access modifiers changed from: private */
    public boolean runKeyguardConfirmation(int i) {
        return new ChooseLockSettingsHelper.Builder(getActivity(), this).setRequestCode(i).setTitle(getActivity().getResources().getText(R$string.main_clear_short_title)).show();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        onActivityResultInternal(i, i2, intent);
    }

    /* access modifiers changed from: package-private */
    public void onActivityResultInternal(int i, int i2, Intent intent) {
        Intent accountConfirmationIntent;
        if (isValidRequestCode(i)) {
            if (i2 != -1) {
                establishInitialState();
            } else if (56 == i || (accountConfirmationIntent = getAccountConfirmationIntent()) == null) {
                showFinalConfirmation();
            } else {
                showAccountCredentialConfirmation(accountConfirmationIntent);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void showFinalConfirmation() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("erase_sd", this.mExternalStorage.isChecked());
        bundle.putBoolean("erase_esim", this.mEsimStorage.isChecked());
        Intent intent = new Intent();
        intent.setClass(getContext(), Settings.FactoryResetConfirmActivity.class);
        intent.putExtra(":settings:show_fragment", MainClearConfirm.class.getName());
        intent.putExtra(":settings:show_fragment_args", bundle);
        intent.putExtra(":settings:show_fragment_title_resid", R$string.main_clear_confirm_title);
        intent.putExtra(":settings:source_metrics", getMetricsCategory());
        getContext().startActivity(intent);
    }

    /* access modifiers changed from: package-private */
    public void showAccountCredentialConfirmation(Intent intent) {
        startActivityForResult(intent, 56);
    }

    /* access modifiers changed from: package-private */
    public Intent getAccountConfirmationIntent() {
        ActivityInfo activityInfo;
        FragmentActivity activity = getActivity();
        String string = activity.getString(R$string.account_type);
        String string2 = activity.getString(R$string.account_confirmation_package);
        String string3 = activity.getString(R$string.account_confirmation_class);
        if (TextUtils.isEmpty(string) || TextUtils.isEmpty(string2) || TextUtils.isEmpty(string3)) {
            Log.i("MainClear", "Resources not set for account confirmation.");
            return null;
        }
        Account[] accountsByType = AccountManager.get(activity).getAccountsByType(string);
        if (accountsByType == null || accountsByType.length <= 0) {
            Log.d("MainClear", "No " + string + " accounts installed!");
        } else {
            Intent component = new Intent().setPackage(string2).setComponent(new ComponentName(string2, string3));
            ResolveInfo resolveActivity = activity.getPackageManager().resolveActivity(component, 0);
            if (resolveActivity != null && (activityInfo = resolveActivity.activityInfo) != null && string2.equals(activityInfo.packageName)) {
                return component;
            }
            Log.i("MainClear", "Unable to resolve Activity: " + string2 + "/" + string3);
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public void establishInitialState() {
        setUpActionBarAndTitle();
        setUpInitiateButton();
        this.mExternalStorageContainer = this.mContentView.findViewById(R$id.erase_external_container);
        this.mExternalStorage = (CheckBox) this.mContentView.findViewById(R$id.erase_external);
        this.mEsimStorageContainer = this.mContentView.findViewById(R$id.erase_esim_container);
        this.mEsimStorage = (CheckBox) this.mContentView.findViewById(R$id.erase_esim);
        ScrollView scrollView = this.mScrollView;
        if (scrollView != null) {
            scrollView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        }
        this.mScrollView = (ScrollView) this.mContentView.findViewById(R$id.main_clear_scrollview);
        boolean isExternalStorageEmulated = Environment.isExternalStorageEmulated();
        if (isExternalStorageEmulated || (!Environment.isExternalStorageRemovable() && isExtStorageEncrypted())) {
            this.mExternalStorageContainer.setVisibility(8);
            this.mContentView.findViewById(R$id.erase_external_option_text).setVisibility(8);
            this.mContentView.findViewById(R$id.also_erases_external).setVisibility(0);
            this.mExternalStorage.setChecked(!isExternalStorageEmulated);
        } else {
            this.mExternalStorageContainer.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    MainClear.this.mExternalStorage.toggle();
                }
            });
        }
        if (!showWipeEuicc()) {
            this.mEsimStorage.setChecked(false);
        } else if (showWipeEuiccCheckbox()) {
            this.mEsimStorageContainer.setVisibility(0);
            this.mEsimStorageContainer.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    MainClear.this.mEsimStorage.toggle();
                }
            });
        } else {
            this.mContentView.findViewById(R$id.also_erases_esim).setVisibility(0);
            this.mContentView.findViewById(R$id.no_cancel_mobile_plan).setVisibility(0);
            this.mEsimStorage.setChecked(true);
        }
        loadAccountList((UserManager) getActivity().getSystemService("user"));
        StringBuffer stringBuffer = new StringBuffer();
        View findViewById = this.mContentView.findViewById(R$id.main_clear_container);
        getContentDescription(findViewById, stringBuffer);
        findViewById.setContentDescription(stringBuffer);
        this.mScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            public void onScrollChange(View view, int i, int i2, int i3, int i4) {
                if ((view instanceof ScrollView) && MainClear.this.hasReachedBottom((ScrollView) view)) {
                    MainClear.this.mInitiateButton.setEnabled(true);
                    MainClear.this.mScrollView.setOnScrollChangeListener((View.OnScrollChangeListener) null);
                }
            }
        });
        this.mScrollView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /* access modifiers changed from: package-private */
    public boolean showWipeEuicc() {
        Context context = getContext();
        if (!isEuiccEnabled(context)) {
            return false;
        }
        if (Settings.Global.getInt(context.getContentResolver(), "euicc_provisioned", 0) != 0 || DevelopmentSettingsEnabler.isDevelopmentSettingsEnabled(context)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean showWipeEuiccCheckbox() {
        return SystemProperties.getBoolean("masterclear.allow_retain_esim_profiles_after_fdr", false);
    }

    /* access modifiers changed from: protected */
    public boolean isEuiccEnabled(Context context) {
        return ((EuiccManager) context.getSystemService("euicc")).isEnabled();
    }

    /* access modifiers changed from: package-private */
    public boolean hasReachedBottom(ScrollView scrollView) {
        if (scrollView.getChildCount() >= 1 && scrollView.getChildAt(0).getBottom() - (scrollView.getHeight() + scrollView.getScrollY()) > 0) {
            return false;
        }
        return true;
    }

    private void setUpInitiateButton() {
        if (this.mInitiateButton == null) {
            FooterBarMixin footerBarMixin = (FooterBarMixin) ((GlifLayout) this.mContentView.findViewById(R$id.setup_wizard_layout)).getMixin(FooterBarMixin.class);
            footerBarMixin.setPrimaryButton(new FooterButton.Builder(getActivity()).setText(R$string.main_clear_button_text).setListener(this.mInitiateListener).setButtonType(0).setTheme(R$style.SudGlifButton_Primary).build());
            this.mInitiateButton = footerBarMixin.getPrimaryButton();
        }
    }

    private void getContentDescription(View view, StringBuffer stringBuffer) {
        if (view.getVisibility() == 0) {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    getContentDescription(viewGroup.getChildAt(i), stringBuffer);
                }
            } else if (view instanceof TextView) {
                stringBuffer.append(((TextView) view).getText());
                stringBuffer.append(",");
            }
        }
    }

    private boolean isExtStorageEncrypted() {
        return !"".equals((String) VoldProperties.decrypt().orElse(""));
    }

    /* JADX WARNING: Removed duplicated region for block: B:51:0x0169  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void loadAccountList(android.os.UserManager r23) {
        /*
            r22 = this;
            r1 = r22
            android.view.View r0 = r1.mContentView
            int r2 = com.android.settings.R$id.accounts_label
            android.view.View r2 = r0.findViewById(r2)
            android.view.View r0 = r1.mContentView
            int r3 = com.android.settings.R$id.accounts
            android.view.View r0 = r0.findViewById(r3)
            r3 = r0
            android.widget.LinearLayout r3 = (android.widget.LinearLayout) r3
            r3.removeAllViews()
            androidx.fragment.app.FragmentActivity r4 = r22.getActivity()
            int r0 = android.os.UserHandle.myUserId()
            r5 = r23
            java.util.List r6 = r5.getProfiles(r0)
            int r7 = r6.size()
            android.accounts.AccountManager r8 = android.accounts.AccountManager.get(r4)
            java.lang.String r0 = "layout_inflater"
            java.lang.Object r0 = r4.getSystemService(r0)
            r9 = r0
            android.view.LayoutInflater r9 = (android.view.LayoutInflater) r9
            r0 = 0
            r11 = 0
        L_0x0039:
            if (r11 >= r7) goto L_0x01ab
            java.lang.Object r13 = r6.get(r11)
            android.content.pm.UserInfo r13 = (android.content.pm.UserInfo) r13
            int r14 = r13.id
            android.os.UserHandle r15 = new android.os.UserHandle
            r15.<init>(r14)
            android.accounts.Account[] r10 = r8.getAccountsAsUser(r14)
            int r12 = r10.length
            if (r12 != 0) goto L_0x0057
            r17 = r6
            r19 = r7
            r18 = r8
            goto L_0x019f
        L_0x0057:
            int r16 = r0 + r12
            android.accounts.AccountManager r0 = android.accounts.AccountManager.get(r4)
            android.accounts.AuthenticatorDescription[] r14 = r0.getAuthenticatorTypesAsUser(r14)
            int r5 = r14.length
            r17 = r6
            r0 = 1
            if (r7 <= r0) goto L_0x00bb
            android.view.View r0 = com.android.settings.Utils.inflateCategoryHeader(r9, r3)
            int r6 = r0.getPaddingTop()
            r18 = r8
            int r8 = r0.getPaddingBottom()
            r19 = r7
            r7 = 0
            r0.setPadding(r7, r6, r7, r8)
            r6 = 16908310(0x1020016, float:2.387729E-38)
            android.view.View r7 = r0.findViewById(r6)
            android.widget.TextView r7 = (android.widget.TextView) r7
            java.lang.Class<android.app.admin.DevicePolicyManager> r6 = android.app.admin.DevicePolicyManager.class
            java.lang.Object r6 = r4.getSystemService(r6)
            android.app.admin.DevicePolicyManager r6 = (android.app.admin.DevicePolicyManager) r6
            boolean r8 = r13.isManagedProfile()
            if (r8 == 0) goto L_0x00a5
            android.app.admin.DevicePolicyResourcesManager r6 = r6.getResources()
            com.android.settings.MainClear$$ExternalSyntheticLambda1 r8 = new com.android.settings.MainClear$$ExternalSyntheticLambda1
            r8.<init>(r1)
            java.lang.String r13 = "Settings.WORK_CATEGORY_HEADER"
            java.lang.String r6 = r6.getString(r13, r8)
            r7.setText(r6)
            goto L_0x00b7
        L_0x00a5:
            android.app.admin.DevicePolicyResourcesManager r6 = r6.getResources()
            com.android.settings.MainClear$$ExternalSyntheticLambda2 r8 = new com.android.settings.MainClear$$ExternalSyntheticLambda2
            r8.<init>(r1)
            java.lang.String r13 = "Settings.PERSONAL_CATEGORY_HEADER"
            java.lang.String r6 = r6.getString(r13, r8)
            r7.setText(r6)
        L_0x00b7:
            r3.addView(r0)
            goto L_0x00bf
        L_0x00bb:
            r19 = r7
            r18 = r8
        L_0x00bf:
            r7 = 0
        L_0x00c0:
            if (r7 >= r12) goto L_0x019d
            r6 = r10[r7]
            r0 = 0
        L_0x00c5:
            if (r0 >= r5) goto L_0x00da
            java.lang.String r13 = r6.type
            r8 = r14[r0]
            java.lang.String r8 = r8.type
            boolean r8 = r13.equals(r8)
            if (r8 == 0) goto L_0x00d7
            r0 = r14[r0]
            r8 = r0
            goto L_0x00db
        L_0x00d7:
            int r0 = r0 + 1
            goto L_0x00c5
        L_0x00da:
            r8 = 0
        L_0x00db:
            java.lang.String r13 = "MainClear"
            if (r8 != 0) goto L_0x0108
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r8 = "No descriptor for account name="
            r0.append(r8)
            java.lang.String r8 = r6.name
            r0.append(r8)
            java.lang.String r8 = " type="
            r0.append(r8)
            java.lang.String r6 = r6.type
            r0.append(r6)
            java.lang.String r0 = r0.toString()
            android.util.Log.w(r13, r0)
            r20 = r5
            r21 = r10
            r5 = 16908310(0x1020016, float:2.387729E-38)
            goto L_0x0195
        L_0x0108:
            int r0 = r8.iconId     // Catch:{ NameNotFoundException -> 0x014c, NotFoundException -> 0x0130 }
            if (r0 == 0) goto L_0x012b
            java.lang.String r0 = r8.packageName     // Catch:{ NameNotFoundException -> 0x014c, NotFoundException -> 0x0130 }
            r20 = r5
            r5 = 0
            android.content.Context r0 = r4.createPackageContextAsUser(r0, r5, r15)     // Catch:{ NameNotFoundException -> 0x014e, NotFoundException -> 0x0129 }
            android.content.pm.PackageManager r5 = r4.getPackageManager()     // Catch:{ NameNotFoundException -> 0x014e, NotFoundException -> 0x0129 }
            r21 = r10
            int r10 = r8.iconId     // Catch:{ NameNotFoundException -> 0x0150, NotFoundException -> 0x0127 }
            android.graphics.drawable.Drawable r0 = r0.getDrawable(r10)     // Catch:{ NameNotFoundException -> 0x0150, NotFoundException -> 0x0127 }
            android.graphics.drawable.Drawable r0 = r5.getUserBadgedIcon(r0, r15)     // Catch:{ NameNotFoundException -> 0x0150, NotFoundException -> 0x0127 }
            r8 = r0
            goto L_0x0167
        L_0x0127:
            r0 = move-exception
            goto L_0x0135
        L_0x0129:
            r0 = move-exception
            goto L_0x0133
        L_0x012b:
            r20 = r5
            r21 = r10
            goto L_0x0166
        L_0x0130:
            r0 = move-exception
            r20 = r5
        L_0x0133:
            r21 = r10
        L_0x0135:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            r5.<init>()
            java.lang.String r10 = "Invalid icon id for account type "
            r5.append(r10)
            java.lang.String r8 = r8.type
            r5.append(r8)
            java.lang.String r5 = r5.toString()
            android.util.Log.w(r13, r5, r0)
            goto L_0x0166
        L_0x014c:
            r20 = r5
        L_0x014e:
            r21 = r10
        L_0x0150:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r5 = "Bad package name for account type "
            r0.append(r5)
            java.lang.String r5 = r8.type
            r0.append(r5)
            java.lang.String r0 = r0.toString()
            android.util.Log.w(r13, r0)
        L_0x0166:
            r8 = 0
        L_0x0167:
            if (r8 != 0) goto L_0x0171
            android.content.pm.PackageManager r0 = r4.getPackageManager()
            android.graphics.drawable.Drawable r8 = r0.getDefaultActivityIcon()
        L_0x0171:
            int r0 = com.android.settings.R$layout.main_clear_account
            r5 = 0
            android.view.View r0 = r9.inflate(r0, r3, r5)
            r5 = 16908294(0x1020006, float:2.3877246E-38)
            android.view.View r5 = r0.findViewById(r5)
            android.widget.ImageView r5 = (android.widget.ImageView) r5
            r5.setImageDrawable(r8)
            r5 = 16908310(0x1020016, float:2.387729E-38)
            android.view.View r8 = r0.findViewById(r5)
            android.widget.TextView r8 = (android.widget.TextView) r8
            java.lang.String r6 = r6.name
            r8.setText(r6)
            r3.addView(r0)
        L_0x0195:
            int r7 = r7 + 1
            r5 = r20
            r10 = r21
            goto L_0x00c0
        L_0x019d:
            r0 = r16
        L_0x019f:
            int r11 = r11 + 1
            r5 = r23
            r6 = r17
            r8 = r18
            r7 = r19
            goto L_0x0039
        L_0x01ab:
            r19 = r7
            r5 = 1
            r4 = 0
            if (r0 <= 0) goto L_0x01b7
            r2.setVisibility(r4)
            r3.setVisibility(r4)
        L_0x01b7:
            android.view.View r0 = r1.mContentView
            int r1 = com.android.settings.R$id.other_users_present
            android.view.View r0 = r0.findViewById(r1)
            int r1 = r23.getUserCount()
            int r1 = r1 - r19
            if (r1 <= 0) goto L_0x01c9
            r12 = r5
            goto L_0x01ca
        L_0x01c9:
            r12 = r4
        L_0x01ca:
            if (r12 == 0) goto L_0x01ce
            r10 = r4
            goto L_0x01d0
        L_0x01ce:
            r10 = 8
        L_0x01d0:
            r0.setVisibility(r10)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settings.MainClear.loadAccountList(android.os.UserManager):void");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$loadAccountList$0() {
        return getString(R$string.category_work);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ String lambda$loadAccountList$1() {
        return getString(R$string.category_personal);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Context context = getContext();
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(context, "no_factory_reset", UserHandle.myUserId());
        if ((!UserManager.get(context).isAdminUser() || RestrictedLockUtilsInternal.hasBaseUserRestriction(context, "no_factory_reset", UserHandle.myUserId())) && !Utils.isDemoUser(context)) {
            return layoutInflater.inflate(R$layout.main_clear_disallowed_screen, (ViewGroup) null);
        }
        if (checkIfRestrictionEnforced != null) {
            new ActionDisabledByAdminDialogHelper(getActivity()).prepareDialogBuilder("no_factory_reset", checkIfRestrictionEnforced).setOnDismissListener(new MainClear$$ExternalSyntheticLambda0(this)).show();
            return new View(getContext());
        }
        this.mContentView = layoutInflater.inflate(R$layout.main_clear, (ViewGroup) null);
        establishInitialState();
        return this.mContentView;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreateView$2(DialogInterface dialogInterface) {
        getActivity().finish();
    }
}
