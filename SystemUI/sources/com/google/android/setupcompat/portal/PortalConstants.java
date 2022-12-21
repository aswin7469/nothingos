package com.google.android.setupcompat.portal;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PortalConstants {

    @Retention(RetentionPolicy.SOURCE)
    public @interface PendingReason {
        public static final int IN_PROGRESS = 0;
        public static final int MAX = 6;
        public static final int PROGRESS_REQUEST_ANY_NETWORK = 1;
        public static final int PROGRESS_REQUEST_MOBILE = 3;
        public static final int PROGRESS_REQUEST_REMOVED = 5;
        public static final int PROGRESS_REQUEST_WIFI = 2;
        public static final int PROGRESS_RETRY = 4;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RemainingValues {
        public static final String REMAINING_SIZE_TO_BE_DOWNLOAD_IN_KB = "RemainingSizeInKB";
    }

    private PortalConstants() {
    }
}
