package com.google.android.setupcompat.portal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class NotificationComponent implements Parcelable {
    public static final Parcelable.Creator<NotificationComponent> CREATOR = new Parcelable.Creator<NotificationComponent>() {
        public NotificationComponent createFromParcel(Parcel parcel) {
            return new NotificationComponent(parcel);
        }

        public NotificationComponent[] newArray(int i) {
            return new NotificationComponent[i];
        }
    };
    /* access modifiers changed from: private */
    public Bundle extraBundle;
    private final int notificationType;

    @Retention(RetentionPolicy.SOURCE)
    public @interface NotificationType {
        public static final int DEFERRED = 4;
        public static final int DEFERRED_ONGOING = 5;
        public static final int INITIAL_ONGOING = 1;
        public static final int MAX = 7;
        public static final int PORTAL = 6;
        public static final int PREDEFERRED = 2;
        public static final int PREDEFERRED_PREPARING = 3;
        public static final int UNKNOWN = 0;
    }

    public int describeContents() {
        return 0;
    }

    private NotificationComponent(int i) {
        this.extraBundle = new Bundle();
        this.notificationType = i;
    }

    protected NotificationComponent(Parcel parcel) {
        this(parcel.readInt());
        this.extraBundle = parcel.readBundle(Bundle.class.getClassLoader());
    }

    public int getIntExtra(String str, int i) {
        return this.extraBundle.getInt(str, i);
    }

    public int getNotificationType() {
        return this.notificationType;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.notificationType);
        parcel.writeBundle(this.extraBundle);
    }

    public static class Builder {
        private final NotificationComponent component;

        public Builder(int i) {
            this.component = new NotificationComponent(i);
        }

        public Builder putIntExtra(String str, int i) {
            this.component.extraBundle.putInt(str, i);
            return this;
        }

        public NotificationComponent build() {
            return this.component;
        }
    }
}
