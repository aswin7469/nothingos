package com.sysaac.haptic.base;
/* loaded from: classes4.dex */
abstract class j {
    int a;
    int b;
    int c;
    int d;
    int e;

    public j(int i, int i2, int i3) {
        this.a = i;
        this.b = i2;
        this.e = i3;
    }

    abstract int[] a();

    public String toString() {
        return "PatternHeEventBase{mEventType=" + this.a + ", mRelativeTime=" + this.b + ", mDuration=" + this.e + '}';
    }
}
