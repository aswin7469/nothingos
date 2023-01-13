package com.android.systemui.p012qs;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.UserManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.settingslib.Utils;
import com.android.settingslib.drawable.UserIconDrawable;
import com.android.systemui.C1894R;
import com.android.systemui.statusbar.phone.MultiUserSwitch;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\u0002\u0010\u0006J\u0016\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u000eJ\b\u0010\u0016\u001a\u00020\u0012H\u0014J\u0012\u0010\u0017\u001a\u00020\u000e2\b\u0010\u0018\u001a\u0004\u0018\u00010\u0019H\u0016J\u0012\u0010\u001a\u001a\u00020\u000e2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0019H\u0016J\u0018\u0010\u001c\u001a\u00020\u00122\b\u0010\u001d\u001a\u0004\u0018\u00010\u001e2\u0006\u0010\u001f\u001a\u00020\u000eJ\b\u0010 \u001a\u00020\u0012H\u0002J\u000e\u0010!\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u000eJ\u0010\u0010\"\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u000eH\u0002R\u000e\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX.¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX.¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X.¢\u0006\u0002\n\u0000¨\u0006#"}, mo65043d2 = {"Lcom/android/systemui/qs/FooterActionsView;", "Landroid/widget/LinearLayout;", "context", "Landroid/content/Context;", "attrs", "Landroid/util/AttributeSet;", "(Landroid/content/Context;Landroid/util/AttributeSet;)V", "expansionAmount", "", "multiUserAvatar", "Landroid/widget/ImageView;", "multiUserSwitch", "Lcom/android/systemui/statusbar/phone/MultiUserSwitch;", "qsDisabled", "", "settingsContainer", "Landroid/view/View;", "disable", "", "state2", "", "multiUserEnabled", "onFinishInflate", "onInterceptTouchEvent", "ev", "Landroid/view/MotionEvent;", "onTouchEvent", "event", "onUserInfoChanged", "picture", "Landroid/graphics/drawable/Drawable;", "isGuestUser", "updateClickabilities", "updateEverything", "updateVisibilities", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.FooterActionsView */
/* compiled from: FooterActionsView.kt */
public final class FooterActionsView extends LinearLayout {
    public Map<Integer, View> _$_findViewCache = new LinkedHashMap();
    private float expansionAmount;
    private ImageView multiUserAvatar;
    private MultiUserSwitch multiUserSwitch;
    private boolean qsDisabled;
    private View settingsContainer;

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

    public FooterActionsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        View findViewById = findViewById(C1894R.C1898id.settings_button_container);
        Intrinsics.checkNotNullExpressionValue(findViewById, "findViewById(R.id.settings_button_container)");
        this.settingsContainer = findViewById;
        View findViewById2 = findViewById(C1894R.C1898id.multi_user_switch);
        Intrinsics.checkNotNullExpressionValue(findViewById2, "findViewById(R.id.multi_user_switch)");
        MultiUserSwitch multiUserSwitch2 = (MultiUserSwitch) findViewById2;
        this.multiUserSwitch = multiUserSwitch2;
        View view = null;
        if (multiUserSwitch2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("multiUserSwitch");
            multiUserSwitch2 = null;
        }
        View findViewById3 = multiUserSwitch2.findViewById(C1894R.C1898id.multi_user_avatar);
        Intrinsics.checkNotNullExpressionValue(findViewById3, "multiUserSwitch.findView…d(R.id.multi_user_avatar)");
        this.multiUserAvatar = (ImageView) findViewById3;
        View view2 = this.settingsContainer;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settingsContainer");
            view2 = null;
        }
        if (view2.getBackground() instanceof RippleDrawable) {
            View view3 = this.settingsContainer;
            if (view3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("settingsContainer");
            } else {
                view = view3;
            }
            Drawable background = view.getBackground();
            if (background != null) {
                ((RippleDrawable) background).setForceSoftware(true);
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.RippleDrawable");
            }
        }
        setImportantForAccessibility(1);
    }

    public final void disable(int i, boolean z) {
        boolean z2 = true;
        if ((i & 1) == 0) {
            z2 = false;
        }
        if (z2 != this.qsDisabled) {
            this.qsDisabled = z2;
            updateEverything(z);
        }
    }

    public final void updateEverything(boolean z) {
        post(new FooterActionsView$$ExternalSyntheticLambda0(this, z));
    }

    /* access modifiers changed from: private */
    /* renamed from: updateEverything$lambda-0  reason: not valid java name */
    public static final void m2905updateEverything$lambda0(FooterActionsView footerActionsView, boolean z) {
        Intrinsics.checkNotNullParameter(footerActionsView, "this$0");
        footerActionsView.updateVisibilities(z);
        footerActionsView.updateClickabilities();
        footerActionsView.setClickable(false);
    }

    private final void updateClickabilities() {
        MultiUserSwitch multiUserSwitch2 = this.multiUserSwitch;
        View view = null;
        if (multiUserSwitch2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("multiUserSwitch");
            multiUserSwitch2 = null;
        }
        MultiUserSwitch multiUserSwitch3 = this.multiUserSwitch;
        if (multiUserSwitch3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("multiUserSwitch");
            multiUserSwitch3 = null;
        }
        int visibility = multiUserSwitch3.getVisibility();
        boolean z = true;
        multiUserSwitch2.setClickable(visibility == 0);
        View view2 = this.settingsContainer;
        if (view2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settingsContainer");
            view2 = null;
        }
        View view3 = this.settingsContainer;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settingsContainer");
        } else {
            view = view3;
        }
        if (view.getVisibility() != 0) {
            z = false;
        }
        view2.setClickable(z);
    }

    private final void updateVisibilities(boolean z) {
        View view = this.settingsContainer;
        View view2 = null;
        if (view == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settingsContainer");
            view = null;
        }
        int i = 8;
        int i2 = 0;
        view.setVisibility(this.qsDisabled ? 8 : 0);
        MultiUserSwitch multiUserSwitch2 = this.multiUserSwitch;
        if (multiUserSwitch2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("multiUserSwitch");
            multiUserSwitch2 = null;
        }
        if (z) {
            i = 0;
        }
        multiUserSwitch2.setVisibility(i);
        boolean isDeviceInDemoMode = UserManager.isDeviceInDemoMode(getContext());
        View view3 = this.settingsContainer;
        if (view3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("settingsContainer");
        } else {
            view2 = view3;
        }
        if (isDeviceInDemoMode) {
            i2 = 4;
        }
        view2.setVisibility(i2);
    }

    public final void onUserInfoChanged(Drawable drawable, boolean z) {
        if (drawable != null && z && !(drawable instanceof UserIconDrawable)) {
            drawable = drawable.getConstantState().newDrawable(getResources()).mutate();
            drawable.setColorFilter(Utils.getColorAttrDefaultColor(this.mContext, 16842800), PorterDuff.Mode.SRC_IN);
        }
        ImageView imageView = this.multiUserAvatar;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("multiUserAvatar");
            imageView = null;
        }
        imageView.setImageDrawable(drawable);
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (FooterActionsViewKt.VERBOSE) {
            Log.d("FooterActionsView", "FooterActionsView onInterceptTouchEvent " + (motionEvent != null ? FooterActionsViewKt.getString(motionEvent) : null));
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (FooterActionsViewKt.VERBOSE) {
            Log.d("FooterActionsView", "FooterActionsView onTouchEvent " + (motionEvent != null ? FooterActionsViewKt.getString(motionEvent) : null));
        }
        return super.onTouchEvent(motionEvent);
    }
}
