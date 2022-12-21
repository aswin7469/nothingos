package android.net;

import android.annotation.SystemApi;
import android.net.ICaptivePortal;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;

public class CaptivePortal implements Parcelable {
    private static final int APP_REQUEST_BASE = 100;
    @SystemApi
    public static final int APP_REQUEST_REEVALUATION_REQUIRED = 100;
    @SystemApi
    public static final int APP_RETURN_DISMISSED = 0;
    @SystemApi
    public static final int APP_RETURN_UNWANTED = 1;
    @SystemApi
    public static final int APP_RETURN_WANTED_AS_IS = 2;
    public static final Parcelable.Creator<CaptivePortal> CREATOR = new Parcelable.Creator<CaptivePortal>() {
        public CaptivePortal createFromParcel(Parcel parcel) {
            return new CaptivePortal(parcel.readStrongBinder());
        }

        public CaptivePortal[] newArray(int i) {
            return new CaptivePortal[i];
        }
    };
    private final IBinder mBinder;

    public int describeContents() {
        return 0;
    }

    @SystemApi
    @Deprecated
    public void logEvent(int i, String str) {
    }

    public CaptivePortal(IBinder iBinder) {
        this.mBinder = iBinder;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStrongBinder(this.mBinder);
    }

    public void reportCaptivePortalDismissed() {
        try {
            ICaptivePortal.Stub.asInterface(this.mBinder).appResponse(0);
        } catch (RemoteException unused) {
        }
    }

    public void ignoreNetwork() {
        try {
            ICaptivePortal.Stub.asInterface(this.mBinder).appResponse(1);
        } catch (RemoteException unused) {
        }
    }

    @SystemApi
    public void useNetwork() {
        try {
            ICaptivePortal.Stub.asInterface(this.mBinder).appResponse(2);
        } catch (RemoteException unused) {
        }
    }

    @SystemApi
    public void reevaluateNetwork() {
        try {
            ICaptivePortal.Stub.asInterface(this.mBinder).appRequest(100);
        } catch (RemoteException unused) {
        }
    }
}
