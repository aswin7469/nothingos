package com.airbnb.lottie.parser;

import com.airbnb.lottie.parser.moshi.JsonReader;
import java.p026io.IOException;

interface ValueParser<V> {
    V parse(JsonReader jsonReader, float f) throws IOException;
}
