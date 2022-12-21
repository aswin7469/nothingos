package com.android.systemui.doze;

import android.hardware.display.AmbientDisplayConfiguration;
import android.util.Log;
import com.android.systemui.dock.DockManager;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.dagger.DozeScope;
import java.p026io.PrintWriter;
import javax.inject.Inject;

@DozeScope
public class DozeDockHandler implements DozeMachine.Part {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = DozeService.DEBUG;
    private static final String TAG = "DozeDockHandler";
    /* access modifiers changed from: private */
    public final AmbientDisplayConfiguration mConfig;
    private final DockEventListener mDockEventListener;
    /* access modifiers changed from: private */
    public final DockManager mDockManager;
    /* access modifiers changed from: private */
    public int mDockState = 0;
    /* access modifiers changed from: private */
    public DozeMachine mMachine;

    @Inject
    DozeDockHandler(AmbientDisplayConfiguration ambientDisplayConfiguration, DockManager dockManager) {
        this.mConfig = ambientDisplayConfiguration;
        this.mDockManager = dockManager;
        this.mDockEventListener = new DockEventListener(this, (C20551) null);
    }

    public void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }

    /* renamed from: com.android.systemui.doze.DozeDockHandler$1 */
    static /* synthetic */ class C20551 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$doze$DozeMachine$State;

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        static {
            /*
                com.android.systemui.doze.DozeMachine$State[] r0 = com.android.systemui.doze.DozeMachine.State.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$com$android$systemui$doze$DozeMachine$State = r0
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.INITIALIZED     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = $SwitchMap$com$android$systemui$doze$DozeMachine$State     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.doze.DozeMachine$State r1 = com.android.systemui.doze.DozeMachine.State.FINISH     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeDockHandler.C20551.<clinit>():void");
        }
    }

    public void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        int i = C20551.$SwitchMap$com$android$systemui$doze$DozeMachine$State[state2.ordinal()];
        if (i == 1) {
            this.mDockEventListener.register();
        } else if (i == 2) {
            this.mDockEventListener.unregister();
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("DozeDockHandler:");
        printWriter.println(" dockState=" + this.mDockState);
    }

    private class DockEventListener implements DockManager.DockEventListener {
        private boolean mRegistered;

        private DockEventListener() {
        }

        /* synthetic */ DockEventListener(DozeDockHandler dozeDockHandler, C20551 r2) {
            this();
        }

        public void onEvent(int i) {
            DozeMachine.State state;
            if (DozeDockHandler.DEBUG) {
                Log.d(DozeDockHandler.TAG, "dock event = " + i);
            }
            if (DozeDockHandler.this.mDockState != i) {
                int unused = DozeDockHandler.this.mDockState = i;
                if (!isPulsing()) {
                    int access$200 = DozeDockHandler.this.mDockState;
                    if (access$200 != 0) {
                        if (access$200 == 1) {
                            state = DozeMachine.State.DOZE_AOD_DOCKED;
                        } else if (access$200 == 2) {
                            state = DozeMachine.State.DOZE;
                        } else {
                            return;
                        }
                    } else if (DozeDockHandler.this.mConfig.alwaysOnEnabled(-2)) {
                        state = DozeMachine.State.DOZE_AOD;
                    } else {
                        state = DozeMachine.State.DOZE;
                    }
                    DozeDockHandler.this.mMachine.requestState(state);
                }
            }
        }

        private boolean isPulsing() {
            DozeMachine.State state = DozeDockHandler.this.mMachine.getState();
            return state == DozeMachine.State.DOZE_REQUEST_PULSE || state == DozeMachine.State.DOZE_PULSING || state == DozeMachine.State.DOZE_PULSING_BRIGHT;
        }

        /* access modifiers changed from: package-private */
        public void register() {
            if (!this.mRegistered) {
                if (DozeDockHandler.this.mDockManager != null) {
                    DozeDockHandler.this.mDockManager.addListener(this);
                }
                this.mRegistered = true;
            }
        }

        /* access modifiers changed from: package-private */
        public void unregister() {
            if (this.mRegistered) {
                if (DozeDockHandler.this.mDockManager != null) {
                    DozeDockHandler.this.mDockManager.removeListener(this);
                }
                this.mRegistered = false;
            }
        }
    }
}
