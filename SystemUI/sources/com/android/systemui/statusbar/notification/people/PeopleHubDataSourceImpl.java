package com.android.systemui.statusbar.notification.people;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.UserManager;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.IconDrawableFactory;
import android.util.SparseArray;
import com.android.settingslib.notification.ConversationIconFactory;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000«\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u001f\b\u0007\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001Bc\b\u0007\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\b\b\u0001\u0010\u0011\u001a\u00020\u0012\u0012\b\b\u0001\u0010\u0013\u001a\u00020\u0012\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0017¢\u0006\u0002\u0010\u0018J\u0010\u0010&\u001a\u00020'2\u0006\u0010(\u001a\u00020)H\u0002J\n\u0010*\u001a\u0004\u0018\u00010\u0002H\u0002J\u0016\u0010+\u001a\u00020%2\f\u0010,\u001a\b\u0012\u0004\u0012\u00020\u00020\u001bH\u0016J\u0018\u0010-\u001a\u00020'2\u0006\u0010(\u001a\u00020)2\u0006\u0010.\u001a\u00020/H\u0002J\b\u00100\u001a\u00020'H\u0002J\u000e\u00101\u001a\u0004\u0018\u000102*\u00020)H\u0002J\u0014\u00103\u001a\n\u0018\u000104j\u0004\u0018\u0001`5*\u00020)H\u0002J\u001e\u00106\u001a\u0004\u0018\u000107*\u0002082\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u00109\u001a\u00020:H\u0002R\u000e\u0010\u0011\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u0019\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00020\u001b0\u001aX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0012X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001e\u001a\u00020\u001fX\u0004¢\u0006\u0004\n\u0002\u0010 R\u000e\u0010\u0014\u001a\u00020\u0015X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010!\u001a\b\u0012\u0004\u0012\u00020#0\"X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010$\u001a\u0004\u0018\u00010%X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000¨\u0006;"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/people/PeopleHubDataSourceImpl;", "Lcom/android/systemui/statusbar/notification/people/DataSource;", "Lcom/android/systemui/statusbar/notification/people/PeopleHubModel;", "notifCollection", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;", "extractor", "Lcom/android/systemui/statusbar/notification/people/NotificationPersonExtractor;", "userManager", "Landroid/os/UserManager;", "launcherApps", "Landroid/content/pm/LauncherApps;", "packageManager", "Landroid/content/pm/PackageManager;", "context", "Landroid/content/Context;", "notificationListener", "Lcom/android/systemui/statusbar/NotificationListener;", "bgExecutor", "Ljava/util/concurrent/Executor;", "mainExecutor", "notifLockscreenUserMgr", "Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;", "peopleNotificationIdentifier", "Lcom/android/systemui/statusbar/notification/people/PeopleNotificationIdentifier;", "(Lcom/android/systemui/statusbar/notification/collection/notifcollection/CommonNotifCollection;Lcom/android/systemui/statusbar/notification/people/NotificationPersonExtractor;Landroid/os/UserManager;Landroid/content/pm/LauncherApps;Landroid/content/pm/PackageManager;Landroid/content/Context;Lcom/android/systemui/statusbar/NotificationListener;Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Lcom/android/systemui/statusbar/NotificationLockscreenUserManager;Lcom/android/systemui/statusbar/notification/people/PeopleNotificationIdentifier;)V", "dataListeners", "", "Lcom/android/systemui/statusbar/notification/people/DataListener;", "iconFactory", "Lcom/android/settingslib/notification/ConversationIconFactory;", "notifCollectionListener", "com/android/systemui/statusbar/notification/people/PeopleHubDataSourceImpl$notifCollectionListener$1", "Lcom/android/systemui/statusbar/notification/people/PeopleHubDataSourceImpl$notifCollectionListener$1;", "peopleHubManagerForUser", "Landroid/util/SparseArray;", "Lcom/android/systemui/statusbar/notification/people/PeopleHubManager;", "userChangeSubscription", "Lcom/android/systemui/statusbar/notification/people/Subscription;", "addVisibleEntry", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "getPeopleHubModelForCurrentUser", "registerListener", "listener", "removeVisibleEntry", "reason", "", "updateUi", "extractPerson", "Lcom/android/systemui/statusbar/notification/people/PersonModel;", "extractPersonKey", "", "Lcom/android/systemui/statusbar/notification/people/PersonKey;", "getIcon", "Landroid/graphics/drawable/Drawable;", "Landroid/service/notification/NotificationListenerService$Ranking;", "sbn", "Landroid/service/notification/StatusBarNotification;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PeopleHubNotificationListener.kt */
public final class PeopleHubDataSourceImpl implements DataSource<PeopleHubModel> {
    private final Executor bgExecutor;
    /* access modifiers changed from: private */
    public final List<DataListener<PeopleHubModel>> dataListeners = new ArrayList();
    private final NotificationPersonExtractor extractor;
    private final ConversationIconFactory iconFactory;
    private final Executor mainExecutor;
    /* access modifiers changed from: private */
    public final CommonNotifCollection notifCollection;
    /* access modifiers changed from: private */
    public final PeopleHubDataSourceImpl$notifCollectionListener$1 notifCollectionListener;
    private final NotificationLockscreenUserManager notifLockscreenUserMgr;
    private final NotificationListener notificationListener;
    private final SparseArray<PeopleHubManager> peopleHubManagerForUser = new SparseArray<>();
    private final PeopleNotificationIdentifier peopleNotificationIdentifier;
    /* access modifiers changed from: private */
    public Subscription userChangeSubscription;
    private final UserManager userManager;

    @Inject
    public PeopleHubDataSourceImpl(CommonNotifCollection commonNotifCollection, NotificationPersonExtractor notificationPersonExtractor, UserManager userManager2, LauncherApps launcherApps, PackageManager packageManager, Context context, NotificationListener notificationListener2, @Background Executor executor, @Main Executor executor2, NotificationLockscreenUserManager notificationLockscreenUserManager, PeopleNotificationIdentifier peopleNotificationIdentifier2) {
        Intrinsics.checkNotNullParameter(commonNotifCollection, "notifCollection");
        Intrinsics.checkNotNullParameter(notificationPersonExtractor, "extractor");
        Intrinsics.checkNotNullParameter(userManager2, "userManager");
        Intrinsics.checkNotNullParameter(launcherApps, "launcherApps");
        Intrinsics.checkNotNullParameter(packageManager, "packageManager");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(notificationListener2, "notificationListener");
        Intrinsics.checkNotNullParameter(executor, "bgExecutor");
        Intrinsics.checkNotNullParameter(executor2, "mainExecutor");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "notifLockscreenUserMgr");
        Intrinsics.checkNotNullParameter(peopleNotificationIdentifier2, "peopleNotificationIdentifier");
        this.notifCollection = commonNotifCollection;
        this.extractor = notificationPersonExtractor;
        this.userManager = userManager2;
        this.notificationListener = notificationListener2;
        this.bgExecutor = executor;
        this.mainExecutor = executor2;
        this.notifLockscreenUserMgr = notificationLockscreenUserManager;
        this.peopleNotificationIdentifier = peopleNotificationIdentifier2;
        PeopleHubDataSourceImpl peopleHubDataSourceImpl = this;
        Context applicationContext = context.getApplicationContext();
        this.iconFactory = new ConversationIconFactory(applicationContext, launcherApps, packageManager, IconDrawableFactory.newInstance(applicationContext), applicationContext.getResources().getDimensionPixelSize(C1894R.dimen.notification_guts_conversation_icon_size));
        this.notifCollectionListener = new PeopleHubDataSourceImpl$notifCollectionListener$1(this);
    }

    /* access modifiers changed from: private */
    public final void removeVisibleEntry(NotificationEntry notificationEntry, int i) {
        NotificationPersonExtractor notificationPersonExtractor = this.extractor;
        StatusBarNotification sbn = notificationEntry.getSbn();
        Intrinsics.checkNotNullExpressionValue(sbn, "entry.sbn");
        String extractPersonKey = notificationPersonExtractor.extractPersonKey(sbn);
        if (extractPersonKey == null) {
            extractPersonKey = extractPersonKey(notificationEntry);
        }
        if (extractPersonKey != null) {
            this.bgExecutor.execute(new PeopleHubDataSourceImpl$$ExternalSyntheticLambda4(this, notificationEntry.getSbn().getUser().getIdentifier(), i, extractPersonKey));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: removeVisibleEntry$lambda-3$lambda-2  reason: not valid java name */
    public static final void m3132removeVisibleEntry$lambda3$lambda2(PeopleHubDataSourceImpl peopleHubDataSourceImpl, int i, int i2, String str) {
        Intrinsics.checkNotNullParameter(peopleHubDataSourceImpl, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        UserInfo profileParent = peopleHubDataSourceImpl.userManager.getProfileParent(i);
        if (profileParent != null) {
            i = profileParent.id;
        }
        peopleHubDataSourceImpl.mainExecutor.execute(new PeopleHubDataSourceImpl$$ExternalSyntheticLambda0(i2, peopleHubDataSourceImpl, i, str));
    }

    /* access modifiers changed from: private */
    /* renamed from: removeVisibleEntry$lambda-3$lambda-2$lambda-1  reason: not valid java name */
    public static final void m3133removeVisibleEntry$lambda3$lambda2$lambda1(int i, PeopleHubDataSourceImpl peopleHubDataSourceImpl, int i2, String str) {
        Intrinsics.checkNotNullParameter(peopleHubDataSourceImpl, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        if (i == 18) {
            PeopleHubManager peopleHubManager = peopleHubDataSourceImpl.peopleHubManagerForUser.get(i2);
            boolean z = false;
            if (peopleHubManager != null && peopleHubManager.migrateActivePerson(str)) {
                z = true;
            }
            if (z) {
                peopleHubDataSourceImpl.updateUi();
                return;
            }
            return;
        }
        PeopleHubManager peopleHubManager2 = peopleHubDataSourceImpl.peopleHubManagerForUser.get(i2);
        if (peopleHubManager2 != null) {
            peopleHubManager2.removeActivePerson(str);
        }
    }

    /* access modifiers changed from: private */
    public final void addVisibleEntry(NotificationEntry notificationEntry) {
        PersonModel extractPerson = extractPerson(notificationEntry);
        if (extractPerson != null) {
            this.bgExecutor.execute(new PeopleHubDataSourceImpl$$ExternalSyntheticLambda3(this, notificationEntry.getSbn().getUser().getIdentifier(), extractPerson));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: addVisibleEntry$lambda-7$lambda-6  reason: not valid java name */
    public static final void m3129addVisibleEntry$lambda7$lambda6(PeopleHubDataSourceImpl peopleHubDataSourceImpl, int i, PersonModel personModel) {
        Intrinsics.checkNotNullParameter(peopleHubDataSourceImpl, "this$0");
        Intrinsics.checkNotNullParameter(personModel, "$personModel");
        UserInfo profileParent = peopleHubDataSourceImpl.userManager.getProfileParent(i);
        if (profileParent != null) {
            i = profileParent.id;
        }
        peopleHubDataSourceImpl.mainExecutor.execute(new PeopleHubDataSourceImpl$$ExternalSyntheticLambda1(peopleHubDataSourceImpl, i, personModel));
    }

    /* access modifiers changed from: private */
    /* renamed from: addVisibleEntry$lambda-7$lambda-6$lambda-5  reason: not valid java name */
    public static final void m3130addVisibleEntry$lambda7$lambda6$lambda5(PeopleHubDataSourceImpl peopleHubDataSourceImpl, int i, PersonModel personModel) {
        Intrinsics.checkNotNullParameter(peopleHubDataSourceImpl, "this$0");
        Intrinsics.checkNotNullParameter(personModel, "$personModel");
        PeopleHubManager peopleHubManager = peopleHubDataSourceImpl.peopleHubManagerForUser.get(i);
        if (peopleHubManager == null) {
            peopleHubManager = new PeopleHubManager();
            peopleHubDataSourceImpl.peopleHubManagerForUser.put(i, peopleHubManager);
        }
        if (peopleHubManager.addActivePerson(personModel)) {
            peopleHubDataSourceImpl.updateUi();
        }
    }

    public Subscription registerListener(DataListener<? super PeopleHubModel> dataListener) {
        Intrinsics.checkNotNullParameter(dataListener, "listener");
        boolean isEmpty = this.dataListeners.isEmpty();
        this.dataListeners.add(dataListener);
        if (isEmpty) {
            this.userChangeSubscription = PeopleHubNotificationListenerKt.registerListener(this.notifLockscreenUserMgr, new PeopleHubDataSourceImpl$registerListener$1(this));
            this.notifCollection.addCollectionListener(this.notifCollectionListener);
        } else {
            PeopleHubModel peopleHubModelForCurrentUser = getPeopleHubModelForCurrentUser();
            if (peopleHubModelForCurrentUser != null) {
                dataListener.onDataChanged(peopleHubModelForCurrentUser);
            }
        }
        return new PeopleHubDataSourceImpl$registerListener$3(this, dataListener);
    }

    private final PeopleHubModel getPeopleHubModelForCurrentUser() {
        PeopleHubModel peopleHubModel;
        PeopleHubManager peopleHubManager = this.peopleHubManagerForUser.get(this.notifLockscreenUserMgr.getCurrentUserId());
        if (peopleHubManager == null || (peopleHubModel = peopleHubManager.getPeopleHubModel()) == null) {
            return null;
        }
        SparseArray<UserInfo> currentProfiles = this.notifLockscreenUserMgr.getCurrentProfiles();
        Collection arrayList = new ArrayList();
        for (Object next : peopleHubModel.getPeople()) {
            UserInfo userInfo = currentProfiles.get(((PersonModel) next).getUserId());
            boolean z = false;
            if (userInfo != null && !userInfo.isQuietModeEnabled()) {
                z = true;
            }
            if (z) {
                arrayList.add(next);
            }
        }
        return peopleHubModel.copy((List) arrayList);
    }

    /* access modifiers changed from: private */
    public final void updateUi() {
        PeopleHubModel peopleHubModelForCurrentUser = getPeopleHubModelForCurrentUser();
        if (peopleHubModelForCurrentUser != null) {
            for (DataListener<PeopleHubModel> onDataChanged : this.dataListeners) {
                onDataChanged.onDataChanged(peopleHubModelForCurrentUser);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0023, code lost:
        r2 = r2.getLabel();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0029, code lost:
        r2 = r0.getCharSequence(androidx.core.app.NotificationCompat.EXTRA_CONVERSATION_TITLE);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final com.android.systemui.statusbar.notification.people.PersonModel extractPerson(com.android.systemui.statusbar.notification.collection.NotificationEntry r9) {
        /*
            r8 = this;
            com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier r0 = r8.peopleNotificationIdentifier
            int r0 = r0.getPeopleNotificationType(r9)
            r1 = 0
            if (r0 != 0) goto L_0x000a
            return r1
        L_0x000a:
            com.android.systemui.statusbar.notification.people.PeopleHubDataSourceImpl$$ExternalSyntheticLambda2 r7 = new com.android.systemui.statusbar.notification.people.PeopleHubDataSourceImpl$$ExternalSyntheticLambda2
            r7.<init>(r8, r9)
            android.service.notification.StatusBarNotification r0 = r9.getSbn()
            android.app.Notification r0 = r0.getNotification()
            android.os.Bundle r0 = r0.extras
            android.service.notification.NotificationListenerService$Ranking r2 = r9.getRanking()
            android.content.pm.ShortcutInfo r2 = r2.getConversationShortcutInfo()
            if (r2 == 0) goto L_0x0029
            java.lang.CharSequence r2 = r2.getLabel()
            if (r2 != 0) goto L_0x0039
        L_0x0029:
            java.lang.String r2 = "android.conversationTitle"
            java.lang.CharSequence r2 = r0.getCharSequence(r2)
            if (r2 != 0) goto L_0x0039
            java.lang.String r2 = "android.title"
            java.lang.CharSequence r0 = r0.getCharSequence(r2)
            r5 = r0
            goto L_0x003a
        L_0x0039:
            r5 = r2
        L_0x003a:
            if (r5 != 0) goto L_0x003d
            return r1
        L_0x003d:
            android.service.notification.NotificationListenerService$Ranking r0 = r9.getRanking()
            java.lang.String r1 = "ranking"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r1)
            com.android.settingslib.notification.ConversationIconFactory r1 = r8.iconFactory
            android.service.notification.StatusBarNotification r2 = r9.getSbn()
            java.lang.String r3 = "sbn"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r2, r3)
            android.graphics.drawable.Drawable r0 = r8.getIcon(r0, r1, r2)
            if (r0 != 0) goto L_0x0081
            com.android.settingslib.notification.ConversationIconFactory r8 = r8.iconFactory
            android.graphics.drawable.Drawable r0 = com.android.systemui.statusbar.notification.people.PeopleHubNotificationListenerKt.extractAvatarFromRow(r9)
            android.service.notification.StatusBarNotification r1 = r9.getSbn()
            java.lang.String r1 = r1.getPackageName()
            android.service.notification.StatusBarNotification r2 = r9.getSbn()
            int r2 = r2.getUid()
            android.service.notification.NotificationListenerService$Ranking r3 = r9.getRanking()
            android.app.NotificationChannel r3 = r3.getChannel()
            boolean r3 = r3.isImportantConversation()
            android.graphics.drawable.Drawable r8 = r8.getConversationDrawable((android.graphics.drawable.Drawable) r0, (java.lang.String) r1, (int) r2, (boolean) r3)
            r6 = r8
            goto L_0x0082
        L_0x0081:
            r6 = r0
        L_0x0082:
            com.android.systemui.statusbar.notification.people.PersonModel r8 = new com.android.systemui.statusbar.notification.people.PersonModel
            java.lang.String r3 = r9.getKey()
            java.lang.String r0 = "key"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, r0)
            android.service.notification.StatusBarNotification r9 = r9.getSbn()
            android.os.UserHandle r9 = r9.getUser()
            int r4 = r9.getIdentifier()
            java.lang.String r9 = "drawable"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r6, r9)
            r2 = r8
            r2.<init>(r3, r4, r5, r6, r7)
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.people.PeopleHubDataSourceImpl.extractPerson(com.android.systemui.statusbar.notification.collection.NotificationEntry):com.android.systemui.statusbar.notification.people.PersonModel");
    }

    /* access modifiers changed from: private */
    /* renamed from: extractPerson$lambda-9  reason: not valid java name */
    public static final void m3131extractPerson$lambda9(PeopleHubDataSourceImpl peopleHubDataSourceImpl, NotificationEntry notificationEntry) {
        Intrinsics.checkNotNullParameter(peopleHubDataSourceImpl, "this$0");
        Intrinsics.checkNotNullParameter(notificationEntry, "$this_extractPerson");
        peopleHubDataSourceImpl.notificationListener.unsnoozeNotification(notificationEntry.getKey());
    }

    private final Drawable getIcon(NotificationListenerService.Ranking ranking, ConversationIconFactory conversationIconFactory, StatusBarNotification statusBarNotification) {
        ShortcutInfo conversationShortcutInfo = ranking.getConversationShortcutInfo();
        if (conversationShortcutInfo != null) {
            return conversationIconFactory.getConversationDrawable(conversationShortcutInfo, statusBarNotification.getPackageName(), statusBarNotification.getUid(), ranking.getChannel().isImportantConversation());
        }
        return null;
    }

    private final String extractPersonKey(NotificationEntry notificationEntry) {
        if (this.peopleNotificationIdentifier.getPeopleNotificationType(notificationEntry) != 0) {
            return notificationEntry.getKey();
        }
        return null;
    }
}
