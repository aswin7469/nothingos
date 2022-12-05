package android.view;

import android.graphics.Insets;
import android.graphics.Rect;
import android.util.imetracing.ImeTracing;
import android.util.proto.ProtoOutputStream;
import android.view.SurfaceControl;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;
import java.util.function.Supplier;
/* loaded from: classes3.dex */
public class InsetsSourceConsumer {
    private static final String TAG = "InsetsSourceConsumer";
    protected final InsetsController mController;
    private boolean mHasViewFocusWhenWindowFocusGain;
    private boolean mHasWindowFocus;
    private boolean mIsAnimationPending;
    private Rect mPendingFrame;
    private Rect mPendingVisibleFrame;
    protected boolean mRequestedVisible;
    private InsetsSourceControl mSourceControl;
    protected final InsetsState mState;
    private final Supplier<SurfaceControl.Transaction> mTransactionSupplier;
    protected final int mType;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes3.dex */
    @interface ShowResult {
        public static final int IME_SHOW_DELAYED = 1;
        public static final int IME_SHOW_FAILED = 2;
        public static final int SHOW_IMMEDIATELY = 0;
    }

    public InsetsSourceConsumer(int type, InsetsState state, Supplier<SurfaceControl.Transaction> transactionSupplier, InsetsController controller) {
        this.mType = type;
        this.mState = state;
        this.mTransactionSupplier = transactionSupplier;
        this.mController = controller;
        this.mRequestedVisible = InsetsState.getDefaultVisibility(type);
    }

    public void setControl(InsetsSourceControl control, int[] showTypes, int[] hideTypes) {
        SurfaceControl surfaceControl = null;
        if (this.mType == 19) {
            ImeTracing.getInstance().triggerClientDump("InsetsSourceConsumer#setControl", this.mController.getHost().getInputMethodManager(), null);
        }
        if (Objects.equals(this.mSourceControl, control)) {
            InsetsSourceControl insetsSourceControl = this.mSourceControl;
            if (insetsSourceControl != null && insetsSourceControl != control) {
                insetsSourceControl.release(InsetsAnimationThreadControlRunner$$ExternalSyntheticLambda2.INSTANCE);
                this.mSourceControl = control;
                return;
            }
            return;
        }
        InsetsSourceControl insetsSourceControl2 = this.mSourceControl;
        if (insetsSourceControl2 != null) {
            surfaceControl = insetsSourceControl2.getLeash();
        }
        SurfaceControl oldLeash = surfaceControl;
        InsetsSourceControl lastControl = this.mSourceControl;
        this.mSourceControl = control;
        if (control == null) {
            this.mController.notifyControlRevoked(this);
            InsetsSource source = this.mState.getSource(this.mType);
            boolean serverVisibility = this.mController.getLastDispatchedState().getSourceOrDefaultVisibility(this.mType);
            if (source.isVisible() != serverVisibility) {
                source.setVisible(serverVisibility);
                this.mController.notifyVisibilityChanged();
            }
            applyLocalVisibilityOverride();
        } else {
            boolean requestedVisible = isRequestedVisibleAwaitingControl();
            boolean needAnimation = requestedVisible != this.mState.getSource(this.mType).isVisible();
            if (control.getLeash() != null && (needAnimation || this.mIsAnimationPending)) {
                if (requestedVisible) {
                    showTypes[0] = showTypes[0] | InsetsState.toPublicType(getType());
                } else {
                    hideTypes[0] = hideTypes[0] | InsetsState.toPublicType(getType());
                }
                this.mIsAnimationPending = false;
            } else {
                if (needAnimation) {
                    this.mIsAnimationPending = true;
                }
                if (applyLocalVisibilityOverride()) {
                    this.mController.notifyVisibilityChanged();
                }
                SurfaceControl newLeash = this.mSourceControl.getLeash();
                if (oldLeash == null || newLeash == null || !oldLeash.isSameSurface(newLeash)) {
                    applyHiddenToControl();
                }
                if (!requestedVisible && !this.mIsAnimationPending && lastControl == null) {
                    removeSurface();
                }
            }
        }
        if (lastControl != null) {
            lastControl.release(InsetsAnimationThreadControlRunner$$ExternalSyntheticLambda2.INSTANCE);
        }
    }

    public InsetsSourceControl getControl() {
        return this.mSourceControl;
    }

    protected boolean isRequestedVisibleAwaitingControl() {
        return isRequestedVisible();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getType() {
        return this.mType;
    }

    public void show(boolean fromIme) {
        setRequestedVisible(true);
    }

    public void hide() {
        setRequestedVisible(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void hide(boolean animationFinished, int animationType) {
        hide();
    }

    public void onWindowFocusGained(boolean hasViewFocus) {
        this.mHasWindowFocus = true;
        this.mHasViewFocusWhenWindowFocusGain = hasViewFocus;
    }

    public void onWindowFocusLost() {
        this.mHasWindowFocus = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean hasViewFocusWhenWindowFocusGain() {
        return this.mHasViewFocusWhenWindowFocusGain;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean applyLocalVisibilityOverride() {
        InsetsSource source = this.mState.peekSource(this.mType);
        boolean isVisible = source != null ? source.isVisible() : InsetsState.getDefaultVisibility(this.mType);
        boolean hasControl = this.mSourceControl != null;
        if (this.mType == 19) {
            ImeTracing.getInstance().triggerClientDump("InsetsSourceConsumer#applyLocalVisibilityOverride", this.mController.getHost().getInputMethodManager(), null);
        }
        this.mController.updateCompatSysUiVisibility(this.mType, (hasControl || source == null) ? this.mRequestedVisible : isVisible, hasControl);
        if (hasControl && isVisible != this.mRequestedVisible) {
            this.mState.getSource(this.mType).setVisible(this.mRequestedVisible);
            return true;
        }
        return false;
    }

    public boolean isRequestedVisible() {
        return this.mRequestedVisible;
    }

    public int requestShow(boolean fromController) {
        return 0;
    }

    public void onPerceptible(boolean perceptible) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void notifyHidden() {
    }

    public void removeSurface() {
    }

    public void updateSource(InsetsSource newSource, int animationType) {
        InsetsSource source = this.mState.peekSource(this.mType);
        Rect rect = null;
        if (source == null || animationType == -1 || source.getFrame().equals(newSource.getFrame())) {
            this.mPendingFrame = null;
            this.mPendingVisibleFrame = null;
            this.mState.addSource(newSource);
            return;
        }
        InsetsSource newSource2 = new InsetsSource(newSource);
        this.mPendingFrame = new Rect(newSource2.getFrame());
        if (newSource2.getVisibleFrame() != null) {
            rect = new Rect(newSource2.getVisibleFrame());
        }
        this.mPendingVisibleFrame = rect;
        newSource2.setFrame(source.getFrame());
        newSource2.setVisibleFrame(source.getVisibleFrame());
        this.mState.addSource(newSource2);
    }

    public boolean notifyAnimationFinished() {
        if (this.mPendingFrame != null) {
            InsetsSource source = this.mState.getSource(this.mType);
            source.setFrame(this.mPendingFrame);
            source.setVisibleFrame(this.mPendingVisibleFrame);
            this.mPendingFrame = null;
            this.mPendingVisibleFrame = null;
            return true;
        }
        return false;
    }

    protected void setRequestedVisible(boolean requestedVisible) {
        InsetsSourceControl insetsSourceControl;
        if (this.mRequestedVisible != requestedVisible) {
            this.mRequestedVisible = requestedVisible;
            this.mIsAnimationPending = !this.mIsAnimationPending && (insetsSourceControl = this.mSourceControl) != null && insetsSourceControl.getLeash() == null && !Insets.NONE.equals(this.mSourceControl.getInsetsHint());
            this.mController.onRequestedVisibilityChanged(this);
        }
        if (applyLocalVisibilityOverride()) {
            this.mController.notifyVisibilityChanged();
        }
    }

    private void applyHiddenToControl() {
        InsetsSourceControl insetsSourceControl = this.mSourceControl;
        if (insetsSourceControl == null || insetsSourceControl.getLeash() == null) {
            return;
        }
        SurfaceControl.Transaction t = this.mTransactionSupplier.get();
        if (this.mRequestedVisible) {
            t.show(this.mSourceControl.getLeash());
        } else {
            t.hide(this.mSourceControl.getLeash());
        }
        t.apply();
        onPerceptible(this.mRequestedVisible);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void dumpDebug(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        proto.write(1138166333441L, InsetsState.typeToString(this.mType));
        proto.write(1133871366146L, this.mHasWindowFocus);
        proto.write(1133871366147L, this.mRequestedVisible);
        InsetsSourceControl insetsSourceControl = this.mSourceControl;
        if (insetsSourceControl != null) {
            insetsSourceControl.dumpDebug(proto, 1146756268036L);
        }
        Rect rect = this.mPendingFrame;
        if (rect != null) {
            rect.dumpDebug(proto, 1146756268037L);
        }
        Rect rect2 = this.mPendingVisibleFrame;
        if (rect2 != null) {
            rect2.dumpDebug(proto, 1146756268038L);
        }
        proto.end(token);
    }
}
