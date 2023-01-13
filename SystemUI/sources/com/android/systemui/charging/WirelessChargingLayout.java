package com.android.systemui.charging;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.motion.widget.Key;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.charging.ChargingRippleView;
import com.android.systemui.statusbar.phone.NotificationTapHelper;
import java.text.NumberFormat;

public class WirelessChargingLayout extends FrameLayout {
    private static final long RIPPLE_ANIMATION_DURATION = 1500;
    private static final int SCRIM_COLOR = 1275068416;
    private static final int SCRIM_FADE_DURATION = 300;
    public static final int UNKNOWN_BATTERY_LEVEL = -1;
    /* access modifiers changed from: private */
    public ChargingRippleView mRippleView;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public WirelessChargingLayout(Context context) {
        super(context);
        init(context, (AttributeSet) null, false);
    }

    public WirelessChargingLayout(Context context, int i, int i2, boolean z) {
        super(context);
        init(context, (AttributeSet) null, i, i2, z);
    }

    public WirelessChargingLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, false);
    }

    private void init(Context context, AttributeSet attributeSet, boolean z) {
        init(context, attributeSet, -1, -1, false);
    }

    private void init(Context context, AttributeSet attributeSet, int i, int i2, boolean z) {
        int i3 = i;
        int i4 = i2;
        boolean z2 = i3 != -1;
        inflate(new ContextThemeWrapper(context, z ? C1894R.style.ChargingAnim_DarkBackground : C1894R.style.ChargingAnim_WallpaperBackground), C1894R.layout.wireless_charging_layout, this);
        TextView textView = (TextView) findViewById(C1894R.C1898id.wireless_charging_percentage);
        if (i4 != -1) {
            textView.setText(NumberFormat.getPercentInstance().format((double) (((float) i4) / 100.0f)));
            textView.setAlpha(0.0f);
        }
        long integer = (long) context.getResources().getInteger(C1894R.integer.wireless_charging_fade_offset);
        long integer2 = (long) context.getResources().getInteger(C1894R.integer.wireless_charging_fade_duration);
        float f = context.getResources().getFloat(C1894R.dimen.wireless_charging_anim_battery_level_text_size_start);
        float f2 = context.getResources().getFloat(C1894R.dimen.wireless_charging_anim_battery_level_text_size_end) * (z2 ? 0.75f : 1.0f);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(textView, "textSize", new float[]{f, f2});
        ofFloat.setInterpolator(new PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f));
        ofFloat.setDuration((long) context.getResources().getInteger(C1894R.integer.wireless_charging_battery_level_text_scale_animation_duration));
        ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(textView, Key.ALPHA, new float[]{0.0f, 1.0f});
        ofFloat2.setInterpolator(Interpolators.LINEAR);
        float f3 = f;
        String str = "textSize";
        ofFloat2.setDuration((long) context.getResources().getInteger(C1894R.integer.wireless_charging_battery_level_text_opacity_duration));
        int integer3 = context.getResources().getInteger(C1894R.integer.wireless_charging_anim_opacity_offset);
        String str2 = Key.ALPHA;
        ofFloat2.setStartDelay((long) integer3);
        ObjectAnimator ofFloat3 = ObjectAnimator.ofFloat(textView, str2, new float[]{1.0f, 0.0f});
        ofFloat3.setDuration(integer2);
        ofFloat3.setInterpolator(Interpolators.LINEAR);
        ofFloat3.setStartDelay(integer);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(new Animator[]{ofFloat, ofFloat2, ofFloat3});
        ObjectAnimator ofArgb = ObjectAnimator.ofArgb(this, "backgroundColor", new int[]{0, SCRIM_COLOR});
        float f4 = f2;
        ofArgb.setDuration(300);
        ofArgb.setInterpolator(Interpolators.LINEAR);
        ObjectAnimator ofArgb2 = ObjectAnimator.ofArgb(this, "backgroundColor", new int[]{SCRIM_COLOR, 0});
        ofArgb2.setDuration(300);
        ofArgb2.setInterpolator(Interpolators.LINEAR);
        ofArgb2.setStartDelay(NotificationTapHelper.DOUBLE_TAP_TIMEOUT_MS);
        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(new Animator[]{ofArgb, ofArgb2});
        animatorSet2.start();
        this.mRippleView = (ChargingRippleView) findViewById(C1894R.C1898id.wireless_charging_ripple);
        this.mRippleView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            public void onViewDetachedFromWindow(View view) {
            }

            public void onViewAttachedToWindow(View view) {
                WirelessChargingLayout.this.mRippleView.setDuration(1500);
                WirelessChargingLayout.this.mRippleView.startRipple();
                WirelessChargingLayout.this.mRippleView.removeOnAttachStateChangeListener(this);
            }
        });
        if (!z2) {
            animatorSet.start();
            return;
        }
        TextView textView2 = (TextView) findViewById(C1894R.C1898id.reverse_wireless_charging_percentage);
        textView2.setVisibility(0);
        textView2.setText(NumberFormat.getPercentInstance().format((double) (((float) i3) / 100.0f)));
        textView2.setAlpha(0.0f);
        ObjectAnimator ofFloat4 = ObjectAnimator.ofFloat(textView2, str, new float[]{f3, f4});
        ofFloat4.setInterpolator(new PathInterpolator(0.0f, 0.0f, 0.0f, 1.0f));
        ofFloat4.setDuration((long) context.getResources().getInteger(C1894R.integer.wireless_charging_battery_level_text_scale_animation_duration));
        ObjectAnimator ofFloat5 = ObjectAnimator.ofFloat(textView2, str2, new float[]{0.0f, 1.0f});
        ofFloat5.setInterpolator(Interpolators.LINEAR);
        ofFloat5.setDuration((long) context.getResources().getInteger(C1894R.integer.wireless_charging_battery_level_text_opacity_duration));
        ofFloat5.setStartDelay((long) context.getResources().getInteger(C1894R.integer.wireless_charging_anim_opacity_offset));
        ObjectAnimator ofFloat6 = ObjectAnimator.ofFloat(textView2, str2, new float[]{1.0f, 0.0f});
        ofFloat6.setDuration(integer2);
        ofFloat6.setInterpolator(Interpolators.LINEAR);
        ofFloat6.setStartDelay(integer);
        AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.playTogether(new Animator[]{ofFloat4, ofFloat5, ofFloat6});
        ImageView imageView = (ImageView) findViewById(C1894R.C1898id.reverse_wireless_charging_icon);
        imageView.setVisibility(0);
        int round = Math.round(TypedValue.applyDimension(1, f4, getResources().getDisplayMetrics()));
        imageView.setPadding(round, 0, round, 0);
        ObjectAnimator ofFloat7 = ObjectAnimator.ofFloat(imageView, str2, new float[]{0.0f, 1.0f});
        ofFloat7.setInterpolator(Interpolators.LINEAR);
        ofFloat7.setDuration((long) context.getResources().getInteger(C1894R.integer.wireless_charging_battery_level_text_opacity_duration));
        ofFloat7.setStartDelay((long) context.getResources().getInteger(C1894R.integer.wireless_charging_anim_opacity_offset));
        ObjectAnimator ofFloat8 = ObjectAnimator.ofFloat(imageView, str2, new float[]{1.0f, 0.0f});
        ofFloat8.setDuration(integer2);
        ofFloat8.setInterpolator(Interpolators.LINEAR);
        ofFloat8.setStartDelay(integer);
        AnimatorSet animatorSet4 = new AnimatorSet();
        animatorSet4.playTogether(new Animator[]{ofFloat7, ofFloat8});
        animatorSet.start();
        animatorSet3.start();
        animatorSet4.start();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mRippleView != null) {
            int measuredWidth = getMeasuredWidth();
            int measuredHeight = getMeasuredHeight();
            ChargingRippleView chargingRippleView = this.mRippleView;
            chargingRippleView.setColor(Utils.getColorAttr(chargingRippleView.getContext(), 16843829).getDefaultColor());
            this.mRippleView.setOrigin(new PointF((float) (measuredWidth / 2), (float) (measuredHeight / 2)));
            this.mRippleView.setRadius(((float) Math.max(measuredWidth, measuredHeight)) * 0.5f);
        }
        super.onLayout(z, i, i2, i3, i4);
    }
}
