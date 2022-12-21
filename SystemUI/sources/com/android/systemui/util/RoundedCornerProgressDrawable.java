package com.android.systemui.util;

import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.InsetDrawable;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 \u00132\u00020\u0001:\u0002\u0013\u0014B\u0013\b\u0007\u0012\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\bH\u0016J\b\u0010\t\u001a\u00020\nH\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0014J\u0010\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\bH\u0016J\u0010\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\bH\u0014¨\u0006\u0015"}, mo64987d2 = {"Lcom/android/systemui/util/RoundedCornerProgressDrawable;", "Landroid/graphics/drawable/InsetDrawable;", "drawable", "Landroid/graphics/drawable/Drawable;", "(Landroid/graphics/drawable/Drawable;)V", "canApplyTheme", "", "getChangingConfigurations", "", "getConstantState", "Landroid/graphics/drawable/Drawable$ConstantState;", "onBoundsChange", "", "bounds", "Landroid/graphics/Rect;", "onLayoutDirectionChanged", "layoutDirection", "onLevelChange", "level", "Companion", "RoundedCornerState", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RoundedCornerProgressDrawable.kt */
public final class RoundedCornerProgressDrawable extends InsetDrawable {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int MAX_LEVEL = 10000;

    public RoundedCornerProgressDrawable() {
        this((Drawable) null, 1, (DefaultConstructorMarker) null);
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ RoundedCornerProgressDrawable(Drawable drawable, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : drawable);
    }

    public RoundedCornerProgressDrawable(Drawable drawable) {
        super(drawable, 0);
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/util/RoundedCornerProgressDrawable$Companion;", "", "()V", "MAX_LEVEL", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: RoundedCornerProgressDrawable.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public boolean onLayoutDirectionChanged(int i) {
        onLevelChange(getLevel());
        return super.onLayoutDirectionChanged(i);
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect rect) {
        Intrinsics.checkNotNullParameter(rect, "bounds");
        super.onBoundsChange(rect);
        onLevelChange(getLevel());
    }

    /* access modifiers changed from: protected */
    public boolean onLevelChange(int i) {
        Drawable drawable = getDrawable();
        Rect bounds = drawable != null ? drawable.getBounds() : null;
        Intrinsics.checkNotNull(bounds);
        int height = getBounds().height() + (((getBounds().width() - getBounds().height()) * i) / 10000);
        Drawable drawable2 = getDrawable();
        if (drawable2 != null) {
            drawable2.setBounds(getBounds().left, bounds.top, getBounds().left + height, bounds.bottom);
        }
        return super.onLevelChange(i);
    }

    public Drawable.ConstantState getConstantState() {
        Drawable.ConstantState constantState = super.getConstantState();
        Intrinsics.checkNotNull(constantState);
        return new RoundedCornerState(constantState);
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | 4096;
    }

    public boolean canApplyTheme() {
        Drawable drawable = getDrawable();
        return (drawable != null ? drawable.canApplyTheme() : false) || super.canApplyTheme();
    }

    @Metadata(mo64986d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001¢\u0006\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0016J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\b\u0010\b\u001a\u00020\tH\u0016J \u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000b2\f\u0010\f\u001a\b\u0018\u00010\rR\u00020\u000bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0001X\u0004¢\u0006\u0002\n\u0000¨\u0006\u000e"}, mo64987d2 = {"Lcom/android/systemui/util/RoundedCornerProgressDrawable$RoundedCornerState;", "Landroid/graphics/drawable/Drawable$ConstantState;", "wrappedState", "(Landroid/graphics/drawable/Drawable$ConstantState;)V", "canApplyTheme", "", "getChangingConfigurations", "", "newDrawable", "Landroid/graphics/drawable/Drawable;", "res", "Landroid/content/res/Resources;", "theme", "Landroid/content/res/Resources$Theme;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: RoundedCornerProgressDrawable.kt */
    private static final class RoundedCornerState extends Drawable.ConstantState {
        private final Drawable.ConstantState wrappedState;

        public boolean canApplyTheme() {
            return true;
        }

        public RoundedCornerState(Drawable.ConstantState constantState) {
            Intrinsics.checkNotNullParameter(constantState, "wrappedState");
            this.wrappedState = constantState;
        }

        public Drawable newDrawable() {
            return newDrawable((Resources) null, (Resources.Theme) null);
        }

        public Drawable newDrawable(Resources resources, Resources.Theme theme) {
            Drawable newDrawable = this.wrappedState.newDrawable(resources, theme);
            if (newDrawable != null) {
                return new RoundedCornerProgressDrawable(((DrawableWrapper) newDrawable).getDrawable());
            }
            throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.DrawableWrapper");
        }

        public int getChangingConfigurations() {
            return this.wrappedState.getChangingConfigurations();
        }
    }
}
