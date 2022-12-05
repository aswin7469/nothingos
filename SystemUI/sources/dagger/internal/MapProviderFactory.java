package dagger.internal;

import dagger.Lazy;
import dagger.internal.AbstractMapFactory;
import java.util.Map;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class MapProviderFactory<K, V> extends AbstractMapFactory<K, V, Provider<V>> implements Lazy<Map<K, Provider<V>>> {
    public static <K, V> Builder<K, V> builder(int i) {
        return new Builder<>(i);
    }

    private MapProviderFactory(Map<K, Provider<V>> map) {
        super(map);
    }

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public Map<K, Provider<V>> mo1933get() {
        return contributingMap();
    }

    /* loaded from: classes2.dex */
    public static final class Builder<K, V> extends AbstractMapFactory.Builder<K, V, Provider<V>> {
        private Builder(int i) {
            super(i);
        }

        @Override // dagger.internal.AbstractMapFactory.Builder
        public Builder<K, V> put(K k, Provider<V> provider) {
            super.put((Builder<K, V>) k, (Provider) provider);
            return this;
        }

        public MapProviderFactory<K, V> build() {
            return new MapProviderFactory<>(this.map);
        }
    }
}
