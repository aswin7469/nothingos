package com.android.systemui.statusbar.notification.people;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u001e\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u0013\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\u0002\u0010\u0005J\u000f\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\u0019\u0010\t\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\r\u001a\u00020\u000eHÖ\u0001J\t\u0010\u000f\u001a\u00020\u0010HÖ\u0001R\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u0011"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleHubModel;", "", "people", "", "Lcom/android/systemui/statusbar/notification/people/PersonModel;", "(Ljava/util/Collection;)V", "getPeople", "()Ljava/util/Collection;", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHub.kt */
public final class PeopleHubModel {
    private final Collection<PersonModel> people;

    public static /* synthetic */ PeopleHubModel copy$default(PeopleHubModel peopleHubModel, Collection<PersonModel> collection, int i, Object obj) {
        if ((i & 1) != 0) {
            collection = peopleHubModel.people;
        }
        return peopleHubModel.copy(collection);
    }

    public final Collection<PersonModel> component1() {
        return this.people;
    }

    public final PeopleHubModel copy(Collection<PersonModel> collection) {
        Intrinsics.checkNotNullParameter(collection, "people");
        return new PeopleHubModel(collection);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof PeopleHubModel) && Intrinsics.areEqual((Object) this.people, (Object) ((PeopleHubModel) obj).people);
    }

    public int hashCode() {
        return this.people.hashCode();
    }

    public String toString() {
        return "PeopleHubModel(people=" + this.people + ')';
    }

    public PeopleHubModel(Collection<PersonModel> collection) {
        Intrinsics.checkNotNullParameter(collection, "people");
        this.people = collection;
    }

    public final Collection<PersonModel> getPeople() {
        return this.people;
    }
}
