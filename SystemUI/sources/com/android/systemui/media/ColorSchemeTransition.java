package com.android.systemui.media;

import android.content.Context;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.monet.ColorScheme;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0010\u0011\n\u0002\b\u0010\n\u0002\u0010\u000b\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006BS\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012:\u0010\u0007\u001a6\u0012\u0004\u0012\u00020\t\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\t0\n\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\f0\n\u0012\u0004\u0012\u00020\r0\bj\u0002`\u000e¢\u0006\u0002\u0010\u000fJ\u0010\u0010)\u001a\u00020\t2\u0006\u0010*\u001a\u00020\tH\u0002J\u0010\u0010+\u001a\u00020,2\b\u0010-\u001a\u0004\u0018\u00010\u000bR\u0011\u0010\u0010\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0011\u0010\u0015\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0018\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0012R\u0019\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\r0\u001b¢\u0006\n\n\u0002\u0010\u001e\u001a\u0004\b\u001c\u0010\u001dR\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u001f\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b \u0010\u0012R\u0011\u0010!\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\"\u0010\u0012R\u0011\u0010#\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b$\u0010\u0012R\u0011\u0010%\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b&\u0010\u0012R\u0011\u0010'\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b(\u0010\u0012¨\u0006."}, mo65043d2 = {"Lcom/android/systemui/media/ColorSchemeTransition;", "", "context", "Landroid/content/Context;", "mediaViewHolder", "Lcom/android/systemui/media/MediaViewHolder;", "(Landroid/content/Context;Lcom/android/systemui/media/MediaViewHolder;)V", "animatingColorTransitionFactory", "Lkotlin/Function3;", "", "Lkotlin/Function1;", "Lcom/android/systemui/monet/ColorScheme;", "", "Lcom/android/systemui/media/AnimatingColorTransition;", "Lcom/android/systemui/media/AnimatingColorTransitionFactory;", "(Landroid/content/Context;Lcom/android/systemui/media/MediaViewHolder;Lkotlin/jvm/functions/Function3;)V", "accentPrimary", "getAccentPrimary", "()Lcom/android/systemui/media/AnimatingColorTransition;", "accentSecondary", "getAccentSecondary", "bgColor", "getBgColor", "()I", "colorSeamless", "getColorSeamless", "colorTransitions", "", "getColorTransitions", "()[Lcom/android/systemui/media/AnimatingColorTransition;", "[Lcom/android/systemui/media/AnimatingColorTransition;", "surfaceColor", "getSurfaceColor", "textPrimary", "getTextPrimary", "textPrimaryInverse", "getTextPrimaryInverse", "textSecondary", "getTextSecondary", "textTertiary", "getTextTertiary", "loadDefaultColor", "id", "updateColorScheme", "", "colorScheme", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ColorSchemeTransition.kt */
public final class ColorSchemeTransition {
    private final AnimatingColorTransition accentPrimary;
    private final AnimatingColorTransition accentSecondary;
    private final int bgColor;
    private final AnimatingColorTransition colorSeamless;
    private final AnimatingColorTransition[] colorTransitions;
    /* access modifiers changed from: private */
    public final Context context;
    /* access modifiers changed from: private */
    public final MediaViewHolder mediaViewHolder;
    private final AnimatingColorTransition surfaceColor;
    private final AnimatingColorTransition textPrimary;
    private final AnimatingColorTransition textPrimaryInverse;
    private final AnimatingColorTransition textSecondary;
    private final AnimatingColorTransition textTertiary;

    public ColorSchemeTransition(Context context2, MediaViewHolder mediaViewHolder2, Function3<? super Integer, ? super Function1<? super ColorScheme, Integer>, ? super Function1<? super Integer, Unit>, ? extends AnimatingColorTransition> function3) {
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(mediaViewHolder2, "mediaViewHolder");
        Intrinsics.checkNotNullParameter(function3, "animatingColorTransitionFactory");
        this.context = context2;
        this.mediaViewHolder = mediaViewHolder2;
        int color = context2.getColor(C1894R.C1895color.material_dynamic_secondary95);
        this.bgColor = color;
        AnimatingColorTransition animatingColorTransition = (AnimatingColorTransition) function3.invoke(Integer.valueOf(color), ColorSchemeTransition$surfaceColor$1.INSTANCE, new ColorSchemeTransition$surfaceColor$2(this));
        this.surfaceColor = animatingColorTransition;
        AnimatingColorTransition animatingColorTransition2 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16842806)), ColorSchemeTransition$accentPrimary$1.INSTANCE, new ColorSchemeTransition$accentPrimary$2(this));
        this.accentPrimary = animatingColorTransition2;
        AnimatingColorTransition animatingColorTransition3 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16842806)), ColorSchemeTransition$accentSecondary$1.INSTANCE, new ColorSchemeTransition$accentSecondary$2(this));
        this.accentSecondary = animatingColorTransition3;
        AnimatingColorTransition animatingColorTransition4 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16842806)), new ColorSchemeTransition$colorSeamless$1(this), new ColorSchemeTransition$colorSeamless$2(this));
        this.colorSeamless = animatingColorTransition4;
        AnimatingColorTransition animatingColorTransition5 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16842806)), ColorSchemeTransition$textPrimary$1.INSTANCE, new ColorSchemeTransition$textPrimary$2(this));
        this.textPrimary = animatingColorTransition5;
        AnimatingColorTransition animatingColorTransition6 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16842809)), ColorSchemeTransition$textPrimaryInverse$1.INSTANCE, new ColorSchemeTransition$textPrimaryInverse$2(this));
        this.textPrimaryInverse = animatingColorTransition6;
        AnimatingColorTransition animatingColorTransition7 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16842808)), ColorSchemeTransition$textSecondary$1.INSTANCE, new ColorSchemeTransition$textSecondary$2(this));
        this.textSecondary = animatingColorTransition7;
        AnimatingColorTransition animatingColorTransition8 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(loadDefaultColor(16843282)), ColorSchemeTransition$textTertiary$1.INSTANCE, new ColorSchemeTransition$textTertiary$2(this));
        this.textTertiary = animatingColorTransition8;
        this.colorTransitions = new AnimatingColorTransition[]{animatingColorTransition, animatingColorTransition4, animatingColorTransition2, animatingColorTransition3, animatingColorTransition5, animatingColorTransition6, animatingColorTransition7, animatingColorTransition8};
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public ColorSchemeTransition(Context context2, MediaViewHolder mediaViewHolder2) {
        this(context2, mediaViewHolder2, C21831.INSTANCE);
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(mediaViewHolder2, "mediaViewHolder");
    }

    public final int getBgColor() {
        return this.bgColor;
    }

    public final AnimatingColorTransition getSurfaceColor() {
        return this.surfaceColor;
    }

    public final AnimatingColorTransition getAccentPrimary() {
        return this.accentPrimary;
    }

    public final AnimatingColorTransition getAccentSecondary() {
        return this.accentSecondary;
    }

    public final AnimatingColorTransition getColorSeamless() {
        return this.colorSeamless;
    }

    public final AnimatingColorTransition getTextPrimary() {
        return this.textPrimary;
    }

    public final AnimatingColorTransition getTextPrimaryInverse() {
        return this.textPrimaryInverse;
    }

    public final AnimatingColorTransition getTextSecondary() {
        return this.textSecondary;
    }

    public final AnimatingColorTransition getTextTertiary() {
        return this.textTertiary;
    }

    public final AnimatingColorTransition[] getColorTransitions() {
        return this.colorTransitions;
    }

    private final int loadDefaultColor(int i) {
        return Utils.getColorAttr(this.context, i).getDefaultColor();
    }

    public final boolean updateColorScheme(ColorScheme colorScheme) {
        AnimatingColorTransition[] animatingColorTransitionArr = this.colorTransitions;
        int length = animatingColorTransitionArr.length;
        boolean z = false;
        for (int i = 0; i < length; i++) {
            z = animatingColorTransitionArr[i].updateColorScheme(colorScheme) || z;
        }
        if (colorScheme != null) {
            this.mediaViewHolder.getGutsViewHolder().setColorScheme(colorScheme);
        }
        return z;
    }
}
