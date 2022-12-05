package androidx.palette.graphics;

import android.graphics.Color;
import androidx.core.graphics.ColorUtils;
import androidx.palette.graphics.Palette;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ColorCutQuantizer {
    private static final Comparator<Vbox> VBOX_COMPARATOR_VOLUME = new Comparator<Vbox>() { // from class: androidx.palette.graphics.ColorCutQuantizer.1
        @Override // java.util.Comparator
        public int compare(Vbox lhs, Vbox rhs) {
            return rhs.getVolume() - lhs.getVolume();
        }
    };
    final int[] mColors;
    final Palette.Filter[] mFilters;
    final int[] mHistogram;
    final List<Palette.Swatch> mQuantizedColors;
    private final float[] mTempHsl = new float[3];

    private static int modifyWordWidth(int value, int currentWidth, int targetWidth) {
        return (targetWidth > currentWidth ? value << (targetWidth - currentWidth) : value >> (currentWidth - targetWidth)) & ((1 << targetWidth) - 1);
    }

    static int quantizedBlue(int color) {
        return color & 31;
    }

    static int quantizedGreen(int color) {
        return (color >> 5) & 31;
    }

    static int quantizedRed(int color) {
        return (color >> 10) & 31;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorCutQuantizer(int[] pixels, int maxColors, Palette.Filter[] filters) {
        this.mFilters = filters;
        int[] iArr = new int[32768];
        this.mHistogram = iArr;
        for (int i = 0; i < pixels.length; i++) {
            int quantizeFromRgb888 = quantizeFromRgb888(pixels[i]);
            pixels[i] = quantizeFromRgb888;
            iArr[quantizeFromRgb888] = iArr[quantizeFromRgb888] + 1;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < 32768; i3++) {
            if (iArr[i3] > 0 && shouldIgnoreColor(i3)) {
                iArr[i3] = 0;
            }
            if (iArr[i3] > 0) {
                i2++;
            }
        }
        int[] iArr2 = new int[i2];
        this.mColors = iArr2;
        int i4 = 0;
        for (int i5 = 0; i5 < 32768; i5++) {
            if (iArr[i5] > 0) {
                iArr2[i4] = i5;
                i4++;
            }
        }
        if (i2 <= maxColors) {
            this.mQuantizedColors = new ArrayList();
            for (int i6 = 0; i6 < i2; i6++) {
                int i7 = iArr2[i6];
                this.mQuantizedColors.add(new Palette.Swatch(approximateToRgb888(i7), iArr[i7]));
            }
            return;
        }
        this.mQuantizedColors = quantizePixels(maxColors);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public List<Palette.Swatch> getQuantizedColors() {
        return this.mQuantizedColors;
    }

    private List<Palette.Swatch> quantizePixels(int maxColors) {
        PriorityQueue<Vbox> priorityQueue = new PriorityQueue<>(maxColors, VBOX_COMPARATOR_VOLUME);
        priorityQueue.offer(new Vbox(0, this.mColors.length - 1));
        splitBoxes(priorityQueue, maxColors);
        return generateAverageColors(priorityQueue);
    }

    private void splitBoxes(final PriorityQueue<Vbox> queue, final int maxSize) {
        Vbox poll;
        while (queue.size() < maxSize && (poll = queue.poll()) != null && poll.canSplit()) {
            queue.offer(poll.splitBox());
            queue.offer(poll);
        }
    }

    private List<Palette.Swatch> generateAverageColors(Collection<Vbox> vboxes) {
        ArrayList arrayList = new ArrayList(vboxes.size());
        for (Vbox vbox : vboxes) {
            Palette.Swatch averageColor = vbox.getAverageColor();
            if (!shouldIgnoreColor(averageColor)) {
                arrayList.add(averageColor);
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Vbox {
        private int mLowerIndex;
        private int mMaxBlue;
        private int mMaxGreen;
        private int mMaxRed;
        private int mMinBlue;
        private int mMinGreen;
        private int mMinRed;
        private int mPopulation;
        private int mUpperIndex;

        Vbox(int lowerIndex, int upperIndex) {
            this.mLowerIndex = lowerIndex;
            this.mUpperIndex = upperIndex;
            fitBox();
        }

        final int getVolume() {
            return ((this.mMaxRed - this.mMinRed) + 1) * ((this.mMaxGreen - this.mMinGreen) + 1) * ((this.mMaxBlue - this.mMinBlue) + 1);
        }

        final boolean canSplit() {
            return getColorCount() > 1;
        }

        final int getColorCount() {
            return (this.mUpperIndex + 1) - this.mLowerIndex;
        }

        final void fitBox() {
            ColorCutQuantizer colorCutQuantizer = ColorCutQuantizer.this;
            int[] iArr = colorCutQuantizer.mColors;
            int[] iArr2 = colorCutQuantizer.mHistogram;
            int i = Integer.MAX_VALUE;
            int i2 = Integer.MIN_VALUE;
            int i3 = Integer.MIN_VALUE;
            int i4 = Integer.MIN_VALUE;
            int i5 = 0;
            int i6 = Integer.MAX_VALUE;
            int i7 = Integer.MAX_VALUE;
            for (int i8 = this.mLowerIndex; i8 <= this.mUpperIndex; i8++) {
                int i9 = iArr[i8];
                i5 += iArr2[i9];
                int quantizedRed = ColorCutQuantizer.quantizedRed(i9);
                int quantizedGreen = ColorCutQuantizer.quantizedGreen(i9);
                int quantizedBlue = ColorCutQuantizer.quantizedBlue(i9);
                if (quantizedRed > i2) {
                    i2 = quantizedRed;
                }
                if (quantizedRed < i) {
                    i = quantizedRed;
                }
                if (quantizedGreen > i3) {
                    i3 = quantizedGreen;
                }
                if (quantizedGreen < i6) {
                    i6 = quantizedGreen;
                }
                if (quantizedBlue > i4) {
                    i4 = quantizedBlue;
                }
                if (quantizedBlue < i7) {
                    i7 = quantizedBlue;
                }
            }
            this.mMinRed = i;
            this.mMaxRed = i2;
            this.mMinGreen = i6;
            this.mMaxGreen = i3;
            this.mMinBlue = i7;
            this.mMaxBlue = i4;
            this.mPopulation = i5;
        }

        final Vbox splitBox() {
            if (!canSplit()) {
                throw new IllegalStateException("Can not split a box with only 1 color");
            }
            int findSplitPoint = findSplitPoint();
            Vbox vbox = new Vbox(findSplitPoint + 1, this.mUpperIndex);
            this.mUpperIndex = findSplitPoint;
            fitBox();
            return vbox;
        }

        final int getLongestColorDimension() {
            int i = this.mMaxRed - this.mMinRed;
            int i2 = this.mMaxGreen - this.mMinGreen;
            int i3 = this.mMaxBlue - this.mMinBlue;
            if (i < i2 || i < i3) {
                return (i2 < i || i2 < i3) ? -1 : -2;
            }
            return -3;
        }

        final int findSplitPoint() {
            int longestColorDimension = getLongestColorDimension();
            ColorCutQuantizer colorCutQuantizer = ColorCutQuantizer.this;
            int[] iArr = colorCutQuantizer.mColors;
            int[] iArr2 = colorCutQuantizer.mHistogram;
            ColorCutQuantizer.modifySignificantOctet(iArr, longestColorDimension, this.mLowerIndex, this.mUpperIndex);
            Arrays.sort(iArr, this.mLowerIndex, this.mUpperIndex + 1);
            ColorCutQuantizer.modifySignificantOctet(iArr, longestColorDimension, this.mLowerIndex, this.mUpperIndex);
            int i = this.mPopulation / 2;
            int i2 = this.mLowerIndex;
            int i3 = 0;
            while (true) {
                int i4 = this.mUpperIndex;
                if (i2 <= i4) {
                    i3 += iArr2[iArr[i2]];
                    if (i3 >= i) {
                        return Math.min(i4 - 1, i2);
                    }
                    i2++;
                } else {
                    return this.mLowerIndex;
                }
            }
        }

        final Palette.Swatch getAverageColor() {
            ColorCutQuantizer colorCutQuantizer = ColorCutQuantizer.this;
            int[] iArr = colorCutQuantizer.mColors;
            int[] iArr2 = colorCutQuantizer.mHistogram;
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            for (int i5 = this.mLowerIndex; i5 <= this.mUpperIndex; i5++) {
                int i6 = iArr[i5];
                int i7 = iArr2[i6];
                i2 += i7;
                i += ColorCutQuantizer.quantizedRed(i6) * i7;
                i3 += ColorCutQuantizer.quantizedGreen(i6) * i7;
                i4 += i7 * ColorCutQuantizer.quantizedBlue(i6);
            }
            float f = i2;
            return new Palette.Swatch(ColorCutQuantizer.approximateToRgb888(Math.round(i / f), Math.round(i3 / f), Math.round(i4 / f)), i2);
        }
    }

    static void modifySignificantOctet(final int[] a, final int dimension, final int lower, final int upper) {
        if (dimension == -2) {
            while (lower <= upper) {
                int i = a[lower];
                a[lower] = quantizedBlue(i) | (quantizedGreen(i) << 10) | (quantizedRed(i) << 5);
                lower++;
            }
        } else if (dimension != -1) {
        } else {
            while (lower <= upper) {
                int i2 = a[lower];
                a[lower] = quantizedRed(i2) | (quantizedBlue(i2) << 10) | (quantizedGreen(i2) << 5);
                lower++;
            }
        }
    }

    private boolean shouldIgnoreColor(int color565) {
        int approximateToRgb888 = approximateToRgb888(color565);
        ColorUtils.colorToHSL(approximateToRgb888, this.mTempHsl);
        return shouldIgnoreColor(approximateToRgb888, this.mTempHsl);
    }

    private boolean shouldIgnoreColor(Palette.Swatch color) {
        return shouldIgnoreColor(color.getRgb(), color.getHsl());
    }

    private boolean shouldIgnoreColor(int rgb, float[] hsl) {
        Palette.Filter[] filterArr = this.mFilters;
        if (filterArr != null && filterArr.length > 0) {
            int length = filterArr.length;
            for (int i = 0; i < length; i++) {
                if (!this.mFilters[i].isAllowed(rgb, hsl)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int quantizeFromRgb888(int color) {
        return modifyWordWidth(Color.blue(color), 8, 5) | (modifyWordWidth(Color.red(color), 8, 5) << 10) | (modifyWordWidth(Color.green(color), 8, 5) << 5);
    }

    static int approximateToRgb888(int r, int g, int b) {
        return Color.rgb(modifyWordWidth(r, 5, 8), modifyWordWidth(g, 5, 8), modifyWordWidth(b, 5, 8));
    }

    private static int approximateToRgb888(int color) {
        return approximateToRgb888(quantizedRed(color), quantizedGreen(color), quantizedBlue(color));
    }
}
