package com.android.settingslib.media;

import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRoute2Info;
import android.media.MediaRouter2Manager;
import android.media.RoutingSessionInfo;
import android.text.TextUtils;
import android.util.Log;
import com.android.settingslib.bluetooth.A2dpProfile;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.CachedBluetoothDeviceManager;
import com.android.settingslib.bluetooth.HearingAidProfile;
import com.android.settingslib.bluetooth.LeAudioProfile;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.media.MediaManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LocalMediaManager implements BluetoothCallback {
    private static final int MAX_DISCONNECTED_DEVICE_NUM = 5;
    private static final String TAG = "LocalMediaManager";
    BluetoothAdapter mBluetoothAdapter;
    private final Collection<DeviceCallback> mCallbacks = new CopyOnWriteArrayList();
    /* access modifiers changed from: private */
    public Context mContext;
    MediaDevice mCurrentConnectedDevice;
    DeviceAttributeChangeCallback mDeviceAttributeChangeCallback = new DeviceAttributeChangeCallback();
    List<MediaDevice> mDisconnectedMediaDevices = new CopyOnWriteArrayList();
    /* access modifiers changed from: private */
    public InfoMediaManager mInfoMediaManager;
    /* access modifiers changed from: private */
    public LocalBluetoothManager mLocalBluetoothManager;
    final MediaDeviceCallback mMediaDeviceCallback = new MediaDeviceCallback();
    List<MediaDevice> mMediaDevices = new CopyOnWriteArrayList();
    /* access modifiers changed from: private */
    public final Object mMediaDevicesLock = new Object();
    /* access modifiers changed from: private */
    public MediaDevice mOnTransferBluetoothDevice;
    /* access modifiers changed from: private */
    public String mPackageName;
    MediaDevice mPhoneDevice;

    public interface DeviceCallback {
        void onAboutToConnectDeviceAdded(String str, String str2, Drawable drawable) {
        }

        void onAboutToConnectDeviceRemoved() {
        }

        void onDeviceAttributesChanged() {
        }

        void onDeviceListUpdate(List<MediaDevice> list) {
        }

        void onRequestFailed(int i) {
        }

        void onSelectedDeviceStateChanged(MediaDevice mediaDevice, int i) {
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaDeviceState {
        public static final int STATE_CONNECTED = 0;
        public static final int STATE_CONNECTING = 1;
        public static final int STATE_CONNECTING_FAILED = 3;
        public static final int STATE_DISCONNECTED = 2;
        public static final int STATE_GROUPING = 5;
        public static final int STATE_SELECTED = 4;
    }

    public void registerCallback(DeviceCallback deviceCallback) {
        this.mCallbacks.add(deviceCallback);
    }

    public void unregisterCallback(DeviceCallback deviceCallback) {
        this.mCallbacks.remove(deviceCallback);
    }

    public LocalMediaManager(Context context, String str, Notification notification) {
        this.mContext = context;
        this.mPackageName = str;
        this.mLocalBluetoothManager = LocalBluetoothManager.getInstance(context, (LocalBluetoothManager.BluetoothManagerCallback) null);
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (this.mLocalBluetoothManager == null) {
            Log.e(TAG, "Bluetooth is not supported on this device");
        } else {
            this.mInfoMediaManager = new InfoMediaManager(context, str, notification, this.mLocalBluetoothManager);
        }
    }

    public LocalMediaManager(Context context, LocalBluetoothManager localBluetoothManager, InfoMediaManager infoMediaManager, String str) {
        this.mContext = context;
        this.mLocalBluetoothManager = localBluetoothManager;
        this.mInfoMediaManager = infoMediaManager;
        this.mPackageName = str;
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean connectDevice(MediaDevice mediaDevice) {
        MediaDevice mediaDeviceById;
        synchronized (this.mMediaDevicesLock) {
            mediaDeviceById = getMediaDeviceById(this.mMediaDevices, mediaDevice.getId());
        }
        if (mediaDeviceById == null) {
            Log.w(TAG, "connectDevice() connectDevice not in the list!");
            return false;
        }
        if (mediaDeviceById instanceof BluetoothMediaDevice) {
            CachedBluetoothDevice cachedDevice = ((BluetoothMediaDevice) mediaDeviceById).getCachedDevice();
            if (!cachedDevice.isConnected() && !cachedDevice.isBusy()) {
                this.mOnTransferBluetoothDevice = mediaDevice;
                mediaDeviceById.setState(1);
                cachedDevice.connect();
                return true;
            }
        }
        if (mediaDeviceById.equals(this.mCurrentConnectedDevice)) {
            Log.d(TAG, "connectDevice() this device is already connected! : " + mediaDeviceById.getName());
            return false;
        }
        MediaDevice mediaDevice2 = this.mCurrentConnectedDevice;
        if (mediaDevice2 != null) {
            mediaDevice2.disconnect();
        }
        mediaDeviceById.setState(1);
        if (TextUtils.isEmpty(this.mPackageName)) {
            this.mInfoMediaManager.connectDeviceWithoutPackageName(mediaDeviceById);
        } else {
            mediaDeviceById.connect();
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void dispatchSelectedDeviceStateChanged(MediaDevice mediaDevice, int i) {
        for (DeviceCallback onSelectedDeviceStateChanged : getCallbacks()) {
            onSelectedDeviceStateChanged.onSelectedDeviceStateChanged(mediaDevice, i);
        }
    }

    public boolean isMediaSessionAvailableForVolumeControl() {
        return this.mInfoMediaManager.isRoutingSessionAvailableForVolumeControl();
    }

    public void startScan() {
        synchronized (this.mMediaDevicesLock) {
            this.mMediaDevices.clear();
        }
        this.mInfoMediaManager.registerCallback(this.mMediaDeviceCallback);
        this.mInfoMediaManager.startScan();
    }

    /* access modifiers changed from: package-private */
    public void dispatchDeviceListUpdate() {
        ArrayList arrayList = new ArrayList(this.mMediaDevices);
        for (DeviceCallback onDeviceListUpdate : getCallbacks()) {
            onDeviceListUpdate.onDeviceListUpdate(arrayList);
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchDeviceAttributesChanged() {
        for (DeviceCallback onDeviceAttributesChanged : getCallbacks()) {
            onDeviceAttributesChanged.onDeviceAttributesChanged();
        }
    }

    /* access modifiers changed from: package-private */
    public void dispatchOnRequestFailed(int i) {
        for (DeviceCallback onRequestFailed : getCallbacks()) {
            onRequestFailed.onRequestFailed(i);
        }
    }

    public void dispatchAboutToConnectDeviceAdded(String str, String str2, Drawable drawable) {
        for (DeviceCallback onAboutToConnectDeviceAdded : getCallbacks()) {
            onAboutToConnectDeviceAdded.onAboutToConnectDeviceAdded(str, str2, drawable);
        }
    }

    public void dispatchAboutToConnectDeviceRemoved() {
        for (DeviceCallback onAboutToConnectDeviceRemoved : getCallbacks()) {
            onAboutToConnectDeviceRemoved.onAboutToConnectDeviceRemoved();
        }
    }

    public void stopScan() {
        this.mInfoMediaManager.unregisterCallback(this.mMediaDeviceCallback);
        this.mInfoMediaManager.stopScan();
        unRegisterDeviceAttributeChangeCallback();
    }

    public MediaDevice getMediaDeviceById(List<MediaDevice> list, String str) {
        for (MediaDevice next : list) {
            if (TextUtils.equals(next.getId(), str)) {
                return next;
            }
        }
        Log.i(TAG, "getMediaDeviceById() can't found device");
        return null;
    }

    public MediaDevice getMediaDeviceById(String str) {
        synchronized (this.mMediaDevicesLock) {
            for (MediaDevice next : this.mMediaDevices) {
                if (TextUtils.equals(next.getId(), str)) {
                    return next;
                }
            }
            Log.i(TAG, "Unable to find device " + str);
            return null;
        }
    }

    public MediaDevice getCurrentConnectedDevice() {
        return this.mCurrentConnectedDevice;
    }

    public boolean addDeviceToPlayMedia(MediaDevice mediaDevice) {
        mediaDevice.setState(5);
        return this.mInfoMediaManager.addDeviceToPlayMedia(mediaDevice);
    }

    public boolean removeDeviceFromPlayMedia(MediaDevice mediaDevice) {
        mediaDevice.setState(5);
        return this.mInfoMediaManager.removeDeviceFromPlayMedia(mediaDevice);
    }

    public List<MediaDevice> getSelectableMediaDevice() {
        return this.mInfoMediaManager.getSelectableMediaDevice();
    }

    public List<MediaDevice> getDeselectableMediaDevice() {
        return this.mInfoMediaManager.getDeselectableMediaDevice();
    }

    public boolean releaseSession() {
        return this.mInfoMediaManager.releaseSession();
    }

    public List<MediaDevice> getSelectedMediaDevice() {
        return this.mInfoMediaManager.getSelectedMediaDevice();
    }

    public void adjustSessionVolume(String str, int i) {
        for (RoutingSessionInfo next : getActiveMediaSession()) {
            if (TextUtils.equals(str, next.getId())) {
                this.mInfoMediaManager.adjustSessionVolume(next, i);
                return;
            }
        }
        Log.w(TAG, "adjustSessionVolume: Unable to find session: " + str);
    }

    public void adjustSessionVolume(int i) {
        this.mInfoMediaManager.adjustSessionVolume(i);
    }

    public int getSessionVolumeMax() {
        return this.mInfoMediaManager.getSessionVolumeMax();
    }

    public int getSessionVolume() {
        return this.mInfoMediaManager.getSessionVolume();
    }

    public CharSequence getSessionName() {
        return this.mInfoMediaManager.getSessionName();
    }

    public List<RoutingSessionInfo> getActiveMediaSession() {
        return this.mInfoMediaManager.getActiveMediaSession();
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public boolean shouldDisableMediaOutput(String str) {
        return this.mInfoMediaManager.shouldDisableMediaOutput(str);
    }

    public boolean shouldEnableVolumeSeekBar(RoutingSessionInfo routingSessionInfo) {
        return this.mInfoMediaManager.shouldEnableVolumeSeekBar(routingSessionInfo);
    }

    /* access modifiers changed from: package-private */
    public MediaDevice updateCurrentConnectedDevice() {
        synchronized (this.mMediaDevicesLock) {
            MediaDevice mediaDevice = null;
            for (MediaDevice next : this.mMediaDevices) {
                if (next instanceof BluetoothMediaDevice) {
                    if (isActiveDevice(((BluetoothMediaDevice) next).getCachedDevice()) && next.isConnected()) {
                        return next;
                    }
                } else if (next instanceof PhoneMediaDevice) {
                    mediaDevice = next;
                }
            }
            return mediaDevice;
        }
    }

    private boolean isActiveDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        LeAudioProfile leAudioProfile;
        HearingAidProfile hearingAidProfile;
        A2dpProfile a2dpProfile = this.mLocalBluetoothManager.getProfileManager().getA2dpProfile();
        boolean equals = a2dpProfile != null ? cachedBluetoothDevice.getDevice().equals(a2dpProfile.getActiveDevice()) : false;
        boolean contains = (equals || (hearingAidProfile = this.mLocalBluetoothManager.getProfileManager().getHearingAidProfile()) == null) ? false : hearingAidProfile.getActiveDevices().contains(cachedBluetoothDevice.getDevice());
        boolean contains2 = (equals || contains || (leAudioProfile = this.mLocalBluetoothManager.getProfileManager().getLeAudioProfile()) == null) ? false : leAudioProfile.getActiveDevices().contains(cachedBluetoothDevice.getDevice());
        if (equals || contains || contains2) {
            return true;
        }
        return false;
    }

    private Collection<DeviceCallback> getCallbacks() {
        return new CopyOnWriteArrayList(this.mCallbacks);
    }

    class MediaDeviceCallback implements MediaManager.MediaDeviceCallback {
        MediaDeviceCallback() {
        }

        public void onDeviceAdded(MediaDevice mediaDevice) {
            boolean z;
            synchronized (LocalMediaManager.this.mMediaDevicesLock) {
                if (!LocalMediaManager.this.mMediaDevices.contains(mediaDevice)) {
                    LocalMediaManager.this.mMediaDevices.add(mediaDevice);
                    z = true;
                } else {
                    z = false;
                }
            }
            if (z) {
                LocalMediaManager.this.dispatchDeviceListUpdate();
            }
        }

        /* JADX WARNING: Removed duplicated region for block: B:29:0x003d A[EDGE_INSN: B:29:0x003d->B:13:0x003d ?: BREAK  , SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:6:0x001f  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onDeviceListAdded(java.util.List<com.android.settingslib.media.MediaDevice> r4) {
            /*
                r3 = this;
                com.android.settingslib.media.LocalMediaManager r0 = com.android.settingslib.media.LocalMediaManager.this
                java.lang.Object r0 = r0.mMediaDevicesLock
                monitor-enter(r0)
                com.android.settingslib.media.LocalMediaManager r1 = com.android.settingslib.media.LocalMediaManager.this     // Catch:{ all -> 0x008f }
                java.util.List<com.android.settingslib.media.MediaDevice> r1 = r1.mMediaDevices     // Catch:{ all -> 0x008f }
                r1.clear()     // Catch:{ all -> 0x008f }
                com.android.settingslib.media.LocalMediaManager r1 = com.android.settingslib.media.LocalMediaManager.this     // Catch:{ all -> 0x008f }
                java.util.List<com.android.settingslib.media.MediaDevice> r1 = r1.mMediaDevices     // Catch:{ all -> 0x008f }
                r1.addAll(r4)     // Catch:{ all -> 0x008f }
                java.util.Iterator r4 = r4.iterator()     // Catch:{ all -> 0x008f }
            L_0x0019:
                boolean r1 = r4.hasNext()     // Catch:{ all -> 0x008f }
                if (r1 == 0) goto L_0x003d
                java.lang.Object r1 = r4.next()     // Catch:{ all -> 0x008f }
                com.android.settingslib.media.MediaDevice r1 = (com.android.settingslib.media.MediaDevice) r1     // Catch:{ all -> 0x008f }
                int r1 = r1.getDeviceType()     // Catch:{ all -> 0x008f }
                r2 = 2
                if (r1 == r2) goto L_0x0032
                r2 = 3
                if (r1 == r2) goto L_0x0032
                r2 = 1
                if (r1 != r2) goto L_0x0019
            L_0x0032:
                com.android.settingslib.media.LocalMediaManager r4 = com.android.settingslib.media.LocalMediaManager.this     // Catch:{ all -> 0x008f }
                java.util.List<com.android.settingslib.media.MediaDevice> r4 = r4.mMediaDevices     // Catch:{ all -> 0x008f }
                java.util.List r1 = r3.buildDisconnectedBluetoothDevice()     // Catch:{ all -> 0x008f }
                r4.addAll(r1)     // Catch:{ all -> 0x008f }
            L_0x003d:
                monitor-exit(r0)     // Catch:{ all -> 0x008f }
                com.android.settingslib.media.LocalMediaManager r4 = com.android.settingslib.media.LocalMediaManager.this
                com.android.settingslib.media.InfoMediaManager r4 = r4.mInfoMediaManager
                com.android.settingslib.media.MediaDevice r4 = r4.getCurrentConnectedDevice()
                com.android.settingslib.media.LocalMediaManager r0 = com.android.settingslib.media.LocalMediaManager.this
                if (r4 == 0) goto L_0x004d
                goto L_0x0051
            L_0x004d:
                com.android.settingslib.media.MediaDevice r4 = r0.updateCurrentConnectedDevice()
            L_0x0051:
                r0.mCurrentConnectedDevice = r4
                com.android.settingslib.media.LocalMediaManager r4 = com.android.settingslib.media.LocalMediaManager.this
                r4.dispatchDeviceListUpdate()
                com.android.settingslib.media.LocalMediaManager r4 = com.android.settingslib.media.LocalMediaManager.this
                com.android.settingslib.media.MediaDevice r4 = r4.mOnTransferBluetoothDevice
                if (r4 == 0) goto L_0x008e
                com.android.settingslib.media.LocalMediaManager r4 = com.android.settingslib.media.LocalMediaManager.this
                com.android.settingslib.media.MediaDevice r4 = r4.mOnTransferBluetoothDevice
                boolean r4 = r4.isConnected()
                if (r4 == 0) goto L_0x008e
                com.android.settingslib.media.LocalMediaManager r4 = com.android.settingslib.media.LocalMediaManager.this
                com.android.settingslib.media.MediaDevice r0 = r4.mOnTransferBluetoothDevice
                r4.connectDevice(r0)
                com.android.settingslib.media.LocalMediaManager r4 = com.android.settingslib.media.LocalMediaManager.this
                com.android.settingslib.media.MediaDevice r4 = r4.mOnTransferBluetoothDevice
                r0 = 0
                r4.setState(r0)
                com.android.settingslib.media.LocalMediaManager r4 = com.android.settingslib.media.LocalMediaManager.this
                com.android.settingslib.media.MediaDevice r1 = r4.mOnTransferBluetoothDevice
                r4.dispatchSelectedDeviceStateChanged(r1, r0)
                com.android.settingslib.media.LocalMediaManager r3 = com.android.settingslib.media.LocalMediaManager.this
                r4 = 0
                com.android.settingslib.media.MediaDevice unused = r3.mOnTransferBluetoothDevice = r4
            L_0x008e:
                return
            L_0x008f:
                r3 = move-exception
                monitor-exit(r0)     // Catch:{ all -> 0x008f }
                throw r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.media.LocalMediaManager.MediaDeviceCallback.onDeviceListAdded(java.util.List):void");
        }

        private List<MediaDevice> buildDisconnectedBluetoothDevice() {
            if (LocalMediaManager.this.mBluetoothAdapter == null) {
                Log.w(LocalMediaManager.TAG, "buildDisconnectedBluetoothDevice() BluetoothAdapter is null");
                return new ArrayList();
            }
            List<BluetoothDevice> mostRecentlyConnectedDevices = LocalMediaManager.this.mBluetoothAdapter.getMostRecentlyConnectedDevices();
            CachedBluetoothDeviceManager cachedDeviceManager = LocalMediaManager.this.mLocalBluetoothManager.getCachedDeviceManager();
            ArrayList<CachedBluetoothDevice> arrayList = new ArrayList<>();
            int i = 0;
            for (BluetoothDevice findDevice : mostRecentlyConnectedDevices) {
                CachedBluetoothDevice findDevice2 = cachedDeviceManager.findDevice(findDevice);
                if (findDevice2 != null && findDevice2.getBondState() == 12 && !findDevice2.isConnected() && isMediaDevice(findDevice2)) {
                    i++;
                    arrayList.add(findDevice2);
                    if (i >= 5) {
                        break;
                    }
                }
            }
            LocalMediaManager.this.unRegisterDeviceAttributeChangeCallback();
            LocalMediaManager.this.mDisconnectedMediaDevices.clear();
            for (CachedBluetoothDevice cachedBluetoothDevice : arrayList) {
                BluetoothMediaDevice bluetoothMediaDevice = new BluetoothMediaDevice(LocalMediaManager.this.mContext, cachedBluetoothDevice, (MediaRouter2Manager) null, (MediaRoute2Info) null, LocalMediaManager.this.mPackageName);
                if (!LocalMediaManager.this.mMediaDevices.contains(bluetoothMediaDevice)) {
                    cachedBluetoothDevice.registerCallback(LocalMediaManager.this.mDeviceAttributeChangeCallback);
                    LocalMediaManager.this.mDisconnectedMediaDevices.add(bluetoothMediaDevice);
                }
            }
            return new ArrayList(LocalMediaManager.this.mDisconnectedMediaDevices);
        }

        /* JADX WARNING: Removed duplicated region for block: B:3:0x000e  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean isMediaDevice(com.android.settingslib.bluetooth.CachedBluetoothDevice r2) {
            /*
                r1 = this;
                java.util.List r1 = r2.getConnectableProfiles()
                java.util.Iterator r1 = r1.iterator()
            L_0x0008:
                boolean r2 = r1.hasNext()
                if (r2 == 0) goto L_0x0022
                java.lang.Object r2 = r1.next()
                com.android.settingslib.bluetooth.LocalBluetoothProfile r2 = (com.android.settingslib.bluetooth.LocalBluetoothProfile) r2
                boolean r0 = r2 instanceof com.android.settingslib.bluetooth.A2dpProfile
                if (r0 != 0) goto L_0x0020
                boolean r0 = r2 instanceof com.android.settingslib.bluetooth.HearingAidProfile
                if (r0 != 0) goto L_0x0020
                boolean r2 = r2 instanceof com.android.settingslib.bluetooth.LeAudioProfile
                if (r2 == 0) goto L_0x0008
            L_0x0020:
                r1 = 1
                return r1
            L_0x0022:
                r1 = 0
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.media.LocalMediaManager.MediaDeviceCallback.isMediaDevice(com.android.settingslib.bluetooth.CachedBluetoothDevice):boolean");
        }

        public void onDeviceRemoved(MediaDevice mediaDevice) {
            boolean z;
            synchronized (LocalMediaManager.this.mMediaDevicesLock) {
                if (LocalMediaManager.this.mMediaDevices.contains(mediaDevice)) {
                    LocalMediaManager.this.mMediaDevices.remove((Object) mediaDevice);
                    z = true;
                } else {
                    z = false;
                }
            }
            if (z) {
                LocalMediaManager.this.dispatchDeviceListUpdate();
            }
        }

        public void onDeviceListRemoved(List<MediaDevice> list) {
            synchronized (LocalMediaManager.this.mMediaDevicesLock) {
                LocalMediaManager.this.mMediaDevices.removeAll(list);
            }
            LocalMediaManager.this.dispatchDeviceListUpdate();
        }

        public void onConnectedDeviceChanged(String str) {
            MediaDevice mediaDeviceById;
            synchronized (LocalMediaManager.this.mMediaDevicesLock) {
                LocalMediaManager localMediaManager = LocalMediaManager.this;
                mediaDeviceById = localMediaManager.getMediaDeviceById(localMediaManager.mMediaDevices, str);
            }
            if (mediaDeviceById == null) {
                mediaDeviceById = LocalMediaManager.this.updateCurrentConnectedDevice();
            }
            LocalMediaManager.this.mCurrentConnectedDevice = mediaDeviceById;
            if (mediaDeviceById != null) {
                mediaDeviceById.setState(0);
                LocalMediaManager localMediaManager2 = LocalMediaManager.this;
                localMediaManager2.dispatchSelectedDeviceStateChanged(localMediaManager2.mCurrentConnectedDevice, 0);
            }
        }

        public void onDeviceAttributesChanged() {
            LocalMediaManager.this.dispatchDeviceAttributesChanged();
        }

        public void onRequestFailed(int i) {
            synchronized (LocalMediaManager.this.mMediaDevicesLock) {
                for (MediaDevice next : LocalMediaManager.this.mMediaDevices) {
                    if (next.getState() == 1) {
                        next.setState(3);
                    }
                }
            }
            LocalMediaManager.this.dispatchOnRequestFailed(i);
        }
    }

    /* access modifiers changed from: private */
    public void unRegisterDeviceAttributeChangeCallback() {
        Iterator<MediaDevice> it = this.mDisconnectedMediaDevices.iterator();
        while (it.hasNext()) {
            ((BluetoothMediaDevice) it.next()).getCachedDevice().unregisterCallback(this.mDeviceAttributeChangeCallback);
        }
    }

    class DeviceAttributeChangeCallback implements CachedBluetoothDevice.Callback {
        DeviceAttributeChangeCallback() {
        }

        public void onDeviceAttributesChanged() {
            if (LocalMediaManager.this.mOnTransferBluetoothDevice != null && !((BluetoothMediaDevice) LocalMediaManager.this.mOnTransferBluetoothDevice).getCachedDevice().isBusy() && !LocalMediaManager.this.mOnTransferBluetoothDevice.isConnected()) {
                LocalMediaManager.this.mOnTransferBluetoothDevice.setState(3);
                MediaDevice unused = LocalMediaManager.this.mOnTransferBluetoothDevice = null;
                LocalMediaManager.this.dispatchOnRequestFailed(0);
            }
            LocalMediaManager.this.dispatchDeviceAttributesChanged();
        }
    }
}
