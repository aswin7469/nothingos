package com.android.systemui.biometrics;

import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;
import androidx.core.app.NotificationCompat;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.UdfpsAnimationView;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionChangeEvent;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.util.ViewController;
import java.p026io.PrintWriter;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0007\n\u0002\b\u0002\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0012\u0004\u0012\u0002H\u00010\u00032\u00020\u0004B-\u0012\u0006\u0010\u0005\u001a\u00028\u0000\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r¢\u0006\u0002\u0010\u000eJ\u0010\u00102\u001a\u0002032\u0006\u00104\u001a\u00020\u0016H\u0016J\u0006\u00105\u001a\u000203J#\u00106\u001a\u0002032\u0006\u00107\u001a\u0002082\f\u00109\u001a\b\u0012\u0004\u0012\u00020\u00160:H\u0016¢\u0006\u0002\u0010;J\b\u0010<\u001a\u00020\u0018H\u0016J\u0006\u0010=\u001a\u000203J\u0006\u0010>\u001a\u000203J\u000e\u0010?\u001a\u0002032\u0006\u0010@\u001a\u00020AJ\b\u0010B\u001a\u000203H\u0016J\b\u0010C\u001a\u000203H\u0014J\b\u0010D\u001a\u000203H\u0014J\u0006\u0010E\u001a\u000203J\b\u0010F\u001a\u00020\u0018H\u0016J\b\u0010G\u001a\u000203H\u0016J\u0010\u0010G\u001a\u0002032\u0006\u0010H\u001a\u00020IH\u0016J\u0006\u0010J\u001a\u000203R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0017\u001a\u00020\u0018X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0019\u0010\u001a\"\u0004\b\u001b\u0010\u001cR\u0014\u0010\u001d\u001a\u00020\u001eXD¢\u0006\b\n\u0000\u001a\u0004\b\u001f\u0010 R\u0014\u0010!\u001a\u00020\u001eXD¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010 R\u000e\u0010#\u001a\u00020$X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\u00020\tX\u0004¢\u0006\b\n\u0000\u001a\u0004\b%\u0010&R\u0014\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\b\n\u0000\u001a\u0004\b'\u0010(R\u0012\u0010)\u001a\u00020\u0016X¤\u0004¢\u0006\u0006\u001a\u0004\b*\u0010+R\u0014\u0010,\u001a\u00020-X\u0004¢\u0006\b\n\u0000\u001a\u0004\b.\u0010/R\u0014\u0010\u0005\u001a\u00028\u00008BX\u0004¢\u0006\u0006\u001a\u0004\b0\u00101¨\u0006K"}, mo64987d2 = {"Lcom/android/systemui/biometrics/UdfpsAnimationViewController;", "T", "Lcom/android/systemui/biometrics/UdfpsAnimationView;", "Lcom/android/systemui/util/ViewController;", "Lcom/android/systemui/Dumpable;", "view", "statusBarStateController", "Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "panelExpansionStateManager", "Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionStateManager;", "dialogManager", "Lcom/android/systemui/statusbar/phone/SystemUIDialogManager;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Lcom/android/systemui/biometrics/UdfpsAnimationView;Lcom/android/systemui/plugins/statusbar/StatusBarStateController;Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionStateManager;Lcom/android/systemui/statusbar/phone/SystemUIDialogManager;Lcom/android/systemui/dump/DumpManager;)V", "dialogAlphaAnimator", "Landroid/animation/ValueAnimator;", "dialogListener", "Lcom/android/systemui/statusbar/phone/SystemUIDialogManager$Listener;", "getDialogManager", "()Lcom/android/systemui/statusbar/phone/SystemUIDialogManager;", "dumpTag", "", "notificationShadeVisible", "", "getNotificationShadeVisible", "()Z", "setNotificationShadeVisible", "(Z)V", "paddingX", "", "getPaddingX", "()I", "paddingY", "getPaddingY", "panelExpansionListener", "Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionListener;", "getPanelExpansionStateManager", "()Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionStateManager;", "getStatusBarStateController", "()Lcom/android/systemui/plugins/statusbar/StatusBarStateController;", "tag", "getTag", "()Ljava/lang/String;", "touchTranslation", "Landroid/graphics/PointF;", "getTouchTranslation", "()Landroid/graphics/PointF;", "getView", "()Lcom/android/systemui/biometrics/UdfpsAnimationView;", "doAnnounceForAccessibility", "", "str", "dozeTimeTick", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "listenForTouchesOutsideView", "onIlluminationStarting", "onIlluminationStopped", "onSensorRectUpdated", "sensorRect", "Landroid/graphics/RectF;", "onTouchOutsideView", "onViewAttached", "onViewDetached", "runDialogAlphaAnimator", "shouldPauseAuth", "updateAlpha", "alpha", "", "updatePauseAuth", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: UdfpsAnimationViewController.kt */
public abstract class UdfpsAnimationViewController<T extends UdfpsAnimationView> extends ViewController<T> implements Dumpable {
    private ValueAnimator dialogAlphaAnimator;
    private final SystemUIDialogManager.Listener dialogListener = new UdfpsAnimationViewController$$ExternalSyntheticLambda0(this);
    private final SystemUIDialogManager dialogManager;
    private final DumpManager dumpManager;
    private final String dumpTag;
    private boolean notificationShadeVisible;
    private final int paddingX;
    private final int paddingY;
    private final PanelExpansionListener panelExpansionListener;
    private final PanelExpansionStateManager panelExpansionStateManager;
    private final StatusBarStateController statusBarStateController;
    private final PointF touchTranslation;

    public void doAnnounceForAccessibility(String str) {
        Intrinsics.checkNotNullParameter(str, "str");
    }

    /* access modifiers changed from: protected */
    public abstract String getTag();

    public boolean listenForTouchesOutsideView() {
        return false;
    }

    public void onTouchOutsideView() {
    }

    /* access modifiers changed from: protected */
    public final StatusBarStateController getStatusBarStateController() {
        return this.statusBarStateController;
    }

    /* access modifiers changed from: protected */
    public final PanelExpansionStateManager getPanelExpansionStateManager() {
        return this.panelExpansionStateManager;
    }

    /* access modifiers changed from: protected */
    public final SystemUIDialogManager getDialogManager() {
        return this.dialogManager;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public UdfpsAnimationViewController(T t, StatusBarStateController statusBarStateController2, PanelExpansionStateManager panelExpansionStateManager2, SystemUIDialogManager systemUIDialogManager, DumpManager dumpManager2) {
        super((View) t);
        Intrinsics.checkNotNullParameter(t, "view");
        Intrinsics.checkNotNullParameter(statusBarStateController2, "statusBarStateController");
        Intrinsics.checkNotNullParameter(panelExpansionStateManager2, "panelExpansionStateManager");
        Intrinsics.checkNotNullParameter(systemUIDialogManager, "dialogManager");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        this.statusBarStateController = statusBarStateController2;
        this.panelExpansionStateManager = panelExpansionStateManager2;
        this.dialogManager = systemUIDialogManager;
        this.dumpManager = dumpManager2;
        this.panelExpansionListener = new UdfpsAnimationViewController$$ExternalSyntheticLambda1(this, t);
        this.touchTranslation = new PointF(0.0f, 0.0f);
        this.dumpTag = getTag() + " (" + this + ')';
    }

    private final T getView() {
        T t = this.mView;
        Intrinsics.checkNotNull(t);
        return (UdfpsAnimationView) t;
    }

    /* access modifiers changed from: private */
    /* renamed from: dialogListener$lambda-0  reason: not valid java name */
    public static final void m2579dialogListener$lambda0(UdfpsAnimationViewController udfpsAnimationViewController, boolean z) {
        Intrinsics.checkNotNullParameter(udfpsAnimationViewController, "this$0");
        udfpsAnimationViewController.runDialogAlphaAnimator();
    }

    /* access modifiers changed from: private */
    /* renamed from: panelExpansionListener$lambda-1  reason: not valid java name */
    public static final void m2580panelExpansionListener$lambda1(UdfpsAnimationViewController udfpsAnimationViewController, UdfpsAnimationView udfpsAnimationView, PanelExpansionChangeEvent panelExpansionChangeEvent) {
        Intrinsics.checkNotNullParameter(udfpsAnimationViewController, "this$0");
        Intrinsics.checkNotNullParameter(udfpsAnimationView, "$view");
        Intrinsics.checkNotNullParameter(panelExpansionChangeEvent, NotificationCompat.CATEGORY_EVENT);
        udfpsAnimationViewController.notificationShadeVisible = panelExpansionChangeEvent.getExpanded() && panelExpansionChangeEvent.getFraction() > 0.0f;
        udfpsAnimationView.onExpansionChanged(panelExpansionChangeEvent.getFraction());
        udfpsAnimationViewController.updatePauseAuth();
    }

    public final boolean getNotificationShadeVisible() {
        return this.notificationShadeVisible;
    }

    public final void setNotificationShadeVisible(boolean z) {
        this.notificationShadeVisible = z;
    }

    public PointF getTouchTranslation() {
        return this.touchTranslation;
    }

    public int getPaddingX() {
        return this.paddingX;
    }

    public int getPaddingY() {
        return this.paddingY;
    }

    public void updateAlpha() {
        getView().updateAlpha();
    }

    public final void runDialogAlphaAnimator() {
        boolean shouldHideAffordance = this.dialogManager.shouldHideAffordance();
        ValueAnimator valueAnimator = this.dialogAlphaAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        float[] fArr = new float[2];
        fArr[0] = ((float) getView().calculateAlpha()) / 255.0f;
        fArr[1] = shouldHideAffordance ? 0.0f : 1.0f;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(fArr);
        ofFloat.setDuration(shouldHideAffordance ? 83 : 200);
        ofFloat.setInterpolator(shouldHideAffordance ? Interpolators.LINEAR : Interpolators.ALPHA_IN);
        ofFloat.addUpdateListener(new UdfpsAnimationViewController$$ExternalSyntheticLambda2(this));
        ofFloat.start();
        this.dialogAlphaAnimator = ofFloat;
    }

    /* access modifiers changed from: private */
    /* renamed from: runDialogAlphaAnimator$lambda-3$lambda-2  reason: not valid java name */
    public static final void m2581runDialogAlphaAnimator$lambda3$lambda2(UdfpsAnimationViewController udfpsAnimationViewController, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(udfpsAnimationViewController, "this$0");
        UdfpsAnimationView view = udfpsAnimationViewController.getView();
        Object animatedValue = valueAnimator.getAnimatedValue();
        if (animatedValue != null) {
            view.setDialogSuggestedAlpha(((Float) animatedValue).floatValue());
            udfpsAnimationViewController.updateAlpha();
            udfpsAnimationViewController.updatePauseAuth();
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Float");
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.panelExpansionStateManager.addExpansionListener(this.panelExpansionListener);
        this.dialogManager.registerListener(this.dialogListener);
        this.dumpManager.registerDumpable(this.dumpTag, this);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        this.panelExpansionStateManager.removeExpansionListener(this.panelExpansionListener);
        this.dialogManager.unregisterListener(this.dialogListener);
        this.dumpManager.unregisterDumpable(this.dumpTag);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("mNotificationShadeVisible=" + this.notificationShadeVisible);
        printWriter.println("shouldPauseAuth()=" + shouldPauseAuth());
        printWriter.println("isPauseAuth=" + getView().isPauseAuth());
        printWriter.println("dialogSuggestedAlpha=" + getView().getDialogSuggestedAlpha());
    }

    public boolean shouldPauseAuth() {
        return this.notificationShadeVisible || this.dialogManager.shouldHideAffordance();
    }

    public final void updatePauseAuth() {
        if (getView().setPauseAuth(shouldPauseAuth())) {
            getView().postInvalidate();
        }
    }

    public final void onSensorRectUpdated(RectF rectF) {
        Intrinsics.checkNotNullParameter(rectF, "sensorRect");
        getView().onSensorRectUpdated(rectF);
    }

    public final void dozeTimeTick() {
        if (getView().dozeTimeTick()) {
            getView().postInvalidate();
        }
    }

    public final void onIlluminationStarting() {
        getView().onIlluminationStarting();
        getView().postInvalidate();
    }

    public final void onIlluminationStopped() {
        getView().onIlluminationStopped();
        getView().postInvalidate();
    }

    public void updateAlpha(float f) {
        ((UdfpsAnimationView) this.mView).setAlpha(f);
    }
}
