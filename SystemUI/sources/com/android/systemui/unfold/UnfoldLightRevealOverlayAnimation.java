package com.android.systemui.unfold;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.hardware.devicestate.DeviceStateManager;
import android.hardware.display.DisplayManager;
import android.hardware.input.InputManager;
import android.os.IBinder;
import android.os.Trace;
import android.util.AttributeSet;
import android.view.Choreographer;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.IRotationWatcher;
import android.view.IWindowManager;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import com.android.p019wm.shell.displayareahelper.DisplayAreaHelper;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dagger.qualifiers.UiBackground;
import com.android.systemui.statusbar.LightRevealEffect;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.LinearLightRevealEffect;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt;

@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001:\u0003234BQ\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000b\u0012\b\b\u0001\u0010\r\u001a\u00020\u000e\u0012\b\b\u0001\u0010\u000f\u001a\u00020\u000e\u0012\u0006\u0010\u0010\u001a\u00020\u0011¢\u0006\u0002\u0010\u0012J\u0014\u0010&\u001a\u00020'2\n\b\u0002\u0010(\u001a\u0004\u0018\u00010)H\u0002J\b\u0010*\u001a\u00020+H\u0002J\b\u0010,\u001a\u00020'H\u0002J\b\u0010-\u001a\u00020.H\u0002J\b\u0010/\u001a\u00020#H\u0002J\u0006\u00100\u001a\u00020'J\u000e\u00101\u001a\u00020'2\u0006\u0010(\u001a\u00020)R\u000e\u0010\u000f\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X.¢\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u000e¢\u0006\u0002\n\u0000R\u0012\u0010\u001c\u001a\u00060\u001dR\u00020\u0000X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u0004\u0018\u00010\u001fX\u000e¢\u0006\u0002\n\u0000R\u0012\u0010 \u001a\u00060!R\u00020\u0000X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X.¢\u0006\u0002\n\u0000¨\u00065"}, mo64987d2 = {"Lcom/android/systemui/unfold/UnfoldLightRevealOverlayAnimation;", "", "context", "Landroid/content/Context;", "deviceStateManager", "Landroid/hardware/devicestate/DeviceStateManager;", "displayManager", "Landroid/hardware/display/DisplayManager;", "unfoldTransitionProgressProvider", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider;", "displayAreaHelper", "Ljava/util/Optional;", "Lcom/android/wm/shell/displayareahelper/DisplayAreaHelper;", "executor", "Ljava/util/concurrent/Executor;", "backgroundExecutor", "windowManagerInterface", "Landroid/view/IWindowManager;", "(Landroid/content/Context;Landroid/hardware/devicestate/DeviceStateManager;Landroid/hardware/display/DisplayManager;Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider;Ljava/util/Optional;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Landroid/view/IWindowManager;)V", "currentRotation", "", "isFolded", "", "isUnfoldHandled", "overlayContainer", "Landroid/view/SurfaceControl;", "root", "Landroid/view/SurfaceControlViewHost;", "rotationWatcher", "Lcom/android/systemui/unfold/UnfoldLightRevealOverlayAnimation$RotationWatcher;", "scrimView", "Lcom/android/systemui/statusbar/LightRevealScrim;", "transitionListener", "Lcom/android/systemui/unfold/UnfoldLightRevealOverlayAnimation$TransitionListener;", "unfoldedDisplayInfo", "Landroid/view/DisplayInfo;", "wwm", "Landroid/view/WindowlessWindowManager;", "addView", "", "onOverlayReady", "Ljava/lang/Runnable;", "createLightRevealEffect", "Lcom/android/systemui/statusbar/LightRevealEffect;", "ensureOverlayRemoved", "getLayoutParams", "Landroid/view/WindowManager$LayoutParams;", "getUnfoldedDisplayInfo", "init", "onScreenTurningOn", "FoldListener", "RotationWatcher", "TransitionListener", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
@SysUIUnfoldScope
/* compiled from: UnfoldLightRevealOverlayAnimation.kt */
public final class UnfoldLightRevealOverlayAnimation {
    private final Executor backgroundExecutor;
    /* access modifiers changed from: private */
    public final Context context;
    /* access modifiers changed from: private */
    public int currentRotation;
    private final DeviceStateManager deviceStateManager;
    private final Optional<DisplayAreaHelper> displayAreaHelper;
    private final DisplayManager displayManager;
    private final Executor executor;
    /* access modifiers changed from: private */
    public boolean isFolded;
    /* access modifiers changed from: private */
    public boolean isUnfoldHandled = true;
    private SurfaceControl overlayContainer;
    /* access modifiers changed from: private */
    public SurfaceControlViewHost root;
    private final RotationWatcher rotationWatcher = new RotationWatcher();
    /* access modifiers changed from: private */
    public LightRevealScrim scrimView;
    private final TransitionListener transitionListener = new TransitionListener();
    private final UnfoldTransitionProgressProvider unfoldTransitionProgressProvider;
    private DisplayInfo unfoldedDisplayInfo;
    private final IWindowManager windowManagerInterface;
    private WindowlessWindowManager wwm;

    /* access modifiers changed from: private */
    /* renamed from: addView$lambda-3$lambda-2  reason: not valid java name */
    public static final void m3271addView$lambda3$lambda2(Boolean bool) {
        Intrinsics.checkNotNullParameter(bool, "it");
    }

    @Inject
    public UnfoldLightRevealOverlayAnimation(Context context2, DeviceStateManager deviceStateManager2, DisplayManager displayManager2, UnfoldTransitionProgressProvider unfoldTransitionProgressProvider2, Optional<DisplayAreaHelper> optional, @Main Executor executor2, @UiBackground Executor executor3, IWindowManager iWindowManager) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(deviceStateManager2, "deviceStateManager");
        Intrinsics.checkNotNullParameter(displayManager2, "displayManager");
        Intrinsics.checkNotNullParameter(unfoldTransitionProgressProvider2, "unfoldTransitionProgressProvider");
        Intrinsics.checkNotNullParameter(optional, "displayAreaHelper");
        Intrinsics.checkNotNullParameter(executor2, "executor");
        Intrinsics.checkNotNullParameter(executor3, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(iWindowManager, "windowManagerInterface");
        this.context = context2;
        this.deviceStateManager = deviceStateManager2;
        this.displayManager = displayManager2;
        this.unfoldTransitionProgressProvider = unfoldTransitionProgressProvider2;
        this.displayAreaHelper = optional;
        this.executor = executor2;
        this.backgroundExecutor = executor3;
        this.windowManagerInterface = iWindowManager;
        Display display = context2.getDisplay();
        Intrinsics.checkNotNull(display);
        this.currentRotation = display.getRotation();
    }

    public final void init() {
        this.deviceStateManager.registerCallback(this.executor, new FoldListener());
        this.unfoldTransitionProgressProvider.addCallback(this.transitionListener);
        this.windowManagerInterface.watchRotation(this.rotationWatcher, this.context.getDisplay().getDisplayId());
        this.displayAreaHelper.get().attachToRootDisplayArea(0, new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName("unfold-overlay-container"), new UnfoldLightRevealOverlayAnimation$$ExternalSyntheticLambda1(this));
        this.unfoldedDisplayInfo = getUnfoldedDisplayInfo();
    }

    /* access modifiers changed from: private */
    /* renamed from: init$lambda-1  reason: not valid java name */
    public static final void m3274init$lambda1(UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation, SurfaceControl.Builder builder) {
        Intrinsics.checkNotNullParameter(unfoldLightRevealOverlayAnimation, "this$0");
        unfoldLightRevealOverlayAnimation.executor.execute(new UnfoldLightRevealOverlayAnimation$$ExternalSyntheticLambda0(unfoldLightRevealOverlayAnimation, builder));
    }

    /* access modifiers changed from: private */
    /* renamed from: init$lambda-1$lambda-0  reason: not valid java name */
    public static final void m3275init$lambda1$lambda0(UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation, SurfaceControl.Builder builder) {
        Intrinsics.checkNotNullParameter(unfoldLightRevealOverlayAnimation, "this$0");
        SurfaceControl build = builder.build();
        Intrinsics.checkNotNullExpressionValue(build, "builder.build()");
        unfoldLightRevealOverlayAnimation.overlayContainer = build;
        SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
        SurfaceControl surfaceControl = unfoldLightRevealOverlayAnimation.overlayContainer;
        if (surfaceControl == null) {
            Intrinsics.throwUninitializedPropertyAccessException("overlayContainer");
            surfaceControl = null;
        }
        SurfaceControl.Transaction layer = transaction.setLayer(surfaceControl, Integer.MAX_VALUE);
        SurfaceControl surfaceControl2 = unfoldLightRevealOverlayAnimation.overlayContainer;
        if (surfaceControl2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("overlayContainer");
            surfaceControl2 = null;
        }
        layer.show(surfaceControl2).apply();
        Configuration configuration = unfoldLightRevealOverlayAnimation.context.getResources().getConfiguration();
        SurfaceControl surfaceControl3 = unfoldLightRevealOverlayAnimation.overlayContainer;
        if (surfaceControl3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("overlayContainer");
            surfaceControl3 = null;
        }
        unfoldLightRevealOverlayAnimation.wwm = new WindowlessWindowManager(configuration, surfaceControl3, (IBinder) null);
    }

    public final void onScreenTurningOn(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "onOverlayReady");
        Trace.beginSection("UnfoldLightRevealOverlayAnimation#onScreenTurningOn");
        try {
            if (this.isFolded || this.isUnfoldHandled || !ValueAnimator.areAnimatorsEnabled()) {
                ensureOverlayRemoved();
                runnable.run();
            } else {
                addView(runnable);
                this.isUnfoldHandled = true;
            }
        } finally {
            Trace.endSection();
        }
    }

    static /* synthetic */ void addView$default(UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation, Runnable runnable, int i, Object obj) {
        if ((i & 1) != 0) {
            runnable = null;
        }
        unfoldLightRevealOverlayAnimation.addView(runnable);
    }

    private final void addView(Runnable runnable) {
        if (this.wwm != null) {
            ensureOverlayRemoved();
            Context context2 = this.context;
            Display display = context2.getDisplay();
            Intrinsics.checkNotNull(display);
            WindowlessWindowManager windowlessWindowManager = this.wwm;
            if (windowlessWindowManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("wwm");
                windowlessWindowManager = null;
            }
            SurfaceControlViewHost surfaceControlViewHost = new SurfaceControlViewHost(context2, display, windowlessWindowManager, false);
            LightRevealScrim lightRevealScrim = new LightRevealScrim(this.context, (AttributeSet) null);
            lightRevealScrim.setRevealEffect(createLightRevealEffect());
            lightRevealScrim.setScrimOpaqueChangedListener(new UnfoldLightRevealOverlayAnimation$$ExternalSyntheticLambda2());
            lightRevealScrim.setRevealAmount(0.0f);
            WindowManager.LayoutParams layoutParams = getLayoutParams();
            surfaceControlViewHost.setView(lightRevealScrim, layoutParams);
            if (runnable != null) {
                Trace.beginAsyncSection("UnfoldLightRevealOverlayAnimation#relayout", 0);
                surfaceControlViewHost.relayout(layoutParams, new UnfoldLightRevealOverlayAnimation$$ExternalSyntheticLambda3(this, runnable));
            }
            this.scrimView = lightRevealScrim;
            this.root = surfaceControlViewHost;
        } else if (runnable != null) {
            runnable.run();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: addView$lambda-6$lambda-5  reason: not valid java name */
    public static final void m3272addView$lambda6$lambda5(UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation, Runnable runnable, SurfaceControl.Transaction transaction) {
        Intrinsics.checkNotNullParameter(unfoldLightRevealOverlayAnimation, "this$0");
        Intrinsics.checkNotNullParameter(runnable, "$callback");
        long vsyncId = Choreographer.getSfInstance().getVsyncId();
        transaction.setFrameTimelineVsync(vsyncId).apply();
        transaction.setFrameTimelineVsync(vsyncId + 1).addTransactionCommittedListener(unfoldLightRevealOverlayAnimation.backgroundExecutor, new UnfoldLightRevealOverlayAnimation$$ExternalSyntheticLambda4(runnable)).apply();
    }

    /* access modifiers changed from: private */
    /* renamed from: addView$lambda-6$lambda-5$lambda-4  reason: not valid java name */
    public static final void m3273addView$lambda6$lambda5$lambda4(Runnable runnable) {
        Intrinsics.checkNotNullParameter(runnable, "$callback");
        Trace.endAsyncSection("UnfoldLightRevealOverlayAnimation#relayout", 0);
        runnable.run();
    }

    /* access modifiers changed from: private */
    public final WindowManager.LayoutParams getLayoutParams() {
        int i;
        int i2;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        int i3 = this.currentRotation;
        boolean z = i3 == 0 || i3 == 2;
        DisplayInfo displayInfo = null;
        DisplayInfo displayInfo2 = this.unfoldedDisplayInfo;
        if (z) {
            if (displayInfo2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("unfoldedDisplayInfo");
                displayInfo2 = null;
            }
            i = displayInfo2.getNaturalHeight();
        } else {
            if (displayInfo2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("unfoldedDisplayInfo");
                displayInfo2 = null;
            }
            i = displayInfo2.getNaturalWidth();
        }
        layoutParams.height = i;
        if (z) {
            DisplayInfo displayInfo3 = this.unfoldedDisplayInfo;
            if (displayInfo3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("unfoldedDisplayInfo");
            } else {
                displayInfo = displayInfo3;
            }
            i2 = displayInfo.getNaturalWidth();
        } else {
            DisplayInfo displayInfo4 = this.unfoldedDisplayInfo;
            if (displayInfo4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("unfoldedDisplayInfo");
            } else {
                displayInfo = displayInfo4;
            }
            i2 = displayInfo.getNaturalHeight();
        }
        layoutParams.width = i2;
        layoutParams.format = -3;
        layoutParams.type = 2026;
        layoutParams.setTitle("Unfold Light Reveal Animation");
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.flags = 8;
        layoutParams.setTrustedOverlay();
        String opPackageName = this.context.getOpPackageName();
        Intrinsics.checkNotNullExpressionValue(opPackageName, "context.opPackageName");
        layoutParams.packageName = opPackageName;
        return layoutParams;
    }

    /* access modifiers changed from: private */
    public final LightRevealEffect createLightRevealEffect() {
        int i = this.currentRotation;
        return new LinearLightRevealEffect(i == 0 || i == 2);
    }

    /* access modifiers changed from: private */
    public final void ensureOverlayRemoved() {
        SurfaceControlViewHost surfaceControlViewHost = this.root;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
        }
        this.root = null;
        this.scrimView = null;
    }

    private final DisplayInfo getUnfoldedDisplayInfo() {
        Object obj;
        Display[] displays = this.displayManager.getDisplays();
        Intrinsics.checkNotNullExpressionValue(displays, "displayManager\n            .displays");
        Iterator it = SequencesKt.filter(SequencesKt.map(ArraysKt.asSequence((T[]) (Object[]) displays), UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$1.INSTANCE), UnfoldLightRevealOverlayAnimation$getUnfoldedDisplayInfo$2.INSTANCE).iterator();
        if (!it.hasNext()) {
            obj = null;
        } else {
            Object next = it.next();
            if (!it.hasNext()) {
                obj = next;
            } else {
                int naturalWidth = ((DisplayInfo) next).getNaturalWidth();
                do {
                    Object next2 = it.next();
                    int naturalWidth2 = ((DisplayInfo) next2).getNaturalWidth();
                    if (naturalWidth < naturalWidth2) {
                        next = next2;
                        naturalWidth = naturalWidth2;
                    }
                } while (it.hasNext());
            }
            obj = next;
        }
        Intrinsics.checkNotNull(obj);
        return (DisplayInfo) obj;
    }

    @Metadata(mo64986d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0016J\u0010\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u0010\b\u001a\u00020\u0004H\u0016¨\u0006\t"}, mo64987d2 = {"Lcom/android/systemui/unfold/UnfoldLightRevealOverlayAnimation$TransitionListener;", "Lcom/android/systemui/unfold/UnfoldTransitionProgressProvider$TransitionProgressListener;", "(Lcom/android/systemui/unfold/UnfoldLightRevealOverlayAnimation;)V", "onTransitionFinished", "", "onTransitionProgress", "progress", "", "onTransitionStarted", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: UnfoldLightRevealOverlayAnimation.kt */
    private final class TransitionListener implements UnfoldTransitionProgressProvider.TransitionProgressListener {
        public TransitionListener() {
        }

        public void onTransitionProgress(float f) {
            LightRevealScrim access$getScrimView$p = UnfoldLightRevealOverlayAnimation.this.scrimView;
            if (access$getScrimView$p != null) {
                access$getScrimView$p.setRevealAmount(f);
            }
        }

        public void onTransitionFinished() {
            UnfoldLightRevealOverlayAnimation.this.ensureOverlayRemoved();
        }

        public void onTransitionStarted() {
            if (UnfoldLightRevealOverlayAnimation.this.scrimView == null) {
                UnfoldLightRevealOverlayAnimation.addView$default(UnfoldLightRevealOverlayAnimation.this, (Runnable) null, 1, (Object) null);
            }
            InputManager.getInstance().cancelCurrentTouch();
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0007"}, mo64987d2 = {"Lcom/android/systemui/unfold/UnfoldLightRevealOverlayAnimation$RotationWatcher;", "Landroid/view/IRotationWatcher$Stub;", "(Lcom/android/systemui/unfold/UnfoldLightRevealOverlayAnimation;)V", "onRotationChanged", "", "newRotation", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: UnfoldLightRevealOverlayAnimation.kt */
    private final class RotationWatcher extends IRotationWatcher.Stub {
        public RotationWatcher() {
        }

        public void onRotationChanged(int i) {
            UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation = UnfoldLightRevealOverlayAnimation.this;
            Trace.beginSection("UnfoldLightRevealOverlayAnimation#onRotationChanged");
            try {
                if (unfoldLightRevealOverlayAnimation.currentRotation != i) {
                    unfoldLightRevealOverlayAnimation.currentRotation = i;
                    LightRevealScrim access$getScrimView$p = unfoldLightRevealOverlayAnimation.scrimView;
                    if (access$getScrimView$p != null) {
                        access$getScrimView$p.setRevealEffect(unfoldLightRevealOverlayAnimation.createLightRevealEffect());
                    }
                    SurfaceControlViewHost access$getRoot$p = unfoldLightRevealOverlayAnimation.root;
                    if (access$getRoot$p != null) {
                        access$getRoot$p.relayout(unfoldLightRevealOverlayAnimation.getLayoutParams());
                    }
                }
                Unit unit = Unit.INSTANCE;
            } finally {
                Trace.endSection();
            }
        }
    }

    @Metadata(mo64986d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, mo64987d2 = {"Lcom/android/systemui/unfold/UnfoldLightRevealOverlayAnimation$FoldListener;", "Landroid/hardware/devicestate/DeviceStateManager$FoldStateListener;", "(Lcom/android/systemui/unfold/UnfoldLightRevealOverlayAnimation;)V", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: UnfoldLightRevealOverlayAnimation.kt */
    private final class FoldListener extends DeviceStateManager.FoldStateListener {
        public FoldListener() {
            super(UnfoldLightRevealOverlayAnimation.this.context, new C3238x499045eb(UnfoldLightRevealOverlayAnimation.this));
        }

        /* access modifiers changed from: private */
        /* renamed from: _init_$lambda-0  reason: not valid java name */
        public static final void m3276_init_$lambda0(UnfoldLightRevealOverlayAnimation unfoldLightRevealOverlayAnimation, Boolean bool) {
            Intrinsics.checkNotNullParameter(unfoldLightRevealOverlayAnimation, "this$0");
            Intrinsics.checkNotNullExpressionValue(bool, "isFolded");
            if (bool.booleanValue()) {
                unfoldLightRevealOverlayAnimation.ensureOverlayRemoved();
                unfoldLightRevealOverlayAnimation.isUnfoldHandled = false;
            }
            unfoldLightRevealOverlayAnimation.isFolded = bool.booleanValue();
        }
    }
}
