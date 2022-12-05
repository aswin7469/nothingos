package com.android.launcher3.icons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.os.Process;
import android.os.UserHandle;
import com.android.launcher3.icons.BitmapInfo;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ThemedIconDrawable$ThemedAdaptiveIcon extends AdaptiveIconDrawable implements BitmapInfo.Extender {
    protected final ThemedIconDrawable$ThemeData mThemeData;

    public ThemedIconDrawable$ThemedAdaptiveIcon(AdaptiveIconDrawable adaptiveIconDrawable, ThemedIconDrawable$ThemeData themedIconDrawable$ThemeData) {
        super(adaptiveIconDrawable.getBackground(), adaptiveIconDrawable.getForeground());
        this.mThemeData = themedIconDrawable$ThemeData;
    }

    @Override // com.android.launcher3.icons.BitmapInfo.Extender
    /* renamed from: getExtendedInfo */
    public BitmapInfo mo253getExtendedInfo(final Bitmap bitmap, final int i, BaseIconFactory baseIconFactory, final float f, UserHandle userHandle) {
        final Bitmap userBadgeBitmap = Process.myUserHandle().equals(userHandle) ? null : baseIconFactory.getUserBadgeBitmap(userHandle);
        final ThemedIconDrawable$ThemeData themedIconDrawable$ThemeData = this.mThemeData;
        return new BitmapInfo(bitmap, i, themedIconDrawable$ThemeData, f, userBadgeBitmap) { // from class: com.android.launcher3.icons.ThemedIconDrawable$ThemedBitmapInfo
            final float mNormalizationScale;
            final ThemedIconDrawable$ThemeData mThemeData;
            final Bitmap mUserBadge;

            {
                this.mThemeData = themedIconDrawable$ThemeData;
                this.mNormalizationScale = f;
                this.mUserBadge = userBadgeBitmap;
            }
        };
    }

    @Override // com.android.launcher3.icons.BitmapInfo.Extender
    public void drawForPersistence(Canvas canvas) {
        draw(canvas);
    }
}
