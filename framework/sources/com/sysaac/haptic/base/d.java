package com.sysaac.haptic.base;

import android.content.Context;
import java.io.File;
/* loaded from: classes4.dex */
public abstract class d {
    private static d a = null;

    public static int a(int i, int i2) {
        if (i2 < 41 || i2 > 68) {
            if (i > 0 && i < 50) {
                return 10;
            }
            return (i < 50 || i > 100) ? 0 : 15;
        } else if (i > 0 && i < 50) {
            return 15;
        } else {
            if (i >= 50 && i < 75) {
                return 20;
            }
            return (i < 75 || i > 100) ? 0 : 30;
        }
    }

    public static d a(Context context) {
        if (a == null) {
            synchronized (d.class) {
                if (a == null) {
                    a = new e(context);
                }
            }
        }
        return a;
    }

    public abstract int a(String str);

    public abstract void a();

    public abstract void a(int i);

    public abstract void a(int i, int i2, int i3);

    public abstract void a(File file, int i, int i2, int i3, int i4);

    public abstract void a(String str, int i, int i2, int i3, int i4);

    public abstract void b(int i);

    public abstract void b(int i, int i2);

    public abstract void b(File file, int i, int i2, int i3, int i4);

    public abstract void b(String str, int i, int i2, int i3, int i4);

    public abstract void c(int i);

    public abstract void c(String str, int i, int i2, int i3, int i4);

    public abstract void d(String str, int i, int i2, int i3, int i4);
}