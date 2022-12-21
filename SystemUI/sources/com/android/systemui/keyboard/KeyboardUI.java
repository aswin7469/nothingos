package com.android.systemui.keyboard;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.hardware.input.InputManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.Toast;
import com.android.settingslib.bluetooth.BluetoothCallback;
import com.android.settingslib.bluetooth.BluetoothUtils;
import com.android.settingslib.bluetooth.CachedBluetoothDevice;
import com.android.settingslib.bluetooth.CachedBluetoothDeviceManager;
import com.android.settingslib.bluetooth.LocalBluetoothAdapter;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.bluetooth.LocalBluetoothProfileManager;
import com.android.systemui.C1893R;
import com.android.systemui.CoreStartable;
import com.android.systemui.Dependency;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.google.android.setupcompat.util.WizardManagerHelper;
import java.p026io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;

@SysUISingleton
public class KeyboardUI extends CoreStartable implements InputManager.OnTabletModeChangedListener {
    private static final long BLUETOOTH_SCAN_TIMEOUT_MILLIS = 30000;
    private static final long BLUETOOTH_START_DELAY_MILLIS = 10000;
    private static final boolean DEBUG = false;
    private static final int MSG_BLE_ABORT_SCAN = 10;
    private static final int MSG_DISMISS_BLUETOOTH_DIALOG = 9;
    private static final int MSG_ENABLE_BLUETOOTH = 3;
    private static final int MSG_INIT = 0;
    private static final int MSG_ON_BLE_SCAN_FAILED = 7;
    private static final int MSG_ON_BLUETOOTH_DEVICE_ADDED = 6;
    private static final int MSG_ON_BLUETOOTH_STATE_CHANGED = 4;
    private static final int MSG_ON_BOOT_COMPLETED = 1;
    private static final int MSG_ON_DEVICE_BOND_STATE_CHANGED = 5;
    private static final int MSG_PROCESS_KEYBOARD_STATE = 2;
    private static final int MSG_SHOW_BLUETOOTH_DIALOG = 8;
    private static final int MSG_SHOW_ERROR = 11;
    private static final int STATE_DEVICE_NOT_FOUND = 9;
    private static final int STATE_NOT_ENABLED = -1;
    private static final int STATE_PAIRED = 6;
    private static final int STATE_PAIRING = 5;
    private static final int STATE_PAIRING_FAILED = 7;
    private static final int STATE_UNKNOWN = 0;
    private static final int STATE_USER_CANCELLED = 8;
    private static final int STATE_WAITING_FOR_BLUETOOTH = 4;
    private static final int STATE_WAITING_FOR_BOOT_COMPLETED = 1;
    private static final int STATE_WAITING_FOR_DEVICE_DISCOVERY = 3;
    private static final int STATE_WAITING_FOR_TABLET_MODE_EXIT = 2;
    private static final String TAG = "KeyboardUI";
    private boolean mBootCompleted;
    private long mBootCompletedTime;
    private CachedBluetoothDeviceManager mCachedDeviceManager;
    protected volatile Context mContext;
    /* access modifiers changed from: private */
    public BluetoothDialog mDialog;
    private boolean mEnabled;
    /* access modifiers changed from: private */
    public volatile KeyboardHandler mHandler;
    private int mInTabletMode = -1;
    private String mKeyboardName;
    /* access modifiers changed from: private */
    public LocalBluetoothAdapter mLocalBluetoothAdapter;
    private LocalBluetoothProfileManager mProfileManager;
    private int mScanAttempt = 0;
    private ScanCallback mScanCallback;
    /* access modifiers changed from: private */
    public int mState;
    private volatile KeyboardUIHandler mUIHandler;

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Inject
    public KeyboardUI(Context context) {
        super(context);
    }

    public void start() {
        this.mContext = this.mContext;
        HandlerThread handlerThread = new HandlerThread("Keyboard", 10);
        handlerThread.start();
        this.mHandler = new KeyboardHandler(handlerThread.getLooper());
        this.mHandler.sendEmptyMessage(0);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyboardUI:");
        printWriter.println("  mEnabled=" + this.mEnabled);
        printWriter.println("  mBootCompleted=" + this.mEnabled);
        printWriter.println("  mBootCompletedTime=" + this.mBootCompletedTime);
        printWriter.println("  mKeyboardName=" + this.mKeyboardName);
        printWriter.println("  mInTabletMode=" + this.mInTabletMode);
        printWriter.println("  mState=" + stateToString(this.mState));
    }

    /* access modifiers changed from: protected */
    public void onBootCompleted() {
        this.mHandler.sendEmptyMessage(1);
    }

    public void onTabletModeChanged(long j, boolean z) {
        if ((z && this.mInTabletMode != 1) || (!z && this.mInTabletMode != 0)) {
            this.mInTabletMode = z ? 1 : 0;
            processKeyboardState();
        }
    }

    /* access modifiers changed from: private */
    public void init() {
        LocalBluetoothManager localBluetoothManager;
        Context context = this.mContext;
        String string = context.getString(17040012);
        this.mKeyboardName = string;
        if (!TextUtils.isEmpty(string) && (localBluetoothManager = (LocalBluetoothManager) Dependency.get(LocalBluetoothManager.class)) != null) {
            this.mEnabled = true;
            this.mCachedDeviceManager = localBluetoothManager.getCachedDeviceManager();
            this.mLocalBluetoothAdapter = localBluetoothManager.getBluetoothAdapter();
            this.mProfileManager = localBluetoothManager.getProfileManager();
            localBluetoothManager.getEventManager().registerCallback(new BluetoothCallbackHandler());
            BluetoothUtils.setErrorListener(new BluetoothErrorListener());
            InputManager inputManager = (InputManager) context.getSystemService(InputManager.class);
            inputManager.registerOnTabletModeChangedListener(this, this.mHandler);
            this.mInTabletMode = inputManager.isInTabletMode();
            processKeyboardState();
            this.mUIHandler = new KeyboardUIHandler();
        }
    }

    /* access modifiers changed from: private */
    public void processKeyboardState() {
        this.mHandler.removeMessages(2);
        if (!this.mEnabled) {
            this.mState = -1;
        } else if (!this.mBootCompleted) {
            this.mState = 1;
        } else if (this.mInTabletMode != 0) {
            int i = this.mState;
            if (i == 3) {
                stopScanning();
            } else if (i == 4) {
                this.mUIHandler.sendEmptyMessage(9);
            }
            this.mState = 2;
        } else {
            int state = this.mLocalBluetoothAdapter.getState();
            if ((state == 11 || state == 12) && this.mState == 4) {
                this.mUIHandler.sendEmptyMessage(9);
            }
            if (state == 11) {
                this.mState = 4;
            } else if (state != 12) {
                this.mState = 4;
                showBluetoothDialog();
            } else {
                CachedBluetoothDevice pairedKeyboard = getPairedKeyboard();
                int i2 = this.mState;
                if (i2 == 2 || i2 == 4) {
                    if (pairedKeyboard != null) {
                        this.mState = 6;
                        pairedKeyboard.connect(false);
                        return;
                    }
                    this.mCachedDeviceManager.clearNonBondedDevices();
                }
                CachedBluetoothDevice discoveredKeyboard = getDiscoveredKeyboard();
                if (discoveredKeyboard != null) {
                    this.mState = 5;
                    discoveredKeyboard.startPairing();
                    return;
                }
                this.mState = 3;
                startScanning();
            }
        }
    }

    public void onBootCompletedInternal() {
        this.mBootCompleted = true;
        this.mBootCompletedTime = SystemClock.uptimeMillis();
        if (this.mState == 1) {
            processKeyboardState();
        }
    }

    private void showBluetoothDialog() {
        if (isUserSetupComplete()) {
            long uptimeMillis = SystemClock.uptimeMillis();
            long j = this.mBootCompletedTime + BLUETOOTH_START_DELAY_MILLIS;
            if (j < uptimeMillis) {
                this.mUIHandler.sendEmptyMessage(8);
            } else {
                this.mHandler.sendEmptyMessageAtTime(2, j);
            }
        } else {
            this.mLocalBluetoothAdapter.enable();
        }
    }

    private boolean isUserSetupComplete() {
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), WizardManagerHelper.SETTINGS_SECURE_USER_SETUP_COMPLETE, 0, -2) != 0) {
            return true;
        }
        return false;
    }

    private CachedBluetoothDevice getPairedKeyboard() {
        for (BluetoothDevice next : this.mLocalBluetoothAdapter.getBondedDevices()) {
            if (this.mKeyboardName.equals(next.getName())) {
                return getCachedBluetoothDevice(next);
            }
        }
        return null;
    }

    private CachedBluetoothDevice getDiscoveredKeyboard() {
        for (CachedBluetoothDevice next : this.mCachedDeviceManager.getCachedDevicesCopy()) {
            if (next.getName().equals(this.mKeyboardName)) {
                return next;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public CachedBluetoothDevice getCachedBluetoothDevice(BluetoothDevice bluetoothDevice) {
        CachedBluetoothDevice findDevice = this.mCachedDeviceManager.findDevice(bluetoothDevice);
        return findDevice == null ? this.mCachedDeviceManager.addDevice(bluetoothDevice) : findDevice;
    }

    private void startScanning() {
        BluetoothLeScanner bluetoothLeScanner = this.mLocalBluetoothAdapter.getBluetoothLeScanner();
        ScanFilter build = new ScanFilter.Builder().setDeviceName(this.mKeyboardName).build();
        ScanSettings build2 = new ScanSettings.Builder().setCallbackType(1).setNumOfMatches(1).setScanMode(2).setReportDelay(0).build();
        this.mScanCallback = new KeyboardScanCallback();
        bluetoothLeScanner.startScan(Arrays.asList(build), build2, this.mScanCallback);
        KeyboardHandler keyboardHandler = this.mHandler;
        int i = this.mScanAttempt + 1;
        this.mScanAttempt = i;
        this.mHandler.sendMessageDelayed(keyboardHandler.obtainMessage(10, i, 0), 30000);
    }

    private void stopScanning() {
        if (this.mScanCallback != null) {
            BluetoothLeScanner bluetoothLeScanner = this.mLocalBluetoothAdapter.getBluetoothLeScanner();
            if (bluetoothLeScanner != null) {
                bluetoothLeScanner.stopScan(this.mScanCallback);
            }
            this.mScanCallback = null;
        }
    }

    /* access modifiers changed from: private */
    public void bleAbortScanInternal(int i) {
        if (this.mState == 3 && i == this.mScanAttempt) {
            stopScanning();
            this.mState = 9;
        }
    }

    /* access modifiers changed from: private */
    public void onDeviceAddedInternal(CachedBluetoothDevice cachedBluetoothDevice) {
        if (this.mState == 3 && cachedBluetoothDevice.getName().equals(this.mKeyboardName)) {
            stopScanning();
            cachedBluetoothDevice.startPairing();
            this.mState = 5;
        }
    }

    /* access modifiers changed from: private */
    public void onBluetoothStateChangedInternal(int i) {
        if (i == 12 && this.mState == 4) {
            processKeyboardState();
        }
    }

    /* access modifiers changed from: private */
    public void onDeviceBondStateChangedInternal(CachedBluetoothDevice cachedBluetoothDevice, int i) {
        if (this.mState == 5 && cachedBluetoothDevice.getName().equals(this.mKeyboardName)) {
            if (i == 12) {
                this.mState = 6;
            } else if (i == 10) {
                this.mState = 7;
            }
        }
    }

    /* access modifiers changed from: private */
    public void onBleScanFailedInternal() {
        this.mScanCallback = null;
        if (this.mState == 3) {
            this.mState = 9;
        }
    }

    /* access modifiers changed from: private */
    public void onShowErrorInternal(Context context, String str, int i) {
        int i2 = this.mState;
        if ((i2 == 5 || i2 == 7) && this.mKeyboardName.equals(str)) {
            Toast.makeText(context, context.getString(i, new Object[]{str}), 0).show();
        }
    }

    private final class KeyboardUIHandler extends Handler {
        public KeyboardUIHandler() {
            super(Looper.getMainLooper(), (Handler.Callback) null, true);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 8) {
                if (i == 9 && KeyboardUI.this.mDialog != null) {
                    KeyboardUI.this.mDialog.dismiss();
                }
            } else if (KeyboardUI.this.mDialog == null) {
                BluetoothDialogClickListener bluetoothDialogClickListener = new BluetoothDialogClickListener();
                BluetoothDialogDismissListener bluetoothDialogDismissListener = new BluetoothDialogDismissListener();
                BluetoothDialog unused = KeyboardUI.this.mDialog = new BluetoothDialog(KeyboardUI.this.mContext);
                KeyboardUI.this.mDialog.setTitle(C1893R.string.enable_bluetooth_title);
                KeyboardUI.this.mDialog.setMessage(C1893R.string.enable_bluetooth_message);
                KeyboardUI.this.mDialog.setPositiveButton(C1893R.string.enable_bluetooth_confirmation_ok, bluetoothDialogClickListener);
                KeyboardUI.this.mDialog.setNegativeButton(17039360, bluetoothDialogClickListener);
                KeyboardUI.this.mDialog.setOnDismissListener(bluetoothDialogDismissListener);
                KeyboardUI.this.mDialog.show();
            }
        }
    }

    private final class KeyboardHandler extends Handler {
        public KeyboardHandler(Looper looper) {
            super(looper, (Handler.Callback) null, true);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    KeyboardUI.this.init();
                    return;
                case 1:
                    KeyboardUI.this.onBootCompletedInternal();
                    return;
                case 2:
                    KeyboardUI.this.processKeyboardState();
                    return;
                case 3:
                    boolean z = true;
                    if (message.arg1 != 1) {
                        z = false;
                    }
                    if (z) {
                        KeyboardUI.this.mLocalBluetoothAdapter.enable();
                        return;
                    } else {
                        int unused = KeyboardUI.this.mState = 8;
                        return;
                    }
                case 4:
                    KeyboardUI.this.onBluetoothStateChangedInternal(message.arg1);
                    return;
                case 5:
                    int i = message.arg1;
                    KeyboardUI.this.onDeviceBondStateChangedInternal((CachedBluetoothDevice) message.obj, i);
                    return;
                case 6:
                    KeyboardUI.this.onDeviceAddedInternal(KeyboardUI.this.getCachedBluetoothDevice((BluetoothDevice) message.obj));
                    return;
                case 7:
                    KeyboardUI.this.onBleScanFailedInternal();
                    return;
                case 10:
                    KeyboardUI.this.bleAbortScanInternal(message.arg1);
                    return;
                case 11:
                    Pair pair = (Pair) message.obj;
                    KeyboardUI.this.onShowErrorInternal((Context) pair.first, (String) pair.second, message.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    private final class BluetoothDialogClickListener implements DialogInterface.OnClickListener {
        private BluetoothDialogClickListener() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            KeyboardUI.this.mHandler.obtainMessage(3, -1 == i ? 1 : 0, 0).sendToTarget();
            BluetoothDialog unused = KeyboardUI.this.mDialog = null;
        }
    }

    private final class BluetoothDialogDismissListener implements DialogInterface.OnDismissListener {
        private BluetoothDialogDismissListener() {
        }

        public void onDismiss(DialogInterface dialogInterface) {
            BluetoothDialog unused = KeyboardUI.this.mDialog = null;
        }
    }

    private final class KeyboardScanCallback extends ScanCallback {
        private KeyboardScanCallback() {
        }

        private boolean isDeviceDiscoverable(ScanResult scanResult) {
            return (scanResult.getScanRecord().getAdvertiseFlags() & 3) != 0;
        }

        public void onBatchScanResults(List<ScanResult> list) {
            BluetoothDevice bluetoothDevice = null;
            int i = Integer.MIN_VALUE;
            for (ScanResult next : list) {
                if (isDeviceDiscoverable(next) && next.getRssi() > i) {
                    bluetoothDevice = next.getDevice();
                    i = next.getRssi();
                }
            }
            if (bluetoothDevice != null) {
                KeyboardUI.this.mHandler.obtainMessage(6, bluetoothDevice).sendToTarget();
            }
        }

        public void onScanFailed(int i) {
            KeyboardUI.this.mHandler.obtainMessage(7).sendToTarget();
        }

        public void onScanResult(int i, ScanResult scanResult) {
            if (isDeviceDiscoverable(scanResult)) {
                KeyboardUI.this.mHandler.obtainMessage(6, scanResult.getDevice()).sendToTarget();
            }
        }
    }

    private final class BluetoothCallbackHandler implements BluetoothCallback {
        private BluetoothCallbackHandler() {
        }

        public void onBluetoothStateChanged(int i) {
            KeyboardUI.this.mHandler.obtainMessage(4, i, 0).sendToTarget();
        }

        public void onDeviceBondStateChanged(CachedBluetoothDevice cachedBluetoothDevice, int i) {
            KeyboardUI.this.mHandler.obtainMessage(5, i, 0, cachedBluetoothDevice).sendToTarget();
        }
    }

    private final class BluetoothErrorListener implements BluetoothUtils.ErrorListener {
        private BluetoothErrorListener() {
        }

        public void onShowError(Context context, String str, int i) {
            KeyboardUI.this.mHandler.obtainMessage(11, i, 0, new Pair(context, str)).sendToTarget();
        }
    }

    private static String stateToString(int i) {
        switch (i) {
            case -1:
                return "STATE_NOT_ENABLED";
            case 1:
                return "STATE_WAITING_FOR_BOOT_COMPLETED";
            case 2:
                return "STATE_WAITING_FOR_TABLET_MODE_EXIT";
            case 3:
                return "STATE_WAITING_FOR_DEVICE_DISCOVERY";
            case 4:
                return "STATE_WAITING_FOR_BLUETOOTH";
            case 5:
                return "STATE_PAIRING";
            case 6:
                return "STATE_PAIRED";
            case 7:
                return "STATE_PAIRING_FAILED";
            case 8:
                return "STATE_USER_CANCELLED";
            case 9:
                return "STATE_DEVICE_NOT_FOUND";
            default:
                return "STATE_UNKNOWN (" + i + NavigationBarInflaterView.KEY_CODE_END;
        }
    }
}
