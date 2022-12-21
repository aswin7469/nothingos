package com.android.settingslib.media;

import android.app.Notification;
import android.content.Context;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import android.media.RoutingSessionInfo;
import android.text.TextUtils;
import android.util.Log;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InfoMediaManager extends MediaManager {
    /* access modifiers changed from: private */
    public static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final String TAG = "InfoMediaManager";
    private LocalBluetoothManager mBluetoothManager;
    /* access modifiers changed from: private */
    public MediaDevice mCurrentConnectedDevice;
    final Executor mExecutor = Executors.newSingleThreadExecutor();
    final RouterManagerCallback mMediaRouterCallback = new RouterManagerCallback();
    String mPackageName;
    MediaRouter2Manager mRouterManager;
    private final boolean mVolumeAdjustmentForRemoteGroupSessions;

    public InfoMediaManager(Context context, String str, Notification notification, LocalBluetoothManager localBluetoothManager) {
        super(context, notification);
        this.mRouterManager = MediaRouter2Manager.getInstance(context);
        this.mBluetoothManager = localBluetoothManager;
        if (!TextUtils.isEmpty(str)) {
            this.mPackageName = str;
        }
        this.mVolumeAdjustmentForRemoteGroupSessions = context.getResources().getBoolean(17891829);
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

    /* access modifiers changed from: package-private */
    public MediaDevice getCurrentConnectedDevice() {
        return this.mCurrentConnectedDevice;
    }

    /* access modifiers changed from: package-private */
    public boolean connectDeviceWithoutPackageName(MediaDevice mediaDevice) {
        RoutingSessionInfo systemRoutingSession = this.mRouterManager.getSystemRoutingSession((String) null);
        if (systemRoutingSession == null) {
            return false;
        }
        this.mRouterManager.transfer(systemRoutingSession, mediaDevice.mRouteInfo);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean addDeviceToPlayMedia(MediaDevice mediaDevice) {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w(TAG, "addDeviceToPlayMedia() package name is null or empty!");
            return false;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo == null || !routingSessionInfo.getSelectableRoutes().contains(mediaDevice.mRouteInfo.getId())) {
            Log.w(TAG, "addDeviceToPlayMedia() Ignoring selecting a non-selectable device : " + mediaDevice.getName());
            return false;
        }
        this.mRouterManager.selectRoute(routingSessionInfo, mediaDevice.mRouteInfo);
        return true;
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

    /* access modifiers changed from: package-private */
    public boolean isRoutingSessionAvailableForVolumeControl() {
        boolean z;
        if (this.mVolumeAdjustmentForRemoteGroupSessions) {
            return true;
        }
        Iterator it = this.mRouterManager.getRoutingSessions(this.mPackageName).iterator();
        boolean z2 = false;
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            RoutingSessionInfo routingSessionInfo = (RoutingSessionInfo) it.next();
            if (!routingSessionInfo.isSystemSession()) {
                if (routingSessionInfo.getSelectedRoutes().size() > 1) {
                    z = true;
                    z2 = true;
                    break;
                }
                z2 = true;
            }
        }
        if (z2) {
            return !z;
        }
        Log.d(TAG, "No routing session for " + this.mPackageName);
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean removeDeviceFromPlayMedia(MediaDevice mediaDevice) {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w(TAG, "removeDeviceFromMedia() package name is null or empty!");
            return false;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo == null || !routingSessionInfo.getSelectedRoutes().contains(mediaDevice.mRouteInfo.getId())) {
            Log.w(TAG, "removeDeviceFromMedia() Ignoring deselecting a non-deselectable device : " + mediaDevice.getName());
            return false;
        }
        this.mRouterManager.deselectRoute(routingSessionInfo, mediaDevice.mRouteInfo);
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean releaseSession() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w(TAG, "releaseSession() package name is null or empty!");
            return false;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            this.mRouterManager.releaseSession(routingSessionInfo);
            return true;
        }
        Log.w(TAG, "releaseSession() Ignoring release session : " + this.mPackageName);
        return false;
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getSelectableMediaDevice() {
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w(TAG, "getSelectableMediaDevice() package name is null or empty!");
            return arrayList;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            for (MediaRoute2Info infoMediaDevice : this.mRouterManager.getSelectableRoutes(routingSessionInfo)) {
                arrayList.add(new InfoMediaDevice(this.mContext, this.mRouterManager, infoMediaDevice, this.mPackageName));
            }
            return arrayList;
        }
        Log.w(TAG, "getSelectableMediaDevice() cannot found selectable MediaDevice from : " + this.mPackageName);
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getDeselectableMediaDevice() {
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.d(TAG, "getDeselectableMediaDevice() package name is null or empty!");
            return arrayList;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getDeselectableRoutes(routingSessionInfo)) {
                arrayList.add(new InfoMediaDevice(this.mContext, this.mRouterManager, mediaRoute2Info, this.mPackageName));
                Log.d(TAG, mediaRoute2Info.getName() + " is deselectable for " + this.mPackageName);
            }
            return arrayList;
        }
        Log.d(TAG, "getDeselectableMediaDevice() cannot found deselectable MediaDevice from : " + this.mPackageName);
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public List<MediaDevice> getSelectedMediaDevice() {
        ArrayList arrayList = new ArrayList();
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w(TAG, "getSelectedMediaDevice() package name is null or empty!");
            return arrayList;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            for (MediaRoute2Info infoMediaDevice : this.mRouterManager.getSelectedRoutes(routingSessionInfo)) {
                arrayList.add(new InfoMediaDevice(this.mContext, this.mRouterManager, infoMediaDevice, this.mPackageName));
            }
            return arrayList;
        }
        Log.w(TAG, "getSelectedMediaDevice() cannot found selectable MediaDevice from : " + this.mPackageName);
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public void adjustSessionVolume(RoutingSessionInfo routingSessionInfo, int i) {
        if (routingSessionInfo == null) {
            Log.w(TAG, "Unable to adjust session volume. RoutingSessionInfo is empty");
        } else {
            this.mRouterManager.setSessionVolume(routingSessionInfo, i);
        }
    }

    /* access modifiers changed from: package-private */
    public void adjustSessionVolume(int i) {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w(TAG, "adjustSessionVolume() package name is null or empty!");
            return;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            Log.d(TAG, "adjustSessionVolume() adjust volume : " + i + ", with : " + this.mPackageName);
            this.mRouterManager.setSessionVolume(routingSessionInfo, i);
            return;
        }
        Log.w(TAG, "adjustSessionVolume() can't found corresponding RoutingSession with : " + this.mPackageName);
    }

    public int getSessionVolumeMax() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w(TAG, "getSessionVolumeMax() package name is null or empty!");
            return -1;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            return routingSessionInfo.getVolumeMax();
        }
        Log.w(TAG, "getSessionVolumeMax() can't found corresponding RoutingSession with : " + this.mPackageName);
        return -1;
    }

    public int getSessionVolume() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w(TAG, "getSessionVolume() package name is null or empty!");
            return -1;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            return routingSessionInfo.getVolume();
        }
        Log.w(TAG, "getSessionVolume() can't found corresponding RoutingSession with : " + this.mPackageName);
        return -1;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getSessionName() {
        if (TextUtils.isEmpty(this.mPackageName)) {
            Log.w(TAG, "Unable to get session name. The package name is null or empty!");
            return null;
        }
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo();
        if (routingSessionInfo != null) {
            return routingSessionInfo.getName();
        }
        Log.w(TAG, "Unable to get session name for package: " + this.mPackageName);
        return null;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldDisableMediaOutput(String str) {
        if (!TextUtils.isEmpty(str)) {
            return this.mRouterManager.getTransferableRoutes(str).isEmpty();
        }
        Log.w(TAG, "shouldDisableMediaOutput() package name is null or empty!");
        return true;
    }

    /* access modifiers changed from: package-private */
    public boolean shouldEnableVolumeSeekBar(RoutingSessionInfo routingSessionInfo) {
        if (routingSessionInfo.isSystemSession() || this.mVolumeAdjustmentForRemoteGroupSessions || routingSessionInfo.getSelectedRoutes().size() <= 1) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
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

    /* access modifiers changed from: private */
    public void buildAllRoutes() {
        for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getAllRoutes()) {
            if (DEBUG) {
                Log.d(TAG, "buildAllRoutes() route : " + mediaRoute2Info.getName() + ", volume : " + mediaRoute2Info.getVolume() + ", type : " + mediaRoute2Info.getType());
            }
            if (mediaRoute2Info.isSystemRoute()) {
                addMediaDevice(mediaRoute2Info);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public List<RoutingSessionInfo> getActiveMediaSession() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.mRouterManager.getSystemRoutingSession((String) null));
        arrayList.addAll(this.mRouterManager.getRemoteSessions());
        return arrayList;
    }

    /* access modifiers changed from: private */
    public void buildAvailableRoutes() {
        for (MediaRoute2Info next : getAvailableRoutes(this.mPackageName)) {
            if (DEBUG) {
                Log.d(TAG, "buildAvailableRoutes() route : " + next.getName() + ", volume : " + next.getVolume() + ", type : " + next.getType());
            }
            addMediaDevice(next);
        }
    }

    private List<MediaRoute2Info> getAvailableRoutes(String str) {
        boolean z;
        ArrayList arrayList = new ArrayList();
        RoutingSessionInfo routingSessionInfo = getRoutingSessionInfo(str);
        if (routingSessionInfo != null) {
            arrayList.addAll(this.mRouterManager.getSelectedRoutes(routingSessionInfo));
            arrayList.addAll(this.mRouterManager.getSelectableRoutes(routingSessionInfo));
        }
        for (MediaRoute2Info mediaRoute2Info : this.mRouterManager.getTransferableRoutes(str)) {
            Iterator it = arrayList.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (TextUtils.equals(mediaRoute2Info.getId(), ((MediaRoute2Info) it.next()).getId())) {
                        z = true;
                        break;
                    }
                } else {
                    z = false;
                    break;
                }
            }
            if (!z) {
                arrayList.add(mediaRoute2Info);
            }
        }
        return arrayList;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v1, resolved type: com.android.settingslib.media.BluetoothMediaDevice} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v2, resolved type: com.android.settingslib.media.InfoMediaDevice} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: com.android.settingslib.media.BluetoothMediaDevice} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v7, resolved type: com.android.settingslib.media.PhoneMediaDevice} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v8, resolved type: com.android.settingslib.media.BluetoothMediaDevice} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v9, resolved type: com.android.settingslib.media.BluetoothMediaDevice} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v10, resolved type: com.android.settingslib.media.BluetoothMediaDevice} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v11, resolved type: com.android.settingslib.media.BluetoothMediaDevice} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v10, resolved type: com.android.settingslib.media.BluetoothMediaDevice} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v12, resolved type: com.android.settingslib.media.BluetoothMediaDevice} */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00ab  */
    /* JADX WARNING: Removed duplicated region for block: B:40:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addMediaDevice(android.media.MediaRoute2Info r9) {
        /*
            r8 = this;
            int r0 = r9.getType()
            r1 = 4
            if (r0 == 0) goto L_0x007b
            r2 = 26
            if (r0 == r2) goto L_0x0053
            r2 = 2000(0x7d0, float:2.803E-42)
            if (r0 == r2) goto L_0x007b
            r2 = 2
            if (r0 == r2) goto L_0x0047
            r2 = 3
            if (r0 == r2) goto L_0x0047
            if (r0 == r1) goto L_0x0047
            r2 = 8
            if (r0 == r2) goto L_0x0053
            r2 = 9
            if (r0 == r2) goto L_0x0047
            r2 = 22
            if (r0 == r2) goto L_0x0047
            r2 = 23
            if (r0 == r2) goto L_0x0053
            r2 = 1001(0x3e9, float:1.403E-42)
            if (r0 == r2) goto L_0x007b
            r2 = 1002(0x3ea, float:1.404E-42)
            if (r0 == r2) goto L_0x007b
            switch(r0) {
                case 11: goto L_0x0047;
                case 12: goto L_0x0047;
                case 13: goto L_0x0047;
                default: goto L_0x0032;
            }
        L_0x0032:
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            java.lang.String r1 = "addMediaDevice() unknown device type : "
            r9.<init>((java.lang.String) r1)
            java.lang.StringBuilder r9 = r9.append((int) r0)
            java.lang.String r9 = r9.toString()
            java.lang.String r0 = "InfoMediaManager"
            android.util.Log.w(r0, r9)
            goto L_0x0079
        L_0x0047:
            com.android.settingslib.media.PhoneMediaDevice r0 = new com.android.settingslib.media.PhoneMediaDevice
            android.content.Context r1 = r8.mContext
            android.media.MediaRouter2Manager r2 = r8.mRouterManager
            java.lang.String r3 = r8.mPackageName
            r0.<init>(r1, r2, r9, r3)
            goto L_0x00a9
        L_0x0053:
            android.bluetooth.BluetoothAdapter r0 = android.bluetooth.BluetoothAdapter.getDefaultAdapter()
            java.lang.String r1 = r9.getAddress()
            android.bluetooth.BluetoothDevice r0 = r0.getRemoteDevice(r1)
            com.android.settingslib.bluetooth.LocalBluetoothManager r1 = r8.mBluetoothManager
            com.android.settingslib.bluetooth.CachedBluetoothDeviceManager r1 = r1.getCachedDeviceManager()
            com.android.settingslib.bluetooth.CachedBluetoothDevice r4 = r1.findDevice(r0)
            if (r4 == 0) goto L_0x0079
            com.android.settingslib.media.BluetoothMediaDevice r0 = new com.android.settingslib.media.BluetoothMediaDevice
            android.content.Context r3 = r8.mContext
            android.media.MediaRouter2Manager r5 = r8.mRouterManager
            java.lang.String r7 = r8.mPackageName
            r2 = r0
            r6 = r9
            r2.<init>(r3, r4, r5, r6, r7)
            goto L_0x00a9
        L_0x0079:
            r0 = 0
            goto L_0x00a9
        L_0x007b:
            com.android.settingslib.media.InfoMediaDevice r0 = new com.android.settingslib.media.InfoMediaDevice
            android.content.Context r2 = r8.mContext
            android.media.MediaRouter2Manager r3 = r8.mRouterManager
            java.lang.String r4 = r8.mPackageName
            r0.<init>(r2, r3, r9, r4)
            java.lang.String r2 = r8.mPackageName
            boolean r2 = android.text.TextUtils.isEmpty(r2)
            if (r2 != 0) goto L_0x00a9
            android.media.RoutingSessionInfo r2 = r8.getRoutingSessionInfo()
            java.util.List r2 = r2.getSelectedRoutes()
            java.lang.String r9 = r9.getId()
            boolean r9 = r2.contains(r9)
            if (r9 == 0) goto L_0x00a9
            r0.setState(r1)
            com.android.settingslib.media.MediaDevice r9 = r8.mCurrentConnectedDevice
            if (r9 != 0) goto L_0x00a9
            r8.mCurrentConnectedDevice = r0
        L_0x00a9:
            if (r0 == 0) goto L_0x00b0
            java.util.List r8 = r8.mMediaDevices
            r8.add(r0)
        L_0x00b0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.media.InfoMediaManager.addMediaDevice(android.media.MediaRoute2Info):void");
    }

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
                Log.d(InfoMediaManager.TAG, "onTransferred() oldSession : " + routingSessionInfo.getName() + ", newSession : " + routingSessionInfo2.getName());
            }
            InfoMediaManager.this.mMediaDevices.clear();
            String str = null;
            MediaDevice unused = InfoMediaManager.this.mCurrentConnectedDevice = null;
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
