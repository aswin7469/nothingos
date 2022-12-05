package com.android.systemui.statusbar.policy;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes2.dex */
final class DelayedOnClickListener implements View.OnClickListener {
    @NotNull
    private final View.OnClickListener mActualListener;
    private final long mInitDelayMs;
    private final long mInitTimeMs = SystemClock.elapsedRealtime();

    public DelayedOnClickListener(@NotNull View.OnClickListener mActualListener, long j) {
        Intrinsics.checkNotNullParameter(mActualListener, "mActualListener");
        this.mActualListener = mActualListener;
        this.mInitDelayMs = j;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(@NotNull View v) {
        Intrinsics.checkNotNullParameter(v, "v");
        if (hasFinishedInitialization()) {
            this.mActualListener.onClick(v);
        } else {
            Log.i("SmartReplyViewInflater", Intrinsics.stringPlus("Accidental Smart Suggestion click registered, delay: ", Long.valueOf(this.mInitDelayMs)));
        }
    }

    private final boolean hasFinishedInitialization() {
        return SystemClock.elapsedRealtime() >= this.mInitTimeMs + this.mInitDelayMs;
    }
}
