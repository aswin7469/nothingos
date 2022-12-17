package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothCodecConfig;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothUuid;
import android.content.Context;
import android.os.ParcelUuid;
import android.util.Log;
import com.android.settingslib.R$string;
import java.util.ArrayList;
import java.util.List;

public class A2dpProfile implements LocalBluetoothProfile {
    static final ParcelUuid[] SINK_UUIDS = {BluetoothUuid.A2DP_SINK, BluetoothUuid.ADV_AUDIO_DIST};

    /* renamed from: V */
    private static boolean f225V = true;
    private final BluetoothAdapter mBluetoothAdapter;
    private Context mContext;
    /* access modifiers changed from: private */
    public final CachedBluetoothDeviceManager mDeviceManager;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public final LocalBluetoothProfileManager mProfileManager;
    /* access modifiers changed from: private */
    public BluetoothA2dp mService;

    public boolean accessProfileEnabled() {
        return true;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 17302336;
    }

    public int getOrdinal() {
        return 1;
    }

    public int getProfileId() {
        return 2;
    }

    public String toString() {
        return "A2DP";
    }

    private final class A2dpServiceListener implements BluetoothProfile.ServiceListener {
        private A2dpServiceListener() {
        }

        public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
            A2dpProfile.this.mService = (BluetoothA2dp) bluetoothProfile;
            List<BluetoothDevice> connectedDevices = A2dpProfile.this.mService.getConnectedDevices();
            while (!connectedDevices.isEmpty()) {
                BluetoothDevice remove = connectedDevices.remove(0);
                CachedBluetoothDevice findDevice = A2dpProfile.this.mDeviceManager.findDevice(remove);
                if (findDevice == null) {
                    Log.w("A2dpProfile", "A2dpProfile found new device: " + remove);
                    findDevice = A2dpProfile.this.mDeviceManager.addDevice(remove);
                }
                findDevice.onProfileStateChanged(A2dpProfile.this, 2);
                findDevice.refresh();
            }
            A2dpProfile.this.mIsProfileReady = true;
            A2dpProfile.this.mProfileManager.callServiceConnectedListeners();
        }

        public void onServiceDisconnected(int i) {
            A2dpProfile.this.mIsProfileReady = false;
        }
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    A2dpProfile(Context context, CachedBluetoothDeviceManager cachedBluetoothDeviceManager, LocalBluetoothProfileManager localBluetoothProfileManager) {
        this.mContext = context;
        this.mDeviceManager = cachedBluetoothDeviceManager;
        this.mProfileManager = localBluetoothProfileManager;
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        this.mBluetoothAdapter = defaultAdapter;
        defaultAdapter.getProfileProxy(context, new A2dpServiceListener(), 2);
    }

    public List<BluetoothDevice> getConnectedDevices() {
        return getDevicesByStates(new int[]{2, 1, 3});
    }

    private List<BluetoothDevice> getDevicesByStates(int[] iArr) {
        BluetoothA2dp bluetoothA2dp = this.mService;
        if (bluetoothA2dp == null) {
            return new ArrayList(0);
        }
        return bluetoothA2dp.getDevicesMatchingConnectionStates(iArr);
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothA2dp bluetoothA2dp = this.mService;
        if (bluetoothA2dp == null) {
            return 0;
        }
        return bluetoothA2dp.getConnectionState(bluetoothDevice);
    }

    public boolean setActiveDevice(BluetoothDevice bluetoothDevice) {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return false;
        }
        if (bluetoothDevice == null) {
            return bluetoothAdapter.removeActiveDevice(0);
        }
        return bluetoothAdapter.setActiveDevice(bluetoothDevice, 0);
    }

    public BluetoothDevice getActiveDevice() {
        BluetoothAdapter bluetoothAdapter = this.mBluetoothAdapter;
        if (bluetoothAdapter == null) {
            return null;
        }
        List activeDevices = bluetoothAdapter.getActiveDevices(2);
        if (activeDevices.size() > 0) {
            return (BluetoothDevice) activeDevices.get(0);
        }
        return null;
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothA2dp bluetoothA2dp = this.mService;
        if (bluetoothA2dp != null && bluetoothA2dp.getConnectionPolicy(bluetoothDevice) > 0) {
            return true;
        }
        return false;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        BluetoothA2dp bluetoothA2dp = this.mService;
        if (bluetoothA2dp == null) {
            return false;
        }
        if (!z) {
            return bluetoothA2dp.setConnectionPolicy(bluetoothDevice, 0);
        }
        if (bluetoothA2dp.getConnectionPolicy(bluetoothDevice) < 100) {
            return this.mService.setConnectionPolicy(bluetoothDevice, 100);
        }
        return false;
    }

    public boolean supportsHighQualityAudio(BluetoothDevice bluetoothDevice) {
        if (f225V) {
            Log.d("A2dpProfile", " execute supportsHighQualityAudio()");
        }
        if (this.mService == null) {
            if (f225V) {
                Log.d("A2dpProfile", "mService is null.");
            }
            return false;
        }
        if (bluetoothDevice == null) {
            bluetoothDevice = getActiveDevice();
        }
        if (bluetoothDevice != null && this.mService.isOptionalCodecsSupported(bluetoothDevice) == 1) {
            return true;
        }
        return false;
    }

    public boolean isHighQualityAudioEnabled(BluetoothDevice bluetoothDevice) {
        if (f225V) {
            Log.d("A2dpProfile", " execute isHighQualityAudioEnabled()");
        }
        if (this.mService == null) {
            if (f225V) {
                Log.d("A2dpProfile", "mService is null.");
            }
            return false;
        }
        if (bluetoothDevice == null) {
            bluetoothDevice = getActiveDevice();
        }
        if (bluetoothDevice == null) {
            return false;
        }
        int isOptionalCodecsEnabled = this.mService.isOptionalCodecsEnabled(bluetoothDevice);
        if (isOptionalCodecsEnabled != -1) {
            if (isOptionalCodecsEnabled == 1) {
                return true;
            }
            return false;
        } else if (getConnectionStatus(bluetoothDevice) != 2 && supportsHighQualityAudio(bluetoothDevice)) {
            return true;
        } else {
            BluetoothCodecConfig bluetoothCodecConfig = null;
            if (this.mService.getCodecStatus(bluetoothDevice) != null) {
                bluetoothCodecConfig = this.mService.getCodecStatus(bluetoothDevice).getCodecConfig();
            }
            if (bluetoothCodecConfig != null) {
                return !bluetoothCodecConfig.isMandatoryCodec();
            }
            return false;
        }
    }

    public boolean isMandatoryCodec(BluetoothDevice bluetoothDevice) {
        if (f225V) {
            Log.d("A2dpProfile", " execute isMandatoryCodec()");
        }
        BluetoothA2dp bluetoothA2dp = this.mService;
        if (bluetoothA2dp == null) {
            if (f225V) {
                Log.d("A2dpProfile", "mService is null.");
            }
            return false;
        }
        if (bluetoothDevice == null) {
            bluetoothDevice = bluetoothA2dp.getActiveDevice();
        }
        if (bluetoothDevice == null) {
            return false;
        }
        BluetoothCodecConfig bluetoothCodecConfig = null;
        if (this.mService.getCodecStatus(bluetoothDevice) != null) {
            bluetoothCodecConfig = this.mService.getCodecStatus(bluetoothDevice).getCodecConfig();
        }
        if (bluetoothCodecConfig != null) {
            return bluetoothCodecConfig.isMandatoryCodec();
        }
        return false;
    }

    public void setHighQualityAudioEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        if (f225V) {
            Log.d("A2dpProfile", " execute setHighQualityAudioEnabled()");
        }
        if (this.mService != null) {
            if (bluetoothDevice == null) {
                bluetoothDevice = getActiveDevice();
            }
            if (bluetoothDevice != null) {
                this.mService.setOptionalCodecsEnabled(bluetoothDevice, z ? 1 : 0);
                if (getConnectionStatus(bluetoothDevice) == 2) {
                    if (z) {
                        this.mService.enableOptionalCodecs(bluetoothDevice);
                    } else {
                        this.mService.disableOptionalCodecs(bluetoothDevice);
                    }
                }
            }
        } else if (f225V) {
            Log.d("A2dpProfile", "mService is null.");
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r7v8, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v5, resolved type: android.bluetooth.BluetoothCodecConfig} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String getHighQualityAudioOptionLabel(android.bluetooth.BluetoothDevice r7) {
        /*
            r6 = this;
            boolean r0 = f225V
            if (r0 == 0) goto L_0x000b
            java.lang.String r0 = "A2dpProfile"
            java.lang.String r1 = " execute getHighQualityAudioOptionLabel()"
            android.util.Log.d(r0, r1)
        L_0x000b:
            if (r7 == 0) goto L_0x000f
            r0 = r7
            goto L_0x0013
        L_0x000f:
            android.bluetooth.BluetoothDevice r0 = r6.getActiveDevice()
        L_0x0013:
            int r1 = com.android.settingslib.R$string.bluetooth_profile_a2dp_high_quality_unknown_codec
            if (r0 == 0) goto L_0x00b3
            boolean r0 = r6.supportsHighQualityAudio(r7)
            if (r0 == 0) goto L_0x00b3
            int r0 = r6.getConnectionStatus(r7)
            r2 = 2
            if (r0 == r2) goto L_0x0026
            goto L_0x00b3
        L_0x0026:
            android.bluetooth.BluetoothA2dp r0 = r6.mService
            r3 = 0
            if (r0 == 0) goto L_0x0044
            android.bluetooth.BluetoothCodecStatus r0 = r0.getCodecStatus(r7)
            if (r0 == 0) goto L_0x0044
            android.bluetooth.BluetoothA2dp r0 = r6.mService
            android.bluetooth.BluetoothCodecStatus r7 = r0.getCodecStatus(r7)
            java.util.List r7 = r7.getCodecsSelectableCapabilities()
            com.android.settingslib.bluetooth.A2dpProfile$$ExternalSyntheticLambda0 r0 = new com.android.settingslib.bluetooth.A2dpProfile$$ExternalSyntheticLambda0
            r0.<init>()
            java.util.Collections.sort(r7, r0)
            goto L_0x0045
        L_0x0044:
            r7 = r3
        L_0x0045:
            r0 = 0
            r4 = 1
            if (r7 == 0) goto L_0x0057
            int r5 = r7.size()
            if (r5 >= r4) goto L_0x0050
            goto L_0x0057
        L_0x0050:
            java.lang.Object r7 = r7.get(r0)
            r3 = r7
            android.bluetooth.BluetoothCodecConfig r3 = (android.bluetooth.BluetoothCodecConfig) r3
        L_0x0057:
            if (r3 == 0) goto L_0x0065
            boolean r7 = r3.isMandatoryCodec()
            if (r7 == 0) goto L_0x0060
            goto L_0x0065
        L_0x0060:
            int r7 = r3.getCodecType()
            goto L_0x0068
        L_0x0065:
            r7 = 1000000(0xf4240, float:1.401298E-39)
        L_0x0068:
            r3 = -1
            r5 = 9
            switch(r7) {
                case 0: goto L_0x007f;
                case 1: goto L_0x0080;
                case 2: goto L_0x007d;
                case 3: goto L_0x007b;
                case 4: goto L_0x0079;
                case 5: goto L_0x0076;
                case 6: goto L_0x0074;
                case 7: goto L_0x0074;
                case 8: goto L_0x0074;
                case 9: goto L_0x0074;
                case 10: goto L_0x0072;
                case 11: goto L_0x0070;
                default: goto L_0x006e;
            }
        L_0x006e:
            r2 = r3
            goto L_0x0080
        L_0x0070:
            r2 = 7
            goto L_0x0080
        L_0x0072:
            r2 = 6
            goto L_0x0080
        L_0x0074:
            r2 = r5
            goto L_0x0080
        L_0x0076:
            r2 = 8
            goto L_0x0080
        L_0x0079:
            r2 = 5
            goto L_0x0080
        L_0x007b:
            r2 = 4
            goto L_0x0080
        L_0x007d:
            r2 = 3
            goto L_0x0080
        L_0x007f:
            r2 = r4
        L_0x0080:
            if (r2 >= 0) goto L_0x0089
            android.content.Context r6 = r6.mContext
            java.lang.String r6 = r6.getString(r1)
            return r6
        L_0x0089:
            if (r2 != r5) goto L_0x009a
            android.content.Context r6 = r6.mContext
            int r7 = com.android.settingslib.R$string.bluetooth_profile_a2dp_high_quality
            java.lang.Object[] r1 = new java.lang.Object[r4]
            java.lang.String r2 = "LHDC"
            r1[r0] = r2
            java.lang.String r6 = r6.getString(r7, r1)
            return r6
        L_0x009a:
            android.content.Context r6 = r6.mContext
            int r7 = com.android.settingslib.R$string.bluetooth_profile_a2dp_high_quality
            java.lang.Object[] r1 = new java.lang.Object[r4]
            android.content.res.Resources r3 = r6.getResources()
            int r4 = com.android.settingslib.R$array.bluetooth_a2dp_codec_titles
            java.lang.String[] r3 = r3.getStringArray(r4)
            r2 = r3[r2]
            r1[r0] = r2
            java.lang.String r6 = r6.getString(r7, r1)
            return r6
        L_0x00b3:
            android.content.Context r6 = r6.mContext
            java.lang.String r6 = r6.getString(r1)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.bluetooth.A2dpProfile.getHighQualityAudioOptionLabel(android.bluetooth.BluetoothDevice):java.lang.String");
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ int lambda$getHighQualityAudioOptionLabel$0(BluetoothCodecConfig bluetoothCodecConfig, BluetoothCodecConfig bluetoothCodecConfig2) {
        return bluetoothCodecConfig2.getCodecPriority() - bluetoothCodecConfig.getCodecPriority();
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return R$string.bluetooth_profile_a2dp;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d("A2dpProfile", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(2, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("A2dpProfile", "Error cleaning up A2DP proxy", th);
            }
        }
    }
}
