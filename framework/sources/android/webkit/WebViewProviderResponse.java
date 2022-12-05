package android.webkit;

import android.content.pm.PackageInfo;
import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes3.dex */
public final class WebViewProviderResponse implements Parcelable {
    public static final Parcelable.Creator<WebViewProviderResponse> CREATOR = new Parcelable.Creator<WebViewProviderResponse>() { // from class: android.webkit.WebViewProviderResponse.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public WebViewProviderResponse mo3559createFromParcel(Parcel in) {
            return new WebViewProviderResponse(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public WebViewProviderResponse[] mo3560newArray(int size) {
            return new WebViewProviderResponse[size];
        }
    };
    public final PackageInfo packageInfo;
    public final int status;

    public WebViewProviderResponse(PackageInfo packageInfo, int status) {
        this.packageInfo = packageInfo;
        this.status = status;
    }

    private WebViewProviderResponse(Parcel in) {
        this.packageInfo = (PackageInfo) in.readTypedObject(PackageInfo.CREATOR);
        this.status = in.readInt();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeTypedObject(this.packageInfo, flags);
        out.writeInt(this.status);
    }
}