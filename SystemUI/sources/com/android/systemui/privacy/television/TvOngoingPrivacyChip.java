package com.android.systemui.privacy.television;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.IWindowManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.systemui.C1893R;
import com.android.systemui.CoreStartable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.privacy.PrivacyChipBuilder;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.PrivacyType;
import com.android.systemui.privacy.television.PrivacyChipDrawable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.inject.Inject;

@SysUISingleton
public class TvOngoingPrivacyChip extends CoreStartable implements PrivacyItemController.Callback, PrivacyChipDrawable.PrivacyChipDrawableListener {
    private static final int ACCESSIBILITY_ANNOUNCEMENT_DELAY_MS = 500;
    private static final boolean DEBUG = false;
    private static final int EXPANDED_DURATION_MS = 4000;
    private static final String LAYOUT_PARAMS_TITLE = "MicrophoneCaptureIndicator";
    private static final int STATE_APPEARING = 1;
    private static final int STATE_COLLAPSED = 3;
    private static final int STATE_DISAPPEARING = 4;
    private static final int STATE_EXPANDED = 2;
    private static final int STATE_NOT_SHOWN = 0;
    private static final String TAG = "TvOngoingPrivacyChip";
    private final Runnable mAccessibilityRunnable = new TvOngoingPrivacyChip$$ExternalSyntheticLambda1(this);
    private boolean mAllIndicatorsEnabled;
    public final int mAnimationDurationMs;
    private ObjectAnimator mAnimator;
    private final Rect[] mBounds = new Rect[4];
    /* access modifiers changed from: private */
    public PrivacyChipDrawable mChipDrawable;
    private final Runnable mCollapseRunnable = new TvOngoingPrivacyChip$$ExternalSyntheticLambda0(this);
    private final Context mContext;
    private final IWindowManager mIWindowManager;
    private final int mIconMarginStart;
    private final int mIconSize;
    private LinearLayout mIconsContainer;
    /* access modifiers changed from: private */
    public ViewGroup mIndicatorView;
    private boolean mIsRtl;
    private final List<PrivacyItem> mItemsBeforeLastAnnouncement = new LinkedList();
    private boolean mMicCameraIndicatorFlagEnabled;
    private final PrivacyItemController mPrivacyItemController;
    private List<PrivacyItem> mPrivacyItems = Collections.emptyList();
    /* access modifiers changed from: private */
    public int mState;
    private final Handler mUiThreadHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public boolean mViewAndWindowAdded;

    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }

    private String stateToString(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i != 4 ? "INVALID" : "DISAPPEARING" : "COLLAPSED" : "EXPANDED" : "APPEARING" : "NOT_SHOWN";
    }

    @Inject
    public TvOngoingPrivacyChip(Context context, PrivacyItemController privacyItemController, IWindowManager iWindowManager) {
        super(context);
        boolean z = false;
        this.mState = 0;
        this.mContext = context;
        this.mPrivacyItemController = privacyItemController;
        this.mIWindowManager = iWindowManager;
        Resources resources = context.getResources();
        this.mIconMarginStart = Math.round(resources.getDimension(C1893R.dimen.privacy_chip_icon_margin_in_between));
        this.mIconSize = resources.getDimensionPixelSize(C1893R.dimen.privacy_chip_icon_size);
        this.mIsRtl = context.getResources().getConfiguration().getLayoutDirection() == 1 ? true : z;
        updateStaticPrivacyIndicatorBounds();
        this.mAnimationDurationMs = resources.getInteger(C1893R.integer.privacy_chip_animation_millis);
        this.mMicCameraIndicatorFlagEnabled = privacyItemController.getMicCameraAvailable();
        this.mAllIndicatorsEnabled = privacyItemController.getAllIndicatorsAvailable();
    }

    public void onConfigurationChanged(Configuration configuration) {
        boolean z = true;
        if (configuration.getLayoutDirection() != 1) {
            z = false;
        }
        if (this.mIsRtl != z) {
            this.mIsRtl = z;
            updateStaticPrivacyIndicatorBounds();
            if (this.mState != 0 && this.mIndicatorView != null) {
                fadeOutIndicator();
                createAndShowIndicator();
            }
        }
    }

    public void start() {
        this.mPrivacyItemController.addCallback((PrivacyItemController.Callback) this);
    }

    public void onPrivacyItemsChanged(List<PrivacyItem> list) {
        ArrayList arrayList = new ArrayList(list);
        arrayList.removeIf(new TvOngoingPrivacyChip$$ExternalSyntheticLambda2());
        if (isChipDisabled()) {
            fadeOutIndicator();
            this.mPrivacyItems = arrayList;
        } else if (arrayList.size() != this.mPrivacyItems.size() || !this.mPrivacyItems.containsAll(arrayList)) {
            this.mPrivacyItems = arrayList;
            postAccessibilityAnnouncement();
            updateChip();
        }
    }

    static /* synthetic */ boolean lambda$onPrivacyItemsChanged$0(PrivacyItem privacyItem) {
        return privacyItem.getPrivacyType() == PrivacyType.TYPE_LOCATION;
    }

    private void updateStaticPrivacyIndicatorBounds() {
        int i;
        int i2;
        Resources resources = this.mContext.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(C1893R.dimen.privacy_chip_max_width);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(C1893R.dimen.privacy_chip_height);
        int dimensionPixelSize3 = resources.getDimensionPixelSize(C1893R.dimen.privacy_chip_margin) * 2;
        Rect bounds = ((WindowManager) this.mContext.getSystemService(WindowManager.class)).getCurrentWindowMetrics().getBounds();
        Rect[] rectArr = this.mBounds;
        if (this.mIsRtl) {
            i = bounds.left;
        } else {
            i = (bounds.right - dimensionPixelSize3) - dimensionPixelSize;
        }
        int i3 = bounds.top;
        if (this.mIsRtl) {
            i2 = bounds.left + dimensionPixelSize3 + dimensionPixelSize;
        } else {
            i2 = bounds.right;
        }
        rectArr[0] = new Rect(i, i3, i2, bounds.top + dimensionPixelSize3 + dimensionPixelSize2);
        try {
            this.mIWindowManager.updateStaticPrivacyIndicatorBounds(this.mContext.getDisplayId(), this.mBounds);
        } catch (RemoteException unused) {
            Log.w(TAG, "could not update privacy indicator bounds");
        }
    }

    private void updateChip() {
        if (this.mPrivacyItems.isEmpty()) {
            fadeOutIndicator();
            return;
        }
        int i = this.mState;
        if (i == 0) {
            createAndShowIndicator();
        } else if (i == 1 || i == 2) {
            updateIcons();
            collapseLater();
        } else if (i == 3 || i == 4) {
            this.mState = 2;
            updateIcons();
            animateIconAppearance();
        }
    }

    private void collapseLater() {
        this.mUiThreadHandler.removeCallbacks(this.mCollapseRunnable);
        this.mUiThreadHandler.postDelayed(this.mCollapseRunnable, 4000);
    }

    /* access modifiers changed from: private */
    public void collapseChip() {
        if (this.mState == 2) {
            this.mState = 3;
            PrivacyChipDrawable privacyChipDrawable = this.mChipDrawable;
            if (privacyChipDrawable != null) {
                privacyChipDrawable.collapse();
            }
            animateIconDisappearance();
        }
    }

    public void onFlagMicCameraChanged(boolean z) {
        this.mMicCameraIndicatorFlagEnabled = z;
        updateChipOnFlagChanged();
    }

    public void onFlagAllChanged(boolean z) {
        this.mAllIndicatorsEnabled = z;
        updateChipOnFlagChanged();
    }

    private boolean isChipDisabled() {
        return !this.mMicCameraIndicatorFlagEnabled && !this.mAllIndicatorsEnabled;
    }

    private void updateChipOnFlagChanged() {
        if (isChipDisabled()) {
            fadeOutIndicator();
        } else {
            updateChip();
        }
    }

    private void fadeOutIndicator() {
        int i = this.mState;
        if (i != 0 && i != 4) {
            this.mUiThreadHandler.removeCallbacks(this.mCollapseRunnable);
            if (this.mViewAndWindowAdded) {
                this.mState = 4;
                animateIconDisappearance();
            } else {
                this.mState = 0;
                removeIndicatorView();
            }
            PrivacyChipDrawable privacyChipDrawable = this.mChipDrawable;
            if (privacyChipDrawable != null) {
                privacyChipDrawable.updateIcons(0);
            }
        }
    }

    private void createAndShowIndicator() {
        this.mState = 1;
        if (this.mIndicatorView != null || this.mViewAndWindowAdded) {
            removeIndicatorView();
        }
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.mContext).inflate(C1893R.layout.tv_ongoing_privacy_chip, (ViewGroup) null);
        this.mIndicatorView = viewGroup;
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (TvOngoingPrivacyChip.this.mState == 1) {
                    boolean unused = TvOngoingPrivacyChip.this.mViewAndWindowAdded = true;
                    TvOngoingPrivacyChip.this.mIndicatorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    TvOngoingPrivacyChip.this.postAccessibilityAnnouncement();
                    TvOngoingPrivacyChip.this.animateIconAppearance();
                    TvOngoingPrivacyChip.this.mChipDrawable.startInitialFadeIn();
                }
            }
        });
        PrivacyChipDrawable privacyChipDrawable = new PrivacyChipDrawable(this.mContext);
        this.mChipDrawable = privacyChipDrawable;
        privacyChipDrawable.setListener(this);
        this.mChipDrawable.setRtl(this.mIsRtl);
        ImageView imageView = (ImageView) this.mIndicatorView.findViewById(C1893R.C1897id.chip_drawable);
        if (imageView != null) {
            imageView.setImageDrawable(this.mChipDrawable);
        }
        LinearLayout linearLayout = (LinearLayout) this.mIndicatorView.findViewById(C1893R.C1897id.icons_container);
        this.mIconsContainer = linearLayout;
        linearLayout.setAlpha(0.0f);
        updateIcons();
        ((WindowManager) this.mContext.getSystemService(WindowManager.class)).addView(this.mIndicatorView, getWindowLayoutParams());
    }

    private WindowManager.LayoutParams getWindowLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, Types.REF, 8, -3);
        layoutParams.gravity = (this.mIsRtl ? 3 : 5) | 48;
        layoutParams.setTitle(LAYOUT_PARAMS_TITLE);
        layoutParams.packageName = this.mContext.getPackageName();
        return layoutParams;
    }

    private void updateIcons() {
        List<Drawable> generateIcons = new PrivacyChipBuilder(this.mContext, this.mPrivacyItems).generateIcons();
        this.mIconsContainer.removeAllViews();
        for (int i = 0; i < generateIcons.size(); i++) {
            Drawable drawable = generateIcons.get(i);
            drawable.mutate().setTint(this.mContext.getColor(C1893R.C1894color.privacy_icon_tint));
            ImageView imageView = new ImageView(this.mContext);
            imageView.setImageDrawable(drawable);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            LinearLayout linearLayout = this.mIconsContainer;
            int i2 = this.mIconSize;
            linearLayout.addView(imageView, i2, i2);
            if (i != 0) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                marginLayoutParams.setMarginStart(this.mIconMarginStart);
                imageView.setLayoutParams(marginLayoutParams);
            }
        }
        PrivacyChipDrawable privacyChipDrawable = this.mChipDrawable;
        if (privacyChipDrawable != null) {
            privacyChipDrawable.updateIcons(generateIcons.size());
        }
    }

    /* access modifiers changed from: private */
    public void animateIconAppearance() {
        animateIconAlphaTo(1.0f);
    }

    private void animateIconDisappearance() {
        animateIconAlphaTo(0.0f);
    }

    private void animateIconAlphaTo(float f) {
        ObjectAnimator objectAnimator = this.mAnimator;
        if (objectAnimator == null) {
            ObjectAnimator objectAnimator2 = new ObjectAnimator();
            this.mAnimator = objectAnimator2;
            objectAnimator2.setTarget(this.mIconsContainer);
            this.mAnimator.setProperty(View.ALPHA);
            this.mAnimator.addListener(new AnimatorListenerAdapter() {
                boolean mCancelled;

                public void onAnimationStart(Animator animator, boolean z) {
                    this.mCancelled = false;
                }

                public void onAnimationCancel(Animator animator) {
                    this.mCancelled = true;
                }

                public void onAnimationEnd(Animator animator) {
                    if (!this.mCancelled) {
                        TvOngoingPrivacyChip.this.onIconAnimationFinished();
                    }
                }
            });
        } else if (objectAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        if (this.mIconsContainer.getAlpha() != f) {
            this.mAnimator.setDuration((long) this.mAnimationDurationMs);
            this.mAnimator.setFloatValues(new float[]{f});
            this.mAnimator.start();
        }
    }

    public void onFadeOutFinished() {
        if (this.mState == 4) {
            removeIndicatorView();
            this.mState = 0;
        }
    }

    /* access modifiers changed from: private */
    public void onIconAnimationFinished() {
        int i = this.mState;
        if (i == 1 || i == 2) {
            collapseLater();
        }
        int i2 = this.mState;
        if (i2 == 1) {
            this.mState = 2;
        } else if (i2 == 4) {
            removeIndicatorView();
            this.mState = 0;
        }
    }

    private void removeIndicatorView() {
        ViewGroup viewGroup;
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService(WindowManager.class);
        if (!(windowManager == null || (viewGroup = this.mIndicatorView) == null)) {
            windowManager.removeView(viewGroup);
        }
        this.mIndicatorView = null;
        this.mAnimator = null;
        PrivacyChipDrawable privacyChipDrawable = this.mChipDrawable;
        if (privacyChipDrawable != null) {
            privacyChipDrawable.setListener((PrivacyChipDrawable.PrivacyChipDrawableListener) null);
            this.mChipDrawable = null;
        }
        this.mViewAndWindowAdded = false;
    }

    /* access modifiers changed from: private */
    public void postAccessibilityAnnouncement() {
        this.mUiThreadHandler.removeCallbacks(this.mAccessibilityRunnable);
        if (this.mPrivacyItems.size() == 0) {
            makeAccessibilityAnnouncement();
        } else {
            this.mUiThreadHandler.postDelayed(this.mAccessibilityRunnable, 500);
        }
    }

    /* access modifiers changed from: private */
    public void makeAccessibilityAnnouncement() {
        int i;
        if (this.mIndicatorView != null) {
            boolean listContainsPrivacyType = listContainsPrivacyType(this.mItemsBeforeLastAnnouncement, PrivacyType.TYPE_CAMERA);
            boolean listContainsPrivacyType2 = listContainsPrivacyType(this.mPrivacyItems, PrivacyType.TYPE_CAMERA);
            boolean listContainsPrivacyType3 = listContainsPrivacyType(this.mItemsBeforeLastAnnouncement, PrivacyType.TYPE_MICROPHONE);
            boolean listContainsPrivacyType4 = listContainsPrivacyType(this.mPrivacyItems, PrivacyType.TYPE_MICROPHONE);
            if (!listContainsPrivacyType && listContainsPrivacyType2 && !listContainsPrivacyType3 && listContainsPrivacyType4) {
                i = C1893R.string.mic_and_camera_recording_announcement;
            } else if (!listContainsPrivacyType || listContainsPrivacyType2 || !listContainsPrivacyType3 || listContainsPrivacyType4) {
                i = (!listContainsPrivacyType || listContainsPrivacyType2) ? (listContainsPrivacyType || !listContainsPrivacyType2) ? 0 : C1893R.string.camera_recording_announcement : C1893R.string.camera_stopped_recording_announcement;
                if (i != 0) {
                    this.mIndicatorView.announceForAccessibility(this.mContext.getString(i));
                    i = 0;
                }
                if (listContainsPrivacyType3 && !listContainsPrivacyType4) {
                    i = C1893R.string.mic_stopped_recording_announcement;
                } else if (!listContainsPrivacyType3 && listContainsPrivacyType4) {
                    i = C1893R.string.mic_recording_announcement;
                }
            } else {
                i = C1893R.string.mic_camera_stopped_recording_announcement;
            }
            if (i != 0) {
                this.mIndicatorView.announceForAccessibility(this.mContext.getString(i));
            }
            this.mItemsBeforeLastAnnouncement.clear();
            this.mItemsBeforeLastAnnouncement.addAll(this.mPrivacyItems);
        }
    }

    private boolean listContainsPrivacyType(List<PrivacyItem> list, PrivacyType privacyType) {
        for (PrivacyItem privacyType2 : list) {
            if (privacyType2.getPrivacyType() == privacyType) {
                return true;
            }
        }
        return false;
    }
}
