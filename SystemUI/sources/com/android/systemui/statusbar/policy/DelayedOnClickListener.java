package com.android.systemui.statusbar.policy;

import android.icu.text.DateFormat;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\b\u0010\u0007\u001a\u00020\bH\u0002J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0016R\u000e\u0010\u0002\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/statusbar/policy/DelayedOnClickListener;", "Landroid/view/View$OnClickListener;", "mActualListener", "mInitDelayMs", "", "(Landroid/view/View$OnClickListener;J)V", "mInitTimeMs", "hasFinishedInitialization", "", "onClick", "", "v", "Landroid/view/View;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SmartReplyStateInflater.kt */
final class DelayedOnClickListener implements View.OnClickListener {
    private final View.OnClickListener mActualListener;
    private final long mInitDelayMs;
    private final long mInitTimeMs = SystemClock.elapsedRealtime();

    public DelayedOnClickListener(View.OnClickListener onClickListener, long j) {
        Intrinsics.checkNotNullParameter(onClickListener, "mActualListener");
        this.mActualListener = onClickListener;
        this.mInitDelayMs = j;
    }

    public void onClick(View view) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        if (hasFinishedInitialization()) {
            this.mActualListener.onClick(view);
        } else {
            Log.i("SmartReplyViewInflater", "Accidental Smart Suggestion click registered, delay: " + this.mInitDelayMs);
        }
    }

    private final boolean hasFinishedInitialization() {
        return SystemClock.elapsedRealtime() >= this.mInitTimeMs + this.mInitDelayMs;
    }
}
