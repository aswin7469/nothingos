package android.bluetooth;

import android.graphics.FontListParser;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BleBroadcastSourceInfo implements Parcelable {
    private static final int BIS_NO_PREF = -1;
    public static final int BROADCASTER_ID_INVALID = 65535;
    @Deprecated
    public static final int BROADCAST_ASSIST_ADDRESS_TYPE_INVALID = 65535;
    @Deprecated
    public static final int BROADCAST_ASSIST_ADDRESS_TYPE_PUBLIC = 0;
    @Deprecated
    public static final int BROADCAST_ASSIST_ADDRESS_TYPE_RANDOM = 1;
    public static final int BROADCAST_ASSIST_AUDIO_SYNC_STATE_INVALID = 65535;
    public static final int BROADCAST_ASSIST_AUDIO_SYNC_STATE_NOT_SYNCHRONIZED = 0;
    public static final int BROADCAST_ASSIST_AUDIO_SYNC_STATE_SYNCHRONIZED = 1;
    public static final int BROADCAST_ASSIST_ENC_STATE_BADCODE = 3;
    public static final int BROADCAST_ASSIST_ENC_STATE_DECRYPTING = 2;
    public static final int BROADCAST_ASSIST_ENC_STATE_INVALID = 65535;
    public static final int BROADCAST_ASSIST_ENC_STATE_PIN_NEEDED = 1;
    public static final int BROADCAST_ASSIST_ENC_STATE_UNENCRYPTED = 0;
    public static final byte BROADCAST_ASSIST_INVALID_SOURCE_ID = 0;
    public static final int BROADCAST_ASSIST_PA_SYNC_STATE_IDLE = 0;
    public static final int BROADCAST_ASSIST_PA_SYNC_STATE_INVALID = 65535;
    public static final int BROADCAST_ASSIST_PA_SYNC_STATE_IN_SYNC = 2;
    public static final int BROADCAST_ASSIST_PA_SYNC_STATE_NO_PAST = 4;
    public static final int BROADCAST_ASSIST_PA_SYNC_STATE_SYNCINFO_REQ = 1;
    public static final int BROADCAST_ASSIST_PA_SYNC_STATE_SYNC_FAIL = 3;
    private static final int BROADCAST_CODE_SIZE = 16;
    public static final String EXTRA_MAX_NUM_SOURCE_INFOS = "android.bluetooth.device.extra.MAX_NUM_SOURCE_INFOS";
    public static final String EXTRA_SOURCE_INFO = "android.bluetooth.device.extra.SOURCE_INFO";
    public static final String EXTRA_SOURCE_INFO_INDEX = "android.bluetooth.device.extra.SOURCE_INFO_INDEX";
    private Map<Integer, Integer> mAudioBisIndexList;
    private int mAudioSyncState;
    private byte[] mBadBroadcastCode;
    private String mBroadcastCode;
    private int mBroadcasterId;
    private int mEncyptionStatus;
    private int mMetaDataSyncState;
    private Map<Integer, byte[]> mMetadataList;
    private byte mNumSubGroups;
    private int mSourceAddressType;
    private byte mSourceAdvSid;
    private BluetoothDevice mSourceDevice;
    private byte mSourceId;
    private static final String TAG = "BleBroadcastSourceInfo";
    private static final boolean BASS_DBG = Log.isLoggable(TAG, 2);
    public static final Parcelable.Creator<BleBroadcastSourceInfo> CREATOR = new Parcelable.Creator<BleBroadcastSourceInfo>() { // from class: android.bluetooth.BleBroadcastSourceInfo.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public BleBroadcastSourceInfo mo3559createFromParcel(Parcel in) {
            byte[] badBroadcastCode;
            BleBroadcastSourceInfo.BASS_Debug(BleBroadcastSourceInfo.TAG, "createFromParcel>");
            byte sourceId = in.readByte();
            int sourceAddressType = in.readInt();
            BluetoothDevice sourceDevice = (BluetoothDevice) in.readTypedObject(BluetoothDevice.CREATOR);
            byte sourceAdvSid = in.readByte();
            int broadcastId = in.readInt();
            BleBroadcastSourceInfo.BASS_Debug(BleBroadcastSourceInfo.TAG, "broadcastId" + broadcastId);
            int metaDataSyncState = in.readInt();
            int audioSyncState = in.readInt();
            BleBroadcastSourceInfo.BASS_Debug(BleBroadcastSourceInfo.TAG, "audioSyncState" + audioSyncState);
            int encyptionStatus = in.readInt();
            int badBroadcastLen = in.readInt();
            if (badBroadcastLen <= 0) {
                badBroadcastCode = null;
            } else {
                byte[] badBroadcastCode2 = new byte[badBroadcastLen];
                in.readByteArray(badBroadcastCode2);
                badBroadcastCode = badBroadcastCode2;
            }
            byte numSubGroups = in.readByte();
            String broadcastCode = in.readString();
            Map<Integer, Integer> bisIndexList = new HashMap<>();
            BleBroadcastSourceInfo.readMapFromParcel(in, bisIndexList);
            Map<Integer, byte[]> metadataList = new HashMap<>();
            BleBroadcastSourceInfo.readMetadataListFromParcel(in, metadataList);
            BleBroadcastSourceInfo srcInfo = new BleBroadcastSourceInfo(sourceDevice, sourceId, sourceAdvSid, broadcastId, sourceAddressType, metaDataSyncState, audioSyncState, encyptionStatus, broadcastCode, badBroadcastCode, numSubGroups, bisIndexList, metadataList);
            BleBroadcastSourceInfo.BASS_Debug(BleBroadcastSourceInfo.TAG, "createFromParcel:" + srcInfo);
            return srcInfo;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public BleBroadcastSourceInfo[] mo3560newArray(int size) {
            return new BleBroadcastSourceInfo[size];
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    @Deprecated
    /* loaded from: classes.dex */
    public @interface BroadcastAssistAddressType {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface BroadcastAssistAudioSyncState {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface BroadcastAssistEncryptionState {
    }

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface BroadcastAssistMetadataSyncState {
    }

    @Deprecated
    public BleBroadcastSourceInfo(byte sourceId) {
        this.mAudioBisIndexList = new HashMap();
        this.mMetadataList = new HashMap();
        this.mSourceId = sourceId;
        this.mMetaDataSyncState = 65535;
        this.mAudioSyncState = 65535;
        this.mSourceAddressType = 65535;
        this.mSourceDevice = null;
        this.mSourceAdvSid = (byte) 0;
        this.mEncyptionStatus = 65535;
        this.mBroadcastCode = null;
        this.mBadBroadcastCode = null;
        this.mNumSubGroups = (byte) 0;
        this.mBroadcasterId = 65535;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public BleBroadcastSourceInfo(BluetoothDevice audioSource, byte advSid, int addressType, int metadataSyncstate, int audioSyncstate, List<BleBroadcastSourceChannel> selectedBISIndicies) {
        this.mAudioBisIndexList = new HashMap();
        this.mMetadataList = new HashMap();
        this.mMetaDataSyncState = metadataSyncstate;
        this.mAudioSyncState = audioSyncstate;
        this.mSourceAddressType = addressType;
        this.mSourceDevice = audioSource;
        this.mSourceAdvSid = advSid;
        this.mBroadcasterId = 65535;
        if (selectedBISIndicies == null) {
            BASS_Debug(TAG, "selectedBISIndiciesList is null");
        } else {
            for (int i = 0; i < selectedBISIndicies.size(); i++) {
                if (selectedBISIndicies.get(i).getStatus()) {
                    Integer audioBisIndex = 0;
                    int subGroupId = selectedBISIndicies.get(i).getSubGroupId();
                    if (this.mAudioBisIndexList.containsKey(Integer.valueOf(subGroupId))) {
                        Integer audioBisIndex2 = this.mAudioBisIndexList.get(Integer.valueOf(subGroupId));
                        audioBisIndex = audioBisIndex2;
                    }
                    Integer audioBisIndex3 = Integer.valueOf((1 << selectedBISIndicies.get(i).getIndex()) | audioBisIndex.intValue());
                    BASS_Debug(TAG, FontListParser.ATTR_INDEX + selectedBISIndicies.get(i).getIndex() + "is set");
                    this.mAudioBisIndexList.put(Integer.valueOf(subGroupId), audioBisIndex3);
                }
            }
        }
        this.mSourceId = (byte) 0;
        this.mEncyptionStatus = 65535;
        this.mBroadcastCode = null;
        this.mBadBroadcastCode = null;
        this.mNumSubGroups = (byte) 0;
    }

    @Deprecated
    public BleBroadcastSourceInfo(BluetoothDevice audioSource, byte sourceId, byte advSid, int broadcasterId, int addressType, int metadataSyncstate, int encryptionStatus, byte[] badCode, byte numSubGroups, int audioSyncstate, Map<Integer, List<BleBroadcastSourceChannel>> selectedBISIndiciesList, Map<Integer, byte[]> metadataList) {
        this.mAudioBisIndexList = new HashMap();
        this.mMetadataList = new HashMap();
        this.mSourceId = sourceId;
        this.mSourceAddressType = addressType;
        this.mSourceDevice = audioSource;
        this.mSourceAdvSid = advSid;
        this.mBroadcasterId = broadcasterId;
        this.mMetaDataSyncState = metadataSyncstate;
        this.mAudioSyncState = audioSyncstate;
        this.mEncyptionStatus = encryptionStatus;
        if (badCode != null) {
            byte[] bArr = new byte[16];
            this.mBadBroadcastCode = bArr;
            System.arraycopy(badCode, 0, bArr, 0, bArr.length);
        }
        this.mNumSubGroups = numSubGroups;
        int audioBisIndex = 0;
        if (selectedBISIndiciesList != null) {
            for (Map.Entry<Integer, List<BleBroadcastSourceChannel>> entry : selectedBISIndiciesList.entrySet()) {
                List<BleBroadcastSourceChannel> selectedBISIndicies = entry.getValue();
                if (selectedBISIndicies == null) {
                    BASS_Debug(TAG, "selectedBISIndiciesList is null");
                } else {
                    for (int i = 0; i < selectedBISIndicies.size(); i++) {
                        if (selectedBISIndicies.get(i).getStatus()) {
                            audioBisIndex |= 1 << selectedBISIndicies.get(i).getIndex();
                            BASS_Debug(TAG, FontListParser.ATTR_INDEX + selectedBISIndicies.get(i).getIndex() + "is set");
                        }
                    }
                }
                BASS_Debug(TAG, "subGroupId:" + entry.getKey() + "audioBisIndex" + audioBisIndex);
                this.mAudioBisIndexList.put(entry.getKey(), Integer.valueOf(audioBisIndex));
            }
        }
        if (metadataList != null) {
            for (Map.Entry<Integer, byte[]> entry2 : metadataList.entrySet()) {
                byte[] metadata = entry2.getValue();
                if (metadata != null && metadata.length != 0) {
                    byte[] mD = new byte[metadata.length];
                    System.arraycopy(metadata, 0, mD, 0, metadata.length);
                }
                this.mMetadataList.put(entry2.getKey(), metadata);
            }
        }
    }

    BleBroadcastSourceInfo(BluetoothDevice device, byte sourceId, byte advSid, int addressType, int metadataSyncstate, int audioSyncstate, List<BleBroadcastSourceChannel> selectedBISIndicies, int encryptionStatus, String broadcastCode) {
        this.mAudioBisIndexList = new HashMap();
        this.mMetadataList = new HashMap();
        this.mSourceId = sourceId;
        this.mMetaDataSyncState = metadataSyncstate;
        this.mAudioSyncState = audioSyncstate;
        this.mEncyptionStatus = encryptionStatus;
        this.mSourceAddressType = addressType;
        this.mSourceDevice = device;
        this.mSourceAdvSid = advSid;
        this.mBroadcasterId = 65535;
        int i = 0;
        if (selectedBISIndicies == null) {
            BASS_Debug(TAG, "selectedBISIndiciesList is null");
        } else {
            int i2 = 0;
            while (i2 < selectedBISIndicies.size()) {
                if (selectedBISIndicies.get(i2).getStatus()) {
                    Integer audioBisIndex = Integer.valueOf(i);
                    int subGroupId = selectedBISIndicies.get(i2).getSubGroupId();
                    if (this.mAudioBisIndexList.containsKey(Integer.valueOf(subGroupId))) {
                        Integer audioBisIndex2 = this.mAudioBisIndexList.get(Integer.valueOf(subGroupId));
                        audioBisIndex = audioBisIndex2;
                    }
                    Integer audioBisIndex3 = Integer.valueOf(audioBisIndex.intValue() | (1 << selectedBISIndicies.get(i2).getIndex()));
                    BASS_Debug(TAG, FontListParser.ATTR_INDEX + selectedBISIndicies.get(i2).getIndex() + "is set");
                    StringBuilder sb = new StringBuilder();
                    sb.append("audioBisIndex");
                    sb.append(audioBisIndex3);
                    BASS_Debug(TAG, sb.toString());
                    this.mAudioBisIndexList.put(Integer.valueOf(subGroupId), audioBisIndex3);
                }
                i2++;
                i = 0;
            }
        }
        this.mBroadcastCode = broadcastCode;
        this.mBadBroadcastCode = null;
        this.mNumSubGroups = (byte) 0;
    }

    BleBroadcastSourceInfo(BluetoothDevice device, byte sourceId, byte advSid, int broadcasterId, int addressType, int metadataSyncstate, int audioSyncstate, int encryptionStatus, String broadcastCode, byte[] badCode, byte numSubGroups, Map<Integer, Integer> bisIndiciesList, Map<Integer, byte[]> metadataList) {
        this.mAudioBisIndexList = new HashMap();
        this.mMetadataList = new HashMap();
        this.mSourceId = sourceId;
        this.mMetaDataSyncState = metadataSyncstate;
        this.mAudioSyncState = audioSyncstate;
        this.mEncyptionStatus = encryptionStatus;
        this.mSourceAddressType = addressType;
        this.mSourceDevice = device;
        this.mSourceAdvSid = advSid;
        this.mBroadcasterId = broadcasterId;
        this.mBroadcastCode = broadcastCode;
        if (badCode != null && badCode.length != 0) {
            byte[] bArr = new byte[badCode.length];
            this.mBadBroadcastCode = bArr;
            System.arraycopy(badCode, 0, bArr, 0, badCode.length);
        }
        this.mNumSubGroups = numSubGroups;
        this.mAudioBisIndexList = new HashMap(bisIndiciesList);
        this.mMetadataList = new HashMap(metadataList);
    }

    public boolean equals(Object o) {
        if (o instanceof BleBroadcastSourceInfo) {
            BleBroadcastSourceInfo other = (BleBroadcastSourceInfo) o;
            BASS_Debug(TAG, "other>>  " + o.toString());
            BASS_Debug(TAG, "local>>  " + toString());
            return other.mSourceId == this.mSourceId && other.mMetaDataSyncState == this.mMetaDataSyncState && other.mAudioSyncState == this.mAudioSyncState && other.mSourceAddressType == this.mSourceAddressType && other.mSourceDevice == this.mSourceDevice && other.mSourceAdvSid == this.mSourceAdvSid && other.mEncyptionStatus == this.mEncyptionStatus && other.mBroadcastCode == this.mBroadcastCode && other.mBroadcasterId == this.mBroadcasterId;
        }
        return false;
    }

    public boolean isEmptyEntry() {
        boolean ret = false;
        if (this.mMetaDataSyncState == 65535 && this.mAudioSyncState == 65535 && this.mSourceAddressType == 65535 && this.mSourceDevice == null && this.mSourceAdvSid == 0 && this.mEncyptionStatus == 65535) {
            ret = true;
        }
        BASS_Debug(TAG, "isEmptyEntry returns: " + ret);
        return ret;
    }

    public boolean matches(BleBroadcastSourceInfo srcInfo) {
        boolean ret = false;
        if (srcInfo == null) {
            ret = false;
        } else {
            BluetoothDevice bluetoothDevice = this.mSourceDevice;
            if (bluetoothDevice == null) {
                if (this.mSourceAdvSid == srcInfo.getAdvertisingSid() && this.mSourceAddressType == srcInfo.getAdvAddressType()) {
                    ret = true;
                }
            } else if (bluetoothDevice.equals(srcInfo.getSourceDevice()) && this.mSourceAdvSid == srcInfo.getAdvertisingSid() && this.mSourceAddressType == srcInfo.getAdvAddressType() && this.mBroadcasterId == srcInfo.getBroadcasterId()) {
                ret = true;
            }
        }
        BASS_Debug(TAG, "matches returns: " + ret);
        return ret;
    }

    public int hashCode() {
        return Objects.hash(Byte.valueOf(this.mSourceId), Integer.valueOf(this.mMetaDataSyncState), Integer.valueOf(this.mAudioSyncState), Integer.valueOf(this.mSourceAddressType), this.mSourceDevice, Byte.valueOf(this.mSourceAdvSid), Integer.valueOf(this.mEncyptionStatus), this.mBroadcastCode);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "{BleBroadcastSourceInfo : mSourceId" + ((int) this.mSourceId) + " sourceDevice: " + this.mSourceDevice + " addressType: " + this.mSourceAddressType + " mSourceAdvSid:" + ((int) this.mSourceAdvSid) + " mMetaDataSyncState:" + this.mMetaDataSyncState + " mAudioSyncState" + this.mAudioSyncState + " mEncyptionStatus" + this.mEncyptionStatus + " mBadBroadcastCode" + this.mBadBroadcastCode + " mNumSubGroups" + ((int) this.mNumSubGroups) + " mBroadcastCode" + this.mBroadcastCode + " mAudioBisIndexList" + this.mAudioBisIndexList + " mMetadataList" + this.mMetadataList + " mBroadcasterId" + this.mBroadcasterId + "}";
    }

    public byte getSourceId() {
        return this.mSourceId;
    }

    public void setSourceId(byte sourceId) {
        this.mSourceId = sourceId;
    }

    public void setSourceDevice(BluetoothDevice sourceDevice) {
        this.mSourceDevice = sourceDevice;
    }

    public BluetoothDevice getSourceDevice() {
        return this.mSourceDevice;
    }

    public void setAdvAddressType(int addressType) {
        this.mSourceAddressType = addressType;
    }

    @Deprecated
    public int getAdvAddressType() {
        return this.mSourceAddressType;
    }

    public void setAdvertisingSid(byte advSid) {
        this.mSourceAdvSid = advSid;
    }

    public byte getAdvertisingSid() {
        return this.mSourceAdvSid;
    }

    public int getBroadcasterId() {
        return this.mBroadcasterId;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setMetadataSyncState(int metadataSyncState) {
        this.mMetaDataSyncState = metadataSyncState;
    }

    public int getMetadataSyncState() {
        return this.mMetaDataSyncState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAudioSyncState(int audioSyncState) {
        this.mAudioSyncState = audioSyncState;
    }

    public int getAudioSyncState() {
        return this.mAudioSyncState;
    }

    void setEncryptionStatus(int encryptionStatus) {
        this.mEncyptionStatus = encryptionStatus;
    }

    public int getEncryptionStatus() {
        return this.mEncyptionStatus;
    }

    public byte[] getBadBroadcastCode() {
        return this.mBadBroadcastCode;
    }

    @Deprecated
    public byte getNumberOfSubGroups() {
        return this.mNumSubGroups;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBroadcastChannelsSyncStatus(List<BleBroadcastSourceChannel> selectedBISIndicies) {
        if (selectedBISIndicies == null) {
            BASS_Debug(TAG, "selectedBISIndiciesList is null");
            return;
        }
        for (int i = 0; i < selectedBISIndicies.size(); i++) {
            if (selectedBISIndicies.get(i).getStatus()) {
                Integer audioBisIndex = 0;
                int subGroupId = selectedBISIndicies.get(i).getSubGroupId();
                if (this.mAudioBisIndexList.containsKey(Integer.valueOf(subGroupId))) {
                    Integer audioBisIndex2 = this.mAudioBisIndexList.get(Integer.valueOf(subGroupId));
                    audioBisIndex = audioBisIndex2;
                }
                Integer audioBisIndex3 = Integer.valueOf((1 << selectedBISIndicies.get(i).getIndex()) | audioBisIndex.intValue());
                BASS_Debug(TAG, FontListParser.ATTR_INDEX + selectedBISIndicies.get(i).getIndex() + "is set");
                this.mAudioBisIndexList.put(Integer.valueOf(subGroupId), audioBisIndex3);
            }
        }
    }

    public List<BleBroadcastSourceChannel> getBroadcastChannelsSyncStatus() {
        List<BleBroadcastSourceChannel> bcastIndicies = new ArrayList<>();
        for (int i = 0; i < this.mNumSubGroups; i++) {
            int bisIndexValue = this.mAudioBisIndexList.get(Integer.valueOf(i)).intValue();
            int bisIndexValue2 = bisIndexValue;
            int index = 0;
            while (bisIndexValue2 != 0) {
                if ((bisIndexValue2 & 1) == 1) {
                    BleBroadcastSourceChannel bI = new BleBroadcastSourceChannel(index, String.valueOf(index), true, i, this.mMetadataList.get(Integer.valueOf(i)));
                    bcastIndicies.add(bI);
                }
                bisIndexValue2 >>= 1;
                index++;
            }
        }
        BASS_Debug(TAG, "returning Bisindicies:" + bcastIndicies);
        return bcastIndicies;
    }

    @Deprecated
    public Map<Integer, Integer> getBisIndexList() {
        return this.mAudioBisIndexList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBroadcastCode(String broadcastCode) {
        this.mBroadcastCode = broadcastCode;
    }

    @Deprecated
    public void setBroadcasterId(int broadcasterId) {
        this.mBroadcasterId = broadcasterId;
    }

    @Deprecated
    public String getBroadcastCode() {
        return this.mBroadcastCode;
    }

    private void writeMapToParcel(Parcel dest, Map<Integer, Integer> bisIndexList) {
        dest.writeInt(bisIndexList.size());
        for (Map.Entry<Integer, Integer> entry : bisIndexList.entrySet()) {
            dest.writeInt(entry.getKey().intValue());
            dest.writeInt(entry.getValue().intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void readMapFromParcel(Parcel in, Map<Integer, Integer> bisIndexList) {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            Integer key = Integer.valueOf(in.readInt());
            Integer value = Integer.valueOf(in.readInt());
            bisIndexList.put(key, value);
        }
    }

    private void writeMetadataListToParcel(Parcel dest, Map<Integer, byte[]> metadataList) {
        dest.writeInt(metadataList.size());
        for (Map.Entry<Integer, byte[]> entry : metadataList.entrySet()) {
            dest.writeInt(entry.getKey().intValue());
            byte[] metadata = entry.getValue();
            if (metadata != null) {
                dest.writeInt(metadata.length);
                dest.writeByteArray(metadata);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void readMetadataListFromParcel(Parcel in, Map<Integer, byte[]> metadataList) {
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            Integer key = Integer.valueOf(in.readInt());
            Integer metaDataLen = Integer.valueOf(in.readInt());
            byte[] metadata = null;
            if (metaDataLen.intValue() != 0) {
                metadata = new byte[metaDataLen.intValue()];
                in.readByteArray(metadata);
            }
            metadataList.put(key, metadata);
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        BASS_Debug(TAG, "writeToParcel>");
        out.writeByte(this.mSourceId);
        out.writeInt(this.mSourceAddressType);
        out.writeTypedObject(this.mSourceDevice, 0);
        out.writeByte(this.mSourceAdvSid);
        out.writeInt(this.mBroadcasterId);
        out.writeInt(this.mMetaDataSyncState);
        out.writeInt(this.mAudioSyncState);
        out.writeInt(this.mEncyptionStatus);
        byte[] bArr = this.mBadBroadcastCode;
        if (bArr != null) {
            out.writeInt(bArr.length);
            out.writeByteArray(this.mBadBroadcastCode);
        } else {
            out.writeInt(0);
        }
        out.writeByte(this.mNumSubGroups);
        out.writeString(this.mBroadcastCode);
        writeMapToParcel(out, this.mAudioBisIndexList);
        writeMetadataListToParcel(out, this.mMetadataList);
        BASS_Debug(TAG, "writeToParcel:" + toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void BASS_Debug(String TAG2, String msg) {
        if (BASS_DBG) {
            Log.d(TAG2, msg);
        }
    }
}
