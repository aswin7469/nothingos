package com.sysaac.haptic.base;

import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class m extends g {
    final /* synthetic */ e a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public m(e eVar) {
        super(eVar);
        this.a = eVar;
        this.e = 7;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sysaac.haptic.base.g
    public int[] a() {
        int[] iArr = new int[this.e];
        Arrays.fill(iArr, 0);
        iArr[0] = this.d;
        iArr[1] = this.e - 2;
        iArr[2] = this.f;
        iArr[3] = this.g;
        iArr[4] = this.h;
        iArr[5] = this.i;
        iArr[6] = this.j;
        return iArr;
    }
}
