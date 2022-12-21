package com.android.p019wm.shell.common;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Region;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.MergedConfiguration;
import android.util.Slog;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.IScrollCaptureResponseListener;
import android.view.IWindow;
import android.view.IWindowManager;
import android.view.IWindowSession;
import android.view.IWindowSessionCallback;
import android.view.InsetsSourceControl;
import android.view.InsetsState;
import android.view.ScrollCaptureResponse;
import android.view.SurfaceControl;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceSession;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowlessWindowManager;
import android.window.ClientWindowFrames;
import com.android.internal.os.IResultReceiver;
import com.android.p019wm.shell.common.DisplayController;
import java.util.HashMap;

/* renamed from: com.android.wm.shell.common.SystemWindows */
public class SystemWindows {
    private static final String TAG = "SystemWindows";
    /* access modifiers changed from: private */
    public final DisplayController mDisplayController;
    private final DisplayController.OnDisplaysChangedListener mDisplayListener;
    /* access modifiers changed from: private */
    public final SparseArray<PerDisplay> mPerDisplay = new SparseArray<>();
    private IWindowSession mSession;
    /* access modifiers changed from: private */
    public final HashMap<View, SurfaceControlViewHost> mViewRoots = new HashMap<>();
    /* access modifiers changed from: private */
    public final IWindowManager mWmService;

    public SystemWindows(DisplayController displayController, IWindowManager iWindowManager) {
        C34381 r0 = new DisplayController.OnDisplaysChangedListener() {
            public void onDisplayAdded(int i) {
            }

            public void onDisplayRemoved(int i) {
            }

            public void onDisplayConfigurationChanged(int i, Configuration configuration) {
                PerDisplay perDisplay = (PerDisplay) SystemWindows.this.mPerDisplay.get(i);
                if (perDisplay != null) {
                    perDisplay.updateConfiguration(configuration);
                }
            }
        };
        this.mDisplayListener = r0;
        this.mWmService = iWindowManager;
        this.mDisplayController = displayController;
        displayController.addDisplayWindowListener(r0);
        try {
            this.mSession = iWindowManager.openSession(new IWindowSessionCallback.Stub() {
                public void onAnimatorScaleChanged(float f) {
                }
            });
        } catch (RemoteException e) {
            Slog.e(TAG, "Unable to create layer", e);
        }
    }

    public void addView(View view, WindowManager.LayoutParams layoutParams, int i, int i2) {
        PerDisplay perDisplay = this.mPerDisplay.get(i);
        if (perDisplay == null) {
            perDisplay = new PerDisplay(i);
            this.mPerDisplay.put(i, perDisplay);
        }
        perDisplay.addView(view, layoutParams, i2);
    }

    public void removeView(View view) {
        this.mViewRoots.remove(view).release();
    }

    public void updateViewLayout(View view, ViewGroup.LayoutParams layoutParams) {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewRoots.get(view);
        if (surfaceControlViewHost != null && (layoutParams instanceof WindowManager.LayoutParams)) {
            view.setLayoutParams(layoutParams);
            surfaceControlViewHost.relayout((WindowManager.LayoutParams) layoutParams);
        }
    }

    public void setShellRootAccessibilityWindow(int i, int i2, View view) {
        PerDisplay perDisplay = this.mPerDisplay.get(i);
        if (perDisplay != null) {
            perDisplay.setShellRootAccessibilityWindow(i2, view);
        }
    }

    public void setTouchableRegion(View view, Region region) {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewRoots.get(view);
        if (surfaceControlViewHost != null) {
            SysUiWindowManager windowlessWM = surfaceControlViewHost.getWindowlessWM();
            if (windowlessWM instanceof SysUiWindowManager) {
                windowlessWM.setTouchableRegionForWindow(view, region);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public IWindow getWindow(int i, int i2) {
        PerDisplay perDisplay = this.mPerDisplay.get(i);
        if (perDisplay == null) {
            return null;
        }
        return perDisplay.getWindow(i2);
    }

    public SurfaceControl getViewSurface(View view) {
        for (int i = 0; i < this.mPerDisplay.size(); i++) {
            for (int i2 = 0; i2 < this.mPerDisplay.valueAt(i).mWwms.size(); i2++) {
                SurfaceControl surfaceControlForWindow = ((SysUiWindowManager) this.mPerDisplay.valueAt(i).mWwms.valueAt(i2)).getSurfaceControlForWindow(view);
                if (surfaceControlForWindow != null) {
                    return surfaceControlForWindow;
                }
            }
        }
        return null;
    }

    public IBinder getFocusGrantToken(View view) {
        SurfaceControlViewHost surfaceControlViewHost = this.mViewRoots.get(view);
        if (surfaceControlViewHost != null) {
            return surfaceControlViewHost.getFocusGrantToken();
        }
        Slog.e(TAG, "Couldn't get focus grant token since view does not exist in SystemWindow:" + view);
        return null;
    }

    /* renamed from: com.android.wm.shell.common.SystemWindows$PerDisplay */
    private class PerDisplay {
        final int mDisplayId;
        /* access modifiers changed from: private */
        public final SparseArray<SysUiWindowManager> mWwms = new SparseArray<>();

        PerDisplay(int i) {
            this.mDisplayId = i;
        }

        public void addView(View view, WindowManager.LayoutParams layoutParams, int i) {
            SysUiWindowManager addRoot = addRoot(i);
            if (addRoot == null) {
                Slog.e(SystemWindows.TAG, "Unable to create systemui root");
                return;
            }
            SurfaceControlViewHost surfaceControlViewHost = new SurfaceControlViewHost(view.getContext(), SystemWindows.this.mDisplayController.getDisplay(this.mDisplayId), addRoot, true);
            layoutParams.flags |= 16777216;
            surfaceControlViewHost.setView(view, layoutParams);
            SystemWindows.this.mViewRoots.put(view, surfaceControlViewHost);
            setShellRootAccessibilityWindow(i, view);
        }

        /* access modifiers changed from: package-private */
        public SysUiWindowManager addRoot(int i) {
            SurfaceControl surfaceControl;
            SysUiWindowManager sysUiWindowManager = this.mWwms.get(i);
            if (sysUiWindowManager != null) {
                return sysUiWindowManager;
            }
            ContainerWindow containerWindow = new ContainerWindow();
            try {
                surfaceControl = SystemWindows.this.mWmService.addShellRoot(this.mDisplayId, containerWindow, i);
            } catch (RemoteException unused) {
                surfaceControl = null;
            }
            if (surfaceControl == null) {
                Slog.e(SystemWindows.TAG, "Unable to get root surfacecontrol for systemui");
                return null;
            }
            SysUiWindowManager sysUiWindowManager2 = new SysUiWindowManager(this.mDisplayId, SystemWindows.this.mDisplayController.getDisplayContext(this.mDisplayId), surfaceControl, containerWindow);
            this.mWwms.put(i, sysUiWindowManager2);
            return sysUiWindowManager2;
        }

        /* access modifiers changed from: package-private */
        public IWindow getWindow(int i) {
            SysUiWindowManager sysUiWindowManager = this.mWwms.get(i);
            if (sysUiWindowManager == null) {
                return null;
            }
            return sysUiWindowManager.mContainerWindow;
        }

        /* access modifiers changed from: package-private */
        public void setShellRootAccessibilityWindow(int i, View view) {
            if (this.mWwms.get(i) != null) {
                try {
                    SystemWindows.this.mWmService.setShellRootAccessibilityWindow(this.mDisplayId, i, view != null ? ((SurfaceControlViewHost) SystemWindows.this.mViewRoots.get(view)).getWindowToken() : null);
                } catch (RemoteException e) {
                    Slog.e(SystemWindows.TAG, "Error setting accessibility window for " + this.mDisplayId + ":" + i, e);
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void updateConfiguration(Configuration configuration) {
            for (int i = 0; i < this.mWwms.size(); i++) {
                this.mWwms.valueAt(i).updateConfiguration(configuration);
            }
        }
    }

    /* renamed from: com.android.wm.shell.common.SystemWindows$SysUiWindowManager */
    public class SysUiWindowManager extends WindowlessWindowManager {
        ContainerWindow mContainerWindow;
        final int mDisplayId;
        final HashMap<IBinder, SurfaceControl> mLeashForWindow = new HashMap<>();

        public SysUiWindowManager(int i, Context context, SurfaceControl surfaceControl, ContainerWindow containerWindow) {
            super(context.getResources().getConfiguration(), surfaceControl, (IBinder) null);
            this.mContainerWindow = containerWindow;
            this.mDisplayId = i;
        }

        /* access modifiers changed from: package-private */
        public void updateConfiguration(Configuration configuration) {
            setConfiguration(configuration);
        }

        /* access modifiers changed from: package-private */
        public SurfaceControl getSurfaceControlForWindow(View view) {
            SurfaceControl surfaceControl;
            synchronized (this) {
                surfaceControl = this.mLeashForWindow.get(getWindowBinder(view));
            }
            return surfaceControl;
        }

        /* access modifiers changed from: protected */
        public void attachToParentSurface(IWindow iWindow, SurfaceControl.Builder builder) {
            SurfaceControl build = new SurfaceControl.Builder(new SurfaceSession()).setContainerLayer().setName("SystemWindowLeash").setHidden(false).setParent(this.mRootSurface).setCallsite("SysUiWIndowManager#attachToParentSurface").build();
            synchronized (this) {
                this.mLeashForWindow.put(iWindow.asBinder(), build);
            }
            builder.setParent(build);
        }

        public void remove(IWindow iWindow) throws RemoteException {
            SystemWindows.super.remove(iWindow);
            synchronized (this) {
                IBinder asBinder = iWindow.asBinder();
                new SurfaceControl.Transaction().remove(this.mLeashForWindow.get(asBinder)).apply();
                this.mLeashForWindow.remove(asBinder);
            }
        }

        /* access modifiers changed from: package-private */
        public void setTouchableRegionForWindow(View view, Region region) {
            IBinder windowToken = view.getWindowToken();
            if (windowToken != null) {
                setTouchRegion(windowToken, region);
            }
        }
    }

    /* renamed from: com.android.wm.shell.common.SystemWindows$ContainerWindow */
    static class ContainerWindow extends IWindow.Stub {
        public void closeSystemDialogs(String str) {
        }

        public void dispatchAppVisibility(boolean z) {
        }

        public void dispatchDragEvent(DragEvent dragEvent) {
        }

        public void dispatchGetNewSurface() {
        }

        public void dispatchWallpaperCommand(String str, int i, int i2, int i3, Bundle bundle, boolean z) {
        }

        public void dispatchWallpaperOffsets(float f, float f2, float f3, float f4, float f5, boolean z) {
        }

        public void dispatchWindowShown() {
        }

        public void executeCommand(String str, String str2, ParcelFileDescriptor parcelFileDescriptor) {
        }

        public void hideInsets(int i, boolean z) {
        }

        public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] insetsSourceControlArr) {
        }

        public void moved(int i, int i2) {
        }

        public void requestAppKeyboardShortcuts(IResultReceiver iResultReceiver, int i) {
        }

        public void resized(ClientWindowFrames clientWindowFrames, boolean z, MergedConfiguration mergedConfiguration, InsetsState insetsState, boolean z2, boolean z3, int i, int i2, int i3) {
        }

        public void showInsets(int i, boolean z) {
        }

        public void updatePointerIcon(float f, float f2) {
        }

        ContainerWindow() {
        }

        public void requestScrollCapture(IScrollCaptureResponseListener iScrollCaptureResponseListener) {
            try {
                iScrollCaptureResponseListener.onScrollCaptureResponse(new ScrollCaptureResponse.Builder().setDescription("Not Implemented").build());
            } catch (RemoteException unused) {
            }
        }
    }
}
