package java.time.format;

import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatterBuilder;
import java.util.function.Consumer;

/* renamed from: java.time.format.DateTimeFormatterBuilder$ReducedPrinterParser$$ExternalSyntheticLambda0 */
/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class C2880xdc213142 implements Consumer {
    public final /* synthetic */ DateTimeFormatterBuilder.ReducedPrinterParser f$0;
    public final /* synthetic */ DateTimeParseContext f$1;
    public final /* synthetic */ long f$2;
    public final /* synthetic */ int f$3;
    public final /* synthetic */ int f$4;

    public /* synthetic */ C2880xdc213142(DateTimeFormatterBuilder.ReducedPrinterParser reducedPrinterParser, DateTimeParseContext dateTimeParseContext, long j, int i, int i2) {
        this.f$0 = reducedPrinterParser;
        this.f$1 = dateTimeParseContext;
        this.f$2 = j;
        this.f$3 = i;
        this.f$4 = i2;
    }

    public final void accept(Object obj) {
        this.f$0.mo43507xdf3a601e(this.f$1, this.f$2, this.f$3, this.f$4, (Chronology) obj);
    }
}
