package android.location;

import android.os.Parcel;
import android.os.Parcelable;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
/* loaded from: classes2.dex */
public final class LocationResult implements Parcelable {
    public static final Parcelable.Creator<LocationResult> CREATOR = new Parcelable.Creator<LocationResult>() { // from class: android.location.LocationResult.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: createFromParcel */
        public LocationResult mo3559createFromParcel(Parcel in) {
            ArrayList createTypedArrayList = in.createTypedArrayList(Location.CREATOR);
            Objects.requireNonNull(createTypedArrayList);
            return new LocationResult(createTypedArrayList);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        /* renamed from: newArray */
        public LocationResult[] mo3560newArray(int size) {
            return new LocationResult[size];
        }
    };
    private final ArrayList<Location> mLocations;

    public static LocationResult create(List<Location> locations) {
        Preconditions.checkArgument(!locations.isEmpty());
        ArrayList<Location> locationsCopy = new ArrayList<>(locations.size());
        for (Location location : locations) {
            Objects.requireNonNull(location);
            locationsCopy.add(new Location(location));
        }
        return new LocationResult(locationsCopy);
    }

    public static LocationResult create(Location... locations) {
        Preconditions.checkArgument(locations.length > 0);
        ArrayList<Location> locationsCopy = new ArrayList<>(locations.length);
        for (Location location : locations) {
            Objects.requireNonNull(location);
            locationsCopy.add(new Location(location));
        }
        return new LocationResult(locationsCopy);
    }

    public static LocationResult wrap(List<Location> locations) {
        Preconditions.checkArgument(!locations.isEmpty());
        return new LocationResult(new ArrayList(locations));
    }

    public static LocationResult wrap(Location... locations) {
        Preconditions.checkArgument(locations.length > 0);
        ArrayList<Location> newLocations = new ArrayList<>(locations.length);
        for (Location location : locations) {
            Objects.requireNonNull(location);
            newLocations.add(location);
        }
        return new LocationResult(newLocations);
    }

    private LocationResult(ArrayList<Location> locations) {
        Preconditions.checkArgument(!locations.isEmpty());
        this.mLocations = locations;
    }

    public LocationResult validate() {
        long prevElapsedRealtimeNs = 0;
        int size = this.mLocations.size();
        for (int i = 0; i < size; i++) {
            Location location = this.mLocations.get(i);
            if (!location.isComplete()) {
                throw new IllegalArgumentException("incomplete location at index " + i + ": " + this.mLocations);
            } else if (location.getElapsedRealtimeNanos() < prevElapsedRealtimeNs) {
                throw new IllegalArgumentException("incorrectly ordered location at index " + i + ": " + this.mLocations);
            } else {
                prevElapsedRealtimeNs = location.getElapsedRealtimeNanos();
            }
        }
        return this;
    }

    public Location getLastLocation() {
        ArrayList<Location> arrayList = this.mLocations;
        return arrayList.get(arrayList.size() - 1);
    }

    public int size() {
        return this.mLocations.size();
    }

    public Location get(int i) {
        return this.mLocations.get(i);
    }

    public List<Location> asList() {
        return Collections.unmodifiableList(this.mLocations);
    }

    public LocationResult deepCopy() {
        int size = this.mLocations.size();
        ArrayList<Location> copy = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            copy.add(new Location(this.mLocations.get(i)));
        }
        return new LocationResult(copy);
    }

    public LocationResult asLastLocationResult() {
        return this.mLocations.size() == 1 ? this : wrap(getLastLocation());
    }

    public LocationResult filter(Predicate<Location> predicate) {
        ArrayList<Location> filtered = this.mLocations;
        int size = this.mLocations.size();
        for (int i = 0; i < size; i++) {
            if (!predicate.test(this.mLocations.get(i))) {
                if (filtered == this.mLocations) {
                    filtered = new ArrayList<>(this.mLocations.size() - 1);
                    for (int j = 0; j < i; j++) {
                        filtered.add(this.mLocations.get(j));
                    }
                }
            } else {
                ArrayList<Location> arrayList = this.mLocations;
                if (filtered != arrayList) {
                    filtered.add(arrayList.get(i));
                }
            }
        }
        if (filtered == this.mLocations) {
            return this;
        }
        if (filtered.isEmpty()) {
            return null;
        }
        return new LocationResult(filtered);
    }

    public LocationResult map(Function<Location, Location> function) {
        ArrayList<Location> mapped = this.mLocations;
        int size = this.mLocations.size();
        for (int i = 0; i < size; i++) {
            Location location = this.mLocations.get(i);
            Location newLocation = function.apply(location);
            if (mapped != this.mLocations) {
                mapped.add(newLocation);
            } else if (newLocation != location) {
                mapped = new ArrayList<>(this.mLocations.size());
                for (int j = 0; j < i; j++) {
                    mapped.add(this.mLocations.get(j));
                }
                mapped.add(newLocation);
            }
        }
        if (mapped == this.mLocations) {
            return this;
        }
        return new LocationResult(mapped);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeTypedList(this.mLocations);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocationResult that = (LocationResult) o;
        return this.mLocations.equals(that.mLocations);
    }

    public int hashCode() {
        return Objects.hash(this.mLocations);
    }

    public String toString() {
        return this.mLocations.toString();
    }
}