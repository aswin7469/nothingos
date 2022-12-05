package com.android.systemui.colorextraction;

import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.Context;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.internal.colorextraction.types.ExtractionType;
import com.android.internal.colorextraction.types.Tonal;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.Dumpable;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
/* loaded from: classes.dex */
public class SysuiColorExtractor extends ColorExtractor implements Dumpable, ConfigurationController.ConfigurationListener {
    private final ColorExtractor.GradientColors mBackdropColors;
    private boolean mHasMediaArtwork;
    private final ColorExtractor.GradientColors mNeutralColorsLock;
    private final Tonal mTonal;

    public SysuiColorExtractor(Context context, ConfigurationController configurationController) {
        this(context, new Tonal(context), configurationController, (WallpaperManager) context.getSystemService(WallpaperManager.class), false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @VisibleForTesting
    public SysuiColorExtractor(Context context, ExtractionType extractionType, ConfigurationController configurationController, WallpaperManager wallpaperManager, boolean z) {
        super(context, extractionType, z, wallpaperManager);
        this.mTonal = extractionType instanceof Tonal ? (Tonal) extractionType : new Tonal(context);
        this.mNeutralColorsLock = new ColorExtractor.GradientColors();
        configurationController.addCallback(this);
        ColorExtractor.GradientColors gradientColors = new ColorExtractor.GradientColors();
        this.mBackdropColors = gradientColors;
        gradientColors.setMainColor(-16777216);
        if (wallpaperManager.isWallpaperSupported()) {
            wallpaperManager.removeOnColorsChangedListener(this);
            wallpaperManager.addOnColorsChangedListener(this, null, -1);
        }
    }

    protected void extractWallpaperColors() {
        ColorExtractor.GradientColors gradientColors;
        super.extractWallpaperColors();
        Tonal tonal = this.mTonal;
        if (tonal == null || (gradientColors = this.mNeutralColorsLock) == null) {
            return;
        }
        WallpaperColors wallpaperColors = ((ColorExtractor) this).mLockColors;
        if (wallpaperColors == null) {
            wallpaperColors = ((ColorExtractor) this).mSystemColors;
        }
        tonal.applyFallback(wallpaperColors, gradientColors);
    }

    public void onColorsChanged(WallpaperColors wallpaperColors, int i, int i2) {
        if (i2 != KeyguardUpdateMonitor.getCurrentUser()) {
            return;
        }
        if ((i & 2) != 0) {
            this.mTonal.applyFallback(wallpaperColors, this.mNeutralColorsLock);
        }
        super.onColorsChanged(wallpaperColors, i);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onUiModeChanged() {
        extractWallpaperColors();
        triggerColorsChanged(3);
    }

    public ColorExtractor.GradientColors getColors(int i, int i2) {
        if (this.mHasMediaArtwork && (i & 2) != 0) {
            return this.mBackdropColors;
        }
        return super.getColors(i, i2);
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

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("SysuiColorExtractor:");
        printWriter.println("  Current wallpaper colors:");
        printWriter.println("    system: " + ((ColorExtractor) this).mSystemColors);
        printWriter.println("    lock: " + ((ColorExtractor) this).mLockColors);
        printWriter.println("  Gradients:");
        printWriter.println("    system: " + Arrays.toString((ColorExtractor.GradientColors[]) ((ColorExtractor) this).mGradientColors.get(1)));
        printWriter.println("    lock: " + Arrays.toString((ColorExtractor.GradientColors[]) ((ColorExtractor) this).mGradientColors.get(2)));
        printWriter.println("  Neutral colors: " + this.mNeutralColorsLock);
        printWriter.println("  Has media backdrop: " + this.mHasMediaArtwork);
    }
}
