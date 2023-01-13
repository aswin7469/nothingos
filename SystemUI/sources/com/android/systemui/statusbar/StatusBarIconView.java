package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.Log;
import android.util.Property;
import android.util.TypedValue;
import android.view.ViewDebug;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.ColorUtils;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.internal.util.ContrastColorUtil;
import com.android.systemui.C1894R;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.notification.NotificationIconDozeHelper;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.util.drawable.DrawableSize;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class StatusBarIconView extends AnimatedImageView implements StatusIconDisplayable {
    private static final float DARK_ALPHA_BOOST = 0.67f;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final Property<StatusBarIconView, Float> DOT_APPEAR_AMOUNT = new FloatProperty<StatusBarIconView>("dot_appear_amount") {
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
            super.set(obj, (Float) obj2);
        }

        public void setValue(StatusBarIconView statusBarIconView, float f) {
            statusBarIconView.setDotAppearAmount(f);
        }

        public Float get(StatusBarIconView statusBarIconView) {
            return Float.valueOf(statusBarIconView.getDotAppearAmount());
        }
    };
    private static final Property<StatusBarIconView, Float> ICON_APPEAR_AMOUNT = new FloatProperty<StatusBarIconView>("iconAppearAmount") {
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
            super.set(obj, (Float) obj2);
        }

        public void setValue(StatusBarIconView statusBarIconView, float f) {
            statusBarIconView.setIconAppearAmount(f);
        }

        public Float get(StatusBarIconView statusBarIconView) {
            return Float.valueOf(statusBarIconView.getIconAppearAmount());
        }
    };
    public static final int NO_COLOR = 0;
    public static final int STATE_DOT = 1;
    public static final int STATE_HIDDEN = 2;
    public static final int STATE_ICON = 0;
    private static final String TAG = "StatusBarIconView";
    private final int ANIMATION_DURATION_FAST;
    private boolean mAlwaysScaleIcon;
    /* access modifiers changed from: private */
    public int mAnimationStartColor;
    private final boolean mBlocked;
    private int mCachedContrastBackgroundColor;
    /* access modifiers changed from: private */
    public ValueAnimator mColorAnimator;
    private final ValueAnimator.AnimatorUpdateListener mColorUpdater;
    private int mContrastedDrawableColor;
    private int mCurrentSetColor;
    private final Paint mDebugPaint;
    private int mDecorColor;
    private int mDensity;
    private boolean mDismissed;
    /* access modifiers changed from: private */
    public ObjectAnimator mDotAnimator;
    private float mDotAppearAmount;
    private final Paint mDotPaint;
    private float mDotRadius;
    private float mDozeAmount;
    private final NotificationIconDozeHelper mDozer;
    private int mDrawableColor;
    private StatusBarIcon mIcon;
    private float mIconAppearAmount;
    /* access modifiers changed from: private */
    public ObjectAnimator mIconAppearAnimator;
    private int mIconColor;
    private float mIconScale;
    private boolean mIncreasedSize;
    private boolean mIsInShelf;
    private Runnable mLayoutRunnable;
    private float[] mMatrix;
    private ColorMatrixColorFilter mMatrixColorFilter;
    private boolean mNightMode;
    private StatusBarNotification mNotification;
    private Drawable mNumberBackground;
    private Paint mNumberPain;
    private String mNumberText;
    private int mNumberX;
    private int mNumberY;
    private Runnable mOnDismissListener;
    private OnVisibilityChangedListener mOnVisibilityChangedListener;
    private boolean mShowsConversation;
    @ViewDebug.ExportedProperty
    private String mSlot;
    private int mStaticDotRadius;
    private int mStatusBarIconDrawingSize;
    private int mStatusBarIconDrawingSizeIncreased;
    private int mStatusBarIconSize;
    private float mSystemIconDefaultScale;
    private float mSystemIconDesiredHeight;
    private float mSystemIconIntrinsicHeight;
    private int mVisibleState;

    public interface OnVisibilityChangedListener {
        void onVisibilityChanged(int i);
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$new$0$com-android-systemui-statusbar-StatusBarIconView  reason: not valid java name */
    public /* synthetic */ void m3046lambda$new$0$comandroidsystemuistatusbarStatusBarIconView(ValueAnimator valueAnimator) {
        setColorInternal(NotificationUtils.interpolateColors(this.mAnimationStartColor, this.mIconColor, valueAnimator.getAnimatedFraction()));
    }

    public StatusBarIconView(Context context, String str, StatusBarNotification statusBarNotification) {
        this(context, str, statusBarNotification, false);
    }

    public StatusBarIconView(Context context, String str, StatusBarNotification statusBarNotification, boolean z) {
        super(context);
        this.mSystemIconDesiredHeight = 15.0f;
        this.mSystemIconIntrinsicHeight = 17.0f;
        this.mSystemIconDefaultScale = 15.0f / 17.0f;
        this.ANIMATION_DURATION_FAST = 100;
        boolean z2 = true;
        this.mStatusBarIconDrawingSizeIncreased = 1;
        this.mStatusBarIconDrawingSize = 1;
        this.mStatusBarIconSize = 1;
        this.mIconScale = 1.0f;
        this.mDotPaint = new Paint(1);
        this.mVisibleState = 0;
        this.mIconAppearAmount = 1.0f;
        this.mCurrentSetColor = 0;
        this.mAnimationStartColor = 0;
        this.mColorUpdater = new StatusBarIconView$$ExternalSyntheticLambda0(this);
        this.mCachedContrastBackgroundColor = 0;
        Paint paint = new Paint(1);
        this.mDebugPaint = paint;
        this.mDozer = new NotificationIconDozeHelper(context);
        this.mBlocked = z;
        this.mSlot = str;
        Paint paint2 = new Paint();
        this.mNumberPain = paint2;
        paint2.setTextAlign(Paint.Align.CENTER);
        this.mNumberPain.setColor(context.getColor(C1894R.C1896drawable.notification_number_text_color));
        this.mNumberPain.setAntiAlias(true);
        setNotification(statusBarNotification);
        setScaleType(ImageView.ScaleType.CENTER);
        this.mDensity = context.getResources().getDisplayMetrics().densityDpi;
        this.mNightMode = (context.getResources().getConfiguration().uiMode & 48) != 32 ? false : z2;
        initializeDecorColor();
        reloadDimens();
        maybeUpdateIconScaleDimens();
        if (DEBUG) {
            paint.setColor(-65536);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2.0f);
        }
    }

    public StatusBarIconView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSystemIconDesiredHeight = 15.0f;
        this.mSystemIconIntrinsicHeight = 17.0f;
        this.mSystemIconDefaultScale = 15.0f / 17.0f;
        this.ANIMATION_DURATION_FAST = 100;
        this.mStatusBarIconDrawingSizeIncreased = 1;
        this.mStatusBarIconDrawingSize = 1;
        this.mStatusBarIconSize = 1;
        this.mIconScale = 1.0f;
        this.mDotPaint = new Paint(1);
        this.mVisibleState = 0;
        this.mIconAppearAmount = 1.0f;
        this.mCurrentSetColor = 0;
        this.mAnimationStartColor = 0;
        this.mColorUpdater = new StatusBarIconView$$ExternalSyntheticLambda0(this);
        this.mCachedContrastBackgroundColor = 0;
        this.mDebugPaint = new Paint(1);
        this.mDozer = new NotificationIconDozeHelper(context);
        this.mBlocked = false;
        this.mAlwaysScaleIcon = true;
        reloadDimens();
        maybeUpdateIconScaleDimens();
        this.mDensity = context.getResources().getDisplayMetrics().densityDpi;
    }

    private void maybeUpdateIconScaleDimens() {
        if (this.mNotification != null || this.mAlwaysScaleIcon) {
            updateIconScaleForNotifications();
        } else {
            updateIconScaleForSystemIcons();
        }
    }

    private void updateIconScaleForNotifications() {
        this.mIconScale = ((float) (this.mIncreasedSize ? this.mStatusBarIconDrawingSizeIncreased : this.mStatusBarIconDrawingSize)) / ((float) this.mStatusBarIconSize);
        updatePivot();
    }

    private void updateIconScaleForSystemIcons() {
        float iconHeight = getIconHeight();
        if (iconHeight != 0.0f) {
            this.mIconScale = this.mSystemIconDesiredHeight / iconHeight;
        } else {
            this.mIconScale = this.mSystemIconDefaultScale;
        }
    }

    private float getIconHeight() {
        if (getDrawable() != null) {
            return (float) getDrawable().getIntrinsicHeight();
        }
        return this.mSystemIconIntrinsicHeight;
    }

    public float getIconScaleIncreased() {
        return ((float) this.mStatusBarIconDrawingSizeIncreased) / ((float) this.mStatusBarIconDrawingSize);
    }

    public float getIconScale() {
        return this.mIconScale;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = configuration.densityDpi;
        if (i != this.mDensity) {
            this.mDensity = i;
            reloadDimens();
            updateDrawable();
            maybeUpdateIconScaleDimens();
        }
        boolean z = (configuration.uiMode & 48) == 32;
        if (z != this.mNightMode) {
            this.mNightMode = z;
            initializeDecorColor();
        }
    }

    private void reloadDimens() {
        boolean z = this.mDotRadius == ((float) this.mStaticDotRadius);
        Resources resources = getResources();
        this.mStaticDotRadius = resources.getDimensionPixelSize(C1894R.dimen.overflow_dot_radius);
        this.mStatusBarIconSize = resources.getDimensionPixelSize(C1894R.dimen.status_bar_icon_size);
        this.mStatusBarIconDrawingSizeIncreased = resources.getDimensionPixelSize(C1894R.dimen.status_bar_icon_drawing_size_dark);
        this.mStatusBarIconDrawingSize = resources.getDimensionPixelSize(C1894R.dimen.status_bar_icon_drawing_size);
        if (z) {
            this.mDotRadius = (float) this.mStaticDotRadius;
        }
        this.mSystemIconDesiredHeight = resources.getDimension(17105554);
        float dimension = resources.getDimension(17105553);
        this.mSystemIconIntrinsicHeight = dimension;
        this.mSystemIconDefaultScale = this.mSystemIconDesiredHeight / dimension;
    }

    public void setNotification(StatusBarNotification statusBarNotification) {
        this.mNotification = statusBarNotification;
        if (statusBarNotification != null) {
            setContentDescription(statusBarNotification.getNotification());
        }
        maybeUpdateIconScaleDimens();
    }

    private static boolean streq(String str, String str2) {
        if (str == str2) {
            return true;
        }
        if (str == null && str2 != null) {
            return false;
        }
        if (str == null || str2 != null) {
            return str.equals(str2);
        }
        return false;
    }

    public boolean equalIcons(Icon icon, Icon icon2) {
        if (icon == icon2) {
            return true;
        }
        if (icon.getType() != icon2.getType()) {
            return false;
        }
        int type = icon.getType();
        if (type != 2) {
            if (type == 4 || type == 6) {
                return icon.getUriString().equals(icon2.getUriString());
            }
            return false;
        } else if (!icon.getResPackage().equals(icon2.getResPackage()) || icon.getResId() != icon2.getResId()) {
            return false;
        } else {
            return true;
        }
    }

    public boolean set(StatusBarIcon statusBarIcon) {
        StatusBarIcon statusBarIcon2 = this.mIcon;
        int i = 0;
        boolean z = statusBarIcon2 != null && equalIcons(statusBarIcon2.icon, statusBarIcon.icon);
        boolean z2 = z && this.mIcon.iconLevel == statusBarIcon.iconLevel;
        StatusBarIcon statusBarIcon3 = this.mIcon;
        boolean z3 = statusBarIcon3 != null && statusBarIcon3.visible == statusBarIcon.visible;
        StatusBarIcon statusBarIcon4 = this.mIcon;
        boolean z4 = statusBarIcon4 != null && statusBarIcon4.number == statusBarIcon.number;
        this.mIcon = statusBarIcon.clone();
        setContentDescription(statusBarIcon.contentDescription);
        if (!z) {
            if (!updateDrawable(false)) {
                return false;
            }
            setTag(C1894R.C1898id.icon_is_grayscale, (Object) null);
            maybeUpdateIconScaleDimens();
        }
        if (!z2) {
            setImageLevel(statusBarIcon.iconLevel);
        }
        if (!z4) {
            if (statusBarIcon.number <= 0 || !getContext().getResources().getBoolean(C1894R.bool.config_statusBarShowNumber)) {
                this.mNumberBackground = null;
                this.mNumberText = null;
            } else {
                if (this.mNumberBackground == null) {
                    this.mNumberBackground = getContext().getResources().getDrawable(C1894R.C1896drawable.ic_notification_overlay);
                }
                placeNumber();
            }
            invalidate();
        }
        if (!z3) {
            if (!statusBarIcon.visible || this.mBlocked) {
                i = 8;
            }
            setVisibility(i);
        }
        return true;
    }

    public void updateDrawable() {
        updateDrawable(true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0048, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        android.util.Log.w(TAG, "OOM while inflating " + r4.mIcon.icon + " for slot " + r4.mSlot);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x006d, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x006e, code lost:
        android.os.Trace.endSection();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0071, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x004a */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean updateDrawable(boolean r5) {
        /*
            r4 = this;
            java.lang.String r0 = "StatusBarIconView"
            java.lang.String r1 = "OOM while inflating "
            com.android.internal.statusbar.StatusBarIcon r2 = r4.mIcon
            r3 = 0
            if (r2 != 0) goto L_0x000a
            return r3
        L_0x000a:
            java.lang.String r2 = "StatusBarIconView#updateDrawable()"
            android.os.Trace.beginSection(r2)     // Catch:{ OutOfMemoryError -> 0x004a }
            com.android.internal.statusbar.StatusBarIcon r2 = r4.mIcon     // Catch:{ OutOfMemoryError -> 0x004a }
            android.graphics.drawable.Drawable r1 = r4.getIcon(r2)     // Catch:{ OutOfMemoryError -> 0x004a }
            android.os.Trace.endSection()
            if (r1 != 0) goto L_0x003d
            java.lang.StringBuilder r5 = new java.lang.StringBuilder
            java.lang.String r1 = "No icon for slot "
            r5.<init>((java.lang.String) r1)
            java.lang.String r1 = r4.mSlot
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r1)
            java.lang.String r1 = "; "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r1)
            com.android.internal.statusbar.StatusBarIcon r4 = r4.mIcon
            android.graphics.drawable.Icon r4 = r4.icon
            java.lang.StringBuilder r4 = r5.append((java.lang.Object) r4)
            java.lang.String r4 = r4.toString()
            android.util.Log.w(r0, r4)
            return r3
        L_0x003d:
            if (r5 == 0) goto L_0x0043
            r5 = 0
            r4.setImageDrawable(r5)
        L_0x0043:
            r4.setImageDrawable(r1)
            r4 = 1
            return r4
        L_0x0048:
            r4 = move-exception
            goto L_0x006e
        L_0x004a:
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x0048 }
            r5.<init>((java.lang.String) r1)     // Catch:{ all -> 0x0048 }
            com.android.internal.statusbar.StatusBarIcon r1 = r4.mIcon     // Catch:{ all -> 0x0048 }
            android.graphics.drawable.Icon r1 = r1.icon     // Catch:{ all -> 0x0048 }
            java.lang.StringBuilder r5 = r5.append((java.lang.Object) r1)     // Catch:{ all -> 0x0048 }
            java.lang.String r1 = " for slot "
            java.lang.StringBuilder r5 = r5.append((java.lang.String) r1)     // Catch:{ all -> 0x0048 }
            java.lang.String r4 = r4.mSlot     // Catch:{ all -> 0x0048 }
            java.lang.StringBuilder r4 = r5.append((java.lang.String) r4)     // Catch:{ all -> 0x0048 }
            java.lang.String r4 = r4.toString()     // Catch:{ all -> 0x0048 }
            android.util.Log.w(r0, r4)     // Catch:{ all -> 0x0048 }
            android.os.Trace.endSection()
            return r3
        L_0x006e:
            android.os.Trace.endSection()
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.StatusBarIconView.updateDrawable(boolean):boolean");
    }

    public Icon getSourceIcon() {
        return this.mIcon.icon;
    }

    /* access modifiers changed from: package-private */
    public Drawable getIcon(StatusBarIcon statusBarIcon) {
        Context context = getContext();
        StatusBarNotification statusBarNotification = this.mNotification;
        if (statusBarNotification != null) {
            context = statusBarNotification.getPackageContext(getContext());
        }
        Context context2 = getContext();
        if (context == null) {
            context = getContext();
        }
        return getIcon(context2, context, statusBarIcon);
    }

    private Drawable getIcon(Context context, Context context2, StatusBarIcon statusBarIcon) {
        int identifier = statusBarIcon.user.getIdentifier();
        if (identifier == -1) {
            identifier = 0;
        }
        Drawable loadDrawableAsUser = statusBarIcon.icon.loadDrawableAsUser(context2, identifier);
        TypedValue typedValue = new TypedValue();
        context.getResources().getValue(C1894R.dimen.status_bar_icon_scale_factor, typedValue, true);
        float f = typedValue.getFloat();
        if (loadDrawableAsUser != null) {
            boolean isLowRamDeviceStatic = ActivityManager.isLowRamDeviceStatic();
            Resources resources = context.getResources();
            int dimensionPixelSize = resources.getDimensionPixelSize(isLowRamDeviceStatic ? 17105433 : 17105432);
            loadDrawableAsUser = DrawableSize.downscaleToSize(resources, loadDrawableAsUser, dimensionPixelSize, dimensionPixelSize);
        }
        if (f == 1.0f) {
            return loadDrawableAsUser;
        }
        return new ScalingDrawableWrapper(loadDrawableAsUser, f);
    }

    public StatusBarIcon getStatusBarIcon() {
        return this.mIcon;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        StatusBarNotification statusBarNotification = this.mNotification;
        if (statusBarNotification != null) {
            accessibilityEvent.setParcelableData(statusBarNotification.getNotification());
        }
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (this.mNumberBackground != null) {
            placeNumber();
        }
    }

    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        updateDrawable();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        float f;
        if (this.mIconAppearAmount > 0.0f) {
            canvas.save();
            float f2 = this.mIconScale;
            float f3 = this.mIconAppearAmount;
            canvas.scale(f2 * f3, f2 * f3, (float) (getWidth() / 2), (float) (getHeight() / 2));
            super.onDraw(canvas);
            canvas.restore();
        }
        Drawable drawable = this.mNumberBackground;
        if (drawable != null) {
            drawable.draw(canvas);
            canvas.drawText(this.mNumberText, (float) this.mNumberX, (float) this.mNumberY, this.mNumberPain);
        }
        if (this.mDotAppearAmount != 0.0f) {
            float alpha = ((float) Color.alpha(this.mDecorColor)) / 255.0f;
            float f4 = this.mDotAppearAmount;
            if (f4 <= 1.0f) {
                f = this.mDotRadius * f4;
            } else {
                float f5 = f4 - 1.0f;
                alpha *= 1.0f - f5;
                f = NotificationUtils.interpolate(this.mDotRadius, (float) (getWidth() / 4), f5);
            }
            this.mDotPaint.setAlpha((int) (alpha * 255.0f));
            canvas.drawCircle((float) (this.mStatusBarIconSize / 2), (float) (getHeight() / 2), f, this.mDotPaint);
        }
        if (DEBUG) {
            canvas.drawRect(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight(), this.mDebugPaint);
        }
    }

    /* access modifiers changed from: protected */
    public void debug(int i) {
        super.debug(i);
        Log.d("View", debugIndent(i) + "slot=" + this.mSlot);
        Log.d("View", debugIndent(i) + "icon=" + this.mIcon);
    }

    /* access modifiers changed from: package-private */
    public void placeNumber() {
        String str;
        if (this.mIcon.number > getContext().getResources().getInteger(17694723)) {
            str = getContext().getResources().getString(17039383);
        } else {
            str = NumberFormat.getIntegerInstance().format((long) this.mIcon.number);
        }
        this.mNumberText = str;
        int width = getWidth();
        int height = getHeight();
        Rect rect = new Rect();
        this.mNumberPain.getTextBounds(str, 0, str.length(), rect);
        int i = rect.right - rect.left;
        int i2 = rect.bottom - rect.top;
        this.mNumberBackground.getPadding(rect);
        int i3 = rect.left + i + rect.right;
        if (i3 < this.mNumberBackground.getMinimumWidth()) {
            i3 = this.mNumberBackground.getMinimumWidth();
        }
        this.mNumberX = (width - rect.right) - (((i3 - rect.right) - rect.left) / 2);
        int i4 = rect.top + i2 + rect.bottom;
        if (i4 < this.mNumberBackground.getMinimumWidth()) {
            i4 = this.mNumberBackground.getMinimumWidth();
        }
        this.mNumberY = (height - rect.bottom) - ((((i4 - rect.top) - i2) - rect.bottom) / 2);
        this.mNumberBackground.setBounds(width - i3, height - i4, width, height);
    }

    private void setContentDescription(Notification notification) {
        if (notification != null) {
            String contentDescForNotification = contentDescForNotification(this.mContext, notification);
            if (!TextUtils.isEmpty(contentDescForNotification)) {
                setContentDescription(contentDescForNotification);
            }
        }
    }

    public String toString() {
        return "StatusBarIconView(slot=" + this.mSlot + " icon=" + this.mIcon + " notification=" + this.mNotification + NavigationBarInflaterView.KEY_CODE_END;
    }

    public StatusBarNotification getNotification() {
        return this.mNotification;
    }

    public String getSlot() {
        return this.mSlot;
    }

    public static String contentDescForNotification(Context context, Notification notification) {
        CharSequence charSequence;
        CharSequence charSequence2 = "";
        try {
            charSequence = Notification.Builder.recoverBuilder(context, notification).loadHeaderAppName();
        } catch (RuntimeException e) {
            Log.e(TAG, "Unable to recover builder", e);
            ApplicationInfo applicationInfo = (ApplicationInfo) notification.extras.getParcelable("android.appInfo", ApplicationInfo.class);
            charSequence = applicationInfo != null ? String.valueOf((Object) applicationInfo.loadLabel(context.getPackageManager())) : charSequence2;
        }
        CharSequence charSequence3 = notification.extras.getCharSequence(NotificationCompat.EXTRA_TITLE);
        CharSequence charSequence4 = notification.extras.getCharSequence(NotificationCompat.EXTRA_TEXT);
        CharSequence charSequence5 = notification.tickerText;
        if (TextUtils.equals(charSequence3, charSequence)) {
            charSequence3 = charSequence4;
        }
        if (!TextUtils.isEmpty(charSequence3)) {
            charSequence2 = charSequence3;
        } else if (!TextUtils.isEmpty(charSequence5)) {
            charSequence2 = charSequence5;
        }
        return context.getString(C1894R.string.accessibility_desc_notification_icon, new Object[]{charSequence, charSequence2});
    }

    public void setDecorColor(int i) {
        this.mDecorColor = i;
        updateDecorColor();
    }

    private void initializeDecorColor() {
        if (this.mNotification != null) {
            setDecorColor(getContext().getColor(this.mNightMode ? 17170977 : 17170978));
        }
    }

    private void updateDecorColor() {
        int interpolateColors = NotificationUtils.interpolateColors(this.mDecorColor, -1, this.mDozeAmount);
        if (this.mDotPaint.getColor() != interpolateColors) {
            this.mDotPaint.setColor(interpolateColors);
            if (this.mDotAppearAmount != 0.0f) {
                invalidate();
            }
        }
    }

    public void setStaticDrawableColor(int i) {
        this.mDrawableColor = i;
        setColorInternal(i);
        updateContrastedStaticColor();
        this.mIconColor = i;
        this.mDozer.setColor(i);
    }

    private void setColorInternal(int i) {
        this.mCurrentSetColor = i;
        updateIconColor();
    }

    private void updateIconColor() {
        if (this.mShowsConversation) {
            setColorFilter((ColorFilter) null);
        } else if (this.mCurrentSetColor != 0) {
            if (this.mMatrixColorFilter == null) {
                this.mMatrix = new float[20];
                this.mMatrixColorFilter = new ColorMatrixColorFilter(this.mMatrix);
            }
            updateTintMatrix(this.mMatrix, NotificationUtils.interpolateColors(this.mCurrentSetColor, -1, this.mDozeAmount), this.mDozeAmount * DARK_ALPHA_BOOST);
            this.mMatrixColorFilter.setColorMatrixArray(this.mMatrix);
            setColorFilter((ColorFilter) null);
            setColorFilter(this.mMatrixColorFilter);
        } else {
            this.mDozer.updateGrayscale((ImageView) this, this.mDozeAmount);
        }
    }

    private static void updateTintMatrix(float[] fArr, int i, float f) {
        Arrays.fill(fArr, 0.0f);
        fArr[4] = (float) Color.red(i);
        fArr[9] = (float) Color.green(i);
        fArr[14] = (float) Color.blue(i);
        fArr[18] = (((float) Color.alpha(i)) / 255.0f) + f;
    }

    public void setIconColor(int i, boolean z) {
        if (this.mIconColor != i) {
            this.mIconColor = i;
            ValueAnimator valueAnimator = this.mColorAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            int i2 = this.mCurrentSetColor;
            if (i2 != i) {
                if (!z || i2 == 0) {
                    setColorInternal(i);
                    return;
                }
                this.mAnimationStartColor = i2;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
                this.mColorAnimator = ofFloat;
                ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                this.mColorAnimator.setDuration(100);
                this.mColorAnimator.addUpdateListener(this.mColorUpdater);
                this.mColorAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        ValueAnimator unused = StatusBarIconView.this.mColorAnimator = null;
                        int unused2 = StatusBarIconView.this.mAnimationStartColor = 0;
                    }
                });
                this.mColorAnimator.start();
            }
        }
    }

    public int getStaticDrawableColor() {
        return this.mDrawableColor;
    }

    /* access modifiers changed from: package-private */
    public int getContrastedStaticDrawableColor(int i) {
        if (this.mCachedContrastBackgroundColor != i) {
            this.mCachedContrastBackgroundColor = i;
            updateContrastedStaticColor();
        }
        return this.mContrastedDrawableColor;
    }

    private void updateContrastedStaticColor() {
        if (Color.alpha(this.mCachedContrastBackgroundColor) != 255) {
            this.mContrastedDrawableColor = this.mDrawableColor;
            return;
        }
        int i = this.mDrawableColor;
        if (!ContrastColorUtil.satisfiesTextContrast(this.mCachedContrastBackgroundColor, i)) {
            float[] fArr = new float[3];
            ColorUtils.colorToHSL(this.mDrawableColor, fArr);
            if (fArr[1] < 0.2f) {
                i = 0;
            }
            i = ContrastColorUtil.resolveContrastColor(this.mContext, i, this.mCachedContrastBackgroundColor, !ContrastColorUtil.isColorLight(this.mCachedContrastBackgroundColor));
        }
        this.mContrastedDrawableColor = i;
    }

    public void setVisibleState(int i) {
        setVisibleState(i, true, (Runnable) null);
    }

    public void setVisibleState(int i, boolean z) {
        setVisibleState(i, z, (Runnable) null);
    }

    public void setVisibleState(int i, boolean z, Runnable runnable) {
        setVisibleState(i, z, runnable, 0);
    }

    public void setVisibleState(int i, boolean z, Runnable runnable, long j) {
        float f;
        boolean z2;
        int i2 = i;
        final Runnable runnable2 = runnable;
        boolean z3 = false;
        if (i2 != this.mVisibleState) {
            this.mVisibleState = i2;
            ObjectAnimator objectAnimator = this.mIconAppearAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            ObjectAnimator objectAnimator2 = this.mDotAnimator;
            if (objectAnimator2 != null) {
                objectAnimator2.cancel();
            }
            if (z) {
                Interpolator interpolator = Interpolators.FAST_OUT_LINEAR_IN;
                if (i2 == 0) {
                    interpolator = Interpolators.LINEAR_OUT_SLOW_IN;
                    f = 1.0f;
                } else {
                    f = 0.0f;
                }
                float iconAppearAmount = getIconAppearAmount();
                long j2 = 100;
                if (f != iconAppearAmount) {
                    ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, ICON_APPEAR_AMOUNT, new float[]{iconAppearAmount, f});
                    this.mIconAppearAnimator = ofFloat;
                    ofFloat.setInterpolator(interpolator);
                    this.mIconAppearAnimator.setDuration(j == 0 ? 100 : j);
                    this.mIconAppearAnimator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            ObjectAnimator unused = StatusBarIconView.this.mIconAppearAnimator = null;
                            StatusBarIconView.this.runRunnable(runnable2);
                        }
                    });
                    this.mIconAppearAnimator.start();
                    z2 = true;
                } else {
                    z2 = false;
                }
                float f2 = i2 == 0 ? 2.0f : 0.0f;
                Interpolator interpolator2 = Interpolators.FAST_OUT_LINEAR_IN;
                if (i2 == 1) {
                    interpolator2 = Interpolators.LINEAR_OUT_SLOW_IN;
                    f2 = 1.0f;
                }
                float dotAppearAmount = getDotAppearAmount();
                if (f2 != dotAppearAmount) {
                    ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(this, DOT_APPEAR_AMOUNT, new float[]{dotAppearAmount, f2});
                    this.mDotAnimator = ofFloat2;
                    ofFloat2.setInterpolator(interpolator2);
                    ObjectAnimator objectAnimator3 = this.mDotAnimator;
                    if (j != 0) {
                        j2 = j;
                    }
                    objectAnimator3.setDuration(j2);
                    final boolean z4 = !z2;
                    this.mDotAnimator.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animator) {
                            ObjectAnimator unused = StatusBarIconView.this.mDotAnimator = null;
                            if (z4) {
                                StatusBarIconView.this.runRunnable(runnable2);
                            }
                        }
                    });
                    this.mDotAnimator.start();
                    z3 = true;
                } else {
                    z3 = z2;
                }
            } else {
                setIconAppearAmount(i2 == 0 ? 1.0f : 0.0f);
                setDotAppearAmount(i2 == 1 ? 1.0f : i2 == 0 ? 2.0f : 0.0f);
            }
        }
        if (!z3) {
            runRunnable(runnable2);
        }
    }

    /* access modifiers changed from: private */
    public void runRunnable(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public void setIconAppearAmount(float f) {
        if (this.mIconAppearAmount != f) {
            this.mIconAppearAmount = f;
            invalidate();
        }
    }

    public float getIconAppearAmount() {
        return this.mIconAppearAmount;
    }

    public int getVisibleState() {
        return this.mVisibleState;
    }

    public void setDotAppearAmount(float f) {
        if (this.mDotAppearAmount != f) {
            this.mDotAppearAmount = f;
            invalidate();
        }
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        OnVisibilityChangedListener onVisibilityChangedListener = this.mOnVisibilityChangedListener;
        if (onVisibilityChangedListener != null) {
            onVisibilityChangedListener.onVisibilityChanged(i);
        }
    }

    public float getDotAppearAmount() {
        return this.mDotAppearAmount;
    }

    public void setOnVisibilityChangedListener(OnVisibilityChangedListener onVisibilityChangedListener) {
        this.mOnVisibilityChangedListener = onVisibilityChangedListener;
    }

    public void setDozing(boolean z, boolean z2, long j) {
        this.mDozer.setDozing(new StatusBarIconView$$ExternalSyntheticLambda1(this), z, z2, j, this);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setDozing$1$com-android-systemui-statusbar-StatusBarIconView */
    public /* synthetic */ void mo39075x56d089d2(Float f) {
        this.mDozeAmount = f.floatValue();
        updateDecorColor();
        updateIconColor();
        updateAllowAnimation();
    }

    private void updateAllowAnimation() {
        float f = this.mDozeAmount;
        if (f == 0.0f || f == 1.0f) {
            setAllowAnimation(f == 0.0f);
        }
    }

    public void getDrawingRect(Rect rect) {
        super.getDrawingRect(rect);
        float translationX = getTranslationX();
        float translationY = getTranslationY();
        rect.left = (int) (((float) rect.left) + translationX);
        rect.right = (int) (((float) rect.right) + translationX);
        rect.top = (int) (((float) rect.top) + translationY);
        rect.bottom = (int) (((float) rect.bottom) + translationY);
    }

    public void setIsInShelf(boolean z) {
        this.mIsInShelf = z;
    }

    public boolean isInShelf() {
        return this.mIsInShelf;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        Runnable runnable = this.mLayoutRunnable;
        if (runnable != null) {
            runnable.run();
            this.mLayoutRunnable = null;
        }
        updatePivot();
    }

    private void updatePivot() {
        if (isLayoutRtl()) {
            setPivotX(((this.mIconScale + 1.0f) / 2.0f) * ((float) getWidth()));
        } else {
            setPivotX(((1.0f - this.mIconScale) / 2.0f) * ((float) getWidth()));
        }
        setPivotY((((float) getHeight()) - (this.mIconScale * ((float) getWidth()))) / 2.0f);
    }

    public void executeOnLayout(Runnable runnable) {
        this.mLayoutRunnable = runnable;
    }

    public void setDismissed() {
        this.mDismissed = true;
        Runnable runnable = this.mOnDismissListener;
        if (runnable != null) {
            runnable.run();
        }
    }

    public boolean isDismissed() {
        return this.mDismissed;
    }

    public void setOnDismissListener(Runnable runnable) {
        this.mOnDismissListener = runnable;
    }

    public void onDarkChanged(ArrayList<Rect> arrayList, float f, int i) {
        int tint = DarkIconDispatcher.getTint(arrayList, this, i);
        setImageTintList(ColorStateList.valueOf(tint));
        setDecorColor(tint);
    }

    public boolean isIconVisible() {
        StatusBarIcon statusBarIcon = this.mIcon;
        return statusBarIcon != null && statusBarIcon.visible;
    }

    public boolean isIconBlocked() {
        return this.mBlocked;
    }

    public void setIncreasedSize(boolean z) {
        this.mIncreasedSize = z;
        maybeUpdateIconScaleDimens();
    }

    public void setShowsConversation(boolean z) {
        if (this.mShowsConversation != z) {
            this.mShowsConversation = z;
            updateIconColor();
        }
    }

    public boolean showsConversation() {
        return this.mShowsConversation;
    }
}
