package libcore.util;

import java.lang.reflect.Field;
import libcore.util.ZoneInfo;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class ZoneInfo$$ExternalSyntheticLambda2 implements ZoneInfo.FieldSetter {
    public final /* synthetic */ ZoneInfo f$0;
    public final /* synthetic */ long[] f$1;

    public /* synthetic */ ZoneInfo$$ExternalSyntheticLambda2(ZoneInfo zoneInfo, long[] jArr) {
        this.f$0 = zoneInfo;
        this.f$1 = jArr;
    }

    public final void set(Field field) {
        this.f$0.m5520lambda$readObject$2$libcoreutilZoneInfo(this.f$1, field);
    }
}
