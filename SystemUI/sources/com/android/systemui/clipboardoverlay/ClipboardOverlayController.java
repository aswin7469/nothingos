package com.android.systemui.clipboardoverlay;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.ICompatCameraControlCallback;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Insets;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Icon;
import android.hardware.display.DisplayManager;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.IBinder;
import android.os.LocaleList;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.MathUtils;
import android.util.Size;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.InputMonitor;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import android.view.textclassifier.TextClassificationManager;
import android.view.textclassifier.TextClassifier;
import android.view.textclassifier.TextLinks;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.core.view.accessibility.AccessibilityViewCommand;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.policy.PhoneWindow;
import com.android.systemui.C1893R;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.screenshot.DraggableConstraintLayout;
import com.android.systemui.screenshot.FloatingWindowUtil;
import com.android.systemui.screenshot.OverlayActionChip;
import com.android.systemui.screenshot.TimeoutHandler;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Executor;

public class ClipboardOverlayController {
    private static final int CLIPBOARD_DEFAULT_TIMEOUT_MILLIS = 6000;
    public static final String COPY_OVERLAY_ACTION = "com.android.systemui.COPY";
    private static final String EXTRA_EDIT_SOURCE_CLIPBOARD = "edit_source_clipboard";
    private static final int FONT_SEARCH_STEP_PX = 4;
    private static final String REMOTE_COPY_ACTION = "android.intent.action.REMOTE_COPY";
    public static final String SCREENSHOT_ACTION = "com.android.systemui.SCREENSHOT";
    public static final String SELF_PERMISSION = "com.android.systemui.permission.SELF";
    private static final int SWIPE_PADDING_DP = 12;
    private static final String TAG = "ClipboardOverlayCtrlr";
    private final AccessibilityManager mAccessibilityManager;
    private final ArrayList<OverlayActionChip> mActionChips = new ArrayList<>();
    private final LinearLayout mActionContainer;
    /* access modifiers changed from: private */
    public final View mActionContainerBackground;
    /* access modifiers changed from: private */
    public boolean mBlockAttach = false;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final View mClipboardPreview;
    private BroadcastReceiver mCloseDialogsReceiver;
    /* access modifiers changed from: private */
    public final Context mContext;
    /* access modifiers changed from: private */
    public final View mDismissButton;
    private final DisplayManager mDisplayManager;
    /* access modifiers changed from: private */
    public final DisplayMetrics mDisplayMetrics;
    private final OverlayActionChip mEditChip;
    private Animator mEnterAnimator;
    /* access modifiers changed from: private */
    public Animator mExitAnimator;
    private final TextView mHiddenPreview;
    private final ImageView mImagePreview;
    private InputEventReceiver mInputEventReceiver;
    private InputMonitor mInputMonitor;
    private Runnable mOnSessionCompleteListener;
    /* access modifiers changed from: private */
    public final int mOrientation;
    /* access modifiers changed from: private */
    public final View mPreviewBorder;
    private final OverlayActionChip mRemoteCopyChip;
    private BroadcastReceiver mScreenshotReceiver;
    private final OverlayActionChip mShareChip;
    private final TextClassifier mTextClassifier;
    private final TextView mTextPreview;
    /* access modifiers changed from: private */
    public final TimeoutHandler mTimeoutHandler;
    /* access modifiers changed from: private */
    public final UiEventLogger mUiEventLogger;
    /* access modifiers changed from: private */
    public final DraggableConstraintLayout mView;
    private final PhoneWindow mWindow;
    private final WindowManager.LayoutParams mWindowLayoutParams;
    private final WindowManager mWindowManager;

    public ClipboardOverlayController(Context context, BroadcastDispatcher broadcastDispatcher, BroadcastSender broadcastSender, TimeoutHandler timeoutHandler, UiEventLogger uiEventLogger) {
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mDisplayManager = (DisplayManager) Objects.requireNonNull((DisplayManager) context.getSystemService(DisplayManager.class));
        Context createWindowContext = context.createDisplayContext(getDefaultDisplay()).createWindowContext(2036, (Bundle) null);
        this.mContext = createWindowContext;
        this.mUiEventLogger = uiEventLogger;
        this.mAccessibilityManager = AccessibilityManager.getInstance(createWindowContext);
        this.mTextClassifier = ((TextClassificationManager) Objects.requireNonNull((TextClassificationManager) context.getSystemService(TextClassificationManager.class))).getTextClassifier();
        WindowManager windowManager = (WindowManager) createWindowContext.getSystemService(WindowManager.class);
        this.mWindowManager = windowManager;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.mDisplayMetrics = displayMetrics;
        createWindowContext.getDisplay().getRealMetrics(displayMetrics);
        this.mTimeoutHandler = timeoutHandler;
        timeoutHandler.setDefaultTimeoutMillis(6000);
        WindowManager.LayoutParams floatingWindowParams = FloatingWindowUtil.getFloatingWindowParams();
        this.mWindowLayoutParams = floatingWindowParams;
        floatingWindowParams.setTitle("ClipboardOverlay");
        PhoneWindow floatingWindow = FloatingWindowUtil.getFloatingWindow(createWindowContext);
        this.mWindow = floatingWindow;
        floatingWindow.setWindowManager(windowManager, (IBinder) null, (String) null);
        setWindowFocusable(false);
        DraggableConstraintLayout draggableConstraintLayout = (DraggableConstraintLayout) LayoutInflater.from(createWindowContext).inflate(C1893R.layout.clipboard_overlay, (ViewGroup) null);
        this.mView = draggableConstraintLayout;
        this.mActionContainerBackground = (View) Objects.requireNonNull(draggableConstraintLayout.findViewById(C1893R.C1897id.actions_container_background));
        this.mActionContainer = (LinearLayout) Objects.requireNonNull((LinearLayout) draggableConstraintLayout.findViewById(C1893R.C1897id.actions));
        this.mClipboardPreview = (View) Objects.requireNonNull(draggableConstraintLayout.findViewById(C1893R.C1897id.clipboard_preview));
        this.mImagePreview = (ImageView) Objects.requireNonNull((ImageView) draggableConstraintLayout.findViewById(C1893R.C1897id.image_preview));
        TextView textView = (TextView) Objects.requireNonNull((TextView) draggableConstraintLayout.findViewById(C1893R.C1897id.text_preview));
        this.mTextPreview = textView;
        this.mHiddenPreview = (TextView) Objects.requireNonNull((TextView) draggableConstraintLayout.findViewById(C1893R.C1897id.hidden_preview));
        this.mPreviewBorder = (View) Objects.requireNonNull(draggableConstraintLayout.findViewById(C1893R.C1897id.preview_border));
        OverlayActionChip overlayActionChip = (OverlayActionChip) Objects.requireNonNull((OverlayActionChip) draggableConstraintLayout.findViewById(C1893R.C1897id.edit_chip));
        this.mEditChip = overlayActionChip;
        OverlayActionChip overlayActionChip2 = (OverlayActionChip) Objects.requireNonNull((OverlayActionChip) draggableConstraintLayout.findViewById(C1893R.C1897id.share_chip));
        this.mShareChip = overlayActionChip2;
        OverlayActionChip overlayActionChip3 = (OverlayActionChip) Objects.requireNonNull((OverlayActionChip) draggableConstraintLayout.findViewById(C1893R.C1897id.remote_copy_chip));
        this.mRemoteCopyChip = overlayActionChip3;
        overlayActionChip.setAlpha(1.0f);
        overlayActionChip2.setAlpha(1.0f);
        overlayActionChip3.setAlpha(1.0f);
        View view = (View) Objects.requireNonNull(draggableConstraintLayout.findViewById(C1893R.C1897id.dismiss_button));
        this.mDismissButton = view;
        overlayActionChip2.setContentDescription(createWindowContext.getString(17041496));
        draggableConstraintLayout.setCallbacks(new DraggableConstraintLayout.SwipeDismissCallbacks() {
            public void onInteraction() {
                ClipboardOverlayController.this.mTimeoutHandler.resetTimeout();
            }

            public void onSwipeDismissInitiated(Animator animator) {
                ClipboardOverlayController.this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_SWIPE_DISMISSED);
                Animator unused = ClipboardOverlayController.this.mExitAnimator = animator;
            }

            public void onDismissComplete() {
                ClipboardOverlayController.this.hideImmediate();
            }
        });
        textView.getViewTreeObserver().addOnPreDrawListener(new ClipboardOverlayController$$ExternalSyntheticLambda12(this));
        view.setOnClickListener(new ClipboardOverlayController$$ExternalSyntheticLambda13(this));
        overlayActionChip.setIcon(Icon.createWithResource(createWindowContext, C1893R.C1895drawable.ic_screenshot_edit), true);
        overlayActionChip3.setIcon(Icon.createWithResource(createWindowContext, C1893R.C1895drawable.ic_baseline_devices_24), true);
        overlayActionChip2.setIcon(Icon.createWithResource(createWindowContext, C1893R.C1895drawable.ic_screenshot_share), true);
        this.mOrientation = createWindowContext.getResources().getConfiguration().orientation;
        attachWindow();
        withWindowAttached(new ClipboardOverlayController$$ExternalSyntheticLambda14(this));
        timeoutHandler.setOnTimeoutRunnable(new ClipboardOverlayController$$ExternalSyntheticLambda15(this));
        C20153 r10 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if ("android.intent.action.CLOSE_SYSTEM_DIALOGS".equals(intent.getAction())) {
                    ClipboardOverlayController.this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_DISMISSED_OTHER);
                    ClipboardOverlayController.this.animateOut();
                }
            }
        };
        this.mCloseDialogsReceiver = r10;
        broadcastDispatcher.registerReceiver(r10, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
        C20164 r3 = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (ClipboardOverlayController.SCREENSHOT_ACTION.equals(intent.getAction())) {
                    ClipboardOverlayController.this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_DISMISSED_OTHER);
                    ClipboardOverlayController.this.animateOut();
                }
            }
        };
        this.mScreenshotReceiver = r3;
        broadcastDispatcher.registerReceiver(r3, new IntentFilter(SCREENSHOT_ACTION), (Executor) null, (UserHandle) null, 2, "com.android.systemui.permission.SELF");
        monitorOutsideTouches();
        Intent intent = new Intent(COPY_OVERLAY_ACTION);
        intent.setPackage(createWindowContext.getPackageName());
        broadcastSender.sendBroadcast(intent, "com.android.systemui.permission.SELF");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ boolean mo31300x668837e() {
        TextView textView = this.mTextPreview;
        textView.setMaxLines((this.mTextPreview.getHeight() - (this.mTextPreview.getPaddingTop() + this.mTextPreview.getPaddingBottom())) / textView.getLineHeight());
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$1$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31301x70980b9d(View view) {
        this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_DISMISS_TAPPED);
        animateOut();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$2$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31302xdac793bc() {
        this.mWindow.setContentView(this.mView);
        updateInsets(this.mWindowManager.getCurrentWindowMetrics().getWindowInsets());
        this.mView.requestLayout();
        this.mWindow.peekDecorView().getViewRootImpl().setActivityConfigCallback(new ViewRootImpl.ActivityConfigCallback() {
            public void onConfigurationChanged(Configuration configuration, int i) {
                if (ClipboardOverlayController.this.mContext.getResources().getConfiguration().orientation != ClipboardOverlayController.this.mOrientation) {
                    ClipboardOverlayController.this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_DISMISSED_OTHER);
                    ClipboardOverlayController.this.hideImmediate();
                }
            }

            public void requestCompatCameraControl(boolean z, boolean z2, ICompatCameraControlCallback iCompatCameraControlCallback) {
                Log.w(ClipboardOverlayController.TAG, "unexpected requestCompatCameraControl call");
            }
        });
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$3$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31303x44f71bdb() {
        this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_TIMED_OUT);
        animateOut();
    }

    /* access modifiers changed from: package-private */
    public void setClipData(ClipData clipData, String str) {
        String str2;
        Animator animator = this.mExitAnimator;
        if (animator != null && animator.isRunning()) {
            this.mExitAnimator.cancel();
        }
        reset();
        boolean z = (clipData == null || clipData.getDescription().getExtras() == null || !clipData.getDescription().getExtras().getBoolean("android.content.extra.IS_SENSITIVE")) ? false : true;
        if (clipData == null || clipData.getItemCount() == 0) {
            showTextPreview(this.mContext.getResources().getString(C1893R.string.clipboard_overlay_text_copied), this.mTextPreview);
            str2 = this.mContext.getString(C1893R.string.clipboard_content_copied);
        } else if (!TextUtils.isEmpty(clipData.getItemAt(0).getText())) {
            ClipData.Item itemAt = clipData.getItemAt(0);
            if (DeviceConfig.getBoolean("systemui", "clipboard_overlay_show_actions", false) && itemAt.getTextLinks() != null) {
                AsyncTask.execute(new ClipboardOverlayController$$ExternalSyntheticLambda8(this, clipData, str));
            }
            if (z) {
                showEditableText(this.mContext.getResources().getString(C1893R.string.clipboard_asterisks), true);
            } else {
                showEditableText(itemAt.getText(), false);
            }
            showShareChip(clipData);
            str2 = this.mContext.getString(C1893R.string.clipboard_text_copied);
        } else if (clipData.getItemAt(0).getUri() == null) {
            showTextPreview(this.mContext.getResources().getString(C1893R.string.clipboard_overlay_text_copied), this.mTextPreview);
            str2 = this.mContext.getString(C1893R.string.clipboard_content_copied);
        } else if (tryShowEditableImage(clipData.getItemAt(0).getUri(), z)) {
            showShareChip(clipData);
            str2 = this.mContext.getString(C1893R.string.clipboard_image_copied);
        } else {
            str2 = this.mContext.getString(C1893R.string.clipboard_content_copied);
        }
        Intent remoteCopyIntent = getRemoteCopyIntent(clipData);
        if (this.mContext.getPackageManager().resolveActivity(remoteCopyIntent, PackageManager.ResolveInfoFlags.of(0)) != null) {
            this.mRemoteCopyChip.setContentDescription(this.mContext.getString(C1893R.string.clipboard_send_nearby_description));
            this.mRemoteCopyChip.setVisibility(0);
            this.mRemoteCopyChip.setOnClickListener(new ClipboardOverlayController$$ExternalSyntheticLambda9(this, remoteCopyIntent));
            this.mActionContainerBackground.setVisibility(0);
        } else {
            this.mRemoteCopyChip.setVisibility(8);
        }
        withWindowAttached(new ClipboardOverlayController$$ExternalSyntheticLambda10(this, str2));
        this.mTimeoutHandler.resetTimeout();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setClipData$4$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31304xee2f917e(ClipData clipData, String str) {
        classifyText(clipData.getItemAt(0), str);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setClipData$5$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31305x585f199d(Intent intent, View view) {
        this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_REMOTE_COPY_TAPPED);
        this.mContext.startActivity(intent);
        animateOut();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setClipData$6$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31306xc28ea1bc(String str) {
        updateInsets(this.mWindowManager.getCurrentWindowMetrics().getWindowInsets());
        Animator animator = this.mEnterAnimator;
        if (animator == null || !animator.isRunning()) {
            this.mView.post(new ClipboardOverlayController$$ExternalSyntheticLambda4(this));
        }
        this.mView.announceForAccessibility(str);
    }

    /* access modifiers changed from: package-private */
    public void setOnSessionCompleteListener(Runnable runnable) {
        this.mOnSessionCompleteListener = runnable;
    }

    private void classifyText(ClipData.Item item, String str) {
        ArrayList arrayList = new ArrayList();
        for (TextLinks.TextLink next : item.getTextLinks().getLinks()) {
            arrayList.addAll(this.mTextClassifier.classifyText(item.getText(), next.getStart(), next.getEnd(), (LocaleList) null).getActions());
        }
        this.mView.post(new ClipboardOverlayController$$ExternalSyntheticLambda11(this, arrayList, str));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$classifyText$7$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31292x936ee82c(ArrayList arrayList, String str) {
        resetActionChips();
        if (arrayList.size() > 0) {
            this.mActionContainerBackground.setVisibility(0);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                RemoteAction remoteAction = (RemoteAction) it.next();
                ComponentName component = remoteAction.getActionIntent().getIntent().getComponent();
                if (component != null && !TextUtils.equals(str, component.getPackageName())) {
                    OverlayActionChip constructActionChip = constructActionChip(remoteAction);
                    this.mActionContainer.addView(constructActionChip);
                    this.mActionChips.add(constructActionChip);
                    return;
                }
            }
        }
    }

    private void showShareChip(ClipData clipData) {
        this.mShareChip.setVisibility(0);
        this.mActionContainerBackground.setVisibility(0);
        this.mShareChip.setOnClickListener(new ClipboardOverlayController$$ExternalSyntheticLambda5(this, clipData));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showShareChip$8$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31308x4377dcc8(ClipData clipData, View view) {
        shareContent(clipData);
    }

    private OverlayActionChip constructActionChip(RemoteAction remoteAction) {
        OverlayActionChip overlayActionChip = (OverlayActionChip) LayoutInflater.from(this.mContext).inflate(C1893R.layout.overlay_action_chip, this.mActionContainer, false);
        overlayActionChip.setText(remoteAction.getTitle());
        overlayActionChip.setContentDescription(remoteAction.getTitle());
        overlayActionChip.setIcon(remoteAction.getIcon(), false);
        overlayActionChip.setPendingIntent(remoteAction.getActionIntent(), new ClipboardOverlayController$$ExternalSyntheticLambda3(this));
        overlayActionChip.setAlpha(1.0f);
        return overlayActionChip;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$constructActionChip$9$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31293xd11ad2bc() {
        this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_ACTION_TAPPED);
        animateOut();
    }

    private void monitorOutsideTouches() {
        this.mInputMonitor = ((InputManager) this.mContext.getSystemService(InputManager.class)).monitorGestureInput("clipboard overlay", 0);
        this.mInputEventReceiver = new InputEventReceiver(this.mInputMonitor.getInputChannel(), Looper.getMainLooper()) {
            public void onInputEvent(InputEvent inputEvent) {
                if (inputEvent instanceof MotionEvent) {
                    MotionEvent motionEvent = (MotionEvent) inputEvent;
                    if (motionEvent.getActionMasked() == 0) {
                        Region region = new Region();
                        Rect rect = new Rect();
                        ClipboardOverlayController.this.mPreviewBorder.getBoundsOnScreen(rect);
                        rect.inset((int) FloatingWindowUtil.dpToPx(ClipboardOverlayController.this.mDisplayMetrics, -12.0f), (int) FloatingWindowUtil.dpToPx(ClipboardOverlayController.this.mDisplayMetrics, -12.0f));
                        region.op(rect, Region.Op.UNION);
                        ClipboardOverlayController.this.mActionContainerBackground.getBoundsOnScreen(rect);
                        rect.inset((int) FloatingWindowUtil.dpToPx(ClipboardOverlayController.this.mDisplayMetrics, -12.0f), (int) FloatingWindowUtil.dpToPx(ClipboardOverlayController.this.mDisplayMetrics, -12.0f));
                        region.op(rect, Region.Op.UNION);
                        ClipboardOverlayController.this.mDismissButton.getBoundsOnScreen(rect);
                        region.op(rect, Region.Op.UNION);
                        if (!region.contains((int) motionEvent.getRawX(), (int) motionEvent.getRawY())) {
                            ClipboardOverlayController.this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_TAP_OUTSIDE);
                            ClipboardOverlayController.this.animateOut();
                        }
                    }
                }
                finishInputEvent(inputEvent, true);
            }
        };
    }

    private void editImage(Uri uri) {
        this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_EDIT_TAPPED);
        String string = this.mContext.getString(C1893R.string.config_screenshotEditor);
        Intent intent = new Intent("android.intent.action.EDIT");
        if (!TextUtils.isEmpty(string)) {
            intent.setComponent(ComponentName.unflattenFromString(string));
        }
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(1);
        intent.addFlags(268468224);
        intent.putExtra(EXTRA_EDIT_SOURCE_CLIPBOARD, true);
        this.mContext.startActivity(intent);
        animateOut();
    }

    private void editText() {
        this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_EDIT_TAPPED);
        Intent intent = new Intent(this.mContext, EditTextActivity.class);
        intent.addFlags(268468224);
        this.mContext.startActivity(intent);
        animateOut();
    }

    private void shareContent(ClipData clipData) {
        this.mUiEventLogger.log(ClipboardOverlayEvent.CLIPBOARD_OVERLAY_SHARE_TAPPED);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", clipData.getItemAt(0).getText());
        intent.setDataAndType(clipData.getItemAt(0).getUri(), clipData.getDescription().getMimeType(0));
        intent.putExtra("android.intent.extra.STREAM", clipData.getItemAt(0).getUri());
        intent.addFlags(1);
        this.mContext.startActivity(Intent.createChooser(intent, (CharSequence) null).addFlags(268468224).addFlags(1));
        animateOut();
    }

    private void showSinglePreview(View view) {
        this.mTextPreview.setVisibility(8);
        this.mImagePreview.setVisibility(8);
        this.mHiddenPreview.setVisibility(8);
        view.setVisibility(0);
    }

    private void showTextPreview(CharSequence charSequence, TextView textView) {
        showSinglePreview(textView);
        CharSequence subSequence = charSequence.subSequence(0, Math.min(500, charSequence.length()));
        textView.setText(subSequence);
        updateTextSize(subSequence, textView);
        textView.addOnLayoutChangeListener(new ClipboardOverlayController$$ExternalSyntheticLambda6(this, subSequence, textView));
        this.mEditChip.setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showTextPreview$10$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31309xe508fb0b(CharSequence charSequence, TextView textView, View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (i3 - i != i7 - i5) {
            updateTextSize(charSequence, textView);
        }
    }

    private void updateTextSize(CharSequence charSequence, TextView textView) {
        Paint paint = new Paint(textView.getPaint());
        Resources resources = textView.getResources();
        float dimensionPixelSize = (float) resources.getDimensionPixelSize(C1893R.dimen.clipboard_overlay_min_font);
        float dimensionPixelSize2 = (float) resources.getDimensionPixelSize(C1893R.dimen.clipboard_overlay_max_font);
        if (!isOneWord(charSequence) || !fitsInView(charSequence, textView, paint, dimensionPixelSize)) {
            textView.setAutoSizeTextTypeUniformWithConfiguration((int) dimensionPixelSize, (int) dimensionPixelSize2, 4, 0);
            textView.setGravity(8388627);
            return;
        }
        while (true) {
            float f = 4.0f + dimensionPixelSize;
            if (f >= dimensionPixelSize2 || !fitsInView(charSequence, textView, paint, f)) {
                textView.setAutoSizeTextTypeWithDefaults(0);
                textView.setGravity(17);
                textView.setTextSize(0, (float) ((int) dimensionPixelSize));
            } else {
                dimensionPixelSize = f;
            }
        }
        textView.setAutoSizeTextTypeWithDefaults(0);
        textView.setGravity(17);
        textView.setTextSize(0, (float) ((int) dimensionPixelSize));
    }

    private static boolean fitsInView(CharSequence charSequence, TextView textView, Paint paint, float f) {
        paint.setTextSize(f);
        return paint.measureText(charSequence.toString()) < ((float) ((textView.getWidth() - textView.getPaddingLeft()) - textView.getPaddingRight()));
    }

    private static boolean isOneWord(CharSequence charSequence) {
        return charSequence.toString().split("\\s+", 2).length == 1;
    }

    private void showEditableText(CharSequence charSequence, boolean z) {
        TextView textView = z ? this.mHiddenPreview : this.mTextPreview;
        showTextPreview(charSequence, textView);
        ClipboardOverlayController$$ExternalSyntheticLambda7 clipboardOverlayController$$ExternalSyntheticLambda7 = new ClipboardOverlayController$$ExternalSyntheticLambda7(this);
        setAccessibilityActionToEdit(textView);
        if (DeviceConfig.getBoolean("systemui", "clipboard_overlay_show_edit_button", false)) {
            this.mEditChip.setVisibility(0);
            this.mActionContainerBackground.setVisibility(0);
            this.mEditChip.setContentDescription(this.mContext.getString(C1893R.string.clipboard_edit_text_description));
            this.mEditChip.setOnClickListener(clipboardOverlayController$$ExternalSyntheticLambda7);
        }
        textView.setOnClickListener(clipboardOverlayController$$ExternalSyntheticLambda7);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showEditableText$11$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31307xdd00aaf4(View view) {
        editText();
    }

    private boolean tryShowEditableImage(Uri uri, boolean z) {
        ClipboardOverlayController$$ExternalSyntheticLambda0 clipboardOverlayController$$ExternalSyntheticLambda0 = new ClipboardOverlayController$$ExternalSyntheticLambda0(this, uri);
        ContentResolver contentResolver = this.mContext.getContentResolver();
        String type = contentResolver.getType(uri);
        boolean z2 = type != null && type.startsWith("image");
        if (z) {
            this.mHiddenPreview.setText(this.mContext.getString(C1893R.string.clipboard_text_hidden));
            showSinglePreview(this.mHiddenPreview);
            if (z2) {
                this.mHiddenPreview.setOnClickListener(clipboardOverlayController$$ExternalSyntheticLambda0);
                setAccessibilityActionToEdit(this.mHiddenPreview);
            }
        } else if (z2) {
            try {
                int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.overlay_x_scale);
                Bitmap loadThumbnail = contentResolver.loadThumbnail(uri, new Size(dimensionPixelSize, dimensionPixelSize * 4), (CancellationSignal) null);
                showSinglePreview(this.mImagePreview);
                this.mImagePreview.setImageBitmap(loadThumbnail);
                this.mImagePreview.setOnClickListener(clipboardOverlayController$$ExternalSyntheticLambda0);
                setAccessibilityActionToEdit(this.mImagePreview);
            } catch (IOException e) {
                Log.e(TAG, "Thumbnail loading failed", e);
                showTextPreview(this.mContext.getResources().getString(C1893R.string.clipboard_overlay_text_copied), this.mTextPreview);
                z2 = false;
            }
        } else {
            showTextPreview(this.mContext.getResources().getString(C1893R.string.clipboard_overlay_text_copied), this.mTextPreview);
        }
        if (z2 && DeviceConfig.getBoolean("systemui", "clipboard_overlay_show_edit_button", false)) {
            this.mEditChip.setVisibility(0);
            this.mActionContainerBackground.setVisibility(0);
            this.mEditChip.setOnClickListener(clipboardOverlayController$$ExternalSyntheticLambda0);
            this.mEditChip.setContentDescription(this.mContext.getString(C1893R.string.clipboard_edit_image_description));
        }
        return z2;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$tryShowEditableImage$12$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31310x3cd8afa4(Uri uri, View view) {
        editImage(uri);
    }

    private void setAccessibilityActionToEdit(View view) {
        ViewCompat.replaceAccessibilityAction(view, AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK, this.mContext.getString(C1893R.string.clipboard_edit), (AccessibilityViewCommand) null);
    }

    private Intent getRemoteCopyIntent(ClipData clipData) {
        Intent intent = new Intent(REMOTE_COPY_ACTION);
        String string = this.mContext.getString(C1893R.string.config_remoteCopyPackage);
        if (!TextUtils.isEmpty(string)) {
            intent.setComponent(ComponentName.unflattenFromString(string));
        }
        intent.setClipData(clipData);
        intent.addFlags(1);
        intent.addFlags(268468224);
        return intent;
    }

    /* access modifiers changed from: private */
    public void animateIn() {
        if (this.mAccessibilityManager.isEnabled()) {
            this.mDismissButton.setVisibility(0);
        }
        Animator enterAnimation = getEnterAnimation();
        this.mEnterAnimator = enterAnimation;
        enterAnimation.start();
    }

    /* access modifiers changed from: private */
    public void animateOut() {
        Animator animator = this.mExitAnimator;
        if (animator == null || !animator.isRunning()) {
            Animator exitAnimation = getExitAnimation();
            exitAnimation.addListener(new AnimatorListenerAdapter() {
                private boolean mCancelled;

                public void onAnimationCancel(Animator animator) {
                    super.onAnimationCancel(animator);
                    this.mCancelled = true;
                }

                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    if (!this.mCancelled) {
                        ClipboardOverlayController.this.hideImmediate();
                    }
                }
            });
            this.mExitAnimator = exitAnimation;
            exitAnimation.start();
        }
    }

    private Animator getEnterAnimation() {
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        PathInterpolator pathInterpolator = new PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setInterpolator(linearInterpolator);
        ofFloat.setDuration(66);
        ofFloat.addUpdateListener(new ClipboardOverlayController$$ExternalSyntheticLambda19(this));
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat2.setInterpolator(pathInterpolator);
        ofFloat2.setDuration(333);
        ofFloat2.addUpdateListener(new ClipboardOverlayController$$ExternalSyntheticLambda1(this));
        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat3.setInterpolator(linearInterpolator);
        ofFloat3.setDuration(283);
        ofFloat3.addUpdateListener(new ClipboardOverlayController$$ExternalSyntheticLambda2(this));
        this.mActionContainer.setAlpha(0.0f);
        this.mPreviewBorder.setAlpha(0.0f);
        this.mClipboardPreview.setAlpha(0.0f);
        animatorSet.play(ofFloat).with(ofFloat2);
        animatorSet.play(ofFloat3).after(50).after(ofFloat);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                ClipboardOverlayController.this.mView.setAlpha(1.0f);
                ClipboardOverlayController.this.mTimeoutHandler.resetTimeout();
            }
        });
        return animatorSet;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getEnterAnimation$13$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31294x3efd428c(ValueAnimator valueAnimator) {
        this.mView.setAlpha(valueAnimator.getAnimatedFraction());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getEnterAnimation$14$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31295xa92ccaab(ValueAnimator valueAnimator) {
        float lerp = MathUtils.lerp(0.9f, 1.0f, valueAnimator.getAnimatedFraction());
        this.mClipboardPreview.setScaleX(lerp);
        this.mClipboardPreview.setScaleY(lerp);
        this.mPreviewBorder.setScaleX(lerp);
        this.mPreviewBorder.setScaleY(lerp);
        float width = (((float) this.mClipboardPreview.getWidth()) / 2.0f) + this.mClipboardPreview.getX();
        View view = this.mActionContainerBackground;
        view.setPivotX(width - view.getX());
        LinearLayout linearLayout = this.mActionContainer;
        linearLayout.setPivotX(width - ((View) linearLayout.getParent()).getX());
        float lerp2 = MathUtils.lerp(0.7f, 1.0f, valueAnimator.getAnimatedFraction());
        float lerp3 = MathUtils.lerp(0.9f, 1.0f, valueAnimator.getAnimatedFraction());
        this.mActionContainer.setScaleX(lerp2);
        this.mActionContainer.setScaleY(lerp3);
        this.mActionContainerBackground.setScaleX(lerp2);
        this.mActionContainerBackground.setScaleY(lerp3);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getEnterAnimation$15$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31296x135c52ca(ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        this.mClipboardPreview.setAlpha(animatedFraction);
        this.mPreviewBorder.setAlpha(animatedFraction);
        this.mDismissButton.setAlpha(animatedFraction);
        this.mActionContainer.setAlpha(animatedFraction);
    }

    private Animator getExitAnimation() {
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        PathInterpolator pathInterpolator = new PathInterpolator(0.3f, 0.0f, 1.0f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat.setInterpolator(linearInterpolator);
        ofFloat.setDuration(100);
        ofFloat.addUpdateListener(new ClipboardOverlayController$$ExternalSyntheticLambda16(this));
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat2.setInterpolator(pathInterpolator);
        ofFloat2.setDuration(250);
        ofFloat2.addUpdateListener(new ClipboardOverlayController$$ExternalSyntheticLambda17(this));
        ValueAnimator ofFloat3 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        ofFloat3.setInterpolator(linearInterpolator);
        ofFloat3.setDuration(166);
        ofFloat3.addUpdateListener(new ClipboardOverlayController$$ExternalSyntheticLambda18(this));
        animatorSet.play(ofFloat3).with(ofFloat2);
        animatorSet.play(ofFloat).after(150).after(ofFloat3);
        return animatorSet;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getExitAnimation$16$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31297xe1e62191(ValueAnimator valueAnimator) {
        this.mView.setAlpha(1.0f - valueAnimator.getAnimatedFraction());
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getExitAnimation$17$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31298x4c15a9b0(ValueAnimator valueAnimator) {
        float lerp = MathUtils.lerp(1.0f, 0.9f, valueAnimator.getAnimatedFraction());
        this.mClipboardPreview.setScaleX(lerp);
        this.mClipboardPreview.setScaleY(lerp);
        this.mPreviewBorder.setScaleX(lerp);
        this.mPreviewBorder.setScaleY(lerp);
        float width = (((float) this.mClipboardPreview.getWidth()) / 2.0f) + this.mClipboardPreview.getX();
        View view = this.mActionContainerBackground;
        view.setPivotX(width - view.getX());
        LinearLayout linearLayout = this.mActionContainer;
        linearLayout.setPivotX(width - ((View) linearLayout.getParent()).getX());
        float lerp2 = MathUtils.lerp(1.0f, 0.8f, valueAnimator.getAnimatedFraction());
        float lerp3 = MathUtils.lerp(1.0f, 0.9f, valueAnimator.getAnimatedFraction());
        this.mActionContainer.setScaleX(lerp2);
        this.mActionContainer.setScaleY(lerp3);
        this.mActionContainerBackground.setScaleX(lerp2);
        this.mActionContainerBackground.setScaleY(lerp3);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$getExitAnimation$18$com-android-systemui-clipboardoverlay-ClipboardOverlayController */
    public /* synthetic */ void mo31299xb64531cf(ValueAnimator valueAnimator) {
        float animatedFraction = 1.0f - valueAnimator.getAnimatedFraction();
        this.mClipboardPreview.setAlpha(animatedFraction);
        this.mPreviewBorder.setAlpha(animatedFraction);
        this.mDismissButton.setAlpha(animatedFraction);
        this.mActionContainer.setAlpha(animatedFraction);
    }

    /* access modifiers changed from: private */
    public void hideImmediate() {
        this.mTimeoutHandler.cancelTimeout();
        View peekDecorView = this.mWindow.peekDecorView();
        if (peekDecorView != null && peekDecorView.isAttachedToWindow()) {
            this.mWindowManager.removeViewImmediate(peekDecorView);
        }
        BroadcastReceiver broadcastReceiver = this.mCloseDialogsReceiver;
        if (broadcastReceiver != null) {
            this.mBroadcastDispatcher.unregisterReceiver(broadcastReceiver);
            this.mCloseDialogsReceiver = null;
        }
        BroadcastReceiver broadcastReceiver2 = this.mScreenshotReceiver;
        if (broadcastReceiver2 != null) {
            this.mBroadcastDispatcher.unregisterReceiver(broadcastReceiver2);
            this.mScreenshotReceiver = null;
        }
        InputEventReceiver inputEventReceiver = this.mInputEventReceiver;
        if (inputEventReceiver != null) {
            inputEventReceiver.dispose();
            this.mInputEventReceiver = null;
        }
        InputMonitor inputMonitor = this.mInputMonitor;
        if (inputMonitor != null) {
            inputMonitor.dispose();
            this.mInputMonitor = null;
        }
        Runnable runnable = this.mOnSessionCompleteListener;
        if (runnable != null) {
            runnable.run();
        }
    }

    private void resetActionChips() {
        Iterator<OverlayActionChip> it = this.mActionChips.iterator();
        while (it.hasNext()) {
            this.mActionContainer.removeView(it.next());
        }
        this.mActionChips.clear();
    }

    private void reset() {
        this.mView.setTranslationX(0.0f);
        this.mView.setAlpha(0.0f);
        this.mActionContainerBackground.setVisibility(8);
        this.mShareChip.setVisibility(8);
        this.mEditChip.setVisibility(8);
        this.mRemoteCopyChip.setVisibility(8);
        resetActionChips();
        this.mTimeoutHandler.cancelTimeout();
    }

    private void attachWindow() {
        View decorView = this.mWindow.getDecorView();
        if (!decorView.isAttachedToWindow() && !this.mBlockAttach) {
            this.mBlockAttach = true;
            this.mWindowManager.addView(decorView, this.mWindowLayoutParams);
            decorView.requestApplyInsets();
            this.mView.requestApplyInsets();
            decorView.getViewTreeObserver().addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
                public void onWindowDetached() {
                }

                public void onWindowAttached() {
                    boolean unused = ClipboardOverlayController.this.mBlockAttach = false;
                }
            });
        }
    }

    private void withWindowAttached(final Runnable runnable) {
        final View decorView = this.mWindow.getDecorView();
        if (decorView.isAttachedToWindow()) {
            runnable.run();
        } else {
            decorView.getViewTreeObserver().addOnWindowAttachListener(new ViewTreeObserver.OnWindowAttachListener() {
                public void onWindowDetached() {
                }

                public void onWindowAttached() {
                    boolean unused = ClipboardOverlayController.this.mBlockAttach = false;
                    decorView.getViewTreeObserver().removeOnWindowAttachListener(this);
                    runnable.run();
                }
            });
        }
    }

    private void updateInsets(WindowInsets windowInsets) {
        int i = this.mContext.getResources().getConfiguration().orientation;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mView.getLayoutParams();
        if (layoutParams != null) {
            DisplayCutout displayCutout = windowInsets.getDisplayCutout();
            Insets insets = windowInsets.getInsets(WindowInsets.Type.navigationBars());
            Insets insets2 = windowInsets.getInsets(WindowInsets.Type.ime());
            if (displayCutout == null) {
                layoutParams.setMargins(0, 0, 0, Math.max(insets2.bottom, insets.bottom));
            } else {
                Insets waterfallInsets = displayCutout.getWaterfallInsets();
                if (i == 1) {
                    layoutParams.setMargins(waterfallInsets.left, Math.max(displayCutout.getSafeInsetTop(), waterfallInsets.top), waterfallInsets.right, Math.max(insets2.bottom, Math.max(displayCutout.getSafeInsetBottom(), Math.max(insets.bottom, waterfallInsets.bottom))));
                } else {
                    layoutParams.setMargins(waterfallInsets.left, waterfallInsets.top, waterfallInsets.right, Math.max(insets2.bottom, Math.max(insets.bottom, waterfallInsets.bottom)));
                }
            }
            this.mView.setLayoutParams(layoutParams);
            this.mView.requestLayout();
        }
    }

    private Display getDefaultDisplay() {
        return this.mDisplayManager.getDisplay(0);
    }

    private void setWindowFocusable(boolean z) {
        View peekDecorView;
        int i = this.mWindowLayoutParams.flags;
        if (z) {
            this.mWindowLayoutParams.flags &= -9;
        } else {
            this.mWindowLayoutParams.flags |= 8;
        }
        if (this.mWindowLayoutParams.flags != i && (peekDecorView = this.mWindow.peekDecorView()) != null && peekDecorView.isAttachedToWindow()) {
            this.mWindowManager.updateViewLayout(peekDecorView, this.mWindowLayoutParams);
        }
    }
}
