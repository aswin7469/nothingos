package com.android.systemui.media;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.PendingIntent;
import android.app.WallpaperColors;
import android.app.smartspace.SmartspaceAction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.graphics.drawable.TransitionDrawable;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Trace;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.internal.logging.InstanceId;
import com.android.settingslib.widget.AdaptiveIcon;
import com.android.systemui.ActivityIntentHelper;
import com.android.systemui.C1894R;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.GhostedViewLaunchAnimatorController;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.bluetooth.BroadcastDialogController;
import com.android.systemui.broadcast.BroadcastSender;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.media.MediaViewController;
import com.android.systemui.media.SeekBarViewModel;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.monet.ColorScheme;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.animation.TransitionLayout;
import com.android.systemui.util.time.SystemClock;
import dagger.Lazy;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Unit;

public class MediaControlPanel {
    private static final float DISABLED_ALPHA = 0.38f;
    private static final String EXPORTED_SMARTSPACE_TRAMPOLINE_ACTIVITY_NAME = "com.google.android.apps.gsa.staticplugins.opa.smartspace.ExportedSmartspaceTrampolineActivity";
    private static final String EXTRAS_SMARTSPACE_INTENT = "com.google.android.apps.gsa.smartspace.extra.SMARTSPACE_INTENT";
    protected static final String KEY_SMARTSPACE_APP_NAME = "KEY_SMARTSPACE_APP_NAME";
    private static final String KEY_SMARTSPACE_ARTIST_NAME = "artist_name";
    private static final String KEY_SMARTSPACE_OPEN_IN_FOREGROUND = "KEY_OPEN_IN_FOREGROUND";
    private static final List<Integer> SEMANTIC_ACTIONS_ALL;
    private static final List<Integer> SEMANTIC_ACTIONS_COMPACT;
    private static final List<Integer> SEMANTIC_ACTIONS_HIDE_WHEN_SCRUBBING;
    private static final Intent SETTINGS_INTENT = new Intent("android.settings.ACTION_MEDIA_CONTROLS_SETTINGS");
    private static final int SMARTSPACE_CARD_CLICK_EVENT = 760;
    protected static final int SMARTSPACE_CARD_DISMISS_EVENT = 761;
    protected static final String TAG = "MediaControlPanel";
    private final ActivityIntentHelper mActivityIntentHelper;
    private final ActivityStarter mActivityStarter;
    private int mArtworkBoundId = 0;
    private int mArtworkNextBindRequestId = 0;
    protected final Executor mBackgroundExecutor;
    private final BroadcastDialogController mBroadcastDialogController;
    private final BroadcastSender mBroadcastSender;
    private ColorSchemeTransition mColorSchemeTransition;
    /* access modifiers changed from: private */
    public Context mContext;
    private MediaController mController;
    private final SeekBarViewModel.EnabledChangeListener mEnabledChangeListener = new MediaControlPanel$$ExternalSyntheticLambda10(this);
    private final FalsingManager mFalsingManager;
    private InstanceId mInstanceId;
    private boolean mIsArtworkBound = false;
    private boolean mIsCurrentBroadcastedApp = false;
    protected boolean mIsImpressed = false;
    private boolean mIsScrubbing = false;
    private boolean mIsSeekBarEnabled = false;
    private String mKey;
    private final KeyguardStateController mKeyguardStateController;
    private final NotificationLockscreenUserManager mLockscreenUserManager;
    private MediaUiEventLogger mLogger;
    private final Executor mMainExecutor;
    private MediaCarouselController mMediaCarouselController;
    private MediaData mMediaData;
    private Lazy<MediaDataManager> mMediaDataManagerLazy;
    private final MediaOutputDialogFactory mMediaOutputDialogFactory;
    private MediaViewController mMediaViewController;
    private MediaViewHolder mMediaViewHolder;
    private MetadataAnimationHandler mMetadataAnimationHandler;
    private String mPackageName;
    private Drawable mPrevArtwork = null;
    private SmartspaceMediaData mRecommendationData;
    private RecommendationViewHolder mRecommendationViewHolder;
    private final SeekBarViewModel.ScrubbingChangeListener mScrubbingChangeListener = new MediaControlPanel$$ExternalSyntheticLambda9(this);
    private SeekBarObserver mSeekBarObserver;
    private final SeekBarViewModel mSeekBarViewModel;
    private boolean mShowBroadcastDialogButton = false;
    protected int mSmartspaceId = -1;
    private int mSmartspaceMediaItemsCount;
    private String mSwitchBroadcastApp;
    private SystemClock mSystemClock;
    private MediaSession.Token mToken;
    protected int mUid = -1;

    static {
        Integer valueOf = Integer.valueOf((int) C1894R.C1898id.actionPlayPause);
        Integer valueOf2 = Integer.valueOf((int) C1894R.C1898id.actionPrev);
        Integer valueOf3 = Integer.valueOf((int) C1894R.C1898id.actionNext);
        SEMANTIC_ACTIONS_COMPACT = List.m1731of(valueOf, valueOf2, valueOf3);
        SEMANTIC_ACTIONS_HIDE_WHEN_SCRUBBING = List.m1730of(valueOf2, valueOf3);
        SEMANTIC_ACTIONS_ALL = List.m1733of(valueOf, valueOf2, valueOf3, Integer.valueOf((int) C1894R.C1898id.action0), Integer.valueOf((int) C1894R.C1898id.action1));
    }

    @Inject
    public MediaControlPanel(Context context, @Background Executor executor, @Main Executor executor2, ActivityStarter activityStarter, BroadcastSender broadcastSender, MediaViewController mediaViewController, SeekBarViewModel seekBarViewModel, Lazy<MediaDataManager> lazy, MediaOutputDialogFactory mediaOutputDialogFactory, MediaCarouselController mediaCarouselController, FalsingManager falsingManager, SystemClock systemClock, MediaUiEventLogger mediaUiEventLogger, KeyguardStateController keyguardStateController, ActivityIntentHelper activityIntentHelper, NotificationLockscreenUserManager notificationLockscreenUserManager, BroadcastDialogController broadcastDialogController) {
        this.mContext = context;
        this.mBackgroundExecutor = executor;
        this.mMainExecutor = executor2;
        this.mActivityStarter = activityStarter;
        this.mBroadcastSender = broadcastSender;
        this.mSeekBarViewModel = seekBarViewModel;
        this.mMediaViewController = mediaViewController;
        this.mMediaDataManagerLazy = lazy;
        this.mMediaOutputDialogFactory = mediaOutputDialogFactory;
        this.mMediaCarouselController = mediaCarouselController;
        this.mFalsingManager = falsingManager;
        this.mSystemClock = systemClock;
        this.mLogger = mediaUiEventLogger;
        this.mKeyguardStateController = keyguardStateController;
        this.mActivityIntentHelper = activityIntentHelper;
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mBroadcastDialogController = broadcastDialogController;
        seekBarViewModel.setLogSeek(new MediaControlPanel$$ExternalSyntheticLambda12(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-media-MediaControlPanel  reason: not valid java name */
    public /* synthetic */ Unit m2785lambda$new$0$comandroidsystemuimediaMediaControlPanel() {
        InstanceId instanceId;
        String str = this.mPackageName;
        if (!(str == null || (instanceId = this.mInstanceId) == null)) {
            this.mLogger.logSeek(this.mUid, str, instanceId);
        }
        logSmartspaceCardReported(SMARTSPACE_CARD_CLICK_EVENT);
        return Unit.INSTANCE;
    }

    public void onDestroy() {
        if (this.mSeekBarObserver != null) {
            this.mSeekBarViewModel.getProgress().removeObserver(this.mSeekBarObserver);
        }
        this.mSeekBarViewModel.removeScrubbingChangeListener(this.mScrubbingChangeListener);
        this.mSeekBarViewModel.removeEnabledChangeListener(this.mEnabledChangeListener);
        this.mSeekBarViewModel.onDestroy();
        this.mMediaViewController.onDestroy();
    }

    public MediaViewHolder getMediaViewHolder() {
        return this.mMediaViewHolder;
    }

    public RecommendationViewHolder getRecommendationViewHolder() {
        return this.mRecommendationViewHolder;
    }

    public MediaViewController getMediaViewController() {
        return this.mMediaViewController;
    }

    public void setListening(boolean z) {
        this.mSeekBarViewModel.setListening(z);
    }

    /* access modifiers changed from: private */
    public void setIsScrubbing(boolean z) {
        MediaData mediaData = this.mMediaData;
        if (mediaData != null && mediaData.getSemanticActions() != null && z != this.mIsScrubbing) {
            this.mIsScrubbing = z;
            this.mMainExecutor.execute(new MediaControlPanel$$ExternalSyntheticLambda20(this));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setIsScrubbing$1$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33671xbe2f0b99() {
        updateDisplayForScrubbingChange(this.mMediaData.getSemanticActions());
    }

    /* access modifiers changed from: private */
    public void setIsSeekBarEnabled(boolean z) {
        if (z != this.mIsSeekBarEnabled) {
            this.mIsSeekBarEnabled = z;
            updateSeekBarVisibility();
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public void attachPlayer(MediaViewHolder mediaViewHolder) {
        this.mMediaViewHolder = mediaViewHolder;
        TransitionLayout player = mediaViewHolder.getPlayer();
        this.mSeekBarObserver = new SeekBarObserver(mediaViewHolder);
        this.mSeekBarViewModel.getProgress().observeForever(this.mSeekBarObserver);
        this.mSeekBarViewModel.attachTouchHandlers(mediaViewHolder.getSeekBar());
        this.mSeekBarViewModel.setScrubbingChangeListener(this.mScrubbingChangeListener);
        this.mSeekBarViewModel.setEnabledChangeListener(this.mEnabledChangeListener);
        this.mMediaViewController.attach(player, MediaViewController.TYPE.PLAYER);
        mediaViewHolder.getPlayer().setOnLongClickListener(new MediaControlPanel$$ExternalSyntheticLambda27(this));
        this.mMediaViewHolder.getAlbumView().setLayerType(2, (Paint) null);
        TextView titleText = this.mMediaViewHolder.getTitleText();
        TextView artistText = this.mMediaViewHolder.getArtistText();
        AnimatorSet loadAnimator = loadAnimator(C1894R.anim.media_metadata_enter, Interpolators.EMPHASIZED_DECELERATE, titleText, artistText);
        AnimatorSet loadAnimator2 = loadAnimator(C1894R.anim.media_metadata_exit, Interpolators.EMPHASIZED_ACCELERATE, titleText, artistText);
        this.mColorSchemeTransition = new ColorSchemeTransition(this.mContext, this.mMediaViewHolder);
        this.mMetadataAnimationHandler = new MetadataAnimationHandler(loadAnimator2, loadAnimator);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$attachPlayer$2$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ boolean mo33651xa6105d35(View view) {
        if (!this.mMediaViewController.isGutsVisible()) {
            openGuts();
            return true;
        }
        closeGuts();
        return true;
    }

    /* access modifiers changed from: protected */
    public AnimatorSet loadAnimator(int i, Interpolator interpolator, View... viewArr) {
        ArrayList arrayList = new ArrayList();
        for (View target : viewArr) {
            AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(this.mContext, i);
            animatorSet.getChildAnimations().get(0).setInterpolator(interpolator);
            animatorSet.setTarget(target);
            arrayList.add(animatorSet);
        }
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(arrayList);
        return animatorSet2;
    }

    public void attachRecommendation(RecommendationViewHolder recommendationViewHolder) {
        this.mRecommendationViewHolder = recommendationViewHolder;
        this.mMediaViewController.attach(recommendationViewHolder.getRecommendations(), MediaViewController.TYPE.RECOMMENDATION);
        this.mRecommendationViewHolder.getRecommendations().setOnLongClickListener(new MediaControlPanel$$ExternalSyntheticLambda26(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$attachRecommendation$3$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ boolean mo33652xbc201cfc(View view) {
        if (!this.mMediaViewController.isGutsVisible()) {
            openGuts();
            return true;
        }
        closeGuts();
        return true;
    }

    public void bindPlayer(MediaData mediaData, String str) {
        if (this.mMediaViewHolder != null) {
            Trace.beginSection("MediaControlPanel#bindPlayer<" + str + ">");
            this.mKey = str;
            this.mMediaData = mediaData;
            MediaSession.Token token = mediaData.getToken();
            this.mPackageName = mediaData.getPackageName();
            int appUid = mediaData.getAppUid();
            this.mUid = appUid;
            if (this.mSmartspaceId == -1) {
                this.mSmartspaceId = SmallHash.hash(appUid + ((int) this.mSystemClock.currentTimeMillis()));
            }
            this.mInstanceId = mediaData.getInstanceId();
            MediaSession.Token token2 = this.mToken;
            if (token2 == null || !token2.equals(token)) {
                this.mToken = token;
            }
            if (this.mToken != null) {
                this.mController = new MediaController(this.mContext, this.mToken);
            } else {
                this.mController = null;
            }
            PendingIntent clickIntent = mediaData.getClickIntent();
            if (clickIntent != null) {
                this.mMediaViewHolder.getPlayer().setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda18(this, clickIntent));
            }
            this.mBackgroundExecutor.execute(new MediaControlPanel$$ExternalSyntheticLambda19(this, getController()));
            boolean z = mediaData.getDevice() != null && mediaData.getDevice().getShowBroadcastButton();
            this.mShowBroadcastDialogButton = z;
            bindOutputSwitcherAndBroadcastButton(z, mediaData);
            bindGutsMenuForPlayer(mediaData);
            bindPlayerContentDescription(mediaData);
            bindScrubbingTime(mediaData);
            bindActionButtons(mediaData);
            bindArtworkAndColors(mediaData, str, bindSongMetadata(mediaData));
            if (!this.mMetadataAnimationHandler.isRunning()) {
                this.mMediaViewController.refreshState();
            }
            Trace.endSection();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindPlayer$4$com-android-systemui-media-MediaControlPanel  reason: not valid java name */
    public /* synthetic */ void m2783lambda$bindPlayer$4$comandroidsystemuimediaMediaControlPanel(PendingIntent pendingIntent, View view) {
        if (!this.mFalsingManager.isFalseTap(1) && !this.mMediaViewController.isGutsVisible()) {
            this.mLogger.logTapContentView(this.mUid, this.mPackageName, this.mInstanceId);
            logSmartspaceCardReported(SMARTSPACE_CARD_CLICK_EVENT);
            if (this.mKeyguardStateController.isShowing() && this.mActivityIntentHelper.wouldShowOverLockscreen(pendingIntent.getIntent(), this.mLockscreenUserManager.getCurrentUserId())) {
                this.mActivityStarter.startActivity(pendingIntent.getIntent(), true, (ActivityLaunchAnimator.Controller) null, true);
            } else {
                this.mActivityStarter.postStartActivityDismissingKeyguard(pendingIntent, buildLaunchAnimatorController(this.mMediaViewHolder.getPlayer()));
            }
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindPlayer$5$com-android-systemui-media-MediaControlPanel  reason: not valid java name */
    public /* synthetic */ void m2784lambda$bindPlayer$5$comandroidsystemuimediaMediaControlPanel(MediaController mediaController) {
        this.mSeekBarViewModel.updateController(mediaController);
    }

    private void bindOutputSwitcherAndBroadcastButton(boolean z, MediaData mediaData) {
        CharSequence charSequence;
        int i;
        ViewGroup seamless = this.mMediaViewHolder.getSeamless();
        boolean z2 = false;
        seamless.setVisibility(0);
        ImageView seamlessIcon = this.mMediaViewHolder.getSeamlessIcon();
        TextView seamlessText = this.mMediaViewHolder.getSeamlessText();
        MediaDeviceData device = mediaData.getDevice();
        boolean z3 = true;
        if (z) {
            if (device != null) {
                CharSequence name = device.getName();
                Context context = this.mContext;
                if (TextUtils.equals(name, MediaDataUtils.getAppLabel(context, this.mPackageName, context.getString(C1894R.string.bt_le_audio_broadcast_dialog_unknown_name)))) {
                    z2 = true;
                }
            }
            this.mIsCurrentBroadcastedApp = z2;
            z2 = !z2;
            charSequence = this.mContext.getString(C1894R.string.bt_le_audio_broadcast_dialog_unknown_name);
            i = C1894R.C1896drawable.settings_input_antenna;
        } else {
            if ((device != null && !device.getEnabled()) || mediaData.getResumption()) {
                z2 = true;
            }
            z3 = !z2;
            charSequence = this.mContext.getString(C1894R.string.media_seamless_other_device);
            i = C1894R.C1896drawable.ic_media_home_devices;
        }
        this.mMediaViewHolder.getSeamlessButton().setAlpha(z2 ? 0.38f : 1.0f);
        seamless.setEnabled(z3);
        if (device != null) {
            Drawable icon = device.getIcon();
            if (icon instanceof AdaptiveIcon) {
                AdaptiveIcon adaptiveIcon = (AdaptiveIcon) icon;
                adaptiveIcon.setBackgroundColor(this.mColorSchemeTransition.getBgColor());
                seamlessIcon.setImageDrawable(adaptiveIcon);
            } else {
                seamlessIcon.setImageDrawable(icon);
            }
            if (device.getName() != null) {
                charSequence = device.getName();
            }
        } else {
            seamlessIcon.setImageResource(i);
        }
        seamlessText.setText(charSequence);
        seamless.setContentDescription(charSequence);
        seamless.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda29(this, z, device));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindOutputSwitcherAndBroadcastButton$6$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33660x9729501c(boolean z, MediaDeviceData mediaDeviceData, View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            if (!z) {
                this.mLogger.logOpenOutputSwitcher(this.mUid, this.mPackageName, this.mInstanceId);
                if (mediaDeviceData.getIntent() == null) {
                    this.mMediaOutputDialogFactory.create(this.mPackageName, true, this.mMediaViewHolder.getSeamlessButton());
                } else if (mediaDeviceData.getIntent().isActivity()) {
                    this.mActivityStarter.startActivity(mediaDeviceData.getIntent().getIntent(), true);
                } else {
                    try {
                        mediaDeviceData.getIntent().send();
                    } catch (PendingIntent.CanceledException unused) {
                        Log.e(TAG, "Device pending intent was canceled");
                    }
                }
            } else if (!this.mIsCurrentBroadcastedApp) {
                this.mLogger.logOpenBroadcastDialog(this.mUid, this.mPackageName, this.mInstanceId);
                String charSequence = mediaDeviceData.getName().toString();
                this.mSwitchBroadcastApp = charSequence;
                this.mBroadcastDialogController.createBroadcastDialog(charSequence, this.mPackageName, true, this.mMediaViewHolder.getSeamlessButton());
            } else {
                this.mLogger.logOpenOutputSwitcher(this.mUid, this.mPackageName, this.mInstanceId);
                this.mMediaOutputDialogFactory.create(this.mPackageName, true, this.mMediaViewHolder.getSeamlessButton());
            }
        }
    }

    private void bindGutsMenuForPlayer(MediaData mediaData) {
        bindGutsMenuCommon(mediaData.isClearable(), mediaData.getApp(), this.mMediaViewHolder.getGutsViewHolder(), new MediaControlPanel$$ExternalSyntheticLambda21(this, mediaData));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindGutsMenuForPlayer$7$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33659x4b2cc6cf(MediaData mediaData) {
        if (this.mKey != null) {
            closeGuts();
            if (!this.mMediaDataManagerLazy.get().dismissMediaData(this.mKey, MediaViewController.GUTS_ANIMATION_DURATION + 100)) {
                Log.w(TAG, "Manager failed to dismiss media " + this.mKey);
                this.mMediaCarouselController.removePlayer(this.mKey, false, false);
                return;
            }
            return;
        }
        Log.w(TAG, "Dismiss media with null notification. Token uid=" + mediaData.getToken().getUid());
    }

    private boolean bindSongMetadata(MediaData mediaData) {
        return this.mMetadataAnimationHandler.setNext(Pair.create(mediaData.getSong(), mediaData.getArtist()), new MediaControlPanel$$ExternalSyntheticLambda24(this, this.mMediaViewHolder.getTitleText(), mediaData, this.mMediaViewHolder.getArtistText()), new MediaControlPanel$$ExternalSyntheticLambda25(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindSongMetadata$8$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ Unit mo33666x74421d54(TextView textView, MediaData mediaData, TextView textView2) {
        textView.setText(mediaData.getSong());
        textView2.setText(mediaData.getArtist());
        this.mMediaViewController.refreshState();
        return Unit.INSTANCE;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindSongMetadata$9$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ Unit mo33667x2db9aaf3() {
        this.mMediaViewController.refreshState();
        return Unit.INSTANCE;
    }

    private void bindPlayerContentDescription(MediaData mediaData) {
        CharSequence charSequence;
        if (this.mMediaViewHolder != null) {
            if (this.mMediaViewController.isGutsVisible()) {
                charSequence = this.mMediaViewHolder.getGutsViewHolder().getGutsText().getText();
            } else if (mediaData != null) {
                charSequence = this.mContext.getString(C1894R.string.controls_media_playing_item_description, new Object[]{mediaData.getSong(), mediaData.getArtist(), mediaData.getApp()});
            } else {
                charSequence = null;
            }
            this.mMediaViewHolder.getPlayer().setContentDescription(charSequence);
        }
    }

    private void bindRecommendationContentDescription(SmartspaceMediaData smartspaceMediaData) {
        CharSequence charSequence;
        if (this.mRecommendationViewHolder != null) {
            if (this.mMediaViewController.isGutsVisible()) {
                charSequence = this.mRecommendationViewHolder.getGutsViewHolder().getGutsText().getText();
            } else if (smartspaceMediaData != null) {
                Context context = this.mContext;
                charSequence = context.getString(C1894R.string.controls_media_smartspace_rec_description, new Object[]{smartspaceMediaData.getAppName(context)});
            } else {
                charSequence = null;
            }
            this.mRecommendationViewHolder.getRecommendations().setContentDescription(charSequence);
        }
    }

    private void bindArtworkAndColors(MediaData mediaData, String str, boolean z) {
        int hashCode = mediaData.hashCode();
        String str2 = "MediaControlPanel#bindArtworkAndColors<" + str + ">";
        Trace.beginAsyncSection(str2, hashCode);
        int i = this.mArtworkNextBindRequestId;
        this.mArtworkNextBindRequestId = i + 1;
        if (z) {
            this.mIsArtworkBound = false;
        }
        this.mBackgroundExecutor.execute(new MediaControlPanel$$ExternalSyntheticLambda13(this, mediaData, this.mMediaViewHolder.getAlbumView().getMeasuredWidth(), this.mMediaViewHolder.getAlbumView().getMeasuredHeight(), i, str2, hashCode, z));
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0037  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x007f  */
    /* renamed from: lambda$bindArtworkAndColors$11$com-android-systemui-media-MediaControlPanel */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ void mo33654x8c064b8f(com.android.systemui.media.MediaData r15, int r16, int r17, int r18, java.lang.String r19, int r20, boolean r21) {
        /*
            r14 = this;
            r2 = r14
            android.graphics.drawable.Icon r0 = r15.getArtwork()
            r1 = 1
            r3 = 0
            if (r0 == 0) goto L_0x0033
            int r4 = r0.getType()
            if (r4 == r1) goto L_0x002a
            int r4 = r0.getType()
            r5 = 5
            if (r4 != r5) goto L_0x0017
            goto L_0x002a
        L_0x0017:
            android.content.Context r4 = r2.mContext
            android.graphics.drawable.Drawable r4 = r0.loadDrawable(r4)
            if (r4 == 0) goto L_0x0033
            android.content.Context r4 = r2.mContext
            android.graphics.drawable.Drawable r4 = r0.loadDrawable(r4)
            android.app.WallpaperColors r4 = android.app.WallpaperColors.fromDrawable(r4)
            goto L_0x0034
        L_0x002a:
            android.graphics.Bitmap r4 = r0.getBitmap()
            android.app.WallpaperColors r4 = android.app.WallpaperColors.fromBitmap(r4)
            goto L_0x0034
        L_0x0033:
            r4 = r3
        L_0x0034:
            r5 = 0
            if (r4 == 0) goto L_0x007f
            com.android.systemui.monet.ColorScheme r3 = new com.android.systemui.monet.ColorScheme
            com.android.systemui.monet.Style r6 = com.android.systemui.monet.Style.CONTENT
            r3.<init>((android.app.WallpaperColors) r4, (boolean) r1, (com.android.systemui.monet.Style) r6)
            r10 = r16
            r11 = r17
            android.graphics.drawable.Drawable r0 = r14.getScaledBackground(r0, r10, r11)
            android.content.Context r4 = r2.mContext
            r6 = 2131232764(0x7f0807fc, float:1.8081646E38)
            android.graphics.drawable.Drawable r4 = r4.getDrawable(r6)
            android.graphics.drawable.GradientDrawable r4 = (android.graphics.drawable.GradientDrawable) r4
            r6 = 2
            int[] r7 = new int[r6]
            int r8 = com.android.systemui.media.MediaColorSchemesKt.backgroundStartFromScheme(r3)
            r9 = 1048576000(0x3e800000, float:0.25)
            int r8 = com.android.systemui.util.ColorUtilKt.getColorWithAlpha(r8, r9)
            r7[r5] = r8
            int r8 = com.android.systemui.media.MediaColorSchemesKt.backgroundEndFromScheme(r3)
            r9 = 1063675494(0x3f666666, float:0.9)
            int r8 = com.android.systemui.util.ColorUtilKt.getColorWithAlpha(r8, r9)
            r7[r1] = r8
            r4.setColors(r7)
            android.graphics.drawable.LayerDrawable r7 = new android.graphics.drawable.LayerDrawable
            android.graphics.drawable.Drawable[] r6 = new android.graphics.drawable.Drawable[r6]
            r6[r5] = r0
            r6[r1] = r4
            r7.<init>(r6)
            r8 = r1
            r6 = r3
            r9 = r7
            goto L_0x00be
        L_0x007f:
            r10 = r16
            r11 = r17
            android.graphics.drawable.ColorDrawable r4 = new android.graphics.drawable.ColorDrawable
            r4.<init>(r5)
            android.content.Context r0 = r2.mContext     // Catch:{ NameNotFoundException -> 0x00a2 }
            android.content.pm.PackageManager r0 = r0.getPackageManager()     // Catch:{ NameNotFoundException -> 0x00a2 }
            java.lang.String r6 = r15.getPackageName()     // Catch:{ NameNotFoundException -> 0x00a2 }
            android.graphics.drawable.Drawable r0 = r0.getApplicationIcon(r6)     // Catch:{ NameNotFoundException -> 0x00a2 }
            com.android.systemui.monet.ColorScheme r6 = new com.android.systemui.monet.ColorScheme     // Catch:{ NameNotFoundException -> 0x00a2 }
            android.app.WallpaperColors r0 = android.app.WallpaperColors.fromDrawable(r0)     // Catch:{ NameNotFoundException -> 0x00a2 }
            com.android.systemui.monet.Style r7 = com.android.systemui.monet.Style.CONTENT     // Catch:{ NameNotFoundException -> 0x00a2 }
            r6.<init>((android.app.WallpaperColors) r0, (boolean) r1, (com.android.systemui.monet.Style) r7)     // Catch:{ NameNotFoundException -> 0x00a2 }
            goto L_0x00bc
        L_0x00a2:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r6 = "Cannot find icon for package "
            r1.<init>((java.lang.String) r6)
            java.lang.String r6 = r15.getPackageName()
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r6)
            java.lang.String r1 = r1.toString()
            java.lang.String r6 = "MediaControlPanel"
            android.util.Log.w(r6, r1, r0)
            r6 = r3
        L_0x00bc:
            r9 = r4
            r8 = r5
        L_0x00be:
            java.util.concurrent.Executor r0 = r2.mMainExecutor
            com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda8 r13 = new com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda8
            r1 = r13
            r2 = r14
            r3 = r18
            r4 = r19
            r5 = r20
            r7 = r21
            r10 = r16
            r11 = r17
            r12 = r15
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12)
            r0.execute(r13)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaControlPanel.mo33654x8c064b8f(com.android.systemui.media.MediaData, int, int, int, java.lang.String, int, boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindArtworkAndColors$10$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33653xd28ebdf0(int i, String str, int i2, ColorScheme colorScheme, boolean z, boolean z2, Drawable drawable, int i3, int i4, MediaData mediaData) {
        if (i < this.mArtworkBoundId) {
            Trace.endAsyncSection(str, i2);
            return;
        }
        this.mArtworkBoundId = i;
        boolean updateColorScheme = this.mColorSchemeTransition.updateColorScheme(colorScheme);
        ImageView albumView = this.mMediaViewHolder.getAlbumView();
        albumView.setPadding(0, 0, 0, 0);
        if (z || updateColorScheme || (!this.mIsArtworkBound && z2)) {
            if (this.mPrevArtwork == null) {
                albumView.setImageDrawable(drawable);
            } else {
                TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{this.mPrevArtwork, drawable});
                scaleTransitionDrawableLayer(transitionDrawable, 0, i3, i4);
                scaleTransitionDrawableLayer(transitionDrawable, 1, i3, i4);
                transitionDrawable.setLayerGravity(0, 17);
                transitionDrawable.setLayerGravity(1, 17);
                transitionDrawable.setCrossFadeEnabled(!z2);
                albumView.setImageDrawable(transitionDrawable);
                transitionDrawable.startTransition(z2 ? 333 : 80);
            }
            this.mPrevArtwork = drawable;
            this.mIsArtworkBound = z2;
        }
        ImageView appIcon = this.mMediaViewHolder.getAppIcon();
        appIcon.clearColorFilter();
        if (mediaData.getAppIcon() == null || mediaData.getResumption()) {
            appIcon.setColorFilter(getGrayscaleFilter());
            try {
                appIcon.setImageDrawable(this.mContext.getPackageManager().getApplicationIcon(mediaData.getPackageName()));
            } catch (PackageManager.NameNotFoundException e) {
                Log.w(TAG, "Cannot find icon for package " + mediaData.getPackageName(), e);
                appIcon.setImageResource(C1894R.C1896drawable.ic_music_note);
            }
        } else {
            appIcon.setImageIcon(mediaData.getAppIcon());
            appIcon.setColorFilter(this.mColorSchemeTransition.getAccentPrimary().getTargetColor());
        }
        Trace.endAsyncSection(str, i2);
    }

    private void scaleTransitionDrawableLayer(TransitionDrawable transitionDrawable, int i, int i2, int i3) {
        Drawable drawable = transitionDrawable.getDrawable(i);
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            if (intrinsicWidth != 0 && intrinsicHeight != 0 && i2 != 0 && i3 != 0) {
                float f = (float) intrinsicWidth;
                float f2 = (float) intrinsicHeight;
                float f3 = (float) i2;
                float f4 = (float) i3;
                float f5 = f / f2 > f3 / f4 ? f4 / f2 : f3 / f;
                transitionDrawable.setLayerSize(i, (int) (f * f5), (int) (f5 * f2));
            }
        }
    }

    private void bindActionButtons(MediaData mediaData) {
        MediaButton semanticActions = mediaData.getSemanticActions();
        ArrayList<ImageButton> arrayList = new ArrayList<>();
        for (Integer intValue : MediaViewHolder.Companion.getGenericButtonIds()) {
            arrayList.add(this.mMediaViewHolder.getAction(intValue.intValue()));
        }
        ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
        ConstraintSet collapsedLayout = this.mMediaViewController.getCollapsedLayout();
        if (semanticActions != null) {
            for (ImageButton imageButton : arrayList) {
                setVisibleAndAlpha(collapsedLayout, imageButton.getId(), false);
                setVisibleAndAlpha(expandedLayout, imageButton.getId(), false);
            }
            for (Integer intValue2 : SEMANTIC_ACTIONS_ALL) {
                int intValue3 = intValue2.intValue();
                setSemanticButton(this.mMediaViewHolder.getAction(intValue3), semanticActions.getActionById(intValue3), semanticActions);
            }
        } else {
            for (Integer intValue4 : SEMANTIC_ACTIONS_COMPACT) {
                int intValue5 = intValue4.intValue();
                setVisibleAndAlpha(collapsedLayout, intValue5, false);
                setVisibleAndAlpha(expandedLayout, intValue5, false);
            }
            List<Integer> actionsToShowInCompact = mediaData.getActionsToShowInCompact();
            List<MediaAction> actions = mediaData.getActions();
            int i = 0;
            while (i < actions.size() && i < arrayList.size()) {
                setGenericButton((ImageButton) arrayList.get(i), actions.get(i), collapsedLayout, expandedLayout, actionsToShowInCompact.contains(Integer.valueOf(i)));
                i++;
            }
            while (i < arrayList.size()) {
                setGenericButton((ImageButton) arrayList.get(i), (MediaAction) null, collapsedLayout, expandedLayout, false);
                i++;
            }
        }
        updateSeekBarVisibility();
    }

    private void updateSeekBarVisibility() {
        ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
        expandedLayout.setVisibility(C1894R.C1898id.media_progress_bar, getSeekBarVisibility());
        expandedLayout.setAlpha(C1894R.C1898id.media_progress_bar, this.mIsSeekBarEnabled ? 1.0f : 0.0f);
    }

    private int getSeekBarVisibility() {
        if (this.mIsSeekBarEnabled) {
            return 0;
        }
        return areAnyExpandedBottomActionsVisible() ? 4 : 8;
    }

    private boolean areAnyExpandedBottomActionsVisible() {
        ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
        for (Integer intValue : MediaViewHolder.Companion.getExpandedBottomActionIds()) {
            if (expandedLayout.getVisibility(intValue.intValue()) == 0) {
                return true;
            }
        }
        return false;
    }

    private void setGenericButton(ImageButton imageButton, MediaAction mediaAction, ConstraintSet constraintSet, ConstraintSet constraintSet2, boolean z) {
        bindButtonCommon(imageButton, mediaAction);
        boolean z2 = true;
        boolean z3 = mediaAction != null;
        setVisibleAndAlpha(constraintSet2, imageButton.getId(), z3);
        int id = imageButton.getId();
        if (!z3 || !z) {
            z2 = false;
        }
        setVisibleAndAlpha(constraintSet, id, z2);
    }

    private void setSemanticButton(ImageButton imageButton, MediaAction mediaAction, MediaButton mediaButton) {
        AnimationBindHandler animationBindHandler;
        if (imageButton.getTag() == null) {
            animationBindHandler = new AnimationBindHandler();
            imageButton.setTag(animationBindHandler);
        } else {
            animationBindHandler = (AnimationBindHandler) imageButton.getTag();
        }
        animationBindHandler.tryExecute(new MediaControlPanel$$ExternalSyntheticLambda28(this, imageButton, mediaAction, animationBindHandler, mediaButton));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setSemanticButton$12$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ Unit mo33672xd7690b7e(ImageButton imageButton, MediaAction mediaAction, AnimationBindHandler animationBindHandler, MediaButton mediaButton) {
        bindButtonWithAnimations(imageButton, mediaAction, animationBindHandler);
        setSemanticButtonVisibleAndAlpha(imageButton.getId(), mediaAction, mediaButton);
        return Unit.INSTANCE;
    }

    private void bindButtonWithAnimations(ImageButton imageButton, MediaAction mediaAction, AnimationBindHandler animationBindHandler) {
        if (mediaAction == null) {
            animationBindHandler.unregisterAll();
            clearButton(imageButton);
        } else if (animationBindHandler.updateRebindId(mediaAction.getRebindId())) {
            animationBindHandler.unregisterAll();
            animationBindHandler.tryRegister(mediaAction.getIcon());
            animationBindHandler.tryRegister(mediaAction.getBackground());
            bindButtonCommon(imageButton, mediaAction);
        }
    }

    private void bindButtonCommon(ImageButton imageButton, MediaAction mediaAction) {
        if (mediaAction != null) {
            Drawable icon = mediaAction.getIcon();
            imageButton.setImageDrawable(icon);
            imageButton.setContentDescription(mediaAction.getContentDescription());
            Drawable background = mediaAction.getBackground();
            imageButton.setBackground(background);
            Runnable action = mediaAction.getAction();
            if (action == null) {
                imageButton.setEnabled(false);
                return;
            }
            imageButton.setEnabled(true);
            imageButton.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda16(this, imageButton, action, icon, background));
            return;
        }
        clearButton(imageButton);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindButtonCommon$13$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33655xc7b836f7(ImageButton imageButton, Runnable runnable, Drawable drawable, Drawable drawable2, View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mLogger.logTapAction(imageButton.getId(), this.mUid, this.mPackageName, this.mInstanceId);
            logSmartspaceCardReported(SMARTSPACE_CARD_CLICK_EVENT);
            runnable.run();
            if (drawable instanceof Animatable) {
                ((Animatable) drawable).start();
            }
            if (drawable2 instanceof Animatable) {
                ((Animatable) drawable2).start();
            }
        }
    }

    private void clearButton(ImageButton imageButton) {
        imageButton.setImageDrawable((Drawable) null);
        imageButton.setContentDescription((CharSequence) null);
        imageButton.setEnabled(false);
        imageButton.setBackground((Drawable) null);
    }

    private void setSemanticButtonVisibleAndAlpha(int i, MediaAction mediaAction, MediaButton mediaButton) {
        ConstraintSet collapsedLayout = this.mMediaViewController.getCollapsedLayout();
        ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
        boolean contains = SEMANTIC_ACTIONS_COMPACT.contains(Integer.valueOf(i));
        boolean z = true;
        boolean z2 = mediaAction != null && !(scrubbingTimeViewsEnabled(mediaButton) && SEMANTIC_ACTIONS_HIDE_WHEN_SCRUBBING.contains(Integer.valueOf(i)) && this.mIsScrubbing);
        setVisibleAndAlpha(expandedLayout, i, z2, ((i != C1894R.C1898id.actionPrev || !mediaButton.getReservePrev()) && (i != C1894R.C1898id.actionNext || !mediaButton.getReserveNext())) ? 8 : 4);
        if (!z2 || !contains) {
            z = false;
        }
        setVisibleAndAlpha(collapsedLayout, i, z);
    }

    private void updateDisplayForScrubbingChange(MediaButton mediaButton) {
        bindScrubbingTime(this.mMediaData);
        SEMANTIC_ACTIONS_HIDE_WHEN_SCRUBBING.forEach(new MediaControlPanel$$ExternalSyntheticLambda2(this, mediaButton));
        if (!this.mMetadataAnimationHandler.isRunning()) {
            this.mMediaViewController.refreshState();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateDisplayForScrubbingChange$14$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33674xfbd618f1(MediaButton mediaButton, Integer num) {
        setSemanticButtonVisibleAndAlpha(num.intValue(), mediaButton.getActionById(num.intValue()), mediaButton);
    }

    private void bindScrubbingTime(MediaData mediaData) {
        ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
        ConstraintSet collapsedLayout = this.mMediaViewController.getCollapsedLayout();
        int id = this.mMediaViewHolder.getScrubbingElapsedTimeView().getId();
        int id2 = this.mMediaViewHolder.getScrubbingTotalTimeView().getId();
        boolean z = scrubbingTimeViewsEnabled(mediaData.getSemanticActions()) && this.mIsScrubbing;
        setVisibleAndAlpha(expandedLayout, id, z);
        setVisibleAndAlpha(expandedLayout, id2, z);
        ConstraintSet.Layout layout = expandedLayout.getConstraint(C1894R.C1898id.media_progress_bar).layout;
        if (z) {
            layout.leftToRight = C1894R.C1898id.media_scrubbing_elapsed_time;
            layout.rightToLeft = C1894R.C1898id.media_scrubbing_total_time;
        } else {
            layout.leftToRight = C1894R.C1898id.actionPrev;
            layout.rightToLeft = C1894R.C1898id.actionNext;
        }
        setVisibleAndAlpha(collapsedLayout, id, false);
        setVisibleAndAlpha(collapsedLayout, id2, false);
    }

    private boolean scrubbingTimeViewsEnabled(MediaButton mediaButton) {
        return mediaButton != null && SEMANTIC_ACTIONS_HIDE_WHEN_SCRUBBING.stream().allMatch(new MediaControlPanel$$ExternalSyntheticLambda7(mediaButton));
    }

    static /* synthetic */ boolean lambda$scrubbingTimeViewsEnabled$15(MediaButton mediaButton, Integer num) {
        return mediaButton.getActionById(num.intValue()) != null;
    }

    private ActivityLaunchAnimator.Controller buildLaunchAnimatorController(TransitionLayout transitionLayout) {
        if (transitionLayout.getParent() instanceof ViewGroup) {
            return new GhostedViewLaunchAnimatorController(transitionLayout, 31) {
                /* access modifiers changed from: protected */
                public float getCurrentTopCornerRadius() {
                    return MediaControlPanel.this.mContext.getResources().getDimension(C1894R.dimen.notification_corner_radius);
                }

                /* access modifiers changed from: protected */
                public float getCurrentBottomCornerRadius() {
                    return getCurrentTopCornerRadius();
                }
            };
        }
        Log.wtf(TAG, "Skipping player animation as it is not attached to a ViewGroup", new Exception());
        return null;
    }

    public void bindRecommendation(SmartspaceMediaData smartspaceMediaData) {
        SmartspaceMediaData smartspaceMediaData2 = smartspaceMediaData;
        if (this.mRecommendationViewHolder != null) {
            if (!smartspaceMediaData.isValid()) {
                Log.e(TAG, "Received an invalid recommendation list; returning");
                return;
            }
            Trace.beginSection("MediaControlPanel#bindRecommendation<" + smartspaceMediaData.getPackageName() + ">");
            this.mRecommendationData = smartspaceMediaData2;
            this.mSmartspaceId = SmallHash.hash(smartspaceMediaData.getTargetId());
            this.mPackageName = smartspaceMediaData.getPackageName();
            this.mInstanceId = smartspaceMediaData.getInstanceId();
            try {
                ApplicationInfo applicationInfo = this.mContext.getPackageManager().getApplicationInfo(smartspaceMediaData.getPackageName(), 0);
                this.mUid = applicationInfo.uid;
                CharSequence appName = smartspaceMediaData2.getAppName(this.mContext);
                if (appName == null) {
                    Log.w(TAG, "Fail to get media recommendation's app name");
                    Trace.endSection();
                    return;
                }
                Drawable applicationIcon = this.mContext.getPackageManager().getApplicationIcon(applicationInfo);
                this.mRecommendationViewHolder.getCardIcon().setImageDrawable(applicationIcon);
                fetchAndUpdateRecommendationColors(applicationIcon);
                setSmartspaceRecItemOnClickListener(this.mRecommendationViewHolder.getRecommendations(), smartspaceMediaData.getCardAction(), -1);
                bindRecommendationContentDescription(smartspaceMediaData);
                List<ImageView> mediaCoverItems = this.mRecommendationViewHolder.getMediaCoverItems();
                List<ViewGroup> mediaCoverContainers = this.mRecommendationViewHolder.getMediaCoverContainers();
                List<SmartspaceAction> validRecommendations = smartspaceMediaData.getValidRecommendations();
                boolean z = false;
                boolean z2 = false;
                for (int i = 0; i < 3; i++) {
                    SmartspaceAction smartspaceAction = validRecommendations.get(i);
                    ImageView imageView = mediaCoverItems.get(i);
                    imageView.setImageIcon(smartspaceAction.getIcon());
                    ViewGroup viewGroup = mediaCoverContainers.get(i);
                    setSmartspaceRecItemOnClickListener(viewGroup, smartspaceAction, i);
                    viewGroup.setOnLongClickListener(new MediaControlPanel$$ExternalSyntheticLambda0());
                    String str = "";
                    String string = smartspaceAction.getExtras().getString(KEY_SMARTSPACE_ARTIST_NAME, str);
                    if (string.isEmpty()) {
                        imageView.setContentDescription(this.mContext.getString(C1894R.string.controls_media_smartspace_rec_item_no_artist_description, new Object[]{smartspaceAction.getTitle(), appName}));
                    } else {
                        imageView.setContentDescription(this.mContext.getString(C1894R.string.controls_media_smartspace_rec_item_description, new Object[]{smartspaceAction.getTitle(), string, appName}));
                    }
                    CharSequence title = smartspaceAction.getTitle();
                    z |= !TextUtils.isEmpty(title);
                    this.mRecommendationViewHolder.getMediaTitles().get(i).setText(title);
                    CharSequence subtitle = TextUtils.isEmpty(title) ^ true ? smartspaceAction.getSubtitle() : str;
                    z2 |= !TextUtils.isEmpty(subtitle);
                    this.mRecommendationViewHolder.getMediaSubtitles().get(i).setText(subtitle);
                }
                this.mSmartspaceMediaItemsCount = 3;
                ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
                this.mRecommendationViewHolder.getMediaTitles().forEach(new MediaControlPanel$$ExternalSyntheticLambda11(this, expandedLayout, z));
                this.mRecommendationViewHolder.getMediaSubtitles().forEach(new MediaControlPanel$$ExternalSyntheticLambda22(this, expandedLayout, z2));
                bindGutsMenuCommon(true, appName.toString(), this.mRecommendationViewHolder.getGutsViewHolder(), new MediaControlPanel$$ExternalSyntheticLambda23(this, smartspaceMediaData2));
                this.mController = null;
                MetadataAnimationHandler metadataAnimationHandler = this.mMetadataAnimationHandler;
                if (metadataAnimationHandler == null || !metadataAnimationHandler.isRunning()) {
                    this.mMediaViewController.refreshState();
                }
                Trace.endSection();
            } catch (PackageManager.NameNotFoundException e) {
                Log.w(TAG, "Fail to get media recommendation's app info", e);
                Trace.endSection();
            }
        }
    }

    static /* synthetic */ boolean lambda$bindRecommendation$16(View view) {
        View view2 = (View) view.getParent();
        if (view2 == null) {
            return true;
        }
        view2.performLongClick();
        return true;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindRecommendation$17$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33663x711f16ef(ConstraintSet constraintSet, boolean z, TextView textView) {
        setVisibleAndAlpha(constraintSet, textView.getId(), z);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindRecommendation$18$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33664x2a96a48e(ConstraintSet constraintSet, boolean z, TextView textView) {
        setVisibleAndAlpha(constraintSet, textView.getId(), z);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindRecommendation$19$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33665xe40e322d(SmartspaceMediaData smartspaceMediaData) {
        closeGuts();
        this.mMediaDataManagerLazy.get().dismissSmartspaceRecommendation(smartspaceMediaData.getTargetId(), MediaViewController.GUTS_ANIMATION_DURATION + 100);
        Intent dismissIntent = smartspaceMediaData.getDismissIntent();
        if (dismissIntent == null) {
            Log.w(TAG, "Cannot create dismiss action click action: extras missing dismiss_intent.");
        } else if (dismissIntent.getComponent() == null || !dismissIntent.getComponent().getClassName().equals(EXPORTED_SMARTSPACE_TRAMPOLINE_ACTIVITY_NAME)) {
            this.mBroadcastSender.sendBroadcast(dismissIntent);
        } else {
            this.mContext.startActivity(dismissIntent);
        }
    }

    private void fetchAndUpdateRecommendationColors(Drawable drawable) {
        this.mBackgroundExecutor.execute(new MediaControlPanel$$ExternalSyntheticLambda17(this, drawable));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$fetchAndUpdateRecommendationColors$21$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33669xf09685af(Drawable drawable) {
        this.mMainExecutor.execute(new MediaControlPanel$$ExternalSyntheticLambda6(this, new ColorScheme(WallpaperColors.fromDrawable(drawable), true)));
    }

    /* access modifiers changed from: private */
    /* renamed from: setRecommendationColors */
    public void mo33668x371ef810(ColorScheme colorScheme) {
        if (this.mRecommendationViewHolder != null) {
            int surfaceFromScheme = MediaColorSchemesKt.surfaceFromScheme(colorScheme);
            int textPrimaryFromScheme = MediaColorSchemesKt.textPrimaryFromScheme(colorScheme);
            int textSecondaryFromScheme = MediaColorSchemesKt.textSecondaryFromScheme(colorScheme);
            this.mRecommendationViewHolder.getRecommendations().setBackgroundTintList(ColorStateList.valueOf(surfaceFromScheme));
            this.mRecommendationViewHolder.getMediaTitles().forEach(new MediaControlPanel$$ExternalSyntheticLambda14(textPrimaryFromScheme));
            this.mRecommendationViewHolder.getMediaSubtitles().forEach(new MediaControlPanel$$ExternalSyntheticLambda15(textSecondaryFromScheme));
            this.mRecommendationViewHolder.getGutsViewHolder().setColors(colorScheme);
        }
    }

    private void bindGutsMenuCommon(boolean z, String str, GutsViewHolder gutsViewHolder, Runnable runnable) {
        String str2;
        int i = 0;
        if (z) {
            str2 = this.mContext.getString(C1894R.string.controls_media_close_session, new Object[]{str});
        } else {
            str2 = this.mContext.getString(C1894R.string.controls_media_active_session);
        }
        gutsViewHolder.getGutsText().setText(str2);
        TextView dismissText = gutsViewHolder.getDismissText();
        if (!z) {
            i = 8;
        }
        dismissText.setVisibility(i);
        gutsViewHolder.getDismiss().setEnabled(z);
        gutsViewHolder.getDismiss().setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda3(this, runnable));
        TextView cancelText = gutsViewHolder.getCancelText();
        if (z) {
            cancelText.setBackground(this.mContext.getDrawable(C1894R.C1896drawable.qs_media_outline_button));
        } else {
            cancelText.setBackground(this.mContext.getDrawable(C1894R.C1896drawable.qs_media_solid_button));
        }
        gutsViewHolder.getCancel().setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda4(this));
        gutsViewHolder.setDismissible(z);
        gutsViewHolder.getSettings().setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda5(this));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindGutsMenuCommon$24$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33656x36d43891(Runnable runnable, View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            logSmartspaceCardReported(SMARTSPACE_CARD_DISMISS_EVENT);
            this.mLogger.logLongPressDismiss(this.mUid, this.mPackageName, this.mInstanceId);
            runnable.run();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindGutsMenuCommon$25$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33657xf04bc630(View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            closeGuts();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$bindGutsMenuCommon$26$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33658xa9c353cf(View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mLogger.logLongPressSettings(this.mUid, this.mPackageName, this.mInstanceId);
            this.mActivityStarter.startActivity(SETTINGS_INTENT, true);
        }
    }

    public void closeGuts(boolean z) {
        MediaViewHolder mediaViewHolder = this.mMediaViewHolder;
        if (mediaViewHolder != null) {
            mediaViewHolder.marquee(false, MediaViewController.GUTS_ANIMATION_DURATION);
        } else {
            RecommendationViewHolder recommendationViewHolder = this.mRecommendationViewHolder;
            if (recommendationViewHolder != null) {
                recommendationViewHolder.marquee(false, MediaViewController.GUTS_ANIMATION_DURATION);
            }
        }
        this.mMediaViewController.closeGuts(z);
        if (this.mMediaViewHolder != null) {
            bindPlayerContentDescription(this.mMediaData);
        } else if (this.mRecommendationViewHolder != null) {
            bindRecommendationContentDescription(this.mRecommendationData);
        }
    }

    private void closeGuts() {
        closeGuts(false);
    }

    private void openGuts() {
        MediaViewHolder mediaViewHolder = this.mMediaViewHolder;
        if (mediaViewHolder != null) {
            mediaViewHolder.marquee(true, MediaViewController.GUTS_ANIMATION_DURATION);
        } else {
            RecommendationViewHolder recommendationViewHolder = this.mRecommendationViewHolder;
            if (recommendationViewHolder != null) {
                recommendationViewHolder.marquee(true, MediaViewController.GUTS_ANIMATION_DURATION);
            }
        }
        this.mMediaViewController.openGuts();
        if (this.mMediaViewHolder != null) {
            bindPlayerContentDescription(this.mMediaData);
        } else if (this.mRecommendationViewHolder != null) {
            bindRecommendationContentDescription(this.mRecommendationData);
        }
        this.mLogger.logLongPressOpen(this.mUid, this.mPackageName, this.mInstanceId);
    }

    private Drawable getScaledBackground(Icon icon, int i, int i2) {
        if (icon == null) {
            return null;
        }
        Drawable loadDrawable = icon.loadDrawable(this.mContext);
        Rect rect = new Rect(0, 0, i, i2);
        if (rect.width() > i || rect.height() > i2) {
            rect.offset((int) (-(((float) (rect.width() - i)) / 2.0f)), (int) (-(((float) (rect.height() - i2)) / 2.0f)));
        }
        loadDrawable.setBounds(rect);
        return loadDrawable;
    }

    public MediaController getController() {
        return this.mController;
    }

    public boolean isPlaying() {
        return isPlaying(this.mController);
    }

    /* access modifiers changed from: protected */
    public boolean isPlaying(MediaController mediaController) {
        PlaybackState playbackState;
        if (mediaController == null || (playbackState = mediaController.getPlaybackState()) == null || playbackState.getState() != 3) {
            return false;
        }
        return true;
    }

    private ColorMatrixColorFilter getGrayscaleFilter() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        return new ColorMatrixColorFilter(colorMatrix);
    }

    private void setVisibleAndAlpha(ConstraintSet constraintSet, int i, boolean z) {
        setVisibleAndAlpha(constraintSet, i, z, 8);
    }

    private void setVisibleAndAlpha(ConstraintSet constraintSet, int i, boolean z, int i2) {
        if (z) {
            i2 = 0;
        }
        constraintSet.setVisibility(i, i2);
        constraintSet.setAlpha(i, z ? 1.0f : 0.0f);
    }

    private void setSmartspaceRecItemOnClickListener(View view, SmartspaceAction smartspaceAction, int i) {
        if (view == null || smartspaceAction == null || smartspaceAction.getIntent() == null || smartspaceAction.getIntent().getExtras() == null) {
            Log.e(TAG, "No tap action can be set up");
        } else {
            view.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda1(this, i, smartspaceAction, view));
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setSmartspaceRecItemOnClickListener$27$com-android-systemui-media-MediaControlPanel */
    public /* synthetic */ void mo33673x34ef25b9(int i, SmartspaceAction smartspaceAction, View view, View view2) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            if (i == -1) {
                this.mLogger.logRecommendationCardTap(this.mPackageName, this.mInstanceId);
            } else {
                this.mLogger.logRecommendationItemTap(this.mPackageName, this.mInstanceId, i);
            }
            logSmartspaceCardReported(SMARTSPACE_CARD_CLICK_EVENT, i, this.mSmartspaceMediaItemsCount);
            if (shouldSmartspaceRecItemOpenInForeground(smartspaceAction)) {
                this.mActivityStarter.postStartActivityDismissingKeyguard(smartspaceAction.getIntent(), 0, buildLaunchAnimatorController(this.mRecommendationViewHolder.getRecommendations()));
            } else {
                view.getContext().startActivity(smartspaceAction.getIntent());
            }
            this.mMediaCarouselController.setShouldScrollToActivePlayer(true);
        }
    }

    private boolean shouldSmartspaceRecItemOpenInForeground(SmartspaceAction smartspaceAction) {
        String string;
        if (smartspaceAction == null || smartspaceAction.getIntent() == null || smartspaceAction.getIntent().getExtras() == null || (string = smartspaceAction.getIntent().getExtras().getString(EXTRAS_SMARTSPACE_INTENT)) == null) {
            return false;
        }
        try {
            return Intent.parseUri(string, 1).getBooleanExtra(KEY_SMARTSPACE_OPEN_IN_FOREGROUND, false);
        } catch (URISyntaxException e) {
            Log.wtf(TAG, "Failed to create intent from URI: " + string);
            e.printStackTrace();
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public int getSurfaceForSmartspaceLogging() {
        int currentEndLocation = this.mMediaViewController.getCurrentEndLocation();
        if (currentEndLocation == 1 || currentEndLocation == 0) {
            return 4;
        }
        if (currentEndLocation == 2) {
            return 2;
        }
        return currentEndLocation == 3 ? 5 : 0;
    }

    private void logSmartspaceCardReported(int i) {
        logSmartspaceCardReported(i, 0, 0);
    }

    private void logSmartspaceCardReported(int i, int i2, int i3) {
        this.mMediaCarouselController.logSmartspaceCardReported(i, this.mSmartspaceId, this.mUid, new int[]{getSurfaceForSmartspaceLogging()}, i2, i3);
    }
}
