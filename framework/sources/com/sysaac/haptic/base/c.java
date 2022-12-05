package com.sysaac.haptic.base;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes4.dex */
public class c extends Thread {
    final Context c;
    private final String f = "NonRichTapThread";
    final Object a = new Object();
    final Object b = new Object();
    volatile boolean d = false;
    List<b> e = new ArrayList();

    public c(Context context) {
        this.c = context;
    }

    long a() {
        return SystemClock.elapsedRealtime();
    }

    public void a(int i) {
        b bVar;
        long a;
        synchronized (this.a) {
            synchronized (this.b) {
                int size = this.e.size();
                if (size > 1) {
                    Log.d("NonRichTapThread", "vibrating ,so interrupt it,size > 1,remove one");
                    this.e.get(1).g = false;
                    bVar = this.e.get(0);
                    a = a();
                } else if (size > 0) {
                    Log.d("NonRichTapThread", "vibrating ,so interrupt it,size == 1,just set next time play");
                    bVar = this.e.get(0);
                    a = a();
                }
                bVar.f = a + i + 5;
            }
            try {
                this.a.notify();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void a(b bVar) {
        synchronized (this.a) {
            bVar.a(bVar.d() + d.a(this.c).a(bVar.b()));
            bVar.f = 0L;
            synchronized (this.b) {
                if (this.e.size() > 0) {
                    Log.d("NonRichTapThread", "vibrating ,interrupt it");
                    this.e.get(0).g = false;
                }
                this.e.add(0, bVar);
            }
            try {
                this.a.notify();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void b() {
        synchronized (this.a) {
            try {
            } catch (Exception e) {
                e.printStackTrace();
            }
            synchronized (this.b) {
                if (this.e.isEmpty()) {
                    return;
                }
                b bVar = this.e.get(0);
                if (bVar.g) {
                    bVar.g = false;
                }
                this.a.notify();
            }
        }
    }

    public void b(b bVar) {
        synchronized (this.a) {
            synchronized (this.b) {
                if (this.e.isEmpty()) {
                    Log.d("NonRichTapThread", "vib list is empty,do nothing!!");
                    return;
                }
                int e = bVar.e();
                b bVar2 = this.e.get(0);
                if (!bVar2.g) {
                    return;
                }
                if (e != -1) {
                    bVar2.c(e);
                }
                int d = bVar.d();
                if (d != -1) {
                    int d2 = d - bVar2.d();
                    int a = bVar2.a() + d2;
                    Log.d("NonRichTapThread", "updateParam interval:" + d + " pre interval:" + bVar2.d() + " delta:" + d2 + " duration:" + a);
                    bVar2.b(d);
                    bVar2.a(a);
                }
                int f = bVar.f();
                if (f != -1) {
                    bVar2.d(f);
                }
                try {
                    this.a.notify();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public void c() {
        this.d = true;
        synchronized (this.a) {
            try {
                synchronized (this.b) {
                    this.e.clear();
                    this.e = null;
                }
                this.a.notify();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    boolean d() {
        synchronized (this.b) {
            for (b bVar : this.e) {
                if (bVar.g) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        b bVar;
        String str;
        String str2;
        String str3 = "NonRichTapThread";
        String str4 = "non richTap thread start!!";
        loop0: while (true) {
            Log.d(str3, str4);
            while (!this.d) {
                List<b> list = this.e;
                if (list != null) {
                    if (list.isEmpty() || !d()) {
                        synchronized (this.a) {
                            try {
                                synchronized (this.b) {
                                    this.e.clear();
                                }
                                Log.d("NonRichTapThread", "nothing is in list,just wait!!");
                                this.a.wait();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        long a = a();
                        bVar = this.e.get(0);
                        if (bVar.g) {
                            if (bVar.f > a) {
                                long j = bVar.f - a;
                                synchronized (this.a) {
                                    try {
                                        Log.d("NonRichTapThread", "go to sleep :" + j);
                                        this.a.wait(j);
                                    } catch (Exception e2) {
                                        e2.printStackTrace();
                                    }
                                }
                                if (bVar.i > bVar.c()) {
                                    str = "NonRichTapThread";
                                    str2 = " looper finished,remove it!!";
                                }
                            } else {
                                d.a(this.c).c(bVar.b(), bVar.c(), bVar.d(), bVar.e(), bVar.f());
                                bVar.i++;
                                Log.d("NonRichTapThread", " vib mHasVibNum:" + bVar.i);
                                if (bVar.i >= bVar.c()) {
                                    str = "NonRichTapThread";
                                    str2 = " wake up vib looper is end ,remove it!!";
                                }
                            }
                            Log.d(str, str2);
                            bVar.g = false;
                        } else {
                            continue;
                        }
                    }
                }
            }
            Log.d("NonRichTapThread", "non richTap thread quit!");
            return;
            bVar.f = a() + bVar.a();
            str3 = "NonRichTapThread";
            str4 = " vib now:" + a() + " mWhen:" + bVar.f + " lastTime:" + bVar.a();
        }
    }
}
