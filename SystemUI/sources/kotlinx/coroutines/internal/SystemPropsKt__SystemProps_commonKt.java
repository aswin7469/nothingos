package kotlinx.coroutines.internal;

import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringNumberConversionsKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: SystemProps.common.kt */
/* loaded from: classes2.dex */
public final /* synthetic */ class SystemPropsKt__SystemProps_commonKt {
    public static final boolean systemProp(@NotNull String propertyName, boolean z) {
        Intrinsics.checkParameterIsNotNull(propertyName, "propertyName");
        String systemProp = SystemPropsKt.systemProp(propertyName);
        return systemProp != null ? Boolean.parseBoolean(systemProp) : z;
    }

    public static /* synthetic */ int systemProp$default(String str, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 4) != 0) {
            i2 = 1;
        }
        if ((i4 & 8) != 0) {
            i3 = Integer.MAX_VALUE;
        }
        return SystemPropsKt.systemProp(str, i, i2, i3);
    }

    public static final int systemProp(@NotNull String propertyName, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(propertyName, "propertyName");
        return (int) SystemPropsKt.systemProp(propertyName, i, i2, i3);
    }

    public static /* synthetic */ long systemProp$default(String str, long j, long j2, long j3, int i, Object obj) {
        if ((i & 4) != 0) {
            j2 = 1;
        }
        long j4 = j2;
        if ((i & 8) != 0) {
            j3 = Long.MAX_VALUE;
        }
        return SystemPropsKt.systemProp(str, j, j4, j3);
    }

    public static final long systemProp(@NotNull String propertyName, long j, long j2, long j3) {
        Long longOrNull;
        Intrinsics.checkParameterIsNotNull(propertyName, "propertyName");
        String systemProp = SystemPropsKt.systemProp(propertyName);
        if (systemProp != null) {
            longOrNull = StringsKt__StringNumberConversionsKt.toLongOrNull(systemProp);
            if (longOrNull == null) {
                throw new IllegalStateException(("System property '" + propertyName + "' has unrecognized value '" + systemProp + '\'').toString());
            }
            long longValue = longOrNull.longValue();
            if (j2 <= longValue && j3 >= longValue) {
                return longValue;
            }
            throw new IllegalStateException(("System property '" + propertyName + "' should be in range " + j2 + ".." + j3 + ", but is '" + longValue + '\'').toString());
        }
        return j;
    }
}
