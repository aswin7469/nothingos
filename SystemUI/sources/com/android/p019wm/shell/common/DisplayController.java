package com.android.p019wm.shell.common;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.os.RemoteException;
import android.util.ArraySet;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Display;
import android.view.IDisplayWindowListener;
import android.view.IWindowManager;
import android.view.InsetsState;
import com.android.p019wm.shell.common.DisplayChangeController;
import com.android.p019wm.shell.common.annotations.ShellMainThread;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/* renamed from: com.android.wm.shell.common.DisplayController */
public class DisplayController {
    private static final String TAG = "DisplayController";
    private final DisplayChangeController mChangeController;
    private final Context mContext;
    private final ArrayList<OnDisplaysChangedListener> mDisplayChangedListeners = new ArrayList<>();
    private final IDisplayWindowListener mDisplayContainerListener;
    private final SparseArray<DisplayRecord> mDisplays = new SparseArray<>();
    /* access modifiers changed from: private */
    public final ShellExecutor mMainExecutor;
    private final IWindowManager mWmService;

    @ShellMainThread
    /* renamed from: com.android.wm.shell.common.DisplayController$OnDisplaysChangedListener */
    public interface OnDisplaysChangedListener {
        void onDisplayAdded(int i) {
        }

        void onDisplayConfigurationChanged(int i, Configuration configuration) {
        }

        void onDisplayRemoved(int i) {
        }

        void onFixedRotationFinished(int i) {
        }

        void onFixedRotationStarted(int i, int i2) {
        }

        void onKeepClearAreasChanged(int i, Set<Rect> set, Set<Rect> set2) {
        }
    }

    public DisplayController(Context context, IWindowManager iWindowManager, ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
        this.mContext = context;
        this.mWmService = iWindowManager;
        this.mChangeController = new DisplayChangeController(iWindowManager, shellExecutor);
        this.mDisplayContainerListener = new DisplayWindowListenerImpl();
    }

    public void initialize() {
        try {
            int[] registerDisplayWindowListener = this.mWmService.registerDisplayWindowListener(this.mDisplayContainerListener);
            for (int onDisplayAdded : registerDisplayWindowListener) {
                onDisplayAdded(onDisplayAdded);
            }
        } catch (RemoteException unused) {
            throw new RuntimeException("Unable to register display controller");
        }
    }

    public DisplayChangeController getChangeController() {
        return this.mChangeController;
    }

    public Display getDisplay(int i) {
        return ((DisplayManager) this.mContext.getSystemService(DisplayManager.class)).getDisplay(i);
    }

    public DisplayLayout getDisplayLayout(int i) {
        DisplayRecord displayRecord = this.mDisplays.get(i);
        if (displayRecord != null) {
            return displayRecord.mDisplayLayout;
        }
        return null;
    }

    public Context getDisplayContext(int i) {
        DisplayRecord displayRecord = this.mDisplays.get(i);
        if (displayRecord != null) {
            return displayRecord.mContext;
        }
        return null;
    }

    public InsetsState getInsetsState(int i) {
        DisplayRecord displayRecord = this.mDisplays.get(i);
        if (displayRecord != null) {
            return displayRecord.mInsetsState;
        }
        return null;
    }

    public void updateDisplayInsets(int i, InsetsState insetsState) {
        DisplayRecord displayRecord = this.mDisplays.get(i);
        if (displayRecord != null) {
            displayRecord.setInsets(insetsState);
        }
    }

    public void addDisplayWindowListener(OnDisplaysChangedListener onDisplaysChangedListener) {
        synchronized (this.mDisplays) {
            if (!this.mDisplayChangedListeners.contains(onDisplaysChangedListener)) {
                this.mDisplayChangedListeners.add(onDisplaysChangedListener);
                for (int i = 0; i < this.mDisplays.size(); i++) {
                    onDisplaysChangedListener.onDisplayAdded(this.mDisplays.keyAt(i));
                }
            }
        }
    }

    public void removeDisplayWindowListener(OnDisplaysChangedListener onDisplaysChangedListener) {
        synchronized (this.mDisplays) {
            this.mDisplayChangedListeners.remove((Object) onDisplaysChangedListener);
        }
    }

    public void addDisplayChangingController(DisplayChangeController.OnDisplayChangingListener onDisplayChangingListener) {
        this.mChangeController.addRotationListener(onDisplayChangingListener);
    }

    public void removeDisplayChangingController(DisplayChangeController.OnDisplayChangingListener onDisplayChangingListener) {
        this.mChangeController.removeRotationListener(onDisplayChangingListener);
    }

    /* access modifiers changed from: private */
    public void onDisplayAdded(int i) {
        Context context;
        synchronized (this.mDisplays) {
            if (this.mDisplays.get(i) == null) {
                Display display = getDisplay(i);
                if (display != null) {
                    if (i == 0) {
                        context = this.mContext;
                    } else {
                        context = this.mContext.createDisplayContext(display);
                    }
                    DisplayRecord displayRecord = new DisplayRecord(i);
                    displayRecord.setDisplayLayout(context, new DisplayLayout(context, display));
                    this.mDisplays.put(i, displayRecord);
                    for (int i2 = 0; i2 < this.mDisplayChangedListeners.size(); i2++) {
                        this.mDisplayChangedListeners.get(i2).onDisplayAdded(i);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onDisplayConfigurationChanged(int i, Configuration configuration) {
        Context context;
        synchronized (this.mDisplays) {
            DisplayRecord displayRecord = this.mDisplays.get(i);
            if (displayRecord == null) {
                Slog.w(TAG, "Skipping Display Configuration change on non-added display.");
                return;
            }
            Display display = getDisplay(i);
            if (display == null) {
                Slog.w(TAG, "Skipping Display Configuration change on invalid display. It may have been removed.");
                return;
            }
            if (i == 0) {
                context = this.mContext;
            } else {
                context = this.mContext.createDisplayContext(display);
            }
            Context createConfigurationContext = context.createConfigurationContext(configuration);
            displayRecord.setDisplayLayout(createConfigurationContext, new DisplayLayout(createConfigurationContext, display));
            for (int i2 = 0; i2 < this.mDisplayChangedListeners.size(); i2++) {
                this.mDisplayChangedListeners.get(i2).onDisplayConfigurationChanged(i, configuration);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onDisplayRemoved(int i) {
        synchronized (this.mDisplays) {
            if (this.mDisplays.get(i) != null) {
                for (int size = this.mDisplayChangedListeners.size() - 1; size >= 0; size--) {
                    this.mDisplayChangedListeners.get(size).onDisplayRemoved(i);
                }
                this.mDisplays.remove(i);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onFixedRotationStarted(int i, int i2) {
        synchronized (this.mDisplays) {
            if (this.mDisplays.get(i) != null) {
                if (getDisplay(i) != null) {
                    for (int size = this.mDisplayChangedListeners.size() - 1; size >= 0; size--) {
                        this.mDisplayChangedListeners.get(size).onFixedRotationStarted(i, i2);
                    }
                    return;
                }
            }
            Slog.w(TAG, "Skipping onFixedRotationStarted on unknown display, displayId=" + i);
        }
    }

    /* access modifiers changed from: private */
    public void onFixedRotationFinished(int i) {
        synchronized (this.mDisplays) {
            if (this.mDisplays.get(i) != null) {
                if (getDisplay(i) != null) {
                    for (int size = this.mDisplayChangedListeners.size() - 1; size >= 0; size--) {
                        this.mDisplayChangedListeners.get(size).onFixedRotationFinished(i);
                    }
                    return;
                }
            }
            Slog.w(TAG, "Skipping onFixedRotationFinished on unknown display, displayId=" + i);
        }
    }

    /* access modifiers changed from: private */
    public void onKeepClearAreasChanged(int i, Set<Rect> set, Set<Rect> set2) {
        synchronized (this.mDisplays) {
            if (this.mDisplays.get(i) != null) {
                if (getDisplay(i) != null) {
                    for (int size = this.mDisplayChangedListeners.size() - 1; size >= 0; size--) {
                        this.mDisplayChangedListeners.get(size).onKeepClearAreasChanged(i, set, set2);
                    }
                    return;
                }
            }
            Slog.w(TAG, "Skipping onKeepClearAreasChanged on unknown display, displayId=" + i);
        }
    }

    /* renamed from: com.android.wm.shell.common.DisplayController$DisplayRecord */
    private static class DisplayRecord {
        /* access modifiers changed from: private */
        public Context mContext;
        private int mDisplayId;
        /* access modifiers changed from: private */
        public DisplayLayout mDisplayLayout;
        /* access modifiers changed from: private */
        public InsetsState mInsetsState;

        private DisplayRecord(int i) {
            this.mInsetsState = new InsetsState();
            this.mDisplayId = i;
        }

        /* access modifiers changed from: private */
        public void setDisplayLayout(Context context, DisplayLayout displayLayout) {
            this.mContext = context;
            this.mDisplayLayout = displayLayout;
            displayLayout.setInsets(context.getResources(), this.mInsetsState);
        }

        /* access modifiers changed from: private */
        public void setInsets(InsetsState insetsState) {
            this.mInsetsState = insetsState;
            this.mDisplayLayout.setInsets(this.mContext.getResources(), insetsState);
        }
    }

    /* renamed from: com.android.wm.shell.common.DisplayController$DisplayWindowListenerImpl */
    private class DisplayWindowListenerImpl extends IDisplayWindowListener.Stub {
        private DisplayWindowListenerImpl() {
        }

        public void onDisplayAdded(int i) {
            DisplayController.this.mMainExecutor.execute(new C3421xd1ef1ed1(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onDisplayAdded$0$com-android-wm-shell-common-DisplayController$DisplayWindowListenerImpl */
        public /* synthetic */ void mo49024xe415290a(int i) {
            DisplayController.this.onDisplayAdded(i);
        }

        public void onDisplayConfigurationChanged(int i, Configuration configuration) {
            DisplayController.this.mMainExecutor.execute(new C3424xd1ef1ed4(this, i, configuration));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onDisplayConfigurationChanged$1$com-android-wm-shell-common-DisplayController$DisplayWindowListenerImpl */
        public /* synthetic */ void mo49025xae15a375(int i, Configuration configuration) {
            DisplayController.this.onDisplayConfigurationChanged(i, configuration);
        }

        public void onDisplayRemoved(int i) {
            DisplayController.this.mMainExecutor.execute(new C3422xd1ef1ed2(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onDisplayRemoved$2$com-android-wm-shell-common-DisplayController$DisplayWindowListenerImpl */
        public /* synthetic */ void mo49026x2a767ec(int i) {
            DisplayController.this.onDisplayRemoved(i);
        }

        public void onFixedRotationStarted(int i, int i2) {
            DisplayController.this.mMainExecutor.execute(new C3419xd1ef1ecf(this, i, i2));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onFixedRotationStarted$3$com-android-wm-shell-common-DisplayController$DisplayWindowListenerImpl */
        public /* synthetic */ void mo49028xb203687e(int i, int i2) {
            DisplayController.this.onFixedRotationStarted(i, i2);
        }

        public void onFixedRotationFinished(int i) {
            DisplayController.this.mMainExecutor.execute(new C3423xd1ef1ed3(this, i));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onFixedRotationFinished$4$com-android-wm-shell-common-DisplayController$DisplayWindowListenerImpl */
        public /* synthetic */ void mo49027xbefafb5c(int i) {
            DisplayController.this.onFixedRotationFinished(i);
        }

        public void onKeepClearAreasChanged(int i, List<Rect> list, List<Rect> list2) {
            DisplayController.this.mMainExecutor.execute(new C3420xd1ef1ed0(this, i, list, list2));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onKeepClearAreasChanged$5$com-android-wm-shell-common-DisplayController$DisplayWindowListenerImpl */
        public /* synthetic */ void mo49029x4d802e6f(int i, List list, List list2) {
            DisplayController.this.onKeepClearAreasChanged(i, new ArraySet(list), new ArraySet(list2));
        }
    }
}
