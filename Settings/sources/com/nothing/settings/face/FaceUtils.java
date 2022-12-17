package com.nothing.settings.face;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.provider.Settings;
import android.view.View;

public class FaceUtils {
    public static void startShakeAnim(View view) {
        ObjectAnimator.ofFloat(view, "translationX", new float[]{0.0f, 5.6f, 17.9f, 30.2f, 35.8f, 26.4f, 5.7f, -14.9f, -24.3f, -19.6f, -8.4f, 4.9f, 16.1f, 20.8f, 17.0f, 8.1f, -2.6f, -11.5f, -15.3f, -13.4f, -8.7f, -2.6f, 3.4f, 8.1f, 10.0f, 9.6f, 8.4f, 6.8f, 5.0f, 3.2f, 1.6f, 0.4f, 0.0f}).setDuration(550).start();
    }

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
