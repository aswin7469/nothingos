package com.sysaac.haptic;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.sysaac.haptic.base.ApiInfo;
import com.sysaac.haptic.base.r;
import com.sysaac.haptic.player.PlayerEventCallback;
import com.sysaac.haptic.player.l;
import com.sysaac.haptic.sync.SyncCallback;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes4.dex */
public class AACHapticPlayer implements com.sysaac.haptic.player.e {
    private static final boolean DEBUG = false;
    private static final String TAG = "AACHapticPlayer";
    private static final ExecutorService mExcutor = Executors.newSingleThreadExecutor();
    private Context mContext;
    private com.sysaac.haptic.player.f mPlayer;
    private PlayerEventCallback mPlayerEventCallback;

    private AACHapticPlayer() {
    }

    private AACHapticPlayer(Context context, int i) {
        com.sysaac.haptic.player.f bVar;
        Log.i(TAG, "sdk version:" + ApiInfo.VERSION_NAME + " versionCode:" + ApiInfo.VERSION_CODE + ",playerType:" + i);
        this.mContext = context;
        switch (i) {
            case 0:
                bVar = new com.sysaac.haptic.player.b(context);
                break;
            case 1:
                bVar = new l(context);
                break;
            case 2:
                bVar = new com.sysaac.haptic.player.g(context);
                break;
            default:
                Log.w(TAG, "unknown player type:" + i);
                return;
        }
        this.mPlayer = bVar;
    }

    public static void convertM2VHeToWaveformParams(File file, ArrayList<Long> arrayList, ArrayList<Integer> arrayList2) {
        try {
            r.b(r.b(file), arrayList, arrayList2);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static void convertM2VHeToWaveformParams(String str, ArrayList<Long> arrayList, ArrayList<Integer> arrayList2) {
        try {
            r.b(str, arrayList, arrayList2);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public static AACHapticPlayer create(Context context) {
        return com.sysaac.haptic.player.e.a(2) ? create(context, 2) : com.sysaac.haptic.player.e.a(1) ? create(context, 1) : create(context, 0);
    }

    public static AACHapticPlayer create(Context context, int i) {
        String str;
        if (Build.VERSION.SDK_INT < 26) {
            str = "OS is lower than Android O, NOT SUPPORTED!";
        } else if (context == null) {
            str = "context == null";
        } else if (com.sysaac.haptic.player.e.a(i)) {
            return new AACHapticPlayer(context, i);
        } else {
            str = "specified player type not available!";
        }
        Log.e(TAG, str);
        return null;
    }

    public static boolean isSupportedRichTap() {
        return com.sysaac.haptic.player.g.j() || l.j();
    }

    @Override // com.sysaac.haptic.player.e
    public int getCurrentPosition() {
        com.sysaac.haptic.player.f fVar = this.mPlayer;
        if (fVar == null) {
            Log.e(TAG, "null == mPlayer!");
            return 0;
        }
        return fVar.f();
    }

    @Override // com.sysaac.haptic.player.e
    public int getDuration() {
        com.sysaac.haptic.player.f fVar = this.mPlayer;
        if (fVar == null) {
            Log.e(TAG, "null == mPlayer!");
            return 0;
        }
        return fVar.g();
    }

    @Override // com.sysaac.haptic.player.e
    public boolean isPlaying() {
        com.sysaac.haptic.player.f fVar = this.mPlayer;
        if (fVar == null) {
            Log.e(TAG, "null == mPlayer!");
            return false;
        }
        return fVar.h();
    }

    @Override // com.sysaac.haptic.player.e
    public void pause() {
        com.sysaac.haptic.player.f fVar = this.mPlayer;
        if (fVar == null) {
            Log.e(TAG, "null == mPlayer!");
            return;
        }
        fVar.c();
        PlayerEventCallback playerEventCallback = this.mPlayerEventCallback;
        if (playerEventCallback == null) {
            return;
        }
        playerEventCallback.onPlayerStateChanged(7);
    }

    @Override // com.sysaac.haptic.player.e
    public void prepare() {
        PlayerEventCallback playerEventCallback;
        com.sysaac.haptic.player.f fVar = this.mPlayer;
        if (fVar == null) {
            Log.e(TAG, "null == mPlayer!");
        } else if (!fVar.e() || (playerEventCallback = this.mPlayerEventCallback) == null) {
        } else {
            playerEventCallback.onPlayerStateChanged(5);
        }
    }

    @Override // com.sysaac.haptic.player.e
    public void registerPlayerEventCallback(PlayerEventCallback playerEventCallback) {
        this.mPlayerEventCallback = playerEventCallback;
        this.mPlayer.a(playerEventCallback);
    }

    @Override // com.sysaac.haptic.player.e
    public void release() {
        com.sysaac.haptic.player.f fVar = this.mPlayer;
        if (fVar != null) {
            fVar.b();
        }
        PlayerEventCallback playerEventCallback = this.mPlayerEventCallback;
        if (playerEventCallback != null) {
            playerEventCallback.onPlayerStateChanged(1);
        }
    }

    @Override // com.sysaac.haptic.player.e
    public void reset() {
        com.sysaac.haptic.player.f fVar = this.mPlayer;
        if (fVar == null) {
            Log.e(TAG, "null == mPlayer!");
            return;
        }
        fVar.a();
        PlayerEventCallback playerEventCallback = this.mPlayerEventCallback;
        if (playerEventCallback == null) {
            return;
        }
        playerEventCallback.onPlayerStateChanged(0);
    }

    @Override // com.sysaac.haptic.player.e
    public void seekTo(int i) {
        PlayerEventCallback playerEventCallback;
        com.sysaac.haptic.player.f fVar = this.mPlayer;
        if (fVar == null) {
            Log.e(TAG, "null == mPlayer!");
        } else if (!fVar.a(i) || (playerEventCallback = this.mPlayerEventCallback) == null) {
        } else {
            playerEventCallback.onSeekCompleted(i);
        }
    }

    @Override // com.sysaac.haptic.player.e
    public void setDataSource(File file, int i, int i2, SyncCallback syncCallback) {
        com.sysaac.haptic.player.f fVar = this.mPlayer;
        if (fVar == null) {
            Log.e(TAG, "null == mPlayer!");
            return;
        }
        fVar.a(file, i, i2, syncCallback);
        PlayerEventCallback playerEventCallback = this.mPlayerEventCallback;
        if (playerEventCallback == null) {
            return;
        }
        playerEventCallback.onPlayerStateChanged(3);
    }

    @Override // com.sysaac.haptic.player.e
    public void setDataSource(String str, int i, int i2, SyncCallback syncCallback) {
        com.sysaac.haptic.player.f fVar = this.mPlayer;
        if (fVar == null) {
            Log.e(TAG, "null == mPlayer!");
            return;
        }
        fVar.b(str, i, i2, syncCallback);
        PlayerEventCallback playerEventCallback = this.mPlayerEventCallback;
        if (playerEventCallback == null) {
            return;
        }
        playerEventCallback.onPlayerStateChanged(3);
    }

    @Override // com.sysaac.haptic.player.e
    public void setLooping(boolean z) {
        com.sysaac.haptic.player.f fVar = this.mPlayer;
        if (fVar != null) {
            fVar.a(z);
        }
    }

    @Override // com.sysaac.haptic.player.e
    public void start() {
        PlayerEventCallback playerEventCallback;
        com.sysaac.haptic.player.f fVar = this.mPlayer;
        if (fVar == null) {
            Log.e(TAG, "null == mPlayer!");
        } else if (!fVar.d() || (playerEventCallback = this.mPlayerEventCallback) == null) {
        } else {
            playerEventCallback.onPlayerStateChanged(6);
        }
    }

    @Override // com.sysaac.haptic.player.e
    public void stop() {
        com.sysaac.haptic.player.f fVar = this.mPlayer;
        if (fVar == null) {
            Log.e(TAG, "null == mPlayer!");
            return;
        }
        fVar.a();
        PlayerEventCallback playerEventCallback = this.mPlayerEventCallback;
        if (playerEventCallback == null) {
            return;
        }
        playerEventCallback.onPlayerStateChanged(8);
    }

    @Override // com.sysaac.haptic.player.e
    public void unregisterPlayerEventCallback() {
        this.mPlayerEventCallback = null;
    }
}
