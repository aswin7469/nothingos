package com.sysaac.haptic.base;

import java.util.Arrays;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class i {
    int a;
    g[] b;
    final /* synthetic */ e c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(e eVar) {
        this.c = eVar;
    }

    int a() {
        g[] gVarArr;
        int i = 0;
        for (g gVar : this.b) {
            int i2 = gVar.d;
            if (i2 == 4096) {
                i += (((f) gVar).a * 3) + 8;
            } else if (i2 == 4097) {
                i += 7;
            }
        }
        return i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] a(int i) {
        int[] iArr = new int[b()];
        Arrays.fill(iArr, 0);
        iArr[0] = i;
        iArr[1] = this.a;
        g[] gVarArr = this.b;
        iArr[2] = gVarArr.length;
        int i2 = 3;
        for (g gVar : gVarArr) {
            int[] a = gVar.a();
            System.arraycopy(a, 0, iArr, i2, a.length);
            i2 += a.length;
        }
        return iArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int b() {
        return a() + 3;
    }
}
