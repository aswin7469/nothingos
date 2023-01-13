package com.android.systemui.media;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.widget.SeekBar;
import androidx.lifecycle.Observer;
import com.android.systemui.C1894R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.media.SeekBarViewModel;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0016\u0018\u0000 \u001d2\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u001dB\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0010\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u0007H\u0017J\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u0002H\u0017J\u0010\u0010\u001b\u001a\u00020\u00192\u0006\u0010\u001c\u001a\u00020\u0007H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0011\u0010\f\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\tR\u0011\u0010\u000e\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\tR\u001c\u0010\u0010\u001a\u0004\u0018\u00010\u0011X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015¨\u0006\u001e"}, mo65043d2 = {"Lcom/android/systemui/media/SeekBarObserver;", "Landroidx/lifecycle/Observer;", "Lcom/android/systemui/media/SeekBarViewModel$Progress;", "holder", "Lcom/android/systemui/media/MediaViewHolder;", "(Lcom/android/systemui/media/MediaViewHolder;)V", "seekBarDisabledHeight", "", "getSeekBarDisabledHeight", "()I", "seekBarDisabledVerticalPadding", "getSeekBarDisabledVerticalPadding", "seekBarEnabledMaxHeight", "getSeekBarEnabledMaxHeight", "seekBarEnabledVerticalPadding", "getSeekBarEnabledVerticalPadding", "seekBarResetAnimator", "Landroid/animation/Animator;", "getSeekBarResetAnimator", "()Landroid/animation/Animator;", "setSeekBarResetAnimator", "(Landroid/animation/Animator;)V", "buildResetAnimator", "targetTime", "onChanged", "", "data", "setVerticalPadding", "padding", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: SeekBarObserver.kt */
public class SeekBarObserver implements Observer<SeekBarViewModel.Progress> {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final int RESET_ANIMATION_DURATION_MS = 750;
    /* access modifiers changed from: private */
    public static final int RESET_ANIMATION_THRESHOLD_MS = 250;
    private final MediaViewHolder holder;
    private final int seekBarDisabledHeight;
    private final int seekBarDisabledVerticalPadding;
    private final int seekBarEnabledMaxHeight;
    private final int seekBarEnabledVerticalPadding;
    private Animator seekBarResetAnimator;

    public static final int getRESET_ANIMATION_DURATION_MS() {
        return Companion.getRESET_ANIMATION_DURATION_MS();
    }

    public static final int getRESET_ANIMATION_THRESHOLD_MS() {
        return Companion.getRESET_ANIMATION_THRESHOLD_MS();
    }

    public SeekBarObserver(MediaViewHolder mediaViewHolder) {
        Intrinsics.checkNotNullParameter(mediaViewHolder, "holder");
        this.holder = mediaViewHolder;
        this.seekBarEnabledMaxHeight = mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(C1894R.dimen.qs_media_enabled_seekbar_height);
        this.seekBarDisabledHeight = mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(C1894R.dimen.qs_media_disabled_seekbar_height);
        this.seekBarEnabledVerticalPadding = mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(C1894R.dimen.qs_media_session_enabled_seekbar_vertical_padding);
        this.seekBarDisabledVerticalPadding = mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(C1894R.dimen.qs_media_session_disabled_seekbar_vertical_padding);
        float dimensionPixelSize = (float) mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(C1894R.dimen.qs_media_seekbar_progress_wavelength);
        float dimensionPixelSize2 = (float) mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(C1894R.dimen.qs_media_seekbar_progress_amplitude);
        float dimensionPixelSize3 = (float) mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(C1894R.dimen.qs_media_seekbar_progress_phase);
        float dimensionPixelSize4 = (float) mediaViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(C1894R.dimen.qs_media_seekbar_progress_stroke_width);
        Drawable progressDrawable = mediaViewHolder.getSeekBar().getProgressDrawable();
        SquigglyProgress squigglyProgress = progressDrawable instanceof SquigglyProgress ? (SquigglyProgress) progressDrawable : null;
        if (squigglyProgress != null) {
            squigglyProgress.setWaveLength(dimensionPixelSize);
            squigglyProgress.setLineAmplitude(dimensionPixelSize2);
            squigglyProgress.setPhaseSpeed(dimensionPixelSize3);
            squigglyProgress.setStrokeWidth(dimensionPixelSize4);
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0007\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u001c\u0010\u0003\u001a\u00020\u00048\u0006XD¢\u0006\u000e\n\u0000\u0012\u0004\b\u0005\u0010\u0002\u001a\u0004\b\u0006\u0010\u0007R\u001c\u0010\b\u001a\u00020\u00048\u0006XD¢\u0006\u000e\n\u0000\u0012\u0004\b\t\u0010\u0002\u001a\u0004\b\n\u0010\u0007¨\u0006\u000b"}, mo65043d2 = {"Lcom/android/systemui/media/SeekBarObserver$Companion;", "", "()V", "RESET_ANIMATION_DURATION_MS", "", "getRESET_ANIMATION_DURATION_MS$annotations", "getRESET_ANIMATION_DURATION_MS", "()I", "RESET_ANIMATION_THRESHOLD_MS", "getRESET_ANIMATION_THRESHOLD_MS$annotations", "getRESET_ANIMATION_THRESHOLD_MS", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: SeekBarObserver.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @JvmStatic
        public static /* synthetic */ void getRESET_ANIMATION_DURATION_MS$annotations() {
        }

        @JvmStatic
        public static /* synthetic */ void getRESET_ANIMATION_THRESHOLD_MS$annotations() {
        }

        private Companion() {
        }

        public final int getRESET_ANIMATION_DURATION_MS() {
            return SeekBarObserver.RESET_ANIMATION_DURATION_MS;
        }

        public final int getRESET_ANIMATION_THRESHOLD_MS() {
            return SeekBarObserver.RESET_ANIMATION_THRESHOLD_MS;
        }
    }

    public final int getSeekBarEnabledMaxHeight() {
        return this.seekBarEnabledMaxHeight;
    }

    public final int getSeekBarDisabledHeight() {
        return this.seekBarDisabledHeight;
    }

    public final int getSeekBarEnabledVerticalPadding() {
        return this.seekBarEnabledVerticalPadding;
    }

    public final int getSeekBarDisabledVerticalPadding() {
        return this.seekBarDisabledVerticalPadding;
    }

    public final Animator getSeekBarResetAnimator() {
        return this.seekBarResetAnimator;
    }

    public final void setSeekBarResetAnimator(Animator animator) {
        this.seekBarResetAnimator = animator;
    }

    public void onChanged(SeekBarViewModel.Progress progress) {
        Intrinsics.checkNotNullParameter(progress, "data");
        Drawable progressDrawable = this.holder.getSeekBar().getProgressDrawable();
        SquigglyProgress squigglyProgress = progressDrawable instanceof SquigglyProgress ? (SquigglyProgress) progressDrawable : null;
        if (!progress.getEnabled()) {
            if (this.holder.getSeekBar().getMaxHeight() != this.seekBarDisabledHeight) {
                this.holder.getSeekBar().setMaxHeight(this.seekBarDisabledHeight);
                setVerticalPadding(this.seekBarDisabledVerticalPadding);
            }
            this.holder.getSeekBar().setEnabled(false);
            if (squigglyProgress != null) {
                squigglyProgress.setAnimate(false);
            }
            this.holder.getSeekBar().getThumb().setAlpha(0);
            this.holder.getSeekBar().setProgress(0);
            this.holder.getSeekBar().setContentDescription("");
            this.holder.getScrubbingElapsedTimeView().setText("");
            this.holder.getScrubbingTotalTimeView().setText("");
            return;
        }
        this.holder.getSeekBar().getThumb().setAlpha(progress.getSeekAvailable() ? 255 : 0);
        this.holder.getSeekBar().setEnabled(progress.getSeekAvailable());
        if (squigglyProgress != null) {
            squigglyProgress.setAnimate(progress.getPlaying() && !progress.getScrubbing());
        }
        if (squigglyProgress != null) {
            squigglyProgress.setTransitionEnabled(!progress.getSeekAvailable());
        }
        if (this.holder.getSeekBar().getMaxHeight() != this.seekBarEnabledMaxHeight) {
            this.holder.getSeekBar().setMaxHeight(this.seekBarEnabledMaxHeight);
            setVerticalPadding(this.seekBarEnabledVerticalPadding);
        }
        this.holder.getSeekBar().setMax(progress.getDuration());
        String formatElapsedTime = DateUtils.formatElapsedTime(((long) progress.getDuration()) / 1000);
        if (progress.getScrubbing()) {
            this.holder.getScrubbingTotalTimeView().setText(formatElapsedTime);
        }
        Integer elapsedTime = progress.getElapsedTime();
        if (elapsedTime != null) {
            int intValue = elapsedTime.intValue();
            if (!progress.getScrubbing()) {
                Animator animator = this.seekBarResetAnimator;
                if (!(animator != null ? animator.isRunning() : false)) {
                    int i = RESET_ANIMATION_THRESHOLD_MS;
                    if (intValue > i || this.holder.getSeekBar().getProgress() <= i) {
                        this.holder.getSeekBar().setProgress(intValue);
                    } else {
                        Animator buildResetAnimator = buildResetAnimator(intValue);
                        buildResetAnimator.start();
                        this.seekBarResetAnimator = buildResetAnimator;
                    }
                }
            }
            String formatElapsedTime2 = DateUtils.formatElapsedTime(((long) intValue) / 1000);
            if (progress.getScrubbing()) {
                this.holder.getScrubbingElapsedTimeView().setText(formatElapsedTime2);
            }
            this.holder.getSeekBar().setContentDescription(this.holder.getSeekBar().getContext().getString(C1894R.string.controls_media_seekbar_description, new Object[]{formatElapsedTime2, formatElapsedTime}));
        }
    }

    public Animator buildResetAnimator(int i) {
        SeekBar seekBar = this.holder.getSeekBar();
        int i2 = RESET_ANIMATION_DURATION_MS;
        ObjectAnimator ofInt = ObjectAnimator.ofInt(seekBar, "progress", new int[]{this.holder.getSeekBar().getProgress(), i + i2});
        ofInt.setAutoCancel(true);
        ofInt.setDuration((long) i2);
        ofInt.setInterpolator(Interpolators.EMPHASIZED);
        Intrinsics.checkNotNullExpressionValue(ofInt, "animator");
        return ofInt;
    }

    public final void setVerticalPadding(int i) {
        this.holder.getSeekBar().setPadding(this.holder.getSeekBar().getPaddingLeft(), i, this.holder.getSeekBar().getPaddingRight(), this.holder.getSeekBar().getPaddingBottom());
    }
}
