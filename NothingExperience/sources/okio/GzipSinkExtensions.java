package okio;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo14006bv = {1, 0, 3}, mo14007d1 = {"\u0000\f\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\b¨\u0006\u0003"}, mo14008d2 = {"gzip", "Lokio/GzipSink;", "Lokio/Sink;", "okio"}, mo14009k = 2, mo14010mv = {1, 4, 0})
/* renamed from: okio.-GzipSinkExtensions  reason: invalid class name */
/* compiled from: GzipSink.kt */
public final class GzipSinkExtensions {
    public static final GzipSink gzip(Sink sink) {
        Intrinsics.checkNotNullParameter(sink, "$this$gzip");
        return new GzipSink(sink);
    }
}
