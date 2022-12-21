package java.time.zone;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class ZoneRulesProvider {
    private static final CopyOnWriteArrayList<ZoneRulesProvider> PROVIDERS = new CopyOnWriteArrayList<>();
    private static final ConcurrentMap<String, ZoneRulesProvider> ZONES = new ConcurrentHashMap(512, 0.75f, 2);
    private static volatile Set<String> ZONE_IDS;

    /* access modifiers changed from: protected */
    public boolean provideRefresh() {
        return false;
    }

    /* access modifiers changed from: protected */
    public abstract ZoneRules provideRules(String str, boolean z);

    /* access modifiers changed from: protected */
    public abstract NavigableMap<String, ZoneRules> provideVersions(String str);

    /* access modifiers changed from: protected */
    public abstract Set<String> provideZoneIds();

    static {
        registerProvider(new IcuZoneRulesProvider());
    }

    public static Set<String> getAvailableZoneIds() {
        return ZONE_IDS;
    }

    public static ZoneRules getRules(String str, boolean z) {
        Objects.requireNonNull(str, "zoneId");
        return getProvider(str).provideRules(str, z);
    }

    public static NavigableMap<String, ZoneRules> getVersions(String str) {
        Objects.requireNonNull(str, "zoneId");
        return getProvider(str).provideVersions(str);
    }

    private static ZoneRulesProvider getProvider(String str) {
        ConcurrentMap<String, ZoneRulesProvider> concurrentMap = ZONES;
        ZoneRulesProvider zoneRulesProvider = concurrentMap.get(str);
        if (zoneRulesProvider != null) {
            return zoneRulesProvider;
        }
        if (concurrentMap.isEmpty()) {
            throw new ZoneRulesException("No time-zone data files registered");
        }
        throw new ZoneRulesException("Unknown time-zone ID: " + str);
    }

    public static void registerProvider(ZoneRulesProvider zoneRulesProvider) {
        Objects.requireNonNull(zoneRulesProvider, "provider");
        registerProvider0(zoneRulesProvider);
        PROVIDERS.add(zoneRulesProvider);
    }

    private static synchronized void registerProvider0(ZoneRulesProvider zoneRulesProvider) {
        synchronized (ZoneRulesProvider.class) {
            for (String next : zoneRulesProvider.provideZoneIds()) {
                Objects.requireNonNull(next, "zoneId");
                if (ZONES.putIfAbsent(next, zoneRulesProvider) != null) {
                    throw new ZoneRulesException("Unable to register zone as one already registered with that ID: " + next + ", currently loading from provider: " + zoneRulesProvider);
                }
            }
            ZONE_IDS = Collections.unmodifiableSet(new HashSet(ZONES.keySet()));
        }
    }

    public static boolean refresh() {
        Iterator<ZoneRulesProvider> it = PROVIDERS.iterator();
        boolean z = false;
        while (it.hasNext()) {
            z |= it.next().provideRefresh();
        }
        return z;
    }

    protected ZoneRulesProvider() {
    }
}
