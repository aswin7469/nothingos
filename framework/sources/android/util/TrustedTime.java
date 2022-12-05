package android.util;
/* loaded from: classes3.dex */
public interface TrustedTime {
    @Deprecated
    long currentTimeMillis();

    @Deprecated
    boolean forceRefresh();

    boolean forceSync();

    @Deprecated
    long getCacheAge();

    @Deprecated
    boolean hasCache();
}
