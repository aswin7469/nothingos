package com.android.net.module.util;

import java.util.Collection;

public final class CollectionUtils {
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }
}
