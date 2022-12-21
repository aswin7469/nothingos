package java.time.zone;

import android.icu.util.TimeZone;
import com.android.icu.util.ExtendedTimeZone;
import java.util.Collections;
import java.util.HashSet;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import libcore.util.BasicLruCache;

public class IcuZoneRulesProvider extends ZoneRulesProvider {
    private final BasicLruCache<String, ZoneRules> cache = new ZoneRulesCache(8);

    /* access modifiers changed from: protected */
    public Set<String> provideZoneIds() {
        HashSet hashSet = new HashSet(TimeZone.getAvailableIDs(TimeZone.SystemTimeZoneType.ANY, (String) null, (Integer) null));
        hashSet.remove("GMT+0");
        hashSet.remove("GMT-0");
        return hashSet;
    }

    /* access modifiers changed from: protected */
    public ZoneRules provideRules(String str, boolean z) {
        return this.cache.get(str);
    }

    /* access modifiers changed from: protected */
    public NavigableMap<String, ZoneRules> provideVersions(String str) {
        return new TreeMap(Collections.singletonMap(TimeZone.getTZDataVersion(), provideRules(str, false)));
    }

    static ZoneRules generateZoneRules(String str) {
        return ExtendedTimeZone.getInstance(str).createZoneRules();
    }

    private static class ZoneRulesCache extends BasicLruCache<String, ZoneRules> {
        ZoneRulesCache(int i) {
            super(i);
        }

        /* access modifiers changed from: protected */
        public ZoneRules create(String str) {
            String canonicalID = TimeZone.getCanonicalID(str);
            if (!canonicalID.equals(str)) {
                return (ZoneRules) get(canonicalID);
            }
            return IcuZoneRulesProvider.generateZoneRules(str);
        }
    }
}
