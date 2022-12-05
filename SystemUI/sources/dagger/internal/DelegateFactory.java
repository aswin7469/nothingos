package dagger.internal;

import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class DelegateFactory<T> implements Factory<T> {
    private Provider<T> delegate;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public T mo1933get() {
        Provider<T> provider = this.delegate;
        if (provider == null) {
            throw new IllegalStateException();
        }
        return provider.mo1933get();
    }

    public static <T> void setDelegate(Provider<T> provider, Provider<T> provider2) {
        Preconditions.checkNotNull(provider2);
        DelegateFactory delegateFactory = (DelegateFactory) provider;
        if (delegateFactory.delegate != null) {
            throw new IllegalStateException();
        }
        delegateFactory.delegate = provider2;
    }
}
