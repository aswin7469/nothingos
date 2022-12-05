package com.sysaac.haptic.base;

import java.util.Arrays;
/* loaded from: classes4.dex */
class f extends g {
    int a;
    l[] b;
    final /* synthetic */ e c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public f(e eVar) {
        super(eVar);
        this.c = eVar;
    }

    @Override // com.sysaac.haptic.base.g
    int[] a() {
        int i = 8;
        int[] iArr = new int[(this.a * 3) + 8];
        Arrays.fill(iArr, 0);
        iArr[0] = this.d;
        iArr[1] = ((this.a * 3) + 8) - 2;
        iArr[2] = this.f;
        iArr[3] = this.g;
        iArr[4] = this.h;
        iArr[5] = this.i;
        iArr[6] = this.j;
        iArr[7] = this.a;
        for (int i2 = 0; i2 < this.a; i2++) {
            iArr[i] = this.b[i2].a;
            int i3 = i + 1;
            iArr[i3] = this.b[i2].b;
            int i4 = i3 + 1;
            iArr[i4] = this.b[i2].c;
            i = i4 + 1;
        }
        return iArr;
    }

    @Override // com.sysaac.haptic.base.g
    public String toString() {
        return "Continuous{mPointNum=" + this.a + ", mPoint=" + Arrays.toString(this.b) + '}';
    }
}
