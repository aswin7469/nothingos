package android.app.job;

import android.app.job.IJobCallback;
import android.content.ClipData;
import android.net.Network;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.PowerManager;
import android.os.RemoteException;
import android.provider.DeviceConfig;
import com.android.internal.location.GpsNetInitiatedHandler;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public class JobParameters implements Parcelable {
    public static final int INTERNAL_STOP_REASON_CANCELED = 0;
    public static final int INTERNAL_STOP_REASON_CONSTRAINTS_NOT_SATISFIED = 1;
    public static final int INTERNAL_STOP_REASON_DATA_CLEARED = 8;
    public static final int INTERNAL_STOP_REASON_DEVICE_IDLE = 4;
    public static final int INTERNAL_STOP_REASON_DEVICE_THERMAL = 5;
    public static final int INTERNAL_STOP_REASON_PREEMPT = 2;
    public static final int INTERNAL_STOP_REASON_RESTRICTED_BUCKET = 6;
    public static final int INTERNAL_STOP_REASON_RTC_UPDATED = 9;
    public static final int INTERNAL_STOP_REASON_SUCCESSFUL_FINISH = 10;
    public static final int INTERNAL_STOP_REASON_TIMEOUT = 3;
    public static final int INTERNAL_STOP_REASON_UNINSTALL = 7;
    public static final int STOP_REASON_APP_STANDBY = 12;
    public static final int STOP_REASON_BACKGROUND_RESTRICTION = 11;
    public static final int STOP_REASON_CANCELLED_BY_APP = 1;
    public static final int STOP_REASON_CONSTRAINT_BATTERY_NOT_LOW = 5;
    public static final int STOP_REASON_CONSTRAINT_CHARGING = 6;
    public static final int STOP_REASON_CONSTRAINT_CONNECTIVITY = 7;
    public static final int STOP_REASON_CONSTRAINT_DEVICE_IDLE = 8;
    public static final int STOP_REASON_CONSTRAINT_STORAGE_NOT_LOW = 9;
    public static final int STOP_REASON_DEVICE_STATE = 4;
    public static final int STOP_REASON_PREEMPT = 2;
    public static final int STOP_REASON_QUOTA = 10;
    public static final int STOP_REASON_SYSTEM_PROCESSING = 14;
    public static final int STOP_REASON_TIMEOUT = 3;
    public static final int STOP_REASON_UNDEFINED = 0;
    public static final int STOP_REASON_USER = 13;
    private final IBinder callback;
    private final ClipData clipData;
    private final int clipGrantFlags;
    private String debugStopReason;
    private final PersistableBundle extras;
    private final int jobId;
    private int mInternalStopReason;
    private final boolean mIsExpedited;
    private int mStopReason;
    private final String[] mTriggeredContentAuthorities;
    private final Uri[] mTriggeredContentUris;
    private final Network network;
    private final boolean overrideDeadlineExpired;
    private final Bundle transientExtras;
    public static final int[] JOB_STOP_REASON_CODES = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    public static final Parcelable.Creator<JobParameters> CREATOR = new Parcelable.Creator<JobParameters>() { // from class: android.app.job.JobParameters.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public JobParameters mo3559createFromParcel(Parcel in) {
            return new JobParameters(in);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public JobParameters[] mo3560newArray(int size) {
            return new JobParameters[size];
        }
    };

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface StopReason {
    }

    public static String getInternalReasonCodeDescription(int reasonCode) {
        switch (reasonCode) {
            case 0:
                return "canceled";
            case 1:
                return "constraints";
            case 2:
                return "preempt";
            case 3:
                return GpsNetInitiatedHandler.NI_INTENT_KEY_TIMEOUT;
            case 4:
                return DeviceConfig.NAMESPACE_DEVICE_IDLE;
            case 5:
                return PowerManager.SHUTDOWN_THERMAL_STATE;
            case 6:
                return "restricted_bucket";
            case 7:
                return "uninstall";
            case 8:
                return "data_cleared";
            case 9:
                return "rtc_updated";
            case 10:
                return "successful_finish";
            default:
                return "unknown:" + reasonCode;
        }
    }

    public static int[] getJobStopReasonCodes() {
        return JOB_STOP_REASON_CODES;
    }

    public JobParameters(IBinder callback, int jobId, PersistableBundle extras, Bundle transientExtras, ClipData clipData, int clipGrantFlags, boolean overrideDeadlineExpired, boolean isExpedited, Uri[] triggeredContentUris, String[] triggeredContentAuthorities, Network network) {
        this.mStopReason = 0;
        this.jobId = jobId;
        this.extras = extras;
        this.transientExtras = transientExtras;
        this.clipData = clipData;
        this.clipGrantFlags = clipGrantFlags;
        this.callback = callback;
        this.overrideDeadlineExpired = overrideDeadlineExpired;
        this.mIsExpedited = isExpedited;
        this.mTriggeredContentUris = triggeredContentUris;
        this.mTriggeredContentAuthorities = triggeredContentAuthorities;
        this.network = network;
    }

    public int getJobId() {
        return this.jobId;
    }

    public int getStopReason() {
        return this.mStopReason;
    }

    public int getInternalStopReasonCode() {
        return this.mInternalStopReason;
    }

    public String getDebugStopReason() {
        return this.debugStopReason;
    }

    public PersistableBundle getExtras() {
        return this.extras;
    }

    public Bundle getTransientExtras() {
        return this.transientExtras;
    }

    public ClipData getClipData() {
        return this.clipData;
    }

    public int getClipGrantFlags() {
        return this.clipGrantFlags;
    }

    public boolean isExpeditedJob() {
        return this.mIsExpedited;
    }

    public boolean isOverrideDeadlineExpired() {
        return this.overrideDeadlineExpired;
    }

    public Uri[] getTriggeredContentUris() {
        return this.mTriggeredContentUris;
    }

    public String[] getTriggeredContentAuthorities() {
        return this.mTriggeredContentAuthorities;
    }

    public Network getNetwork() {
        return this.network;
    }

    public JobWorkItem dequeueWork() {
        try {
            return getCallback().dequeueWork(getJobId());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void completeWork(JobWorkItem work) {
        try {
            if (!getCallback().completeWork(getJobId(), work.getWorkId())) {
                throw new IllegalArgumentException("Given work is not active: " + work);
            }
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public IJobCallback getCallback() {
        return IJobCallback.Stub.asInterface(this.callback);
    }

    private JobParameters(Parcel in) {
        boolean z = false;
        this.mStopReason = 0;
        this.jobId = in.readInt();
        this.extras = in.readPersistableBundle();
        this.transientExtras = in.readBundle();
        if (in.readInt() != 0) {
            this.clipData = ClipData.CREATOR.mo3559createFromParcel(in);
            this.clipGrantFlags = in.readInt();
        } else {
            this.clipData = null;
            this.clipGrantFlags = 0;
        }
        this.callback = in.readStrongBinder();
        this.overrideDeadlineExpired = in.readInt() == 1 ? true : z;
        this.mIsExpedited = in.readBoolean();
        this.mTriggeredContentUris = (Uri[]) in.createTypedArray(Uri.CREATOR);
        this.mTriggeredContentAuthorities = in.createStringArray();
        if (in.readInt() != 0) {
            this.network = (Network) Network.CREATOR.mo3559createFromParcel(in);
        } else {
            this.network = null;
        }
        this.mStopReason = in.readInt();
        this.mInternalStopReason = in.readInt();
        this.debugStopReason = in.readString();
    }

    public void setStopReason(int reason, int internalStopReason, String debugStopReason) {
        this.mStopReason = reason;
        this.mInternalStopReason = internalStopReason;
        this.debugStopReason = debugStopReason;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.jobId);
        dest.writePersistableBundle(this.extras);
        dest.writeBundle(this.transientExtras);
        if (this.clipData != null) {
            dest.writeInt(1);
            this.clipData.writeToParcel(dest, flags);
            dest.writeInt(this.clipGrantFlags);
        } else {
            dest.writeInt(0);
        }
        dest.writeStrongBinder(this.callback);
        dest.writeInt(this.overrideDeadlineExpired ? 1 : 0);
        dest.writeBoolean(this.mIsExpedited);
        dest.writeTypedArray(this.mTriggeredContentUris, flags);
        dest.writeStringArray(this.mTriggeredContentAuthorities);
        if (this.network != null) {
            dest.writeInt(1);
            this.network.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.mStopReason);
        dest.writeInt(this.mInternalStopReason);
        dest.writeString(this.debugStopReason);
    }
}