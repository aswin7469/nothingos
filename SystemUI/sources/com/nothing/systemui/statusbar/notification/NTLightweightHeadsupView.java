package com.nothing.systemui.statusbar.notification;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.systemui.C1893R;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0010\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0016\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0016\u0018\u0000 \u00012\u00020\u0001:\u0002\u0001B\u001d\b\u0016\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\b\u0010l\u001a\u00020!H\u0014J\b\u0010m\u001a\u00020!H\u0014J\b\u0010n\u001a\u00020!H\u0004J\b\u0010o\u001a\u00020!H\u0014J\b\u0010p\u001a\u00020!H\u0014J\b\u0010q\u001a\u00020rH\u0014J\b\u0010s\u001a\u00020rH\u0014J\b\u0010t\u001a\u00020rH\u0014J\b\u0010u\u001a\u00020\bH\u0016J\b\u0010v\u001a\u00020rH\u0014J\u0012\u0010w\u001a\u00020r2\b\u0010x\u001a\u0004\u0018\u00010yH\u0014J\u001a\u0010z\u001a\u00020r2\b\u0010{\u001a\u0004\u0018\u00010|2\u0006\u0010}\u001a\u00020!H\u0014J\u001c\u0010~\u001a\u00020r2\u0006\u0010\u001a\u00020\b2\n\u0010\u0001\u001a\u0005\u0018\u00010\u0001H\u0016J\u0012\u0010\u0001\u001a\u00020r2\u0007\u0010\u0001\u001a\u00020\fH\u0004J\u0007\u0010\u0001\u001a\u00020rJ\u0007\u0010\u0001\u001a\u00020rR\u0014\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\u000e\"\u0004\b\u000f\u0010\u0010R\u001a\u0010\u0011\u001a\u00020\fX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u000e\"\u0004\b\u0013\u0010\u0010R\u001a\u0010\u0014\u001a\u00020\u0015X.¢\u0006\u000e\n\u0000\u001a\u0004\b\u0016\u0010\u0017\"\u0004\b\u0018\u0010\u0019R\u001a\u0010\u001a\u001a\u00020\u001bX.¢\u0006\u000e\n\u0000\u001a\u0004\b\u001c\u0010\u001d\"\u0004\b\u001e\u0010\u001fR\u001a\u0010 \u001a\u00020!X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\"\u0010#\"\u0004\b$\u0010%R\u001a\u0010&\u001a\u00020'X.¢\u0006\u000e\n\u0000\u001a\u0004\b(\u0010)\"\u0004\b*\u0010+R\u001a\u0010,\u001a\u00020-X.¢\u0006\u000e\n\u0000\u001a\u0004\b.\u0010/\"\u0004\b0\u00101R\u001a\u00102\u001a\u00020!X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u0010#\"\u0004\b4\u0010%R\u0014\u00105\u001a\u000206X\u0004¢\u0006\b\n\u0000\u001a\u0004\b7\u00108R\u001a\u00109\u001a\u00020:X.¢\u0006\u000e\n\u0000\u001a\u0004\b;\u0010<\"\u0004\b=\u0010>R\u001a\u0010?\u001a\u00020\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b?\u0010\n\"\u0004\b@\u0010AR\u001a\u0010B\u001a\u00020\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bB\u0010\n\"\u0004\bC\u0010AR\u001a\u0010D\u001a\u00020'X.¢\u0006\u000e\n\u0000\u001a\u0004\bE\u0010)\"\u0004\bF\u0010+R\u001a\u0010G\u001a\u00020!X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bH\u0010#\"\u0004\bI\u0010%R\u0014\u0010J\u001a\u00020KX\u0004¢\u0006\b\n\u0000\u001a\u0004\bL\u0010MR\u001a\u0010N\u001a\u00020:X.¢\u0006\u000e\n\u0000\u001a\u0004\bO\u0010<\"\u0004\bP\u0010>R\u001a\u0010Q\u001a\u00020RX.¢\u0006\u000e\n\u0000\u001a\u0004\bS\u0010T\"\u0004\bU\u0010VR\u001a\u0010W\u001a\u00020!X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bX\u0010#\"\u0004\bY\u0010%R\u001a\u0010Z\u001a\u00020[X.¢\u0006\u000e\n\u0000\u001a\u0004\b\\\u0010]\"\u0004\b^\u0010_R\u001a\u0010`\u001a\u00020-X.¢\u0006\u000e\n\u0000\u001a\u0004\ba\u0010/\"\u0004\bb\u00101R\u001a\u0010c\u001a\u00020\u0015X.¢\u0006\u000e\n\u0000\u001a\u0004\bd\u0010\u0017\"\u0004\be\u0010\u0019R\u001a\u0010f\u001a\u00020\u0015X.¢\u0006\u000e\n\u0000\u001a\u0004\bg\u0010\u0017\"\u0004\bh\u0010\u0019R\u001a\u0010i\u001a\u00020\u0015X.¢\u0006\u000e\n\u0000\u001a\u0004\bj\u0010\u0017\"\u0004\bk\u0010\u0019¨\u0006\u0001"}, mo64987d2 = {"Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupView;", "Landroid/widget/FrameLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "DEBUG", "", "getDEBUG", "()Z", "OFFSET_Y", "", "getOFFSET_Y", "()F", "setOFFSET_Y", "(F)V", "OFFSET_Y_OUT_SCREEN", "getOFFSET_Y_OUT_SCREEN", "setOFFSET_Y_OUT_SCREEN", "allOutAnimator", "Landroid/animation/ObjectAnimator;", "getAllOutAnimator", "()Landroid/animation/ObjectAnimator;", "setAllOutAnimator", "(Landroid/animation/ObjectAnimator;)V", "appIconView", "Landroid/widget/ImageView;", "getAppIconView", "()Landroid/widget/ImageView;", "setAppIconView", "(Landroid/widget/ImageView;)V", "centerY", "", "getCenterY", "()I", "setCenterY", "(I)V", "container", "Landroid/widget/LinearLayout;", "getContainer", "()Landroid/widget/LinearLayout;", "setContainer", "(Landroid/widget/LinearLayout;)V", "contentView", "Landroid/widget/TextView;", "getContentView", "()Landroid/widget/TextView;", "setContentView", "(Landroid/widget/TextView;)V", "diameter", "getDiameter", "setDiameter", "displayMetrics", "Landroid/util/DisplayMetrics;", "getDisplayMetrics", "()Landroid/util/DisplayMetrics;", "inAnimatorSet", "Landroid/animation/AnimatorSet;", "getInAnimatorSet", "()Landroid/animation/AnimatorSet;", "setInAnimatorSet", "(Landroid/animation/AnimatorSet;)V", "isPopShowing", "setPopShowing", "(Z)V", "isReleased", "setReleased", "linearLayout", "getLinearLayout", "setLinearLayout", "messagePaddingEnd", "getMessagePaddingEnd", "setMessagePaddingEnd", "myDisplay", "Landroid/view/Display;", "getMyDisplay", "()Landroid/view/Display;", "outAnimatorSet", "getOutAnimatorSet", "setOutAnimatorSet", "popMessageLayout", "Landroid/widget/RelativeLayout;", "getPopMessageLayout", "()Landroid/widget/RelativeLayout;", "setPopMessageLayout", "(Landroid/widget/RelativeLayout;)V", "screenWidth", "getScreenWidth", "setScreenWidth", "textPaint", "Landroid/text/TextPaint;", "getTextPaint", "()Landroid/text/TextPaint;", "setTextPaint", "(Landroid/text/TextPaint;)V", "titleView", "getTitleView", "setTitleView", "translationYInAnimator", "getTranslationYInAnimator", "setTranslationYInAnimator", "translationYOutAnimator", "getTranslationYOutAnimator", "setTranslationYOutAnimator", "zoomInAnimator", "getZoomInAnimator", "setZoomInAnimator", "calculateTextLayoutWidth", "getInflateLayoutId", "getLargerTextViewWidth", "getLayoutHeight", "getLayoutWidth", "init", "", "initAnimator", "initView", "isShowing", "onAttachedToWindow", "onConfigurationChanged", "newConfig", "Landroid/content/res/Configuration;", "onVisibilityChanged", "changedView", "Landroid/view/View;", "visibility", "releasePopWithAnimation", "show", "runnable", "Ljava/lang/Runnable;", "setScale", "scale", "updateLayoutOffset", "updateQuickOpenVisibility", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NTLightweightHeadsupView.kt */
public class NTLightweightHeadsupView extends FrameLayout {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final String TAG = "NTLightweightHeadsupView";
    private final boolean DEBUG;
    private float OFFSET_Y;
    private float OFFSET_Y_OUT_SCREEN;
    public Map<Integer, View> _$_findViewCache;
    protected ObjectAnimator allOutAnimator;
    protected ImageView appIconView;
    private int centerY;
    protected LinearLayout container;
    protected TextView contentView;
    private int diameter;
    private final DisplayMetrics displayMetrics;
    protected AnimatorSet inAnimatorSet;
    private boolean isPopShowing;
    private boolean isReleased;
    protected LinearLayout linearLayout;
    private int messagePaddingEnd;
    private final Display myDisplay;
    protected AnimatorSet outAnimatorSet;
    protected RelativeLayout popMessageLayout;
    private int screenWidth;
    protected TextPaint textPaint;
    protected TextView titleView;
    protected ObjectAnimator translationYInAnimator;
    protected ObjectAnimator translationYOutAnimator;
    protected ObjectAnimator zoomInAnimator;

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

    /* access modifiers changed from: protected */
    public int getInflateLayoutId() {
        return C1893R.layout.game_mode_toast;
    }

    /* access modifiers changed from: protected */
    public void initAnimator() {
    }

    /* access modifiers changed from: protected */
    public final boolean getDEBUG() {
        return this.DEBUG;
    }

    /* access modifiers changed from: protected */
    public final boolean isPopShowing() {
        return this.isPopShowing;
    }

    /* access modifiers changed from: protected */
    public final void setPopShowing(boolean z) {
        this.isPopShowing = z;
    }

    /* access modifiers changed from: protected */
    public final ImageView getAppIconView() {
        ImageView imageView = this.appIconView;
        if (imageView != null) {
            return imageView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("appIconView");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setAppIconView(ImageView imageView) {
        Intrinsics.checkNotNullParameter(imageView, "<set-?>");
        this.appIconView = imageView;
    }

    /* access modifiers changed from: protected */
    public final TextView getTitleView() {
        TextView textView = this.titleView;
        if (textView != null) {
            return textView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("titleView");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setTitleView(TextView textView) {
        Intrinsics.checkNotNullParameter(textView, "<set-?>");
        this.titleView = textView;
    }

    /* access modifiers changed from: protected */
    public final TextView getContentView() {
        TextView textView = this.contentView;
        if (textView != null) {
            return textView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("contentView");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setContentView(TextView textView) {
        Intrinsics.checkNotNullParameter(textView, "<set-?>");
        this.contentView = textView;
    }

    /* access modifiers changed from: protected */
    public final LinearLayout getLinearLayout() {
        LinearLayout linearLayout2 = this.linearLayout;
        if (linearLayout2 != null) {
            return linearLayout2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("linearLayout");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setLinearLayout(LinearLayout linearLayout2) {
        Intrinsics.checkNotNullParameter(linearLayout2, "<set-?>");
        this.linearLayout = linearLayout2;
    }

    /* access modifiers changed from: protected */
    public final RelativeLayout getPopMessageLayout() {
        RelativeLayout relativeLayout = this.popMessageLayout;
        if (relativeLayout != null) {
            return relativeLayout;
        }
        Intrinsics.throwUninitializedPropertyAccessException("popMessageLayout");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setPopMessageLayout(RelativeLayout relativeLayout) {
        Intrinsics.checkNotNullParameter(relativeLayout, "<set-?>");
        this.popMessageLayout = relativeLayout;
    }

    /* access modifiers changed from: protected */
    public final LinearLayout getContainer() {
        LinearLayout linearLayout2 = this.container;
        if (linearLayout2 != null) {
            return linearLayout2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("container");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setContainer(LinearLayout linearLayout2) {
        Intrinsics.checkNotNullParameter(linearLayout2, "<set-?>");
        this.container = linearLayout2;
    }

    /* access modifiers changed from: protected */
    public final DisplayMetrics getDisplayMetrics() {
        return this.displayMetrics;
    }

    /* access modifiers changed from: protected */
    public final Display getMyDisplay() {
        return this.myDisplay;
    }

    /* access modifiers changed from: protected */
    public final AnimatorSet getInAnimatorSet() {
        AnimatorSet animatorSet = this.inAnimatorSet;
        if (animatorSet != null) {
            return animatorSet;
        }
        Intrinsics.throwUninitializedPropertyAccessException("inAnimatorSet");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setInAnimatorSet(AnimatorSet animatorSet) {
        Intrinsics.checkNotNullParameter(animatorSet, "<set-?>");
        this.inAnimatorSet = animatorSet;
    }

    /* access modifiers changed from: protected */
    public final AnimatorSet getOutAnimatorSet() {
        AnimatorSet animatorSet = this.outAnimatorSet;
        if (animatorSet != null) {
            return animatorSet;
        }
        Intrinsics.throwUninitializedPropertyAccessException("outAnimatorSet");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setOutAnimatorSet(AnimatorSet animatorSet) {
        Intrinsics.checkNotNullParameter(animatorSet, "<set-?>");
        this.outAnimatorSet = animatorSet;
    }

    /* access modifiers changed from: protected */
    public final ObjectAnimator getZoomInAnimator() {
        ObjectAnimator objectAnimator = this.zoomInAnimator;
        if (objectAnimator != null) {
            return objectAnimator;
        }
        Intrinsics.throwUninitializedPropertyAccessException("zoomInAnimator");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setZoomInAnimator(ObjectAnimator objectAnimator) {
        Intrinsics.checkNotNullParameter(objectAnimator, "<set-?>");
        this.zoomInAnimator = objectAnimator;
    }

    /* access modifiers changed from: protected */
    public final ObjectAnimator getTranslationYInAnimator() {
        ObjectAnimator objectAnimator = this.translationYInAnimator;
        if (objectAnimator != null) {
            return objectAnimator;
        }
        Intrinsics.throwUninitializedPropertyAccessException("translationYInAnimator");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setTranslationYInAnimator(ObjectAnimator objectAnimator) {
        Intrinsics.checkNotNullParameter(objectAnimator, "<set-?>");
        this.translationYInAnimator = objectAnimator;
    }

    /* access modifiers changed from: protected */
    public final ObjectAnimator getAllOutAnimator() {
        ObjectAnimator objectAnimator = this.allOutAnimator;
        if (objectAnimator != null) {
            return objectAnimator;
        }
        Intrinsics.throwUninitializedPropertyAccessException("allOutAnimator");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setAllOutAnimator(ObjectAnimator objectAnimator) {
        Intrinsics.checkNotNullParameter(objectAnimator, "<set-?>");
        this.allOutAnimator = objectAnimator;
    }

    /* access modifiers changed from: protected */
    public final ObjectAnimator getTranslationYOutAnimator() {
        ObjectAnimator objectAnimator = this.translationYOutAnimator;
        if (objectAnimator != null) {
            return objectAnimator;
        }
        Intrinsics.throwUninitializedPropertyAccessException("translationYOutAnimator");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setTranslationYOutAnimator(ObjectAnimator objectAnimator) {
        Intrinsics.checkNotNullParameter(objectAnimator, "<set-?>");
        this.translationYOutAnimator = objectAnimator;
    }

    /* access modifiers changed from: protected */
    public final int getCenterY() {
        return this.centerY;
    }

    /* access modifiers changed from: protected */
    public final void setCenterY(int i) {
        this.centerY = i;
    }

    /* access modifiers changed from: protected */
    public final int getDiameter() {
        return this.diameter;
    }

    /* access modifiers changed from: protected */
    public final void setDiameter(int i) {
        this.diameter = i;
    }

    /* access modifiers changed from: protected */
    public final int getMessagePaddingEnd() {
        return this.messagePaddingEnd;
    }

    /* access modifiers changed from: protected */
    public final void setMessagePaddingEnd(int i) {
        this.messagePaddingEnd = i;
    }

    /* access modifiers changed from: protected */
    public final TextPaint getTextPaint() {
        TextPaint textPaint2 = this.textPaint;
        if (textPaint2 != null) {
            return textPaint2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("textPaint");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setTextPaint(TextPaint textPaint2) {
        Intrinsics.checkNotNullParameter(textPaint2, "<set-?>");
        this.textPaint = textPaint2;
    }

    /* access modifiers changed from: protected */
    public final boolean isReleased() {
        return this.isReleased;
    }

    /* access modifiers changed from: protected */
    public final void setReleased(boolean z) {
        this.isReleased = z;
    }

    /* access modifiers changed from: protected */
    public final float getOFFSET_Y() {
        return this.OFFSET_Y;
    }

    /* access modifiers changed from: protected */
    public final void setOFFSET_Y(float f) {
        this.OFFSET_Y = f;
    }

    /* access modifiers changed from: protected */
    public final float getOFFSET_Y_OUT_SCREEN() {
        return this.OFFSET_Y_OUT_SCREEN;
    }

    /* access modifiers changed from: protected */
    public final void setOFFSET_Y_OUT_SCREEN(float f) {
        this.OFFSET_Y_OUT_SCREEN = f;
    }

    /* access modifiers changed from: protected */
    public final int getScreenWidth() {
        return this.screenWidth;
    }

    /* access modifiers changed from: protected */
    public final void setScreenWidth(int i) {
        this.screenWidth = i;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0076, code lost:
        r2 = r2.getResources();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public NTLightweightHeadsupView(android.content.Context r2, android.util.AttributeSet r3) {
        /*
            r1 = this;
            java.util.LinkedHashMap r0 = new java.util.LinkedHashMap
            r0.<init>()
            java.util.Map r0 = (java.util.Map) r0
            r1._$_findViewCache = r0
            r1.<init>(r2, r3)
            java.lang.String r2 = TAG
            r3 = 3
            boolean r3 = android.util.Log.isLoggable(r2, r3)
            r1.DEBUG = r3
            r3 = 1
            r1.isReleased = r3
            r3 = -1007026176(0xffffffffc3fa0000, float:-500.0)
            r1.OFFSET_Y_OUT_SCREEN = r3
            android.util.DisplayMetrics r3 = new android.util.DisplayMetrics
            r3.<init>()
            r1.displayMetrics = r3
            android.content.Context r3 = r1.getContext()
            java.lang.String r0 = "window"
            java.lang.Object r3 = r3.getSystemService(r0)
            if (r3 == 0) goto L_0x00b0
            android.view.WindowManager r3 = (android.view.WindowManager) r3
            android.view.Display r3 = r3.getDefaultDisplay()
            java.lang.String r0 = "windowManager.getDefaultDisplay()"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r3, r0)
            r1.myDisplay = r3
            r3 = 0
            r1.setWillNotDraw(r3)
            int r3 = r1.getLayoutHeight()
            r1.diameter = r3
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r0 = "[foree] setUpView: diameter = "
            r3.<init>((java.lang.String) r0)
            int r0 = r1.diameter
            java.lang.StringBuilder r3 = r3.append((int) r0)
            java.lang.String r3 = r3.toString()
            android.util.Log.d(r2, r3)
            android.content.res.Resources r2 = r1.getResources()
            r3 = 2131166686(0x7f0705de, float:1.7947624E38)
            int r2 = r2.getDimensionPixelSize(r3)
            r1.messagePaddingEnd = r2
            int r2 = r1.diameter
            int r2 = r2 / 2
            r1.centerY = r2
            android.content.Context r2 = r1.getContext()
            r3 = 0
            if (r2 == 0) goto L_0x0088
            android.content.res.Resources r2 = r2.getResources()
            if (r2 == 0) goto L_0x0088
            r0 = 2131166677(0x7f0705d5, float:1.7947606E38)
            float r2 = r2.getDimension(r0)
            java.lang.Float r2 = java.lang.Float.valueOf((float) r2)
            goto L_0x0089
        L_0x0088:
            r2 = r3
        L_0x0089:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r2)
            float r2 = r2.floatValue()
            r1.OFFSET_Y = r2
            android.content.Context r2 = r1.getContext()
            if (r2 == 0) goto L_0x009c
            android.content.res.Resources r3 = r2.getResources()
        L_0x009c:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r3)
            r2 = 2131166646(0x7f0705b6, float:1.7947543E38)
            float r2 = r3.getDimension(r2)
            float r2 = -r2
            float r3 = r1.OFFSET_Y
            float r2 = r2 - r3
            r1.OFFSET_Y_OUT_SCREEN = r2
            r1.init()
            return
        L_0x00b0:
            java.lang.NullPointerException r1 = new java.lang.NullPointerException
            java.lang.String r2 = "null cannot be cast to non-null type android.view.WindowManager"
            r1.<init>(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.statusbar.notification.NTLightweightHeadsupView.<init>(android.content.Context, android.util.AttributeSet):void");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ NTLightweightHeadsupView(Context context, AttributeSet attributeSet, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : attributeSet);
    }

    /* access modifiers changed from: protected */
    public void init() {
        initView();
        initAnimator();
        setPivotY((float) this.centerY);
        setVisibility(8);
        setAlpha(0.0f);
    }

    /* access modifiers changed from: protected */
    public void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(getInflateLayoutId(), this, false);
        if (inflate != null) {
            setLinearLayout((LinearLayout) inflate);
            View findViewById = getLinearLayout().findViewById(C1893R.C1897id.icon);
            if (findViewById != null) {
                setAppIconView((ImageView) findViewById);
                View findViewById2 = getLinearLayout().findViewById(C1893R.C1897id.pop_message);
                if (findViewById2 != null) {
                    setPopMessageLayout((RelativeLayout) findViewById2);
                    View findViewById3 = getPopMessageLayout().findViewById(C1893R.C1897id.title);
                    if (findViewById3 != null) {
                        setTitleView((TextView) findViewById3);
                        View findViewById4 = getPopMessageLayout().findViewById(C1893R.C1897id.content);
                        if (findViewById4 != null) {
                            setContentView((TextView) findViewById4);
                            View findViewById5 = getLinearLayout().findViewById(C1893R.C1897id.container);
                            Intrinsics.checkNotNullExpressionValue(findViewById5, "linearLayout.findViewByI…arLayout>(R.id.container)");
                            setContainer((LinearLayout) findViewById5);
                            TextPaint paint = getTitleView().getPaint();
                            Intrinsics.checkNotNullExpressionValue(paint, "titleView.getPaint()");
                            setTextPaint(paint);
                            getPopMessageLayout().setAlpha(0.0f);
                            setLayoutParams(new FrameLayout.LayoutParams(getLayoutWidth(), getLayoutHeight()));
                            addView(getLinearLayout(), getLayoutParams());
                            return;
                        }
                        throw new NullPointerException("null cannot be cast to non-null type android.widget.TextView");
                    }
                    throw new NullPointerException("null cannot be cast to non-null type android.widget.TextView");
                }
                throw new NullPointerException("null cannot be cast to non-null type android.widget.RelativeLayout");
            }
            throw new NullPointerException("null cannot be cast to non-null type android.widget.ImageView");
        }
        throw new NullPointerException("null cannot be cast to non-null type android.widget.LinearLayout");
    }

    /* access modifiers changed from: protected */
    public int getLayoutWidth() {
        return getResources().getDimensionPixelSize(C1893R.dimen.nt_game_mode_toast_max_width);
    }

    /* access modifiers changed from: protected */
    public int calculateTextLayoutWidth() {
        return getResources().getDimensionPixelSize(C1893R.dimen.nt_game_mode_toast_max_width);
    }

    /* access modifiers changed from: protected */
    public final int getLargerTextViewWidth() {
        getTitleView().measure(0, 0);
        getContentView().measure(0, 0);
        int measuredWidth = getTitleView().getMeasuredWidth();
        return measuredWidth < getContentView().getMeasuredWidth() ? getContentView().getMeasuredWidth() : measuredWidth;
    }

    /* access modifiers changed from: protected */
    public int getLayoutHeight() {
        return getResources().getDimensionPixelSize(C1893R.dimen.nt_pop_view_height);
    }

    /* access modifiers changed from: protected */
    public final void setScale(float f) {
        if (this.DEBUG) {
            Log.d(TAG, "setScale() called with: scale = [" + f + ']');
        }
        setScaleX(f);
        setScaleY(f);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateLayoutOffset();
    }

    public void releasePopWithAnimation(boolean z, Runnable runnable) {
        Log.d(TAG, "releaseToastWithAnimation() called with: show = " + z + ", runnable = " + runnable + ", isReleased = " + this.isReleased + " , in running = " + getInAnimatorSet().isRunning() + ", outRunning = " + getAllOutAnimator().isRunning() + ", getAlpha = " + getAlpha());
        if (z) {
            if (this.isReleased && !getInAnimatorSet().isRunning()) {
                View.TRANSLATION_X.set(this, Float.valueOf(0.0f));
                View.TRANSLATION_Y.set(this, Float.valueOf(this.OFFSET_Y_OUT_SCREEN));
                setAlpha(0.0f);
                getInAnimatorSet().start();
                this.isReleased = false;
            }
        } else if (getOutAnimatorSet().isRunning()) {
            getOutAnimatorSet().end();
            this.isReleased = true;
        } else if (runnable != null) {
            getOutAnimatorSet().removeAllListeners();
            getOutAnimatorSet().addListener(new NTLightweightHeadsupView$releasePopWithAnimation$1(this, runnable));
            getOutAnimatorSet().start();
        }
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        this.isPopShowing = i == 0;
    }

    public boolean isShowing() {
        return this.isPopShowing && !this.isReleased;
    }

    public final void updateLayoutOffset() {
        this.myDisplay.getRealMetrics(this.displayMetrics);
        this.screenWidth = this.displayMetrics.widthPixels;
        getPopMessageLayout().getLayoutParams().width = calculateTextLayoutWidth();
        getLayoutParams().width = getLayoutWidth();
        getLinearLayout().getLayoutParams().width = getLayoutParams().width;
        setPivotX((float) (this.screenWidth / 2));
        updateQuickOpenVisibility();
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateLayoutOffset();
    }

    public final void updateQuickOpenVisibility() {
        this.messagePaddingEnd = getResources().getDimensionPixelSize(C1893R.dimen.nt_pop_view_message_margin_end);
        getPopMessageLayout().post(new NTLightweightHeadsupView$updateQuickOpenVisibility$1(this));
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupView$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: NTLightweightHeadsupView.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    static {
        Intrinsics.checkNotNullExpressionValue("NTLightweightHeadsupView", "NTLightweightHeadsupView…lass.java.getSimpleName()");
    }
}
