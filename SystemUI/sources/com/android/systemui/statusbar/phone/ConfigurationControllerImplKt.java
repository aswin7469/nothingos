package com.android.systemui.statusbar.phone;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import sun.security.util.SecurityConstants;

@Metadata(mo64986d1 = {"\u0000\u001a\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u001e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\u001aD\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00032\u0012\u0010\u0004\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00060\u00052\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u00020\u00010\u0005H\bø\u0001\u0000\u0002\u0007\n\u0005\b20\u0001¨\u0006\b"}, mo64987d2 = {"filterForEach", "", "T", "", "f", "Lkotlin/Function1;", "", "execute", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ConfigurationControllerImpl.kt */
public final class ConfigurationControllerImplKt {
    public static final <T> void filterForEach(Collection<? extends T> collection, Function1<? super T, Boolean> function1, Function1<? super T, Unit> function12) {
        Intrinsics.checkNotNullParameter(collection, "<this>");
        Intrinsics.checkNotNullParameter(function1, "f");
        Intrinsics.checkNotNullParameter(function12, SecurityConstants.FILE_EXECUTE_ACTION);
        for (Object next : collection) {
            if (function1.invoke(next).booleanValue()) {
                function12.invoke(next);
            }
        }
    }
}
