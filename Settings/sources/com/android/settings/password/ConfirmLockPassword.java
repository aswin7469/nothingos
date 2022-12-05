package com.android.settings.password;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.SystemClock;
import android.os.UserManager;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Slog;
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
import com.android.settings.R;
import com.android.settings.password.ConfirmDeviceCredentialBaseActivity;
import com.android.settings.password.ConfirmLockPassword;
import com.android.settings.password.CredentialCheckResultTracker;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import com.google.android.setupdesign.GlifLayout;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class ConfirmLockPassword extends ConfirmDeviceCredentialBaseActivity {
    private static final int[] DETAIL_TEXTS = {R.string.lockpassword_confirm_your_pin_generic, R.string.lockpassword_confirm_your_password_generic, R.string.lockpassword_confirm_your_pin_generic_profile, R.string.lockpassword_confirm_your_password_generic_profile, R.string.lockpassword_strong_auth_required_device_pin, R.string.lockpassword_strong_auth_required_device_password, R.string.lockpassword_strong_auth_required_work_pin, R.string.lockpassword_strong_auth_required_work_password};

    /* loaded from: classes.dex */
    public static class InternalActivity extends ConfirmLockPassword {
    }

    @Override // com.android.settings.SettingsActivity, android.app.Activity
    public Intent getIntent() {
        Intent intent = new Intent(super.getIntent());
        intent.putExtra(":settings:show_fragment", ConfirmLockPasswordFragment.class.getName());
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.settings.SettingsActivity
    public boolean isValidFragment(String str) {
        return ConfirmLockPasswordFragment.class.getName().equals(str);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        Fragment findFragmentById = getSupportFragmentManager().findFragmentById(R.id.main_content);
        if (findFragmentById == null || !(findFragmentById instanceof ConfirmLockPasswordFragment)) {
            return;
        }
        ((ConfirmLockPasswordFragment) findFragmentById).onWindowFocusChanged(z);
    }

    /* loaded from: classes.dex */
    public static class ConfirmLockPasswordFragment extends ConfirmDeviceCredentialBaseFragment implements View.OnClickListener, TextView.OnEditorActionListener, CredentialCheckResultTracker.Listener {
        private AppearAnimationUtils mAppearAnimationUtils;
        private CountDownTimer mCountdownTimer;
        private CredentialCheckResultTracker mCredentialCheckResultTracker;
        private DisappearAnimationUtils mDisappearAnimationUtils;
        private boolean mDisappearing = false;
        private GlifLayout mGlifLayout;
        private InputMethodManager mImm;
        private boolean mIsAlpha;
        private boolean mIsManagedProfile;
        private ImeAwareEditText mPasswordEntry;
        private TextViewInputDisabler mPasswordEntryInputDisabler;
        private AsyncTask<?, ?, ?> mPendingLockCheck;

        @Override // com.android.settingslib.core.instrumentation.Instrumentable
        public int getMetricsCategory() {
            return 30;
        }

        @Override // androidx.fragment.app.Fragment
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            int i;
            int keyguardStoredPasswordQuality = this.mLockPatternUtils.getKeyguardStoredPasswordQuality(this.mEffectiveUserId);
            if (((ConfirmLockPassword) getActivity()).getConfirmCredentialTheme() == ConfirmDeviceCredentialBaseActivity.ConfirmCredentialTheme.NORMAL) {
                i = R.layout.confirm_lock_password_normal;
            } else {
                i = R.layout.confirm_lock_password;
            }
            View inflate = layoutInflater.inflate(i, viewGroup, false);
            this.mGlifLayout = (GlifLayout) inflate.findViewById(R.id.setup_wizard_layout);
            inflate.findViewById(R.id.sud_layout_header).setVisibility(8);
            ImeAwareEditText findViewById = inflate.findViewById(R.id.password_entry);
            this.mPasswordEntry = findViewById;
            findViewById.setOnEditorActionListener(this);
            this.mPasswordEntry.requestFocus();
            this.mPasswordEntryInputDisabler = new TextViewInputDisabler(this.mPasswordEntry);
            this.mErrorTextView = (TextView) inflate.findViewById(R.id.errorText);
            this.mIsAlpha = 262144 == keyguardStoredPasswordQuality || 327680 == keyguardStoredPasswordQuality || 393216 == keyguardStoredPasswordQuality || 524288 == keyguardStoredPasswordQuality;
            this.mImm = (InputMethodManager) getActivity().getSystemService("input_method");
            this.mIsManagedProfile = UserManager.get(getActivity()).isManagedProfile(this.mEffectiveUserId);
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                CharSequence charSequenceExtra = intent.getCharSequenceExtra("com.android.settings.ConfirmCredentials.header");
                CharSequence charSequenceExtra2 = intent.getCharSequenceExtra("com.android.settings.ConfirmCredentials.details");
                if (TextUtils.isEmpty(charSequenceExtra) && this.mIsManagedProfile) {
                    charSequenceExtra = this.mDevicePolicyManager.getOrganizationNameForUser(this.mUserId);
                }
                if (TextUtils.isEmpty(charSequenceExtra)) {
                    charSequenceExtra = getString(getDefaultHeader());
                }
                if (TextUtils.isEmpty(charSequenceExtra2)) {
                    charSequenceExtra2 = getString(getDefaultDetails());
                }
                this.mGlifLayout.setHeaderText(charSequenceExtra);
                this.mGlifLayout.setDescriptionText(charSequenceExtra2);
                TextView textView = (TextView) inflate.findViewById(R.id.title);
                if (textView != null) {
                    textView.setText(charSequenceExtra);
                }
                TextView textView2 = (TextView) inflate.findViewById(R.id.summary);
                if (textView2 != null) {
                    textView2.setText(charSequenceExtra2);
                }
            }
            int inputType = this.mPasswordEntry.getInputType();
            if (this.mIsAlpha) {
                this.mPasswordEntry.setInputType(inputType);
                this.mPasswordEntry.setContentDescription(getContext().getString(R.string.unlock_set_unlock_password_title));
            } else {
                this.mPasswordEntry.setInputType(18);
                this.mPasswordEntry.setContentDescription(getContext().getString(R.string.unlock_set_unlock_pin_title));
            }
            this.mPasswordEntry.setTypeface(Typeface.create(getContext().getString(17039948), 0));
            this.mAppearAnimationUtils = new AppearAnimationUtils(getContext(), 220L, 2.0f, 1.0f, AnimationUtils.loadInterpolator(getContext(), 17563662));
            this.mDisappearAnimationUtils = new DisappearAnimationUtils(getContext(), 110L, 1.0f, 0.5f, AnimationUtils.loadInterpolator(getContext(), 17563663));
            setAccessibilityTitle(this.mGlifLayout.getHeaderText());
            CredentialCheckResultTracker credentialCheckResultTracker = (CredentialCheckResultTracker) getFragmentManager().findFragmentByTag("check_lock_result");
            this.mCredentialCheckResultTracker = credentialCheckResultTracker;
            if (credentialCheckResultTracker == null) {
                this.mCredentialCheckResultTracker = new CredentialCheckResultTracker();
                getFragmentManager().beginTransaction().add(this.mCredentialCheckResultTracker, "check_lock_result").commit();
            }
            return inflate;
        }

        @Override // com.android.settings.password.ConfirmDeviceCredentialBaseFragment, androidx.fragment.app.Fragment
        public void onViewCreated(View view, Bundle bundle) {
            int i;
            super.onViewCreated(view, bundle);
            Button button = this.mForgotButton;
            if (button != null) {
                if (this.mIsAlpha) {
                    i = R.string.lockpassword_forgot_password;
                } else {
                    i = R.string.lockpassword_forgot_pin;
                }
                button.setText(i);
            }
        }

        @Override // com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            this.mPasswordEntry.setText((CharSequence) null);
            new Handler(Looper.myLooper()).postDelayed(ConfirmLockPassword$ConfirmLockPasswordFragment$$ExternalSyntheticLambda3.INSTANCE, 5000L);
        }

        public static /* synthetic */ void lambda$onDestroy$0() {
            System.gc();
            System.runFinalization();
            System.gc();
        }

        private int getDefaultHeader() {
            return this.mFrp ? this.mIsAlpha ? R.string.lockpassword_confirm_your_password_header_frp : R.string.lockpassword_confirm_your_pin_header_frp : this.mIsManagedProfile ? this.mIsAlpha ? R.string.lockpassword_confirm_your_work_password_header : R.string.lockpassword_confirm_your_work_pin_header : this.mIsAlpha ? R.string.lockpassword_confirm_your_password_header : R.string.lockpassword_confirm_your_pin_header;
        }

        private int getDefaultDetails() {
            if (this.mFrp) {
                return this.mIsAlpha ? R.string.lockpassword_confirm_your_password_details_frp : R.string.lockpassword_confirm_your_pin_details_frp;
            }
            return ConfirmLockPassword.DETAIL_TEXTS[((isStrongAuthRequired() ? 1 : 0) << 2) + ((this.mIsManagedProfile ? 1 : 0) << 1) + (this.mIsAlpha ? 1 : 0)];
        }

        private int getErrorMessage() {
            return this.mIsAlpha ? R.string.lockpassword_invalid_password : R.string.lockpassword_invalid_pin;
        }

        @Override // com.android.settings.password.ConfirmDeviceCredentialBaseFragment
        protected int getLastTryErrorMessage(int i) {
            if (i == 1) {
                return this.mIsAlpha ? R.string.lock_last_password_attempt_before_wipe_device : R.string.lock_last_pin_attempt_before_wipe_device;
            } else if (i == 2) {
                return this.mIsAlpha ? R.string.lock_last_password_attempt_before_wipe_profile : R.string.lock_last_pin_attempt_before_wipe_profile;
            } else if (i == 3) {
                return this.mIsAlpha ? R.string.lock_last_password_attempt_before_wipe_user : R.string.lock_last_pin_attempt_before_wipe_user;
            } else {
                throw new IllegalArgumentException("Unrecognized user type:" + i);
            }
        }

        @Override // com.android.settings.password.ConfirmDeviceCredentialBaseFragment
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

        @Override // com.android.settings.password.ConfirmDeviceCredentialBaseFragment
        public void startEnterAnimation() {
            super.startEnterAnimation();
            this.mAppearAnimationUtils.startAnimation(getActiveViews(), new ConfirmLockPassword$ConfirmLockPasswordFragment$$ExternalSyntheticLambda1(this));
        }

        @Override // com.android.settings.password.ConfirmDeviceCredentialBaseFragment, com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
        public void onPause() {
            super.onPause();
            CountDownTimer countDownTimer = this.mCountdownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
                this.mCountdownTimer = null;
            }
            this.mCredentialCheckResultTracker.setListener(null);
        }

        @Override // com.android.settings.password.ConfirmDeviceCredentialBaseFragment, com.android.settings.core.InstrumentedFragment, com.android.settingslib.core.lifecycle.ObservableFragment, androidx.fragment.app.Fragment
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

        public void updatePasswordEntry() {
            boolean z = this.mLockPatternUtils.getLockoutAttemptDeadline(this.mEffectiveUserId) != 0;
            this.mPasswordEntry.setEnabled(!z);
            this.mPasswordEntryInputDisabler.setInputEnabled(!z);
            if (z) {
                this.mImm.hideSoftInputFromWindow(this.mPasswordEntry.getWindowToken(), 0);
            } else {
                this.mPasswordEntry.scheduleShowSoftInput();
            }
        }

        public void onWindowFocusChanged(boolean z) {
            if (!z) {
                return;
            }
            this.mPasswordEntry.post(new ConfirmLockPassword$ConfirmLockPasswordFragment$$ExternalSyntheticLambda1(this));
        }

        private void handleNext() {
            String str = ConfirmDeviceCredentialBaseFragment.TAG;
            Slog.i(str, "handleNext");
            if (this.mPendingLockCheck != null || this.mDisappearing) {
                StringBuilder sb = new StringBuilder();
                sb.append("mPendingLockCheck");
                sb.append(this.mPendingLockCheck != null ? " != null" : " == null");
                Slog.i(str, sb.toString());
                Slog.i(str, "mDisappearing:" + this.mDisappearing);
                return;
            }
            Editable text = this.mPasswordEntry.getText();
            if (TextUtils.isEmpty(text)) {
                Slog.i(str, "passwordText is empty");
                return;
            }
            LockscreenCredential createPassword = this.mIsAlpha ? LockscreenCredential.createPassword(text) : LockscreenCredential.createPin(text);
            this.mPasswordEntryInputDisabler.setInputEnabled(false);
            Slog.i(str, "mReturnGatekeeperPassword:" + this.mReturnGatekeeperPassword + ",mForceVerifyPath:" + this.mForceVerifyPath + ",isInternalActivity():" + isInternalActivity());
            Intent intent = new Intent();
            if (this.mReturnGatekeeperPassword) {
                if (isInternalActivity()) {
                    startVerifyPassword(createPassword, intent, 1);
                    return;
                }
            } else if (this.mForceVerifyPath) {
                if (isInternalActivity()) {
                    startVerifyPassword(createPassword, intent, 0);
                    return;
                }
            } else {
                startCheckPassword(createPassword, intent);
                return;
            }
            this.mCredentialCheckResultTracker.setResult(false, intent, 0, this.mEffectiveUserId);
        }

        public boolean isInternalActivity() {
            return getActivity() instanceof InternalActivity;
        }

        private void startVerifyPassword(LockscreenCredential lockscreenCredential, final Intent intent, final int i) {
            AsyncTask<?, ?, ?> verifyTiedProfileChallenge;
            final int i2 = this.mEffectiveUserId;
            int i3 = this.mUserId;
            LockPatternChecker.OnVerifyCallback onVerifyCallback = new LockPatternChecker.OnVerifyCallback() { // from class: com.android.settings.password.ConfirmLockPassword$ConfirmLockPasswordFragment$$ExternalSyntheticLambda0
                public final void onVerified(VerifyCredentialResponse verifyCredentialResponse, int i4) {
                    ConfirmLockPassword.ConfirmLockPasswordFragment.this.lambda$startVerifyPassword$1(i, intent, i2, verifyCredentialResponse, i4);
                }
            };
            if (i2 == i3) {
                verifyTiedProfileChallenge = LockPatternChecker.verifyCredential(this.mLockPatternUtils, lockscreenCredential, i3, i, onVerifyCallback);
            } else {
                verifyTiedProfileChallenge = LockPatternChecker.verifyTiedProfileChallenge(this.mLockPatternUtils, lockscreenCredential, i3, i, onVerifyCallback);
            }
            this.mPendingLockCheck = verifyTiedProfileChallenge;
        }

        public /* synthetic */ void lambda$startVerifyPassword$1(int i, Intent intent, int i2, VerifyCredentialResponse verifyCredentialResponse, int i3) {
            this.mPendingLockCheck = null;
            boolean isMatched = verifyCredentialResponse.isMatched();
            String str = ConfirmDeviceCredentialBaseFragment.TAG;
            Slog.i(str, "onVerified, matched:" + isMatched);
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
            this.mPendingLockCheck = LockPatternChecker.checkCredential(this.mLockPatternUtils, lockscreenCredential, i, new LockPatternChecker.OnCheckCallback() { // from class: com.android.settings.password.ConfirmLockPassword.ConfirmLockPasswordFragment.1
                {
                    ConfirmLockPasswordFragment.this = this;
                }

                public void onChecked(boolean z, int i2) {
                    ConfirmLockPasswordFragment.this.mPendingLockCheck = null;
                    if (z && ConfirmLockPasswordFragment.this.isInternalActivity()) {
                        ConfirmLockPasswordFragment confirmLockPasswordFragment = ConfirmLockPasswordFragment.this;
                        if (confirmLockPasswordFragment.mReturnCredentials) {
                            intent.putExtra("type", confirmLockPasswordFragment.mIsAlpha ? 0 : 3);
                            intent.putExtra("password", (Parcelable) lockscreenCredential);
                        }
                    }
                    ConfirmLockPasswordFragment.this.mCredentialCheckResultTracker.setResult(z, intent, i2, i);
                }
            });
        }

        private void startDisappearAnimation(final Intent intent) {
            if (this.mDisappearing) {
                return;
            }
            this.mDisappearing = true;
            final ConfirmLockPassword confirmLockPassword = (ConfirmLockPassword) getActivity();
            if (confirmLockPassword == null || confirmLockPassword.isFinishing()) {
                return;
            }
            if (confirmLockPassword.getConfirmCredentialTheme() == ConfirmDeviceCredentialBaseActivity.ConfirmCredentialTheme.DARK) {
                this.mDisappearAnimationUtils.startAnimation(getActiveViews(), new Runnable() { // from class: com.android.settings.password.ConfirmLockPassword$ConfirmLockPasswordFragment$$ExternalSyntheticLambda2
                    @Override // java.lang.Runnable
                    public final void run() {
                        ConfirmLockPassword.ConfirmLockPasswordFragment.lambda$startDisappearAnimation$2(ConfirmLockPassword.this, intent);
                    }
                });
                return;
            }
            confirmLockPassword.setResult(-1, intent);
            confirmLockPassword.finish();
        }

        public static /* synthetic */ void lambda$startDisappearAnimation$2(ConfirmLockPassword confirmLockPassword, Intent intent) {
            confirmLockPassword.setResult(-1, intent);
            confirmLockPassword.finish();
            confirmLockPassword.overridePendingTransition(R.anim.confirm_credential_close_enter, R.anim.confirm_credential_close_exit);
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
                showError(getErrorMessage(), 3000L);
            }
            if (!z2) {
                return;
            }
            reportFailedAttempt();
        }

        @Override // com.android.settings.password.CredentialCheckResultTracker.Listener
        public void onCredentialChecked(boolean z, Intent intent, int i, int i2, boolean z2) {
            onPasswordChecked(z, intent, i, i2, z2);
        }

        @Override // com.android.settings.password.ConfirmDeviceCredentialBaseFragment
        protected void onShowError() {
            this.mPasswordEntry.setText((CharSequence) null);
        }

        private void handleAttemptLockout(long j) {
            this.mCountdownTimer = new CountDownTimer(j - SystemClock.elapsedRealtime(), 1000L) { // from class: com.android.settings.password.ConfirmLockPassword.ConfirmLockPasswordFragment.2
                {
                    ConfirmLockPasswordFragment.this = this;
                }

                @Override // android.os.CountDownTimer
                public void onTick(long j2) {
                    ConfirmLockPasswordFragment confirmLockPasswordFragment = ConfirmLockPasswordFragment.this;
                    confirmLockPasswordFragment.showError(confirmLockPasswordFragment.getString(R.string.lockpattern_too_many_failed_confirmation_attempts, Integer.valueOf((int) (j2 / 1000))), 0L);
                }

                @Override // android.os.CountDownTimer
                public void onFinish() {
                    ConfirmLockPasswordFragment.this.updatePasswordEntry();
                    ConfirmLockPasswordFragment.this.mErrorTextView.setText("");
                    ConfirmLockPasswordFragment confirmLockPasswordFragment = ConfirmLockPasswordFragment.this;
                    confirmLockPasswordFragment.updateErrorMessage(confirmLockPasswordFragment.mLockPatternUtils.getCurrentFailedPasswordAttempts(confirmLockPasswordFragment.mEffectiveUserId));
                }
            }.start();
            updatePasswordEntry();
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getId() == R.id.next_button) {
                handleNext();
            } else if (view.getId() != R.id.cancel_button) {
            } else {
                getActivity().setResult(0);
                getActivity().finish();
            }
        }

        @Override // android.widget.TextView.OnEditorActionListener
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == 0 || i == 6 || i == 5) {
                handleNext();
                return true;
            }
            return false;
        }
    }
}
