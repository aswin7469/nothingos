package com.android.systemui.statusbar.policy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.ClipData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.UserHandle;
import android.text.Editable;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.util.ArraySet;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.ContentInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.OnReceiveContentListener;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.WindowInsetsController;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.internal.graphics.ColorUtils;
import com.android.internal.logging.UiEventLogger;
import com.android.p019wm.shell.animation.Interpolators;
import com.android.systemui.C1893R;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper;
import com.android.systemui.statusbar.phone.LightBarController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class RemoteInputView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = "RemoteInput";
    public static final Object VIEW_TAG = new Object();
    private boolean mColorized;
    private GradientDrawable mContentBackground;
    /* access modifiers changed from: private */
    public RemoteInputController mController;
    private ImageView mDelete;
    private ImageView mDeleteBg;
    /* access modifiers changed from: private */
    public RemoteEditText mEditText;
    private final ArrayList<View.OnFocusChangeListener> mEditTextFocusChangeListeners = new ArrayList<>();
    private final TextView.OnEditorActionListener mEditorActionHandler = new EditorActionHandler();
    /* access modifiers changed from: private */
    public NotificationEntry mEntry;
    private final ArrayList<Runnable> mOnSendListeners = new ArrayList<>();
    private final ArrayList<Consumer<Boolean>> mOnVisibilityChangedListeners = new ArrayList<>();
    private ProgressBar mProgressBar;
    /* access modifiers changed from: private */
    public boolean mRemoved;
    private boolean mResetting;
    private RevealParams mRevealParams;
    private ImageButton mSendButton;
    private boolean mSending;
    private final SendButtonTextWatcher mTextWatcher = new SendButtonTextWatcher();
    private int mTint;
    public final Object mToken = new Object();
    private final UiEventLogger mUiEventLogger = ((UiEventLogger) Dependency.get(UiEventLogger.class));
    private RemoteInputViewController mViewController;
    /* access modifiers changed from: private */
    public NotificationViewWrapper mWrapper;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    enum NotificationRemoteInputEvent implements UiEventLogger.UiEventEnum {
        NOTIFICATION_REMOTE_INPUT_OPEN(795),
        NOTIFICATION_REMOTE_INPUT_CLOSE(796),
        NOTIFICATION_REMOTE_INPUT_SEND(797),
        NOTIFICATION_REMOTE_INPUT_FAILURE(798),
        NOTIFICATION_REMOTE_INPUT_ATTACH_IMAGE(825);
        
        private final int mId;

        private NotificationRemoteInputEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public RemoteInputView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{16843829, 17956909});
        this.mTint = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
    }

    public void hideIme() {
        this.mEditText.hideIme();
    }

    private ColorStateList colorStateListWithDisabledAlpha(int i, int i2) {
        return new ColorStateList(new int[][]{new int[]{-16842910}, new int[0]}, new int[]{ColorUtils.setAlphaComponent(i, i2), i});
    }

    public void setBackgroundTintColor(int i, boolean z) {
        ColorStateList colorStateList;
        int i2;
        ColorStateList colorStateList2;
        int i3;
        int i4;
        int i5;
        if (z != this.mColorized || i != this.mTint) {
            this.mColorized = z;
            this.mTint = i;
            int dimensionPixelSize = z ? this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.remote_input_view_text_stroke) : 0;
            if (z) {
                boolean isColorDark = Notification.Builder.isColorDark(i);
                int i6 = -1;
                int i7 = isColorDark ? -1 : -16777216;
                if (isColorDark) {
                    i6 = -16777216;
                }
                colorStateList = colorStateListWithDisabledAlpha(i7, 77);
                colorStateList2 = colorStateListWithDisabledAlpha(i7, 153);
                i4 = ColorUtils.setAlphaComponent(i7, 153);
                i2 = i7;
                i3 = i6;
                i5 = i;
            } else {
                colorStateList = this.mContext.getColorStateList(C1893R.C1894color.remote_input_send);
                colorStateList2 = this.mContext.getColorStateList(C1893R.C1894color.remote_input_text);
                i4 = this.mContext.getColor(C1893R.C1894color.remote_input_hint);
                i3 = colorStateList2.getDefaultColor();
                TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(new int[]{17956911, 17956912});
                try {
                    i5 = obtainStyledAttributes.getColor(0, i);
                    i2 = obtainStyledAttributes.getColor(1, -7829368);
                    if (obtainStyledAttributes != null) {
                        obtainStyledAttributes.close();
                    }
                } catch (Throwable th) {
                    th.addSuppressed(th);
                }
            }
            this.mEditText.setTextColor(colorStateList2);
            this.mEditText.setHintTextColor(i4);
            this.mEditText.getTextCursorDrawable().setColorFilter(colorStateList.getDefaultColor(), PorterDuff.Mode.SRC_IN);
            this.mContentBackground.setColor(i5);
            this.mContentBackground.setStroke(dimensionPixelSize, colorStateList);
            this.mDelete.setImageTintList(ColorStateList.valueOf(i3));
            this.mDeleteBg.setImageTintList(ColorStateList.valueOf(i2));
            this.mSendButton.setImageTintList(colorStateList);
            this.mProgressBar.setProgressTintList(colorStateList);
            this.mProgressBar.setIndeterminateTintList(colorStateList);
            this.mProgressBar.setSecondaryProgressTintList(colorStateList);
            setBackgroundColor(i);
            return;
        }
        return;
        throw th;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mProgressBar = (ProgressBar) findViewById(C1893R.C1897id.remote_input_progress);
        ImageButton imageButton = (ImageButton) findViewById(C1893R.C1897id.remote_input_send);
        this.mSendButton = imageButton;
        imageButton.setOnClickListener(this);
        this.mContentBackground = (GradientDrawable) this.mContext.getDrawable(C1893R.C1895drawable.remote_input_view_text_bg).mutate();
        this.mDelete = (ImageView) findViewById(C1893R.C1897id.remote_input_delete);
        ImageView imageView = (ImageView) findViewById(C1893R.C1897id.remote_input_delete_bg);
        this.mDeleteBg = imageView;
        imageView.setImageTintBlendMode(BlendMode.SRC_IN);
        this.mDelete.setImageTintBlendMode(BlendMode.SRC_IN);
        this.mDelete.setOnClickListener(new RemoteInputView$$ExternalSyntheticLambda0(this));
        ((LinearLayout) findViewById(C1893R.C1897id.remote_input_content)).setBackground(this.mContentBackground);
        RemoteEditText remoteEditText = (RemoteEditText) findViewById(C1893R.C1897id.remote_input_text);
        this.mEditText = remoteEditText;
        remoteEditText.setInnerFocusable(false);
        this.mEditText.setEnabled(false);
        this.mEditText.setWindowInsetsAnimationCallback(new WindowInsetsAnimation.Callback(0) {
            public WindowInsets onProgress(WindowInsets windowInsets, List<WindowInsetsAnimation> list) {
                return windowInsets;
            }

            public void onEnd(WindowInsetsAnimation windowInsetsAnimation) {
                RemoteInputView.super.onEnd(windowInsetsAnimation);
                if (windowInsetsAnimation.getTypeMask() == WindowInsets.Type.ime()) {
                    boolean z = false;
                    RemoteInputView.this.mEntry.mRemoteEditImeAnimatingAway = false;
                    WindowInsets rootWindowInsets = RemoteInputView.this.mEditText.getRootWindowInsets();
                    if (rootWindowInsets == null) {
                        Log.w(RemoteInputView.TAG, "onEnd called on detached view", new Exception());
                    }
                    NotificationEntry access$300 = RemoteInputView.this.mEntry;
                    if (rootWindowInsets != null && rootWindowInsets.isVisible(WindowInsets.Type.ime())) {
                        z = true;
                    }
                    access$300.mRemoteEditImeVisible = z;
                    if (!RemoteInputView.this.mEntry.mRemoteEditImeVisible && !RemoteInputView.this.mEditText.mShowImeOnInputConnection) {
                        RemoteInputView.this.mController.removeRemoteInput(RemoteInputView.this.mEntry, RemoteInputView.this.mToken);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$onFinishInflate$0$com-android-systemui-statusbar-policy-RemoteInputView */
    public /* synthetic */ void mo45959xd8e6eb5a(View view) {
        setAttachment((ContentInfo) null);
    }

    @Deprecated
    public void setController(RemoteInputViewController remoteInputViewController) {
        this.mViewController = remoteInputViewController;
    }

    @Deprecated
    public RemoteInputViewController getController() {
        return this.mViewController;
    }

    public void clearAttachment() {
        setAttachment((ContentInfo) null);
    }

    /* access modifiers changed from: protected */
    public void setAttachment(ContentInfo contentInfo) {
        if (!(this.mEntry.remoteInputAttachment == null || this.mEntry.remoteInputAttachment == contentInfo)) {
            this.mEntry.remoteInputAttachment.releasePermissions();
        }
        this.mEntry.remoteInputAttachment = contentInfo;
        if (contentInfo != null) {
            this.mEntry.remoteInputUri = contentInfo.getClip().getItemAt(0).getUri();
            this.mEntry.remoteInputMimeType = contentInfo.getClip().getDescription().getMimeType(0);
        }
        View findViewById = findViewById(C1893R.C1897id.remote_input_content_container);
        ImageView imageView = (ImageView) findViewById(C1893R.C1897id.remote_input_attachment_image);
        imageView.setImageDrawable((Drawable) null);
        if (contentInfo == null) {
            findViewById.setVisibility(8);
            return;
        }
        imageView.setImageURI(contentInfo.getClip().getItemAt(0).getUri());
        if (imageView.getDrawable() == null) {
            findViewById.setVisibility(8);
        } else {
            findViewById.setVisibility(0);
            this.mUiEventLogger.logWithInstanceId(NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_ATTACH_IMAGE, this.mEntry.getSbn().getUid(), this.mEntry.getSbn().getPackageName(), this.mEntry.getSbn().getInstanceId());
        }
        updateSendButton();
    }

    public void startSending() {
        this.mEditText.setEnabled(false);
        this.mSending = true;
        this.mSendButton.setVisibility(4);
        this.mProgressBar.setVisibility(0);
        this.mEditText.mShowImeOnInputConnection = false;
    }

    /* access modifiers changed from: private */
    public void sendRemoteInput() {
        Iterator it = new ArrayList(this.mOnSendListeners).iterator();
        while (it.hasNext()) {
            ((Runnable) it.next()).run();
        }
    }

    public CharSequence getText() {
        return this.mEditText.getText();
    }

    public static RemoteInputView inflate(Context context, ViewGroup viewGroup, NotificationEntry notificationEntry, RemoteInputController remoteInputController) {
        RemoteInputView remoteInputView = (RemoteInputView) LayoutInflater.from(context).inflate(C1893R.layout.remote_input, viewGroup, false);
        remoteInputView.mController = remoteInputController;
        remoteInputView.mEntry = notificationEntry;
        UserHandle computeTextOperationUser = computeTextOperationUser(notificationEntry.getSbn().getUser());
        remoteInputView.mEditText.mUser = computeTextOperationUser;
        remoteInputView.mEditText.setTextOperationUser(computeTextOperationUser);
        remoteInputView.setTag(VIEW_TAG);
        return remoteInputView;
    }

    public void onClick(View view) {
        if (view == this.mSendButton) {
            sendRemoteInput();
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }

    /* access modifiers changed from: private */
    public void onDefocus(boolean z, boolean z2) {
        RevealParams revealParams;
        this.mController.removeRemoteInput(this.mEntry, this.mToken);
        this.mEntry.remoteInputText = this.mEditText.getText();
        if (!this.mRemoved) {
            if (!z || (revealParams = this.mRevealParams) == null || revealParams.radius <= 0) {
                setVisibility(8);
                NotificationViewWrapper notificationViewWrapper = this.mWrapper;
                if (notificationViewWrapper != null) {
                    notificationViewWrapper.setRemoteInputVisible(false);
                }
            } else {
                Animator createCircularHideAnimator = this.mRevealParams.createCircularHideAnimator(this);
                createCircularHideAnimator.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
                createCircularHideAnimator.setDuration(150);
                createCircularHideAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        RemoteInputView.this.setVisibility(8);
                        if (RemoteInputView.this.mWrapper != null) {
                            RemoteInputView.this.mWrapper.setRemoteInputVisible(false);
                        }
                    }
                });
                createCircularHideAnimator.start();
            }
        }
        if (z2) {
            this.mUiEventLogger.logWithInstanceId(NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_CLOSE, this.mEntry.getSbn().getUid(), this.mEntry.getSbn().getPackageName(), this.mEntry.getSbn().getInstanceId());
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        RemoteInputView unused = this.mEditText.mRemoteInputView = this;
        this.mEditText.setOnEditorActionListener(this.mEditorActionHandler);
        this.mEditText.addTextChangedListener(this.mTextWatcher);
        if (this.mEntry.getRow().isChangingPosition() && getVisibility() == 0 && this.mEditText.isFocusable()) {
            this.mEditText.requestFocus();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mEditText.removeTextChangedListener(this.mTextWatcher);
        this.mEditText.setOnEditorActionListener((TextView.OnEditorActionListener) null);
        RemoteInputView unused = this.mEditText.mRemoteInputView = null;
        if (!this.mEntry.getRow().isChangingPosition() && !isTemporarilyDetached()) {
            this.mController.removeRemoteInput(this.mEntry, this.mToken);
            this.mController.removeSpinning(this.mEntry.getKey(), this.mToken);
        }
    }

    public void onVisibilityAggregated(boolean z) {
        super.onVisibilityAggregated(z);
        this.mEditText.setEnabled(z && !this.mSending);
    }

    public void setHintText(CharSequence charSequence) {
        this.mEditText.setHint(charSequence);
    }

    public void setSupportedMimeTypes(Collection<String> collection) {
        this.mEditText.setSupportedMimeTypes(collection);
    }

    public void setEditTextContent(CharSequence charSequence) {
        this.mEditText.setText(charSequence);
    }

    public void focusAnimated() {
        RevealParams revealParams;
        if (!(getVisibility() == 0 || (revealParams = this.mRevealParams) == null)) {
            Animator createCircularRevealAnimator = revealParams.createCircularRevealAnimator(this);
            createCircularRevealAnimator.setDuration(360);
            createCircularRevealAnimator.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
            createCircularRevealAnimator.start();
        }
        focus();
    }

    private static UserHandle computeTextOperationUser(UserHandle userHandle) {
        return UserHandle.ALL.equals(userHandle) ? UserHandle.of(ActivityManager.getCurrentUser()) : userHandle;
    }

    public void focus() {
        this.mUiEventLogger.logWithInstanceId(NotificationRemoteInputEvent.NOTIFICATION_REMOTE_INPUT_OPEN, this.mEntry.getSbn().getUid(), this.mEntry.getSbn().getPackageName(), this.mEntry.getSbn().getInstanceId());
        setVisibility(0);
        NotificationViewWrapper notificationViewWrapper = this.mWrapper;
        if (notificationViewWrapper != null) {
            notificationViewWrapper.setRemoteInputVisible(true);
        }
        this.mEditText.setInnerFocusable(true);
        this.mEditText.mShowImeOnInputConnection = true;
        this.mEditText.setText(this.mEntry.remoteInputText);
        RemoteEditText remoteEditText = this.mEditText;
        remoteEditText.setSelection(remoteEditText.length());
        this.mEditText.requestFocus();
        this.mController.addRemoteInput(this.mEntry, this.mToken);
        setAttachment(this.mEntry.remoteInputAttachment);
        updateSendButton();
    }

    public void onNotificationUpdateOrReset() {
        NotificationViewWrapper notificationViewWrapper;
        if (this.mProgressBar.getVisibility() == 0) {
            reset();
        }
        if (isActive() && (notificationViewWrapper = this.mWrapper) != null) {
            notificationViewWrapper.setRemoteInputVisible(true);
        }
    }

    private void reset() {
        this.mResetting = true;
        this.mSending = false;
        this.mEntry.remoteInputTextWhenReset = SpannedString.valueOf(this.mEditText.getText());
        this.mEditText.getText().clear();
        this.mEditText.setEnabled(isAggregatedVisible());
        this.mSendButton.setVisibility(0);
        this.mProgressBar.setVisibility(4);
        this.mController.removeSpinning(this.mEntry.getKey(), this.mToken);
        updateSendButton();
        onDefocus(false, false);
        setAttachment((ContentInfo) null);
        this.mResetting = false;
    }

    public boolean onRequestSendAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        if (!this.mResetting || view != this.mEditText) {
            return super.onRequestSendAccessibilityEvent(view, accessibilityEvent);
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void updateSendButton() {
        this.mSendButton.setEnabled((this.mEditText.length() == 0 && this.mEntry.remoteInputAttachment == null) ? false : true);
    }

    public void close() {
        this.mEditText.defocusIfNeeded(false);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            this.mController.requestDisallowLongPressAndDismiss();
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public boolean requestScrollTo() {
        this.mController.lockScrollTo(this.mEntry);
        return true;
    }

    public boolean isActive() {
        return this.mEditText.isFocused() && this.mEditText.isEnabled();
    }

    public void setRemoved() {
        this.mRemoved = true;
    }

    public void setRevealParameters(RevealParams revealParams) {
        this.mRevealParams = revealParams;
    }

    public void dispatchStartTemporaryDetach() {
        super.dispatchStartTemporaryDetach();
        int indexOfChild = indexOfChild(this.mEditText);
        if (indexOfChild != -1) {
            detachViewFromParent(indexOfChild);
        }
    }

    public void dispatchFinishTemporaryDetach() {
        if (isAttachedToWindow()) {
            RemoteEditText remoteEditText = this.mEditText;
            attachViewToParent(remoteEditText, 0, remoteEditText.getLayoutParams());
        } else {
            removeDetachedView(this.mEditText, false);
        }
        super.dispatchFinishTemporaryDetach();
    }

    public void setWrapper(NotificationViewWrapper notificationViewWrapper) {
        this.mWrapper = notificationViewWrapper;
    }

    public void addOnVisibilityChangedListener(Consumer<Boolean> consumer) {
        this.mOnVisibilityChangedListeners.add(consumer);
    }

    public void removeOnVisibilityChangedListener(Consumer<Boolean> consumer) {
        this.mOnVisibilityChangedListeners.remove((Object) consumer);
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (view == this) {
            Iterator it = new ArrayList(this.mOnVisibilityChangedListeners).iterator();
            while (it.hasNext()) {
                ((Consumer) it.next()).accept(Boolean.valueOf(i == 0));
            }
            if (i != 0 && !this.mController.isRemoteInputActive()) {
                this.mEditText.hideIme();
            }
        }
    }

    public boolean isSending() {
        return getVisibility() == 0 && this.mController.isSpinning(this.mEntry.getKey(), this.mToken);
    }

    public void addOnEditTextFocusChangedListener(View.OnFocusChangeListener onFocusChangeListener) {
        this.mEditTextFocusChangeListeners.add(onFocusChangeListener);
    }

    public void removeOnEditTextFocusChangedListener(View.OnFocusChangeListener onFocusChangeListener) {
        this.mEditTextFocusChangeListeners.remove((Object) onFocusChangeListener);
    }

    public boolean editTextHasFocus() {
        RemoteEditText remoteEditText = this.mEditText;
        return remoteEditText != null && remoteEditText.hasFocus();
    }

    /* access modifiers changed from: private */
    public void onEditTextFocusChanged(RemoteEditText remoteEditText, boolean z) {
        Iterator it = new ArrayList(this.mEditTextFocusChangeListeners).iterator();
        while (it.hasNext()) {
            ((View.OnFocusChangeListener) it.next()).onFocusChange(remoteEditText, z);
        }
    }

    public void addOnSendRemoteInputListener(Runnable runnable) {
        this.mOnSendListeners.add(runnable);
    }

    public void removeOnSendRemoteInputListener(Runnable runnable) {
        this.mOnSendListeners.remove((Object) runnable);
    }

    private class EditorActionHandler implements TextView.OnEditorActionListener {
        private EditorActionHandler() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            boolean z = keyEvent == null && (i == 6 || i == 5 || i == 4);
            boolean z2 = keyEvent != null && KeyEvent.isConfirmKey(keyEvent.getKeyCode()) && keyEvent.getAction() == 0;
            if (!z && !z2) {
                return false;
            }
            if (RemoteInputView.this.mEditText.length() > 0 || RemoteInputView.this.mEntry.remoteInputAttachment != null) {
                RemoteInputView.this.sendRemoteInput();
            }
            return true;
        }
    }

    private class SendButtonTextWatcher implements TextWatcher {
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        private SendButtonTextWatcher() {
        }

        public void afterTextChanged(Editable editable) {
            RemoteInputView.this.updateSendButton();
        }
    }

    public static class RemoteEditText extends EditText {
        /* access modifiers changed from: private */
        public InputMethodManager mInputMethodManager;
        private LightBarController mLightBarController = ((LightBarController) Dependency.get(LightBarController.class));
        private final OnReceiveContentListener mOnReceiveContentListener = new RemoteInputView$RemoteEditText$$ExternalSyntheticLambda0(this);
        /* access modifiers changed from: private */
        public RemoteInputView mRemoteInputView;
        boolean mShowImeOnInputConnection;
        private ArraySet<String> mSupportedMimes = new ArraySet<>();
        UserHandle mUser;

        public /* bridge */ /* synthetic */ CharSequence getText() {
            return super.getText();
        }

        public RemoteEditText(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        /* access modifiers changed from: package-private */
        public void setSupportedMimeTypes(Collection<String> collection) {
            OnReceiveContentListener onReceiveContentListener;
            String[] strArr;
            if (collection == null || collection.isEmpty()) {
                strArr = null;
                onReceiveContentListener = null;
            } else {
                strArr = (String[]) collection.toArray((T[]) new String[0]);
                onReceiveContentListener = this.mOnReceiveContentListener;
            }
            setOnReceiveContentListener(strArr, onReceiveContentListener);
            this.mSupportedMimes.clear();
            this.mSupportedMimes.addAll(collection);
        }

        /* access modifiers changed from: private */
        public void hideIme() {
            WindowInsetsController windowInsetsController = getWindowInsetsController();
            if (windowInsetsController != null) {
                windowInsetsController.hide(WindowInsets.Type.ime());
            }
        }

        /* access modifiers changed from: private */
        public void defocusIfNeeded(boolean z) {
            RemoteInputView remoteInputView;
            RemoteInputView remoteInputView2 = this.mRemoteInputView;
            if ((remoteInputView2 == null || !remoteInputView2.mEntry.getRow().isChangingPosition()) && !isTemporarilyDetached()) {
                if (isFocusable() && isEnabled()) {
                    setInnerFocusable(false);
                    RemoteInputView remoteInputView3 = this.mRemoteInputView;
                    if (remoteInputView3 != null) {
                        remoteInputView3.onDefocus(z, true);
                    }
                    this.mShowImeOnInputConnection = false;
                }
            } else if (isTemporarilyDetached() && (remoteInputView = this.mRemoteInputView) != null) {
                remoteInputView.mEntry.remoteInputText = getText();
            }
        }

        /* access modifiers changed from: protected */
        public void onVisibilityChanged(View view, int i) {
            super.onVisibilityChanged(view, i);
            if (!isShown()) {
                defocusIfNeeded(false);
            }
        }

        /* access modifiers changed from: protected */
        public void onFocusChanged(boolean z, int i, Rect rect) {
            super.onFocusChanged(z, i, rect);
            RemoteInputView remoteInputView = this.mRemoteInputView;
            if (remoteInputView != null) {
                remoteInputView.onEditTextFocusChanged(this, z);
            }
            if (!z) {
                defocusIfNeeded(true);
            }
            RemoteInputView remoteInputView2 = this.mRemoteInputView;
            if (remoteInputView2 != null && !remoteInputView2.mRemoved) {
                this.mLightBarController.setDirectReplying(z);
            }
        }

        public void getFocusedRect(Rect rect) {
            super.getFocusedRect(rect);
            rect.top = this.mScrollY;
            rect.bottom = this.mScrollY + (this.mBottom - this.mTop);
        }

        public boolean requestRectangleOnScreen(Rect rect) {
            return this.mRemoteInputView.requestScrollTo();
        }

        public boolean onKeyDown(int i, KeyEvent keyEvent) {
            if (i == 4) {
                return true;
            }
            return super.onKeyDown(i, keyEvent);
        }

        public boolean onKeyUp(int i, KeyEvent keyEvent) {
            if (i != 4) {
                return super.onKeyUp(i, keyEvent);
            }
            defocusIfNeeded(true);
            return true;
        }

        public boolean onKeyPreIme(int i, KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == 4 && keyEvent.getAction() == 1) {
                defocusIfNeeded(true);
            }
            return super.onKeyPreIme(i, keyEvent);
        }

        public boolean onCheckIsTextEditor() {
            RemoteInputView remoteInputView = this.mRemoteInputView;
            if ((remoteInputView != null && remoteInputView.mRemoved) || !super.onCheckIsTextEditor()) {
                return false;
            }
            return true;
        }

        public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
            Context context;
            InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
            try {
                context = this.mContext.createPackageContextAsUser(this.mContext.getPackageName(), 0, this.mUser);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(RemoteInputView.TAG, "Unable to create user context:" + e.getMessage(), e);
                context = null;
            }
            if (this.mShowImeOnInputConnection && onCreateInputConnection != null) {
                if (context == null) {
                    context = getContext();
                }
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(InputMethodManager.class);
                this.mInputMethodManager = inputMethodManager;
                if (inputMethodManager != null) {
                    post(new Runnable() {
                        public void run() {
                            RemoteEditText.this.mInputMethodManager.viewClicked(RemoteEditText.this);
                            RemoteEditText.this.mInputMethodManager.showSoftInput(RemoteEditText.this, 0);
                        }
                    });
                }
            }
            return onCreateInputConnection;
        }

        public void onCommitCompletion(CompletionInfo completionInfo) {
            clearComposingText();
            setText(completionInfo.getText());
            setSelection(getText().length());
        }

        /* access modifiers changed from: package-private */
        public void setInnerFocusable(boolean z) {
            setFocusableInTouchMode(z);
            setFocusable(z);
            setCursorVisible(z);
            if (z) {
                requestFocus();
            }
        }

        /* access modifiers changed from: private */
        public ContentInfo onReceiveContent(View view, ContentInfo contentInfo) {
            Pair partition = contentInfo.partition(new RemoteInputView$RemoteEditText$$ExternalSyntheticLambda1());
            ContentInfo contentInfo2 = (ContentInfo) partition.first;
            ContentInfo contentInfo3 = (ContentInfo) partition.second;
            if (contentInfo2 != null) {
                this.mRemoteInputView.setAttachment(contentInfo2);
            }
            return contentInfo3;
        }

        static /* synthetic */ boolean lambda$onReceiveContent$0(ClipData.Item item) {
            return item.getUri() != null;
        }
    }

    public static class RevealParams {
        final int centerX;
        final int centerY;
        final int radius;

        public RevealParams(int i, int i2, int i3) {
            this.centerX = i;
            this.centerY = i2;
            this.radius = i3;
        }

        /* access modifiers changed from: package-private */
        public Animator createCircularHideAnimator(View view) {
            return ViewAnimationUtils.createCircularReveal(view, this.centerX, this.centerY, (float) this.radius, 0.0f);
        }

        /* access modifiers changed from: package-private */
        public Animator createCircularRevealAnimator(View view) {
            return ViewAnimationUtils.createCircularReveal(view, this.centerX, this.centerY, 0.0f, (float) this.radius);
        }
    }
}
