package com.airbnb.lottie.parser;

class MaskParser {
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x006c, code lost:
        if (r1.equals("s") == false) goto L_0x0063;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static com.airbnb.lottie.model.content.Mask parse(com.airbnb.lottie.parser.moshi.JsonReader r11, com.airbnb.lottie.LottieComposition r12) throws java.io.IOException {
        /*
            r11.beginObject()
            r0 = 0
            r1 = 0
            r4 = r0
            r2 = r1
            r3 = r2
        L_0x0008:
            boolean r5 = r11.hasNext()
            if (r5 == 0) goto L_0x00d6
            java.lang.String r5 = r11.nextName()
            r5.hashCode()
            int r6 = r5.hashCode()
            r7 = 3
            r8 = 2
            r9 = 1
            r10 = -1
            switch(r6) {
                case 111: goto L_0x0044;
                case 3588: goto L_0x0038;
                case 104433: goto L_0x002d;
                case 3357091: goto L_0x0022;
                default: goto L_0x0020;
            }
        L_0x0020:
            r6 = r10
            goto L_0x004e
        L_0x0022:
            java.lang.String r6 = "mode"
            boolean r6 = r5.equals(r6)
            if (r6 != 0) goto L_0x002b
            goto L_0x0020
        L_0x002b:
            r6 = r7
            goto L_0x004e
        L_0x002d:
            java.lang.String r6 = "inv"
            boolean r6 = r5.equals(r6)
            if (r6 != 0) goto L_0x0036
            goto L_0x0020
        L_0x0036:
            r6 = r8
            goto L_0x004e
        L_0x0038:
            java.lang.String r6 = "pt"
            boolean r6 = r5.equals(r6)
            if (r6 != 0) goto L_0x0042
            goto L_0x0020
        L_0x0042:
            r6 = r9
            goto L_0x004e
        L_0x0044:
            java.lang.String r6 = "o"
            boolean r6 = r5.equals(r6)
            if (r6 != 0) goto L_0x004d
            goto L_0x0020
        L_0x004d:
            r6 = r0
        L_0x004e:
            switch(r6) {
                case 0: goto L_0x00d0;
                case 1: goto L_0x00ca;
                case 2: goto L_0x00c4;
                case 3: goto L_0x0055;
                default: goto L_0x0051;
            }
        L_0x0051:
            r11.skipValue()
            goto L_0x0008
        L_0x0055:
            java.lang.String r1 = r11.nextString()
            r1.hashCode()
            int r6 = r1.hashCode()
            switch(r6) {
                case 97: goto L_0x0085;
                case 105: goto L_0x007a;
                case 110: goto L_0x006f;
                case 115: goto L_0x0065;
                default: goto L_0x0063;
            }
        L_0x0063:
            r7 = r10
            goto L_0x008f
        L_0x0065:
            java.lang.String r6 = "s"
            boolean r1 = r1.equals(r6)
            if (r1 != 0) goto L_0x008f
            goto L_0x0063
        L_0x006f:
            java.lang.String r6 = "n"
            boolean r1 = r1.equals(r6)
            if (r1 != 0) goto L_0x0078
            goto L_0x0063
        L_0x0078:
            r7 = r8
            goto L_0x008f
        L_0x007a:
            java.lang.String r6 = "i"
            boolean r1 = r1.equals(r6)
            if (r1 != 0) goto L_0x0083
            goto L_0x0063
        L_0x0083:
            r7 = r9
            goto L_0x008f
        L_0x0085:
            java.lang.String r6 = "a"
            boolean r1 = r1.equals(r6)
            if (r1 != 0) goto L_0x008e
            goto L_0x0063
        L_0x008e:
            r7 = r0
        L_0x008f:
            switch(r7) {
                case 0: goto L_0x00c0;
                case 1: goto L_0x00b7;
                case 2: goto L_0x00b3;
                case 3: goto L_0x00af;
                default: goto L_0x0092;
            }
        L_0x0092:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r6 = "Unknown mask mode "
            r1.append(r6)
            r1.append(r5)
            java.lang.String r5 = ". Defaulting to Add."
            r1.append(r5)
            java.lang.String r1 = r1.toString()
            com.airbnb.lottie.utils.Logger.warning(r1)
            com.airbnb.lottie.model.content.Mask$MaskMode r1 = com.airbnb.lottie.model.content.Mask.MaskMode.MASK_MODE_ADD
            goto L_0x0008
        L_0x00af:
            com.airbnb.lottie.model.content.Mask$MaskMode r1 = com.airbnb.lottie.model.content.Mask.MaskMode.MASK_MODE_SUBTRACT
            goto L_0x0008
        L_0x00b3:
            com.airbnb.lottie.model.content.Mask$MaskMode r1 = com.airbnb.lottie.model.content.Mask.MaskMode.MASK_MODE_NONE
            goto L_0x0008
        L_0x00b7:
            java.lang.String r1 = "Animation contains intersect masks. They are not supported but will be treated like add masks."
            r12.addWarning(r1)
            com.airbnb.lottie.model.content.Mask$MaskMode r1 = com.airbnb.lottie.model.content.Mask.MaskMode.MASK_MODE_INTERSECT
            goto L_0x0008
        L_0x00c0:
            com.airbnb.lottie.model.content.Mask$MaskMode r1 = com.airbnb.lottie.model.content.Mask.MaskMode.MASK_MODE_ADD
            goto L_0x0008
        L_0x00c4:
            boolean r4 = r11.nextBoolean()
            goto L_0x0008
        L_0x00ca:
            com.airbnb.lottie.model.animatable.AnimatableShapeValue r2 = com.airbnb.lottie.parser.AnimatableValueParser.parseShapeData(r11, r12)
            goto L_0x0008
        L_0x00d0:
            com.airbnb.lottie.model.animatable.AnimatableIntegerValue r3 = com.airbnb.lottie.parser.AnimatableValueParser.parseInteger(r11, r12)
            goto L_0x0008
        L_0x00d6:
            r11.endObject()
            com.airbnb.lottie.model.content.Mask r11 = new com.airbnb.lottie.model.content.Mask
            r11.<init>(r1, r2, r3, r4)
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.MaskParser.parse(com.airbnb.lottie.parser.moshi.JsonReader, com.airbnb.lottie.LottieComposition):com.airbnb.lottie.model.content.Mask");
    }
}
