package com.android.systemui.statusbar.events;

import android.animation.Animator;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.internal.annotations.GuardedBy;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsChangedListener;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.leak.RotationUtils;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: PrivacyDotViewController.kt */
/* loaded from: classes.dex */
public final class PrivacyDotViewController {
    @NotNull
    private final SystemStatusAnimationScheduler animationScheduler;
    private View bl;
    private View br;
    @Nullable
    private Runnable cancelRunnable;
    @NotNull
    private final ConfigurationController configurationController;
    @NotNull
    private final StatusBarContentInsetsProvider contentInsetsProvider;
    @NotNull
    private ViewState currentViewState;
    @NotNull
    private final Executor mainExecutor;
    @GuardedBy({"lock"})
    @NotNull
    private ViewState nextViewState;
    private int sbHeightLandscape;
    private int sbHeightPortrait;
    @NotNull
    private final StatusBarStateController stateController;
    private View tl;
    private View tr;
    @Nullable
    private DelayableExecutor uiExecutor;
    @NotNull
    private final Object lock = new Object();
    @NotNull
    private final SystemStatusAnimationCallback systemStatusAnimationCallback = new SystemStatusAnimationCallback() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController$systemStatusAnimationCallback$1
        @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
        @Nullable
        public Animator onSystemStatusAnimationTransitionToPersistentDot(@Nullable String str) {
            Object obj;
            ViewState copy;
            obj = PrivacyDotViewController.this.lock;
            PrivacyDotViewController privacyDotViewController = PrivacyDotViewController.this;
            synchronized (obj) {
                copy = r2.copy((r30 & 1) != 0 ? r2.viewInitialized : false, (r30 & 2) != 0 ? r2.systemPrivacyEventIsActive : true, (r30 & 4) != 0 ? r2.shadeExpanded : false, (r30 & 8) != 0 ? r2.qsExpanded : false, (r30 & 16) != 0 ? r2.portraitRect : null, (r30 & 32) != 0 ? r2.landscapeRect : null, (r30 & 64) != 0 ? r2.upsideDownRect : null, (r30 & 128) != 0 ? r2.seascapeRect : null, (r30 & 256) != 0 ? r2.layoutRtl : false, (r30 & 512) != 0 ? r2.rotation : 0, (r30 & 1024) != 0 ? r2.height : 0, (r30 & 2048) != 0 ? r2.cornerIndex : 0, (r30 & 4096) != 0 ? r2.designatedCorner : null, (r30 & 8192) != 0 ? privacyDotViewController.nextViewState.contentDescription : str);
                privacyDotViewController.setNextViewState(copy);
                Unit unit = Unit.INSTANCE;
            }
            return null;
        }

        @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
        @Nullable
        public Animator onHidePersistentDot() {
            Object obj;
            ViewState copy;
            obj = PrivacyDotViewController.this.lock;
            PrivacyDotViewController privacyDotViewController = PrivacyDotViewController.this;
            synchronized (obj) {
                copy = r2.copy((r30 & 1) != 0 ? r2.viewInitialized : false, (r30 & 2) != 0 ? r2.systemPrivacyEventIsActive : false, (r30 & 4) != 0 ? r2.shadeExpanded : false, (r30 & 8) != 0 ? r2.qsExpanded : false, (r30 & 16) != 0 ? r2.portraitRect : null, (r30 & 32) != 0 ? r2.landscapeRect : null, (r30 & 64) != 0 ? r2.upsideDownRect : null, (r30 & 128) != 0 ? r2.seascapeRect : null, (r30 & 256) != 0 ? r2.layoutRtl : false, (r30 & 512) != 0 ? r2.rotation : 0, (r30 & 1024) != 0 ? r2.height : 0, (r30 & 2048) != 0 ? r2.cornerIndex : 0, (r30 & 4096) != 0 ? r2.designatedCorner : null, (r30 & 8192) != 0 ? privacyDotViewController.nextViewState.contentDescription : null);
                privacyDotViewController.setNextViewState(copy);
                Unit unit = Unit.INSTANCE;
            }
            return null;
        }
    };

    private final int rotatedCorner(int i, int i2) {
        int i3 = i - i2;
        return i3 < 0 ? i3 + 4 : i3;
    }

    public PrivacyDotViewController(@NotNull Executor mainExecutor, @NotNull StatusBarStateController stateController, @NotNull ConfigurationController configurationController, @NotNull StatusBarContentInsetsProvider contentInsetsProvider, @NotNull SystemStatusAnimationScheduler animationScheduler) {
        ViewState copy;
        Intrinsics.checkNotNullParameter(mainExecutor, "mainExecutor");
        Intrinsics.checkNotNullParameter(stateController, "stateController");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(contentInsetsProvider, "contentInsetsProvider");
        Intrinsics.checkNotNullParameter(animationScheduler, "animationScheduler");
        this.mainExecutor = mainExecutor;
        this.stateController = stateController;
        this.configurationController = configurationController;
        this.contentInsetsProvider = contentInsetsProvider;
        this.animationScheduler = animationScheduler;
        ViewState viewState = new ViewState(false, false, false, false, null, null, null, null, false, 0, 0, 0, null, null, 16383, null);
        this.currentViewState = viewState;
        copy = viewState.copy((r30 & 1) != 0 ? viewState.viewInitialized : false, (r30 & 2) != 0 ? viewState.systemPrivacyEventIsActive : false, (r30 & 4) != 0 ? viewState.shadeExpanded : false, (r30 & 8) != 0 ? viewState.qsExpanded : false, (r30 & 16) != 0 ? viewState.portraitRect : null, (r30 & 32) != 0 ? viewState.landscapeRect : null, (r30 & 64) != 0 ? viewState.upsideDownRect : null, (r30 & 128) != 0 ? viewState.seascapeRect : null, (r30 & 256) != 0 ? viewState.layoutRtl : false, (r30 & 512) != 0 ? viewState.rotation : 0, (r30 & 1024) != 0 ? viewState.height : 0, (r30 & 2048) != 0 ? viewState.cornerIndex : 0, (r30 & 4096) != 0 ? viewState.designatedCorner : null, (r30 & 8192) != 0 ? viewState.contentDescription : null);
        this.nextViewState = copy;
        contentInsetsProvider.addCallback(new StatusBarContentInsetsChangedListener() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController.1
            @Override // com.android.systemui.statusbar.phone.StatusBarContentInsetsChangedListener
            public void onStatusBarContentInsetsChanged() {
                PrivacyDotViewControllerKt.dlog("onStatusBarContentInsetsChanged: ");
                PrivacyDotViewController.this.setNewLayoutRects();
            }
        });
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController.2
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onLayoutDirectionChanged(boolean z) {
                ViewState copy2;
                PrivacyDotViewController privacyDotViewController = PrivacyDotViewController.this;
                synchronized (this) {
                    copy2 = r3.copy((r30 & 1) != 0 ? r3.viewInitialized : false, (r30 & 2) != 0 ? r3.systemPrivacyEventIsActive : false, (r30 & 4) != 0 ? r3.shadeExpanded : false, (r30 & 8) != 0 ? r3.qsExpanded : false, (r30 & 16) != 0 ? r3.portraitRect : null, (r30 & 32) != 0 ? r3.landscapeRect : null, (r30 & 64) != 0 ? r3.upsideDownRect : null, (r30 & 128) != 0 ? r3.seascapeRect : null, (r30 & 256) != 0 ? r3.layoutRtl : z, (r30 & 512) != 0 ? r3.rotation : 0, (r30 & 1024) != 0 ? r3.height : 0, (r30 & 2048) != 0 ? r3.cornerIndex : 0, (r30 & 4096) != 0 ? r3.designatedCorner : privacyDotViewController.selectDesignatedCorner(privacyDotViewController.nextViewState.getRotation(), z), (r30 & 8192) != 0 ? privacyDotViewController.nextViewState.contentDescription : null);
                    privacyDotViewController.setNextViewState(copy2);
                    Unit unit = Unit.INSTANCE;
                }
            }
        });
        stateController.addCallback(new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController.3
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onExpandedChanged(boolean z) {
                PrivacyDotViewController.this.updateStatusBarState();
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public void onStateChanged(int i) {
                PrivacyDotViewController.this.updateStatusBarState();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setNextViewState(ViewState viewState) {
        this.nextViewState = viewState;
        scheduleUpdate();
    }

    private final Sequence<View> getViews() {
        Sequence<View> sequenceOf;
        Sequence<View> sequenceOf2;
        View view = this.tl;
        if (view == null) {
            sequenceOf2 = SequencesKt__SequencesKt.sequenceOf(new View[0]);
            return sequenceOf2;
        }
        View[] viewArr = new View[4];
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            throw null;
        }
        viewArr[0] = view;
        View view2 = this.tr;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tr");
            throw null;
        }
        viewArr[1] = view2;
        View view3 = this.br;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("br");
            throw null;
        }
        viewArr[2] = view3;
        View view4 = this.bl;
        if (view4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bl");
            throw null;
        }
        viewArr[3] = view4;
        sequenceOf = SequencesKt__SequencesKt.sequenceOf(viewArr);
        return sequenceOf;
    }

    public final void setUiExecutor(@NotNull DelayableExecutor e) {
        Intrinsics.checkNotNullParameter(e, "e");
        this.uiExecutor = e;
    }

    public final void setQsExpanded(boolean z) {
        ViewState copy;
        PrivacyDotViewControllerKt.dlog(Intrinsics.stringPlus("setQsExpanded ", Boolean.valueOf(z)));
        synchronized (this.lock) {
            copy = r2.copy((r30 & 1) != 0 ? r2.viewInitialized : false, (r30 & 2) != 0 ? r2.systemPrivacyEventIsActive : false, (r30 & 4) != 0 ? r2.shadeExpanded : false, (r30 & 8) != 0 ? r2.qsExpanded : z, (r30 & 16) != 0 ? r2.portraitRect : null, (r30 & 32) != 0 ? r2.landscapeRect : null, (r30 & 64) != 0 ? r2.upsideDownRect : null, (r30 & 128) != 0 ? r2.seascapeRect : null, (r30 & 256) != 0 ? r2.layoutRtl : false, (r30 & 512) != 0 ? r2.rotation : 0, (r30 & 1024) != 0 ? r2.height : 0, (r30 & 2048) != 0 ? r2.cornerIndex : 0, (r30 & 4096) != 0 ? r2.designatedCorner : null, (r30 & 8192) != 0 ? this.nextViewState.contentDescription : null);
            setNextViewState(copy);
            Unit unit = Unit.INSTANCE;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x004a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final void setNewRotation(int i) {
        int i2;
        Object obj;
        Object obj2;
        ViewState copy;
        PrivacyDotViewControllerKt.dlog(Intrinsics.stringPlus("updateRotation: ", Integer.valueOf(i)));
        synchronized (this.lock) {
            if (i == this.nextViewState.getRotation()) {
                return;
            }
            boolean layoutRtl = this.nextViewState.getLayoutRtl();
            Unit unit = Unit.INSTANCE;
            setCornerVisibilities(4);
            View selectDesignatedCorner = selectDesignatedCorner(i, layoutRtl);
            int cornerIndex = cornerIndex(selectDesignatedCorner);
            if (i != 0) {
                if (i != 1) {
                    if (i != 2) {
                        if (i != 3) {
                            i2 = 0;
                            int i3 = i2;
                            obj = this.lock;
                            synchronized (obj) {
                                try {
                                    obj2 = obj;
                                } catch (Throwable th) {
                                    th = th;
                                    obj2 = obj;
                                }
                                try {
                                    copy = r15.copy((r30 & 1) != 0 ? r15.viewInitialized : false, (r30 & 2) != 0 ? r15.systemPrivacyEventIsActive : false, (r30 & 4) != 0 ? r15.shadeExpanded : false, (r30 & 8) != 0 ? r15.qsExpanded : false, (r30 & 16) != 0 ? r15.portraitRect : null, (r30 & 32) != 0 ? r15.landscapeRect : null, (r30 & 64) != 0 ? r15.upsideDownRect : null, (r30 & 128) != 0 ? r15.seascapeRect : null, (r30 & 256) != 0 ? r15.layoutRtl : false, (r30 & 512) != 0 ? r15.rotation : i, (r30 & 1024) != 0 ? r15.height : i3, (r30 & 2048) != 0 ? r15.cornerIndex : cornerIndex, (r30 & 4096) != 0 ? r15.designatedCorner : selectDesignatedCorner, (r30 & 8192) != 0 ? this.nextViewState.contentDescription : null);
                                    setNextViewState(copy);
                                    return;
                                } catch (Throwable th2) {
                                    th = th2;
                                    throw th;
                                }
                            }
                        }
                    }
                }
                i2 = this.sbHeightLandscape;
                int i32 = i2;
                obj = this.lock;
                synchronized (obj) {
                }
            }
            i2 = this.sbHeightPortrait;
            int i322 = i2;
            obj = this.lock;
            synchronized (obj) {
            }
        }
    }

    private final void hideDotView(final View view, boolean z) {
        view.clearAnimation();
        if (z) {
            view.animate().setDuration(160L).setInterpolator(Interpolators.ALPHA_OUT).alpha(0.0f).withEndAction(new Runnable() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController$hideDotView$1
                @Override // java.lang.Runnable
                public final void run() {
                    view.setVisibility(4);
                }
            }).start();
        } else {
            view.setVisibility(4);
        }
    }

    private final void showDotView(View view, boolean z) {
        view.clearAnimation();
        if (z) {
            view.setVisibility(0);
            view.setAlpha(0.0f);
            view.animate().alpha(1.0f).setDuration(160L).setInterpolator(Interpolators.ALPHA_IN).start();
            return;
        }
        view.setVisibility(0);
        view.setAlpha(1.0f);
    }

    private final void updateRotations(int i) {
        int gravity;
        int innerGravity;
        for (View view : getViews()) {
            int rotatedCorner = rotatedCorner(cornerForView(view), i);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
            gravity = PrivacyDotViewControllerKt.toGravity(rotatedCorner);
            ((FrameLayout.LayoutParams) layoutParams).gravity = gravity;
            ViewGroup.LayoutParams layoutParams2 = view.findViewById(R$id.privacy_dot).getLayoutParams();
            Objects.requireNonNull(layoutParams2, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
            innerGravity = PrivacyDotViewControllerKt.innerGravity(rotatedCorner);
            ((FrameLayout.LayoutParams) layoutParams2).gravity = innerGravity;
        }
    }

    private final void setCornerSizes(ViewState viewState) {
        int i;
        int i2;
        boolean layoutRtl = viewState.getLayoutRtl();
        Point point = new Point();
        View view = this.tl;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            throw null;
        }
        view.getContext().getDisplay().getRealSize(point);
        View view2 = this.tl;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            throw null;
        }
        int exactRotation = RotationUtils.getExactRotation(view2.getContext());
        if (exactRotation == 1 || exactRotation == 3) {
            i = point.y;
            i2 = point.x;
        } else {
            i = point.x;
            i2 = point.y;
        }
        View view3 = this.tl;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            throw null;
        }
        Rect contentRectForRotation = viewState.contentRectForRotation(activeRotationForCorner(view3, layoutRtl));
        View view4 = this.tl;
        if (view4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            throw null;
        }
        ViewGroup.LayoutParams layoutParams = view4.getLayoutParams();
        Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
        layoutParams2.height = contentRectForRotation.height();
        if (layoutRtl) {
            layoutParams2.width = contentRectForRotation.left;
        } else {
            layoutParams2.width = i2 - contentRectForRotation.right;
        }
        View view5 = this.tr;
        if (view5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tr");
            throw null;
        }
        Rect contentRectForRotation2 = viewState.contentRectForRotation(activeRotationForCorner(view5, layoutRtl));
        View view6 = this.tr;
        if (view6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tr");
            throw null;
        }
        ViewGroup.LayoutParams layoutParams3 = view6.getLayoutParams();
        Objects.requireNonNull(layoutParams3, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
        FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) layoutParams3;
        layoutParams4.height = contentRectForRotation2.height();
        if (layoutRtl) {
            layoutParams4.width = contentRectForRotation2.left;
        } else {
            layoutParams4.width = i - contentRectForRotation2.right;
        }
        View view7 = this.br;
        if (view7 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("br");
            throw null;
        }
        Rect contentRectForRotation3 = viewState.contentRectForRotation(activeRotationForCorner(view7, layoutRtl));
        View view8 = this.br;
        if (view8 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("br");
            throw null;
        }
        ViewGroup.LayoutParams layoutParams5 = view8.getLayoutParams();
        Objects.requireNonNull(layoutParams5, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
        FrameLayout.LayoutParams layoutParams6 = (FrameLayout.LayoutParams) layoutParams5;
        layoutParams6.height = contentRectForRotation3.height();
        if (layoutRtl) {
            layoutParams6.width = contentRectForRotation3.left;
        } else {
            layoutParams6.width = i2 - contentRectForRotation3.right;
        }
        View view9 = this.bl;
        if (view9 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bl");
            throw null;
        }
        Rect contentRectForRotation4 = viewState.contentRectForRotation(activeRotationForCorner(view9, layoutRtl));
        View view10 = this.bl;
        if (view10 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bl");
            throw null;
        }
        ViewGroup.LayoutParams layoutParams7 = view10.getLayoutParams();
        Objects.requireNonNull(layoutParams7, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
        FrameLayout.LayoutParams layoutParams8 = (FrameLayout.LayoutParams) layoutParams7;
        layoutParams8.height = contentRectForRotation4.height();
        if (layoutRtl) {
            layoutParams8.width = contentRectForRotation4.left;
        } else {
            layoutParams8.width = i - contentRectForRotation4.right;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final View selectDesignatedCorner(int i, boolean z) {
        View view = this.tl;
        if (view == null) {
            return null;
        }
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        throw new IllegalStateException("unknown rotation");
                    }
                    if (z) {
                        view = this.bl;
                        if (view == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("bl");
                            throw null;
                        }
                    } else if (view == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("tl");
                        throw null;
                    }
                } else if (z) {
                    view = this.br;
                    if (view == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("br");
                        throw null;
                    }
                } else {
                    view = this.bl;
                    if (view == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("bl");
                        throw null;
                    }
                }
            } else if (z) {
                view = this.tr;
                if (view == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("tr");
                    throw null;
                }
            } else {
                view = this.br;
                if (view == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("br");
                    throw null;
                }
            }
        } else if (!z) {
            view = this.tr;
            if (view == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tr");
                throw null;
            }
        } else if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            throw null;
        }
        return view;
    }

    private final void updateDesignatedCorner(View view, boolean z) {
        if (!z || view == null) {
            return;
        }
        view.clearAnimation();
        view.setVisibility(0);
        view.setAlpha(0.0f);
        view.animate().alpha(1.0f).setDuration(300L).start();
    }

    private final void setCornerVisibilities(int i) {
        for (View view : getViews()) {
            view.setVisibility(i);
        }
    }

    private final int cornerForView(View view) {
        View view2 = this.tl;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            throw null;
        } else if (Intrinsics.areEqual(view, view2)) {
            return 0;
        } else {
            View view3 = this.tr;
            if (view3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tr");
                throw null;
            } else if (Intrinsics.areEqual(view, view3)) {
                return 1;
            } else {
                View view4 = this.bl;
                if (view4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("bl");
                    throw null;
                } else if (Intrinsics.areEqual(view, view4)) {
                    return 3;
                } else {
                    View view5 = this.br;
                    if (view5 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("br");
                        throw null;
                    } else if (!Intrinsics.areEqual(view, view5)) {
                        throw new IllegalArgumentException("not a corner view");
                    } else {
                        return 2;
                    }
                }
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:?, code lost:
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x001f, code lost:
        if (r8 != false) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x000f, code lost:
        if (r8 != false) goto L6;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private final int activeRotationForCorner(View view, boolean z) {
        View view2 = this.tr;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tr");
            throw null;
        } else if (!Intrinsics.areEqual(view, view2)) {
            View view3 = this.tl;
            if (view3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tl");
                throw null;
            }
            if (!Intrinsics.areEqual(view, view3)) {
                View view4 = this.br;
                if (view4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("br");
                    throw null;
                } else if (Intrinsics.areEqual(view, view4)) {
                    if (z) {
                        return 2;
                    }
                    return 1;
                } else if (!z) {
                    return 2;
                }
            }
            return 3;
        }
    }

    public final void initialize(@NotNull View topLeft, @NotNull View topRight, @NotNull View bottomLeft, @NotNull View bottomRight) {
        ViewState copy;
        Intrinsics.checkNotNullParameter(topLeft, "topLeft");
        Intrinsics.checkNotNullParameter(topRight, "topRight");
        Intrinsics.checkNotNullParameter(bottomLeft, "bottomLeft");
        Intrinsics.checkNotNullParameter(bottomRight, "bottomRight");
        View view = this.tl;
        if (view != null && this.tr != null && this.bl != null && this.br != null) {
            if (view == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tl");
                throw null;
            } else if (Intrinsics.areEqual(view, topLeft)) {
                View view2 = this.tr;
                if (view2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("tr");
                    throw null;
                } else if (Intrinsics.areEqual(view2, topRight)) {
                    View view3 = this.bl;
                    if (view3 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("bl");
                        throw null;
                    } else if (Intrinsics.areEqual(view3, bottomLeft)) {
                        View view4 = this.br;
                        if (view4 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("br");
                            throw null;
                        } else if (Intrinsics.areEqual(view4, bottomRight)) {
                            return;
                        }
                    }
                }
            }
        }
        this.tl = topLeft;
        this.tr = topRight;
        this.bl = bottomLeft;
        this.br = bottomRight;
        boolean isLayoutRtl = this.configurationController.isLayoutRtl();
        View selectDesignatedCorner = selectDesignatedCorner(0, isLayoutRtl);
        int cornerIndex = cornerIndex(selectDesignatedCorner);
        this.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController$initialize$5
            @Override // java.lang.Runnable
            public final void run() {
                SystemStatusAnimationScheduler systemStatusAnimationScheduler;
                SystemStatusAnimationCallback systemStatusAnimationCallback;
                systemStatusAnimationScheduler = PrivacyDotViewController.this.animationScheduler;
                systemStatusAnimationCallback = PrivacyDotViewController.this.systemStatusAnimationCallback;
                systemStatusAnimationScheduler.addCallback(systemStatusAnimationCallback);
            }
        });
        Rect statusBarContentInsetsForRotation = this.contentInsetsProvider.getStatusBarContentInsetsForRotation(3);
        Rect statusBarContentInsetsForRotation2 = this.contentInsetsProvider.getStatusBarContentInsetsForRotation(0);
        Rect statusBarContentInsetsForRotation3 = this.contentInsetsProvider.getStatusBarContentInsetsForRotation(1);
        Rect statusBarContentInsetsForRotation4 = this.contentInsetsProvider.getStatusBarContentInsetsForRotation(2);
        synchronized (this.lock) {
            copy = r2.copy((r30 & 1) != 0 ? r2.viewInitialized : true, (r30 & 2) != 0 ? r2.systemPrivacyEventIsActive : false, (r30 & 4) != 0 ? r2.shadeExpanded : false, (r30 & 8) != 0 ? r2.qsExpanded : false, (r30 & 16) != 0 ? r2.portraitRect : statusBarContentInsetsForRotation2, (r30 & 32) != 0 ? r2.landscapeRect : statusBarContentInsetsForRotation3, (r30 & 64) != 0 ? r2.upsideDownRect : statusBarContentInsetsForRotation4, (r30 & 128) != 0 ? r2.seascapeRect : statusBarContentInsetsForRotation, (r30 & 256) != 0 ? r2.layoutRtl : isLayoutRtl, (r30 & 512) != 0 ? r2.rotation : 0, (r30 & 1024) != 0 ? r2.height : 0, (r30 & 2048) != 0 ? r2.cornerIndex : cornerIndex, (r30 & 4096) != 0 ? r2.designatedCorner : selectDesignatedCorner, (r30 & 8192) != 0 ? this.nextViewState.contentDescription : null);
            setNextViewState(copy);
            Unit unit = Unit.INSTANCE;
        }
    }

    public final void setStatusBarHeights(int i, int i2) {
        this.sbHeightPortrait = i;
        this.sbHeightLandscape = i2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void updateStatusBarState() {
        ViewState copy;
        synchronized (this.lock) {
            copy = r2.copy((r30 & 1) != 0 ? r2.viewInitialized : false, (r30 & 2) != 0 ? r2.systemPrivacyEventIsActive : false, (r30 & 4) != 0 ? r2.shadeExpanded : isShadeInQs(), (r30 & 8) != 0 ? r2.qsExpanded : false, (r30 & 16) != 0 ? r2.portraitRect : null, (r30 & 32) != 0 ? r2.landscapeRect : null, (r30 & 64) != 0 ? r2.upsideDownRect : null, (r30 & 128) != 0 ? r2.seascapeRect : null, (r30 & 256) != 0 ? r2.layoutRtl : false, (r30 & 512) != 0 ? r2.rotation : 0, (r30 & 1024) != 0 ? r2.height : 0, (r30 & 2048) != 0 ? r2.cornerIndex : 0, (r30 & 4096) != 0 ? r2.designatedCorner : null, (r30 & 8192) != 0 ? this.nextViewState.contentDescription : null);
            setNextViewState(copy);
            Unit unit = Unit.INSTANCE;
        }
    }

    @GuardedBy({"lock"})
    private final boolean isShadeInQs() {
        return (this.stateController.isExpanded() && this.stateController.getState() == 0) || this.stateController.getState() == 2;
    }

    private final void scheduleUpdate() {
        PrivacyDotViewControllerKt.dlog("scheduleUpdate: ");
        Runnable runnable = this.cancelRunnable;
        if (runnable != null) {
            runnable.run();
        }
        DelayableExecutor delayableExecutor = this.uiExecutor;
        this.cancelRunnable = delayableExecutor == null ? null : delayableExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController$scheduleUpdate$1
            @Override // java.lang.Runnable
            public final void run() {
                PrivacyDotViewController.this.processNextViewState();
            }
        }, 100L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void processNextViewState() {
        ViewState copy;
        PrivacyDotViewControllerKt.dlog("processNextViewState: ");
        synchronized (this.lock) {
            copy = r2.copy((r30 & 1) != 0 ? r2.viewInitialized : false, (r30 & 2) != 0 ? r2.systemPrivacyEventIsActive : false, (r30 & 4) != 0 ? r2.shadeExpanded : false, (r30 & 8) != 0 ? r2.qsExpanded : false, (r30 & 16) != 0 ? r2.portraitRect : null, (r30 & 32) != 0 ? r2.landscapeRect : null, (r30 & 64) != 0 ? r2.upsideDownRect : null, (r30 & 128) != 0 ? r2.seascapeRect : null, (r30 & 256) != 0 ? r2.layoutRtl : false, (r30 & 512) != 0 ? r2.rotation : 0, (r30 & 1024) != 0 ? r2.height : 0, (r30 & 2048) != 0 ? r2.cornerIndex : 0, (r30 & 4096) != 0 ? r2.designatedCorner : null, (r30 & 8192) != 0 ? this.nextViewState.contentDescription : null);
            Unit unit = Unit.INSTANCE;
        }
        resolveState(copy);
    }

    private final void resolveState(ViewState viewState) {
        View designatedCorner;
        PrivacyDotViewControllerKt.dlog(Intrinsics.stringPlus("resolveState ", viewState));
        if (!viewState.getViewInitialized()) {
            PrivacyDotViewControllerKt.dlog("resolveState: view is not initialized. skipping.");
        } else if (Intrinsics.areEqual(viewState, this.currentViewState)) {
            PrivacyDotViewControllerKt.dlog("resolveState: skipping");
        } else {
            if (viewState.getRotation() != this.currentViewState.getRotation()) {
                updateRotations(viewState.getRotation());
            }
            if (viewState.needsLayout(this.currentViewState)) {
                setCornerSizes(viewState);
                for (View view : getViews()) {
                    view.requestLayout();
                }
            }
            if (!Intrinsics.areEqual(viewState.getDesignatedCorner(), this.currentViewState.getDesignatedCorner())) {
                View designatedCorner2 = this.currentViewState.getDesignatedCorner();
                if (designatedCorner2 != null) {
                    designatedCorner2.setContentDescription(null);
                }
                View designatedCorner3 = viewState.getDesignatedCorner();
                if (designatedCorner3 != null) {
                    designatedCorner3.setContentDescription(viewState.getContentDescription());
                }
                updateDesignatedCorner(viewState.getDesignatedCorner(), viewState.shouldShowDot());
            } else if (!Intrinsics.areEqual(viewState.getContentDescription(), this.currentViewState.getContentDescription()) && (designatedCorner = viewState.getDesignatedCorner()) != null) {
                designatedCorner.setContentDescription(viewState.getContentDescription());
            }
            boolean shouldShowDot = viewState.shouldShowDot();
            if (shouldShowDot != this.currentViewState.shouldShowDot()) {
                if (shouldShowDot && viewState.getDesignatedCorner() != null) {
                    showDotView(viewState.getDesignatedCorner(), true);
                } else if (!shouldShowDot && viewState.getDesignatedCorner() != null) {
                    hideDotView(viewState.getDesignatedCorner(), true);
                }
            }
            this.currentViewState = viewState;
        }
    }

    private final int cornerIndex(View view) {
        if (view != null) {
            return cornerForView(view);
        }
        return -1;
    }

    private final List<Rect> getLayoutRects() {
        List<Rect> listOf;
        listOf = CollectionsKt__CollectionsKt.listOf((Object[]) new Rect[]{this.contentInsetsProvider.getStatusBarContentInsetsForRotation(3), this.contentInsetsProvider.getStatusBarContentInsetsForRotation(0), this.contentInsetsProvider.getStatusBarContentInsetsForRotation(1), this.contentInsetsProvider.getStatusBarContentInsetsForRotation(2)});
        return listOf;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void setNewLayoutRects() {
        ViewState copy;
        List<Rect> layoutRects = getLayoutRects();
        synchronized (this.lock) {
            copy = r4.copy((r30 & 1) != 0 ? r4.viewInitialized : false, (r30 & 2) != 0 ? r4.systemPrivacyEventIsActive : false, (r30 & 4) != 0 ? r4.shadeExpanded : false, (r30 & 8) != 0 ? r4.qsExpanded : false, (r30 & 16) != 0 ? r4.portraitRect : layoutRects.get(1), (r30 & 32) != 0 ? r4.landscapeRect : layoutRects.get(2), (r30 & 64) != 0 ? r4.upsideDownRect : layoutRects.get(3), (r30 & 128) != 0 ? r4.seascapeRect : layoutRects.get(0), (r30 & 256) != 0 ? r4.layoutRtl : false, (r30 & 512) != 0 ? r4.rotation : 0, (r30 & 1024) != 0 ? r4.height : 0, (r30 & 2048) != 0 ? r4.cornerIndex : 0, (r30 & 4096) != 0 ? r4.designatedCorner : null, (r30 & 8192) != 0 ? this.nextViewState.contentDescription : null);
            setNextViewState(copy);
            Unit unit = Unit.INSTANCE;
        }
    }
}
