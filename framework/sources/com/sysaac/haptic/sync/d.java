package com.sysaac.haptic.sync;

import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;
import java.io.FileDescriptor;
/* loaded from: classes4.dex */
public class d {
    public static final int b = 100;
    public static final int c = 101;
    public static final int d = 102;
    public static final long e = 10;
    private static final String f = "VibrationTrack";
    private static final int g = 20;
    private Handler i;
    private FileDescriptor j;
    private a k;
    private long m;
    public boolean a = false;
    private long l = -1;
    private long h = -1;

    public d(Handler handler, String str) {
        this.i = handler;
        this.k = new a(str);
    }

    public d(Handler handler, String str, com.sysaac.haptic.player.a aVar) {
        this.i = handler;
        this.k = new a(str, aVar);
    }

    public void a() {
        synchronized (this) {
            if (this.a) {
                Log.d(f, "onStop");
            }
            this.h = -1L;
            this.j = null;
            this.i.removeMessages(100);
            this.i.removeMessages(101);
        }
        this.l = -1L;
    }

    public void a(long j) {
        if (this.a) {
            Log.d(f, "onSeek " + j);
        }
        synchronized (this) {
            b(j, j);
        }
        c();
    }

    public void a(long j, long j2) {
        if (this.a) {
            Log.d(f, "onTimedEvent " + j2);
        }
        synchronized (this) {
            b(j, j2);
        }
        c();
    }

    public void b() {
        synchronized (this) {
            if (this.a) {
                Log.d(f, "onPause");
            }
            this.i.removeMessages(100);
            this.i.removeMessages(101);
        }
        this.l = -1L;
    }

    public void b(long j) {
        this.h = j;
    }

    protected void b(long j, long j2) {
        try {
            b a = this.k.a(j2);
            if (this.a) {
                Log.d(f, "synchronize curPos:" + j + ",timeUs:" + j2 + " with " + a);
            }
            if (a == null || a.a.isEmpty()) {
                return;
            }
            Parcel obtain = Parcel.obtain();
            a.writeToParcel(obtain, 0);
            obtain.setDataPosition(0);
            Message obtainMessage = this.i.obtainMessage(101, 0, 0, obtain);
            long j3 = 0;
            if (j2 <= j) {
                this.i.sendMessage(obtainMessage);
                this.m = 0L;
                return;
            }
            long j4 = j2 - j;
            if (j4 > 20) {
                j3 = j4 - 20;
            }
            if (this.a) {
                Log.d(f, "synchronize vib scheduleTime:" + j3);
            }
            this.m = j3;
            this.i.sendMessageDelayed(obtainMessage, j3);
        } catch (Exception e2) {
            Log.e(f, e2.getMessage(), e2);
        }
    }

    protected void c() {
        try {
            this.l = this.k.a();
            if (this.a) {
                Log.d(f, "scheduleTimedEvents @" + this.l + " after " + this.h);
            }
            long j = this.l;
            if (j != -1) {
                long j2 = (j - this.h) - 20;
                Message obtainMessage = this.i.obtainMessage(100, 0, 0, Long.valueOf(j));
                if (this.a) {
                    Log.d(f, "scheduleTimedEvents scheduleTime:" + j2);
                }
                this.i.sendMessageDelayed(obtainMessage, j2);
                return;
            }
            if (this.a) {
                Log.d(f, "scheduleTimedEvents @ completed- tail pattern duration:" + this.k.b() + ",mLastScheduledTime:" + this.m);
            }
            this.i.sendEmptyMessageDelayed(102, this.m + this.k.b());
        } catch (Exception e2) {
            Log.w(f, "ex in scheduleTimedEvents");
        }
    }
}
