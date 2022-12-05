package android.os;

import android.annotation.SystemApi;
import android.os.Parcelable;
import android.util.IntArray;
import java.util.ArrayList;
@SystemApi
/* loaded from: classes2.dex */
public final class IncidentReportArgs implements Parcelable {
    public static final Parcelable.Creator<IncidentReportArgs> CREATOR = new Parcelable.Creator<IncidentReportArgs>() { // from class: android.os.IncidentReportArgs.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public IncidentReportArgs mo3559createFromParcel(Parcel in) {
            return new IncidentReportArgs(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public IncidentReportArgs[] mo3560newArray(int size) {
            return new IncidentReportArgs[size];
        }
    };
    private boolean mAll;
    private final ArrayList<byte[]> mHeaders;
    private int mPrivacyPolicy;
    private String mReceiverCls;
    private String mReceiverPkg;
    private final IntArray mSections;

    public IncidentReportArgs() {
        this.mSections = new IntArray();
        this.mHeaders = new ArrayList<>();
        this.mPrivacyPolicy = 200;
    }

    public IncidentReportArgs(Parcel in) {
        this.mSections = new IntArray();
        this.mHeaders = new ArrayList<>();
        readFromParcel(in);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mAll ? 1 : 0);
        int N = this.mSections.size();
        out.writeInt(N);
        for (int i = 0; i < N; i++) {
            out.writeInt(this.mSections.get(i));
        }
        int N2 = this.mHeaders.size();
        out.writeInt(N2);
        for (int i2 = 0; i2 < N2; i2++) {
            out.writeByteArray(this.mHeaders.get(i2));
        }
        int i3 = this.mPrivacyPolicy;
        out.writeInt(i3);
        out.writeString(this.mReceiverPkg);
        out.writeString(this.mReceiverCls);
    }

    public void readFromParcel(Parcel in) {
        this.mAll = in.readInt() != 0;
        this.mSections.clear();
        int N = in.readInt();
        for (int i = 0; i < N; i++) {
            this.mSections.add(in.readInt());
        }
        this.mHeaders.clear();
        int N2 = in.readInt();
        for (int i2 = 0; i2 < N2; i2++) {
            this.mHeaders.add(in.createByteArray());
        }
        int i3 = in.readInt();
        this.mPrivacyPolicy = i3;
        this.mReceiverPkg = in.readString();
        this.mReceiverCls = in.readString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Incident(");
        if (this.mAll) {
            sb.append("all");
        } else {
            int N = this.mSections.size();
            if (N > 0) {
                sb.append(this.mSections.get(0));
            }
            for (int i = 1; i < N; i++) {
                sb.append(" ");
                sb.append(this.mSections.get(i));
            }
        }
        sb.append(", ");
        sb.append(this.mHeaders.size());
        sb.append(" headers), ");
        sb.append("privacy: ");
        sb.append(this.mPrivacyPolicy);
        sb.append("receiver pkg: ");
        sb.append(this.mReceiverPkg);
        sb.append("receiver cls: ");
        sb.append(this.mReceiverCls);
        return sb.toString();
    }

    public void setAll(boolean all) {
        this.mAll = all;
        if (all) {
            this.mSections.clear();
        }
    }

    public void setPrivacyPolicy(int privacyPolicy) {
        switch (privacyPolicy) {
            case 0:
            case 100:
            case 200:
                this.mPrivacyPolicy = privacyPolicy;
                return;
            default:
                this.mPrivacyPolicy = 200;
                return;
        }
    }

    public void addSection(int section) {
        if (!this.mAll && section > 1) {
            this.mSections.add(section);
        }
    }

    public boolean isAll() {
        return this.mAll;
    }

    public boolean containsSection(int section) {
        return this.mAll || this.mSections.indexOf(section) >= 0;
    }

    public int sectionCount() {
        return this.mSections.size();
    }

    public void addHeader(byte[] header) {
        this.mHeaders.add(header);
    }
}
