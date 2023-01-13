package com.android.systemui.statusbar.notification.people;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

@Metadata(mo65042d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\n\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\u00020\u0001B\u001b\u0012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J\u000f\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003HÆ\u0003J\t\u0010\f\u001a\u00020\u0006HÆ\u0003J#\u0010\r\u001a\u00020\u00002\u000e\b\u0002\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006HÆ\u0001J\u0013\u0010\u000e\u001a\u00020\u00062\b\u0010\u000f\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0010\u001a\u00020\u0011HÖ\u0001J\t\u0010\u0012\u001a\u00020\u0013HÖ\u0001R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\bR\u0017\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0014"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleHubViewModel;", "", "people", "Lkotlin/sequences/Sequence;", "Lcom/android/systemui/statusbar/notification/people/PersonViewModel;", "isVisible", "", "(Lkotlin/sequences/Sequence;Z)V", "()Z", "getPeople", "()Lkotlin/sequences/Sequence;", "component1", "component2", "copy", "equals", "other", "hashCode", "", "toString", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHub.kt */
public final class PeopleHubViewModel {
    private final boolean isVisible;
    private final Sequence<PersonViewModel> people;

    public static /* synthetic */ PeopleHubViewModel copy$default(PeopleHubViewModel peopleHubViewModel, Sequence<PersonViewModel> sequence, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            sequence = peopleHubViewModel.people;
        }
        if ((i & 2) != 0) {
            z = peopleHubViewModel.isVisible;
        }
        return peopleHubViewModel.copy(sequence, z);
    }

    public final Sequence<PersonViewModel> component1() {
        return this.people;
    }

    public final boolean component2() {
        return this.isVisible;
    }

    public final PeopleHubViewModel copy(Sequence<PersonViewModel> sequence, boolean z) {
        Intrinsics.checkNotNullParameter(sequence, "people");
        return new PeopleHubViewModel(sequence, z);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PeopleHubViewModel)) {
            return false;
        }
        PeopleHubViewModel peopleHubViewModel = (PeopleHubViewModel) obj;
        return Intrinsics.areEqual((Object) this.people, (Object) peopleHubViewModel.people) && this.isVisible == peopleHubViewModel.isVisible;
    }

    public int hashCode() {
        int hashCode = this.people.hashCode() * 31;
        boolean z = this.isVisible;
        if (z) {
            z = true;
        }
        return hashCode + (z ? 1 : 0);
    }

    public String toString() {
        return "PeopleHubViewModel(people=" + this.people + ", isVisible=" + this.isVisible + ')';
    }

    public PeopleHubViewModel(Sequence<PersonViewModel> sequence, boolean z) {
        Intrinsics.checkNotNullParameter(sequence, "people");
        this.people = sequence;
        this.isVisible = z;
    }

    public final Sequence<PersonViewModel> getPeople() {
        return this.people;
    }

    public final boolean isVisible() {
        return this.isVisible;
    }
}
