package android.net.wifi;

import android.annotation.SystemApi;
import android.net.Uri;
import android.util.SparseArray;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class EasyConnectStatusCallback {
    public static final int EASY_CONNECT_EVENT_FAILURE_AUTHENTICATION = -2;
    public static final int EASY_CONNECT_EVENT_FAILURE_BUSY = -5;
    public static final int EASY_CONNECT_EVENT_FAILURE_CANNOT_FIND_NETWORK = -10;
    public static final int EASY_CONNECT_EVENT_FAILURE_CONFIGURATION = -4;
    public static final int EASY_CONNECT_EVENT_FAILURE_ENROLLEE_AUTHENTICATION = -11;

    /* renamed from: EASY_CONNECT_EVENT_FAILURE_ENROLLEE_FAILED_TO_SCAN_NETWORK_CHANNEL */
    public static final int f50xc1dad3ff = -14;
    public static final int EASY_CONNECT_EVENT_FAILURE_ENROLLEE_REJECTED_CONFIGURATION = -12;
    public static final int EASY_CONNECT_EVENT_FAILURE_GENERIC = -7;
    public static final int EASY_CONNECT_EVENT_FAILURE_INVALID_NETWORK = -9;
    public static final int EASY_CONNECT_EVENT_FAILURE_INVALID_URI = -1;
    public static final int EASY_CONNECT_EVENT_FAILURE_NOT_COMPATIBLE = -3;
    public static final int EASY_CONNECT_EVENT_FAILURE_NOT_SUPPORTED = -8;
    public static final int EASY_CONNECT_EVENT_FAILURE_TIMEOUT = -6;
    public static final int EASY_CONNECT_EVENT_FAILURE_URI_GENERATION = -13;
    @SystemApi
    public static final int EASY_CONNECT_EVENT_PROGRESS_AUTHENTICATION_SUCCESS = 0;
    @SystemApi
    public static final int EASY_CONNECT_EVENT_PROGRESS_CONFIGURATION_ACCEPTED = 3;
    @SystemApi
    public static final int EASY_CONNECT_EVENT_PROGRESS_CONFIGURATION_SENT_WAITING_RESPONSE = 2;
    @SystemApi
    public static final int EASY_CONNECT_EVENT_PROGRESS_RESPONSE_PENDING = 1;
    @SystemApi
    public static final int EASY_CONNECT_EVENT_SUCCESS_CONFIGURATION_APPLIED = 1;
    @SystemApi
    public static final int EASY_CONNECT_EVENT_SUCCESS_CONFIGURATION_SENT = 0;

    @Retention(RetentionPolicy.SOURCE)
    public @interface EasyConnectFailureStatusCode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EasyConnectProgressStatusCode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EasyConnectSuccessStatusCode {
    }

    @SystemApi
    public void onBootstrapUriGenerated(Uri uri) {
    }

    @SystemApi
    public abstract void onConfiguratorSuccess(int i);

    @SystemApi
    public abstract void onEnrolleeSuccess(int i);

    @SystemApi
    public void onFailure(int i) {
    }

    @SystemApi
    public abstract void onProgress(int i);

    @SystemApi
    public void onFailure(int i, String str, SparseArray<int[]> sparseArray, int[] iArr) {
        onFailure(i);
    }
}
