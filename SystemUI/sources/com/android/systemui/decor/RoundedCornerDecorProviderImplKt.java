package com.android.systemui.decor;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.android.systemui.C1893R;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000&\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u001a\"\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u00042\u0006\u0010\u0006\u001a\u00020\u0005H\u0002\u001a\u001c\u0010\u0007\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000bH\u0002\u001a\u0014\u0010\f\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0002¨\u0006\r"}, mo64987d2 = {"adjustRotation", "", "Landroid/widget/ImageView;", "alignedBounds", "", "", "rotation", "setRoundedCornerImage", "resDelegate", "Lcom/android/systemui/decor/RoundedCornerResDelegate;", "isTop", "", "toLayoutGravity", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RoundedCornerDecorProviderImpl.kt */
public final class RoundedCornerDecorProviderImplKt {
    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0020, code lost:
        if (r6 != 2) goto L_0x002a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0012, code lost:
        if (r6 != 2) goto L_0x002c;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final int toLayoutGravity(int r6, int r7) {
        /*
            r0 = 80
            r1 = 5
            r2 = 48
            r3 = 2
            r4 = 3
            r5 = 1
            if (r7 == 0) goto L_0x0023
            if (r7 == r5) goto L_0x001c
            if (r7 == r4) goto L_0x0015
            if (r6 == 0) goto L_0x002a
            if (r6 == r5) goto L_0x002f
            if (r6 == r3) goto L_0x002e
            goto L_0x002c
        L_0x0015:
            if (r6 == 0) goto L_0x002c
            if (r6 == r5) goto L_0x002a
            if (r6 == r3) goto L_0x002f
            goto L_0x002e
        L_0x001c:
            if (r6 == 0) goto L_0x002f
            if (r6 == r5) goto L_0x002e
            if (r6 == r3) goto L_0x002c
            goto L_0x002a
        L_0x0023:
            if (r6 == 0) goto L_0x002e
            if (r6 == r5) goto L_0x002c
            if (r6 == r3) goto L_0x002a
            goto L_0x002f
        L_0x002a:
            r0 = r1
            goto L_0x002f
        L_0x002c:
            r0 = r2
            goto L_0x002f
        L_0x002e:
            r0 = r4
        L_0x002f:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.decor.RoundedCornerDecorProviderImplKt.toLayoutGravity(int, int):int");
    }

    /* access modifiers changed from: private */
    public static final void setRoundedCornerImage(ImageView imageView, RoundedCornerResDelegate roundedCornerResDelegate, boolean z) {
        Drawable drawable;
        if (z) {
            drawable = roundedCornerResDelegate.getTopRoundedDrawable();
        } else {
            drawable = roundedCornerResDelegate.getBottomRoundedDrawable();
        }
        if (drawable != null) {
            imageView.setImageDrawable(drawable);
        } else {
            imageView.setImageResource(z ? C1893R.C1895drawable.rounded_corner_top : C1893R.C1895drawable.rounded_corner_bottom);
        }
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x002c, code lost:
        if (r8 != false) goto L_0x0033;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003c, code lost:
        if (r8 != false) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x004e, code lost:
        if (r8 != false) goto L_0x0057;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0060, code lost:
        if (r8 != false) goto L_0x0043;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final void adjustRotation(android.widget.ImageView r7, java.util.List<java.lang.Integer> r8, int r9) {
        /*
            r0 = 1
            java.lang.Integer r1 = java.lang.Integer.valueOf((int) r0)
            boolean r1 = r8.contains(r1)
            r2 = 0
            java.lang.Integer r2 = java.lang.Integer.valueOf((int) r2)
            boolean r8 = r8.contains(r2)
            r2 = 1127481344(0x43340000, float:180.0)
            r3 = -1082130432(0xffffffffbf800000, float:-1.0)
            r4 = 0
            r5 = 1065353216(0x3f800000, float:1.0)
            if (r9 == 0) goto L_0x0051
            if (r9 == r0) goto L_0x003f
            r0 = 3
            if (r9 == r0) goto L_0x002f
            if (r1 == 0) goto L_0x0025
            if (r8 == 0) goto L_0x0025
        L_0x0024:
            goto L_0x0057
        L_0x0025:
            if (r1 == 0) goto L_0x002a
            if (r8 != 0) goto L_0x002a
            goto L_0x0043
        L_0x002a:
            if (r1 != 0) goto L_0x0056
            if (r8 == 0) goto L_0x0056
            goto L_0x0033
        L_0x002f:
            if (r1 == 0) goto L_0x0035
            if (r8 == 0) goto L_0x0035
        L_0x0033:
            r2 = r4
            goto L_0x0063
        L_0x0035:
            if (r1 == 0) goto L_0x003a
            if (r8 != 0) goto L_0x003a
            goto L_0x0024
        L_0x003a:
            if (r1 != 0) goto L_0x0043
            if (r8 != 0) goto L_0x0056
            goto L_0x0043
        L_0x003f:
            if (r1 == 0) goto L_0x0048
            if (r8 == 0) goto L_0x0048
        L_0x0043:
            r2 = r4
            r6 = r5
            r5 = r3
            r3 = r6
            goto L_0x0063
        L_0x0048:
            if (r1 == 0) goto L_0x004c
            if (r8 == 0) goto L_0x0056
        L_0x004c:
            if (r1 != 0) goto L_0x0033
            if (r8 == 0) goto L_0x0033
            goto L_0x0024
        L_0x0051:
            if (r1 == 0) goto L_0x0059
            if (r8 != 0) goto L_0x0056
            goto L_0x0059
        L_0x0056:
            r2 = r4
        L_0x0057:
            r3 = r5
            goto L_0x0063
        L_0x0059:
            if (r1 == 0) goto L_0x005e
            if (r8 != 0) goto L_0x005e
            goto L_0x0033
        L_0x005e:
            if (r1 != 0) goto L_0x0057
            if (r8 == 0) goto L_0x0057
            goto L_0x0043
        L_0x0063:
            r7.setRotation(r2)
            r7.setScaleX(r3)
            r7.setScaleY(r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.decor.RoundedCornerDecorProviderImplKt.adjustRotation(android.widget.ImageView, java.util.List, int):void");
    }
}
