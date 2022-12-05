package com.android.systemui.people;

import android.app.Notification;
import android.app.backup.BackupManager;
import android.app.people.ConversationChannel;
import android.app.people.IPeopleManager;
import android.app.people.PeopleSpaceTile;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.UserManager;
import android.provider.ContactsContract;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import androidx.preference.PreferenceManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.ArrayUtils;
import com.android.internal.widget.MessagingMessage;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.R$string;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.people.widget.PeopleTileKey;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/* loaded from: classes.dex */
public class PeopleSpaceUtils {
    public static final PeopleTileKey EMPTY_KEY = new PeopleTileKey("", -1, "");

    /* loaded from: classes.dex */
    public enum NotificationAction {
        POSTED,
        REMOVED
    }

    public static void setSharedPreferencesStorageForTile(Context context, PeopleTileKey peopleTileKey, int i, Uri uri, BackupManager backupManager) {
        if (!PeopleTileKey.isValid(peopleTileKey)) {
            Log.e("PeopleSpaceUtils", "Not storing for invalid key");
            return;
        }
        SharedPreferencesHelper.setPeopleTileKey(context.getSharedPreferences(String.valueOf(i), 0), peopleTileKey);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        String uri2 = uri == null ? "" : uri.toString();
        edit.putString(String.valueOf(i), uri2);
        addAppWidgetIdForKey(defaultSharedPreferences, edit, i, peopleTileKey.toString());
        if (!TextUtils.isEmpty(uri2)) {
            addAppWidgetIdForKey(defaultSharedPreferences, edit, i, uri2);
        }
        edit.apply();
        backupManager.dataChanged();
    }

    public static void removeSharedPreferencesStorageForTile(Context context, PeopleTileKey peopleTileKey, int i, String str) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.remove(String.valueOf(i));
        removeAppWidgetIdForKey(defaultSharedPreferences, edit, i, peopleTileKey.toString());
        removeAppWidgetIdForKey(defaultSharedPreferences, edit, i, str);
        edit.apply();
        SharedPreferences.Editor edit2 = context.getSharedPreferences(String.valueOf(i), 0).edit();
        edit2.remove("package_name");
        edit2.remove("user_id");
        edit2.remove("shortcut_id");
        edit2.apply();
    }

    private static void addAppWidgetIdForKey(SharedPreferences sharedPreferences, SharedPreferences.Editor editor, int i, String str) {
        HashSet hashSet = new HashSet(sharedPreferences.getStringSet(str, new HashSet()));
        hashSet.add(String.valueOf(i));
        editor.putStringSet(str, hashSet);
    }

    private static void removeAppWidgetIdForKey(SharedPreferences sharedPreferences, SharedPreferences.Editor editor, int i, String str) {
        HashSet hashSet = new HashSet(sharedPreferences.getStringSet(str, new HashSet()));
        hashSet.remove(String.valueOf(i));
        editor.putStringSet(str, hashSet);
    }

    public static List<NotificationEntry> getNotificationsByUri(final PackageManager packageManager, final String str, Map<PeopleTileKey, Set<NotificationEntry>> map) {
        if (TextUtils.isEmpty(str)) {
            return new ArrayList();
        }
        return (List) map.entrySet().stream().flatMap(PeopleSpaceUtils$$ExternalSyntheticLambda4.INSTANCE).filter(new Predicate() { // from class: com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda5
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getNotificationsByUri$1;
                lambda$getNotificationsByUri$1 = PeopleSpaceUtils.lambda$getNotificationsByUri$1(packageManager, str, (NotificationEntry) obj);
                return lambda$getNotificationsByUri$1;
            }
        }).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ Stream lambda$getNotificationsByUri$0(Map.Entry entry) {
        return ((Set) entry.getValue()).stream();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getNotificationsByUri$1(PackageManager packageManager, String str, NotificationEntry notificationEntry) {
        return NotificationHelper.hasReadContactsPermission(packageManager, notificationEntry.getSbn()) && NotificationHelper.shouldMatchNotificationByUri(notificationEntry.getSbn()) && Objects.equals(str, NotificationHelper.getContactUri(notificationEntry.getSbn()));
    }

    public static int getMessagesCount(Set<NotificationEntry> set) {
        List<Notification.MessagingStyle.Message> messagingStyleMessages;
        int i = 0;
        for (NotificationEntry notificationEntry : set) {
            Notification notification = notificationEntry.getSbn().getNotification();
            if (!NotificationHelper.isMissedCall(notification) && (messagingStyleMessages = NotificationHelper.getMessagingStyleMessages(notification)) != null) {
                i += messagingStyleMessages.size();
            }
        }
        return i;
    }

    public static PeopleSpaceTile removeNotificationFields(PeopleSpaceTile peopleSpaceTile) {
        PeopleSpaceTile.Builder notificationCategory = peopleSpaceTile.toBuilder().setNotificationKey((String) null).setNotificationContent((CharSequence) null).setNotificationSender((CharSequence) null).setNotificationDataUri((Uri) null).setMessagesCount(0).setNotificationCategory((String) null);
        if (!TextUtils.isEmpty(peopleSpaceTile.getNotificationKey())) {
            notificationCategory.setLastInteractionTimestamp(System.currentTimeMillis());
        }
        return notificationCategory.build();
    }

    public static PeopleSpaceTile augmentTileFromNotification(Context context, PeopleSpaceTile peopleSpaceTile, PeopleTileKey peopleTileKey, NotificationEntry notificationEntry, int i, Optional<Integer> optional, BackupManager backupManager) {
        if (notificationEntry == null || notificationEntry.getSbn().getNotification() == null) {
            return removeNotificationFields(peopleSpaceTile);
        }
        StatusBarNotification sbn = notificationEntry.getSbn();
        Notification notification = sbn.getNotification();
        PeopleSpaceTile.Builder builder = peopleSpaceTile.toBuilder();
        String contactUri = NotificationHelper.getContactUri(sbn);
        if (optional.isPresent() && peopleSpaceTile.getContactUri() == null && !TextUtils.isEmpty(contactUri)) {
            Uri parse = Uri.parse(contactUri);
            setSharedPreferencesStorageForTile(context, new PeopleTileKey(peopleSpaceTile), optional.get().intValue(), parse, backupManager);
            builder.setContactUri(parse);
        }
        boolean isMissedCall = NotificationHelper.isMissedCall(notification);
        List<Notification.MessagingStyle.Message> messagingStyleMessages = NotificationHelper.getMessagingStyleMessages(notification);
        if (!isMissedCall && ArrayUtils.isEmpty(messagingStyleMessages)) {
            return removeNotificationFields(builder.build());
        }
        boolean z = false;
        Uri uri = null;
        Notification.MessagingStyle.Message message = messagingStyleMessages != null ? messagingStyleMessages.get(0) : null;
        if (message != null && !TextUtils.isEmpty(message.getText())) {
            z = true;
        }
        CharSequence text = (!isMissedCall || z) ? message.getText() : context.getString(R$string.missed_call);
        if (message != null && MessagingMessage.hasImage(message)) {
            uri = message.getDataUri();
        }
        return builder.setLastInteractionTimestamp(sbn.getPostTime()).setNotificationKey(sbn.getKey()).setNotificationCategory(notification.category).setNotificationContent(text).setNotificationSender(NotificationHelper.getSenderIfGroupConversation(notification, message)).setNotificationDataUri(uri).setMessagesCount(i).build();
    }

    public static List<PeopleSpaceTile> getSortedTiles(final IPeopleManager iPeopleManager, final LauncherApps launcherApps, final UserManager userManager, Stream<ShortcutInfo> stream) {
        return (List) stream.filter(PeopleSpaceUtils$$ExternalSyntheticLambda8.INSTANCE).filter(new Predicate() { // from class: com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda6
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                boolean lambda$getSortedTiles$2;
                lambda$getSortedTiles$2 = PeopleSpaceUtils.lambda$getSortedTiles$2(userManager, (ShortcutInfo) obj);
                return lambda$getSortedTiles$2;
            }
        }).map(new Function() { // from class: com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda3
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                PeopleSpaceTile lambda$getSortedTiles$3;
                lambda$getSortedTiles$3 = PeopleSpaceUtils.lambda$getSortedTiles$3(launcherApps, (ShortcutInfo) obj);
                return lambda$getSortedTiles$3;
            }
        }).filter(PeopleSpaceUtils$$ExternalSyntheticLambda7.INSTANCE).map(new Function() { // from class: com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda2
            @Override // java.util.function.Function
            public final Object apply(Object obj) {
                PeopleSpaceTile lambda$getSortedTiles$5;
                lambda$getSortedTiles$5 = PeopleSpaceUtils.lambda$getSortedTiles$5(iPeopleManager, (PeopleSpaceTile) obj);
                return lambda$getSortedTiles$5;
            }
        }).sorted(PeopleSpaceUtils$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$getSortedTiles$2(UserManager userManager, ShortcutInfo shortcutInfo) {
        return !userManager.isQuietModeEnabled(shortcutInfo.getUserHandle());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ PeopleSpaceTile lambda$getSortedTiles$3(LauncherApps launcherApps, ShortcutInfo shortcutInfo) {
        return new PeopleSpaceTile.Builder(shortcutInfo, launcherApps).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ PeopleSpaceTile lambda$getSortedTiles$5(IPeopleManager iPeopleManager, PeopleSpaceTile peopleSpaceTile) {
        return peopleSpaceTile.toBuilder().setLastInteractionTimestamp(getLastInteraction(iPeopleManager, peopleSpaceTile).longValue()).build();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int lambda$getSortedTiles$6(PeopleSpaceTile peopleSpaceTile, PeopleSpaceTile peopleSpaceTile2) {
        return new Long(peopleSpaceTile2.getLastInteractionTimestamp()).compareTo(new Long(peopleSpaceTile.getLastInteractionTimestamp()));
    }

    public static PeopleSpaceTile getTile(ConversationChannel conversationChannel, LauncherApps launcherApps) {
        if (conversationChannel == null) {
            Log.i("PeopleSpaceUtils", "ConversationChannel is null");
            return null;
        }
        PeopleSpaceTile build = new PeopleSpaceTile.Builder(conversationChannel, launcherApps).build();
        if (shouldKeepConversation(build)) {
            return build;
        }
        Log.i("PeopleSpaceUtils", "PeopleSpaceTile is not valid");
        return null;
    }

    private static Long getLastInteraction(IPeopleManager iPeopleManager, PeopleSpaceTile peopleSpaceTile) {
        try {
            return Long.valueOf(iPeopleManager.getLastInteraction(peopleSpaceTile.getPackageName(), getUserId(peopleSpaceTile), peopleSpaceTile.getId()));
        } catch (Exception e) {
            Log.e("PeopleSpaceUtils", "Couldn't retrieve last interaction time", e);
            return 0L;
        }
    }

    public static Bitmap convertDrawableToBitmap(Drawable drawable) {
        Bitmap createBitmap;
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            createBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    public static boolean shouldKeepConversation(PeopleSpaceTile peopleSpaceTile) {
        return peopleSpaceTile != null && !TextUtils.isEmpty(peopleSpaceTile.getUserName());
    }

    private static boolean hasBirthdayStatus(PeopleSpaceTile peopleSpaceTile, Context context) {
        return peopleSpaceTile.getBirthdayText() != null && peopleSpaceTile.getBirthdayText().equals(context.getString(R$string.birthday_status));
    }

    public static void getDataFromContactsOnBackgroundThread(final Context context, final PeopleSpaceWidgetManager peopleSpaceWidgetManager, final Map<Integer, PeopleSpaceTile> map, final int[] iArr) {
        ThreadUtils.postOnBackgroundThread(new Runnable() { // from class: com.android.systemui.people.PeopleSpaceUtils$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                PeopleSpaceUtils.getDataFromContacts(context, peopleSpaceWidgetManager, map, iArr);
            }
        });
    }

    @VisibleForTesting
    public static void getDataFromContacts(Context context, PeopleSpaceWidgetManager peopleSpaceWidgetManager, Map<Integer, PeopleSpaceTile> map, int[] iArr) {
        if (iArr.length == 0) {
            return;
        }
        List<String> contactLookupKeysWithBirthdaysToday = getContactLookupKeysWithBirthdaysToday(context);
        for (int i : iArr) {
            PeopleSpaceTile peopleSpaceTile = map.get(Integer.valueOf(i));
            if (peopleSpaceTile == null || peopleSpaceTile.getContactUri() == null) {
                updateTileContactFields(peopleSpaceWidgetManager, context, peopleSpaceTile, i, 0.0f, null);
            } else {
                updateTileWithBirthdayAndUpdateAffinity(context, peopleSpaceWidgetManager, contactLookupKeysWithBirthdaysToday, peopleSpaceTile, i);
            }
        }
    }

    private static void updateTileContactFields(PeopleSpaceWidgetManager peopleSpaceWidgetManager, Context context, PeopleSpaceTile peopleSpaceTile, int i, float f, String str) {
        boolean z = true;
        boolean z2 = hasBirthdayStatus(peopleSpaceTile, context) && str == null;
        boolean z3 = !hasBirthdayStatus(peopleSpaceTile, context) && str != null;
        if (peopleSpaceTile.getContactAffinity() == f && !z2 && !z3) {
            z = false;
        }
        if (z) {
            peopleSpaceWidgetManager.lambda$addNewWidget$5(i, peopleSpaceTile.toBuilder().setBirthdayText(str).setContactAffinity(f).build());
        }
    }

    private static void updateTileWithBirthdayAndUpdateAffinity(Context context, PeopleSpaceWidgetManager peopleSpaceWidgetManager, List<String> list, PeopleSpaceTile peopleSpaceTile, int i) {
        Cursor cursor = null;
        try {
            try {
                cursor = context.getContentResolver().query(peopleSpaceTile.getContactUri(), null, null, null, null);
                while (cursor != null) {
                    if (!cursor.moveToNext()) {
                        break;
                    }
                    String string = cursor.getString(cursor.getColumnIndex("lookup"));
                    float contactAffinity = getContactAffinity(cursor);
                    if (!string.isEmpty() && list.contains(string)) {
                        updateTileContactFields(peopleSpaceWidgetManager, context, peopleSpaceTile, i, contactAffinity, context.getString(R$string.birthday_status));
                    } else {
                        updateTileContactFields(peopleSpaceWidgetManager, context, peopleSpaceTile, i, contactAffinity, null);
                    }
                }
                if (cursor == null) {
                    return;
                }
            } catch (SQLException e) {
                Log.e("PeopleSpaceUtils", "Failed to query contact: " + e);
                if (cursor == null) {
                    return;
                }
            }
            cursor.close();
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    private static float getContactAffinity(Cursor cursor) {
        int columnIndex = cursor.getColumnIndex("starred");
        if (columnIndex >= 0) {
            if (!(cursor.getInt(columnIndex) != 0)) {
                return 0.5f;
            }
            return Math.max(0.5f, 1.0f);
        }
        return 0.5f;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0070, code lost:
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x006d, code lost:
        if (r1 == null) goto L11;
     */
    @VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static List<String> getContactLookupKeysWithBirthdaysToday(Context context) {
        ArrayList arrayList = new ArrayList(1);
        String format = new SimpleDateFormat("MM-dd").format(new Date());
        Cursor cursor = null;
        try {
            try {
                cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{"lookup", "data1"}, "mimetype= ? AND data2=3 AND (substr(data1,6) = ? OR substr(data1,3) = ? )", new String[]{"vnd.android.cursor.item/contact_event", format, format}, null);
                while (cursor != null) {
                    if (!cursor.moveToNext()) {
                        break;
                    }
                    arrayList.add(cursor.getString(cursor.getColumnIndex("lookup")));
                }
            } catch (SQLException e) {
                Log.e("PeopleSpaceUtils", "Failed to query birthdays: " + e);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static int getUserId(PeopleSpaceTile peopleSpaceTile) {
        return peopleSpaceTile.getUserHandle().getIdentifier();
    }

    /* loaded from: classes.dex */
    public enum PeopleSpaceWidgetEvent implements UiEventLogger.UiEventEnum {
        PEOPLE_SPACE_WIDGET_DELETED(666),
        PEOPLE_SPACE_WIDGET_ADDED(667),
        PEOPLE_SPACE_WIDGET_CLICKED(668);
        
        private final int mId;

        PeopleSpaceWidgetEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
