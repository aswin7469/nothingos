package com.android.keyguard;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.UserHandle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.TextKeyListener;
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
import com.android.systemui.R$bool;
import com.android.systemui.R$color;
import com.android.systemui.R$id;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.List;
/* loaded from: classes.dex */
public class KeyguardPasswordViewController extends KeyguardAbsKeyInputViewController<KeyguardPasswordView> {
    private final InputMethodManager mInputMethodManager;
    private final KeyguardSecurityCallback mKeyguardSecurityCallback;
    private final DelayableExecutor mMainExecutor;
    private EditText mPasswordEntry;
    private final boolean mShowImeAtScreenOn;
    private final TextView.OnEditorActionListener mOnEditorActionListener = new TextView.OnEditorActionListener() { // from class: com.android.keyguard.KeyguardPasswordViewController$$ExternalSyntheticLambda3
        @Override // android.widget.TextView.OnEditorActionListener
        public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            boolean lambda$new$0;
            lambda$new$0 = KeyguardPasswordViewController.this.lambda$new$0(textView, i, keyEvent);
            return lambda$new$0;
        }
    };
    private final TextWatcher mTextWatcher = new TextWatcher() { // from class: com.android.keyguard.KeyguardPasswordViewController.1
        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            KeyguardPasswordViewController.this.mKeyguardSecurityCallback.userActivity();
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            if (!TextUtils.isEmpty(editable)) {
                KeyguardPasswordViewController.this.onUserInput();
            }
        }
    };
    private ImageView mSwitchImeButton = (ImageView) ((KeyguardPasswordView) this.mView).findViewById(R$id.switch_ime_button);

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardSecurityView
    public boolean needsInput() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(TextView textView, int i, KeyEvent keyEvent) {
        boolean z = keyEvent == null && (i == 0 || i == 6 || i == 5);
        boolean z2 = keyEvent != null && KeyEvent.isConfirmKey(keyEvent.getKeyCode()) && keyEvent.getAction() == 0;
        if (z || z2) {
            verifyPasswordAndUnlock();
            return true;
        }
        return false;
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController
    public void reloadColors() {
        super.reloadColors();
        int color = ((KeyguardPasswordView) this.mView).getContext().getColor(R$color.nothingDefaultTextColor);
        this.mPasswordEntry.setTextColor(color);
        this.mPasswordEntry.setHighlightColor(color);
        this.mPasswordEntry.setBackgroundTintList(ColorStateList.valueOf(color));
        this.mPasswordEntry.setForegroundTintList(ColorStateList.valueOf(color));
        this.mSwitchImeButton.setImageTintList(ColorStateList.valueOf(color));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public KeyguardPasswordViewController(KeyguardPasswordView keyguardPasswordView, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel.SecurityMode securityMode, LockPatternUtils lockPatternUtils, KeyguardSecurityCallback keyguardSecurityCallback, KeyguardMessageAreaController.Factory factory, LatencyTracker latencyTracker, InputMethodManager inputMethodManager, EmergencyButtonController emergencyButtonController, DelayableExecutor delayableExecutor, Resources resources, FalsingCollector falsingCollector) {
        super(keyguardPasswordView, keyguardUpdateMonitor, securityMode, lockPatternUtils, keyguardSecurityCallback, factory, latencyTracker, falsingCollector, emergencyButtonController);
        this.mKeyguardSecurityCallback = keyguardSecurityCallback;
        this.mInputMethodManager = inputMethodManager;
        this.mMainExecutor = delayableExecutor;
        this.mShowImeAtScreenOn = resources.getBoolean(R$bool.kg_show_ime_at_screen_on);
        T t = this.mView;
        this.mPasswordEntry = (EditText) ((KeyguardPasswordView) t).findViewById(((KeyguardPasswordView) t).getPasswordTextViewId());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onViewAttached() {
        super.onViewAttached();
        this.mPasswordEntry.setTextOperationUser(UserHandle.of(KeyguardUpdateMonitor.getCurrentUser()));
        this.mPasswordEntry.setKeyListener(TextKeyListener.getInstance());
        this.mPasswordEntry.setInputType(129);
        this.mPasswordEntry.setSelected(true);
        this.mPasswordEntry.setOnEditorActionListener(this.mOnEditorActionListener);
        this.mPasswordEntry.addTextChangedListener(this.mTextWatcher);
        this.mPasswordEntry.setOnClickListener(new View.OnClickListener() { // from class: com.android.keyguard.KeyguardPasswordViewController$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                KeyguardPasswordViewController.this.lambda$onViewAttached$1(view);
            }
        });
        this.mSwitchImeButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.keyguard.KeyguardPasswordViewController$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                KeyguardPasswordViewController.this.lambda$onViewAttached$2(view);
            }
        });
        View findViewById = ((KeyguardPasswordView) this.mView).findViewById(R$id.cancel_button);
        if (findViewById != null) {
            findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.android.keyguard.KeyguardPasswordViewController$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    KeyguardPasswordViewController.this.lambda$onViewAttached$3(view);
                }
            });
        }
        updateSwitchImeButton();
        this.mMainExecutor.executeDelayed(new Runnable() { // from class: com.android.keyguard.KeyguardPasswordViewController$$ExternalSyntheticLambda4
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardPasswordViewController.this.updateSwitchImeButton();
            }
        }, 500L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewAttached$1(View view) {
        this.mKeyguardSecurityCallback.userActivity();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewAttached$2(View view) {
        this.mKeyguardSecurityCallback.userActivity();
        this.mInputMethodManager.showInputMethodPickerFromSystem(false, ((KeyguardPasswordView) this.mView).getContext().getDisplayId());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewAttached$3(View view) {
        this.mKeyguardSecurityCallback.reset();
        this.mKeyguardSecurityCallback.onCancelClicked();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.keyguard.KeyguardInputViewController, com.android.systemui.util.ViewController
    public void onViewDetached() {
        super.onViewDetached();
        this.mPasswordEntry.setOnEditorActionListener(null);
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController
    void resetState() {
        this.mPasswordEntry.setTextOperationUser(UserHandle.of(KeyguardUpdateMonitor.getCurrentUser()));
        this.mMessageAreaController.setMessage("");
        boolean isEnabled = this.mPasswordEntry.isEnabled();
        ((KeyguardPasswordView) this.mView).setPasswordEntryEnabled(true);
        ((KeyguardPasswordView) this.mView).setPasswordEntryInputEnabled(true);
        if (!this.mResumed || !this.mPasswordEntry.isVisibleToUser() || !isEnabled) {
            return;
        }
        showInput();
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController
    public void onResume(int i) {
        super.onResume(i);
        if (i != 1 || this.mShowImeAtScreenOn) {
            showInput();
        }
    }

    private void showInput() {
        ((KeyguardPasswordView) this.mView).post(new Runnable() { // from class: com.android.keyguard.KeyguardPasswordViewController$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardPasswordViewController.this.lambda$showInput$4();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$showInput$4() {
        if (((KeyguardPasswordView) this.mView).isShown()) {
            this.mPasswordEntry.requestFocus();
            this.mInputMethodManager.showSoftInput(this.mPasswordEntry, 1);
        }
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputViewController, com.android.keyguard.KeyguardInputViewController
    public void onPause() {
        if (!this.mPasswordEntry.isVisibleToUser()) {
            super.onPause();
        } else {
            ((KeyguardPasswordView) this.mView).setOnFinishImeAnimationRunnable(new Runnable() { // from class: com.android.keyguard.KeyguardPasswordViewController$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardPasswordViewController.this.lambda$onPause$5();
                }
            });
        }
        if (this.mPasswordEntry.isAttachedToWindow()) {
            this.mPasswordEntry.getWindowInsetsController().hide(WindowInsets.Type.ime());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onPause$5() {
        this.mPasswordEntry.clearFocus();
        super.onPause();
    }

    @Override // com.android.keyguard.KeyguardSecurityView
    public void onStartingToHide() {
        if (this.mPasswordEntry.isAttachedToWindow()) {
            this.mPasswordEntry.getWindowInsetsController().hide(WindowInsets.Type.ime());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateSwitchImeButton() {
        boolean z = this.mSwitchImeButton.getVisibility() == 0;
        boolean hasMultipleEnabledIMEsOrSubtypes = hasMultipleEnabledIMEsOrSubtypes(this.mInputMethodManager, false);
        if (z != hasMultipleEnabledIMEsOrSubtypes) {
            this.mSwitchImeButton.setVisibility(hasMultipleEnabledIMEsOrSubtypes ? 0 : 8);
        }
        this.mSwitchImeButton.setVisibility(8);
        if (this.mSwitchImeButton.getVisibility() != 0) {
            ViewGroup.LayoutParams layoutParams = this.mPasswordEntry.getLayoutParams();
            if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
                return;
            }
            ((ViewGroup.MarginLayoutParams) layoutParams).setMarginStart(0);
            this.mPasswordEntry.setLayoutParams(layoutParams);
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
                for (InputMethodSubtype inputMethodSubtype : enabledInputMethodSubtypeList) {
                    if (inputMethodSubtype.isAuxiliary()) {
                        i2++;
                    }
                }
                if (enabledInputMethodSubtypeList.size() - i2 <= 0) {
                    if (z && i2 > 1) {
                    }
                }
            }
            i++;
        }
        return i > 1 || inputMethodManager.getEnabledInputMethodSubtypeList(null, false).size() > 1;
    }
}
