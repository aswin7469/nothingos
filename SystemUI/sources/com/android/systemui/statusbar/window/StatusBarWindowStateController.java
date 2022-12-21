package com.android.systemui.statusbar.window;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.DisplayId;
import com.android.systemui.statusbar.CommandQueue;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u00009\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000*\u0001\b\b\u0007\u0018\u00002\u00020\u0001B\u0019\b\u0007\u0012\b\b\u0001\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\fJ \u0010\u0013\u001a\u00020\u00112\u0006\u0010\u0014\u001a\u00020\u00032\u0006\u0010\u0015\u001a\u00020\u00032\u0006\u0010\u0016\u001a\u00020\u0003H\u0002J\u0006\u0010\u0017\u001a\u00020\u0018R\u0010\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0004\n\u0002\u0010\tR\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\u00020\u0003X\u000e¢\u0006\b\n\u0000\u0012\u0004\b\u000e\u0010\u000f¨\u0006\u0019"}, mo64987d2 = {"Lcom/android/systemui/statusbar/window/StatusBarWindowStateController;", "", "thisDisplayId", "", "commandQueue", "Lcom/android/systemui/statusbar/CommandQueue;", "(ILcom/android/systemui/statusbar/CommandQueue;)V", "commandQueueCallback", "com/android/systemui/statusbar/window/StatusBarWindowStateController$commandQueueCallback$1", "Lcom/android/systemui/statusbar/window/StatusBarWindowStateController$commandQueueCallback$1;", "listeners", "", "Lcom/android/systemui/statusbar/window/StatusBarWindowStateListener;", "windowState", "getWindowState$annotations", "()V", "addListener", "", "listener", "setWindowState", "displayId", "window", "state", "windowIsShowing", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: StatusBarWindowStateController.kt */
public final class StatusBarWindowStateController {
    private final StatusBarWindowStateController$commandQueueCallback$1 commandQueueCallback;
    private final Set<StatusBarWindowStateListener> listeners = new HashSet();
    private final int thisDisplayId;
    private int windowState;

    private static /* synthetic */ void getWindowState$annotations() {
    }

    @Inject
    public StatusBarWindowStateController(@DisplayId int i, CommandQueue commandQueue) {
        Intrinsics.checkNotNullParameter(commandQueue, "commandQueue");
        this.thisDisplayId = i;
        StatusBarWindowStateController$commandQueueCallback$1 statusBarWindowStateController$commandQueueCallback$1 = new StatusBarWindowStateController$commandQueueCallback$1(this);
        this.commandQueueCallback = statusBarWindowStateController$commandQueueCallback$1;
        commandQueue.addCallback((CommandQueue.Callbacks) statusBarWindowStateController$commandQueueCallback$1);
    }

    public final void addListener(StatusBarWindowStateListener statusBarWindowStateListener) {
        Intrinsics.checkNotNullParameter(statusBarWindowStateListener, "listener");
        this.listeners.add(statusBarWindowStateListener);
    }

    public final boolean windowIsShowing() {
        return this.windowState == 0;
    }

    /* access modifiers changed from: private */
    public final void setWindowState(int i, int i2, int i3) {
        if (i == this.thisDisplayId && i2 == 1 && this.windowState != i3) {
            this.windowState = i3;
            for (StatusBarWindowStateListener onStatusBarWindowStateChanged : this.listeners) {
                onStatusBarWindowStateChanged.onStatusBarWindowStateChanged(i3);
            }
        }
    }
}
