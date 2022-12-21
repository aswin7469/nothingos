package android.nearby;

import android.annotation.SystemApi;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public interface BroadcastCallback {
    public static final int STATUS_FAILURE = 1;
    public static final int STATUS_FAILURE_ALREADY_REGISTERED = 2;
    public static final int STATUS_FAILURE_MISSING_PERMISSIONS = 4;
    public static final int STATUS_FAILURE_SIZE_EXCEED_LIMIT = 3;
    public static final int STATUS_OK = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface BroadcastStatus {
    }

    void onStatusChanged(int i);
}
