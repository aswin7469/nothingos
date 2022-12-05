package android.media;

import android.text.TextUtils;
import android.util.Log;
/* loaded from: classes2.dex */
public class MediaRouter2Utils {
    static final String SEPARATOR = ":";
    static final String TAG = "MR2Utils";

    public static String toUniqueId(String providerId, String id) {
        if (TextUtils.isEmpty(providerId)) {
            Log.w(TAG, "toUniqueId: providerId shouldn't be empty");
            return null;
        } else if (TextUtils.isEmpty(id)) {
            Log.w(TAG, "toUniqueId: id shouldn't be null");
            return null;
        } else {
            return providerId + ":" + id;
        }
    }

    public static String getProviderId(String uniqueId) {
        if (TextUtils.isEmpty(uniqueId)) {
            Log.w(TAG, "getProviderId: uniqueId shouldn't be empty");
            return null;
        }
        int firstIndexOfSeparator = uniqueId.indexOf(":");
        if (firstIndexOfSeparator == -1) {
            return null;
        }
        String providerId = uniqueId.substring(0, firstIndexOfSeparator);
        if (!TextUtils.isEmpty(providerId)) {
            return providerId;
        }
        return null;
    }

    public static String getOriginalId(String uniqueId) {
        if (TextUtils.isEmpty(uniqueId)) {
            Log.w(TAG, "getOriginalId: uniqueId shouldn't be empty");
            return null;
        }
        int firstIndexOfSeparator = uniqueId.indexOf(":");
        if (firstIndexOfSeparator == -1 || firstIndexOfSeparator + 1 >= uniqueId.length()) {
            return null;
        }
        String providerId = uniqueId.substring(firstIndexOfSeparator + 1);
        if (!TextUtils.isEmpty(providerId)) {
            return providerId;
        }
        return null;
    }
}
