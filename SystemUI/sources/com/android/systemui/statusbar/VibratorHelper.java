package com.android.systemui.statusbar;

import android.media.AudioAttributes;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import java.util.Objects;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public class VibratorHelper {
    private static final VibrationAttributes TOUCH_VIBRATION_ATTRIBUTES = VibrationAttributes.createForUsage(18);
    private final Executor mExecutor;
    private final Vibrator mVibrator;

    @Inject
    public VibratorHelper(Vibrator vibrator, @Background Executor executor) {
        this.mExecutor = executor;
        this.mVibrator = vibrator;
    }

    public void vibrate(int i) {
        if (hasVibrator()) {
            this.mExecutor.execute(new VibratorHelper$$ExternalSyntheticLambda1(this, i));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$vibrate$0$com-android-systemui-statusbar-VibratorHelper  reason: not valid java name */
    public /* synthetic */ void m3047lambda$vibrate$0$comandroidsystemuistatusbarVibratorHelper(int i) {
        this.mVibrator.vibrate(VibrationEffect.get(i, false), TOUCH_VIBRATION_ATTRIBUTES);
    }

    public void vibrate(int i, String str, VibrationEffect vibrationEffect, String str2, VibrationAttributes vibrationAttributes) {
        if (hasVibrator()) {
            this.mExecutor.execute(new VibratorHelper$$ExternalSyntheticLambda4(this, i, str, vibrationEffect, str2, vibrationAttributes));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$vibrate$1$com-android-systemui-statusbar-VibratorHelper  reason: not valid java name */
    public /* synthetic */ void m3048lambda$vibrate$1$comandroidsystemuistatusbarVibratorHelper(int i, String str, VibrationEffect vibrationEffect, String str2, VibrationAttributes vibrationAttributes) {
        this.mVibrator.vibrate(i, str, vibrationEffect, str2, vibrationAttributes);
    }

    public void vibrate(VibrationEffect vibrationEffect, AudioAttributes audioAttributes) {
        if (hasVibrator()) {
            this.mExecutor.execute(new VibratorHelper$$ExternalSyntheticLambda0(this, vibrationEffect, audioAttributes));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$vibrate$2$com-android-systemui-statusbar-VibratorHelper  reason: not valid java name */
    public /* synthetic */ void m3049lambda$vibrate$2$comandroidsystemuistatusbarVibratorHelper(VibrationEffect vibrationEffect, AudioAttributes audioAttributes) {
        this.mVibrator.vibrate(vibrationEffect, audioAttributes);
    }

    public void vibrate(VibrationEffect vibrationEffect) {
        if (hasVibrator()) {
            this.mExecutor.execute(new VibratorHelper$$ExternalSyntheticLambda3(this, vibrationEffect));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$vibrate$3$com-android-systemui-statusbar-VibratorHelper  reason: not valid java name */
    public /* synthetic */ void m3050lambda$vibrate$3$comandroidsystemuistatusbarVibratorHelper(VibrationEffect vibrationEffect) {
        this.mVibrator.vibrate(vibrationEffect);
    }

    public boolean hasVibrator() {
        Vibrator vibrator = this.mVibrator;
        return vibrator != null && vibrator.hasVibrator();
    }

    public void cancel() {
        if (hasVibrator()) {
            Executor executor = this.mExecutor;
            Vibrator vibrator = this.mVibrator;
            Objects.requireNonNull(vibrator);
            executor.execute(new VibratorHelper$$ExternalSyntheticLambda2(vibrator));
        }
    }
}
