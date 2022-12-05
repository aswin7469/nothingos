package com.nothingos.systemui.statusbar.policy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.Dependency;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.SwipeHelper;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.phone.StatusBar;
import com.nothingos.headsup.NothingOSHeadsupManager;
/* loaded from: classes2.dex */
public class PopNotificationView extends FrameLayout implements View.OnClickListener, SwipeHelper.Callback, ViewTreeObserver.OnComputeInternalInsetsListener {
    private static final String TAG = PopNotificationView.class.getSimpleName();
    private boolean DEBUG;
    private boolean isPopShowing;
    private boolean isReleased;
    private LinearLayout linearLayout;
    private ObjectAnimator mAllOut;
    private Drawable mAppIcon;
    private int mCenterY;
    private AnimatorSet mClickOutSet;
    private ObjectAnimator mCollapseAnimator;
    private LinearLayout mContainer;
    private int mDiameter;
    private Display mDisplay;
    private DisplayMetrics mDisplayMetrics;
    private boolean mDraging;
    private EdgeSwipeHelper mEdgeSwipeHelper;
    private ObjectAnimator mExpandAnimator;
    private ObjectAnimator mFlipOutAnimator;
    private AnimatorSet mInSet;
    private ImageView mIvAppIcon;
    private final float mMaxAlpha;
    private CharSequence mMessageContent;
    private int mMessagePaddingEnd;
    private CharSequence mMessageTitle;
    private NothingOSHeadsupManager mNothingOSHeadsupManager;
    private String mPackage;
    private PackageManager mPm;
    private RelativeLayout mPopMessageLayout;
    private ExpandableNotificationRow mRow;
    private SwipeHelper mSwipeHelper;
    private float mTargetWidth;
    private TextPaint mTextPaint;
    private int[] mTmpTwoArray;
    private TextView mTvContent;
    private TextView mTvTitle;
    private ObjectAnimator mUpdateCollapseAnimator;
    private ObjectAnimator mUpdateExpandAnimator;
    private ObjectAnimator mZoomInAnimator;

    @Override // com.android.systemui.SwipeHelper.Callback
    public boolean canChildBeDismissed(View view) {
        return true;
    }

    @Override // com.android.systemui.SwipeHelper.Callback
    public boolean canChildBeDragged(View view) {
        return true;
    }

    @Override // com.android.systemui.SwipeHelper.Callback
    public View getChildAtPosition(MotionEvent motionEvent) {
        return this;
    }

    @Override // com.android.systemui.SwipeHelper.Callback
    public int getConstrainSwipeStartPosition() {
        return 0;
    }

    @Override // com.android.systemui.SwipeHelper.Callback
    public float getFalsingThresholdFactor() {
        return 1.0f;
    }

    @Override // com.android.systemui.SwipeHelper.Callback
    public boolean isAntiFalsingNeeded() {
        return false;
    }

    @Override // com.android.systemui.SwipeHelper.Callback
    public boolean updateSwipeProgress(View view, boolean z, float f) {
        return false;
    }

    public PopNotificationView(Context context) {
        this(context, null);
    }

    public PopNotificationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        String str = TAG;
        this.DEBUG = Log.isLoggable(str, 3);
        this.isPopShowing = false;
        this.mTargetWidth = 0.0f;
        this.mMaxAlpha = 1.0f;
        this.isReleased = true;
        this.mTmpTwoArray = new int[2];
        this.mDisplayMetrics = new DisplayMetrics();
        this.mDisplay = ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay();
        setWillNotDraw(false);
        this.mDiameter = getResources().getDimensionPixelSize(R$dimen.pop_view_height);
        Log.d(str, "[foree] setUpView: mDiameter = " + this.mDiameter);
        this.mMessagePaddingEnd = getResources().getDimensionPixelSize(R$dimen.pop_view_message_margin_end);
        this.mCenterY = this.mDiameter / 2;
        init();
    }

    private void init() {
        setUpView();
        setUpAnimator();
        setOnClickListener(this);
        setPivotY(this.mCenterY);
        setOffset(0.0f);
        setScale(0.0f);
        setAlpha(0.0f);
        SwipeHelper swipeHelper = new SwipeHelper(0, this, ((FrameLayout) this).mContext.getResources(), ViewConfiguration.get(((FrameLayout) this).mContext), (FalsingManager) Dependency.get(FalsingManager.class));
        this.mSwipeHelper = swipeHelper;
        swipeHelper.setMaxSwipeProgress(1.0f);
        this.mEdgeSwipeHelper = new EdgeSwipeHelper(ViewConfiguration.get(getContext()).getScaledTouchSlop());
    }

    private void setUpView() {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R$layout.pop_notification_layout, (ViewGroup) this, false);
        this.linearLayout = linearLayout;
        this.mIvAppIcon = (ImageView) linearLayout.findViewById(R$id.icon);
        RelativeLayout relativeLayout = (RelativeLayout) this.linearLayout.findViewById(R$id.pop_message);
        this.mPopMessageLayout = relativeLayout;
        this.mTvTitle = (TextView) relativeLayout.findViewById(R$id.title);
        this.mTvContent = (TextView) this.mPopMessageLayout.findViewById(R$id.content);
        this.mContainer = (LinearLayout) this.linearLayout.findViewById(R$id.container);
        this.mTextPaint = this.mTvTitle.getPaint();
        this.mPopMessageLayout.setAlpha(0.0f);
        this.mContainer.setOnClickListener(this);
        addView(this.linearLayout, new FrameLayout.LayoutParams(getResources().getDimensionPixelSize(R$dimen.pop_view_max_width), this.mDiameter));
    }

    private void setUpAnimator() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "scale", 0.0f, 1.0f);
        this.mZoomInAnimator = ofFloat;
        ofFloat.setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.67f, 1.0f));
        this.mZoomInAnimator.setDuration(150L);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, "offset", 0.0f, this.mTargetWidth);
        this.mExpandAnimator = ofFloat2;
        ofFloat2.setInterpolator(new PathInterpolator(0.3f, 0.53f, 0.2f, 1.0f));
        this.mExpandAnimator.setDuration(330L);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this.mPopMessageLayout, "alpha", 0.0f, 1.0f);
        ofFloat3.setInterpolator(new PathInterpolator(0.3f, 0.0f, 0.67f, 1.0f));
        ofFloat3.setDuration(200L);
        ofFloat3.setStartDelay(280L);
        AnimatorSet animatorSet = new AnimatorSet();
        this.mInSet = animatorSet;
        animatorSet.play(this.mZoomInAnimator).with(ofFloat3);
        this.mInSet.addListener(new AnimatorListenerAdapter() { // from class: com.nothingos.systemui.statusbar.policy.PopNotificationView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                super.onAnimationStart(animator);
                Log.d(PopNotificationView.TAG, "[foree] onAnimationStart: mInset start");
                PopNotificationView.this.setAlpha(1.0f);
                PopNotificationView.this.mPopMessageLayout.setAlpha(0.0f);
                PopNotificationView.this.mIvAppIcon.setImageDrawable(PopNotificationView.this.mAppIcon);
                PopNotificationView.this.mTvTitle.setText(PopNotificationView.this.mMessageTitle);
                PopNotificationView.this.mTvContent.setText(PopNotificationView.this.mMessageContent);
            }
        });
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this.mPopMessageLayout, "alpha", 1.0f, 0.0f);
        ofFloat4.setInterpolator(new PathInterpolator(0.3f, 0.16f, 0.2f, 1.0f));
        ofFloat4.setDuration(70L);
        ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(this, "offset", this.mTargetWidth, 0.0f);
        this.mCollapseAnimator = ofFloat5;
        ofFloat5.setInterpolator(new PathInterpolator(0.3f, 0.65f, 0.2f, 1.0f));
        this.mCollapseAnimator.setDuration(250L);
        ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(this, "scale", 1.0f, 0.0f);
        ofFloat6.setInterpolator(new PathInterpolator(0.3f, 0.0f, 0.67f, 1.0f));
        ofFloat6.setDuration(130L);
        ofFloat6.setStartDelay(183L);
        ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
        this.mAllOut = ofFloat7;
        ofFloat7.setInterpolator(new PathInterpolator(0.3f, 0.0f, 0.67f, 1.0f));
        this.mAllOut.setDuration(200L);
        ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
        ofFloat8.setInterpolator(new PathInterpolator(0.3f, 0.0f, 0.67f, 1.0f));
        ofFloat8.setDuration(200L);
        ofFloat8.setStartDelay(117L);
        AnimatorSet animatorSet2 = new AnimatorSet();
        this.mClickOutSet = animatorSet2;
        animatorSet2.playTogether(ofFloat4, ofFloat6, ofFloat8);
        this.mClickOutSet.addListener(new AnimatorListenerAdapter() { // from class: com.nothingos.systemui.statusbar.policy.PopNotificationView.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                if (PopNotificationView.this.mRow != null) {
                    ((NothingOSHeadsupManager) Dependency.get(NothingOSHeadsupManager.class)).hidePopNotificationView();
                    PopNotificationView.this.mRow.performClick();
                    Log.d(PopNotificationView.TAG, "[foree] onAnimationEnd: performClick");
                    PopNotificationView.this.isReleased = true;
                }
            }
        });
        ObjectAnimator ofFloat9 = ObjectAnimator.ofFloat(this, "offset", this.mTargetWidth);
        this.mUpdateExpandAnimator = ofFloat9;
        ofFloat9.setDuration(20L);
        ObjectAnimator ofFloat10 = ObjectAnimator.ofFloat(this, "offset", this.mTargetWidth, 0.0f);
        this.mUpdateCollapseAnimator = ofFloat10;
        ofFloat10.setDuration(20L);
        ObjectAnimator ofFloat11 = ObjectAnimator.ofFloat(this, "translationY", 0.0f, -20.0f);
        this.mFlipOutAnimator = ofFloat11;
        ofFloat11.setDuration(200L);
    }

    public void startFlipOutAnimation(Animator.AnimatorListener animatorListener) {
        this.mFlipOutAnimator.removeAllListeners();
        this.mFlipOutAnimator.addListener(animatorListener);
        this.mFlipOutAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.nothingos.systemui.statusbar.policy.PopNotificationView.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                PopNotificationView.this.setAlpha(0.0f);
                PopNotificationView.this.mPopMessageLayout.postDelayed(new Runnable() { // from class: com.nothingos.systemui.statusbar.policy.PopNotificationView.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        PopNotificationView.this.isReleased = true;
                    }
                }, 2000L);
            }
        });
        this.mFlipOutAnimator.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setOffset(float f) {
        if (this.DEBUG) {
            String str = TAG;
            Log.d(str, "setOffset() called with: offset = [" + f + "]");
        }
        this.mZoomInAnimator.isRunning();
        invalidate();
    }

    private void setScale(float f) {
        if (this.DEBUG) {
            String str = TAG;
            Log.d(str, "setScale() called with: scale = [" + f + "]");
        }
        setScaleX(f);
        setScaleY(f);
    }

    public boolean updateNotificationRow(ExpandableNotificationRow expandableNotificationRow) {
        Notification notification = expandableNotificationRow.getEntry().getSbn().getNotification();
        String opPkg = expandableNotificationRow.getEntry().getSbn().getOpPkg();
        String string = notification.extras.getString("package_name");
        if (string == null) {
            string = opPkg;
        }
        Drawable appIcon = getAppIcon(string, expandableNotificationRow.getEntry().getSbn().getUser().getIdentifier());
        if (appIcon != null) {
            String str = "" + ((Object) notification.extras.getCharSequence("android.title"));
            String str2 = "" + ((Object) notification.extras.getCharSequence("android.text"));
            float min = (Math.min(this.mTextPaint.measureText(str.toString()) + this.mTextPaint.measureText(str2.toString()), this.mTvTitle.getMaxWidth() + this.mTvContent.getMaxWidth()) + this.mMessagePaddingEnd) / 2.0f;
            this.mTargetWidth = min;
            this.mExpandAnimator.setFloatValues(0.0f, min);
            this.mCollapseAnimator.setFloatValues(this.mTargetWidth, 0.0f);
            this.mAppIcon = appIcon;
            this.mMessageTitle = str;
            this.mMessageContent = str2;
            if (isShowing()) {
                this.mIvAppIcon.setImageDrawable(this.mAppIcon);
                this.mTvTitle.setText(this.mMessageTitle);
                this.mTvContent.setText(this.mMessageContent);
            }
            this.mRow = expandableNotificationRow;
            this.mPackage = opPkg;
            return true;
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mNothingOSHeadsupManager = (NothingOSHeadsupManager) Dependency.get(NothingOSHeadsupManager.class);
        updateLayoutOffset();
        getViewTreeObserver().addOnComputeInternalInsetsListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.container) {
            this.mClickOutSet.start();
        }
    }

    public void releasePopWithAnimation(boolean z, Animator.AnimatorListener animatorListener) {
        String str = TAG;
        Log.d(str, "releasePopWithAnimation() called with: show = " + z + ", listener = " + animatorListener + ", isReleased = " + this.isReleased + ", in running = " + this.mInSet.isRunning() + ", outRunning = " + this.mAllOut.isRunning() + ", getAlpha = " + getAlpha());
        Float valueOf = Float.valueOf(0.0f);
        if (z) {
            FrameLayout.TRANSLATION_X.set(this, valueOf);
            FrameLayout.TRANSLATION_Y.set(this, valueOf);
            this.mSwipeHelper.snapChild(this, 0.0f, 1.0f);
            setAlpha(1.0f);
            if (!this.isReleased || this.mInSet.isRunning()) {
                return;
            }
            this.mInSet.start();
            this.isReleased = false;
        } else if (getAlpha() == 0.0f && animatorListener != null) {
            this.mAllOut.addListener(animatorListener);
            this.mAllOut.end();
            setOffset(0.0f);
        } else if (this.mAllOut.isRunning()) {
            this.mAllOut.removeAllListeners();
            this.mAllOut.end();
            setOffset(0.0f);
            this.isReleased = true;
        } else if (animatorListener == null) {
        } else {
            this.mAllOut.removeAllListeners();
            this.mAllOut.addListener(animatorListener);
            this.mAllOut.addListener(new AnimatorListenerAdapter() { // from class: com.nothingos.systemui.statusbar.policy.PopNotificationView.4
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    Log.d(PopNotificationView.TAG, "onAnimationEnd: mAllout setOffset end");
                    PopNotificationView.this.setOffset(0.0f);
                    PopNotificationView.this.isReleased = true;
                }
            });
            this.mAllOut.start();
        }
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i) {
        this.isPopShowing = i == 0;
    }

    public boolean isShowing() {
        return this.mNothingOSHeadsupManager.isShowPopNotification() && this.isPopShowing && !this.isReleased;
    }

    public void updateLayoutOffset() {
        int dimensionPixelSize;
        this.mDisplay.getRealMetrics(this.mDisplayMetrics);
        if (getResources().getConfiguration().orientation == 1) {
            dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.pop_view_min_width);
        } else {
            dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.pop_view_max_width);
        }
        int i = this.mDisplayMetrics.widthPixels;
        this.linearLayout.getLayoutParams().width = dimensionPixelSize;
        setPivotX(i / 2);
        updateQuickOpenVisibility();
    }

    @Override // com.android.systemui.SwipeHelper.Callback
    public boolean canChildBeDismissedInDirection(View view, boolean z) {
        return canChildBeDismissed(view);
    }

    @Override // com.android.systemui.SwipeHelper.Callback
    public void onBeginDrag(View view) {
        Log.d(TAG, "onBeginDrag");
        this.mDraging = true;
    }

    @Override // com.android.systemui.SwipeHelper.Callback
    public void onChildDismissed(View view) {
        Log.d(TAG, "onChildDismissed");
        this.mDraging = false;
        if (this.mRow != null) {
            ((NotificationEntryManager) Dependency.get(NotificationEntryManager.class)).performRemoveNotification(this.mRow.getEntry().getSbn(), getDismissedByUserStats(this.mRow.getEntry(), false), 2);
        }
    }

    private DismissedByUserStats getDismissedByUserStats(NotificationEntry notificationEntry, boolean z) {
        return new DismissedByUserStats(3, 1, NotificationVisibility.obtain(notificationEntry.getKey(), notificationEntry.getRanking().getRank(), 0, z, NotificationLogger.getNotificationLocation(notificationEntry)));
    }

    @Override // com.android.systemui.SwipeHelper.Callback
    public void onDragCancelled(View view) {
        Log.d(TAG, "onDragCancelled");
    }

    @Override // com.android.systemui.SwipeHelper.Callback
    public void onChildSnappedBack(View view, float f) {
        Log.d(TAG, "onChildSnappedBack");
        this.mDraging = false;
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z = this.mEdgeSwipeHelper.onInterceptTouchEvent(motionEvent) || this.mSwipeHelper.onInterceptTouchEvent(motionEvent) || super.onInterceptTouchEvent(motionEvent);
        String str = TAG;
        Log.d(str, "onInterceptTouchEvent =" + z + ", action " + motionEvent.getAction());
        return z;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mEdgeSwipeHelper.onTouchEvent(motionEvent)) {
            if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
                this.mEdgeSwipeHelper.mConsuming = false;
            }
            return true;
        } else if (this.mSwipeHelper.onTouchEvent(motionEvent) && this.mDraging) {
            return true;
        } else {
            return super.onTouchEvent(motionEvent);
        }
    }

    public void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        internalInsetsInfo.setTouchableInsets(3);
        if (getChildAt(0) != null) {
            getChildAt(0).getLocationOnScreen(this.mTmpTwoArray);
            int width = getChildAt(0).getWidth();
            int height = getChildAt(0).getHeight();
            Region region = internalInsetsInfo.touchableRegion;
            int[] iArr = this.mTmpTwoArray;
            region.set(iArr[0], iArr[1], iArr[0] + width, iArr[1] + height);
        }
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateLayoutOffset();
    }

    public void updateQuickOpenVisibility() {
        this.mMessagePaddingEnd = getResources().getDimensionPixelSize(R$dimen.pop_view_message_margin_end);
        this.mPopMessageLayout.post(new Runnable() { // from class: com.nothingos.systemui.statusbar.policy.PopNotificationView.5
            @Override // java.lang.Runnable
            public void run() {
                if (Build.VERSION.SDK_INT <= 22 || !PopNotificationView.this.mNothingOSHeadsupManager.isForceQuickReply()) {
                    PopNotificationView.this.mMessagePaddingEnd -= PopNotificationView.this.getResources().getDimensionPixelSize(R$dimen.pop_view_button_width);
                }
            }
        });
    }

    private Drawable getAppIcon(String str, int i) {
        if (this.mPm == null) {
            this.mPm = StatusBar.getPackageManagerForUser(((FrameLayout) this).mContext, i);
        }
        try {
            ApplicationInfo applicationInfo = this.mPm.getApplicationInfo(str, 795136);
            if (applicationInfo == null) {
                return null;
            }
            return this.mPm.getApplicationIcon(applicationInfo);
        } catch (PackageManager.NameNotFoundException unused) {
            return this.mPm.getDefaultActivityIcon();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class EdgeSwipeHelper implements Gefingerpoken {
        private boolean mConsuming;
        private float mFirstX;
        private float mFirstY;
        private final float mTouchSlop;

        public EdgeSwipeHelper(float f) {
            this.mTouchSlop = f;
        }

        @Override // com.android.systemui.Gefingerpoken
        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked == 0) {
                this.mFirstX = motionEvent.getX();
                this.mFirstY = motionEvent.getY();
                this.mConsuming = false;
            } else if (actionMasked == 2) {
                float y = motionEvent.getY() - this.mFirstY;
                float abs = Math.abs(motionEvent.getX() - this.mFirstX);
                float abs2 = Math.abs(y);
                if (!this.mConsuming && abs < abs2 && abs2 > this.mTouchSlop * 2.0f) {
                    if (y > 0.0f) {
                        Log.d(PopNotificationView.TAG, "[foree] onInterceptTouchEvent: pop notification snooze");
                        PopNotificationView.this.mNothingOSHeadsupManager.snooze(PopNotificationView.this.mPackage);
                        PopNotificationView.this.mNothingOSHeadsupManager.hidePopNotificationView();
                    } else {
                        PopNotificationView.this.startFlipOutAnimation(new AnimatorListenerAdapter() { // from class: com.nothingos.systemui.statusbar.policy.PopNotificationView.EdgeSwipeHelper.1
                            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                            public void onAnimationEnd(Animator animator) {
                                super.onAnimationEnd(animator);
                                PopNotificationView.this.mNothingOSHeadsupManager.snooze(PopNotificationView.this.mPackage);
                            }
                        });
                    }
                    this.mConsuming = true;
                }
            }
            if (this.mConsuming) {
                String str = PopNotificationView.TAG;
                Log.d(str, "EdgeSwipeHelper onInterceptTouchEvent action=" + motionEvent.getActionMasked());
            }
            return this.mConsuming;
        }

        @Override // com.android.systemui.Gefingerpoken
        public boolean onTouchEvent(MotionEvent motionEvent) {
            return onInterceptTouchEvent(motionEvent);
        }
    }
}
