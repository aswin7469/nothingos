package com.android.settings.password;

import android.app.admin.DevicePolicyManager;
import android.app.admin.PasswordMetrics;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.UserManager;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImeAwareEditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.widget.LockPatternUtils;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.PasswordValidationError;
import com.android.internal.widget.TextViewInputDisabler;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$plurals;
import com.android.settings.R$string;
import com.android.settings.R$style;
import com.android.settings.SettingsActivity;
import com.android.settings.SetupWizardUtils;
import com.android.settings.Utils;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.notification.RedactionInterstitial;
import com.android.settings.password.ChooseLockSettingsHelper;
import com.android.settings.password.SaveChosenLockWorkerBase;
import com.android.settingslib.utils.StringUtil;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.util.ThemeHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChooseLockPassword extends SettingsActivity {
    /* access modifiers changed from: protected */
    public boolean isToolbarEnabled() {
        return false;
    }

    public Intent getIntent() {
        Intent intent = new Intent(super.getIntent());
        intent.putExtra(":settings:show_fragment", getFragmentClass().getName());
        return intent;
    }

    public static class IntentBuilder {
        private final Intent mIntent;

        public IntentBuilder(Context context) {
            Intent intent = new Intent(context, ChooseLockPassword.class);
            this.mIntent = intent;
            intent.putExtra("confirm_credentials", false);
            intent.putExtra("extra_require_password", false);
        }

        public IntentBuilder setPasswordType(int i) {
            this.mIntent.putExtra("lockscreen.password_type", i);
            return this;
        }

        public IntentBuilder setUserId(int i) {
            this.mIntent.putExtra("android.intent.extra.USER_ID", i);
            return this;
        }

        public IntentBuilder setRequestGatekeeperPasswordHandle(boolean z) {
            this.mIntent.putExtra("request_gk_pw_handle", z);
            return this;
        }

        public IntentBuilder setPassword(LockscreenCredential lockscreenCredential) {
            this.mIntent.putExtra("password", lockscreenCredential);
            return this;
        }

        public IntentBuilder setForFingerprint(boolean z) {
            this.mIntent.putExtra("for_fingerprint", z);
            return this;
        }

        public IntentBuilder setForFace(boolean z) {
            this.mIntent.putExtra("for_face", z);
            return this;
        }

        public IntentBuilder setForBiometrics(boolean z) {
            this.mIntent.putExtra("for_biometrics", z);
            return this;
        }

        public IntentBuilder setPasswordRequirement(int i, PasswordMetrics passwordMetrics) {
            this.mIntent.putExtra("min_complexity", i);
            this.mIntent.putExtra("min_metrics", passwordMetrics);
            return this;
        }

        public IntentBuilder setProfileToUnify(int i, LockscreenCredential lockscreenCredential) {
            this.mIntent.putExtra("unification_profile_id", i);
            this.mIntent.putExtra("unification_profile_credential", lockscreenCredential);
            return this;
        }

        public Intent build() {
            return this.mIntent;
        }
    }

    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return ChooseLockPasswordFragment.class.getName().equals(str);
    }

    /* access modifiers changed from: package-private */
    public Class<? extends Fragment> getFragmentClass() {
        return ChooseLockPasswordFragment.class;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        ThemeHelper.trySetDynamicColor(this);
        super.onCreate(bundle);
        findViewById(R$id.content_parent).setFitsSystemWindows(false);
        getWindow().addFlags(8192);
    }

    public static class ChooseLockPasswordFragment extends InstrumentedFragment implements TextView.OnEditorActionListener, TextWatcher, SaveChosenLockWorkerBase.Listener {
        private LockscreenCredential mChosenPassword;
        private LockscreenCredential mCurrentCredential;
        private LockscreenCredential mFirstPassword;
        protected boolean mForBiometrics;
        protected boolean mForFace;
        protected boolean mForFingerprint;
        protected boolean mIsAlphaMode;
        protected boolean mIsManagedProfile;
        private GlifLayout mLayout;
        private LockPatternUtils mLockPatternUtils;
        private TextView mMessage;
        private int mMinComplexity = 0;
        private PasswordMetrics mMinMetrics;
        private FooterButton mNextButton;
        private TextView mNothingTitle;
        private ImeAwareEditText mPasswordEntry;
        private TextViewInputDisabler mPasswordEntryInputDisabler;
        private byte[] mPasswordHistoryHashFactor;
        private PasswordRequirementAdapter mPasswordRequirementAdapter;
        private RecyclerView mPasswordRestrictionView;
        private int mPasswordType = 131072;
        private boolean mRequestGatekeeperPassword;
        private SaveAndFinishWorker mSaveAndFinishWorker;
        protected FooterButton mSkipOrClearButton;
        private TextChangedHandler mTextChangedHandler;
        protected Stage mUiStage = Stage.Introduction;
        private int mUnificationProfileId = -10000;
        protected int mUserId;
        private List<PasswordValidationError> mValidationErrors;

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public int getMetricsCategory() {
            return 28;
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        /* access modifiers changed from: protected */
        public int toVisibility(boolean z) {
            return z ? 0 : 8;
        }

        protected enum Stage {
            Introduction(r3, "Settings.SET_WORK_PROFILE_PASSWORD_HEADER", r5, r6, r7, r8, r9, "Settings.SET_WORK_PROFILE_PIN_HEADER", r11, r12, r13, r14, r16, r16, R$string.next_label),
            NeedToConfirm(r25, "Settings.REENTER_WORK_PROFILE_PASSWORD_HEADER", r24, r25, r25, r25, r33, "Settings.REENTER_WORK_PROFILE_PIN_HEADER", r30, r33, r33, r33, 0, 0, r36),
            ConfirmWrong(r40, "UNDEFINED", r42, r43, r44, r43, r46, "UNDEFINED", r48, r49, r50, r50, 0, 0, r36);
            
            public final int alphaHint;
            public final int alphaHintForBiometrics;
            public final int alphaHintForFace;
            public final int alphaHintForFingerprint;
            public final int alphaHintForProfile;
            public final String alphaHintOverrideForProfile;
            public final int alphaMessageForBiometrics;
            public final int buttonText;
            public final int numericHint;
            public final int numericHintForBiometrics;
            public final int numericHintForFace;
            public final int numericHintForFingerprint;
            public final int numericHintForProfile;
            public final String numericHintOverrideForProfile;
            public final int numericMessageForBiometrics;

            private Stage(int i, String str, int i2, int i3, int i4, int i5, int i6, String str2, int i7, int i8, int i9, int i10, int i11, int i12, int i13) {
                this.alphaHint = i;
                this.alphaHintOverrideForProfile = str;
                this.alphaHintForProfile = i2;
                this.alphaHintForFingerprint = i3;
                this.alphaHintForFace = i4;
                this.alphaHintForBiometrics = i5;
                this.numericHint = i6;
                this.numericHintOverrideForProfile = str2;
                this.numericHintForProfile = i7;
                this.numericHintForFingerprint = i8;
                this.numericHintForFace = i9;
                this.numericHintForBiometrics = i10;
                this.alphaMessageForBiometrics = i11;
                this.numericMessageForBiometrics = i12;
                this.buttonText = i13;
            }

            public String getHint(Context context, boolean z, int i, boolean z2) {
                if (z) {
                    if (i == 1) {
                        return context.getString(this.alphaHintForFingerprint);
                    }
                    if (i == 2) {
                        return context.getString(this.alphaHintForFace);
                    }
                    if (i == 3) {
                        return context.getString(this.alphaHintForBiometrics);
                    }
                    if (z2) {
                        return ((DevicePolicyManager) context.getSystemService(DevicePolicyManager.class)).getResources().getString(this.alphaHintOverrideForProfile, new C1269x36b0f3fd(this, context));
                    }
                    return context.getString(this.alphaHint);
                } else if (i == 1) {
                    return context.getString(this.numericHintForFingerprint);
                } else {
                    if (i == 2) {
                        return context.getString(this.numericHintForFace);
                    }
                    if (i == 3) {
                        return context.getString(this.numericHintForBiometrics);
                    }
                    if (z2) {
                        return ((DevicePolicyManager) context.getSystemService(DevicePolicyManager.class)).getResources().getString(this.numericHintOverrideForProfile, new C1270x36b0f3fe(this, context));
                    }
                    return context.getString(this.numericHint);
                }
            }

            public int getMessage(boolean z, int i) {
                if (i == 1 || i == 2 || i == 3) {
                    return z ? this.alphaMessageForBiometrics : this.numericMessageForBiometrics;
                }
                return 0;
            }
        }

        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            this.mLockPatternUtils = new LockPatternUtils(getActivity());
            Intent intent = getActivity().getIntent();
            if (getActivity() instanceof ChooseLockPassword) {
                this.mUserId = Utils.getUserIdFromBundle(getActivity(), intent.getExtras());
                this.mIsManagedProfile = UserManager.get(getActivity()).isManagedProfile(this.mUserId);
                this.mForFingerprint = intent.getBooleanExtra("for_fingerprint", false);
                this.mForFace = intent.getBooleanExtra("for_face", false);
                this.mForBiometrics = intent.getBooleanExtra("for_biometrics", false);
                this.mPasswordType = intent.getIntExtra("lockscreen.password_type", 131072);
                this.mUnificationProfileId = intent.getIntExtra("unification_profile_id", -10000);
                this.mMinComplexity = intent.getIntExtra("min_complexity", 0);
                PasswordMetrics parcelableExtra = intent.getParcelableExtra("min_metrics");
                this.mMinMetrics = parcelableExtra;
                if (parcelableExtra == null) {
                    this.mMinMetrics = new PasswordMetrics(-1);
                }
                if (intent.getBooleanExtra("for_cred_req_boot", false)) {
                    SaveAndFinishWorker saveAndFinishWorker = new SaveAndFinishWorker();
                    boolean booleanExtra = getActivity().getIntent().getBooleanExtra("extra_require_password", true);
                    LockscreenCredential parcelableExtra2 = intent.getParcelableExtra("password");
                    LockPatternUtils lockPatternUtils = new LockPatternUtils(getActivity());
                    saveAndFinishWorker.setBlocking(true);
                    saveAndFinishWorker.setListener(this);
                    saveAndFinishWorker.start(lockPatternUtils, booleanExtra, false, parcelableExtra2, parcelableExtra2, this.mUserId);
                }
                this.mTextChangedHandler = new TextChangedHandler();
                return;
            }
            throw new SecurityException("Fragment contained in wrong activity");
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(R$layout.choose_lock_password, viewGroup, false);
        }

        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            GlifLayout glifLayout = (GlifLayout) view;
            this.mLayout = glifLayout;
            glifLayout.findViewById(R$id.sud_layout_header).setVisibility(8);
            TextView textView = (TextView) view.findViewById(R$id.title);
            this.mNothingTitle = textView;
            textView.setTypeface(Typeface.create("NDot57", 0));
            ((ViewGroup) view.findViewById(R$id.password_container)).setOpticalInsets(Insets.NONE);
            FooterBarMixin footerBarMixin = (FooterBarMixin) this.mLayout.getMixin(FooterBarMixin.class);
            footerBarMixin.setSecondaryButton(new FooterButton.Builder(getActivity()).setText(R$string.lockpassword_clear_label).setListener(new C1265xcfed0643(this)).setButtonType(7).setTheme(R$style.SudGlifButton_Secondary).build());
            footerBarMixin.setPrimaryButton(new FooterButton.Builder(getActivity()).setText(R$string.next_label).setListener(new C1266xcfed0644(this)).setButtonType(5).setTheme(R$style.SudGlifButton_Primary).build());
            this.mSkipOrClearButton = footerBarMixin.getSecondaryButton();
            this.mNextButton = footerBarMixin.getPrimaryButton();
            TextView textView2 = (TextView) view.findViewById(R$id.sud_layout_description);
            this.mMessage = textView2;
            textView2.setGravity(8388611);
            this.mLayout.setIcon(getActivity().getDrawable(R$drawable.ic_lock));
            int i = this.mPasswordType;
            this.mIsAlphaMode = 262144 == i || 327680 == i || 393216 == i;
            setupPasswordRequirementsView(view);
            this.mPasswordRestrictionView.setLayoutManager(new LinearLayoutManager(getActivity()));
            ImeAwareEditText findViewById = view.findViewById(R$id.password_entry);
            this.mPasswordEntry = findViewById;
            findViewById.setOnEditorActionListener(this);
            this.mPasswordEntry.addTextChangedListener(this);
            this.mPasswordEntry.requestFocus();
            this.mPasswordEntryInputDisabler = new TextViewInputDisabler(this.mPasswordEntry);
            FragmentActivity activity = getActivity();
            int inputType = this.mPasswordEntry.getInputType();
            ImeAwareEditText imeAwareEditText = this.mPasswordEntry;
            if (!this.mIsAlphaMode) {
                inputType = 18;
            }
            imeAwareEditText.setInputType(inputType);
            if (this.mIsAlphaMode) {
                this.mPasswordEntry.setContentDescription(getString(R$string.unlock_set_unlock_password_title));
            } else {
                this.mPasswordEntry.setContentDescription(getString(R$string.unlock_set_unlock_pin_title));
            }
            this.mPasswordEntry.setTypeface(Typeface.create(getContext().getString(17039983), 0));
            Intent intent = getActivity().getIntent();
            boolean booleanExtra = intent.getBooleanExtra("confirm_credentials", true);
            this.mCurrentCredential = intent.getParcelableExtra("password");
            this.mRequestGatekeeperPassword = intent.getBooleanExtra("request_gk_pw_handle", false);
            if (bundle == null) {
                updateStage(Stage.Introduction);
                if (booleanExtra) {
                    new ChooseLockSettingsHelper.Builder(getActivity()).setRequestCode(58).setTitle(getString(R$string.unlock_set_unlock_launch_picker_title)).setReturnCredentials(true).setRequestGatekeeperPasswordHandle(this.mRequestGatekeeperPassword).setUserId(this.mUserId).show();
                }
            } else {
                this.mFirstPassword = bundle.getParcelable("first_password");
                String string = bundle.getString("ui_stage");
                if (string != null) {
                    Stage valueOf = Stage.valueOf(string);
                    this.mUiStage = valueOf;
                    updateStage(valueOf);
                }
                this.mCurrentCredential = bundle.getParcelable("current_credential");
                this.mSaveAndFinishWorker = (SaveAndFinishWorker) getFragmentManager().findFragmentByTag("save_and_finish_worker");
            }
            if (activity instanceof SettingsActivity) {
                String hint = Stage.Introduction.getHint(getContext(), this.mIsAlphaMode, getStageType(), this.mIsManagedProfile);
                ((SettingsActivity) activity).setTitle((CharSequence) hint);
                this.mLayout.setHeaderText((CharSequence) hint);
                TextView textView3 = this.mNothingTitle;
                if (textView3 != null) {
                    textView3.setText(hint);
                }
            }
        }

        public void onDestroy() {
            super.onDestroy();
            LockscreenCredential lockscreenCredential = this.mCurrentCredential;
            if (lockscreenCredential != null) {
                lockscreenCredential.zeroize();
            }
            System.gc();
            System.runFinalization();
            System.gc();
        }

        /* access modifiers changed from: protected */
        public int getStageType() {
            if (this.mForFingerprint) {
                return 1;
            }
            if (this.mForFace) {
                return 2;
            }
            return this.mForBiometrics ? 3 : 0;
        }

        private void setupPasswordRequirementsView(View view) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R$id.password_requirements_view);
            this.mPasswordRestrictionView = recyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            PasswordRequirementAdapter passwordRequirementAdapter = new PasswordRequirementAdapter();
            this.mPasswordRequirementAdapter = passwordRequirementAdapter;
            this.mPasswordRestrictionView.setAdapter(passwordRequirementAdapter);
        }

        public void onResume() {
            super.onResume();
            updateStage(this.mUiStage);
            SaveAndFinishWorker saveAndFinishWorker = this.mSaveAndFinishWorker;
            if (saveAndFinishWorker != null) {
                saveAndFinishWorker.setListener(this);
                return;
            }
            this.mPasswordEntry.requestFocus();
            this.mPasswordEntry.scheduleShowSoftInput();
        }

        public void onPause() {
            SaveAndFinishWorker saveAndFinishWorker = this.mSaveAndFinishWorker;
            if (saveAndFinishWorker != null) {
                saveAndFinishWorker.setListener((SaveChosenLockWorkerBase.Listener) null);
            }
            super.onPause();
        }

        public void onSaveInstanceState(Bundle bundle) {
            super.onSaveInstanceState(bundle);
            bundle.putString("ui_stage", this.mUiStage.name());
            bundle.putParcelable("first_password", this.mFirstPassword);
            LockscreenCredential lockscreenCredential = this.mCurrentCredential;
            if (lockscreenCredential != null) {
                bundle.putParcelable("current_credential", lockscreenCredential.duplicate());
            }
        }

        public void onActivityResult(int i, int i2, Intent intent) {
            super.onActivityResult(i, i2, intent);
            if (i == 58) {
                if (i2 != -1) {
                    getActivity().setResult(1);
                    getActivity().finish();
                    return;
                }
                this.mCurrentCredential = intent.getParcelableExtra("password");
            }
        }

        /* access modifiers changed from: protected */
        public Intent getRedactionInterstitialIntent(Context context) {
            return RedactionInterstitial.createStartIntent(context, this.mUserId);
        }

        /* access modifiers changed from: protected */
        public void updateStage(Stage stage) {
            Stage stage2 = this.mUiStage;
            this.mUiStage = stage;
            updateUi();
            if (stage2 != stage) {
                GlifLayout glifLayout = this.mLayout;
                glifLayout.announceForAccessibility(glifLayout.getHeaderText());
            }
        }

        /* access modifiers changed from: package-private */
        @VisibleForTesting
        public boolean validatePassword(LockscreenCredential lockscreenCredential) {
            byte[] credential = lockscreenCredential.getCredential();
            List<PasswordValidationError> validatePassword = PasswordMetrics.validatePassword(this.mMinMetrics, this.mMinComplexity, !this.mIsAlphaMode, credential);
            this.mValidationErrors = validatePassword;
            if (validatePassword.isEmpty() && this.mLockPatternUtils.checkPasswordHistory(credential, getPasswordHistoryHashFactor(), this.mUserId)) {
                this.mValidationErrors = Collections.singletonList(new PasswordValidationError(14));
            }
            return this.mValidationErrors.isEmpty();
        }

        private byte[] getPasswordHistoryHashFactor() {
            if (this.mPasswordHistoryHashFactor == null) {
                LockPatternUtils lockPatternUtils = this.mLockPatternUtils;
                LockscreenCredential lockscreenCredential = this.mCurrentCredential;
                if (lockscreenCredential == null) {
                    lockscreenCredential = LockscreenCredential.createNone();
                }
                this.mPasswordHistoryHashFactor = lockPatternUtils.getPasswordHistoryHashFactor(lockscreenCredential, this.mUserId);
            }
            return this.mPasswordHistoryHashFactor;
        }

        public void handleNext() {
            LockscreenCredential lockscreenCredential;
            if (this.mSaveAndFinishWorker == null) {
                Editable text = this.mPasswordEntry.getText();
                if (!TextUtils.isEmpty(text)) {
                    if (this.mIsAlphaMode) {
                        lockscreenCredential = LockscreenCredential.createPassword(text);
                    } else {
                        lockscreenCredential = LockscreenCredential.createPin(text);
                    }
                    this.mChosenPassword = lockscreenCredential;
                    Stage stage = this.mUiStage;
                    if (stage == Stage.Introduction) {
                        if (validatePassword(lockscreenCredential)) {
                            this.mFirstPassword = this.mChosenPassword;
                            this.mPasswordEntry.setText("");
                            updateStage(Stage.NeedToConfirm);
                            return;
                        }
                        this.mChosenPassword.zeroize();
                    } else if (stage != Stage.NeedToConfirm) {
                    } else {
                        if (lockscreenCredential.equals(this.mFirstPassword)) {
                            startSaveAndFinish();
                            return;
                        }
                        Editable text2 = this.mPasswordEntry.getText();
                        if (text2 != null) {
                            Selection.setSelection(text2, 0, text2.length());
                        }
                        updateStage(Stage.ConfirmWrong);
                        this.mChosenPassword.zeroize();
                    }
                }
            }
        }

        /* access modifiers changed from: protected */
        public void setNextEnabled(boolean z) {
            this.mNextButton.setEnabled(z);
        }

        /* access modifiers changed from: protected */
        public void setNextText(int i) {
            this.mNextButton.setText(getActivity(), i);
        }

        /* access modifiers changed from: protected */
        public void onSkipOrClearButtonClick(View view) {
            this.mPasswordEntry.setText("");
        }

        /* access modifiers changed from: protected */
        public void onNextButtonClick(View view) {
            handleNext();
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 0 && i != 6 && i != 5) {
                return false;
            }
            handleNext();
            return true;
        }

        /* access modifiers changed from: package-private */
        public String[] convertErrorCodeToMessages() {
            int i;
            int i2;
            ArrayList arrayList = new ArrayList();
            for (PasswordValidationError next : this.mValidationErrors) {
                switch (next.errorCode) {
                    case 2:
                        arrayList.add(getString(R$string.lockpassword_illegal_character));
                        break;
                    case 3:
                        Resources resources = getResources();
                        if (this.mIsAlphaMode) {
                            i = R$plurals.lockpassword_password_too_short;
                        } else {
                            i = R$plurals.lockpassword_pin_too_short;
                        }
                        int i3 = next.requirement;
                        arrayList.add(resources.getQuantityString(i, i3, new Object[]{Integer.valueOf(i3)}));
                        break;
                    case 4:
                        arrayList.add(StringUtil.getIcuPluralsString(getContext(), next.requirement, R$string.lockpassword_password_too_short_all_numeric));
                        break;
                    case 5:
                        Resources resources2 = getResources();
                        if (this.mIsAlphaMode) {
                            i2 = R$plurals.lockpassword_password_too_long;
                        } else {
                            i2 = R$plurals.lockpassword_pin_too_long;
                        }
                        int i4 = next.requirement;
                        arrayList.add(resources2.getQuantityString(i2, i4 + 1, new Object[]{Integer.valueOf(i4 + 1)}));
                        break;
                    case 6:
                        arrayList.add(getString(R$string.lockpassword_pin_no_sequential_digits));
                        break;
                    case 7:
                        Resources resources3 = getResources();
                        int i5 = R$plurals.lockpassword_password_requires_letters;
                        int i6 = next.requirement;
                        arrayList.add(resources3.getQuantityString(i5, i6, new Object[]{Integer.valueOf(i6)}));
                        break;
                    case 8:
                        Resources resources4 = getResources();
                        int i7 = R$plurals.lockpassword_password_requires_uppercase;
                        int i8 = next.requirement;
                        arrayList.add(resources4.getQuantityString(i7, i8, new Object[]{Integer.valueOf(i8)}));
                        break;
                    case 9:
                        Resources resources5 = getResources();
                        int i9 = R$plurals.lockpassword_password_requires_lowercase;
                        int i10 = next.requirement;
                        arrayList.add(resources5.getQuantityString(i9, i10, new Object[]{Integer.valueOf(i10)}));
                        break;
                    case 10:
                        Resources resources6 = getResources();
                        int i11 = R$plurals.lockpassword_password_requires_numeric;
                        int i12 = next.requirement;
                        arrayList.add(resources6.getQuantityString(i11, i12, new Object[]{Integer.valueOf(i12)}));
                        break;
                    case 11:
                        Resources resources7 = getResources();
                        int i13 = R$plurals.lockpassword_password_requires_symbols;
                        int i14 = next.requirement;
                        arrayList.add(resources7.getQuantityString(i13, i14, new Object[]{Integer.valueOf(i14)}));
                        break;
                    case 12:
                        Resources resources8 = getResources();
                        int i15 = R$plurals.lockpassword_password_requires_nonletter;
                        int i16 = next.requirement;
                        arrayList.add(resources8.getQuantityString(i15, i16, new Object[]{Integer.valueOf(i16)}));
                        break;
                    case 13:
                        Resources resources9 = getResources();
                        int i17 = R$plurals.lockpassword_password_requires_nonnumerical;
                        int i18 = next.requirement;
                        arrayList.add(resources9.getQuantityString(i17, i18, new Object[]{Integer.valueOf(i18)}));
                        break;
                    case 14:
                        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) getContext().getSystemService(DevicePolicyManager.class);
                        if (!this.mIsAlphaMode) {
                            arrayList.add(devicePolicyManager.getResources().getString("Settings.PIN_RECENTLY_USED", new C1268xcfed0646(this)));
                            break;
                        } else {
                            arrayList.add(devicePolicyManager.getResources().getString("Settings.PASSWORD_RECENTLY_USED", new C1267xcfed0645(this)));
                            break;
                        }
                    default:
                        Log.wtf("ChooseLockPassword", "unknown error validating password: " + next);
                        break;
                }
            }
            return (String[]) arrayList.toArray(new String[0]);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ String lambda$convertErrorCodeToMessages$0() {
            return getString(R$string.lockpassword_password_recently_used);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ String lambda$convertErrorCodeToMessages$1() {
            return getString(R$string.lockpassword_pin_recently_used);
        }

        /* access modifiers changed from: protected */
        public void updateUi() {
            LockscreenCredential lockscreenCredential;
            boolean z = true;
            boolean z2 = this.mSaveAndFinishWorker == null;
            if (this.mIsAlphaMode) {
                lockscreenCredential = LockscreenCredential.createPasswordOrNone(this.mPasswordEntry.getText());
            } else {
                lockscreenCredential = LockscreenCredential.createPinOrNone(this.mPasswordEntry.getText());
            }
            int size = lockscreenCredential.size();
            if (this.mUiStage == Stage.Introduction) {
                this.mPasswordRestrictionView.setVisibility(0);
                boolean validatePassword = validatePassword(lockscreenCredential);
                this.mPasswordRequirementAdapter.setRequirements(convertErrorCodeToMessages());
                setNextEnabled(validatePassword);
            } else {
                this.mPasswordRestrictionView.setVisibility(8);
                setHeaderText(this.mUiStage.getHint(getContext(), this.mIsAlphaMode, getStageType(), this.mIsManagedProfile));
                setNextEnabled(z2 && size >= 4);
                FooterButton footerButton = this.mSkipOrClearButton;
                if (!z2 || size <= 0) {
                    z = false;
                }
                footerButton.setVisibility(toVisibility(z));
            }
            int stageType = getStageType();
            if (getStageType() != 0) {
                int message = this.mUiStage.getMessage(this.mIsAlphaMode, stageType);
                if (message != 0) {
                    this.mMessage.setVisibility(0);
                    this.mMessage.setText(message);
                } else {
                    this.mMessage.setVisibility(4);
                }
            } else {
                this.mMessage.setVisibility(8);
            }
            setNextText(this.mUiStage.buttonText);
            this.mPasswordEntryInputDisabler.setInputEnabled(z2);
            lockscreenCredential.zeroize();
        }

        private void setHeaderText(String str) {
            if (TextUtils.isEmpty(this.mLayout.getHeaderText()) || !this.mLayout.getHeaderText().toString().equals(str)) {
                this.mLayout.setHeaderText((CharSequence) str);
                TextView textView = this.mNothingTitle;
                if (textView != null) {
                    textView.setText(str);
                }
            }
        }

        public void afterTextChanged(Editable editable) {
            if (this.mUiStage == Stage.ConfirmWrong) {
                this.mUiStage = Stage.NeedToConfirm;
            }
            this.mTextChangedHandler.notifyAfterTextChanged();
        }

        private void startSaveAndFinish() {
            if (this.mSaveAndFinishWorker != null) {
                Log.w("ChooseLockPassword", "startSaveAndFinish with an existing SaveAndFinishWorker.");
                return;
            }
            this.mPasswordEntryInputDisabler.setInputEnabled(false);
            setNextEnabled(false);
            SaveAndFinishWorker saveAndFinishWorker = new SaveAndFinishWorker();
            this.mSaveAndFinishWorker = saveAndFinishWorker;
            saveAndFinishWorker.setListener(this);
            getFragmentManager().beginTransaction().add((Fragment) this.mSaveAndFinishWorker, "save_and_finish_worker").commit();
            getFragmentManager().executePendingTransactions();
            Intent intent = getActivity().getIntent();
            boolean booleanExtra = intent.getBooleanExtra("extra_require_password", true);
            if (this.mUnificationProfileId != -10000) {
                LockscreenCredential parcelableExtra = intent.getParcelableExtra("unification_profile_credential");
                try {
                    this.mSaveAndFinishWorker.setProfileToUnify(this.mUnificationProfileId, parcelableExtra);
                    if (parcelableExtra != null) {
                        parcelableExtra.close();
                    }
                } catch (Throwable th) {
                    th.addSuppressed(th);
                }
            }
            this.mSaveAndFinishWorker.start(this.mLockPatternUtils, booleanExtra, this.mRequestGatekeeperPassword, this.mChosenPassword, this.mCurrentCredential, this.mUserId);
            return;
            throw th;
        }

        public void onChosenLockSaveFinished(boolean z, Intent intent) {
            Intent redactionInterstitialIntent;
            getActivity().setResult(1, intent);
            LockscreenCredential lockscreenCredential = this.mChosenPassword;
            if (lockscreenCredential != null) {
                lockscreenCredential.zeroize();
            }
            LockscreenCredential lockscreenCredential2 = this.mCurrentCredential;
            if (lockscreenCredential2 != null) {
                lockscreenCredential2.zeroize();
            }
            LockscreenCredential lockscreenCredential3 = this.mFirstPassword;
            if (lockscreenCredential3 != null) {
                lockscreenCredential3.zeroize();
            }
            this.mPasswordEntry.setText("");
            if (!z && (redactionInterstitialIntent = getRedactionInterstitialIntent(getActivity())) != null) {
                startActivity(redactionInterstitialIntent);
            }
            getActivity().finish();
        }

        class TextChangedHandler extends Handler {
            TextChangedHandler() {
            }

            /* access modifiers changed from: private */
            public void notifyAfterTextChanged() {
                removeMessages(1);
                sendEmptyMessageDelayed(1, 100);
            }

            public void handleMessage(Message message) {
                if (ChooseLockPasswordFragment.this.getActivity() != null && message.what == 1) {
                    ChooseLockPasswordFragment.this.updateUi();
                }
            }
        }
    }

    public static class SaveAndFinishWorker extends SaveChosenLockWorkerBase {
        private LockscreenCredential mChosenPassword;
        private LockscreenCredential mCurrentCredential;

        public /* bridge */ /* synthetic */ void onCreate(Bundle bundle) {
            super.onCreate(bundle);
        }

        public /* bridge */ /* synthetic */ void setBlocking(boolean z) {
            super.setBlocking(z);
        }

        public /* bridge */ /* synthetic */ void setListener(SaveChosenLockWorkerBase.Listener listener) {
            super.setListener(listener);
        }

        public /* bridge */ /* synthetic */ void setProfileToUnify(int i, LockscreenCredential lockscreenCredential) {
            super.setProfileToUnify(i, lockscreenCredential);
        }

        public void start(LockPatternUtils lockPatternUtils, boolean z, boolean z2, LockscreenCredential lockscreenCredential, LockscreenCredential lockscreenCredential2, int i) {
            prepare(lockPatternUtils, z, z2, i);
            this.mChosenPassword = lockscreenCredential;
            if (lockscreenCredential2 == null) {
                lockscreenCredential2 = LockscreenCredential.createNone();
            }
            this.mCurrentCredential = lockscreenCredential2;
            this.mUserId = i;
            start();
        }

        /* access modifiers changed from: protected */
        public Pair<Boolean, Intent> saveAndVerifyInBackground() {
            boolean lockCredential = this.mUtils.setLockCredential(this.mChosenPassword, this.mCurrentCredential, this.mUserId);
            if (lockCredential) {
                unifyProfileCredentialIfRequested();
            }
            Intent intent = null;
            if (lockCredential && this.mRequestGatekeeperPassword) {
                VerifyCredentialResponse verifyCredential = this.mUtils.verifyCredential(this.mChosenPassword, this.mUserId, 1);
                if (!verifyCredential.isMatched() || !verifyCredential.containsGatekeeperPasswordHandle()) {
                    Log.e("ChooseLockPassword", "critical: bad response or missing GK PW handle for known good password: " + verifyCredential.toString());
                }
                intent = new Intent();
                intent.putExtra("gk_pw_handle", verifyCredential.getGatekeeperPasswordHandle());
            }
            return Pair.create(Boolean.valueOf(lockCredential), intent);
        }
    }
}
