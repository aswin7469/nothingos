package com.android.systemui.media;

import android.content.res.ColorStateList;
import android.widget.ImageButton;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;

@Metadata(mo65042d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo65043d2 = {"<anonymous>", "", "textPrimary", "", "invoke"}, mo65044k = 3, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ColorSchemeTransition.kt */
final class ColorSchemeTransition$textPrimary$2 extends Lambda implements Function1<Integer, Unit> {
    final /* synthetic */ ColorSchemeTransition this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ColorSchemeTransition$textPrimary$2(ColorSchemeTransition colorSchemeTransition) {
        super(1);
        this.this$0 = colorSchemeTransition;
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        invoke(((Number) obj).intValue());
        return Unit.INSTANCE;
    }

    public final void invoke(int i) {
        this.this$0.mediaViewHolder.getTitleText().setTextColor(i);
        ColorStateList valueOf = ColorStateList.valueOf(i);
        this.this$0.mediaViewHolder.getSeekBar().getThumb().setTintList(valueOf);
        this.this$0.mediaViewHolder.getSeekBar().setProgressTintList(valueOf);
        this.this$0.mediaViewHolder.getScrubbingElapsedTimeView().setTextColor(valueOf);
        this.this$0.mediaViewHolder.getScrubbingTotalTimeView().setTextColor(valueOf);
        for (ImageButton imageTintList : this.this$0.mediaViewHolder.getTransparentActionButtons()) {
            imageTintList.setImageTintList(valueOf);
        }
        this.this$0.mediaViewHolder.getGutsViewHolder().setTextPrimaryColor(i);
    }
}
