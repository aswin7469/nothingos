package com.airbnb.lottie.parser;

import android.icu.text.DateFormat;
import com.airbnb.lottie.parser.moshi.JsonReader;

class ContentModelParser {
    private static JsonReader.Options NAMES = JsonReader.Options.m137of("ty", DateFormat.DAY);

    private ContentModelParser() {
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00bc, code lost:
        if (r2.equals("gf") == false) goto L_0x0037;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static com.airbnb.lottie.model.content.ContentModel parse(com.airbnb.lottie.parser.moshi.JsonReader r7, com.airbnb.lottie.LottieComposition r8) throws java.p026io.IOException {
        /*
            r7.beginObject()
            r0 = 2
            r1 = r0
        L_0x0005:
            boolean r2 = r7.hasNext()
            r3 = 1
            r4 = 0
            if (r2 == 0) goto L_0x0028
            com.airbnb.lottie.parser.moshi.JsonReader$Options r2 = NAMES
            int r2 = r7.selectName(r2)
            if (r2 == 0) goto L_0x0023
            if (r2 == r3) goto L_0x001e
            r7.skipName()
            r7.skipValue()
            goto L_0x0005
        L_0x001e:
            int r1 = r7.nextInt()
            goto L_0x0005
        L_0x0023:
            java.lang.String r2 = r7.nextString()
            goto L_0x0029
        L_0x0028:
            r2 = r4
        L_0x0029:
            if (r2 != 0) goto L_0x002c
            return r4
        L_0x002c:
            r2.hashCode()
            int r5 = r2.hashCode()
            r6 = -1
            switch(r5) {
                case 3239: goto L_0x00cc;
                case 3270: goto L_0x00c0;
                case 3295: goto L_0x00b6;
                case 3307: goto L_0x00ab;
                case 3308: goto L_0x00a0;
                case 3488: goto L_0x0095;
                case 3633: goto L_0x008a;
                case 3646: goto L_0x007f;
                case 3669: goto L_0x0072;
                case 3679: goto L_0x0064;
                case 3681: goto L_0x0056;
                case 3705: goto L_0x0048;
                case 3710: goto L_0x003a;
                default: goto L_0x0037;
            }
        L_0x0037:
            r0 = r6
            goto L_0x00d7
        L_0x003a:
            java.lang.String r0 = "tr"
            boolean r0 = r2.equals(r0)
            if (r0 != 0) goto L_0x0044
            goto L_0x0037
        L_0x0044:
            r0 = 12
            goto L_0x00d7
        L_0x0048:
            java.lang.String r0 = "tm"
            boolean r0 = r2.equals(r0)
            if (r0 != 0) goto L_0x0052
            goto L_0x0037
        L_0x0052:
            r0 = 11
            goto L_0x00d7
        L_0x0056:
            java.lang.String r0 = "st"
            boolean r0 = r2.equals(r0)
            if (r0 != 0) goto L_0x0060
            goto L_0x0037
        L_0x0060:
            r0 = 10
            goto L_0x00d7
        L_0x0064:
            java.lang.String r0 = "sr"
            boolean r0 = r2.equals(r0)
            if (r0 != 0) goto L_0x006e
            goto L_0x0037
        L_0x006e:
            r0 = 9
            goto L_0x00d7
        L_0x0072:
            java.lang.String r0 = "sh"
            boolean r0 = r2.equals(r0)
            if (r0 != 0) goto L_0x007c
            goto L_0x0037
        L_0x007c:
            r0 = 8
            goto L_0x00d7
        L_0x007f:
            java.lang.String r0 = "rp"
            boolean r0 = r2.equals(r0)
            if (r0 != 0) goto L_0x0088
            goto L_0x0037
        L_0x0088:
            r0 = 7
            goto L_0x00d7
        L_0x008a:
            java.lang.String r0 = "rc"
            boolean r0 = r2.equals(r0)
            if (r0 != 0) goto L_0x0093
            goto L_0x0037
        L_0x0093:
            r0 = 6
            goto L_0x00d7
        L_0x0095:
            java.lang.String r0 = "mm"
            boolean r0 = r2.equals(r0)
            if (r0 != 0) goto L_0x009e
            goto L_0x0037
        L_0x009e:
            r0 = 5
            goto L_0x00d7
        L_0x00a0:
            java.lang.String r0 = "gs"
            boolean r0 = r2.equals(r0)
            if (r0 != 0) goto L_0x00a9
            goto L_0x0037
        L_0x00a9:
            r0 = 4
            goto L_0x00d7
        L_0x00ab:
            java.lang.String r0 = "gr"
            boolean r0 = r2.equals(r0)
            if (r0 != 0) goto L_0x00b4
            goto L_0x0037
        L_0x00b4:
            r0 = 3
            goto L_0x00d7
        L_0x00b6:
            java.lang.String r3 = "gf"
            boolean r3 = r2.equals(r3)
            if (r3 != 0) goto L_0x00d7
            goto L_0x0037
        L_0x00c0:
            java.lang.String r0 = "fl"
            boolean r0 = r2.equals(r0)
            if (r0 != 0) goto L_0x00ca
            goto L_0x0037
        L_0x00ca:
            r0 = r3
            goto L_0x00d7
        L_0x00cc:
            java.lang.String r0 = "el"
            boolean r0 = r2.equals(r0)
            if (r0 != 0) goto L_0x00d6
            goto L_0x0037
        L_0x00d6:
            r0 = 0
        L_0x00d7:
            switch(r0) {
                case 0: goto L_0x012e;
                case 1: goto L_0x0129;
                case 2: goto L_0x0124;
                case 3: goto L_0x011f;
                case 4: goto L_0x011a;
                case 5: goto L_0x0110;
                case 6: goto L_0x010b;
                case 7: goto L_0x0106;
                case 8: goto L_0x0101;
                case 9: goto L_0x00fc;
                case 10: goto L_0x00f7;
                case 11: goto L_0x00f2;
                case 12: goto L_0x00ed;
                default: goto L_0x00da;
            }
        L_0x00da:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r0 = "Unknown shape type "
            r8.<init>((java.lang.String) r0)
            java.lang.StringBuilder r8 = r8.append((java.lang.String) r2)
            java.lang.String r8 = r8.toString()
            com.airbnb.lottie.utils.Logger.warning(r8)
            goto L_0x0132
        L_0x00ed:
            com.airbnb.lottie.model.animatable.AnimatableTransform r4 = com.airbnb.lottie.parser.AnimatableTransformParser.parse(r7, r8)
            goto L_0x0132
        L_0x00f2:
            com.airbnb.lottie.model.content.ShapeTrimPath r4 = com.airbnb.lottie.parser.ShapeTrimPathParser.parse(r7, r8)
            goto L_0x0132
        L_0x00f7:
            com.airbnb.lottie.model.content.ShapeStroke r4 = com.airbnb.lottie.parser.ShapeStrokeParser.parse(r7, r8)
            goto L_0x0132
        L_0x00fc:
            com.airbnb.lottie.model.content.PolystarShape r4 = com.airbnb.lottie.parser.PolystarShapeParser.parse(r7, r8)
            goto L_0x0132
        L_0x0101:
            com.airbnb.lottie.model.content.ShapePath r4 = com.airbnb.lottie.parser.ShapePathParser.parse(r7, r8)
            goto L_0x0132
        L_0x0106:
            com.airbnb.lottie.model.content.Repeater r4 = com.airbnb.lottie.parser.RepeaterParser.parse(r7, r8)
            goto L_0x0132
        L_0x010b:
            com.airbnb.lottie.model.content.RectangleShape r4 = com.airbnb.lottie.parser.RectangleShapeParser.parse(r7, r8)
            goto L_0x0132
        L_0x0110:
            com.airbnb.lottie.model.content.MergePaths r4 = com.airbnb.lottie.parser.MergePathsParser.parse(r7)
            java.lang.String r0 = "Animation contains merge paths. Merge paths are only supported on KitKat+ and must be manually enabled by calling enableMergePathsForKitKatAndAbove()."
            r8.addWarning(r0)
            goto L_0x0132
        L_0x011a:
            com.airbnb.lottie.model.content.GradientStroke r4 = com.airbnb.lottie.parser.GradientStrokeParser.parse(r7, r8)
            goto L_0x0132
        L_0x011f:
            com.airbnb.lottie.model.content.ShapeGroup r4 = com.airbnb.lottie.parser.ShapeGroupParser.parse(r7, r8)
            goto L_0x0132
        L_0x0124:
            com.airbnb.lottie.model.content.GradientFill r4 = com.airbnb.lottie.parser.GradientFillParser.parse(r7, r8)
            goto L_0x0132
        L_0x0129:
            com.airbnb.lottie.model.content.ShapeFill r4 = com.airbnb.lottie.parser.ShapeFillParser.parse(r7, r8)
            goto L_0x0132
        L_0x012e:
            com.airbnb.lottie.model.content.CircleShape r4 = com.airbnb.lottie.parser.CircleShapeParser.parse(r7, r8, r1)
        L_0x0132:
            boolean r8 = r7.hasNext()
            if (r8 == 0) goto L_0x013c
            r7.skipValue()
            goto L_0x0132
        L_0x013c:
            r7.endObject()
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.ContentModelParser.parse(com.airbnb.lottie.parser.moshi.JsonReader, com.airbnb.lottie.LottieComposition):com.airbnb.lottie.model.content.ContentModel");
    }
}
