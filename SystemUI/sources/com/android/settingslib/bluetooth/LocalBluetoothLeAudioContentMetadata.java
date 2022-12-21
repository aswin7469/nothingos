package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothLeAudioContentMetadata;

public class LocalBluetoothLeAudioContentMetadata {
    private static final String TAG = "LocalBluetoothLeAudioContentMetadata";
    private final BluetoothLeAudioContentMetadata mContentMetadata;
    private final String mLanguage;
    private String mProgramInfo;
    private final byte[] mRawMetadata;

    LocalBluetoothLeAudioContentMetadata(BluetoothLeAudioContentMetadata bluetoothLeAudioContentMetadata) {
        this.mContentMetadata = bluetoothLeAudioContentMetadata;
        this.mProgramInfo = bluetoothLeAudioContentMetadata.getProgramInfo();
        this.mLanguage = bluetoothLeAudioContentMetadata.getLanguage();
        this.mRawMetadata = bluetoothLeAudioContentMetadata.getRawMetadata();
    }

    public void setProgramInfo(String str) {
        this.mProgramInfo = str;
    }

    public String getProgramInfo() {
        return this.mProgramInfo;
    }
}
