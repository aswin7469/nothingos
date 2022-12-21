package com.android.keyguard;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.UserHandle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.util.LatencyTracker;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.List;

public class KeyguardPasswordViewController extends KeyguardAbsKeyInputViewController<KeyguardPasswordView> {
    private static final int DELAY_MILLIS_TO_REEVALUATE_IME_SWITCH_ICON = 500;
    private final String TAG = "KeyguardPasswordViewController";
    private final InputMethodManager mInputMethodManager;
    /* access modifiers changed from: private */
    public final KeyguardSecurityCallback mKeyguardSecurityCallback;
    private final KeyguardViewController mKeyguardViewController;
    private final LockPatternUtils mLockPatternUtils;
    private final DelayableExecutor mMainExecutor;
    private final TextView.OnEditorActionListener mOnEditorActionListener = new KeyguardPasswordViewController$$ExternalSyntheticLambda0(this);
    private EditText mPasswordEntry;
    private boolean mPaused;
    private final boolean mShowImeAtScreenOn;
    private ImageView mSwitchImeButton;
    private final TextWatcher mTextWatcher = new TextWatcher() {
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            KeyguardPasswordViewController.this.mKeyguardSecurityCallback.userActivity();
        }

        public void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(editable)) {
                KeyguardPasswordViewController.this.onUserInput();
            }
        }
    };

    public boolean needsInput() {
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-keyguard-KeyguardPasswordViewController  reason: not valid java name */
    public /* synthetic */ boolean m2287lambda$new$0$comandroidkeyguardKeyguardPasswordViewController(TextView textView, int i, KeyEvent keyEvent) {
        boolean z = keyEvent == null && (i == 0 || i == 6 || i == 5);
        boolean z2 = keyEvent != null && KeyEvent.isConfirmKey(keyEvent.getKeyCode()) && keyEvent.getAction() == 0;
        if (!z && !z2) {
            return false;
        }
        verifyPasswordAndUnlock();
        return true;
    }

    public void reloadColors() {
        super.reloadColors();
        int defaultColor = Utils.getColorAttr(((KeyguardPasswordView) this.mView).getContext(), 16842806).getDefaultColor();
        this.mPasswordEntry.setTextColor(defaultColor);
        this.mPasswordEntry.setHighlightColor(defaultColor);
        this.mPasswordEntry.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        this.mPasswordEntry.setForegroundTintList(ColorStateList.valueOf(defaultColor));
        this.mSwitchImeButton.setImageTintList(ColorStateList.valueOf(defaultColor));
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    protected KeyguardPasswordViewController(KeyguardPasswordView keyguardPasswordView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, InputMethodManager inputMethodManager, EmergencyButtonController emergencyButtonController, @Main DelayableExecutor delayableExecutor, @Main Resources resources, FalsingCollector falsingCollector, KeyguardViewController keyguardViewController) {
        super(keyguardPasswordView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, falsingCollector, emergencyButtonController);
        this.mKeyguardSecurityCallback = keyguardSecurityCallback;
        this.mInputMethodManager = inputMethodManager;
        this.mMainExecutor = delayableExecutor;
        this.mKeyguardViewController = keyguardViewController;
        this.mShowImeAtScreenOn = resources.getBoolean(C1893R.bool.kg_show_ime_at_screen_on);
        this.mPasswordEntry = (EditText) ((KeyguardPasswordView) this.mView).findViewById(((KeyguardPasswordView) this.mView).getPasswordTextViewId());
        this.mSwitchImeButton = (ImageView) ((KeyguardPasswordView) this.mView).findViewById(C1893R.C1897id.switch_ime_button);
        this.mLockPatternUtils = lockPatternUtils;
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        super.onViewAttached();
        this.mPasswordEntry.setTextOperationUser(UserHandle.of(KeyguardUpdateMonitor.getCurrentUser()));
        this.mPasswordEntry.setKeyListener(TextKeyListener.getInstance());
        this.mPasswordEntry.setInputType(129);
        this.mPasswordEntry.setSelected(true);
        this.mPasswordEntry.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mPasswordEntry.addTextChangedListener(this.mTextWatcher);
        this.mPasswordEntry.setOnClickListener(new KeyguardPasswordViewController$$ExternalSyntheticLambda2(this));
        this.mSwitchImeButton.setOnClickListener(new KeyguardPasswordViewController$$ExternalSyntheticLambda3(this));
        View findViewById = ((KeyguardPasswordView) this.mView).findViewById(C1893R.C1897id.cancel_button);
        if (findViewById != null) {
            findViewById.setOnClickListener(new KeyguardPasswordViewController$$ExternalSyntheticLambda4(this));
        }
        updateSwitchImeButton();
        this.mMainExecutor.executeDelayed(new KeyguardPasswordViewController$$ExternalSyntheticLambda5(this), 500);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$1$com-android-keyguard-KeyguardPasswordViewController */
    public /* synthetic */ void mo25944x7d32d6d(View view) {
        this.mKeyguardSecurityCallback.userActivity();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$2$com-android-keyguard-KeyguardPasswordViewController */
    public /* synthetic */ void mo25945x950ddeee(View view) {
        this.mKeyguardSecurityCallback.userActivity();
        this.mInputMethodManager.showInputMethodPickerFromSystem(false, ((KeyguardPasswordView) this.mView).getContext().getDisplayId());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onViewAttached$3$com-android-keyguard-KeyguardPasswordViewController */
    public /* synthetic */ void mo25946x2248906f(View view) {
        this.mKeyguardSecurityCallback.reset();
        this.mKeyguardSecurityCallback.onCancelClicked();
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        super.onViewDetached();
        this.mPasswordEntry.setOnEditorActionListener((TextView.OnEditorActionListener) null);
    }

    /* access modifiers changed from: package-private */
    public void resetState() {
        this.mPasswordEntry.setTextOperationUser(UserHandle.of(KeyguardUpdateMonitor.getCurrentUser()));
        this.mMessageAreaController.setMessage((CharSequence) "");
        boolean isEnabled = this.mPasswordEntry.isEnabled();
        ((KeyguardPasswordView) this.mView).setPasswordEntryEnabled(true);
        ((KeyguardPasswordView) this.mView).setPasswordEntryInputEnabled(true);
        if (this.mResumed && this.mPasswordEntry.isVisibleToUser() && isEnabled) {
            showInput();
        }
    }

    public void onResume(int i) {
        super.onResume(i);
        this.mPaused = false;
        if (i != 1 || this.mShowImeAtScreenOn) {
            showInput();
        }
    }

    private void showInput() {
        if (this.mKeyguardViewController.isBouncerShowing()) {
            if (this.mLockPatternUtils.getLockoutAttemptDeadline(KeyguardUpdateMonitor.getCurrentUser()) > 0) {
                Log.d("KeyguardPasswordViewController", "lockout return");
            } else {
                ((KeyguardPasswordView) this.mView).post(new KeyguardPasswordViewController$$ExternalSyntheticLambda1(this));
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showInput$4$com-android-keyguard-KeyguardPasswordViewController */
    public /* synthetic */ void mo25947xbd13447b() {
        if (((KeyguardPasswordView) this.mView).isShown()) {
            this.mPasswordEntry.requestFocus();
            this.mPasswordEntry.getWindowInsetsController().show(WindowInsets.Type.ime());
        }
    }

    public void onPause() {
        if (!this.mPaused) {
            this.mPaused = true;
            if (!this.mPasswordEntry.isVisibleToUser()) {
                super.onPause();
            } else {
                ((KeyguardPasswordView) this.mView).setOnFinishImeAnimationRunnable(new KeyguardPasswordViewController$$ExternalSyntheticLambda6(this));
            }
            if (this.mPasswordEntry.isAttachedToWindow()) {
                this.mPasswordEntry.getWindowInsetsController().hide(WindowInsets.Type.ime());
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onPause$5$com-android-keyguard-KeyguardPasswordViewController */
    public /* synthetic */ void mo25943xe8e06fc6() {
        this.mPasswordEntry.clearFocus();
        super.onPause();
    }

    public void onStartingToHide() {
        if (this.mPasswordEntry.isAttachedToWindow()) {
            this.mPasswordEntry.getWindowInsetsController().hide(WindowInsets.Type.ime());
        }
    }

    /* access modifiers changed from: private */
    public void updateSwitchImeButton() {
        boolean z = this.mSwitchImeButton.getVisibility() == 0;
        boolean hasMultipleEnabledIMEsOrSubtypes = hasMultipleEnabledIMEsOrSubtypes(this.mInputMethodManager, false);
        if (z != hasMultipleEnabledIMEsOrSubtypes) {
            this.mSwitchImeButton.setVisibility(hasMultipleEnabledIMEsOrSubtypes ? 0 : 8);
        }
        if (this.mSwitchImeButton.getVisibility() != 0) {
            ViewGroup.LayoutParams layoutParams = this.mPasswordEntry.getLayoutParams();
            if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) layoutParams).setMarginStart(0);
                this.mPasswordEntry.setLayoutParams(layoutParams);
            }
        }
    }

    private boolean hasMultipleEnabledIMEsOrSubtypes(InputMethodManager inputMethodManager, boolean z) {
        int i = 0;
        for (InputMethodInfo inputMethodInfo : inputMethodManager.getEnabledInputMethodListAsUser(KeyguardUpdateMonitor.getCurrentUser())) {
            if (i > 1) {
                return true;
            }
            List<InputMethodSubtype> enabledInputMethodSubtypeList = inputMethodManager.getEnabledInputMethodSubtypeList(inputMethodInfo, true);
            if (!enabledInputMethodSubtypeList.isEmpty()) {
                int i2 = 0;
                for (InputMethodSubtype isAuxiliary : enabledInputMethodSubtypeList) {
                    if (isAuxiliary.isAuxiliary()) {
                        i2++;
                    }
                }
                if (enabledInputMethodSubtypeList.size() - i2 <= 0) {
                    if (z) {
                        if (i2 <= 1) {
                        }
                    }
                }
            }
            i++;
        }
        if (i > 1 || inputMethodManager.getEnabledInputMethodSubtypeList((InputMethodInfo) null, false).size() > 1) {
            return true;
        }
        return false;
    }
}
