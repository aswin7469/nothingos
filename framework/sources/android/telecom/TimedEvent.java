package android.telecom;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes3.dex */
public abstract class TimedEvent<T> {
    /* renamed from: getKey */
    public abstract T mo2411getKey();

    public abstract long getTime();

    public static <T> Map<T, Double> averageTimings(Collection<? extends TimedEvent<T>> events) {
        HashMap<T, Integer> counts = new HashMap<>();
        HashMap<T, Double> result = new HashMap<>();
        for (TimedEvent<T> entry : events) {
            if (counts.containsKey(entry.mo2411getKey())) {
                counts.put(entry.mo2411getKey(), Integer.valueOf(counts.get(entry.mo2411getKey()).intValue() + 1));
                result.put(entry.mo2411getKey(), Double.valueOf(result.get(entry.mo2411getKey()).doubleValue() + entry.getTime()));
            } else {
                counts.put(entry.mo2411getKey(), 1);
                result.put(entry.mo2411getKey(), Double.valueOf(entry.getTime()));
            }
        }
        for (Map.Entry<T, Double> entry2 : result.entrySet()) {
            result.put(entry2.getKey(), Double.valueOf(entry2.getValue().doubleValue() / counts.get(entry2.getKey()).intValue()));
        }
        return result;
    }
}
