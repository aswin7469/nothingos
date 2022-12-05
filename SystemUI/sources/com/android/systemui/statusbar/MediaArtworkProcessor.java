package com.android.systemui.statusbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.util.MathUtils;
import android.view.Display;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.statusbar.notification.MediaNotificationProcessor;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: MediaArtworkProcessor.kt */
/* loaded from: classes.dex */
public final class MediaArtworkProcessor {
    @Nullable
    private Bitmap mArtworkCache;
    @NotNull
    private final Point mTmpSize = new Point();

    /* JADX WARN: Removed duplicated region for block: B:26:0x00e4  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00ea  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00f3  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00fc  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x010b  */
    @Nullable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Bitmap processArtwork(@NotNull Context context, @NotNull Bitmap artwork) {
        Bitmap bitmap;
        Allocation allocation;
        Allocation allocation2;
        Bitmap createScaledBitmap;
        Bitmap createBitmap;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(artwork, "artwork");
        Bitmap bitmap2 = this.mArtworkCache;
        if (bitmap2 != null) {
            return bitmap2;
        }
        RenderScript create = RenderScript.create(context);
        ScriptIntrinsicBlur create2 = ScriptIntrinsicBlur.create(create, Element.U8_4(create));
        Allocation allocation3 = null;
        try {
            Display display = context.getDisplay();
            if (display != null) {
                display.getSize(this.mTmpSize);
            }
            Rect rect = new Rect(0, 0, artwork.getWidth(), artwork.getHeight());
            Point point = this.mTmpSize;
            MathUtils.fitRect(rect, Math.max(point.x / 6, point.y / 6));
            createScaledBitmap = Bitmap.createScaledBitmap(artwork, rect.width(), rect.height(), true);
            try {
                Bitmap.Config config = createScaledBitmap.getConfig();
                Bitmap.Config config2 = Bitmap.Config.ARGB_8888;
                if (config != config2) {
                    bitmap = createScaledBitmap.copy(config2, false);
                    try {
                        createScaledBitmap.recycle();
                        createScaledBitmap = bitmap;
                    } catch (IllegalArgumentException e) {
                        e = e;
                        allocation = null;
                        allocation2 = allocation;
                        try {
                            Log.e("MediaArtworkProcessor", "error while processing artwork", e);
                            if (allocation2 != null) {
                                allocation2.destroy();
                            }
                            if (allocation != null) {
                                allocation.destroy();
                            }
                            create2.destroy();
                            if (bitmap != null) {
                                bitmap.recycle();
                            }
                            return null;
                        } catch (Throwable th) {
                            th = th;
                            allocation3 = allocation2;
                            if (allocation3 != null) {
                                allocation3.destroy();
                            }
                            if (allocation != null) {
                                allocation.destroy();
                            }
                            create2.destroy();
                            if (bitmap != null) {
                                bitmap.recycle();
                            }
                            throw th;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        allocation = null;
                        if (allocation3 != null) {
                        }
                        if (allocation != null) {
                        }
                        create2.destroy();
                        if (bitmap != null) {
                        }
                        throw th;
                    }
                }
                createBitmap = Bitmap.createBitmap(createScaledBitmap.getWidth(), createScaledBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                allocation2 = Allocation.createFromBitmap(create, createScaledBitmap, Allocation.MipmapControl.MIPMAP_NONE, 2);
                try {
                    allocation = Allocation.createFromBitmap(create, createBitmap);
                } catch (IllegalArgumentException e2) {
                    e = e2;
                    allocation = null;
                } catch (Throwable th3) {
                    th = th3;
                    allocation = null;
                }
            } catch (IllegalArgumentException e3) {
                e = e3;
                allocation = null;
                allocation2 = null;
            } catch (Throwable th4) {
                th = th4;
                allocation = null;
            }
        } catch (IllegalArgumentException e4) {
            e = e4;
            bitmap = null;
            allocation = null;
        } catch (Throwable th5) {
            th = th5;
            bitmap = null;
            allocation = null;
        }
        try {
            create2.setRadius(25.0f);
            create2.setInput(allocation2);
            create2.forEach(allocation);
            allocation.copyTo(createBitmap);
            new Canvas(createBitmap).drawColor(ColorUtils.setAlphaComponent(MediaNotificationProcessor.findBackgroundSwatch(artwork).getRgb(), 178));
            if (allocation2 != null) {
                allocation2.destroy();
            }
            allocation.destroy();
            create2.destroy();
            createScaledBitmap.recycle();
            return createBitmap;
        } catch (IllegalArgumentException e5) {
            e = e5;
            IllegalArgumentException illegalArgumentException = e;
            bitmap = createScaledBitmap;
            e = illegalArgumentException;
            Log.e("MediaArtworkProcessor", "error while processing artwork", e);
            if (allocation2 != null) {
            }
            if (allocation != null) {
            }
            create2.destroy();
            if (bitmap != null) {
            }
            return null;
        } catch (Throwable th6) {
            th = th6;
            allocation3 = allocation2;
            Throwable th7 = th;
            bitmap = createScaledBitmap;
            th = th7;
            if (allocation3 != null) {
            }
            if (allocation != null) {
            }
            create2.destroy();
            if (bitmap != null) {
            }
            throw th;
        }
    }

    public final void clearCache() {
        Bitmap bitmap = this.mArtworkCache;
        if (bitmap != null) {
            bitmap.recycle();
        }
        this.mArtworkCache = null;
    }
}
