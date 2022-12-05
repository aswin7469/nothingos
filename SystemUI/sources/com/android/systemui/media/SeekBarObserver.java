package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import androidx.lifecycle.Observer;
import com.android.systemui.R$dimen;
import com.android.systemui.media.SeekBarViewModel;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: SeekBarObserver.kt */
/* loaded from: classes.dex */
public final class SeekBarObserver implements Observer<SeekBarViewModel.Progress> {
    @NotNull
    private final PlayerViewHolder holder;
    private final int seekBarDisabledHeight;
    private final int seekBarDisabledVerticalPadding;
    private final int seekBarEnabledMaxHeight;
    private final int seekBarEnabledVerticalPadding;

    public SeekBarObserver(@NotNull PlayerViewHolder holder) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        this.holder = holder;
        this.seekBarEnabledMaxHeight = holder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_enabled_seekbar_height);
        this.seekBarDisabledHeight = holder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_disabled_seekbar_height);
        this.seekBarEnabledVerticalPadding = holder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_enabled_seekbar_vertical_padding);
        this.seekBarDisabledVerticalPadding = holder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_disabled_seekbar_vertical_padding);
    }

    @Override // androidx.lifecycle.Observer
    public void onChanged(@NotNull SeekBarViewModel.Progress data) {
        Intrinsics.checkNotNullParameter(data, "data");
        int i = 0;
        if (!data.getEnabled()) {
            if (this.holder.getSeekBar().getMaxHeight() != this.seekBarDisabledHeight) {
                this.holder.getSeekBar().setMaxHeight(this.seekBarDisabledHeight);
                setVerticalPadding(this.seekBarDisabledVerticalPadding);
            }
            this.holder.getSeekBar().setEnabled(false);
            this.holder.getSeekBar().getThumb().setAlpha(0);
            this.holder.getSeekBar().setProgress(0);
            this.holder.getElapsedTimeView().setText("");
            this.holder.getTotalTimeView().setText("");
            return;
        }
        Drawable thumb = this.holder.getSeekBar().getThumb();
        if (data.getSeekAvailable()) {
            i = 255;
        }
        thumb.setAlpha(i);
        this.holder.getSeekBar().setEnabled(data.getSeekAvailable());
        if (this.holder.getSeekBar().getMaxHeight() != this.seekBarEnabledMaxHeight) {
            this.holder.getSeekBar().setMaxHeight(this.seekBarEnabledMaxHeight);
            setVerticalPadding(this.seekBarEnabledVerticalPadding);
        }
        int duration = data.getDuration();
        this.holder.getSeekBar().setMax(duration);
        this.holder.getTotalTimeView().setText(DateUtils.formatElapsedTime(duration / 1000));
        Integer elapsedTime = data.getElapsedTime();
        if (elapsedTime == null) {
            return;
        }
        int intValue = elapsedTime.intValue();
        this.holder.getSeekBar().setProgress(intValue);
        this.holder.getElapsedTimeView().setText(DateUtils.formatElapsedTime(intValue / 1000));
    }

    public final void setVerticalPadding(int i) {
        this.holder.getSeekBar().setPadding(this.holder.getSeekBar().getPaddingLeft(), i, this.holder.getSeekBar().getPaddingRight(), this.holder.getSeekBar().getPaddingBottom());
    }
}
