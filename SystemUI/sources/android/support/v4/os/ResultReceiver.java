package android.support.v4.os;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.IResultReceiver;
@SuppressLint({"BanParcelableUsage"})
/* loaded from: classes.dex */
public class ResultReceiver implements Parcelable {
    public static final Parcelable.Creator<ResultReceiver> CREATOR = new Parcelable.Creator<ResultReceiver>() { // from class: android.support.v4.os.ResultReceiver.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public ResultReceiver mo20createFromParcel(Parcel in) {
            return new ResultReceiver(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public ResultReceiver[] mo21newArray(int size) {
            return new ResultReceiver[size];
        }
    };
    IResultReceiver mReceiver;
    final boolean mLocal = false;
    final Handler mHandler = null;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    protected void onReceiveResult(int resultCode, Bundle resultData) {
    }

    /* loaded from: classes.dex */
    class MyRunnable implements Runnable {
        final int mResultCode;
        final Bundle mResultData;

        MyRunnable(int resultCode, Bundle resultData) {
            this.mResultCode = resultCode;
            this.mResultData = resultData;
        }

        @Override // java.lang.Runnable
        public void run() {
            ResultReceiver.this.onReceiveResult(this.mResultCode, this.mResultData);
        }
    }

    /* loaded from: classes.dex */
    class MyResultReceiver extends IResultReceiver.Stub {
        MyResultReceiver() {
        }

        @Override // android.support.v4.os.IResultReceiver
        public void send(int resultCode, Bundle resultData) {
            ResultReceiver resultReceiver = ResultReceiver.this;
            Handler handler = resultReceiver.mHandler;
            if (handler != null) {
                handler.post(new MyRunnable(resultCode, resultData));
            } else {
                resultReceiver.onReceiveResult(resultCode, resultData);
            }
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        synchronized (this) {
            if (this.mReceiver == null) {
                this.mReceiver = new MyResultReceiver();
            }
            out.writeStrongBinder(this.mReceiver.asBinder());
        }
    }

    ResultReceiver(Parcel in) {
        this.mReceiver = IResultReceiver.Stub.asInterface(in.readStrongBinder());
    }
}
