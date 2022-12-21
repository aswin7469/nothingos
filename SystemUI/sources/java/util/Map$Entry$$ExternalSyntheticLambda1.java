package java.util;

import java.p026io.Serializable;
import java.util.Map;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Map$Entry$$ExternalSyntheticLambda1 implements Comparator, Serializable {
    public final int compare(Object obj, Object obj2) {
        return ((Comparable) ((Map.Entry) obj).getValue()).compareTo(((Map.Entry) obj2).getValue());
    }
}
