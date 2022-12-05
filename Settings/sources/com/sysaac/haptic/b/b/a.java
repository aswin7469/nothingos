package com.sysaac.haptic.b.b;

import com.sysaac.haptic.b.a.c;
import com.sysaac.haptic.b.a.e;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class a implements c {
    public b a;
    public ArrayList<e> b;

    @Override // com.sysaac.haptic.b.a.c
    public int a() {
        return this.a.a;
    }

    @Override // com.sysaac.haptic.b.a.c
    public int b() {
        try {
            ArrayList<e> arrayList = this.b;
            com.sysaac.haptic.b.a.b bVar = arrayList.get(arrayList.size() - 1).a;
            return "continuous".equals(bVar.a) ? bVar.b + bVar.c : bVar.b + 48;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
