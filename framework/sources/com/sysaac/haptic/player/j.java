package com.sysaac.haptic.player;

import android.content.Context;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes4.dex */
public class j extends com.sysaac.haptic.base.q {
    final /* synthetic */ String a;
    final /* synthetic */ int b;
    final /* synthetic */ int c;
    final /* synthetic */ g d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j(g gVar, String str, int i, int i2) {
        this.d = gVar;
        this.a = str;
        this.b = i;
        this.c = i2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sysaac.haptic.base.q
    public void a() {
        Context context;
        com.sysaac.haptic.base.o oVar;
        int i;
        com.sysaac.haptic.base.o oVar2;
        int i2;
        com.sysaac.haptic.base.o oVar3;
        com.sysaac.haptic.base.o oVar4;
        context = this.d.d;
        com.sysaac.haptic.base.d a = com.sysaac.haptic.base.d.a(context);
        String str = this.a;
        oVar = this.d.l;
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
        a.b(b, 1, 0, i2, this.c);
    }
}
