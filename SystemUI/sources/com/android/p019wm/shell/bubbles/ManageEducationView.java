package com.android.p019wm.shell.bubbles;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import com.android.p019wm.shell.C3353R;
import com.android.p019wm.shell.TaskView;
import com.android.p019wm.shell.animation.Interpolators;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0006\u0010 \u001a\u00020!J\b\u0010\"\u001a\u00020!H\u0014J\b\u0010#\u001a\u00020!H\u0002J\b\u0010$\u001a\u00020!H\u0002J\u0010\u0010%\u001a\u00020!2\u0006\u0010&\u001a\u00020'H\u0016J\u0010\u0010(\u001a\u00020!2\u0006\u0010)\u001a\u00020\u0015H\u0002J\u000e\u0010*\u001a\u00020!2\u0006\u0010+\u001a\u00020\fR\u000e\u0010\u0007\u001a\u00020\bXD¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u000e¢\u0006\u0002\n\u0000R#\u0010\r\u001a\n \u000f*\u0004\u0018\u00010\u000e0\u000e8BX\u0002¢\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0010\u0010\u0011R\u000e\u0010\u0014\u001a\u00020\u0015X\u000e¢\u0006\u0002\n\u0000R#\u0010\u0016\u001a\n \u000f*\u0004\u0018\u00010\u000e0\u000e8BX\u0002¢\u0006\f\n\u0004\b\u0018\u0010\u0013\u001a\u0004\b\u0017\u0010\u0011R#\u0010\u0019\u001a\n \u000f*\u0004\u0018\u00010\u001a0\u001a8BX\u0002¢\u0006\f\n\u0004\b\u001d\u0010\u0013\u001a\u0004\b\u001b\u0010\u001cR\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u000e¢\u0006\u0002\n\u0000¨\u0006,"}, mo65043d2 = {"Lcom/android/wm/shell/bubbles/ManageEducationView;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "positioner", "Lcom/android/wm/shell/bubbles/BubblePositioner;", "(Landroid/content/Context;Lcom/android/wm/shell/bubbles/BubblePositioner;)V", "ANIMATE_DURATION", "", "TAG", "", "bubbleExpandedView", "Lcom/android/wm/shell/bubbles/BubbleExpandedView;", "gotItButton", "Landroid/widget/Button;", "kotlin.jvm.PlatformType", "getGotItButton", "()Landroid/widget/Button;", "gotItButton$delegate", "Lkotlin/Lazy;", "isHiding", "", "manageButton", "getManageButton", "manageButton$delegate", "manageView", "Landroid/view/ViewGroup;", "getManageView", "()Landroid/view/ViewGroup;", "manageView$delegate", "realManageButtonRect", "Landroid/graphics/Rect;", "hide", "", "onFinishInflate", "setButtonColor", "setDrawableDirection", "setLayoutDirection", "layoutDirection", "", "setShouldShow", "shouldShow", "show", "expandedView", "WMShell_release"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.wm.shell.bubbles.ManageEducationView */
/* compiled from: ManageEducationView.kt */
public final class ManageEducationView extends LinearLayout {
    private final long ANIMATE_DURATION;
    private final String TAG;
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private BubbleExpandedView bubbleExpandedView;
    private final Lazy gotItButton$delegate;
    private boolean isHiding;
    private final Lazy manageButton$delegate;
    private final Lazy manageView$delegate;
    private final BubblePositioner positioner;
    private Rect realManageButtonRect;

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

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public ManageEducationView(Context context, BubblePositioner bubblePositioner) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(bubblePositioner, "positioner");
        this.TAG = BubbleDebugConfig.TAG_BUBBLES;
        this.ANIMATE_DURATION = 200;
        this.positioner = bubblePositioner;
        this.manageView$delegate = LazyKt.lazy(new ManageEducationView$manageView$2(this));
        this.manageButton$delegate = LazyKt.lazy(new ManageEducationView$manageButton$2(this));
        this.gotItButton$delegate = LazyKt.lazy(new ManageEducationView$gotItButton$2(this));
        this.realManageButtonRect = new Rect();
        LayoutInflater.from(context).inflate(C3353R.layout.bubbles_manage_button_education, this);
        setVisibility(8);
        setElevation((float) getResources().getDimensionPixelSize(C3353R.dimen.bubble_elevation));
        setLayoutDirection(3);
    }

    private final ViewGroup getManageView() {
        return (ViewGroup) this.manageView$delegate.getValue();
    }

    private final Button getManageButton() {
        return (Button) this.manageButton$delegate.getValue();
    }

    private final Button getGotItButton() {
        return (Button) this.gotItButton$delegate.getValue();
    }

    public void setLayoutDirection(int i) {
        super.setLayoutDirection(i);
        setDrawableDirection();
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        setLayoutDirection(getResources().getConfiguration().getLayoutDirection());
    }

    private final void setButtonColor() {
        TypedArray obtainStyledAttributes = this.mContext.obtainStyledAttributes(new int[]{17956900});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        getManageButton().setTextColor(this.mContext.getColor(17170472));
        getManageButton().setBackgroundDrawable(new ColorDrawable(color));
        getGotItButton().setBackgroundDrawable(new ColorDrawable(color));
    }

    private final void setDrawableDirection() {
        int i;
        ViewGroup manageView = getManageView();
        if (getResources().getConfiguration().getLayoutDirection() == 1) {
            i = C3353R.C3355drawable.bubble_stack_user_education_bg_rtl;
        } else {
            i = C3353R.C3355drawable.bubble_stack_user_education_bg;
        }
        manageView.setBackgroundResource(i);
    }

    public final void show(BubbleExpandedView bubbleExpandedView2) {
        int i;
        Intrinsics.checkNotNullParameter(bubbleExpandedView2, "expandedView");
        setButtonColor();
        if (getVisibility() != 0) {
            this.bubbleExpandedView = bubbleExpandedView2;
            TaskView taskView = bubbleExpandedView2.getTaskView();
            if (taskView != null) {
                taskView.setObscuredTouchRect(new Rect(this.positioner.getScreenRect()));
            }
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (this.positioner.isLargeScreen() || this.positioner.isLandscape()) {
                i = getContext().getResources().getDimensionPixelSize(C3353R.dimen.bubbles_user_education_width);
            } else {
                i = -1;
            }
            layoutParams.width = i;
            setAlpha(0.0f);
            setVisibility(0);
            bubbleExpandedView2.getManageButtonBoundsOnScreen(this.realManageButtonRect);
            boolean z = true;
            if (this.mContext.getResources().getConfiguration().getLayoutDirection() != 1) {
                z = false;
            }
            if (z) {
                getManageView().setPadding(getManageView().getPaddingLeft(), getManageView().getPaddingTop(), (this.positioner.getScreenRect().right - this.realManageButtonRect.right) - bubbleExpandedView2.getManageButtonMargin(), getManageView().getPaddingBottom());
            } else {
                getManageView().setPadding(this.realManageButtonRect.left - bubbleExpandedView2.getManageButtonMargin(), getManageView().getPaddingTop(), getManageView().getPaddingRight(), getManageView().getPaddingBottom());
            }
            post(new ManageEducationView$$ExternalSyntheticLambda3(this, z, bubbleExpandedView2));
            setShouldShow(false);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: show$lambda-3  reason: not valid java name */
    public static final void m3436show$lambda3(ManageEducationView manageEducationView, boolean z, BubbleExpandedView bubbleExpandedView2) {
        Intrinsics.checkNotNullParameter(manageEducationView, "this$0");
        Intrinsics.checkNotNullParameter(bubbleExpandedView2, "$expandedView");
        manageEducationView.getManageButton().setOnClickListener(new ManageEducationView$$ExternalSyntheticLambda0(manageEducationView, bubbleExpandedView2));
        manageEducationView.getGotItButton().setOnClickListener(new ManageEducationView$$ExternalSyntheticLambda1(manageEducationView));
        manageEducationView.setOnClickListener(new ManageEducationView$$ExternalSyntheticLambda2(manageEducationView));
        Rect rect = new Rect();
        manageEducationView.getManageButton().getDrawingRect(rect);
        manageEducationView.getManageView().offsetDescendantRectToMyCoords(manageEducationView.getManageButton(), rect);
        if (!z || (!manageEducationView.positioner.isLargeScreen() && !manageEducationView.positioner.isLandscape())) {
            manageEducationView.setTranslationX(0.0f);
        } else {
            manageEducationView.setTranslationX((float) (manageEducationView.positioner.getScreenRect().right - manageEducationView.getWidth()));
        }
        manageEducationView.setTranslationY((float) (manageEducationView.realManageButtonRect.top - rect.top));
        manageEducationView.bringToFront();
        manageEducationView.animate().setDuration(manageEducationView.ANIMATE_DURATION).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).alpha(1.0f);
    }

    /* access modifiers changed from: private */
    /* renamed from: show$lambda-3$lambda-0  reason: not valid java name */
    public static final void m3437show$lambda3$lambda0(ManageEducationView manageEducationView, BubbleExpandedView bubbleExpandedView2, View view) {
        Intrinsics.checkNotNullParameter(manageEducationView, "this$0");
        Intrinsics.checkNotNullParameter(bubbleExpandedView2, "$expandedView");
        manageEducationView.hide();
        bubbleExpandedView2.findViewById(C3353R.C3356id.manage_button).performClick();
    }

    /* access modifiers changed from: private */
    /* renamed from: show$lambda-3$lambda-1  reason: not valid java name */
    public static final void m3438show$lambda3$lambda1(ManageEducationView manageEducationView, View view) {
        Intrinsics.checkNotNullParameter(manageEducationView, "this$0");
        manageEducationView.hide();
    }

    /* access modifiers changed from: private */
    /* renamed from: show$lambda-3$lambda-2  reason: not valid java name */
    public static final void m3439show$lambda3$lambda2(ManageEducationView manageEducationView, View view) {
        Intrinsics.checkNotNullParameter(manageEducationView, "this$0");
        manageEducationView.hide();
    }

    public final void hide() {
        TaskView taskView;
        BubbleExpandedView bubbleExpandedView2 = this.bubbleExpandedView;
        if (!(bubbleExpandedView2 == null || (taskView = bubbleExpandedView2.getTaskView()) == null)) {
            taskView.setObscuredTouchRect((Rect) null);
        }
        if (getVisibility() == 0 && !this.isHiding) {
            animate().withStartAction(new ManageEducationView$$ExternalSyntheticLambda4(this)).alpha(0.0f).setDuration(this.ANIMATE_DURATION).withEndAction(new ManageEducationView$$ExternalSyntheticLambda5(this));
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: hide$lambda-4  reason: not valid java name */
    public static final void m3434hide$lambda4(ManageEducationView manageEducationView) {
        Intrinsics.checkNotNullParameter(manageEducationView, "this$0");
        manageEducationView.isHiding = true;
    }

    /* access modifiers changed from: private */
    /* renamed from: hide$lambda-5  reason: not valid java name */
    public static final void m3435hide$lambda5(ManageEducationView manageEducationView) {
        Intrinsics.checkNotNullParameter(manageEducationView, "this$0");
        manageEducationView.isHiding = false;
        manageEducationView.setVisibility(8);
    }

    private final void setShouldShow(boolean z) {
        getContext().getSharedPreferences(getContext().getPackageName(), 0).edit().putBoolean(ManageEducationViewKt.PREF_MANAGED_EDUCATION, !z).apply();
    }
}
