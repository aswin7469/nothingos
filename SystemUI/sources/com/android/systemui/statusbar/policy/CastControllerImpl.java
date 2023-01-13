package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.MediaRouter;
import android.media.projection.MediaProjectionInfo;
import android.media.projection.MediaProjectionManager;
import android.os.Handler;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.util.Utils;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.inject.Inject;

@SysUISingleton
public class CastControllerImpl implements CastController {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "CastController";
    private boolean mCallbackRegistered;
    private final ArrayList<CastController.Callback> mCallbacks = new ArrayList<>();
    private final Context mContext;
    private boolean mDiscovering;
    private final Object mDiscoveringLock = new Object();
    private final MediaRouter.SimpleCallback mMediaCallback = new MediaRouter.SimpleCallback() {
        public void onRouteAdded(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            if (CastControllerImpl.DEBUG) {
                Log.d(CastControllerImpl.TAG, "onRouteAdded: " + CastControllerImpl.routeToString(routeInfo));
            }
            CastControllerImpl.this.updateRemoteDisplays();
        }

        public void onRouteChanged(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            if (CastControllerImpl.DEBUG) {
                Log.d(CastControllerImpl.TAG, "onRouteChanged: " + CastControllerImpl.routeToString(routeInfo));
            }
            CastControllerImpl.this.updateRemoteDisplays();
        }

        public void onRouteRemoved(MediaRouter mediaRouter, MediaRouter.RouteInfo routeInfo) {
            if (CastControllerImpl.DEBUG) {
                Log.d(CastControllerImpl.TAG, "onRouteRemoved: " + CastControllerImpl.routeToString(routeInfo));
            }
            CastControllerImpl.this.updateRemoteDisplays();
        }

        public void onRouteSelected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            if (CastControllerImpl.DEBUG) {
                Log.d(CastControllerImpl.TAG, "onRouteSelected(" + i + "): " + CastControllerImpl.routeToString(routeInfo));
            }
            CastControllerImpl.this.updateRemoteDisplays();
        }

        public void onRouteUnselected(MediaRouter mediaRouter, int i, MediaRouter.RouteInfo routeInfo) {
            if (CastControllerImpl.DEBUG) {
                Log.d(CastControllerImpl.TAG, "onRouteUnselected(" + i + "): " + CastControllerImpl.routeToString(routeInfo));
            }
            CastControllerImpl.this.updateRemoteDisplays();
        }
    };
    private final MediaRouter mMediaRouter;
    private MediaProjectionInfo mProjection;
    private final MediaProjectionManager.Callback mProjectionCallback;
    private final Object mProjectionLock = new Object();
    private final MediaProjectionManager mProjectionManager;
    private final ArrayMap<String, MediaRouter.RouteInfo> mRoutes = new ArrayMap<>();

    @Inject
    public CastControllerImpl(Context context, DumpManager dumpManager) {
        C31482 r0 = new MediaProjectionManager.Callback() {
            public void onStart(MediaProjectionInfo mediaProjectionInfo) {
                CastControllerImpl.this.setProjection(mediaProjectionInfo, true);
            }

            public void onStop(MediaProjectionInfo mediaProjectionInfo) {
                CastControllerImpl.this.setProjection(mediaProjectionInfo, false);
            }
        };
        this.mProjectionCallback = r0;
        this.mContext = context;
        MediaRouter mediaRouter = (MediaRouter) context.getSystemService("media_router");
        this.mMediaRouter = mediaRouter;
        mediaRouter.setRouterGroupId("android.media.mirroring_group");
        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) context.getSystemService("media_projection");
        this.mProjectionManager = mediaProjectionManager;
        this.mProjection = mediaProjectionManager.getActiveProjectionInfo();
        mediaProjectionManager.addCallback(r0, new Handler());
        dumpManager.registerDumpable(TAG, this);
        if (DEBUG) {
            Log.d(TAG, "new CastController()");
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("CastController state:");
        printWriter.print("  mDiscovering=");
        printWriter.println(this.mDiscovering);
        printWriter.print("  mCallbackRegistered=");
        printWriter.println(this.mCallbackRegistered);
        printWriter.print("  mCallbacks.size=");
        synchronized (this.mCallbacks) {
            printWriter.println(this.mCallbacks.size());
        }
        printWriter.print("  mRoutes.size=");
        printWriter.println(this.mRoutes.size());
        for (int i = 0; i < this.mRoutes.size(); i++) {
            printWriter.print("    ");
            printWriter.println(routeToString(this.mRoutes.valueAt(i)));
        }
        printWriter.print("  mProjection=");
        printWriter.println((Object) this.mProjection);
    }

    public void addCallback(CastController.Callback callback) {
        synchronized (this.mCallbacks) {
            this.mCallbacks.add(callback);
        }
        fireOnCastDevicesChanged(callback);
        synchronized (this.mDiscoveringLock) {
            handleDiscoveryChangeLocked();
        }
    }

    public void removeCallback(CastController.Callback callback) {
        synchronized (this.mCallbacks) {
            this.mCallbacks.remove((Object) callback);
        }
        synchronized (this.mDiscoveringLock) {
            handleDiscoveryChangeLocked();
        }
    }

    public void setDiscovering(boolean z) {
        synchronized (this.mDiscoveringLock) {
            if (this.mDiscovering != z) {
                this.mDiscovering = z;
                if (DEBUG) {
                    Log.d(TAG, "setDiscovering: " + z);
                }
                handleDiscoveryChangeLocked();
            }
        }
    }

    private void handleDiscoveryChangeLocked() {
        boolean isEmpty;
        if (this.mCallbackRegistered) {
            this.mMediaRouter.removeCallback(this.mMediaCallback);
            this.mCallbackRegistered = false;
        }
        if (this.mDiscovering) {
            this.mMediaRouter.addCallback(4, this.mMediaCallback, 4);
            this.mCallbackRegistered = true;
            return;
        }
        synchronized (this.mCallbacks) {
            isEmpty = this.mCallbacks.isEmpty();
        }
        if (!isEmpty) {
            this.mMediaRouter.addCallback(4, this.mMediaCallback, 8);
            this.mCallbackRegistered = true;
        }
    }

    public void setCurrentUserId(int i) {
        this.mMediaRouter.rebindAsUser(i);
    }

    public List<CastController.CastDevice> getCastDevices() {
        ArrayList arrayList = new ArrayList();
        synchronized (this.mRoutes) {
            for (MediaRouter.RouteInfo next : this.mRoutes.values()) {
                CastController.CastDevice castDevice = new CastController.CastDevice();
                castDevice.f394id = next.getTag().toString();
                CharSequence name = next.getName(this.mContext);
                String str = null;
                castDevice.name = name != null ? name.toString() : null;
                CharSequence description = next.getDescription();
                if (description != null) {
                    str = description.toString();
                }
                castDevice.description = str;
                int statusCode = next.getStatusCode();
                if (statusCode == 2) {
                    castDevice.state = 1;
                } else {
                    if (!next.isSelected()) {
                        if (statusCode != 6) {
                            castDevice.state = 0;
                        }
                    }
                    castDevice.state = 2;
                }
                castDevice.tag = next;
                arrayList.add(castDevice);
            }
        }
        synchronized (this.mProjectionLock) {
            if (this.mProjection != null) {
                CastController.CastDevice castDevice2 = new CastController.CastDevice();
                castDevice2.f394id = this.mProjection.getPackageName();
                castDevice2.name = getAppName(this.mProjection.getPackageName());
                castDevice2.description = this.mContext.getString(C1894R.string.quick_settings_casting);
                castDevice2.state = 2;
                castDevice2.tag = this.mProjection;
                arrayList.add(castDevice2);
            }
        }
        return arrayList;
    }

    public void startCasting(CastController.CastDevice castDevice) {
        if (castDevice != null && castDevice.tag != null) {
            MediaRouter.RouteInfo routeInfo = (MediaRouter.RouteInfo) castDevice.tag;
            if (DEBUG) {
                Log.d(TAG, "startCasting: " + routeToString(routeInfo));
            }
            this.mMediaRouter.selectRoute(4, routeInfo);
        }
    }

    public void stopCasting(CastController.CastDevice castDevice) {
        boolean z = castDevice.tag instanceof MediaProjectionInfo;
        if (DEBUG) {
            Log.d(TAG, "stopCasting isProjection=" + z);
        }
        if (z) {
            MediaProjectionInfo mediaProjectionInfo = (MediaProjectionInfo) castDevice.tag;
            if (Objects.equals(this.mProjectionManager.getActiveProjectionInfo(), mediaProjectionInfo)) {
                this.mProjectionManager.stopActiveProjection();
            } else {
                Log.w(TAG, "Projection is no longer active: " + mediaProjectionInfo);
            }
        } else {
            this.mMediaRouter.getFallbackRoute().select();
        }
    }

    /* access modifiers changed from: private */
    public void setProjection(MediaProjectionInfo mediaProjectionInfo, boolean z) {
        boolean z2;
        MediaProjectionInfo mediaProjectionInfo2 = this.mProjection;
        synchronized (this.mProjectionLock) {
            boolean equals = Objects.equals(mediaProjectionInfo, this.mProjection);
            z2 = true;
            if (z && !equals) {
                this.mProjection = mediaProjectionInfo;
            } else if (z || !equals) {
                z2 = false;
            } else {
                this.mProjection = null;
            }
        }
        if (z2) {
            if (DEBUG) {
                Log.d(TAG, "setProjection: " + mediaProjectionInfo2 + " -> " + this.mProjection);
            }
            fireOnCastDevicesChanged();
        }
    }

    private String getAppName(String str) {
        PackageManager packageManager = this.mContext.getPackageManager();
        if (Utils.isHeadlessRemoteDisplayProvider(packageManager, str)) {
            return "";
        }
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 0);
            if (applicationInfo != null) {
                CharSequence loadLabel = applicationInfo.loadLabel(packageManager);
                if (!TextUtils.isEmpty(loadLabel)) {
                    return loadLabel.toString();
                }
            }
            Log.w(TAG, "No label found for package: " + str);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "Error getting appName for package: " + str, e);
        }
        return str;
    }

    /* access modifiers changed from: private */
    public void updateRemoteDisplays() {
        synchronized (this.mRoutes) {
            this.mRoutes.clear();
            int routeCount = this.mMediaRouter.getRouteCount();
            for (int i = 0; i < routeCount; i++) {
                MediaRouter.RouteInfo routeAt = this.mMediaRouter.getRouteAt(i);
                if (routeAt.isEnabled()) {
                    if (routeAt.matchesTypes(4)) {
                        ensureTagExists(routeAt);
                        this.mRoutes.put(routeAt.getTag().toString(), routeAt);
                    }
                }
            }
            MediaRouter.RouteInfo selectedRoute = this.mMediaRouter.getSelectedRoute(4);
            if (selectedRoute != null && !selectedRoute.isDefault()) {
                ensureTagExists(selectedRoute);
                this.mRoutes.put(selectedRoute.getTag().toString(), selectedRoute);
            }
        }
        fireOnCastDevicesChanged();
    }

    private void ensureTagExists(MediaRouter.RouteInfo routeInfo) {
        if (routeInfo.getTag() == null) {
            routeInfo.setTag(UUID.randomUUID().toString());
        }
    }

    /* access modifiers changed from: package-private */
    public void fireOnCastDevicesChanged() {
        synchronized (this.mCallbacks) {
            Iterator<CastController.Callback> it = this.mCallbacks.iterator();
            while (it.hasNext()) {
                fireOnCastDevicesChanged(it.next());
            }
        }
    }

    private void fireOnCastDevicesChanged(CastController.Callback callback) {
        callback.onCastDevicesChanged();
    }

    /* access modifiers changed from: private */
    public static String routeToString(MediaRouter.RouteInfo routeInfo) {
        if (routeInfo == null) {
            return null;
        }
        StringBuilder append = new StringBuilder().append(routeInfo.getName()).append('/').append(routeInfo.getDescription()).append('@').append(routeInfo.getDeviceAddress()).append(",status=").append(routeInfo.getStatus());
        if (routeInfo.isDefault()) {
            append.append(",default");
        }
        if (routeInfo.isEnabled()) {
            append.append(",enabled");
        }
        if (routeInfo.isConnecting()) {
            append.append(",connecting");
        }
        if (routeInfo.isSelected()) {
            append.append(",selected");
        }
        return append.append(",id=").append(routeInfo.getTag()).toString();
    }
}
