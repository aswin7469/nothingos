package com.sysaac.haptic.player;

import com.sysaac.haptic.b.a.e;
import com.sysaac.haptic.sync.SyncCallback;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class a {
    public String a;
    public long b;
    public int c;
    public int d;
    public int e;
    public com.sysaac.haptic.b.a.c g;
    public SyncCallback h;
    public int i;
    public int k;

    public static boolean a(com.sysaac.haptic.b.a.c cVar) {
        com.sysaac.haptic.b.c.a aVar;
        ArrayList<com.sysaac.haptic.b.c.c> arrayList;
        if (cVar == null) {
            return false;
        }
        if (1 != cVar.a()) {
            return 2 == cVar.a() && (arrayList = (aVar = (com.sysaac.haptic.b.c.a) cVar).b) != null && arrayList.size() >= 1 && aVar.b.get(0).b != null && aVar.b.get(0).b.size() >= 1 && aVar.b.get(0).b.get(0).a != null;
        }
        com.sysaac.haptic.b.b.a aVar2 = (com.sysaac.haptic.b.b.a) cVar;
        ArrayList<e> arrayList2 = aVar2.b;
        return (arrayList2 == null || arrayList2.size() < 1 || aVar2.b.get(0).a == null) ? false : true;
    }

    public String toString() {
        return "CurrentPlayingHeInfo{mHeString='" + this.a + "', mStartTime=" + this.b + ", mLoop=" + this.c + ", mAmplitude=" + this.d + ", mFreq=" + this.e + ", mHeRoot=" + this.g + ", mSyncCallback=" + this.h + ", mStartPosition=" + this.i + ", mStatus:" + this.k + '}';
    }
}
