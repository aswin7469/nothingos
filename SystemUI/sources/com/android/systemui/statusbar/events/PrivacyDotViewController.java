package com.android.systemui.statusbar.events;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.systemui.C1894R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsChangedListener;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.leak.RotationUtils;
import dalvik.bytecode.Opcodes;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b+\b\u0007\u0018\u00002\u00020\u0001:\u0001[B1\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u0018\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020\u000e2\u0006\u0010)\u001a\u00020*H\u0002J\u0010\u0010+\u001a\u00020'2\u0006\u0010,\u001a\u00020\u000eH\u0002J\u000e\u0010-\u001a\b\u0012\u0004\u0012\u00020/0.H\u0002J\u0018\u00100\u001a\u0002012\u0006\u00102\u001a\u00020\u000e2\u0006\u00103\u001a\u00020*H\u0002J&\u00104\u001a\u0002012\u0006\u00105\u001a\u00020\u000e2\u0006\u00106\u001a\u00020\u000e2\u0006\u00107\u001a\u00020\u000e2\u0006\u00108\u001a\u00020\u000eJ\b\u00109\u001a\u00020*H\u0003J\b\u0010:\u001a\u000201H\u0002J\u0010\u0010;\u001a\u0002012\u0006\u0010<\u001a\u00020\u0013H\u0002J\u0018\u0010=\u001a\u00020'2\u0006\u0010(\u001a\u00020'2\u0006\u0010>\u001a\u00020'H\u0002J\b\u0010?\u001a\u000201H\u0002J\u001a\u0010@\u001a\u0004\u0018\u00010\u000e2\u0006\u0010A\u001a\u00020'2\u0006\u0010B\u001a\u00020*H\u0002J\u0010\u0010C\u001a\u0002012\u0006\u0010<\u001a\u00020\u0013H\u0002J\u0010\u0010D\u001a\u0002012\u0006\u0010E\u001a\u00020'H\u0002J\b\u0010F\u001a\u000201H\u0002J\u000e\u0010G\u001a\u0002012\u0006\u0010H\u001a\u00020'J\u000e\u0010I\u001a\u0002012\u0006\u0010J\u001a\u00020*J\u0010\u0010K\u001a\u0002012\b\u0010L\u001a\u0004\u0018\u00010\u001bJ\u000e\u0010M\u001a\u0002012\u0006\u0010N\u001a\u00020!J\u0018\u0010O\u001a\u0002012\u0006\u00102\u001a\u00020\u000e2\u0006\u00103\u001a\u00020*H\u0002J \u0010P\u001a\u0002012\u0006\u0010L\u001a\u00020'2\u0006\u0010A\u001a\u00020'2\u0006\u0010>\u001a\u00020'H\u0002J\u001a\u0010Q\u001a\u0002012\b\u0010R\u001a\u0004\u0018\u00010\u000e2\u0006\u0010S\u001a\u00020*H\u0002J\u0018\u0010T\u001a\u0002012\u0006\u0010>\u001a\u00020'2\u0006\u0010U\u001a\u00020'H\u0002J\b\u0010V\u001a\u000201H\u0002J \u0010W\u001a\u00020'2\u0006\u0010(\u001a\u00020'2\u0006\u0010X\u001a\u00020'2\u0006\u0010Y\u001a\u00020'H\u0002J\u000e\u0010Z\u001a\u00020'*\u0004\u0018\u00010\u000eH\u0002R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX.¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u000eX.¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R \u0010\u0017\u001a\u00020\u00132\u0006\u0010\u0016\u001a\u00020\u00138\u0002@BX\u000e¢\u0006\b\n\u0000\"\u0004\b\u0018\u0010\u0019R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u000eX.¢\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020\u000eX.¢\u0006\u0002\n\u0000R\u0010\u0010 \u001a\u0004\u0018\u00010!X\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\"\u001a\b\u0012\u0004\u0012\u00020\u000e0#8BX\u0004¢\u0006\u0006\u001a\u0004\b$\u0010%¨\u0006\\"}, mo65043d2 = {"Lcom/android/systemui/statusbar/events/PrivacyDotViewController;", "", "mainExecutor", "Ljava/util/concurrent/Executor;", "stateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "contentInsetsProvider", "Lcom/android/systemui/statusbar/phone/StatusBarContentInsetsProvider;", "animationScheduler", "Lcom/android/systemui/statusbar/events/SystemStatusAnimationScheduler;", "(Ljava/util/concurrent/Executor;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/statusbar/phone/StatusBarContentInsetsProvider;Lcom/android/systemui/statusbar/events/SystemStatusAnimationScheduler;)V", "bl", "Landroid/view/View;", "br", "cancelRunnable", "Ljava/lang/Runnable;", "currentViewState", "Lcom/android/systemui/statusbar/events/ViewState;", "lock", "Ljava/lang/Object;", "value", "nextViewState", "setNextViewState", "(Lcom/android/systemui/statusbar/events/ViewState;)V", "showingListener", "Lcom/android/systemui/statusbar/events/PrivacyDotViewController$ShowingListener;", "systemStatusAnimationCallback", "Lcom/android/systemui/statusbar/events/SystemStatusAnimationCallback;", "tl", "tr", "uiExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "views", "Lkotlin/sequences/Sequence;", "getViews", "()Lkotlin/sequences/Sequence;", "activeRotationForCorner", "", "corner", "rtl", "", "cornerForView", "v", "getLayoutRects", "", "Landroid/graphics/Rect;", "hideDotView", "", "dot", "animate", "initialize", "topLeft", "topRight", "bottomLeft", "bottomRight", "isShadeInQs", "processNextViewState", "resolveState", "state", "rotatedCorner", "rotation", "scheduleUpdate", "selectDesignatedCorner", "r", "isRtl", "setCornerSizes", "setCornerVisibilities", "vis", "setNewLayoutRects", "setNewRotation", "rot", "setQsExpanded", "expanded", "setShowingListener", "l", "setUiExecutor", "e", "showDotView", "updateCornerSizes", "updateDesignatedCorner", "newCorner", "shouldShowDot", "updateRotations", "paddingTop", "updateStatusBarState", "widthForCorner", "left", "right", "cornerIndex", "ShowingListener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyDotViewController.kt */
public final class PrivacyDotViewController {
    private final SystemStatusAnimationScheduler animationScheduler;

    /* renamed from: bl */
    private View f370bl;

    /* renamed from: br */
    private View f371br;
    private Runnable cancelRunnable;
    private final ConfigurationController configurationController;
    private final StatusBarContentInsetsProvider contentInsetsProvider;
    private ViewState currentViewState;
    /* access modifiers changed from: private */
    public final Object lock = new Object();
    private final Executor mainExecutor;
    /* access modifiers changed from: private */
    public ViewState nextViewState;
    private ShowingListener showingListener;
    private final StatusBarStateController stateController;
    private final SystemStatusAnimationCallback systemStatusAnimationCallback;

    /* renamed from: tl */
    private View f372tl;

    /* renamed from: tr */
    private View f373tr;
    /* access modifiers changed from: private */
    public DelayableExecutor uiExecutor;

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0007À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/statusbar/events/PrivacyDotViewController$ShowingListener;", "", "onPrivacyDotHidden", "", "v", "Landroid/view/View;", "onPrivacyDotShown", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: PrivacyDotViewController.kt */
    public interface ShowingListener {
        void onPrivacyDotHidden(View view);

        void onPrivacyDotShown(View view);
    }

    private final int rotatedCorner(int i, int i2) {
        int i3 = i - i2;
        return i3 < 0 ? i3 + 4 : i3;
    }

    @Inject
    public PrivacyDotViewController(@Main Executor executor, StatusBarStateController statusBarStateController, ConfigurationController configurationController2, StatusBarContentInsetsProvider statusBarContentInsetsProvider, SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        Executor executor2 = executor;
        StatusBarStateController statusBarStateController2 = statusBarStateController;
        ConfigurationController configurationController3 = configurationController2;
        StatusBarContentInsetsProvider statusBarContentInsetsProvider2 = statusBarContentInsetsProvider;
        SystemStatusAnimationScheduler systemStatusAnimationScheduler2 = systemStatusAnimationScheduler;
        Intrinsics.checkNotNullParameter(executor2, "mainExecutor");
        Intrinsics.checkNotNullParameter(statusBarStateController2, "stateController");
        Intrinsics.checkNotNullParameter(configurationController3, "configurationController");
        Intrinsics.checkNotNullParameter(statusBarContentInsetsProvider2, "contentInsetsProvider");
        Intrinsics.checkNotNullParameter(systemStatusAnimationScheduler2, "animationScheduler");
        this.mainExecutor = executor2;
        this.stateController = statusBarStateController2;
        this.configurationController = configurationController3;
        this.contentInsetsProvider = statusBarContentInsetsProvider2;
        this.animationScheduler = systemStatusAnimationScheduler2;
        ViewState viewState = new ViewState(false, false, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8191, (DefaultConstructorMarker) null);
        this.currentViewState = viewState;
        this.nextViewState = ViewState.copy$default(viewState, false, false, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8191, (Object) null);
        statusBarContentInsetsProvider2.addCallback((StatusBarContentInsetsChangedListener) new StatusBarContentInsetsChangedListener(this) {
            final /* synthetic */ PrivacyDotViewController this$0;

            {
                this.this$0 = r1;
            }

            public void onStatusBarContentInsetsChanged() {
                PrivacyDotViewControllerKt.dlog("onStatusBarContentInsetsChanged: ");
                this.this$0.setNewLayoutRects();
            }
        });
        configurationController3.addCallback(new ConfigurationController.ConfigurationListener(this) {
            final /* synthetic */ PrivacyDotViewController this$0;

            {
                this.this$0 = r1;
            }

            public void onLayoutDirectionChanged(boolean z) {
                DelayableExecutor access$getUiExecutor$p = this.this$0.uiExecutor;
                if (access$getUiExecutor$p != null) {
                    access$getUiExecutor$p.execute(new PrivacyDotViewController$2$$ExternalSyntheticLambda0(this.this$0, this, z));
                }
            }

            /* access modifiers changed from: private */
            /* renamed from: onLayoutDirectionChanged$lambda-1  reason: not valid java name */
            public static final void m3065onLayoutDirectionChanged$lambda1(PrivacyDotViewController privacyDotViewController, C26392 r20, boolean z) {
                PrivacyDotViewController privacyDotViewController2 = privacyDotViewController;
                Intrinsics.checkNotNullParameter(privacyDotViewController2, "this$0");
                Intrinsics.checkNotNullParameter(r20, "this$1");
                privacyDotViewController2.setCornerVisibilities(4);
                synchronized (r20) {
                    privacyDotViewController2.setNextViewState(ViewState.copy$default(privacyDotViewController.nextViewState, false, false, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, z, 0, 0, 0, privacyDotViewController2.selectDesignatedCorner(privacyDotViewController.nextViewState.getRotation(), z), Opcodes.OP_IPUT_WIDE_JUMBO, (Object) null));
                    Unit unit = Unit.INSTANCE;
                }
            }
        });
        statusBarStateController2.addCallback(new StatusBarStateController.StateListener(this) {
            final /* synthetic */ PrivacyDotViewController this$0;

            {
                this.this$0 = r1;
            }

            public void onExpandedChanged(boolean z) {
                this.this$0.updateStatusBarState();
            }

            public void onStateChanged(int i) {
                this.this$0.updateStatusBarState();
            }
        });
        this.systemStatusAnimationCallback = new PrivacyDotViewController$systemStatusAnimationCallback$1(this);
    }

    /* access modifiers changed from: private */
    public final void setNextViewState(ViewState viewState) {
        this.nextViewState = viewState;
        scheduleUpdate();
    }

    private final Sequence<View> getViews() {
        View view = this.f372tl;
        if (view == null) {
            return SequencesKt.sequenceOf(new View[0]);
        }
        View[] viewArr = new View[4];
        View view2 = null;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            view = null;
        }
        viewArr[0] = view;
        View view3 = this.f373tr;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tr");
            view3 = null;
        }
        viewArr[1] = view3;
        View view4 = this.f371br;
        if (view4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("br");
            view4 = null;
        }
        viewArr[2] = view4;
        View view5 = this.f370bl;
        if (view5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bl");
        } else {
            view2 = view5;
        }
        viewArr[3] = view2;
        return SequencesKt.sequenceOf(viewArr);
    }

    public final void setUiExecutor(DelayableExecutor delayableExecutor) {
        Intrinsics.checkNotNullParameter(delayableExecutor, "e");
        this.uiExecutor = delayableExecutor;
    }

    public final void setShowingListener(ShowingListener showingListener2) {
        this.showingListener = showingListener2;
    }

    public final void setQsExpanded(boolean z) {
        PrivacyDotViewControllerKt.dlog("setQsExpanded " + z);
        synchronized (this.lock) {
            setNextViewState(ViewState.copy$default(this.nextViewState, false, false, false, z, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8183, (Object) null));
            Unit unit = Unit.INSTANCE;
        }
    }

    public final void setNewRotation(int i) {
        Object obj;
        int i2 = i;
        PrivacyDotViewControllerKt.dlog("updateRotation: " + i2);
        synchronized (this.lock) {
            if (i2 != this.nextViewState.getRotation()) {
                boolean layoutRtl = this.nextViewState.getLayoutRtl();
                Unit unit = Unit.INSTANCE;
                setCornerVisibilities(4);
                View selectDesignatedCorner = selectDesignatedCorner(i2, layoutRtl);
                int cornerIndex = cornerIndex(selectDesignatedCorner);
                int statusBarPaddingTop = this.contentInsetsProvider.getStatusBarPaddingTop(Integer.valueOf(i));
                Object obj2 = this.lock;
                synchronized (obj2) {
                    try {
                        obj = obj2;
                        try {
                            setNextViewState(ViewState.copy$default(this.nextViewState, false, false, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, i, statusBarPaddingTop, cornerIndex, selectDesignatedCorner, 511, (Object) null));
                            Unit unit2 = Unit.INSTANCE;
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        obj = obj2;
                        throw th;
                    }
                }
            }
        }
    }

    private final void hideDotView(View view, boolean z) {
        view.clearAnimation();
        if (z) {
            view.animate().setDuration(160).setInterpolator(Interpolators.ALPHA_OUT).alpha(0.0f).withEndAction(new PrivacyDotViewController$$ExternalSyntheticLambda1(view, this)).start();
            return;
        }
        view.setVisibility(4);
        ShowingListener showingListener2 = this.showingListener;
        if (showingListener2 != null) {
            showingListener2.onPrivacyDotHidden(view);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: hideDotView$lambda-3  reason: not valid java name */
    public static final void m3062hideDotView$lambda3(View view, PrivacyDotViewController privacyDotViewController) {
        Intrinsics.checkNotNullParameter(view, "$dot");
        Intrinsics.checkNotNullParameter(privacyDotViewController, "this$0");
        view.setVisibility(4);
        ShowingListener showingListener2 = privacyDotViewController.showingListener;
        if (showingListener2 != null) {
            showingListener2.onPrivacyDotHidden(view);
        }
    }

    private final void showDotView(View view, boolean z) {
        ShowingListener showingListener2 = this.showingListener;
        if (showingListener2 != null) {
            showingListener2.onPrivacyDotShown(view);
        }
        view.clearAnimation();
        if (z) {
            view.setVisibility(0);
            view.setAlpha(0.0f);
            view.animate().alpha(1.0f).setDuration(160).setInterpolator(Interpolators.ALPHA_IN).start();
            return;
        }
        view.setVisibility(0);
        view.setAlpha(1.0f);
    }

    private final void updateRotations(int i, int i2) {
        for (View next : getViews()) {
            next.setPadding(0, i2, 0, 0);
            int rotatedCorner = rotatedCorner(cornerForView(next), i);
            ViewGroup.LayoutParams layoutParams = next.getLayoutParams();
            if (layoutParams != null) {
                ((FrameLayout.LayoutParams) layoutParams).gravity = PrivacyDotViewControllerKt.toGravity(rotatedCorner);
                ViewGroup.LayoutParams layoutParams2 = next.findViewById(C1894R.C1898id.privacy_dot).getLayoutParams();
                if (layoutParams2 != null) {
                    ((FrameLayout.LayoutParams) layoutParams2).gravity = PrivacyDotViewControllerKt.innerGravity(rotatedCorner);
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                }
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
            }
        }
    }

    private final void updateCornerSizes(int i, int i2, int i3) {
        for (View next : getViews()) {
            int widthForCorner = widthForCorner(rotatedCorner(cornerForView(next), i3), i, i2);
            ViewGroup.LayoutParams layoutParams = next.getLayoutParams();
            if (layoutParams != null) {
                ((FrameLayout.LayoutParams) layoutParams).width = widthForCorner;
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
            }
        }
    }

    private final void setCornerSizes(ViewState viewState) {
        int i;
        int i2;
        boolean layoutRtl = viewState.getLayoutRtl();
        Point point = new Point();
        View view = this.f372tl;
        View view2 = null;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            view = null;
        }
        view.getContext().getDisplay().getRealSize(point);
        View view3 = this.f372tl;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            view3 = null;
        }
        int exactRotation = RotationUtils.getExactRotation(view3.getContext());
        if (exactRotation == 1 || exactRotation == 3) {
            i = point.y;
            i2 = point.x;
        } else {
            i = point.x;
            i2 = point.y;
        }
        View view4 = this.f372tl;
        if (view4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            view4 = null;
        }
        Rect contentRectForRotation = viewState.contentRectForRotation(activeRotationForCorner(view4, layoutRtl));
        View view5 = this.f372tl;
        if (view5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            view5 = null;
        }
        view5.setPadding(0, viewState.getPaddingTop(), 0, 0);
        View view6 = this.f372tl;
        if (view6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            view6 = null;
        }
        ViewGroup.LayoutParams layoutParams = view6.getLayoutParams();
        if (layoutParams != null) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
            layoutParams2.height = contentRectForRotation.height();
            if (layoutRtl) {
                layoutParams2.width = contentRectForRotation.left;
            } else {
                layoutParams2.width = i2 - contentRectForRotation.right;
            }
            View view7 = this.f373tr;
            if (view7 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tr");
                view7 = null;
            }
            Rect contentRectForRotation2 = viewState.contentRectForRotation(activeRotationForCorner(view7, layoutRtl));
            View view8 = this.f373tr;
            if (view8 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tr");
                view8 = null;
            }
            view8.setPadding(0, viewState.getPaddingTop(), 0, 0);
            View view9 = this.f373tr;
            if (view9 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tr");
                view9 = null;
            }
            ViewGroup.LayoutParams layoutParams3 = view9.getLayoutParams();
            if (layoutParams3 != null) {
                FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) layoutParams3;
                layoutParams4.height = contentRectForRotation2.height();
                if (layoutRtl) {
                    layoutParams4.width = contentRectForRotation2.left;
                } else {
                    layoutParams4.width = i - contentRectForRotation2.right;
                }
                View view10 = this.f371br;
                if (view10 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("br");
                    view10 = null;
                }
                Rect contentRectForRotation3 = viewState.contentRectForRotation(activeRotationForCorner(view10, layoutRtl));
                View view11 = this.f371br;
                if (view11 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("br");
                    view11 = null;
                }
                view11.setPadding(0, viewState.getPaddingTop(), 0, 0);
                View view12 = this.f371br;
                if (view12 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("br");
                    view12 = null;
                }
                ViewGroup.LayoutParams layoutParams5 = view12.getLayoutParams();
                if (layoutParams5 != null) {
                    FrameLayout.LayoutParams layoutParams6 = (FrameLayout.LayoutParams) layoutParams5;
                    layoutParams6.height = contentRectForRotation3.height();
                    if (layoutRtl) {
                        layoutParams6.width = contentRectForRotation3.left;
                    } else {
                        layoutParams6.width = i2 - contentRectForRotation3.right;
                    }
                    View view13 = this.f370bl;
                    if (view13 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("bl");
                        view13 = null;
                    }
                    Rect contentRectForRotation4 = viewState.contentRectForRotation(activeRotationForCorner(view13, layoutRtl));
                    View view14 = this.f370bl;
                    if (view14 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("bl");
                        view14 = null;
                    }
                    view14.setPadding(0, viewState.getPaddingTop(), 0, 0);
                    View view15 = this.f370bl;
                    if (view15 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("bl");
                    } else {
                        view2 = view15;
                    }
                    ViewGroup.LayoutParams layoutParams7 = view2.getLayoutParams();
                    if (layoutParams7 != null) {
                        FrameLayout.LayoutParams layoutParams8 = (FrameLayout.LayoutParams) layoutParams7;
                        layoutParams8.height = contentRectForRotation4.height();
                        if (layoutRtl) {
                            layoutParams8.width = contentRectForRotation4.left;
                        } else {
                            layoutParams8.width = i - contentRectForRotation4.right;
                        }
                    } else {
                        throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                    }
                } else {
                    throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                }
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
            }
        } else {
            throw new NullPointerException("null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
        }
    }

    /* access modifiers changed from: private */
    public final View selectDesignatedCorner(int i, boolean z) {
        View view = this.f372tl;
        if (view == null) {
            return null;
        }
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        throw new IllegalStateException("unknown rotation");
                    } else if (z) {
                        View view2 = this.f370bl;
                        if (view2 != null) {
                            return view2;
                        }
                        Intrinsics.throwUninitializedPropertyAccessException("bl");
                    } else if (view != null) {
                        return view;
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("tl");
                    }
                } else if (z) {
                    View view3 = this.f371br;
                    if (view3 != null) {
                        return view3;
                    }
                    Intrinsics.throwUninitializedPropertyAccessException("br");
                } else {
                    View view4 = this.f370bl;
                    if (view4 != null) {
                        return view4;
                    }
                    Intrinsics.throwUninitializedPropertyAccessException("bl");
                }
            } else if (z) {
                View view5 = this.f373tr;
                if (view5 != null) {
                    return view5;
                }
                Intrinsics.throwUninitializedPropertyAccessException("tr");
            } else {
                View view6 = this.f371br;
                if (view6 != null) {
                    return view6;
                }
                Intrinsics.throwUninitializedPropertyAccessException("br");
            }
        } else if (!z) {
            View view7 = this.f373tr;
            if (view7 != null) {
                return view7;
            }
            Intrinsics.throwUninitializedPropertyAccessException("tr");
        } else if (view != null) {
            return view;
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
        }
        return null;
    }

    private final void updateDesignatedCorner(View view, boolean z) {
        if (z) {
            ShowingListener showingListener2 = this.showingListener;
            if (showingListener2 != null) {
                showingListener2.onPrivacyDotShown(view);
            }
            if (view != null) {
                view.clearAnimation();
                view.setVisibility(0);
                view.setAlpha(0.0f);
                view.animate().alpha(1.0f).setDuration(300).start();
            }
        }
    }

    /* access modifiers changed from: private */
    public final void setCornerVisibilities(int i) {
        for (View next : getViews()) {
            next.setVisibility(i);
            if (i == 0) {
                ShowingListener showingListener2 = this.showingListener;
                if (showingListener2 != null) {
                    showingListener2.onPrivacyDotShown(next);
                }
            } else {
                ShowingListener showingListener3 = this.showingListener;
                if (showingListener3 != null) {
                    showingListener3.onPrivacyDotHidden(next);
                }
            }
        }
    }

    private final int cornerForView(View view) {
        View view2 = this.f372tl;
        View view3 = null;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tl");
            view2 = null;
        }
        if (Intrinsics.areEqual((Object) view, (Object) view2)) {
            return 0;
        }
        View view4 = this.f373tr;
        if (view4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("tr");
            view4 = null;
        }
        if (Intrinsics.areEqual((Object) view, (Object) view4)) {
            return 1;
        }
        View view5 = this.f370bl;
        if (view5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("bl");
            view5 = null;
        }
        if (Intrinsics.areEqual((Object) view, (Object) view5)) {
            return 3;
        }
        View view6 = this.f371br;
        if (view6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("br");
        } else {
            view3 = view6;
        }
        if (Intrinsics.areEqual((Object) view, (Object) view3)) {
            return 2;
        }
        throw new IllegalArgumentException("not a corner view");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002b, code lost:
        if (r7 != false) goto L_0x0017;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0045, code lost:
        if (r7 != false) goto L_0x002e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int activeRotationForCorner(android.view.View r6, boolean r7) {
        /*
            r5 = this;
            android.view.View r0 = r5.f373tr
            r1 = 0
            if (r0 != 0) goto L_0x000c
            java.lang.String r0 = "tr"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r0)
            r0 = r1
        L_0x000c:
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r6, (java.lang.Object) r0)
            r2 = 1
            r3 = 0
            if (r0 == 0) goto L_0x0019
            if (r7 == 0) goto L_0x0017
            goto L_0x0049
        L_0x0017:
            r2 = r3
            goto L_0x0049
        L_0x0019:
            android.view.View r0 = r5.f372tl
            if (r0 != 0) goto L_0x0024
            java.lang.String r0 = "tl"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r0)
            r0 = r1
        L_0x0024:
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r6, (java.lang.Object) r0)
            r4 = 3
            if (r0 == 0) goto L_0x0030
            if (r7 == 0) goto L_0x002e
            goto L_0x0017
        L_0x002e:
            r2 = r4
            goto L_0x0049
        L_0x0030:
            android.view.View r5 = r5.f371br
            if (r5 != 0) goto L_0x003a
            java.lang.String r5 = "br"
            kotlin.jvm.internal.Intrinsics.throwUninitializedPropertyAccessException(r5)
            goto L_0x003b
        L_0x003a:
            r1 = r5
        L_0x003b:
            boolean r5 = kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r6, (java.lang.Object) r1)
            r6 = 2
            if (r5 == 0) goto L_0x0045
            if (r7 == 0) goto L_0x0049
            goto L_0x0048
        L_0x0045:
            if (r7 == 0) goto L_0x0048
            goto L_0x002e
        L_0x0048:
            r2 = r6
        L_0x0049:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.events.PrivacyDotViewController.activeRotationForCorner(android.view.View, boolean):int");
    }

    private final int widthForCorner(int i, int i2, int i3) {
        if (i == 0) {
            return i2;
        }
        if (i == 1 || i == 2) {
            return i3;
        }
        if (i == 3) {
            return i2;
        }
        throw new IllegalArgumentException("Unknown corner");
    }

    public final void initialize(View view, View view2, View view3, View view4) {
        View view5 = view;
        View view6 = view2;
        View view7 = view3;
        View view8 = view4;
        Intrinsics.checkNotNullParameter(view5, "topLeft");
        Intrinsics.checkNotNullParameter(view6, "topRight");
        Intrinsics.checkNotNullParameter(view7, "bottomLeft");
        Intrinsics.checkNotNullParameter(view8, "bottomRight");
        View view9 = this.f372tl;
        if (!(view9 == null || this.f373tr == null || this.f370bl == null || this.f371br == null)) {
            if (view9 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("tl");
                view9 = null;
            }
            if (Intrinsics.areEqual((Object) view9, (Object) view5)) {
                View view10 = this.f373tr;
                if (view10 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("tr");
                    view10 = null;
                }
                if (Intrinsics.areEqual((Object) view10, (Object) view6)) {
                    View view11 = this.f370bl;
                    if (view11 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("bl");
                        view11 = null;
                    }
                    if (Intrinsics.areEqual((Object) view11, (Object) view7)) {
                        View view12 = this.f371br;
                        if (view12 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("br");
                            view12 = null;
                        }
                        if (Intrinsics.areEqual((Object) view12, (Object) view8)) {
                            return;
                        }
                    }
                }
            }
        }
        this.f372tl = view5;
        this.f373tr = view6;
        this.f370bl = view7;
        this.f371br = view8;
        boolean isLayoutRtl = this.configurationController.isLayoutRtl();
        View selectDesignatedCorner = selectDesignatedCorner(0, isLayoutRtl);
        int cornerIndex = cornerIndex(selectDesignatedCorner);
        this.mainExecutor.execute(new PrivacyDotViewController$$ExternalSyntheticLambda0(this));
        Rect statusBarContentAreaForRotation = this.contentInsetsProvider.getStatusBarContentAreaForRotation(3);
        Rect statusBarContentAreaForRotation2 = this.contentInsetsProvider.getStatusBarContentAreaForRotation(0);
        Rect statusBarContentAreaForRotation3 = this.contentInsetsProvider.getStatusBarContentAreaForRotation(1);
        Rect statusBarContentAreaForRotation4 = this.contentInsetsProvider.getStatusBarContentAreaForRotation(2);
        int statusBarPaddingTop$default = StatusBarContentInsetsProvider.getStatusBarPaddingTop$default(this.contentInsetsProvider, (Integer) null, 1, (Object) null);
        synchronized (this.lock) {
            setNextViewState(ViewState.copy$default(this.nextViewState, true, false, false, false, statusBarContentAreaForRotation2, statusBarContentAreaForRotation3, statusBarContentAreaForRotation4, statusBarContentAreaForRotation, isLayoutRtl, 0, statusBarPaddingTop$default, cornerIndex, selectDesignatedCorner, 526, (Object) null));
            Unit unit = Unit.INSTANCE;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: initialize$lambda-13  reason: not valid java name */
    public static final void m3063initialize$lambda13(PrivacyDotViewController privacyDotViewController) {
        Intrinsics.checkNotNullParameter(privacyDotViewController, "this$0");
        privacyDotViewController.animationScheduler.addCallback(privacyDotViewController.systemStatusAnimationCallback);
    }

    /* access modifiers changed from: private */
    public final void updateStatusBarState() {
        synchronized (this.lock) {
            setNextViewState(ViewState.copy$default(this.nextViewState, false, false, isShadeInQs(), false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8187, (Object) null));
            Unit unit = Unit.INSTANCE;
        }
    }

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
        this.cancelRunnable = delayableExecutor != null ? delayableExecutor.executeDelayed(new PrivacyDotViewController$$ExternalSyntheticLambda2(this), 100) : null;
    }

    /* access modifiers changed from: private */
    /* renamed from: scheduleUpdate$lambda-16  reason: not valid java name */
    public static final void m3064scheduleUpdate$lambda16(PrivacyDotViewController privacyDotViewController) {
        Intrinsics.checkNotNullParameter(privacyDotViewController, "this$0");
        privacyDotViewController.processNextViewState();
    }

    private final void processNextViewState() {
        ViewState copy$default;
        PrivacyDotViewControllerKt.dlog("processNextViewState: ");
        synchronized (this.lock) {
            copy$default = ViewState.copy$default(this.nextViewState, false, false, false, false, (Rect) null, (Rect) null, (Rect) null, (Rect) null, false, 0, 0, 0, (View) null, 8191, (Object) null);
            Unit unit = Unit.INSTANCE;
        }
        resolveState(copy$default);
    }

    private final void resolveState(ViewState viewState) {
        PrivacyDotViewControllerKt.dlog("resolveState " + viewState);
        if (!viewState.getViewInitialized()) {
            PrivacyDotViewControllerKt.dlog("resolveState: view is not initialized. skipping");
        } else if (Intrinsics.areEqual((Object) viewState, (Object) this.currentViewState)) {
            PrivacyDotViewControllerKt.dlog("resolveState: skipping");
        } else {
            if (viewState.getRotation() != this.currentViewState.getRotation()) {
                updateRotations(viewState.getRotation(), viewState.getPaddingTop());
            }
            if (viewState.needsLayout(this.currentViewState)) {
                setCornerSizes(viewState);
                for (View requestLayout : getViews()) {
                    requestLayout.requestLayout();
                }
            }
            if (!Intrinsics.areEqual((Object) viewState.getDesignatedCorner(), (Object) this.currentViewState.getDesignatedCorner())) {
                updateDesignatedCorner(viewState.getDesignatedCorner(), viewState.shouldShowDot());
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
        return CollectionsKt.listOf(this.contentInsetsProvider.getStatusBarContentAreaForRotation(3), this.contentInsetsProvider.getStatusBarContentAreaForRotation(0), this.contentInsetsProvider.getStatusBarContentAreaForRotation(1), this.contentInsetsProvider.getStatusBarContentAreaForRotation(2));
    }

    /* access modifiers changed from: private */
    public final void setNewLayoutRects() {
        List<Rect> layoutRects = getLayoutRects();
        synchronized (this.lock) {
            setNextViewState(ViewState.copy$default(this.nextViewState, false, false, false, false, layoutRects.get(1), layoutRects.get(2), layoutRects.get(3), layoutRects.get(0), false, 0, 0, 0, (View) null, 7951, (Object) null));
            Unit unit = Unit.INSTANCE;
        }
    }
}
