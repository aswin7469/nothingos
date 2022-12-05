package com.nt.settings.face;

import android.content.Context;
import android.provider.Settings;
/* loaded from: classes2.dex */
public class NtFaceUtil {
    public static int getFaceDataCount(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "nt_face_management_added_data", 0);
    }

    public static void setFaceDataCount(Context context, int i) {
        Settings.Secure.putInt(context.getContentResolver(), "nt_face_management_added_data", i);
    }

    public static boolean isFaceDataExist(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(), "nt_face_management_added_data", 0) > 0;
    }
}
