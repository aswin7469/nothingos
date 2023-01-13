package com.android.p019wm.shell.onehanded;

import android.content.Context;
import android.graphics.Rect;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.SurfaceControl;
import android.window.DisplayAreaAppearedInfo;
import android.window.DisplayAreaInfo;
import android.window.DisplayAreaOrganizer;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.common.DisplayLayout;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.onehanded.OneHandedAnimationController;
import com.android.p019wm.shell.onehanded.OneHandedSurfaceTransactionHelper;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer */
public class OneHandedDisplayAreaOrganizer extends DisplayAreaOrganizer {
    private static final String ONE_HANDED_MODE_TRANSLATE_ANIMATION_DURATION = "persist.debug.one_handed_translate_animation_duration";
    private static final String TAG = "OneHandedDisplayAreaOrganizer";
    /* access modifiers changed from: private */
    public OneHandedAnimationController mAnimationController;
    private final Context mContext;
    private final Rect mDefaultDisplayBounds = new Rect();
    private ArrayMap<WindowContainerToken, SurfaceControl> mDisplayAreaTokenMap = new ArrayMap<>();
    private DisplayLayout mDisplayLayout = new DisplayLayout();
    private int mEnterExitAnimationDurationMs;
    private boolean mIsReady;
    private final InteractionJankMonitor mJankMonitor;
    private final Rect mLastVisualDisplayBounds = new Rect();
    private float mLastVisualOffset = 0.0f;
    OneHandedAnimationCallback mOneHandedAnimationCallback = new OneHandedAnimationCallback() {
        public void onOneHandedAnimationStart(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
            boolean z = oneHandedTransitionAnimator.getTransitionDirection() == 1;
            if (!OneHandedDisplayAreaOrganizer.this.mTransitionCallbacks.isEmpty()) {
                for (int size = OneHandedDisplayAreaOrganizer.this.mTransitionCallbacks.size() - 1; size >= 0; size--) {
                    ((OneHandedTransitionCallback) OneHandedDisplayAreaOrganizer.this.mTransitionCallbacks.get(size)).onStartTransition(z);
                }
            }
        }

        public void onOneHandedAnimationEnd(SurfaceControl.Transaction transaction, OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
            OneHandedDisplayAreaOrganizer.this.mAnimationController.removeAnimator(oneHandedTransitionAnimator.getToken());
            boolean z = true;
            if (oneHandedTransitionAnimator.getTransitionDirection() != 1) {
                z = false;
            }
            if (OneHandedDisplayAreaOrganizer.this.mAnimationController.isAnimatorsConsumed()) {
                OneHandedDisplayAreaOrganizer.this.endCUJTracing(z ? 42 : 43);
                OneHandedDisplayAreaOrganizer.this.finishOffset((int) oneHandedTransitionAnimator.getDestinationOffset(), oneHandedTransitionAnimator.getTransitionDirection());
            }
        }

        public void onOneHandedAnimationCancel(OneHandedAnimationController.OneHandedTransitionAnimator oneHandedTransitionAnimator) {
            OneHandedDisplayAreaOrganizer.this.mAnimationController.removeAnimator(oneHandedTransitionAnimator.getToken());
            boolean z = true;
            if (oneHandedTransitionAnimator.getTransitionDirection() != 1) {
                z = false;
            }
            if (OneHandedDisplayAreaOrganizer.this.mAnimationController.isAnimatorsConsumed()) {
                OneHandedDisplayAreaOrganizer.this.cancelCUJTracing(z ? 42 : 43);
                OneHandedDisplayAreaOrganizer.this.finishOffset((int) oneHandedTransitionAnimator.getDestinationOffset(), oneHandedTransitionAnimator.getTransitionDirection());
            }
        }
    };
    private final OneHandedSettingsUtil mOneHandedSettingsUtil;
    private OneHandedSurfaceTransactionHelper.SurfaceControlTransactionFactory mSurfaceControlTransactionFactory;
    /* access modifiers changed from: private */
    public List<OneHandedTransitionCallback> mTransitionCallbacks = new ArrayList();
    private OneHandedTutorialHandler mTutorialHandler;

    public static /* synthetic */ SurfaceControl.Transaction $r8$lambda$VwOlxiZ0_YlCJjS6K81dTETC6w4() {
        return new SurfaceControl.Transaction();
    }

    public OneHandedDisplayAreaOrganizer(Context context, DisplayLayout displayLayout, OneHandedSettingsUtil oneHandedSettingsUtil, OneHandedAnimationController oneHandedAnimationController, OneHandedTutorialHandler oneHandedTutorialHandler, InteractionJankMonitor interactionJankMonitor, ShellExecutor shellExecutor) {
        super(shellExecutor);
        this.mContext = context;
        setDisplayLayout(displayLayout);
        this.mOneHandedSettingsUtil = oneHandedSettingsUtil;
        this.mAnimationController = oneHandedAnimationController;
        this.mJankMonitor = interactionJankMonitor;
        this.mEnterExitAnimationDurationMs = SystemProperties.getInt(ONE_HANDED_MODE_TRANSLATE_ANIMATION_DURATION, context.getResources().getInteger(C3353R.integer.config_one_handed_translate_animation_duration));
        this.mSurfaceControlTransactionFactory = new OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda2();
        this.mTutorialHandler = oneHandedTutorialHandler;
    }

    public void onDisplayAreaAppeared(DisplayAreaInfo displayAreaInfo, SurfaceControl surfaceControl) {
        this.mDisplayAreaTokenMap.put(displayAreaInfo.token, surfaceControl);
    }

    public void onDisplayAreaVanished(DisplayAreaInfo displayAreaInfo) {
        this.mDisplayAreaTokenMap.remove(displayAreaInfo.token);
    }

    public List<DisplayAreaAppearedInfo> registerOrganizer(int i) {
        List<DisplayAreaAppearedInfo> registerOrganizer = OneHandedDisplayAreaOrganizer.super.registerOrganizer(i);
        for (int i2 = 0; i2 < registerOrganizer.size(); i2++) {
            DisplayAreaAppearedInfo displayAreaAppearedInfo = registerOrganizer.get(i2);
            onDisplayAreaAppeared(displayAreaAppearedInfo.getDisplayAreaInfo(), displayAreaAppearedInfo.getLeash());
        }
        this.mIsReady = true;
        updateDisplayBounds();
        return registerOrganizer;
    }

    public void unregisterOrganizer() {
        OneHandedDisplayAreaOrganizer.super.unregisterOrganizer();
        this.mIsReady = false;
        resetWindowsOffset();
    }

    /* access modifiers changed from: package-private */
    public boolean isReady() {
        return this.mIsReady;
    }

    public void onRotateDisplay(Context context, int i, WindowContainerTransaction windowContainerTransaction) {
        if (this.mDisplayLayout.rotation() != i) {
            this.mDisplayLayout.rotateTo(context.getResources(), i);
            updateDisplayBounds();
            finishOffset(0, 2);
        }
    }

    public void scheduleOffset(int i, int i2) {
        float f = this.mLastVisualOffset;
        int i3 = i2 > 0 ? 1 : 2;
        if (i3 == 1) {
            beginCUJTracing(42, "enterOneHanded");
        } else {
            beginCUJTracing(43, "stopOneHanded");
        }
        this.mDisplayAreaTokenMap.forEach(new OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda0(this, f, i2, i3));
        this.mLastVisualOffset = (float) i2;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$scheduleOffset$0$com-android-wm-shell-onehanded-OneHandedDisplayAreaOrganizer */
    public /* synthetic */ void mo49909x42db20c2(float f, int i, int i2, WindowContainerToken windowContainerToken, SurfaceControl surfaceControl) {
        animateWindows(windowContainerToken, surfaceControl, f, (float) i, i2, this.mEnterExitAnimationDurationMs);
    }

    /* access modifiers changed from: package-private */
    public void resetWindowsOffset() {
        SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
        this.mDisplayAreaTokenMap.forEach(new OneHandedDisplayAreaOrganizer$$ExternalSyntheticLambda1(this, transaction));
        transaction.apply();
        this.mLastVisualOffset = 0.0f;
        this.mLastVisualDisplayBounds.offsetTo(0, 0);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$resetWindowsOffset$1$com-android-wm-shell-onehanded-OneHandedDisplayAreaOrganizer */
    public /* synthetic */ void mo49908x4ed14564(SurfaceControl.Transaction transaction, WindowContainerToken windowContainerToken, SurfaceControl surfaceControl) {
        OneHandedAnimationController.OneHandedTransitionAnimator remove = this.mAnimationController.getAnimatorMap().remove(windowContainerToken);
        if (remove != null && remove.isRunning()) {
            remove.cancel();
        }
        transaction.setPosition(surfaceControl, 0.0f, 0.0f).setWindowCrop(surfaceControl, -1, -1).setCornerRadius(surfaceControl, -1.0f);
    }

    private void animateWindows(WindowContainerToken windowContainerToken, SurfaceControl surfaceControl, float f, float f2, int i, int i2) {
        OneHandedAnimationController.OneHandedTransitionAnimator animator = this.mAnimationController.getAnimator(windowContainerToken, surfaceControl, f, f2, this.mLastVisualDisplayBounds);
        if (animator != null) {
            animator.setTransitionDirection(i).addOneHandedAnimationCallback(this.mOneHandedAnimationCallback).addOneHandedAnimationCallback(this.mTutorialHandler).setDuration((long) i2).start();
        }
    }

    /* access modifiers changed from: package-private */
    public void finishOffset(int i, int i2) {
        if (i2 == 2) {
            resetWindowsOffset();
        }
        float f = i2 == 1 ? (float) i : 0.0f;
        this.mLastVisualOffset = f;
        this.mLastVisualDisplayBounds.offsetTo(0, Math.round(f));
        for (int size = this.mTransitionCallbacks.size() - 1; size >= 0; size--) {
            OneHandedTransitionCallback oneHandedTransitionCallback = this.mTransitionCallbacks.get(size);
            if (i2 == 1) {
                oneHandedTransitionCallback.onStartFinished(getLastVisualDisplayBounds());
            } else {
                oneHandedTransitionCallback.onStopFinished(getLastVisualDisplayBounds());
            }
        }
    }

    private Rect getLastVisualDisplayBounds() {
        return this.mLastVisualDisplayBounds;
    }

    /* access modifiers changed from: package-private */
    public Rect getLastDisplayBounds() {
        return this.mLastVisualDisplayBounds;
    }

    public DisplayLayout getDisplayLayout() {
        return this.mDisplayLayout;
    }

    /* access modifiers changed from: package-private */
    public void setDisplayLayout(DisplayLayout displayLayout) {
        this.mDisplayLayout.set(displayLayout);
        updateDisplayBounds();
    }

    /* access modifiers changed from: package-private */
    public ArrayMap<WindowContainerToken, SurfaceControl> getDisplayAreaTokenMap() {
        return this.mDisplayAreaTokenMap;
    }

    /* access modifiers changed from: package-private */
    public void updateDisplayBounds() {
        this.mDefaultDisplayBounds.set(0, 0, this.mDisplayLayout.width(), this.mDisplayLayout.height());
        this.mLastVisualDisplayBounds.set(this.mDefaultDisplayBounds);
    }

    public void registerTransitionCallback(OneHandedTransitionCallback oneHandedTransitionCallback) {
        this.mTransitionCallbacks.add(oneHandedTransitionCallback);
    }

    /* access modifiers changed from: package-private */
    public void beginCUJTracing(int i, String str) {
        InteractionJankMonitor.Configuration.Builder withSurface = InteractionJankMonitor.Configuration.Builder.withSurface(i, this.mContext, (SurfaceControl) getDisplayAreaTokenMap().entrySet().iterator().next().getValue());
        if (!TextUtils.isEmpty(str)) {
            withSurface.setTag(str);
        }
        this.mJankMonitor.begin(withSurface);
    }

    /* access modifiers changed from: package-private */
    public void endCUJTracing(int i) {
        this.mJankMonitor.end(i);
    }

    /* access modifiers changed from: package-private */
    public void cancelCUJTracing(int i) {
        this.mJankMonitor.cancel(i);
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.println(TAG);
        printWriter.print("  mDisplayLayout.rotation()=");
        printWriter.println(this.mDisplayLayout.rotation());
        printWriter.print("  mDisplayAreaTokenMap=");
        printWriter.println((Object) this.mDisplayAreaTokenMap);
        printWriter.print("  mDefaultDisplayBounds=");
        printWriter.println((Object) this.mDefaultDisplayBounds);
        printWriter.print("  mIsReady=");
        printWriter.println(this.mIsReady);
        printWriter.print("  mLastVisualDisplayBounds=");
        printWriter.println((Object) this.mLastVisualDisplayBounds);
        printWriter.print("  mLastVisualOffset=");
        printWriter.println(this.mLastVisualOffset);
        OneHandedAnimationController oneHandedAnimationController = this.mAnimationController;
        if (oneHandedAnimationController != null) {
            oneHandedAnimationController.dump(printWriter);
        }
    }
}
