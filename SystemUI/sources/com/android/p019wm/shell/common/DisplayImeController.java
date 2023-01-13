package com.android.p019wm.shell.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.Slog;
import android.util.SparseArray;
import android.view.IWindowManager;
import android.view.InsetsSource;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.InsetsVisibilities;
import android.view.SurfaceControl;
import android.view.WindowInsets;
import android.view.animation.Interpolator;
import android.view.animation.PathInterpolator;
import com.android.internal.view.IInputMethodManager;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.DisplayInsetsController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Executor;

/* renamed from: com.android.wm.shell.common.DisplayImeController */
public class DisplayImeController implements DisplayController.OnDisplaysChangedListener {
    public static final int ANIMATION_DURATION_HIDE_MS = 340;
    public static final int ANIMATION_DURATION_SHOW_MS = 275;
    private static final boolean DEBUG = false;
    private static final int DIRECTION_HIDE = 2;
    private static final int DIRECTION_NONE = 0;
    private static final int DIRECTION_SHOW = 1;
    private static final int FLOATING_IME_BOTTOM_INSET = -80;
    public static final Interpolator INTERPOLATOR = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f);
    private static final String TAG = "DisplayImeController";
    /* access modifiers changed from: private */
    public final DisplayController mDisplayController;
    /* access modifiers changed from: private */
    public final DisplayInsetsController mDisplayInsetsController;
    private final SparseArray<PerDisplay> mImePerDisplay = new SparseArray<>();
    protected final Executor mMainExecutor;
    private final ArrayList<ImePositionProcessor> mPositionProcessors = new ArrayList<>();
    /* access modifiers changed from: private */
    public final TransactionPool mTransactionPool;
    protected final IWindowManager mWmService;

    /* renamed from: com.android.wm.shell.common.DisplayImeController$ImePositionProcessor */
    public interface ImePositionProcessor {
        public static final int IME_ANIMATION_NO_ALPHA = 1;

        /* renamed from: com.android.wm.shell.common.DisplayImeController$ImePositionProcessor$ImeAnimationFlags */
        public @interface ImeAnimationFlags {
        }

        void onImeControlTargetChanged(int i, boolean z) {
        }

        void onImeEndPositioning(int i, boolean z, SurfaceControl.Transaction transaction) {
        }

        void onImePositionChanged(int i, int i2, SurfaceControl.Transaction transaction) {
        }

        int onImeStartPositioning(int i, int i2, int i3, boolean z, boolean z2, SurfaceControl.Transaction transaction) {
            return 0;
        }

        void onImeVisibilityChanged(int i, boolean z) {
        }
    }

    public DisplayImeController(IWindowManager iWindowManager, DisplayController displayController, DisplayInsetsController displayInsetsController, Executor executor, TransactionPool transactionPool) {
        this.mWmService = iWindowManager;
        this.mDisplayController = displayController;
        this.mDisplayInsetsController = displayInsetsController;
        this.mMainExecutor = executor;
        this.mTransactionPool = transactionPool;
    }

    public void startMonitorDisplays() {
        this.mDisplayController.addDisplayWindowListener(this);
    }

    public void onDisplayAdded(int i) {
        PerDisplay perDisplay = new PerDisplay(i, this.mDisplayController.getDisplayLayout(i).rotation());
        perDisplay.register();
        this.mImePerDisplay.put(i, perDisplay);
    }

    public void onDisplayConfigurationChanged(int i, Configuration configuration) {
        PerDisplay perDisplay = this.mImePerDisplay.get(i);
        if (perDisplay != null && this.mDisplayController.getDisplayLayout(i).rotation() != perDisplay.mRotation && isImeShowing(i)) {
            perDisplay.startAnimation(true, false);
        }
    }

    public void onDisplayRemoved(int i) {
        PerDisplay perDisplay = this.mImePerDisplay.get(i);
        if (perDisplay != null) {
            perDisplay.unregister();
            this.mImePerDisplay.remove(i);
        }
    }

    private boolean isImeShowing(int i) {
        InsetsSource source;
        PerDisplay perDisplay = this.mImePerDisplay.get(i);
        if (perDisplay == null || (source = perDisplay.mInsetsState.getSource(19)) == null || perDisplay.mImeSourceControl == null || !source.isVisible()) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: private */
    public void dispatchPositionChanged(int i, int i2, SurfaceControl.Transaction transaction) {
        synchronized (this.mPositionProcessors) {
            Iterator<ImePositionProcessor> it = this.mPositionProcessors.iterator();
            while (it.hasNext()) {
                it.next().onImePositionChanged(i, i2, transaction);
            }
        }
    }

    /* access modifiers changed from: private */
    public int dispatchStartPositioning(int i, int i2, int i3, boolean z, boolean z2, SurfaceControl.Transaction transaction) {
        int i4;
        synchronized (this.mPositionProcessors) {
            Iterator<ImePositionProcessor> it = this.mPositionProcessors.iterator();
            i4 = 0;
            while (it.hasNext()) {
                i4 |= it.next().onImeStartPositioning(i, i2, i3, z, z2, transaction);
            }
        }
        return i4;
    }

    /* access modifiers changed from: private */
    public void dispatchEndPositioning(int i, boolean z, SurfaceControl.Transaction transaction) {
        synchronized (this.mPositionProcessors) {
            Iterator<ImePositionProcessor> it = this.mPositionProcessors.iterator();
            while (it.hasNext()) {
                it.next().onImeEndPositioning(i, z, transaction);
            }
        }
    }

    /* access modifiers changed from: private */
    public void dispatchImeControlTargetChanged(int i, boolean z) {
        synchronized (this.mPositionProcessors) {
            Iterator<ImePositionProcessor> it = this.mPositionProcessors.iterator();
            while (it.hasNext()) {
                it.next().onImeControlTargetChanged(i, z);
            }
        }
    }

    /* access modifiers changed from: private */
    public void dispatchVisibilityChanged(int i, boolean z) {
        synchronized (this.mPositionProcessors) {
            Iterator<ImePositionProcessor> it = this.mPositionProcessors.iterator();
            while (it.hasNext()) {
                it.next().onImeVisibilityChanged(i, z);
            }
        }
    }

    public void addPositionProcessor(ImePositionProcessor imePositionProcessor) {
        synchronized (this.mPositionProcessors) {
            if (!this.mPositionProcessors.contains(imePositionProcessor)) {
                this.mPositionProcessors.add(imePositionProcessor);
            }
        }
    }

    public void removePositionProcessor(ImePositionProcessor imePositionProcessor) {
        synchronized (this.mPositionProcessors) {
            this.mPositionProcessors.remove((Object) imePositionProcessor);
        }
    }

    /* renamed from: com.android.wm.shell.common.DisplayImeController$PerDisplay */
    public class PerDisplay implements DisplayInsetsController.OnInsetsChangedListener {
        boolean mAnimateAlpha = true;
        ValueAnimator mAnimation = null;
        int mAnimationDirection = 0;
        final int mDisplayId;
        final Rect mImeFrame = new Rect();
        boolean mImeShowing = false;
        InsetsSourceControl mImeSourceControl = null;
        final InsetsState mInsetsState = new InsetsState();
        final InsetsVisibilities mRequestedVisibilities = new InsetsVisibilities();
        int mRotation = 0;

        public void topFocusedWindowChanged(String str, InsetsVisibilities insetsVisibilities) {
        }

        public PerDisplay(int i, int i2) {
            this.mDisplayId = i;
            this.mRotation = i2;
        }

        public void register() {
            DisplayImeController.this.mDisplayInsetsController.addInsetsChangedListener(this.mDisplayId, this);
        }

        public void unregister() {
            DisplayImeController.this.mDisplayInsetsController.removeInsetsChangedListener(this.mDisplayId, this);
        }

        public void insetsChanged(InsetsState insetsState) {
            if (!this.mInsetsState.equals(insetsState)) {
                updateImeVisibility(insetsState.getSourceOrDefaultVisibility(19));
                InsetsSource source = insetsState.getSource(19);
                Rect frame = source.getFrame();
                Rect frame2 = this.mInsetsState.getSource(19).getFrame();
                this.mInsetsState.set(insetsState, true);
                if (this.mImeShowing && !frame.equals(frame2) && source.isVisible()) {
                    startAnimation(this.mImeShowing, true);
                }
            }
        }

        public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
            InsetsSourceControl insetsSourceControl;
            insetsChanged(insetsState);
            boolean z = false;
            Point point = null;
            if (insetsSourceControlArr != null) {
                insetsSourceControl = null;
                for (InsetsSourceControl insetsSourceControl2 : insetsSourceControlArr) {
                    if (insetsSourceControl2 != null && insetsSourceControl2.getType() == 19) {
                        insetsSourceControl = insetsSourceControl2;
                    }
                }
            } else {
                insetsSourceControl = null;
            }
            boolean z2 = this.mImeSourceControl != null;
            if (insetsSourceControl != null) {
                z = true;
            }
            if (z2 != z) {
                DisplayImeController.this.dispatchImeControlTargetChanged(this.mDisplayId, z);
            }
            if (z) {
                InsetsSourceControl insetsSourceControl3 = this.mImeSourceControl;
                if (insetsSourceControl3 != null) {
                    point = insetsSourceControl3.getSurfacePosition();
                }
                boolean z3 = !insetsSourceControl.getSurfacePosition().equals(point);
                boolean z4 = !DisplayImeController.haveSameLeash(this.mImeSourceControl, insetsSourceControl);
                if (this.mAnimation == null) {
                    if (z4) {
                        applyVisibilityToLeash(insetsSourceControl);
                    }
                    if (!this.mImeShowing) {
                        DisplayImeController.this.removeImeSurface();
                    }
                    InsetsSourceControl insetsSourceControl4 = this.mImeSourceControl;
                    if (insetsSourceControl4 != null) {
                        insetsSourceControl4.release(new DisplayImeController$PerDisplay$$ExternalSyntheticLambda1());
                    }
                } else if (z3) {
                    startAnimation(this.mImeShowing, true);
                }
                this.mImeSourceControl = insetsSourceControl;
            }
        }

        private void applyVisibilityToLeash(InsetsSourceControl insetsSourceControl) {
            SurfaceControl leash = insetsSourceControl.getLeash();
            if (leash != null) {
                SurfaceControl.Transaction acquire = DisplayImeController.this.mTransactionPool.acquire();
                if (this.mImeShowing) {
                    acquire.show(leash);
                } else {
                    acquire.hide(leash);
                }
                acquire.apply();
                DisplayImeController.this.mTransactionPool.release(acquire);
            }
        }

        public void showInsets(int i, boolean z) {
            if ((i & WindowInsets.Type.ime()) != 0) {
                startAnimation(true, false);
            }
        }

        public void hideInsets(int i, boolean z) {
            if ((i & WindowInsets.Type.ime()) != 0) {
                startAnimation(false, false);
            }
        }

        private void setVisibleDirectly(boolean z) {
            this.mInsetsState.getSource(19).setVisible(z);
            this.mRequestedVisibilities.setVisibility(19, z);
            try {
                DisplayImeController.this.mWmService.updateDisplayWindowRequestedVisibilities(this.mDisplayId, this.mRequestedVisibilities);
            } catch (RemoteException unused) {
            }
        }

        /* access modifiers changed from: private */
        public int imeTop(float f) {
            return this.mImeFrame.top + ((int) f);
        }

        private boolean calcIsFloating(InsetsSource insetsSource) {
            Rect frame = insetsSource.getFrame();
            if (frame.height() != 0 && frame.height() > DisplayImeController.this.mDisplayController.getDisplayLayout(this.mDisplayId).navBarFrameHeight()) {
                return false;
            }
            return true;
        }

        /* access modifiers changed from: private */
        public void startAnimation(boolean z, boolean z2) {
            boolean z3;
            InsetsSource source = this.mInsetsState.getSource(19);
            if (source != null && this.mImeSourceControl != null) {
                Rect frame = source.getFrame();
                final boolean z4 = calcIsFloating(source) && z;
                if (z4) {
                    this.mImeFrame.set(frame);
                    this.mImeFrame.bottom -= (int) (DisplayImeController.this.mDisplayController.getDisplayLayout(this.mDisplayId).density() * -80.0f);
                } else if (frame.height() != 0) {
                    this.mImeFrame.set(frame);
                }
                if (!z2 && this.mAnimationDirection == 1 && z) {
                    return;
                }
                if (this.mAnimationDirection != 2 || z) {
                    ValueAnimator valueAnimator = this.mAnimation;
                    float f = 0.0f;
                    if (valueAnimator != null) {
                        if (valueAnimator.isRunning()) {
                            f = ((Float) this.mAnimation.getAnimatedValue()).floatValue();
                            z3 = true;
                        } else {
                            z3 = false;
                        }
                        this.mAnimation.cancel();
                    } else {
                        z3 = false;
                    }
                    float f2 = (float) this.mImeSourceControl.getSurfacePosition().y;
                    float f3 = (float) this.mImeSourceControl.getSurfacePosition().x;
                    float height = f2 + ((float) this.mImeFrame.height());
                    float f4 = z ? height : f2;
                    float f5 = z ? f2 : height;
                    if (this.mAnimationDirection == 0 && this.mImeShowing && z) {
                        f = f2;
                        z3 = true;
                    }
                    this.mAnimationDirection = z ? 1 : 2;
                    updateImeVisibility(z);
                    ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{f4, f5});
                    this.mAnimation = ofFloat;
                    ofFloat.setDuration(z ? 275 : 340);
                    if (z3) {
                        this.mAnimation.setCurrentFraction((f - f4) / (f5 - f4));
                    }
                    ValueAnimator valueAnimator2 = this.mAnimation;
                    final float f6 = f3;
                    final float f7 = height;
                    DisplayImeController$PerDisplay$$ExternalSyntheticLambda0 displayImeController$PerDisplay$$ExternalSyntheticLambda0 = r0;
                    final float f8 = f2;
                    DisplayImeController$PerDisplay$$ExternalSyntheticLambda0 displayImeController$PerDisplay$$ExternalSyntheticLambda02 = new DisplayImeController$PerDisplay$$ExternalSyntheticLambda0(this, f6, z4, f7, f8);
                    valueAnimator2.addUpdateListener(displayImeController$PerDisplay$$ExternalSyntheticLambda0);
                    this.mAnimation.setInterpolator(DisplayImeController.INTERPOLATOR);
                    final float f9 = f4;
                    final float f10 = f5;
                    this.mAnimation.addListener(new AnimatorListenerAdapter() {
                        private boolean mCancelled = false;

                        public void onAnimationStart(Animator animator) {
                            float f;
                            SurfaceControl.Transaction acquire = DisplayImeController.this.mTransactionPool.acquire();
                            acquire.setPosition(PerDisplay.this.mImeSourceControl.getLeash(), f6, f9);
                            boolean z = false;
                            int access$700 = DisplayImeController.this.dispatchStartPositioning(PerDisplay.this.mDisplayId, PerDisplay.this.imeTop(f7), PerDisplay.this.imeTop(f8), PerDisplay.this.mAnimationDirection == 1, z4, acquire);
                            PerDisplay perDisplay = PerDisplay.this;
                            if ((access$700 & 1) == 0) {
                                z = true;
                            }
                            perDisplay.mAnimateAlpha = z;
                            if (PerDisplay.this.mAnimateAlpha || z4) {
                                float f2 = f9;
                                float f3 = f7;
                                f = (f2 - f3) / (f8 - f3);
                            } else {
                                f = 1.0f;
                            }
                            acquire.setAlpha(PerDisplay.this.mImeSourceControl.getLeash(), f);
                            if (PerDisplay.this.mAnimationDirection == 1) {
                                acquire.show(PerDisplay.this.mImeSourceControl.getLeash());
                            }
                            acquire.apply();
                            DisplayImeController.this.mTransactionPool.release(acquire);
                        }

                        public void onAnimationCancel(Animator animator) {
                            this.mCancelled = true;
                        }

                        public void onAnimationEnd(Animator animator) {
                            SurfaceControl.Transaction acquire = DisplayImeController.this.mTransactionPool.acquire();
                            if (!this.mCancelled) {
                                acquire.setPosition(PerDisplay.this.mImeSourceControl.getLeash(), f6, f10);
                                acquire.setAlpha(PerDisplay.this.mImeSourceControl.getLeash(), 1.0f);
                            }
                            DisplayImeController.this.dispatchEndPositioning(PerDisplay.this.mDisplayId, this.mCancelled, acquire);
                            if (PerDisplay.this.mAnimationDirection == 2 && !this.mCancelled) {
                                acquire.hide(PerDisplay.this.mImeSourceControl.getLeash());
                                DisplayImeController.this.removeImeSurface();
                            }
                            acquire.apply();
                            DisplayImeController.this.mTransactionPool.release(acquire);
                            PerDisplay.this.mAnimationDirection = 0;
                            PerDisplay.this.mAnimation = null;
                        }
                    });
                    if (!z) {
                        setVisibleDirectly(false);
                    }
                    this.mAnimation.start();
                    if (z) {
                        setVisibleDirectly(true);
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$startAnimation$0$com-android-wm-shell-common-DisplayImeController$PerDisplay */
        public /* synthetic */ void mo49065x97ad683d(float f, boolean z, float f2, float f3, ValueAnimator valueAnimator) {
            SurfaceControl.Transaction acquire = DisplayImeController.this.mTransactionPool.acquire();
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            acquire.setPosition(this.mImeSourceControl.getLeash(), f, floatValue);
            acquire.setAlpha(this.mImeSourceControl.getLeash(), (this.mAnimateAlpha || z) ? (floatValue - f2) / (f3 - f2) : 1.0f);
            DisplayImeController.this.dispatchPositionChanged(this.mDisplayId, imeTop(floatValue), acquire);
            acquire.apply();
            DisplayImeController.this.mTransactionPool.release(acquire);
        }

        private void updateImeVisibility(boolean z) {
            if (this.mImeShowing != z) {
                this.mImeShowing = z;
                DisplayImeController.this.dispatchVisibilityChanged(this.mDisplayId, z);
            }
        }

        public InsetsSourceControl getImeSourceControl() {
            return this.mImeSourceControl;
        }
    }

    /* access modifiers changed from: package-private */
    public void removeImeSurface() {
        IInputMethodManager imms = getImms();
        if (imms != null) {
            try {
                imms.removeImeSurface();
            } catch (RemoteException e) {
                Slog.e(TAG, "Failed to remove IME surface.", e);
            }
        }
    }

    public IInputMethodManager getImms() {
        return IInputMethodManager.Stub.asInterface(ServiceManager.getService("input_method"));
    }

    /* access modifiers changed from: private */
    public static boolean haveSameLeash(InsetsSourceControl insetsSourceControl, InsetsSourceControl insetsSourceControl2) {
        if (insetsSourceControl == insetsSourceControl2) {
            return true;
        }
        if (!(insetsSourceControl == null || insetsSourceControl2 == null)) {
            if (insetsSourceControl.getLeash() == insetsSourceControl2.getLeash()) {
                return true;
            }
            if (!(insetsSourceControl.getLeash() == null || insetsSourceControl2.getLeash() == null)) {
                return insetsSourceControl.getLeash().isSameSurface(insetsSourceControl2.getLeash());
            }
        }
        return false;
    }
}
