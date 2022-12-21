package java.util;

import java.p026io.Serializable;
import java.util.Map;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class Map$Entry$$ExternalSyntheticLambda3 implements Comparator, Serializable {
    public final /* synthetic */ Comparator f$0;

    public /* synthetic */ Map$Entry$$ExternalSyntheticLambda3(Comparator comparator) {
        this.f$0 = comparator;
    }

    public final int compare(Object obj, Object obj2) {
        return this.f$0.compare(((Map.Entry) obj).getValue(), ((Map.Entry) obj2).getValue());
    }
}
