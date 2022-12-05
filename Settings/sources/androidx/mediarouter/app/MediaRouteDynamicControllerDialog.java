package androidx.mediarouter.app;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDialog;
import androidx.core.util.ObjectsCompat;
import androidx.mediarouter.R$dimen;
import androidx.mediarouter.R$id;
import androidx.mediarouter.R$integer;
import androidx.mediarouter.R$layout;
import androidx.mediarouter.R$string;
import androidx.mediarouter.media.MediaRouteProvider;
import androidx.mediarouter.media.MediaRouteSelector;
import androidx.mediarouter.media.MediaRouter;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/* loaded from: classes.dex */
public class MediaRouteDynamicControllerDialog extends AppCompatDialog {
    static final boolean DEBUG = Log.isLoggable("MediaRouteCtrlDialog", 3);
    RecyclerAdapter mAdapter;
    int mArtIconBackgroundColor;
    Bitmap mArtIconBitmap;
    boolean mArtIconIsLoaded;
    Bitmap mArtIconLoadedBitmap;
    Uri mArtIconUri;
    ImageView mArtView;
    private boolean mAttachedToWindow;
    private final MediaRouterCallback mCallback;
    private ImageButton mCloseButton;
    Context mContext;
    MediaControllerCallback mControllerCallback;
    private boolean mCreated;
    MediaDescriptionCompat mDescription;
    FetchArtTask mFetchArtTask;
    final List<MediaRouter.RouteInfo> mGroupableRoutes;
    final Handler mHandler;
    boolean mIsAnimatingVolumeSliderLayout;
    boolean mIsSelectingRoute;
    private long mLastUpdateTime;
    MediaControllerCompat mMediaController;
    final List<MediaRouter.RouteInfo> mMemberRoutes;
    private ImageView mMetadataBackground;
    private View mMetadataBlackScrim;
    RecyclerView mRecyclerView;
    MediaRouter.RouteInfo mRouteForVolumeUpdatingByUser;
    final MediaRouter mRouter;
    MediaRouter.RouteInfo mSelectedRoute;
    private MediaRouteSelector mSelector;
    private Button mStopCastingButton;
    private TextView mSubtitleView;
    private String mTitlePlaceholder;
    private TextView mTitleView;
    final List<MediaRouter.RouteInfo> mTransferableRoutes;
    final List<MediaRouter.RouteInfo> mUngroupableRoutes;
    Map<String, Integer> mUnmutedVolumeMap;
    private boolean mUpdateMetadataViewsDeferred;
    private boolean mUpdateRoutesViewDeferred;
    VolumeChangeListener mVolumeChangeListener;
    Map<String, MediaRouteVolumeSliderHolder> mVolumeSliderHolderMap;

    public MediaRouteDynamicControllerDialog(Context context) {
        this(context, 0);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public MediaRouteDynamicControllerDialog(Context context, int theme) {
        super(r2, MediaRouterThemeHelper.createThemedDialogStyle(r2));
        Context createThemedDialogContext = MediaRouterThemeHelper.createThemedDialogContext(context, theme, false);
        this.mSelector = MediaRouteSelector.EMPTY;
        this.mMemberRoutes = new ArrayList();
        this.mGroupableRoutes = new ArrayList();
        this.mTransferableRoutes = new ArrayList();
        this.mUngroupableRoutes = new ArrayList();
        this.mHandler = new Handler() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.1
            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i = message.what;
                if (i == 1) {
                    MediaRouteDynamicControllerDialog.this.updateRoutesView();
                } else if (i != 2) {
                } else {
                    MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
                    if (mediaRouteDynamicControllerDialog.mRouteForVolumeUpdatingByUser == null) {
                        return;
                    }
                    mediaRouteDynamicControllerDialog.mRouteForVolumeUpdatingByUser = null;
                    mediaRouteDynamicControllerDialog.updateViewsIfNeeded();
                }
            }
        };
        Context context2 = getContext();
        this.mContext = context2;
        MediaRouter mediaRouter = MediaRouter.getInstance(context2);
        this.mRouter = mediaRouter;
        this.mCallback = new MediaRouterCallback();
        this.mSelectedRoute = mediaRouter.getSelectedRoute();
        this.mControllerCallback = new MediaControllerCallback();
        setMediaSession(mediaRouter.getMediaSessionToken());
    }

    private void setMediaSession(MediaSessionCompat.Token sessionToken) {
        MediaControllerCompat mediaControllerCompat = this.mMediaController;
        MediaDescriptionCompat mediaDescriptionCompat = null;
        if (mediaControllerCompat != null) {
            mediaControllerCompat.unregisterCallback(this.mControllerCallback);
            this.mMediaController = null;
        }
        if (sessionToken != null && this.mAttachedToWindow) {
            MediaControllerCompat mediaControllerCompat2 = new MediaControllerCompat(this.mContext, sessionToken);
            this.mMediaController = mediaControllerCompat2;
            mediaControllerCompat2.registerCallback(this.mControllerCallback);
            MediaMetadataCompat metadata = this.mMediaController.getMetadata();
            if (metadata != null) {
                mediaDescriptionCompat = metadata.getDescription();
            }
            this.mDescription = mediaDescriptionCompat;
            reloadIconIfNeeded();
            updateMetadataViews();
        }
    }

    public void setRouteSelector(MediaRouteSelector selector) {
        if (selector == null) {
            throw new IllegalArgumentException("selector must not be null");
        }
        if (this.mSelector.equals(selector)) {
            return;
        }
        this.mSelector = selector;
        if (!this.mAttachedToWindow) {
            return;
        }
        this.mRouter.removeCallback(this.mCallback);
        this.mRouter.addCallback(selector, this.mCallback, 1);
        updateRoutes();
    }

    public void onFilterRoutes(List<MediaRouter.RouteInfo> routes) {
        for (int size = routes.size() - 1; size >= 0; size--) {
            if (!onFilterRoute(routes.get(size))) {
                routes.remove(size);
            }
        }
    }

    public boolean onFilterRoute(MediaRouter.RouteInfo route) {
        return !route.isDefaultOrBluetooth() && route.isEnabled() && route.matchesSelector(this.mSelector) && this.mSelectedRoute != route;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R$layout.mr_cast_dialog);
        MediaRouterThemeHelper.setDialogBackgroundColor(this.mContext, this);
        ImageButton imageButton = (ImageButton) findViewById(R$id.mr_cast_close_button);
        this.mCloseButton = imageButton;
        imageButton.setColorFilter(-1);
        this.mCloseButton.setOnClickListener(new View.OnClickListener() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                MediaRouteDynamicControllerDialog.this.dismiss();
            }
        });
        Button button = (Button) findViewById(R$id.mr_cast_stop_button);
        this.mStopCastingButton = button;
        button.setTextColor(-1);
        this.mStopCastingButton.setOnClickListener(new View.OnClickListener() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                if (MediaRouteDynamicControllerDialog.this.mSelectedRoute.isSelected()) {
                    MediaRouteDynamicControllerDialog.this.mRouter.unselect(2);
                }
                MediaRouteDynamicControllerDialog.this.dismiss();
            }
        });
        this.mAdapter = new RecyclerAdapter();
        RecyclerView recyclerView = (RecyclerView) findViewById(R$id.mr_cast_list);
        this.mRecyclerView = recyclerView;
        recyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        this.mVolumeChangeListener = new VolumeChangeListener();
        this.mVolumeSliderHolderMap = new HashMap();
        this.mUnmutedVolumeMap = new HashMap();
        this.mMetadataBackground = (ImageView) findViewById(R$id.mr_cast_meta_background);
        this.mMetadataBlackScrim = findViewById(R$id.mr_cast_meta_black_scrim);
        this.mArtView = (ImageView) findViewById(R$id.mr_cast_meta_art);
        TextView textView = (TextView) findViewById(R$id.mr_cast_meta_title);
        this.mTitleView = textView;
        textView.setTextColor(-1);
        TextView textView2 = (TextView) findViewById(R$id.mr_cast_meta_subtitle);
        this.mSubtitleView = textView2;
        textView2.setTextColor(-1);
        this.mTitlePlaceholder = this.mContext.getResources().getString(R$string.mr_cast_dialog_title_view_placeholder);
        this.mCreated = true;
        updateLayout();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateLayout() {
        getWindow().setLayout(MediaRouteDialogHelper.getDialogWidthForDynamicGroup(this.mContext), MediaRouteDialogHelper.getDialogHeight(this.mContext));
        this.mArtIconBitmap = null;
        this.mArtIconUri = null;
        reloadIconIfNeeded();
        updateMetadataViews();
        updateRoutesView();
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttachedToWindow = true;
        this.mRouter.addCallback(this.mSelector, this.mCallback, 1);
        updateRoutes();
        setMediaSession(this.mRouter.getMediaSessionToken());
    }

    @Override // android.app.Dialog, android.view.Window.Callback
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mAttachedToWindow = false;
        this.mRouter.removeCallback(this.mCallback);
        this.mHandler.removeCallbacksAndMessages(null);
        setMediaSession(null);
    }

    static boolean isBitmapRecycled(Bitmap bitmap) {
        return bitmap != null && bitmap.isRecycled();
    }

    void reloadIconIfNeeded() {
        MediaDescriptionCompat mediaDescriptionCompat = this.mDescription;
        Uri uri = null;
        Bitmap iconBitmap = mediaDescriptionCompat == null ? null : mediaDescriptionCompat.getIconBitmap();
        MediaDescriptionCompat mediaDescriptionCompat2 = this.mDescription;
        if (mediaDescriptionCompat2 != null) {
            uri = mediaDescriptionCompat2.getIconUri();
        }
        FetchArtTask fetchArtTask = this.mFetchArtTask;
        Bitmap iconBitmap2 = fetchArtTask == null ? this.mArtIconBitmap : fetchArtTask.getIconBitmap();
        FetchArtTask fetchArtTask2 = this.mFetchArtTask;
        Uri iconUri = fetchArtTask2 == null ? this.mArtIconUri : fetchArtTask2.getIconUri();
        if (iconBitmap2 != iconBitmap || (iconBitmap2 == null && !ObjectsCompat.equals(iconUri, uri))) {
            FetchArtTask fetchArtTask3 = this.mFetchArtTask;
            if (fetchArtTask3 != null) {
                fetchArtTask3.cancel(true);
            }
            FetchArtTask fetchArtTask4 = new FetchArtTask();
            this.mFetchArtTask = fetchArtTask4;
            fetchArtTask4.execute(new Void[0]);
        }
    }

    void clearLoadedBitmap() {
        this.mArtIconIsLoaded = false;
        this.mArtIconLoadedBitmap = null;
        this.mArtIconBackgroundColor = 0;
    }

    private boolean shouldDeferUpdateViews() {
        if (this.mRouteForVolumeUpdatingByUser != null || this.mIsSelectingRoute || this.mIsAnimatingVolumeSliderLayout) {
            return true;
        }
        return !this.mCreated;
    }

    void updateViewsIfNeeded() {
        if (this.mUpdateRoutesViewDeferred) {
            updateRoutesView();
        }
        if (this.mUpdateMetadataViewsDeferred) {
            updateMetadataViews();
        }
    }

    void updateMetadataViews() {
        if (shouldDeferUpdateViews()) {
            this.mUpdateMetadataViewsDeferred = true;
            return;
        }
        this.mUpdateMetadataViewsDeferred = false;
        if (!this.mSelectedRoute.isSelected() || this.mSelectedRoute.isDefaultOrBluetooth()) {
            dismiss();
        }
        CharSequence charSequence = null;
        if (this.mArtIconIsLoaded && !isBitmapRecycled(this.mArtIconLoadedBitmap) && this.mArtIconLoadedBitmap != null) {
            this.mArtView.setVisibility(0);
            this.mArtView.setImageBitmap(this.mArtIconLoadedBitmap);
            this.mArtView.setBackgroundColor(this.mArtIconBackgroundColor);
            this.mMetadataBlackScrim.setVisibility(0);
            if (Build.VERSION.SDK_INT >= 17) {
                this.mMetadataBackground.setImageBitmap(blurBitmap(this.mArtIconLoadedBitmap, 10.0f, this.mContext));
            } else {
                this.mMetadataBackground.setImageBitmap(Bitmap.createBitmap(this.mArtIconLoadedBitmap));
            }
        } else {
            if (isBitmapRecycled(this.mArtIconLoadedBitmap)) {
                Log.w("MediaRouteCtrlDialog", "Can't set artwork image with recycled bitmap: " + this.mArtIconLoadedBitmap);
            }
            this.mArtView.setVisibility(8);
            this.mMetadataBlackScrim.setVisibility(8);
            this.mMetadataBackground.setImageBitmap(null);
        }
        clearLoadedBitmap();
        MediaDescriptionCompat mediaDescriptionCompat = this.mDescription;
        CharSequence title = mediaDescriptionCompat == null ? null : mediaDescriptionCompat.getTitle();
        boolean z = !TextUtils.isEmpty(title);
        MediaDescriptionCompat mediaDescriptionCompat2 = this.mDescription;
        if (mediaDescriptionCompat2 != null) {
            charSequence = mediaDescriptionCompat2.getSubtitle();
        }
        boolean isEmpty = true ^ TextUtils.isEmpty(charSequence);
        if (z) {
            this.mTitleView.setText(title);
        } else {
            this.mTitleView.setText(this.mTitlePlaceholder);
        }
        if (isEmpty) {
            this.mSubtitleView.setText(charSequence);
            this.mSubtitleView.setVisibility(0);
            return;
        }
        this.mSubtitleView.setVisibility(8);
    }

    static void setLayoutHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class VolumeChangeListener implements SeekBar.OnSeekBarChangeListener {
        VolumeChangeListener() {
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStartTrackingTouch(SeekBar seekBar) {
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            if (mediaRouteDynamicControllerDialog.mRouteForVolumeUpdatingByUser != null) {
                mediaRouteDynamicControllerDialog.mHandler.removeMessages(2);
            }
            MediaRouteDynamicControllerDialog.this.mRouteForVolumeUpdatingByUser = (MediaRouter.RouteInfo) seekBar.getTag();
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onStopTrackingTouch(SeekBar seekBar) {
            MediaRouteDynamicControllerDialog.this.mHandler.sendEmptyMessageDelayed(2, 500L);
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                MediaRouter.RouteInfo routeInfo = (MediaRouter.RouteInfo) seekBar.getTag();
                MediaRouteVolumeSliderHolder mediaRouteVolumeSliderHolder = MediaRouteDynamicControllerDialog.this.mVolumeSliderHolderMap.get(routeInfo.getId());
                if (mediaRouteVolumeSliderHolder != null) {
                    mediaRouteVolumeSliderHolder.setMute(progress == 0);
                }
                routeInfo.requestSetVolume(progress);
            }
        }
    }

    List<MediaRouter.RouteInfo> getCurrentGroupableRoutes() {
        ArrayList arrayList = new ArrayList();
        for (MediaRouter.RouteInfo routeInfo : this.mSelectedRoute.getProvider().getRoutes()) {
            MediaRouter.RouteInfo.DynamicGroupState dynamicGroupState = this.mSelectedRoute.getDynamicGroupState(routeInfo);
            if (dynamicGroupState != null && dynamicGroupState.isGroupable()) {
                arrayList.add(routeInfo);
            }
        }
        return arrayList;
    }

    void updateRoutesView() {
        if (this.mAttachedToWindow) {
            if (SystemClock.uptimeMillis() - this.mLastUpdateTime >= 300) {
                if (shouldDeferUpdateViews()) {
                    this.mUpdateRoutesViewDeferred = true;
                    return;
                }
                this.mUpdateRoutesViewDeferred = false;
                if (!this.mSelectedRoute.isSelected() || this.mSelectedRoute.isDefaultOrBluetooth()) {
                    dismiss();
                }
                this.mLastUpdateTime = SystemClock.uptimeMillis();
                this.mAdapter.notifyAdapterDataSetChanged();
                return;
            }
            this.mHandler.removeMessages(1);
            this.mHandler.sendEmptyMessageAtTime(1, this.mLastUpdateTime + 300);
        }
    }

    void updateRoutes() {
        this.mMemberRoutes.clear();
        this.mGroupableRoutes.clear();
        this.mTransferableRoutes.clear();
        this.mMemberRoutes.addAll(this.mSelectedRoute.getMemberRoutes());
        for (MediaRouter.RouteInfo routeInfo : this.mSelectedRoute.getProvider().getRoutes()) {
            MediaRouter.RouteInfo.DynamicGroupState dynamicGroupState = this.mSelectedRoute.getDynamicGroupState(routeInfo);
            if (dynamicGroupState != null) {
                if (dynamicGroupState.isGroupable()) {
                    this.mGroupableRoutes.add(routeInfo);
                }
                if (dynamicGroupState.isTransferable()) {
                    this.mTransferableRoutes.add(routeInfo);
                }
            }
        }
        onFilterRoutes(this.mGroupableRoutes);
        onFilterRoutes(this.mTransferableRoutes);
        List<MediaRouter.RouteInfo> list = this.mMemberRoutes;
        RouteComparator routeComparator = RouteComparator.sInstance;
        Collections.sort(list, routeComparator);
        Collections.sort(this.mGroupableRoutes, routeComparator);
        Collections.sort(this.mTransferableRoutes, routeComparator);
        this.mAdapter.updateItems();
    }

    private static Bitmap blurBitmap(Bitmap bitmap, float radius, Context context) {
        RenderScript create = RenderScript.create(context);
        Allocation createFromBitmap = Allocation.createFromBitmap(create, bitmap);
        Allocation createTyped = Allocation.createTyped(create, createFromBitmap.getType());
        ScriptIntrinsicBlur create2 = ScriptIntrinsicBlur.create(create, Element.U8_4(create));
        create2.setRadius(radius);
        create2.setInput(createFromBitmap);
        create2.forEach(createTyped);
        Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
        createTyped.copyTo(copy);
        createFromBitmap.destroy();
        createTyped.destroy();
        create2.destroy();
        create.destroy();
        return copy;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public abstract class MediaRouteVolumeSliderHolder extends RecyclerView.ViewHolder {
        final ImageButton mMuteButton;
        MediaRouter.RouteInfo mRoute;
        final MediaRouteVolumeSlider mVolumeSlider;

        MediaRouteVolumeSliderHolder(View itemView, ImageButton muteButton, MediaRouteVolumeSlider volumeSlider) {
            super(itemView);
            this.mMuteButton = muteButton;
            this.mVolumeSlider = volumeSlider;
            muteButton.setImageDrawable(MediaRouterThemeHelper.getMuteButtonDrawableIcon(MediaRouteDynamicControllerDialog.this.mContext));
            MediaRouterThemeHelper.setVolumeSliderColor(MediaRouteDynamicControllerDialog.this.mContext, volumeSlider);
        }

        void bindRouteVolumeSliderHolder(MediaRouter.RouteInfo route) {
            this.mRoute = route;
            int volume = route.getVolume();
            this.mMuteButton.setActivated(volume == 0);
            this.mMuteButton.setOnClickListener(new View.OnClickListener() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.MediaRouteVolumeSliderHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
                    if (mediaRouteDynamicControllerDialog.mRouteForVolumeUpdatingByUser != null) {
                        mediaRouteDynamicControllerDialog.mHandler.removeMessages(2);
                    }
                    MediaRouteVolumeSliderHolder mediaRouteVolumeSliderHolder = MediaRouteVolumeSliderHolder.this;
                    MediaRouteDynamicControllerDialog.this.mRouteForVolumeUpdatingByUser = mediaRouteVolumeSliderHolder.mRoute;
                    boolean z = !v.isActivated();
                    int unmutedVolume = z ? 0 : MediaRouteVolumeSliderHolder.this.getUnmutedVolume();
                    MediaRouteVolumeSliderHolder.this.setMute(z);
                    MediaRouteVolumeSliderHolder.this.mVolumeSlider.setProgress(unmutedVolume);
                    MediaRouteVolumeSliderHolder.this.mRoute.requestSetVolume(unmutedVolume);
                    MediaRouteDynamicControllerDialog.this.mHandler.sendEmptyMessageDelayed(2, 500L);
                }
            });
            this.mVolumeSlider.setTag(this.mRoute);
            this.mVolumeSlider.setMax(route.getVolumeMax());
            this.mVolumeSlider.setProgress(volume);
            this.mVolumeSlider.setOnSeekBarChangeListener(MediaRouteDynamicControllerDialog.this.mVolumeChangeListener);
        }

        void updateVolume() {
            int volume = this.mRoute.getVolume();
            setMute(volume == 0);
            this.mVolumeSlider.setProgress(volume);
        }

        void setMute(boolean mute) {
            if (this.mMuteButton.isActivated() == mute) {
                return;
            }
            this.mMuteButton.setActivated(mute);
            if (mute) {
                MediaRouteDynamicControllerDialog.this.mUnmutedVolumeMap.put(this.mRoute.getId(), Integer.valueOf(this.mVolumeSlider.getProgress()));
            } else {
                MediaRouteDynamicControllerDialog.this.mUnmutedVolumeMap.remove(this.mRoute.getId());
            }
        }

        int getUnmutedVolume() {
            Integer num = MediaRouteDynamicControllerDialog.this.mUnmutedVolumeMap.get(this.mRoute.getId());
            if (num == null) {
                return 1;
            }
            return Math.max(1, num.intValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private final Drawable mDefaultIcon;
        private Item mGroupVolumeItem;
        private final LayoutInflater mInflater;
        private final int mLayoutAnimationDurationMs;
        private final Drawable mSpeakerGroupIcon;
        private final Drawable mSpeakerIcon;
        private final Drawable mTvIcon;
        private final ArrayList<Item> mItems = new ArrayList<>();
        private final Interpolator mAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();

        RecyclerAdapter() {
            this.mInflater = LayoutInflater.from(MediaRouteDynamicControllerDialog.this.mContext);
            this.mDefaultIcon = MediaRouterThemeHelper.getDefaultDrawableIcon(MediaRouteDynamicControllerDialog.this.mContext);
            this.mTvIcon = MediaRouterThemeHelper.getTvDrawableIcon(MediaRouteDynamicControllerDialog.this.mContext);
            this.mSpeakerIcon = MediaRouterThemeHelper.getSpeakerDrawableIcon(MediaRouteDynamicControllerDialog.this.mContext);
            this.mSpeakerGroupIcon = MediaRouterThemeHelper.getSpeakerGroupDrawableIcon(MediaRouteDynamicControllerDialog.this.mContext);
            this.mLayoutAnimationDurationMs = MediaRouteDynamicControllerDialog.this.mContext.getResources().getInteger(R$integer.mr_cast_volume_slider_layout_animation_duration_ms);
            updateItems();
        }

        boolean isGroupVolumeNeeded() {
            return MediaRouteDynamicControllerDialog.this.mSelectedRoute.getMemberRoutes().size() > 1;
        }

        void animateLayoutHeight(final View view, final int targetHeight) {
            final int i = view.getLayoutParams().height;
            Animation animation = new Animation() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.RecyclerAdapter.1
                @Override // android.view.animation.Animation
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    int i2 = targetHeight;
                    int i3 = i;
                    MediaRouteDynamicControllerDialog.setLayoutHeight(view, i3 + ((int) ((i2 - i3) * interpolatedTime)));
                }
            };
            animation.setAnimationListener(new Animation.AnimationListener() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.RecyclerAdapter.2
                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationRepeat(Animation animation2) {
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationStart(Animation animation2) {
                    MediaRouteDynamicControllerDialog.this.mIsAnimatingVolumeSliderLayout = true;
                }

                @Override // android.view.animation.Animation.AnimationListener
                public void onAnimationEnd(Animation animation2) {
                    MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
                    mediaRouteDynamicControllerDialog.mIsAnimatingVolumeSliderLayout = false;
                    mediaRouteDynamicControllerDialog.updateViewsIfNeeded();
                }
            });
            animation.setDuration(this.mLayoutAnimationDurationMs);
            animation.setInterpolator(this.mAccelerateDecelerateInterpolator);
            view.startAnimation(animation);
        }

        void mayUpdateGroupVolume(MediaRouter.RouteInfo route, boolean selected) {
            List<MediaRouter.RouteInfo> memberRoutes = MediaRouteDynamicControllerDialog.this.mSelectedRoute.getMemberRoutes();
            boolean z = true;
            int max = Math.max(1, memberRoutes.size());
            int i = -1;
            if (route.isGroup()) {
                for (MediaRouter.RouteInfo routeInfo : route.getMemberRoutes()) {
                    if (memberRoutes.contains(routeInfo) != selected) {
                        max += selected ? 1 : -1;
                    }
                }
            } else {
                if (selected) {
                    i = 1;
                }
                max += i;
            }
            boolean isGroupVolumeNeeded = isGroupVolumeNeeded();
            int i2 = 0;
            if (max < 2) {
                z = false;
            }
            if (isGroupVolumeNeeded != z) {
                RecyclerView.ViewHolder findViewHolderForAdapterPosition = MediaRouteDynamicControllerDialog.this.mRecyclerView.findViewHolderForAdapterPosition(0);
                if (!(findViewHolderForAdapterPosition instanceof GroupVolumeViewHolder)) {
                    return;
                }
                GroupVolumeViewHolder groupVolumeViewHolder = (GroupVolumeViewHolder) findViewHolderForAdapterPosition;
                View view = groupVolumeViewHolder.itemView;
                if (z) {
                    i2 = groupVolumeViewHolder.getExpandedHeight();
                }
                animateLayoutHeight(view, i2);
            }
        }

        void updateItems() {
            this.mItems.clear();
            this.mGroupVolumeItem = new Item(MediaRouteDynamicControllerDialog.this.mSelectedRoute, 1);
            if (!MediaRouteDynamicControllerDialog.this.mMemberRoutes.isEmpty()) {
                for (MediaRouter.RouteInfo routeInfo : MediaRouteDynamicControllerDialog.this.mMemberRoutes) {
                    this.mItems.add(new Item(routeInfo, 3));
                }
            } else {
                this.mItems.add(new Item(MediaRouteDynamicControllerDialog.this.mSelectedRoute, 3));
            }
            boolean z = false;
            if (!MediaRouteDynamicControllerDialog.this.mGroupableRoutes.isEmpty()) {
                boolean z2 = false;
                for (MediaRouter.RouteInfo routeInfo2 : MediaRouteDynamicControllerDialog.this.mGroupableRoutes) {
                    if (!MediaRouteDynamicControllerDialog.this.mMemberRoutes.contains(routeInfo2)) {
                        if (!z2) {
                            MediaRouteProvider.DynamicGroupRouteController dynamicGroupController = MediaRouteDynamicControllerDialog.this.mSelectedRoute.getDynamicGroupController();
                            String groupableSelectionTitle = dynamicGroupController != null ? dynamicGroupController.getGroupableSelectionTitle() : null;
                            if (TextUtils.isEmpty(groupableSelectionTitle)) {
                                groupableSelectionTitle = MediaRouteDynamicControllerDialog.this.mContext.getString(R$string.mr_dialog_groupable_header);
                            }
                            this.mItems.add(new Item(groupableSelectionTitle, 2));
                            z2 = true;
                        }
                        this.mItems.add(new Item(routeInfo2, 3));
                    }
                }
            }
            if (!MediaRouteDynamicControllerDialog.this.mTransferableRoutes.isEmpty()) {
                for (MediaRouter.RouteInfo routeInfo3 : MediaRouteDynamicControllerDialog.this.mTransferableRoutes) {
                    MediaRouter.RouteInfo routeInfo4 = MediaRouteDynamicControllerDialog.this.mSelectedRoute;
                    if (routeInfo4 != routeInfo3) {
                        if (!z) {
                            MediaRouteProvider.DynamicGroupRouteController dynamicGroupController2 = routeInfo4.getDynamicGroupController();
                            String transferableSectionTitle = dynamicGroupController2 != null ? dynamicGroupController2.getTransferableSectionTitle() : null;
                            if (TextUtils.isEmpty(transferableSectionTitle)) {
                                transferableSectionTitle = MediaRouteDynamicControllerDialog.this.mContext.getString(R$string.mr_dialog_transferable_header);
                            }
                            this.mItems.add(new Item(transferableSectionTitle, 2));
                            z = true;
                        }
                        this.mItems.add(new Item(routeInfo3, 4));
                    }
                }
            }
            notifyAdapterDataSetChanged();
        }

        void notifyAdapterDataSetChanged() {
            MediaRouteDynamicControllerDialog.this.mUngroupableRoutes.clear();
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            mediaRouteDynamicControllerDialog.mUngroupableRoutes.addAll(MediaRouteDialogHelper.getItemsRemoved(mediaRouteDynamicControllerDialog.mGroupableRoutes, mediaRouteDynamicControllerDialog.getCurrentGroupableRoutes()));
            notifyDataSetChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder */
        public RecyclerView.ViewHolder mo960onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType != 1) {
                if (viewType == 2) {
                    return new HeaderViewHolder(this.mInflater.inflate(R$layout.mr_cast_header_item, parent, false));
                }
                if (viewType == 3) {
                    return new RouteViewHolder(this.mInflater.inflate(R$layout.mr_cast_route_item, parent, false));
                }
                if (viewType == 4) {
                    return new GroupViewHolder(this.mInflater.inflate(R$layout.mr_cast_group_item, parent, false));
                }
                Log.w("MediaRouteCtrlDialog", "Cannot create ViewHolder because of wrong view type");
                return null;
            }
            return new GroupVolumeViewHolder(this.mInflater.inflate(R$layout.mr_cast_group_volume_item, parent, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int itemViewType = getItemViewType(position);
            Item item = getItem(position);
            if (itemViewType == 1) {
                MediaRouteDynamicControllerDialog.this.mVolumeSliderHolderMap.put(((MediaRouter.RouteInfo) item.getData()).getId(), (MediaRouteVolumeSliderHolder) holder);
                ((GroupVolumeViewHolder) holder).bindGroupVolumeViewHolder(item);
            } else if (itemViewType == 2) {
                ((HeaderViewHolder) holder).bindHeaderViewHolder(item);
            } else if (itemViewType == 3) {
                MediaRouteDynamicControllerDialog.this.mVolumeSliderHolderMap.put(((MediaRouter.RouteInfo) item.getData()).getId(), (MediaRouteVolumeSliderHolder) holder);
                ((RouteViewHolder) holder).bindRouteViewHolder(item);
            } else if (itemViewType == 4) {
                ((GroupViewHolder) holder).bindGroupViewHolder(item);
            } else {
                Log.w("MediaRouteCtrlDialog", "Cannot bind item to ViewHolder because of wrong view type");
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            super.onViewRecycled(holder);
            MediaRouteDynamicControllerDialog.this.mVolumeSliderHolderMap.values().remove(holder);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return this.mItems.size() + 1;
        }

        Drawable getIconDrawable(MediaRouter.RouteInfo route) {
            Uri iconUri = route.getIconUri();
            if (iconUri != null) {
                try {
                    Drawable createFromStream = Drawable.createFromStream(MediaRouteDynamicControllerDialog.this.mContext.getContentResolver().openInputStream(iconUri), null);
                    if (createFromStream != null) {
                        return createFromStream;
                    }
                } catch (IOException e) {
                    Log.w("MediaRouteCtrlDialog", "Failed to load " + iconUri, e);
                }
            }
            return getDefaultIconDrawable(route);
        }

        private Drawable getDefaultIconDrawable(MediaRouter.RouteInfo route) {
            int deviceType = route.getDeviceType();
            if (deviceType != 1) {
                if (deviceType == 2) {
                    return this.mSpeakerIcon;
                }
                if (route.isGroup()) {
                    return this.mSpeakerGroupIcon;
                }
                return this.mDefaultIcon;
            }
            return this.mTvIcon;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemViewType(int position) {
            return getItem(position).getType();
        }

        public Item getItem(int position) {
            if (position == 0) {
                return this.mGroupVolumeItem;
            }
            return this.mItems.get(position - 1);
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public class Item {
            private final Object mData;
            private final int mType;

            Item(Object data, int type) {
                this.mData = data;
                this.mType = type;
            }

            public Object getData() {
                return this.mData;
            }

            public int getType() {
                return this.mType;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: classes.dex */
        public class GroupVolumeViewHolder extends MediaRouteVolumeSliderHolder {
            private final int mExpandedHeight;
            private final TextView mTextView;

            GroupVolumeViewHolder(View itemView) {
                super(itemView, (ImageButton) itemView.findViewById(R$id.mr_cast_mute_button), (MediaRouteVolumeSlider) itemView.findViewById(R$id.mr_cast_volume_slider));
                this.mTextView = (TextView) itemView.findViewById(R$id.mr_group_volume_route_name);
                Resources resources = MediaRouteDynamicControllerDialog.this.mContext.getResources();
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                TypedValue typedValue = new TypedValue();
                resources.getValue(R$dimen.mr_dynamic_volume_group_list_item_height, typedValue, true);
                this.mExpandedHeight = (int) typedValue.getDimension(displayMetrics);
            }

            void bindGroupVolumeViewHolder(Item item) {
                MediaRouteDynamicControllerDialog.setLayoutHeight(this.itemView, RecyclerAdapter.this.isGroupVolumeNeeded() ? this.mExpandedHeight : 0);
                MediaRouter.RouteInfo routeInfo = (MediaRouter.RouteInfo) item.getData();
                super.bindRouteVolumeSliderHolder(routeInfo);
                this.mTextView.setText(routeInfo.getName());
            }

            int getExpandedHeight() {
                return this.mExpandedHeight;
            }
        }

        /* loaded from: classes.dex */
        private class HeaderViewHolder extends RecyclerView.ViewHolder {
            private final TextView mTextView;

            HeaderViewHolder(View itemView) {
                super(itemView);
                this.mTextView = (TextView) itemView.findViewById(R$id.mr_cast_header_name);
            }

            void bindHeaderViewHolder(Item item) {
                this.mTextView.setText(item.getData().toString());
            }
        }

        /* loaded from: classes.dex */
        private class RouteViewHolder extends MediaRouteVolumeSliderHolder {
            final CheckBox mCheckBox;
            final float mDisabledAlpha;
            final int mExpandedLayoutHeight;
            final ImageView mImageView;
            final View mItemView;
            final ProgressBar mProgressBar;
            final TextView mTextView;
            final RelativeLayout mVolumeSliderLayout;
            final View.OnClickListener mViewClickListener = new View.OnClickListener() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.RecyclerAdapter.RouteViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    RouteViewHolder routeViewHolder = RouteViewHolder.this;
                    boolean z = !routeViewHolder.isSelected(routeViewHolder.mRoute);
                    boolean isGroup = RouteViewHolder.this.mRoute.isGroup();
                    if (z) {
                        RouteViewHolder routeViewHolder2 = RouteViewHolder.this;
                        MediaRouteDynamicControllerDialog.this.mRouter.addMemberToDynamicGroup(routeViewHolder2.mRoute);
                    } else {
                        RouteViewHolder routeViewHolder3 = RouteViewHolder.this;
                        MediaRouteDynamicControllerDialog.this.mRouter.removeMemberFromDynamicGroup(routeViewHolder3.mRoute);
                    }
                    RouteViewHolder.this.showSelectingProgress(z, !isGroup);
                    if (isGroup) {
                        List<MediaRouter.RouteInfo> memberRoutes = MediaRouteDynamicControllerDialog.this.mSelectedRoute.getMemberRoutes();
                        for (MediaRouter.RouteInfo routeInfo : RouteViewHolder.this.mRoute.getMemberRoutes()) {
                            if (memberRoutes.contains(routeInfo) != z) {
                                MediaRouteVolumeSliderHolder mediaRouteVolumeSliderHolder = MediaRouteDynamicControllerDialog.this.mVolumeSliderHolderMap.get(routeInfo.getId());
                                if (mediaRouteVolumeSliderHolder instanceof RouteViewHolder) {
                                    ((RouteViewHolder) mediaRouteVolumeSliderHolder).showSelectingProgress(z, true);
                                }
                            }
                        }
                    }
                    RouteViewHolder routeViewHolder4 = RouteViewHolder.this;
                    RecyclerAdapter.this.mayUpdateGroupVolume(routeViewHolder4.mRoute, z);
                }
            };
            final int mCollapsedLayoutHeight = 0;

            RouteViewHolder(View itemView) {
                super(itemView, (ImageButton) itemView.findViewById(R$id.mr_cast_mute_button), (MediaRouteVolumeSlider) itemView.findViewById(R$id.mr_cast_volume_slider));
                this.mItemView = itemView;
                this.mImageView = (ImageView) itemView.findViewById(R$id.mr_cast_route_icon);
                ProgressBar progressBar = (ProgressBar) itemView.findViewById(R$id.mr_cast_route_progress_bar);
                this.mProgressBar = progressBar;
                this.mTextView = (TextView) itemView.findViewById(R$id.mr_cast_route_name);
                this.mVolumeSliderLayout = (RelativeLayout) itemView.findViewById(R$id.mr_cast_volume_layout);
                CheckBox checkBox = (CheckBox) itemView.findViewById(R$id.mr_cast_checkbox);
                this.mCheckBox = checkBox;
                checkBox.setButtonDrawable(MediaRouterThemeHelper.getCheckBoxDrawableIcon(MediaRouteDynamicControllerDialog.this.mContext));
                MediaRouterThemeHelper.setIndeterminateProgressBarColor(MediaRouteDynamicControllerDialog.this.mContext, progressBar);
                this.mDisabledAlpha = MediaRouterThemeHelper.getDisabledAlpha(MediaRouteDynamicControllerDialog.this.mContext);
                Resources resources = MediaRouteDynamicControllerDialog.this.mContext.getResources();
                DisplayMetrics displayMetrics = resources.getDisplayMetrics();
                TypedValue typedValue = new TypedValue();
                resources.getValue(R$dimen.mr_dynamic_dialog_row_height, typedValue, true);
                this.mExpandedLayoutHeight = (int) typedValue.getDimension(displayMetrics);
            }

            boolean isSelected(MediaRouter.RouteInfo route) {
                if (route.isSelected()) {
                    return true;
                }
                MediaRouter.RouteInfo.DynamicGroupState dynamicGroupState = MediaRouteDynamicControllerDialog.this.mSelectedRoute.getDynamicGroupState(route);
                return dynamicGroupState != null && dynamicGroupState.getSelectionState() == 3;
            }

            private boolean isEnabled(MediaRouter.RouteInfo route) {
                if (MediaRouteDynamicControllerDialog.this.mUngroupableRoutes.contains(route)) {
                    return false;
                }
                if (isSelected(route) && MediaRouteDynamicControllerDialog.this.mSelectedRoute.getMemberRoutes().size() < 2) {
                    return false;
                }
                if (!isSelected(route)) {
                    return true;
                }
                MediaRouter.RouteInfo.DynamicGroupState dynamicGroupState = MediaRouteDynamicControllerDialog.this.mSelectedRoute.getDynamicGroupState(route);
                return dynamicGroupState != null && dynamicGroupState.isUnselectable();
            }

            void bindRouteViewHolder(Item item) {
                MediaRouter.RouteInfo routeInfo = (MediaRouter.RouteInfo) item.getData();
                if (routeInfo == MediaRouteDynamicControllerDialog.this.mSelectedRoute && routeInfo.getMemberRoutes().size() > 0) {
                    Iterator<MediaRouter.RouteInfo> it = routeInfo.getMemberRoutes().iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        MediaRouter.RouteInfo next = it.next();
                        if (!MediaRouteDynamicControllerDialog.this.mGroupableRoutes.contains(next)) {
                            routeInfo = next;
                            break;
                        }
                    }
                }
                bindRouteVolumeSliderHolder(routeInfo);
                this.mImageView.setImageDrawable(RecyclerAdapter.this.getIconDrawable(routeInfo));
                this.mTextView.setText(routeInfo.getName());
                boolean z = false;
                this.mCheckBox.setVisibility(0);
                boolean isSelected = isSelected(routeInfo);
                boolean isEnabled = isEnabled(routeInfo);
                this.mCheckBox.setChecked(isSelected);
                this.mProgressBar.setVisibility(4);
                this.mImageView.setVisibility(0);
                this.mItemView.setEnabled(isEnabled);
                this.mCheckBox.setEnabled(isEnabled);
                this.mMuteButton.setEnabled(isEnabled || isSelected);
                MediaRouteVolumeSlider mediaRouteVolumeSlider = this.mVolumeSlider;
                if (isEnabled || isSelected) {
                    z = true;
                }
                mediaRouteVolumeSlider.setEnabled(z);
                this.mItemView.setOnClickListener(this.mViewClickListener);
                this.mCheckBox.setOnClickListener(this.mViewClickListener);
                MediaRouteDynamicControllerDialog.setLayoutHeight(this.mVolumeSliderLayout, (!isSelected || this.mRoute.isGroup()) ? this.mCollapsedLayoutHeight : this.mExpandedLayoutHeight);
                float f = 1.0f;
                this.mItemView.setAlpha((isEnabled || isSelected) ? 1.0f : this.mDisabledAlpha);
                CheckBox checkBox = this.mCheckBox;
                if (!isEnabled && isSelected) {
                    f = this.mDisabledAlpha;
                }
                checkBox.setAlpha(f);
            }

            void showSelectingProgress(boolean selected, boolean shouldChangeHeight) {
                this.mCheckBox.setEnabled(false);
                this.mItemView.setEnabled(false);
                this.mCheckBox.setChecked(selected);
                if (selected) {
                    this.mImageView.setVisibility(4);
                    this.mProgressBar.setVisibility(0);
                }
                if (shouldChangeHeight) {
                    RecyclerAdapter.this.animateLayoutHeight(this.mVolumeSliderLayout, selected ? this.mExpandedLayoutHeight : this.mCollapsedLayoutHeight);
                }
            }
        }

        /* loaded from: classes.dex */
        private class GroupViewHolder extends RecyclerView.ViewHolder {
            final float mDisabledAlpha;
            final ImageView mImageView;
            final View mItemView;
            final ProgressBar mProgressBar;
            MediaRouter.RouteInfo mRoute;
            final TextView mTextView;

            GroupViewHolder(View itemView) {
                super(itemView);
                this.mItemView = itemView;
                this.mImageView = (ImageView) itemView.findViewById(R$id.mr_cast_group_icon);
                ProgressBar progressBar = (ProgressBar) itemView.findViewById(R$id.mr_cast_group_progress_bar);
                this.mProgressBar = progressBar;
                this.mTextView = (TextView) itemView.findViewById(R$id.mr_cast_group_name);
                this.mDisabledAlpha = MediaRouterThemeHelper.getDisabledAlpha(MediaRouteDynamicControllerDialog.this.mContext);
                MediaRouterThemeHelper.setIndeterminateProgressBarColor(MediaRouteDynamicControllerDialog.this.mContext, progressBar);
            }

            private boolean isEnabled(MediaRouter.RouteInfo route) {
                List<MediaRouter.RouteInfo> memberRoutes = MediaRouteDynamicControllerDialog.this.mSelectedRoute.getMemberRoutes();
                return (memberRoutes.size() == 1 && memberRoutes.get(0) == route) ? false : true;
            }

            void bindGroupViewHolder(Item item) {
                MediaRouter.RouteInfo routeInfo = (MediaRouter.RouteInfo) item.getData();
                this.mRoute = routeInfo;
                this.mImageView.setVisibility(0);
                this.mProgressBar.setVisibility(4);
                this.mItemView.setAlpha(isEnabled(routeInfo) ? 1.0f : this.mDisabledAlpha);
                this.mItemView.setOnClickListener(new View.OnClickListener() { // from class: androidx.mediarouter.app.MediaRouteDynamicControllerDialog.RecyclerAdapter.GroupViewHolder.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view) {
                        GroupViewHolder groupViewHolder = GroupViewHolder.this;
                        MediaRouteDynamicControllerDialog.this.mRouter.transferToRoute(groupViewHolder.mRoute);
                        GroupViewHolder.this.mImageView.setVisibility(4);
                        GroupViewHolder.this.mProgressBar.setVisibility(0);
                    }
                });
                this.mImageView.setImageDrawable(RecyclerAdapter.this.getIconDrawable(routeInfo));
                this.mTextView.setText(routeInfo.getName());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class MediaRouterCallback extends MediaRouter.Callback {
        MediaRouterCallback() {
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public void onRouteAdded(MediaRouter router, MediaRouter.RouteInfo info) {
            MediaRouteDynamicControllerDialog.this.updateRoutesView();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public void onRouteRemoved(MediaRouter router, MediaRouter.RouteInfo info) {
            MediaRouteDynamicControllerDialog.this.updateRoutesView();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public void onRouteSelected(MediaRouter router, MediaRouter.RouteInfo route) {
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            mediaRouteDynamicControllerDialog.mSelectedRoute = route;
            mediaRouteDynamicControllerDialog.mIsSelectingRoute = false;
            mediaRouteDynamicControllerDialog.updateViewsIfNeeded();
            MediaRouteDynamicControllerDialog.this.updateRoutes();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public void onRouteUnselected(MediaRouter router, MediaRouter.RouteInfo route) {
            MediaRouteDynamicControllerDialog.this.updateRoutesView();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public void onRouteChanged(MediaRouter router, MediaRouter.RouteInfo route) {
            boolean z;
            MediaRouter.RouteInfo.DynamicGroupState dynamicGroupState;
            if (route == MediaRouteDynamicControllerDialog.this.mSelectedRoute && route.getDynamicGroupController() != null) {
                for (MediaRouter.RouteInfo routeInfo : route.getProvider().getRoutes()) {
                    if (!MediaRouteDynamicControllerDialog.this.mSelectedRoute.getMemberRoutes().contains(routeInfo) && (dynamicGroupState = MediaRouteDynamicControllerDialog.this.mSelectedRoute.getDynamicGroupState(routeInfo)) != null && dynamicGroupState.isGroupable() && !MediaRouteDynamicControllerDialog.this.mGroupableRoutes.contains(routeInfo)) {
                        z = true;
                        break;
                    }
                }
            }
            z = false;
            if (z) {
                MediaRouteDynamicControllerDialog.this.updateViewsIfNeeded();
                MediaRouteDynamicControllerDialog.this.updateRoutes();
                return;
            }
            MediaRouteDynamicControllerDialog.this.updateRoutesView();
        }

        @Override // androidx.mediarouter.media.MediaRouter.Callback
        public void onRouteVolumeChanged(MediaRouter router, MediaRouter.RouteInfo route) {
            MediaRouteVolumeSliderHolder mediaRouteVolumeSliderHolder;
            int volume = route.getVolume();
            if (MediaRouteDynamicControllerDialog.DEBUG) {
                Log.d("MediaRouteCtrlDialog", "onRouteVolumeChanged(), route.getVolume:" + volume);
            }
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            if (mediaRouteDynamicControllerDialog.mRouteForVolumeUpdatingByUser == route || (mediaRouteVolumeSliderHolder = mediaRouteDynamicControllerDialog.mVolumeSliderHolderMap.get(route.getId())) == null) {
                return;
            }
            mediaRouteVolumeSliderHolder.updateVolume();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class MediaControllerCallback extends MediaControllerCompat.Callback {
        MediaControllerCallback() {
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.Callback
        public void onSessionDestroyed() {
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            MediaControllerCompat mediaControllerCompat = mediaRouteDynamicControllerDialog.mMediaController;
            if (mediaControllerCompat != null) {
                mediaControllerCompat.unregisterCallback(mediaRouteDynamicControllerDialog.mControllerCallback);
                MediaRouteDynamicControllerDialog.this.mMediaController = null;
            }
        }

        @Override // android.support.v4.media.session.MediaControllerCompat.Callback
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            MediaRouteDynamicControllerDialog.this.mDescription = metadata == null ? null : metadata.getDescription();
            MediaRouteDynamicControllerDialog.this.reloadIconIfNeeded();
            MediaRouteDynamicControllerDialog.this.updateMetadataViews();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class FetchArtTask extends AsyncTask<Void, Void, Bitmap> {
        private int mBackgroundColor;
        private final Bitmap mIconBitmap;
        private final Uri mIconUri;

        FetchArtTask() {
            MediaDescriptionCompat mediaDescriptionCompat = MediaRouteDynamicControllerDialog.this.mDescription;
            Uri uri = null;
            Bitmap iconBitmap = mediaDescriptionCompat == null ? null : mediaDescriptionCompat.getIconBitmap();
            if (MediaRouteDynamicControllerDialog.isBitmapRecycled(iconBitmap)) {
                Log.w("MediaRouteCtrlDialog", "Can't fetch the given art bitmap because it's already recycled.");
                iconBitmap = null;
            }
            this.mIconBitmap = iconBitmap;
            MediaDescriptionCompat mediaDescriptionCompat2 = MediaRouteDynamicControllerDialog.this.mDescription;
            this.mIconUri = mediaDescriptionCompat2 != null ? mediaDescriptionCompat2.getIconUri() : uri;
        }

        Bitmap getIconBitmap() {
            return this.mIconBitmap;
        }

        Uri getIconUri() {
            return this.mIconUri;
        }

        @Override // android.os.AsyncTask
        protected void onPreExecute() {
            MediaRouteDynamicControllerDialog.this.clearLoadedBitmap();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:74:0x00d0  */
        /* JADX WARN: Removed duplicated region for block: B:76:0x00e5  */
        /* JADX WARN: Type inference failed for: r4v0 */
        /* JADX WARN: Type inference failed for: r4v1 */
        /* JADX WARN: Type inference failed for: r4v2, types: [java.io.InputStream] */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public Bitmap doInBackground(Void... arg) {
            InputStream inputStream;
            Bitmap bitmap = this.mIconBitmap;
            int i = 0;
            ?? r4 = 0;
            if (bitmap == null) {
                Uri uri = this.mIconUri;
                try {
                    if (uri != null) {
                        try {
                            inputStream = openInputStreamByScheme(uri);
                            try {
                                if (inputStream == null) {
                                    Log.w("MediaRouteCtrlDialog", "Unable to open: " + this.mIconUri);
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                        } catch (IOException unused) {
                                        }
                                    }
                                    return null;
                                }
                                BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inJustDecodeBounds = true;
                                BitmapFactory.decodeStream(inputStream, null, options);
                                if (options.outWidth == 0 || options.outHeight == 0) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException unused2) {
                                    }
                                    return null;
                                }
                                try {
                                    inputStream.reset();
                                } catch (IOException unused3) {
                                    inputStream.close();
                                    inputStream = openInputStreamByScheme(this.mIconUri);
                                    if (inputStream == null) {
                                        Log.w("MediaRouteCtrlDialog", "Unable to open: " + this.mIconUri);
                                        if (inputStream != null) {
                                            try {
                                                inputStream.close();
                                            } catch (IOException unused4) {
                                            }
                                        }
                                        return null;
                                    }
                                }
                                options.inJustDecodeBounds = false;
                                options.inSampleSize = Math.max(1, Integer.highestOneBit(options.outHeight / MediaRouteDynamicControllerDialog.this.mContext.getResources().getDimensionPixelSize(R$dimen.mr_cast_meta_art_size)));
                                if (isCancelled()) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException unused5) {
                                    }
                                    return null;
                                }
                                Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
                                try {
                                    inputStream.close();
                                } catch (IOException unused6) {
                                }
                                bitmap = decodeStream;
                            } catch (IOException e) {
                                e = e;
                                Log.w("MediaRouteCtrlDialog", "Unable to open: " + this.mIconUri, e);
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException unused7) {
                                    }
                                }
                                bitmap = null;
                                if (!MediaRouteDynamicControllerDialog.isBitmapRecycled(bitmap)) {
                                }
                            }
                        } catch (IOException e2) {
                            e = e2;
                            inputStream = null;
                        } catch (Throwable th) {
                            th = th;
                            if (r4 != 0) {
                                try {
                                    r4.close();
                                } catch (IOException unused8) {
                                }
                            }
                            throw th;
                        }
                    }
                    bitmap = null;
                } catch (Throwable th2) {
                    th = th2;
                    r4 = uri;
                }
            }
            if (!MediaRouteDynamicControllerDialog.isBitmapRecycled(bitmap)) {
                Log.w("MediaRouteCtrlDialog", "Can't use recycled bitmap: " + bitmap);
                return null;
            }
            if (bitmap != null && bitmap.getWidth() < bitmap.getHeight()) {
                Palette generate = new Palette.Builder(bitmap).maximumColorCount(1).generate();
                if (!generate.getSwatches().isEmpty()) {
                    i = ((Palette.Swatch) generate.getSwatches().get(0)).getRgb();
                }
                this.mBackgroundColor = i;
            }
            return bitmap;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap art) {
            MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog = MediaRouteDynamicControllerDialog.this;
            mediaRouteDynamicControllerDialog.mFetchArtTask = null;
            if (!ObjectsCompat.equals(mediaRouteDynamicControllerDialog.mArtIconBitmap, this.mIconBitmap) || !ObjectsCompat.equals(MediaRouteDynamicControllerDialog.this.mArtIconUri, this.mIconUri)) {
                MediaRouteDynamicControllerDialog mediaRouteDynamicControllerDialog2 = MediaRouteDynamicControllerDialog.this;
                mediaRouteDynamicControllerDialog2.mArtIconBitmap = this.mIconBitmap;
                mediaRouteDynamicControllerDialog2.mArtIconLoadedBitmap = art;
                mediaRouteDynamicControllerDialog2.mArtIconUri = this.mIconUri;
                mediaRouteDynamicControllerDialog2.mArtIconBackgroundColor = this.mBackgroundColor;
                mediaRouteDynamicControllerDialog2.mArtIconIsLoaded = true;
                mediaRouteDynamicControllerDialog2.updateMetadataViews();
            }
        }

        private InputStream openInputStreamByScheme(Uri uri) throws IOException {
            InputStream openInputStream;
            String lowerCase = uri.getScheme().toLowerCase();
            if ("android.resource".equals(lowerCase) || "content".equals(lowerCase) || "file".equals(lowerCase)) {
                openInputStream = MediaRouteDynamicControllerDialog.this.mContext.getContentResolver().openInputStream(uri);
            } else {
                URLConnection openConnection = new URL(uri.toString()).openConnection();
                openConnection.setConnectTimeout(30000);
                openConnection.setReadTimeout(30000);
                openInputStream = openConnection.getInputStream();
            }
            if (openInputStream == null) {
                return null;
            }
            return new BufferedInputStream(openInputStream);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static final class RouteComparator implements Comparator<MediaRouter.RouteInfo> {
        static final RouteComparator sInstance = new RouteComparator();

        RouteComparator() {
        }

        @Override // java.util.Comparator
        public int compare(MediaRouter.RouteInfo lhs, MediaRouter.RouteInfo rhs) {
            return lhs.getName().compareToIgnoreCase(rhs.getName());
        }
    }
}
