package com.android.systemui.statusbar.phone;

import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.p026io.PrintWriter;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\f\b\u0007\u0018\u00002\u00020\u0001B)\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0001\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ%\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00192\u000e\u0010\u001a\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u001c0\u001bH\u0016¢\u0006\u0002\u0010\u001dJ\u0006\u0010\u001e\u001a\u00020\fJ\u000e\u0010\u001f\u001a\u00020\u00172\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u0010 \u001a\u00020\u00172\u0006\u0010\u000e\u001a\u00020\u000fJ\u000e\u0010!\u001a\u00020\u00172\u0006\u0010\u0011\u001a\u00020\fJ\u000e\u0010\"\u001a\u00020\u00172\u0006\u0010\u0012\u001a\u00020\fJ\u0010\u0010#\u001a\u00020\u00172\u0006\u0010$\u001a\u00020\u000fH\u0002J\u000e\u0010%\u001a\u00020\u00172\u0006\u0010\u0014\u001a\u00020\fJ\u0010\u0010&\u001a\u00020\u00172\u0006\u0010'\u001a\u00020\fH\u0002R\u000e\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000¨\u0006("}, mo64987d2 = {"Lcom/android/systemui/statusbar/phone/StatusBarHideIconsForBouncerManager;", "Lcom/android/systemui/Dumpable;", "commandQueue", "Lcom/android/systemui/statusbar/CommandQueue;", "mainExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "statusBarWindowStateController", "Lcom/android/systemui/statusbar/window/StatusBarWindowStateController;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "(Lcom/android/systemui/statusbar/CommandQueue;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/statusbar/window/StatusBarWindowStateController;Lcom/android/systemui/dump/DumpManager;)V", "bouncerShowing", "", "bouncerWasShowingWhenHidden", "displayId", "", "hideIconsForBouncer", "isOccluded", "panelExpanded", "statusBarWindowHidden", "topAppHidesStatusBar", "wereIconsJustHidden", "dump", "", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "getShouldHideStatusBarIconsForBouncer", "setBouncerShowingAndTriggerUpdate", "setDisplayId", "setIsOccludedAndTriggerUpdate", "setPanelExpandedAndTriggerUpdate", "setStatusBarStateAndTriggerUpdate", "state", "setTopAppHidesStatusBarAndTriggerUpdate", "updateHideIconsForBouncer", "animate", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: StatusBarHideIconsForBouncerManager.kt */
public final class StatusBarHideIconsForBouncerManager implements Dumpable {
    private boolean bouncerShowing;
    private boolean bouncerWasShowingWhenHidden;
    private final CommandQueue commandQueue;
    private int displayId;
    private boolean hideIconsForBouncer;
    private boolean isOccluded;
    private final DelayableExecutor mainExecutor;
    private boolean panelExpanded;
    private boolean statusBarWindowHidden;
    private boolean topAppHidesStatusBar;
    private boolean wereIconsJustHidden;

    @Inject
    public StatusBarHideIconsForBouncerManager(CommandQueue commandQueue2, @Main DelayableExecutor delayableExecutor, StatusBarWindowStateController statusBarWindowStateController, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(commandQueue2, "commandQueue");
        Intrinsics.checkNotNullParameter(delayableExecutor, "mainExecutor");
        Intrinsics.checkNotNullParameter(statusBarWindowStateController, "statusBarWindowStateController");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.commandQueue = commandQueue2;
        this.mainExecutor = delayableExecutor;
        dumpManager.registerDumpable(this);
        statusBarWindowStateController.addListener(new StatusBarHideIconsForBouncerManager$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    /* renamed from: _init_$lambda-0  reason: not valid java name */
    public static final void m3190_init_$lambda0(StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, int i) {
        Intrinsics.checkNotNullParameter(statusBarHideIconsForBouncerManager, "this$0");
        statusBarHideIconsForBouncerManager.setStatusBarStateAndTriggerUpdate(i);
    }

    public final boolean getShouldHideStatusBarIconsForBouncer() {
        return this.hideIconsForBouncer || this.wereIconsJustHidden;
    }

    private final void setStatusBarStateAndTriggerUpdate(int i) {
        this.statusBarWindowHidden = i == 2;
        updateHideIconsForBouncer(false);
    }

    public final void setDisplayId(int i) {
        this.displayId = i;
    }

    public final void setPanelExpandedAndTriggerUpdate(boolean z) {
        this.panelExpanded = z;
        updateHideIconsForBouncer(false);
    }

    public final void setIsOccludedAndTriggerUpdate(boolean z) {
        this.isOccluded = z;
        updateHideIconsForBouncer(false);
    }

    public final void setBouncerShowingAndTriggerUpdate(boolean z) {
        this.bouncerShowing = z;
        updateHideIconsForBouncer(true);
    }

    public final void setTopAppHidesStatusBarAndTriggerUpdate(boolean z) {
        this.topAppHidesStatusBar = z;
        if (!z && this.wereIconsJustHidden) {
            this.wereIconsJustHidden = false;
            this.commandQueue.recomputeDisableFlags(this.displayId, true);
        }
        updateHideIconsForBouncer(true);
    }

    private final void updateHideIconsForBouncer(boolean z) {
        boolean z2 = false;
        boolean z3 = this.topAppHidesStatusBar && this.isOccluded && (this.statusBarWindowHidden || this.bouncerShowing);
        boolean z4 = !this.panelExpanded && !this.isOccluded && this.bouncerShowing;
        if (z3 || z4) {
            z2 = true;
        }
        if (this.hideIconsForBouncer != z2) {
            this.hideIconsForBouncer = z2;
            if (z2 || !this.bouncerWasShowingWhenHidden) {
                this.commandQueue.recomputeDisableFlags(this.displayId, z);
            } else {
                this.wereIconsJustHidden = true;
                this.mainExecutor.executeDelayed(new StatusBarHideIconsForBouncerManager$$ExternalSyntheticLambda1(this), 500);
            }
        }
        if (z2) {
            this.bouncerWasShowingWhenHidden = this.bouncerShowing;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: updateHideIconsForBouncer$lambda-1  reason: not valid java name */
    public static final void m3191updateHideIconsForBouncer$lambda1(StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager) {
        Intrinsics.checkNotNullParameter(statusBarHideIconsForBouncerManager, "this$0");
        statusBarHideIconsForBouncerManager.wereIconsJustHidden = false;
        statusBarHideIconsForBouncerManager.commandQueue.recomputeDisableFlags(statusBarHideIconsForBouncerManager.displayId, true);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("---- State variables set externally ----");
        printWriter.println("panelExpanded=" + this.panelExpanded);
        printWriter.println("isOccluded=" + this.isOccluded);
        printWriter.println("bouncerShowing=" + this.bouncerShowing);
        printWriter.println("topAppHideStatusBar=" + this.topAppHidesStatusBar);
        printWriter.println("statusBarWindowHidden=" + this.statusBarWindowHidden);
        printWriter.println("displayId=" + this.displayId);
        printWriter.println("---- State variables calculated internally ----");
        printWriter.println("hideIconsForBouncer=" + this.hideIconsForBouncer);
        printWriter.println("bouncerWasShowingWhenHidden=" + this.bouncerWasShowingWhenHidden);
        printWriter.println("wereIconsJustHidden=" + this.wereIconsJustHidden);
    }
}
