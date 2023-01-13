package com.android.systemui.statusbar.phone.panelstate;

import com.android.systemui.dagger.SysUISingleton;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0007\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\tJ\u000e\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000fJ\u0010\u0010\u0015\u001a\u00020\u00122\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\u0006\u0010\u0018\u001a\u00020\u0006J(\u0010\u0019\u001a\u00020\u00122\b\b\u0001\u0010\n\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u0003\u001a\u00020\u0004J\u000e\u0010\u001a\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\tJ\u000e\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u000fJ\u000e\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u000b\u001a\u00020\fJ\u0010\u0010\u001d\u001a\u00020\u00122\u0006\u0010\u000b\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0004¢\u0006\u0002\n\u0000R\u0012\u0010\n\u001a\u00020\u00048\u0002@\u0002X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\b\n\u0000\u0012\u0004\b\r\u0010\u0002R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001e"}, mo65043d2 = {"Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionStateManager;", "", "()V", "dragDownPxAmount", "", "expanded", "", "expansionListeners", "", "Lcom/android/systemui/statusbar/phone/panelstate/PanelExpansionListener;", "fraction", "state", "", "getState$annotations", "stateListeners", "Lcom/android/systemui/statusbar/phone/panelstate/PanelStateListener;", "tracking", "addExpansionListener", "", "listener", "addStateListener", "debugLog", "msg", "", "isClosed", "onPanelExpansionChanged", "removeExpansionListener", "removeStateListener", "updateState", "updateStateInternal", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PanelExpansionStateManager.kt */
public final class PanelExpansionStateManager {
    private float dragDownPxAmount;
    private boolean expanded;
    private final List<PanelExpansionListener> expansionListeners = new ArrayList();
    private float fraction;
    private int state;
    private final List<PanelStateListener> stateListeners = new ArrayList();
    private boolean tracking;

    private final void debugLog(String str) {
    }

    private static /* synthetic */ void getState$annotations() {
    }

    public final void addExpansionListener(PanelExpansionListener panelExpansionListener) {
        Intrinsics.checkNotNullParameter(panelExpansionListener, "listener");
        this.expansionListeners.add(panelExpansionListener);
        panelExpansionListener.onPanelExpansionChanged(new PanelExpansionChangeEvent(this.fraction, this.expanded, this.tracking, this.dragDownPxAmount));
    }

    public final void removeExpansionListener(PanelExpansionListener panelExpansionListener) {
        Intrinsics.checkNotNullParameter(panelExpansionListener, "listener");
        this.expansionListeners.remove((Object) panelExpansionListener);
    }

    public final void addStateListener(PanelStateListener panelStateListener) {
        Intrinsics.checkNotNullParameter(panelStateListener, "listener");
        this.stateListeners.add(panelStateListener);
    }

    public final void removeStateListener(PanelStateListener panelStateListener) {
        Intrinsics.checkNotNullParameter(panelStateListener, "listener");
        this.stateListeners.remove((Object) panelStateListener);
    }

    public final boolean isClosed() {
        return this.state == 0;
    }

    public final void onPanelExpansionChanged(float f, boolean z, boolean z2, float f2) {
        boolean z3;
        boolean z4 = true;
        if (!Float.isNaN(f)) {
            int i = this.state;
            this.fraction = f;
            this.expanded = z;
            this.tracking = z2;
            this.dragDownPxAmount = f2;
            if (z) {
                if (i == 0) {
                    updateStateInternal(1);
                }
                if (f < 1.0f) {
                    z4 = false;
                }
                z3 = false;
            } else {
                z3 = true;
                z4 = false;
            }
            if (z4 && !z2) {
                updateStateInternal(2);
            } else if (z3 && !z2 && this.state != 0) {
                updateStateInternal(0);
            }
            String str = "";
            StringBuilder append = new StringBuilder("panelExpansionChanged:start state=").append(PanelExpansionStateManagerKt.stateToString(i)).append(" end state=").append(PanelExpansionStateManagerKt.stateToString(this.state)).append(" f=").append(f).append(" expanded=").append(z).append(" tracking=").append(z2).append("drawDownPxAmount=").append(f2).append(' ').append(z4 ? " fullyOpened" : str).append(' ');
            if (z3) {
                str = " fullyClosed";
            }
            debugLog(append.append(str).toString());
            PanelExpansionChangeEvent panelExpansionChangeEvent = new PanelExpansionChangeEvent(f, z, z2, f2);
            for (PanelExpansionListener onPanelExpansionChanged : this.expansionListeners) {
                onPanelExpansionChanged.onPanelExpansionChanged(panelExpansionChangeEvent);
            }
            return;
        }
        throw new IllegalArgumentException("fraction cannot be NaN".toString());
    }

    public final void updateState(int i) {
        debugLog("update state: " + PanelExpansionStateManagerKt.stateToString(this.state) + " -> " + PanelExpansionStateManagerKt.stateToString(i));
        if (this.state != i) {
            updateStateInternal(i);
        }
    }

    private final void updateStateInternal(int i) {
        debugLog("go state: " + PanelExpansionStateManagerKt.stateToString(this.state) + " -> " + PanelExpansionStateManagerKt.stateToString(i));
        this.state = i;
        for (PanelStateListener onPanelStateChanged : this.stateListeners) {
            onPanelStateChanged.onPanelStateChanged(i);
        }
    }
}
