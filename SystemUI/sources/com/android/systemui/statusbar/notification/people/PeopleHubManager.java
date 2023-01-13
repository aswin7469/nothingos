package com.android.systemui.statusbar.notification.people;

import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u000e\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0007J\u0006\u0010\r\u001a\u00020\u000eJ\u0012\u0010\u000f\u001a\u00020\u000b2\n\u0010\u0010\u001a\u00060\u0005j\u0002`\u0006J\u0012\u0010\u0011\u001a\u00020\u00122\n\u0010\u0010\u001a\u00060\u0005j\u0002`\u0006R\u001e\u0010\u0003\u001a\u0012\u0012\b\u0012\u00060\u0005j\u0002`\u0006\u0012\u0004\u0012\u00020\u00070\u0004X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\tX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0013"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleHubManager;", "", "()V", "activePeople", "", "", "Lcom/android/systemui/statusbar/notification/people/PersonKey;", "Lcom/android/systemui/statusbar/notification/people/PersonModel;", "inactivePeople", "Ljava/util/ArrayDeque;", "addActivePerson", "", "person", "getPeopleHubModel", "Lcom/android/systemui/statusbar/notification/people/PeopleHubModel;", "migrateActivePerson", "key", "removeActivePerson", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubNotificationListener.kt */
public final class PeopleHubManager {
    private final Map<String, PersonModel> activePeople = new LinkedHashMap();
    private final ArrayDeque<PersonModel> inactivePeople = new ArrayDeque<>(10);

    public final boolean migrateActivePerson(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        PersonModel remove = this.activePeople.remove(str);
        if (remove == null) {
            return false;
        }
        if (this.inactivePeople.size() >= 10) {
            this.inactivePeople.removeLast();
        }
        this.inactivePeople.addFirst(remove);
        return true;
    }

    public final void removeActivePerson(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.activePeople.remove(str);
    }

    public final boolean addActivePerson(PersonModel personModel) {
        Intrinsics.checkNotNullParameter(personModel, "person");
        this.activePeople.put(personModel.getKey(), personModel);
        return this.inactivePeople.removeIf(new PeopleHubManager$$ExternalSyntheticLambda0(personModel));
    }

    /* access modifiers changed from: private */
    /* renamed from: addActivePerson$lambda-1  reason: not valid java name */
    public static final boolean m3134addActivePerson$lambda1(PersonModel personModel, PersonModel personModel2) {
        Intrinsics.checkNotNullParameter(personModel, "$person");
        return Intrinsics.areEqual((Object) personModel2.getKey(), (Object) personModel.getKey());
    }

    public final PeopleHubModel getPeopleHubModel() {
        return new PeopleHubModel(this.inactivePeople);
    }
}
