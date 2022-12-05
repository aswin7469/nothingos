package android.view;

import android.util.proto.ProtoOutputStream;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import java.util.Objects;
/* loaded from: classes3.dex */
public final class ImeFocusController {
    private static final boolean DEBUG = false;
    private static final String TAG = "ImeFocusController";
    private InputMethodManagerDelegate mDelegate;
    private boolean mHasImeFocus = false;
    private View mNextServedView;
    private View mServedView;
    private final ViewRootImpl mViewRootImpl;

    /* loaded from: classes3.dex */
    public interface InputMethodManagerDelegate {
        void closeCurrentIme();

        void finishComposingText();

        void finishInput();

        void finishInputAndReportToIme();

        boolean hasActiveConnection(View view);

        boolean isCurrentRootView(ViewRootImpl viewRootImpl);

        boolean isRestartOnNextWindowFocus(boolean z);

        void setCurrentRootView(ViewRootImpl viewRootImpl);

        boolean startInput(int i, View view, int i2, int i3, int i4);

        void startInputAsyncOnWindowFocusGain(View view, int i, int i2, boolean z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ImeFocusController(ViewRootImpl viewRootImpl) {
        this.mViewRootImpl = viewRootImpl;
    }

    private InputMethodManagerDelegate getImmDelegate() {
        InputMethodManagerDelegate delegate = this.mDelegate;
        if (delegate != null) {
            return delegate;
        }
        InputMethodManagerDelegate delegate2 = ((InputMethodManager) this.mViewRootImpl.mContext.getSystemService(InputMethodManager.class)).getDelegate();
        this.mDelegate = delegate2;
        return delegate2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onMovedToDisplay() {
        this.mDelegate = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onTraversal(boolean hasWindowFocus, WindowManager.LayoutParams windowAttribute) {
        boolean hasImeFocus = updateImeFocusable(windowAttribute, false);
        if (!hasWindowFocus || isInLocalFocusMode(windowAttribute) || hasImeFocus == this.mHasImeFocus) {
            return;
        }
        this.mHasImeFocus = hasImeFocus;
        if (hasImeFocus) {
            onPreWindowFocus(true, windowAttribute);
            onPostWindowFocus(this.mViewRootImpl.mView.findFocus(), true, windowAttribute);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPreWindowFocus(boolean hasWindowFocus, WindowManager.LayoutParams windowAttribute) {
        if (this.mHasImeFocus && !isInLocalFocusMode(windowAttribute) && hasWindowFocus) {
            getImmDelegate().setCurrentRootView(this.mViewRootImpl);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean updateImeFocusable(WindowManager.LayoutParams windowAttribute, boolean force) {
        boolean hasImeFocus = WindowManager.LayoutParams.mayUseInputMethod(windowAttribute.flags);
        if (force) {
            this.mHasImeFocus = hasImeFocus;
        }
        return hasImeFocus;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onPostWindowFocus(View focusedView, boolean hasWindowFocus, WindowManager.LayoutParams windowAttribute) {
        if (!hasWindowFocus || !this.mHasImeFocus || isInLocalFocusMode(windowAttribute)) {
            return;
        }
        View viewForWindowFocus = focusedView != null ? focusedView : this.mViewRootImpl.mView;
        boolean forceFocus = false;
        InputMethodManagerDelegate immDelegate = getImmDelegate();
        boolean nextFocusIsServedView = true;
        if (immDelegate.isRestartOnNextWindowFocus(true)) {
            forceFocus = true;
        }
        onViewFocusChanged(viewForWindowFocus, true);
        if (this.mServedView != viewForWindowFocus) {
            nextFocusIsServedView = false;
        }
        if (nextFocusIsServedView && !immDelegate.hasActiveConnection(viewForWindowFocus)) {
            forceFocus = true;
        }
        immDelegate.startInputAsyncOnWindowFocusGain(viewForWindowFocus, windowAttribute.softInputMode, windowAttribute.flags, forceFocus);
    }

    public boolean checkFocus(boolean forceNewFocus, boolean startInput) {
        InputMethodManagerDelegate immDelegate = getImmDelegate();
        if (immDelegate.isCurrentRootView(this.mViewRootImpl)) {
            View view = this.mServedView;
            View view2 = this.mNextServedView;
            if (view != view2 || forceNewFocus) {
                if (view2 == null) {
                    immDelegate.finishInput();
                    immDelegate.closeCurrentIme();
                    return false;
                }
                this.mServedView = view2;
                immDelegate.finishComposingText();
                if (startInput) {
                    immDelegate.startInput(5, null, 0, 0, 0);
                    return true;
                }
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onViewFocusChanged(View view, boolean hasFocus) {
        if (view == null || view.isTemporarilyDetached() || !getImmDelegate().isCurrentRootView(view.getViewRootImpl()) || !view.hasImeFocus() || !view.hasWindowFocus()) {
            return;
        }
        if (hasFocus) {
            this.mNextServedView = view;
        }
        this.mViewRootImpl.dispatchCheckFocus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onViewDetachedFromWindow(View view) {
        if (getImmDelegate().isCurrentRootView(view.getViewRootImpl()) && this.mServedView == view) {
            this.mNextServedView = null;
            this.mViewRootImpl.dispatchCheckFocus();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onWindowDismissed() {
        InputMethodManagerDelegate immDelegate = getImmDelegate();
        if (!immDelegate.isCurrentRootView(this.mViewRootImpl)) {
            return;
        }
        if (this.mServedView != null) {
            immDelegate.finishInput();
        }
        immDelegate.setCurrentRootView(null);
        this.mHasImeFocus = false;
    }

    public void onInteractiveChanged(boolean interactive) {
        InputMethodManagerDelegate immDelegate = getImmDelegate();
        if (!immDelegate.isCurrentRootView(this.mViewRootImpl)) {
            return;
        }
        if (interactive) {
            View focusedView = this.mViewRootImpl.mView.findFocus();
            onViewFocusChanged(focusedView, focusedView != null);
            return;
        }
        this.mDelegate.finishInputAndReportToIme();
    }

    private static boolean isInLocalFocusMode(WindowManager.LayoutParams windowAttribute) {
        return (windowAttribute.flags & 268435456) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int onProcessImeInputStage(Object token, InputEvent event, WindowManager.LayoutParams windowAttribute, InputMethodManager.FinishedInputEventCallback callback) {
        InputMethodManager imm;
        if (!this.mHasImeFocus || isInLocalFocusMode(windowAttribute) || (imm = (InputMethodManager) this.mViewRootImpl.mContext.getSystemService(InputMethodManager.class)) == null) {
            return 0;
        }
        return imm.dispatchInputEvent(event, token, callback, this.mViewRootImpl.mHandler);
    }

    public View getServedView() {
        return this.mServedView;
    }

    public View getNextServedView() {
        return this.mNextServedView;
    }

    public void setServedView(View view) {
        this.mServedView = view;
    }

    public void setNextServedView(View view) {
        this.mNextServedView = view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasImeFocus() {
        return this.mHasImeFocus;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1133871366145L, this.mHasImeFocus);
        proto.write(1138166333442L, Objects.toString(this.mServedView));
        proto.write(1138166333443L, Objects.toString(this.mNextServedView));
        proto.end(token);
    }
}
