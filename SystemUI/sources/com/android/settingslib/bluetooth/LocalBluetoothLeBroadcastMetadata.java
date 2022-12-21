package com.android.settingslib.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothLeAudioCodecConfigMetadata;
import android.bluetooth.BluetoothLeAudioContentMetadata;
import android.bluetooth.BluetoothLeBroadcastChannel;
import android.bluetooth.BluetoothLeBroadcastMetadata;
import android.bluetooth.BluetoothLeBroadcastSubgroup;
import android.util.Log;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalBluetoothLeBroadcastMetadata {
    private static final boolean DEBUG = true;
    private static final String METADATA_END = ">";
    private static final String METADATA_START = "<";
    private static final String PATTERN_REGEX = "<(.*?)>";
    private static final String TAG = "LocalBluetoothLeBroadcastMetadata";
    private long mAudioLocation;
    private byte[] mBroadcastCode;
    private int mBroadcastId;
    private BluetoothLeBroadcastChannel mChannel;
    private int mChannelIndex;
    private long mCodecId;
    private BluetoothLeAudioCodecConfigMetadata mConfigMetadata;
    private BluetoothLeAudioContentMetadata mContentMetadata;
    private boolean mIsEncrypted;
    private boolean mIsSelected;
    private String mLanguage;
    private int mPaSyncInterval;
    private int mPresentationDelayMicros;
    private String mProgramInfo;
    private int mSourceAddressType;
    private int mSourceAdvertisingSid;
    private BluetoothDevice mSourceDevice;
    private BluetoothLeBroadcastSubgroup mSubgroup;
    private List<BluetoothLeBroadcastSubgroup> mSubgroupList;

    LocalBluetoothLeBroadcastMetadata(BluetoothLeBroadcastMetadata bluetoothLeBroadcastMetadata) {
        this.mSourceAddressType = bluetoothLeBroadcastMetadata.getSourceAddressType();
        this.mSourceDevice = bluetoothLeBroadcastMetadata.getSourceDevice();
        this.mSourceAdvertisingSid = bluetoothLeBroadcastMetadata.getSourceAdvertisingSid();
        this.mBroadcastId = bluetoothLeBroadcastMetadata.getBroadcastId();
        this.mPaSyncInterval = bluetoothLeBroadcastMetadata.getPaSyncInterval();
        this.mIsEncrypted = bluetoothLeBroadcastMetadata.isEncrypted();
        this.mBroadcastCode = bluetoothLeBroadcastMetadata.getBroadcastCode();
        this.mPresentationDelayMicros = bluetoothLeBroadcastMetadata.getPresentationDelayMicros();
        this.mSubgroupList = bluetoothLeBroadcastMetadata.getSubgroups();
    }

    public LocalBluetoothLeBroadcastMetadata() {
    }

    public void setBroadcastCode(byte[] bArr) {
        this.mBroadcastCode = bArr;
    }

    public int getBroadcastId() {
        return this.mBroadcastId;
    }

    public String convertToQrCodeString() {
        return "BT:T:<" + this.mSourceAddressType + ">;D:<" + this.mSourceDevice + ">;AS:<" + this.mSourceAdvertisingSid + ">;B:<" + this.mBroadcastId + ">;SI:<" + this.mPaSyncInterval + ">;E:<" + this.mIsEncrypted + ">;C:<" + Arrays.toString(this.mBroadcastCode) + ">;D:<" + this.mPresentationDelayMicros + ">;G:<" + convertSubgroupToString(this.mSubgroupList) + ">;";
    }

    private String convertSubgroupToString(List<BluetoothLeBroadcastSubgroup> list) {
        StringBuilder sb = new StringBuilder();
        for (BluetoothLeBroadcastSubgroup next : list) {
            String convertAudioCodecConfigToString = convertAudioCodecConfigToString(next.getCodecSpecificConfig());
            String convertAudioContentToString = convertAudioContentToString(next.getContentMetadata());
            sb.append("CID:<" + next.getCodecId() + ">;CC:<" + convertAudioCodecConfigToString + ">;AC:<" + convertAudioContentToString + ">;BC:<" + convertChannelToString(next.getChannels()) + ">;");
        }
        return sb.toString();
    }

    private String convertAudioCodecConfigToString(BluetoothLeAudioCodecConfigMetadata bluetoothLeAudioCodecConfigMetadata) {
        String valueOf = String.valueOf(bluetoothLeAudioCodecConfigMetadata.getAudioLocation());
        return "AL:<" + valueOf + ">;CCRM:<" + new String(bluetoothLeAudioCodecConfigMetadata.getRawMetadata(), StandardCharsets.UTF_8) + ">;";
    }

    private String convertAudioContentToString(BluetoothLeAudioContentMetadata bluetoothLeAudioContentMetadata) {
        return "PI:<" + bluetoothLeAudioContentMetadata.getProgramInfo() + ">;L:<" + bluetoothLeAudioContentMetadata.getLanguage() + ">;ACRM:<" + new String(bluetoothLeAudioContentMetadata.getRawMetadata(), StandardCharsets.UTF_8) + ">;";
    }

    private String convertChannelToString(List<BluetoothLeBroadcastChannel> list) {
        StringBuilder sb = new StringBuilder();
        for (BluetoothLeBroadcastChannel next : list) {
            sb.append("CI:<" + next.getChannelIndex() + ">;BCCM:<" + convertAudioCodecConfigToString(next.getCodecMetadata()) + ">;");
        }
        return sb.toString();
    }

    public BluetoothLeBroadcastMetadata convertToBroadcastMetadata(String str) {
        Log.d(TAG, "Convert " + str + "to BluetoothLeBroadcastMetadata");
        Matcher matcher = Pattern.compile(PATTERN_REGEX).matcher(str);
        if (matcher.find()) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(matcher.group(1));
            this.mSourceAddressType = Integer.parseInt((String) arrayList.get(0));
            this.mSourceDevice = BluetoothAdapter.getDefaultAdapter().getRemoteDevice((String) arrayList.get(1));
            this.mSourceAdvertisingSid = Integer.parseInt((String) arrayList.get(2));
            this.mBroadcastId = Integer.parseInt((String) arrayList.get(3));
            this.mPaSyncInterval = Integer.parseInt((String) arrayList.get(4));
            this.mIsEncrypted = Boolean.valueOf((String) arrayList.get(5)).booleanValue();
            this.mBroadcastCode = ((String) arrayList.get(6)).getBytes();
            this.mPresentationDelayMicros = Integer.parseInt((String) arrayList.get(7));
            this.mSubgroup = convertToSubgroup((String) arrayList.get(8));
            Log.d(TAG, "Converted qrCodeString result: " + matcher.group());
            return new BluetoothLeBroadcastMetadata.Builder().setSourceDevice(this.mSourceDevice, this.mSourceAddressType).setSourceAdvertisingSid(this.mSourceAdvertisingSid).setBroadcastId(this.mBroadcastId).setPaSyncInterval(this.mPaSyncInterval).setEncrypted(this.mIsEncrypted).setBroadcastCode(this.mBroadcastCode).setPresentationDelayMicros(this.mPresentationDelayMicros).addSubgroup(this.mSubgroup).build();
        }
        Log.d(TAG, "The match fail, can not convert it to BluetoothLeBroadcastMetadata.");
        return null;
    }

    private BluetoothLeBroadcastSubgroup convertToSubgroup(String str) {
        Log.d(TAG, "Convert " + str + "to BluetoothLeBroadcastSubgroup");
        Matcher matcher = Pattern.compile(PATTERN_REGEX).matcher(str);
        if (matcher.find()) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(matcher.group(1));
            this.mCodecId = Long.getLong((String) arrayList.get(0)).longValue();
            this.mConfigMetadata = convertToConfigMetadata((String) arrayList.get(1));
            this.mContentMetadata = convertToContentMetadata((String) arrayList.get(2));
            this.mChannel = convertToChannel((String) arrayList.get(3), this.mConfigMetadata);
            Log.d(TAG, "Converted subgroupString result: " + matcher.group());
            return new BluetoothLeBroadcastSubgroup.Builder().setCodecId(this.mCodecId).setCodecSpecificConfig(this.mConfigMetadata).setContentMetadata(this.mContentMetadata).addChannel(this.mChannel).build();
        }
        Log.d(TAG, "The match fail, can not convert it to BluetoothLeBroadcastSubgroup.");
        return null;
    }

    private BluetoothLeAudioCodecConfigMetadata convertToConfigMetadata(String str) {
        Log.d(TAG, "Convert " + str + "to BluetoothLeAudioCodecConfigMetadata");
        Matcher matcher = Pattern.compile(PATTERN_REGEX).matcher(str);
        if (matcher.find()) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(matcher.group(1));
            this.mAudioLocation = Long.getLong((String) arrayList.get(0)).longValue();
            Log.d(TAG, "Converted configMetadataString result: " + matcher.group());
            return new BluetoothLeAudioCodecConfigMetadata.Builder().setAudioLocation(this.mAudioLocation).build();
        }
        Log.d(TAG, "The match fail, can not convert it to BluetoothLeAudioCodecConfigMetadata.");
        return null;
    }

    private BluetoothLeAudioContentMetadata convertToContentMetadata(String str) {
        Log.d(TAG, "Convert " + str + "to BluetoothLeAudioContentMetadata");
        Matcher matcher = Pattern.compile(PATTERN_REGEX).matcher(str);
        if (matcher.find()) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(matcher.group(1));
            this.mProgramInfo = (String) arrayList.get(0);
            this.mLanguage = (String) arrayList.get(1);
            Log.d(TAG, "Converted contentMetadataString result: " + matcher.group());
            return new BluetoothLeAudioContentMetadata.Builder().setProgramInfo(this.mProgramInfo).setLanguage(this.mLanguage).build();
        }
        Log.d(TAG, "The match fail, can not convert it to BluetoothLeAudioContentMetadata.");
        return null;
    }

    private BluetoothLeBroadcastChannel convertToChannel(String str, BluetoothLeAudioCodecConfigMetadata bluetoothLeAudioCodecConfigMetadata) {
        Log.d(TAG, "Convert " + str + "to BluetoothLeBroadcastChannel");
        Matcher matcher = Pattern.compile(PATTERN_REGEX).matcher(str);
        if (matcher.find()) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(matcher.group(1));
            this.mIsSelected = Boolean.valueOf((String) arrayList.get(0)).booleanValue();
            this.mChannelIndex = Integer.parseInt((String) arrayList.get(1));
            Log.d(TAG, "Converted channelString result: " + matcher.group());
            return new BluetoothLeBroadcastChannel.Builder().setSelected(this.mIsSelected).setChannelIndex(this.mChannelIndex).setCodecMetadata(bluetoothLeAudioCodecConfigMetadata).build();
        }
        Log.d(TAG, "The match fail, can not convert it to BluetoothLeBroadcastChannel.");
        return null;
    }
}
