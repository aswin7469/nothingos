package com.android.systemui.navigationbar;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Icon;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import androidx.core.view.GravityCompat;
import com.android.systemui.C1893R;
import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.navigationbar.buttons.KeyButtonView;
import com.android.systemui.navigationbar.buttons.ReverseLinearLayout;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.shared.system.QuickStepContract;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.navigationbar.NavigationBarInflaterViewEx;
import java.p026io.PrintWriter;
import java.util.Objects;

public class NavigationBarInflaterView extends FrameLayout implements NavigationModeController.ModeChangedListener {
    private static final String ABSOLUTE_SUFFIX = "A";
    private static final String ABSOLUTE_VERTICAL_CENTERED_SUFFIX = "C";
    public static final String BACK = "back";
    public static final String BUTTON_SEPARATOR = ",";
    public static final String CLIPBOARD = "clipboard";
    public static final String CONTEXTUAL = "contextual";
    public static final String GRAVITY_SEPARATOR = ";";
    public static final String HOME = "home";
    public static final String HOME_HANDLE = "home_handle";
    public static final String IME_SWITCHER = "ime_switcher";
    public static final String KEY = "key";
    public static final String KEY_CODE_END = ")";
    public static final String KEY_CODE_START = "(";
    public static final String KEY_IMAGE_DELIM = ":";
    public static final String LEFT = "left";
    public static final String MENU_IME_ROTATE = "menu_ime";
    public static final String NAVSPACE = "space";
    public static final String NAV_BAR_LEFT = "sysui_nav_bar_left";
    public static final String NAV_BAR_RIGHT = "sysui_nav_bar_right";
    public static final String NAV_BAR_VIEWS = "sysui_nav_bar";
    public static final String RECENT = "recent";
    public static final String RIGHT = "right";
    public static final String SIZE_MOD_END = "]";
    public static final String SIZE_MOD_START = "[";
    private static final String TAG = "NavBarInflater";
    private static final String WEIGHT_CENTERED_SUFFIX = "WC";
    private static final String WEIGHT_SUFFIX = "W";
    private boolean mAlternativeOrder;
    SparseArray<ButtonDispatcher> mButtonDispatchers;
    private String mCurrentLayout;
    protected FrameLayout mHorizontal;
    private boolean mIsVertical;
    protected LayoutInflater mLandscapeInflater;
    private View mLastLandscape;
    private View mLastPortrait;
    protected LayoutInflater mLayoutInflater;
    private int mNavBarMode = 0;
    private OverviewProxyService mOverviewProxyService;
    protected FrameLayout mVertical;

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

    public NavigationBarInflaterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        createInflaters();
        this.mOverviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
        this.mNavBarMode = ((NavigationModeController) Dependency.get(NavigationModeController.class)).addListener(this);
    }

    /* access modifiers changed from: package-private */
    public void createInflaters() {
        this.mLayoutInflater = LayoutInflater.from(this.mContext);
        Configuration configuration = new Configuration();
        configuration.setTo(this.mContext.getResources().getConfiguration());
        configuration.orientation = 2;
        this.mLandscapeInflater = LayoutInflater.from(this.mContext.createConfigurationContext(configuration));
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        inflateChildren();
        clearViews();
        inflateLayout(getDefaultLayout());
    }

    private void inflateChildren() {
        removeAllViews();
        FrameLayout frameLayout = (FrameLayout) this.mLayoutInflater.inflate(C1893R.layout.navigation_layout, this, false);
        this.mHorizontal = frameLayout;
        addView(frameLayout);
        FrameLayout frameLayout2 = (FrameLayout) this.mLayoutInflater.inflate(C1893R.layout.navigation_layout_vertical, this, false);
        this.mVertical = frameLayout2;
        addView(frameLayout2);
        updateAlternativeOrder();
    }

    /* access modifiers changed from: protected */
    public String getDefaultLayout() {
        int i;
        if (NTDependencyEx.get(NavigationBarInflaterViewEx.class) != null) {
            return ((NavigationBarInflaterViewEx) NTDependencyEx.get(NavigationBarInflaterViewEx.class)).getDefaultLayout(getContext(), QuickStepContract.isGesturalMode(this.mNavBarMode), this.mOverviewProxyService.shouldShowSwipeUpUI());
        }
        if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
            i = C1893R.string.config_navBarLayoutHandle;
        } else {
            i = this.mOverviewProxyService.shouldShowSwipeUpUI() ? C1893R.string.config_navBarLayoutQuickstep : C1893R.string.config_navBarLayout;
        }
        return getContext().getString(i);
    }

    public void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        ((NavigationModeController) Dependency.get(NavigationModeController.class)).removeListener(this);
        super.onDetachedFromWindow();
    }

    public void onLikelyDefaultLayoutChange() {
        String defaultLayout = getDefaultLayout();
        if (!Objects.equals(this.mCurrentLayout, defaultLayout)) {
            clearViews();
            inflateLayout(defaultLayout);
        }
    }

    public void setButtonDispatchers(SparseArray<ButtonDispatcher> sparseArray) {
        this.mButtonDispatchers = sparseArray;
        clearDispatcherViews();
        for (int i = 0; i < sparseArray.size(); i++) {
            initiallyFill(sparseArray.valueAt(i));
        }
    }

    /* access modifiers changed from: package-private */
    public void updateButtonDispatchersCurrentView() {
        if (this.mButtonDispatchers != null) {
            FrameLayout frameLayout = this.mIsVertical ? this.mVertical : this.mHorizontal;
            for (int i = 0; i < this.mButtonDispatchers.size(); i++) {
                this.mButtonDispatchers.valueAt(i).setCurrentView(frameLayout);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setVertical(boolean z) {
        if (z != this.mIsVertical) {
            this.mIsVertical = z;
        }
    }

    /* access modifiers changed from: package-private */
    public void setAlternativeOrder(boolean z) {
        if (z != this.mAlternativeOrder) {
            this.mAlternativeOrder = z;
            updateAlternativeOrder();
        }
    }

    private void updateAlternativeOrder() {
        updateAlternativeOrder(this.mHorizontal.findViewById(C1893R.C1897id.ends_group));
        updateAlternativeOrder(this.mHorizontal.findViewById(C1893R.C1897id.center_group));
        updateAlternativeOrder(this.mVertical.findViewById(C1893R.C1897id.ends_group));
        updateAlternativeOrder(this.mVertical.findViewById(C1893R.C1897id.center_group));
    }

    private void updateAlternativeOrder(View view) {
        if (view instanceof ReverseLinearLayout) {
            ((ReverseLinearLayout) view).setAlternativeOrder(this.mAlternativeOrder);
        }
    }

    private void initiallyFill(ButtonDispatcher buttonDispatcher) {
        addAll(buttonDispatcher, (ViewGroup) this.mHorizontal.findViewById(C1893R.C1897id.ends_group));
        addAll(buttonDispatcher, (ViewGroup) this.mHorizontal.findViewById(C1893R.C1897id.center_group));
        addAll(buttonDispatcher, (ViewGroup) this.mVertical.findViewById(C1893R.C1897id.ends_group));
        addAll(buttonDispatcher, (ViewGroup) this.mVertical.findViewById(C1893R.C1897id.center_group));
    }

    private void addAll(ButtonDispatcher buttonDispatcher, ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            if (viewGroup.getChildAt(i).getId() == buttonDispatcher.getId()) {
                buttonDispatcher.addView(viewGroup.getChildAt(i));
            }
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                addAll(buttonDispatcher, (ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void inflateLayout(String str) {
        this.mCurrentLayout = str;
        if (str == null) {
            str = getDefaultLayout();
        }
        String[] split = str.split(GRAVITY_SEPARATOR, 3);
        if (split.length != 3) {
            Log.d(TAG, "Invalid layout.");
            split = getDefaultLayout().split(GRAVITY_SEPARATOR, 3);
        }
        String[] split2 = split[0].split(BUTTON_SEPARATOR);
        String[] split3 = split[1].split(BUTTON_SEPARATOR);
        String[] split4 = split[2].split(BUTTON_SEPARATOR);
        inflateButtons(split2, (ViewGroup) this.mHorizontal.findViewById(C1893R.C1897id.ends_group), false, true);
        inflateButtons(split2, (ViewGroup) this.mVertical.findViewById(C1893R.C1897id.ends_group), true, true);
        inflateButtons(split3, (ViewGroup) this.mHorizontal.findViewById(C1893R.C1897id.center_group), false, false);
        inflateButtons(split3, (ViewGroup) this.mVertical.findViewById(C1893R.C1897id.center_group), true, false);
        addGravitySpacer((LinearLayout) this.mHorizontal.findViewById(C1893R.C1897id.ends_group));
        addGravitySpacer((LinearLayout) this.mVertical.findViewById(C1893R.C1897id.ends_group));
        inflateButtons(split4, (ViewGroup) this.mHorizontal.findViewById(C1893R.C1897id.ends_group), false, false);
        inflateButtons(split4, (ViewGroup) this.mVertical.findViewById(C1893R.C1897id.ends_group), true, false);
        updateButtonDispatchersCurrentView();
    }

    private void addGravitySpacer(LinearLayout linearLayout) {
        linearLayout.addView(new Space(this.mContext), new LinearLayout.LayoutParams(0, 0, 1.0f));
    }

    private void inflateButtons(String[] strArr, ViewGroup viewGroup, boolean z, boolean z2) {
        for (String inflateButton : strArr) {
            inflateButton(inflateButton, viewGroup, z, z2);
        }
    }

    private ViewGroup.LayoutParams copy(ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof LinearLayout.LayoutParams) {
            return new LinearLayout.LayoutParams(layoutParams.width, layoutParams.height, ((LinearLayout.LayoutParams) layoutParams).weight);
        }
        return new FrameLayout.LayoutParams(layoutParams.width, layoutParams.height);
    }

    /* access modifiers changed from: protected */
    public View inflateButton(String str, ViewGroup viewGroup, boolean z, boolean z2) {
        View createView = createView(str, viewGroup, z ? this.mLandscapeInflater : this.mLayoutInflater);
        if (createView == null) {
            return null;
        }
        View applySize = applySize(createView, str, z, z2);
        viewGroup.addView(applySize);
        addToDispatchers(applySize);
        View view = z ? this.mLastLandscape : this.mLastPortrait;
        View childAt = applySize instanceof ReverseLinearLayout.ReverseRelativeLayout ? ((ReverseLinearLayout.ReverseRelativeLayout) applySize).getChildAt(0) : applySize;
        if (view != null) {
            childAt.setAccessibilityTraversalAfter(view.getId());
        }
        if (z) {
            this.mLastLandscape = childAt;
        } else {
            this.mLastPortrait = childAt;
        }
        return applySize;
    }

    private View applySize(View view, String str, boolean z, boolean z2) {
        String extractSize = extractSize(str);
        if (extractSize == null) {
            return view;
        }
        if (extractSize.contains("W") || extractSize.contains("A")) {
            ReverseLinearLayout.ReverseRelativeLayout reverseRelativeLayout = new ReverseLinearLayout.ReverseRelativeLayout(this.mContext);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(view.getLayoutParams());
            int i = z ? z2 ? 48 : 80 : z2 ? 8388611 : GravityCompat.END;
            if (extractSize.endsWith(WEIGHT_CENTERED_SUFFIX)) {
                i = 17;
            } else if (extractSize.endsWith(ABSOLUTE_VERTICAL_CENTERED_SUFFIX)) {
                i = 16;
            }
            reverseRelativeLayout.setDefaultGravity(i);
            reverseRelativeLayout.setGravity(i);
            reverseRelativeLayout.addView(view, layoutParams);
            if (extractSize.contains("W")) {
                reverseRelativeLayout.setLayoutParams(new LinearLayout.LayoutParams(0, -1, Float.parseFloat(extractSize.substring(0, extractSize.indexOf("W")))));
            } else {
                reverseRelativeLayout.setLayoutParams(new LinearLayout.LayoutParams((int) convertDpToPx(this.mContext, Float.parseFloat(extractSize.substring(0, extractSize.indexOf("A")))), -1));
            }
            reverseRelativeLayout.setClipChildren(false);
            reverseRelativeLayout.setClipToPadding(false);
            return reverseRelativeLayout;
        }
        float parseFloat = Float.parseFloat(extractSize);
        ViewGroup.LayoutParams layoutParams2 = view.getLayoutParams();
        layoutParams2.width = (int) (((float) layoutParams2.width) * parseFloat);
        return view;
    }

    /* access modifiers changed from: package-private */
    public View createView(String str, ViewGroup viewGroup, LayoutInflater layoutInflater) {
        String extractButton = extractButton(str);
        if (LEFT.equals(extractButton)) {
            extractButton = extractButton(NAVSPACE);
        } else if (RIGHT.equals(extractButton)) {
            extractButton = extractButton(MENU_IME_ROTATE);
        }
        if (HOME.equals(extractButton)) {
            return layoutInflater.inflate(C1893R.layout.home, viewGroup, false);
        }
        if (BACK.equals(extractButton)) {
            return layoutInflater.inflate(C1893R.layout.back, viewGroup, false);
        }
        if (RECENT.equals(extractButton)) {
            return layoutInflater.inflate(C1893R.layout.recent_apps, viewGroup, false);
        }
        if (MENU_IME_ROTATE.equals(extractButton)) {
            return layoutInflater.inflate(C1893R.layout.menu_ime, viewGroup, false);
        }
        if (NAVSPACE.equals(extractButton)) {
            return layoutInflater.inflate(C1893R.layout.nav_key_space, viewGroup, false);
        }
        if (CLIPBOARD.equals(extractButton)) {
            return layoutInflater.inflate(C1893R.layout.clipboard, viewGroup, false);
        }
        if (CONTEXTUAL.equals(extractButton)) {
            return layoutInflater.inflate(C1893R.layout.contextual, viewGroup, false);
        }
        if (HOME_HANDLE.equals(extractButton)) {
            return layoutInflater.inflate(C1893R.layout.home_handle, viewGroup, false);
        }
        if (IME_SWITCHER.equals(extractButton)) {
            return layoutInflater.inflate(C1893R.layout.ime_switcher, viewGroup, false);
        }
        if (!extractButton.startsWith("key")) {
            return null;
        }
        String extractImage = extractImage(extractButton);
        int extractKeycode = extractKeycode(extractButton);
        View inflate = layoutInflater.inflate(C1893R.layout.custom_key, viewGroup, false);
        KeyButtonView keyButtonView = (KeyButtonView) inflate;
        keyButtonView.setCode(extractKeycode);
        if (extractImage != null) {
            if (extractImage.contains(":")) {
                keyButtonView.loadAsync(Icon.createWithContentUri(extractImage));
            } else if (extractImage.contains("/")) {
                int indexOf = extractImage.indexOf(47);
                keyButtonView.loadAsync(Icon.createWithResource(extractImage.substring(0, indexOf), Integer.parseInt(extractImage.substring(indexOf + 1))));
            }
        }
        return inflate;
    }

    public static String extractImage(String str) {
        if (!str.contains(":")) {
            return null;
        }
        return str.substring(str.indexOf(":") + 1, str.indexOf(KEY_CODE_END));
    }

    public static int extractKeycode(String str) {
        if (!str.contains(KEY_CODE_START)) {
            return 1;
        }
        return Integer.parseInt(str.substring(str.indexOf(KEY_CODE_START) + 1, str.indexOf(":")));
    }

    public static String extractSize(String str) {
        if (!str.contains(SIZE_MOD_START)) {
            return null;
        }
        return str.substring(str.indexOf(SIZE_MOD_START) + 1, str.indexOf(SIZE_MOD_END));
    }

    public static String extractButton(String str) {
        if (!str.contains(SIZE_MOD_START)) {
            return str;
        }
        return str.substring(0, str.indexOf(SIZE_MOD_START));
    }

    private void addToDispatchers(View view) {
        SparseArray<ButtonDispatcher> sparseArray = this.mButtonDispatchers;
        if (sparseArray != null) {
            int indexOfKey = sparseArray.indexOfKey(view.getId());
            if (indexOfKey >= 0) {
                this.mButtonDispatchers.valueAt(indexOfKey).addView(view);
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    addToDispatchers(viewGroup.getChildAt(i));
                }
            }
        }
    }

    private void clearDispatcherViews() {
        if (this.mButtonDispatchers != null) {
            for (int i = 0; i < this.mButtonDispatchers.size(); i++) {
                this.mButtonDispatchers.valueAt(i).clear();
            }
        }
    }

    private void clearViews() {
        clearDispatcherViews();
        clearAllChildren((ViewGroup) this.mHorizontal.findViewById(C1893R.C1897id.nav_buttons));
        clearAllChildren((ViewGroup) this.mVertical.findViewById(C1893R.C1897id.nav_buttons));
    }

    private void clearAllChildren(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            ((ViewGroup) viewGroup.getChildAt(i)).removeAllViews();
        }
    }

    private static float convertDpToPx(Context context, float f) {
        return f * context.getResources().getDisplayMetrics().density;
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("NavigationBarInflaterView");
        printWriter.println("  mCurrentLayout: " + this.mCurrentLayout);
    }
}
