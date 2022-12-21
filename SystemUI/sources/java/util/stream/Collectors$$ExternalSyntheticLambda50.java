package java.util.stream;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda50 implements Function {
    public final Object apply(Object obj) {
        return Map.ofEntries((Map.Entry[]) ((HashMap) obj).entrySet().toArray(new Map.Entry[0]));
    }
}
