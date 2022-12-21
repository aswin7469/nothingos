package com.nothing.systemui.p024qs.tileimpl;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.C1893R;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0016J0\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\b2\u0006\u0010\u0012\u001a\u00020\bH\u0016¨\u0006\u0014"}, mo64987d2 = {"Lcom/nothing/systemui/qs/tileimpl/QSIconViewImplEx;", "", "()V", "getCircleIconDrawable", "Landroid/graphics/drawable/Drawable;", "context", "Landroid/content/Context;", "getCircleIconSize", "", "measureCircleIcon", "", "icon", "Landroid/view/View;", "circleIconBg", "Landroid/widget/ImageView;", "circleIconFrame", "Landroid/widget/FrameLayout;", "iconSpec", "circleIconSpec", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.nothing.systemui.qs.tileimpl.QSIconViewImplEx */
/* compiled from: QSIconViewImplEx.kt */
public final class QSIconViewImplEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "QSIconViewImplEx";

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/nothing/systemui/qs/tileimpl/QSIconViewImplEx$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* renamed from: com.nothing.systemui.qs.tileimpl.QSIconViewImplEx$Companion */
    /* compiled from: QSIconViewImplEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public Drawable getCircleIconDrawable(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Drawable drawable = context.getResources().getDrawable(C1893R.C1895drawable.circle_tile_icon_bg);
        Intrinsics.checkNotNullExpressionValue(drawable, "context.getResources().g…able.circle_tile_icon_bg)");
        return drawable;
    }

    public int getCircleIconSize(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return context.getResources().getDimensionPixelSize(C1893R.dimen.circle_qs_icon_size);
    }

    public void measureCircleIcon(View view, ImageView imageView, FrameLayout frameLayout, int i, int i2) {
        Intrinsics.checkNotNullParameter(view, BaseIconCache.IconDB.COLUMN_ICON);
        Intrinsics.checkNotNullParameter(imageView, "circleIconBg");
        Intrinsics.checkNotNullParameter(frameLayout, "circleIconFrame");
        view.measure(i, i);
        imageView.measure(i2, i2);
        frameLayout.measure(i2, i2);
    }
}
