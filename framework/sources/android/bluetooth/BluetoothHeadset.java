package android.bluetooth;

import android.Manifest;
import android.annotation.SystemApi;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.IBluetoothHeadset;
import android.bluetooth.IBluetoothProfileServiceConnection;
import android.bluetooth.IBluetoothStateChangeCallback;
import android.content.Attributable;
import android.content.AttributionSource;
import android.content.ComponentName;
import android.content.Context;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.CloseGuard;
import android.util.Log;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/* loaded from: classes.dex */
public final class BluetoothHeadset implements BluetoothProfile {
    public static final String ACTION_ACTIVE_DEVICE_CHANGED = "android.bluetooth.headset.profile.action.ACTIVE_DEVICE_CHANGED";
    public static final String ACTION_AUDIO_STATE_CHANGED = "android.bluetooth.headset.profile.action.AUDIO_STATE_CHANGED";
    public static final String ACTION_CONNECTION_STATE_CHANGED = "android.bluetooth.headset.profile.action.CONNECTION_STATE_CHANGED";
    public static final String ACTION_HF_INDICATORS_VALUE_CHANGED = "android.bluetooth.headset.action.HF_INDICATORS_VALUE_CHANGED";
    public static final String ACTION_HF_TWSP_BATTERY_STATE_CHANGED = "android.bluetooth.headset.action.HF_TWSP_BATTERY_STATE_CHANGED";
    public static final String ACTION_VENDOR_SPECIFIC_HEADSET_EVENT = "android.bluetooth.headset.action.VENDOR_SPECIFIC_HEADSET_EVENT";
    public static final int AT_CMD_TYPE_ACTION = 4;
    public static final int AT_CMD_TYPE_BASIC = 3;
    public static final int AT_CMD_TYPE_READ = 0;
    public static final int AT_CMD_TYPE_SET = 2;
    public static final int AT_CMD_TYPE_TEST = 1;
    private static final boolean DBG = true;
    public static final String EXTRA_HF_INDICATORS_IND_ID = "android.bluetooth.headset.extra.HF_INDICATORS_IND_ID";
    public static final String EXTRA_HF_INDICATORS_IND_VALUE = "android.bluetooth.headset.extra.HF_INDICATORS_IND_VALUE";
    public static final String EXTRA_HF_TWSP_BATTERY_LEVEL = "android.bluetooth.headset.extra.HF_TWSP_BATTERY_LEVEL";
    public static final String EXTRA_HF_TWSP_BATTERY_STATE = "android.bluetooth.headset.extra.HF_TWSP_BATTERY_STATE";
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_ARGS";
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_CMD";
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE = "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_CMD_TYPE";
    private static final int MESSAGE_HEADSET_SERVICE_CONNECTED = 100;
    private static final int MESSAGE_HEADSET_SERVICE_DISCONNECTED = 101;
    public static final int STATE_AUDIO_CONNECTED = 12;
    public static final int STATE_AUDIO_CONNECTING = 11;
    public static final int STATE_AUDIO_DISCONNECTED = 10;
    public static final int STATE_AUDIO_DISCONNECTING = 13;
    private static final String TAG = "BluetoothHeadset";
    private static final boolean VDBG = false;
    public static final String VENDOR_RESULT_CODE_COMMAND_ANDROID = "+ANDROID";
    public static final String VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID_CATEGORY = "android.bluetooth.headset.intent.category.companyid";
    public static final String VENDOR_SPECIFIC_HEADSET_EVENT_IPHONEACCEV = "+IPHONEACCEV";
    public static final int VENDOR_SPECIFIC_HEADSET_EVENT_IPHONEACCEV_BATTERY_LEVEL = 1;
    public static final String VENDOR_SPECIFIC_HEADSET_EVENT_XAPL = "+XAPL";
    public static final String VENDOR_SPECIFIC_HEADSET_EVENT_XEVENT = "+XEVENT";
    public static final String VENDOR_SPECIFIC_HEADSET_EVENT_XEVENT_BATTERY_LEVEL = "BATTERY";
    private final BluetoothAdapter mAdapter;
    private final AttributionSource mAttributionSource;
    private final IBluetoothStateChangeCallback mBluetoothStateChangeCallback;
    private Context mContext;
    private IBluetoothHeadset mService;
    private BluetoothProfile.ServiceListener mServiceListener;
    private final CloseGuard mCloseGuard = new CloseGuard();
    private final ReentrantReadWriteLock mServiceLock = new ReentrantReadWriteLock();
    private final IBluetoothProfileServiceConnection mConnection = new IBluetoothProfileServiceConnection.Stub() { // from class: android.bluetooth.BluetoothHeadset.2
        @Override // android.bluetooth.IBluetoothProfileServiceConnection
        public void onServiceConnected(ComponentName className, IBinder service) {
            Log.d(BluetoothHeadset.TAG, "Proxy object connected");
            try {
                BluetoothHeadset.this.mServiceLock.writeLock().lock();
                BluetoothHeadset.this.mService = IBluetoothHeadset.Stub.asInterface(Binder.allowBlocking(service));
                BluetoothHeadset.this.mHandler.sendMessage(BluetoothHeadset.this.mHandler.obtainMessage(100));
            } finally {
                BluetoothHeadset.this.mServiceLock.writeLock().unlock();
            }
        }

        @Override // android.bluetooth.IBluetoothProfileServiceConnection
        public void onServiceDisconnected(ComponentName className) {
            Log.d(BluetoothHeadset.TAG, "Proxy object disconnected");
            try {
                BluetoothHeadset.this.mServiceLock.writeLock().lock();
                BluetoothHeadset.this.mHandler.sendMessage(BluetoothHeadset.this.mHandler.obtainMessage(101));
            } finally {
                BluetoothHeadset.this.mServiceLock.writeLock().unlock();
            }
        }
    };
    private final Handler mHandler = new Handler(Looper.getMainLooper()) { // from class: android.bluetooth.BluetoothHeadset.3
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    if (BluetoothHeadset.this.mServiceListener != null) {
                        BluetoothHeadset.this.mServiceListener.onServiceConnected(1, BluetoothHeadset.this);
                        return;
                    }
                    return;
                case 101:
                    if (BluetoothHeadset.this.mServiceListener != null) {
                        BluetoothHeadset.this.mServiceListener.onServiceDisconnected(1);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    public BluetoothHeadset(Context context, BluetoothProfile.ServiceListener l, BluetoothAdapter adapter) {
        IBluetoothStateChangeCallback.Stub stub = new IBluetoothStateChangeCallback.Stub() { // from class: android.bluetooth.BluetoothHeadset.1
            @Override // android.bluetooth.IBluetoothStateChangeCallback
            public void onBluetoothStateChange(boolean up) {
                Log.d(BluetoothHeadset.TAG, "onBluetoothStateChange: up=" + up);
                if (!up) {
                    BluetoothHeadset.this.doUnbind();
                } else {
                    BluetoothHeadset.this.doBind();
                }
            }
        };
        this.mBluetoothStateChangeCallback = stub;
        this.mContext = context;
        this.mServiceListener = l;
        this.mAdapter = adapter;
        this.mAttributionSource = adapter.getAttributionSource();
        if (context.getApplicationInfo().targetSdkVersion <= 30 && context.checkSelfPermission(Manifest.permission.BLUETOOTH) != 0) {
            throw new SecurityException("Need BLUETOOTH permission");
        }
        IBluetoothManager mgr = adapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.registerStateChangeCallback(stub);
            } catch (RemoteException e) {
                Log.e(TAG, "", e);
            }
        }
        doBind();
        this.mCloseGuard.open("close");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean doBind() {
        synchronized (this.mConnection) {
            if (this.mService == null) {
                Log.d(TAG, "Binding service...");
                try {
                    return this.mAdapter.getBluetoothManager().bindBluetoothProfileService(1, this.mConnection);
                } catch (RemoteException e) {
                    Log.e(TAG, "Unable to bind HeadsetService", e);
                }
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doUnbind() {
        ReentrantReadWriteLock.WriteLock writeLock;
        synchronized (this.mConnection) {
            Log.d(TAG, "Unbinding service...");
            if (this.mService != null) {
                try {
                    this.mAdapter.getBluetoothManager().unbindBluetoothProfileService(1, this.mConnection);
                    this.mServiceLock.writeLock().lock();
                    this.mService = null;
                    writeLock = this.mServiceLock.writeLock();
                } catch (RemoteException e) {
                    Log.e(TAG, "Unable to unbind HeadsetService", e);
                    this.mServiceLock.writeLock().lock();
                    this.mService = null;
                    writeLock = this.mServiceLock.writeLock();
                }
                writeLock.unlock();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void close() {
        IBluetoothManager mgr = this.mAdapter.getBluetoothManager();
        if (mgr != null) {
            try {
                mgr.unregisterStateChangeCallback(this.mBluetoothStateChangeCallback);
            } catch (RemoteException re) {
                Log.e(TAG, "", re);
            }
        }
        this.mServiceListener = null;
        doUnbind();
        this.mCloseGuard.close();
    }

    protected void finalize() throws Throwable {
        this.mCloseGuard.warnIfOpen();
        close();
    }

    @SystemApi
    public boolean connect(BluetoothDevice device) {
        log("connect(" + device + ")");
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.connect(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    @SystemApi
    public boolean disconnect(BluetoothDevice device) {
        log("disconnect(" + device + ")");
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.disconnect(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getConnectedDevices() {
        try {
            this.mServiceLock.readLock().lock();
            IBluetoothHeadset service = this.mService;
            if (service != null && isEnabled()) {
                return Attributable.setAttributionSource(service.getConnectedDevicesWithAttribution(this.mAttributionSource), this.mAttributionSource);
            }
            if (service == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            return new ArrayList();
        } catch (RemoteException e) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
            return new ArrayList();
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    @Override // android.bluetooth.BluetoothProfile
    public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) {
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled()) {
            try {
                return Attributable.setAttributionSource(service.getDevicesMatchingConnectionStates(states, this.mAttributionSource), this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return new ArrayList();
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return new ArrayList();
    }

    @Override // android.bluetooth.BluetoothProfile
    public int getConnectionState(BluetoothDevice device) {
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.getConnectionState(device);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return 0;
    }

    @SystemApi
    @Deprecated
    public boolean setPriority(BluetoothDevice device, int priority) {
        log("setPriority(" + device + ", " + priority + ")");
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            if (priority != 0 && priority != 100) {
                return false;
            }
            try {
                return service.setPriority(device, BluetoothAdapter.priorityToConnectionPolicy(priority), this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    @SystemApi
    public boolean setConnectionPolicy(BluetoothDevice device, int connectionPolicy) {
        log("setConnectionPolicy(" + device + ", " + connectionPolicy + ")");
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            if (connectionPolicy != 0 && connectionPolicy != 100) {
                return false;
            }
            try {
                return service.setConnectionPolicy(device, connectionPolicy, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return false;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return false;
    }

    public int getPriority(BluetoothDevice device) {
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return BluetoothAdapter.connectionPolicyToPriority(service.getPriority(device, this.mAttributionSource));
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return 0;
    }

    @SystemApi
    public int getConnectionPolicy(BluetoothDevice device) {
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.getConnectionPolicy(device, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
                return 0;
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
        }
        return 0;
    }

    public boolean isNoiseReductionSupported(BluetoothDevice device) {
        log("isNoiseReductionSupported()");
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.isNoiseReductionSupported(device, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public boolean isVoiceRecognitionSupported(BluetoothDevice device) {
        log("isVoiceRecognitionSupported()");
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.isVoiceRecognitionSupported(device, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public boolean startVoiceRecognition(BluetoothDevice device) {
        log("startVoiceRecognition()");
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.startVoiceRecognition(device, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public boolean stopVoiceRecognition(BluetoothDevice device) {
        log("stopVoiceRecognition()");
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.stopVoiceRecognition(device, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public boolean isAudioConnected(BluetoothDevice device) {
        IBluetoothHeadset service = this.mService;
        try {
            this.mServiceLock.readLock().lock();
            if (service != null && isEnabled() && isValidDevice(device)) {
                try {
                    return service.isAudioConnected(device, this.mAttributionSource);
                } catch (RemoteException e) {
                    Log.e(TAG, Log.getStackTraceString(new Throwable()));
                }
            }
            if (service == null) {
                Log.w(TAG, "Proxy not attached to service");
            }
            this.mServiceLock.readLock().unlock();
            return false;
        } finally {
            this.mServiceLock.readLock().unlock();
        }
    }

    public static boolean isBluetoothVoiceDialingEnabled(Context context) {
        return context.getResources().getBoolean(R.bool.config_bluetooth_sco_off_call);
    }

    public int getAudioState(BluetoothDevice device) {
        IBluetoothHeadset service = this.mService;
        if (service == null || isDisabled()) {
            Log.w(TAG, "Proxy not attached to service");
            Log.d(TAG, Log.getStackTraceString(new Throwable()));
            return 10;
        }
        try {
            return service.getAudioState(device, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
            return 10;
        }
    }

    public void setAudioRouteAllowed(boolean allowed) {
        IBluetoothHeadset service = this.mService;
        if (service == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            Log.d(TAG, Log.getStackTraceString(new Throwable()));
            return;
        }
        try {
            service.setAudioRouteAllowed(allowed, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
        }
    }

    public boolean getAudioRouteAllowed() {
        IBluetoothHeadset service = this.mService;
        if (service == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            Log.d(TAG, Log.getStackTraceString(new Throwable()));
            return false;
        }
        try {
            return service.getAudioRouteAllowed(this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public void setForceScoAudio(boolean forced) {
        IBluetoothHeadset service = this.mService;
        if (service == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            Log.d(TAG, Log.getStackTraceString(new Throwable()));
            return;
        }
        try {
            service.setForceScoAudio(forced, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
        }
    }

    public boolean isAudioOn() {
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled()) {
            try {
                return service.isAudioOn(this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public boolean connectAudio() {
        IBluetoothHeadset service = this.mService;
        if (service == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            Log.d(TAG, Log.getStackTraceString(new Throwable()));
            return false;
        }
        try {
            return service.connectAudio(this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean disconnectAudio() {
        IBluetoothHeadset service = this.mService;
        if (service == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            Log.d(TAG, Log.getStackTraceString(new Throwable()));
            return false;
        }
        try {
            return service.disconnectAudio(this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean startScoUsingVirtualVoiceCall() {
        log("startScoUsingVirtualVoiceCall()");
        IBluetoothHeadset service = this.mService;
        if (service == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            Log.d(TAG, Log.getStackTraceString(new Throwable()));
            return false;
        }
        try {
            return service.startScoUsingVirtualVoiceCall(this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public boolean stopScoUsingVirtualVoiceCall() {
        log("stopScoUsingVirtualVoiceCall()");
        IBluetoothHeadset service = this.mService;
        if (service == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            Log.d(TAG, Log.getStackTraceString(new Throwable()));
            return false;
        }
        try {
            return service.stopScoUsingVirtualVoiceCall(this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }

    public void phoneStateChanged(int numActive, int numHeld, int callState, String number, int type, String name) {
        IBluetoothHeadset service = this.mService;
        if (service == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            Log.d(TAG, Log.getStackTraceString(new Throwable()));
            return;
        }
        try {
            service.phoneStateChanged(numActive, numHeld, callState, number, type, name, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
        }
    }

    public void clccResponse(int index, int direction, int status, int mode, boolean mpty, String number, int type) {
        IBluetoothHeadset service = this.mService;
        if (service == null || !isEnabled()) {
            Log.w(TAG, "Proxy not attached to service");
            Log.d(TAG, Log.getStackTraceString(new Throwable()));
            return;
        }
        try {
            service.clccResponse(index, direction, status, mode, mpty, number, type, this.mAttributionSource);
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
        }
    }

    public boolean sendVendorSpecificResultCode(BluetoothDevice device, String command, String arg) {
        log("sendVendorSpecificResultCode()");
        if (command == null) {
            throw new IllegalArgumentException("command is null");
        }
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && isValidDevice(device)) {
            try {
                return service.sendVendorSpecificResultCode(device, command, arg, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public boolean setActiveDevice(BluetoothDevice device) {
        Log.d(TAG, "setActiveDevice: " + device);
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && (device == null || isValidDevice(device))) {
            try {
                return service.setActiveDevice(device, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public BluetoothDevice getActiveDevice() {
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled()) {
            try {
                return (BluetoothDevice) Attributable.setAttributionSource(service.getActiveDevice(this.mAttributionSource), this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return null;
        }
        return null;
    }

    public boolean isInbandRingingEnabled() {
        log("isInbandRingingEnabled()");
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled()) {
            try {
                return service.isInbandRingingEnabled(this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public boolean isLowLatencySupported(BluetoothDevice device) {
        Log.d(TAG, "isLowLatencySupported: " + device);
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && (device == null || isValidDevice(device))) {
            try {
                return service.isLowLatencySupported(device, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public boolean isEqSupported(BluetoothDevice device) {
        Log.d(TAG, "isEqSupported: " + device);
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && (device == null || isValidDevice(device))) {
            try {
                return service.isEqSupported(device, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public boolean getLowLatencyMode(BluetoothDevice device) {
        Log.d(TAG, "getLowLatencyMode: " + device);
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && (device == null || isValidDevice(device))) {
            try {
                return service.getLowLatencyMode(device, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public boolean getEqMode(BluetoothDevice device) {
        Log.d(TAG, "getEqMode: " + device);
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && (device == null || isValidDevice(device))) {
            try {
                return service.getEqMode(device, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public boolean setLowLatencyMode(BluetoothDevice device, boolean enable) {
        Log.d(TAG, "setLowLatencyMode: " + device + ",enable: " + enable);
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && (device == null || isValidDevice(device))) {
            try {
                return service.setLowLatencyMode(device, enable, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public boolean setEqMode(BluetoothDevice device, boolean enable) {
        Log.d(TAG, "setEqMode: " + device + ",enable: " + enable);
        IBluetoothHeadset service = this.mService;
        if (service != null && isEnabled() && (device == null || isValidDevice(device))) {
            try {
                return service.setEqMode(device, enable, this.mAttributionSource);
            } catch (RemoteException e) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
        }
        if (service == null) {
            Log.w(TAG, "Proxy not attached to service");
            return false;
        }
        return false;
    }

    public static boolean isInbandRingingSupported(Context context) {
        return context.getResources().getBoolean(R.bool.config_bluetooth_hfp_inband_ringing_support);
    }

    private boolean isEnabled() {
        return this.mAdapter.getState() == 12;
    }

    private boolean isDisabled() {
        return this.mAdapter.getState() == 10;
    }

    private static boolean isValidDevice(BluetoothDevice device) {
        return device != null && BluetoothAdapter.checkBluetoothAddress(device.getAddress());
    }

    private static void log(String msg) {
        Log.d(TAG, msg);
    }
}
