package com.android.p019wm.shell.pip.p020tv;

import android.app.ActivityManager;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.view.IWindow;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewRootImpl;
import android.view.WindowManagerGlobal;
import com.android.internal.protolog.common.ProtoLog;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.common.SystemWindows;
import com.android.p019wm.shell.pip.PipMediaController;
import com.android.p019wm.shell.pip.PipMenuController;
import com.android.p019wm.shell.pip.p020tv.TvPipMenuView;
import com.android.p019wm.shell.protolog.ShellProtoLogGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/* renamed from: com.android.wm.shell.pip.tv.TvPipMenuController */
public class TvPipMenuController implements PipMenuController, TvPipMenuView.Listener {
    private static final String BACKGROUND_WINDOW_TITLE = "PipBackgroundView";
    private static final boolean DEBUG = false;
    private static final String TAG = "TvPipMenuController";
    private final List<RemoteAction> mAppActions = new ArrayList();
    private SyncRtSurfaceTransactionApplier mApplier;
    private SyncRtSurfaceTransactionApplier mBackgroundApplier;
    private RemoteAction mCloseAction;
    private boolean mCloseAfterExitMoveMenu;
    private final Runnable mCloseEduTextRunnable = new TvPipMenuController$$ExternalSyntheticLambda0(this);
    private final Context mContext;
    private Delegate mDelegate;
    private boolean mInMoveMode;
    /* access modifiers changed from: private */
    public SurfaceControl mLeash;
    private final Handler mMainHandler;
    private final List<RemoteAction> mMediaActions = new ArrayList();
    Matrix mMoveTransform = new Matrix();
    private View mPipBackgroundView;
    private final int mPipEduTextHeight;
    private final int mPipEduTextShowDurationMs;
    private final int mPipMenuBorderWidth;
    private TvPipMenuView mPipMenuView;
    private final SystemWindows mSystemWindows;
    RectF mTmpDestinationRectF = new RectF();
    RectF mTmpSourceRectF = new RectF();
    private final TvPipBoundsState mTvPipBoundsState;

    /* renamed from: com.android.wm.shell.pip.tv.TvPipMenuController$Delegate */
    interface Delegate {
        void closeEduText();

        void closePip();

        int getPipGravity();

        void movePip(int i);

        void movePipToFullscreen();

        void onInMoveModeChanged();

        void onMenuClosed();

        void togglePipExpansion();
    }

    public boolean isMenuVisible() {
        return true;
    }

    public TvPipMenuController(Context context, TvPipBoundsState tvPipBoundsState, SystemWindows systemWindows, PipMediaController pipMediaController, Handler handler) {
        this.mContext = context;
        this.mTvPipBoundsState = tvPipBoundsState;
        this.mSystemWindows = systemWindows;
        this.mMainHandler = handler;
        context.registerReceiverForAllUsers(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                TvPipMenuController.this.closeMenu();
            }
        }, new IntentFilter("android.intent.action.CLOSE_SYSTEM_DIALOGS"), (String) null, handler, 2);
        pipMediaController.addActionListener(new TvPipMenuController$$ExternalSyntheticLambda1(this));
        this.mPipEduTextShowDurationMs = context.getResources().getInteger(C3353R.integer.pip_edu_text_show_duration_ms);
        this.mPipEduTextHeight = context.getResources().getDimensionPixelSize(C3353R.dimen.pip_menu_edu_text_view_height);
        this.mPipMenuBorderWidth = context.getResources().getDimensionPixelSize(C3353R.dimen.pip_menu_border_width);
    }

    /* access modifiers changed from: package-private */
    public void setDelegate(Delegate delegate) {
        if (this.mDelegate != null) {
            throw new IllegalStateException("The delegate has already been set and should not change.");
        } else if (delegate != null) {
            this.mDelegate = delegate;
        } else {
            throw new IllegalArgumentException("The delegate must not be null.");
        }
    }

    public void attach(SurfaceControl surfaceControl) {
        if (this.mDelegate != null) {
            this.mLeash = surfaceControl;
            attachPipMenu();
            return;
        }
        throw new IllegalStateException("Delegate is not set.");
    }

    private void attachPipMenu() {
        if (this.mPipMenuView != null) {
            detachPipMenu();
        }
        attachPipBackgroundView();
        attachPipMenuView();
        TvPipBoundsState tvPipBoundsState = this.mTvPipBoundsState;
        int i = this.mPipMenuBorderWidth;
        tvPipBoundsState.setPipMenuPermanentDecorInsets(Insets.of(-i, -i, -i, -i));
        this.mTvPipBoundsState.setPipMenuTemporaryDecorInsets(Insets.of(0, 0, 0, -this.mPipEduTextHeight));
        this.mMainHandler.postDelayed(this.mCloseEduTextRunnable, (long) this.mPipEduTextShowDurationMs);
    }

    private void attachPipMenuView() {
        TvPipMenuView tvPipMenuView = new TvPipMenuView(this.mContext);
        this.mPipMenuView = tvPipMenuView;
        tvPipMenuView.setListener(this);
        setUpViewSurfaceZOrder(this.mPipMenuView, 1);
        addPipMenuViewToSystemWindows(this.mPipMenuView, PipMenuController.MENU_WINDOW_TITLE);
        maybeUpdateMenuViewActions();
    }

    private void attachPipBackgroundView() {
        View inflate = LayoutInflater.from(this.mContext).inflate(C3353R.layout.tv_pip_menu_background, (ViewGroup) null);
        this.mPipBackgroundView = inflate;
        setUpViewSurfaceZOrder(inflate, -1);
        addPipMenuViewToSystemWindows(this.mPipBackgroundView, BACKGROUND_WINDOW_TITLE);
    }

    private void setUpViewSurfaceZOrder(View view, final int i) {
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            public void onViewDetachedFromWindow(View view) {
            }

            public void onViewAttachedToWindow(View view) {
                view.getViewRootImpl().addSurfaceChangedCallback(new PipMenuSurfaceChangedCallback(view, i));
            }
        });
    }

    private void addPipMenuViewToSystemWindows(View view, String str) {
        this.mSystemWindows.addView(view, getPipMenuLayoutParams(str, 0, 0), 0, 1);
    }

    /* access modifiers changed from: package-private */
    public void notifyPipAnimating(boolean z) {
        this.mPipMenuView.setEduTextActive(!z);
        if (!z) {
            this.mPipMenuView.onPipTransitionFinished();
        }
    }

    /* access modifiers changed from: package-private */
    public void showMovementMenuOnly() {
        setInMoveMode(true);
        this.mCloseAfterExitMoveMenu = true;
        showMenuInternal();
    }

    public void showMenu() {
        setInMoveMode(false);
        this.mCloseAfterExitMoveMenu = false;
        showMenuInternal();
    }

    private void showMenuInternal() {
        if (this.mPipMenuView != null) {
            maybeCloseEduText();
            maybeUpdateMenuViewActions();
            updateExpansionState();
            grantPipMenuFocus(true);
            if (this.mInMoveMode) {
                this.mPipMenuView.showMoveMenu(this.mDelegate.getPipGravity());
            } else {
                this.mPipMenuView.showButtonsMenu();
            }
            this.mPipMenuView.updateBounds(this.mTvPipBoundsState.getBounds());
        }
    }

    /* access modifiers changed from: package-private */
    public void onPipTransitionStarted(Rect rect) {
        TvPipMenuView tvPipMenuView = this.mPipMenuView;
        if (tvPipMenuView != null) {
            tvPipMenuView.onPipTransitionStarted(rect);
        }
    }

    private void maybeCloseEduText() {
        if (this.mMainHandler.hasCallbacks(this.mCloseEduTextRunnable)) {
            this.mMainHandler.removeCallbacks(this.mCloseEduTextRunnable);
            this.mCloseEduTextRunnable.run();
        }
    }

    /* access modifiers changed from: private */
    public void closeEduText() {
        this.mTvPipBoundsState.setPipMenuTemporaryDecorInsets(Insets.NONE);
        this.mPipMenuView.hideEduText();
        this.mDelegate.closeEduText();
    }

    /* access modifiers changed from: package-private */
    public void updateGravity(int i) {
        this.mPipMenuView.showMovementHints(i);
    }

    /* access modifiers changed from: package-private */
    public void updateExpansionState() {
        this.mPipMenuView.setExpandedModeEnabled(this.mTvPipBoundsState.isTvExpandedPipSupported() && this.mTvPipBoundsState.getDesiredTvExpandedAspectRatio() != 0.0f);
        this.mPipMenuView.setIsExpanded(this.mTvPipBoundsState.isTvPipExpanded());
    }

    private Rect calculateMenuSurfaceBounds(Rect rect) {
        return this.mPipMenuView.getPipMenuContainerBounds(rect);
    }

    /* access modifiers changed from: package-private */
    public void closeMenu() {
        TvPipMenuView tvPipMenuView = this.mPipMenuView;
        if (tvPipMenuView != null) {
            tvPipMenuView.hideAllUserControls();
            grantPipMenuFocus(false);
            this.mDelegate.onMenuClosed();
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isInMoveMode() {
        return this.mInMoveMode;
    }

    private void setInMoveMode(boolean z) {
        if (this.mInMoveMode != z) {
            this.mInMoveMode = z;
            Delegate delegate = this.mDelegate;
            if (delegate != null) {
                delegate.onInMoveModeChanged();
            }
        }
    }

    public void onEnterMoveMode() {
        setInMoveMode(true);
        this.mPipMenuView.showMoveMenu(this.mDelegate.getPipGravity());
    }

    public boolean onExitMoveMode() {
        if (this.mCloseAfterExitMoveMenu) {
            setInMoveMode(false);
            this.mCloseAfterExitMoveMenu = false;
            closeMenu();
            return true;
        } else if (!this.mInMoveMode) {
            return false;
        } else {
            setInMoveMode(false);
            this.mPipMenuView.showButtonsMenu();
            return true;
        }
    }

    public boolean onPipMovement(int i) {
        if (this.mInMoveMode) {
            this.mDelegate.movePip(i);
        }
        return this.mInMoveMode;
    }

    public void detach() {
        closeMenu();
        this.mMainHandler.removeCallbacks(this.mCloseEduTextRunnable);
        detachPipMenu();
        this.mLeash = null;
    }

    public void setAppActions(List<RemoteAction> list, RemoteAction remoteAction) {
        updateAdditionalActionsList(this.mAppActions, list, remoteAction);
    }

    /* access modifiers changed from: private */
    public void onMediaActionsChanged(List<RemoteAction> list) {
        ArrayList arrayList = new ArrayList();
        for (RemoteAction next : list) {
            if (next.isEnabled()) {
                arrayList.add(next);
            }
        }
        updateAdditionalActionsList(this.mMediaActions, arrayList, this.mCloseAction);
    }

    private void updateAdditionalActionsList(List<RemoteAction> list, List<RemoteAction> list2, RemoteAction remoteAction) {
        int size = list2 != null ? list2.size() : 0;
        if (size != 0 || !list.isEmpty() || !Objects.equals(remoteAction, this.mCloseAction)) {
            this.mCloseAction = remoteAction;
            list.clear();
            if (size > 0) {
                list.addAll(list2);
            }
            maybeUpdateMenuViewActions();
        }
    }

    private void maybeUpdateMenuViewActions() {
        if (this.mPipMenuView != null) {
            if (!this.mAppActions.isEmpty()) {
                this.mPipMenuView.setAdditionalActions(this.mAppActions, this.mCloseAction, this.mMainHandler);
            } else {
                this.mPipMenuView.setAdditionalActions(this.mMediaActions, this.mCloseAction, this.mMainHandler);
            }
        }
    }

    public void resizePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
        if (!rect.isEmpty() && maybeCreateSyncApplier()) {
            Rect calculateMenuSurfaceBounds = calculateMenuSurfaceBounds(rect);
            SyncRtSurfaceTransactionApplier.SurfaceParams build = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(getSurfaceControl(this.mPipMenuView)).withWindowCrop(calculateMenuSurfaceBounds).build();
            SyncRtSurfaceTransactionApplier.SurfaceParams build2 = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(getSurfaceControl(this.mPipBackgroundView)).withWindowCrop(calculateMenuSurfaceBounds).build();
            this.mBackgroundApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build2});
            if (surfaceControl == null || transaction == null) {
                this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build});
                return;
            }
            SyncRtSurfaceTransactionApplier.SurfaceParams build3 = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(surfaceControl).withMergeTransaction(transaction).build();
            this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build, build3});
        }
    }

    /* access modifiers changed from: private */
    public SurfaceControl getSurfaceControl(View view) {
        return this.mSystemWindows.getViewSurface(view);
    }

    public void movePipMenu(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
        if (!rect.isEmpty() && maybeCreateSyncApplier()) {
            Rect calculateMenuSurfaceBounds = calculateMenuSurfaceBounds(rect);
            Rect rect2 = new Rect();
            if (surfaceControl == null || transaction == null) {
                rect2.set(0, 0, calculateMenuSurfaceBounds.width(), calculateMenuSurfaceBounds.height());
            } else {
                this.mPipMenuView.getBoundsOnScreen(rect2);
            }
            this.mTmpSourceRectF.set(rect2);
            this.mTmpDestinationRectF.set(calculateMenuSurfaceBounds);
            this.mMoveTransform.setTranslate(this.mTmpDestinationRectF.left, this.mTmpDestinationRectF.top);
            SyncRtSurfaceTransactionApplier.SurfaceParams build = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(getSurfaceControl(this.mPipMenuView)).withMatrix(this.mMoveTransform).build();
            SyncRtSurfaceTransactionApplier.SurfaceParams build2 = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(getSurfaceControl(this.mPipBackgroundView)).withMatrix(this.mMoveTransform).build();
            this.mBackgroundApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build2});
            if (surfaceControl == null || transaction == null) {
                this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build});
            } else {
                SyncRtSurfaceTransactionApplier.SurfaceParams build3 = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(surfaceControl).withMergeTransaction(transaction).build();
                this.mApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{build, build3});
            }
            updateMenuBounds(rect);
        }
    }

    private boolean maybeCreateSyncApplier() {
        TvPipMenuView tvPipMenuView = this.mPipMenuView;
        if (tvPipMenuView == null || tvPipMenuView.getViewRootImpl() == null) {
            ProtoLog.v(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Not going to move PiP, either menu or its parent is not created.", new Object[]{TAG});
            return false;
        }
        if (this.mApplier == null) {
            this.mApplier = new SyncRtSurfaceTransactionApplier(this.mPipMenuView);
        }
        if (this.mBackgroundApplier == null) {
            this.mBackgroundApplier = new SyncRtSurfaceTransactionApplier(this.mPipBackgroundView);
        }
        return true;
    }

    private void detachPipMenu() {
        TvPipMenuView tvPipMenuView = this.mPipMenuView;
        if (tvPipMenuView != null) {
            this.mApplier = null;
            this.mSystemWindows.removeView(tvPipMenuView);
            this.mPipMenuView = null;
        }
        View view = this.mPipBackgroundView;
        if (view != null) {
            this.mBackgroundApplier = null;
            this.mSystemWindows.removeView(view);
            this.mPipBackgroundView = null;
        }
    }

    public void updateMenuBounds(Rect rect) {
        Rect calculateMenuSurfaceBounds = calculateMenuSurfaceBounds(rect);
        this.mSystemWindows.updateViewLayout(this.mPipBackgroundView, getPipMenuLayoutParams(BACKGROUND_WINDOW_TITLE, calculateMenuSurfaceBounds.width(), calculateMenuSurfaceBounds.height()));
        this.mSystemWindows.updateViewLayout(this.mPipMenuView, getPipMenuLayoutParams(PipMenuController.MENU_WINDOW_TITLE, calculateMenuSurfaceBounds.width(), calculateMenuSurfaceBounds.height()));
        TvPipMenuView tvPipMenuView = this.mPipMenuView;
        if (tvPipMenuView != null) {
            tvPipMenuView.updateBounds(rect);
        }
    }

    public void onFocusTaskChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        ProtoLog.d(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: onFocusTaskChanged", new Object[]{TAG});
    }

    public void onBackPress() {
        if (!onExitMoveMode()) {
            closeMenu();
        }
    }

    public void onCloseButtonClick() {
        this.mDelegate.closePip();
    }

    public void onFullscreenButtonClick() {
        this.mDelegate.movePipToFullscreen();
    }

    public void onToggleExpandedMode() {
        this.mDelegate.togglePipExpansion();
    }

    private void grantPipMenuFocus(boolean z) {
        try {
            WindowManagerGlobal.getWindowSession().grantEmbeddedWindowFocus((IWindow) null, this.mSystemWindows.getFocusGrantToken(this.mPipMenuView), z);
        } catch (Exception e) {
            ProtoLog.e(ShellProtoLogGroup.WM_SHELL_PICTURE_IN_PICTURE, "%s: Unable to update focus, %s", new Object[]{TAG, e});
        }
    }

    /* renamed from: com.android.wm.shell.pip.tv.TvPipMenuController$PipMenuSurfaceChangedCallback */
    private class PipMenuSurfaceChangedCallback implements ViewRootImpl.SurfaceChangedCallback {
        private final View mView;
        private final int mZOrder;

        public void surfaceDestroyed() {
        }

        public void surfaceReplaced(SurfaceControl.Transaction transaction) {
        }

        PipMenuSurfaceChangedCallback(View view, int i) {
            this.mView = view;
            this.mZOrder = i;
        }

        public void surfaceCreated(SurfaceControl.Transaction transaction) {
            SurfaceControl access$000 = TvPipMenuController.this.getSurfaceControl(this.mView);
            if (access$000 != null) {
                transaction.setRelativeLayer(access$000, TvPipMenuController.this.mLeash, this.mZOrder);
            }
        }
    }
}
