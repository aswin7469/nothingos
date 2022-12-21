package com.android.systemui.statusbar.notification.people;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.lang.annotation.RetentionPolicy;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

@Metadata(mo64986d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u0000 \t2\u00020\u0001:\u0001\tJ\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0003H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH&ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\nÀ\u0006\u0001"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleNotificationIdentifier;", "", "compareTo", "", "a", "b", "getPeopleNotificationType", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PeopleNotificationIdentifier.kt */
public interface PeopleNotificationIdentifier {
    public static final Companion Companion = Companion.$$INSTANCE;
    public static final int TYPE_FULL_PERSON = 2;
    public static final int TYPE_IMPORTANT_PERSON = 3;
    public static final int TYPE_NON_PERSON = 0;
    public static final int TYPE_PERSON = 1;

    int compareTo(int i, int i2);

    int getPeopleNotificationType(NotificationEntry notificationEntry);

    @Metadata(mo64986d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\b\u0003\u0018\u00002\u00020\u0001:\u0001\bB\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\t"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleNotificationIdentifier$Companion;", "", "()V", "TYPE_FULL_PERSON", "", "TYPE_IMPORTANT_PERSON", "TYPE_NON_PERSON", "TYPE_PERSON", "PeopleNotificationType", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: PeopleNotificationIdentifier.kt */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();
        public static final int TYPE_FULL_PERSON = 2;
        public static final int TYPE_IMPORTANT_PERSON = 3;
        public static final int TYPE_NON_PERSON = 0;
        public static final int TYPE_PERSON = 1;

        @Metadata(mo64986d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleNotificationIdentifier$Companion$PeopleNotificationType;", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
        @Retention(AnnotationRetention.SOURCE)
        @java.lang.annotation.Retention(RetentionPolicy.SOURCE)
        /* compiled from: PeopleNotificationIdentifier.kt */
        public @interface PeopleNotificationType {
        }

        private Companion() {
        }
    }
}
