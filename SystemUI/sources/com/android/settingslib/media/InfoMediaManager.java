package com.android.settingslib.media;

import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import android.media.RoutingSessionInfo;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public class InfoMediaManager extends MediaManager {
    private static final boolean DEBUG = Log.isLoggable("InfoMediaManager", 3);
    private LocalBluetoothManager mBluetoothManager;
    private MediaDevice mCurrentConnectedDevice;
    @VisibleForTesting
    String mPackageName;
    @VisibleForTesting
    MediaRouter2Manager mRouterManager;
    @VisibleForTesting
    final RouterManagerCallback mMediaRouterCallback = new RouterManagerCallback();
    @VisibleForTesting
    final Executor mExecutor = Executors.newSingleThreadExecutor();

    public InfoMediaManager(Context context, String str, Notification notification, LocalBluetoothManager localBluetoothManager) {
        super(context, notification);
        this.mRouterManager = MediaRouter2Manager.getInstance(context);
        this.mBluetoothManager = localBluetoothManager;
        if (!TextUtils.isEmpty(str)) {
            this.mPackageName = str;
        }
    }

    public void startScan() {
        this.mMediaDevices.clear();
        this.mRouterManager.registerCallback(this.mExecutor, this.mMediaRouterCallback);
        this.mRouterManager.startScan();
        refreshDevices();
    }

    public void stopScan() {
        this.mRouterManager.unregisterCallback(this.mMediaRouterCallback);
        this.mRouterManager.stopScan();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MediaDevice getCurrentConnectedDevice() {
        return this.mCurrentConnectedDevice;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean connectDeviceWithoutPackageName(MediaDevice mediaDevice) {
        List activeSessions = this.mRouterManager.getActiveSessions();
        if (activeSessions.size() > 0) {
            this.mRouterManager.transfer((RoutingSessionInfo) activeSessions.get(0), mediaDevice.mRouteInfo);
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean addDeviceToPlayMedia(MediaDevice mediaDevice) {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "addDeviceToPlayMedia() package name is null or empty!");
            return false;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null && routingSessionInfo.getSelectableRoutes().contains(mediaDevice.mRouteInfo.getId())) {
            this.mRouterManager.selectRoute(routingSessionInfo, mediaDevice.mRouteInfo);
            return true;
        }
        Log.w("InfoMediaManager", "addDeviceToPlayMedia() Ignoring selecting a non-selectable device : " + mediaDevice.getName());
        return false;
    }

    private RoutingSessionInfo getRoutingSessionInfo() {
        return getRoutingSessionInfo(this.mPackageName);
    }

    private RoutingSessionInfo getRoutingSessionInfo(String str) {
        List routingSessions = this.mRouterManager.getRoutingSessions(str);
        if (routingSessions == null || routingSessions.isEmpty()) {
            return null;
        }
        return (RoutingSessionInfo) routingSessions.get(routingSessions.size() - 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean removeDeviceFromPlayMedia(MediaDevice mediaDevice) {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "removeDeviceFromMedia() package name is null or empty!");
            return false;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null && routingSessionInfo.getSelectedRoutes().contains(mediaDevice.mRouteInfo.getId())) {
            this.mRouterManager.deselectRoute(routingSessionInfo, mediaDevice.mRouteInfo);
            return true;
        }
        Log.w("InfoMediaManager", "removeDeviceFromMedia() Ignoring deselecting a non-deselectable device : " + mediaDevice.getName());
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean releaseSession() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "releaseSession() package name is null or empty!");
            return false;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            this.mRouterManager.releaseSession(routingSessionInfo);
            return true;
        }
        Log.w("InfoMediaManager", "releaseSession() Ignoring release session : " + this.mPackageName);
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<MediaDevice> getSelectableMediaDevice() {
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "getSelectableMediaDevice() package name is null or empty!");
            return arrayList;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getSelectableRoutes(routingSessionInfo)) {
                arrayList.add(new InfoMediaDevice(this.mContext, this.mRouterManager, mediaRoute2Info, this.mPackageName));
            }
            return arrayList;
        }
        Log.w("InfoMediaManager", "getSelectableMediaDevice() cannot found selectable MediaDevice from : " + this.mPackageName);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<MediaDevice> getDeselectableMediaDevice() {
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.d("InfoMediaManager", "getDeselectableMediaDevice() package name is null or empty!");
            return arrayList;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getDeselectableRoutes(routingSessionInfo)) {
                arrayList.add(new InfoMediaDevice(this.mContext, this.mRouterManager, mediaRoute2Info, this.mPackageName));
                Log.d("InfoMediaManager", ((Object) mediaRoute2Info.getName()) + " is deselectable for " + this.mPackageName);
            }
            return arrayList;
        }
        Log.d("InfoMediaManager", "getDeselectableMediaDevice() cannot found deselectable MediaDevice from : " + this.mPackageName);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<MediaDevice> getSelectedMediaDevice() {
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "getSelectedMediaDevice() package name is null or empty!");
            return arrayList;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getSelectedRoutes(routingSessionInfo)) {
                arrayList.add(new InfoMediaDevice(this.mContext, this.mRouterManager, mediaRoute2Info, this.mPackageName));
            }
            return arrayList;
        }
        Log.w("InfoMediaManager", "getSelectedMediaDevice() cannot found selectable MediaDevice from : " + this.mPackageName);
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void adjustSessionVolume(int i) {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "adjustSessionVolume() package name is null or empty!");
            return;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            Log.d("InfoMediaManager", "adjustSessionVolume() adjust volume : " + i + ", with : " + this.mPackageName);
            this.mRouterManager.setSessionVolume(routingSessionInfo, i);
            return;
        }
        Log.w("InfoMediaManager", "adjustSessionVolume() can't found corresponding RoutingSession with : " + this.mPackageName);
    }

    public int getSessionVolumeMax() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "getSessionVolumeMax() package name is null or empty!");
            return -1;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            return routingSessionInfo.getVolumeMax();
        }
        Log.w("InfoMediaManager", "getSessionVolumeMax() can't found corresponding RoutingSession with : " + this.mPackageName);
        return -1;
    }

    public int getSessionVolume() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "getSessionVolume() package name is null or empty!");
            return -1;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            return routingSessionInfo.getVolume();
        }
        Log.w("InfoMediaManager", "getSessionVolume() can't found corresponding RoutingSession with : " + this.mPackageName);
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharSequence getSessionName() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w("InfoMediaManager", "Unable to get session name. The package name is null or empty!");
            return null;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            return routingSessionInfo.getName();
        }
        Log.w("InfoMediaManager", "Unable to get session name for package: " + this.mPackageName);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshDevices() {
        this.mMediaDevices.clear();
        this.mCurrentConnectedDevice = null;
        if (TextUtils.isEmpty(this.mPackageName)) {
            buildAllRoutes();
        } else {
            buildAvailableRoutes();
        }
        dispatchDeviceListAdded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildAllRoutes() {
        for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getAllRoutes()) {
            if (DEBUG) {
                Log.d("InfoMediaManager", "buildAllRoutes() route : " + ((Object) mediaRoute2Info.getName()) + ", volume : " + mediaRoute2Info.getVolume() + ", type : " + mediaRoute2Info.getType());
            }
            if (mediaRoute2Info.isSystemRoute()) {
                addMediaDevice(mediaRoute2Info);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void buildAvailableRoutes() {
        for (MediaRoute2Info mediaRoute2Info : getAvailableRoutes(this.mPackageName)) {
            if (DEBUG) {
                Log.d("InfoMediaManager", "buildAvailableRoutes() route : " + ((Object) mediaRoute2Info.getName()) + ", volume : " + mediaRoute2Info.getVolume() + ", type : " + mediaRoute2Info.getType());
            }
            addMediaDevice(mediaRoute2Info);
        }
    }

    private List<MediaRoute2Info> getAvailableRoutes(String str) {
        ArrayList arrayList = new ArrayList();
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo(str);
        if (routingSessionInfo != null) {
            arrayList.addAll(this.mRouterManager.getSelectedRoutes(routingSessionInfo));
        }
        for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getTransferableRoutes(str)) {
            boolean z = false;
            Iterator it = arrayList.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                } else if (TextUtils.equals(mediaRoute2Info.getId(), ((MediaRoute2Info) it.next()).getId())) {
                    z = true;
                    break;
                }
            }
            if (!z) {
                arrayList.add(mediaRoute2Info);
            }
        }
        return arrayList;
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x00a6  */
    /* JADX WARN: Removed duplicated region for block: B:30:? A[RETURN, SYNTHETIC] */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    void addMediaDevice(MediaRoute2Info mediaRoute2Info) {
        MediaDevice infoMediaDevice;
        int type = mediaRoute2Info.getType();
        if (type != 0 && type != 2000) {
            if (type != 2 && type != 3 && type != 4) {
                if (type != 8) {
                    if (type != 9 && type != 22) {
                        if (type != 23) {
                            if (type != 1001 && type != 1002) {
                                switch (type) {
                                    case 11:
                                    case 12:
                                    case 13:
                                        break;
                                    default:
                                        Log.w("InfoMediaManager", "addMediaDevice() unknown device type : " + type);
                                        infoMediaDevice = null;
                                        break;
                                }
                                if (infoMediaDevice == null) {
                                    return;
                                }
                                this.mMediaDevices.add(infoMediaDevice);
                                return;
                            }
                        }
                    }
                }
                CachedBluetoothDevice findDevice = this.mBluetoothManager.getCachedDeviceManager().findDevice(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mediaRoute2Info.getAddress()));
                if (findDevice != null) {
                    infoMediaDevice = new BluetoothMediaDevice(this.mContext, findDevice, this.mRouterManager, mediaRoute2Info, this.mPackageName);
                    if (infoMediaDevice == null) {
                    }
                }
                infoMediaDevice = null;
                if (infoMediaDevice == null) {
                }
            }
            infoMediaDevice = new PhoneMediaDevice(this.mContext, this.mRouterManager, mediaRoute2Info, this.mPackageName);
            if (infoMediaDevice == null) {
            }
        }
        infoMediaDevice = new InfoMediaDevice(this.mContext, this.mRouterManager, mediaRoute2Info, this.mPackageName);
        if (!TextUtils.isEmpty(this.mPackageName) && getRoutingSessionInfo().getSelectedRoutes().contains(mediaRoute2Info.getId()) && this.mCurrentConnectedDevice == null) {
            this.mCurrentConnectedDevice = infoMediaDevice;
        }
        if (infoMediaDevice == null) {
        }
    }

    /* loaded from: classes.dex */
    class RouterManagerCallback implements MediaRouter2Manager.Callback {
        RouterManagerCallback() {
        }

        public void onRoutesAdded(List<MediaRoute2Info> list) {
            InfoMediaManager.this.refreshDevices();
        }

        public void onPreferredFeaturesChanged(String str, List<String> list) {
            if (TextUtils.equals(InfoMediaManager.this.mPackageName, str)) {
                InfoMediaManager.this.refreshDevices();
            }
        }

        public void onRoutesChanged(List<MediaRoute2Info> list) {
            InfoMediaManager.this.refreshDevices();
        }

        public void onRoutesRemoved(List<MediaRoute2Info> list) {
            InfoMediaManager.this.refreshDevices();
        }

        public void onTransferred(RoutingSessionInfo routingSessionInfo, RoutingSessionInfo routingSessionInfo2) {
            if (InfoMediaManager.DEBUG) {
                Log.d("InfoMediaManager", "onTransferred() oldSession : " + ((Object) routingSessionInfo.getName()) + ", newSession : " + ((Object) routingSessionInfo2.getName()));
            }
            InfoMediaManager.this.mMediaDevices.clear();
            String str = null;
            InfoMediaManager.this.mCurrentConnectedDevice = null;
            if (TextUtils.isEmpty(InfoMediaManager.this.mPackageName)) {
                InfoMediaManager.this.buildAllRoutes();
            } else {
                InfoMediaManager.this.buildAvailableRoutes();
            }
            if (InfoMediaManager.this.mCurrentConnectedDevice != null) {
                str = InfoMediaManager.this.mCurrentConnectedDevice.getId();
            }
            InfoMediaManager.this.dispatchConnectedDeviceChanged(str);
        }

        public void onTransferFailed(RoutingSessionInfo routingSessionInfo, MediaRoute2Info mediaRoute2Info) {
            InfoMediaManager.this.dispatchOnRequestFailed(0);
        }

        public void onRequestFailed(int i) {
            InfoMediaManager.this.dispatchOnRequestFailed(i);
        }

        public void onSessionUpdated(RoutingSessionInfo routingSessionInfo) {
            InfoMediaManager.this.dispatchDataChanged();
        }
    }
}
