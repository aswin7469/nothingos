package com.android.systemui.p012qs.tileimpl;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Trace;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.animation.LaunchableView;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.plugins.p011qs.QSIconView;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.plugins.p011qs.QSTileView;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSTileHostEx;
import com.nothing.systemui.p024qs.tileimpl.QSTileViewImplEx;
import com.nothing.systemui.util.NTLogUtil;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000Ô\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u001f\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\r\n\u0002\b\u0002\n\u0002\u0010\u0015\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\t\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u001c\b\u0016\u0018\u0000 Æ\u00012\u00020\u00012\u00020\u00022\u00020\u0003:\u0002Æ\u0001B!\b\u0007\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\b\u0010w\u001a\u00020\tH\u0014J\u0010\u0010x\u001a\u00020y2\u0006\u0010z\u001a\u00020{H\u0002J\b\u0010|\u001a\u00020yH\u0016J\b\u0010}\u001a\u00020yH\u0002J\b\u0010~\u001a\u00020\u001aH\u0016J\b\u0010\u001a\u00020yH\u0014J\u0011\u0010\u0001\u001a\u00020\u000e2\u0006\u0010z\u001a\u00020\u000eH\u0016J\u0011\u0010\u0001\u001a\u00020\u000e2\u0006\u0010z\u001a\u00020\u000eH\u0002J\t\u0010\u0001\u001a\u00020\u000eH\u0016J\t\u0010\u0001\u001a\u00020\u0007H\u0016J\n\u0010\u0001\u001a\u00030\u0001H\u0016J\t\u0010G\u001a\u00030\u0001H\u0016J\u0011\u0010\u0001\u001a\u00020\u000e2\u0006\u0010z\u001a\u00020\u000eH\u0016J\t\u0010M\u001a\u00030\u0001H\u0016J\n\u0010\u0001\u001a\u00030\u0001H\u0016J\t\u0010a\u001a\u00030\u0001H\u0016J\u0011\u0010\u0001\u001a\u00020\u000e2\u0006\u0010z\u001a\u00020\u000eH\u0016J\u0011\u0010\u0001\u001a\u00020\u00102\u0006\u0010z\u001a\u00020{H\u0002J\t\u0010\u0001\u001a\u00020\u000eH\u0016J\t\u0010\u0001\u001a\u00020\u000eH\u0016J\u0011\u0010\u0001\u001a\u00020y2\u0006\u0010z\u001a\u00020{H\u0014J\t\u0010\u0001\u001a\u00020\tH\u0016J!\u0010\u0001\u001a\u00020y2\n\u0010\u0001\u001a\u0005\u0018\u00010\u00012\n\u0010\u0001\u001a\u0005\u0018\u00010\u0001H\u0002J\u0013\u0010\u0001\u001a\u00020y2\b\u0010\u0001\u001a\u00030\u0001H\u0016J\u0011\u0010\u0001\u001a\u00020y2\u0006\u0010z\u001a\u00020{H\u0002J\u0015\u0010\u0001\u001a\u00020y2\n\u0010\u0001\u001a\u0005\u0018\u00010\u0001H\u0014J\u0013\u0010\u0001\u001a\u00020y2\b\u0010\u0001\u001a\u00030\u0001H\u0016J\u0013\u0010\u0001\u001a\u00020y2\b\u0010\u0001\u001a\u00030\u0001H\u0016J6\u0010\u0001\u001a\u00020y2\u0007\u0010 \u0001\u001a\u00020\t2\u0007\u0010¡\u0001\u001a\u00020\u000e2\u0007\u0010¢\u0001\u001a\u00020\u000e2\u0007\u0010£\u0001\u001a\u00020\u000e2\u0007\u0010¤\u0001\u001a\u00020\u000eH\u0014J\u001b\u0010¥\u0001\u001a\u00020y2\u0007\u0010¦\u0001\u001a\u00020\u000e2\u0007\u0010§\u0001\u001a\u00020\u000eH\u0014J\u0011\u0010¨\u0001\u001a\u00020y2\u0006\u0010z\u001a\u00020{H\u0016J\u0013\u0010©\u0001\u001a\u00020\t2\b\u0010ª\u0001\u001a\u00030«\u0001H\u0016J\t\u0010¬\u0001\u001a\u00020yH\u0016J-\u0010­\u0001\u001a\u00020y2\u0007\u0010®\u0001\u001a\u00020\u000e2\u0007\u0010¯\u0001\u001a\u00020\u000e2\u0007\u0010°\u0001\u001a\u00020\u000e2\u0007\u0010±\u0001\u001a\u00020\u000eH\u0002J\u0012\u0010²\u0001\u001a\u00020y2\u0007\u0010³\u0001\u001a\u00020\u000eH\u0002J\u0012\u0010´\u0001\u001a\u00020y2\u0007\u0010µ\u0001\u001a\u00020\tH\u0016J\u0012\u0010¶\u0001\u001a\u00020y2\u0007\u0010³\u0001\u001a\u00020\u000eH\u0016J\u0012\u0010·\u0001\u001a\u00020y2\u0007\u0010³\u0001\u001a\u00020\u000eH\u0002J\u0012\u0010¸\u0001\u001a\u00020y2\u0007\u0010¹\u0001\u001a\u00020\u000eH\u0016J\u0012\u0010º\u0001\u001a\u00020y2\u0007\u0010³\u0001\u001a\u00020\u000eH\u0002J\u0012\u0010»\u0001\u001a\u00020y2\u0007\u0010¼\u0001\u001a\u00020\tH\u0016J\u0012\u0010½\u0001\u001a\u00020y2\u0007\u0010¾\u0001\u001a\u00020\u000eH\u0016J\u0012\u0010¿\u0001\u001a\u00020y2\u0007\u0010¾\u0001\u001a\u00020\u000eH\u0016J\t\u0010À\u0001\u001a\u00020\tH\u0016J\t\u0010Á\u0001\u001a\u00020\u0010H\u0016J\u0016\u0010Â\u0001\u001a\u00030\u00012\n\u0010Ã\u0001\u001a\u0005\u0018\u00010\u0001H\u0016J\t\u0010Ä\u0001\u001a\u00020yH\u0002J\t\u0010Å\u0001\u001a\u00020yH\u0016R\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X.¢\u0006\u0002\n\u0000R\u0011\u0010\b\u001a\u00020\t¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u00020\u000eX\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001a\u0010\u0019\u001a\u00020\u001aX.¢\u0006\u000e\n\u0000\u001a\u0004\b\u001b\u0010\u001c\"\u0004\b\u001d\u0010\u001eR\u001a\u0010\u001f\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b \u0010\u0018\"\u0004\b!\u0010\"R\u001a\u0010#\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b$\u0010\u0018\"\u0004\b%\u0010\"R\u0014\u0010&\u001a\u00020\u000eX\u0004¢\u0006\b\n\u0000\u001a\u0004\b'\u0010\u0018R\u0014\u0010(\u001a\u00020\u000eX\u0004¢\u0006\b\n\u0000\u001a\u0004\b)\u0010\u0018R\u0014\u0010*\u001a\u00020\u000eX\u0004¢\u0006\b\n\u0000\u001a\u0004\b+\u0010\u0018R\u0014\u0010,\u001a\u00020\u000eX\u0004¢\u0006\b\n\u0000\u001a\u0004\b-\u0010\u0018R\u0014\u0010.\u001a\u00020\u000eX\u0004¢\u0006\b\n\u0000\u001a\u0004\b/\u0010\u0018R\u0014\u00100\u001a\u00020\u000eX\u0004¢\u0006\b\n\u0000\u001a\u0004\b1\u0010\u0018R\u001a\u00102\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b3\u0010\u0018\"\u0004\b4\u0010\"R\u001a\u00105\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b6\u0010\u0018\"\u0004\b7\u0010\"R\u000e\u00108\u001a\u00020\u0013X.¢\u0006\u0002\n\u0000R\u0016\u00109\u001a\n ;*\u0004\u0018\u00010:0:X\u000e¢\u0006\u0002\n\u0000R$\u0010=\u001a\u00020\u000e2\u0006\u0010<\u001a\u00020\u000e@VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b>\u0010\u0018\"\u0004\b?\u0010\"R\u001a\u0010@\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b@\u0010\u0015\"\u0004\bA\u0010BR\u001a\u0010C\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bC\u0010\u0015\"\u0004\bD\u0010BR\u001a\u0010E\u001a\u00020FX.¢\u0006\u000e\n\u0000\u001a\u0004\bG\u0010H\"\u0004\bI\u0010JR\u001a\u0010K\u001a\u00020LX.¢\u0006\u000e\n\u0000\u001a\u0004\bM\u0010N\"\u0004\bO\u0010PR\u001a\u0010Q\u001a\u00020\u000eX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bR\u0010\u0018\"\u0004\bS\u0010\"R\u0010\u0010T\u001a\u0004\u0018\u00010UX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010V\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010W\u001a\u00020XX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010Y\u001a\u00020\u000eX\u000e¢\u0006\u0002\n\u0000R\u001a\u0010Z\u001a\u00020[X.¢\u0006\u000e\n\u0000\u001a\u0004\b\\\u0010]\"\u0004\b^\u0010_R\u001a\u0010`\u001a\u00020FX.¢\u0006\u000e\n\u0000\u001a\u0004\ba\u0010H\"\u0004\bb\u0010JR\u001a\u0010c\u001a\u00020\tX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bd\u0010\u0015\"\u0004\be\u0010BR\u001a\u0010f\u001a\u00020gX.¢\u0006\u000e\n\u0000\u001a\u0004\bh\u0010i\"\u0004\bj\u0010kR\u000e\u0010l\u001a\u00020mX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010n\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R$\u0010p\u001a\u00020o2\u0006\u0010<\u001a\u00020o@VX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\bq\u0010r\"\u0004\bs\u0010tR\u0010\u0010u\u001a\u0004\u0018\u00010UX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010v\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000¨\u0006Ç\u0001"}, mo65043d2 = {"Lcom/android/systemui/qs/tileimpl/QSTileViewImpl;", "Lcom/android/systemui/plugins/qs/QSTileView;", "Lcom/android/systemui/qs/tileimpl/HeightOverrideable;", "Lcom/android/systemui/animation/LaunchableView;", "context", "Landroid/content/Context;", "_icon", "Lcom/android/systemui/plugins/qs/QSIconView;", "collapsed", "", "(Landroid/content/Context;Lcom/android/systemui/plugins/qs/QSIconView;Z)V", "get_icon", "()Lcom/android/systemui/plugins/qs/QSIconView;", "_position", "", "accessibilityClass", "", "blockVisibilityChanges", "chevronView", "Landroid/widget/ImageView;", "getCollapsed", "()Z", "colorActive", "getColorActive", "()I", "colorBackgroundDrawable", "Landroid/graphics/drawable/Drawable;", "getColorBackgroundDrawable", "()Landroid/graphics/drawable/Drawable;", "setColorBackgroundDrawable", "(Landroid/graphics/drawable/Drawable;)V", "colorInactive", "getColorInactive", "setColorInactive", "(I)V", "colorInactiveCircle", "getColorInactiveCircle", "setColorInactiveCircle", "colorLabelActive", "getColorLabelActive", "colorLabelInactive", "getColorLabelInactive", "colorLabelUnavailable", "getColorLabelUnavailable", "colorSecondaryLabelActive", "getColorSecondaryLabelActive", "colorSecondaryLabelInactive", "getColorSecondaryLabelInactive", "colorSecondaryLabelUnavailable", "getColorSecondaryLabelUnavailable", "colorUnavailable", "getColorUnavailable", "setColorUnavailable", "colorUnavailableCircle", "getColorUnavailableCircle", "setColorUnavailableCircle", "customDrawableView", "ex", "Lcom/nothing/systemui/qs/tileimpl/QSTileViewImplEx;", "kotlin.jvm.PlatformType", "value", "heightOverride", "getHeightOverride", "setHeightOverride", "isSignalOrBt", "setSignalOrBt", "(Z)V", "isTesla", "setTesla", "label", "Landroid/widget/TextView;", "getLabel", "()Landroid/widget/TextView;", "setLabel", "(Landroid/widget/TextView;)V", "labelContainer", "Lcom/android/systemui/qs/tileimpl/IgnorableChildLinearLayout;", "getLabelContainer", "()Lcom/android/systemui/qs/tileimpl/IgnorableChildLinearLayout;", "setLabelContainer", "(Lcom/android/systemui/qs/tileimpl/IgnorableChildLinearLayout;)V", "lastState", "getLastState", "setLastState", "lastStateDescription", "", "lastVisibility", "locInScreen", "", "paintColor", "ripple", "Landroid/graphics/drawable/RippleDrawable;", "getRipple", "()Landroid/graphics/drawable/RippleDrawable;", "setRipple", "(Landroid/graphics/drawable/RippleDrawable;)V", "secondaryLabel", "getSecondaryLabel", "setSecondaryLabel", "showRippleEffect", "getShowRippleEffect", "setShowRippleEffect", "sideView", "Landroid/view/ViewGroup;", "getSideView", "()Landroid/view/ViewGroup;", "setSideView", "(Landroid/view/ViewGroup;)V", "singleAnimator", "Landroid/animation/ValueAnimator;", "skipTintBt", "", "squishinessFraction", "getSquishinessFraction", "()F", "setSquishinessFraction", "(F)V", "stateDescriptionDeltas", "tileState", "animationsEnabled", "cacheTileState", "", "state", "Lcom/android/systemui/plugins/qs/QSTile$State;", "createAndAddLabels", "createAndAddSideView", "createTileBackground", "dealAndAddIcon", "getBackgroundColorForState", "getChevronColorForState", "getDetailY", "getIcon", "getIconWithBackground", "Landroid/view/View;", "getLabelColorForState", "getSecondaryIcon", "getSecondaryLabelColorForState", "getStateText", "getTileGravity", "getTileOrientation", "handleStateChanged", "hasOverlappingRendering", "init", "click", "Landroid/view/View$OnClickListener;", "longClick", "Landroid/view/View$OnLongClickListener;", "tile", "Lcom/android/systemui/plugins/qs/QSTile;", "loadSideViewDrawableIfNecessary", "onConfigurationChanged", "newConfig", "Landroid/content/res/Configuration;", "onInitializeAccessibilityEvent", "event", "Landroid/view/accessibility/AccessibilityEvent;", "onInitializeAccessibilityNodeInfo", "info", "Landroid/view/accessibility/AccessibilityNodeInfo;", "onLayout", "changed", "l", "t", "r", "b", "onMeasure", "widthMeasureSpec", "heightMeasureSpec", "onStateChanged", "onTouchEvent", "ev", "Landroid/view/MotionEvent;", "resetOverride", "setAllColors", "backgroundColor", "labelColor", "secondaryLabelColor", "chevronColor", "setChevronColor", "color", "setClickable", "clickable", "setColor", "setLabelColor", "setPosition", "position", "setSecondaryLabelColor", "setShouldBlockVisibilityChanges", "block", "setTransitionVisibility", "visibility", "setVisibility", "shouldInterceptClick", "toString", "updateAccessibilityOrder", "previousView", "updateHeight", "updateResources", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.android.systemui.qs.tileimpl.QSTileViewImpl */
/* compiled from: QSTileViewImpl.kt */
public class QSTileViewImpl extends QSTileView implements HeightOverrideable, LaunchableView {
    private static final String BACKGROUND_NAME = "background";
    private static final String CHEVRON_NAME = "chevron";
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final int INVALID = -1;
    private static final String LABEL_NAME = "label";
    private static final String SECONDARY_LABEL_NAME = "secondaryLabel";
    public static final String TILE_STATE_RES_PREFIX = "tile_states_";
    public static final float UNAVAILABLE_ALPHA = 0.3f;
    public Map<Integer, View> _$_findViewCache;
    private final QSIconView _icon;
    private int _position;
    private String accessibilityClass;
    private boolean blockVisibilityChanges;
    private ImageView chevronView;
    private final boolean collapsed;
    private final int colorActive;
    protected Drawable colorBackgroundDrawable;
    private int colorInactive;
    private int colorInactiveCircle;
    private final int colorLabelActive;
    private final int colorLabelInactive;
    private final int colorLabelUnavailable;
    private final int colorSecondaryLabelActive;
    private final int colorSecondaryLabelInactive;
    private final int colorSecondaryLabelUnavailable;
    private int colorUnavailable;
    private int colorUnavailableCircle;
    private ImageView customDrawableView;

    /* renamed from: ex */
    private QSTileViewImplEx f332ex;
    private int heightOverride;
    private boolean isSignalOrBt;
    private boolean isTesla;
    protected TextView label;
    protected IgnorableChildLinearLayout labelContainer;
    private int lastState;
    private CharSequence lastStateDescription;
    private int lastVisibility;
    private final int[] locInScreen;
    private int paintColor;
    protected RippleDrawable ripple;
    protected TextView secondaryLabel;
    private boolean showRippleEffect;
    protected ViewGroup sideView;
    private final ValueAnimator singleAnimator;
    private boolean skipTintBt;
    private float squishinessFraction;
    private CharSequence stateDescriptionDeltas;
    private boolean tileState;

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public QSTileViewImpl(Context context, QSIconView qSIconView) {
        this(context, qSIconView, false, 4, (DefaultConstructorMarker) null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(qSIconView, "_icon");
    }

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

    public int getTileGravity() {
        return 8388627;
    }

    public int getTileOrientation() {
        return 0;
    }

    public boolean hasOverlappingRendering() {
        return false;
    }

    public boolean shouldInterceptClick() {
        return false;
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public QSTileViewImpl(Context context, QSIconView qSIconView, boolean z) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(qSIconView, "_icon");
        this._$_findViewCache = new LinkedHashMap();
        this._icon = qSIconView;
        this.collapsed = z;
        this._position = -1;
        this.heightOverride = -1;
        this.squishinessFraction = 1.0f;
        this.colorActive = Utils.getColorAttrDefaultColor(context, 17956900);
        this.colorInactive = context.getColor(C1894R.C1895color.off_state_color);
        this.colorUnavailable = Utils.applyAlpha(0.3f, getColorInactive());
        this.colorInactiveCircle = context.getColor(C1894R.C1895color.circle_off_state_color);
        this.colorUnavailableCircle = Utils.applyAlpha(0.3f, getColorInactiveCircle());
        this.colorLabelActive = Utils.getColorAttrDefaultColor(context, 17957103);
        this.colorLabelInactive = Utils.getColorAttrDefaultColor(context, 16842806);
        this.colorLabelUnavailable = Utils.applyAlpha(0.3f, getColorLabelInactive());
        this.colorSecondaryLabelActive = Utils.getColorAttrDefaultColor(context, 16842810);
        this.colorSecondaryLabelInactive = Utils.getColorAttrDefaultColor(context, 16842808);
        this.colorSecondaryLabelUnavailable = Utils.applyAlpha(0.3f, getColorSecondaryLabelInactive());
        this.f332ex = (QSTileViewImplEx) NTDependencyEx.get(QSTileViewImplEx.class);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(350);
        valueAnimator.addUpdateListener(new QSTileViewImpl$$ExternalSyntheticLambda1(this));
        this.singleAnimator = valueAnimator;
        this.lastState = -1;
        this.locInScreen = new int[2];
        setId(QSTileView.generateViewId());
        setOrientation(0);
        setGravity(8388627);
        setOrientation(getTileOrientation());
        setGravity(getTileGravity());
        setImportantForAccessibility(1);
        setClipChildren(false);
        setClipToPadding(false);
        setFocusable(true);
        setBackground(createTileBackground());
        setColor(getBackgroundColorForState(2));
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.qs_tile_padding);
        setPaddingRelative(getResources().getDimensionPixelSize(C1894R.dimen.qs_tile_start_padding), dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        dealAndAddIcon();
        createAndAddLabels();
        createAndAddSideView();
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    public /* synthetic */ QSTileViewImpl(Context context, QSIconView qSIconView, boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, qSIconView, (i & 4) != 0 ? false : z);
    }

    public final QSIconView get_icon() {
        return this._icon;
    }

    public final boolean getCollapsed() {
        return this.collapsed;
    }

    @Metadata(mo65042d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0007\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0016\u0010\n\u001a\u00020\u00048\u0000XT¢\u0006\b\n\u0000\u0012\u0004\b\u000b\u0010\u0002R\u000e\u0010\f\u001a\u00020\rXT¢\u0006\u0002\n\u0000¨\u0006\u000e"}, mo65043d2 = {"Lcom/android/systemui/qs/tileimpl/QSTileViewImpl$Companion;", "", "()V", "BACKGROUND_NAME", "", "CHEVRON_NAME", "INVALID", "", "LABEL_NAME", "SECONDARY_LABEL_NAME", "TILE_STATE_RES_PREFIX", "getTILE_STATE_RES_PREFIX$SystemUI_nothingRelease$annotations", "UNAVAILABLE_ALPHA", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.android.systemui.qs.tileimpl.QSTileViewImpl$Companion */
    /* compiled from: QSTileViewImpl.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ void getTILE_STATE_RES_PREFIX$SystemUI_nothingRelease$annotations() {
        }

        private Companion() {
        }
    }

    public void setPosition(int i) {
        this._position = i;
    }

    public int getHeightOverride() {
        return this.heightOverride;
    }

    public void setHeightOverride(int i) {
        if (this.heightOverride != i) {
            this.heightOverride = i;
            updateHeight();
        }
    }

    public float getSquishinessFraction() {
        return this.squishinessFraction;
    }

    public void setSquishinessFraction(float f) {
        if (!(this.squishinessFraction == f)) {
            this.squishinessFraction = f;
            updateHeight();
        }
    }

    public int getColorActive() {
        return this.colorActive;
    }

    public int getColorInactive() {
        return this.colorInactive;
    }

    public void setColorInactive(int i) {
        this.colorInactive = i;
    }

    public int getColorUnavailable() {
        return this.colorUnavailable;
    }

    public void setColorUnavailable(int i) {
        this.colorUnavailable = i;
    }

    public int getColorInactiveCircle() {
        return this.colorInactiveCircle;
    }

    public void setColorInactiveCircle(int i) {
        this.colorInactiveCircle = i;
    }

    public int getColorUnavailableCircle() {
        return this.colorUnavailableCircle;
    }

    public void setColorUnavailableCircle(int i) {
        this.colorUnavailableCircle = i;
    }

    public int getColorLabelActive() {
        return this.colorLabelActive;
    }

    public int getColorLabelInactive() {
        return this.colorLabelInactive;
    }

    public int getColorLabelUnavailable() {
        return this.colorLabelUnavailable;
    }

    public int getColorSecondaryLabelActive() {
        return this.colorSecondaryLabelActive;
    }

    public int getColorSecondaryLabelInactive() {
        return this.colorSecondaryLabelInactive;
    }

    public int getColorSecondaryLabelUnavailable() {
        return this.colorSecondaryLabelUnavailable;
    }

    /* access modifiers changed from: protected */
    /* renamed from: getLabel  reason: collision with other method in class */
    public final TextView m2969getLabel() {
        TextView textView = this.label;
        if (textView != null) {
            return textView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("label");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setLabel(TextView textView) {
        Intrinsics.checkNotNullParameter(textView, "<set-?>");
        this.label = textView;
    }

    /* access modifiers changed from: protected */
    /* renamed from: getSecondaryLabel  reason: collision with other method in class */
    public final TextView m2971getSecondaryLabel() {
        TextView textView = this.secondaryLabel;
        if (textView != null) {
            return textView;
        }
        Intrinsics.throwUninitializedPropertyAccessException(SECONDARY_LABEL_NAME);
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setSecondaryLabel(TextView textView) {
        Intrinsics.checkNotNullParameter(textView, "<set-?>");
        this.secondaryLabel = textView;
    }

    /* access modifiers changed from: protected */
    /* renamed from: getLabelContainer  reason: collision with other method in class */
    public final IgnorableChildLinearLayout m2970getLabelContainer() {
        IgnorableChildLinearLayout ignorableChildLinearLayout = this.labelContainer;
        if (ignorableChildLinearLayout != null) {
            return ignorableChildLinearLayout;
        }
        Intrinsics.throwUninitializedPropertyAccessException("labelContainer");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setLabelContainer(IgnorableChildLinearLayout ignorableChildLinearLayout) {
        Intrinsics.checkNotNullParameter(ignorableChildLinearLayout, "<set-?>");
        this.labelContainer = ignorableChildLinearLayout;
    }

    public final boolean isSignalOrBt() {
        return this.isSignalOrBt;
    }

    public final void setSignalOrBt(boolean z) {
        this.isSignalOrBt = z;
    }

    public final boolean isTesla() {
        return this.isTesla;
    }

    public final void setTesla(boolean z) {
        this.isTesla = z;
    }

    /* access modifiers changed from: protected */
    public final ViewGroup getSideView() {
        ViewGroup viewGroup = this.sideView;
        if (viewGroup != null) {
            return viewGroup;
        }
        Intrinsics.throwUninitializedPropertyAccessException("sideView");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setSideView(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "<set-?>");
        this.sideView = viewGroup;
    }

    /* access modifiers changed from: protected */
    public final boolean getShowRippleEffect() {
        return this.showRippleEffect;
    }

    /* access modifiers changed from: protected */
    public final void setShowRippleEffect(boolean z) {
        this.showRippleEffect = z;
    }

    /* access modifiers changed from: protected */
    public final RippleDrawable getRipple() {
        RippleDrawable rippleDrawable = this.ripple;
        if (rippleDrawable != null) {
            return rippleDrawable;
        }
        Intrinsics.throwUninitializedPropertyAccessException("ripple");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setRipple(RippleDrawable rippleDrawable) {
        Intrinsics.checkNotNullParameter(rippleDrawable, "<set-?>");
        this.ripple = rippleDrawable;
    }

    /* access modifiers changed from: protected */
    public final Drawable getColorBackgroundDrawable() {
        Drawable drawable = this.colorBackgroundDrawable;
        if (drawable != null) {
            return drawable;
        }
        Intrinsics.throwUninitializedPropertyAccessException("colorBackgroundDrawable");
        return null;
    }

    /* access modifiers changed from: protected */
    public final void setColorBackgroundDrawable(Drawable drawable) {
        Intrinsics.checkNotNullParameter(drawable, "<set-?>");
        this.colorBackgroundDrawable = drawable;
    }

    /* access modifiers changed from: private */
    /* renamed from: singleAnimator$lambda-1$lambda-0  reason: not valid java name */
    public static final void m2968singleAnimator$lambda1$lambda0(QSTileViewImpl qSTileViewImpl, ValueAnimator valueAnimator) {
        Intrinsics.checkNotNullParameter(qSTileViewImpl, "this$0");
        Object animatedValue = valueAnimator.getAnimatedValue(BACKGROUND_NAME);
        if (animatedValue != null) {
            int intValue = ((Integer) animatedValue).intValue();
            Object animatedValue2 = valueAnimator.getAnimatedValue("label");
            if (animatedValue2 != null) {
                int intValue2 = ((Integer) animatedValue2).intValue();
                Object animatedValue3 = valueAnimator.getAnimatedValue(SECONDARY_LABEL_NAME);
                if (animatedValue3 != null) {
                    int intValue3 = ((Integer) animatedValue3).intValue();
                    Object animatedValue4 = valueAnimator.getAnimatedValue(CHEVRON_NAME);
                    if (animatedValue4 != null) {
                        qSTileViewImpl.setAllColors(intValue, intValue2, intValue3, ((Integer) animatedValue4).intValue());
                        return;
                    }
                    throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
                }
                throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
            }
            throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
        }
        throw new NullPointerException("null cannot be cast to non-null type kotlin.Int");
    }

    public final int getLastState() {
        return this.lastState;
    }

    public final void setLastState(int i) {
        this.lastState = i;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        Trace.traceBegin(4096, "QSTileViewImpl#onMeasure");
        super.onMeasure(i, i2);
        Trace.endSection();
    }

    public void resetOverride() {
        setHeightOverride(-1);
        updateHeight();
    }

    public void updateResources() {
        FontSizeUtils.updateFontSize(getLabel(), C1894R.dimen.qs_tile_text_size);
        FontSizeUtils.updateFontSize(getSecondaryLabel(), C1894R.dimen.qs_tile_text_size);
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(C1894R.dimen.qs_icon_size);
        ViewGroup.LayoutParams layoutParams = this._icon.getLayoutParams();
        layoutParams.height = dimensionPixelSize;
        layoutParams.width = dimensionPixelSize;
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(C1894R.dimen.qs_tile_padding);
        setPaddingRelative(getResources().getDimensionPixelSize(C1894R.dimen.qs_tile_start_padding), dimensionPixelSize2, dimensionPixelSize2, dimensionPixelSize2);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(C1894R.dimen.qs_label_container_margin);
        this.f332ex.applyLabelContainerMarginStart(getTileOrientation() == 0, getLabelContainer(), dimensionPixelSize3);
        ViewGroup.LayoutParams layoutParams2 = getSideView().getLayoutParams();
        if (layoutParams2 != null) {
            ((ViewGroup.MarginLayoutParams) layoutParams2).setMarginStart(dimensionPixelSize3);
            ImageView imageView = this.chevronView;
            ImageView imageView2 = null;
            if (imageView == null) {
                Intrinsics.throwUninitializedPropertyAccessException("chevronView");
                imageView = null;
            }
            ViewGroup.LayoutParams layoutParams3 = imageView.getLayoutParams();
            if (layoutParams3 != null) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams3;
                marginLayoutParams.height = dimensionPixelSize;
                marginLayoutParams.width = dimensionPixelSize;
                int dimensionPixelSize4 = getResources().getDimensionPixelSize(C1894R.dimen.qs_drawable_end_margin);
                ImageView imageView3 = this.customDrawableView;
                if (imageView3 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                } else {
                    imageView2 = imageView3;
                }
                ViewGroup.LayoutParams layoutParams4 = imageView2.getLayoutParams();
                if (layoutParams4 != null) {
                    ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) layoutParams4;
                    marginLayoutParams2.height = dimensionPixelSize;
                    marginLayoutParams2.setMarginEnd(dimensionPixelSize4);
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
            }
            throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
    }

    public void createAndAddLabels() {
        QSTileViewImplEx qSTileViewImplEx = this.f332ex;
        boolean z = getTileOrientation() == 0;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "context");
        setLabelContainer(qSTileViewImplEx.inflateLabelContainer(z, context, this));
        View requireViewById = getLabelContainer().requireViewById(C1894R.C1898id.tile_label);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "labelContainer.requireViewById(R.id.tile_label)");
        setLabel((TextView) requireViewById);
        View requireViewById2 = getLabelContainer().requireViewById(C1894R.C1898id.app_label);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "labelContainer.requireViewById(R.id.app_label)");
        setSecondaryLabel((TextView) requireViewById2);
        if (this.collapsed) {
            getLabelContainer().setIgnoreLastView(true);
            getLabelContainer().setForceUnspecifiedMeasure(true);
            getSecondaryLabel().setAlpha(0.0f);
        }
        setLabelColor(getLabelColorForState(2));
        setSecondaryLabelColor(getSecondaryLabelColorForState(2));
        addView(getLabelContainer());
    }

    private final void createAndAddSideView() {
        View inflate = LayoutInflater.from(getContext()).inflate(C1894R.layout.qs_tile_side_icon, this, false);
        if (inflate != null) {
            setSideView((ViewGroup) inflate);
            View requireViewById = getSideView().requireViewById(C1894R.C1898id.customDrawable);
            Intrinsics.checkNotNullExpressionValue(requireViewById, "sideView.requireViewById(R.id.customDrawable)");
            this.customDrawableView = (ImageView) requireViewById;
            View requireViewById2 = getSideView().requireViewById(C1894R.C1898id.chevron);
            Intrinsics.checkNotNullExpressionValue(requireViewById2, "sideView.requireViewById(R.id.chevron)");
            this.chevronView = (ImageView) requireViewById2;
            setChevronColor(getChevronColorForState(2));
            if (getTileOrientation() != 1) {
                addView(getSideView());
                return;
            }
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
    }

    public Drawable createTileBackground() {
        Drawable drawable = this.mContext.getDrawable(C1894R.C1896drawable.qs_tile_background);
        if (drawable != null) {
            setRipple((RippleDrawable) drawable);
            Drawable findDrawableByLayerId = getRipple().findDrawableByLayerId(C1894R.C1898id.background);
            Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ripple.findDrawableByLayerId(R.id.background)");
            setColorBackgroundDrawable(findDrawableByLayerId);
            return getColorBackgroundDrawable();
        }
        throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.RippleDrawable");
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateHeight();
    }

    private final void updateHeight() {
        int i;
        QSTileViewImplEx qSTileViewImplEx = this.f332ex;
        if (qSTileViewImplEx != null) {
            setBottom(qSTileViewImplEx.getBottom(getBottom(), getTop(), getHeightOverride()));
            return;
        }
        if (getHeightOverride() != -1) {
            i = getHeightOverride();
        } else {
            i = getMeasuredHeight();
        }
        setBottom(getTop() + ((int) (((float) i) * QSTileViewImplKt.constrainSquishiness(getSquishinessFraction()))));
        setScrollY((i - getHeight()) / 2);
    }

    public View updateAccessibilityOrder(View view) {
        setAccessibilityTraversalAfter(view != null ? view.getId() : 0);
        return this;
    }

    public QSIconView getIcon() {
        return this._icon;
    }

    public View getIconWithBackground() {
        return getIcon();
    }

    public void init(QSTile qSTile) {
        Intrinsics.checkNotNullParameter(qSTile, "tile");
        init(new QSTileViewImpl$$ExternalSyntheticLambda2(this, qSTile), new QSTileViewImpl$$ExternalSyntheticLambda3(this, qSTile));
        QSTileHostEx.Companion companion = QSTileHostEx.Companion;
        String tileSpec = qSTile.getTileSpec();
        Intrinsics.checkNotNullExpressionValue(tileSpec, "tile.tileSpec");
        boolean isSignalOrBtTile = companion.isSignalOrBtTile(tileSpec);
        this.isSignalOrBt = isSignalOrBtTile;
        this.f332ex.setCircleTileLabelIfNeeded(isSignalOrBtTile, getLabel(), getSecondaryLabel());
    }

    /* access modifiers changed from: private */
    /* renamed from: init$lambda-6  reason: not valid java name */
    public static final void m2965init$lambda6(QSTileViewImpl qSTileViewImpl, QSTile qSTile, View view) {
        Intrinsics.checkNotNullParameter(qSTileViewImpl, "this$0");
        Intrinsics.checkNotNullParameter(qSTile, "$tile");
        if (!qSTileViewImpl.shouldInterceptClick()) {
            qSTile.click(qSTileViewImpl);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: init$lambda-7  reason: not valid java name */
    public static final boolean m2966init$lambda7(QSTileViewImpl qSTileViewImpl, QSTile qSTile, View view) {
        Intrinsics.checkNotNullParameter(qSTileViewImpl, "this$0");
        Intrinsics.checkNotNullParameter(qSTile, "$tile");
        if (qSTileViewImpl.isTesla) {
            qSTileViewImpl.setTag(C1894R.C1898id.qs_tesla_tag, "tesla");
        }
        if (qSTileViewImpl.f332ex.isIntercepted()) {
            qSTileViewImpl.f332ex.resetIntercepted();
            NTLogUtil.m1686d("QSTileViewImpl", "Mistouch longClick");
            return false;
        }
        qSTile.longClick(qSTileViewImpl);
        return true;
    }

    private final void init(View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        setOnClickListener(onClickListener);
        setOnLongClickListener(onLongClickListener);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        Intrinsics.checkNotNullParameter(motionEvent, "ev");
        if (this.f332ex.onTouchEvent(motionEvent)) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public void onStateChanged(QSTile.State state) {
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        cacheTileState(state);
        post(new QSTileViewImpl$$ExternalSyntheticLambda0(this, state));
    }

    /* access modifiers changed from: private */
    /* renamed from: onStateChanged$lambda-8  reason: not valid java name */
    public static final void m2967onStateChanged$lambda8(QSTileViewImpl qSTileViewImpl, QSTile.State state) {
        Intrinsics.checkNotNullParameter(qSTileViewImpl, "this$0");
        Intrinsics.checkNotNullParameter(state, "$state");
        qSTileViewImpl.handleStateChanged(state);
    }

    public int getDetailY() {
        return getTop() + (getHeight() / 2);
    }

    public void setClickable(boolean z) {
        super.setClickable(z);
    }

    public View getLabelContainer() {
        return getLabelContainer();
    }

    public View getLabel() {
        return getLabel();
    }

    public View getSecondaryLabel() {
        return getSecondaryLabel();
    }

    public View getSecondaryIcon() {
        return getSideView();
    }

    public void setShouldBlockVisibilityChanges(boolean z) {
        this.blockVisibilityChanges = z;
        if (z) {
            this.lastVisibility = getVisibility();
        } else {
            setVisibility(this.lastVisibility);
        }
    }

    public void setVisibility(int i) {
        if (this.blockVisibilityChanges) {
            this.lastVisibility = i;
        } else {
            super.setVisibility(i);
        }
    }

    public void setTransitionVisibility(int i) {
        if (this.blockVisibilityChanges) {
            this.lastVisibility = i;
        } else {
            super.setTransitionVisibility(i);
        }
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Intrinsics.checkNotNullParameter(accessibilityEvent, NotificationCompat.CATEGORY_EVENT);
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (!TextUtils.isEmpty(this.accessibilityClass)) {
            accessibilityEvent.setClassName(this.accessibilityClass);
        }
        if (accessibilityEvent.getContentChangeTypes() == 64 && this.stateDescriptionDeltas != null) {
            accessibilityEvent.getText().add(this.stateDescriptionDeltas);
            this.stateDescriptionDeltas = null;
        }
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        Intrinsics.checkNotNullParameter(accessibilityNodeInfo, "info");
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setSelected(false);
        if (!TextUtils.isEmpty(this.accessibilityClass)) {
            accessibilityNodeInfo.setClassName(this.accessibilityClass);
            if (Intrinsics.areEqual((Object) Switch.class.getName(), (Object) this.accessibilityClass)) {
                accessibilityNodeInfo.setText(getResources().getString(this.tileState ? C1894R.string.switch_bar_on : C1894R.string.switch_bar_off));
                accessibilityNodeInfo.setChecked(this.tileState);
                accessibilityNodeInfo.setCheckable(true);
                if (isLongClickable()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_LONG_CLICK.getId(), getResources().getString(C1894R.string.accessibility_long_click_tile)));
                }
            }
        }
        if (this._position != -1) {
            accessibilityNodeInfo.setCollectionItemInfo(new AccessibilityNodeInfo.CollectionItemInfo(this._position, 1, 0, 1, false));
        }
    }

    public String toString() {
        StringBuilder append = new StringBuilder(getClass().getSimpleName()).append('[');
        append.append("locInScreen=(" + this.locInScreen[0] + ", " + this.locInScreen[1] + ')');
        append.append(", iconView=" + this._icon);
        append.append(", tileState=" + this.tileState);
        append.append(", lastState=" + this.lastState);
        append.append(", des=" + getContentDescription());
        append.append(", clickable=" + isClickable());
        append.append(", labelVis=" + getLabel().getVisibility());
        append.append(", labelText=" + getLabel().getText());
        append.append(", iconVis=" + getIcon().getVisibility());
        append.append(", ripple=" + this.showRippleEffect);
        append.append(", isSignalOrBt=" + this.isSignalOrBt);
        append.append(", bg=" + getBackground());
        append.append(", colorActive=" + Integer.toHexString(getColorActive()));
        append.append(", colorLabelActive=" + Integer.toHexString(getColorLabelActive()));
        append.append(", interceptClick=" + shouldInterceptClick());
        append.append(NavigationBarInflaterView.SIZE_MOD_END);
        String sb = append.toString();
        Intrinsics.checkNotNullExpressionValue(sb, "sb.toString()");
        return sb;
    }

    /* access modifiers changed from: protected */
    public void handleStateChanged(QSTile.State state) {
        String str;
        boolean z;
        Intrinsics.checkNotNullParameter(state, AuthDialog.KEY_BIOMETRIC_STATE);
        boolean animationsEnabled = animationsEnabled();
        this.showRippleEffect = false;
        if (state.icon != null) {
            state.icon.skipTintBt = this.skipTintBt;
        }
        setClickable(state.state != 0);
        setLongClickable(state.handlesLongClick);
        getIcon().setIcon(state, animationsEnabled);
        QSIconView icon = getIcon();
        QSTileViewImplEx qSTileViewImplEx = this.f332ex;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "context");
        icon.setCircleIconBgColor(qSTileViewImplEx.getCircleIconBackgroundColorForState(context, state.state, getColorActive(), getColorInactiveCircle(), getColorUnavailableCircle()));
        if (state.icon != null) {
            this.isTesla = state.icon.isTesla;
        }
        setContentDescription(state.contentDescription);
        StringBuilder sb = new StringBuilder();
        String stateText = getStateText(state);
        CharSequence charSequence = stateText;
        if (!TextUtils.isEmpty(charSequence)) {
            sb.append(stateText);
            if (TextUtils.isEmpty(state.secondaryLabel)) {
                state.secondaryLabel = charSequence;
            }
        }
        if (!TextUtils.isEmpty(state.stateDescription)) {
            sb.append(", ");
            sb.append(state.stateDescription);
            if (this.lastState != -1 && state.state == this.lastState && !Intrinsics.areEqual((Object) state.stateDescription, (Object) this.lastStateDescription)) {
                this.stateDescriptionDeltas = state.stateDescription;
            }
        }
        setStateDescription(sb.toString());
        this.lastStateDescription = state.stateDescription;
        ImageView imageView = null;
        if (state.state == 0) {
            String str2 = null;
            str = null;
        } else {
            str = state.expandedAccessibilityClassName;
        }
        this.accessibilityClass = str;
        if ((state instanceof QSTile.BooleanState) && this.tileState != (z = ((QSTile.BooleanState) state).value)) {
            this.tileState = z;
        }
        if (!Objects.equals(getLabel().getText(), state.label)) {
            getLabel().setText(state.label);
        }
        if (!Objects.equals(getSecondaryLabel().getText(), state.secondaryLabel)) {
            getSecondaryLabel().setText(state.secondaryLabel);
            getSecondaryLabel().setVisibility(TextUtils.isEmpty(state.secondaryLabel) ? 8 : 0);
        }
        if (state.state != this.lastState) {
            this.singleAnimator.cancel();
            if (animationsEnabled) {
                ValueAnimator valueAnimator = this.singleAnimator;
                PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[4];
                propertyValuesHolderArr[0] = QSTileViewImplKt.colorValuesHolder(BACKGROUND_NAME, this.paintColor, getBackgroundColorForState(state.state));
                propertyValuesHolderArr[1] = QSTileViewImplKt.colorValuesHolder("label", getLabel().getCurrentTextColor(), getLabelColorForState(state.state));
                propertyValuesHolderArr[2] = QSTileViewImplKt.colorValuesHolder(SECONDARY_LABEL_NAME, getSecondaryLabel().getCurrentTextColor(), getSecondaryLabelColorForState(state.state));
                int[] iArr = new int[2];
                ImageView imageView2 = this.chevronView;
                if (imageView2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("chevronView");
                } else {
                    imageView = imageView2;
                }
                ColorStateList imageTintList = imageView.getImageTintList();
                iArr[0] = imageTintList != null ? imageTintList.getDefaultColor() : 0;
                iArr[1] = getChevronColorForState(state.state);
                propertyValuesHolderArr[3] = QSTileViewImplKt.colorValuesHolder(CHEVRON_NAME, iArr);
                valueAnimator.setValues(propertyValuesHolderArr);
                this.singleAnimator.start();
            } else {
                setAllColors(getBackgroundColorForState(state.state), getLabelColorForState(state.state), getSecondaryLabelColorForState(state.state), getChevronColorForState(state.state));
            }
        }
        loadSideViewDrawableIfNecessary(state);
        getLabel().setEnabled(!state.disabledByPolicy);
        this.lastState = state.state;
    }

    private final void setAllColors(int i, int i2, int i3, int i4) {
        setColor(i);
        setLabelColor(i2);
        setSecondaryLabelColor(i3);
        setChevronColor(i4);
    }

    public void setColor(int i) {
        getColorBackgroundDrawable().mutate().setTint(i);
        this.paintColor = i;
    }

    private final void setLabelColor(int i) {
        getLabel().setTextColor(i);
    }

    private final void setSecondaryLabelColor(int i) {
        getSecondaryLabel().setTextColor(i);
    }

    private final void setChevronColor(int i) {
        ImageView imageView = this.chevronView;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("chevronView");
            imageView = null;
        }
        imageView.setImageTintList(ColorStateList.valueOf(i));
    }

    private final void loadSideViewDrawableIfNecessary(QSTile.State state) {
        ImageView imageView = null;
        if (state.sideViewCustomDrawable != null) {
            ImageView imageView2 = this.customDrawableView;
            if (imageView2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                imageView2 = null;
            }
            imageView2.setImageDrawable(state.sideViewCustomDrawable);
            ImageView imageView3 = this.customDrawableView;
            if (imageView3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                imageView3 = null;
            }
            imageView3.setVisibility(0);
            ImageView imageView4 = this.chevronView;
            if (imageView4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("chevronView");
            } else {
                imageView = imageView4;
            }
            imageView.setVisibility(8);
        } else if (!(state instanceof QSTile.BooleanState) || ((QSTile.BooleanState) state).forceExpandIcon) {
            ImageView imageView5 = this.customDrawableView;
            if (imageView5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                imageView5 = null;
            }
            imageView5.setImageDrawable((Drawable) null);
            ImageView imageView6 = this.customDrawableView;
            if (imageView6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                imageView6 = null;
            }
            imageView6.setVisibility(8);
            ImageView imageView7 = this.chevronView;
            if (imageView7 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("chevronView");
            } else {
                imageView = imageView7;
            }
            imageView.setVisibility(0);
        } else {
            ImageView imageView8 = this.customDrawableView;
            if (imageView8 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                imageView8 = null;
            }
            imageView8.setImageDrawable((Drawable) null);
            ImageView imageView9 = this.customDrawableView;
            if (imageView9 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                imageView9 = null;
            }
            imageView9.setVisibility(8);
            ImageView imageView10 = this.chevronView;
            if (imageView10 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("chevronView");
            } else {
                imageView = imageView10;
            }
            imageView.setVisibility(8);
        }
    }

    private final String getStateText(QSTile.State state) {
        if (state.disabledByPolicy) {
            String string = getContext().getString(C1894R.string.tile_disabled);
            Intrinsics.checkNotNullExpressionValue(string, "context.getString(R.string.tile_disabled)");
            return string;
        } else if (state.state != 0 && !(state instanceof QSTile.BooleanState)) {
            return "";
        } else {
            String str = getResources().getStringArray(SubtitleArrayMapping.INSTANCE.getSubtitleId(state.spec))[state.state];
            Intrinsics.checkNotNullExpressionValue(str, "{\n            var arrayR…ay[state.state]\n        }");
            return str;
        }
    }

    /* access modifiers changed from: protected */
    public boolean animationsEnabled() {
        if (!isShown()) {
            return false;
        }
        if (!(getAlpha() == 1.0f)) {
            return false;
        }
        getLocationOnScreen(this.locInScreen);
        if (this.locInScreen[1] >= (-getHeight())) {
            return true;
        }
        return false;
    }

    public int getBackgroundColorForState(int i) {
        if (this.isSignalOrBt) {
            return getColorInactive();
        }
        if (i == 0) {
            return getColorUnavailable();
        }
        if (i == 1) {
            return getColorInactive();
        }
        if (i == 2) {
            return getColorActive();
        }
        Log.e("QSTileViewImpl", "Invalid state " + i);
        return 0;
    }

    public int getLabelColorForState(int i) {
        if (i == 0) {
            return getColorLabelUnavailable();
        }
        if (i == 1) {
            return getColorLabelInactive();
        }
        if (i == 2) {
            return this.f332ex.getLabelColorForState(this.isSignalOrBt, getColorLabelInactive(), getColorLabelActive());
        }
        Log.e("QSTileViewImpl", "Invalid state " + i);
        return 0;
    }

    public int getSecondaryLabelColorForState(int i) {
        if (i == 0) {
            return getColorSecondaryLabelUnavailable();
        }
        if (i == 1) {
            return getColorSecondaryLabelInactive();
        }
        if (i == 2) {
            return getColorSecondaryLabelActive();
        }
        Log.e("QSTileViewImpl", "Invalid state " + i);
        return 0;
    }

    private final int getChevronColorForState(int i) {
        return getSecondaryLabelColorForState(i);
    }

    private final void cacheTileState(QSTile.State state) {
        if (state.icon != null) {
            this.skipTintBt = state.icon.skipTintBt;
        }
    }

    /* access modifiers changed from: protected */
    public void dealAndAddIcon() {
        int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.qs_icon_size);
        addView(this._icon, new LinearLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize));
    }
}
