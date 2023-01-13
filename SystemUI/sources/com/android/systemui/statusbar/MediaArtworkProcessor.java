package com.android.systemui.statusbar;

import android.graphics.Bitmap;
import android.graphics.Point;
import com.android.systemui.dagger.SysUISingleton;
import kotlin.Metadata;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\bJ\u0018\u0010\t\u001a\u0004\u0018\u00010\u00042\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0004R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, mo65043d2 = {"Lcom/android/systemui/statusbar/MediaArtworkProcessor;", "", "()V", "mArtworkCache", "Landroid/graphics/Bitmap;", "mTmpSize", "Landroid/graphics/Point;", "clearCache", "", "processArtwork", "context", "Landroid/content/Context;", "artwork", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaArtworkProcessor.kt */
public final class MediaArtworkProcessor {
    private Bitmap mArtworkCache;
    private final Point mTmpSize = new Point();

    /* JADX WARNING: Removed duplicated region for block: B:57:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x00f0  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x00f8  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x0100  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0105  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x010d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.graphics.Bitmap processArtwork(android.content.Context r8, android.graphics.Bitmap r9) {
        /*
            r7 = this;
            java.lang.String r0 = "context"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r8, r0)
            java.lang.String r0 = "artwork"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r9, r0)
            android.graphics.Bitmap r0 = r7.mArtworkCache
            if (r0 == 0) goto L_0x000f
            return r0
        L_0x000f:
            android.renderscript.RenderScript r0 = android.renderscript.RenderScript.create(r8)
            android.renderscript.Element r1 = android.renderscript.Element.U8_4(r0)
            android.renderscript.ScriptIntrinsicBlur r1 = android.renderscript.ScriptIntrinsicBlur.create(r0, r1)
            r2 = 0
            android.view.Display r8 = r8.getDisplay()     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            if (r8 == 0) goto L_0x0027
            android.graphics.Point r3 = r7.mTmpSize     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            r8.getSize(r3)     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
        L_0x0027:
            android.graphics.Rect r8 = new android.graphics.Rect     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            int r3 = r9.getWidth()     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            int r4 = r9.getHeight()     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            r5 = 0
            r8.<init>(r5, r5, r3, r4)     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            android.graphics.Point r3 = r7.mTmpSize     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            int r3 = r3.x     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            int r3 = r3 / 6
            android.graphics.Point r7 = r7.mTmpSize     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            int r7 = r7.y     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            int r7 = r7 / 6
            int r7 = java.lang.Math.max((int) r3, (int) r7)     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            android.util.MathUtils.fitRect(r8, r7)     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            int r7 = r8.width()     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            int r8 = r8.height()     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            r3 = 1
            android.graphics.Bitmap r7 = android.graphics.Bitmap.createScaledBitmap(r9, r7, r8, r3)     // Catch:{ IllegalArgumentException -> 0x00dc, all -> 0x00d8 }
            android.graphics.Bitmap$Config r8 = r7.getConfig()     // Catch:{ IllegalArgumentException -> 0x00d1, all -> 0x00cb }
            android.graphics.Bitmap$Config r3 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ IllegalArgumentException -> 0x00d1, all -> 0x00cb }
            if (r8 == r3) goto L_0x0070
            android.graphics.Bitmap$Config r8 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ IllegalArgumentException -> 0x00d1, all -> 0x00cb }
            android.graphics.Bitmap r8 = r7.copy(r8, r5)     // Catch:{ IllegalArgumentException -> 0x00d1, all -> 0x00cb }
            r7.recycle()     // Catch:{ IllegalArgumentException -> 0x006c, all -> 0x0068 }
            r7 = r8
            goto L_0x0070
        L_0x0068:
            r7 = move-exception
            r0 = r2
            goto L_0x00fe
        L_0x006c:
            r7 = move-exception
            r0 = r2
            goto L_0x00df
        L_0x0070:
            int r8 = r7.getWidth()     // Catch:{ IllegalArgumentException -> 0x00d1, all -> 0x00cb }
            int r3 = r7.getHeight()     // Catch:{ IllegalArgumentException -> 0x00d1, all -> 0x00cb }
            android.graphics.Bitmap$Config r4 = android.graphics.Bitmap.Config.ARGB_8888     // Catch:{ IllegalArgumentException -> 0x00d1, all -> 0x00cb }
            android.graphics.Bitmap r8 = android.graphics.Bitmap.createBitmap(r8, r3, r4)     // Catch:{ IllegalArgumentException -> 0x00d1, all -> 0x00cb }
            android.renderscript.Allocation$MipmapControl r3 = android.renderscript.Allocation.MipmapControl.MIPMAP_NONE     // Catch:{ IllegalArgumentException -> 0x00d1, all -> 0x00cb }
            r4 = 2
            android.renderscript.Allocation r3 = android.renderscript.Allocation.createFromBitmap(r0, r7, r3, r4)     // Catch:{ IllegalArgumentException -> 0x00d1, all -> 0x00cb }
            android.renderscript.Allocation r0 = android.renderscript.Allocation.createFromBitmap(r0, r8)     // Catch:{ IllegalArgumentException -> 0x00c8, all -> 0x00c4 }
            r4 = 1103626240(0x41c80000, float:25.0)
            r1.setRadius(r4)     // Catch:{ IllegalArgumentException -> 0x00c2, all -> 0x00c0 }
            r1.setInput(r3)     // Catch:{ IllegalArgumentException -> 0x00c2, all -> 0x00c0 }
            r1.forEach(r0)     // Catch:{ IllegalArgumentException -> 0x00c2, all -> 0x00c0 }
            r0.copyTo(r8)     // Catch:{ IllegalArgumentException -> 0x00c2, all -> 0x00c0 }
            androidx.palette.graphics.Palette$Swatch r9 = com.android.systemui.statusbar.notification.MediaNotificationProcessor.findBackgroundSwatch((android.graphics.Bitmap) r9)     // Catch:{ IllegalArgumentException -> 0x00c2, all -> 0x00c0 }
            android.graphics.Canvas r4 = new android.graphics.Canvas     // Catch:{ IllegalArgumentException -> 0x00c2, all -> 0x00c0 }
            r4.<init>(r8)     // Catch:{ IllegalArgumentException -> 0x00c2, all -> 0x00c0 }
            int r9 = r9.getRgb()     // Catch:{ IllegalArgumentException -> 0x00c2, all -> 0x00c0 }
            r5 = 178(0xb2, float:2.5E-43)
            int r9 = com.android.internal.graphics.ColorUtils.setAlphaComponent(r9, r5)     // Catch:{ IllegalArgumentException -> 0x00c2, all -> 0x00c0 }
            r4.drawColor(r9)     // Catch:{ IllegalArgumentException -> 0x00c2, all -> 0x00c0 }
            if (r3 == 0) goto L_0x00b2
            r3.destroy()
        L_0x00b2:
            if (r0 == 0) goto L_0x00b7
            r0.destroy()
        L_0x00b7:
            r1.destroy()
            if (r7 == 0) goto L_0x00bf
            r7.recycle()
        L_0x00bf:
            return r8
        L_0x00c0:
            r8 = move-exception
            goto L_0x00c6
        L_0x00c2:
            r8 = move-exception
            goto L_0x00d4
        L_0x00c4:
            r8 = move-exception
            r0 = r2
        L_0x00c6:
            r2 = r3
            goto L_0x00cd
        L_0x00c8:
            r8 = move-exception
            r0 = r2
            goto L_0x00d4
        L_0x00cb:
            r8 = move-exception
            r0 = r2
        L_0x00cd:
            r6 = r8
            r8 = r7
            r7 = r6
            goto L_0x00fe
        L_0x00d1:
            r8 = move-exception
            r0 = r2
            r3 = r0
        L_0x00d4:
            r6 = r8
            r8 = r7
            r7 = r6
            goto L_0x00e0
        L_0x00d8:
            r7 = move-exception
            r8 = r2
            r0 = r8
            goto L_0x00fe
        L_0x00dc:
            r7 = move-exception
            r8 = r2
            r0 = r8
        L_0x00df:
            r3 = r0
        L_0x00e0:
            java.lang.String r9 = "MediaArtworkProcessor"
            java.lang.String r4 = "error while processing artwork"
            java.lang.Throwable r7 = (java.lang.Throwable) r7     // Catch:{ all -> 0x00fc }
            android.util.Log.e(r9, r4, r7)     // Catch:{ all -> 0x00fc }
            if (r3 == 0) goto L_0x00ee
            r3.destroy()
        L_0x00ee:
            if (r0 == 0) goto L_0x00f3
            r0.destroy()
        L_0x00f3:
            r1.destroy()
            if (r8 == 0) goto L_0x00fb
            r8.recycle()
        L_0x00fb:
            return r2
        L_0x00fc:
            r7 = move-exception
            r2 = r3
        L_0x00fe:
            if (r2 == 0) goto L_0x0103
            r2.destroy()
        L_0x0103:
            if (r0 == 0) goto L_0x0108
            r0.destroy()
        L_0x0108:
            r1.destroy()
            if (r8 == 0) goto L_0x0110
            r8.recycle()
        L_0x0110:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.MediaArtworkProcessor.processArtwork(android.content.Context, android.graphics.Bitmap):android.graphics.Bitmap");
    }

    public final void clearCache() {
        Bitmap bitmap = this.mArtworkCache;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.mArtworkCache = null;
    }
}
