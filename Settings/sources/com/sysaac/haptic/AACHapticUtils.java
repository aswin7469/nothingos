package com.sysaac.haptic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import com.sysaac.haptic.base.ApiInfo;
import com.sysaac.haptic.base.r;
import com.sysaac.haptic.player.b;
import com.sysaac.haptic.player.f;
import com.sysaac.haptic.player.g;
import com.sysaac.haptic.player.l;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes2.dex */
public class AACHapticUtils extends ApiInfo {
    private static final ExecutorService mExcutor = Executors.newSingleThreadExecutor();
    @SuppressLint({"StaticFieldLeak"})
    private static AACHapticUtils sInstance;
    private Context mContext;
    private f mPlayer;
    private Vibrator mVibrator;

    @SuppressLint({"PrivateApi"})
    private AACHapticUtils() {
    }

    public static AACHapticUtils getInstance() {
        if (sInstance == null) {
            synchronized (AACHapticUtils.class) {
                if (sInstance == null) {
                    sInstance = new AACHapticUtils();
                }
            }
        }
        return sInstance;
    }

    private f getPlayer(Context context) {
        String str;
        if (this.mVibrator == null) {
            str = "Please call the init method first";
        } else if (Build.VERSION.SDK_INT >= 26) {
            this.mPlayer = g.j() ? new g(this.mContext) : l.j() ? new l(this.mContext) : new b(this.mContext);
            return this.mPlayer;
        } else {
            str = "OS is lower than Android O!";
        }
        Log.e("AACHapticUtils", str);
        return null;
    }

    public AACHapticUtils init(Context context) {
        if (context != null) {
            Log.i("AACHapticUtils", "init ,version:" + ApiInfo.VERSION_NAME + " versionCode:" + ApiInfo.VERSION_CODE);
            this.mVibrator = (Vibrator) context.getSystemService("vibrator");
            this.mContext = context;
            useNonRichTap(false);
            return sInstance;
        }
        throw new IllegalArgumentException("context shouldn't be null");
    }

    public boolean isSupportedRichTap() {
        return g.j() || l.j();
    }

    public void playPattern(File file, int i) {
        playPattern(file, i, 0, 255, 0);
    }

    public void playPattern(File file, int i, int i2, int i3, int i4) {
        if (this.mVibrator == null) {
            Log.e("AACHapticUtils", "Please call the init method");
        } else if (i < 1) {
            throw new IllegalArgumentException("Wrong parameter {loop: " + i + "} less than 1!");
        } else if (r.a(file.getPath(), ".he")) {
            if (r.b(file.getPath(), ".he")) {
                mExcutor.execute(new d(this, file, i, i2, i3, i4));
            } else {
                Log.e("AACHapticUtils", "Input file is not he format!!");
            }
        } else {
            throw new IllegalArgumentException("Wrong parameter {patternFile: " + file.getPath() + "} doesn't exist or has wrong file format!");
        }
    }

    public void useNonRichTap(boolean z) {
        this.mPlayer = z ? new b(this.mContext) : getPlayer(this.mContext);
    }
}
