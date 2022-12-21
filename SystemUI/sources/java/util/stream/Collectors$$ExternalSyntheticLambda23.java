package java.util.stream;

import java.util.Map;
import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Collectors$$ExternalSyntheticLambda23 implements Function {
    public final Object apply(Object obj) {
        return Map.ofEntries((Map.Entry[]) ((Map) obj).entrySet().toArray(new Map.Entry[0]));
    }
}
