package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
/* loaded from: classes3.dex */
public final class UssdResponse implements Parcelable {
    public static final Parcelable.Creator<UssdResponse> CREATOR = new Parcelable.Creator<UssdResponse>() { // from class: android.telephony.UssdResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public UssdResponse mo3559createFromParcel(Parcel in) {
            String request = in.readString();
            CharSequence message = TextUtils.CHAR_SEQUENCE_CREATOR.mo3559createFromParcel(in);
            return new UssdResponse(request, message);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public UssdResponse[] mo3560newArray(int size) {
            return new UssdResponse[size];
        }
    };
    private CharSequence mReturnMessage;
    private String mUssdRequest;

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUssdRequest);
        TextUtils.writeToParcel(this.mReturnMessage, dest, 0);
    }

    public String getUssdRequest() {
        return this.mUssdRequest;
    }

    public CharSequence getReturnMessage() {
        return this.mReturnMessage;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public UssdResponse(String ussdRequest, CharSequence returnMessage) {
        this.mUssdRequest = ussdRequest;
        this.mReturnMessage = returnMessage;
    }
}
