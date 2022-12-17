package com.android.settings.homepage;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.FeatureFlagUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.window.embedding.SplitController;
import com.android.settings.R$color;
import com.android.settings.R$dimen;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.android.settings.R$string;
import com.android.settings.Settings;
import com.android.settings.SettingsApplication;
import com.android.settings.accounts.AvatarViewMixin;
import com.android.settings.activityembedding.ActivityEmbeddingRulesController;
import com.android.settings.activityembedding.ActivityEmbeddingUtils;
import com.android.settings.core.CategoryMixin;
import com.android.settings.homepage.contextualcards.ContextualCardsFragment;
import com.android.settings.overlay.FeatureFactory;
import com.android.settingslib.Utils;
import com.android.settingslib.core.lifecycle.HideNonSystemOverlayMixin;
import java.net.URISyntaxException;
import java.util.Set;

public class SettingsHomepageActivity extends FragmentActivity implements CategoryMixin.CategoryHandler {
    static final int DEFAULT_HIGHLIGHT_MENU_KEY = R$string.menu_key_network;
    private CategoryMixin mCategoryMixin;
    private View mHomepageView;
    private boolean mIsEmbeddingActivityEnabled;
    private boolean mIsRegularLayout = true;
    private boolean mIsTwoPane;
    private Set<HomepageLoadedListener> mLoadedListeners;
    private TopLevelSettings mMainFragment;
    private SplitController mSplitController;
    private View mSuggestionView;
    private View mTwoPaneSuggestionView;

    private interface FragmentCreator<T extends Fragment> {
        T create();

        void init(Fragment fragment) {
        }
    }

    public interface HomepageLoadedListener {
        void onHomepageLoaded();
    }

    public boolean addHomepageLoadedListener(HomepageLoadedListener homepageLoadedListener) {
        if (this.mHomepageView == null) {
            return false;
        }
        if (this.mLoadedListeners.contains(homepageLoadedListener)) {
            return true;
        }
        this.mLoadedListeners.add(homepageLoadedListener);
        return true;
    }

    public void showHomepageWithSuggestion(boolean z) {
        if (this.mHomepageView != null) {
            Log.i("SettingsHomepageActivity", "showHomepageWithSuggestion: " + z);
            View view = this.mHomepageView;
            int i = 8;
            this.mSuggestionView.setVisibility(z ? 0 : 8);
            View view2 = this.mTwoPaneSuggestionView;
            if (z) {
                i = 0;
            }
            view2.setVisibility(i);
            this.mHomepageView = null;
            this.mLoadedListeners.forEach(new SettingsHomepageActivity$$ExternalSyntheticLambda5());
            this.mLoadedListeners.clear();
            view.setVisibility(0);
        }
    }

    public TopLevelSettings getMainFragment() {
        return this.mMainFragment;
    }

    public CategoryMixin getCategoryMixin() {
        return this.mCategoryMixin;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        boolean isEmbeddingActivityEnabled = ActivityEmbeddingUtils.isEmbeddingActivityEnabled(this);
        this.mIsEmbeddingActivityEnabled = isEmbeddingActivityEnabled;
        if (isEmbeddingActivityEnabled) {
            UserManager userManager = (UserManager) getSystemService(UserManager.class);
            if (userManager.getUserInfo(getUser().getIdentifier()).isManagedProfile()) {
                Intent putExtra = new Intent(getIntent()).setClass(this, DeepLinkHomepageActivityInternal.class).addFlags(33554432).putExtra("user_handle", getUser());
                putExtra.removeFlags(268435456);
                startActivityAsUser(putExtra, userManager.getPrimaryUser().getUserHandle());
                finish();
                return;
            }
        }
        setupEdgeToEdge();
        setContentView(R$layout.settings_homepage_container);
        SplitController instance = SplitController.getInstance();
        this.mSplitController = instance;
        this.mIsTwoPane = instance.isActivityEmbedded(this);
        updateAppBarMinHeight();
        initHomepageContainer();
        updateHomepageAppBar();
        updateHomepageBackground();
        this.mLoadedListeners = new ArraySet();
        initSearchBarView();
        getLifecycle().addObserver(new HideNonSystemOverlayMixin(this));
        this.mCategoryMixin = new CategoryMixin(this);
        getLifecycle().addObserver(this.mCategoryMixin);
        String highlightMenuKey = getHighlightMenuKey();
        if (!((ActivityManager) getSystemService(ActivityManager.class)).isLowRamDevice()) {
            initAvatarView();
            showSuggestionFragment(this.mIsEmbeddingActivityEnabled && !TextUtils.equals(getString(DEFAULT_HIGHLIGHT_MENU_KEY), highlightMenuKey));
            if (FeatureFlagUtils.isEnabled(this, "settings_contextual_home")) {
                showFragment(new SettingsHomepageActivity$$ExternalSyntheticLambda0(), R$id.contextual_cards_content);
                ((FrameLayout) findViewById(R$id.main_content)).getLayoutTransition().enableTransitionType(4);
            }
        }
        this.mMainFragment = (TopLevelSettings) showFragment(new SettingsHomepageActivity$$ExternalSyntheticLambda1(highlightMenuKey), R$id.main_content);
        launchDeepLinkIntentToRight();
        updateHomepagePaddings();
        updateSplitLayout();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ ContextualCardsFragment lambda$onCreate$1() {
        return new ContextualCardsFragment();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ TopLevelSettings lambda$onCreate$2(String str) {
        TopLevelSettings topLevelSettings = new TopLevelSettings();
        topLevelSettings.getArguments().putString(":settings:fragment_args_key", str);
        return topLevelSettings;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        ((SettingsApplication) getApplication()).setHomeActivity(this);
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        reloadHighlightMenuKey();
        if (!isFinishing()) {
            launchDeepLinkIntentToRight();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        boolean isActivityEmbedded = this.mSplitController.isActivityEmbedded(this);
        if (this.mIsTwoPane != isActivityEmbedded) {
            this.mIsTwoPane = isActivityEmbedded;
            updateHomepageAppBar();
            updateHomepageBackground();
            updateHomepagePaddings();
        }
        updateSplitLayout();
    }

    private void updateSplitLayout() {
        int i;
        if (this.mIsEmbeddingActivityEnabled) {
            if (this.mIsTwoPane) {
                if (this.mIsRegularLayout == ActivityEmbeddingUtils.isRegularHomepageLayout(this)) {
                    return;
                }
            } else if (this.mIsRegularLayout) {
                return;
            }
            this.mIsRegularLayout = !this.mIsRegularLayout;
            View findViewById = findViewById(R$id.search_bar_title);
            if (findViewById != null) {
                Resources resources = getResources();
                if (this.mIsRegularLayout) {
                    i = R$dimen.search_bar_title_padding_start_regular_two_pane;
                } else {
                    i = R$dimen.search_bar_title_padding_start;
                }
                findViewById.setPaddingRelative(resources.getDimensionPixelSize(i), 0, 0, 0);
            }
            getSupportFragmentManager().getFragments().forEach(new SettingsHomepageActivity$$ExternalSyntheticLambda4(this));
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateSplitLayout$3(Fragment fragment) {
        if (fragment instanceof SplitLayoutListener) {
            ((SplitLayoutListener) fragment).onSplitLayoutChanged(this.mIsRegularLayout);
        }
    }

    private void setupEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(16908290), new SettingsHomepageActivity$$ExternalSyntheticLambda2());
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ WindowInsetsCompat lambda$setupEdgeToEdge$4(View view, WindowInsetsCompat windowInsetsCompat) {
        Insets insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars());
        view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
        return WindowInsetsCompat.CONSUMED;
    }

    private void initSearchBarView() {
        FeatureFactory.getFactory(this).getSearchFeatureProvider().initSearchToolbar(this, (Toolbar) findViewById(R$id.search_action_bar), 1502);
        if (this.mIsEmbeddingActivityEnabled) {
            FeatureFactory.getFactory(this).getSearchFeatureProvider().initSearchToolbar(this, (Toolbar) findViewById(R$id.search_action_bar_two_pane), 1502);
        }
    }

    private void initAvatarView() {
        ImageView imageView = (ImageView) findViewById(R$id.account_avatar);
        ImageView imageView2 = (ImageView) findViewById(R$id.account_avatar_two_pane_version);
        if (AvatarViewMixin.isAvatarSupported(this)) {
            imageView.setVisibility(0);
            getLifecycle().addObserver(new AvatarViewMixin(this, imageView));
            if (this.mIsEmbeddingActivityEnabled) {
                imageView2.setVisibility(0);
                getLifecycle().addObserver(new AvatarViewMixin(this, imageView2));
            }
        }
    }

    private void updateHomepageBackground() {
        int i;
        if (this.mIsEmbeddingActivityEnabled) {
            Window window = getWindow();
            if (this.mIsTwoPane) {
                i = getColor(R$color.settings_two_pane_background_color);
            } else {
                i = Utils.getColorAttrDefaultColor(this, 16842801);
            }
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(i);
            findViewById(16908290).setBackgroundColor(i);
        }
    }

    private void showSuggestionFragment(boolean z) {
        Class<? extends Fragment> contextualSuggestionFragment = FeatureFactory.getFactory(this).getSuggestionFeatureProvider(this).getContextualSuggestionFragment();
        if (contextualSuggestionFragment != null) {
            int i = R$id.suggestion_content;
            this.mSuggestionView = findViewById(i);
            int i2 = R$id.two_pane_suggestion_content;
            this.mTwoPaneSuggestionView = findViewById(i2);
            View findViewById = findViewById(R$id.settings_homepage_container);
            this.mHomepageView = findViewById;
            findViewById.setVisibility(z ? 4 : 8);
            this.mHomepageView.postDelayed(new SettingsHomepageActivity$$ExternalSyntheticLambda3(this), 300);
            showFragment(new SuggestionFragCreator(contextualSuggestionFragment, false), i);
            if (this.mIsEmbeddingActivityEnabled) {
                showFragment(new SuggestionFragCreator(contextualSuggestionFragment, true), i2);
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showSuggestionFragment$5() {
        showHomepageWithSuggestion(false);
    }

    private <T extends Fragment> T showFragment(FragmentCreator<T> fragmentCreator, int i) {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        T findFragmentById = supportFragmentManager.findFragmentById(i);
        if (findFragmentById == null) {
            findFragmentById = fragmentCreator.create();
            fragmentCreator.init(findFragmentById);
            beginTransaction.add(i, (Fragment) findFragmentById);
        } else {
            fragmentCreator.init(findFragmentById);
            beginTransaction.show(findFragmentById);
        }
        beginTransaction.commit();
        return findFragmentById;
    }

    private void launchDeepLinkIntentToRight() {
        Intent intent;
        if (!this.mIsEmbeddingActivityEnabled || (intent = getIntent()) == null || !TextUtils.equals(intent.getAction(), "android.settings.SETTINGS_EMBED_DEEP_LINK_ACTIVITY")) {
            return;
        }
        if ((this instanceof DeepLinkHomepageActivity) || (this instanceof DeepLinkHomepageActivityInternal)) {
            String stringExtra = intent.getStringExtra("android.provider.extra.SETTINGS_EMBEDDED_DEEP_LINK_INTENT_URI");
            if (TextUtils.isEmpty(stringExtra)) {
                Log.e("SettingsHomepageActivity", "No EXTRA_SETTINGS_EMBEDDED_DEEP_LINK_INTENT_URI to deep link");
                finish();
                return;
            }
            try {
                Intent parseUri = Intent.parseUri(stringExtra, 1);
                ComponentName resolveActivity = parseUri.resolveActivity(getPackageManager());
                if (resolveActivity == null) {
                    Log.e("SettingsHomepageActivity", "No valid target for the deep link intent: " + parseUri);
                    finish();
                    return;
                }
                parseUri.setComponent(resolveActivity);
                intent.setAction((String) null);
                parseUri.removeFlags(268959744);
                parseUri.addFlags(33554432);
                parseUri.replaceExtras(intent);
                parseUri.putExtra("is_from_settings_homepage", true);
                parseUri.putExtra("is_from_slice", false);
                parseUri.setData((Uri) intent.getParcelableExtra("settings_large_screen_deep_link_intent_data"));
                ComponentName componentName = resolveActivity;
                ActivityEmbeddingRulesController.registerTwoPanePairRule(this, new ComponentName(getApplicationContext(), getClass()), componentName, parseUri.getAction(), 1, 1, true);
                ActivityEmbeddingRulesController.registerTwoPanePairRule(this, new ComponentName(getApplicationContext(), Settings.class), componentName, parseUri.getAction(), 1, 1, true);
                UserHandle userHandle = (UserHandle) intent.getParcelableExtra("user_handle", UserHandle.class);
                if (userHandle != null) {
                    startActivityAsUser(parseUri, userHandle);
                } else {
                    startActivity(parseUri);
                }
            } catch (URISyntaxException e) {
                Log.e("SettingsHomepageActivity", "Failed to parse deep link intent: " + e);
                finish();
            }
        } else {
            Log.e("SettingsHomepageActivity", "Not a deep link component");
            finish();
        }
    }

    private String getHighlightMenuKey() {
        Intent intent = getIntent();
        if (intent != null && TextUtils.equals(intent.getAction(), "android.settings.SETTINGS_EMBED_DEEP_LINK_ACTIVITY")) {
            String stringExtra = intent.getStringExtra("android.provider.extra.SETTINGS_EMBEDDED_DEEP_LINK_HIGHLIGHT_MENU_KEY");
            if (!TextUtils.isEmpty(stringExtra)) {
                return stringExtra;
            }
        }
        return getString(DEFAULT_HIGHLIGHT_MENU_KEY);
    }

    private void reloadHighlightMenuKey() {
        this.mMainFragment.getArguments().putString(":settings:fragment_args_key", getHighlightMenuKey());
        this.mMainFragment.reloadHighlightMenuKey();
    }

    private void initHomepageContainer() {
        View findViewById = findViewById(R$id.homepage_container);
        findViewById.setFocusableInTouchMode(true);
        findViewById.requestFocus();
    }

    private void updateHomepageAppBar() {
        if (this.mIsEmbeddingActivityEnabled) {
            updateAppBarMinHeight();
            if (this.mIsTwoPane) {
                findViewById(R$id.homepage_app_bar_regular_phone_view).setVisibility(8);
                findViewById(R$id.homepage_app_bar_two_pane_view).setVisibility(0);
                findViewById(R$id.suggestion_container_two_pane).setVisibility(0);
                return;
            }
            findViewById(R$id.homepage_app_bar_regular_phone_view).setVisibility(0);
            findViewById(R$id.homepage_app_bar_two_pane_view).setVisibility(8);
            findViewById(R$id.suggestion_container_two_pane).setVisibility(8);
        }
    }

    private void updateHomepagePaddings() {
        if (this.mIsEmbeddingActivityEnabled) {
            if (this.mIsTwoPane) {
                this.mMainFragment.setPaddingHorizontal(getResources().getDimensionPixelSize(R$dimen.homepage_padding_horizontal_two_pane));
            } else {
                this.mMainFragment.setPaddingHorizontal(0);
            }
            this.mMainFragment.updatePreferencePadding(this.mIsTwoPane);
        }
    }

    private void updateAppBarMinHeight() {
        int i;
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.search_bar_height);
        Resources resources = getResources();
        if (!this.mIsEmbeddingActivityEnabled || !this.mIsTwoPane) {
            i = R$dimen.search_bar_margin;
        } else {
            i = R$dimen.homepage_app_bar_padding_two_pane;
        }
        findViewById(R$id.app_bar_container).setMinimumHeight(dimensionPixelSize + (resources.getDimensionPixelSize(i) * 2));
    }

    private static class SuggestionFragCreator implements FragmentCreator {
        private final Class<? extends Fragment> mClass;
        private final boolean mIsTwoPaneLayout;

        SuggestionFragCreator(Class<? extends Fragment> cls, boolean z) {
            this.mClass = cls;
            this.mIsTwoPaneLayout = z;
        }

        public Fragment create() {
            try {
                return (Fragment) this.mClass.getConstructor(new Class[0]).newInstance(new Object[0]);
            } catch (Exception e) {
                Log.w("SettingsHomepageActivity", "Cannot show fragment", e);
                return null;
            }
        }

        public void init(Fragment fragment) {
            if (fragment instanceof SplitLayoutListener) {
                ((SplitLayoutListener) fragment).setSplitLayoutSupported(this.mIsTwoPaneLayout);
            }
        }
    }
}
