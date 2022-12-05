package com.airbnb.lottie.parser;

import com.airbnb.lottie.parser.moshi.JsonReader;
import java.io.IOException;
/* loaded from: classes.dex */
interface ValueParser<V> {
    /* renamed from: parse */
    V mo189parse(JsonReader jsonReader, float f) throws IOException;
}
