package com.android.systemui.statusbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.AsyncTask;
import android.os.Trace;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.widget.ImageView;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.media.MediaData;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.SmartspaceMediaData;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.LockscreenWallpaper;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ScrimState;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.Utils;
import com.android.systemui.util.concurrency.DelayableExecutor;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public class NotificationMediaManager implements Dumpable {
    private static final HashSet<Integer> PAUSED_MEDIA_STATES;
    private BackDropView mBackdrop;
    private ImageView mBackdropBack;
    private ImageView mBackdropFront;
    private BiometricUnlockController mBiometricUnlockController;
    private final Context mContext;
    private final NotificationEntryManager mEntryManager;
    private final KeyguardBypassController mKeyguardBypassController;
    private LockscreenWallpaper mLockscreenWallpaper;
    private final DelayableExecutor mMainExecutor;
    private final MediaArtworkProcessor mMediaArtworkProcessor;
    private MediaController mMediaController;
    private final MediaDataManager mMediaDataManager;
    private MediaMetadata mMediaMetadata;
    private String mMediaNotificationKey;
    private final MediaSessionManager mMediaSessionManager;
    private final NotifCollection mNotifCollection;
    private final NotifPipeline mNotifPipeline;
    private Lazy<NotificationShadeWindowController> mNotificationShadeWindowController;
    protected NotificationPresenter mPresenter;
    private ScrimController mScrimController;
    private final Lazy<StatusBar> mStatusBarLazy;
    private final boolean mUsingNotifPipeline;
    private final StatusBarStateController mStatusBarStateController = (StatusBarStateController) Dependency.get(StatusBarStateController.class);
    private final SysuiColorExtractor mColorExtractor = (SysuiColorExtractor) Dependency.get(SysuiColorExtractor.class);
    private final KeyguardStateController mKeyguardStateController = (KeyguardStateController) Dependency.get(KeyguardStateController.class);
    private final Set<AsyncTask<?, ?, ?>> mProcessArtworkTasks = new ArraySet();
    private final MediaController.Callback mMediaListener = new MediaController.Callback() { // from class: com.android.systemui.statusbar.NotificationMediaManager.1
        @Override // android.media.session.MediaController.Callback
        public void onPlaybackStateChanged(PlaybackState playbackState) {
            super.onPlaybackStateChanged(playbackState);
            if (playbackState != null) {
                if (!NotificationMediaManager.this.isPlaybackActive(playbackState.getState())) {
                    NotificationMediaManager.this.clearCurrentMediaNotification();
                }
                NotificationMediaManager.this.findAndUpdateMediaNotifications();
            }
        }

        @Override // android.media.session.MediaController.Callback
        public void onMetadataChanged(MediaMetadata mediaMetadata) {
            super.onMetadataChanged(mediaMetadata);
            NotificationMediaManager.this.mMediaArtworkProcessor.clearCache();
            NotificationMediaManager.this.mMediaMetadata = mediaMetadata;
            NotificationMediaManager.this.dispatchUpdateMediaMetaData(true, true);
        }
    };
    protected final Runnable mHideBackdropFront = new Runnable() { // from class: com.android.systemui.statusbar.NotificationMediaManager.7
        @Override // java.lang.Runnable
        public void run() {
            NotificationMediaManager.this.mBackdropFront.setVisibility(4);
            NotificationMediaManager.this.mBackdropFront.animate().cancel();
            NotificationMediaManager.this.mBackdropFront.setImageDrawable(null);
        }
    };
    private final ArrayList<MediaListener> mMediaListeners = new ArrayList<>();

    /* loaded from: classes.dex */
    public interface MediaListener {
        default void onPrimaryMetadataOrStateChanged(MediaMetadata mediaMetadata, int i) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isPlaybackActive(int i) {
        return (i == 1 || i == 7 || i == 0) ? false : true;
    }

    static {
        HashSet<Integer> hashSet = new HashSet<>();
        PAUSED_MEDIA_STATES = hashSet;
        hashSet.add(0);
        hashSet.add(1);
        hashSet.add(2);
        hashSet.add(7);
        hashSet.add(8);
    }

    public NotificationMediaManager(Context context, Lazy<StatusBar> lazy, Lazy<NotificationShadeWindowController> lazy2, NotificationEntryManager notificationEntryManager, MediaArtworkProcessor mediaArtworkProcessor, KeyguardBypassController keyguardBypassController, NotifPipeline notifPipeline, NotifCollection notifCollection, FeatureFlags featureFlags, DelayableExecutor delayableExecutor, DeviceConfigProxy deviceConfigProxy, MediaDataManager mediaDataManager) {
        this.mContext = context;
        this.mMediaArtworkProcessor = mediaArtworkProcessor;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mMediaSessionManager = (MediaSessionManager) context.getSystemService("media_session");
        this.mStatusBarLazy = lazy;
        this.mNotificationShadeWindowController = lazy2;
        this.mEntryManager = notificationEntryManager;
        this.mMainExecutor = delayableExecutor;
        this.mMediaDataManager = mediaDataManager;
        this.mNotifPipeline = notifPipeline;
        this.mNotifCollection = notifCollection;
        if (!featureFlags.isNewNotifPipelineRenderingEnabled()) {
            setupNEM();
            this.mUsingNotifPipeline = false;
            return;
        }
        setupNotifPipeline();
        this.mUsingNotifPipeline = true;
    }

    private void setupNotifPipeline() {
        this.mNotifPipeline.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.statusbar.NotificationMediaManager.2
            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryAdded(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.getKey(), notificationEntry.getSbn());
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryUpdated(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.getKey(), notificationEntry.getSbn());
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryBind(NotificationEntry notificationEntry, StatusBarNotification statusBarNotification) {
                NotificationMediaManager.this.findAndUpdateMediaNotifications();
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryRemoved(NotificationEntry notificationEntry, int i) {
                NotificationMediaManager.this.removeEntry(notificationEntry);
            }

            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryCleanUp(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.removeEntry(notificationEntry);
            }
        });
        this.mMediaDataManager.addListener(new AnonymousClass3());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.statusbar.NotificationMediaManager$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 implements MediaDataManager.Listener {
        @Override // com.android.systemui.media.MediaDataManager.Listener
        public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, boolean z2) {
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public void onSmartspaceMediaDataRemoved(String str, boolean z) {
        }

        AnonymousClass3() {
        }

        @Override // com.android.systemui.media.MediaDataManager.Listener
        public void onMediaDataRemoved(final String str) {
            NotificationMediaManager.this.mNotifPipeline.getAllNotifs().stream().filter(new Predicate() { // from class: com.android.systemui.statusbar.NotificationMediaManager$3$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$onMediaDataRemoved$0;
                    lambda$onMediaDataRemoved$0 = NotificationMediaManager.AnonymousClass3.lambda$onMediaDataRemoved$0(str, (NotificationEntry) obj);
                    return lambda$onMediaDataRemoved$0;
                }
            }).findAny().ifPresent(new Consumer() { // from class: com.android.systemui.statusbar.NotificationMediaManager$3$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    NotificationMediaManager.AnonymousClass3.this.lambda$onMediaDataRemoved$1((NotificationEntry) obj);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ boolean lambda$onMediaDataRemoved$0(String str, NotificationEntry notificationEntry) {
            return Objects.equals(notificationEntry.getKey(), str);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onMediaDataRemoved$1(NotificationEntry notificationEntry) {
            NotificationMediaManager.this.mNotifCollection.dismissNotification(notificationEntry, NotificationMediaManager.this.getDismissedByUserStats(notificationEntry));
        }
    }

    private void setupNEM() {
        this.mEntryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.statusbar.NotificationMediaManager.4
            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onPendingEntryAdded(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.getKey(), notificationEntry.getSbn());
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onPreEntryUpdated(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.mMediaDataManager.onNotificationAdded(notificationEntry.getKey(), notificationEntry.getSbn());
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryInflated(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.findAndUpdateMediaNotifications();
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryReinflated(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.findAndUpdateMediaNotifications();
            }

            @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
            public void onEntryRemoved(NotificationEntry notificationEntry, NotificationVisibility notificationVisibility, boolean z, int i) {
                NotificationMediaManager.this.removeEntry(notificationEntry);
            }
        });
        this.mEntryManager.addCollectionListener(new NotifCollectionListener() { // from class: com.android.systemui.statusbar.NotificationMediaManager.5
            @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
            public void onEntryCleanUp(NotificationEntry notificationEntry) {
                NotificationMediaManager.this.removeEntry(notificationEntry);
            }
        });
        this.mMediaDataManager.addListener(new MediaDataManager.Listener() { // from class: com.android.systemui.statusbar.NotificationMediaManager.6
            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, boolean z2) {
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onSmartspaceMediaDataRemoved(String str, boolean z) {
            }

            @Override // com.android.systemui.media.MediaDataManager.Listener
            public void onMediaDataRemoved(String str) {
                NotificationEntry pendingOrActiveNotif = NotificationMediaManager.this.mEntryManager.getPendingOrActiveNotif(str);
                if (pendingOrActiveNotif != null) {
                    NotificationMediaManager.this.mEntryManager.performRemoveNotification(pendingOrActiveNotif.getSbn(), NotificationMediaManager.this.getDismissedByUserStats(pendingOrActiveNotif), 2);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DismissedByUserStats getDismissedByUserStats(NotificationEntry notificationEntry) {
        int activeNotificationsCount;
        if (this.mUsingNotifPipeline) {
            activeNotificationsCount = this.mNotifPipeline.getShadeListCount();
        } else {
            activeNotificationsCount = this.mEntryManager.getActiveNotificationsCount();
        }
        return new DismissedByUserStats(3, 1, NotificationVisibility.obtain(notificationEntry.getKey(), notificationEntry.getRanking().getRank(), activeNotificationsCount, true, NotificationLogger.getNotificationLocation(notificationEntry)));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeEntry(NotificationEntry notificationEntry) {
        onNotificationRemoved(notificationEntry.getKey());
        this.mMediaDataManager.onNotificationRemoved(notificationEntry.getKey());
    }

    public static boolean isPlayingState(int i) {
        return !PAUSED_MEDIA_STATES.contains(Integer.valueOf(i));
    }

    public void setUpWithPresenter(NotificationPresenter notificationPresenter) {
        this.mPresenter = notificationPresenter;
    }

    public void onNotificationRemoved(String str) {
        if (str.equals(this.mMediaNotificationKey)) {
            clearCurrentMediaNotification();
            dispatchUpdateMediaMetaData(true, true);
        }
    }

    public String getMediaNotificationKey() {
        return this.mMediaNotificationKey;
    }

    public MediaMetadata getMediaMetadata() {
        return this.mMediaMetadata;
    }

    public void addCallback(MediaListener mediaListener) {
        this.mMediaListeners.add(mediaListener);
        mediaListener.onPrimaryMetadataOrStateChanged(this.mMediaMetadata, getMediaControllerPlaybackState(this.mMediaController));
    }

    public void findAndUpdateMediaNotifications() {
        boolean findPlayingMediaNotification;
        boolean z;
        if (this.mUsingNotifPipeline) {
            z = findPlayingMediaNotification(this.mNotifPipeline.getAllNotifs());
        } else {
            synchronized (this.mEntryManager) {
                findPlayingMediaNotification = findPlayingMediaNotification(this.mEntryManager.getAllNotifs());
            }
            if (findPlayingMediaNotification) {
                this.mEntryManager.updateNotifications("NotificationMediaManager - metaDataChanged");
            }
            z = findPlayingMediaNotification;
        }
        dispatchUpdateMediaMetaData(z, true);
    }

    private boolean findPlayingMediaNotification(Collection<NotificationEntry> collection) {
        NotificationEntry notificationEntry;
        MediaController mediaController;
        boolean z;
        MediaSessionManager mediaSessionManager;
        MediaSession.Token token;
        Iterator<NotificationEntry> it = collection.iterator();
        while (true) {
            if (!it.hasNext()) {
                notificationEntry = null;
                mediaController = null;
                break;
            }
            notificationEntry = it.next();
            if (notificationEntry.isMediaNotification() && (token = (MediaSession.Token) notificationEntry.getSbn().getNotification().extras.getParcelable("android.mediaSession")) != null) {
                mediaController = new MediaController(this.mContext, token);
                if (3 == getMediaControllerPlaybackState(mediaController)) {
                    break;
                }
            }
        }
        if (notificationEntry == null && (mediaSessionManager = this.mMediaSessionManager) != null) {
            for (MediaController mediaController2 : mediaSessionManager.getActiveSessionsForUser(null, UserHandle.ALL)) {
                String packageName = mediaController2.getPackageName();
                Iterator<NotificationEntry> it2 = collection.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        NotificationEntry next = it2.next();
                        if (next.getSbn().getPackageName().equals(packageName)) {
                            mediaController = mediaController2;
                            notificationEntry = next;
                            break;
                        }
                    }
                }
            }
        }
        if (mediaController == null || sameSessions(this.mMediaController, mediaController)) {
            z = false;
        } else {
            clearCurrentMediaNotificationSession();
            this.mMediaController = mediaController;
            mediaController.registerCallback(this.mMediaListener);
            this.mMediaMetadata = this.mMediaController.getMetadata();
            z = true;
        }
        if (notificationEntry != null && !notificationEntry.getSbn().getKey().equals(this.mMediaNotificationKey)) {
            this.mMediaNotificationKey = notificationEntry.getSbn().getKey();
        }
        return z;
    }

    public void clearCurrentMediaNotification() {
        this.mMediaNotificationKey = null;
        clearCurrentMediaNotificationSession();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchUpdateMediaMetaData(boolean z, boolean z2) {
        NotificationPresenter notificationPresenter = this.mPresenter;
        if (notificationPresenter != null) {
            notificationPresenter.updateMediaMetaData(z, z2);
        }
        int mediaControllerPlaybackState = getMediaControllerPlaybackState(this.mMediaController);
        ArrayList arrayList = new ArrayList(this.mMediaListeners);
        for (int i = 0; i < arrayList.size(); i++) {
            ((MediaListener) arrayList.get(i)).onPrimaryMetadataOrStateChanged(this.mMediaMetadata, mediaControllerPlaybackState);
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print("    mMediaSessionManager=");
        printWriter.println(this.mMediaSessionManager);
        printWriter.print("    mMediaNotificationKey=");
        printWriter.println(this.mMediaNotificationKey);
        printWriter.print("    mMediaController=");
        printWriter.print(this.mMediaController);
        if (this.mMediaController != null) {
            printWriter.print(" state=" + this.mMediaController.getPlaybackState());
        }
        printWriter.println();
        printWriter.print("    mMediaMetadata=");
        printWriter.print(this.mMediaMetadata);
        if (this.mMediaMetadata != null) {
            printWriter.print(" title=" + ((Object) this.mMediaMetadata.getText("android.media.metadata.TITLE")));
        }
        printWriter.println();
    }

    private boolean sameSessions(MediaController mediaController, MediaController mediaController2) {
        if (mediaController == mediaController2) {
            return true;
        }
        if (mediaController != null) {
            return mediaController.controlsSameSession(mediaController2);
        }
        return false;
    }

    private int getMediaControllerPlaybackState(MediaController mediaController) {
        PlaybackState playbackState;
        if (mediaController == null || (playbackState = mediaController.getPlaybackState()) == null) {
            return 0;
        }
        return playbackState.getState();
    }

    private void clearCurrentMediaNotificationSession() {
        this.mMediaArtworkProcessor.clearCache();
        this.mMediaMetadata = null;
        MediaController mediaController = this.mMediaController;
        if (mediaController != null) {
            mediaController.unregisterCallback(this.mMediaListener);
        }
        this.mMediaController = null;
    }

    public void updateMediaMetaData(boolean z, boolean z2) {
        Bitmap bitmap;
        Trace.beginSection("StatusBar#updateMediaMetaData");
        if (this.mBackdrop == null) {
            Trace.endSection();
            return;
        }
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
        boolean z3 = biometricUnlockController != null && biometricUnlockController.isWakeAndUnlock();
        if (this.mKeyguardStateController.isLaunchTransitionFadingAway() || z3) {
            this.mBackdrop.setVisibility(4);
            Trace.endSection();
            return;
        }
        MediaMetadata mediaMetadata = getMediaMetadata();
        if (mediaMetadata == null || this.mKeyguardBypassController.getBypassEnabled()) {
            bitmap = null;
        } else {
            bitmap = mediaMetadata.getBitmap("android.media.metadata.ART");
            if (bitmap == null) {
                bitmap = mediaMetadata.getBitmap("android.media.metadata.ALBUM_ART");
            }
        }
        if (z) {
            for (AsyncTask<?, ?, ?> asyncTask : this.mProcessArtworkTasks) {
                asyncTask.cancel(true);
            }
            this.mProcessArtworkTasks.clear();
        }
        if (bitmap != null && !Utils.useQsMediaPlayer(this.mContext)) {
            this.mProcessArtworkTasks.add(new ProcessArtworkTask(this, z, z2).execute(bitmap));
        } else {
            finishUpdateMediaMetaData(z, z2, null);
        }
        Trace.endSection();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0051  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00b2  */
    /* JADX WARN: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0116  */
    /* JADX WARN: Removed duplicated region for block: B:78:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0053  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void finishUpdateMediaMetaData(boolean z, boolean z2, Bitmap bitmap) {
        boolean z3;
        boolean z4;
        ScrimController scrimController;
        BiometricUnlockController biometricUnlockController;
        Drawable bitmapDrawable = bitmap != null ? new BitmapDrawable(this.mBackdropBack.getResources(), bitmap) : null;
        boolean z5 = true;
        boolean z6 = bitmapDrawable != null;
        if (bitmapDrawable == null) {
            LockscreenWallpaper lockscreenWallpaper = this.mLockscreenWallpaper;
            Bitmap bitmap2 = lockscreenWallpaper != null ? lockscreenWallpaper.getBitmap() : null;
            if (bitmap2 != null) {
                bitmapDrawable = new LockscreenWallpaper.WallpaperDrawable(this.mBackdropBack.getResources(), bitmap2);
                if (this.mStatusBarStateController.getState() == 1) {
                    z3 = true;
                    NotificationShadeWindowController notificationShadeWindowController = this.mNotificationShadeWindowController.get();
                    boolean isOccluded = this.mStatusBarLazy.get().isOccluded();
                    z4 = bitmapDrawable == null;
                    this.mColorExtractor.setHasMediaArtwork(z6);
                    scrimController = this.mScrimController;
                    if (scrimController != null) {
                        scrimController.setHasBackdrop(z4);
                    }
                    if (z4 && ((this.mStatusBarStateController.getState() != 0 || z3) && (biometricUnlockController = this.mBiometricUnlockController) != null && biometricUnlockController.getMode() != 2 && !isOccluded)) {
                        if (this.mBackdrop.getVisibility() != 0) {
                            this.mBackdrop.setVisibility(0);
                            if (z2) {
                                this.mBackdrop.setAlpha(0.0f);
                                this.mBackdrop.animate().alpha(1.0f);
                            } else {
                                this.mBackdrop.animate().cancel();
                                this.mBackdrop.setAlpha(1.0f);
                            }
                            if (notificationShadeWindowController != null) {
                                notificationShadeWindowController.setBackdropShowing(true);
                            }
                            z = true;
                        }
                        if (z) {
                            return;
                        }
                        if (this.mBackdropBack.getDrawable() != null) {
                            this.mBackdropFront.setImageDrawable(this.mBackdropBack.getDrawable().getConstantState().newDrawable(this.mBackdropFront.getResources()).mutate());
                            this.mBackdropFront.setAlpha(1.0f);
                            this.mBackdropFront.setVisibility(0);
                        } else {
                            this.mBackdropFront.setVisibility(4);
                        }
                        this.mBackdropBack.setImageDrawable(bitmapDrawable);
                        if (this.mBackdropFront.getVisibility() != 0) {
                            return;
                        }
                        this.mBackdropFront.animate().setDuration(250L).alpha(0.0f).withEndAction(this.mHideBackdropFront);
                        return;
                    } else if (this.mBackdrop.getVisibility() != 8) {
                        return;
                    } else {
                        if (!this.mStatusBarStateController.isDozing() || ScrimState.AOD.getAnimateChange()) {
                            z5 = false;
                        }
                        boolean isBypassFadingAnimation = this.mKeyguardStateController.isBypassFadingAnimation();
                        BiometricUnlockController biometricUnlockController2 = this.mBiometricUnlockController;
                        if ((((biometricUnlockController2 != null && biometricUnlockController2.getMode() == 2) || z5) && !isBypassFadingAnimation) || isOccluded) {
                            this.mBackdrop.setVisibility(8);
                            this.mBackdropBack.setImageDrawable(null);
                            if (notificationShadeWindowController == null) {
                                return;
                            }
                            notificationShadeWindowController.setBackdropShowing(false);
                            return;
                        }
                        if (notificationShadeWindowController != null) {
                            notificationShadeWindowController.setBackdropShowing(false);
                        }
                        this.mBackdrop.animate().alpha(0.0f).setInterpolator(Interpolators.ACCELERATE_DECELERATE).setDuration(300L).setStartDelay(0L).withEndAction(new Runnable() { // from class: com.android.systemui.statusbar.NotificationMediaManager$$ExternalSyntheticLambda0
                            @Override // java.lang.Runnable
                            public final void run() {
                                NotificationMediaManager.this.lambda$finishUpdateMediaMetaData$2();
                            }
                        });
                        if (!this.mKeyguardStateController.isKeyguardFadingAway()) {
                            return;
                        }
                        this.mBackdrop.animate().setDuration(this.mKeyguardStateController.getShortenedFadingAwayDuration()).setStartDelay(this.mKeyguardStateController.getKeyguardFadingAwayDelay()).setInterpolator(Interpolators.LINEAR).start();
                        return;
                    }
                }
            }
        }
        z3 = false;
        NotificationShadeWindowController notificationShadeWindowController2 = this.mNotificationShadeWindowController.get();
        boolean isOccluded2 = this.mStatusBarLazy.get().isOccluded();
        if (bitmapDrawable == null) {
        }
        this.mColorExtractor.setHasMediaArtwork(z6);
        scrimController = this.mScrimController;
        if (scrimController != null) {
        }
        if (z4) {
            if (this.mBackdrop.getVisibility() != 0) {
            }
            if (z) {
            }
        }
        if (this.mBackdrop.getVisibility() != 8) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$finishUpdateMediaMetaData$2() {
        this.mBackdrop.setVisibility(8);
        this.mBackdropFront.animate().cancel();
        this.mBackdropBack.setImageDrawable(null);
        this.mMainExecutor.execute(this.mHideBackdropFront);
    }

    public void setup(BackDropView backDropView, ImageView imageView, ImageView imageView2, ScrimController scrimController, LockscreenWallpaper lockscreenWallpaper) {
        this.mBackdrop = backDropView;
        this.mBackdropFront = imageView;
        this.mBackdropBack = imageView2;
        this.mScrimController = scrimController;
        this.mLockscreenWallpaper = lockscreenWallpaper;
    }

    public void setBiometricUnlockController(BiometricUnlockController biometricUnlockController) {
        this.mBiometricUnlockController = biometricUnlockController;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Bitmap processArtwork(Bitmap bitmap) {
        return this.mMediaArtworkProcessor.processArtwork(this.mContext, bitmap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void removeTask(AsyncTask<?, ?, ?> asyncTask) {
        this.mProcessArtworkTasks.remove(asyncTask);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class ProcessArtworkTask extends AsyncTask<Bitmap, Void, Bitmap> {
        private final boolean mAllowEnterAnimation;
        private final WeakReference<NotificationMediaManager> mManagerRef;
        private final boolean mMetaDataChanged;

        ProcessArtworkTask(NotificationMediaManager notificationMediaManager, boolean z, boolean z2) {
            this.mManagerRef = new WeakReference<>(notificationMediaManager);
            this.mMetaDataChanged = z;
            this.mAllowEnterAnimation = z2;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Bitmap... bitmapArr) {
            NotificationMediaManager notificationMediaManager = this.mManagerRef.get();
            if (notificationMediaManager == null || bitmapArr.length == 0 || isCancelled()) {
                return null;
            }
            return notificationMediaManager.processArtwork(bitmapArr[0]);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            NotificationMediaManager notificationMediaManager = this.mManagerRef.get();
            if (notificationMediaManager == null || isCancelled()) {
                return;
            }
            notificationMediaManager.removeTask(this);
            notificationMediaManager.finishUpdateMediaMetaData(this.mMetaDataChanged, this.mAllowEnterAnimation, bitmap);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onCancelled(Bitmap bitmap) {
            if (bitmap != null) {
                bitmap.recycle();
            }
            NotificationMediaManager notificationMediaManager = this.mManagerRef.get();
            if (notificationMediaManager != null) {
                notificationMediaManager.removeTask(this);
            }
        }
    }

    public NotificationEntry getAODMediaNotificationEntry() {
        MediaController mediaController;
        if (this.mMediaNotificationKey == null || (mediaController = this.mMediaController) == null || !isPlayingState(getMediaControllerPlaybackState(mediaController))) {
            return null;
        }
        if (this.mUsingNotifPipeline) {
            return this.mNotifPipeline.getAllNotifs().stream().filter(new Predicate() { // from class: com.android.systemui.statusbar.NotificationMediaManager$$ExternalSyntheticLambda1
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    boolean lambda$getAODMediaNotificationEntry$3;
                    lambda$getAODMediaNotificationEntry$3 = NotificationMediaManager.this.lambda$getAODMediaNotificationEntry$3((NotificationEntry) obj);
                    return lambda$getAODMediaNotificationEntry$3;
                }
            }).findAny().orElse(null);
        }
        synchronized (this.mEntryManager) {
            NotificationEntry activeNotificationUnfiltered = this.mEntryManager.getActiveNotificationUnfiltered(this.mMediaNotificationKey);
            if (activeNotificationUnfiltered != null && activeNotificationUnfiltered.getIcons().getAodIcon() != null) {
                return activeNotificationUnfiltered;
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$getAODMediaNotificationEntry$3(NotificationEntry notificationEntry) {
        return Objects.equals(notificationEntry.getKey(), this.mMediaNotificationKey);
    }
}
