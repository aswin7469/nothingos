package com.sysaac.haptic.b.c;

import com.sysaac.haptic.b.a.e;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes2.dex */
public class a implements com.sysaac.haptic.b.a.c {
    public b a;
    public ArrayList<c> b;

    @Override // com.sysaac.haptic.b.a.c
    public int a() {
        return this.a.a;
    }

    @Override // com.sysaac.haptic.b.a.c
    public int b() {
        ArrayList<c> arrayList;
        int i;
        try {
            c cVar = this.b.get(arrayList.size() - 1);
            Iterator<e> it = cVar.b.iterator();
            int i2 = 0;
            while (it.hasNext()) {
                e next = it.next();
                if (next.a.a.equals("continuous")) {
                    com.sysaac.haptic.b.a.b bVar = next.a;
                    i = bVar.b + bVar.c;
                } else {
                    i = next.a.b + 48;
                }
                if (i > i2) {
                    i2 = i;
                }
            }
            return i2 + cVar.a;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
