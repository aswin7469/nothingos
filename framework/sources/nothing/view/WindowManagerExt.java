package nothing.view;

import android.content.Context;
import android.content.ContextExt;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nothing.view.IWindowManagerExt;
import nothing.view.IWindowModeCallback;
/* loaded from: classes4.dex */
public class WindowManagerExt {
    public static final int VERSION_CODE = 9001;
    private static IWindowManagerExt mService;
    private static WindowManagerExt sInstance;
    private final SparseArray<WindowModeCallback> mWMCbs = new SparseArray<>();

    /* loaded from: classes4.dex */
    public interface WindowModeListener {
        public static final int WINDOWMODE_STATE_DISMISS_BOTTOM = 32;
        public static final int WINDOWMODE_STATE_DISMISS_TOP = 16;
        public static final int WINDOWMODE_STATE_HIDE_MODE = 2;
        public static final int WINDOWMODE_STATE_MASK = 65535;
        public static final int WINDOWMODE_STATE_MAX_MODE = 8;
        public static final int WINDOWMODE_STATE_MINIMIZE_MODE = 4;
        public static final int WINDOWMODE_STATE_SHOW_MENU = 1;

        void onStackWindowModeChanged(boolean z, Bundle bundle);

        default void onWindowModeChanged(int windowingMode, boolean isWindowMode) {
        }

        default void onPackageNameChanged(int windowingMode, String packageName, int uid, int pid, boolean topStack) {
        }

        default void onWindowModeBoundChanged(int windowingMode, Rect displayBound, int displayRotation) {
        }

        default void onWindowModeStateChanged(int windowingMode, int windowModeState) {
        }

        default void onWindowModeFlingToTarget(int windowingMode, Rect fromBound, Rect toBound) {
        }
    }

    public static WindowManagerExt getInstance(Context context) {
        WindowManagerExt windowManagerExt;
        synchronized (WindowManagerExt.class) {
            if (sInstance == null) {
                sInstance = new WindowManagerExt(context);
            }
            windowManagerExt = sInstance;
        }
        return windowManagerExt;
    }

    private WindowManagerExt(Context context) {
        IBinder b = ServiceManager.getService(ContextExt.WINDOW_EXT_SERVICE);
        mService = IWindowManagerExt.Stub.asInterface(b);
    }

    private WindowModeCallback getOrCreateCallback(int windowingMode, boolean create) {
        WindowModeCallback callback = this.mWMCbs.get(windowingMode);
        if (callback == null && create) {
            WindowModeCallback callback2 = new WindowModeCallback(windowingMode);
            this.mWMCbs.put(windowingMode, callback2);
            return callback2;
        }
        return callback;
    }

    public void registerWindowModeListener(WindowModeListener listener) {
        registerWindowModeListener(11, listener);
    }

    public void registerWindowModeListener(int windowingMode, WindowModeListener listener) {
        try {
            synchronized (this.mWMCbs) {
                WindowModeCallback callback = getOrCreateCallback(windowingMode, true);
                callback.addListener(listener);
                if (callback.size() == 1) {
                    mService.registerWindowModeListener(windowingMode, callback);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unregisterWindowModeListener(WindowModeListener listener) {
        unregisterWindowModeListener(11, listener);
    }

    public void unregisterWindowModeListener(int windowingMode, WindowModeListener listener) {
        try {
            synchronized (this.mWMCbs) {
                WindowModeCallback callback = getOrCreateCallback(windowingMode, false);
                if (callback != null) {
                    callback.removeListener(listener);
                    if (callback.size() == 0) {
                        mService.unregisterWindowModeListener(windowingMode, callback);
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Rect getWindowModeBound() {
        return getWindowModeBound(11);
    }

    public Rect getWindowModeBound(int windowingMode) {
        try {
            return mService.getWindowModeBound(windowingMode);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isWindowMode(IBinder token) {
        return isWindowMode(11, token);
    }

    public boolean isWindowMode(int windowingMode, IBinder token) {
        try {
            return mService.isWindowMode(windowingMode, token);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Point covertWindowModeCoordinate(Point point) {
        return covertWindowModeCoordinate(11, point);
    }

    public Point covertWindowModeCoordinate(int windowingMode, Point point) {
        try {
            return mService.covertWindowModeCoordinate(windowingMode, point);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updateWindowModeShowState(boolean show) {
        updateWindowModeShowState(11, show);
    }

    public void updateWindowModeShowState(int windowingMode, boolean show, boolean nextChange) {
        try {
            mService.updateWindowModeShowState(windowingMode, show, nextChange);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void updateWindowModeShowState(int windowingMode, boolean show) {
        updateWindowModeShowState(windowingMode, show, false);
    }

    public void closeWindowMode(boolean anim) {
        closeWindowMode(11, anim);
    }

    public void closeWindowMode(int windowingMode, boolean anim) {
        try {
            mService.closeWindowMode(windowingMode, anim);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void enterWindowMode(int taskId, int oldWindowingMode, int newWindowingMode, Rect showFrame) {
        enterWindowMode(taskId, oldWindowingMode, newWindowingMode, showFrame, -1.0f);
    }

    public void enterWindowMode(int taskId, int oldWindowingMode, int newWindowingMode, Rect showFrame, float cornerRadius) {
        try {
            mService.enterWindowMode(taskId, oldWindowingMode, newWindowingMode, showFrame, cornerRadius);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void moveWindowModeBound(int windowingMode, Rect showFrame, boolean anim) {
        try {
            mService.moveWindowModeBound(windowingMode, showFrame, null, anim);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void moveWindowModeBound(int windowingMode, Rect showFrame, float[] float9, boolean anim) {
        try {
            mService.moveWindowModeBound(windowingMode, showFrame, float9, anim);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void switchWindowModeToMaxMode(int windowingMode, boolean maxMode) {
        try {
            mService.switchWindowModeToMaxMode(windowingMode, maxMode);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void exitWindowModeToFullscreen(boolean anim) {
        exitWindowModeToFullscreen(11, anim);
    }

    public void exitWindowModeToFullscreen(int windowingMode, boolean anim) {
        try {
            mService.exitWindowModeToFullscreen(windowingMode, anim);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void flingWindowMode(float velocityX, float velocityY, Point startPosition) {
        try {
            mService.flingWindowMode(velocityX, velocityY, startPosition);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setAppMuted(String packageName, int uid, boolean muted) {
        try {
            mService.setAppMuted(packageName, uid, muted);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public boolean isAppMuted(String packageName, int uid) {
        try {
            return mService.isAppMuted(packageName, uid);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void setStartWindowMode(String packageName) {
        try {
            mService.setStartWindowMode(packageName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void addNonHighRefreshRatePackage(String packageName) {
        try {
            mService.addNonHighRefreshRatePackage(packageName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void removeNonHighRefreshRatePackage(String packageName) {
        try {
            mService.removeNonHighRefreshRatePackage(packageName);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void clearNonHighRefreshRatePackage() {
        try {
            mService.clearNonHighRefreshRatePackage();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getNonHighRefreshRatePackages() {
        try {
            Bundle bundle = mService.getNonHighRefreshRatePackages();
            return bundle.getStringArrayList("packages");
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void shutdownDueToTemperature(int temperatureType) {
        try {
            mService.shutdownDueToTemperature(temperatureType);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public static final class WindowModeCallback extends IWindowModeCallback.Stub {
        private final List<WeakReference<WindowModeListener>> mListener = new ArrayList();
        private int mWindowingMode;

        public WindowModeCallback(int windowingMode) {
            this.mWindowingMode = windowingMode;
        }

        public void addListener(WindowModeListener listener) {
            if (listener == null) {
                return;
            }
            boolean hasAdd = false;
            Iterator<WeakReference<WindowModeListener>> it = this.mListener.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                WeakReference<WindowModeListener> wrListener = it.next();
                WindowModeListener l = wrListener.get();
                if (l != null && l.equals(listener)) {
                    hasAdd = true;
                    break;
                }
            }
            if (!hasAdd) {
                this.mListener.add(new WeakReference<>(listener));
            }
        }

        public void removeListener(WindowModeListener listener) {
            if (listener == null) {
                return;
            }
            for (int i = this.mListener.size() - 1; i >= 0; i--) {
                WindowModeListener l = this.mListener.get(i).get();
                if (l == null || l.equals(listener)) {
                    this.mListener.remove(i);
                }
            }
        }

        public int size() {
            return this.mListener.size();
        }

        @Override // nothing.view.IWindowModeCallback
        public void onStackWindowModeChanged(boolean windowMode, Bundle data) {
            for (WeakReference<WindowModeListener> wrListener : this.mListener) {
                WindowModeListener listener = wrListener.get();
                if (listener != null) {
                    listener.onStackWindowModeChanged(windowMode, data);
                    listener.onWindowModeChanged(this.mWindowingMode, windowMode);
                }
            }
        }

        @Override // nothing.view.IWindowModeCallback
        public void onPackageNameChanged(String packageName, int uid, int pid, boolean topStack) {
            for (WeakReference<WindowModeListener> wrListener : this.mListener) {
                WindowModeListener listener = wrListener.get();
                if (listener != null) {
                    listener.onPackageNameChanged(this.mWindowingMode, packageName, uid, pid, topStack);
                }
            }
        }

        @Override // nothing.view.IWindowModeCallback
        public void onWindowModeBoundChanged(Rect displayBound, int displayRotation) {
            for (WeakReference<WindowModeListener> wrListener : this.mListener) {
                WindowModeListener listener = wrListener.get();
                if (listener != null) {
                    listener.onWindowModeBoundChanged(this.mWindowingMode, displayBound, displayRotation);
                }
            }
        }

        @Override // nothing.view.IWindowModeCallback
        public void onWindowModeStateChanged(int windowModeState) {
            for (WeakReference<WindowModeListener> wrListener : this.mListener) {
                WindowModeListener listener = wrListener.get();
                if (listener != null) {
                    listener.onWindowModeStateChanged(this.mWindowingMode, windowModeState);
                }
            }
        }

        @Override // nothing.view.IWindowModeCallback
        public void onWindowModeFlingToTarget(Rect fromBound, Rect toBound) {
            for (WeakReference<WindowModeListener> wrListener : this.mListener) {
                WindowModeListener listener = wrListener.get();
                if (listener != null) {
                    listener.onWindowModeFlingToTarget(this.mWindowingMode, fromBound, toBound);
                }
            }
        }
    }
}
