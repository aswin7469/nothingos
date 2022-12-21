package com.android.p019wm.shell.pip;

import android.content.ComponentName;
import android.os.RemoteException;
import android.view.IPinnedTaskListener;
import android.view.WindowManagerGlobal;
import com.android.p019wm.shell.common.ShellExecutor;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: com.android.wm.shell.pip.PinnedStackListenerForwarder */
public class PinnedStackListenerForwarder {
    private final IPinnedTaskListener mListenerImpl = new PinnedTaskListenerImpl();
    private final ArrayList<PinnedTaskListener> mListeners = new ArrayList<>();
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;

    /* renamed from: com.android.wm.shell.pip.PinnedStackListenerForwarder$PinnedTaskListener */
    public static class PinnedTaskListener {
        public void onActivityHidden(ComponentName componentName) {
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

    public void removeListener(PinnedTaskListener pinnedTaskListener) {
        this.mListeners.remove((Object) pinnedTaskListener);
    }

    public void register(int i) throws RemoteException {
        WindowManagerGlobal.getWindowManagerService().registerPinnedTaskListener(i, this.mListenerImpl);
    }

    /* access modifiers changed from: private */
    public void onMovementBoundsChanged(boolean z) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onMovementBoundsChanged(z);
        }
    }

    /* access modifiers changed from: private */
    public void onImeVisibilityChanged(boolean z, int i) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onImeVisibilityChanged(z, i);
        }
    }

    /* access modifiers changed from: private */
    public void onActivityHidden(ComponentName componentName) {
        Iterator<PinnedTaskListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onActivityHidden(componentName);
        }
    }

    /* renamed from: com.android.wm.shell.pip.PinnedStackListenerForwarder$PinnedTaskListenerImpl */
    private class PinnedTaskListenerImpl extends IPinnedTaskListener.Stub {
        private PinnedTaskListenerImpl() {
        }

        public void onMovementBoundsChanged(boolean z) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new C3504x2eacda58(this, z));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onMovementBoundsChanged$0$com-android-wm-shell-pip-PinnedStackListenerForwarder$PinnedTaskListenerImpl */
        public /* synthetic */ void mo49985xbe9c536b(boolean z) {
            PinnedStackListenerForwarder.this.onMovementBoundsChanged(z);
        }

        public void onImeVisibilityChanged(boolean z, int i) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new C3502x2eacda56(this, z, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onImeVisibilityChanged$1$com-android-wm-shell-pip-PinnedStackListenerForwarder$PinnedTaskListenerImpl */
        public /* synthetic */ void mo49984x9304c9c1(boolean z, int i) {
            PinnedStackListenerForwarder.this.onImeVisibilityChanged(z, i);
        }

        public void onActivityHidden(ComponentName componentName) {
            PinnedStackListenerForwarder.this.mMainExecutor.execute(new C3503x2eacda57(this, componentName));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onActivityHidden$2$com-android-wm-shell-pip-PinnedStackListenerForwarder$PinnedTaskListenerImpl */
        public /* synthetic */ void mo49983x83040748(ComponentName componentName) {
            PinnedStackListenerForwarder.this.onActivityHidden(componentName);
        }
    }
}
