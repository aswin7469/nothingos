package android.compat;

import android.annotation.SystemApi;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class Compatibility {
    private static final BehaviorChangeDelegate DEFAULT_CALLBACKS;
    private static volatile BehaviorChangeDelegate sCallbacks;

    private Compatibility() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void reportUnconditionalChange(long j) {
        sCallbacks.onChangeReported(j);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static boolean isChangeEnabled(long j) {
        return sCallbacks.isChangeEnabled(j);
    }

    static {
        C00041 r0 = new BehaviorChangeDelegate() {
        };
        DEFAULT_CALLBACKS = r0;
        sCallbacks = r0;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setBehaviorChangeDelegate(BehaviorChangeDelegate behaviorChangeDelegate) {
        sCallbacks = (BehaviorChangeDelegate) Objects.requireNonNull(behaviorChangeDelegate);
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void clearBehaviorChangeDelegate() {
        sCallbacks = DEFAULT_CALLBACKS;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void setOverrides(ChangeConfig changeConfig) {
        if (!(sCallbacks instanceof OverrideCallbacks)) {
            sCallbacks = new OverrideCallbacks(sCallbacks, changeConfig);
            return;
        }
        throw new IllegalStateException("setOverrides has already been called!");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static void clearOverrides() {
        if (sCallbacks instanceof OverrideCallbacks) {
            sCallbacks = ((OverrideCallbacks) sCallbacks).delegate;
            return;
        }
        throw new IllegalStateException("No overrides set");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public interface BehaviorChangeDelegate {
        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        void onChangeReported(long j) {
            System.logW("No Compatibility callbacks set! Reporting change " + j);
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        boolean isChangeEnabled(long j) {
            System.logW("No Compatibility callbacks set! Querying change " + j);
            return true;
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final class ChangeConfig {
        private final Set<Long> disabled;
        private final Set<Long> enabled;

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public ChangeConfig(Set<Long> set, Set<Long> set2) {
            this.enabled = (Set) Objects.requireNonNull(set);
            this.disabled = (Set) Objects.requireNonNull(set2);
            if (set.contains((Object) null)) {
                throw null;
            } else if (!set2.contains((Object) null)) {
                HashSet hashSet = new HashSet(set);
                hashSet.retainAll(set2);
                if (!hashSet.isEmpty()) {
                    throw new IllegalArgumentException("Cannot have changes " + hashSet + " enabled and disabled!");
                }
            } else {
                throw null;
            }
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public boolean isEmpty() {
            return this.enabled.isEmpty() && this.disabled.isEmpty();
        }

        private static long[] toLongArray(Set<Long> set) {
            long[] jArr = new long[set.size()];
            int i = 0;
            for (Long longValue : set) {
                jArr[i] = longValue.longValue();
                i++;
            }
            return jArr;
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public long[] getEnabledChangesArray() {
            return toLongArray(this.enabled);
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public long[] getDisabledChangesArray() {
            return toLongArray(this.disabled);
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public Set<Long> getEnabledSet() {
            return Collections.unmodifiableSet(this.enabled);
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public Set<Long> getDisabledSet() {
            return Collections.unmodifiableSet(this.disabled);
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public boolean isForceEnabled(long j) {
            return this.enabled.contains(Long.valueOf(j));
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public boolean isForceDisabled(long j) {
            return this.disabled.contains(Long.valueOf(j));
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ChangeConfig)) {
                return false;
            }
            ChangeConfig changeConfig = (ChangeConfig) obj;
            if (!this.enabled.equals(changeConfig.enabled) || !this.disabled.equals(changeConfig.disabled)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return Objects.hash(this.enabled, this.disabled);
        }

        public String toString() {
            return "ChangeConfig{enabled=" + this.enabled + ", disabled=" + this.disabled + '}';
        }
    }

    private static class OverrideCallbacks implements BehaviorChangeDelegate {
        private final ChangeConfig changeConfig;
        /* access modifiers changed from: private */
        public final BehaviorChangeDelegate delegate;

        private OverrideCallbacks(BehaviorChangeDelegate behaviorChangeDelegate, ChangeConfig changeConfig2) {
            this.delegate = (BehaviorChangeDelegate) Objects.requireNonNull(behaviorChangeDelegate);
            this.changeConfig = (ChangeConfig) Objects.requireNonNull(changeConfig2);
        }

        public boolean isChangeEnabled(long j) {
            if (this.changeConfig.isForceEnabled(j)) {
                return true;
            }
            if (this.changeConfig.isForceDisabled(j)) {
                return false;
            }
            return this.delegate.isChangeEnabled(j);
        }
    }
}
