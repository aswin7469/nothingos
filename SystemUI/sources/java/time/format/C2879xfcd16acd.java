package java.time.format;

import java.time.chrono.Chronology;
import java.util.function.Supplier;

/* renamed from: java.time.format.DateTimeFormatterBuilder$ChronoPrinterParser$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2879xfcd16acd implements Supplier {
    public final /* synthetic */ Chronology f$0;

    public /* synthetic */ C2879xfcd16acd(Chronology chronology) {
        this.f$0 = chronology;
    }

    public final Object get() {
        return this.f$0.getId();
    }
}
