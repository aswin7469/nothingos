package com.android.systemui.people.widget;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.app.UnlaunchableAppActivity;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.p019wm.shell.bubbles.Bubble;
import com.android.systemui.people.PeopleSpaceUtils;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.wmshell.BubblesManager;
import java.util.Optional;
import javax.inject.Inject;

public class LaunchConversationActivity extends Activity {
    private static final boolean DEBUG = false;
    private static final String TAG = "PeopleSpaceLaunchConv";
    /* access modifiers changed from: private */
    public Bubble mBubble;
    /* access modifiers changed from: private */
    public final Optional<BubblesManager> mBubblesManagerOptional;
    /* access modifiers changed from: private */
    public CommandQueue mCommandQueue;
    private CommonNotifCollection mCommonNotifCollection;
    /* access modifiers changed from: private */
    public NotificationEntry mEntryToBubble;
    private IStatusBarService mIStatusBarService;
    private boolean mIsForTesting;
    private UiEventLogger mUiEventLogger = new UiEventLoggerImpl();
    private final UserManager mUserManager;
    private NotificationVisibilityProvider mVisibilityProvider;

    @Inject
    public LaunchConversationActivity(NotificationVisibilityProvider notificationVisibilityProvider, CommonNotifCollection commonNotifCollection, Optional<BubblesManager> optional, UserManager userManager, CommandQueue commandQueue) {
        this.mVisibilityProvider = notificationVisibilityProvider;
        this.mCommonNotifCollection = commonNotifCollection;
        this.mBubblesManagerOptional = optional;
        this.mUserManager = userManager;
        this.mCommandQueue = commandQueue;
        commandQueue.addCallback((CommandQueue.Callbacks) new CommandQueue.Callbacks() {
            public void appTransitionFinished(int i) {
                if (LaunchConversationActivity.this.mBubblesManagerOptional.isPresent()) {
                    if (LaunchConversationActivity.this.mBubble != null) {
                        ((BubblesManager) LaunchConversationActivity.this.mBubblesManagerOptional.get()).expandStackAndSelectBubble(LaunchConversationActivity.this.mBubble);
                    } else if (LaunchConversationActivity.this.mEntryToBubble != null) {
                        ((BubblesManager) LaunchConversationActivity.this.mBubblesManagerOptional.get()).expandStackAndSelectBubble(LaunchConversationActivity.this.mEntryToBubble);
                    }
                }
                LaunchConversationActivity.this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
            }
        });
    }

    public void onCreate(Bundle bundle) {
        if (!this.mIsForTesting) {
            super.onCreate(bundle);
        }
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra(PeopleSpaceWidgetProvider.EXTRA_TILE_ID);
        String stringExtra2 = intent.getStringExtra(PeopleSpaceWidgetProvider.EXTRA_PACKAGE_NAME);
        UserHandle userHandle = (UserHandle) intent.getParcelableExtra(PeopleSpaceWidgetProvider.EXTRA_USER_HANDLE);
        String stringExtra3 = intent.getStringExtra(PeopleSpaceWidgetProvider.EXTRA_NOTIFICATION_KEY);
        if (!TextUtils.isEmpty(stringExtra)) {
            this.mUiEventLogger.log(PeopleSpaceUtils.PeopleSpaceWidgetEvent.PEOPLE_SPACE_WIDGET_CLICKED);
            try {
                if (this.mUserManager.isQuietModeEnabled(userHandle)) {
                    getApplicationContext().startActivity(UnlaunchableAppActivity.createInQuietModeDialogIntent(userHandle.getIdentifier()));
                    finish();
                    return;
                }
                if (this.mBubblesManagerOptional.isPresent() && stringExtra3 != null) {
                    this.mBubble = this.mBubblesManagerOptional.get().getBubbleWithShortcutId(stringExtra);
                    NotificationEntry entry = this.mCommonNotifCollection.getEntry(stringExtra3);
                    if (this.mBubble != null || (entry != null && entry.canBubble())) {
                        this.mEntryToBubble = entry;
                        finish();
                        return;
                    }
                }
                if (this.mIStatusBarService == null) {
                    this.mIStatusBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
                }
                clearNotificationIfPresent(stringExtra3, stringExtra2, userHandle);
                ((LauncherApps) getApplicationContext().getSystemService(LauncherApps.class)).startShortcut(stringExtra2, stringExtra, (Rect) null, (Bundle) null, userHandle);
            } catch (Exception e) {
                Log.e(TAG, "Exception launching shortcut:" + e);
            }
        }
        finish();
    }

    /* access modifiers changed from: package-private */
    public void clearNotificationIfPresent(String str, String str2, UserHandle userHandle) {
        if (!TextUtils.isEmpty(str)) {
            try {
                if (this.mIStatusBarService != null) {
                    CommonNotifCollection commonNotifCollection = this.mCommonNotifCollection;
                    if (commonNotifCollection != null) {
                        NotificationEntry entry = commonNotifCollection.getEntry(str);
                        if (entry == null) {
                            return;
                        }
                        if (entry.getRanking() != null) {
                            NotificationVisibility obtain = this.mVisibilityProvider.obtain(entry, true);
                            int i = obtain.rank;
                            this.mIStatusBarService.onNotificationClear(str2, userHandle.getIdentifier(), str, 0, 2, obtain);
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception cancelling notification:" + e);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setIsForTesting(boolean z, IStatusBarService iStatusBarService) {
        this.mIsForTesting = z;
        this.mIStatusBarService = iStatusBarService;
    }
}
