package androidx.core.content.res;

import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.LinearGradient;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import androidx.core.R$styleable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: classes.dex */
final class GradientColorInflaterCompat {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static Shader createFromXmlInner(Resources resources, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws IOException, XmlPullParserException {
        String name = parser.getName();
        if (!name.equals("gradient")) {
            throw new XmlPullParserException(parser.getPositionDescription() + ": invalid gradient color tag " + name);
        }
        TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attrs, R$styleable.GradientColor);
        float namedFloat = TypedArrayUtils.getNamedFloat(obtainAttributes, parser, "startX", R$styleable.GradientColor_android_startX, 0.0f);
        float namedFloat2 = TypedArrayUtils.getNamedFloat(obtainAttributes, parser, "startY", R$styleable.GradientColor_android_startY, 0.0f);
        float namedFloat3 = TypedArrayUtils.getNamedFloat(obtainAttributes, parser, "endX", R$styleable.GradientColor_android_endX, 0.0f);
        float namedFloat4 = TypedArrayUtils.getNamedFloat(obtainAttributes, parser, "endY", R$styleable.GradientColor_android_endY, 0.0f);
        float namedFloat5 = TypedArrayUtils.getNamedFloat(obtainAttributes, parser, "centerX", R$styleable.GradientColor_android_centerX, 0.0f);
        float namedFloat6 = TypedArrayUtils.getNamedFloat(obtainAttributes, parser, "centerY", R$styleable.GradientColor_android_centerY, 0.0f);
        int namedInt = TypedArrayUtils.getNamedInt(obtainAttributes, parser, "type", R$styleable.GradientColor_android_type, 0);
        int namedColor = TypedArrayUtils.getNamedColor(obtainAttributes, parser, "startColor", R$styleable.GradientColor_android_startColor, 0);
        boolean hasAttribute = TypedArrayUtils.hasAttribute(parser, "centerColor");
        int namedColor2 = TypedArrayUtils.getNamedColor(obtainAttributes, parser, "centerColor", R$styleable.GradientColor_android_centerColor, 0);
        int namedColor3 = TypedArrayUtils.getNamedColor(obtainAttributes, parser, "endColor", R$styleable.GradientColor_android_endColor, 0);
        int namedInt2 = TypedArrayUtils.getNamedInt(obtainAttributes, parser, "tileMode", R$styleable.GradientColor_android_tileMode, 0);
        float namedFloat7 = TypedArrayUtils.getNamedFloat(obtainAttributes, parser, "gradientRadius", R$styleable.GradientColor_android_gradientRadius, 0.0f);
        obtainAttributes.recycle();
        ColorStops checkColors = checkColors(inflateChildElements(resources, parser, attrs, theme), namedColor, namedColor3, hasAttribute, namedColor2);
        if (namedInt != 1) {
            if (namedInt == 2) {
                return new SweepGradient(namedFloat5, namedFloat6, checkColors.mColors, checkColors.mOffsets);
            }
            return new LinearGradient(namedFloat, namedFloat2, namedFloat3, namedFloat4, checkColors.mColors, checkColors.mOffsets, parseTileMode(namedInt2));
        } else if (namedFloat7 <= 0.0f) {
            throw new XmlPullParserException("<gradient> tag requires 'gradientRadius' attribute with radial type");
        } else {
            return new RadialGradient(namedFloat5, namedFloat6, namedFloat7, checkColors.mColors, checkColors.mOffsets, parseTileMode(namedInt2));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0080, code lost:
        throw new org.xmlpull.v1.XmlPullParserException(r10.getPositionDescription() + ": <item> tag requires a 'color' attribute and a 'offset' attribute!");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static ColorStops inflateChildElements(Resources resources, XmlPullParser parser, AttributeSet attrs, Resources.Theme theme) throws XmlPullParserException, IOException {
        int depth;
        int depth2 = parser.getDepth() + 1;
        ArrayList arrayList = new ArrayList(20);
        ArrayList arrayList2 = new ArrayList(20);
        while (true) {
            int next = parser.next();
            if (next == 1 || ((depth = parser.getDepth()) < depth2 && next == 3)) {
                break;
            } else if (next == 2 && depth <= depth2 && parser.getName().equals("item")) {
                TypedArray obtainAttributes = TypedArrayUtils.obtainAttributes(resources, theme, attrs, R$styleable.GradientColorItem);
                int i = R$styleable.GradientColorItem_android_color;
                boolean hasValue = obtainAttributes.hasValue(i);
                int i2 = R$styleable.GradientColorItem_android_offset;
                boolean hasValue2 = obtainAttributes.hasValue(i2);
                if (!hasValue || !hasValue2) {
                    break;
                }
                int color = obtainAttributes.getColor(i, 0);
                float f = obtainAttributes.getFloat(i2, 0.0f);
                obtainAttributes.recycle();
                arrayList2.add(Integer.valueOf(color));
                arrayList.add(Float.valueOf(f));
            }
        }
        if (arrayList2.size() > 0) {
            return new ColorStops(arrayList2, arrayList);
        }
        return null;
    }

    private static ColorStops checkColors(ColorStops colorItems, int startColor, int endColor, boolean hasCenterColor, int centerColor) {
        if (colorItems != null) {
            return colorItems;
        }
        if (hasCenterColor) {
            return new ColorStops(startColor, centerColor, endColor);
        }
        return new ColorStops(startColor, endColor);
    }

    private static Shader.TileMode parseTileMode(int tileMode) {
        if (tileMode != 1) {
            if (tileMode == 2) {
                return Shader.TileMode.MIRROR;
            }
            return Shader.TileMode.CLAMP;
        }
        return Shader.TileMode.REPEAT;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class ColorStops {
        final int[] mColors;
        final float[] mOffsets;

        ColorStops(List<Integer> colorsList, List<Float> offsetsList) {
            int size = colorsList.size();
            this.mColors = new int[size];
            this.mOffsets = new float[size];
            for (int i = 0; i < size; i++) {
                this.mColors[i] = colorsList.get(i).intValue();
                this.mOffsets[i] = offsetsList.get(i).floatValue();
            }
        }

        ColorStops(int startColor, int endColor) {
            this.mColors = new int[]{startColor, endColor};
            this.mOffsets = new float[]{0.0f, 1.0f};
        }

        ColorStops(int startColor, int centerColor, int endColor) {
            this.mColors = new int[]{startColor, centerColor, endColor};
            this.mOffsets = new float[]{0.0f, 0.5f, 1.0f};
        }
    }
}
