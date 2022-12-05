package com.sysaac.haptic.player;

import com.sysaac.haptic.sync.SyncCallback;
import java.io.File;
/* loaded from: classes4.dex */
public interface e {
    static boolean a(int i) {
        switch (i) {
            case 0:
                return true;
            case 1:
                return l.j();
            case 2:
                return g.j();
            default:
                return false;
        }
    }

    int getCurrentPosition();

    int getDuration();

    boolean isPlaying();

    void pause();

    void prepare();

    void registerPlayerEventCallback(PlayerEventCallback playerEventCallback);

    void release();

    void reset();

    void seekTo(int i);

    void setDataSource(File file, int i, int i2, SyncCallback syncCallback);

    void setDataSource(String str, int i, int i2, SyncCallback syncCallback);

    void setLooping(boolean z);

    void start();

    void stop();

    void unregisterPlayerEventCallback();
}
