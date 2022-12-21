package java.time.chrono;

import java.p026io.Serializable;
import java.util.Comparator;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ChronoLocalDate$$ExternalSyntheticLambda0 implements Comparator, Serializable {
    public final int compare(Object obj, Object obj2) {
        return Long.compare(((ChronoLocalDate) obj).toEpochDay(), ((ChronoLocalDate) obj2).toEpochDay());
    }
}
