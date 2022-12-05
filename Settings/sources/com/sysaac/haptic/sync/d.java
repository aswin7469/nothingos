package com.sysaac.haptic.sync;

import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.util.Log;
import androidx.constraintlayout.widget.R$styleable;
/* loaded from: classes2.dex */
public class d {
    private Handler i;
    private a k;
    private long m;
    public boolean a = false;
    private long l = -1;
    private long h = -1;

    public d(Handler handler, String str, com.sysaac.haptic.player.a aVar) {
        this.i = handler;
        this.k = new a(str, aVar);
    }

    public void a(long j) {
        if (this.a) {
            Log.d("VibrationTrack", "onSeek " + j);
        }
        synchronized (this) {
            b(j, j);
        }
        c();
    }

    public void a(long j, long j2) {
        if (this.a) {
            Log.d("VibrationTrack", "onTimedEvent " + j2);
        }
        synchronized (this) {
            b(j, j2);
        }
        c();
    }

    public void b(long j) {
        this.h = j;
    }

    protected void b(long j, long j2) {
        try {
            b a = this.k.a(j2);
            if (this.a) {
                Log.d("VibrationTrack", "synchronize curPos:" + j + ",timeUs:" + j2 + " with " + a);
            }
            if (a == null || a.a.isEmpty()) {
                return;
            }
            Parcel obtain = Parcel.obtain();
            a.writeToParcel(obtain, 0);
            obtain.setDataPosition(0);
            Message obtainMessage = this.i.obtainMessage(R$styleable.Constraint_layout_goneMarginRight, 0, 0, obtain);
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
                Log.d("VibrationTrack", "synchronize vib scheduleTime:" + j3);
            }
            this.m = j3;
            this.i.sendMessageDelayed(obtainMessage, j3);
        } catch (Exception e) {
            Log.e("VibrationTrack", e.getMessage(), e);
        }
    }

    protected void c() {
        try {
            this.l = this.k.a();
            if (this.a) {
                Log.d("VibrationTrack", "scheduleTimedEvents @" + this.l + " after " + this.h);
            }
            long j = this.l;
            if (j != -1) {
                long j2 = (j - this.h) - 20;
                Message obtainMessage = this.i.obtainMessage(100, 0, 0, Long.valueOf(j));
                if (this.a) {
                    Log.d("VibrationTrack", "scheduleTimedEvents scheduleTime:" + j2);
                }
                this.i.sendMessageDelayed(obtainMessage, j2);
                return;
            }
            if (this.a) {
                Log.d("VibrationTrack", "scheduleTimedEvents @ completed- tail pattern duration:" + this.k.b() + ",mLastScheduledTime:" + this.m);
            }
            this.i.sendEmptyMessageDelayed(R$styleable.Constraint_layout_goneMarginStart, this.m + this.k.b());
        } catch (Exception unused) {
            Log.w("VibrationTrack", "ex in scheduleTimedEvents");
        }
    }
}
