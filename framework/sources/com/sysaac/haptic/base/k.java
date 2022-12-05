package com.sysaac.haptic.base;

import java.util.Arrays;
/* loaded from: classes4.dex */
class k extends j {
    /* JADX INFO: Access modifiers changed from: package-private */
    public k(int i, int i2, int i3, int i4, int i5) {
        super(i, i2, i3);
        this.a = i;
        this.b = i2;
        this.e = i3;
        this.c = i4;
        this.d = i5;
    }

    @Override // com.sysaac.haptic.base.j
    public int[] a() {
        int[] iArr = new int[17];
        Arrays.fill(iArr, 0);
        iArr[0] = this.a;
        iArr[1] = this.b;
        iArr[2] = this.c;
        iArr[3] = this.d;
        iArr[4] = this.e;
        return iArr;
    }

    @Override // com.sysaac.haptic.base.j
    public String toString() {
        return "PatternHeEventPreBaked{mEventType=" + this.a + ", mRelativeTime=" + this.b + ", mDuration=" + this.e + ", mIntensity=" + this.c + ", mFrequency=" + this.d + '}';
    }
}
