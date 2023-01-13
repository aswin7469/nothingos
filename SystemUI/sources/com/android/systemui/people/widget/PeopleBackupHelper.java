package com.android.systemui.people.widget;

import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.app.people.IPeopleManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.people.PeopleBackupFollowUpJob;
import com.android.systemui.people.PeopleSpaceUtils;
import com.android.systemui.people.SharedPreferencesHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PeopleBackupHelper extends SharedPreferencesBackupHelper {
    public static final String ADD_USER_ID_TO_URI = "add_user_id_to_uri_";
    public static final String SHARED_BACKUP = "shared_backup";
    private static final String TAG = "PeopleBackupHelper";
    private final AppWidgetManager mAppWidgetManager;
    private final Context mContext;
    private final IPeopleManager mIPeopleManager;
    private final PackageManager mPackageManager;
    private final UserHandle mUserHandle;

    enum SharedFileEntryType {
        UNKNOWN,
        WIDGET_ID,
        PEOPLE_TILE_KEY,
        CONTACT_URI
    }

    public /* bridge */ /* synthetic */ void writeNewStateDescription(ParcelFileDescriptor parcelFileDescriptor) {
        super.writeNewStateDescription(parcelFileDescriptor);
    }

    public static List<String> getFilesToBackup() {
        return Collections.singletonList(SHARED_BACKUP);
    }

    public PeopleBackupHelper(Context context, UserHandle userHandle, String[] strArr) {
        super(context, strArr);
        this.mContext = context;
        this.mUserHandle = userHandle;
        this.mPackageManager = context.getPackageManager();
        this.mIPeopleManager = IPeopleManager.Stub.asInterface(ServiceManager.getService("people"));
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
    }

    public PeopleBackupHelper(Context context, UserHandle userHandle, String[] strArr, PackageManager packageManager, IPeopleManager iPeopleManager) {
        super(context, strArr);
        this.mContext = context;
        this.mUserHandle = userHandle;
        this.mPackageManager = packageManager;
        this.mIPeopleManager = iPeopleManager;
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
    }

    public void performBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        if (!defaultSharedPreferences.getAll().isEmpty()) {
            SharedPreferences.Editor edit = this.mContext.getSharedPreferences(SHARED_BACKUP, 0).edit();
            edit.clear();
            List<String> existingWidgetsForUser = getExistingWidgetsForUser(this.mUserHandle.getIdentifier());
            if (!existingWidgetsForUser.isEmpty()) {
                defaultSharedPreferences.getAll().entrySet().forEach(new PeopleBackupHelper$$ExternalSyntheticLambda0(this, edit, existingWidgetsForUser));
                edit.apply();
                super.performBackup(parcelFileDescriptor, backupDataOutput, parcelFileDescriptor2);
            }
        }
    }

    public void restoreEntity(BackupDataInputStream backupDataInputStream) {
        super.restoreEntity(backupDataInputStream);
        boolean z = false;
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(SHARED_BACKUP, 0);
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(this.mContext).edit();
        SharedPreferences.Editor edit2 = this.mContext.getSharedPreferences(PeopleBackupFollowUpJob.SHARED_FOLLOW_UP, 0).edit();
        for (Map.Entry<String, ?> restoreKey : sharedPreferences.getAll().entrySet()) {
            if (!restoreKey(restoreKey, edit, edit2, sharedPreferences)) {
                z = true;
            }
        }
        edit.apply();
        edit2.apply();
        SharedPreferencesHelper.clear(sharedPreferences);
        if (z) {
            PeopleBackupFollowUpJob.scheduleJob(this.mContext);
        }
        updateWidgets(this.mContext);
    }

    /* renamed from: backupKey */
    public void mo35161x49d8378(Map.Entry<String, ?> entry, SharedPreferences.Editor editor, List<String> list) {
        String key = entry.getKey();
        if (!TextUtils.isEmpty(key)) {
            int i = C22961.f324xd18c5f3a[getEntryType(entry).ordinal()];
            if (i == 1) {
                backupWidgetIdKey(key, String.valueOf((Object) entry.getValue()), editor, list);
            } else if (i == 2) {
                backupPeopleTileKey(key, (Set) entry.getValue(), editor, list);
            } else if (i != 3) {
                Log.w(TAG, "Key not identified, skipping: " + key);
            } else {
                backupContactUriKey(key, (Set) entry.getValue(), editor);
            }
        }
    }

    /* renamed from: com.android.systemui.people.widget.PeopleBackupHelper$1 */
    static /* synthetic */ class C22961 {

        /* renamed from: $SwitchMap$com$android$systemui$people$widget$PeopleBackupHelper$SharedFileEntryType */
        static final /* synthetic */ int[] f324xd18c5f3a;

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        static {
            /*
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType[] r0 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                f324xd18c5f3a = r0
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r1 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.WIDGET_ID     // Catch:{ NoSuchFieldError -> 0x0012 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0012 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0012 }
            L_0x0012:
                int[] r0 = f324xd18c5f3a     // Catch:{ NoSuchFieldError -> 0x001d }
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r1 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.PEOPLE_TILE_KEY     // Catch:{ NoSuchFieldError -> 0x001d }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001d }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001d }
            L_0x001d:
                int[] r0 = f324xd18c5f3a     // Catch:{ NoSuchFieldError -> 0x0028 }
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r1 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.CONTACT_URI     // Catch:{ NoSuchFieldError -> 0x0028 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0028 }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0028 }
            L_0x0028:
                int[] r0 = f324xd18c5f3a     // Catch:{ NoSuchFieldError -> 0x0033 }
                com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r1 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.UNKNOWN     // Catch:{ NoSuchFieldError -> 0x0033 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0033 }
                r2 = 4
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0033 }
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleBackupHelper.C22961.<clinit>():void");
        }
    }

    /* access modifiers changed from: package-private */
    public boolean restoreKey(Map.Entry<String, ?> entry, SharedPreferences.Editor editor, SharedPreferences.Editor editor2, SharedPreferences sharedPreferences) {
        String key = entry.getKey();
        SharedFileEntryType entryType = getEntryType(entry);
        int i = sharedPreferences.getInt(ADD_USER_ID_TO_URI + key, -1);
        int i2 = C22961.f324xd18c5f3a[entryType.ordinal()];
        if (i2 == 1) {
            restoreWidgetIdKey(key, String.valueOf((Object) entry.getValue()), editor, i);
            return true;
        } else if (i2 == 2) {
            return restorePeopleTileKeyAndCorrespondingWidgetFile(key, (Set) entry.getValue(), editor, editor2);
        } else {
            if (i2 != 3) {
                Log.e(TAG, "Key not identified, skipping:" + key);
                return true;
            }
            restoreContactUriKey(key, (Set) entry.getValue(), editor, i);
            return true;
        }
    }

    private void backupWidgetIdKey(String str, String str2, SharedPreferences.Editor editor, List<String> list) {
        if (list.contains(str)) {
            Uri parse = Uri.parse(str2);
            if (ContentProvider.uriHasUserId(parse)) {
                editor.putInt(ADD_USER_ID_TO_URI + str, ContentProvider.getUserIdFromUri(parse));
                parse = ContentProvider.getUriWithoutUserId(parse);
            }
            editor.putString(str, parse.toString());
        }
    }

    private void restoreWidgetIdKey(String str, String str2, SharedPreferences.Editor editor, int i) {
        Uri parse = Uri.parse(str2);
        if (i != -1) {
            parse = ContentProvider.createContentUriForUser(parse, UserHandle.of(i));
        }
        editor.putString(str, parse.toString());
    }

    private void backupPeopleTileKey(String str, Set<String> set, SharedPreferences.Editor editor, List<String> list) {
        PeopleTileKey fromString = PeopleTileKey.fromString(str);
        if (fromString.getUserId() == this.mUserHandle.getIdentifier()) {
            Set set2 = (Set) set.stream().filter(new PeopleBackupHelper$$ExternalSyntheticLambda1(list)).collect(Collectors.toSet());
            if (!set2.isEmpty()) {
                fromString.setUserId(-1);
                editor.putStringSet(fromString.toString(), set2);
            }
        }
    }

    private boolean restorePeopleTileKeyAndCorrespondingWidgetFile(String str, Set<String> set, SharedPreferences.Editor editor, SharedPreferences.Editor editor2) {
        PeopleTileKey fromString = PeopleTileKey.fromString(str);
        if (fromString == null) {
            return true;
        }
        fromString.setUserId(this.mUserHandle.getIdentifier());
        if (!PeopleTileKey.isValid(fromString)) {
            return true;
        }
        boolean isReadyForRestore = isReadyForRestore(this.mIPeopleManager, this.mPackageManager, fromString);
        if (!isReadyForRestore) {
            editor2.putStringSet(fromString.toString(), set);
        }
        editor.putStringSet(fromString.toString(), set);
        restoreWidgetIdFiles(this.mContext, set, fromString);
        return isReadyForRestore;
    }

    private void backupContactUriKey(String str, Set<String> set, SharedPreferences.Editor editor) {
        Uri parse = Uri.parse(String.valueOf((Object) str));
        if (ContentProvider.uriHasUserId(parse)) {
            int userIdFromUri = ContentProvider.getUserIdFromUri(parse);
            if (userIdFromUri == this.mUserHandle.getIdentifier()) {
                Uri uriWithoutUserId = ContentProvider.getUriWithoutUserId(parse);
                editor.putInt(ADD_USER_ID_TO_URI + uriWithoutUserId.toString(), userIdFromUri);
                editor.putStringSet(uriWithoutUserId.toString(), set);
            }
        } else if (this.mUserHandle.isSystem()) {
            editor.putStringSet(parse.toString(), set);
        }
    }

    private void restoreContactUriKey(String str, Set<String> set, SharedPreferences.Editor editor, int i) {
        Uri parse = Uri.parse(str);
        if (i != -1) {
            parse = ContentProvider.createContentUriForUser(parse, UserHandle.of(i));
        }
        editor.putStringSet(parse.toString(), set);
    }

    public static void restoreWidgetIdFiles(Context context, Set<String> set, PeopleTileKey peopleTileKey) {
        for (String sharedPreferences : set) {
            SharedPreferencesHelper.setPeopleTileKey(context.getSharedPreferences(sharedPreferences, 0), peopleTileKey);
        }
    }

    private List<String> getExistingWidgetsForUser(int i) {
        ArrayList arrayList = new ArrayList();
        for (int valueOf : this.mAppWidgetManager.getAppWidgetIds(new ComponentName(this.mContext, PeopleSpaceWidgetProvider.class))) {
            String valueOf2 = String.valueOf(valueOf);
            if (this.mContext.getSharedPreferences(valueOf2, 0).getInt(PeopleSpaceUtils.USER_ID, -1) == i) {
                arrayList.add(valueOf2);
            }
        }
        return arrayList;
    }

    /* JADX WARNING: No exception handlers in catch block: Catch:{  } */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isReadyForRestore(android.app.people.IPeopleManager r3, android.content.pm.PackageManager r4, com.android.systemui.people.widget.PeopleTileKey r5) {
        /*
            boolean r0 = com.android.systemui.people.widget.PeopleTileKey.isValid(r5)
            if (r0 != 0) goto L_0x0008
            r3 = 1
            return r3
        L_0x0008:
            r0 = 0
            java.lang.String r1 = r5.getPackageName()     // Catch:{ NameNotFoundException -> 0x0025 }
            int r2 = r5.getUserId()     // Catch:{ NameNotFoundException -> 0x0025 }
            r4.getPackageInfoAsUser(r1, r0, r2)     // Catch:{ NameNotFoundException -> 0x0025 }
            java.lang.String r4 = r5.getPackageName()     // Catch:{  }
            int r1 = r5.getUserId()     // Catch:{  }
            java.lang.String r5 = r5.getShortcutId()     // Catch:{  }
            boolean r3 = r3.isConversation(r4, r1, r5)     // Catch:{  }
            return r3
        L_0x0025:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleBackupHelper.isReadyForRestore(android.app.people.IPeopleManager, android.content.pm.PackageManager, com.android.systemui.people.widget.PeopleTileKey):boolean");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:14|15|16|(2:18|19)(3:20|21|22)) */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r3 = (java.util.Set) r5.getValue();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003c, code lost:
        if (com.android.systemui.people.widget.PeopleTileKey.fromString(r2) != null) goto L_0x003e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0040, code lost:
        return com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.PEOPLE_TILE_KEY;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:?, code lost:
        android.net.Uri.parse(r2);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0046, code lost:
        return com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.CONTACT_URI;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0049, code lost:
        return com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.UNKNOWN;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:0x004a, code lost:
        android.util.Log.w(TAG, "Malformed value, skipping:" + r5.getValue());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0060, code lost:
        return com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.UNKNOWN;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:14:0x0032 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType getEntryType(java.util.Map.Entry<java.lang.String, ?> r5) {
        /*
            java.lang.String r0 = "PeopleBackupHelper"
            java.lang.String r1 = "Malformed value, skipping:"
            java.lang.Object r2 = r5.getKey()
            java.lang.String r2 = (java.lang.String) r2
            if (r2 != 0) goto L_0x000f
            com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r5 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.UNKNOWN
            return r5
        L_0x000f:
            java.lang.Integer.parseInt(r2)     // Catch:{ NumberFormatException -> 0x0032 }
            java.lang.Object r3 = r5.getValue()     // Catch:{ Exception -> 0x001b }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ Exception -> 0x001b }
            com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r5 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.WIDGET_ID     // Catch:{ NumberFormatException -> 0x0032 }
            return r5
        L_0x001b:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ NumberFormatException -> 0x0032 }
            r3.<init>((java.lang.String) r1)     // Catch:{ NumberFormatException -> 0x0032 }
            java.lang.Object r4 = r5.getValue()     // Catch:{ NumberFormatException -> 0x0032 }
            java.lang.StringBuilder r3 = r3.append((java.lang.Object) r4)     // Catch:{ NumberFormatException -> 0x0032 }
            java.lang.String r3 = r3.toString()     // Catch:{ NumberFormatException -> 0x0032 }
            android.util.Log.w(r0, r3)     // Catch:{ NumberFormatException -> 0x0032 }
            com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r5 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.UNKNOWN     // Catch:{ NumberFormatException -> 0x0032 }
            return r5
        L_0x0032:
            java.lang.Object r3 = r5.getValue()     // Catch:{ Exception -> 0x004a }
            java.util.Set r3 = (java.util.Set) r3     // Catch:{ Exception -> 0x004a }
            com.android.systemui.people.widget.PeopleTileKey r5 = com.android.systemui.people.widget.PeopleTileKey.fromString(r2)
            if (r5 == 0) goto L_0x0041
            com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r5 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.PEOPLE_TILE_KEY
            return r5
        L_0x0041:
            android.net.Uri.parse(r2)     // Catch:{ Exception -> 0x0047 }
            com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r5 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.CONTACT_URI     // Catch:{ Exception -> 0x0047 }
            return r5
        L_0x0047:
            com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r5 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.UNKNOWN
            return r5
        L_0x004a:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>((java.lang.String) r1)
            java.lang.Object r5 = r5.getValue()
            java.lang.StringBuilder r5 = r2.append((java.lang.Object) r5)
            java.lang.String r5 = r5.toString()
            android.util.Log.w(r0, r5)
            com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType r5 = com.android.systemui.people.widget.PeopleBackupHelper.SharedFileEntryType.UNKNOWN
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.people.widget.PeopleBackupHelper.getEntryType(java.util.Map$Entry):com.android.systemui.people.widget.PeopleBackupHelper$SharedFileEntryType");
    }

    public static void updateWidgets(Context context) {
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(new ComponentName(context, PeopleSpaceWidgetProvider.class));
        if (appWidgetIds != null && appWidgetIds.length != 0) {
            Intent intent = new Intent(context, PeopleSpaceWidgetProvider.class);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            intent.putExtra("appWidgetIds", appWidgetIds);
            context.sendBroadcast(intent);
        }
    }
}
