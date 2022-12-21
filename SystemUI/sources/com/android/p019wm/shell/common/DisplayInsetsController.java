package com.android.p019wm.shell.common;

import android.os.RemoteException;
import android.util.Slog;
import android.util.SparseArray;
import android.view.IDisplayWindowInsetsController;
import android.view.IWindowManager;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.InsetsVisibilities;
import com.android.p019wm.shell.common.DisplayController;
import com.android.p019wm.shell.common.annotations.ShellMainThread;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* renamed from: com.android.wm.shell.common.DisplayInsetsController */
public class DisplayInsetsController implements DisplayController.OnDisplaysChangedListener {
    private static final String TAG = "DisplayInsetsController";
    /* access modifiers changed from: private */
    public final DisplayController mDisplayController;
    private final SparseArray<PerDisplay> mInsetsPerDisplay = new SparseArray<>();
    /* access modifiers changed from: private */
    public final SparseArray<CopyOnWriteArrayList<OnInsetsChangedListener>> mListeners = new SparseArray<>();
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    /* access modifiers changed from: private */
    public final IWindowManager mWmService;

    @ShellMainThread
    /* renamed from: com.android.wm.shell.common.DisplayInsetsController$OnInsetsChangedListener */
    public interface OnInsetsChangedListener {
        void hideInsets(int i, boolean z) {
        }

        void insetsChanged(InsetsState insetsState) {
        }

        void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
        }

        void showInsets(int i, boolean z) {
        }

        void topFocusedWindowChanged(String str, InsetsVisibilities insetsVisibilities) {
        }
    }

    public DisplayInsetsController(IWindowManager iWindowManager, DisplayController displayController, ShellExecutor shellExecutor) {
        this.mWmService = iWindowManager;
        this.mDisplayController = displayController;
        this.mMainExecutor = shellExecutor;
    }

    public void initialize() {
        this.mDisplayController.addDisplayWindowListener(this);
    }

    public void addInsetsChangedListener(int i, OnInsetsChangedListener onInsetsChangedListener) {
        CopyOnWriteArrayList copyOnWriteArrayList = this.mListeners.get(i);
        if (copyOnWriteArrayList == null) {
            copyOnWriteArrayList = new CopyOnWriteArrayList();
            this.mListeners.put(i, copyOnWriteArrayList);
        }
        if (!copyOnWriteArrayList.contains(onInsetsChangedListener)) {
            copyOnWriteArrayList.add(onInsetsChangedListener);
        }
    }

    public void removeInsetsChangedListener(int i, OnInsetsChangedListener onInsetsChangedListener) {
        CopyOnWriteArrayList copyOnWriteArrayList = this.mListeners.get(i);
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.remove((Object) onInsetsChangedListener);
        }
    }

    public void onDisplayAdded(int i) {
        PerDisplay perDisplay = new PerDisplay(i);
        perDisplay.register();
        this.mInsetsPerDisplay.put(i, perDisplay);
    }

    public void onDisplayRemoved(int i) {
        PerDisplay perDisplay = this.mInsetsPerDisplay.get(i);
        if (perDisplay != null) {
            perDisplay.unregister();
            this.mInsetsPerDisplay.remove(i);
        }
    }

    /* renamed from: com.android.wm.shell.common.DisplayInsetsController$PerDisplay */
    public class PerDisplay {
        private final int mDisplayId;
        private final DisplayWindowInsetsControllerImpl mInsetsControllerImpl = new DisplayWindowInsetsControllerImpl();

        public PerDisplay(int i) {
            this.mDisplayId = i;
        }

        public void register() {
            try {
                DisplayInsetsController.this.mWmService.setDisplayWindowInsetsController(this.mDisplayId, this.mInsetsControllerImpl);
            } catch (RemoteException unused) {
                Slog.w(DisplayInsetsController.TAG, "Unable to set insets controller on display " + this.mDisplayId);
            }
        }

        public void unregister() {
            try {
                DisplayInsetsController.this.mWmService.setDisplayWindowInsetsController(this.mDisplayId, (IDisplayWindowInsetsController) null);
            } catch (RemoteException unused) {
                Slog.w(DisplayInsetsController.TAG, "Unable to remove insets controller on display " + this.mDisplayId);
            }
        }

        /* access modifiers changed from: private */
        public void insetsChanged(InsetsState insetsState) {
            CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) DisplayInsetsController.this.mListeners.get(this.mDisplayId);
            if (copyOnWriteArrayList != null) {
                DisplayInsetsController.this.mDisplayController.updateDisplayInsets(this.mDisplayId, insetsState);
                Iterator it = copyOnWriteArrayList.iterator();
                while (it.hasNext()) {
                    ((OnInsetsChangedListener) it.next()).insetsChanged(insetsState);
                }
            }
        }

        /* access modifiers changed from: private */
        public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
            CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) DisplayInsetsController.this.mListeners.get(this.mDisplayId);
            if (copyOnWriteArrayList != null) {
                Iterator it = copyOnWriteArrayList.iterator();
                while (it.hasNext()) {
                    ((OnInsetsChangedListener) it.next()).insetsControlChanged(insetsState, insetsSourceControlArr);
                }
            }
        }

        /* access modifiers changed from: private */
        public void showInsets(int i, boolean z) {
            CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) DisplayInsetsController.this.mListeners.get(this.mDisplayId);
            if (copyOnWriteArrayList != null) {
                Iterator it = copyOnWriteArrayList.iterator();
                while (it.hasNext()) {
                    ((OnInsetsChangedListener) it.next()).showInsets(i, z);
                }
            }
        }

        /* access modifiers changed from: private */
        public void hideInsets(int i, boolean z) {
            CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) DisplayInsetsController.this.mListeners.get(this.mDisplayId);
            if (copyOnWriteArrayList != null) {
                Iterator it = copyOnWriteArrayList.iterator();
                while (it.hasNext()) {
                    ((OnInsetsChangedListener) it.next()).hideInsets(i, z);
                }
            }
        }

        /* access modifiers changed from: private */
        public void topFocusedWindowChanged(String str, InsetsVisibilities insetsVisibilities) {
            CopyOnWriteArrayList copyOnWriteArrayList = (CopyOnWriteArrayList) DisplayInsetsController.this.mListeners.get(this.mDisplayId);
            if (copyOnWriteArrayList != null) {
                Iterator it = copyOnWriteArrayList.iterator();
                while (it.hasNext()) {
                    ((OnInsetsChangedListener) it.next()).topFocusedWindowChanged(str, insetsVisibilities);
                }
            }
        }

        /* renamed from: com.android.wm.shell.common.DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl */
        private class DisplayWindowInsetsControllerImpl extends IDisplayWindowInsetsController.Stub {
            private DisplayWindowInsetsControllerImpl() {
            }

            public void topFocusedWindowChanged(String str, InsetsVisibilities insetsVisibilities) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new C3429x9b4a9e26(this, str, insetsVisibilities));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$topFocusedWindowChanged$0$com-android-wm-shell-common-DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl */
            public /* synthetic */ void mo49077x2b1167c0(String str, InsetsVisibilities insetsVisibilities) {
                PerDisplay.this.topFocusedWindowChanged(str, insetsVisibilities);
            }

            public void insetsChanged(InsetsState insetsState) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new C3428x9b4a9e25(this, insetsState));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$insetsChanged$1$com-android-wm-shell-common-DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl */
            public /* synthetic */ void mo49074x57bb9b23(InsetsState insetsState) {
                PerDisplay.this.insetsChanged(insetsState);
            }

            public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new C3430x9b4a9e27(this, insetsState, insetsSourceControlArr));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$insetsControlChanged$2$com-android-wm-shell-common-DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl */
            public /* synthetic */ void mo49075xea796379(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
                PerDisplay.this.insetsControlChanged(insetsState, insetsSourceControlArr);
            }

            public void showInsets(int i, boolean z) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new C3427x9b4a9e24(this, i, z));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$showInsets$3$com-android-wm-shell-common-DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl */
            public /* synthetic */ void mo49076x7182b072(int i, boolean z) {
                PerDisplay.this.showInsets(i, z);
            }

            public void hideInsets(int i, boolean z) throws RemoteException {
                DisplayInsetsController.this.mMainExecutor.execute(new C3431x9b4a9e28(this, i, z));
            }

            /* access modifiers changed from: package-private */
            /* renamed from: lambda$hideInsets$4$com-android-wm-shell-common-DisplayInsetsController$PerDisplay$DisplayWindowInsetsControllerImpl */
            public /* synthetic */ void mo49073xdee033ec(int i, boolean z) {
                PerDisplay.this.hideInsets(i, z);
            }
        }
    }
}
