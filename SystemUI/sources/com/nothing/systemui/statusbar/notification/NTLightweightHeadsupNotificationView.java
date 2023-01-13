package com.nothing.systemui.statusbar.notification;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.icu.text.DateFormat;
import android.service.notification.StatusBarNotification;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.PathInterpolator;
import androidx.constraintlayout.motion.widget.Key;
import androidx.core.app.NotificationCompat;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.C1894R;
import com.android.systemui.Dependency;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.SwipeHelper;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.DismissedByUserStats;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.nothing.systemui.NTDependencyEx;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000¨\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0015\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\b\u0016\u0018\u0000 U2\u00020\u00012\u00020\u00022\u00020\u0003:\u0002UVB\u001d\b\u0016\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\u0002\u0010\bJ\b\u0010\"\u001a\u00020#H\u0014J\u0012\u0010$\u001a\u00020\u00102\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\u001a\u0010'\u001a\u00020\u00102\b\u0010%\u001a\u0004\u0018\u00010&2\u0006\u0010(\u001a\u00020\u0010H\u0016J\u0010\u0010)\u001a\u00020\u00102\u0006\u0010*\u001a\u00020&H\u0016J\u001a\u0010+\u001a\u0004\u0018\u00010\f2\u0006\u0010,\u001a\u00020\u001b2\u0006\u0010-\u001a\u00020#H\u0002J\u0014\u0010.\u001a\u0004\u0018\u00010&2\b\u0010/\u001a\u0004\u0018\u000100H\u0016J\b\u00101\u001a\u00020#H\u0016J\u0018\u00102\u001a\u0002032\u0006\u00104\u001a\u0002052\u0006\u00106\u001a\u00020\u0010H\u0002J\b\u00107\u001a\u00020\nH\u0016J\b\u00108\u001a\u00020#H\u0014J\b\u00109\u001a\u00020#H\u0014J\b\u0010:\u001a\u00020#H\u0014J\b\u0010;\u001a\u00020<H\u0014J\b\u0010=\u001a\u00020<H\u0014J\b\u0010>\u001a\u00020<H\u0014J\b\u0010?\u001a\u00020\u0010H\u0016J\b\u0010@\u001a\u00020\u0010H\u0016J\u0012\u0010A\u001a\u00020<2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\u0012\u0010B\u001a\u00020<2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\u001a\u0010C\u001a\u00020<2\b\u0010*\u001a\u0004\u0018\u00010&2\u0006\u0010D\u001a\u00020\nH\u0016J\u0010\u0010E\u001a\u00020<2\u0006\u0010%\u001a\u00020&H\u0016J\u0012\u0010F\u001a\u00020<2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\u0010\u0010G\u001a\u00020\u00102\u0006\u0010/\u001a\u000200H\u0016J\u0012\u0010H\u001a\u00020<2\b\u0010%\u001a\u0004\u0018\u00010&H\u0016J\u0010\u0010I\u001a\u00020\u00102\u0006\u0010/\u001a\u000200H\u0016J\u001a\u0010J\u001a\u00020<2\u0006\u0010K\u001a\u00020\u00102\b\u0010L\u001a\u0004\u0018\u00010MH\u0016J\u0010\u0010N\u001a\u00020<2\b\u0010O\u001a\u0004\u0018\u00010PJ\u000e\u0010Q\u001a\u00020\u00102\u0006\u0010\u001c\u001a\u00020\u001dJ\"\u0010R\u001a\u00020\u00102\b\u0010*\u001a\u0004\u0018\u00010&2\u0006\u0010S\u001a\u00020\u00102\u0006\u0010T\u001a\u00020\nH\u0016R\u000e\u0010\t\u001a\u00020\nXD¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX.¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0018\u00010\u0012R\u00020\u0000X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X.¢\u0006\u0002\n\u0000R\u0010\u0010\u0015\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0017\u001a\u0004\u0018\u00010\u0016X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0018\u001a\u0004\u0018\u00010\u0019X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u001a\u001a\u0004\u0018\u00010\u001bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u001dX.¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX.¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0004¢\u0006\u0002\n\u0000¨\u0006W"}, mo65043d2 = {"Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupNotificationView;", "Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupView;", "Landroid/view/View$OnClickListener;", "Lcom/android/systemui/SwipeHelper$Callback;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "MAX_ALPHA", "", "appIcon", "Landroid/graphics/drawable/Drawable;", "clickOutAnimatorSet", "Landroid/animation/AnimatorSet;", "draging", "", "edgeSwipeHelper", "Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupNotificationView$EdgeSwipeHelper;", "flipOutAnimator", "Landroid/animation/ObjectAnimator;", "messageContent", "", "messageTitle", "packageManager", "Landroid/content/pm/PackageManager;", "packageName", "", "row", "Lcom/android/systemui/statusbar/notification/row/ExpandableNotificationRow;", "swipeHelper", "Lcom/android/systemui/SwipeHelper;", "tmpPosOnScreen", "", "calculateTextLayoutWidth", "", "canChildBeDismissed", "v", "Landroid/view/View;", "canChildBeDismissedInDirection", "isRightOrDown", "canChildBeDragged", "animView", "getAppIcon", "pkgName", "userId", "getChildAtPosition", "ev", "Landroid/view/MotionEvent;", "getConstrainSwipeStartPosition", "getDismissedByUserStats", "Lcom/android/systemui/statusbar/notification/collection/notifcollection/DismissedByUserStats;", "entry", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "isVisible", "getFalsingThresholdFactor", "getInflateLayoutId", "getLayoutHeight", "getLayoutWidth", "init", "", "initAnimator", "initView", "isAntiFalsingNeeded", "isShowing", "onBeginDrag", "onChildDismissed", "onChildSnappedBack", "targetLeft", "onClick", "onDragCancelled", "onInterceptTouchEvent", "onLongPressSent", "onTouchEvent", "releasePopWithAnimation", "show", "runnable", "Ljava/lang/Runnable;", "startFlipOutAnimation", "listener", "Landroid/animation/Animator$AnimatorListener;", "updateNotificationRow", "updateSwipeProgress", "dismissable", "swipeProgress", "Companion", "EdgeSwipeHelper", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NTLightweightHeadsupNotificationView.kt */
public class NTLightweightHeadsupNotificationView extends NTLightweightHeadsupView implements View.OnClickListener, SwipeHelper.Callback {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final boolean DEBUG_EDGE_SWIPE = false;
    private static final boolean DISABLE_TOUCH_AND_SWIPE = true;
    /* access modifiers changed from: private */
    public static final String TAG = "NTLightweightHeadsupNotificationView";
    private final float MAX_ALPHA;
    public Map<Integer, View> _$_findViewCache;
    /* access modifiers changed from: private */
    public Drawable appIcon;
    private AnimatorSet clickOutAnimatorSet;
    private boolean draging;
    private EdgeSwipeHelper edgeSwipeHelper;
    private ObjectAnimator flipOutAnimator;
    /* access modifiers changed from: private */
    public CharSequence messageContent;
    /* access modifiers changed from: private */
    public CharSequence messageTitle;
    private PackageManager packageManager;
    private String packageName;
    /* access modifiers changed from: private */
    public ExpandableNotificationRow row;
    private SwipeHelper swipeHelper;
    private final int[] tmpPosOnScreen;

    public void _$_clearFindViewByIdCache() {
        this._$_findViewCache.clear();
    }

    public View _$_findCachedViewById(int i) {
        Map<Integer, View> map = this._$_findViewCache;
        View view = map.get(Integer.valueOf(i));
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        if (findViewById == null) {
            return null;
        }
        map.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public boolean canChildBeDismissed(View view) {
        return true;
    }

    public boolean canChildBeDragged(View view) {
        Intrinsics.checkNotNullParameter(view, "animView");
        return true;
    }

    public int getConstrainSwipeStartPosition() {
        return 0;
    }

    public float getFalsingThresholdFactor() {
        return 1.0f;
    }

    /* access modifiers changed from: protected */
    public int getInflateLayoutId() {
        return C1894R.layout.lightweight_headsup_notification_layout;
    }

    public boolean isAntiFalsingNeeded() {
        return false;
    }

    public void onDragCancelled(View view) {
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "ev");
        return false;
    }

    public void onLongPressSent(View view) {
    }

    public boolean updateSwipeProgress(View view, boolean z, float f) {
        return false;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public NTLightweightHeadsupNotificationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this._$_findViewCache = new LinkedHashMap();
        this.MAX_ALPHA = 1.0f;
        this.tmpPosOnScreen = new int[2];
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ NTLightweightHeadsupNotificationView(Context context, AttributeSet attributeSet, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : attributeSet);
    }

    /* access modifiers changed from: protected */
    public int getLayoutWidth() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.nt_pop_view_margin_start) + getResources().getDimensionPixelSize(C1894R.dimen.nt_pop_icon_diameter) + getResources().getDimensionPixelSize(C1894R.dimen.nt_pop_view_margin_mid) + getResources().getDimensionPixelSize(C1894R.dimen.nt_pop_view_margin_end) + getLargerTextViewWidth();
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(C1894R.dimen.nt_pop_view_min_width);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(C1894R.dimen.nt_pop_view_max_width);
        if (dimensionPixelSize3 > getScreenWidth()) {
            dimensionPixelSize3 = getScreenWidth();
        }
        if (dimensionPixelSize < dimensionPixelSize2) {
            return dimensionPixelSize2;
        }
        return dimensionPixelSize > dimensionPixelSize3 ? dimensionPixelSize3 : dimensionPixelSize;
    }

    /* access modifiers changed from: protected */
    public int calculateTextLayoutWidth() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.nt_pop_view_margin_start) + getResources().getDimensionPixelSize(C1894R.dimen.nt_pop_icon_diameter) + getResources().getDimensionPixelSize(C1894R.dimen.nt_pop_view_margin_mid) + getResources().getDimensionPixelSize(C1894R.dimen.nt_pop_view_margin_end);
        int largerTextViewWidth = getLargerTextViewWidth();
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(C1894R.dimen.nt_pop_view_max_width);
        if (dimensionPixelSize2 > getScreenWidth()) {
            dimensionPixelSize2 = getScreenWidth();
        }
        return Math.min(dimensionPixelSize2 - dimensionPixelSize, largerTextViewWidth);
    }

    /* access modifiers changed from: protected */
    public int getLayoutHeight() {
        return getResources().getDimensionPixelSize(C1894R.dimen.nt_pop_view_height);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0009, code lost:
        r0 = r0.getResources();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void init() {
        /*
            r2 = this;
            super.init()
            android.content.Context r0 = r2.getContext()
            if (r0 == 0) goto L_0x001b
            android.content.res.Resources r0 = r0.getResources()
            if (r0 == 0) goto L_0x001b
            r1 = 2131166683(0x7f0705db, float:1.7947618E38)
            float r0 = r0.getDimension(r1)
            java.lang.Float r0 = java.lang.Float.valueOf((float) r0)
            goto L_0x001c
        L_0x001b:
            r0 = 0
        L_0x001c:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            float r0 = r0.floatValue()
            r2.setOFFSET_Y(r0)
            float r0 = r2.getOFFSET_Y()
            r2.setOFFSET_Y_OUT_SCREEN(r0)
            android.content.Context r2 = r2.getContext()
            android.view.ViewConfiguration r2 = android.view.ViewConfiguration.get(r2)
            java.lang.String r0 = "get(getContext())"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r2, r0)
            r2.getScaledTouchSlop()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.statusbar.notification.NTLightweightHeadsupNotificationView.init():void");
    }

    /* access modifiers changed from: protected */
    public void initView() {
        super.initView();
    }

    /* access modifiers changed from: protected */
    public void initAnimator() {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, Key.ALPHA, new float[]{0.0f, 1.0f});
        Intrinsics.checkNotNullExpressionValue(ofFloat, "ofFloat(this, \"alpha\", 0f, 1f)");
        ofFloat.setInterpolator(new PathInterpolator(0.3f, 0.0f, 0.67f, 1.0f));
        ofFloat.setDuration(100);
        ofFloat.setStartDelay(280);
        setInAnimatorSet(new AnimatorSet());
        getInAnimatorSet().play(ofFloat);
        getInAnimatorSet().addListener(new NTLightweightHeadsupNotificationView$initAnimator$1(this));
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(getPopMessageLayout(), Key.ALPHA, new float[]{1.0f, 0.0f});
        Intrinsics.checkNotNullExpressionValue(ofFloat2, "ofFloat(popMessageLayout, \"alpha\", 1f, 0f)");
        ofFloat2.setInterpolator(new PathInterpolator(0.3f, 0.16f, 0.2f, 1.0f));
        ofFloat2.setDuration(70);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(this, "scale", new float[]{1.0f, 0.0f});
        Intrinsics.checkNotNullExpressionValue(ofFloat3, "ofFloat(this, \"scale\", 1f, 0f)");
        ofFloat3.setInterpolator(new PathInterpolator(0.3f, 0.0f, 0.67f, 1.0f));
        ofFloat3.setDuration(130);
        ofFloat3.setStartDelay(183);
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(this, Key.ALPHA, new float[]{1.0f, 0.0f});
        Intrinsics.checkNotNullExpressionValue(ofFloat4, "ofFloat(this, \"alpha\", 1f, 0f)");
        setAllOutAnimator(ofFloat4);
        getAllOutAnimator().setInterpolator(new PathInterpolator(0.3f, 0.0f, 0.67f, 1.0f));
        getAllOutAnimator().setDuration(100);
        ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(this, Key.ALPHA, new float[]{1.0f, 0.0f});
        Intrinsics.checkNotNullExpressionValue(ofFloat5, "ofFloat(this, \"alpha\", 1f, 0f)");
        ofFloat5.setInterpolator(new PathInterpolator(0.3f, 0.0f, 0.67f, 1.0f));
        ofFloat5.setDuration(200);
        ofFloat5.setStartDelay(117);
        ofFloat5.addListener(new NTLightweightHeadsupNotificationView$initAnimator$2(this));
        AnimatorSet animatorSet = new AnimatorSet();
        this.clickOutAnimatorSet = animatorSet;
        Animator animator = ofFloat5;
        animatorSet.playTogether(new Animator[]{ofFloat2, ofFloat3, animator});
        AnimatorSet animatorSet2 = this.clickOutAnimatorSet;
        ObjectAnimator objectAnimator = null;
        if (animatorSet2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("clickOutAnimatorSet");
            animatorSet2 = null;
        }
        animatorSet2.addListener(new NTLightweightHeadsupNotificationView$initAnimator$3(this));
        setOutAnimatorSet(new AnimatorSet());
        getOutAnimatorSet().play(animator);
        ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(this, Key.TRANSLATION_Y, new float[]{getOFFSET_Y(), getOFFSET_Y() - 200.0f});
        Intrinsics.checkNotNullExpressionValue(ofFloat6, "ofFloat(this, \"translati… OFFSET_Y, OFFSET_Y-200f)");
        this.flipOutAnimator = ofFloat6;
        if (ofFloat6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("flipOutAnimator");
        } else {
            objectAnimator = ofFloat6;
        }
        objectAnimator.setDuration(200);
    }

    public final void startFlipOutAnimation(Animator.AnimatorListener animatorListener) {
        ObjectAnimator objectAnimator = null;
        if (getDEBUG()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder("startFlipOutAnimation animator = ");
            ObjectAnimator objectAnimator2 = this.flipOutAnimator;
            if (objectAnimator2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("flipOutAnimator");
                objectAnimator2 = null;
            }
            Log.d(str, sb.append((Object) objectAnimator2).toString());
        }
        ObjectAnimator objectAnimator3 = this.flipOutAnimator;
        if (objectAnimator3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("flipOutAnimator");
            objectAnimator3 = null;
        }
        objectAnimator3.removeAllListeners();
        ObjectAnimator objectAnimator4 = this.flipOutAnimator;
        if (objectAnimator4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("flipOutAnimator");
            objectAnimator4 = null;
        }
        objectAnimator4.addListener(animatorListener);
        ObjectAnimator objectAnimator5 = this.flipOutAnimator;
        if (objectAnimator5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("flipOutAnimator");
        } else {
            objectAnimator = objectAnimator5;
        }
        objectAnimator.addListener(new NTLightweightHeadsupNotificationView$startFlipOutAnimation$1(this));
    }

    public final boolean updateNotificationRow(ExpandableNotificationRow expandableNotificationRow) {
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "row");
        Notification notification = expandableNotificationRow.getEntry().getSbn().getNotification();
        Intrinsics.checkNotNullExpressionValue(notification, "row.getEntry().getSbn().getNotification()");
        String opPkg = expandableNotificationRow.getEntry().getSbn().getOpPkg();
        Intrinsics.checkNotNullExpressionValue(opPkg, "row.getEntry().getSbn().getOpPkg()");
        if (notification.extras == null) {
            return false;
        }
        String string = notification.extras.getString("package_name");
        if (string == null) {
            string = opPkg;
        }
        Drawable appIcon2 = getAppIcon(string, expandableNotificationRow.getEntry().getSbn().getUser().getIdentifier());
        if (appIcon2 == null) {
            return false;
        }
        this.appIcon = appIcon2;
        this.messageTitle = "" + notification.extras.getCharSequence(NotificationCompat.EXTRA_TITLE);
        this.messageContent = "" + notification.extras.getCharSequence(NotificationCompat.EXTRA_TEXT);
        getAppIconView().setImageDrawable(this.appIcon);
        getTitleView().setText(this.messageTitle);
        getContentView().setText(this.messageContent);
        updateLayoutOffset();
        this.row = expandableNotificationRow;
        this.packageName = opPkg;
        return true;
    }

    public void onClick(View view) {
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        if (getDEBUG()) {
            Log.d(TAG, "onClick id = " + view.getId() + ", container = 2131427731");
        }
        AnimatorSet animatorSet = this.clickOutAnimatorSet;
        if (animatorSet == null) {
            Intrinsics.throwUninitializedPropertyAccessException("clickOutAnimatorSet");
            animatorSet = null;
        }
        animatorSet.start();
    }

    public void releasePopWithAnimation(boolean z, Runnable runnable) {
        if (!z) {
            if ((getAlpha() == 0.0f) && runnable != null) {
                getOutAnimatorSet().end();
                runnable.run();
                return;
            }
        }
        super.releasePopWithAnimation(z, runnable);
    }

    public boolean isShowing() {
        return ((NTLightweightHeadsupManager) NTDependencyEx.get(NTLightweightHeadsupManager.class)).isShowPopNotification() && isPopShowing() && !isReleased();
    }

    public View getChildAtPosition(MotionEvent motionEvent) {
        return this;
    }

    public boolean canChildBeDismissedInDirection(View view, boolean z) {
        return canChildBeDismissed(view);
    }

    public void onBeginDrag(View view) {
        this.draging = true;
    }

    public void onChildDismissed(View view) {
        Log.d(TAG, "onChildDismissed");
        this.draging = false;
        if (this.row == null) {
            Intrinsics.throwUninitializedPropertyAccessException("row");
        }
        Object obj = Dependency.get(NotificationEntryManager.class);
        Intrinsics.checkNotNullExpressionValue(obj, "get(NotificationEntryManager::class.java)");
        NotificationEntryManager notificationEntryManager = (NotificationEntryManager) obj;
        ExpandableNotificationRow expandableNotificationRow = this.row;
        ExpandableNotificationRow expandableNotificationRow2 = null;
        if (expandableNotificationRow == null) {
            Intrinsics.throwUninitializedPropertyAccessException("row");
            expandableNotificationRow = null;
        }
        StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
        ExpandableNotificationRow expandableNotificationRow3 = this.row;
        if (expandableNotificationRow3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("row");
        } else {
            expandableNotificationRow2 = expandableNotificationRow3;
        }
        NotificationEntry entry = expandableNotificationRow2.getEntry();
        Intrinsics.checkNotNullExpressionValue(entry, "row.getEntry()");
        notificationEntryManager.performRemoveNotification(sbn, getDismissedByUserStats(entry, false), 2);
    }

    private final DismissedByUserStats getDismissedByUserStats(NotificationEntry notificationEntry, boolean z) {
        return new DismissedByUserStats(3, 1, NotificationVisibility.obtain(notificationEntry.getKey(), notificationEntry.getRanking().getRank(), 0, z, NotificationLogger.getNotificationLocation(notificationEntry)));
    }

    public void onChildSnappedBack(View view, float f) {
        this.draging = false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "ev");
        return super.onTouchEvent(motionEvent);
    }

    private final Drawable getAppIcon(String str, int i) {
        Drawable defaultActivityIcon;
        PackageManager packageManager2;
        if (this.packageManager == null) {
            this.packageManager = CentralSurfaces.getPackageManagerForUser(this.mContext, i);
        }
        try {
            PackageManager packageManager3 = this.packageManager;
            ApplicationInfo applicationInfo = packageManager3 != null ? packageManager3.getApplicationInfo(str, 795136) : null;
            if (applicationInfo == null || (packageManager2 = this.packageManager) == null) {
                return null;
            }
            defaultActivityIcon = packageManager2.getApplicationIcon(applicationInfo);
            return defaultActivityIcon;
        } catch (PackageManager.NameNotFoundException unused) {
            PackageManager packageManager4 = this.packageManager;
            if (packageManager4 == null) {
                return null;
            }
            defaultActivityIcon = packageManager4.getDefaultActivityIcon();
        }
    }

    @Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\r\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u0010\u0010\u0010\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u000fH\u0016R\u001a\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0003X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo65043d2 = {"Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupNotificationView$EdgeSwipeHelper;", "Lcom/android/systemui/Gefingerpoken;", "touchSlop", "", "(Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupNotificationView;F)V", "consuming", "", "getConsuming", "()Z", "setConsuming", "(Z)V", "downX", "downY", "onInterceptTouchEvent", "ev", "Landroid/view/MotionEvent;", "onTouchEvent", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NTLightweightHeadsupNotificationView.kt */
    public final class EdgeSwipeHelper implements Gefingerpoken {
        private boolean consuming;
        private float downX;
        private float downY;
        private final float touchSlop;

        public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, "ev");
            return true;
        }

        public EdgeSwipeHelper(float f) {
            this.touchSlop = f;
        }

        public final boolean getConsuming() {
            return this.consuming;
        }

        public final void setConsuming(boolean z) {
            this.consuming = z;
        }

        public boolean onTouchEvent(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, "ev");
            return onInterceptTouchEvent(motionEvent);
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000¨\u0006\b"}, mo65043d2 = {"Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupNotificationView$Companion;", "", "()V", "DEBUG_EDGE_SWIPE", "", "DISABLE_TOUCH_AND_SWIPE", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NTLightweightHeadsupNotificationView.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    static {
        Intrinsics.checkNotNullExpressionValue("NTLightweightHeadsupNotificationView", "NTLightweightHeadsupNoti…lass.java.getSimpleName()");
    }
}
