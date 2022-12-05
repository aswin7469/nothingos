package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
/* loaded from: classes.dex */
public final class BluetoothQualityReport implements Parcelable {
    public static final Parcelable.Creator<BluetoothQualityReport> CREATOR = new Parcelable.Creator<BluetoothQualityReport>() { // from class: android.bluetooth.BluetoothQualityReport.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public BluetoothQualityReport mo3559createFromParcel(Parcel in) {
            return new BluetoothQualityReport(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public BluetoothQualityReport[] mo3560newArray(int size) {
            return new BluetoothQualityReport[size];
        }
    };
    public static final int QUALITY_REPORT_ID_A2DP_CHOPPY = 3;
    public static final int QUALITY_REPORT_ID_APPROACH_LSTO = 2;
    public static final int QUALITY_REPORT_ID_CONN_FAIL = 32;
    public static final int QUALITY_REPORT_ID_MONITOR = 1;
    public static final int QUALITY_REPORT_ID_SCO_CHOPPY = 4;
    private static final String TAG = "BluetoothQualityReport";
    private String mAddr;
    private int mBluetoothClass;
    private BqrCommon mBqrCommon;
    private BqrVsA2dpChoppy mBqrVsA2dpChoppy;
    private BqrVsCommon mBqrVsCommon;
    private BqrVsConnectFail mBqrVsConnectFail;
    private BqrVsLsto mBqrVsLsto;
    private BqrVsScoChoppy mBqrVsScoChoppy;
    private int mLmpSubVer;
    private int mLmpVer;
    private int mManufacturerId;
    private String mName;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum PacketType {
        INVALID,
        TYPE_ID,
        TYPE_NULL,
        TYPE_POLL,
        TYPE_FHS,
        TYPE_HV1,
        TYPE_HV2,
        TYPE_HV3,
        TYPE_DV,
        TYPE_EV3,
        TYPE_EV4,
        TYPE_EV5,
        TYPE_2EV3,
        TYPE_2EV5,
        TYPE_3EV3,
        TYPE_3EV5,
        TYPE_DM1,
        TYPE_DH1,
        TYPE_DM3,
        TYPE_DH3,
        TYPE_DM5,
        TYPE_DH5,
        TYPE_AUX1,
        TYPE_2DH1,
        TYPE_2DH3,
        TYPE_2DH5,
        TYPE_3DH1,
        TYPE_3DH3,
        TYPE_3DH5;
        
        private static PacketType[] sAllValues = values();

        static PacketType fromOrdinal(int n) {
            PacketType[] packetTypeArr = sAllValues;
            if (n < packetTypeArr.length) {
                return packetTypeArr[n];
            }
            return INVALID;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum ConnState {
        CONN_IDLE(0),
        CONN_ACTIVE(129),
        CONN_HOLD(2),
        CONN_SNIFF_IDLE(3),
        CONN_SNIFF_ACTIVE(132),
        CONN_SNIFF_MASTER_TRANSITION(133),
        CONN_PARK(6),
        CONN_PARK_PEND(71),
        CONN_UNPARK_PEND(8),
        CONN_UNPARK_ACTIVE(137),
        CONN_DISCONNECT_PENDING(74),
        CONN_PAGING(11),
        CONN_PAGE_SCAN(12),
        CONN_LOCAL_LOOPBACK(13),
        CONN_LE_ACTIVE(14),
        CONN_ANT_ACTIVE(15),
        CONN_TRIGGER_SCAN(16),
        CONN_RECONNECTING(17),
        CONN_SEMI_CONN(18);
        
        private static ConnState[] sAllStates = values();
        private int mValue;

        ConnState(int val) {
            this.mValue = val;
        }

        public static String getName(int val) {
            ConnState[] connStateArr;
            for (ConnState state : sAllStates) {
                if (state.mValue == val) {
                    return state.toString();
                }
            }
            return "INVALID";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum LinkQuality {
        ULTRA_HIGH,
        HIGH,
        STANDARD,
        MEDIUM,
        LOW,
        INVALID;
        
        private static LinkQuality[] sAllValues = values();

        static LinkQuality fromOrdinal(int n) {
            LinkQuality[] linkQualityArr = sAllValues;
            if (n < linkQualityArr.length - 1) {
                return linkQualityArr[n];
            }
            return INVALID;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public enum AirMode {
        uLaw,
        aLaw,
        CVSD,
        transparent_msbc,
        INVALID;
        
        private static AirMode[] sAllValues = values();

        static AirMode fromOrdinal(int n) {
            AirMode[] airModeArr = sAllValues;
            if (n < airModeArr.length - 1) {
                return airModeArr[n];
            }
            return INVALID;
        }
    }

    public BluetoothQualityReport(String remoteAddr, int lmpVer, int lmpSubVer, int manufacturerId, String remoteName, int remoteCoD, byte[] rawData) {
        if (!BluetoothAdapter.checkBluetoothAddress(remoteAddr)) {
            Log.d(TAG, "remote addr is invalid");
            this.mAddr = "00:00:00:00:00:00";
        } else {
            this.mAddr = remoteAddr;
        }
        this.mLmpVer = lmpVer;
        this.mLmpSubVer = lmpSubVer;
        this.mManufacturerId = manufacturerId;
        if (remoteName == null) {
            Log.d(TAG, "remote name is null");
            this.mName = "";
        } else {
            this.mName = remoteName;
        }
        this.mBluetoothClass = remoteCoD;
        this.mBqrCommon = new BqrCommon(rawData, 0);
        this.mBqrVsCommon = new BqrVsCommon(rawData, 48);
        int id = this.mBqrCommon.getQualityReportId();
        if (id == 1) {
            return;
        }
        int vsPartOffset = this.mBqrVsCommon.getLength() + 48;
        if (id == 2) {
            this.mBqrVsLsto = new BqrVsLsto(rawData, vsPartOffset);
        } else if (id == 3) {
            this.mBqrVsA2dpChoppy = new BqrVsA2dpChoppy(rawData, vsPartOffset);
        } else if (id == 4) {
            this.mBqrVsScoChoppy = new BqrVsScoChoppy(rawData, vsPartOffset);
        } else if (id == 32) {
            this.mBqrVsConnectFail = new BqrVsConnectFail(rawData, vsPartOffset);
        } else {
            throw new IllegalArgumentException("BluetoothQualityReport: unkown quality report id:" + id);
        }
    }

    private BluetoothQualityReport(Parcel in) {
        this.mBqrCommon = new BqrCommon(in);
        this.mAddr = in.readString();
        this.mLmpVer = in.readInt();
        this.mLmpSubVer = in.readInt();
        this.mManufacturerId = in.readInt();
        this.mName = in.readString();
        this.mBluetoothClass = in.readInt();
        this.mBqrVsCommon = new BqrVsCommon(in);
        int id = this.mBqrCommon.getQualityReportId();
        if (id == 2) {
            this.mBqrVsLsto = new BqrVsLsto(in);
        } else if (id == 3) {
            this.mBqrVsA2dpChoppy = new BqrVsA2dpChoppy(in);
        } else if (id == 4) {
            this.mBqrVsScoChoppy = new BqrVsScoChoppy(in);
        } else if (id == 32) {
            this.mBqrVsConnectFail = new BqrVsConnectFail(in);
        }
    }

    public int getQualityReportId() {
        return this.mBqrCommon.getQualityReportId();
    }

    public String getQualityReportIdStr() {
        int id = this.mBqrCommon.getQualityReportId();
        switch (id) {
            case 1:
                return "Quality monitor";
            case 2:
                return "Approaching LSTO";
            case 3:
                return "A2DP choppy";
            case 4:
                return "SCO choppy";
            case 32:
                return "Connect fail";
            default:
                return "INVALID";
        }
    }

    public String getAddress() {
        return this.mAddr;
    }

    public int getLmpVersion() {
        return this.mLmpVer;
    }

    public int getLmpSubVersion() {
        return this.mLmpSubVer;
    }

    public int getManufacturerId() {
        return this.mManufacturerId;
    }

    public String getName() {
        return this.mName;
    }

    public int getBluetoothClass() {
        return this.mBluetoothClass;
    }

    public BqrCommon getBqrCommon() {
        return this.mBqrCommon;
    }

    public BqrVsCommon getBqrVsCommon() {
        return this.mBqrVsCommon;
    }

    public BqrVsLsto getBqrVsLsto() {
        return this.mBqrVsLsto;
    }

    public BqrVsA2dpChoppy getBqrVsA2dpChoppy() {
        return this.mBqrVsA2dpChoppy;
    }

    public BqrVsScoChoppy getBqrVsScoChoppy() {
        return this.mBqrVsScoChoppy;
    }

    public BqrVsConnectFail getBqrVsConnectFail() {
        return this.mBqrVsConnectFail;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        this.mBqrCommon.writeToParcel(out, flags);
        out.writeString(this.mAddr);
        out.writeInt(this.mLmpVer);
        out.writeInt(this.mLmpSubVer);
        out.writeInt(this.mManufacturerId);
        out.writeString(this.mName);
        out.writeInt(this.mBluetoothClass);
        this.mBqrVsCommon.writeToParcel(out, flags);
        int id = this.mBqrCommon.getQualityReportId();
        if (id == 2) {
            this.mBqrVsLsto.writeToParcel(out, flags);
        } else if (id == 3) {
            this.mBqrVsA2dpChoppy.writeToParcel(out, flags);
        } else if (id == 4) {
            this.mBqrVsScoChoppy.writeToParcel(out, flags);
        } else if (id == 32) {
            this.mBqrVsConnectFail.writeToParcel(out, flags);
        }
    }

    public String toString() {
        String str = "BQR: {\n  mAddr: " + this.mAddr + ", mLmpVer: " + String.format("0x%02X", Integer.valueOf(this.mLmpVer)) + ", mLmpSubVer: " + String.format("0x%04X", Integer.valueOf(this.mLmpSubVer)) + ", mManufacturerId: " + String.format("0x%04X", Integer.valueOf(this.mManufacturerId)) + ", mName: " + this.mName + ", mBluetoothClass: " + String.format("0x%X", Integer.valueOf(this.mBluetoothClass)) + ",\n" + this.mBqrCommon + "\n" + this.mBqrVsCommon + "\n";
        int id = this.mBqrCommon.getQualityReportId();
        if (id == 2) {
            return str + this.mBqrVsLsto + "\n}";
        } else if (id == 3) {
            return str + this.mBqrVsA2dpChoppy + "\n}";
        } else if (id == 4) {
            return str + this.mBqrVsScoChoppy + "\n}";
        } else if (id == 32) {
            return str + this.mBqrVsConnectFail + "\n}";
        } else if (id == 1) {
            return str + "}";
        } else {
            return str;
        }
    }

    /* loaded from: classes.dex */
    public class BqrCommon implements Parcelable {
        static final int BQR_COMMON_LEN = 48;
        private static final String TAG = "BluetoothQualityReport.BqrCommon";
        private int mAfhSelectUnidealChannelCount;
        private int mConnectionHandle;
        private int mConnectionRole;
        private long mFlowOffCount;
        private long mLastFlowOnTimestamp;
        private long mLastTxAckTimestamp;
        private int mLsto;
        private long mNakCount;
        private long mNoRxCount;
        private long mOverflowCount;
        private int mPacketType;
        private long mPiconetClock;
        private int mQualityReportId;
        private long mRetransmissionCount;
        private int mRssi;
        private int mSnr;
        private int mTxPowerLevel;
        private long mUnderflowCount;
        private int mUnusedAfhChannelCount;

        private BqrCommon(byte[] rawData, int offset) {
            if (rawData == null || rawData.length < offset + 48) {
                throw new IllegalArgumentException("BluetoothQualityReport.BqrCommon: BQR raw data length is abnormal.");
            }
            ByteBuffer bqrBuf = ByteBuffer.wrap(rawData, offset, rawData.length - offset).asReadOnlyBuffer();
            bqrBuf.order(ByteOrder.LITTLE_ENDIAN);
            this.mQualityReportId = bqrBuf.get() & 255;
            this.mPacketType = bqrBuf.get() & 255;
            this.mConnectionHandle = bqrBuf.getShort() & 65535;
            this.mConnectionRole = bqrBuf.get() & 255;
            this.mTxPowerLevel = bqrBuf.get() & 255;
            this.mRssi = bqrBuf.get();
            this.mSnr = bqrBuf.get();
            this.mUnusedAfhChannelCount = bqrBuf.get() & 255;
            this.mAfhSelectUnidealChannelCount = bqrBuf.get() & 255;
            this.mLsto = bqrBuf.getShort() & 65535;
            this.mPiconetClock = bqrBuf.getInt() & 4294967295L;
            this.mRetransmissionCount = bqrBuf.getInt() & 4294967295L;
            this.mNoRxCount = bqrBuf.getInt() & 4294967295L;
            this.mNakCount = bqrBuf.getInt() & 4294967295L;
            this.mLastTxAckTimestamp = bqrBuf.getInt() & 4294967295L;
            this.mFlowOffCount = bqrBuf.getInt() & 4294967295L;
            this.mLastFlowOnTimestamp = bqrBuf.getInt() & 4294967295L;
            this.mOverflowCount = bqrBuf.getInt() & 4294967295L;
            this.mUnderflowCount = bqrBuf.getInt() & 4294967295L;
        }

        private BqrCommon(Parcel in) {
            this.mQualityReportId = in.readInt();
            this.mPacketType = in.readInt();
            this.mConnectionHandle = in.readInt();
            this.mConnectionRole = in.readInt();
            this.mTxPowerLevel = in.readInt();
            this.mRssi = in.readInt();
            this.mSnr = in.readInt();
            this.mUnusedAfhChannelCount = in.readInt();
            this.mAfhSelectUnidealChannelCount = in.readInt();
            this.mLsto = in.readInt();
            this.mPiconetClock = in.readLong();
            this.mRetransmissionCount = in.readLong();
            this.mNoRxCount = in.readLong();
            this.mNakCount = in.readLong();
            this.mLastTxAckTimestamp = in.readLong();
            this.mFlowOffCount = in.readLong();
            this.mLastFlowOnTimestamp = in.readLong();
            this.mOverflowCount = in.readLong();
            this.mUnderflowCount = in.readLong();
        }

        int getQualityReportId() {
            return this.mQualityReportId;
        }

        public int getPacketType() {
            return this.mPacketType;
        }

        public String getPacketTypeStr() {
            PacketType type = PacketType.fromOrdinal(this.mPacketType);
            return type.toString();
        }

        public int getConnectionHandle() {
            return this.mConnectionHandle;
        }

        public String getConnectionRole() {
            int i = this.mConnectionRole;
            if (i == 0) {
                return "Master";
            }
            if (i == 1) {
                return "Slave";
            }
            return "INVALID:" + this.mConnectionRole;
        }

        public int getTxPowerLevel() {
            return this.mTxPowerLevel;
        }

        public int getRssi() {
            return this.mRssi;
        }

        public int getSnr() {
            return this.mSnr;
        }

        public int getUnusedAfhChannelCount() {
            return this.mUnusedAfhChannelCount;
        }

        public int getAfhSelectUnidealChannelCount() {
            return this.mAfhSelectUnidealChannelCount;
        }

        public int getLsto() {
            return this.mLsto;
        }

        public long getPiconetClock() {
            return this.mPiconetClock;
        }

        public long getRetransmissionCount() {
            return this.mRetransmissionCount;
        }

        public long getNoRxCount() {
            return this.mNoRxCount;
        }

        public long getNakCount() {
            return this.mNakCount;
        }

        public long getLastTxAckTimestamp() {
            return this.mLastTxAckTimestamp;
        }

        public long getFlowOffCount() {
            return this.mFlowOffCount;
        }

        public long getLastFlowOnTimestamp() {
            return this.mLastFlowOnTimestamp;
        }

        public long getOverflowCount() {
            return this.mOverflowCount;
        }

        public long getUnderflowCount() {
            return this.mUnderflowCount;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mQualityReportId);
            dest.writeInt(this.mPacketType);
            dest.writeInt(this.mConnectionHandle);
            dest.writeInt(this.mConnectionRole);
            dest.writeInt(this.mTxPowerLevel);
            dest.writeInt(this.mRssi);
            dest.writeInt(this.mSnr);
            dest.writeInt(this.mUnusedAfhChannelCount);
            dest.writeInt(this.mAfhSelectUnidealChannelCount);
            dest.writeInt(this.mLsto);
            dest.writeLong(this.mPiconetClock);
            dest.writeLong(this.mRetransmissionCount);
            dest.writeLong(this.mNoRxCount);
            dest.writeLong(this.mNakCount);
            dest.writeLong(this.mLastTxAckTimestamp);
            dest.writeLong(this.mFlowOffCount);
            dest.writeLong(this.mLastFlowOnTimestamp);
            dest.writeLong(this.mOverflowCount);
            dest.writeLong(this.mUnderflowCount);
        }

        public String toString() {
            String str = "  BqrCommon: {\n    mQualityReportId: " + BluetoothQualityReport.this.getQualityReportIdStr() + "(" + String.format("0x%02X", Integer.valueOf(this.mQualityReportId)) + "), mPacketType: " + getPacketTypeStr() + "(" + String.format("0x%02X", Integer.valueOf(this.mPacketType)) + "), mConnectionHandle: " + String.format("0x%04X", Integer.valueOf(this.mConnectionHandle)) + ", mConnectionRole: " + getConnectionRole() + "(" + this.mConnectionRole + "), mTxPowerLevel: " + this.mTxPowerLevel + ", mRssi: " + this.mRssi + ", mSnr: " + this.mSnr + ", mUnusedAfhChannelCount: " + this.mUnusedAfhChannelCount + ",\n    mAfhSelectUnidealChannelCount: " + this.mAfhSelectUnidealChannelCount + ", mLsto: " + this.mLsto + ", mPiconetClock: " + String.format("0x%08X", Long.valueOf(this.mPiconetClock)) + ", mRetransmissionCount: " + this.mRetransmissionCount + ", mNoRxCount: " + this.mNoRxCount + ", mNakCount: " + this.mNakCount + ", mLastTxAckTimestamp: " + String.format("0x%08X", Long.valueOf(this.mLastTxAckTimestamp)) + ", mFlowOffCount: " + this.mFlowOffCount + ",\n    mLastFlowOnTimestamp: " + String.format("0x%08X", Long.valueOf(this.mLastFlowOnTimestamp)) + ", mOverflowCount: " + this.mOverflowCount + ", mUnderflowCount: " + this.mUnderflowCount + "\n  }";
            return str;
        }
    }

    /* loaded from: classes.dex */
    public class BqrVsCommon implements Parcelable {
        private static final int BQR_VS_COMMON_LEN = 7;
        private static final String TAG = "BluetoothQualityReport.BqrVsCommon";
        private String mAddr;
        private int mCalFailedItemCount;

        private BqrVsCommon(byte[] rawData, int offset) {
            if (rawData == null || rawData.length < offset + 7) {
                throw new IllegalArgumentException("BluetoothQualityReport.BqrVsCommon: BQR raw data length is abnormal.");
            }
            ByteBuffer bqrBuf = ByteBuffer.wrap(rawData, offset, rawData.length - offset).asReadOnlyBuffer();
            bqrBuf.order(ByteOrder.LITTLE_ENDIAN);
            this.mAddr = String.format("%02X:%02X:%02X:%02X:%02X:%02X", Byte.valueOf(bqrBuf.get(offset + 5)), Byte.valueOf(bqrBuf.get(offset + 4)), Byte.valueOf(bqrBuf.get(offset + 3)), Byte.valueOf(bqrBuf.get(offset + 2)), Byte.valueOf(bqrBuf.get(offset + 1)), Byte.valueOf(bqrBuf.get(offset + 0)));
            bqrBuf.position(offset + 6);
            this.mCalFailedItemCount = bqrBuf.get() & 255;
        }

        private BqrVsCommon(Parcel in) {
            this.mAddr = in.readString();
            this.mCalFailedItemCount = in.readInt();
        }

        public int getCalFailedItemCount() {
            return this.mCalFailedItemCount;
        }

        int getLength() {
            return 7;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.mAddr);
            dest.writeInt(this.mCalFailedItemCount);
        }

        public String toString() {
            String str = "  BqrVsCommon: {\n    mAddr: " + this.mAddr + ", mCalFailedItemCount: " + this.mCalFailedItemCount + "\n  }";
            return str;
        }
    }

    /* loaded from: classes.dex */
    public class BqrVsLsto implements Parcelable {
        private static final String TAG = "BluetoothQualityReport.BqrVsLsto";
        private long mBasebandStats;
        private int mConnState;
        private int mCxmDenials;
        private long mLastTxAckTimestamp;
        private long mNativeClock;
        private int mRfLoss;
        private long mSlotsUsed;
        private int mTxSkipped;

        private BqrVsLsto(byte[] rawData, int offset) {
            if (rawData == null || rawData.length <= offset) {
                throw new IllegalArgumentException("BluetoothQualityReport.BqrVsLsto: BQR raw data length is abnormal.");
            }
            ByteBuffer bqrBuf = ByteBuffer.wrap(rawData, offset, rawData.length - offset).asReadOnlyBuffer();
            bqrBuf.order(ByteOrder.LITTLE_ENDIAN);
            this.mConnState = bqrBuf.get() & 255;
            this.mBasebandStats = bqrBuf.getInt() & 4294967295L;
            this.mSlotsUsed = bqrBuf.getInt() & 4294967295L;
            this.mCxmDenials = bqrBuf.getShort() & 65535;
            this.mTxSkipped = bqrBuf.getShort() & 65535;
            this.mRfLoss = bqrBuf.getShort() & 65535;
            this.mNativeClock = bqrBuf.getInt() & 4294967295L;
            this.mLastTxAckTimestamp = bqrBuf.getInt() & 4294967295L;
        }

        private BqrVsLsto(Parcel in) {
            this.mConnState = in.readInt();
            this.mBasebandStats = in.readLong();
            this.mSlotsUsed = in.readLong();
            this.mCxmDenials = in.readInt();
            this.mTxSkipped = in.readInt();
            this.mRfLoss = in.readInt();
            this.mNativeClock = in.readLong();
            this.mLastTxAckTimestamp = in.readLong();
        }

        public int getConnState() {
            return this.mConnState;
        }

        public String getConnStateStr() {
            return ConnState.getName(this.mConnState);
        }

        public long getBasebandStats() {
            return this.mBasebandStats;
        }

        public long getSlotsUsed() {
            return this.mSlotsUsed;
        }

        public int getCxmDenials() {
            return this.mCxmDenials;
        }

        public int getTxSkipped() {
            return this.mTxSkipped;
        }

        public int getRfLoss() {
            return this.mRfLoss;
        }

        public long getNativeClock() {
            return this.mNativeClock;
        }

        public long getLastTxAckTimestamp() {
            return this.mLastTxAckTimestamp;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mConnState);
            dest.writeLong(this.mBasebandStats);
            dest.writeLong(this.mSlotsUsed);
            dest.writeInt(this.mCxmDenials);
            dest.writeInt(this.mTxSkipped);
            dest.writeInt(this.mRfLoss);
            dest.writeLong(this.mNativeClock);
            dest.writeLong(this.mLastTxAckTimestamp);
        }

        public String toString() {
            String str = "  BqrVsLsto: {\n    mConnState: " + getConnStateStr() + "(" + String.format("0x%02X", Integer.valueOf(this.mConnState)) + "), mBasebandStats: " + String.format("0x%08X", Long.valueOf(this.mBasebandStats)) + ", mSlotsUsed: " + this.mSlotsUsed + ", mCxmDenials: " + this.mCxmDenials + ", mTxSkipped: " + this.mTxSkipped + ", mRfLoss: " + this.mRfLoss + ", mNativeClock: " + String.format("0x%08X", Long.valueOf(this.mNativeClock)) + ", mLastTxAckTimestamp: " + String.format("0x%08X", Long.valueOf(this.mLastTxAckTimestamp)) + "\n  }";
            return str;
        }
    }

    /* loaded from: classes.dex */
    public class BqrVsA2dpChoppy implements Parcelable {
        private static final String TAG = "BluetoothQualityReport.BqrVsA2dpChoppy";
        private int mAclTxQueueLength;
        private long mArrivalTime;
        private int mGlitchCount;
        private int mLinkQuality;
        private int mRxCxmDenials;
        private long mScheduleTime;
        private int mTxCxmDenials;

        private BqrVsA2dpChoppy(byte[] rawData, int offset) {
            if (rawData == null || rawData.length <= offset) {
                throw new IllegalArgumentException("BluetoothQualityReport.BqrVsA2dpChoppy: BQR raw data length is abnormal.");
            }
            ByteBuffer bqrBuf = ByteBuffer.wrap(rawData, offset, rawData.length - offset).asReadOnlyBuffer();
            bqrBuf.order(ByteOrder.LITTLE_ENDIAN);
            this.mArrivalTime = bqrBuf.getInt() & 4294967295L;
            this.mScheduleTime = bqrBuf.getInt() & 4294967295L;
            this.mGlitchCount = bqrBuf.getShort() & 65535;
            this.mTxCxmDenials = bqrBuf.getShort() & 65535;
            this.mRxCxmDenials = bqrBuf.getShort() & 65535;
            this.mAclTxQueueLength = bqrBuf.get() & 255;
            this.mLinkQuality = bqrBuf.get() & 255;
        }

        private BqrVsA2dpChoppy(Parcel in) {
            this.mArrivalTime = in.readLong();
            this.mScheduleTime = in.readLong();
            this.mGlitchCount = in.readInt();
            this.mTxCxmDenials = in.readInt();
            this.mRxCxmDenials = in.readInt();
            this.mAclTxQueueLength = in.readInt();
            this.mLinkQuality = in.readInt();
        }

        public long getArrivalTime() {
            return this.mArrivalTime;
        }

        public long getScheduleTime() {
            return this.mScheduleTime;
        }

        public int getGlitchCount() {
            return this.mGlitchCount;
        }

        public int getTxCxmDenials() {
            return this.mTxCxmDenials;
        }

        public int getRxCxmDenials() {
            return this.mRxCxmDenials;
        }

        public int getAclTxQueueLength() {
            return this.mAclTxQueueLength;
        }

        public int getLinkQuality() {
            return this.mLinkQuality;
        }

        public String getLinkQualityStr() {
            LinkQuality q = LinkQuality.fromOrdinal(this.mLinkQuality);
            return q.toString();
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.mArrivalTime);
            dest.writeLong(this.mScheduleTime);
            dest.writeInt(this.mGlitchCount);
            dest.writeInt(this.mTxCxmDenials);
            dest.writeInt(this.mRxCxmDenials);
            dest.writeInt(this.mAclTxQueueLength);
            dest.writeInt(this.mLinkQuality);
        }

        public String toString() {
            String str = "  BqrVsA2dpChoppy: {\n    mArrivalTime: " + String.format("0x%08X", Long.valueOf(this.mArrivalTime)) + ", mScheduleTime: " + String.format("0x%08X", Long.valueOf(this.mScheduleTime)) + ", mGlitchCount: " + this.mGlitchCount + ", mTxCxmDenials: " + this.mTxCxmDenials + ", mRxCxmDenials: " + this.mRxCxmDenials + ", mAclTxQueueLength: " + this.mAclTxQueueLength + ", mLinkQuality: " + getLinkQualityStr() + "(" + String.format("0x%02X", Integer.valueOf(this.mLinkQuality)) + ")\n  }";
            return str;
        }
    }

    /* loaded from: classes.dex */
    public class BqrVsScoChoppy implements Parcelable {
        private static final String TAG = "BluetoothQualityReport.BqrVsScoChoppy";
        private int mAirFormat;
        private int mGlitchCount;
        private int mGoodRxFrameCount;
        private int mInstanceCount;
        private int mIntervalEsco;
        private int mLateDispatch;
        private int mLpaIntrMiss;
        private int mMicIntrMiss;
        private int mMissedInstanceCount;
        private int mPlcDiscardCount;
        private int mPlcFillCount;
        private int mRxCxmDenials;
        private int mRxRetransmitSlotCount;
        private int mSprIntrMiss;
        private int mTxAbortCount;
        private int mTxCxmDenials;
        private int mTxRetransmitSlotCount;
        private int mWindowEsco;

        private BqrVsScoChoppy(byte[] rawData, int offset) {
            if (rawData == null || rawData.length <= offset) {
                throw new IllegalArgumentException("BluetoothQualityReport.BqrVsScoChoppy: BQR raw data length is abnormal.");
            }
            ByteBuffer bqrBuf = ByteBuffer.wrap(rawData, offset, rawData.length - offset).asReadOnlyBuffer();
            bqrBuf.order(ByteOrder.LITTLE_ENDIAN);
            this.mGlitchCount = bqrBuf.getShort() & 65535;
            this.mIntervalEsco = bqrBuf.get() & 255;
            this.mWindowEsco = bqrBuf.get() & 255;
            this.mAirFormat = bqrBuf.get() & 255;
            this.mInstanceCount = bqrBuf.getShort() & 65535;
            this.mTxCxmDenials = bqrBuf.getShort() & 65535;
            this.mRxCxmDenials = bqrBuf.getShort() & 65535;
            this.mTxAbortCount = bqrBuf.getShort() & 65535;
            this.mLateDispatch = bqrBuf.getShort() & 65535;
            this.mMicIntrMiss = bqrBuf.getShort() & 65535;
            this.mLpaIntrMiss = bqrBuf.getShort() & 65535;
            this.mSprIntrMiss = bqrBuf.getShort() & 65535;
            this.mPlcFillCount = bqrBuf.getShort() & 65535;
            this.mPlcDiscardCount = bqrBuf.getShort() & 65535;
            try {
                this.mMissedInstanceCount = bqrBuf.getShort() & 65535;
                this.mTxRetransmitSlotCount = bqrBuf.getShort() & 65535;
                this.mRxRetransmitSlotCount = bqrBuf.getShort() & 65535;
                this.mGoodRxFrameCount = bqrBuf.getShort() & 65535;
            } catch (BufferUnderflowException e) {
                Log.v(TAG, "some fields are not contained");
            }
        }

        private BqrVsScoChoppy(Parcel in) {
            this.mGlitchCount = in.readInt();
            this.mIntervalEsco = in.readInt();
            this.mWindowEsco = in.readInt();
            this.mAirFormat = in.readInt();
            this.mInstanceCount = in.readInt();
            this.mTxCxmDenials = in.readInt();
            this.mRxCxmDenials = in.readInt();
            this.mTxAbortCount = in.readInt();
            this.mLateDispatch = in.readInt();
            this.mMicIntrMiss = in.readInt();
            this.mLpaIntrMiss = in.readInt();
            this.mSprIntrMiss = in.readInt();
            this.mPlcFillCount = in.readInt();
            this.mPlcDiscardCount = in.readInt();
            this.mMissedInstanceCount = in.readInt();
            this.mTxRetransmitSlotCount = in.readInt();
            this.mRxRetransmitSlotCount = in.readInt();
            this.mGoodRxFrameCount = in.readInt();
        }

        public int getGlitchCount() {
            return this.mGlitchCount;
        }

        public int getIntervalEsco() {
            return this.mIntervalEsco;
        }

        public int getWindowEsco() {
            return this.mWindowEsco;
        }

        public int getAirFormat() {
            return this.mAirFormat;
        }

        public String getAirFormatStr() {
            AirMode m = AirMode.fromOrdinal(this.mAirFormat);
            return m.toString();
        }

        public int getInstanceCount() {
            return this.mInstanceCount;
        }

        public int getTxCxmDenials() {
            return this.mTxCxmDenials;
        }

        public int getRxCxmDenials() {
            return this.mRxCxmDenials;
        }

        public int getTxAbortCount() {
            return this.mTxAbortCount;
        }

        public int getLateDispatch() {
            return this.mLateDispatch;
        }

        public int getMicIntrMiss() {
            return this.mMicIntrMiss;
        }

        public int getLpaIntrMiss() {
            return this.mLpaIntrMiss;
        }

        public int getSprIntrMiss() {
            return this.mSprIntrMiss;
        }

        public int getPlcFillCount() {
            return this.mPlcFillCount;
        }

        public int getPlcDiscardCount() {
            return this.mPlcDiscardCount;
        }

        public int getMissedInstanceCount() {
            return this.mMissedInstanceCount;
        }

        public int getTxRetransmitSlotCount() {
            return this.mTxRetransmitSlotCount;
        }

        public int getRxRetransmitSlotCount() {
            return this.mRxRetransmitSlotCount;
        }

        public int getGoodRxFrameCount() {
            return this.mGoodRxFrameCount;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mGlitchCount);
            dest.writeInt(this.mIntervalEsco);
            dest.writeInt(this.mWindowEsco);
            dest.writeInt(this.mAirFormat);
            dest.writeInt(this.mInstanceCount);
            dest.writeInt(this.mTxCxmDenials);
            dest.writeInt(this.mRxCxmDenials);
            dest.writeInt(this.mTxAbortCount);
            dest.writeInt(this.mLateDispatch);
            dest.writeInt(this.mMicIntrMiss);
            dest.writeInt(this.mLpaIntrMiss);
            dest.writeInt(this.mSprIntrMiss);
            dest.writeInt(this.mPlcFillCount);
            dest.writeInt(this.mPlcDiscardCount);
            dest.writeInt(this.mMissedInstanceCount);
            dest.writeInt(this.mTxRetransmitSlotCount);
            dest.writeInt(this.mRxRetransmitSlotCount);
            dest.writeInt(this.mGoodRxFrameCount);
        }

        public String toString() {
            String str = "  BqrVsScoChoppy: {\n    mGlitchCount: " + this.mGlitchCount + ", mIntervalEsco: " + this.mIntervalEsco + ", mWindowEsco: " + this.mWindowEsco + ", mAirFormat: " + getAirFormatStr() + "(" + String.format("0x%02X", Integer.valueOf(this.mAirFormat)) + "), mInstanceCount: " + this.mInstanceCount + ", mTxCxmDenials: " + this.mTxCxmDenials + ", mRxCxmDenials: " + this.mRxCxmDenials + ", mTxAbortCount: " + this.mTxAbortCount + ",\n    mLateDispatch: " + this.mLateDispatch + ", mMicIntrMiss: " + this.mMicIntrMiss + ", mLpaIntrMiss: " + this.mLpaIntrMiss + ", mSprIntrMiss: " + this.mSprIntrMiss + ", mPlcFillCount: " + this.mPlcFillCount + ", mPlcDiscardCount: " + this.mPlcDiscardCount + ", mMissedInstanceCount: " + this.mMissedInstanceCount + ", mTxRetransmitSlotCount: " + this.mTxRetransmitSlotCount + ",\n    mRxRetransmitSlotCount: " + this.mRxRetransmitSlotCount + ", mGoodRxFrameCount: " + this.mGoodRxFrameCount + "\n  }";
            return str;
        }
    }

    /* loaded from: classes.dex */
    public class BqrVsConnectFail implements Parcelable {
        private static final String TAG = "BluetoothQualityReport.BqrVsConnectFail";
        private int mFailReason;

        private BqrVsConnectFail(byte[] rawData, int offset) {
            if (rawData == null || rawData.length <= offset) {
                throw new IllegalArgumentException("BluetoothQualityReport.BqrVsConnectFail: BQR raw data length is abnormal.");
            }
            ByteBuffer bqrBuf = ByteBuffer.wrap(rawData, offset, rawData.length - offset).asReadOnlyBuffer();
            bqrBuf.order(ByteOrder.LITTLE_ENDIAN);
            this.mFailReason = bqrBuf.get() & 255;
        }

        private BqrVsConnectFail(Parcel in) {
            this.mFailReason = in.readInt();
        }

        public int getFailReason() {
            return this.mFailReason;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mFailReason);
        }

        public String toString() {
            String str = "  BqrVsConnectFail: {\n    mFailReason: " + String.format("0x%02X", Integer.valueOf(this.mFailReason)) + "\n  }";
            return str;
        }
    }
}
