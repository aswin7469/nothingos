package com.sysaac.haptic.player;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class i extends com.sysaac.haptic.base.q {
    final /* synthetic */ String a;
    final /* synthetic */ int b;
    final /* synthetic */ int c;
    final /* synthetic */ g d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i(g gVar, String str, int i, int i2) {
        this.d = gVar;
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
        g gVar = this.d;
        String str = this.a;
        oVar = gVar.l;
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
        gVar.c(b, i2, this.c, null);
    }
}
