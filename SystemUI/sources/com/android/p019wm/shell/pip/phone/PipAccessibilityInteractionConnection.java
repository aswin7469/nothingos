package com.android.p019wm.shell.pip.phone;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.MagnificationSpec;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.IAccessibilityInteractionConnection;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.common.ShellExecutor;
import com.android.p019wm.shell.pip.PipBoundsState;
import com.android.p019wm.shell.pip.PipSnapAlgorithm;
import com.android.p019wm.shell.pip.PipTaskOrganizer;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection */
public class PipAccessibilityInteractionConnection {
    private static final long ACCESSIBILITY_NODE_ID = 1;
    private List<AccessibilityNodeInfo> mAccessibilityNodeInfoList;
    private final AccessibilityCallbacks mCallbacks;
    private final IAccessibilityInteractionConnection mConnectionImpl;
    private final Context mContext;
    private final Rect mExpandedBounds = new Rect();
    private final Rect mExpandedMovementBounds = new Rect();
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExcutor;
    private final PipMotionHelper mMotionHelper;
    private final Rect mNormalBounds = new Rect();
    private final Rect mNormalMovementBounds = new Rect();
    private final PipBoundsState mPipBoundsState;
    private final PipSnapAlgorithm mSnapAlgorithm;
    private final PipTaskOrganizer mTaskOrganizer;
    private Rect mTmpBounds = new Rect();
    private final Runnable mUnstashCallback;
    private final Runnable mUpdateMovementBoundCallback;

    /* renamed from: com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection$AccessibilityCallbacks */
    public interface AccessibilityCallbacks {
        void onAccessibilityShowMenu();
    }

    public PipAccessibilityInteractionConnection(Context context, PipBoundsState pipBoundsState, PipMotionHelper pipMotionHelper, PipTaskOrganizer pipTaskOrganizer, PipSnapAlgorithm pipSnapAlgorithm, AccessibilityCallbacks accessibilityCallbacks, Runnable runnable, Runnable runnable2, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mMainExcutor = shellExecutor;
        this.mPipBoundsState = pipBoundsState;
        this.mMotionHelper = pipMotionHelper;
        this.mTaskOrganizer = pipTaskOrganizer;
        this.mSnapAlgorithm = pipSnapAlgorithm;
        this.mUpdateMovementBoundCallback = runnable;
        this.mUnstashCallback = runnable2;
        this.mCallbacks = accessibilityCallbacks;
        this.mConnectionImpl = new PipAccessibilityInteractionConnectionImpl();
    }

    public void register(AccessibilityManager accessibilityManager) {
        accessibilityManager.setPictureInPictureActionReplacingConnection(this.mConnectionImpl);
    }

    /* access modifiers changed from: private */
    public void findAccessibilityNodeInfoByAccessibilityId(long j, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec, Bundle bundle) {
        try {
            iAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfosResult(j == AccessibilityNodeInfo.ROOT_NODE_ID ? getNodeList() : null, i);
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void performAccessibilityAction(long j, int i, Bundle bundle, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2) {
        boolean z = false;
        if (j == AccessibilityNodeInfo.ROOT_NODE_ID) {
            if (i == C3353R.C3356id.action_pip_resize) {
                if (this.mPipBoundsState.getBounds().width() == this.mNormalBounds.width() && this.mPipBoundsState.getBounds().height() == this.mNormalBounds.height()) {
                    setToExpandedBounds();
                } else {
                    setToNormalBounds();
                }
            } else if (i == C3353R.C3356id.action_pip_stash) {
                this.mMotionHelper.animateToStashedClosestEdge();
            } else if (i == C3353R.C3356id.action_pip_unstash) {
                this.mUnstashCallback.run();
                this.mPipBoundsState.setStashed(0);
            } else if (i == 16) {
                this.mCallbacks.onAccessibilityShowMenu();
            } else if (i == 262144) {
                this.mMotionHelper.expandLeavePip(false);
            } else if (i == 1048576) {
                this.mMotionHelper.dismissPip();
            } else if (i == 16908354) {
                int i5 = bundle.getInt(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_MOVE_WINDOW_X);
                int i6 = bundle.getInt(AccessibilityNodeInfoCompat.ACTION_ARGUMENT_MOVE_WINDOW_Y);
                new Rect().set(this.mPipBoundsState.getBounds());
                this.mTmpBounds.offsetTo(i5, i6);
                this.mMotionHelper.movePip(this.mTmpBounds);
            }
            z = true;
        }
        try {
            iAccessibilityInteractionConnectionCallback.setPerformAccessibilityActionResult(z, i2);
        } catch (RemoteException unused) {
        }
    }

    private void setToExpandedBounds() {
        this.mSnapAlgorithm.applySnapFraction(this.mExpandedBounds, this.mExpandedMovementBounds, this.mSnapAlgorithm.getSnapFraction(this.mPipBoundsState.getBounds(), this.mNormalMovementBounds));
        this.mTaskOrganizer.scheduleFinishResizePip(this.mExpandedBounds, new PipAccessibilityInteractionConnection$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setToExpandedBounds$0$com-android-wm-shell-pip-phone-PipAccessibilityInteractionConnection */
    public /* synthetic */ void mo50304x6e815584(Rect rect) {
        this.mMotionHelper.synchronizePinnedStackBounds();
        this.mUpdateMovementBoundCallback.run();
    }

    private void setToNormalBounds() {
        this.mSnapAlgorithm.applySnapFraction(this.mNormalBounds, this.mNormalMovementBounds, this.mSnapAlgorithm.getSnapFraction(this.mPipBoundsState.getBounds(), this.mExpandedMovementBounds));
        this.mTaskOrganizer.scheduleFinishResizePip(this.mNormalBounds, new PipAccessibilityInteractionConnection$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setToNormalBounds$1$com-android-wm-shell-pip-phone-PipAccessibilityInteractionConnection */
    public /* synthetic */ void mo50305xd98a70b5(Rect rect) {
        this.mMotionHelper.synchronizePinnedStackBounds();
        this.mUpdateMovementBoundCallback.run();
    }

    /* access modifiers changed from: private */
    public void findAccessibilityNodeInfosByViewId(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) {
        try {
            iAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfoResult((AccessibilityNodeInfo) null, i);
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void findAccessibilityNodeInfosByText(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) {
        try {
            iAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfoResult((AccessibilityNodeInfo) null, i);
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void findFocus(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) {
        try {
            iAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfoResult((AccessibilityNodeInfo) null, i2);
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void focusSearch(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) {
        try {
            iAccessibilityInteractionConnectionCallback.setFindAccessibilityNodeInfoResult((AccessibilityNodeInfo) null, i2);
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: package-private */
    public void onMovementBoundsChanged(Rect rect, Rect rect2, Rect rect3, Rect rect4) {
        this.mNormalBounds.set(rect);
        this.mExpandedBounds.set(rect2);
        this.mNormalMovementBounds.set(rect3);
        this.mExpandedMovementBounds.set(rect4);
    }

    public static AccessibilityNodeInfo obtainRootAccessibilityNodeInfo(Context context) {
        AccessibilityNodeInfo obtain = AccessibilityNodeInfo.obtain();
        obtain.setSourceNodeId(AccessibilityNodeInfo.ROOT_NODE_ID, -3);
        obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_CLICK);
        obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_DISMISS);
        obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_MOVE_WINDOW);
        obtain.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_EXPAND);
        obtain.addAction(new AccessibilityNodeInfo.AccessibilityAction(C3353R.C3356id.action_pip_resize, context.getString(C3353R.string.accessibility_action_pip_resize)));
        obtain.addAction(new AccessibilityNodeInfo.AccessibilityAction(C3353R.C3356id.action_pip_stash, context.getString(C3353R.string.accessibility_action_pip_stash)));
        obtain.addAction(new AccessibilityNodeInfo.AccessibilityAction(C3353R.C3356id.action_pip_unstash, context.getString(C3353R.string.accessibility_action_pip_unstash)));
        obtain.setImportantForAccessibility(true);
        obtain.setClickable(true);
        obtain.setVisibleToUser(true);
        return obtain;
    }

    private List<AccessibilityNodeInfo> getNodeList() {
        if (this.mAccessibilityNodeInfoList == null) {
            this.mAccessibilityNodeInfoList = new ArrayList(1);
        }
        AccessibilityNodeInfo obtainRootAccessibilityNodeInfo = obtainRootAccessibilityNodeInfo(this.mContext);
        this.mAccessibilityNodeInfoList.clear();
        this.mAccessibilityNodeInfoList.add(obtainRootAccessibilityNodeInfo);
        return this.mAccessibilityNodeInfoList;
    }

    /* renamed from: com.android.wm.shell.pip.phone.PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl */
    private class PipAccessibilityInteractionConnectionImpl extends IAccessibilityInteractionConnection.Stub {
        public void clearAccessibilityFocus() throws RemoteException {
        }

        public void notifyOutsideTouch() throws RemoteException {
        }

        private PipAccessibilityInteractionConnectionImpl() {
        }

        public void findAccessibilityNodeInfoByAccessibilityId(long j, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec, float[] fArr, Bundle bundle) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new C3532xdda251a1(this, j, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec, bundle));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$findAccessibilityNodeInfoByAccessibilityId$0$com-android-wm-shell-pip-phone-PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl */
        public /* synthetic */ void mo50315x49b39ffd(long j, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec, Bundle bundle) {
            PipAccessibilityInteractionConnection.this.findAccessibilityNodeInfoByAccessibilityId(j, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec, bundle);
        }

        public void findAccessibilityNodeInfosByViewId(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec, float[] fArr) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new C3533xdda251a2(this, j, str, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$findAccessibilityNodeInfosByViewId$1$com-android-wm-shell-pip-phone-PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl */
        public /* synthetic */ void mo50317xc7c7b304(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) {
            PipAccessibilityInteractionConnection.this.findAccessibilityNodeInfosByViewId(j, str, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec);
        }

        public void findAccessibilityNodeInfosByText(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec, float[] fArr) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new C3530xdda2519f(this, j, str, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$findAccessibilityNodeInfosByText$2$com-android-wm-shell-pip-phone-PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl */
        public /* synthetic */ void mo50316xe9dd8d76(long j, String str, Region region, int i, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i2, int i3, long j2, MagnificationSpec magnificationSpec) {
            PipAccessibilityInteractionConnection.this.findAccessibilityNodeInfosByText(j, str, region, i, iAccessibilityInteractionConnectionCallback, i2, i3, j2, magnificationSpec);
        }

        public void findFocus(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec, float[] fArr) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new C3531xdda251a0(this, j, i, region, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2, magnificationSpec));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$findFocus$3$com-android-wm-shell-pip-phone-PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl */
        public /* synthetic */ void mo50318xa41f6ec2(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) {
            PipAccessibilityInteractionConnection.this.findFocus(j, i, region, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2, magnificationSpec);
        }

        public void focusSearch(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec, float[] fArr) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new C3528xdda2519d(this, j, i, region, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2, magnificationSpec));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$focusSearch$4$com-android-wm-shell-pip-phone-PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl */
        public /* synthetic */ void mo50319xe838b180(long j, int i, Region region, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2, MagnificationSpec magnificationSpec) {
            PipAccessibilityInteractionConnection.this.focusSearch(j, i, region, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2, magnificationSpec);
        }

        public void performAccessibilityAction(long j, int i, Bundle bundle, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2) throws RemoteException {
            PipAccessibilityInteractionConnection.this.mMainExcutor.execute(new C3529xdda2519e(this, j, i, bundle, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$performAccessibilityAction$5$com-android-wm-shell-pip-phone-PipAccessibilityInteractionConnection$PipAccessibilityInteractionConnectionImpl */
        public /* synthetic */ void mo50320x2a817942(long j, int i, Bundle bundle, int i2, IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback, int i3, int i4, long j2) {
            PipAccessibilityInteractionConnection.this.performAccessibilityAction(j, i, bundle, i2, iAccessibilityInteractionConnectionCallback, i3, i4, j2);
        }
    }
}
