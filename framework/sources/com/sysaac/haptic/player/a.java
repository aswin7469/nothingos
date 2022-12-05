package com.sysaac.haptic.player;

import android.text.format.DateFormat;
import com.sysaac.haptic.sync.SyncCallback;
import java.io.File;
import java.util.Iterator;
/* loaded from: classes4.dex */
public class a {
    private static final String m = "CurrentPlayingInfo";
    public String a;
    public long b;
    public int c;
    public int d;
    public int e;
    public int f;
    public com.sysaac.haptic.b.a.c g;
    public SyncCallback h;
    public int i;
    public int j;
    public int k;
    public File l;

    public static boolean a(com.sysaac.haptic.b.a.c cVar) {
        com.sysaac.haptic.b.c.a aVar;
        if (cVar == null) {
            return false;
        }
        if (1 != cVar.a()) {
            return (2 != cVar.a() || (aVar = (com.sysaac.haptic.b.c.a) cVar) == null || aVar.b == null || aVar.b.size() < 1 || aVar.b.get(0).b == null || aVar.b.get(0).b.size() < 1 || aVar.b.get(0).b.get(0).a == null) ? false : true;
        }
        com.sysaac.haptic.b.b.a aVar2 = (com.sysaac.haptic.b.b.a) cVar;
        return (aVar2 == null || aVar2.b == null || aVar2.b.size() < 1 || aVar2.b.get(0).a == null) ? false : true;
    }

    public void a() {
        this.a = null;
        this.b = 0L;
        this.c = 0;
        this.d = 0;
        this.e = 0;
        this.f = 0;
        this.g = null;
        this.h = null;
        this.i = 0;
        this.k = 0;
    }

    public int b() {
        com.sysaac.haptic.b.a.c cVar = this.g;
        if (cVar == null) {
            return -1;
        }
        if (2 == cVar.a()) {
            Iterator<com.sysaac.haptic.b.c.c> it = ((com.sysaac.haptic.b.c.a) this.g).b.iterator();
            int i = 0;
            while (it.hasNext()) {
                com.sysaac.haptic.b.c.c next = it.next();
                i += next.b == null ? 0 : next.b.size();
            }
            return i;
        } else if (1 != this.g.a()) {
            return -1;
        } else {
            com.sysaac.haptic.b.b.a aVar = (com.sysaac.haptic.b.b.a) this.g;
            if (aVar.b != null) {
                return aVar.b.size();
            }
            return 0;
        }
    }

    public String toString() {
        return "CurrentPlayingHeInfo{mHeString='" + this.a + DateFormat.QUOTE + ", mStartTime=" + this.b + ", mLoop=" + this.c + ", mAmplitude=" + this.d + ", mFreq=" + this.e + ", mHeRoot=" + this.g + ", mSyncCallback=" + this.h + ", mStartPosition=" + this.i + ", mStatus:" + this.k + '}';
    }
}
