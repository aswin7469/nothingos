package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothLeAudioContentMetadata;
import android.bluetooth.BluetoothLeBroadcast;
import android.bluetooth.BluetoothLeBroadcastMetadata;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import com.android.settingslib.R$string;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class LocalBluetoothLeBroadcast implements LocalBluetoothProfile {
    private String mAppSourceName = "";
    private BluetoothLeAudioContentMetadata mBluetoothLeAudioContentMetadata;
    private BluetoothLeBroadcastMetadata mBluetoothLeBroadcastMetadata;
    /* access modifiers changed from: private */
    public final BluetoothLeBroadcast.Callback mBroadcastCallback;
    private byte[] mBroadcastCode;
    private int mBroadcastId = -1;
    private BluetoothLeAudioContentMetadata.Builder mBuilder;
    /* access modifiers changed from: private */
    public Executor mExecutor;
    /* access modifiers changed from: private */
    public boolean mIsProfileReady;
    /* access modifiers changed from: private */
    public String mNewAppSourceName = "";
    private String mProgramInfo;
    /* access modifiers changed from: private */
    public BluetoothLeBroadcast mService;
    private final BluetoothProfile.ServiceListener mServiceListener;
    private SharedPreferences mSharedPref;

    public boolean accessProfileEnabled() {
        return false;
    }

    public int getDrawableResource(BluetoothClass bluetoothClass) {
        return 0;
    }

    public int getOrdinal() {
        return 1;
    }

    public int getProfileId() {
        return 26;
    }

    public boolean setEnabled(BluetoothDevice bluetoothDevice, boolean z) {
        return false;
    }

    public String toString() {
        return "LE_AUDIO_BROADCAST";
    }

    LocalBluetoothLeBroadcast(Context context) {
        byte[] bArr;
        C15521 r1 = new BluetoothProfile.ServiceListener() {
            public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
                Log.d("LocalBluetoothLeBroadcast", "Bluetooth service connected");
                if (!LocalBluetoothLeBroadcast.this.mIsProfileReady) {
                    LocalBluetoothLeBroadcast.this.mService = (BluetoothLeBroadcast) bluetoothProfile;
                    LocalBluetoothLeBroadcast.this.mIsProfileReady = true;
                    LocalBluetoothLeBroadcast localBluetoothLeBroadcast = LocalBluetoothLeBroadcast.this;
                    localBluetoothLeBroadcast.registerServiceCallBack(localBluetoothLeBroadcast.mExecutor, LocalBluetoothLeBroadcast.this.mBroadcastCallback);
                }
            }

            public void onServiceDisconnected(int i) {
                Log.d("LocalBluetoothLeBroadcast", "Bluetooth service disconnected");
                if (LocalBluetoothLeBroadcast.this.mIsProfileReady) {
                    LocalBluetoothLeBroadcast.this.mIsProfileReady = false;
                    LocalBluetoothLeBroadcast localBluetoothLeBroadcast = LocalBluetoothLeBroadcast.this;
                    localBluetoothLeBroadcast.unregisterServiceCallBack(localBluetoothLeBroadcast.mBroadcastCallback);
                }
            }
        };
        this.mServiceListener = r1;
        this.mBroadcastCallback = new BluetoothLeBroadcast.Callback() {
            public void onPlaybackStarted(int i, int i2) {
            }

            public void onPlaybackStopped(int i, int i2) {
            }

            public void onBroadcastStarted(int i, int i2) {
                Log.d("LocalBluetoothLeBroadcast", "onBroadcastStarted(), reason = " + i + ", broadcastId = " + i2);
                LocalBluetoothLeBroadcast.this.setLatestBroadcastId(i2);
                LocalBluetoothLeBroadcast localBluetoothLeBroadcast = LocalBluetoothLeBroadcast.this;
                localBluetoothLeBroadcast.setAppSourceName(localBluetoothLeBroadcast.mNewAppSourceName);
            }

            public void onBroadcastStartFailed(int i) {
                Log.d("LocalBluetoothLeBroadcast", "onBroadcastStartFailed(), reason = " + i);
            }

            public void onBroadcastMetadataChanged(int i, BluetoothLeBroadcastMetadata bluetoothLeBroadcastMetadata) {
                Log.d("LocalBluetoothLeBroadcast", "onBroadcastMetadataChanged(), broadcastId = " + i);
                LocalBluetoothLeBroadcast.this.setLatestBluetoothLeBroadcastMetadata(bluetoothLeBroadcastMetadata);
            }

            public void onBroadcastStopped(int i, int i2) {
                Log.d("LocalBluetoothLeBroadcast", "onBroadcastStopped(), reason = " + i + ", broadcastId = " + i2);
                LocalBluetoothLeBroadcast.this.resetCacheInfo();
            }

            public void onBroadcastStopFailed(int i) {
                Log.d("LocalBluetoothLeBroadcast", "onBroadcastStopFailed(), reason = " + i);
            }

            public void onBroadcastUpdated(int i, int i2) {
                Log.d("LocalBluetoothLeBroadcast", "onBroadcastUpdated(), reason = " + i + ", broadcastId = " + i2);
                LocalBluetoothLeBroadcast.this.setLatestBroadcastId(i2);
                LocalBluetoothLeBroadcast localBluetoothLeBroadcast = LocalBluetoothLeBroadcast.this;
                localBluetoothLeBroadcast.setAppSourceName(localBluetoothLeBroadcast.mNewAppSourceName);
            }

            public void onBroadcastUpdateFailed(int i, int i2) {
                Log.d("LocalBluetoothLeBroadcast", "onBroadcastUpdateFailed(), reason = " + i + ", broadcastId = " + i2);
            }
        };
        this.mExecutor = Executors.newSingleThreadExecutor();
        BluetoothAdapter.getDefaultAdapter().getProfileProxy(context, r1, 26);
        this.mBuilder = new BluetoothLeAudioContentMetadata.Builder();
        SharedPreferences sharedPreferences = context.getSharedPreferences("LocalBluetoothLeBroadcast", 0);
        this.mSharedPref = sharedPreferences;
        if (sharedPreferences != null) {
            String string = sharedPreferences.getString("PrefProgramInfo", "");
            setProgramInfo(string.isEmpty() ? getDefaultValueOfProgramInfo() : string);
            String string2 = this.mSharedPref.getString("PrefBroadcastCode", "");
            if (string2.isEmpty()) {
                bArr = getDefaultValueOfBroadcastCode();
            } else {
                bArr = string2.getBytes(StandardCharsets.UTF_8);
            }
            setBroadcastCode(bArr);
            this.mAppSourceName = this.mSharedPref.getString("PrefAppSourceName", "");
        }
    }

    public void startBroadcast(String str, String str2) {
        this.mNewAppSourceName = str;
        if (this.mService == null) {
            Log.d("LocalBluetoothLeBroadcast", "The BluetoothLeBroadcast is null when starting the broadcast.");
            return;
        }
        Log.d("LocalBluetoothLeBroadcast", "startBroadcast: language = " + str2 + " ,programInfo = " + this.mProgramInfo);
        buildContentMetadata(str2, this.mProgramInfo);
        this.mService.startBroadcast(this.mBluetoothLeAudioContentMetadata, this.mBroadcastCode);
    }

    public void setProgramInfo(String str) {
        if (str == null || str.isEmpty()) {
            Log.d("LocalBluetoothLeBroadcast", "setProgramInfo: programInfo is null or empty");
            return;
        }
        Log.d("LocalBluetoothLeBroadcast", "setProgramInfo: " + str);
        this.mProgramInfo = str;
        SharedPreferences sharedPreferences = this.mSharedPref;
        if (sharedPreferences == null) {
            Log.d("LocalBluetoothLeBroadcast", "setProgramInfo: sharedPref is null");
            return;
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("PrefProgramInfo", this.mProgramInfo);
        edit.apply();
    }

    public void setBroadcastCode(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            Log.d("LocalBluetoothLeBroadcast", "setBroadcastCode: broadcastCode is null or empty");
            return;
        }
        this.mBroadcastCode = bArr;
        SharedPreferences sharedPreferences = this.mSharedPref;
        if (sharedPreferences == null) {
            Log.d("LocalBluetoothLeBroadcast", "setBroadcastCode: sharedPref is null");
            return;
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("PrefBroadcastCode", new String(bArr, StandardCharsets.UTF_8));
        edit.apply();
    }

    /* access modifiers changed from: private */
    public void setLatestBroadcastId(int i) {
        this.mBroadcastId = i;
    }

    /* access modifiers changed from: private */
    public void setAppSourceName(String str) {
        if (TextUtils.isEmpty(str)) {
            str = "";
        }
        this.mAppSourceName = str;
        this.mNewAppSourceName = "";
        SharedPreferences sharedPreferences = this.mSharedPref;
        if (sharedPreferences == null) {
            Log.d("LocalBluetoothLeBroadcast", "setBroadcastCode: sharedPref is null");
            return;
        }
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("PrefAppSourceName", str);
        edit.apply();
    }

    /* access modifiers changed from: private */
    public void setLatestBluetoothLeBroadcastMetadata(BluetoothLeBroadcastMetadata bluetoothLeBroadcastMetadata) {
        if (bluetoothLeBroadcastMetadata != null && bluetoothLeBroadcastMetadata.getBroadcastId() == this.mBroadcastId) {
            this.mBluetoothLeBroadcastMetadata = bluetoothLeBroadcastMetadata;
        }
    }

    public void registerServiceCallBack(Executor executor, BluetoothLeBroadcast.Callback callback) {
        BluetoothLeBroadcast bluetoothLeBroadcast = this.mService;
        if (bluetoothLeBroadcast == null) {
            Log.d("LocalBluetoothLeBroadcast", "The BluetoothLeBroadcast is null.");
        } else {
            bluetoothLeBroadcast.registerCallback(executor, callback);
        }
    }

    public void unregisterServiceCallBack(BluetoothLeBroadcast.Callback callback) {
        BluetoothLeBroadcast bluetoothLeBroadcast = this.mService;
        if (bluetoothLeBroadcast == null) {
            Log.d("LocalBluetoothLeBroadcast", "The BluetoothLeBroadcast is null.");
        } else {
            bluetoothLeBroadcast.unregisterCallback(callback);
        }
    }

    private void buildContentMetadata(String str, String str2) {
        this.mBluetoothLeAudioContentMetadata = this.mBuilder.setLanguage(str).setProgramInfo(str2).build();
    }

    public boolean isProfileReady() {
        return this.mIsProfileReady;
    }

    public int getConnectionStatus(BluetoothDevice bluetoothDevice) {
        BluetoothLeBroadcast bluetoothLeBroadcast = this.mService;
        if (bluetoothLeBroadcast == null) {
            return 0;
        }
        return bluetoothLeBroadcast.getConnectionState(bluetoothDevice);
    }

    public boolean isEnabled(BluetoothDevice bluetoothDevice) {
        BluetoothLeBroadcast bluetoothLeBroadcast = this.mService;
        if (bluetoothLeBroadcast == null) {
            return false;
        }
        return !bluetoothLeBroadcast.getAllBroadcastMetadata().isEmpty();
    }

    public int getNameResource(BluetoothDevice bluetoothDevice) {
        return R$string.summary_empty;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d("LocalBluetoothLeBroadcast", "finalize()");
        if (this.mService != null) {
            try {
                BluetoothAdapter.getDefaultAdapter().closeProfileProxy(26, this.mService);
                this.mService = null;
            } catch (Throwable th) {
                Log.w("LocalBluetoothLeBroadcast", "Error cleaning up LeAudio proxy", th);
            }
        }
    }

    private String getDefaultValueOfProgramInfo() {
        int nextInt = ThreadLocalRandom.current().nextInt(1000, 9999);
        return BluetoothAdapter.getDefaultAdapter().getName() + "_" + nextInt;
    }

    private byte[] getDefaultValueOfBroadcastCode() {
        return generateRandomPassword().getBytes(StandardCharsets.UTF_8);
    }

    /* access modifiers changed from: private */
    public void resetCacheInfo() {
        Log.d("LocalBluetoothLeBroadcast", "resetCacheInfo:");
        this.mNewAppSourceName = "";
        this.mAppSourceName = "";
        this.mBluetoothLeBroadcastMetadata = null;
        this.mBroadcastId = -1;
    }

    private String generateRandomPassword() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8) + uuid.substring(9, 13);
    }
}
