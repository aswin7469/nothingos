package com.nothing.systemui.statusbar.notification;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.PathInterpolator;
import androidx.constraintlayout.motion.widget.Key;
import com.android.systemui.C1893R;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0005\b\u0016\u0018\u0000 \u00102\u00020\u0001:\u0001\u0010B\u001d\b\u0016\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0014J\b\u0010\t\u001a\u00020\bH\u0014J\b\u0010\n\u001a\u00020\bH\u0014J\b\u0010\u000b\u001a\u00020\fH\u0014J\b\u0010\r\u001a\u00020\fH\u0014J\b\u0010\u000e\u001a\u00020\fH\u0002J\b\u0010\u000f\u001a\u00020\fH\u0014¨\u0006\u0011"}, mo64987d2 = {"Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupNotificationToast;", "Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupView;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "calculateTextLayoutWidth", "", "getLayoutHeight", "getLayoutWidth", "init", "", "initAnimator", "initLayoutText", "initView", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NTLightweightHeadsupNotificationToast.kt */
public class NTLightweightHeadsupNotificationToast extends NTLightweightHeadsupView {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final String TAG = "NTLightweightHeadsupNotificationToast";
    public Map<Integer, View> _$_findViewCache;

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
    public NTLightweightHeadsupNotificationToast(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this._$_findViewCache = new LinkedHashMap();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ NTLightweightHeadsupNotificationToast(Context context, AttributeSet attributeSet, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : attributeSet);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:2:0x0047, code lost:
        r0 = r0.getResources();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void init() {
        /*
            r3 = this;
            super.init()
            android.content.res.Resources r0 = r3.getResources()
            r1 = 2131166647(0x7f0705b7, float:1.7947545E38)
            int r0 = r0.getDimensionPixelSize(r1)
            r3.setDiameter(r0)
            java.lang.String r0 = TAG
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "[foree] setUpView: diameter = "
            r1.<init>((java.lang.String) r2)
            int r2 = r3.getDiameter()
            java.lang.StringBuilder r1 = r1.append((int) r2)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r0, r1)
            android.content.res.Resources r0 = r3.getResources()
            r1 = 2131166686(0x7f0705de, float:1.7947624E38)
            int r0 = r0.getDimensionPixelSize(r1)
            r3.setMessagePaddingEnd(r0)
            int r0 = r3.getDiameter()
            int r0 = r0 / 2
            r3.setCenterY(r0)
            android.content.Context r0 = r3.getContext()
            r1 = 0
            if (r0 == 0) goto L_0x0059
            android.content.res.Resources r0 = r0.getResources()
            if (r0 == 0) goto L_0x0059
            r2 = 2131166677(0x7f0705d5, float:1.7947606E38)
            float r0 = r0.getDimension(r2)
            java.lang.Float r0 = java.lang.Float.valueOf((float) r0)
            goto L_0x005a
        L_0x0059:
            r0 = r1
        L_0x005a:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            float r0 = r0.floatValue()
            r3.setOFFSET_Y(r0)
            android.content.Context r0 = r3.getContext()
            if (r0 == 0) goto L_0x006e
            android.content.res.Resources r1 = r0.getResources()
        L_0x006e:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            r0 = 2131166646(0x7f0705b6, float:1.7947543E38)
            float r0 = r1.getDimension(r0)
            float r0 = -r0
            float r1 = r3.getOFFSET_Y()
            float r0 = r0 - r1
            r3.setOFFSET_Y_OUT_SCREEN(r0)
            r3.initLayoutText()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nothing.systemui.statusbar.notification.NTLightweightHeadsupNotificationToast.init():void");
    }

    /* access modifiers changed from: protected */
    public void initView() {
        super.initView();
        getAppIconView().setImageDrawable(this.mContext.getDrawable(C1893R.C1895drawable.ic_game_mode));
        getContentView().setAlpha(0.6f);
    }

    /* access modifiers changed from: protected */
    public int getLayoutWidth() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1893R.dimen.nt_game_mode_toast_margin_start) + getResources().getDimensionPixelSize(C1893R.dimen.nt_game_mode_toast_icon_diameter) + getResources().getDimensionPixelSize(C1893R.dimen.nt_game_mode_toast_margin_mid) + getResources().getDimensionPixelSize(C1893R.dimen.nt_game_mode_toast_margin_end) + getLargerTextViewWidth();
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(C1893R.dimen.nt_game_mode_toast_min_width);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(C1893R.dimen.nt_game_mode_toast_max_width);
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
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1893R.dimen.nt_game_mode_toast_margin_start) + getResources().getDimensionPixelSize(C1893R.dimen.nt_game_mode_toast_icon_diameter) + getResources().getDimensionPixelSize(C1893R.dimen.nt_game_mode_toast_margin_mid) + getResources().getDimensionPixelSize(C1893R.dimen.nt_game_mode_toast_margin_end);
        int largerTextViewWidth = getLargerTextViewWidth();
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(C1893R.dimen.nt_game_mode_toast_max_width);
        if (dimensionPixelSize2 > getScreenWidth()) {
            dimensionPixelSize2 = getScreenWidth();
        }
        return Math.min(dimensionPixelSize2 - dimensionPixelSize, largerTextViewWidth);
    }

    /* access modifiers changed from: protected */
    public int getLayoutHeight() {
        return getResources().getDimensionPixelSize(C1893R.dimen.nt_pop_view_height);
    }

    private final void initLayoutText() {
        getTitleView().setText(C1893R.string.game_mode);
        getContentView().setText(C1893R.string.f263on);
    }

    /* access modifiers changed from: protected */
    public void initAnimator() {
        super.initAnimator();
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "scale", new float[]{0.0f, 1.0f});
        Intrinsics.checkNotNullExpressionValue(ofFloat, "ofFloat(this, \"scale\", 0f, 1.0f)");
        setZoomInAnimator(ofFloat);
        getZoomInAnimator().setInterpolator(new PathInterpolator(0.33f, 0.0f, 0.67f, 1.0f));
        getZoomInAnimator().setDuration(150);
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, Key.TRANSLATION_Y, new float[]{getOFFSET_Y_OUT_SCREEN(), getOFFSET_Y()});
        Intrinsics.checkNotNullExpressionValue(ofFloat2, "ofFloat(this, \"translati…T_Y_OUT_SCREEN, OFFSET_Y)");
        setTranslationYInAnimator(ofFloat2);
        getTranslationYInAnimator().setDuration(400);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(getPopMessageLayout(), Key.ALPHA, new float[]{0.0f, 1.0f});
        Intrinsics.checkNotNullExpressionValue(ofFloat3, "ofFloat(popMessageLayout, \"alpha\", 0f, 1f)");
        ofFloat3.setInterpolator(new PathInterpolator(0.3f, 0.0f, 0.67f, 1.0f));
        ofFloat3.setDuration(200);
        ofFloat3.setStartDelay(280);
        setInAnimatorSet(new AnimatorSet());
        getInAnimatorSet().play(getZoomInAnimator()).with(ofFloat3).with(getTranslationYInAnimator());
        getInAnimatorSet().addListener(new NTLightweightHeadsupNotificationToast$initAnimator$1(this));
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(getPopMessageLayout(), Key.ALPHA, new float[]{1.0f, 0.0f});
        Intrinsics.checkNotNullExpressionValue(ofFloat4, "ofFloat(popMessageLayout, \"alpha\", 1f, 0f)");
        ofFloat4.setInterpolator(new PathInterpolator(0.3f, 0.16f, 0.2f, 1.0f));
        ofFloat4.setDuration(70);
        ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(this, "scale", new float[]{1.0f, 0.0f});
        Intrinsics.checkNotNullExpressionValue(ofFloat5, "ofFloat(this, \"scale\", 1f, 0f)");
        ofFloat5.setInterpolator(new PathInterpolator(0.3f, 0.0f, 0.67f, 1.0f));
        ofFloat5.setDuration(130);
        ofFloat5.setStartDelay(183);
        ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(this, Key.ALPHA, new float[]{1.0f, 0.0f});
        Intrinsics.checkNotNullExpressionValue(ofFloat6, "ofFloat(this, \"alpha\", 1f, 0f)");
        setAllOutAnimator(ofFloat6);
        getAllOutAnimator().setInterpolator(new PathInterpolator(0.3f, 0.0f, 0.67f, 1.0f));
        getAllOutAnimator().setDuration(200);
        ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat(this, Key.ALPHA, new float[]{1.0f, 0.0f});
        Intrinsics.checkNotNullExpressionValue(ofFloat7, "ofFloat(this, \"alpha\", 1f, 0f)");
        ofFloat7.setInterpolator(new PathInterpolator(0.3f, 0.0f, 0.67f, 1.0f));
        ofFloat7.setDuration(200);
        ofFloat7.setStartDelay(117);
        ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat(this, Key.TRANSLATION_Y, new float[]{getOFFSET_Y(), getOFFSET_Y_OUT_SCREEN()});
        Intrinsics.checkNotNullExpressionValue(ofFloat8, "ofFloat(this, \"translati…T_Y, OFFSET_Y_OUT_SCREEN)");
        setTranslationYOutAnimator(ofFloat8);
        getTranslationYOutAnimator().setDuration(400);
        setOutAnimatorSet(new AnimatorSet());
        getOutAnimatorSet().play(ofFloat7).with(getTranslationYOutAnimator());
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/nothing/systemui/statusbar/notification/NTLightweightHeadsupNotificationToast$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: NTLightweightHeadsupNotificationToast.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    static {
        Intrinsics.checkNotNullExpressionValue("NTLightweightHeadsupNotificationToast", "NTLightweightHeadsupNoti…lass.java.getSimpleName()");
    }
}
