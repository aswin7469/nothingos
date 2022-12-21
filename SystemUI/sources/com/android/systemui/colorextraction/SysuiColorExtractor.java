package com.android.systemui.colorextraction;

import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.Context;
import android.os.Handler;
import androidx.core.view.ViewCompat;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.colorextraction.types.ExtractionType;
import com.android.internal.colorextraction.types.Tonal;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.Dumpable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.p026io.PrintWriter;
import java.util.Arrays;
import javax.inject.Inject;

@SysUISingleton
public class SysuiColorExtractor extends ColorExtractor implements Dumpable, ConfigurationController.ConfigurationListener {
    private static final String TAG = "SysuiColorExtractor";
    private final ColorExtractor.GradientColors mBackdropColors;
    private boolean mHasMediaArtwork;
    private final ColorExtractor.GradientColors mNeutralColorsLock;
    private final Tonal mTonal;

    @Inject
    public SysuiColorExtractor(Context context, ConfigurationController configurationController, DumpManager dumpManager) {
        this(context, new Tonal(context), configurationController, (WallpaperManager) context.getSystemService(WallpaperManager.class), dumpManager, false);
    }

    /* JADX WARNING: type inference failed for: r0v0, types: [com.android.systemui.Dumpable, android.app.WallpaperManager$OnColorsChangedListener, java.lang.Object, com.android.systemui.colorextraction.SysuiColorExtractor] */
    public SysuiColorExtractor(Context context, ExtractionType extractionType, ConfigurationController configurationController, WallpaperManager wallpaperManager, DumpManager dumpManager, boolean z) {
        super(context, extractionType, z, wallpaperManager);
        this.mTonal = extractionType instanceof Tonal ? (Tonal) extractionType : new Tonal(context);
        this.mNeutralColorsLock = new ColorExtractor.GradientColors();
        configurationController.addCallback(this);
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
        ColorExtractor.GradientColors gradientColors = new ColorExtractor.GradientColors();
        this.mBackdropColors = gradientColors;
        gradientColors.setMainColor(ViewCompat.MEASURED_STATE_MASK);
        if (wallpaperManager.isWallpaperSupported()) {
            wallpaperManager.removeOnColorsChangedListener(this);
            wallpaperManager.addOnColorsChangedListener(this, (Handler) null, -1);
        }
    }

    /* access modifiers changed from: protected */
    public void extractWallpaperColors() {
        SysuiColorExtractor.super.extractWallpaperColors();
        Tonal tonal = this.mTonal;
        if (tonal != null && this.mNeutralColorsLock != null) {
            tonal.applyFallback(this.mLockColors == null ? this.mSystemColors : this.mLockColors, this.mNeutralColorsLock);
        }
    }

    public void onColorsChanged(WallpaperColors wallpaperColors, int i, int i2) {
        if (i2 == KeyguardUpdateMonitor.getCurrentUser()) {
            if ((i & 2) != 0) {
                this.mTonal.applyFallback(wallpaperColors, this.mNeutralColorsLock);
            }
            SysuiColorExtractor.super.onColorsChanged(wallpaperColors, i);
        }
    }

    public void onUiModeChanged() {
        extractWallpaperColors();
        triggerColorsChanged(3);
    }

    public ColorExtractor.GradientColors getColors(int i, int i2) {
        if (!this.mHasMediaArtwork || (i & 2) == 0) {
            return SysuiColorExtractor.super.getColors(i, i2);
        }
        return this.mBackdropColors;
    }

    public ColorExtractor.GradientColors getNeutralColors() {
        return this.mHasMediaArtwork ? this.mBackdropColors : this.mNeutralColorsLock;
    }

    public void setHasMediaArtwork(boolean z) {
        if (this.mHasMediaArtwork != z) {
            this.mHasMediaArtwork = z;
            triggerColorsChanged(2);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("SysuiColorExtractor:");
        printWriter.println("  Current wallpaper colors:");
        printWriter.println("    system: " + this.mSystemColors);
        printWriter.println("    lock: " + this.mLockColors);
        printWriter.println("  Gradients:");
        printWriter.println("    system: " + Arrays.toString((Object[]) (ColorExtractor.GradientColors[]) this.mGradientColors.get(1)));
        printWriter.println("    lock: " + Arrays.toString((Object[]) (ColorExtractor.GradientColors[]) this.mGradientColors.get(2)));
        printWriter.println("  Neutral colors: " + this.mNeutralColorsLock);
        printWriter.println("  Has media backdrop: " + this.mHasMediaArtwork);
    }
}
