package com.android.systemui.media;

import android.app.PendingIntent;
import android.app.smartspace.SmartspaceAction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.settingslib.widget.AdaptiveIcon;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.GhostedViewLaunchAnimatorController;
import com.android.systemui.media.MediaViewController;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.util.animation.TransitionLayout;
import dagger.Lazy;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* loaded from: classes.dex */
public class MediaControlPanel {
    private final ActivityStarter mActivityStarter;
    private int mAlbumArtSize;
    private int mBackgroundColor;
    protected final Executor mBackgroundExecutor;
    private Context mContext;
    private MediaController mController;
    private int mDevicePadding;
    private String mKey;
    private KeyguardDismissUtil mKeyguardDismissUtil;
    private MediaCarouselController mMediaCarouselController;
    private Lazy<MediaDataManager> mMediaDataManagerLazy;
    private final MediaOutputDialogFactory mMediaOutputDialogFactory;
    private MediaViewController mMediaViewController;
    private PlayerViewHolder mPlayerViewHolder;
    private RecommendationViewHolder mRecommendationViewHolder;
    private SeekBarObserver mSeekBarObserver;
    private final SeekBarViewModel mSeekBarViewModel;
    private int mSmartspaceMediaItemsCount;
    private MediaSession.Token mToken;
    private static final Intent SETTINGS_INTENT = new Intent("android.settings.ACTION_MEDIA_CONTROLS_SETTINGS");
    static final int[] ACTION_IDS = {R$id.action0, R$id.action1, R$id.action2, R$id.action3, R$id.action4};
    protected int mInstanceId = -1;
    protected int mUid = -1;

    public MediaControlPanel(Context context, Executor executor, ActivityStarter activityStarter, MediaViewController mediaViewController, SeekBarViewModel seekBarViewModel, Lazy<MediaDataManager> lazy, KeyguardDismissUtil keyguardDismissUtil, MediaOutputDialogFactory mediaOutputDialogFactory, MediaCarouselController mediaCarouselController) {
        this.mContext = context;
        this.mBackgroundExecutor = executor;
        this.mActivityStarter = activityStarter;
        this.mSeekBarViewModel = seekBarViewModel;
        this.mMediaViewController = mediaViewController;
        this.mMediaDataManagerLazy = lazy;
        this.mKeyguardDismissUtil = keyguardDismissUtil;
        this.mMediaOutputDialogFactory = mediaOutputDialogFactory;
        this.mMediaCarouselController = mediaCarouselController;
        loadDimens();
        seekBarViewModel.setLogSmartspaceClick(new Function0() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda14
            @Override // kotlin.jvm.functions.Function0
            /* renamed from: invoke */
            public final Object mo1951invoke() {
                Unit lambda$new$0;
                lambda$new$0 = MediaControlPanel.this.lambda$new$0();
                return lambda$new$0;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Unit lambda$new$0() {
        logSmartspaceCardReported(760, false);
        return Unit.INSTANCE;
    }

    public void onDestroy() {
        if (this.mSeekBarObserver != null) {
            this.mSeekBarViewModel.getProgress().removeObserver(this.mSeekBarObserver);
        }
        this.mSeekBarViewModel.onDestroy();
        this.mMediaViewController.onDestroy();
    }

    private void loadDimens() {
        this.mAlbumArtSize = this.mContext.getResources().getDimensionPixelSize(R$dimen.qs_media_album_size);
        this.mDevicePadding = this.mContext.getResources().getDimensionPixelSize(R$dimen.qs_media_album_device_padding);
    }

    public PlayerViewHolder getPlayerViewHolder() {
        return this.mPlayerViewHolder;
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

    public Context getContext() {
        return this.mContext;
    }

    public void attachPlayer(PlayerViewHolder playerViewHolder) {
        this.mPlayerViewHolder = playerViewHolder;
        TransitionLayout player = playerViewHolder.getPlayer();
        this.mSeekBarObserver = new SeekBarObserver(playerViewHolder);
        this.mSeekBarViewModel.getProgress().observeForever(this.mSeekBarObserver);
        this.mSeekBarViewModel.attachTouchHandlers(playerViewHolder.getSeekBar());
        this.mMediaViewController.attach(player, MediaViewController.TYPE.PLAYER);
        this.mPlayerViewHolder.getPlayer().setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda11
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$attachPlayer$1;
                lambda$attachPlayer$1 = MediaControlPanel.this.lambda$attachPlayer$1(view);
                return lambda$attachPlayer$1;
            }
        });
        this.mPlayerViewHolder.getCancel().setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MediaControlPanel.this.lambda$attachPlayer$2(view);
            }
        });
        this.mPlayerViewHolder.getSettings().setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MediaControlPanel.this.lambda$attachPlayer$3(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$attachPlayer$1(View view) {
        if (!this.mMediaViewController.isGutsVisible()) {
            openGuts();
            return true;
        }
        closeGuts();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$attachPlayer$2(View view) {
        closeGuts();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$attachPlayer$3(View view) {
        this.mActivityStarter.startActivity(SETTINGS_INTENT, true);
    }

    public void attachRecommendation(RecommendationViewHolder recommendationViewHolder) {
        this.mRecommendationViewHolder = recommendationViewHolder;
        this.mMediaViewController.attach(recommendationViewHolder.getRecommendations(), MediaViewController.TYPE.RECOMMENDATION);
        this.mRecommendationViewHolder.getRecommendations().setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda10
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                boolean lambda$attachRecommendation$4;
                lambda$attachRecommendation$4 = MediaControlPanel.this.lambda$attachRecommendation$4(view);
                return lambda$attachRecommendation$4;
            }
        });
        this.mRecommendationViewHolder.getCancel().setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MediaControlPanel.this.lambda$attachRecommendation$5(view);
            }
        });
        this.mRecommendationViewHolder.getSettings().setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MediaControlPanel.this.lambda$attachRecommendation$6(view);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ boolean lambda$attachRecommendation$4(View view) {
        if (!this.mMediaViewController.isGutsVisible()) {
            openGuts();
            return true;
        }
        closeGuts();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$attachRecommendation$5(View view) {
        closeGuts();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$attachRecommendation$6(View view) {
        this.mActivityStarter.startActivity(SETTINGS_INTENT, true);
    }

    public void bindPlayer(final MediaData mediaData, final String str) {
        Drawable drawable;
        String string;
        int[] iArr;
        int i;
        if (this.mPlayerViewHolder == null) {
            return;
        }
        this.mKey = str;
        MediaSession.Token token = mediaData.getToken();
        try {
            this.mUid = this.mContext.getPackageManager().getApplicationInfo(mediaData.getPackageName(), 0).uid;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("MediaControlPanel", "Unable to look up package name", e);
        }
        this.mInstanceId = SmallHash.hash(this.mUid);
        this.mBackgroundColor = mediaData.getBackgroundColor();
        MediaSession.Token token2 = this.mToken;
        if (token2 == null || !token2.equals(token)) {
            this.mToken = token;
        }
        if (this.mToken != null) {
            this.mController = new MediaController(this.mContext, this.mToken);
        } else {
            this.mController = null;
        }
        ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
        ConstraintSet collapsedLayout = this.mMediaViewController.getCollapsedLayout();
        final PendingIntent clickIntent = mediaData.getClickIntent();
        if (clickIntent != null) {
            this.mPlayerViewHolder.getPlayer().setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MediaControlPanel.this.lambda$bindPlayer$7(clickIntent, view);
                }
            });
        }
        this.mPlayerViewHolder.getPlayer().setContentDescription(this.mContext.getString(R$string.controls_media_playing_item_description, mediaData.getSong(), mediaData.getArtist(), mediaData.getApp()));
        ImageView albumView = this.mPlayerViewHolder.getAlbumView();
        if (mediaData.getArtwork() != null) {
            Drawable scaleDrawable = scaleDrawable(mediaData.getArtwork());
            albumView.setPadding(0, 0, 0, 0);
            albumView.setImageDrawable(scaleDrawable);
        } else {
            if (mediaData.getDevice() != null && mediaData.getDevice().getIcon() != null) {
                drawable = mediaData.getDevice().getIcon().getConstantState().newDrawable().mutate();
            } else {
                drawable = getContext().getDrawable(R$drawable.ic_headphone);
            }
            drawable.setTintList(ColorStateList.valueOf(this.mBackgroundColor));
            int i2 = this.mDevicePadding;
            albumView.setPadding(i2, i2, i2, i2);
            albumView.setImageDrawable(drawable);
        }
        ImageView appIcon = this.mPlayerViewHolder.getAppIcon();
        appIcon.clearColorFilter();
        if (mediaData.getAppIcon() != null && !mediaData.getResumption()) {
            appIcon.setImageIcon(mediaData.getAppIcon());
            appIcon.setColorFilter(this.mContext.getColor(17170511));
        } else {
            appIcon.setColorFilter(getGrayscaleFilter());
            try {
                appIcon.setImageDrawable(this.mContext.getPackageManager().getApplicationIcon(mediaData.getPackageName()));
            } catch (PackageManager.NameNotFoundException e2) {
                Log.w("MediaControlPanel", "Cannot find icon for package " + mediaData.getPackageName(), e2);
                appIcon.setImageResource(R$drawable.ic_music_note);
            }
        }
        this.mPlayerViewHolder.getTitleText().setText(mediaData.getSong());
        this.mPlayerViewHolder.getArtistText().setText(mediaData.getArtist());
        ViewGroup seamless = this.mPlayerViewHolder.getSeamless();
        seamless.setVisibility(0);
        int i3 = R$id.media_seamless;
        setVisibleAndAlpha(collapsedLayout, i3, true);
        setVisibleAndAlpha(expandedLayout, i3, true);
        seamless.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda6
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MediaControlPanel.this.lambda$bindPlayer$8(mediaData, view);
            }
        });
        ImageView seamlessIcon = this.mPlayerViewHolder.getSeamlessIcon();
        TextView seamlessText = this.mPlayerViewHolder.getSeamlessText();
        MediaDeviceData device = mediaData.getDevice();
        int id = this.mPlayerViewHolder.getSeamless().getId();
        boolean z = (device != null && !device.getEnabled()) || mediaData.getResumption();
        float f = z ? 0.38f : 1.0f;
        expandedLayout.setAlpha(id, f);
        collapsedLayout.setAlpha(id, f);
        this.mPlayerViewHolder.getSeamless().setEnabled(!z);
        if (device != null && device.getEnabled()) {
            Drawable icon = device.getIcon();
            if (icon instanceof AdaptiveIcon) {
                AdaptiveIcon adaptiveIcon = (AdaptiveIcon) icon;
                adaptiveIcon.setBackgroundColor(this.mBackgroundColor);
                seamlessIcon.setImageDrawable(adaptiveIcon);
            } else {
                seamlessIcon.setImageDrawable(icon);
            }
            string = device.getName();
        } else {
            Log.w("MediaControlPanel", "Device is null or not enabled: " + device + ", not binding output chip.");
            seamlessIcon.setImageResource(R$drawable.ic_media_home_devices);
            string = this.mContext.getString(R$string.media_seamless_other_device);
        }
        seamlessText.setText(string);
        seamless.setContentDescription(string);
        List<Integer> actionsToShowInCompact = mediaData.getActionsToShowInCompact();
        List<MediaAction> actions = mediaData.getActions();
        int i4 = 0;
        while (i4 < actions.size()) {
            int[] iArr2 = ACTION_IDS;
            if (i4 >= iArr2.length) {
                break;
            }
            int i5 = iArr2[i4];
            ImageButton action = this.mPlayerViewHolder.getAction(i5);
            MediaAction mediaAction = actions.get(i4);
            action.setImageIcon(mediaAction.getIcon());
            action.setContentDescription(mediaAction.getContentDescription());
            final Runnable action2 = mediaAction.getAction();
            if (action2 == null) {
                action.setEnabled(false);
            } else {
                action.setEnabled(true);
                action.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda8
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        MediaControlPanel.this.lambda$bindPlayer$9(action2, view);
                    }
                });
            }
            setVisibleAndAlpha(collapsedLayout, i5, actionsToShowInCompact.contains(Integer.valueOf(i4)));
            setVisibleAndAlpha(expandedLayout, i5, true);
            i4++;
        }
        while (true) {
            iArr = ACTION_IDS;
            if (i4 >= iArr.length) {
                break;
            }
            setVisibleAndAlpha(collapsedLayout, iArr[i4], false);
            setVisibleAndAlpha(expandedLayout, iArr[i4], false);
            i4++;
        }
        if (actions.size() == 0) {
            expandedLayout.setVisibility(iArr[0], 4);
        }
        final MediaController controller = getController();
        this.mBackgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda13
            @Override // java.lang.Runnable
            public final void run() {
                MediaControlPanel.this.lambda$bindPlayer$10(controller);
            }
        });
        boolean isClearable = mediaData.isClearable();
        TextView longPressText = this.mPlayerViewHolder.getLongPressText();
        if (isClearable) {
            i = R$string.controls_media_close_session;
        } else {
            i = R$string.controls_media_active_session;
        }
        longPressText.setText(i);
        this.mPlayerViewHolder.getDismissLabel().setAlpha(isClearable ? 1.0f : 0.38f);
        this.mPlayerViewHolder.getDismiss().setEnabled(isClearable);
        this.mPlayerViewHolder.getDismiss().setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda9
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MediaControlPanel.this.lambda$bindPlayer$11(str, mediaData, view);
            }
        });
        this.mMediaViewController.refreshState();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$bindPlayer$7(PendingIntent pendingIntent, View view) {
        if (this.mMediaViewController.isGutsVisible()) {
            return;
        }
        logSmartspaceCardReported(760, false);
        this.mActivityStarter.postStartActivityDismissingKeyguard(pendingIntent, buildLaunchAnimatorController(this.mPlayerViewHolder.getPlayer()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$bindPlayer$8(MediaData mediaData, View view) {
        this.mMediaOutputDialogFactory.create(mediaData.getPackageName(), true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$bindPlayer$9(Runnable runnable, View view) {
        logSmartspaceCardReported(760, false);
        runnable.run();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$bindPlayer$10(MediaController mediaController) {
        this.mSeekBarViewModel.updateController(mediaController);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$bindPlayer$11(String str, MediaData mediaData, View view) {
        logSmartspaceCardReported(761, false);
        if (this.mKey != null) {
            closeGuts();
            if (this.mMediaDataManagerLazy.get().dismissMediaData(this.mKey, MediaViewController.GUTS_ANIMATION_DURATION + 100)) {
                return;
            }
            Log.w("MediaControlPanel", "Manager failed to dismiss media " + this.mKey);
            this.mMediaCarouselController.removePlayer(str, false, false);
            return;
        }
        Log.w("MediaControlPanel", "Dismiss media with null notification. Token uid=" + mediaData.getToken().getUid());
    }

    private ActivityLaunchAnimator.Controller buildLaunchAnimatorController(final TransitionLayout transitionLayout) {
        if (!(transitionLayout.getParent() instanceof ViewGroup)) {
            Log.wtf("MediaControlPanel", "Skipping player animation as it is not attached to a ViewGroup", new Exception());
            return null;
        }
        return new GhostedViewLaunchAnimatorController(transitionLayout, 31) { // from class: com.android.systemui.media.MediaControlPanel.1
            @Override // com.android.systemui.animation.GhostedViewLaunchAnimatorController
            protected float getCurrentTopCornerRadius() {
                return ((IlluminationDrawable) transitionLayout.getBackground()).getCornerRadius();
            }

            @Override // com.android.systemui.animation.GhostedViewLaunchAnimatorController
            protected float getCurrentBottomCornerRadius() {
                return getCurrentTopCornerRadius();
            }

            @Override // com.android.systemui.animation.GhostedViewLaunchAnimatorController
            protected void setBackgroundCornerRadius(Drawable drawable, float f, float f2) {
                ((IlluminationDrawable) drawable).setCornerRadiusOverride(Float.valueOf(Math.min(f, f2)));
            }

            @Override // com.android.systemui.animation.GhostedViewLaunchAnimatorController, com.android.systemui.animation.ActivityLaunchAnimator.Controller
            public void onLaunchAnimationEnd(boolean z) {
                super.onLaunchAnimationEnd(z);
                ((IlluminationDrawable) transitionLayout.getBackground()).setCornerRadiusOverride(null);
            }
        };
    }

    public void bindRecommendation(final SmartspaceMediaData smartspaceMediaData) {
        List<ImageView> list;
        List<SmartspaceAction> list2;
        List<ViewGroup> list3;
        int i;
        int i2;
        boolean z;
        boolean z2;
        boolean z3;
        if (this.mRecommendationViewHolder == null) {
            return;
        }
        this.mInstanceId = SmallHash.hash(smartspaceMediaData.getTargetId());
        this.mBackgroundColor = smartspaceMediaData.getBackgroundColor();
        TransitionLayout recommendations = this.mRecommendationViewHolder.getRecommendations();
        recommendations.setBackgroundTintList(ColorStateList.valueOf(this.mBackgroundColor));
        List<SmartspaceAction> recommendations2 = smartspaceMediaData.getRecommendations();
        if (recommendations2 == null || recommendations2.isEmpty()) {
            Log.w("MediaControlPanel", "Empty media recommendations");
            return;
        }
        try {
            boolean z4 = false;
            ApplicationInfo applicationInfo = this.mContext.getPackageManager().getApplicationInfo(smartspaceMediaData.getPackageName(), 0);
            this.mUid = applicationInfo.uid;
            PackageManager packageManager = this.mContext.getPackageManager();
            Drawable applicationIcon = packageManager.getApplicationIcon(applicationInfo);
            applicationIcon.setColorFilter(getGrayscaleFilter());
            this.mRecommendationViewHolder.getCardIcon().setImageDrawable(applicationIcon);
            CharSequence applicationLabel = packageManager.getApplicationLabel(applicationInfo);
            if (applicationLabel.length() != 0) {
                this.mRecommendationViewHolder.getCardText().setText(applicationLabel);
            }
            setSmartspaceRecItemOnClickListener(recommendations, smartspaceMediaData.getCardAction(), -1);
            boolean z5 = true;
            recommendations.setContentDescription(this.mContext.getString(R$string.controls_media_smartspace_rec_description, applicationLabel));
            List<ImageView> mediaCoverItems = this.mRecommendationViewHolder.getMediaCoverItems();
            List<ViewGroup> mediaCoverContainers = this.mRecommendationViewHolder.getMediaCoverContainers();
            List<Integer> mediaCoverItemsResIds = this.mRecommendationViewHolder.getMediaCoverItemsResIds();
            List<Integer> mediaCoverContainersResIds = this.mRecommendationViewHolder.getMediaCoverContainersResIds();
            ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
            ConstraintSet collapsedLayout = this.mMediaViewController.getCollapsedLayout();
            int min = Math.min(recommendations2.size(), 6);
            int i3 = 0;
            int i4 = 0;
            while (i3 < min && i4 < min) {
                SmartspaceAction smartspaceAction = recommendations2.get(i3);
                if (smartspaceAction.getIcon() == null) {
                    Log.w("MediaControlPanel", "No media cover is provided. Skipping this item...");
                    list = mediaCoverItems;
                    list2 = recommendations2;
                    list3 = mediaCoverContainers;
                    z2 = z4;
                    z3 = z5;
                    i = min;
                } else {
                    ImageView imageView = mediaCoverItems.get(i4);
                    imageView.setImageIcon(smartspaceAction.getIcon());
                    ViewGroup viewGroup = mediaCoverContainers.get(i4);
                    setSmartspaceRecItemOnClickListener(viewGroup, smartspaceAction, i4);
                    list = mediaCoverItems;
                    viewGroup.setOnLongClickListener(MediaControlPanel$$ExternalSyntheticLambda12.INSTANCE);
                    list2 = recommendations2;
                    String string = smartspaceAction.getExtras().getString("artist_name", "");
                    if (string.isEmpty()) {
                        z = true;
                        imageView.setContentDescription(this.mContext.getString(R$string.controls_media_smartspace_rec_item_no_artist_description, smartspaceAction.getTitle(), applicationLabel));
                        list3 = mediaCoverContainers;
                        i = min;
                        i2 = 3;
                    } else {
                        list3 = mediaCoverContainers;
                        i = min;
                        i2 = 3;
                        z = true;
                        imageView.setContentDescription(this.mContext.getString(R$string.controls_media_smartspace_rec_item_description, smartspaceAction.getTitle(), string, applicationLabel));
                    }
                    if (i4 < i2) {
                        setVisibleAndAlpha(collapsedLayout, mediaCoverItemsResIds.get(i4).intValue(), z);
                        setVisibleAndAlpha(collapsedLayout, mediaCoverContainersResIds.get(i4).intValue(), z);
                        z2 = false;
                    } else {
                        z2 = false;
                        setVisibleAndAlpha(collapsedLayout, mediaCoverItemsResIds.get(i4).intValue(), false);
                        setVisibleAndAlpha(collapsedLayout, mediaCoverContainersResIds.get(i4).intValue(), false);
                    }
                    z3 = true;
                    setVisibleAndAlpha(expandedLayout, mediaCoverItemsResIds.get(i4).intValue(), true);
                    setVisibleAndAlpha(expandedLayout, mediaCoverContainersResIds.get(i4).intValue(), true);
                    i4++;
                }
                i3++;
                z4 = z2;
                z5 = z3;
                mediaCoverItems = list;
                recommendations2 = list2;
                min = i;
                mediaCoverContainers = list3;
            }
            this.mSmartspaceMediaItemsCount = i4;
            this.mRecommendationViewHolder.getDismiss().setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda7
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MediaControlPanel.this.lambda$bindRecommendation$13(smartspaceMediaData, view);
                }
            });
            this.mController = null;
            this.mMediaViewController.refreshState();
        } catch (PackageManager.NameNotFoundException e) {
            Log.w("MediaControlPanel", "Fail to get media recommendation's app info", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$bindRecommendation$12(View view) {
        View view2 = (View) view.getParent();
        if (view2 != null) {
            view2.performLongClick();
            return true;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$bindRecommendation$13(SmartspaceMediaData smartspaceMediaData, View view) {
        logSmartspaceCardReported(761, true);
        closeGuts();
        this.mMediaDataManagerLazy.get().dismissSmartspaceRecommendation(smartspaceMediaData.getTargetId(), MediaViewController.GUTS_ANIMATION_DURATION + 100);
        Intent dismissIntent = smartspaceMediaData.getDismissIntent();
        if (dismissIntent == null) {
            Log.w("MediaControlPanel", "Cannot create dismiss action click action: extras missing dismiss_intent.");
        } else if (dismissIntent.getComponent() != null && dismissIntent.getComponent().getClassName().equals("com.google.android.apps.gsa.staticplugins.opa.smartspace.ExportedSmartspaceTrampolineActivity")) {
            this.mContext.startActivity(dismissIntent);
        } else {
            this.mContext.sendBroadcast(dismissIntent);
        }
    }

    public void closeGuts(boolean z) {
        PlayerViewHolder playerViewHolder = this.mPlayerViewHolder;
        if (playerViewHolder != null) {
            playerViewHolder.marquee(false, MediaViewController.GUTS_ANIMATION_DURATION);
        } else {
            RecommendationViewHolder recommendationViewHolder = this.mRecommendationViewHolder;
            if (recommendationViewHolder != null) {
                recommendationViewHolder.marquee(false, MediaViewController.GUTS_ANIMATION_DURATION);
            }
        }
        this.mMediaViewController.closeGuts(z);
    }

    private void closeGuts() {
        closeGuts(false);
    }

    private void openGuts() {
        Layout layout;
        ConstraintSet expandedLayout = this.mMediaViewController.getExpandedLayout();
        ConstraintSet collapsedLayout = this.mMediaViewController.getCollapsedLayout();
        PlayerViewHolder playerViewHolder = this.mPlayerViewHolder;
        boolean z = true;
        if (playerViewHolder != null) {
            playerViewHolder.marquee(true, MediaViewController.GUTS_ANIMATION_DURATION);
            layout = this.mPlayerViewHolder.getSettingsText().getLayout();
        } else {
            RecommendationViewHolder recommendationViewHolder = this.mRecommendationViewHolder;
            if (recommendationViewHolder != null) {
                recommendationViewHolder.marquee(true, MediaViewController.GUTS_ANIMATION_DURATION);
                layout = this.mRecommendationViewHolder.getSettingsText().getLayout();
            } else {
                layout = null;
            }
        }
        if (layout == null || layout.getEllipsisCount(0) <= 0) {
            z = false;
        }
        this.mMediaViewController.setShouldHideGutsSettings(z);
        if (z) {
            int i = R$id.settings;
            expandedLayout.constrainMaxWidth(i, 0);
            collapsedLayout.constrainMaxWidth(i, 0);
        }
        this.mMediaViewController.openGuts();
    }

    private Drawable scaleDrawable(Icon icon) {
        Rect rect;
        if (icon == null) {
            return null;
        }
        Drawable loadDrawable = icon.loadDrawable(this.mContext);
        float intrinsicHeight = loadDrawable.getIntrinsicHeight() / loadDrawable.getIntrinsicWidth();
        if (intrinsicHeight > 1.0f) {
            int i = this.mAlbumArtSize;
            rect = new Rect(0, 0, i, (int) (i * intrinsicHeight));
        } else {
            int i2 = this.mAlbumArtSize;
            rect = new Rect(0, 0, (int) (i2 / intrinsicHeight), i2);
        }
        if (rect.width() > this.mAlbumArtSize || rect.height() > this.mAlbumArtSize) {
            rect.offset((int) (-((rect.width() - this.mAlbumArtSize) / 2.0f)), (int) (-((rect.height() - this.mAlbumArtSize) / 2.0f)));
        }
        loadDrawable.setBounds(rect);
        return loadDrawable;
    }

    public MediaController getController() {
        return this.mController;
    }

    private ColorMatrixColorFilter getGrayscaleFilter() {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        return new ColorMatrixColorFilter(colorMatrix);
    }

    private void setVisibleAndAlpha(ConstraintSet constraintSet, int i, boolean z) {
        constraintSet.setVisibility(i, z ? 0 : 8);
        constraintSet.setAlpha(i, z ? 1.0f : 0.0f);
    }

    private void setSmartspaceRecItemOnClickListener(final View view, final SmartspaceAction smartspaceAction, final int i) {
        if (view == null || smartspaceAction == null || smartspaceAction.getIntent() == null || smartspaceAction.getIntent().getExtras() == null) {
            Log.e("MediaControlPanel", "No tap action can be set up");
        } else {
            view.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    MediaControlPanel.this.lambda$setSmartspaceRecItemOnClickListener$14(i, smartspaceAction, view, view2);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setSmartspaceRecItemOnClickListener$14(int i, SmartspaceAction smartspaceAction, View view, View view2) {
        logSmartspaceCardReported(760, true, i, getSmartspaceSubCardCardinality());
        if (shouldSmartspaceRecItemOpenInForeground(smartspaceAction)) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(smartspaceAction.getIntent(), 0, buildLaunchAnimatorController(this.mRecommendationViewHolder.getRecommendations()));
        } else {
            view.getContext().startActivity(smartspaceAction.getIntent());
        }
        this.mMediaCarouselController.setShouldScrollToActivePlayer(true);
    }

    private boolean shouldSmartspaceRecItemOpenInForeground(SmartspaceAction smartspaceAction) {
        String string;
        if (smartspaceAction == null || smartspaceAction.getIntent() == null || smartspaceAction.getIntent().getExtras() == null || (string = smartspaceAction.getIntent().getExtras().getString("com.google.android.apps.gsa.smartspace.extra.SMARTSPACE_INTENT")) == null) {
            return false;
        }
        try {
            return Intent.parseUri(string, 1).getBooleanExtra("KEY_OPEN_IN_FOREGROUND", false);
        } catch (URISyntaxException e) {
            Log.wtf("MediaControlPanel", "Failed to create intent from URI: " + string);
            e.printStackTrace();
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getSurfaceForSmartspaceLogging() {
        int currentEndLocation = this.mMediaViewController.getCurrentEndLocation();
        if (currentEndLocation == 1 || currentEndLocation == 0) {
            return 4;
        }
        return currentEndLocation == 2 ? 2 : 0;
    }

    private void logSmartspaceCardReported(int i, boolean z) {
        logSmartspaceCardReported(i, z, 0, 0);
    }

    private void logSmartspaceCardReported(int i, boolean z, int i2, int i3) {
        this.mMediaCarouselController.logSmartspaceCardReported(i, this.mInstanceId, this.mUid, z, getSurfaceForSmartspaceLogging(), i2, i3);
    }

    private int getSmartspaceSubCardCardinality() {
        if (this.mMediaCarouselController.getMediaCarouselScrollHandler().getQsExpanded() || this.mSmartspaceMediaItemsCount <= 3) {
            return this.mSmartspaceMediaItemsCount;
        }
        return 3;
    }
}
