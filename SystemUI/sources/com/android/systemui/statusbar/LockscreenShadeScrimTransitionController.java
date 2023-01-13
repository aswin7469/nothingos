package com.android.systemui.statusbar;

import android.content.Context;
import android.util.IndentingPrintWriter;
import android.util.MathUtils;
import com.android.systemui.C1894R;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0010\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0010\u0010\u001f\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\fH\u0014J\b\u0010!\u001a\u00020\u001cH\u0014R\u001a\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000e\"\u0004\b\u0013\u0010\u0010R\u000e\u0010\u0014\u001a\u00020\u0015X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0015X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0017\u001a\u00020\fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0018\u0010\u000e\"\u0004\b\u0019\u0010\u0010R\u000e\u0010\u001a\u001a\u00020\u0015X\u000e¢\u0006\u0002\n\u0000¨\u0006\""}, mo65043d2 = {"Lcom/android/systemui/statusbar/LockscreenShadeScrimTransitionController;", "Lcom/android/systemui/statusbar/AbstractLockscreenShadeTransitionController;", "scrimController", "Lcom/android/systemui/statusbar/phone/ScrimController;", "context", "Landroid/content/Context;", "configurationController", "Lcom/android/systemui/statusbar/policy/ConfigurationController;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Lcom/android/systemui/statusbar/phone/ScrimController;Landroid/content/Context;Lcom/android/systemui/statusbar/policy/ConfigurationController;Lcom/android/systemui/dump/DumpManager;)V", "notificationsScrimDragAmount", "", "getNotificationsScrimDragAmount", "()F", "setNotificationsScrimDragAmount", "(F)V", "notificationsScrimProgress", "getNotificationsScrimProgress", "setNotificationsScrimProgress", "notificationsScrimTransitionDelay", "", "notificationsScrimTransitionDistance", "scrimProgress", "getScrimProgress", "setScrimProgress", "scrimTransitionDistance", "dump", "", "indentingPrintWriter", "Landroid/util/IndentingPrintWriter;", "onDragDownAmountChanged", "dragDownAmount", "updateResources", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: LockscreenShadeScrimTransitionController.kt */
public final class LockscreenShadeScrimTransitionController extends AbstractLockscreenShadeTransitionController {
    private float notificationsScrimDragAmount;
    private float notificationsScrimProgress;
    private int notificationsScrimTransitionDelay;
    private int notificationsScrimTransitionDistance;
    private final ScrimController scrimController;
    private float scrimProgress;
    private int scrimTransitionDistance;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    @Inject
    public LockscreenShadeScrimTransitionController(ScrimController scrimController2, Context context, ConfigurationController configurationController, DumpManager dumpManager) {
        super(context, configurationController, dumpManager);
        Intrinsics.checkNotNullParameter(scrimController2, "scrimController");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.scrimController = scrimController2;
    }

    public final float getScrimProgress() {
        return this.scrimProgress;
    }

    public final void setScrimProgress(float f) {
        this.scrimProgress = f;
    }

    public final float getNotificationsScrimProgress() {
        return this.notificationsScrimProgress;
    }

    public final void setNotificationsScrimProgress(float f) {
        this.notificationsScrimProgress = f;
    }

    public final float getNotificationsScrimDragAmount() {
        return this.notificationsScrimDragAmount;
    }

    public final void setNotificationsScrimDragAmount(float f) {
        this.notificationsScrimDragAmount = f;
    }

    /* access modifiers changed from: protected */
    public void updateResources() {
        this.scrimTransitionDistance = getContext().getResources().getDimensionPixelSize(C1894R.dimen.lockscreen_shade_scrim_transition_distance);
        this.notificationsScrimTransitionDelay = getContext().getResources().getDimensionPixelSize(C1894R.dimen.lockscreen_shade_notifications_scrim_transition_delay);
        this.notificationsScrimTransitionDistance = getContext().getResources().getDimensionPixelSize(C1894R.dimen.lockscreen_shade_notifications_scrim_transition_distance);
    }

    /* access modifiers changed from: protected */
    public void onDragDownAmountChanged(float f) {
        this.scrimProgress = MathUtils.saturate(f / ((float) this.scrimTransitionDistance));
        float f2 = f - ((float) this.notificationsScrimTransitionDelay);
        this.notificationsScrimDragAmount = f2;
        float saturate = MathUtils.saturate(f2 / ((float) this.notificationsScrimTransitionDistance));
        this.notificationsScrimProgress = saturate;
        this.scrimController.setTransitionToFullShadeProgress(this.scrimProgress, saturate);
    }

    public void dump(IndentingPrintWriter indentingPrintWriter) {
        Intrinsics.checkNotNullParameter(indentingPrintWriter, "indentingPrintWriter");
        indentingPrintWriter.println("LockscreenShadeScrimTransitionController:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("Resources:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("scrimTransitionDistance: " + this.scrimTransitionDistance);
        indentingPrintWriter.println("notificationsScrimTransitionDelay: " + this.notificationsScrimTransitionDelay);
        indentingPrintWriter.println("notificationsScrimTransitionDistance: " + this.notificationsScrimTransitionDistance);
        indentingPrintWriter.decreaseIndent();
        indentingPrintWriter.println("State");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println("dragDownAmount: " + getDragDownAmount());
        indentingPrintWriter.println("scrimProgress: " + this.scrimProgress);
        indentingPrintWriter.println("notificationsScrimProgress: " + this.notificationsScrimProgress);
        indentingPrintWriter.println("notificationsScrimDragAmount: " + this.notificationsScrimDragAmount);
    }
}
