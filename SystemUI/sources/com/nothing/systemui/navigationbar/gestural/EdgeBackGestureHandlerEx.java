package com.nothing.systemui.navigationbar.gestural;

import android.content.Context;
import android.os.SystemClock;
import android.widget.Toast;
import com.android.systemui.C1893R;
import com.nothing.gamemode.NTGameModeHelper;
import com.nothing.systemui.NTDependencyEx;
import java.util.concurrent.Executor;

public class EdgeBackGestureHandlerEx {
    private static final long MISTOUCH_PREVENTION_RESET_DURATION = 2000;
    private static final String TAG = "EdgeBackGetureHandlerEx";
    private long mBackInterceptTime;
    private NTGameModeHelper mGameModeHelper = ((NTGameModeHelper) NTDependencyEx.get(NTGameModeHelper.class));
    private boolean mNeedInterceptBack;
    private Toast mToast;

    public void resetBackIntercept() {
        this.mNeedInterceptBack = false;
    }

    public boolean getIfNeedInterceptBack() {
        return this.mNeedInterceptBack;
    }

    public void setBackInterceptTime() {
        this.mBackInterceptTime = SystemClock.uptimeMillis();
    }

    public long getBackIntercepTime() {
        return this.mBackInterceptTime;
    }

    public void shouldInterceptBack() {
        this.mNeedInterceptBack = this.mGameModeHelper.isMistouchPreventEnabled() && SystemClock.uptimeMillis() - this.mBackInterceptTime >= 2000;
    }

    public void showToast(Executor executor, Context context) {
        executor.execute(new EdgeBackGestureHandlerEx$$ExternalSyntheticLambda0(this, context));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$showToast$0$com-nothing-systemui-navigationbar-gestural-EdgeBackGestureHandlerEx */
    public /* synthetic */ void mo57507x19113d3d(Context context) {
        Toast makeText = Toast.makeText(context, C1893R.string.swipe_again, 0);
        this.mToast = makeText;
        makeText.show();
    }

    public void hideToast(Executor executor) {
        executor.execute(new EdgeBackGestureHandlerEx$$ExternalSyntheticLambda1(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$hideToast$1$com-nothing-systemui-navigationbar-gestural-EdgeBackGestureHandlerEx */
    public /* synthetic */ void mo57506xae11b8a1() {
        Toast toast = this.mToast;
        if (toast != null) {
            toast.cancel();
        }
    }
}
