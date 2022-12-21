package androidx.leanback.app;

import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.C0742R;
import androidx.leanback.transition.TransitionHelper;
import androidx.leanback.widget.DiffCallback;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;
import androidx.leanback.widget.GuidedActionAdapter;
import androidx.leanback.widget.GuidedActionAdapterGroup;
import androidx.leanback.widget.GuidedActionsStylist;
import androidx.leanback.widget.NonOverlappingLinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class GuidedStepSupportFragment extends Fragment implements GuidedActionAdapter.FocusListener {
    private static final boolean DEBUG = false;
    private static final String ENTRY_NAME_ENTRANCE = "GuidedStepEntrance";
    private static final String ENTRY_NAME_REPLACE = "GuidedStepDefault";
    private static final String EXTRA_ACTION_PREFIX = "action_";
    private static final String EXTRA_BUTTON_ACTION_PREFIX = "buttonaction_";
    public static final String EXTRA_UI_STYLE = "uiStyle";
    private static final boolean IS_FRAMEWORK_FRAGMENT = false;
    public static final int SLIDE_FROM_BOTTOM = 1;
    public static final int SLIDE_FROM_SIDE = 0;
    private static final String TAG = "GuidedStepF";
    private static final String TAG_LEAN_BACK_ACTIONS_FRAGMENT = "leanBackGuidedStepSupportFragment";
    public static final int UI_STYLE_ACTIVITY_ROOT = 2;
    @Deprecated
    public static final int UI_STYLE_DEFAULT = 0;
    public static final int UI_STYLE_ENTRANCE = 1;
    public static final int UI_STYLE_REPLACE = 0;
    private int entranceTransitionType = 0;
    private List<GuidedAction> mActions = new ArrayList();
    GuidedActionsStylist mActionsStylist;
    private GuidedActionAdapter mAdapter;
    private GuidedActionAdapterGroup mAdapterGroup;
    private List<GuidedAction> mButtonActions = new ArrayList();
    private GuidedActionsStylist mButtonActionsStylist;
    private GuidedActionAdapter mButtonAdapter;
    private GuidanceStylist mGuidanceStylist;
    private GuidedActionAdapter mSubAdapter;
    private ContextThemeWrapper mThemeWrapper;

    public boolean isFocusOutEndAllowed() {
        return false;
    }

    public boolean isFocusOutStartAllowed() {
        return false;
    }

    public void onCreateActions(List<GuidedAction> list, Bundle bundle) {
    }

    public void onCreateButtonActions(List<GuidedAction> list, Bundle bundle) {
    }

    public void onGuidedActionClicked(GuidedAction guidedAction) {
    }

    @Deprecated
    public void onGuidedActionEdited(GuidedAction guidedAction) {
    }

    public void onGuidedActionFocused(GuidedAction guidedAction) {
    }

    public int onProvideTheme() {
        return -1;
    }

    public boolean onSubGuidedActionClicked(GuidedAction guidedAction) {
        return true;
    }

    public static class DummyFragment extends Fragment {
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            View view = new View(layoutInflater.getContext());
            view.setVisibility(8);
            return view;
        }
    }

    public GuidedStepSupportFragment() {
        onProvideFragmentTransitions();
    }

    public GuidanceStylist onCreateGuidanceStylist() {
        return new GuidanceStylist();
    }

    public GuidedActionsStylist onCreateActionsStylist() {
        return new GuidedActionsStylist();
    }

    public GuidedActionsStylist onCreateButtonActionsStylist() {
        GuidedActionsStylist guidedActionsStylist = new GuidedActionsStylist();
        guidedActionsStylist.setAsButtonActions();
        return guidedActionsStylist;
    }

    public GuidanceStylist.Guidance onCreateGuidance(Bundle bundle) {
        return new GuidanceStylist.Guidance("", "", "", (Drawable) null);
    }

    public boolean isExpanded() {
        return this.mActionsStylist.isExpanded();
    }

    public boolean isSubActionsExpanded() {
        return this.mActionsStylist.isSubActionsExpanded();
    }

    public void expandSubActions(GuidedAction guidedAction) {
        if (guidedAction.hasSubActions()) {
            expandAction(guidedAction, true);
        }
    }

    public void expandAction(GuidedAction guidedAction, boolean z) {
        this.mActionsStylist.expandAction(guidedAction, z);
    }

    public void collapseSubActions() {
        collapseAction(true);
    }

    public void collapseAction(boolean z) {
        GuidedActionsStylist guidedActionsStylist = this.mActionsStylist;
        if (guidedActionsStylist != null && guidedActionsStylist.getActionsGridView() != null) {
            this.mActionsStylist.collapseAction(z);
        }
    }

    public void onGuidedActionEditCanceled(GuidedAction guidedAction) {
        onGuidedActionEdited(guidedAction);
    }

    public long onGuidedActionEditedAndProceed(GuidedAction guidedAction) {
        onGuidedActionEdited(guidedAction);
        return -2;
    }

    public static int add(FragmentManager fragmentManager, GuidedStepSupportFragment guidedStepSupportFragment) {
        return add(fragmentManager, guidedStepSupportFragment, 16908290);
    }

    public static int add(FragmentManager fragmentManager, GuidedStepSupportFragment guidedStepSupportFragment, int i) {
        GuidedStepSupportFragment currentGuidedStepSupportFragment = getCurrentGuidedStepSupportFragment(fragmentManager);
        int i2 = currentGuidedStepSupportFragment != null ? 1 : 0;
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        guidedStepSupportFragment.setUiStyle(1 ^ i2);
        beginTransaction.addToBackStack(guidedStepSupportFragment.generateStackEntryName());
        if (currentGuidedStepSupportFragment != null) {
            guidedStepSupportFragment.onAddSharedElementTransition(beginTransaction, currentGuidedStepSupportFragment);
        }
        return beginTransaction.replace(i, (Fragment) guidedStepSupportFragment, TAG_LEAN_BACK_ACTIONS_FRAGMENT).commit();
    }

    /* access modifiers changed from: protected */
    public void onAddSharedElementTransition(FragmentTransaction fragmentTransaction, GuidedStepSupportFragment guidedStepSupportFragment) {
        View view = guidedStepSupportFragment.getView();
        addNonNullSharedElementTransition(fragmentTransaction, view.findViewById(C0742R.C0745id.action_fragment_root), "action_fragment_root");
        addNonNullSharedElementTransition(fragmentTransaction, view.findViewById(C0742R.C0745id.action_fragment_background), "action_fragment_background");
        addNonNullSharedElementTransition(fragmentTransaction, view.findViewById(C0742R.C0745id.action_fragment), "action_fragment");
        addNonNullSharedElementTransition(fragmentTransaction, view.findViewById(C0742R.C0745id.guidedactions_root), "guidedactions_root");
        addNonNullSharedElementTransition(fragmentTransaction, view.findViewById(C0742R.C0745id.guidedactions_content), "guidedactions_content");
        addNonNullSharedElementTransition(fragmentTransaction, view.findViewById(C0742R.C0745id.guidedactions_list_background), "guidedactions_list_background");
        addNonNullSharedElementTransition(fragmentTransaction, view.findViewById(C0742R.C0745id.guidedactions_root2), "guidedactions_root2");
        addNonNullSharedElementTransition(fragmentTransaction, view.findViewById(C0742R.C0745id.guidedactions_content2), "guidedactions_content2");
        addNonNullSharedElementTransition(fragmentTransaction, view.findViewById(C0742R.C0745id.guidedactions_list_background2), "guidedactions_list_background2");
    }

    private static void addNonNullSharedElementTransition(FragmentTransaction fragmentTransaction, View view, String str) {
        if (view != null) {
            fragmentTransaction.addSharedElement(view, str);
        }
    }

    /* access modifiers changed from: package-private */
    public final String generateStackEntryName() {
        return generateStackEntryName(getUiStyle(), getClass());
    }

    static String generateStackEntryName(int i, Class<?> cls) {
        if (i != 0) {
            return i != 1 ? "" : ENTRY_NAME_ENTRANCE + cls.getName();
        }
        return ENTRY_NAME_REPLACE + cls.getName();
    }

    static boolean isStackEntryUiStyleEntrance(String str) {
        return str != null && str.startsWith(ENTRY_NAME_ENTRANCE);
    }

    static String getGuidedStepSupportFragmentClassName(String str) {
        if (str.startsWith(ENTRY_NAME_REPLACE)) {
            return str.substring(17);
        }
        return str.startsWith(ENTRY_NAME_ENTRANCE) ? str.substring(18) : "";
    }

    public static int addAsRoot(FragmentActivity fragmentActivity, GuidedStepSupportFragment guidedStepSupportFragment, int i) {
        fragmentActivity.getWindow().getDecorView();
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        if (supportFragmentManager.findFragmentByTag(TAG_LEAN_BACK_ACTIONS_FRAGMENT) != null) {
            Log.w(TAG, "Fragment is already exists, likely calling addAsRoot() when savedInstanceState is not null in Activity.onCreate().");
            return -1;
        }
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        guidedStepSupportFragment.setUiStyle(2);
        return beginTransaction.replace(i, (Fragment) guidedStepSupportFragment, TAG_LEAN_BACK_ACTIONS_FRAGMENT).commit();
    }

    public static GuidedStepSupportFragment getCurrentGuidedStepSupportFragment(FragmentManager fragmentManager) {
        Fragment findFragmentByTag = fragmentManager.findFragmentByTag(TAG_LEAN_BACK_ACTIONS_FRAGMENT);
        if (findFragmentByTag instanceof GuidedStepSupportFragment) {
            return (GuidedStepSupportFragment) findFragmentByTag;
        }
        return null;
    }

    public GuidanceStylist getGuidanceStylist() {
        return this.mGuidanceStylist;
    }

    public GuidedActionsStylist getGuidedActionsStylist() {
        return this.mActionsStylist;
    }

    public List<GuidedAction> getButtonActions() {
        return this.mButtonActions;
    }

    public GuidedAction findButtonActionById(long j) {
        int findButtonActionPositionById = findButtonActionPositionById(j);
        if (findButtonActionPositionById >= 0) {
            return this.mButtonActions.get(findButtonActionPositionById);
        }
        return null;
    }

    public int findButtonActionPositionById(long j) {
        if (this.mButtonActions == null) {
            return -1;
        }
        for (int i = 0; i < this.mButtonActions.size(); i++) {
            if (this.mButtonActions.get(i).getId() == j) {
                return i;
            }
        }
        return -1;
    }

    public GuidedActionsStylist getGuidedButtonActionsStylist() {
        return this.mButtonActionsStylist;
    }

    public void setButtonActions(List<GuidedAction> list) {
        this.mButtonActions = list;
        GuidedActionAdapter guidedActionAdapter = this.mButtonAdapter;
        if (guidedActionAdapter != null) {
            guidedActionAdapter.setActions(list);
        }
    }

    public void notifyButtonActionChanged(int i) {
        GuidedActionAdapter guidedActionAdapter = this.mButtonAdapter;
        if (guidedActionAdapter != null) {
            guidedActionAdapter.notifyItemChanged(i);
        }
    }

    public View getButtonActionItemView(int i) {
        RecyclerView.ViewHolder findViewHolderForPosition = this.mButtonActionsStylist.getActionsGridView().findViewHolderForPosition(i);
        if (findViewHolderForPosition == null) {
            return null;
        }
        return findViewHolderForPosition.itemView;
    }

    public void setSelectedButtonActionPosition(int i) {
        this.mButtonActionsStylist.getActionsGridView().setSelectedPosition(i);
    }

    public int getSelectedButtonActionPosition() {
        return this.mButtonActionsStylist.getActionsGridView().getSelectedPosition();
    }

    public List<GuidedAction> getActions() {
        return this.mActions;
    }

    public GuidedAction findActionById(long j) {
        int findActionPositionById = findActionPositionById(j);
        if (findActionPositionById >= 0) {
            return this.mActions.get(findActionPositionById);
        }
        return null;
    }

    public int findActionPositionById(long j) {
        if (this.mActions == null) {
            return -1;
        }
        for (int i = 0; i < this.mActions.size(); i++) {
            if (this.mActions.get(i).getId() == j) {
                return i;
            }
        }
        return -1;
    }

    public void setActions(List<GuidedAction> list) {
        this.mActions = list;
        GuidedActionAdapter guidedActionAdapter = this.mAdapter;
        if (guidedActionAdapter != null) {
            guidedActionAdapter.setActions(list);
        }
    }

    public void setActionsDiffCallback(DiffCallback<GuidedAction> diffCallback) {
        this.mAdapter.setDiffCallback(diffCallback);
    }

    public void notifyActionChanged(int i) {
        GuidedActionAdapter guidedActionAdapter = this.mAdapter;
        if (guidedActionAdapter != null) {
            guidedActionAdapter.notifyItemChanged(i);
        }
    }

    public View getActionItemView(int i) {
        RecyclerView.ViewHolder findViewHolderForPosition = this.mActionsStylist.getActionsGridView().findViewHolderForPosition(i);
        if (findViewHolderForPosition == null) {
            return null;
        }
        return findViewHolderForPosition.itemView;
    }

    public void setSelectedActionPosition(int i) {
        this.mActionsStylist.getActionsGridView().setSelectedPosition(i);
    }

    public int getSelectedActionPosition() {
        return this.mActionsStylist.getActionsGridView().getSelectedPosition();
    }

    /* access modifiers changed from: protected */
    public void onProvideFragmentTransitions() {
        int uiStyle = getUiStyle();
        if (uiStyle == 0) {
            Object createFadeAndShortSlide = TransitionHelper.createFadeAndShortSlide(GravityCompat.END);
            TransitionHelper.exclude(createFadeAndShortSlide, C0742R.C0745id.guidedstep_background, true);
            TransitionHelper.exclude(createFadeAndShortSlide, C0742R.C0745id.guidedactions_sub_list_background, true);
            setEnterTransition(createFadeAndShortSlide);
            Object createFadeTransition = TransitionHelper.createFadeTransition(3);
            TransitionHelper.include(createFadeTransition, C0742R.C0745id.guidedactions_sub_list_background);
            Object createChangeBounds = TransitionHelper.createChangeBounds(false);
            Object createTransitionSet = TransitionHelper.createTransitionSet(false);
            TransitionHelper.addTransition(createTransitionSet, createFadeTransition);
            TransitionHelper.addTransition(createTransitionSet, createChangeBounds);
            setSharedElementEnterTransition(createTransitionSet);
        } else if (uiStyle == 1) {
            if (this.entranceTransitionType == 0) {
                Object createFadeTransition2 = TransitionHelper.createFadeTransition(3);
                TransitionHelper.include(createFadeTransition2, C0742R.C0745id.guidedstep_background);
                Object createFadeAndShortSlide2 = TransitionHelper.createFadeAndShortSlide(GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK);
                TransitionHelper.include(createFadeAndShortSlide2, C0742R.C0745id.content_fragment);
                TransitionHelper.include(createFadeAndShortSlide2, C0742R.C0745id.action_fragment_root);
                Object createTransitionSet2 = TransitionHelper.createTransitionSet(false);
                TransitionHelper.addTransition(createTransitionSet2, createFadeTransition2);
                TransitionHelper.addTransition(createTransitionSet2, createFadeAndShortSlide2);
                setEnterTransition(createTransitionSet2);
            } else {
                Object createFadeAndShortSlide3 = TransitionHelper.createFadeAndShortSlide(80);
                TransitionHelper.include(createFadeAndShortSlide3, C0742R.C0745id.guidedstep_background_view_root);
                Object createTransitionSet3 = TransitionHelper.createTransitionSet(false);
                TransitionHelper.addTransition(createTransitionSet3, createFadeAndShortSlide3);
                setEnterTransition(createTransitionSet3);
            }
            setSharedElementEnterTransition((Object) null);
        } else if (uiStyle == 2) {
            setEnterTransition((Object) null);
            setSharedElementEnterTransition((Object) null);
        }
        Object createFadeAndShortSlide4 = TransitionHelper.createFadeAndShortSlide(8388611);
        TransitionHelper.exclude(createFadeAndShortSlide4, C0742R.C0745id.guidedstep_background, true);
        TransitionHelper.exclude(createFadeAndShortSlide4, C0742R.C0745id.guidedactions_sub_list_background, true);
        setExitTransition(createFadeAndShortSlide4);
    }

    public View onCreateBackgroundView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(C0742R.layout.lb_guidedstep_background, viewGroup, false);
    }

    public void setUiStyle(int i) {
        boolean z;
        int uiStyle = getUiStyle();
        Bundle arguments = getArguments();
        if (arguments == null) {
            arguments = new Bundle();
            z = true;
        } else {
            z = false;
        }
        arguments.putInt("uiStyle", i);
        if (z) {
            setArguments(arguments);
        }
        if (i != uiStyle) {
            onProvideFragmentTransitions();
        }
    }

    public int getUiStyle() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return 1;
        }
        return arguments.getInt("uiStyle", 1);
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mGuidanceStylist = onCreateGuidanceStylist();
        this.mActionsStylist = onCreateActionsStylist();
        this.mButtonActionsStylist = onCreateButtonActionsStylist();
        onProvideFragmentTransitions();
        ArrayList arrayList = new ArrayList();
        onCreateActions(arrayList, bundle);
        if (bundle != null) {
            onRestoreActions(arrayList, bundle);
        }
        setActions(arrayList);
        ArrayList arrayList2 = new ArrayList();
        onCreateButtonActions(arrayList2, bundle);
        if (bundle != null) {
            onRestoreButtonActions(arrayList2, bundle);
        }
        setButtonActions(arrayList2);
    }

    public void onDestroyView() {
        this.mGuidanceStylist.onDestroyView();
        this.mActionsStylist.onDestroyView();
        this.mButtonActionsStylist.onDestroyView();
        this.mAdapter = null;
        this.mSubAdapter = null;
        this.mButtonAdapter = null;
        this.mAdapterGroup = null;
        super.onDestroyView();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Bundle bundle2 = bundle;
        resolveTheme();
        LayoutInflater themeInflater = getThemeInflater(layoutInflater);
        GuidedStepRootLayout guidedStepRootLayout = (GuidedStepRootLayout) themeInflater.inflate(C0742R.layout.lb_guidedstep_fragment, viewGroup, false);
        guidedStepRootLayout.setFocusOutStart(isFocusOutStartAllowed());
        guidedStepRootLayout.setFocusOutEnd(isFocusOutEndAllowed());
        ViewGroup viewGroup2 = (ViewGroup) guidedStepRootLayout.findViewById(C0742R.C0745id.content_fragment);
        ViewGroup viewGroup3 = (ViewGroup) guidedStepRootLayout.findViewById(C0742R.C0745id.action_fragment);
        ((NonOverlappingLinearLayout) viewGroup3).setFocusableViewAvailableFixEnabled(true);
        viewGroup2.addView(this.mGuidanceStylist.onCreateView(themeInflater, viewGroup2, onCreateGuidance(bundle2)));
        viewGroup3.addView(this.mActionsStylist.onCreateView(themeInflater, viewGroup3));
        View onCreateView = this.mButtonActionsStylist.onCreateView(themeInflater, viewGroup3);
        viewGroup3.addView(onCreateView);
        C08241 r13 = new GuidedActionAdapter.EditListener() {
            public void onImeOpen() {
                GuidedStepSupportFragment.this.runImeAnimations(true);
            }

            public void onImeClose() {
                GuidedStepSupportFragment.this.runImeAnimations(false);
            }

            public long onGuidedActionEditedAndProceed(GuidedAction guidedAction) {
                return GuidedStepSupportFragment.this.onGuidedActionEditedAndProceed(guidedAction);
            }

            public void onGuidedActionEditCanceled(GuidedAction guidedAction) {
                GuidedStepSupportFragment.this.onGuidedActionEditCanceled(guidedAction);
            }
        };
        this.mAdapter = new GuidedActionAdapter(this.mActions, new GuidedActionAdapter.ClickListener() {
            public void onGuidedActionClicked(GuidedAction guidedAction) {
                GuidedStepSupportFragment.this.onGuidedActionClicked(guidedAction);
                if (GuidedStepSupportFragment.this.isExpanded()) {
                    GuidedStepSupportFragment.this.collapseAction(true);
                } else if (guidedAction.hasSubActions() || guidedAction.hasEditableActivatorView()) {
                    GuidedStepSupportFragment.this.expandAction(guidedAction, true);
                }
            }
        }, this, this.mActionsStylist, false);
        this.mButtonAdapter = new GuidedActionAdapter(this.mButtonActions, new GuidedActionAdapter.ClickListener() {
            public void onGuidedActionClicked(GuidedAction guidedAction) {
                GuidedStepSupportFragment.this.onGuidedActionClicked(guidedAction);
            }
        }, this, this.mButtonActionsStylist, false);
        this.mSubAdapter = new GuidedActionAdapter((List<GuidedAction>) null, new GuidedActionAdapter.ClickListener() {
            public void onGuidedActionClicked(GuidedAction guidedAction) {
                if (!GuidedStepSupportFragment.this.mActionsStylist.isInExpandTransition() && GuidedStepSupportFragment.this.onSubGuidedActionClicked(guidedAction)) {
                    GuidedStepSupportFragment.this.collapseSubActions();
                }
            }
        }, this, this.mActionsStylist, true);
        GuidedActionAdapterGroup guidedActionAdapterGroup = new GuidedActionAdapterGroup();
        this.mAdapterGroup = guidedActionAdapterGroup;
        guidedActionAdapterGroup.addAdpter(this.mAdapter, this.mButtonAdapter);
        this.mAdapterGroup.addAdpter(this.mSubAdapter, (GuidedActionAdapter) null);
        this.mAdapterGroup.setEditListener(r13);
        this.mActionsStylist.setEditListener(r13);
        this.mActionsStylist.getActionsGridView().setAdapter(this.mAdapter);
        if (this.mActionsStylist.getSubActionsGridView() != null) {
            this.mActionsStylist.getSubActionsGridView().setAdapter(this.mSubAdapter);
        }
        this.mButtonActionsStylist.getActionsGridView().setAdapter(this.mButtonAdapter);
        if (this.mButtonActions.size() == 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) onCreateView.getLayoutParams();
            layoutParams.weight = 0.0f;
            onCreateView.setLayoutParams(layoutParams);
        } else {
            Context context = this.mThemeWrapper;
            if (context == null) {
                context = getContext();
            }
            TypedValue typedValue = new TypedValue();
            if (context.getTheme().resolveAttribute(C0742R.attr.guidedActionContentWidthWeightTwoPanels, typedValue, true)) {
                View findViewById = guidedStepRootLayout.findViewById(C0742R.C0745id.action_fragment_root);
                float f = typedValue.getFloat();
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) findViewById.getLayoutParams();
                layoutParams2.weight = f;
                findViewById.setLayoutParams(layoutParams2);
            }
        }
        View onCreateBackgroundView = onCreateBackgroundView(themeInflater, guidedStepRootLayout, bundle2);
        if (onCreateBackgroundView != null) {
            ((FrameLayout) guidedStepRootLayout.findViewById(C0742R.C0745id.guidedstep_background_view_root)).addView(onCreateBackgroundView, 0);
        }
        return guidedStepRootLayout;
    }

    public void onResume() {
        super.onResume();
        getView().findViewById(C0742R.C0745id.action_fragment).requestFocus();
    }

    /* access modifiers changed from: package-private */
    public final String getAutoRestoreKey(GuidedAction guidedAction) {
        return EXTRA_ACTION_PREFIX + guidedAction.getId();
    }

    /* access modifiers changed from: package-private */
    public final String getButtonAutoRestoreKey(GuidedAction guidedAction) {
        return EXTRA_BUTTON_ACTION_PREFIX + guidedAction.getId();
    }

    static boolean isSaveEnabled(GuidedAction guidedAction) {
        return guidedAction.isAutoSaveRestoreEnabled() && guidedAction.getId() != -1;
    }

    /* access modifiers changed from: package-private */
    public final void onRestoreActions(List<GuidedAction> list, Bundle bundle) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            GuidedAction guidedAction = list.get(i);
            if (isSaveEnabled(guidedAction)) {
                guidedAction.onRestoreInstanceState(bundle, getAutoRestoreKey(guidedAction));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void onRestoreButtonActions(List<GuidedAction> list, Bundle bundle) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            GuidedAction guidedAction = list.get(i);
            if (isSaveEnabled(guidedAction)) {
                guidedAction.onRestoreInstanceState(bundle, getButtonAutoRestoreKey(guidedAction));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void onSaveActions(List<GuidedAction> list, Bundle bundle) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            GuidedAction guidedAction = list.get(i);
            if (isSaveEnabled(guidedAction)) {
                guidedAction.onSaveInstanceState(bundle, getAutoRestoreKey(guidedAction));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public final void onSaveButtonActions(List<GuidedAction> list, Bundle bundle) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            GuidedAction guidedAction = list.get(i);
            if (isSaveEnabled(guidedAction)) {
                guidedAction.onSaveInstanceState(bundle, getButtonAutoRestoreKey(guidedAction));
            }
        }
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        onSaveActions(this.mActions, bundle);
        onSaveButtonActions(this.mButtonActions, bundle);
    }

    private static boolean isGuidedStepTheme(Context context) {
        int i = C0742R.attr.guidedStepThemeFlag;
        TypedValue typedValue = new TypedValue();
        if (!context.getTheme().resolveAttribute(i, typedValue, true) || typedValue.type != 18 || typedValue.data == 0) {
            return false;
        }
        return true;
    }

    public void finishGuidedStepSupportFragments() {
        FragmentManager fragmentManager = getFragmentManager();
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            for (int i = backStackEntryCount - 1; i >= 0; i--) {
                FragmentManager.BackStackEntry backStackEntryAt = fragmentManager.getBackStackEntryAt(i);
                if (isStackEntryUiStyleEntrance(backStackEntryAt.getName())) {
                    GuidedStepSupportFragment currentGuidedStepSupportFragment = getCurrentGuidedStepSupportFragment(fragmentManager);
                    if (currentGuidedStepSupportFragment != null) {
                        currentGuidedStepSupportFragment.setUiStyle(1);
                    }
                    fragmentManager.popBackStackImmediate(backStackEntryAt.getId(), 1);
                    return;
                }
            }
        }
        ActivityCompat.finishAfterTransition(getActivity());
    }

    public void popBackStackToGuidedStepSupportFragment(Class<?> cls, int i) {
        if (GuidedStepSupportFragment.class.isAssignableFrom(cls)) {
            FragmentManager fragmentManager = getFragmentManager();
            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
            String name = cls.getName();
            if (backStackEntryCount > 0) {
                for (int i2 = backStackEntryCount - 1; i2 >= 0; i2--) {
                    FragmentManager.BackStackEntry backStackEntryAt = fragmentManager.getBackStackEntryAt(i2);
                    if (name.equals(getGuidedStepSupportFragmentClassName(backStackEntryAt.getName()))) {
                        fragmentManager.popBackStackImmediate(backStackEntryAt.getId(), i);
                        return;
                    }
                }
            }
        }
    }

    public void setEntranceTransitionType(int i) {
        this.entranceTransitionType = i;
    }

    public void openInEditMode(GuidedAction guidedAction) {
        this.mActionsStylist.openInEditMode(guidedAction);
    }

    private void resolveTheme() {
        Context context = getContext();
        int onProvideTheme = onProvideTheme();
        if (onProvideTheme == -1 && !isGuidedStepTheme(context)) {
            int i = C0742R.attr.guidedStepTheme;
            TypedValue typedValue = new TypedValue();
            boolean resolveAttribute = context.getTheme().resolveAttribute(i, typedValue, true);
            if (resolveAttribute) {
                ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, typedValue.resourceId);
                if (isGuidedStepTheme(contextThemeWrapper)) {
                    this.mThemeWrapper = contextThemeWrapper;
                } else {
                    this.mThemeWrapper = null;
                    resolveAttribute = false;
                }
            }
            if (!resolveAttribute) {
                Log.e(TAG, "GuidedStepSupportFragment does not have an appropriate theme set.");
            }
        } else if (onProvideTheme != -1) {
            this.mThemeWrapper = new ContextThemeWrapper(context, onProvideTheme);
        }
    }

    private LayoutInflater getThemeInflater(LayoutInflater layoutInflater) {
        ContextThemeWrapper contextThemeWrapper = this.mThemeWrapper;
        if (contextThemeWrapper == null) {
            return layoutInflater;
        }
        return layoutInflater.cloneInContext(contextThemeWrapper);
    }

    /* access modifiers changed from: package-private */
    public void runImeAnimations(boolean z) {
        ArrayList arrayList = new ArrayList();
        if (z) {
            this.mGuidanceStylist.onImeAppearing(arrayList);
            this.mActionsStylist.onImeAppearing(arrayList);
            this.mButtonActionsStylist.onImeAppearing(arrayList);
        } else {
            this.mGuidanceStylist.onImeDisappearing(arrayList);
            this.mActionsStylist.onImeDisappearing(arrayList);
            this.mButtonActionsStylist.onImeDisappearing(arrayList);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(arrayList);
        animatorSet.start();
    }
}
