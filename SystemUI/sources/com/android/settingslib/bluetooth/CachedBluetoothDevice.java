package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.LruCache;
import android.util.Pair;
import com.android.internal.util.ArrayUtils;
import com.android.settingslib.utils.ThreadUtils;
import com.android.settingslib.widget.AdaptiveOutlineDrawable;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.power.TemperatureController;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class CachedBluetoothDevice implements Comparable<CachedBluetoothDevice> {
    private static final long MAX_HEARING_AIDS_DELAY_FOR_AUTO_CONNECT = 15000;
    private static final long MAX_HOGP_DELAY_FOR_AUTO_CONNECT = 30000;
    private static final long MAX_LEAUDIO_DELAY_FOR_AUTO_CONNECT = 30000;
    private static final long MAX_MEDIA_PROFILE_CONNECT_DELAY = 60000;
    private static final long MAX_UUID_DELAY_FOR_AUTO_CONNECT = 5000;
    static final int PRIVATE_ADDR = 101;
    private static final String TAG = "CachedBluetoothDevice";
    private static final boolean mIsTwsConnectEnabled = false;
    private final int BREDR = 100;
    private final int GROUPID_END = 15;
    private final int GROUPID_START = 0;
    private final int UNKNOWN = -1;
    private final Collection<Callback> mCallbacks = new CopyOnWriteArrayList();
    private long mConnectAttempted;
    private final Context mContext;
    BluetoothDevice mDevice;
    private int mDeviceMode;
    private int mDeviceSide;
    LruCache<String, BitmapDrawable> mDrawableCache;
    private int mGroupId;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i == 1) {
                boolean unused = CachedBluetoothDevice.this.mIsHeadsetProfileConnectedFail = true;
            } else if (i == 2) {
                boolean unused2 = CachedBluetoothDevice.this.mIsA2dpProfileConnectedFail = true;
            } else if (i == 21) {
                boolean unused3 = CachedBluetoothDevice.this.mIsHearingAidProfileConnectedFail = true;
            } else if (i != 22) {
                Log.w(CachedBluetoothDevice.TAG, "handleMessage(): unknown message : " + message.what);
            } else {
                boolean unused4 = CachedBluetoothDevice.this.mIsLeAudioProfileConnectedFail = true;
            }
            Log.w(CachedBluetoothDevice.TAG, "Connect to profile : " + message.what + " timeout, show error message !");
            CachedBluetoothDevice.this.refresh();
        }
    };
    private long mHiSyncId;
    /* access modifiers changed from: private */
    public boolean mIsA2dpProfileConnectedFail = false;
    private boolean mIsActiveDeviceA2dp = false;
    private boolean mIsActiveDeviceHeadset = false;
    private boolean mIsActiveDeviceHearingAid = false;
    private boolean mIsActiveDeviceLeAudio = false;
    boolean mIsCoordinatedSetMember = false;
    private boolean mIsGroupDevice = false;
    /* access modifiers changed from: private */
    public boolean mIsHeadsetProfileConnectedFail = false;
    /* access modifiers changed from: private */
    public boolean mIsHearingAidProfileConnectedFail = false;
    private boolean mIsIgnore = false;
    private boolean mIsLeAudioEnabled = false;
    /* access modifiers changed from: private */
    public boolean mIsLeAudioProfileConnectedFail = false;
    boolean mJustDiscovered;
    private final BluetoothAdapter mLocalAdapter;
    private boolean mLocalNapRoleConnected;
    private Set<CachedBluetoothDevice> mMemberDevices = new HashSet();
    private final Object mProfileLock = new Object();
    private final LocalBluetoothProfileManager mProfileManager;
    private final Collection<LocalBluetoothProfile> mProfiles = new CopyOnWriteArrayList();
    private int mQGroupId;
    private final Collection<LocalBluetoothProfile> mRemovedProfiles = new CopyOnWriteArrayList();
    short mRssi;
    private CachedBluetoothDevice mSubDevice;
    public int mTwspBatteryLevel;
    public int mTwspBatteryState;
    private int mType = -1;
    private boolean mUnpairing;

    public interface Callback {
        void onDeviceAttributesChanged();
    }

    private boolean isTwsBatteryAvailable(int i, int i2) {
        return i >= 0 && i2 >= 0;
    }

    CachedBluetoothDevice(Context context, LocalBluetoothProfileManager localBluetoothProfileManager, BluetoothDevice bluetoothDevice) {
        this.mContext = context;
        this.mLocalAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mProfileManager = localBluetoothProfileManager;
        this.mDevice = bluetoothDevice;
        fillData();
        this.mHiSyncId = 0;
        this.mGroupId = -1;
        this.mQGroupId = -1;
        initDrawableCache();
        this.mTwspBatteryState = -1;
        this.mTwspBatteryLevel = -1;
        this.mUnpairing = false;
    }

    CachedBluetoothDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mContext = cachedBluetoothDevice.mContext;
        this.mLocalAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mProfileManager = cachedBluetoothDevice.mProfileManager;
        this.mDevice = cachedBluetoothDevice.mDevice;
        fillData();
        this.mHiSyncId = 0;
        initDrawableCache();
        this.mTwspBatteryState = -1;
        this.mTwspBatteryLevel = -1;
        this.mUnpairing = false;
    }

    private void initDrawableCache() {
        this.mDrawableCache = new LruCache<String, BitmapDrawable>(((int) (Runtime.getRuntime().maxMemory() / 1024)) / 8) {
            /* access modifiers changed from: protected */
            public int sizeOf(String str, BitmapDrawable bitmapDrawable) {
                return bitmapDrawable.getBitmap().getByteCount() / 1024;
            }
        };
    }

    private BluetoothDevice getTwsPeerDevice() {
        if (this.mDevice.isTwsPlusDevice()) {
            return BluetoothAdapter.getDefaultAdapter().getRemoteDevice(this.mDevice.getTwsPlusPeerAddress());
        }
        return null;
    }

    private String describe(LocalBluetoothProfile localBluetoothProfile) {
        StringBuilder sb = new StringBuilder("Address:");
        sb.append((Object) this.mDevice);
        if (localBluetoothProfile != null) {
            sb.append(" Profile:").append((Object) localBluetoothProfile);
        }
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public void onProfileStateChanged(LocalBluetoothProfile localBluetoothProfile, int i) {
        Log.d(TAG, "onProfileStateChanged: profile " + localBluetoothProfile + ", device " + this.mDevice.getAnonymizedAddress() + ", newProfileState " + i);
        if (this.mLocalAdapter.getState() == 13) {
            Log.d(TAG, " BT Turninig Off...Profile conn state change ignored...");
            return;
        }
        synchronized (this.mProfileLock) {
            if ((localBluetoothProfile instanceof A2dpProfile) || (localBluetoothProfile instanceof HeadsetProfile) || (localBluetoothProfile instanceof HearingAidProfile)) {
                setProfileConnectedStatus(localBluetoothProfile.getProfileId(), false);
                if (i != 0) {
                    if (i == 1) {
                        this.mHandler.sendEmptyMessageDelayed(localBluetoothProfile.getProfileId(), MAX_MEDIA_PROFILE_CONNECT_DELAY);
                    } else if (i == 2) {
                        this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    } else if (i != 3) {
                        Log.w(TAG, "onProfileStateChanged(): unknown profile state : " + i);
                    } else if (this.mHandler.hasMessages(localBluetoothProfile.getProfileId())) {
                        this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    }
                } else if (this.mHandler.hasMessages(localBluetoothProfile.getProfileId())) {
                    this.mHandler.removeMessages(localBluetoothProfile.getProfileId());
                    setProfileConnectedStatus(localBluetoothProfile.getProfileId(), true);
                }
            }
            if (i == 2) {
                if (localBluetoothProfile instanceof MapProfile) {
                    localBluetoothProfile.setEnabled(this.mDevice, true);
                }
                if (!this.mProfiles.contains(localBluetoothProfile)) {
                    this.mRemovedProfiles.remove(localBluetoothProfile);
                    this.mProfiles.add(localBluetoothProfile);
                    if ((localBluetoothProfile instanceof PanProfile) && ((PanProfile) localBluetoothProfile).isLocalRoleNap(this.mDevice)) {
                        this.mLocalNapRoleConnected = true;
                    }
                }
            } else if ((localBluetoothProfile instanceof MapProfile) && i == 0) {
                localBluetoothProfile.setEnabled(this.mDevice, false);
            } else if (this.mLocalNapRoleConnected && (localBluetoothProfile instanceof PanProfile) && ((PanProfile) localBluetoothProfile).isLocalRoleNap(this.mDevice) && i == 0) {
                Log.d(TAG, "Removing PanProfile from device after NAP disconnect");
                this.mProfiles.remove(localBluetoothProfile);
                this.mRemovedProfiles.add(localBluetoothProfile);
                this.mLocalNapRoleConnected = false;
            } else if ((localBluetoothProfile instanceof HeadsetProfile) && i == 0) {
                this.mTwspBatteryState = -1;
                this.mTwspBatteryLevel = -1;
            }
        }
        fetchActiveDevices();
    }

    /* access modifiers changed from: package-private */
    public void setProfileConnectedStatus(int i, boolean z) {
        if (i == 1) {
            this.mIsHeadsetProfileConnectedFail = z;
        } else if (i == 2) {
            this.mIsA2dpProfileConnectedFail = z;
        } else if (i == 21) {
            this.mIsHearingAidProfileConnectedFail = z;
        } else if (i != 22) {
            Log.w(TAG, "setProfileConnectedStatus(): unknown profile id : " + i);
        } else {
            this.mIsLeAudioProfileConnectedFail = z;
        }
    }

    public void disconnect() {
        synchronized (this.mProfileLock) {
            if (getGroupId() != -1) {
                for (CachedBluetoothDevice next : getMemberDevice()) {
                    Log.d(TAG, "Disconnect the member(" + next.getAddress() + NavigationBarInflaterView.KEY_CODE_END);
                    next.disconnect();
                }
            }
            this.mDevice.disconnect();
        }
        PbapServerProfile pbapProfile = this.mProfileManager.getPbapProfile();
        if (pbapProfile != null && isConnectedProfile(pbapProfile)) {
            pbapProfile.setEnabled(this.mDevice, false);
        }
    }

    public void disconnect(LocalBluetoothProfile localBluetoothProfile) {
        if (localBluetoothProfile.setEnabled(this.mDevice, false)) {
            Log.d(TAG, "Command sent successfully:DISCONNECT " + describe(localBluetoothProfile));
        }
    }

    @Deprecated
    public void connect(boolean z) {
        connect();
    }

    public void connect() {
        if (ensurePaired()) {
            this.mConnectAttempted = SystemClock.elapsedRealtime();
            Log.d(TAG, "connect: mConnectAttempted = " + this.mConnectAttempted);
            connectDevice();
        }
    }

    public int getDeviceSide() {
        return this.mDeviceSide;
    }

    public void setDeviceSide(int i) {
        this.mDeviceSide = i;
    }

    public int getDeviceMode() {
        return this.mDeviceMode;
    }

    public void setDeviceMode(int i) {
        this.mDeviceMode = i;
    }

    public long getHiSyncId() {
        return this.mHiSyncId;
    }

    public void setHiSyncId(long j) {
        this.mHiSyncId = j;
    }

    public boolean isHearingAidDevice() {
        return this.mHiSyncId != 0;
    }

    public void setIsCoordinatedSetMember(boolean z) {
        this.mIsCoordinatedSetMember = z;
    }

    public boolean isCoordinatedSetMemberDevice() {
        return this.mIsCoordinatedSetMember;
    }

    public int getGroupId() {
        return this.mGroupId;
    }

    public int getQGroupId() {
        return this.mQGroupId;
    }

    public void setGroupId(int i) {
        this.mGroupId = i;
    }

    /* access modifiers changed from: package-private */
    public void onBondingDockConnect() {
        connect();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0080, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void connectDevice() {
        /*
            r5 = this;
            java.lang.String r0 = "No profiles. Maybe we will connect later for device "
            java.lang.Object r1 = r5.mProfileLock
            monitor-enter(r1)
            java.util.Collection<com.android.settingslib.bluetooth.LocalBluetoothProfile> r2 = r5.mProfiles     // Catch:{ all -> 0x0081 }
            boolean r2 = r2.isEmpty()     // Catch:{ all -> 0x0081 }
            if (r2 == 0) goto L_0x0023
            java.lang.String r2 = "CachedBluetoothDevice"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0081 }
            r3.<init>((java.lang.String) r0)     // Catch:{ all -> 0x0081 }
            android.bluetooth.BluetoothDevice r5 = r5.mDevice     // Catch:{ all -> 0x0081 }
            java.lang.StringBuilder r5 = r3.append((java.lang.Object) r5)     // Catch:{ all -> 0x0081 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x0081 }
            android.util.Log.d(r2, r5)     // Catch:{ all -> 0x0081 }
            monitor-exit(r1)     // Catch:{ all -> 0x0081 }
            return
        L_0x0023:
            android.bluetooth.BluetoothDevice r0 = r5.mDevice     // Catch:{ all -> 0x0081 }
            boolean r0 = r0.isBondingInitiatedLocally()     // Catch:{ all -> 0x0081 }
            if (r0 == 0) goto L_0x0039
            java.lang.String r0 = "CachedBluetoothDevice"
            java.lang.String r2 = "reset BondingInitiatedLocally flag"
            android.util.Log.w(r0, r2)     // Catch:{ all -> 0x0081 }
            android.bluetooth.BluetoothDevice r0 = r5.mDevice     // Catch:{ all -> 0x0081 }
            r2 = 0
            r0.setBondingInitiatedLocally(r2)     // Catch:{ all -> 0x0081 }
        L_0x0039:
            android.bluetooth.BluetoothDevice r0 = r5.mDevice     // Catch:{ all -> 0x0081 }
            r0.connect()     // Catch:{ all -> 0x0081 }
            int r0 = r5.getGroupId()     // Catch:{ all -> 0x0081 }
            r2 = -1
            if (r0 == r2) goto L_0x007f
            java.util.Set r5 = r5.getMemberDevice()     // Catch:{ all -> 0x0081 }
            java.util.Iterator r5 = r5.iterator()     // Catch:{ all -> 0x0081 }
        L_0x004d:
            boolean r0 = r5.hasNext()     // Catch:{ all -> 0x0081 }
            if (r0 == 0) goto L_0x007f
            java.lang.Object r0 = r5.next()     // Catch:{ all -> 0x0081 }
            com.android.settingslib.bluetooth.CachedBluetoothDevice r0 = (com.android.settingslib.bluetooth.CachedBluetoothDevice) r0     // Catch:{ all -> 0x0081 }
            java.lang.String r2 = "CachedBluetoothDevice"
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x0081 }
            r3.<init>()     // Catch:{ all -> 0x0081 }
            java.lang.String r4 = "connect the member("
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)     // Catch:{ all -> 0x0081 }
            java.lang.String r4 = r0.getAddress()     // Catch:{ all -> 0x0081 }
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)     // Catch:{ all -> 0x0081 }
            java.lang.String r4 = ")"
            java.lang.StringBuilder r3 = r3.append((java.lang.String) r4)     // Catch:{ all -> 0x0081 }
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x0081 }
            android.util.Log.d(r2, r3)     // Catch:{ all -> 0x0081 }
            r0.connect()     // Catch:{ all -> 0x0081 }
            goto L_0x004d
        L_0x007f:
            monitor-exit(r1)     // Catch:{ all -> 0x0081 }
            return
        L_0x0081:
            r5 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0081 }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.connectDevice():void");
    }

    public void connectProfile(LocalBluetoothProfile localBluetoothProfile) {
        this.mConnectAttempted = SystemClock.elapsedRealtime();
        connectInt(localBluetoothProfile);
        refresh();
    }

    /* access modifiers changed from: package-private */
    public synchronized void connectInt(LocalBluetoothProfile localBluetoothProfile) {
        if (ensurePaired()) {
            if (localBluetoothProfile.setEnabled(this.mDevice, true)) {
                Log.d(TAG, "Command sent successfully:CONNECT " + describe(localBluetoothProfile));
            } else {
                Log.i(TAG, "Failed to connect " + localBluetoothProfile.toString() + " to " + getName());
            }
        }
    }

    private boolean ensurePaired() {
        if (getBondState() != 10) {
            return true;
        }
        startPairing();
        return false;
    }

    public boolean startPairing() {
        if (this.mLocalAdapter.isDiscovering()) {
            this.mLocalAdapter.cancelDiscovery();
        }
        return this.mDevice.createBond();
    }

    public void unpair() {
        BluetoothDevice twsPeerDevice;
        int bondState = getBondState();
        if (bondState == 11) {
            this.mDevice.cancelBondProcess();
        }
        if (bondState != 10) {
            BluetoothDevice bluetoothDevice = this.mDevice;
            if (bluetoothDevice.isTwsPlusDevice() && (twsPeerDevice = getTwsPeerDevice()) != null && twsPeerDevice.removeBond()) {
                Log.d(TAG, "Command sent successfully:REMOVE_BOND " + twsPeerDevice.getName());
            }
            if (bluetoothDevice != null) {
                this.mUnpairing = true;
                if (bluetoothDevice.removeBond()) {
                    releaseLruCache();
                    Log.d(TAG, "Command sent successfully:REMOVE_BOND " + describe((LocalBluetoothProfile) null));
                }
            }
        }
    }

    public int getProfileConnectionState(LocalBluetoothProfile localBluetoothProfile) {
        if (localBluetoothProfile != null) {
            return localBluetoothProfile.getConnectionStatus(this.mDevice);
        }
        return 0;
    }

    private void fillData() {
        updateProfiles();
        fetchActiveDevices();
        migratePhonebookPermissionChoice();
        migrateMessagePermissionChoice();
        setLeAudioEnabled();
        mo28170xa3ba5194();
    }

    public BluetoothDevice getDevice() {
        return this.mDevice;
    }

    public String getAddress() {
        return this.mDevice.getAddress();
    }

    public String getIdentityAddress() {
        String identityAddress = this.mDevice.getIdentityAddress();
        return TextUtils.isEmpty(identityAddress) ? getAddress() : identityAddress;
    }

    public String getName() {
        String alias = this.mDevice.getAlias();
        return TextUtils.isEmpty(alias) ? getAddress() : alias;
    }

    public void setName(String str) {
        if (str != null && !TextUtils.equals(str, getName())) {
            this.mDevice.setAlias(str);
            mo28170xa3ba5194();
        }
    }

    public boolean setActive() {
        boolean z;
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        if (a2dpProfile == null || !isConnectedProfile(a2dpProfile) || !a2dpProfile.setActiveDevice(getDevice())) {
            z = false;
        } else {
            Log.i(TAG, "OnPreferenceClickListener: A2DP active device=" + this);
            z = true;
        }
        HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
        if (headsetProfile != null && isConnectedProfile(headsetProfile) && headsetProfile.setActiveDevice(getDevice())) {
            Log.i(TAG, "OnPreferenceClickListener: Headset active device=" + this);
            z = true;
        }
        HearingAidProfile hearingAidProfile = this.mProfileManager.getHearingAidProfile();
        if (hearingAidProfile != null && isConnectedProfile(hearingAidProfile) && hearingAidProfile.setActiveDevice(getDevice())) {
            Log.i(TAG, "OnPreferenceClickListener: Hearing Aid active device=" + this);
            z = true;
        }
        LeAudioProfile leAudioProfile = this.mProfileManager.getLeAudioProfile();
        if (leAudioProfile == null || !isConnectedProfile(leAudioProfile) || !leAudioProfile.setActiveDevice(getDevice())) {
            return z;
        }
        Log.i(TAG, "OnPreferenceClickListener: LeAudio active device=" + this);
        return true;
    }

    /* access modifiers changed from: package-private */
    public void refreshName() {
        Log.d(TAG, "Device name: " + getName());
        mo28170xa3ba5194();
    }

    public boolean hasHumanReadableName() {
        return !TextUtils.isEmpty(this.mDevice.getAlias());
    }

    public int getBatteryLevel() {
        return this.mDevice.getBatteryLevel();
    }

    /* access modifiers changed from: package-private */
    public void refresh() {
        ThreadUtils.postOnBackgroundThread((Runnable) new CachedBluetoothDevice$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$refresh$1$com-android-settingslib-bluetooth-CachedBluetoothDevice */
    public /* synthetic */ void mo28171xc94e5a95() {
        Uri uriMetaData;
        if (BluetoothUtils.isAdvancedDetailsHeader(this.mDevice) && (uriMetaData = BluetoothUtils.getUriMetaData(getDevice(), 5)) != null && this.mDrawableCache.get(uriMetaData.toString()) == null) {
            this.mDrawableCache.put(uriMetaData.toString(), (BitmapDrawable) BluetoothUtils.getBtDrawableWithDescription(this.mContext, this).first);
        }
        ThreadUtils.postOnMainThread(new CachedBluetoothDevice$$ExternalSyntheticLambda0(this));
    }

    public void setJustDiscovered(boolean z) {
        if (this.mJustDiscovered != z) {
            this.mJustDiscovered = z;
            mo28170xa3ba5194();
        }
    }

    public int getBondState() {
        return this.mDevice.getBondState();
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0055  */
    /* JADX WARNING: Removed duplicated region for block: B:29:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActiveDeviceChanged(boolean r4, int r5) {
        /*
            r3 = this;
            r0 = 1
            r1 = 0
            if (r5 == r0) goto L_0x004a
            r2 = 2
            if (r5 == r2) goto L_0x0041
            r2 = 21
            if (r5 == r2) goto L_0x0038
            r2 = 22
            if (r5 == r2) goto L_0x002f
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r2 = "onActiveDeviceChanged: unknown profile "
            r0.<init>((java.lang.String) r2)
            java.lang.StringBuilder r5 = r0.append((int) r5)
            java.lang.String r0 = " isActive "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r0)
            java.lang.StringBuilder r4 = r5.append((boolean) r4)
            java.lang.String r4 = r4.toString()
            java.lang.String r5 = "CachedBluetoothDevice"
            android.util.Log.w(r5, r4)
            goto L_0x0053
        L_0x002f:
            boolean r5 = r3.mIsActiveDeviceLeAudio
            if (r5 == r4) goto L_0x0034
            goto L_0x0035
        L_0x0034:
            r0 = r1
        L_0x0035:
            r3.mIsActiveDeviceLeAudio = r4
            goto L_0x0052
        L_0x0038:
            boolean r5 = r3.mIsActiveDeviceHearingAid
            if (r5 == r4) goto L_0x003d
            goto L_0x003e
        L_0x003d:
            r0 = r1
        L_0x003e:
            r3.mIsActiveDeviceHearingAid = r4
            goto L_0x0052
        L_0x0041:
            boolean r5 = r3.mIsActiveDeviceA2dp
            if (r5 == r4) goto L_0x0046
            goto L_0x0047
        L_0x0046:
            r0 = r1
        L_0x0047:
            r3.mIsActiveDeviceA2dp = r4
            goto L_0x0052
        L_0x004a:
            boolean r5 = r3.mIsActiveDeviceHeadset
            if (r5 == r4) goto L_0x004f
            goto L_0x0050
        L_0x004f:
            r0 = r1
        L_0x0050:
            r3.mIsActiveDeviceHeadset = r4
        L_0x0052:
            r1 = r0
        L_0x0053:
            if (r1 == 0) goto L_0x0058
            r3.mo28170xa3ba5194()
        L_0x0058:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.onActiveDeviceChanged(boolean, int):void");
    }

    /* access modifiers changed from: package-private */
    public void onAudioModeChanged() {
        mo28170xa3ba5194();
    }

    public boolean isActiveDevice(int i) {
        if (i == 1) {
            return this.mIsActiveDeviceHeadset;
        }
        if (i == 2) {
            return this.mIsActiveDeviceA2dp;
        }
        if (i == 21) {
            return this.mIsActiveDeviceHearingAid;
        }
        if (i == 22) {
            return this.mIsActiveDeviceLeAudio;
        }
        Log.w(TAG, "getActiveDevice: unknown profile " + i);
        return false;
    }

    /* access modifiers changed from: package-private */
    public void setRssi(short s) {
        if (this.mRssi != s) {
            this.mRssi = s;
            mo28170xa3ba5194();
        }
    }

    public boolean isConnected() {
        synchronized (this.mProfileLock) {
            for (LocalBluetoothProfile profileConnectionState : this.mProfiles) {
                if (getProfileConnectionState(profileConnectionState) == 2) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean isConnectedProfile(LocalBluetoothProfile localBluetoothProfile) {
        return getProfileConnectionState(localBluetoothProfile) == 2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0020, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x002c, code lost:
        return r3;
     */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0021 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:6:0x0010  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isBusy() {
        /*
            r5 = this;
            java.lang.Object r0 = r5.mProfileLock
            monitor-enter(r0)
            java.util.Collection<com.android.settingslib.bluetooth.LocalBluetoothProfile> r1 = r5.mProfiles     // Catch:{ all -> 0x002d }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x002d }
        L_0x0009:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x002d }
            r3 = 1
            if (r2 == 0) goto L_0x0021
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x002d }
            com.android.settingslib.bluetooth.LocalBluetoothProfile r2 = (com.android.settingslib.bluetooth.LocalBluetoothProfile) r2     // Catch:{ all -> 0x002d }
            int r2 = r5.getProfileConnectionState(r2)     // Catch:{ all -> 0x002d }
            if (r2 == r3) goto L_0x001f
            r4 = 3
            if (r2 != r4) goto L_0x0009
        L_0x001f:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r3
        L_0x0021:
            int r5 = r5.getBondState()     // Catch:{ all -> 0x002d }
            r1 = 11
            if (r5 != r1) goto L_0x002a
            goto L_0x002b
        L_0x002a:
            r3 = 0
        L_0x002b:
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            return r3
        L_0x002d:
            r5 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002d }
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.isBusy():boolean");
    }

    private boolean updateProfiles() {
        ParcelUuid[] uuids = this.mDevice.getUuids();
        if (uuids == null) {
            return false;
        }
        List uuidsList = this.mLocalAdapter.getUuidsList();
        ParcelUuid[] parcelUuidArr = new ParcelUuid[uuidsList.size()];
        uuidsList.toArray(parcelUuidArr);
        processPhonebookAccess();
        synchronized (this.mProfileLock) {
            this.mProfileManager.updateProfiles(uuids, parcelUuidArr, this.mProfiles, this.mRemovedProfiles, this.mLocalNapRoleConnected, this.mDevice);
        }
        Log.d(TAG, "updating profiles for " + this.mDevice.getAnonymizedAddress());
        BluetoothClass bluetoothClass = this.mDevice.getBluetoothClass();
        if (bluetoothClass != null) {
            Log.v(TAG, "Class: " + bluetoothClass.toString());
        }
        Log.v(TAG, "UUID:");
        int length = uuids.length;
        for (int i = 0; i < length; i++) {
            Log.v(TAG, "  " + uuids[i]);
        }
        return true;
    }

    private void fetchActiveDevices() {
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        if (a2dpProfile != null) {
            this.mIsActiveDeviceA2dp = this.mDevice.equals(a2dpProfile.getActiveDevice());
        }
        HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
        if (headsetProfile != null) {
            this.mIsActiveDeviceHeadset = this.mDevice.equals(headsetProfile.getActiveDevice());
        }
        HearingAidProfile hearingAidProfile = this.mProfileManager.getHearingAidProfile();
        if (hearingAidProfile != null) {
            this.mIsActiveDeviceHearingAid = hearingAidProfile.getActiveDevices().contains(this.mDevice);
        }
        LeAudioProfile leAudioProfile = this.mProfileManager.getLeAudioProfile();
        if (leAudioProfile != null) {
            this.mIsActiveDeviceLeAudio = leAudioProfile.getActiveDevices().contains(this.mDevice);
        }
    }

    /* access modifiers changed from: package-private */
    public void onUuidChanged() {
        updateProfiles();
        ParcelUuid[] uuids = this.mDevice.getUuids();
        boolean contains = ArrayUtils.contains(uuids, BluetoothUuid.HOGP);
        long j = TemperatureController.DELAY_MS;
        if (!contains) {
            if (ArrayUtils.contains(uuids, BluetoothUuid.HEARING_AID)) {
                j = MAX_HEARING_AIDS_DELAY_FOR_AUTO_CONNECT;
            } else if (!ArrayUtils.contains(uuids, BluetoothUuid.LE_AUDIO)) {
                j = 5000;
            }
        }
        Log.d(TAG, "onUuidChanged: Time since last connect=" + (SystemClock.elapsedRealtime() - this.mConnectAttempted));
        if (this.mConnectAttempted + j > SystemClock.elapsedRealtime()) {
            Log.d(TAG, "onUuidChanged: triggering connectDevice");
            connectDevice();
        }
        mo28170xa3ba5194();
    }

    /* access modifiers changed from: package-private */
    public void onBondingStateChanged(int i) {
        if (i == 10) {
            synchronized (this.mProfileLock) {
                this.mProfiles.clear();
            }
            this.mDevice.setPhonebookAccessPermission(0);
            this.mDevice.setMessageAccessPermission(0);
            this.mDevice.setSimAccessPermission(0);
        }
        refresh();
        if (i == 12) {
            boolean isBondingInitiatedLocally = this.mDevice.isBondingInitiatedLocally();
            Log.w(TAG, "mIsBondingInitiatedLocally" + isBondingInitiatedLocally);
            if (isBondingInitiatedLocally) {
                connect();
            }
        }
    }

    public BluetoothClass getBtClass() {
        return this.mDevice.getBluetoothClass();
    }

    public List<LocalBluetoothProfile> getProfiles() {
        return new ArrayList(this.mProfiles);
    }

    public boolean isBASeeker() {
        if (this.mDevice == null) {
            Log.e(TAG, "isBASeeker: mDevice is null");
            return false;
        }
        try {
            return ((Boolean) Class.forName("com.android.settingslib.bluetooth.BCProfile").getDeclaredMethod("isBASeeker", BluetoothDevice.class).invoke((Object) null, this.mDevice)).booleanValue();
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<LocalBluetoothProfile> getConnectableProfiles() {
        Class<?> cls;
        ArrayList arrayList = new ArrayList();
        try {
            cls = Class.forName("com.android.settingslib.bluetooth.BCProfile");
        } catch (ClassNotFoundException unused) {
            Log.e(TAG, "no BCProfileClass: exists");
            cls = null;
        }
        synchronized (this.mProfileLock) {
            for (LocalBluetoothProfile next : this.mProfiles) {
                if (cls == null || !cls.isInstance(next)) {
                    if (next.accessProfileEnabled()) {
                        arrayList.add(next);
                    }
                } else if (isBASeeker()) {
                    arrayList.add(next);
                } else {
                    Log.d(TAG, "BC profile is not enabled for" + this.mDevice);
                }
            }
        }
        return arrayList;
    }

    public List<LocalBluetoothProfile> getRemovedProfiles() {
        return new ArrayList(this.mRemovedProfiles);
    }

    public void registerCallback(Callback callback) {
        this.mCallbacks.add(callback);
    }

    public void unregisterCallback(Callback callback) {
        this.mCallbacks.remove(callback);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: dispatchAttributesChanged */
    public void mo28170xa3ba5194() {
        for (Callback onDeviceAttributesChanged : this.mCallbacks) {
            onDeviceAttributesChanged.onDeviceAttributesChanged();
        }
    }

    public String toString() {
        return this.mDevice.toString();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CachedBluetoothDevice)) {
            return false;
        }
        return this.mDevice.equals(((CachedBluetoothDevice) obj).mDevice);
    }

    public int hashCode() {
        return this.mDevice.getAddress().hashCode();
    }

    public int compareTo(CachedBluetoothDevice cachedBluetoothDevice) {
        int i = (cachedBluetoothDevice.isConnected() ? 1 : 0) - (isConnected() ? 1 : 0);
        if (i != 0) {
            return i;
        }
        int i2 = 1;
        int i3 = cachedBluetoothDevice.getBondState() == 12 ? 1 : 0;
        if (getBondState() != 12) {
            i2 = 0;
        }
        int i4 = i3 - i2;
        if (i4 != 0) {
            return i4;
        }
        int i5 = (cachedBluetoothDevice.mJustDiscovered ? 1 : 0) - (this.mJustDiscovered ? 1 : 0);
        if (i5 != 0) {
            return i5;
        }
        int i6 = cachedBluetoothDevice.mRssi - this.mRssi;
        if (i6 != 0) {
            return i6;
        }
        return getName().compareTo(cachedBluetoothDevice.getName());
    }

    private void migratePhonebookPermissionChoice() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("bluetooth_phonebook_permission", 0);
        if (sharedPreferences.contains(this.mDevice.getAddress())) {
            if (this.mDevice.getPhonebookAccessPermission() == 0) {
                int i = sharedPreferences.getInt(this.mDevice.getAddress(), 0);
                if (i == 1) {
                    this.mDevice.setPhonebookAccessPermission(1);
                } else if (i == 2) {
                    this.mDevice.setPhonebookAccessPermission(2);
                }
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(this.mDevice.getAddress());
            edit.commit();
        }
    }

    private void migrateMessagePermissionChoice() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences("bluetooth_message_permission", 0);
        if (sharedPreferences.contains(this.mDevice.getAddress())) {
            if (this.mDevice.getMessageAccessPermission() == 0) {
                int i = sharedPreferences.getInt(this.mDevice.getAddress(), 0);
                if (i == 1) {
                    this.mDevice.setMessageAccessPermission(1);
                } else if (i == 2) {
                    this.mDevice.setMessageAccessPermission(2);
                }
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(this.mDevice.getAddress());
            edit.commit();
        }
    }

    private void processPhonebookAccess() {
        if (this.mDevice.getBondState() == 12 && BluetoothUuid.containsAnyUuid(this.mDevice.getUuids(), PbapServerProfile.PBAB_CLIENT_UUIDS)) {
            BluetoothClass bluetoothClass = this.mDevice.getBluetoothClass();
            if (this.mDevice.getPhonebookAccessPermission() == 0 && bluetoothClass != null) {
                if (bluetoothClass.getDeviceClass() == 1032 || bluetoothClass.getDeviceClass() == 1028) {
                    EventLog.writeEvent(1397638484, new Object[]{"138529441", -1, ""});
                }
            }
        }
    }

    public int getMaxConnectionState() {
        int i;
        synchronized (this.mProfileLock) {
            i = 0;
            for (LocalBluetoothProfile profileConnectionState : getProfiles()) {
                int profileConnectionState2 = getProfileConnectionState(profileConnectionState);
                if (profileConnectionState2 > i) {
                    i = profileConnectionState2;
                }
            }
        }
        return i;
    }

    public String getConnectionSummary() {
        return getConnectionSummary(false);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0138, code lost:
        if (r15.isConnected() == false) goto L_0x013d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:101:0x013a, code lost:
        r11 = com.android.settingslib.C1757R.string.bluetooth_hearing_aid_left_and_right_active;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:102:0x013d, code lost:
        r15 = r14.mDeviceSide;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:103:0x013f, code lost:
        if (r15 != 0) goto L_0x0144;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:104:0x0141, code lost:
        r11 = com.android.settingslib.C1757R.string.bluetooth_hearing_aid_left_active;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:105:0x0144, code lost:
        if (r15 != 1) goto L_0x0149;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:106:0x0146, code lost:
        r11 = com.android.settingslib.C1757R.string.bluetooth_hearing_aid_right_active;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:107:0x0149, code lost:
        r11 = com.android.settingslib.C1757R.string.bluetooth_active_no_battery_level;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:108:0x014c, code lost:
        r4 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:110:0x014f, code lost:
        if (r11 != com.android.settingslib.C1757R.string.bluetooth_pairing) goto L_0x0159;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:112:0x0155, code lost:
        if (getBondState() != 11) goto L_0x0158;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:113:0x0158, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:115:0x015d, code lost:
        if (isTwsBatteryAvailable(r9, r4) == false) goto L_0x0174;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:117:0x0173, code lost:
        return r14.mContext.getString(r11, new java.lang.Object[]{com.android.settingslib.Utils.formatPercentage(r9), com.android.settingslib.Utils.formatPercentage(r4)});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:119:0x017e, code lost:
        return r14.mContext.getString(r11, new java.lang.Object[]{r0});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0079, code lost:
        r9 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0081, code lost:
        if (r14.mDevice.isTwsPlusDevice() == false) goto L_0x00b7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0083, code lost:
        r0 = r14.mTwspBatteryState;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x0085, code lost:
        if (r0 == -1) goto L_0x00b7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x0089, code lost:
        if (r14.mTwspBatteryLevel == -1) goto L_0x00b7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x008d, code lost:
        if (r0 != 1) goto L_0x0092;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x008f, code lost:
        r0 = "Charging, ";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0092, code lost:
        r0 = "Discharging, ";
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x0094, code lost:
        r0 = "TWSP: ".concat(r0).concat(com.android.settingslib.Utils.formatPercentage(r14.mTwspBatteryLevel));
        android.util.Log.i(TAG, "UI string" + r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00b7, code lost:
        r0 = getBatteryLevel();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00bb, code lost:
        if (r0 <= -1) goto L_0x00c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:57:0x00bd, code lost:
        r0 = com.android.settingslib.Utils.formatPercentage(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00c2, code lost:
        r0 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x00c3, code lost:
        r11 = com.android.settingslib.C1757R.string.bluetooth_pairing;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00c7, code lost:
        if (r4 == false) goto L_0x014c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:62:0x00d0, code lost:
        if (com.android.settingslib.bluetooth.BluetoothUtils.getBooleanMetaData(r14.mDevice, 6) == false) goto L_0x00e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00d2, code lost:
        r9 = com.android.settingslib.bluetooth.BluetoothUtils.getIntMetaData(r14.mDevice, 10);
        r4 = com.android.settingslib.bluetooth.BluetoothUtils.getIntMetaData(r14.mDevice, 11);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x00e1, code lost:
        r4 = -1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00e6, code lost:
        if (isTwsBatteryAvailable(r9, r4) == false) goto L_0x00eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00e8, code lost:
        r11 = com.android.settingslib.C1757R.string.bluetooth_battery_level_untethered;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00eb, code lost:
        if (r0 == null) goto L_0x00ef;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00ed, code lost:
        r11 = com.android.settingslib.C1757R.string.bluetooth_battery_level;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x00ef, code lost:
        if (r5 != false) goto L_0x00f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00f1, code lost:
        if (r6 != false) goto L_0x00f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x00f3, code lost:
        if (r7 != false) goto L_0x00f7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00f5, code lost:
        if (r8 == false) goto L_0x014d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x00f7, code lost:
        r5 = com.android.settingslib.Utils.isAudioModeOngoingCall(r14.mContext);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x00ff, code lost:
        if (r14.mIsActiveDeviceHearingAid != false) goto L_0x0111;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x0103, code lost:
        if (r14.mIsActiveDeviceHeadset == false) goto L_0x0107;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x0105, code lost:
        if (r5 != false) goto L_0x0111;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0109, code lost:
        if (r14.mIsActiveDeviceA2dp == false) goto L_0x010d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x010b, code lost:
        if (r5 == false) goto L_0x0111;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x010f, code lost:
        if (r14.mIsActiveDeviceLeAudio == false) goto L_0x0126;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0115, code lost:
        if (isTwsBatteryAvailable(r9, r4) == false) goto L_0x011c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x0117, code lost:
        if (r15 != false) goto L_0x011c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:87:0x0119, code lost:
        r15 = com.android.settingslib.C1757R.string.bluetooth_active_battery_level_untethered;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x011c, code lost:
        if (r0 == null) goto L_0x0123;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:89:0x011e, code lost:
        if (r15 != false) goto L_0x0123;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x0120, code lost:
        r15 = com.android.settingslib.C1757R.string.bluetooth_active_battery_level;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:91:0x0123, code lost:
        r15 = com.android.settingslib.C1757R.string.bluetooth_active_no_battery_level;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0125, code lost:
        r11 = r15;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0128, code lost:
        if (r14.mIsActiveDeviceHearingAid == false) goto L_0x014d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:96:0x012c, code lost:
        if (r11 != com.android.settingslib.C1757R.string.bluetooth_active_no_battery_level) goto L_0x014d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x012e, code lost:
        r15 = getSubDevice();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0132, code lost:
        if (r15 == null) goto L_0x013d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getConnectionSummary(boolean r15) {
        /*
            r14 = this;
            boolean r0 = r14.isProfileConnectedFail()
            if (r0 == 0) goto L_0x0015
            boolean r0 = r14.isConnected()
            if (r0 == 0) goto L_0x0015
            android.content.Context r14 = r14.mContext
            int r15 = com.android.settingslib.C1757R.string.profile_connect_timeout_subtext
            java.lang.String r14 = r14.getString(r15)
            return r14
        L_0x0015:
            java.lang.Object r0 = r14.mProfileLock
            monitor-enter(r0)
            java.util.List r1 = r14.getProfiles()     // Catch:{ all -> 0x017f }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x017f }
            r2 = 0
            r3 = 1
            r4 = r2
            r5 = r3
            r6 = r5
            r7 = r6
            r8 = r7
        L_0x0027:
            boolean r9 = r1.hasNext()     // Catch:{ all -> 0x017f }
            r10 = 2
            if (r9 == 0) goto L_0x0078
            java.lang.Object r9 = r1.next()     // Catch:{ all -> 0x017f }
            com.android.settingslib.bluetooth.LocalBluetoothProfile r9 = (com.android.settingslib.bluetooth.LocalBluetoothProfile) r9     // Catch:{ all -> 0x017f }
            int r11 = r14.getProfileConnectionState(r9)     // Catch:{ all -> 0x017f }
            if (r11 == 0) goto L_0x0050
            if (r11 == r3) goto L_0x0044
            if (r11 == r10) goto L_0x0042
            r9 = 3
            if (r11 == r9) goto L_0x0044
            goto L_0x0027
        L_0x0042:
            r4 = r3
            goto L_0x0027
        L_0x0044:
            android.content.Context r14 = r14.mContext     // Catch:{ all -> 0x017f }
            int r15 = com.android.settingslib.bluetooth.BluetoothUtils.getConnectionStateSummary(r11)     // Catch:{ all -> 0x017f }
            java.lang.String r14 = r14.getString(r15)     // Catch:{ all -> 0x017f }
            monitor-exit(r0)     // Catch:{ all -> 0x017f }
            return r14
        L_0x0050:
            boolean r10 = r9.isProfileReady()     // Catch:{ all -> 0x017f }
            if (r10 == 0) goto L_0x0027
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.A2dpProfile     // Catch:{ all -> 0x017f }
            if (r10 != 0) goto L_0x0076
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.A2dpSinkProfile     // Catch:{ all -> 0x017f }
            if (r10 == 0) goto L_0x005f
            goto L_0x0076
        L_0x005f:
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.HeadsetProfile     // Catch:{ all -> 0x017f }
            if (r10 != 0) goto L_0x0074
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.HfpClientProfile     // Catch:{ all -> 0x017f }
            if (r10 == 0) goto L_0x0068
            goto L_0x0074
        L_0x0068:
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.HearingAidProfile     // Catch:{ all -> 0x017f }
            if (r10 == 0) goto L_0x006e
            r7 = r2
            goto L_0x0027
        L_0x006e:
            boolean r9 = r9 instanceof com.android.settingslib.bluetooth.LeAudioProfile     // Catch:{ all -> 0x017f }
            if (r9 == 0) goto L_0x0027
            r8 = r2
            goto L_0x0027
        L_0x0074:
            r6 = r2
            goto L_0x0027
        L_0x0076:
            r5 = r2
            goto L_0x0027
        L_0x0078:
            monitor-exit(r0)     // Catch:{ all -> 0x017f }
            android.bluetooth.BluetoothDevice r0 = r14.mDevice
            boolean r0 = r0.isTwsPlusDevice()
            r1 = 0
            r9 = -1
            if (r0 == 0) goto L_0x00b7
            int r0 = r14.mTwspBatteryState
            if (r0 == r9) goto L_0x00b7
            int r11 = r14.mTwspBatteryLevel
            if (r11 == r9) goto L_0x00b7
            java.lang.String r11 = "TWSP: "
            if (r0 != r3) goto L_0x0092
            java.lang.String r0 = "Charging, "
            goto L_0x0094
        L_0x0092:
            java.lang.String r0 = "Discharging, "
        L_0x0094:
            java.lang.String r0 = r11.concat(r0)
            int r11 = r14.mTwspBatteryLevel
            java.lang.String r11 = com.android.settingslib.Utils.formatPercentage((int) r11)
            java.lang.String r0 = r0.concat(r11)
            java.lang.String r11 = "CachedBluetoothDevice"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            java.lang.String r13 = "UI string"
            r12.<init>((java.lang.String) r13)
            java.lang.StringBuilder r12 = r12.append((java.lang.String) r0)
            java.lang.String r12 = r12.toString()
            android.util.Log.i(r11, r12)
            goto L_0x00c3
        L_0x00b7:
            int r0 = r14.getBatteryLevel()
            if (r0 <= r9) goto L_0x00c2
            java.lang.String r0 = com.android.settingslib.Utils.formatPercentage((int) r0)
            goto L_0x00c3
        L_0x00c2:
            r0 = r1
        L_0x00c3:
            int r11 = com.android.settingslib.C1757R.string.bluetooth_pairing
            r12 = 11
            if (r4 == 0) goto L_0x014c
            android.bluetooth.BluetoothDevice r4 = r14.mDevice
            r13 = 6
            boolean r4 = com.android.settingslib.bluetooth.BluetoothUtils.getBooleanMetaData(r4, r13)
            if (r4 == 0) goto L_0x00e1
            android.bluetooth.BluetoothDevice r4 = r14.mDevice
            r9 = 10
            int r9 = com.android.settingslib.bluetooth.BluetoothUtils.getIntMetaData(r4, r9)
            android.bluetooth.BluetoothDevice r4 = r14.mDevice
            int r4 = com.android.settingslib.bluetooth.BluetoothUtils.getIntMetaData(r4, r12)
            goto L_0x00e2
        L_0x00e1:
            r4 = r9
        L_0x00e2:
            boolean r13 = r14.isTwsBatteryAvailable(r9, r4)
            if (r13 == 0) goto L_0x00eb
            int r11 = com.android.settingslib.C1757R.string.bluetooth_battery_level_untethered
            goto L_0x00ef
        L_0x00eb:
            if (r0 == 0) goto L_0x00ef
            int r11 = com.android.settingslib.C1757R.string.bluetooth_battery_level
        L_0x00ef:
            if (r5 != 0) goto L_0x00f7
            if (r6 != 0) goto L_0x00f7
            if (r7 != 0) goto L_0x00f7
            if (r8 == 0) goto L_0x014d
        L_0x00f7:
            android.content.Context r5 = r14.mContext
            boolean r5 = com.android.settingslib.Utils.isAudioModeOngoingCall(r5)
            boolean r6 = r14.mIsActiveDeviceHearingAid
            if (r6 != 0) goto L_0x0111
            boolean r6 = r14.mIsActiveDeviceHeadset
            if (r6 == 0) goto L_0x0107
            if (r5 != 0) goto L_0x0111
        L_0x0107:
            boolean r6 = r14.mIsActiveDeviceA2dp
            if (r6 == 0) goto L_0x010d
            if (r5 == 0) goto L_0x0111
        L_0x010d:
            boolean r5 = r14.mIsActiveDeviceLeAudio
            if (r5 == 0) goto L_0x0126
        L_0x0111:
            boolean r5 = r14.isTwsBatteryAvailable(r9, r4)
            if (r5 == 0) goto L_0x011c
            if (r15 != 0) goto L_0x011c
            int r15 = com.android.settingslib.C1757R.string.bluetooth_active_battery_level_untethered
            goto L_0x0125
        L_0x011c:
            if (r0 == 0) goto L_0x0123
            if (r15 != 0) goto L_0x0123
            int r15 = com.android.settingslib.C1757R.string.bluetooth_active_battery_level
            goto L_0x0125
        L_0x0123:
            int r15 = com.android.settingslib.C1757R.string.bluetooth_active_no_battery_level
        L_0x0125:
            r11 = r15
        L_0x0126:
            boolean r15 = r14.mIsActiveDeviceHearingAid
            if (r15 == 0) goto L_0x014d
            int r15 = com.android.settingslib.C1757R.string.bluetooth_active_no_battery_level
            if (r11 != r15) goto L_0x014d
            com.android.settingslib.bluetooth.CachedBluetoothDevice r15 = r14.getSubDevice()
            if (r15 == 0) goto L_0x013d
            boolean r15 = r15.isConnected()
            if (r15 == 0) goto L_0x013d
            int r11 = com.android.settingslib.C1757R.string.bluetooth_hearing_aid_left_and_right_active
            goto L_0x014d
        L_0x013d:
            int r15 = r14.mDeviceSide
            if (r15 != 0) goto L_0x0144
            int r11 = com.android.settingslib.C1757R.string.bluetooth_hearing_aid_left_active
            goto L_0x014d
        L_0x0144:
            if (r15 != r3) goto L_0x0149
            int r11 = com.android.settingslib.C1757R.string.bluetooth_hearing_aid_right_active
            goto L_0x014d
        L_0x0149:
            int r11 = com.android.settingslib.C1757R.string.bluetooth_active_no_battery_level
            goto L_0x014d
        L_0x014c:
            r4 = r9
        L_0x014d:
            int r15 = com.android.settingslib.C1757R.string.bluetooth_pairing
            if (r11 != r15) goto L_0x0159
            int r15 = r14.getBondState()
            if (r15 != r12) goto L_0x0158
            goto L_0x0159
        L_0x0158:
            return r1
        L_0x0159:
            boolean r15 = r14.isTwsBatteryAvailable(r9, r4)
            if (r15 == 0) goto L_0x0174
            android.content.Context r14 = r14.mContext
            java.lang.Object[] r15 = new java.lang.Object[r10]
            java.lang.String r0 = com.android.settingslib.Utils.formatPercentage((int) r9)
            r15[r2] = r0
            java.lang.String r0 = com.android.settingslib.Utils.formatPercentage((int) r4)
            r15[r3] = r0
            java.lang.String r14 = r14.getString(r11, r15)
            return r14
        L_0x0174:
            android.content.Context r14 = r14.mContext
            java.lang.Object[] r15 = new java.lang.Object[r3]
            r15[r2] = r0
            java.lang.String r14 = r14.getString(r11, r15)
            return r14
        L_0x017f:
            r14 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x017f }
            throw r14
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.getConnectionSummary(boolean):java.lang.String");
    }

    private boolean isProfileConnectedFail() {
        return this.mIsA2dpProfileConnectedFail || this.mIsHearingAidProfileConnectedFail || (!isConnectedSapDevice() && this.mIsHeadsetProfileConnectedFail) || this.mIsLeAudioProfileConnectedFail;
    }

    public String getCarConnectionSummary() {
        return getCarConnectionSummary(false);
    }

    public String getCarConnectionSummary(boolean z) {
        return getCarConnectionSummary(z, true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:117:?, code lost:
        return r0.mContext.getString(com.android.settingslib.C1757R.string.bluetooth_disconnected);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:118:?, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x007a, code lost:
        r1 = getBatteryLevel();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x0080, code lost:
        if (r1 <= -1) goto L_0x0087;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0082, code lost:
        r1 = com.android.settingslib.Utils.formatPercentage(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x0087, code lost:
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x0088, code lost:
        r2 = r0.mContext.getResources().getStringArray(com.android.settingslib.C1757R.array.bluetooth_audio_active_device_summaries);
        r13 = r2[0];
        r14 = r0.mIsActiveDeviceA2dp;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x0098, code lost:
        if (r14 == false) goto L_0x00a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x009c, code lost:
        if (r0.mIsActiveDeviceHeadset == false) goto L_0x00a1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x009e, code lost:
        r10 = r2[1];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00a1, code lost:
        if (r14 == false) goto L_0x00a5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00a3, code lost:
        r13 = r2[2];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x00a7, code lost:
        if (r0.mIsActiveDeviceHeadset == false) goto L_0x00ac;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00a9, code lost:
        r10 = r2[3];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00ac, code lost:
        r10 = r13;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00ad, code lost:
        if (r4 != false) goto L_0x00c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x00b1, code lost:
        if (r0.mIsActiveDeviceHearingAid == false) goto L_0x00c2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x00c1, code lost:
        return r0.mContext.getString(com.android.settingslib.C1757R.string.bluetooth_connected, new java.lang.Object[]{r2[1]});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:61:0x00c2, code lost:
        if (r5 != false) goto L_0x00d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:0x00c6, code lost:
        if (r0.mIsActiveDeviceLeAudio == false) goto L_0x00d7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00d6, code lost:
        return r0.mContext.getString(com.android.settingslib.C1757R.string.bluetooth_connected, new java.lang.Object[]{r2[1]});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x00d7, code lost:
        if (r6 == false) goto L_0x0159;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:67:0x00d9, code lost:
        if (r7 == false) goto L_0x00fb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x00db, code lost:
        if (r8 == false) goto L_0x00fb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x00dd, code lost:
        if (r1 == null) goto L_0x00ee;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x00ed, code lost:
        return r0.mContext.getString(com.android.settingslib.C1757R.string.bluetooth_connected_no_headset_no_a2dp_battery_level, new java.lang.Object[]{r1, r10});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:73:0x00fa, code lost:
        return r0.mContext.getString(com.android.settingslib.C1757R.string.bluetooth_connected_no_headset_no_a2dp, new java.lang.Object[]{r10});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x00fb, code lost:
        if (r7 == false) goto L_0x011b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x00fd, code lost:
        if (r1 == null) goto L_0x010e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:77:0x010d, code lost:
        return r0.mContext.getString(com.android.settingslib.C1757R.string.bluetooth_connected_no_a2dp_battery_level, new java.lang.Object[]{r1, r10});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x011a, code lost:
        return r0.mContext.getString(com.android.settingslib.C1757R.string.bluetooth_connected_no_a2dp, new java.lang.Object[]{r10});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x011b, code lost:
        if (r8 == false) goto L_0x013b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:81:0x011d, code lost:
        if (r1 == null) goto L_0x012e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x012d, code lost:
        return r0.mContext.getString(com.android.settingslib.C1757R.string.bluetooth_connected_no_headset_battery_level, new java.lang.Object[]{r1, r10});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x013a, code lost:
        return r0.mContext.getString(com.android.settingslib.C1757R.string.bluetooth_connected_no_headset, new java.lang.Object[]{r10});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:86:0x013b, code lost:
        if (r1 == null) goto L_0x014c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:88:0x014b, code lost:
        return r0.mContext.getString(com.android.settingslib.C1757R.string.bluetooth_connected_battery_level, new java.lang.Object[]{r1, r10});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:90:0x0158, code lost:
        return r0.mContext.getString(com.android.settingslib.C1757R.string.bluetooth_connected, new java.lang.Object[]{r10});
     */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x015f, code lost:
        if (getBondState() != 11) goto L_0x016a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0169, code lost:
        return r0.mContext.getString(com.android.settingslib.C1757R.string.bluetooth_pairing);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:95:0x016a, code lost:
        if (r18 == false) goto L_?;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getCarConnectionSummary(boolean r17, boolean r18) {
        /*
            r16 = this;
            r0 = r16
            java.lang.Object r1 = r0.mProfileLock
            monitor-enter(r1)
            java.util.List r2 = r16.getProfiles()     // Catch:{ all -> 0x0175 }
            java.util.Iterator r2 = r2.iterator()     // Catch:{ all -> 0x0175 }
            r3 = 0
            r4 = r3
            r5 = r4
            r6 = r5
            r7 = r6
            r8 = r7
        L_0x0013:
            boolean r9 = r2.hasNext()     // Catch:{ all -> 0x0175 }
            r10 = 3
            r11 = 2
            r12 = 1
            if (r9 == 0) goto L_0x0079
            java.lang.Object r9 = r2.next()     // Catch:{ all -> 0x0175 }
            com.android.settingslib.bluetooth.LocalBluetoothProfile r9 = (com.android.settingslib.bluetooth.LocalBluetoothProfile) r9     // Catch:{ all -> 0x0175 }
            int r13 = r0.getProfileConnectionState(r9)     // Catch:{ all -> 0x0175 }
            if (r13 == 0) goto L_0x0051
            if (r13 == r12) goto L_0x0045
            if (r13 == r11) goto L_0x002f
            if (r13 == r10) goto L_0x0045
            goto L_0x0013
        L_0x002f:
            if (r17 == 0) goto L_0x0043
            android.content.Context r0 = r0.mContext     // Catch:{ all -> 0x0175 }
            int r2 = com.android.settingslib.bluetooth.BluetoothUtils.getConnectionStateSummary(r13)     // Catch:{ all -> 0x0175 }
            java.lang.Object[] r4 = new java.lang.Object[r12]     // Catch:{ all -> 0x0175 }
            java.lang.String r5 = ""
            r4[r3] = r5     // Catch:{ all -> 0x0175 }
            java.lang.String r0 = r0.getString(r2, r4)     // Catch:{ all -> 0x0175 }
            monitor-exit(r1)     // Catch:{ all -> 0x0175 }
            return r0
        L_0x0043:
            r6 = r12
            goto L_0x0013
        L_0x0045:
            android.content.Context r0 = r0.mContext     // Catch:{ all -> 0x0175 }
            int r2 = com.android.settingslib.bluetooth.BluetoothUtils.getConnectionStateSummary(r13)     // Catch:{ all -> 0x0175 }
            java.lang.String r0 = r0.getString(r2)     // Catch:{ all -> 0x0175 }
            monitor-exit(r1)     // Catch:{ all -> 0x0175 }
            return r0
        L_0x0051:
            boolean r10 = r9.isProfileReady()     // Catch:{ all -> 0x0175 }
            if (r10 == 0) goto L_0x0013
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.A2dpProfile     // Catch:{ all -> 0x0175 }
            if (r10 != 0) goto L_0x0077
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.A2dpSinkProfile     // Catch:{ all -> 0x0175 }
            if (r10 == 0) goto L_0x0060
            goto L_0x0077
        L_0x0060:
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.HeadsetProfile     // Catch:{ all -> 0x0175 }
            if (r10 != 0) goto L_0x0075
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.HfpClientProfile     // Catch:{ all -> 0x0175 }
            if (r10 == 0) goto L_0x0069
            goto L_0x0075
        L_0x0069:
            boolean r10 = r9 instanceof com.android.settingslib.bluetooth.HearingAidProfile     // Catch:{ all -> 0x0175 }
            if (r10 == 0) goto L_0x006f
            r4 = r12
            goto L_0x0013
        L_0x006f:
            boolean r9 = r9 instanceof com.android.settingslib.bluetooth.LeAudioProfile     // Catch:{ all -> 0x0175 }
            if (r9 == 0) goto L_0x0013
            r5 = r12
            goto L_0x0013
        L_0x0075:
            r8 = r12
            goto L_0x0013
        L_0x0077:
            r7 = r12
            goto L_0x0013
        L_0x0079:
            monitor-exit(r1)     // Catch:{ all -> 0x0175 }
            int r1 = r16.getBatteryLevel()
            r2 = -1
            r9 = 0
            if (r1 <= r2) goto L_0x0087
            java.lang.String r1 = com.android.settingslib.Utils.formatPercentage((int) r1)
            goto L_0x0088
        L_0x0087:
            r1 = r9
        L_0x0088:
            android.content.Context r2 = r0.mContext
            android.content.res.Resources r2 = r2.getResources()
            int r13 = com.android.settingslib.C1757R.array.bluetooth_audio_active_device_summaries
            java.lang.String[] r2 = r2.getStringArray(r13)
            r13 = r2[r3]
            boolean r14 = r0.mIsActiveDeviceA2dp
            if (r14 == 0) goto L_0x00a1
            boolean r15 = r0.mIsActiveDeviceHeadset
            if (r15 == 0) goto L_0x00a1
            r10 = r2[r12]
            goto L_0x00ad
        L_0x00a1:
            if (r14 == 0) goto L_0x00a5
            r13 = r2[r11]
        L_0x00a5:
            boolean r14 = r0.mIsActiveDeviceHeadset
            if (r14 == 0) goto L_0x00ac
            r10 = r2[r10]
            goto L_0x00ad
        L_0x00ac:
            r10 = r13
        L_0x00ad:
            if (r4 != 0) goto L_0x00c2
            boolean r4 = r0.mIsActiveDeviceHearingAid
            if (r4 == 0) goto L_0x00c2
            r1 = r2[r12]
            android.content.Context r0 = r0.mContext
            int r2 = com.android.settingslib.C1757R.string.bluetooth_connected
            java.lang.Object[] r4 = new java.lang.Object[r12]
            r4[r3] = r1
            java.lang.String r0 = r0.getString(r2, r4)
            return r0
        L_0x00c2:
            if (r5 != 0) goto L_0x00d7
            boolean r4 = r0.mIsActiveDeviceLeAudio
            if (r4 == 0) goto L_0x00d7
            r1 = r2[r12]
            android.content.Context r0 = r0.mContext
            int r2 = com.android.settingslib.C1757R.string.bluetooth_connected
            java.lang.Object[] r4 = new java.lang.Object[r12]
            r4[r3] = r1
            java.lang.String r0 = r0.getString(r2, r4)
            return r0
        L_0x00d7:
            if (r6 == 0) goto L_0x0159
            if (r7 == 0) goto L_0x00fb
            if (r8 == 0) goto L_0x00fb
            if (r1 == 0) goto L_0x00ee
            android.content.Context r0 = r0.mContext
            int r2 = com.android.settingslib.C1757R.string.bluetooth_connected_no_headset_no_a2dp_battery_level
            java.lang.Object[] r4 = new java.lang.Object[r11]
            r4[r3] = r1
            r4[r12] = r10
            java.lang.String r0 = r0.getString(r2, r4)
            return r0
        L_0x00ee:
            android.content.Context r0 = r0.mContext
            int r1 = com.android.settingslib.C1757R.string.bluetooth_connected_no_headset_no_a2dp
            java.lang.Object[] r2 = new java.lang.Object[r12]
            r2[r3] = r10
            java.lang.String r0 = r0.getString(r1, r2)
            return r0
        L_0x00fb:
            if (r7 == 0) goto L_0x011b
            if (r1 == 0) goto L_0x010e
            android.content.Context r0 = r0.mContext
            int r2 = com.android.settingslib.C1757R.string.bluetooth_connected_no_a2dp_battery_level
            java.lang.Object[] r4 = new java.lang.Object[r11]
            r4[r3] = r1
            r4[r12] = r10
            java.lang.String r0 = r0.getString(r2, r4)
            return r0
        L_0x010e:
            android.content.Context r0 = r0.mContext
            int r1 = com.android.settingslib.C1757R.string.bluetooth_connected_no_a2dp
            java.lang.Object[] r2 = new java.lang.Object[r12]
            r2[r3] = r10
            java.lang.String r0 = r0.getString(r1, r2)
            return r0
        L_0x011b:
            if (r8 == 0) goto L_0x013b
            if (r1 == 0) goto L_0x012e
            android.content.Context r0 = r0.mContext
            int r2 = com.android.settingslib.C1757R.string.bluetooth_connected_no_headset_battery_level
            java.lang.Object[] r4 = new java.lang.Object[r11]
            r4[r3] = r1
            r4[r12] = r10
            java.lang.String r0 = r0.getString(r2, r4)
            return r0
        L_0x012e:
            android.content.Context r0 = r0.mContext
            int r1 = com.android.settingslib.C1757R.string.bluetooth_connected_no_headset
            java.lang.Object[] r2 = new java.lang.Object[r12]
            r2[r3] = r10
            java.lang.String r0 = r0.getString(r1, r2)
            return r0
        L_0x013b:
            if (r1 == 0) goto L_0x014c
            android.content.Context r0 = r0.mContext
            int r2 = com.android.settingslib.C1757R.string.bluetooth_connected_battery_level
            java.lang.Object[] r4 = new java.lang.Object[r11]
            r4[r3] = r1
            r4[r12] = r10
            java.lang.String r0 = r0.getString(r2, r4)
            return r0
        L_0x014c:
            android.content.Context r0 = r0.mContext
            int r1 = com.android.settingslib.C1757R.string.bluetooth_connected
            java.lang.Object[] r2 = new java.lang.Object[r12]
            r2[r3] = r10
            java.lang.String r0 = r0.getString(r1, r2)
            return r0
        L_0x0159:
            int r1 = r16.getBondState()
            r2 = 11
            if (r1 != r2) goto L_0x016a
            android.content.Context r0 = r0.mContext
            int r1 = com.android.settingslib.C1757R.string.bluetooth_pairing
            java.lang.String r0 = r0.getString(r1)
            return r0
        L_0x016a:
            if (r18 == 0) goto L_0x0174
            android.content.Context r0 = r0.mContext
            int r1 = com.android.settingslib.C1757R.string.bluetooth_disconnected
            java.lang.String r9 = r0.getString(r1)
        L_0x0174:
            return r9
        L_0x0175:
            r0 = move-exception
            monitor-exit(r1)     // Catch:{ all -> 0x0175 }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.CachedBluetoothDevice.getCarConnectionSummary(boolean, boolean):java.lang.String");
    }

    public boolean isConnectedA2dpDevice() {
        A2dpProfile a2dpProfile = this.mProfileManager.getA2dpProfile();
        A2dpSinkProfile a2dpSinkProfile = this.mProfileManager.getA2dpSinkProfile();
        Log.i(TAG, "a2dpProfile :" + a2dpProfile + " a2dpSinkProfile :" + a2dpSinkProfile);
        if (a2dpProfile != null) {
            if (a2dpProfile.getConnectionStatus(this.mDevice) == 2) {
                return true;
            }
            return false;
        } else if (a2dpSinkProfile == null) {
            return false;
        } else {
            if (a2dpSinkProfile.getConnectionStatus(this.mDevice) == 2) {
                return true;
            }
            return false;
        }
    }

    public boolean isConnectedHfpDevice() {
        HeadsetProfile headsetProfile = this.mProfileManager.getHeadsetProfile();
        return headsetProfile != null && headsetProfile.getConnectionStatus(this.mDevice) == 2;
    }

    public boolean isConnectedHearingAidDevice() {
        HearingAidProfile hearingAidProfile = this.mProfileManager.getHearingAidProfile();
        return hearingAidProfile != null && hearingAidProfile.getConnectionStatus(this.mDevice) == 2;
    }

    public boolean isConnectedLeAudioDevice() {
        LeAudioProfile leAudioProfile = this.mProfileManager.getLeAudioProfile();
        return leAudioProfile != null && leAudioProfile.getConnectionStatus(this.mDevice) == 2;
    }

    private boolean isConnectedSapDevice() {
        SapProfile sapProfile = this.mProfileManager.getSapProfile();
        return sapProfile != null && sapProfile.getConnectionStatus(this.mDevice) == 2;
    }

    public CachedBluetoothDevice getSubDevice() {
        return this.mSubDevice;
    }

    public void setSubDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mSubDevice = cachedBluetoothDevice;
    }

    public void switchSubDeviceContent() {
        BluetoothDevice bluetoothDevice = this.mDevice;
        short s = this.mRssi;
        boolean z = this.mJustDiscovered;
        int i = this.mDeviceSide;
        CachedBluetoothDevice cachedBluetoothDevice = this.mSubDevice;
        this.mDevice = cachedBluetoothDevice.mDevice;
        this.mRssi = cachedBluetoothDevice.mRssi;
        this.mJustDiscovered = cachedBluetoothDevice.mJustDiscovered;
        this.mDeviceSide = cachedBluetoothDevice.mDeviceSide;
        cachedBluetoothDevice.mDevice = bluetoothDevice;
        cachedBluetoothDevice.mRssi = s;
        cachedBluetoothDevice.mJustDiscovered = z;
        cachedBluetoothDevice.mDeviceSide = i;
        fetchActiveDevices();
    }

    public Set<CachedBluetoothDevice> getMemberDevice() {
        return this.mMemberDevices;
    }

    public void addMemberDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mMemberDevices.add(cachedBluetoothDevice);
    }

    public void removeMemberDevice(CachedBluetoothDevice cachedBluetoothDevice) {
        this.mMemberDevices.remove(cachedBluetoothDevice);
    }

    public void switchMemberDeviceContent(CachedBluetoothDevice cachedBluetoothDevice, CachedBluetoothDevice cachedBluetoothDevice2) {
        BluetoothDevice bluetoothDevice = this.mDevice;
        short s = this.mRssi;
        boolean z = this.mJustDiscovered;
        this.mDevice = cachedBluetoothDevice2.mDevice;
        this.mRssi = cachedBluetoothDevice2.mRssi;
        this.mJustDiscovered = cachedBluetoothDevice2.mJustDiscovered;
        addMemberDevice(cachedBluetoothDevice);
        this.mMemberDevices.remove(cachedBluetoothDevice2);
        cachedBluetoothDevice2.mDevice = bluetoothDevice;
        cachedBluetoothDevice2.mRssi = s;
        cachedBluetoothDevice2.mJustDiscovered = z;
        fetchActiveDevices();
    }

    public Pair<Drawable, String> getDrawableWithDescription() {
        Uri uriMetaData = BluetoothUtils.getUriMetaData(this.mDevice, 5);
        Pair<Drawable, String> btClassDrawableWithDescription = BluetoothUtils.getBtClassDrawableWithDescription(this.mContext, this);
        if (BluetoothUtils.isAdvancedDetailsHeader(this.mDevice) && uriMetaData != null) {
            BitmapDrawable bitmapDrawable = this.mDrawableCache.get(uriMetaData.toString());
            if (bitmapDrawable != null) {
                return new Pair<>(new AdaptiveOutlineDrawable(this.mContext.getResources(), bitmapDrawable.getBitmap()), (String) btClassDrawableWithDescription.second);
            }
            refresh();
        }
        return new Pair<>(BluetoothUtils.buildBtRainbowDrawable(this.mContext, (Drawable) btClassDrawableWithDescription.first, getAddress().hashCode()), (String) btClassDrawableWithDescription.second);
    }

    /* access modifiers changed from: package-private */
    public void releaseLruCache() {
        this.mDrawableCache.evictAll();
    }

    public boolean isGroupDevice() {
        return this.mIsGroupDevice;
    }

    public boolean isPrivateAddr() {
        return this.mIsIgnore;
    }

    public void setDeviceType(int i) {
        if (i != this.mType) {
            this.mType = i;
            if (i == -1 || i == 100) {
                this.mIsGroupDevice = false;
                this.mQGroupId = -1;
                this.mIsIgnore = false;
            } else if (i == 101) {
                this.mIsGroupDevice = false;
                this.mQGroupId = -1;
                this.mIsIgnore = true;
            } else if (i < 0 || i > 15) {
                Log.e(TAG, "setDeviceType error type " + this.mType);
            } else {
                this.mQGroupId = i;
                this.mIsIgnore = false;
                this.mIsGroupDevice = true;
            }
        }
    }

    public boolean isTypeUnKnown() {
        return this.mType == -1;
    }

    public int getmType() {
        return this.mType;
    }

    /* access modifiers changed from: package-private */
    public boolean getUnpairing() {
        return this.mUnpairing;
    }

    /* access modifiers changed from: package-private */
    public void setLeAudioEnabled() {
        this.mIsLeAudioEnabled = this.mProfileManager.getLeAudioProfile() != null;
    }

    /* access modifiers changed from: package-private */
    public boolean isLeAudioEnabled() {
        return this.mIsLeAudioEnabled;
    }

    public Pair<Drawable, Boolean> getDrawableWithDescriptionWithoutRainbow() {
        Uri uriMetaData = BluetoothUtils.getUriMetaData(this.mDevice, 5);
        Pair<Drawable, String> btClassDrawableWithDescription = BluetoothUtils.getBtClassDrawableWithDescription(this.mContext, this);
        if (BluetoothUtils.isAdvancedDetailsHeader(this.mDevice) && uriMetaData != null) {
            Log.d(TAG, "getDrawableWithDescriptionWithoutRainbow uri: " + uriMetaData);
            BitmapDrawable bitmapDrawable = this.mDrawableCache.get(uriMetaData.toString());
            if (bitmapDrawable != null) {
                return new Pair<>(bitmapDrawable, true);
            }
            refresh();
        }
        return new Pair<>((Drawable) btClassDrawableWithDescription.first, false);
    }
}
