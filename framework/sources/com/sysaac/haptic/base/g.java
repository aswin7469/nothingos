package com.sysaac.haptic.base;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public abstract class g {
    int d;
    int e;
    int f;
    int g;
    int h;
    int i;
    int j;
    final /* synthetic */ e k;

    /* JADX INFO: Access modifiers changed from: package-private */
    public g(e eVar) {
        this.k = eVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int[] a();

    public String toString() {
        return "Event{mType=" + this.d + ", mVibId=" + this.f + ", mRelativeTime=" + this.g + ", mIntensity=" + this.h + ", mFreq=" + this.i + ", mDuration=" + this.j + '}';
    }
}
