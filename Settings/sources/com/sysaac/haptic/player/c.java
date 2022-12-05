package com.sysaac.haptic.player;

import com.sysaac.haptic.base.q;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class c extends q {
    final /* synthetic */ String a;
    final /* synthetic */ int b;
    final /* synthetic */ int c;
    final /* synthetic */ b d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(b bVar, String str, int i, int i2) {
        this.d = bVar;
        this.a = str;
        this.b = i;
        this.c = i2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sysaac.haptic.base.q
    public void a() {
        com.sysaac.haptic.base.o oVar;
        int i;
        com.sysaac.haptic.base.o oVar2;
        int i2;
        com.sysaac.haptic.base.o oVar3;
        com.sysaac.haptic.base.o oVar4;
        b bVar = this.d;
        String str = this.a;
        oVar = bVar.l;
        if (oVar != null) {
            oVar4 = this.d.l;
            i = oVar4.b();
        } else {
            i = -1;
        }
        String b = com.sysaac.haptic.base.r.b(str, i);
        oVar2 = this.d.l;
        if (oVar2 != null) {
            oVar3 = this.d.l;
            i2 = oVar3.a();
        } else {
            i2 = this.b;
        }
        bVar.c(b, i2, this.c, null);
    }
}
