package com.android.wm.shell.pip;

import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.pm.ParceledListSlice;
import android.os.RemoteException;
import android.view.IPinnedTaskListener;
import android.view.WindowManagerGlobal;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class PinnedStackListenerForwarder {
    private final IPinnedTaskListener mListenerImpl = new PinnedTaskListenerImpl();
    private final ArrayList<PinnedTaskListener> mListeners = new ArrayList<>();
    private final ShellExecutor mMainExecutor;

    /* loaded from: classes2.dex */
    public static class PinnedTaskListener {
        public void onActionsChanged(ParceledListSlice<RemoteAction> parceledListSlice) {
        }

        public void onActivityHidden(ComponentName componentName) {
        }

        public void onAspectRatioChanged(float f) {
        }

        public void onImeVisibilityChanged(boolean z, int i) {
        }

        public void onMovementBoundsChanged(boolean z) {
        }
    }

    public PinnedStackListenerForwarder(ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
    }

    public void addListener(PinnedTaskListener pinnedTaskListener) {
        this.mListeners.add(pinnedTaskListener);
    }

    public void register(int i) throws RemoteException {
        WindowManagerGlobal.getWindowManagerService().registerPinnedTaskListener(i, this.mListenerImpl);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onMovementBoundsChanged(boolean z) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onMovementBoundsChanged(z);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onImeVisibilityChanged(boolean z, int i) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onImeVisibilityChanged(z, i);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onActionsChanged(ParceledListSlice<RemoteAction> parceledListSlice) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onActionsChanged(parceledListSlice);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onActivityHidden(ComponentName componentName) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onActivityHidden(componentName);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onAspectRatioChanged(float f) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onAspectRatioChanged(f);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class PinnedTaskListenerImpl extends IPinnedTaskListener.Stub {
        private PinnedTaskListenerImpl() {
        }

        public void onMovementBoundsChanged(final boolean z) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    PinnedStackListenerForwarder.PinnedTaskListenerImpl.this.lambda$onMovementBoundsChanged$0(z);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMovementBoundsChanged$0(boolean z) {
            PinnedStackListenerForwarder.this.onMovementBoundsChanged(z);
        }

        public void onImeVisibilityChanged(final boolean z, final int i) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    PinnedStackListenerForwarder.PinnedTaskListenerImpl.this.lambda$onImeVisibilityChanged$1(z, i);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onImeVisibilityChanged$1(boolean z, int i) {
            PinnedStackListenerForwarder.this.onImeVisibilityChanged(z, i);
        }

        public void onActionsChanged(final ParceledListSlice<RemoteAction> parceledListSlice) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    PinnedStackListenerForwarder.PinnedTaskListenerImpl.this.lambda$onActionsChanged$2(parceledListSlice);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onActionsChanged$2(ParceledListSlice parceledListSlice) {
            PinnedStackListenerForwarder.this.onActionsChanged(parceledListSlice);
        }

        public void onActivityHidden(final ComponentName componentName) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    PinnedStackListenerForwarder.PinnedTaskListenerImpl.this.lambda$onActivityHidden$3(componentName);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onActivityHidden$3(ComponentName componentName) {
            PinnedStackListenerForwarder.this.onActivityHidden(componentName);
        }

        public void onAspectRatioChanged(final float f) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.PinnedStackListenerForwarder$PinnedTaskListenerImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    PinnedStackListenerForwarder.PinnedTaskListenerImpl.this.lambda$onAspectRatioChanged$4(f);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onAspectRatioChanged$4(float f) {
            PinnedStackListenerForwarder.this.onAspectRatioChanged(f);
        }
    }
}
