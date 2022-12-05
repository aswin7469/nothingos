package com.android.settings.password;

import android.app.admin.PasswordMetrics;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
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
import com.android.settings.R;
import com.android.settings.SettingsActivity;
import com.android.settings.SetupWizardUtils;
import com.android.settings.Utils;
import com.android.settings.core.InstrumentedFragment;
import com.android.settings.notification.RedactionInterstitial;
import com.android.settings.password.ChooseLockPassword;
import com.android.settings.password.ChooseLockSettingsHelper;
import com.android.settings.password.SaveChosenLockWorkerBase;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.util.ThemeHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class ChooseLockPassword extends SettingsActivity {
    @Override // com.android.settings.core.SettingsBaseActivity
    protected boolean isToolbarEnabled() {
        return false;
    }

    @Override // com.android.settings.SettingsActivity, android.app.Activity
    public Intent getIntent() {
        Intent intent = new Intent(super.getIntent());
        intent.putExtra(":settings:show_fragment", getFragmentClass().getName());
        return intent;
    }

    /* loaded from: classes.dex */
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
            this.mIntent.putExtra("password", (Parcelable) lockscreenCredential);
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
            this.mIntent.putExtra("min_metrics", (Parcelable) passwordMetrics);
            return this;
        }

        public IntentBuilder setProfileToUnify(int i, LockscreenCredential lockscreenCredential) {
            this.mIntent.putExtra("unification_profile_id", i);
            this.mIntent.putExtra("unification_profile_credential", (Parcelable) lockscreenCredential);
            return this;
        }

        public Intent build() {
            return this.mIntent;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.SettingsActivity
    public boolean isValidFragment(String str) {
        return ChooseLockPasswordFragment.class.getName().equals(str);
    }

    Class<? extends Fragment> getFragmentClass() {
        return ChooseLockPasswordFragment.class;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.SettingsActivity, com.android.settings.core.SettingsBaseActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        setTheme(SetupWizardUtils.getTheme(this, getIntent()));
        ThemeHelper.trySetDynamicColor(this);
        super.onCreate(bundle);
        findViewById(R.id.content_parent).setFitsSystemWindows(false);
    }

    /* loaded from: classes.dex */
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
        private PasswordMetrics mMinMetrics;
        private FooterButton mNextButton;
        private TextView mNothingTitle;
        private ImeAwareEditText mPasswordEntry;
        private TextViewInputDisabler mPasswordEntryInputDisabler;
        private byte[] mPasswordHistoryHashFactor;
        private PasswordRequirementAdapter mPasswordRequirementAdapter;
        private RecyclerView mPasswordRestrictionView;
        private boolean mRequestGatekeeperPassword;
        private SaveAndFinishWorker mSaveAndFinishWorker;
        protected FooterButton mSkipOrClearButton;
        private TextChangedHandler mTextChangedHandler;
        protected int mUserId;
        private List<PasswordValidationError> mValidationErrors;
        private int mMinComplexity = 0;
        private int mUnificationProfileId = -10000;
        private int mPasswordType = 131072;
        protected Stage mUiStage = Stage.Introduction;

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // com.android.settingslib.core.instrumentation.Instrumentable
        public int getMetricsCategory() {
            return 28;
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        protected int toVisibility(boolean z) {
            return z ? 0 : 8;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Init of enum ConfirmWrong can be incorrect */
        /* JADX WARN: Init of enum Introduction can be incorrect */
        /* JADX WARN: Init of enum NeedToConfirm can be incorrect */
        /* loaded from: classes.dex */
        public enum Stage {
            Introduction(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, R.string.lockpassword_choose_pin_description, r14, R.string.next_label),
            NeedToConfirm(r25, r23, r25, r25, r25, r31, r28, r31, r31, r31, 0, 0, 0, 0, r36),
            ConfirmWrong(r43, r43, r43, r43, r43, r48, r48, r48, r48, r48, 0, 0, 0, 0, r36);
            
            public final int alphaHint;
            public final int alphaHintForBiometrics;
            public final int alphaHintForFace;
            public final int alphaHintForFingerprint;
            public final int alphaHintForProfile;
            public final int alphaMessage;
            public final int alphaMessageForBiometrics;
            public final int buttonText;
            public final int numericHint;
            public final int numericHintForBiometrics;
            public final int numericHintForFace;
            public final int numericHintForFingerprint;
            public final int numericHintForProfile;
            public final int numericMessage;
            public final int numericMessageForBiometrics;

            static {
                int i = R.string.lockpassword_choose_your_password_header;
                int i2 = R.string.lockpassword_choose_your_profile_password_header;
                int i3 = R.string.lockpassword_choose_your_password_header_for_fingerprint;
                int i4 = R.string.lockpassword_choose_your_password_header_for_face;
                int i5 = R.string.lockpassword_choose_your_password_header_for_biometrics;
                int i6 = R.string.lockpassword_choose_your_pin_header;
                int i7 = R.string.lockpassword_choose_your_profile_pin_header;
                int i8 = R.string.lockpassword_choose_your_pin_header_for_fingerprint;
                int i9 = R.string.lockpassword_choose_your_pin_header_for_face;
                int i10 = R.string.lockpassword_choose_your_pin_header_for_biometrics;
                int i11 = R.string.lockpassword_choose_password_description;
                int i12 = R.string.lock_settings_picker_biometrics_added_security_message;
                int i13 = R.string.lockpassword_confirm_your_password_header;
                int i14 = R.string.lockpassword_reenter_your_profile_password_header;
                int i15 = R.string.lockpassword_confirm_your_pin_header;
                int i16 = R.string.lockpassword_reenter_your_profile_pin_header;
                int i17 = R.string.lockpassword_confirm_label;
                int i18 = R.string.lockpassword_confirm_passwords_dont_match;
                int i19 = R.string.lockpassword_confirm_pins_dont_match;
            }

            Stage(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15) {
                this.alphaHint = i;
                this.alphaHintForProfile = i2;
                this.alphaHintForFingerprint = i3;
                this.alphaHintForFace = i4;
                this.alphaHintForBiometrics = i5;
                this.numericHint = i6;
                this.numericHintForProfile = i7;
                this.numericHintForFingerprint = i8;
                this.numericHintForFace = i9;
                this.numericHintForBiometrics = i10;
                this.alphaMessage = i11;
                this.alphaMessageForBiometrics = i12;
                this.numericMessage = i13;
                this.numericMessageForBiometrics = i14;
                this.buttonText = i15;
            }

            public int getHint(boolean z, int i, boolean z2) {
                if (z) {
                    if (i == 1) {
                        return this.alphaHintForFingerprint;
                    }
                    if (i == 2) {
                        return this.alphaHintForFace;
                    }
                    if (i == 3) {
                        return this.alphaHintForBiometrics;
                    }
                    return z2 ? this.alphaHintForProfile : this.alphaHint;
                } else if (i == 1) {
                    return this.numericHintForFingerprint;
                } else {
                    if (i == 2) {
                        return this.numericHintForFace;
                    }
                    if (i == 3) {
                        return this.numericHintForBiometrics;
                    }
                    return z2 ? this.numericHintForProfile : this.numericHint;
                }
            }

            public int getMessage(boolean z, int i) {
                return (i == 1 || i == 2 || i == 3) ? z ? this.alphaMessageForBiometrics : this.numericMessageForBiometrics : z ? this.alphaMessage : this.numericMessage;
            }
        }

        @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            this.mLockPatternUtils = new LockPatternUtils(getActivity());
            Intent intent = getActivity().getIntent();
            if (!(getActivity() instanceof ChooseLockPassword)) {
                throw new SecurityException("Fragment contained in wrong activity");
            }
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
                LockscreenCredential lockscreenCredential = (LockscreenCredential) intent.getParcelableExtra("password");
                LockPatternUtils lockPatternUtils = new LockPatternUtils(getActivity());
                saveAndFinishWorker.setBlocking(true);
                saveAndFinishWorker.setListener(this);
                saveAndFinishWorker.start(lockPatternUtils, booleanExtra, false, lockscreenCredential, lockscreenCredential, this.mUserId);
            }
            this.mTextChangedHandler = new TextChangedHandler();
        }

        @Override // androidx.fragment.app.Fragment
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            return layoutInflater.inflate(R.layout.choose_lock_password, viewGroup, false);
        }

        @Override // androidx.fragment.app.Fragment
        public void onViewCreated(View view, Bundle bundle) {
            super.onViewCreated(view, bundle);
            GlifLayout glifLayout = (GlifLayout) view;
            this.mLayout = glifLayout;
            glifLayout.findViewById(R.id.sud_layout_header).setVisibility(8);
            this.mNothingTitle = (TextView) view.findViewById(R.id.title);
            ((ViewGroup) view.findViewById(R.id.password_container)).setOpticalInsets(Insets.NONE);
            FooterBarMixin footerBarMixin = (FooterBarMixin) this.mLayout.getMixin(FooterBarMixin.class);
            footerBarMixin.setSecondaryButton(new FooterButton.Builder(getActivity()).setText(R.string.lockpassword_clear_label).setListener(new View.OnClickListener() { // from class: com.android.settings.password.ChooseLockPassword$ChooseLockPasswordFragment$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChooseLockPassword.ChooseLockPasswordFragment.this.onSkipOrClearButtonClick(view2);
                }
            }).setButtonType(7).setTheme(R.style.SudGlifButton_Secondary).build());
            footerBarMixin.setPrimaryButton(new FooterButton.Builder(getActivity()).setText(R.string.next_label).setListener(new View.OnClickListener() { // from class: com.android.settings.password.ChooseLockPassword$ChooseLockPasswordFragment$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ChooseLockPassword.ChooseLockPasswordFragment.this.onNextButtonClick(view2);
                }
            }).setButtonType(5).setTheme(R.style.SudGlifButton_Primary).build());
            this.mSkipOrClearButton = footerBarMixin.getSecondaryButton();
            this.mNextButton = footerBarMixin.getPrimaryButton();
            this.mMessage = (TextView) view.findViewById(R.id.sud_layout_description);
            this.mLayout.setIcon(getActivity().getDrawable(R.drawable.ic_lock));
            int i = this.mPasswordType;
            this.mIsAlphaMode = 262144 == i || 327680 == i || 393216 == i;
            setupPasswordRequirementsView(view);
            this.mPasswordRestrictionView.setLayoutManager(new LinearLayoutManager(getActivity()));
            ImeAwareEditText findViewById = view.findViewById(R.id.password_entry);
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
                this.mPasswordEntry.setContentDescription(getString(R.string.unlock_set_unlock_password_title));
            } else {
                this.mPasswordEntry.setContentDescription(getString(R.string.unlock_set_unlock_pin_title));
            }
            this.mPasswordEntry.setTypeface(Typeface.create(getContext().getString(17039948), 0));
            Intent intent = getActivity().getIntent();
            boolean booleanExtra = intent.getBooleanExtra("confirm_credentials", true);
            this.mCurrentCredential = intent.getParcelableExtra("password");
            this.mRequestGatekeeperPassword = intent.getBooleanExtra("request_gk_pw_handle", false);
            if (bundle == null) {
                updateStage(Stage.Introduction);
                if (booleanExtra) {
                    new ChooseLockSettingsHelper.Builder(getActivity()).setRequestCode(58).setTitle(getString(R.string.unlock_set_unlock_launch_picker_title)).setReturnCredentials(true).setRequestGatekeeperPasswordHandle(this.mRequestGatekeeperPassword).setUserId(this.mUserId).show();
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
                int hint = Stage.Introduction.getHint(this.mIsAlphaMode, getStageType(), this.mIsManagedProfile);
                ((SettingsActivity) activity).setTitle(hint);
                this.mLayout.setHeaderText(hint);
                TextView textView = this.mNothingTitle;
                if (textView == null) {
                    return;
                }
                textView.setText(hint);
            }
        }

        @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
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

        protected int getStageType() {
            if (this.mForFingerprint) {
                return 1;
            }
            if (this.mForFace) {
                return 2;
            }
            return this.mForBiometrics ? 3 : 0;
        }

        private void setupPasswordRequirementsView(View view) {
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.password_requirements_view);
            this.mPasswordRestrictionView = recyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            PasswordRequirementAdapter passwordRequirementAdapter = new PasswordRequirementAdapter();
            this.mPasswordRequirementAdapter = passwordRequirementAdapter;
            this.mPasswordRestrictionView.setAdapter(passwordRequirementAdapter);
        }

        @Override // com.android.settings.core.InstrumentedFragment, com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
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

        @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
        public void onPause() {
            SaveAndFinishWorker saveAndFinishWorker = this.mSaveAndFinishWorker;
            if (saveAndFinishWorker != null) {
                saveAndFinishWorker.setListener(null);
            }
            super.onPause();
        }

        @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
        public void onSaveInstanceState(Bundle bundle) {
            super.onSaveInstanceState(bundle);
            bundle.putString("ui_stage", this.mUiStage.name());
            bundle.putParcelable("first_password", this.mFirstPassword);
            LockscreenCredential lockscreenCredential = this.mCurrentCredential;
            if (lockscreenCredential != null) {
                bundle.putParcelable("current_credential", lockscreenCredential.duplicate());
            }
        }

        @Override // androidx.fragment.app.Fragment
        public void onActivityResult(int i, int i2, Intent intent) {
            super.onActivityResult(i, i2, intent);
            if (i != 58) {
                return;
            }
            if (i2 != -1) {
                getActivity().setResult(1);
                getActivity().finish();
                return;
            }
            this.mCurrentCredential = intent.getParcelableExtra("password");
        }

        protected Intent getRedactionInterstitialIntent(Context context) {
            return RedactionInterstitial.createStartIntent(context, this.mUserId);
        }

        protected void updateStage(Stage stage) {
            Stage stage2 = this.mUiStage;
            this.mUiStage = stage;
            updateUi();
            if (stage2 != stage) {
                GlifLayout glifLayout = this.mLayout;
                glifLayout.announceForAccessibility(glifLayout.getHeaderText());
            }
        }

        @VisibleForTesting
        boolean validatePassword(LockscreenCredential lockscreenCredential) {
            byte[] credential = lockscreenCredential.getCredential();
            List<PasswordValidationError> validatePassword = PasswordMetrics.validatePassword(this.mMinMetrics, this.mMinComplexity, !this.mIsAlphaMode, credential);
            this.mValidationErrors = validatePassword;
            if (validatePassword.isEmpty() && this.mLockPatternUtils.checkPasswordHistory(credential, getPasswordHistoryHashFactor(), this.mUserId)) {
                this.mValidationErrors = Collections.singletonList(new PasswordValidationError(13));
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
            if (this.mSaveAndFinishWorker != null) {
                return;
            }
            Editable text = this.mPasswordEntry.getText();
            if (TextUtils.isEmpty(text)) {
                return;
            }
            LockscreenCredential createPassword = this.mIsAlphaMode ? LockscreenCredential.createPassword(text) : LockscreenCredential.createPin(text);
            this.mChosenPassword = createPassword;
            Stage stage = this.mUiStage;
            if (stage == Stage.Introduction) {
                if (validatePassword(createPassword)) {
                    this.mFirstPassword = this.mChosenPassword;
                    this.mPasswordEntry.setText("");
                    updateStage(Stage.NeedToConfirm);
                    return;
                }
                this.mChosenPassword.zeroize();
            } else if (stage != Stage.NeedToConfirm) {
            } else {
                if (createPassword.equals(this.mFirstPassword)) {
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

        protected void setNextEnabled(boolean z) {
            this.mNextButton.setEnabled(z);
        }

        protected void setNextText(int i) {
            this.mNextButton.setText(getActivity(), i);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onSkipOrClearButtonClick(View view) {
            this.mPasswordEntry.setText("");
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void onNextButtonClick(View view) {
            handleNext();
        }

        @Override // android.widget.TextView.OnEditorActionListener
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 0 || i == 6 || i == 5) {
                handleNext();
                return true;
            }
            return false;
        }

        String[] convertErrorCodeToMessages() {
            int i;
            int i2;
            int i3;
            ArrayList arrayList = new ArrayList();
            for (PasswordValidationError passwordValidationError : this.mValidationErrors) {
                switch (passwordValidationError.errorCode) {
                    case 2:
                        arrayList.add(getString(R.string.lockpassword_illegal_character));
                        break;
                    case 3:
                        Resources resources = getResources();
                        if (this.mIsAlphaMode) {
                            i = R.plurals.lockpassword_password_too_short;
                        } else {
                            i = R.plurals.lockpassword_pin_too_short;
                        }
                        int i4 = passwordValidationError.requirement;
                        arrayList.add(resources.getQuantityString(i, i4, Integer.valueOf(i4)));
                        break;
                    case 4:
                        Resources resources2 = getResources();
                        if (this.mIsAlphaMode) {
                            i2 = R.plurals.lockpassword_password_too_long;
                        } else {
                            i2 = R.plurals.lockpassword_pin_too_long;
                        }
                        int i5 = passwordValidationError.requirement;
                        arrayList.add(resources2.getQuantityString(i2, i5 + 1, Integer.valueOf(i5 + 1)));
                        break;
                    case 5:
                        arrayList.add(getString(R.string.lockpassword_pin_no_sequential_digits));
                        break;
                    case 6:
                        Resources resources3 = getResources();
                        int i6 = R.plurals.lockpassword_password_requires_letters;
                        int i7 = passwordValidationError.requirement;
                        arrayList.add(resources3.getQuantityString(i6, i7, Integer.valueOf(i7)));
                        break;
                    case 7:
                        Resources resources4 = getResources();
                        int i8 = R.plurals.lockpassword_password_requires_uppercase;
                        int i9 = passwordValidationError.requirement;
                        arrayList.add(resources4.getQuantityString(i8, i9, Integer.valueOf(i9)));
                        break;
                    case 8:
                        Resources resources5 = getResources();
                        int i10 = R.plurals.lockpassword_password_requires_lowercase;
                        int i11 = passwordValidationError.requirement;
                        arrayList.add(resources5.getQuantityString(i10, i11, Integer.valueOf(i11)));
                        break;
                    case 9:
                        Resources resources6 = getResources();
                        int i12 = R.plurals.lockpassword_password_requires_numeric;
                        int i13 = passwordValidationError.requirement;
                        arrayList.add(resources6.getQuantityString(i12, i13, Integer.valueOf(i13)));
                        break;
                    case 10:
                        Resources resources7 = getResources();
                        int i14 = R.plurals.lockpassword_password_requires_symbols;
                        int i15 = passwordValidationError.requirement;
                        arrayList.add(resources7.getQuantityString(i14, i15, Integer.valueOf(i15)));
                        break;
                    case 11:
                        Resources resources8 = getResources();
                        int i16 = R.plurals.lockpassword_password_requires_nonletter;
                        int i17 = passwordValidationError.requirement;
                        arrayList.add(resources8.getQuantityString(i16, i17, Integer.valueOf(i17)));
                        break;
                    case 12:
                        Resources resources9 = getResources();
                        int i18 = R.plurals.lockpassword_password_requires_nonnumerical;
                        int i19 = passwordValidationError.requirement;
                        arrayList.add(resources9.getQuantityString(i18, i19, Integer.valueOf(i19)));
                        break;
                    case 13:
                        if (this.mIsAlphaMode) {
                            i3 = R.string.lockpassword_password_recently_used;
                        } else {
                            i3 = R.string.lockpassword_pin_recently_used;
                        }
                        arrayList.add(getString(i3));
                        break;
                    default:
                        Log.wtf("ChooseLockPassword", "unknown error validating password: " + passwordValidationError);
                        break;
                }
            }
            return (String[]) arrayList.toArray(new String[0]);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        public void updateUi() {
            LockscreenCredential createPinOrNone;
            boolean z = true;
            boolean z2 = this.mSaveAndFinishWorker == null;
            if (this.mIsAlphaMode) {
                createPinOrNone = LockscreenCredential.createPasswordOrNone(this.mPasswordEntry.getText());
            } else {
                createPinOrNone = LockscreenCredential.createPinOrNone(this.mPasswordEntry.getText());
            }
            int size = createPinOrNone.size();
            if (this.mUiStage == Stage.Introduction) {
                this.mPasswordRestrictionView.setVisibility(0);
                boolean validatePassword = validatePassword(createPinOrNone);
                this.mPasswordRequirementAdapter.setRequirements(convertErrorCodeToMessages());
                setNextEnabled(validatePassword);
            } else {
                this.mPasswordRestrictionView.setVisibility(8);
                setHeaderText(getString(this.mUiStage.getHint(this.mIsAlphaMode, getStageType(), this.mIsManagedProfile)));
                setNextEnabled(z2 && size >= 4);
                FooterButton footerButton = this.mSkipOrClearButton;
                if (!z2 || size <= 0) {
                    z = false;
                }
                footerButton.setVisibility(toVisibility(z));
            }
            int message = this.mUiStage.getMessage(this.mIsAlphaMode, getStageType());
            if (message != 0) {
                this.mMessage.setVisibility(0);
                this.mMessage.setText(message);
            } else {
                this.mMessage.setVisibility(4);
            }
            setNextText(this.mUiStage.buttonText);
            this.mPasswordEntryInputDisabler.setInputEnabled(z2);
            createPinOrNone.zeroize();
        }

        private void setHeaderText(String str) {
            if (TextUtils.isEmpty(this.mLayout.getHeaderText()) || !this.mLayout.getHeaderText().toString().equals(str)) {
                this.mLayout.setHeaderText(str);
                TextView textView = this.mNothingTitle;
                if (textView == null) {
                    return;
                }
                textView.setText(str);
            }
        }

        @Override // android.text.TextWatcher
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
            getFragmentManager().beginTransaction().add(this.mSaveAndFinishWorker, "save_and_finish_worker").commit();
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
                    if (parcelableExtra != null) {
                        try {
                            parcelableExtra.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            this.mSaveAndFinishWorker.start(this.mLockPatternUtils, booleanExtra, this.mRequestGatekeeperPassword, this.mChosenPassword, this.mCurrentCredential, this.mUserId);
        }

        @Override // com.android.settings.password.SaveChosenLockWorkerBase.Listener
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

        /* loaded from: classes.dex */
        class TextChangedHandler extends Handler {
            TextChangedHandler() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public void notifyAfterTextChanged() {
                removeMessages(1);
                sendEmptyMessageDelayed(1, 100L);
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                if (ChooseLockPasswordFragment.this.getActivity() != null && message.what == 1) {
                    ChooseLockPasswordFragment.this.updateUi();
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class SaveAndFinishWorker extends SaveChosenLockWorkerBase {
        private LockscreenCredential mChosenPassword;
        private LockscreenCredential mCurrentCredential;

        @Override // com.android.settings.password.SaveChosenLockWorkerBase, androidx.fragment.app.Fragment
        public /* bridge */ /* synthetic */ void onCreate(Bundle bundle) {
            super.onCreate(bundle);
        }

        @Override // com.android.settings.password.SaveChosenLockWorkerBase
        public /* bridge */ /* synthetic */ void setBlocking(boolean z) {
            super.setBlocking(z);
        }

        @Override // com.android.settings.password.SaveChosenLockWorkerBase
        public /* bridge */ /* synthetic */ void setListener(SaveChosenLockWorkerBase.Listener listener) {
            super.setListener(listener);
        }

        @Override // com.android.settings.password.SaveChosenLockWorkerBase
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

        @Override // com.android.settings.password.SaveChosenLockWorkerBase
        protected Pair<Boolean, Intent> saveAndVerifyInBackground() {
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
