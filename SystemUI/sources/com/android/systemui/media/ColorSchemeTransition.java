package com.android.systemui.media;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import com.android.settingslib.Utils;
import com.android.systemui.C1893R;
import com.android.systemui.monet.ColorScheme;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\r\n\u0002\u0010\u0007\n\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0016\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006BS\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012:\u0010\u0007\u001a6\u0012\u0004\u0012\u00020\t\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\t0\n\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\t\u0012\u0004\u0012\u00020\f0\n\u0012\u0004\u0012\u00020\r0\bj\u0002`\u000e¢\u0006\u0002\u0010\u000fJ0\u0010/\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\t0\n2\u0012\u00100\u001a\u000e\u0012\u0004\u0012\u00020\u000b\u0012\u0004\u0012\u00020\t0\n2\u0006\u00101\u001a\u000202H\u0002J\u0010\u00103\u001a\u00020\t2\u0006\u00104\u001a\u00020\tH\u0002J\b\u00105\u001a\u00020\fH\u0002J\u0018\u00106\u001a\u00020\f2\b\u00107\u001a\u0004\u0018\u00010\u000b2\u0006\u00108\u001a\u00020$R\u0011\u0010\u0010\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0013\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0011\u0010\u0015\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\u0018\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0012R\u0011\u0010\u001a\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0012R\u0011\u0010\u001c\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0012R\u0019\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020\r0\u001f¢\u0006\n\n\u0002\u0010\"\u001a\u0004\b \u0010!R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0011\u0010%\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b&\u0010\u0012R\u0011\u0010'\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b(\u0010\u0012R\u0011\u0010)\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b*\u0010\u0012R\u0011\u0010+\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b,\u0010\u0012R\u0011\u0010-\u001a\u00020\r¢\u0006\b\n\u0000\u001a\u0004\b.\u0010\u0012¨\u00069"}, mo64987d2 = {"Lcom/android/systemui/media/ColorSchemeTransition;", "", "context", "Landroid/content/Context;", "mediaViewHolder", "Lcom/android/systemui/media/MediaViewHolder;", "(Landroid/content/Context;Lcom/android/systemui/media/MediaViewHolder;)V", "animatingColorTransitionFactory", "Lkotlin/Function3;", "", "Lkotlin/Function1;", "Lcom/android/systemui/monet/ColorScheme;", "", "Lcom/android/systemui/media/AnimatingColorTransition;", "Lcom/android/systemui/media/AnimatingColorTransitionFactory;", "(Landroid/content/Context;Lcom/android/systemui/media/MediaViewHolder;Lkotlin/jvm/functions/Function3;)V", "accentPrimary", "getAccentPrimary", "()Lcom/android/systemui/media/AnimatingColorTransition;", "accentSecondary", "getAccentSecondary", "bgColor", "getBgColor", "()I", "bgGradientEnd", "getBgGradientEnd", "bgGradientStart", "getBgGradientStart", "colorSeamless", "getColorSeamless", "colorTransitions", "", "getColorTransitions", "()[Lcom/android/systemui/media/AnimatingColorTransition;", "[Lcom/android/systemui/media/AnimatingColorTransition;", "isGradientEnabled", "", "surfaceColor", "getSurfaceColor", "textPrimary", "getTextPrimary", "textPrimaryInverse", "getTextPrimaryInverse", "textSecondary", "getTextSecondary", "textTertiary", "getTextTertiary", "albumGradientPicker", "inner", "targetAlpha", "", "loadDefaultColor", "id", "updateAlbumGradient", "updateColorScheme", "colorScheme", "enableGradient", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ColorSchemeTransition.kt */
public final class ColorSchemeTransition {
    private final AnimatingColorTransition accentPrimary;
    private final AnimatingColorTransition accentSecondary;
    private final int bgColor;
    private final AnimatingColorTransition bgGradientEnd;
    private final AnimatingColorTransition bgGradientStart;
    private final AnimatingColorTransition colorSeamless;
    private final AnimatingColorTransition[] colorTransitions;
    /* access modifiers changed from: private */
    public final Context context;
    /* access modifiers changed from: private */
    public boolean isGradientEnabled;
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
        this.isGradientEnabled = true;
        int color = context2.getColor(C1893R.C1894color.material_dynamic_secondary95);
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
        AnimatingColorTransition animatingColorTransition9 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(color), albumGradientPicker(ColorSchemeTransition$bgGradientStart$1.INSTANCE, 0.25f), new ColorSchemeTransition$bgGradientStart$2(this));
        this.bgGradientStart = animatingColorTransition9;
        AnimatingColorTransition animatingColorTransition10 = (AnimatingColorTransition) function3.invoke(Integer.valueOf(color), albumGradientPicker(ColorSchemeTransition$bgGradientEnd$1.INSTANCE, 0.9f), new ColorSchemeTransition$bgGradientEnd$2(this));
        this.bgGradientEnd = animatingColorTransition10;
        this.colorTransitions = new AnimatingColorTransition[]{animatingColorTransition, animatingColorTransition4, animatingColorTransition2, animatingColorTransition3, animatingColorTransition5, animatingColorTransition6, animatingColorTransition7, animatingColorTransition8, animatingColorTransition9, animatingColorTransition10};
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public ColorSchemeTransition(Context context2, MediaViewHolder mediaViewHolder2) {
        this(context2, mediaViewHolder2, C21801.INSTANCE);
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

    public final AnimatingColorTransition getBgGradientStart() {
        return this.bgGradientStart;
    }

    public final AnimatingColorTransition getBgGradientEnd() {
        return this.bgGradientEnd;
    }

    public final AnimatingColorTransition[] getColorTransitions() {
        return this.colorTransitions;
    }

    /* access modifiers changed from: private */
    public final void updateAlbumGradient() {
        Drawable foreground = this.mediaViewHolder.getAlbumView().getForeground();
        Drawable mutate = foreground != null ? foreground.mutate() : null;
        if (mutate instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) mutate;
            int[] iArr = new int[2];
            AnimatingColorTransition animatingColorTransition = this.bgGradientStart;
            int i = 0;
            iArr[0] = animatingColorTransition != null ? animatingColorTransition.getCurrentColor() : 0;
            AnimatingColorTransition animatingColorTransition2 = this.bgGradientEnd;
            if (animatingColorTransition2 != null) {
                i = animatingColorTransition2.getCurrentColor();
            }
            iArr[1] = i;
            gradientDrawable.setColors(iArr);
        }
    }

    private final Function1<ColorScheme, Integer> albumGradientPicker(Function1<? super ColorScheme, Integer> function1, float f) {
        return new ColorSchemeTransition$albumGradientPicker$1(this, function1, f);
    }

    private final int loadDefaultColor(int i) {
        return Utils.getColorAttr(this.context, i).getDefaultColor();
    }

    public final void updateColorScheme(ColorScheme colorScheme, boolean z) {
        this.isGradientEnabled = z;
        for (AnimatingColorTransition updateColorScheme : this.colorTransitions) {
            updateColorScheme.updateColorScheme(colorScheme);
        }
        if (colorScheme != null) {
            this.mediaViewHolder.getGutsViewHolder().setColorScheme(colorScheme);
        }
    }
}
