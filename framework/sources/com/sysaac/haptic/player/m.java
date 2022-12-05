package com.sysaac.haptic.player;
/* loaded from: classes4.dex */
class m extends com.sysaac.haptic.base.q {
    final /* synthetic */ String a;
    final /* synthetic */ l b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m(l lVar, String str) {
        this.b = lVar;
        this.a = str;
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
        l lVar = this.b;
        String str = this.a;
        oVar = lVar.l;
        if (oVar != null) {
            oVar4 = this.b.l;
            i = oVar4.b();
        } else {
            i = -1;
        }
        String b = com.sysaac.haptic.base.r.b(str, i);
        oVar2 = this.b.l;
        if (oVar2 != null) {
            oVar3 = this.b.l;
            i2 = oVar3.a();
        } else {
            i2 = 255;
        }
        lVar.c(b, i2, 0, null);
    }
}
