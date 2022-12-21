package com.airbnb.lottie.parser;

import android.icu.text.DateFormat;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.content.ShapeStroke;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.airbnb.lottie.value.Keyframe;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.Collections;

class ShapeStrokeParser {
    private static final JsonReader.Options DASH_PATTERN_NAMES = JsonReader.Options.m137of("n", DateFormat.ABBR_GENERIC_TZ);
    private static JsonReader.Options NAMES = JsonReader.Options.m137of("nm", "c", "w", "o", "lc", "lj", "ml", "hd", DateFormat.DAY);

    private ShapeStrokeParser() {
    }

    static ShapeStroke parse(JsonReader jsonReader, LottieComposition lottieComposition) throws IOException {
        JsonReader jsonReader2 = jsonReader;
        ArrayList arrayList = new ArrayList();
        float f = 0.0f;
        boolean z = false;
        String str = null;
        AnimatableFloatValue animatableFloatValue = null;
        AnimatableColorValue animatableColorValue = null;
        AnimatableFloatValue animatableFloatValue2 = null;
        ShapeStroke.LineCapType lineCapType = null;
        ShapeStroke.LineJoinType lineJoinType = null;
        AnimatableIntegerValue animatableIntegerValue = null;
        while (jsonReader.hasNext()) {
            switch (jsonReader2.selectName(NAMES)) {
                case 0:
                    LottieComposition lottieComposition2 = lottieComposition;
                    str = jsonReader.nextString();
                    break;
                case 1:
                    LottieComposition lottieComposition3 = lottieComposition;
                    animatableColorValue = AnimatableValueParser.parseColor(jsonReader, lottieComposition);
                    break;
                case 2:
                    LottieComposition lottieComposition4 = lottieComposition;
                    animatableFloatValue2 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                    break;
                case 3:
                    LottieComposition lottieComposition5 = lottieComposition;
                    animatableIntegerValue = AnimatableValueParser.parseInteger(jsonReader, lottieComposition);
                    break;
                case 4:
                    LottieComposition lottieComposition6 = lottieComposition;
                    lineCapType = ShapeStroke.LineCapType.values()[jsonReader.nextInt() - 1];
                    break;
                case 5:
                    LottieComposition lottieComposition7 = lottieComposition;
                    lineJoinType = ShapeStroke.LineJoinType.values()[jsonReader.nextInt() - 1];
                    break;
                case 6:
                    LottieComposition lottieComposition8 = lottieComposition;
                    f = (float) jsonReader.nextDouble();
                    break;
                case 7:
                    LottieComposition lottieComposition9 = lottieComposition;
                    z = jsonReader.nextBoolean();
                    break;
                case 8:
                    jsonReader.beginArray();
                    while (jsonReader.hasNext()) {
                        jsonReader.beginObject();
                        String str2 = null;
                        AnimatableFloatValue animatableFloatValue3 = null;
                        while (jsonReader.hasNext()) {
                            int selectName = jsonReader2.selectName(DASH_PATTERN_NAMES);
                            if (selectName == 0) {
                                str2 = jsonReader.nextString();
                            } else if (selectName != 1) {
                                jsonReader.skipName();
                                jsonReader.skipValue();
                            } else {
                                animatableFloatValue3 = AnimatableValueParser.parseFloat(jsonReader, lottieComposition);
                            }
                        }
                        jsonReader.endObject();
                        str2.hashCode();
                        char c = 65535;
                        switch (str2.hashCode()) {
                            case 100:
                                if (str2.equals(DateFormat.DAY)) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case 103:
                                if (str2.equals("g")) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case 111:
                                if (str2.equals("o")) {
                                    c = 2;
                                    break;
                                }
                                break;
                        }
                        switch (c) {
                            case 0:
                            case 1:
                                lottieComposition.setHasDashPattern(true);
                                arrayList.add(animatableFloatValue3);
                                break;
                            case 2:
                                LottieComposition lottieComposition10 = lottieComposition;
                                animatableFloatValue = animatableFloatValue3;
                                break;
                            default:
                                LottieComposition lottieComposition11 = lottieComposition;
                                break;
                        }
                    }
                    LottieComposition lottieComposition12 = lottieComposition;
                    jsonReader.endArray();
                    if (arrayList.size() != 1) {
                        break;
                    } else {
                        arrayList.add(arrayList.get(0));
                        break;
                    }
                default:
                    LottieComposition lottieComposition13 = lottieComposition;
                    jsonReader.skipValue();
                    break;
            }
        }
        if (animatableIntegerValue == null) {
            animatableIntegerValue = new AnimatableIntegerValue(Collections.singletonList(new Keyframe(100)));
        }
        return new ShapeStroke(str, animatableFloatValue, arrayList, animatableColorValue, animatableIntegerValue, animatableFloatValue2, lineCapType, lineJoinType, f, z);
    }
}
