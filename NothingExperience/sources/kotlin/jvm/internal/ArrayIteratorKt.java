package kotlin.jvm.internal;

import java.util.Iterator;
import kotlin.Metadata;

@Metadata(mo14007d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010(\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\b\u0002\u001a%\u0010\u0000\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\u0004\b\u0000\u0010\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0004¢\u0006\u0002\u0010\u0005¨\u0006\u0006"}, mo14008d2 = {"iterator", "", "T", "array", "", "([Ljava/lang/Object;)Ljava/util/Iterator;", "kotlin-stdlib"}, mo14009k = 2, mo14010mv = {1, 6, 0}, mo14012xi = 48)
/* compiled from: ArrayIterator.kt */
public final class ArrayIteratorKt {
    public static final <T> Iterator<T> iterator(T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "array");
        return new ArrayIterator<>(tArr);
    }
}
