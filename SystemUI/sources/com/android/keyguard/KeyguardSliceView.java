package com.android.keyguard;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.net.Uri;
import android.os.Trace;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.motion.widget.Key;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceQuery;
import androidx.slice.widget.RowContent;
import androidx.slice.widget.SliceContent;
import com.android.internal.graphics.ColorUtils;
import com.android.systemui.C1893R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.util.wakelock.KeepAwakeAnimationListener;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.keyguard.calendar.CalendarManager;
import com.nothing.systemui.keyguard.calendar.CalendarSimpleData;
import com.nothing.systemui.keyguard.weather.KeyguardWeatherController;
import com.nothing.systemui.keyguard.weather.WeatherData;
import com.nothing.systemui.keyguard.weather.WeatherUtils;
import com.nothing.systemui.util.NTLogUtil;
import java.p026io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KeyguardSliceView extends LinearLayout {
    public static final int DEFAULT_ANIM_DURATION = 550;
    private static final String TAG = "KeyguardSliceView";
    private View mCalendarLayout;
    private Runnable mContentChangeListener;
    private float mDarkAmount = 0.0f;
    private boolean mHasHeader;
    private int mIconSize;
    private int mIconSizeWithHeader;
    private final LayoutTransition mLayoutTransition;
    private View.OnClickListener mOnClickListener;
    private Row mRow;
    private int mTextColor;
    TextView mTitle;
    private View mWeatherLayout;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public KeyguardSliceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context.getResources();
        LayoutTransition layoutTransition = new LayoutTransition();
        this.mLayoutTransition = layoutTransition;
        layoutTransition.setStagger(0, 275);
        layoutTransition.setDuration(2, 550);
        layoutTransition.setDuration(3, 275);
        layoutTransition.disableTransitionType(0);
        layoutTransition.disableTransitionType(1);
        layoutTransition.setInterpolator(2, Interpolators.FAST_OUT_SLOW_IN);
        layoutTransition.setInterpolator(3, Interpolators.ALPHA_OUT);
        layoutTransition.setAnimateParentHierarchy(false);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTitle = (TextView) findViewById(C1893R.C1897id.title);
        this.mRow = (Row) findViewById(C1893R.C1897id.row);
        this.mTextColor = this.mContext.getResources().getColor(C1893R.C1894color.nt_default_text_color);
        this.mIconSize = (int) this.mContext.getResources().getDimension(C1893R.dimen.widget_icon_size);
        this.mIconSizeWithHeader = (int) this.mContext.getResources().getDimension(C1893R.dimen.header_icon_size);
        this.mTitle.setBreakStrategy(2);
    }

    public void onVisibilityAggregated(boolean z) {
        super.onVisibilityAggregated(z);
        setLayoutTransition(z ? this.mLayoutTransition : null);
    }

    public boolean hasHeader() {
        return this.mHasHeader;
    }

    /* access modifiers changed from: package-private */
    public void hideSlice() {
        this.mTitle.setVisibility(8);
        this.mRow.setVisibility(8);
        this.mHasHeader = false;
        Runnable runnable = this.mContentChangeListener;
        if (runnable != null) {
            runnable.run();
        }
    }

    /* access modifiers changed from: package-private */
    public Map<View, PendingIntent> showSlice(RowContent rowContent, List<SliceContent> list) {
        int i;
        int i2;
        CharSequence charSequence;
        Drawable drawable;
        Trace.beginSection("KeyguardSliceView#showSlice");
        this.mHasHeader = rowContent != null;
        HashMap hashMap = new HashMap();
        int i3 = 8;
        ViewGroup viewGroup = null;
        if (!this.mHasHeader) {
            this.mTitle.setVisibility(8);
        } else {
            this.mTitle.setVisibility(0);
            SliceItem titleItem = rowContent.getTitleItem();
            this.mTitle.setText(titleItem != null ? titleItem.getText() : null);
            if (!(rowContent.getPrimaryAction() == null || rowContent.getPrimaryAction().getAction() == null)) {
                hashMap.put(this.mTitle, rowContent.getPrimaryAction().getAction());
            }
        }
        int size = list.size();
        int textColor = getTextColor();
        int i4 = this.mHasHeader;
        Row row = this.mRow;
        if (size > 0) {
            i3 = 0;
        }
        row.setVisibility(i3);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mRow.getLayoutParams();
        layoutParams.gravity = 8388611;
        this.mRow.setLayoutParams(layoutParams);
        boolean z = false;
        boolean z2 = false;
        while (i4 < size) {
            RowContent rowContent2 = (RowContent) list.get(i4);
            SliceItem sliceItem = rowContent2.getSliceItem();
            Uri uri = sliceItem.getSlice().getUri();
            NTLogUtil.m1682i(TAG, "itemTag=" + uri);
            if (uri.toString().equals(KeyguardSliceProvider.KEYGUARD_CALENDAR_URI)) {
                NTLogUtil.m1682i(TAG, "add calendar item");
                if (this.mRow.findViewWithTag(uri) == null) {
                    View inflate = LayoutInflater.from(this.mRow.getContext()).inflate(C1893R.layout.nt_keyguard_calendar, viewGroup);
                    this.mCalendarLayout = inflate;
                    inflate.setTag(uri);
                    NTLogUtil.m1682i(TAG, "calendar viewIndex=0, itemTag=" + uri);
                }
                updateCalendarInfo();
                i2 = size;
                z2 = true;
            } else if (uri.toString().equals(KeyguardSliceProvider.KEYGUARD_WEATHER_URI)) {
                NTLogUtil.m1682i(TAG, "add weather item");
                int i5 = i4 - (this.mHasHeader ? 1 : 0);
                if (this.mRow.findViewWithTag(uri) == null) {
                    View inflate2 = LayoutInflater.from(this.mRow.getContext()).inflate(C1893R.layout.nt_keyguard_weather, viewGroup);
                    this.mWeatherLayout = inflate2;
                    inflate2.setTag(uri);
                    NTLogUtil.m1682i(TAG, "weather viewIndex=" + i5 + ", itemTag=" + uri);
                    this.mRow.addView(this.mWeatherLayout, i5);
                }
                updateWeatherInfo();
                i2 = size;
                z = true;
            } else {
                KeyguardSliceTextView keyguardSliceTextView = (KeyguardSliceTextView) this.mRow.findViewWithTag(uri);
                NTLogUtil.m1682i(TAG, "add item " + uri);
                if (keyguardSliceTextView == null) {
                    keyguardSliceTextView = new KeyguardSliceTextView(this.mContext);
                    keyguardSliceTextView.setTextColor(textColor);
                    keyguardSliceTextView.setTag(uri);
                    i2 = size;
                    keyguardSliceTextView.setShadowLayer(5.0f, 0.0f, 5.0f, this.mRow.getResources().getColor(C1893R.C1894color.nt_keyguard_glance_text_shadow_color, (Resources.Theme) null));
                    int i6 = i4 - (this.mHasHeader ? 1 : 0);
                    NTLogUtil.m1682i(TAG, "item viewIndex " + i6 + ", itemTag=" + uri);
                    this.mRow.addView(keyguardSliceTextView, i6);
                } else {
                    i2 = size;
                }
                PendingIntent action = rowContent2.getPrimaryAction() != null ? rowContent2.getPrimaryAction().getAction() : null;
                hashMap.put(keyguardSliceTextView, action);
                SliceItem titleItem2 = rowContent2.getTitleItem();
                if (titleItem2 == null) {
                    charSequence = null;
                } else {
                    charSequence = titleItem2.getText();
                }
                keyguardSliceTextView.setText(charSequence);
                keyguardSliceTextView.setContentDescription(rowContent2.getContentDescription());
                SliceItem find = SliceQuery.find(sliceItem.getSlice(), "image");
                if (find != null) {
                    int i7 = this.mHasHeader ? this.mIconSizeWithHeader : this.mIconSize;
                    drawable = find.getIcon().loadDrawable(this.mContext);
                    if (drawable != null) {
                        if (drawable instanceof InsetDrawable) {
                            drawable = ((InsetDrawable) drawable).getDrawable();
                        }
                        drawable.setBounds(0, 0, Math.max((int) ((((float) drawable.getIntrinsicWidth()) / ((float) drawable.getIntrinsicHeight())) * ((float) i7)), 1), i7);
                    }
                } else {
                    drawable = null;
                }
                viewGroup = null;
                keyguardSliceTextView.setCompoundDrawablesRelative(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
                keyguardSliceTextView.setOnClickListener(this.mOnClickListener);
                keyguardSliceTextView.setClickable(action != null);
            }
            i4++;
            size = i2;
        }
        NTLogUtil.m1682i(TAG, "hasWeather= " + z + ",hasCalendar= " + z2);
        if (!z2 || this.mCalendarLayout == null) {
            i = 0;
        } else {
            this.mRow.removeAllViews();
            i = 0;
            this.mRow.addView(this.mCalendarLayout, 0);
        }
        int i8 = i;
        while (i8 < this.mRow.getChildCount()) {
            View childAt = this.mRow.getChildAt(i8);
            if (childAt == this.mWeatherLayout) {
                if (!z) {
                    NTLogUtil.m1682i(TAG, "remove mWeatherLayout " + i8);
                    this.mRow.removeView(childAt);
                } else {
                    i8++;
                }
            } else if (childAt == this.mCalendarLayout) {
                if (!z2) {
                    NTLogUtil.m1682i(TAG, "remove mCalendarLayout " + i8);
                    this.mRow.removeView(childAt);
                } else {
                    i8++;
                }
            } else if (!hashMap.containsKey(childAt)) {
                NTLogUtil.m1682i(TAG, "remove child " + i8);
                this.mRow.removeView(childAt);
            } else {
                i8++;
            }
            i8--;
            i8++;
        }
        Runnable runnable = this.mContentChangeListener;
        if (runnable != null) {
            runnable.run();
        }
        Trace.endSection();
        return hashMap;
    }

    public void setDarkAmount(float f) {
        this.mDarkAmount = f;
        this.mRow.setDarkAmount(f);
        updateTextColors();
    }

    private void updateTextColors() {
        int textColor = getTextColor();
        this.mTitle.setTextColor(textColor);
        int childCount = this.mRow.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = this.mRow.getChildAt(i);
            if (childAt instanceof TextView) {
                ((TextView) childAt).setTextColor(textColor);
            }
        }
    }

    public void setContentChangeListener(Runnable runnable) {
        this.mContentChangeListener = runnable;
    }

    /* access modifiers changed from: package-private */
    public int getTextColor() {
        return ColorUtils.blendARGB(this.mTextColor, -1, this.mDarkAmount);
    }

    /* access modifiers changed from: package-private */
    public void setTextColor(int i) {
        this.mTextColor = i;
        updateTextColors();
    }

    /* access modifiers changed from: package-private */
    public void onDensityOrFontScaleChanged() {
        this.mIconSize = this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.widget_icon_size);
        this.mIconSizeWithHeader = (int) this.mContext.getResources().getDimension(C1893R.dimen.header_icon_size);
        for (int i = 0; i < this.mRow.getChildCount(); i++) {
            View childAt = this.mRow.getChildAt(i);
            if (childAt instanceof KeyguardSliceTextView) {
                ((KeyguardSliceTextView) childAt).onDensityOrFontScaleChanged();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onOverlayChanged() {
        for (int i = 0; i < this.mRow.getChildCount(); i++) {
            View childAt = this.mRow.getChildAt(i);
            if (childAt instanceof KeyguardSliceTextView) {
                ((KeyguardSliceTextView) childAt).onOverlayChanged();
            }
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Object obj;
        printWriter.println("KeyguardSliceView:");
        StringBuilder sb = new StringBuilder("  mTitle: ");
        TextView textView = this.mTitle;
        boolean z = true;
        Object obj2 = "null";
        if (textView == null) {
            obj = obj2;
        } else {
            obj = Boolean.valueOf(textView.getVisibility() == 0);
        }
        printWriter.println(sb.append(obj).toString());
        StringBuilder sb2 = new StringBuilder("  mRow: ");
        Row row = this.mRow;
        if (row != null) {
            if (row.getVisibility() != 0) {
                z = false;
            }
            obj2 = Boolean.valueOf(z);
        }
        printWriter.println(sb2.append(obj2).toString());
        printWriter.println("  mTextColor: " + Integer.toHexString(this.mTextColor));
        printWriter.println("  mDarkAmount: " + this.mDarkAmount);
        printWriter.println("  mHasHeader: " + this.mHasHeader);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
        this.mTitle.setOnClickListener(onClickListener);
    }

    public static class Row extends LinearLayout {
        private float mDarkAmount;
        private final Animation.AnimationListener mKeepAwakeListener;
        private Set<KeyguardSliceTextView> mKeyguardSliceTextViewSet;
        private LayoutTransition mLayoutTransition;

        public boolean hasOverlappingRendering() {
            return false;
        }

        /* access modifiers changed from: protected */
        public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
            return super.generateDefaultLayoutParams();
        }

        public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
            return super.generateLayoutParams(attributeSet);
        }

        /* access modifiers changed from: protected */
        public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
            return super.generateLayoutParams(layoutParams);
        }

        public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
            return super.getOverlay();
        }

        public Row(Context context) {
            this(context, (AttributeSet) null);
        }

        public Row(Context context, AttributeSet attributeSet) {
            this(context, attributeSet, 0);
        }

        public Row(Context context, AttributeSet attributeSet, int i) {
            this(context, attributeSet, i, 0);
        }

        public Row(Context context, AttributeSet attributeSet, int i, int i2) {
            super(context, attributeSet, i, i2);
            this.mKeyguardSliceTextViewSet = new HashSet();
            this.mKeepAwakeListener = new KeepAwakeAnimationListener(this.mContext);
        }

        /* access modifiers changed from: protected */
        public void onFinishInflate() {
            LayoutTransition layoutTransition = new LayoutTransition();
            this.mLayoutTransition = layoutTransition;
            layoutTransition.setDuration(550);
            ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder((Object) null, new PropertyValuesHolder[]{PropertyValuesHolder.ofInt(NavigationBarInflaterView.LEFT, new int[]{0, 1}), PropertyValuesHolder.ofInt(NavigationBarInflaterView.RIGHT, new int[]{0, 1})});
            this.mLayoutTransition.setAnimator(0, ofPropertyValuesHolder);
            this.mLayoutTransition.setAnimator(1, ofPropertyValuesHolder);
            this.mLayoutTransition.setInterpolator(0, Interpolators.ACCELERATE_DECELERATE);
            this.mLayoutTransition.setInterpolator(1, Interpolators.ACCELERATE_DECELERATE);
            this.mLayoutTransition.setStartDelay(0, 550);
            this.mLayoutTransition.setStartDelay(1, 550);
            this.mLayoutTransition.setAnimator(2, ObjectAnimator.ofFloat((Object) null, Key.ALPHA, new float[]{0.0f, 1.0f}));
            this.mLayoutTransition.setInterpolator(2, Interpolators.ALPHA_IN);
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object) null, Key.ALPHA, new float[]{1.0f, 0.0f});
            this.mLayoutTransition.setInterpolator(3, Interpolators.ALPHA_OUT);
            this.mLayoutTransition.setDuration(3, 137);
            this.mLayoutTransition.setAnimator(3, ofFloat);
            this.mLayoutTransition.setAnimateParentHierarchy(false);
            setOrientation(1);
            setGravity(17);
        }

        public void onVisibilityAggregated(boolean z) {
            super.onVisibilityAggregated(z);
            setLayoutTransition(z ? this.mLayoutTransition : null);
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            View.MeasureSpec.getSize(i);
            int childCount = getChildCount();
            for (int i3 = 0; i3 < childCount; i3++) {
                View childAt = getChildAt(i3);
                if (childAt instanceof KeyguardSliceTextView) {
                    ((KeyguardSliceTextView) childAt).setMaxWidth(Integer.MAX_VALUE);
                }
            }
            super.onMeasure(i, i2);
        }

        public void setDarkAmount(float f) {
            Animation.AnimationListener animationListener;
            boolean z = true;
            boolean z2 = f != 0.0f;
            if (this.mDarkAmount == 0.0f) {
                z = false;
            }
            if (z2 != z) {
                this.mDarkAmount = f;
                if (z2) {
                    animationListener = null;
                } else {
                    animationListener = this.mKeepAwakeListener;
                }
                setLayoutAnimationListener(animationListener);
            }
        }

        public void addView(View view, int i) {
            super.addView(view, i);
            if (view instanceof KeyguardSliceTextView) {
                this.mKeyguardSliceTextViewSet.add((KeyguardSliceTextView) view);
            }
        }

        public void removeView(View view) {
            super.removeView(view);
            if (view instanceof KeyguardSliceTextView) {
                this.mKeyguardSliceTextViewSet.remove((KeyguardSliceTextView) view);
            }
        }
    }

    static class KeyguardSliceTextView extends TextView {
        private static int sStyleId = 2132017924;

        KeyguardSliceTextView(Context context) {
            super(context, (AttributeSet) null, 0, sStyleId);
            onDensityOrFontScaleChanged();
            setEllipsize(TextUtils.TruncateAt.END);
            setTextAlignment(5);
        }

        public void onDensityOrFontScaleChanged() {
            updatePadding();
        }

        public void onOverlayChanged() {
            setTextAppearance(sStyleId);
        }

        public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
            if (!WeatherUtils.isNdotFontSupport()) {
                setTypeface(Typeface.DEFAULT);
            } else {
                setTypeface(getResources().getFont(C1893R.C1896font.ndot55));
            }
            super.setText(charSequence, bufferType);
            updatePadding();
        }

        private void updatePadding() {
            TextUtils.isEmpty(getText());
            int dimension = ((int) getContext().getResources().getDimension(C1893R.dimen.widget_horizontal_padding)) / 2;
            setPadding(0, 5, 0, 0);
            setCompoundDrawablePadding((int) this.mContext.getResources().getDimension(C1893R.dimen.widget_icon_padding));
        }

        public void setTextColor(int i) {
            super.setTextColor(i);
            updateDrawableColors();
        }

        public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
            super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
            updateDrawableColors();
            updatePadding();
        }

        private void updateDrawableColors() {
            int currentTextColor = getCurrentTextColor();
            for (Drawable drawable : getCompoundDrawables()) {
                if (drawable != null) {
                    drawable.setTint(currentTextColor);
                }
            }
        }
    }

    private void updateWeatherInfo() {
        if (this.mWeatherLayout != null && ((KeyguardWeatherController) NTDependencyEx.get(KeyguardWeatherController.class)).getWeatherData() != null) {
            WeatherData weatherData = ((KeyguardWeatherController) NTDependencyEx.get(KeyguardWeatherController.class)).getWeatherData();
            boolean isKeyguardCelsiusUnitOn = ((KeyguardWeatherController) NTDependencyEx.get(KeyguardWeatherController.class)).isKeyguardCelsiusUnitOn();
            StringBuilder sb = new StringBuilder();
            if (isKeyguardCelsiusUnitOn) {
                sb.append(weatherData.getTemp());
            } else {
                sb.append(WeatherUtils.celsiusToFahrenheit(weatherData.getTemp()));
            }
            sb.append("° ");
            sb.append(weatherData.getPhrase());
            NTLogUtil.m1682i(TAG, "updateWeatherInfo2: lastTime=" + weatherData.getLastUpdateTime() + ", iconType=" + weatherData.getIconType() + ", weather=" + sb + ",isCelsiusUnitOn=" + isKeyguardCelsiusUnitOn);
            TextView textView = (TextView) this.mWeatherLayout.findViewById(C1893R.C1897id.nt_keyguard_temp_and_phrase_text);
            if (!WeatherUtils.isNdotFontSupport()) {
                textView.setTypeface(Typeface.DEFAULT);
                textView.setIncludeFontPadding(false);
            } else {
                textView.setTypeface(getResources().getFont(C1893R.C1896font.ndot55));
                textView.setIncludeFontPadding(true);
            }
            textView.setText(sb);
            NTLogUtil.m1682i(TAG, "getIconType=" + weatherData.getIconType());
            ((ImageView) this.mWeatherLayout.findViewById(C1893R.C1897id.weather_iv)).setImageResource(weatherData.getWeatherIcon(weatherData.getIconType()));
        }
    }

    private void updateCalendarInfo() {
        CalendarSimpleData calendarEventData;
        CalendarManager calendarManager = (CalendarManager) NTDependencyEx.get(CalendarManager.class);
        if (this.mCalendarLayout != null && calendarManager != null && (calendarEventData = calendarManager.getCalendarEventData()) != null) {
            TextView textView = (TextView) this.mCalendarLayout.findViewById(C1893R.C1897id.calendar_title);
            TextView textView2 = (TextView) this.mCalendarLayout.findViewById(C1893R.C1897id.calendar_event_status);
            TextView textView3 = (TextView) this.mCalendarLayout.findViewById(C1893R.C1897id.calendar_location);
            TextView textView4 = (TextView) this.mCalendarLayout.findViewById(C1893R.C1897id.calendar_time);
            textView.setText(calendarEventData.getTitle());
            textView3.setText(calendarEventData.getLocation());
            textView3.setVisibility(TextUtils.isEmpty(calendarEventData.getLocation()) ? 8 : 0);
            textView4.setText(calendarManager.getCalenderWidgetTime(calendarEventData));
            textView2.setText(String.format(" %s", calendarManager.getCalendarDescription(calendarEventData)));
            NTLogUtil.m1680d(TAG, "title=" + textView.getText() + " status=" + textView2.getText() + " location=" + textView3.getText() + " time=" + textView4.getText());
        }
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setLayoutDirection(configuration.getLayoutDirection());
    }
}
