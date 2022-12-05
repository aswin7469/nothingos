package com.sysaac.haptic.player;
/* loaded from: classes4.dex */
class n extends com.sysaac.haptic.base.q {
    final /* synthetic */ String a;
    final /* synthetic */ int b;
    final /* synthetic */ l c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public n(l lVar, String str, int i) {
        this.c = lVar;
        this.a = str;
        this.b = i;
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
        l lVar = this.c;
        String str = this.a;
        oVar = lVar.l;
        if (oVar != null) {
            oVar4 = this.c.l;
            i = oVar4.b();
        } else {
            i = -1;
        }
        String b = com.sysaac.haptic.base.r.b(str, i);
        oVar2 = this.c.l;
        if (oVar2 != null) {
            oVar3 = this.c.l;
            i2 = oVar3.a();
        } else {
            i2 = this.b;
        }
        lVar.c(b, i2, 0, null);
    }
}
