package com.google.android.setupcompat.logging;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import com.google.android.setupcompat.internal.ClockProvider;
import com.google.android.setupcompat.internal.PersistableBundles;
import com.google.android.setupcompat.internal.Preconditions;
import com.google.android.setupcompat.internal.Validations;
import com.google.android.setupcompat.util.ObjectUtils;

public final class CustomEvent implements Parcelable {
    private static final String BUNDLE_KEY_BUNDLE_PII_VALUES = "CustomEvent_pii_bundleValues";
    private static final String BUNDLE_KEY_BUNDLE_VALUES = "CustomEvent_bundleValues";
    private static final String BUNDLE_KEY_METRICKEY = "CustomEvent_metricKey";
    private static final String BUNDLE_KEY_TIMESTAMP = "CustomEvent_timestamp";
    private static final String BUNDLE_VERSION = "CustomEvent_version";
    public static final Parcelable.Creator<CustomEvent> CREATOR = new Parcelable.Creator<CustomEvent>() {
        public CustomEvent createFromParcel(Parcel parcel) {
            return new CustomEvent(parcel.readLong(), (MetricKey) parcel.readParcelable(MetricKey.class.getClassLoader()), parcel.readPersistableBundle(MetricKey.class.getClassLoader()), parcel.readPersistableBundle(MetricKey.class.getClassLoader()));
        }

        public CustomEvent[] newArray(int i) {
            return new CustomEvent[i];
        }
    };
    static final int MAX_STR_LENGTH = 50;
    static final int MIN_BUNDLE_KEY_LENGTH = 3;
    private static final int VERSION = 1;
    private final MetricKey metricKey;
    private final PersistableBundle persistableBundle;
    private final PersistableBundle piiValues;
    private final long timestampMillis;

    public int describeContents() {
        return 0;
    }

    public static CustomEvent create(MetricKey metricKey2, PersistableBundle persistableBundle2, PersistableBundle persistableBundle3) {
        Preconditions.checkArgument(true, "The constructor only support on sdk Q or higher");
        return new CustomEvent(ClockProvider.timeInMillis(), metricKey2, PersistableBundles.assertIsValid(persistableBundle2), PersistableBundles.assertIsValid(persistableBundle3));
    }

    public static CustomEvent create(MetricKey metricKey2, PersistableBundle persistableBundle2) {
        Preconditions.checkArgument(true, "The constructor only support on sdk Q or higher.");
        return create(metricKey2, persistableBundle2, PersistableBundle.EMPTY);
    }

    public static CustomEvent toCustomEvent(Bundle bundle) {
        return new CustomEvent(bundle.getLong(BUNDLE_KEY_TIMESTAMP, Long.MIN_VALUE), MetricKey.toMetricKey(bundle.getBundle(BUNDLE_KEY_METRICKEY)), PersistableBundles.fromBundle(bundle.getBundle(BUNDLE_KEY_BUNDLE_VALUES)), PersistableBundles.fromBundle(bundle.getBundle(BUNDLE_KEY_BUNDLE_PII_VALUES)));
    }

    public static Bundle toBundle(CustomEvent customEvent) {
        Preconditions.checkNotNull(customEvent, "CustomEvent cannot be null");
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_VERSION, 1);
        bundle.putLong(BUNDLE_KEY_TIMESTAMP, customEvent.timestampMillis());
        bundle.putBundle(BUNDLE_KEY_METRICKEY, MetricKey.fromMetricKey(customEvent.metricKey()));
        bundle.putBundle(BUNDLE_KEY_BUNDLE_VALUES, PersistableBundles.toBundle(customEvent.values()));
        bundle.putBundle(BUNDLE_KEY_BUNDLE_PII_VALUES, PersistableBundles.toBundle(customEvent.piiValues()));
        return bundle;
    }

    public long timestampMillis() {
        return this.timestampMillis;
    }

    public MetricKey metricKey() {
        return this.metricKey;
    }

    public PersistableBundle values() {
        return new PersistableBundle(this.persistableBundle);
    }

    public PersistableBundle piiValues() {
        return this.piiValues;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.timestampMillis);
        parcel.writeParcelable(this.metricKey, i);
        parcel.writePersistableBundle(this.persistableBundle);
        parcel.writePersistableBundle(this.piiValues);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CustomEvent)) {
            return false;
        }
        CustomEvent customEvent = (CustomEvent) obj;
        if (this.timestampMillis != customEvent.timestampMillis || !ObjectUtils.equals(this.metricKey, customEvent.metricKey) || !PersistableBundles.equals(this.persistableBundle, customEvent.persistableBundle) || !PersistableBundles.equals(this.piiValues, customEvent.piiValues)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return ObjectUtils.hashCode(Long.valueOf(this.timestampMillis), this.metricKey, this.persistableBundle, this.piiValues);
    }

    private CustomEvent(long j, MetricKey metricKey2, PersistableBundle persistableBundle2, PersistableBundle persistableBundle3) {
        Preconditions.checkArgument(j >= 0, "Timestamp cannot be negative.");
        Preconditions.checkNotNull(metricKey2, "MetricKey cannot be null.");
        Preconditions.checkNotNull(persistableBundle2, "Bundle cannot be null.");
        Preconditions.checkArgument(!persistableBundle2.isEmpty(), "Bundle cannot be empty.");
        Preconditions.checkNotNull(persistableBundle3, "piiValues cannot be null.");
        assertPersistableBundleIsValid(persistableBundle2);
        this.timestampMillis = j;
        this.metricKey = metricKey2;
        this.persistableBundle = new PersistableBundle(persistableBundle2);
        this.piiValues = new PersistableBundle(persistableBundle3);
    }

    private static void assertPersistableBundleIsValid(PersistableBundle persistableBundle2) {
        for (String str : persistableBundle2.keySet()) {
            Validations.assertLengthInRange(str, "bundle key", 3, 50);
            Object obj = persistableBundle2.get(str);
            if (obj instanceof String) {
                Preconditions.checkArgument(((String) obj).length() <= 50, String.format("Maximum length of string value for key='%s' cannot exceed %s.", str, 50));
            }
        }
    }

    public static String trimsStringOverMaxLength(String str) {
        if (str.length() <= 50) {
            return str;
        }
        return String.format("%s…", str.substring(0, 49));
    }
}
