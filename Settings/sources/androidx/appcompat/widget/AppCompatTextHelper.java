package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.LocaleList;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.TextView;
import androidx.appcompat.R$styleable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;
import androidx.core.widget.AutoSizeableTextView;
import androidx.core.widget.TextViewCompat;
import java.lang.ref.WeakReference;
import java.util.Locale;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class AppCompatTextHelper {
    private boolean mAsyncFontPending;
    private final AppCompatTextViewAutoSizeHelper mAutoSizeTextHelper;
    private TintInfo mDrawableBottomTint;
    private TintInfo mDrawableEndTint;
    private TintInfo mDrawableLeftTint;
    private TintInfo mDrawableRightTint;
    private TintInfo mDrawableStartTint;
    private TintInfo mDrawableTint;
    private TintInfo mDrawableTopTint;
    private Typeface mFontTypeface;
    private final TextView mView;
    private int mStyle = 0;
    private int mFontWeight = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AppCompatTextHelper(TextView textView) {
        this.mView = textView;
        this.mAutoSizeTextHelper = new AppCompatTextViewAutoSizeHelper(textView);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:104:0x021a  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x022b  */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0268  */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0277  */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0286  */
    /* JADX WARN: Removed duplicated region for block: B:123:0x0295  */
    /* JADX WARN: Removed duplicated region for block: B:126:0x02a4  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x02b3  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x02cd  */
    /* JADX WARN: Removed duplicated region for block: B:135:0x02de  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x0306  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x030d  */
    /* JADX WARN: Removed duplicated region for block: B:142:0x0314  */
    /* JADX WARN: Removed duplicated region for block: B:145:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:146:0x02ee  */
    /* JADX WARN: Removed duplicated region for block: B:147:0x02b9  */
    /* JADX WARN: Removed duplicated region for block: B:148:0x02aa  */
    /* JADX WARN: Removed duplicated region for block: B:149:0x029b  */
    /* JADX WARN: Removed duplicated region for block: B:150:0x028c  */
    /* JADX WARN: Removed duplicated region for block: B:151:0x027d  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x026e  */
    /* JADX WARN: Removed duplicated region for block: B:157:0x0109  */
    /* JADX WARN: Removed duplicated region for block: B:161:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0104  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0172  */
    /* JADX WARN: Removed duplicated region for block: B:67:0x017a  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x018d  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x01b7  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01be  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x01c5 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x01ce  */
    /* JADX WARN: Removed duplicated region for block: B:94:0x01e2  */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01e9  */
    @SuppressLint({"NewApi"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public void loadFromAttributes(AttributeSet attributeSet, int i) {
        boolean z;
        boolean z2;
        String str;
        ColorStateList colorStateList;
        String str2;
        ColorStateList colorStateList2;
        ColorStateList colorStateList3;
        String str3;
        int i2;
        int i3;
        String str4;
        AppCompatDrawableManager appCompatDrawableManager;
        Typeface typeface;
        TintTypedArray obtainStyledAttributes;
        int i4;
        int i5;
        int i6;
        int dimensionPixelSize;
        int dimensionPixelSize2;
        int dimensionPixelSize3;
        int[] autoSizeTextAvailableSizes;
        Context context = this.mView.getContext();
        AppCompatDrawableManager appCompatDrawableManager2 = AppCompatDrawableManager.get();
        int[] iArr = R$styleable.AppCompatTextHelper;
        TintTypedArray obtainStyledAttributes2 = TintTypedArray.obtainStyledAttributes(context, attributeSet, iArr, i, 0);
        TextView textView = this.mView;
        ViewCompat.saveAttributeDataForStyleable(textView, textView.getContext(), iArr, attributeSet, obtainStyledAttributes2.getWrappedTypeArray(), i, 0);
        int resourceId = obtainStyledAttributes2.getResourceId(R$styleable.AppCompatTextHelper_android_textAppearance, -1);
        int i7 = R$styleable.AppCompatTextHelper_android_drawableLeft;
        if (obtainStyledAttributes2.hasValue(i7)) {
            this.mDrawableLeftTint = createTintInfo(context, appCompatDrawableManager2, obtainStyledAttributes2.getResourceId(i7, 0));
        }
        int i8 = R$styleable.AppCompatTextHelper_android_drawableTop;
        if (obtainStyledAttributes2.hasValue(i8)) {
            this.mDrawableTopTint = createTintInfo(context, appCompatDrawableManager2, obtainStyledAttributes2.getResourceId(i8, 0));
        }
        int i9 = R$styleable.AppCompatTextHelper_android_drawableRight;
        if (obtainStyledAttributes2.hasValue(i9)) {
            this.mDrawableRightTint = createTintInfo(context, appCompatDrawableManager2, obtainStyledAttributes2.getResourceId(i9, 0));
        }
        int i10 = R$styleable.AppCompatTextHelper_android_drawableBottom;
        if (obtainStyledAttributes2.hasValue(i10)) {
            this.mDrawableBottomTint = createTintInfo(context, appCompatDrawableManager2, obtainStyledAttributes2.getResourceId(i10, 0));
        }
        int i11 = Build.VERSION.SDK_INT;
        if (i11 >= 17) {
            int i12 = R$styleable.AppCompatTextHelper_android_drawableStart;
            if (obtainStyledAttributes2.hasValue(i12)) {
                this.mDrawableStartTint = createTintInfo(context, appCompatDrawableManager2, obtainStyledAttributes2.getResourceId(i12, 0));
            }
            int i13 = R$styleable.AppCompatTextHelper_android_drawableEnd;
            if (obtainStyledAttributes2.hasValue(i13)) {
                this.mDrawableEndTint = createTintInfo(context, appCompatDrawableManager2, obtainStyledAttributes2.getResourceId(i13, 0));
            }
        }
        obtainStyledAttributes2.recycle();
        boolean z3 = this.mView.getTransformationMethod() instanceof PasswordTransformationMethod;
        if (resourceId != -1) {
            TintTypedArray obtainStyledAttributes3 = TintTypedArray.obtainStyledAttributes(context, resourceId, R$styleable.TextAppearance);
            if (!z3) {
                int i14 = R$styleable.TextAppearance_textAllCaps;
                if (obtainStyledAttributes3.hasValue(i14)) {
                    z = obtainStyledAttributes3.getBoolean(i14, false);
                    z2 = true;
                    updateTypefaceAndStyle(context, obtainStyledAttributes3);
                    if (i11 >= 23) {
                        int i15 = R$styleable.TextAppearance_android_textColor;
                        colorStateList = obtainStyledAttributes3.hasValue(i15) ? obtainStyledAttributes3.getColorStateList(i15) : null;
                        int i16 = R$styleable.TextAppearance_android_textColorHint;
                        colorStateList2 = obtainStyledAttributes3.hasValue(i16) ? obtainStyledAttributes3.getColorStateList(i16) : null;
                        int i17 = R$styleable.TextAppearance_android_textColorLink;
                        if (obtainStyledAttributes3.hasValue(i17)) {
                            colorStateList3 = obtainStyledAttributes3.getColorStateList(i17);
                            int i18 = R$styleable.TextAppearance_textLocale;
                            str2 = obtainStyledAttributes3.hasValue(i18) ? obtainStyledAttributes3.getString(i18) : null;
                            if (i11 >= 26) {
                                int i19 = R$styleable.TextAppearance_fontVariationSettings;
                                if (obtainStyledAttributes3.hasValue(i19)) {
                                    str = obtainStyledAttributes3.getString(i19);
                                    obtainStyledAttributes3.recycle();
                                }
                            }
                            str = null;
                            obtainStyledAttributes3.recycle();
                        }
                    } else {
                        colorStateList = null;
                        colorStateList2 = null;
                    }
                    colorStateList3 = null;
                    int i182 = R$styleable.TextAppearance_textLocale;
                    if (obtainStyledAttributes3.hasValue(i182)) {
                    }
                    if (i11 >= 26) {
                    }
                    str = null;
                    obtainStyledAttributes3.recycle();
                }
            }
            z = false;
            z2 = false;
            updateTypefaceAndStyle(context, obtainStyledAttributes3);
            if (i11 >= 23) {
            }
            colorStateList3 = null;
            int i1822 = R$styleable.TextAppearance_textLocale;
            if (obtainStyledAttributes3.hasValue(i1822)) {
            }
            if (i11 >= 26) {
            }
            str = null;
            obtainStyledAttributes3.recycle();
        } else {
            z = false;
            z2 = false;
            str = null;
            colorStateList = null;
            str2 = null;
            colorStateList2 = null;
            colorStateList3 = null;
        }
        TintTypedArray obtainStyledAttributes4 = TintTypedArray.obtainStyledAttributes(context, attributeSet, R$styleable.TextAppearance, i, 0);
        if (!z3) {
            int i20 = R$styleable.TextAppearance_textAllCaps;
            if (obtainStyledAttributes4.hasValue(i20)) {
                str3 = str;
                z = obtainStyledAttributes4.getBoolean(i20, false);
                i2 = 23;
                z2 = true;
                if (i11 < i2) {
                    int i21 = R$styleable.TextAppearance_android_textColor;
                    if (obtainStyledAttributes4.hasValue(i21)) {
                        colorStateList = obtainStyledAttributes4.getColorStateList(i21);
                    }
                    int i22 = R$styleable.TextAppearance_android_textColorHint;
                    if (obtainStyledAttributes4.hasValue(i22)) {
                        colorStateList2 = obtainStyledAttributes4.getColorStateList(i22);
                    }
                    int i23 = R$styleable.TextAppearance_android_textColorLink;
                    if (obtainStyledAttributes4.hasValue(i23)) {
                        colorStateList3 = obtainStyledAttributes4.getColorStateList(i23);
                    }
                }
                i3 = R$styleable.TextAppearance_textLocale;
                if (obtainStyledAttributes4.hasValue(i3)) {
                    str2 = obtainStyledAttributes4.getString(i3);
                }
                if (i11 >= 26) {
                    int i24 = R$styleable.TextAppearance_fontVariationSettings;
                    if (obtainStyledAttributes4.hasValue(i24)) {
                        str4 = obtainStyledAttributes4.getString(i24);
                        if (i11 >= 28) {
                            int i25 = R$styleable.TextAppearance_android_textSize;
                            if (obtainStyledAttributes4.hasValue(i25)) {
                                appCompatDrawableManager = appCompatDrawableManager2;
                                if (obtainStyledAttributes4.getDimensionPixelSize(i25, -1) == 0) {
                                    this.mView.setTextSize(0, 0.0f);
                                }
                                updateTypefaceAndStyle(context, obtainStyledAttributes4);
                                obtainStyledAttributes4.recycle();
                                if (colorStateList != null) {
                                    this.mView.setTextColor(colorStateList);
                                }
                                if (colorStateList2 != null) {
                                    this.mView.setHintTextColor(colorStateList2);
                                }
                                if (colorStateList3 != null) {
                                    this.mView.setLinkTextColor(colorStateList3);
                                }
                                if (!z3 && z2) {
                                    setAllCaps(z);
                                }
                                typeface = this.mFontTypeface;
                                if (typeface != null) {
                                    if (this.mFontWeight == -1) {
                                        this.mView.setTypeface(typeface, this.mStyle);
                                    } else {
                                        this.mView.setTypeface(typeface);
                                    }
                                }
                                if (str4 != null) {
                                    this.mView.setFontVariationSettings(str4);
                                }
                                if (str2 != null) {
                                    if (i11 >= 24) {
                                        this.mView.setTextLocales(LocaleList.forLanguageTags(str2));
                                    } else if (i11 >= 21) {
                                        this.mView.setTextLocale(Locale.forLanguageTag(str2.substring(0, str2.indexOf(44))));
                                    }
                                }
                                this.mAutoSizeTextHelper.loadFromAttributes(attributeSet, i);
                                if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && this.mAutoSizeTextHelper.getAutoSizeTextType() != 0) {
                                    autoSizeTextAvailableSizes = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
                                    if (autoSizeTextAvailableSizes.length > 0) {
                                        if (this.mView.getAutoSizeStepGranularity() != -1.0f) {
                                            this.mView.setAutoSizeTextTypeUniformWithConfiguration(this.mAutoSizeTextHelper.getAutoSizeMinTextSize(), this.mAutoSizeTextHelper.getAutoSizeMaxTextSize(), this.mAutoSizeTextHelper.getAutoSizeStepGranularity(), 0);
                                        } else {
                                            this.mView.setAutoSizeTextTypeUniformWithPresetSizes(autoSizeTextAvailableSizes, 0);
                                        }
                                    }
                                }
                                obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R$styleable.AppCompatTextView);
                                int resourceId2 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableLeftCompat, -1);
                                AppCompatDrawableManager appCompatDrawableManager3 = appCompatDrawableManager;
                                Drawable drawable = resourceId2 == -1 ? appCompatDrawableManager3.getDrawable(context, resourceId2) : null;
                                int resourceId3 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableTopCompat, -1);
                                Drawable drawable2 = resourceId3 == -1 ? appCompatDrawableManager3.getDrawable(context, resourceId3) : null;
                                int resourceId4 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableRightCompat, -1);
                                Drawable drawable3 = resourceId4 == -1 ? appCompatDrawableManager3.getDrawable(context, resourceId4) : null;
                                int resourceId5 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableBottomCompat, -1);
                                Drawable drawable4 = resourceId5 == -1 ? appCompatDrawableManager3.getDrawable(context, resourceId5) : null;
                                int resourceId6 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableStartCompat, -1);
                                Drawable drawable5 = resourceId6 == -1 ? appCompatDrawableManager3.getDrawable(context, resourceId6) : null;
                                int resourceId7 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableEndCompat, -1);
                                setCompoundDrawables(drawable, drawable2, drawable3, drawable4, drawable5, resourceId7 == -1 ? appCompatDrawableManager3.getDrawable(context, resourceId7) : null);
                                i4 = R$styleable.AppCompatTextView_drawableTint;
                                if (obtainStyledAttributes.hasValue(i4)) {
                                    TextViewCompat.setCompoundDrawableTintList(this.mView, obtainStyledAttributes.getColorStateList(i4));
                                }
                                i5 = R$styleable.AppCompatTextView_drawableTintMode;
                                if (!obtainStyledAttributes.hasValue(i5)) {
                                    i6 = -1;
                                    TextViewCompat.setCompoundDrawableTintMode(this.mView, DrawableUtils.parseTintMode(obtainStyledAttributes.getInt(i5, -1), null));
                                } else {
                                    i6 = -1;
                                }
                                dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.AppCompatTextView_firstBaselineToTopHeight, i6);
                                dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.AppCompatTextView_lastBaselineToBottomHeight, i6);
                                dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.AppCompatTextView_lineHeight, i6);
                                obtainStyledAttributes.recycle();
                                if (dimensionPixelSize != i6) {
                                    TextViewCompat.setFirstBaselineToTopHeight(this.mView, dimensionPixelSize);
                                }
                                if (dimensionPixelSize2 != i6) {
                                    TextViewCompat.setLastBaselineToBottomHeight(this.mView, dimensionPixelSize2);
                                }
                                if (dimensionPixelSize3 != i6) {
                                    return;
                                }
                                TextViewCompat.setLineHeight(this.mView, dimensionPixelSize3);
                                return;
                            }
                        }
                        appCompatDrawableManager = appCompatDrawableManager2;
                        updateTypefaceAndStyle(context, obtainStyledAttributes4);
                        obtainStyledAttributes4.recycle();
                        if (colorStateList != null) {
                        }
                        if (colorStateList2 != null) {
                        }
                        if (colorStateList3 != null) {
                        }
                        if (!z3) {
                            setAllCaps(z);
                        }
                        typeface = this.mFontTypeface;
                        if (typeface != null) {
                        }
                        if (str4 != null) {
                        }
                        if (str2 != null) {
                        }
                        this.mAutoSizeTextHelper.loadFromAttributes(attributeSet, i);
                        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
                            autoSizeTextAvailableSizes = this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
                            if (autoSizeTextAvailableSizes.length > 0) {
                            }
                        }
                        obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R$styleable.AppCompatTextView);
                        int resourceId22 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableLeftCompat, -1);
                        AppCompatDrawableManager appCompatDrawableManager32 = appCompatDrawableManager;
                        if (resourceId22 == -1) {
                        }
                        int resourceId32 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableTopCompat, -1);
                        if (resourceId32 == -1) {
                        }
                        int resourceId42 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableRightCompat, -1);
                        if (resourceId42 == -1) {
                        }
                        int resourceId52 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableBottomCompat, -1);
                        if (resourceId52 == -1) {
                        }
                        int resourceId62 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableStartCompat, -1);
                        if (resourceId62 == -1) {
                        }
                        int resourceId72 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableEndCompat, -1);
                        setCompoundDrawables(drawable, drawable2, drawable3, drawable4, drawable5, resourceId72 == -1 ? appCompatDrawableManager32.getDrawable(context, resourceId72) : null);
                        i4 = R$styleable.AppCompatTextView_drawableTint;
                        if (obtainStyledAttributes.hasValue(i4)) {
                        }
                        i5 = R$styleable.AppCompatTextView_drawableTintMode;
                        if (!obtainStyledAttributes.hasValue(i5)) {
                        }
                        dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.AppCompatTextView_firstBaselineToTopHeight, i6);
                        dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.AppCompatTextView_lastBaselineToBottomHeight, i6);
                        dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.AppCompatTextView_lineHeight, i6);
                        obtainStyledAttributes.recycle();
                        if (dimensionPixelSize != i6) {
                        }
                        if (dimensionPixelSize2 != i6) {
                        }
                        if (dimensionPixelSize3 != i6) {
                        }
                    }
                }
                str4 = str3;
                if (i11 >= 28) {
                }
                appCompatDrawableManager = appCompatDrawableManager2;
                updateTypefaceAndStyle(context, obtainStyledAttributes4);
                obtainStyledAttributes4.recycle();
                if (colorStateList != null) {
                }
                if (colorStateList2 != null) {
                }
                if (colorStateList3 != null) {
                }
                if (!z3) {
                }
                typeface = this.mFontTypeface;
                if (typeface != null) {
                }
                if (str4 != null) {
                }
                if (str2 != null) {
                }
                this.mAutoSizeTextHelper.loadFromAttributes(attributeSet, i);
                if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
                }
                obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R$styleable.AppCompatTextView);
                int resourceId222 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableLeftCompat, -1);
                AppCompatDrawableManager appCompatDrawableManager322 = appCompatDrawableManager;
                if (resourceId222 == -1) {
                }
                int resourceId322 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableTopCompat, -1);
                if (resourceId322 == -1) {
                }
                int resourceId422 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableRightCompat, -1);
                if (resourceId422 == -1) {
                }
                int resourceId522 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableBottomCompat, -1);
                if (resourceId522 == -1) {
                }
                int resourceId622 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableStartCompat, -1);
                if (resourceId622 == -1) {
                }
                int resourceId722 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableEndCompat, -1);
                setCompoundDrawables(drawable, drawable2, drawable3, drawable4, drawable5, resourceId722 == -1 ? appCompatDrawableManager322.getDrawable(context, resourceId722) : null);
                i4 = R$styleable.AppCompatTextView_drawableTint;
                if (obtainStyledAttributes.hasValue(i4)) {
                }
                i5 = R$styleable.AppCompatTextView_drawableTintMode;
                if (!obtainStyledAttributes.hasValue(i5)) {
                }
                dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.AppCompatTextView_firstBaselineToTopHeight, i6);
                dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.AppCompatTextView_lastBaselineToBottomHeight, i6);
                dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.AppCompatTextView_lineHeight, i6);
                obtainStyledAttributes.recycle();
                if (dimensionPixelSize != i6) {
                }
                if (dimensionPixelSize2 != i6) {
                }
                if (dimensionPixelSize3 != i6) {
                }
            }
        }
        str3 = str;
        i2 = 23;
        if (i11 < i2) {
        }
        i3 = R$styleable.TextAppearance_textLocale;
        if (obtainStyledAttributes4.hasValue(i3)) {
        }
        if (i11 >= 26) {
        }
        str4 = str3;
        if (i11 >= 28) {
        }
        appCompatDrawableManager = appCompatDrawableManager2;
        updateTypefaceAndStyle(context, obtainStyledAttributes4);
        obtainStyledAttributes4.recycle();
        if (colorStateList != null) {
        }
        if (colorStateList2 != null) {
        }
        if (colorStateList3 != null) {
        }
        if (!z3) {
        }
        typeface = this.mFontTypeface;
        if (typeface != null) {
        }
        if (str4 != null) {
        }
        if (str2 != null) {
        }
        this.mAutoSizeTextHelper.loadFromAttributes(attributeSet, i);
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
        }
        obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R$styleable.AppCompatTextView);
        int resourceId2222 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableLeftCompat, -1);
        AppCompatDrawableManager appCompatDrawableManager3222 = appCompatDrawableManager;
        if (resourceId2222 == -1) {
        }
        int resourceId3222 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableTopCompat, -1);
        if (resourceId3222 == -1) {
        }
        int resourceId4222 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableRightCompat, -1);
        if (resourceId4222 == -1) {
        }
        int resourceId5222 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableBottomCompat, -1);
        if (resourceId5222 == -1) {
        }
        int resourceId6222 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableStartCompat, -1);
        if (resourceId6222 == -1) {
        }
        int resourceId7222 = obtainStyledAttributes.getResourceId(R$styleable.AppCompatTextView_drawableEndCompat, -1);
        setCompoundDrawables(drawable, drawable2, drawable3, drawable4, drawable5, resourceId7222 == -1 ? appCompatDrawableManager3222.getDrawable(context, resourceId7222) : null);
        i4 = R$styleable.AppCompatTextView_drawableTint;
        if (obtainStyledAttributes.hasValue(i4)) {
        }
        i5 = R$styleable.AppCompatTextView_drawableTintMode;
        if (!obtainStyledAttributes.hasValue(i5)) {
        }
        dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(R$styleable.AppCompatTextView_firstBaselineToTopHeight, i6);
        dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.AppCompatTextView_lastBaselineToBottomHeight, i6);
        dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(R$styleable.AppCompatTextView_lineHeight, i6);
        obtainStyledAttributes.recycle();
        if (dimensionPixelSize != i6) {
        }
        if (dimensionPixelSize2 != i6) {
        }
        if (dimensionPixelSize3 != i6) {
        }
    }

    private void updateTypefaceAndStyle(Context context, TintTypedArray tintTypedArray) {
        String string;
        this.mStyle = tintTypedArray.getInt(R$styleable.TextAppearance_android_textStyle, this.mStyle);
        int i = Build.VERSION.SDK_INT;
        boolean z = false;
        if (i >= 28) {
            int i2 = tintTypedArray.getInt(R$styleable.TextAppearance_android_textFontWeight, -1);
            this.mFontWeight = i2;
            if (i2 != -1) {
                this.mStyle = (this.mStyle & 2) | 0;
            }
        }
        int i3 = R$styleable.TextAppearance_android_fontFamily;
        if (tintTypedArray.hasValue(i3) || tintTypedArray.hasValue(R$styleable.TextAppearance_fontFamily)) {
            this.mFontTypeface = null;
            int i4 = R$styleable.TextAppearance_fontFamily;
            if (tintTypedArray.hasValue(i4)) {
                i3 = i4;
            }
            final int i5 = this.mFontWeight;
            final int i6 = this.mStyle;
            if (!context.isRestricted()) {
                final WeakReference weakReference = new WeakReference(this.mView);
                try {
                    Typeface font = tintTypedArray.getFont(i3, this.mStyle, new ResourcesCompat.FontCallback() { // from class: androidx.appcompat.widget.AppCompatTextHelper.1
                        @Override // androidx.core.content.res.ResourcesCompat.FontCallback
                        public void onFontRetrievalFailed(int i7) {
                        }

                        @Override // androidx.core.content.res.ResourcesCompat.FontCallback
                        public void onFontRetrieved(Typeface typeface) {
                            int i7;
                            if (Build.VERSION.SDK_INT >= 28 && (i7 = i5) != -1) {
                                typeface = Typeface.create(typeface, i7, (i6 & 2) != 0);
                            }
                            AppCompatTextHelper.this.onAsyncTypefaceReceived(weakReference, typeface);
                        }
                    });
                    if (font != null) {
                        if (i >= 28 && this.mFontWeight != -1) {
                            this.mFontTypeface = Typeface.create(Typeface.create(font, 0), this.mFontWeight, (this.mStyle & 2) != 0);
                        } else {
                            this.mFontTypeface = font;
                        }
                    }
                    this.mAsyncFontPending = this.mFontTypeface == null;
                } catch (Resources.NotFoundException | UnsupportedOperationException unused) {
                }
            }
            if (this.mFontTypeface != null || (string = tintTypedArray.getString(i3)) == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= 28 && this.mFontWeight != -1) {
                Typeface create = Typeface.create(string, 0);
                int i7 = this.mFontWeight;
                if ((this.mStyle & 2) != 0) {
                    z = true;
                }
                this.mFontTypeface = Typeface.create(create, i7, z);
                return;
            }
            this.mFontTypeface = Typeface.create(string, this.mStyle);
            return;
        }
        int i8 = R$styleable.TextAppearance_android_typeface;
        if (!tintTypedArray.hasValue(i8)) {
            return;
        }
        this.mAsyncFontPending = false;
        int i9 = tintTypedArray.getInt(i8, 1);
        if (i9 == 1) {
            this.mFontTypeface = Typeface.SANS_SERIF;
        } else if (i9 == 2) {
            this.mFontTypeface = Typeface.SERIF;
        } else if (i9 != 3) {
        } else {
            this.mFontTypeface = Typeface.MONOSPACE;
        }
    }

    void onAsyncTypefaceReceived(WeakReference<TextView> weakReference, final Typeface typeface) {
        if (this.mAsyncFontPending) {
            this.mFontTypeface = typeface;
            final TextView textView = weakReference.get();
            if (textView == null) {
                return;
            }
            if (ViewCompat.isAttachedToWindow(textView)) {
                final int i = this.mStyle;
                textView.post(new Runnable() { // from class: androidx.appcompat.widget.AppCompatTextHelper.2
                    @Override // java.lang.Runnable
                    public void run() {
                        textView.setTypeface(typeface, i);
                    }
                });
                return;
            }
            textView.setTypeface(typeface, this.mStyle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSetTextAppearance(Context context, int i) {
        String string;
        ColorStateList colorStateList;
        ColorStateList colorStateList2;
        ColorStateList colorStateList3;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, i, R$styleable.TextAppearance);
        int i2 = R$styleable.TextAppearance_textAllCaps;
        if (obtainStyledAttributes.hasValue(i2)) {
            setAllCaps(obtainStyledAttributes.getBoolean(i2, false));
        }
        int i3 = Build.VERSION.SDK_INT;
        if (i3 < 23) {
            int i4 = R$styleable.TextAppearance_android_textColor;
            if (obtainStyledAttributes.hasValue(i4) && (colorStateList3 = obtainStyledAttributes.getColorStateList(i4)) != null) {
                this.mView.setTextColor(colorStateList3);
            }
            int i5 = R$styleable.TextAppearance_android_textColorLink;
            if (obtainStyledAttributes.hasValue(i5) && (colorStateList2 = obtainStyledAttributes.getColorStateList(i5)) != null) {
                this.mView.setLinkTextColor(colorStateList2);
            }
            int i6 = R$styleable.TextAppearance_android_textColorHint;
            if (obtainStyledAttributes.hasValue(i6) && (colorStateList = obtainStyledAttributes.getColorStateList(i6)) != null) {
                this.mView.setHintTextColor(colorStateList);
            }
        }
        int i7 = R$styleable.TextAppearance_android_textSize;
        if (obtainStyledAttributes.hasValue(i7) && obtainStyledAttributes.getDimensionPixelSize(i7, -1) == 0) {
            this.mView.setTextSize(0, 0.0f);
        }
        updateTypefaceAndStyle(context, obtainStyledAttributes);
        if (i3 >= 26) {
            int i8 = R$styleable.TextAppearance_fontVariationSettings;
            if (obtainStyledAttributes.hasValue(i8) && (string = obtainStyledAttributes.getString(i8)) != null) {
                this.mView.setFontVariationSettings(string);
            }
        }
        obtainStyledAttributes.recycle();
        Typeface typeface = this.mFontTypeface;
        if (typeface != null) {
            this.mView.setTypeface(typeface, this.mStyle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAllCaps(boolean z) {
        this.mView.setAllCaps(z);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onSetCompoundDrawables() {
        applyCompoundDrawablesTints();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void applyCompoundDrawablesTints() {
        if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
            Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            applyCompoundDrawableTint(compoundDrawables[0], this.mDrawableLeftTint);
            applyCompoundDrawableTint(compoundDrawables[1], this.mDrawableTopTint);
            applyCompoundDrawableTint(compoundDrawables[2], this.mDrawableRightTint);
            applyCompoundDrawableTint(compoundDrawables[3], this.mDrawableBottomTint);
        }
        if (Build.VERSION.SDK_INT >= 17) {
            if (this.mDrawableStartTint == null && this.mDrawableEndTint == null) {
                return;
            }
            Drawable[] compoundDrawablesRelative = this.mView.getCompoundDrawablesRelative();
            applyCompoundDrawableTint(compoundDrawablesRelative[0], this.mDrawableStartTint);
            applyCompoundDrawableTint(compoundDrawablesRelative[2], this.mDrawableEndTint);
        }
    }

    private void applyCompoundDrawableTint(Drawable drawable, TintInfo tintInfo) {
        if (drawable == null || tintInfo == null) {
            return;
        }
        AppCompatDrawableManager.tintDrawable(drawable, tintInfo, this.mView.getDrawableState());
    }

    private static TintInfo createTintInfo(Context context, AppCompatDrawableManager appCompatDrawableManager, int i) {
        ColorStateList tintList = appCompatDrawableManager.getTintList(context, i);
        if (tintList != null) {
            TintInfo tintInfo = new TintInfo();
            tintInfo.mHasTintList = true;
            tintInfo.mTintList = tintList;
            return tintInfo;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (!AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            autoSizeText();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTextSize(int i, float f) {
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE || isAutoSizeEnabled()) {
            return;
        }
        setTextSizeInternal(i, f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void autoSizeText() {
        this.mAutoSizeTextHelper.autoSizeText();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isAutoSizeEnabled() {
        return this.mAutoSizeTextHelper.isAutoSizeEnabled();
    }

    private void setTextSizeInternal(int i, float f) {
        this.mAutoSizeTextHelper.setTextSizeInternal(i, f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAutoSizeTextTypeWithDefaults(int i) {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeWithDefaults(i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAutoSizeTextTypeUniformWithConfiguration(int i, int i2, int i3, int i4) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithConfiguration(i, i2, i3, i4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setAutoSizeTextTypeUniformWithPresetSizes(int[] iArr, int i) throws IllegalArgumentException {
        this.mAutoSizeTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAutoSizeTextType() {
        return this.mAutoSizeTextHelper.getAutoSizeTextType();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAutoSizeStepGranularity() {
        return this.mAutoSizeTextHelper.getAutoSizeStepGranularity();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAutoSizeMinTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMinTextSize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getAutoSizeMaxTextSize() {
        return this.mAutoSizeTextHelper.getAutoSizeMaxTextSize();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] getAutoSizeTextAvailableSizes() {
        return this.mAutoSizeTextHelper.getAutoSizeTextAvailableSizes();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ColorStateList getCompoundDrawableTintList() {
        TintInfo tintInfo = this.mDrawableTint;
        if (tintInfo != null) {
            return tintInfo.mTintList;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCompoundDrawableTintList(ColorStateList colorStateList) {
        if (this.mDrawableTint == null) {
            this.mDrawableTint = new TintInfo();
        }
        TintInfo tintInfo = this.mDrawableTint;
        tintInfo.mTintList = colorStateList;
        tintInfo.mHasTintList = colorStateList != null;
        setCompoundTints();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public PorterDuff.Mode getCompoundDrawableTintMode() {
        TintInfo tintInfo = this.mDrawableTint;
        if (tintInfo != null) {
            return tintInfo.mTintMode;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setCompoundDrawableTintMode(PorterDuff.Mode mode) {
        if (this.mDrawableTint == null) {
            this.mDrawableTint = new TintInfo();
        }
        TintInfo tintInfo = this.mDrawableTint;
        tintInfo.mTintMode = mode;
        tintInfo.mHasTintMode = mode != null;
        setCompoundTints();
    }

    private void setCompoundTints() {
        TintInfo tintInfo = this.mDrawableTint;
        this.mDrawableLeftTint = tintInfo;
        this.mDrawableTopTint = tintInfo;
        this.mDrawableRightTint = tintInfo;
        this.mDrawableBottomTint = tintInfo;
        this.mDrawableStartTint = tintInfo;
        this.mDrawableEndTint = tintInfo;
    }

    private void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5, Drawable drawable6) {
        int i = Build.VERSION.SDK_INT;
        if (i >= 17 && (drawable5 != null || drawable6 != null)) {
            Drawable[] compoundDrawablesRelative = this.mView.getCompoundDrawablesRelative();
            TextView textView = this.mView;
            if (drawable5 == null) {
                drawable5 = compoundDrawablesRelative[0];
            }
            if (drawable2 == null) {
                drawable2 = compoundDrawablesRelative[1];
            }
            if (drawable6 == null) {
                drawable6 = compoundDrawablesRelative[2];
            }
            if (drawable4 == null) {
                drawable4 = compoundDrawablesRelative[3];
            }
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable5, drawable2, drawable6, drawable4);
        } else if (drawable == null && drawable2 == null && drawable3 == null && drawable4 == null) {
        } else {
            if (i >= 17) {
                Drawable[] compoundDrawablesRelative2 = this.mView.getCompoundDrawablesRelative();
                if (compoundDrawablesRelative2[0] != null || compoundDrawablesRelative2[2] != null) {
                    TextView textView2 = this.mView;
                    Drawable drawable7 = compoundDrawablesRelative2[0];
                    if (drawable2 == null) {
                        drawable2 = compoundDrawablesRelative2[1];
                    }
                    Drawable drawable8 = compoundDrawablesRelative2[2];
                    if (drawable4 == null) {
                        drawable4 = compoundDrawablesRelative2[3];
                    }
                    textView2.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable7, drawable2, drawable8, drawable4);
                    return;
                }
            }
            Drawable[] compoundDrawables = this.mView.getCompoundDrawables();
            TextView textView3 = this.mView;
            if (drawable == null) {
                drawable = compoundDrawables[0];
            }
            if (drawable2 == null) {
                drawable2 = compoundDrawables[1];
            }
            if (drawable3 == null) {
                drawable3 = compoundDrawables[2];
            }
            if (drawable4 == null) {
                drawable4 = compoundDrawables[3];
            }
            textView3.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void populateSurroundingTextIfNeeded(TextView textView, InputConnection inputConnection, EditorInfo editorInfo) {
        if (Build.VERSION.SDK_INT >= 30 || inputConnection == null) {
            return;
        }
        EditorInfoCompat.setInitialSurroundingText(editorInfo, textView.getText());
    }
}
