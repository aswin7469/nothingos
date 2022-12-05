package dagger.internal;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.inject.Provider;
/* loaded from: classes2.dex */
abstract class AbstractMapFactory<K, V, V2> implements Factory<Map<K, V2>> {
    private final Map<K, Provider<V>> contributingMap;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractMapFactory(Map<K, Provider<V>> map) {
        this.contributingMap = Collections.unmodifiableMap(map);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Map<K, Provider<V>> contributingMap() {
        return this.contributingMap;
    }

    /* loaded from: classes2.dex */
    public static abstract class Builder<K, V, V2> {
        final LinkedHashMap<K, Provider<V>> map;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Builder(int i) {
            this.map = DaggerCollections.newLinkedHashMapWithExpectedSize(i);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* JADX WARN: Multi-variable type inference failed */
        public Builder<K, V, V2> put(K k, Provider<V> provider) {
            this.map.put(Preconditions.checkNotNull(k, "key"), (Provider) Preconditions.checkNotNull(provider, "provider"));
            return this;
        }
    }
}
