package com.sysaac.haptic.b.c;

import com.sysaac.haptic.b.a.e;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes4.dex */
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
        try {
            c cVar = this.b.get(arrayList.size() - 1);
            Iterator<e> it = cVar.b.iterator();
            int i = 0;
            while (it.hasNext()) {
                e next = it.next();
                int i2 = next.a.a.equals("continuous") ? next.a.b + next.a.c : next.a.b + 48;
                if (i2 > i) {
                    i = i2;
                }
            }
            return i + cVar.a;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
