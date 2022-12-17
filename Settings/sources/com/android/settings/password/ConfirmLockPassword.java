package com.android.settings.password;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.os.UserManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImeAwareEditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.android.internal.widget.LockPatternChecker;
import com.android.internal.widget.LockscreenCredential;
import com.android.internal.widget.TextViewInputDisabler;
import com.android.internal.widget.VerifyCredentialResponse;
import com.android.settings.R$anim;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.password.ConfirmDeviceCredentialBaseActivity;
import com.android.settings.password.CredentialCheckResultTracker;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import com.google.android.setupdesign.GlifLayout;
import java.util.ArrayList;

public class ConfirmLockPassword extends ConfirmDeviceCredentialBaseActivity {
    /* access modifiers changed from: private */
    public static final int[] DETAIL_TEXTS = {R$string.lockpassword_confirm_your_pin_generic, R$string.lockpassword_confirm_your_password_generic, R$string.lockpassword_confirm_your_pin_generic_profile, R$string.lockpassword_confirm_your_password_generic_profile, R$string.lockpassword_strong_auth_required_device_pin, R$string.lockpassword_strong_auth_required_device_password, R$string.lockpassword_strong_auth_required_work_pin, R$string.lockpassword_strong_auth_required_work_password};
    /* access modifiers changed from: private */
    public static final String[] DETAIL_TEXT_OVERRIDES = {"UNDEFINED", "UNDEFINED", "Settings.WORK_PROFILE_CONFIRM_PIN", "Settings.WORK_PROFILE_CONFIRM_PASSWORD", "UNDEFINED", "UNDEFINED", "Settings.WORK_PROFILE_PIN_REQUIRED", "Settings.WORK_PROFILE_PASSWORD_REQUIRED"};

    public static class InternalActivity extends ConfirmLockPassword {
    }

    public Intent getIntent() {
        Intent intent = new Intent(super.getIntent());
        intent.putExtra(":settings:show_fragment", ConfirmLockPasswordFragment.class.getName());
        return intent;
    }

    /* access modifiers changed from: protected */
    public boolean isValidFragment(String str) {
        return ConfirmLockPasswordFragment.class.getName().equals(str);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R$id.main_content);
        if (findFragmentById != null && (findFragmentById instanceof ConfirmLockPasswordFragment)) {
            ((ConfirmLockPasswordFragment) findFragmentById).onWindowFocusChanged(z);
        }
    }

    public static class ConfirmLockPasswordFragment extends ConfirmDeviceCredentialBaseFragment implements View.OnClickListener, TextView.OnEditorActionListener, CredentialCheckResultTracker.Listener {
        private AppearAnimationUtils mAppearAnimationUtils;
        private CountDownTimer mCountdownTimer;
        /* access modifiers changed from: private */
        public CredentialCheckResultTracker mCredentialCheckResultTracker;
        private DisappearAnimationUtils mDisappearAnimationUtils;
        private boolean mDisappearing = false;
        private GlifLayout mGlifLayout;
        private InputMethodManager mImm;
        /* access modifiers changed from: private */
        public boolean mIsAlpha;
        private boolean mIsManagedProfile;
        private ImeAwareEditText mPasswordEntry;
        private TextViewInputDisabler mPasswordEntryInputDisabler;
        /* access modifiers changed from: private */
        public AsyncTask<?, ?, ?> mPendingLockCheck;

        public int getMetricsCategory() {
            return 30;
        }

        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            int i;
            int keyguardStoredPasswordQuality = this.mLockPatternUtils.getKeyguardStoredPasswordQuality(this.mEffectiveUserId);
            if (((ConfirmLockPassword) getActivity()).getConfirmCredentialTheme() == ConfirmDeviceCredentialBaseActivity.ConfirmCredentialTheme.NORMAL) {
                i = R$layout.confirm_lock_password_normal;
            } else {
                i = R$layout.confirm_lock_password;
            }
            View inflate = layoutInflater.inflate(i, viewGroup, false);
            this.mGlifLayout = (GlifLayout) inflate.findViewById(R$id.setup_wizard_layout);
            inflate.findViewById(R$id.sud_layout_header).setVisibility(8);
            ImeAwareEditText findViewById = inflate.findViewById(R$id.password_entry);
            this.mPasswordEntry = findViewById;
            findViewById.setOnEditorActionListener(this);
            this.mPasswordEntry.requestFocus();
            this.mPasswordEntryInputDisabler = new TextViewInputDisabler(this.mPasswordEntry);
            this.mErrorTextView = (TextView) inflate.findViewById(R$id.errorText);
            this.mIsAlpha = 262144 == keyguardStoredPasswordQuality || 327680 == keyguardStoredPasswordQuality || 393216 == keyguardStoredPasswordQuality || 524288 == keyguardStoredPasswordQuality;
            this.mImm = (InputMethodManager) getActivity().getSystemService("input_method");
            this.mIsManagedProfile = UserManager.get(getActivity()).isManagedProfile(this.mEffectiveUserId);
            Typeface create = Typeface.create("NDot57", 0);
            TextView textView = (TextView) inflate.findViewById(R$id.title);
            textView.setTypeface(create);
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                CharSequence charSequenceExtra = intent.getCharSequenceExtra("com.android.settings.ConfirmCredentials.header");
                CharSequence charSequenceExtra2 = intent.getCharSequenceExtra("com.android.settings.ConfirmCredentials.details");
                if (TextUtils.isEmpty(charSequenceExtra) && this.mIsManagedProfile) {
                    charSequenceExtra = this.mDevicePolicyManager.getOrganizationNameForUser(this.mUserId);
                }
                if (TextUtils.isEmpty(charSequenceExtra)) {
                    charSequenceExtra = getDefaultHeader();
                }
                if (TextUtils.isEmpty(charSequenceExtra2)) {
                    charSequenceExtra2 = getDefaultDetails();
                }
                this.mGlifLayout.setHeaderText(charSequenceExtra);
                this.mGlifLayout.setDescriptionText(charSequenceExtra2);
                textView.setText(charSequenceExtra);
                TextView textView2 = (TextView) inflate.findViewById(R$id.summary);
                if (textView2 != null) {
                    textView2.setText(charSequenceExtra2);
                }
            }
            int inputType = this.mPasswordEntry.getInputType();
            if (this.mIsAlpha) {
                this.mPasswordEntry.setInputType(inputType);
                this.mPasswordEntry.setContentDescription(getContext().getString(R$string.unlock_set_unlock_password_title));
            } else {
                this.mPasswordEntry.setInputType(18);
                this.mPasswordEntry.setContentDescription(getContext().getString(R$string.unlock_set_unlock_pin_title));
            }
            this.mPasswordEntry.setTypeface(Typeface.create(getContext().getString(17039983), 0));
            this.mAppearAnimationUtils = new AppearAnimationUtils(getContext(), 220, 2.0f, 1.0f, AnimationUtils.loadInterpolator(getContext(), 17563662));
            this.mDisappearAnimationUtils = new DisappearAnimationUtils(getContext(), 110, 1.0f, 0.5f, AnimationUtils.loadInterpolator(getContext(), 17563663));
            setAccessibilityTitle(this.mGlifLayout.getHeaderText());
            CredentialCheckResultTracker credentialCheckResultTracker = (CredentialCheckResultTracker) getFragmentManager().findFragmentByTag("check_lock_result");
            this.mCredentialCheckResultTracker = credentialCheckResultTracker;
            if (credentialCheckResultTracker == null) {
                this.mCredentialCheckResultTracker = new CredentialCheckResultTracker();
                getFragmentManager().beginTransaction().add((Fragment) this.mCredentialCheckResultTracker, "check_lock_result").commit();
            }
            return inflate;
        }

        public void onViewCreated(View view, Bundle bundle) {
            int i;
            super.onViewCreated(view, bundle);
            Button button = this.mForgotButton;
            if (button != null) {
                if (this.mIsAlpha) {
                    i = R$string.lockpassword_forgot_password;
                } else {
                    i = R$string.lockpassword_forgot_pin;
                }
                button.setText(i);
            }
        }

        public void onDestroy() {
            super.onDestroy();
            this.mPasswordEntry.setText((CharSequence) null);
            new Handler(Looper.myLooper()).postDelayed(new C1283x97dc6e8(), 5000);
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ void lambda$onDestroy$0() {
            System.gc();
            System.runFinalization();
            System.gc();
        }

        private String getDefaultHeader() {
            if (this.mFrp) {
                if (this.mIsAlpha) {
                    return getString(R$string.lockpassword_confirm_your_password_header_frp);
                }
                return getString(R$string.lockpassword_confirm_your_pin_header_frp);
            } else if (this.mIsManagedProfile) {
                if (this.mIsAlpha) {
                    return this.mDevicePolicyManager.getResources().getString("Settings.CONFIRM_WORK_PROFILE_PASSWORD_HEADER", new C1284x97dc6e9(this));
                }
                return this.mDevicePolicyManager.getResources().getString("Settings.CONFIRM_WORK_PROFILE_PIN_HEADER", new C1285x97dc6ea(this));
            } else if (this.mIsAlpha) {
                return getString(R$string.lockpassword_confirm_your_password_header);
            } else {
                return getString(R$string.lockpassword_confirm_your_pin_header);
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ String lambda$getDefaultHeader$1() {
            return getString(R$string.lockpassword_confirm_your_work_password_header);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ String lambda$getDefaultHeader$2() {
            return getString(R$string.lockpassword_confirm_your_work_pin_header);
        }

        private String getDefaultDetails() {
            if (!this.mFrp) {
                int i = ((isStrongAuthRequired() ? 1 : 0) << true) + ((this.mIsManagedProfile ? 1 : 0) << true) + (this.mIsAlpha ? 1 : 0);
                return this.mDevicePolicyManager.getResources().getString(ConfirmLockPassword.DETAIL_TEXT_OVERRIDES[i], new C1286x97dc6eb(this, i));
            } else if (this.mIsAlpha) {
                return getString(R$string.lockpassword_confirm_your_password_details_frp);
            } else {
                return getString(R$string.lockpassword_confirm_your_pin_details_frp);
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ String lambda$getDefaultDetails$3(int i) {
            return getString(ConfirmLockPassword.DETAIL_TEXTS[i]);
        }

        private int getErrorMessage() {
            if (this.mIsAlpha) {
                return R$string.lockpassword_invalid_password;
            }
            return R$string.lockpassword_invalid_pin;
        }

        /* access modifiers changed from: protected */
        public String getLastTryOverrideErrorMessageId(int i) {
            if (i == 2) {
                return this.mIsAlpha ? "Settings.WORK_PROFILE_LAST_PASSWORD_ATTEMPT_BEFORE_WIPE" : "Settings.WORK_PROFILE_LAST_PIN_ATTEMPT_BEFORE_WIPE";
            }
            return "UNDEFINED";
        }

        /* access modifiers changed from: protected */
        public int getLastTryDefaultErrorMessage(int i) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        throw new IllegalArgumentException("Unrecognized user type:" + i);
                    } else if (this.mIsAlpha) {
                        return R$string.lock_last_password_attempt_before_wipe_user;
                    } else {
                        return R$string.lock_last_pin_attempt_before_wipe_user;
                    }
                } else if (this.mIsAlpha) {
                    return R$string.lock_last_password_attempt_before_wipe_profile;
                } else {
                    return R$string.lock_last_pin_attempt_before_wipe_profile;
                }
            } else if (this.mIsAlpha) {
                return R$string.lock_last_password_attempt_before_wipe_device;
            } else {
                return R$string.lock_last_pin_attempt_before_wipe_device;
            }
        }

        public void prepareEnterAnimation() {
            super.prepareEnterAnimation();
            this.mGlifLayout.getHeaderTextView().setAlpha(0.0f);
            this.mGlifLayout.getDescriptionTextView().setAlpha(0.0f);
            this.mCancelButton.setAlpha(0.0f);
            Button button = this.mForgotButton;
            if (button != null) {
                button.setAlpha(0.0f);
            }
            this.mPasswordEntry.setAlpha(0.0f);
            this.mErrorTextView.setAlpha(0.0f);
        }

        private View[] getActiveViews() {
            ArrayList arrayList = new ArrayList();
            arrayList.add(this.mGlifLayout.getHeaderTextView());
            arrayList.add(this.mGlifLayout.getDescriptionTextView());
            if (this.mCancelButton.getVisibility() == 0) {
                arrayList.add(this.mCancelButton);
            }
            Button button = this.mForgotButton;
            if (button != null) {
                arrayList.add(button);
            }
            arrayList.add(this.mPasswordEntry);
            arrayList.add(this.mErrorTextView);
            return (View[]) arrayList.toArray(new View[0]);
        }

        public void startEnterAnimation() {
            super.startEnterAnimation();
            this.mAppearAnimationUtils.startAnimation(getActiveViews(), new C1282x97dc6e7(this));
        }

        public void onPause() {
            super.onPause();
            CountDownTimer countDownTimer = this.mCountdownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
                this.mCountdownTimer = null;
            }
            this.mCredentialCheckResultTracker.setListener((CredentialCheckResultTracker.Listener) null);
        }

        public void onResume() {
            super.onResume();
            long lockoutAttemptDeadline = this.mLockPatternUtils.getLockoutAttemptDeadline(this.mEffectiveUserId);
            if (lockoutAttemptDeadline != 0) {
                this.mCredentialCheckResultTracker.clearResult();
                handleAttemptLockout(lockoutAttemptDeadline);
            } else {
                updatePasswordEntry();
                this.mErrorTextView.setText("");
                updateErrorMessage(this.mLockPatternUtils.getCurrentFailedPasswordAttempts(this.mEffectiveUserId));
            }
            this.mCredentialCheckResultTracker.setListener(this);
        }

        /* access modifiers changed from: private */
        public void updatePasswordEntry() {
            boolean z = this.mLockPatternUtils.getLockoutAttemptDeadline(this.mEffectiveUserId) != 0;
            this.mPasswordEntry.setEnabled(!z);
            this.mPasswordEntryInputDisabler.setInputEnabled(!z);
            if (z) {
                this.mImm.hideSoftInputFromWindow(this.mPasswordEntry.getWindowToken(), 0);
                return;
            }
            this.mPasswordEntry.scheduleShowSoftInput();
            this.mPasswordEntry.requestFocus();
        }

        public void onWindowFocusChanged(boolean z) {
            if (z) {
                this.mPasswordEntry.post(new C1282x97dc6e7(this));
            }
        }

        private void handleNext() {
            LockscreenCredential lockscreenCredential;
            if (this.mPendingLockCheck == null && !this.mDisappearing) {
                Editable text = this.mPasswordEntry.getText();
                if (!TextUtils.isEmpty(text)) {
                    if (this.mIsAlpha) {
                        lockscreenCredential = LockscreenCredential.createPassword(text);
                    } else {
                        lockscreenCredential = LockscreenCredential.createPin(text);
                    }
                    this.mPasswordEntryInputDisabler.setInputEnabled(false);
                    Intent intent = new Intent();
                    if (this.mReturnGatekeeperPassword) {
                        if (isInternalActivity()) {
                            startVerifyPassword(lockscreenCredential, intent, 1);
                            return;
                        }
                    } else if (!this.mForceVerifyPath) {
                        startCheckPassword(lockscreenCredential, intent);
                        return;
                    } else if (isInternalActivity()) {
                        startVerifyPassword(lockscreenCredential, intent, 0);
                        return;
                    }
                    this.mCredentialCheckResultTracker.setResult(false, intent, 0, this.mEffectiveUserId);
                }
            }
        }

        /* access modifiers changed from: private */
        public boolean isInternalActivity() {
            return getActivity() instanceof InternalActivity;
        }

        private void startVerifyPassword(LockscreenCredential lockscreenCredential, Intent intent, int i) {
            AsyncTask<?, ?, ?> asyncTask;
            int i2 = this.mEffectiveUserId;
            int i3 = this.mUserId;
            C1287x97dc6ec confirmLockPassword$ConfirmLockPasswordFragment$$ExternalSyntheticLambda5 = new C1287x97dc6ec(this, i, intent, i2);
            if (i2 == i3) {
                asyncTask = LockPatternChecker.verifyCredential(this.mLockPatternUtils, lockscreenCredential, i3, i, confirmLockPassword$ConfirmLockPasswordFragment$$ExternalSyntheticLambda5);
            } else {
                asyncTask = LockPatternChecker.verifyTiedProfileChallenge(this.mLockPatternUtils, lockscreenCredential, i3, i, confirmLockPassword$ConfirmLockPasswordFragment$$ExternalSyntheticLambda5);
            }
            this.mPendingLockCheck = asyncTask;
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$startVerifyPassword$4(int i, Intent intent, int i2, VerifyCredentialResponse verifyCredentialResponse, int i3) {
            this.mPendingLockCheck = null;
            boolean isMatched = verifyCredentialResponse.isMatched();
            if (isMatched && this.mReturnCredentials) {
                if ((i & 1) != 0) {
                    intent.putExtra("gk_pw_handle", verifyCredentialResponse.getGatekeeperPasswordHandle());
                } else {
                    intent.putExtra("hw_auth_token", verifyCredentialResponse.getGatekeeperHAT());
                }
            }
            this.mCredentialCheckResultTracker.setResult(isMatched, intent, i3, i2);
        }

        private void startCheckPassword(final LockscreenCredential lockscreenCredential, final Intent intent) {
            final int i = this.mEffectiveUserId;
            this.mPendingLockCheck = LockPatternChecker.checkCredential(this.mLockPatternUtils, lockscreenCredential, i, new LockPatternChecker.OnCheckCallback() {
                public void onChecked(boolean z, int i) {
                    ConfirmLockPasswordFragment.this.mPendingLockCheck = null;
                    if (z && ConfirmLockPasswordFragment.this.isInternalActivity()) {
                        ConfirmLockPasswordFragment confirmLockPasswordFragment = ConfirmLockPasswordFragment.this;
                        if (confirmLockPasswordFragment.mReturnCredentials) {
                            intent.putExtra("type", confirmLockPasswordFragment.mIsAlpha ? 0 : 3);
                            intent.putExtra("password", lockscreenCredential);
                        }
                    }
                    ConfirmLockPasswordFragment.this.mCredentialCheckResultTracker.setResult(z, intent, i, i);
                }
            });
        }

        private void startDisappearAnimation(Intent intent) {
            if (!this.mDisappearing) {
                this.mDisappearing = true;
                ConfirmLockPassword confirmLockPassword = (ConfirmLockPassword) getActivity();
                if (confirmLockPassword != null && !confirmLockPassword.isFinishing()) {
                    if (confirmLockPassword.getConfirmCredentialTheme() == ConfirmDeviceCredentialBaseActivity.ConfirmCredentialTheme.DARK) {
                        this.mDisappearAnimationUtils.startAnimation(getActiveViews(), new C1288x97dc6ed(confirmLockPassword, intent));
                        return;
                    }
                    confirmLockPassword.setResult(-1, intent);
                    confirmLockPassword.finish();
                }
            }
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ void lambda$startDisappearAnimation$5(ConfirmLockPassword confirmLockPassword, Intent intent) {
            confirmLockPassword.setResult(-1, intent);
            confirmLockPassword.finish();
            confirmLockPassword.overridePendingTransition(R$anim.confirm_credential_close_enter, R$anim.confirm_credential_close_exit);
        }

        private void onPasswordChecked(boolean z, Intent intent, int i, int i2, boolean z2) {
            this.mPasswordEntryInputDisabler.setInputEnabled(true);
            if (z) {
                if (z2) {
                    ConfirmDeviceCredentialUtils.reportSuccessfulAttempt(this.mLockPatternUtils, this.mUserManager, this.mDevicePolicyManager, this.mEffectiveUserId, true);
                }
                startDisappearAnimation(intent);
                ConfirmDeviceCredentialUtils.checkForPendingIntent(getActivity());
                return;
            }
            if (i > 0) {
                refreshLockScreen();
                handleAttemptLockout(this.mLockPatternUtils.setLockoutAttemptDeadline(i2, i));
            } else {
                showError(getErrorMessage(), 3000);
            }
            if (z2) {
                reportFailedAttempt();
            }
        }

        public void onCredentialChecked(boolean z, Intent intent, int i, int i2, boolean z2) {
            onPasswordChecked(z, intent, i, i2, z2);
        }

        /* access modifiers changed from: protected */
        public void onShowError() {
            this.mPasswordEntry.setText((CharSequence) null);
        }

        private void handleAttemptLockout(long j) {
            this.mCountdownTimer = new CountDownTimer(j - SystemClock.elapsedRealtime(), 1000) {
                public void onTick(long j) {
                    ConfirmLockPasswordFragment confirmLockPasswordFragment = ConfirmLockPasswordFragment.this;
                    confirmLockPasswordFragment.showError((CharSequence) confirmLockPasswordFragment.getString(R$string.lockpattern_too_many_failed_confirmation_attempts, Integer.valueOf((int) (j / 1000))), 0);
                }

                public void onFinish() {
                    ConfirmLockPasswordFragment.this.updatePasswordEntry();
                    ConfirmLockPasswordFragment.this.mErrorTextView.setText("");
                    ConfirmLockPasswordFragment confirmLockPasswordFragment = ConfirmLockPasswordFragment.this;
                    confirmLockPasswordFragment.updateErrorMessage(confirmLockPasswordFragment.mLockPatternUtils.getCurrentFailedPasswordAttempts(confirmLockPasswordFragment.mEffectiveUserId));
                }
            }.start();
            updatePasswordEntry();
        }

        public void onClick(View view) {
            if (view.getId() == R$id.next_button) {
                handleNext();
            } else if (view.getId() == R$id.cancel_button) {
                getActivity().setResult(0);
                getActivity().finish();
            }
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 0 && i != 6 && i != 5) {
                return false;
            }
            handleNext();
            return true;
        }
    }
}
