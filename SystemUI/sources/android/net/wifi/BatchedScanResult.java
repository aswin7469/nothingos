package android.net.wifi;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

@SystemApi
@Deprecated
public class BatchedScanResult implements Parcelable {
    public static final Parcelable.Creator<BatchedScanResult> CREATOR = new Parcelable.Creator<BatchedScanResult>() {
        public BatchedScanResult createFromParcel(Parcel parcel) {
            BatchedScanResult batchedScanResult = new BatchedScanResult();
            boolean z = true;
            if (parcel.readInt() != 1) {
                z = false;
            }
            batchedScanResult.truncated = z;
            int readInt = parcel.readInt();
            while (true) {
                int i = readInt - 1;
                if (readInt <= 0) {
                    return batchedScanResult;
                }
                batchedScanResult.scanResults.add(ScanResult.CREATOR.createFromParcel(parcel));
                readInt = i;
            }
        }

        public BatchedScanResult[] newArray(int i) {
            return new BatchedScanResult[i];
        }
    };
    private static final String TAG = "BatchedScanResult";
    public final List<ScanResult> scanResults = new ArrayList();
    public boolean truncated;

    public int describeContents() {
        return 0;
    }

    public BatchedScanResult() {
    }

    public BatchedScanResult(BatchedScanResult batchedScanResult) {
        this.truncated = batchedScanResult.truncated;
        for (ScanResult scanResult : batchedScanResult.scanResults) {
            this.scanResults.add(new ScanResult(scanResult));
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("BatchedScanResult: truncated: ");
        stringBuffer.append(String.valueOf(this.truncated)).append("scanResults: [");
        for (ScanResult scanResult : this.scanResults) {
            stringBuffer.append(" <").append(scanResult.toString()).append("> ");
        }
        stringBuffer.append(" ]");
        return stringBuffer.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.truncated ? 1 : 0);
        parcel.writeInt(this.scanResults.size());
        for (ScanResult writeToParcel : this.scanResults) {
            writeToParcel.writeToParcel(parcel, i);
        }
    }
}
