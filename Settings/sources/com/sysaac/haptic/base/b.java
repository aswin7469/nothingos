package com.sysaac.haptic.base;
/* loaded from: classes2.dex */
public class b {
    String a;
    int b;
    int c;
    int d;
    int e;
    public long f;
    int h;
    public boolean g = true;
    public int i = 0;

    public b(String str, int i, int i2, int i3, int i4) {
        this.a = str;
        this.b = i;
        this.c = i2;
        this.d = i3;
        this.e = i4;
    }

    public int a() {
        return this.h;
    }

    public void a(int i) {
        this.h = i;
    }

    public String b() {
        return this.a;
    }

    public int c() {
        return this.b;
    }

    public int d() {
        return this.c;
    }

    public int e() {
        return this.d;
    }

    public int f() {
        return this.e;
    }

    public String toString() {
        return "NonRichTapLooperInfo{mPattern='" + this.a + "', mLooper=" + this.b + ", mInterval=" + this.c + ", mAmplitude=" + this.d + ", mFreq=" + this.e + ", mWhen=" + this.f + ", mValid=" + this.g + ", mPatternLastTime=" + this.h + ", mHasVibNum=" + this.i + '}';
    }
}
