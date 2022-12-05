package com.sysaac.haptic.base;

import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class f extends g {
    int a;
    l[] b;
    final /* synthetic */ e c;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public f(e eVar) {
        super(eVar);
        this.c = eVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sysaac.haptic.base.g
    public int[] a() {
        int i = 8;
        int[] iArr = new int[(this.a * 3) + 8];
        Arrays.fill(iArr, 0);
        iArr[0] = this.d;
        int i2 = this.a;
        iArr[1] = ((i2 * 3) + 8) - 2;
        iArr[2] = this.f;
        iArr[3] = this.g;
        iArr[4] = this.h;
        iArr[5] = this.i;
        iArr[6] = this.j;
        iArr[7] = i2;
        for (int i3 = 0; i3 < this.a; i3++) {
            l[] lVarArr = this.b;
            iArr[i] = lVarArr[i3].a;
            int i4 = i + 1;
            iArr[i4] = lVarArr[i3].b;
            int i5 = i4 + 1;
            iArr[i5] = lVarArr[i3].c;
            i = i5 + 1;
        }
        return iArr;
    }

    @Override // com.sysaac.haptic.base.g
    public String toString() {
        return "Continuous{mPointNum=" + this.a + ", mPoint=" + Arrays.toString(this.b) + '}';
    }
}
