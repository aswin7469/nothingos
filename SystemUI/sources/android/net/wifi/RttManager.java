package android.net.wifi;

import android.annotation.SystemApi;
import android.content.Context;
import android.net.wifi.rtt.RangingRequest;
import android.net.wifi.rtt.RangingResult;
import android.net.wifi.rtt.RangingResultCallback;
import android.net.wifi.rtt.WifiRttManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.Log;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.List;

@SystemApi
@Deprecated
public class RttManager {
    public static final int BASE = 160256;
    public static final int CMD_OP_ABORTED = 160260;
    public static final int CMD_OP_DISABLE_RESPONDER = 160262;
    public static final int CMD_OP_ENABLE_RESPONDER = 160261;
    public static final int CMD_OP_ENALBE_RESPONDER_FAILED = 160264;
    public static final int CMD_OP_ENALBE_RESPONDER_SUCCEEDED = 160263;
    public static final int CMD_OP_FAILED = 160258;
    public static final int CMD_OP_REG_BINDER = 160265;
    public static final int CMD_OP_START_RANGING = 160256;
    public static final int CMD_OP_STOP_RANGING = 160257;
    public static final int CMD_OP_SUCCEEDED = 160259;
    private static final boolean DBG = false;
    public static final String DESCRIPTION_KEY = "android.net.wifi.RttManager.Description";
    public static final int PREAMBLE_HT = 2;
    public static final int PREAMBLE_LEGACY = 1;
    public static final int PREAMBLE_VHT = 4;
    public static final int REASON_INITIATOR_NOT_ALLOWED_WHEN_RESPONDER_ON = -6;
    public static final int REASON_INVALID_LISTENER = -3;
    public static final int REASON_INVALID_REQUEST = -4;
    public static final int REASON_NOT_AVAILABLE = -2;
    public static final int REASON_PERMISSION_DENIED = -5;
    public static final int REASON_UNSPECIFIED = -1;
    public static final int RTT_BW_10_SUPPORT = 2;
    public static final int RTT_BW_160_SUPPORT = 32;
    public static final int RTT_BW_20_SUPPORT = 4;
    public static final int RTT_BW_40_SUPPORT = 8;
    public static final int RTT_BW_5_SUPPORT = 1;
    public static final int RTT_BW_80_SUPPORT = 16;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_10 = 6;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_160 = 3;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_20 = 0;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_40 = 1;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_5 = 5;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_80 = 2;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_80P80 = 4;
    @Deprecated
    public static final int RTT_CHANNEL_WIDTH_UNSPECIFIED = -1;
    public static final int RTT_PEER_NAN = 5;
    public static final int RTT_PEER_P2P_CLIENT = 4;
    public static final int RTT_PEER_P2P_GO = 3;
    public static final int RTT_PEER_TYPE_AP = 1;
    public static final int RTT_PEER_TYPE_STA = 2;
    @Deprecated
    public static final int RTT_PEER_TYPE_UNSPECIFIED = 0;
    public static final int RTT_STATUS_ABORTED = 8;
    public static final int RTT_STATUS_FAILURE = 1;
    public static final int RTT_STATUS_FAIL_AP_ON_DIFF_CHANNEL = 6;
    public static final int RTT_STATUS_FAIL_BUSY_TRY_LATER = 12;
    public static final int RTT_STATUS_FAIL_FTM_PARAM_OVERRIDE = 15;
    public static final int RTT_STATUS_FAIL_INVALID_TS = 9;
    public static final int RTT_STATUS_FAIL_NOT_SCHEDULED_YET = 4;
    public static final int RTT_STATUS_FAIL_NO_CAPABILITY = 7;
    public static final int RTT_STATUS_FAIL_NO_RSP = 2;
    public static final int RTT_STATUS_FAIL_PROTOCOL = 10;
    public static final int RTT_STATUS_FAIL_REJECTED = 3;
    public static final int RTT_STATUS_FAIL_SCHEDULE = 11;
    public static final int RTT_STATUS_FAIL_TM_TIMEOUT = 5;
    public static final int RTT_STATUS_INVALID_REQ = 13;
    public static final int RTT_STATUS_NO_WIFI = 14;
    public static final int RTT_STATUS_SUCCESS = 0;
    @Deprecated
    public static final int RTT_TYPE_11_MC = 4;
    @Deprecated
    public static final int RTT_TYPE_11_V = 2;
    public static final int RTT_TYPE_ONE_SIDED = 1;
    public static final int RTT_TYPE_TWO_SIDED = 2;
    @Deprecated
    public static final int RTT_TYPE_UNSPECIFIED = 0;
    private static final String TAG = "RttManager";
    private final Context mContext;
    private final WifiRttManager mNewService;
    private RttCapabilities mRttCapabilities;

    @Deprecated
    public static abstract class ResponderCallback {
        public abstract void onResponderEnableFailure(int i);

        public abstract void onResponderEnabled(ResponderConfig responderConfig);
    }

    @Deprecated
    public interface RttListener {
        void onAborted();

        void onFailure(int i, String str);

        void onSuccess(RttResult[] rttResultArr);
    }

    @Deprecated
    public static class RttResult {
        public WifiInformationElement LCI;
        public WifiInformationElement LCR;
        public String bssid;
        public int burstDuration;
        public int burstNumber;
        public int distance;
        public int distanceSpread;
        public int distanceStandardDeviation;
        @Deprecated
        public int distance_cm;
        @Deprecated
        public int distance_sd_cm;
        @Deprecated
        public int distance_spread_cm;
        public int frameNumberPerBurstPeer;
        public int measurementFrameNumber;
        public int measurementType;
        public int negotiatedBurstNum;
        @Deprecated
        public int requestType;
        public int retryAfterDuration;
        public int rssi;
        public int rssiSpread;
        @Deprecated
        public int rssi_spread;
        public long rtt;
        public long rttSpread;
        public long rttStandardDeviation;
        @Deprecated
        public long rtt_ns;
        @Deprecated
        public long rtt_sd_ns;
        @Deprecated
        public long rtt_spread_ns;
        public int rxRate;
        public boolean secure;
        public int status;
        public int successMeasurementFrameNumber;

        /* renamed from: ts */
        public long f52ts;
        public int txRate;
        @Deprecated
        public int tx_rate;
    }

    @Deprecated
    public static class WifiInformationElement {
        public byte[] data;

        /* renamed from: id */
        public byte f53id;
    }

    @Deprecated
    public class Capabilities {
        public int supportedPeerType;
        public int supportedType;

        public Capabilities() {
        }
    }

    @Deprecated
    public Capabilities getCapabilities() {
        throw new UnsupportedOperationException("getCapabilities is not supported in the adaptation layer");
    }

    @Deprecated
    public static class RttCapabilities implements Parcelable {
        public static final Parcelable.Creator<RttCapabilities> CREATOR = new Parcelable.Creator<RttCapabilities>() {
            public RttCapabilities createFromParcel(Parcel parcel) {
                RttCapabilities rttCapabilities = new RttCapabilities();
                boolean z = false;
                rttCapabilities.oneSidedRttSupported = parcel.readInt() == 1;
                rttCapabilities.twoSided11McRttSupported = parcel.readInt() == 1;
                rttCapabilities.lciSupported = parcel.readInt() == 1;
                rttCapabilities.lcrSupported = parcel.readInt() == 1;
                rttCapabilities.preambleSupported = parcel.readInt();
                rttCapabilities.bwSupported = parcel.readInt();
                rttCapabilities.responderSupported = parcel.readInt() == 1;
                if (parcel.readInt() == 1) {
                    z = true;
                }
                rttCapabilities.secureRttSupported = z;
                rttCapabilities.mcVersion = parcel.readInt();
                return rttCapabilities;
            }

            public RttCapabilities[] newArray(int i) {
                return new RttCapabilities[i];
            }
        };
        public int bwSupported;
        public boolean lciSupported;
        public boolean lcrSupported;
        public int mcVersion;
        public boolean oneSidedRttSupported;
        public int preambleSupported;
        public boolean responderSupported;
        public boolean secureRttSupported;
        @Deprecated
        public boolean supportedPeerType;
        @Deprecated
        public boolean supportedType;
        public boolean twoSided11McRttSupported;

        public int describeContents() {
            return 0;
        }

        public String toString() {
            String str;
            String str2;
            StringBuffer stringBuffer = new StringBuffer("oneSidedRtt ");
            String str3 = "is Supported. ";
            StringBuffer append = stringBuffer.append(this.oneSidedRttSupported ? str3 : "is not supported. ").append("twoSided11McRtt ");
            if (this.twoSided11McRttSupported) {
                str = str3;
            } else {
                str = "is not supported. ";
            }
            StringBuffer append2 = append.append(str).append("lci ");
            if (this.lciSupported) {
                str2 = str3;
            } else {
                str2 = "is not supported. ";
            }
            StringBuffer append3 = append2.append(str2).append("lcr ");
            if (!this.lcrSupported) {
                str3 = "is not supported. ";
            }
            append3.append(str3);
            if ((this.preambleSupported & 1) != 0) {
                stringBuffer.append("Legacy ");
            }
            if ((this.preambleSupported & 2) != 0) {
                stringBuffer.append("HT ");
            }
            if ((this.preambleSupported & 4) != 0) {
                stringBuffer.append("VHT ");
            }
            stringBuffer.append("is supported. ");
            if ((this.bwSupported & 1) != 0) {
                stringBuffer.append("5 MHz ");
            }
            if ((this.bwSupported & 2) != 0) {
                stringBuffer.append("10 MHz ");
            }
            if ((this.bwSupported & 4) != 0) {
                stringBuffer.append("20 MHz ");
            }
            if ((this.bwSupported & 8) != 0) {
                stringBuffer.append("40 MHz ");
            }
            if ((this.bwSupported & 16) != 0) {
                stringBuffer.append("80 MHz ");
            }
            if ((this.bwSupported & 32) != 0) {
                stringBuffer.append("160 MHz ");
            }
            stringBuffer.append("is supported. STA responder role is ");
            String str4 = "supported";
            stringBuffer.append(this.responderSupported ? str4 : "not supported");
            StringBuffer append4 = stringBuffer.append(" Secure RTT protocol is ");
            if (!this.secureRttSupported) {
                str4 = "not supported";
            }
            append4.append(str4);
            stringBuffer.append(" 11mc version is " + this.mcVersion);
            return stringBuffer.toString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.oneSidedRttSupported ? 1 : 0);
            parcel.writeInt(this.twoSided11McRttSupported ? 1 : 0);
            parcel.writeInt(this.lciSupported ? 1 : 0);
            parcel.writeInt(this.lcrSupported ? 1 : 0);
            parcel.writeInt(this.preambleSupported);
            parcel.writeInt(this.bwSupported);
            parcel.writeInt(this.responderSupported ? 1 : 0);
            parcel.writeInt(this.secureRttSupported ? 1 : 0);
            parcel.writeInt(this.mcVersion);
        }
    }

    public RttCapabilities getRttCapabilities() {
        return this.mRttCapabilities;
    }

    @Deprecated
    public static class RttParams {
        public boolean LCIRequest;
        public boolean LCRRequest;
        public int bandwidth = 4;
        public String bssid;
        public int burstTimeout = 15;
        public int centerFreq0;
        public int centerFreq1;
        public int channelWidth;
        public int deviceType = 1;
        public int frequency;
        public int interval;
        public int numRetriesPerFTMR = 0;
        public int numRetriesPerMeasurementFrame = 0;
        public int numSamplesPerBurst = 8;
        @Deprecated
        public int num_retries;
        @Deprecated
        public int num_samples;
        public int numberBurst = 0;
        public int preamble = 2;
        public int requestType = 1;
        public boolean secure;

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("deviceType=" + this.deviceType);
            sb.append(", requestType=" + this.requestType);
            sb.append(", secure=" + this.secure);
            sb.append(", bssid=" + this.bssid);
            sb.append(", frequency=" + this.frequency);
            sb.append(", channelWidth=" + this.channelWidth);
            sb.append(", centerFreq0=" + this.centerFreq0);
            sb.append(", centerFreq1=" + this.centerFreq1);
            sb.append(", num_samples=" + this.num_samples);
            sb.append(", num_retries=" + this.num_retries);
            sb.append(", numberBurst=" + this.numberBurst);
            sb.append(", interval=" + this.interval);
            sb.append(", numSamplesPerBurst=" + this.numSamplesPerBurst);
            sb.append(", numRetriesPerMeasurementFrame=" + this.numRetriesPerMeasurementFrame);
            sb.append(", numRetriesPerFTMR=" + this.numRetriesPerFTMR);
            sb.append(", LCIRequest=" + this.LCIRequest);
            sb.append(", LCRRequest=" + this.LCRRequest);
            sb.append(", burstTimeout=" + this.burstTimeout);
            sb.append(", preamble=" + this.preamble);
            sb.append(", bandwidth=" + this.bandwidth);
            return sb.toString();
        }
    }

    @Deprecated
    public static class ParcelableRttParams implements Parcelable {
        public static final Parcelable.Creator<ParcelableRttParams> CREATOR = new Parcelable.Creator<ParcelableRttParams>() {
            public ParcelableRttParams createFromParcel(Parcel parcel) {
                int readInt = parcel.readInt();
                RttParams[] rttParamsArr = new RttParams[readInt];
                for (int i = 0; i < readInt; i++) {
                    RttParams rttParams = new RttParams();
                    rttParamsArr[i] = rttParams;
                    rttParams.deviceType = parcel.readInt();
                    rttParamsArr[i].requestType = parcel.readInt();
                    boolean z = true;
                    rttParamsArr[i].secure = parcel.readByte() != 0;
                    rttParamsArr[i].bssid = parcel.readString();
                    rttParamsArr[i].channelWidth = parcel.readInt();
                    rttParamsArr[i].frequency = parcel.readInt();
                    rttParamsArr[i].centerFreq0 = parcel.readInt();
                    rttParamsArr[i].centerFreq1 = parcel.readInt();
                    rttParamsArr[i].numberBurst = parcel.readInt();
                    rttParamsArr[i].interval = parcel.readInt();
                    rttParamsArr[i].numSamplesPerBurst = parcel.readInt();
                    rttParamsArr[i].numRetriesPerMeasurementFrame = parcel.readInt();
                    rttParamsArr[i].numRetriesPerFTMR = parcel.readInt();
                    rttParamsArr[i].LCIRequest = parcel.readInt() == 1;
                    RttParams rttParams2 = rttParamsArr[i];
                    if (parcel.readInt() != 1) {
                        z = false;
                    }
                    rttParams2.LCRRequest = z;
                    rttParamsArr[i].burstTimeout = parcel.readInt();
                    rttParamsArr[i].preamble = parcel.readInt();
                    rttParamsArr[i].bandwidth = parcel.readInt();
                }
                return new ParcelableRttParams(rttParamsArr);
            }

            public ParcelableRttParams[] newArray(int i) {
                return new ParcelableRttParams[i];
            }
        };
        public RttParams[] mParams;

        public int describeContents() {
            return 0;
        }

        public ParcelableRttParams(RttParams[] rttParamsArr) {
            this.mParams = rttParamsArr == null ? new RttParams[0] : rttParamsArr;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.mParams.length);
            for (RttParams rttParams : this.mParams) {
                parcel.writeInt(rttParams.deviceType);
                parcel.writeInt(rttParams.requestType);
                parcel.writeByte(rttParams.secure ? (byte) 1 : 0);
                parcel.writeString(rttParams.bssid);
                parcel.writeInt(rttParams.channelWidth);
                parcel.writeInt(rttParams.frequency);
                parcel.writeInt(rttParams.centerFreq0);
                parcel.writeInt(rttParams.centerFreq1);
                parcel.writeInt(rttParams.numberBurst);
                parcel.writeInt(rttParams.interval);
                parcel.writeInt(rttParams.numSamplesPerBurst);
                parcel.writeInt(rttParams.numRetriesPerMeasurementFrame);
                parcel.writeInt(rttParams.numRetriesPerFTMR);
                parcel.writeInt(rttParams.LCIRequest ? 1 : 0);
                parcel.writeInt(rttParams.LCRRequest ? 1 : 0);
                parcel.writeInt(rttParams.burstTimeout);
                parcel.writeInt(rttParams.preamble);
                parcel.writeInt(rttParams.bandwidth);
            }
        }
    }

    @Deprecated
    public static class ParcelableRttResults implements Parcelable {
        public static final Parcelable.Creator<ParcelableRttResults> CREATOR = new Parcelable.Creator<ParcelableRttResults>() {
            public ParcelableRttResults createFromParcel(Parcel parcel) {
                int readInt = parcel.readInt();
                if (readInt == 0) {
                    return new ParcelableRttResults((RttResult[]) null);
                }
                RttResult[] rttResultArr = new RttResult[readInt];
                for (int i = 0; i < readInt; i++) {
                    RttResult rttResult = new RttResult();
                    rttResultArr[i] = rttResult;
                    rttResult.bssid = parcel.readString();
                    rttResultArr[i].burstNumber = parcel.readInt();
                    rttResultArr[i].measurementFrameNumber = parcel.readInt();
                    rttResultArr[i].successMeasurementFrameNumber = parcel.readInt();
                    rttResultArr[i].frameNumberPerBurstPeer = parcel.readInt();
                    rttResultArr[i].status = parcel.readInt();
                    rttResultArr[i].measurementType = parcel.readInt();
                    rttResultArr[i].retryAfterDuration = parcel.readInt();
                    rttResultArr[i].f52ts = parcel.readLong();
                    rttResultArr[i].rssi = parcel.readInt();
                    rttResultArr[i].rssiSpread = parcel.readInt();
                    rttResultArr[i].txRate = parcel.readInt();
                    rttResultArr[i].rtt = parcel.readLong();
                    rttResultArr[i].rttStandardDeviation = parcel.readLong();
                    rttResultArr[i].rttSpread = parcel.readLong();
                    rttResultArr[i].distance = parcel.readInt();
                    rttResultArr[i].distanceStandardDeviation = parcel.readInt();
                    rttResultArr[i].distanceSpread = parcel.readInt();
                    rttResultArr[i].burstDuration = parcel.readInt();
                    rttResultArr[i].negotiatedBurstNum = parcel.readInt();
                    rttResultArr[i].LCI = new WifiInformationElement();
                    rttResultArr[i].LCI.f53id = parcel.readByte();
                    if (rttResultArr[i].LCI.f53id != -1) {
                        rttResultArr[i].LCI.data = new byte[parcel.readByte()];
                        parcel.readByteArray(rttResultArr[i].LCI.data);
                    }
                    rttResultArr[i].LCR = new WifiInformationElement();
                    rttResultArr[i].LCR.f53id = parcel.readByte();
                    if (rttResultArr[i].LCR.f53id != -1) {
                        rttResultArr[i].LCR.data = new byte[parcel.readByte()];
                        parcel.readByteArray(rttResultArr[i].LCR.data);
                    }
                    rttResultArr[i].secure = parcel.readByte() != 0;
                }
                return new ParcelableRttResults(rttResultArr);
            }

            public ParcelableRttResults[] newArray(int i) {
                return new ParcelableRttResults[i];
            }
        };
        public RttResult[] mResults;

        public int describeContents() {
            return 0;
        }

        public ParcelableRttResults(RttResult[] rttResultArr) {
            this.mResults = rttResultArr;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < this.mResults.length; i++) {
                sb.append(NavigationBarInflaterView.SIZE_MOD_START + i + "]: ");
                StringBuilder sb2 = new StringBuilder("bssid=");
                sb2.append(this.mResults[i].bssid);
                sb.append(sb2.toString());
                sb.append(", burstNumber=" + this.mResults[i].burstNumber);
                sb.append(", measurementFrameNumber=" + this.mResults[i].measurementFrameNumber);
                sb.append(", successMeasurementFrameNumber=" + this.mResults[i].successMeasurementFrameNumber);
                sb.append(", frameNumberPerBurstPeer=" + this.mResults[i].frameNumberPerBurstPeer);
                sb.append(", status=" + this.mResults[i].status);
                sb.append(", requestType=" + this.mResults[i].requestType);
                sb.append(", measurementType=" + this.mResults[i].measurementType);
                sb.append(", retryAfterDuration=" + this.mResults[i].retryAfterDuration);
                sb.append(", ts=" + this.mResults[i].f52ts);
                sb.append(", rssi=" + this.mResults[i].rssi);
                sb.append(", rssi_spread=" + this.mResults[i].rssi_spread);
                sb.append(", rssiSpread=" + this.mResults[i].rssiSpread);
                sb.append(", tx_rate=" + this.mResults[i].tx_rate);
                sb.append(", txRate=" + this.mResults[i].txRate);
                sb.append(", rxRate=" + this.mResults[i].rxRate);
                sb.append(", rtt_ns=" + this.mResults[i].rtt_ns);
                sb.append(", rtt=" + this.mResults[i].rtt);
                sb.append(", rtt_sd_ns=" + this.mResults[i].rtt_sd_ns);
                sb.append(", rttStandardDeviation=" + this.mResults[i].rttStandardDeviation);
                sb.append(", rtt_spread_ns=" + this.mResults[i].rtt_spread_ns);
                sb.append(", rttSpread=" + this.mResults[i].rttSpread);
                sb.append(", distance_cm=" + this.mResults[i].distance_cm);
                sb.append(", distance=" + this.mResults[i].distance);
                sb.append(", distance_sd_cm=" + this.mResults[i].distance_sd_cm);
                sb.append(", distanceStandardDeviation=" + this.mResults[i].distanceStandardDeviation);
                sb.append(", distance_spread_cm=" + this.mResults[i].distance_spread_cm);
                sb.append(", distanceSpread=" + this.mResults[i].distanceSpread);
                sb.append(", burstDuration=" + this.mResults[i].burstDuration);
                sb.append(", negotiatedBurstNum=" + this.mResults[i].negotiatedBurstNum);
                sb.append(", LCI=" + this.mResults[i].LCI);
                sb.append(", LCR=" + this.mResults[i].LCR);
                sb.append(", secure=" + this.mResults[i].secure);
            }
            return sb.toString();
        }

        public void writeToParcel(Parcel parcel, int i) {
            RttResult[] rttResultArr = this.mResults;
            if (rttResultArr != null) {
                parcel.writeInt(rttResultArr.length);
                for (RttResult rttResult : this.mResults) {
                    parcel.writeString(rttResult.bssid);
                    parcel.writeInt(rttResult.burstNumber);
                    parcel.writeInt(rttResult.measurementFrameNumber);
                    parcel.writeInt(rttResult.successMeasurementFrameNumber);
                    parcel.writeInt(rttResult.frameNumberPerBurstPeer);
                    parcel.writeInt(rttResult.status);
                    parcel.writeInt(rttResult.measurementType);
                    parcel.writeInt(rttResult.retryAfterDuration);
                    parcel.writeLong(rttResult.f52ts);
                    parcel.writeInt(rttResult.rssi);
                    parcel.writeInt(rttResult.rssiSpread);
                    parcel.writeInt(rttResult.txRate);
                    parcel.writeLong(rttResult.rtt);
                    parcel.writeLong(rttResult.rttStandardDeviation);
                    parcel.writeLong(rttResult.rttSpread);
                    parcel.writeInt(rttResult.distance);
                    parcel.writeInt(rttResult.distanceStandardDeviation);
                    parcel.writeInt(rttResult.distanceSpread);
                    parcel.writeInt(rttResult.burstDuration);
                    parcel.writeInt(rttResult.negotiatedBurstNum);
                    parcel.writeByte(rttResult.LCI.f53id);
                    if (rttResult.LCI.f53id != -1) {
                        parcel.writeByte((byte) rttResult.LCI.data.length);
                        parcel.writeByteArray(rttResult.LCI.data);
                    }
                    parcel.writeByte(rttResult.LCR.f53id);
                    if (rttResult.LCR.f53id != -1) {
                        parcel.writeByte((byte) rttResult.LCR.data.length);
                        parcel.writeByteArray(rttResult.LCR.data);
                    }
                    parcel.writeByte(rttResult.secure ? (byte) 1 : 0);
                }
                return;
            }
            parcel.writeInt(0);
        }
    }

    public void startRanging(RttParams[] rttParamsArr, final RttListener rttListener) {
        Log.i(TAG, "Send RTT request to RTT Service");
        if (!this.mNewService.isAvailable()) {
            rttListener.onFailure(-2, "");
            return;
        }
        RangingRequest.Builder builder = new RangingRequest.Builder();
        for (RttParams rttParams : rttParamsArr) {
            if (rttParams.deviceType != 1) {
                rttListener.onFailure(-4, "Only AP peers are supported");
                return;
            }
            ScanResult scanResult = new ScanResult();
            scanResult.BSSID = rttParams.bssid;
            if (rttParams.requestType == 2) {
                scanResult.setFlag(2);
            }
            scanResult.channelWidth = rttParams.channelWidth;
            scanResult.frequency = rttParams.frequency;
            scanResult.centerFreq0 = rttParams.centerFreq0;
            scanResult.centerFreq1 = rttParams.centerFreq1;
            builder.addResponder(android.net.wifi.rtt.ResponderConfig.fromScanResult(scanResult));
        }
        try {
            this.mNewService.startRanging(builder.build(), this.mContext.getMainExecutor(), new RangingResultCallback() {
                public void onRangingFailure(int i) {
                    rttListener.onFailure(i == 2 ? -2 : -1, "");
                }

                public void onRangingResults(List<RangingResult> list) {
                    RttResult[] rttResultArr = new RttResult[list.size()];
                    int i = 0;
                    for (RangingResult next : list) {
                        RttResult rttResult = new RttResult();
                        rttResultArr[i] = rttResult;
                        rttResult.status = next.getStatus();
                        rttResultArr[i].bssid = next.getMacAddress().toString();
                        if (next.getStatus() == 0) {
                            rttResultArr[i].distance = next.getDistanceMm() / 10;
                            rttResultArr[i].distanceStandardDeviation = next.getDistanceStdDevMm() / 10;
                            rttResultArr[i].rssi = next.getRssi() * -2;
                            rttResultArr[i].f52ts = next.getRangingTimestampMillis() * 1000;
                            rttResultArr[i].measurementFrameNumber = next.getNumAttemptedMeasurements();
                            rttResultArr[i].successMeasurementFrameNumber = next.getNumSuccessfulMeasurements();
                        } else {
                            rttResultArr[i].f52ts = SystemClock.elapsedRealtime() * 1000;
                        }
                        i++;
                    }
                    rttListener.onSuccess(rttResultArr);
                }
            });
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "startRanging: invalid arguments - " + e);
            rttListener.onFailure(-4, e.getMessage());
        } catch (SecurityException e2) {
            Log.e(TAG, "startRanging: security exception - " + e2);
            rttListener.onFailure(-5, e2.getMessage());
        }
    }

    public void stopRanging(RttListener rttListener) {
        Log.e(TAG, "stopRanging: unsupported operation - nop");
    }

    public void enableResponder(ResponderCallback responderCallback) {
        throw new UnsupportedOperationException("enableResponder is not supported in the adaptation layer");
    }

    public void disableResponder(ResponderCallback responderCallback) {
        throw new UnsupportedOperationException("disableResponder is not supported in the adaptation layer");
    }

    @Deprecated
    public static class ResponderConfig implements Parcelable {
        public static final Parcelable.Creator<ResponderConfig> CREATOR = new Parcelable.Creator<ResponderConfig>() {
            public ResponderConfig createFromParcel(Parcel parcel) {
                ResponderConfig responderConfig = new ResponderConfig();
                responderConfig.macAddress = parcel.readString();
                responderConfig.frequency = parcel.readInt();
                responderConfig.centerFreq0 = parcel.readInt();
                responderConfig.centerFreq1 = parcel.readInt();
                responderConfig.channelWidth = parcel.readInt();
                responderConfig.preamble = parcel.readInt();
                return responderConfig;
            }

            public ResponderConfig[] newArray(int i) {
                return new ResponderConfig[i];
            }
        };
        public int centerFreq0;
        public int centerFreq1;
        public int channelWidth;
        public int frequency;
        public String macAddress = "";
        public int preamble;

        public int describeContents() {
            return 0;
        }

        public String toString() {
            return "macAddress = " + this.macAddress + " frequency = " + this.frequency + " centerFreq0 = " + this.centerFreq0 + " centerFreq1 = " + this.centerFreq1 + " channelWidth = " + this.channelWidth + " preamble = " + this.preamble;
        }

        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.macAddress);
            parcel.writeInt(this.frequency);
            parcel.writeInt(this.centerFreq0);
            parcel.writeInt(this.centerFreq1);
            parcel.writeInt(this.channelWidth);
            parcel.writeInt(this.preamble);
        }
    }

    public RttManager(Context context, WifiRttManager wifiRttManager) {
        this.mNewService = wifiRttManager;
        this.mContext = context;
        boolean hasSystemFeature = context.getPackageManager().hasSystemFeature("android.hardware.wifi.rtt");
        RttCapabilities rttCapabilities = new RttCapabilities();
        this.mRttCapabilities = rttCapabilities;
        rttCapabilities.oneSidedRttSupported = hasSystemFeature;
        this.mRttCapabilities.twoSided11McRttSupported = hasSystemFeature;
        this.mRttCapabilities.lciSupported = false;
        this.mRttCapabilities.lcrSupported = false;
        this.mRttCapabilities.preambleSupported = 6;
        this.mRttCapabilities.bwSupported = 24;
        this.mRttCapabilities.responderSupported = false;
        this.mRttCapabilities.secureRttSupported = false;
    }
}
