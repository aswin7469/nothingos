package com.android.internal.app;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.ActivityThread;
import android.app.VoiceInteractor;
import android.app.admin.DevicePolicyEventLogger;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.PermissionChecker;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Insets;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PatternMatcher;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.provider.SettingsStringUtil;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.R;
import com.android.internal.app.AbstractMultiProfilePagerAdapter;
import com.android.internal.app.ResolverListAdapter;
import com.android.internal.app.chooser.ChooserTargetInfo;
import com.android.internal.app.chooser.DisplayResolveInfo;
import com.android.internal.app.chooser.TargetInfo;
import com.android.internal.content.PackageMonitor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.widget.ResolverDrawerLayout;
import com.android.internal.widget.ViewPager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes4.dex */
public class ResolverActivity extends Activity implements ResolverListAdapter.ResolverListCommunicator {
    private static final boolean DEBUG = false;
    public static boolean ENABLE_TABBED_VIEW = true;
    static final String EXTRA_CALLING_USER = "com.android.internal.app.ResolverActivity.EXTRA_CALLING_USER";
    private static final String EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key";
    public static final String EXTRA_IS_AUDIO_CAPTURE_DEVICE = "is_audio_capture_device";
    static final String EXTRA_SELECTED_PROFILE = "com.android.internal.app.ResolverActivity.EXTRA_SELECTED_PROFILE";
    private static final String EXTRA_SHOW_FRAGMENT_ARGS = ":settings:show_fragment_args";
    private static final String LAST_SHOWN_TAB_KEY = "last_shown_tab_key";
    protected static final String METRICS_CATEGORY_CHOOSER = "intent_chooser";
    protected static final String METRICS_CATEGORY_RESOLVER = "intent_resolver";
    private static final String OPEN_LINKS_COMPONENT_KEY = "app_link_state";
    static final int PROFILE_PERSONAL = 0;
    static final int PROFILE_WORK = 1;
    private static final String TAB_TAG_PERSONAL = "personal";
    private static final String TAB_TAG_WORK = "work";
    private static final String TAG = "ResolverActivity";
    private Button mAlwaysButton;
    private int mDefaultTitleResId;
    private UserHandle mHeaderCreatorUser;
    protected int mLaunchedFromUid;
    private int mLayoutId;
    protected AbstractMultiProfilePagerAdapter mMultiProfilePagerAdapter;
    private Button mOnceButton;
    private PackageMonitor mPersonalPackageMonitor;
    private PickTargetOptionRequest mPickOptionRequest;
    protected PackageManager mPm;
    protected View mProfileView;
    private String mReferrerPackage;
    private boolean mRegistered;
    protected ResolverDrawerLayout mResolverDrawerLayout;
    private boolean mRetainInOnStop;
    private boolean mSafeForwardingMode;
    protected boolean mSupportsAlwaysUseOption;
    private CharSequence mTitle;
    private PackageMonitor mWorkPackageMonitor;
    private BroadcastReceiver mWorkProfileStateReceiver;
    private UserHandle mWorkProfileUserHandle;
    private int mLastSelected = -1;
    private boolean mResolvingHome = false;
    private int mProfileSwitchMessageId = -1;
    protected final ArrayList<Intent> mIntents = new ArrayList<>();
    protected Insets mSystemWindowInsets = null;
    private Space mFooterSpacer = null;
    private boolean mWorkProfileHasBeenEnabled = false;

    public static int getLabelRes(String action) {
        return ActionTitle.forAction(action).labelRes;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes4.dex */
    public enum ActionTitle {
        VIEW("android.intent.action.VIEW", R.string.whichViewApplication, R.string.whichViewApplicationNamed, R.string.whichViewApplicationLabel),
        EDIT(Intent.ACTION_EDIT, R.string.whichEditApplication, R.string.whichEditApplicationNamed, R.string.whichEditApplicationLabel),
        SEND(Intent.ACTION_SEND, R.string.whichSendApplication, R.string.whichSendApplicationNamed, R.string.whichSendApplicationLabel),
        SENDTO(Intent.ACTION_SENDTO, R.string.whichSendToApplication, R.string.whichSendToApplicationNamed, R.string.whichSendToApplicationLabel),
        SEND_MULTIPLE(Intent.ACTION_SEND_MULTIPLE, R.string.whichSendApplication, R.string.whichSendApplicationNamed, R.string.whichSendApplicationLabel),
        CAPTURE_IMAGE("android.media.action.IMAGE_CAPTURE", R.string.whichImageCaptureApplication, R.string.whichImageCaptureApplicationNamed, R.string.whichImageCaptureApplicationLabel),
        DEFAULT(null, R.string.whichApplication, R.string.whichApplicationNamed, R.string.whichApplicationLabel),
        HOME(Intent.ACTION_MAIN, R.string.whichHomeApplication, R.string.whichHomeApplicationNamed, R.string.whichHomeApplicationLabel);
        
        public static final int BROWSABLE_APP_TITLE_RES = 17041639;
        public static final int BROWSABLE_HOST_APP_TITLE_RES = 17041637;
        public static final int BROWSABLE_HOST_TITLE_RES = 17041636;
        public static final int BROWSABLE_TITLE_RES = 17041638;
        public final String action;
        public final int labelRes;
        public final int namedTitleRes;
        public final int titleRes;

        ActionTitle(String action, int titleRes, int namedTitleRes, int labelRes) {
            this.action = action;
            this.titleRes = titleRes;
            this.namedTitleRes = namedTitleRes;
            this.labelRes = labelRes;
        }

        public static ActionTitle forAction(String action) {
            ActionTitle[] values;
            for (ActionTitle title : values()) {
                if (title != HOME && action != null && action.equals(title.action)) {
                    return title;
                }
            }
            return DEFAULT;
        }
    }

    protected PackageMonitor createPackageMonitor(final ResolverListAdapter listAdapter) {
        return new PackageMonitor() { // from class: com.android.internal.app.ResolverActivity.1
            @Override // com.android.internal.content.PackageMonitor
            public void onSomePackagesChanged() {
                listAdapter.handlePackagesChanged();
                ResolverActivity.this.updateProfileViewButton();
            }

            @Override // com.android.internal.content.PackageMonitor
            public boolean onPackageChanged(String packageName, int uid, String[] components) {
                return true;
            }
        };
    }

    private Intent makeMyIntent() {
        Intent intent = new Intent(getIntent());
        intent.setComponent(null);
        intent.setFlags(intent.getFlags() & (-8388609));
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = makeMyIntent();
        Set<String> categories = intent.getCategories();
        if (Intent.ACTION_MAIN.equals(intent.getAction()) && categories != null && categories.size() == 1 && categories.contains(Intent.CATEGORY_HOME)) {
            this.mResolvingHome = true;
        }
        setSafeForwardingMode(true);
        onCreate(savedInstanceState, intent, null, 0, null, null, true);
    }

    protected void onCreate(Bundle savedInstanceState, Intent intent, CharSequence title, Intent[] initialIntents, List<ResolveInfo> rList, boolean supportsAlwaysUseOption) {
        onCreate(savedInstanceState, intent, title, 0, initialIntents, rList, supportsAlwaysUseOption);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState, Intent intent, CharSequence title, int defaultTitleRes, Intent[] initialIntents, List<ResolveInfo> rList, boolean supportsAlwaysUseOption) {
        int i;
        setTheme(appliedThemeResId());
        super.onCreate(savedInstanceState);
        setProfileSwitchMessageId(intent.getContentUserHint());
        int launchedFromUid = getLaunchedFromUid();
        this.mLaunchedFromUid = launchedFromUid;
        if (launchedFromUid < 0 || UserHandle.isIsolated(launchedFromUid)) {
            finish();
            return;
        }
        this.mPm = getPackageManager();
        this.mReferrerPackage = getReferrerPackageName();
        this.mIntents.add(0, new Intent(intent));
        this.mTitle = title;
        this.mDefaultTitleResId = defaultTitleRes;
        this.mSupportsAlwaysUseOption = supportsAlwaysUseOption;
        this.mWorkProfileUserHandle = fetchWorkProfileUserProfile();
        boolean filterLastUsed = this.mSupportsAlwaysUseOption && !isVoiceInteraction() && !shouldShowTabs();
        this.mMultiProfilePagerAdapter = createMultiProfilePagerAdapter(initialIntents, rList, filterLastUsed);
        if (configureContentView()) {
            return;
        }
        PackageMonitor createPackageMonitor = createPackageMonitor(this.mMultiProfilePagerAdapter.getPersonalListAdapter());
        this.mPersonalPackageMonitor = createPackageMonitor;
        createPackageMonitor.register((Context) this, getMainLooper(), getPersonalProfileUserHandle(), false);
        if (shouldShowTabs()) {
            PackageMonitor createPackageMonitor2 = createPackageMonitor(this.mMultiProfilePagerAdapter.getWorkListAdapter());
            this.mWorkPackageMonitor = createPackageMonitor2;
            createPackageMonitor2.register((Context) this, getMainLooper(), getWorkProfileUserHandle(), false);
        }
        this.mRegistered = true;
        ResolverDrawerLayout rdl = (ResolverDrawerLayout) findViewById(R.id.contentPanel);
        if (rdl != null) {
            rdl.setOnDismissedListener(new ResolverDrawerLayout.OnDismissedListener() { // from class: com.android.internal.app.ResolverActivity.2
                @Override // com.android.internal.widget.ResolverDrawerLayout.OnDismissedListener
                public void onDismissed() {
                    ResolverActivity.this.finish();
                }
            });
            boolean hasTouchScreen = getPackageManager().hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN);
            if (isVoiceInteraction() || !hasTouchScreen) {
                rdl.setCollapsed(false);
            }
            rdl.setSystemUiVisibility(768);
            rdl.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() { // from class: com.android.internal.app.ResolverActivity$$ExternalSyntheticLambda0
                @Override // android.view.View.OnApplyWindowInsetsListener
                public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
                    return ResolverActivity.this.onApplyWindowInsets(view, windowInsets);
                }
            });
            this.mResolverDrawerLayout = rdl;
        }
        View findViewById = findViewById(R.id.profile_button);
        this.mProfileView = findViewById;
        if (findViewById != null) {
            findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.android.internal.app.ResolverActivity$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    ResolverActivity.this.onProfileClick(view);
                }
            });
            updateProfileViewButton();
        }
        Set<String> categories = intent.getCategories();
        if (this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().hasFilteredItem()) {
            i = MetricsProto.MetricsEvent.ACTION_SHOW_APP_DISAMBIG_APP_FEATURED;
        } else {
            i = MetricsProto.MetricsEvent.ACTION_SHOW_APP_DISAMBIG_NONE_FEATURED;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(intent.getAction());
        sb.append(SettingsStringUtil.DELIMITER);
        sb.append(intent.getType());
        sb.append(SettingsStringUtil.DELIMITER);
        sb.append(categories != null ? Arrays.toString(categories.toArray()) : "");
        MetricsLogger.action(this, i, sb.toString());
    }

    private boolean isIntentPicker() {
        return getClass().equals(ResolverActivity.class);
    }

    protected AbstractMultiProfilePagerAdapter createMultiProfilePagerAdapter(Intent[] initialIntents, List<ResolveInfo> rList, boolean filterLastUsed) {
        if (shouldShowTabs()) {
            AbstractMultiProfilePagerAdapter resolverMultiProfilePagerAdapter = createResolverMultiProfilePagerAdapterForTwoProfiles(initialIntents, rList, filterLastUsed);
            return resolverMultiProfilePagerAdapter;
        }
        AbstractMultiProfilePagerAdapter resolverMultiProfilePagerAdapter2 = createResolverMultiProfilePagerAdapterForOneProfile(initialIntents, rList, filterLastUsed);
        return resolverMultiProfilePagerAdapter2;
    }

    private ResolverMultiProfilePagerAdapter createResolverMultiProfilePagerAdapterForOneProfile(Intent[] initialIntents, List<ResolveInfo> rList, boolean filterLastUsed) {
        ResolverListAdapter adapter = createResolverListAdapter(this, this.mIntents, initialIntents, rList, filterLastUsed, UserHandle.of(UserHandle.myUserId()));
        return new ResolverMultiProfilePagerAdapter(this, adapter, getPersonalProfileUserHandle(), null);
    }

    private ResolverMultiProfilePagerAdapter createResolverMultiProfilePagerAdapterForTwoProfiles(Intent[] initialIntents, List<ResolveInfo> rList, boolean filterLastUsed) {
        UserHandle user;
        int selectedProfile;
        int selectedProfile2 = getCurrentProfile();
        if (getIntent().hasExtra(EXTRA_CALLING_USER)) {
            user = (UserHandle) getIntent().getParcelableExtra(EXTRA_CALLING_USER);
        } else {
            user = getUser();
        }
        UserHandle intentUser = user;
        if (!getUser().equals(intentUser)) {
            if (getPersonalProfileUserHandle().equals(intentUser)) {
                selectedProfile = 0;
            } else {
                if (getWorkProfileUserHandle().equals(intentUser)) {
                    selectedProfile = 1;
                }
                selectedProfile = selectedProfile2;
            }
        } else {
            int selectedProfileExtra = getSelectedProfileExtra();
            if (selectedProfileExtra != -1) {
                selectedProfile = selectedProfileExtra;
            }
            selectedProfile = selectedProfile2;
        }
        ResolverListAdapter personalAdapter = createResolverListAdapter(this, this.mIntents, selectedProfile == 0 ? initialIntents : null, rList, filterLastUsed && UserHandle.myUserId() == getPersonalProfileUserHandle().getIdentifier(), getPersonalProfileUserHandle());
        UserHandle workProfileUserHandle = getWorkProfileUserHandle();
        ResolverListAdapter workAdapter = createResolverListAdapter(this, this.mIntents, selectedProfile == 1 ? initialIntents : null, rList, filterLastUsed && UserHandle.myUserId() == workProfileUserHandle.getIdentifier(), workProfileUserHandle);
        return new ResolverMultiProfilePagerAdapter(this, personalAdapter, workAdapter, selectedProfile, getPersonalProfileUserHandle(), getWorkProfileUserHandle(), getUser().equals(intentUser));
    }

    protected int appliedThemeResId() {
        return R.style.Theme_DeviceDefault_Resolver;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int getSelectedProfileExtra() {
        int selectedProfile = -1;
        if (getIntent().hasExtra(EXTRA_SELECTED_PROFILE) && (selectedProfile = getIntent().getIntExtra(EXTRA_SELECTED_PROFILE, -1)) != 0 && selectedProfile != 1) {
            throw new IllegalArgumentException("com.android.internal.app.ResolverActivity.EXTRA_SELECTED_PROFILE has invalid value " + selectedProfile + ". Must be either ResolverActivity.PROFILE_PERSONAL or ResolverActivity.PROFILE_WORK.");
        }
        return selectedProfile;
    }

    protected int getCurrentProfile() {
        return UserHandle.myUserId() == 0 ? 0 : 1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public UserHandle getPersonalProfileUserHandle() {
        return UserHandle.of(ActivityManager.getCurrentUser());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public UserHandle getWorkProfileUserHandle() {
        return this.mWorkProfileUserHandle;
    }

    protected UserHandle fetchWorkProfileUserProfile() {
        this.mWorkProfileUserHandle = null;
        UserManager userManager = (UserManager) getSystemService(UserManager.class);
        for (UserInfo userInfo : userManager.getProfiles(ActivityManager.getCurrentUser())) {
            if (userInfo.isManagedProfile()) {
                this.mWorkProfileUserHandle = userInfo.getUserHandle();
            }
        }
        return this.mWorkProfileUserHandle;
    }

    private boolean hasWorkProfile() {
        return getWorkProfileUserHandle() != null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean shouldShowTabs() {
        return hasWorkProfile() && ENABLE_TABBED_VIEW;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void onProfileClick(View v) {
        DisplayResolveInfo dri = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getOtherProfile();
        if (dri == null) {
            return;
        }
        this.mProfileSwitchMessageId = -1;
        onTargetSelected(dri, false);
        finish();
    }

    protected boolean shouldAddFooterView() {
        View buttonBar;
        return useLayoutWithDefault() || (buttonBar = findViewById(R.id.button_bar)) == null || buttonBar.getVisibility() == 8;
    }

    protected void applyFooterView(int height) {
        if (this.mFooterSpacer == null) {
            this.mFooterSpacer = new Space(getApplicationContext());
        } else {
            ((ResolverMultiProfilePagerAdapter) this.mMultiProfilePagerAdapter).mo3321getActiveAdapterView().removeFooterView(this.mFooterSpacer);
        }
        this.mFooterSpacer.setLayoutParams(new AbsListView.LayoutParams(-1, this.mSystemWindowInsets.bottom));
        ((ResolverMultiProfilePagerAdapter) this.mMultiProfilePagerAdapter).mo3321getActiveAdapterView().addFooterView(this.mFooterSpacer);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
        Insets systemWindowInsets = insets.getSystemWindowInsets();
        this.mSystemWindowInsets = systemWindowInsets;
        this.mResolverDrawerLayout.setPadding(systemWindowInsets.left, this.mSystemWindowInsets.top, this.mSystemWindowInsets.right, 0);
        resetButtonBar();
        if (shouldAddFooterView()) {
            applyFooterView(this.mSystemWindowInsets.bottom);
        }
        return insets.consumeSystemWindowInsets();
    }

    @Override // android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().handlePackagesChanged();
        if (isIntentPicker() && shouldShowTabs() && !useLayoutWithDefault()) {
            updateIntentPickerPaddings();
        }
        Insets insets = this.mSystemWindowInsets;
        if (insets != null) {
            this.mResolverDrawerLayout.setPadding(insets.left, this.mSystemWindowInsets.top, this.mSystemWindowInsets.right, 0);
        }
    }

    private void updateIntentPickerPaddings() {
        View titleCont = findViewById(R.id.title_container);
        titleCont.setPadding(titleCont.getPaddingLeft(), titleCont.getPaddingTop(), titleCont.getPaddingRight(), getResources().getDimensionPixelSize(R.dimen.resolver_title_padding_bottom));
        View buttonBar = findViewById(R.id.button_bar);
        buttonBar.setPadding(buttonBar.getPaddingLeft(), getResources().getDimensionPixelSize(R.dimen.resolver_button_bar_spacing), buttonBar.getPaddingRight(), getResources().getDimensionPixelSize(R.dimen.resolver_button_bar_spacing));
        this.mMultiProfilePagerAdapter.updateAfterConfigChange();
    }

    @Override // com.android.internal.app.ResolverListAdapter.ResolverListCommunicator
    public void sendVoiceChoicesIfNeeded() {
        if (!isVoiceInteraction()) {
            return;
        }
        int count = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getCount();
        VoiceInteractor.PickOptionRequest.Option[] options = new VoiceInteractor.PickOptionRequest.Option[count];
        int N = options.length;
        for (int i = 0; i < N; i++) {
            TargetInfo target = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().mo3505getItem(i);
            if (target == null) {
                return;
            }
            options[i] = optionForChooserTarget(target, i);
        }
        this.mPickOptionRequest = new PickTargetOptionRequest(new VoiceInteractor.Prompt(getTitle()), options, null);
        getVoiceInteractor().submitRequest(this.mPickOptionRequest);
    }

    VoiceInteractor.PickOptionRequest.Option optionForChooserTarget(TargetInfo target, int index) {
        return new VoiceInteractor.PickOptionRequest.Option(target.getDisplayLabel(), index);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void setAdditionalTargets(Intent[] intents) {
        if (intents != null) {
            for (Intent intent : intents) {
                this.mIntents.add(intent);
            }
        }
    }

    @Override // com.android.internal.app.ResolverListAdapter.ResolverListCommunicator
    public Intent getTargetIntent() {
        if (this.mIntents.isEmpty()) {
            return null;
        }
        return this.mIntents.get(0);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String getReferrerPackageName() {
        Uri referrer = getReferrer();
        if (referrer != null && "android-app".equals(referrer.getScheme())) {
            return referrer.getHost();
        }
        return null;
    }

    public int getLayoutResource() {
        return R.layout.resolver_list;
    }

    @Override // com.android.internal.app.ResolverListAdapter.ResolverListCommunicator
    public void updateProfileViewButton() {
        if (this.mProfileView == null) {
            return;
        }
        DisplayResolveInfo dri = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getOtherProfile();
        if (dri != null && !shouldShowTabs()) {
            this.mProfileView.setVisibility(0);
            View text = this.mProfileView.findViewById(R.id.profile_button);
            if (!(text instanceof TextView)) {
                text = this.mProfileView.findViewById(16908308);
            }
            ((TextView) text).setText(dri.getDisplayLabel());
            return;
        }
        this.mProfileView.setVisibility(8);
    }

    private void setProfileSwitchMessageId(int contentUserHint) {
        if (contentUserHint != -2 && contentUserHint != UserHandle.myUserId()) {
            UserManager userManager = (UserManager) getSystemService("user");
            UserInfo originUserInfo = userManager.getUserInfo(contentUserHint);
            boolean originIsManaged = originUserInfo != null ? originUserInfo.isManagedProfile() : false;
            boolean targetIsManaged = userManager.isManagedProfile();
            if (originIsManaged && !targetIsManaged) {
                this.mProfileSwitchMessageId = R.string.forward_intent_to_owner;
            } else if (!originIsManaged && targetIsManaged) {
                this.mProfileSwitchMessageId = R.string.forward_intent_to_work;
            }
        }
    }

    public void setSafeForwardingMode(boolean safeForwarding) {
        this.mSafeForwardingMode = safeForwarding;
    }

    protected CharSequence getTitleForAction(Intent intent, int defaultTitleRes) {
        ActionTitle title;
        if (this.mResolvingHome) {
            title = ActionTitle.HOME;
        } else {
            title = ActionTitle.forAction(intent.getAction());
        }
        boolean named = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getFilteredPosition() >= 0;
        if (title == ActionTitle.DEFAULT && defaultTitleRes != 0) {
            return getString(defaultTitleRes);
        }
        if (named) {
            return getString(title.namedTitleRes, this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getFilteredItem().getDisplayLabel());
        }
        return getString(title.titleRes);
    }

    void dismiss() {
        if (!isFinishing()) {
            finish();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onRestart() {
        super.onRestart();
        if (!this.mRegistered) {
            this.mPersonalPackageMonitor.register((Context) this, getMainLooper(), getPersonalProfileUserHandle(), false);
            if (shouldShowTabs()) {
                if (this.mWorkPackageMonitor == null) {
                    this.mWorkPackageMonitor = createPackageMonitor(this.mMultiProfilePagerAdapter.getWorkListAdapter());
                }
                this.mWorkPackageMonitor.register((Context) this, getMainLooper(), getWorkProfileUserHandle(), false);
            }
            this.mRegistered = true;
        }
        if (shouldShowTabs() && this.mMultiProfilePagerAdapter.isWaitingToEnableWorkProfile() && this.mMultiProfilePagerAdapter.isQuietModeEnabled(getWorkProfileUserHandle())) {
            this.mMultiProfilePagerAdapter.markWorkProfileEnabledBroadcastReceived();
        }
        this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().handlePackagesChanged();
        updateProfileViewButton();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        getWindow().addSystemFlags(524288);
        if (shouldShowTabs()) {
            this.mWorkProfileStateReceiver = createWorkProfileStateReceiver();
            registerWorkProfileStateReceiver();
            this.mWorkProfileHasBeenEnabled = isWorkProfileEnabled();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isWorkProfileEnabled() {
        UserHandle workUserHandle = getWorkProfileUserHandle();
        UserManager userManager = (UserManager) getSystemService(UserManager.class);
        return !userManager.isQuietModeEnabled(workUserHandle) && userManager.isUserUnlocked(workUserHandle);
    }

    private void registerWorkProfileStateReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_UNLOCKED);
        filter.addAction(Intent.ACTION_MANAGED_PROFILE_AVAILABLE);
        filter.addAction(Intent.ACTION_MANAGED_PROFILE_UNAVAILABLE);
        registerReceiverAsUser(this.mWorkProfileStateReceiver, UserHandle.ALL, filter, null, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onStop() {
        super.onStop();
        Window window = getWindow();
        WindowManager.LayoutParams attrs = window.getAttributes();
        attrs.privateFlags &= -524289;
        window.setAttributes(attrs);
        if (this.mRegistered) {
            this.mPersonalPackageMonitor.unregister();
            PackageMonitor packageMonitor = this.mWorkPackageMonitor;
            if (packageMonitor != null) {
                packageMonitor.unregister();
            }
            this.mRegistered = false;
        }
        Intent intent = getIntent();
        if ((intent.getFlags() & 268435456) != 0 && !isVoiceInteraction() && !this.mResolvingHome && !this.mRetainInOnStop && !isChangingConfigurations()) {
            finish();
        }
        if (this.mWorkPackageMonitor != null) {
            unregisterReceiver(this.mWorkProfileStateReceiver);
            this.mWorkPackageMonitor = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onDestroy() {
        PickTargetOptionRequest pickTargetOptionRequest;
        super.onDestroy();
        if (!isChangingConfigurations() && (pickTargetOptionRequest = this.mPickOptionRequest) != null) {
            pickTargetOptionRequest.cancel();
        }
        if (this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter() != null) {
            this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().onDestroy();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ViewPager viewPager = (ViewPager) findViewById(R.id.profile_pager);
        outState.putInt(LAST_SHOWN_TAB_KEY, viewPager.getCurrentItem());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.app.Activity
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        resetButtonBar();
        ViewPager viewPager = (ViewPager) findViewById(R.id.profile_pager);
        viewPager.setCurrentItem(savedInstanceState.getInt(LAST_SHOWN_TAB_KEY));
        this.mMultiProfilePagerAdapter.clearInactiveProfileCache();
    }

    private boolean hasManagedProfile() {
        UserManager userManager = (UserManager) getSystemService("user");
        if (userManager == null) {
            return false;
        }
        try {
            List<UserInfo> profiles = userManager.getProfiles(getUserId());
            for (UserInfo userInfo : profiles) {
                if (userInfo != null && userInfo.isManagedProfile()) {
                    return true;
                }
            }
            return false;
        } catch (SecurityException e) {
            return false;
        }
    }

    private boolean supportsManagedProfiles(ResolveInfo resolveInfo) {
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(resolveInfo.activityInfo.packageName, 0);
            return appInfo.targetSdkVersion >= 21;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setAlwaysButtonEnabled(boolean hasValidSelection, int checkedPos, boolean filtered) {
        if (!this.mMultiProfilePagerAdapter.getCurrentUserHandle().equals(getUser())) {
            this.mAlwaysButton.setEnabled(false);
            return;
        }
        boolean enabled = false;
        ResolveInfo ri = null;
        if (hasValidSelection) {
            ri = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().resolveInfoForPosition(checkedPos, filtered);
            if (ri == null) {
                Log.e(TAG, "Invalid position supplied to setAlwaysButtonEnabled");
                return;
            } else if (ri.targetUserId != -2) {
                Log.e(TAG, "Attempted to set selection to resolve info for another user");
                return;
            } else {
                enabled = true;
                this.mAlwaysButton.setText(getResources().getString(R.string.activity_resolver_use_always));
            }
        }
        if (ri != null) {
            ActivityInfo activityInfo = ri.activityInfo;
            boolean hasRecordPermission = this.mPm.checkPermission(Manifest.permission.RECORD_AUDIO, activityInfo.packageName) == 0;
            if (!hasRecordPermission) {
                boolean hasAudioCapture = getIntent().getBooleanExtra(EXTRA_IS_AUDIO_CAPTURE_DEVICE, false);
                enabled = !hasAudioCapture;
            }
        }
        this.mAlwaysButton.setEnabled(enabled);
    }

    public void onButtonClick(View v) {
        int which;
        int id = v.getId();
        ListView listView = (ListView) this.mMultiProfilePagerAdapter.mo3321getActiveAdapterView();
        ResolverListAdapter currentListAdapter = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter();
        if (currentListAdapter.hasFilteredItem()) {
            which = currentListAdapter.getFilteredPosition();
        } else {
            which = listView.getCheckedItemPosition();
        }
        boolean z = true;
        boolean hasIndexBeenFiltered = !currentListAdapter.hasFilteredItem();
        if (id != 16908814) {
            z = false;
        }
        startSelected(which, z, hasIndexBeenFiltered);
    }

    public void startSelected(int which, boolean always, boolean hasIndexBeenFiltered) {
        int i;
        if (isFinishing()) {
            return;
        }
        ResolveInfo ri = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().resolveInfoForPosition(which, hasIndexBeenFiltered);
        if (this.mResolvingHome && hasManagedProfile() && !supportsManagedProfiles(ri)) {
            Toast.makeText(this, String.format(getResources().getString(R.string.activity_resolver_work_profiles_support), ri.activityInfo.loadLabel(getPackageManager()).toString()), 1).show();
            return;
        }
        TargetInfo target = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().targetInfoForPosition(which, hasIndexBeenFiltered);
        if (target != null && onTargetSelected(target, always)) {
            if (always && this.mSupportsAlwaysUseOption) {
                MetricsLogger.action(this, (int) MetricsProto.MetricsEvent.ACTION_APP_DISAMBIG_ALWAYS);
            } else if (this.mSupportsAlwaysUseOption) {
                MetricsLogger.action(this, (int) MetricsProto.MetricsEvent.ACTION_APP_DISAMBIG_JUST_ONCE);
            } else {
                MetricsLogger.action(this, (int) MetricsProto.MetricsEvent.ACTION_APP_DISAMBIG_TAP);
            }
            if (this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().hasFilteredItem()) {
                i = MetricsProto.MetricsEvent.ACTION_HIDE_APP_DISAMBIG_APP_FEATURED;
            } else {
                i = MetricsProto.MetricsEvent.ACTION_HIDE_APP_DISAMBIG_NONE_FEATURED;
            }
            MetricsLogger.action(this, i);
            finish();
        }
    }

    public Intent getReplacementIntent(ActivityInfo aInfo, Intent defIntent) {
        return defIntent;
    }

    @Override // com.android.internal.app.ResolverListAdapter.ResolverListCommunicator
    public final void onPostListReady(ResolverListAdapter listAdapter, boolean doPostProcessing, boolean rebuildCompleted) {
        if (isAutolaunching()) {
            return;
        }
        if (isIntentPicker()) {
            ((ResolverMultiProfilePagerAdapter) this.mMultiProfilePagerAdapter).setUseLayoutWithDefault(useLayoutWithDefault());
        }
        if (this.mMultiProfilePagerAdapter.shouldShowEmptyStateScreen(listAdapter)) {
            this.mMultiProfilePagerAdapter.showEmptyResolverListEmptyState(listAdapter);
        } else {
            this.mMultiProfilePagerAdapter.showListView(listAdapter);
        }
        if ((!rebuildCompleted || !maybeAutolaunchActivity()) && doPostProcessing) {
            maybeCreateHeader(listAdapter);
            resetButtonBar();
            onListRebuilt(listAdapter);
        }
    }

    protected void onListRebuilt(ResolverListAdapter listAdapter) {
        ResolverDrawerLayout rdl;
        int i;
        ItemClickListener listener = new ItemClickListener();
        setupAdapterListView((ListView) this.mMultiProfilePagerAdapter.mo3321getActiveAdapterView(), listener);
        if (shouldShowTabs() && isIntentPicker() && (rdl = (ResolverDrawerLayout) findViewById(R.id.contentPanel)) != null) {
            Resources resources = getResources();
            if (useLayoutWithDefault()) {
                i = R.dimen.resolver_max_collapsed_height_with_default_with_tabs;
            } else {
                i = R.dimen.resolver_max_collapsed_height_with_tabs;
            }
            rdl.setMaxCollapsedHeight(resources.getDimensionPixelSize(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:101:0x01d6  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x01fb A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onTargetSelected(TargetInfo target, boolean always) {
        Intent filterIntent;
        ComponentName[] set;
        int otherProfileMatch;
        String mimeType;
        ResolveInfo ri = target.getResolveInfo();
        Intent intent = target != null ? target.getResolvedIntent() : null;
        if (intent != null && ((this.mSupportsAlwaysUseOption || this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().hasFilteredItem()) && this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getUnfilteredResolveList() != null)) {
            IntentFilter filter = new IntentFilter();
            if (intent.getSelector() != null) {
                filterIntent = intent.getSelector();
            } else {
                filterIntent = intent;
            }
            String action = filterIntent.getAction();
            if (action != null) {
                filter.addAction(action);
            }
            Set<String> categories = filterIntent.getCategories();
            if (categories != null) {
                for (String cat : categories) {
                    filter.addCategory(cat);
                }
            }
            filter.addCategory(Intent.CATEGORY_DEFAULT);
            int cat2 = 268369920 & ri.match;
            Uri data = filterIntent.getData();
            if (cat2 == 6291456 && (mimeType = filterIntent.resolveType(this)) != null) {
                try {
                    filter.addDataType(mimeType);
                } catch (IntentFilter.MalformedMimeTypeException e) {
                    Log.w(TAG, e);
                    filter = null;
                }
            }
            if (data != null && data.getScheme() != null && (cat2 != 6291456 || (!ContentResolver.SCHEME_FILE.equals(data.getScheme()) && !"content".equals(data.getScheme())))) {
                filter.addDataScheme(data.getScheme());
                Iterator<PatternMatcher> pIt = ri.filter.schemeSpecificPartsIterator();
                if (pIt != null) {
                    String ssp = data.getSchemeSpecificPart();
                    while (true) {
                        if (ssp == null || !pIt.hasNext()) {
                            break;
                        }
                        PatternMatcher p = pIt.next();
                        if (p.match(ssp)) {
                            filter.addDataSchemeSpecificPart(p.getPath(), p.getType());
                            break;
                        }
                    }
                }
                Iterator<IntentFilter.AuthorityEntry> aIt = ri.filter.authoritiesIterator();
                if (aIt != null) {
                    while (true) {
                        if (!aIt.hasNext()) {
                            break;
                        }
                        IntentFilter.AuthorityEntry a = aIt.next();
                        if (a.match(data) >= 0) {
                            int port = a.getPort();
                            filter.addDataAuthority(a.getHost(), port >= 0 ? Integer.toString(port) : null);
                        }
                    }
                }
                Iterator<PatternMatcher> pIt2 = ri.filter.pathsIterator();
                if (pIt2 != null) {
                    String path = data.getPath();
                    while (true) {
                        if (path == null || !pIt2.hasNext()) {
                            break;
                        }
                        PatternMatcher p2 = pIt2.next();
                        if (p2.match(path)) {
                            filter.addDataPath(p2.getPath(), p2.getType());
                            break;
                        }
                    }
                }
            }
            if (filter != null) {
                int N = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getUnfilteredResolveList().size();
                boolean needToAddBackProfileForwardingComponent = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getOtherProfile() != null;
                if (!needToAddBackProfileForwardingComponent) {
                    set = new ComponentName[N];
                } else {
                    set = new ComponentName[N + 1];
                }
                int bestMatch = 0;
                int i = 0;
                while (i < N) {
                    Intent filterIntent2 = filterIntent;
                    ResolveInfo r = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getUnfilteredResolveList().get(i).getResolveInfoAt(0);
                    String action2 = action;
                    Set<String> categories2 = categories;
                    set[i] = new ComponentName(r.activityInfo.packageName, r.activityInfo.name);
                    if (r.match > bestMatch) {
                        bestMatch = r.match;
                    }
                    i++;
                    filterIntent = filterIntent2;
                    action = action2;
                    categories = categories2;
                }
                if (needToAddBackProfileForwardingComponent) {
                    set[N] = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getOtherProfile().getResolvedComponentName();
                    otherProfileMatch = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getOtherProfile().getResolveInfo().match;
                    if (otherProfileMatch > bestMatch) {
                        if (!always) {
                            int userId = getUserId();
                            PackageManager pm = getPackageManager();
                            pm.addUniquePreferredActivity(filter, otherProfileMatch, set, intent.getComponent());
                            if (ri.handleAllWebDataURI) {
                                String packageName = pm.getDefaultBrowserPackageNameAsUser(userId);
                                if (TextUtils.isEmpty(packageName)) {
                                    pm.setDefaultBrowserPackageNameAsUser(ri.activityInfo.packageName, userId);
                                }
                            }
                        } else {
                            try {
                                this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().mResolverListController.setLastChosen(intent, filter, otherProfileMatch);
                            } catch (RemoteException re) {
                                Log.d(TAG, "Error calling setLastChosenActivity\n" + re);
                            }
                        }
                    }
                }
                otherProfileMatch = bestMatch;
                if (!always) {
                }
            }
        }
        if (target != null) {
            if (intent != null && isLaunchingTargetInOtherProfile()) {
                prepareIntentForCrossProfileLaunch(intent);
            }
            safelyStartActivity(target);
            if (target.isSuspended()) {
                return false;
            }
            return true;
        }
        return true;
    }

    private void prepareIntentForCrossProfileLaunch(Intent intent) {
        intent.fixUris(UserHandle.myUserId());
    }

    private boolean isLaunchingTargetInOtherProfile() {
        return this.mMultiProfilePagerAdapter.getCurrentUserHandle().getIdentifier() != UserHandle.myUserId();
    }

    public void safelyStartActivity(TargetInfo cti) {
        StrictMode.disableDeathOnFileUriExposure();
        try {
            UserHandle currentUserHandle = this.mMultiProfilePagerAdapter.getCurrentUserHandle();
            safelyStartActivityInternal(cti, currentUserHandle);
        } finally {
            StrictMode.enableDeathOnFileUriExposure();
        }
    }

    public void safelyStartActivityAsUser(TargetInfo cti, UserHandle user) {
        StrictMode.disableDeathOnFileUriExposure();
        try {
            safelyStartActivityInternal(cti, user);
        } finally {
            StrictMode.enableDeathOnFileUriExposure();
        }
    }

    private void safelyStartActivityInternal(TargetInfo cti, UserHandle user) {
        if (!cti.isSuspended() && this.mRegistered) {
            PackageMonitor packageMonitor = this.mPersonalPackageMonitor;
            if (packageMonitor != null) {
                packageMonitor.unregister();
            }
            PackageMonitor packageMonitor2 = this.mWorkPackageMonitor;
            if (packageMonitor2 != null) {
                packageMonitor2.unregister();
            }
            this.mRegistered = false;
        }
        int i = this.mProfileSwitchMessageId;
        if (i != -1) {
            Toast.makeText(this, getString(i), 1).show();
        }
        if (!this.mSafeForwardingMode) {
            if (cti.startAsUser(this, null, user)) {
                onActivityStarted(cti);
                maybeLogCrossProfileTargetLaunch(cti, user);
                return;
            }
            return;
        }
        try {
            if (cti.startAsCaller(this, null, user.getIdentifier())) {
                onActivityStarted(cti);
                maybeLogCrossProfileTargetLaunch(cti, user);
            }
        } catch (RuntimeException e) {
            Slog.wtf(TAG, "Unable to launch as uid " + this.mLaunchedFromUid + " package " + getLaunchedFromPackage() + ", while running in " + ActivityThread.currentProcessName(), e);
        }
    }

    private void maybeLogCrossProfileTargetLaunch(TargetInfo cti, UserHandle currentUserHandle) {
        if (!hasWorkProfile() || currentUserHandle.equals(getUser())) {
            return;
        }
        DevicePolicyEventLogger devicePolicyEventLogger = DevicePolicyEventLogger.createEvent(155).setBoolean(currentUserHandle.equals(getPersonalProfileUserHandle()));
        String[] strArr = new String[2];
        strArr[0] = getMetricsCategory();
        strArr[1] = cti instanceof ChooserTargetInfo ? ChooserActivity.LAUNCH_LOCATION_DIRECT_SHARE : "other_target";
        devicePolicyEventLogger.setStrings(strArr).write();
    }

    public boolean startAsCallerImpl(Intent intent, Bundle options, boolean ignoreTargetSecurity, int userId) {
        try {
            IBinder permissionToken = ActivityTaskManager.getService().requestStartActivityPermissionToken(getActivityToken());
            Intent chooserIntent = new Intent();
            ComponentName delegateActivity = ComponentName.unflattenFromString(Resources.getSystem().getString(R.string.config_chooserActivity));
            chooserIntent.setClassName(delegateActivity.getPackageName(), delegateActivity.getClassName());
            chooserIntent.putExtra(ActivityTaskManager.EXTRA_PERMISSION_TOKEN, permissionToken);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, intent);
            chooserIntent.putExtra(ActivityTaskManager.EXTRA_OPTIONS, options);
            chooserIntent.putExtra(ActivityTaskManager.EXTRA_IGNORE_TARGET_SECURITY, ignoreTargetSecurity);
            chooserIntent.putExtra(Intent.EXTRA_USER_ID, userId);
            chooserIntent.addFlags(50331648);
            startActivity(chooserIntent);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, e.toString());
            return true;
        }
    }

    public void onActivityStarted(TargetInfo cti) {
    }

    public boolean shouldGetActivityMetadata() {
        return false;
    }

    public boolean shouldAutoLaunchSingleChoice(TargetInfo target) {
        return !target.isSuspended();
    }

    void showTargetDetails(ResolveInfo ri) {
        Intent in = new Intent().setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.fromParts("package", ri.activityInfo.packageName, null)).addFlags(524288);
        startActivityAsUser(in, this.mMultiProfilePagerAdapter.getCurrentUserHandle());
    }

    protected ResolverListAdapter createResolverListAdapter(Context context, List<Intent> payloadIntents, Intent[] initialIntents, List<ResolveInfo> rList, boolean filterLastUsed, UserHandle userHandle) {
        Intent startIntent = getIntent();
        boolean isAudioCaptureDevice = startIntent.getBooleanExtra(EXTRA_IS_AUDIO_CAPTURE_DEVICE, false);
        return new ResolverListAdapter(context, payloadIntents, initialIntents, rList, filterLastUsed, createListController(userHandle), this, isAudioCaptureDevice);
    }

    protected ResolverListController createListController(UserHandle userHandle) {
        return new ResolverListController(this, this.mPm, getTargetIntent(), getReferrerPackageName(), this.mLaunchedFromUid, userHandle);
    }

    private boolean configureContentView() {
        if (this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter() == null) {
            throw new IllegalStateException("mMultiProfilePagerAdapter.getCurrentListAdapter() cannot be null.");
        }
        boolean z = true;
        boolean rebuildCompleted = this.mMultiProfilePagerAdapter.rebuildActiveTab(true) || this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().isTabLoaded();
        if (shouldShowTabs()) {
            boolean rebuildInactiveCompleted = this.mMultiProfilePagerAdapter.rebuildInactiveTab(false) || this.mMultiProfilePagerAdapter.mo3314getInactiveListAdapter().isTabLoaded();
            if (!rebuildCompleted || !rebuildInactiveCompleted) {
                z = false;
            }
            rebuildCompleted = z;
        }
        if (useLayoutWithDefault()) {
            this.mLayoutId = R.layout.resolver_list_with_default;
        } else {
            this.mLayoutId = getLayoutResource();
        }
        setContentView(this.mLayoutId);
        this.mMultiProfilePagerAdapter.setupViewPager((ViewPager) findViewById(R.id.profile_pager));
        return postRebuildList(rebuildCompleted);
    }

    protected boolean postRebuildList(boolean rebuildCompleted) {
        return postRebuildListInternal(rebuildCompleted);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean postRebuildListInternal(boolean rebuildCompleted) {
        this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getUnfilteredCount();
        if (rebuildCompleted && maybeAutolaunchActivity()) {
            return true;
        }
        setupViewVisibilities();
        if (shouldShowTabs()) {
            setupProfileTabs();
            return false;
        }
        return false;
    }

    private int isPermissionGranted(String permission, int uid) {
        return ActivityManager.checkComponentPermission(permission, uid, -1, true);
    }

    private boolean maybeAutolaunchActivity() {
        int numberOfProfiles = this.mMultiProfilePagerAdapter.getItemCount();
        if (numberOfProfiles == 1 && maybeAutolaunchIfSingleTarget()) {
            return true;
        }
        if (numberOfProfiles == 2 && this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().isTabLoaded() && this.mMultiProfilePagerAdapter.mo3314getInactiveListAdapter().isTabLoaded()) {
            if (maybeAutolaunchIfNoAppsOnInactiveTab() || maybeAutolaunchIfCrossProfileSupported()) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean maybeAutolaunchIfSingleTarget() {
        int count = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getUnfilteredCount();
        if (count == 1 && this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getOtherProfile() == null) {
            TargetInfo target = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().targetInfoForPosition(0, false);
            if (!shouldAutoLaunchSingleChoice(target)) {
                return false;
            }
            safelyStartActivity(target);
            finish();
            return true;
        }
        return false;
    }

    private boolean maybeAutolaunchIfNoAppsOnInactiveTab() {
        int count = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getUnfilteredCount();
        if (count != 1) {
            return false;
        }
        ResolverListAdapter inactiveListAdapter = this.mMultiProfilePagerAdapter.mo3314getInactiveListAdapter();
        if (inactiveListAdapter.getUnfilteredCount() != 0) {
            return false;
        }
        TargetInfo target = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().targetInfoForPosition(0, false);
        safelyStartActivity(target);
        finish();
        return true;
    }

    private boolean maybeAutolaunchIfCrossProfileSupported() {
        ResolverListAdapter activeListAdapter = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter();
        int count = activeListAdapter.getUnfilteredCount();
        if (count != 1) {
            return false;
        }
        ResolverListAdapter inactiveListAdapter = this.mMultiProfilePagerAdapter.mo3314getInactiveListAdapter();
        if (inactiveListAdapter.getUnfilteredCount() != 1) {
            return false;
        }
        TargetInfo activeProfileTarget = activeListAdapter.targetInfoForPosition(0, false);
        TargetInfo inactiveProfileTarget = inactiveListAdapter.targetInfoForPosition(0, false);
        if (!Objects.equals(activeProfileTarget.getResolvedComponentName(), inactiveProfileTarget.getResolvedComponentName()) || !shouldAutoLaunchSingleChoice(activeProfileTarget)) {
            return false;
        }
        String packageName = activeProfileTarget.getResolvedComponentName().getPackageName();
        if (!canAppInteractCrossProfiles(packageName)) {
            return false;
        }
        DevicePolicyEventLogger.createEvent(161).setBoolean(activeListAdapter.getUserHandle().equals(getPersonalProfileUserHandle())).setStrings(getMetricsCategory()).write();
        safelyStartActivity(activeProfileTarget);
        finish();
        return true;
    }

    private boolean canAppInteractCrossProfiles(String packageName) {
        try {
            ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(packageName, 0);
            if (!applicationInfo.crossProfile) {
                return false;
            }
            int packageUid = applicationInfo.uid;
            return isPermissionGranted(Manifest.permission.INTERACT_ACROSS_USERS_FULL, packageUid) == 0 || isPermissionGranted(Manifest.permission.INTERACT_ACROSS_USERS, packageUid) == 0 || PermissionChecker.checkPermissionForPreflight(this, Manifest.permission.INTERACT_ACROSS_PROFILES, -1, packageUid, packageName) == 0;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Package " + packageName + " does not exist on current user.");
            return false;
        }
    }

    private boolean isAutolaunching() {
        return !this.mRegistered && isFinishing();
    }

    private void setupProfileTabs() {
        maybeHideDivider();
        final TabHost tabHost = (TabHost) findViewById(R.id.profile_tabhost);
        tabHost.setup();
        final ViewPager viewPager = (ViewPager) findViewById(R.id.profile_pager);
        viewPager.setSaveEnabled(false);
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("personal").setContent(R.id.profile_pager).setIndicator(getString(R.string.resolver_personal_tab));
        tabHost.addTab(tabSpec);
        TabHost.TabSpec tabSpec2 = tabHost.newTabSpec(TAB_TAG_WORK).setContent(R.id.profile_pager).setIndicator(getString(R.string.resolver_work_tab));
        tabHost.addTab(tabSpec2);
        final TabWidget tabWidget = tabHost.getTabWidget();
        tabWidget.setVisibility(0);
        resetTabsHeaderStyle(tabWidget);
        updateActiveTabStyle(tabHost);
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() { // from class: com.android.internal.app.ResolverActivity$$ExternalSyntheticLambda2
            @Override // android.widget.TabHost.OnTabChangeListener
            public final void onTabChanged(String str) {
                ResolverActivity.this.lambda$setupProfileTabs$0$ResolverActivity(tabWidget, tabHost, viewPager, str);
            }
        });
        viewPager.setVisibility(0);
        tabHost.setCurrentTab(this.mMultiProfilePagerAdapter.getCurrentPage());
        this.mMultiProfilePagerAdapter.setOnProfileSelectedListener(new AbstractMultiProfilePagerAdapter.OnProfileSelectedListener() { // from class: com.android.internal.app.ResolverActivity.3
            @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter.OnProfileSelectedListener
            public void onProfileSelected(int index) {
                tabHost.setCurrentTab(index);
                ResolverActivity.this.resetButtonBar();
                ResolverActivity.this.resetCheckedItem();
            }

            @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter.OnProfileSelectedListener
            public void onProfilePageStateChanged(int state) {
                ResolverActivity.this.onHorizontalSwipeStateChanged(state);
            }
        });
        this.mMultiProfilePagerAdapter.setOnSwitchOnWorkSelectedListener(new AbstractMultiProfilePagerAdapter.OnSwitchOnWorkSelectedListener() { // from class: com.android.internal.app.ResolverActivity$$ExternalSyntheticLambda3
            @Override // com.android.internal.app.AbstractMultiProfilePagerAdapter.OnSwitchOnWorkSelectedListener
            public final void onSwitchOnWorkSelected() {
                ResolverActivity.lambda$setupProfileTabs$1(TabHost.this);
            }
        });
        findViewById(R.id.resolver_tab_divider).setVisibility(0);
    }

    public /* synthetic */ void lambda$setupProfileTabs$0$ResolverActivity(TabWidget tabWidget, TabHost tabHost, ViewPager viewPager, String tabId) {
        resetTabsHeaderStyle(tabWidget);
        updateActiveTabStyle(tabHost);
        if ("personal".equals(tabId)) {
            viewPager.setCurrentItem(0);
        } else {
            viewPager.setCurrentItem(1);
        }
        setupViewVisibilities();
        maybeLogProfileChange();
        onProfileTabSelected();
        DevicePolicyEventLogger.createEvent(156).setInt(viewPager.getCurrentItem()).setStrings(getMetricsCategory()).write();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$setupProfileTabs$1(TabHost tabHost) {
        View workTab = tabHost.getTabWidget().getChildAt(1);
        workTab.setFocusable(true);
        workTab.setFocusableInTouchMode(true);
        workTab.requestFocus();
    }

    void onHorizontalSwipeStateChanged(int state) {
    }

    private void maybeHideDivider() {
        View divider;
        if (!isIntentPicker() || (divider = findViewById(R.id.divider)) == null) {
            return;
        }
        divider.setVisibility(8);
    }

    protected void onProfileTabSelected() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetCheckedItem() {
        if (!isIntentPicker()) {
            return;
        }
        this.mLastSelected = -1;
        ListView inactiveListView = (ListView) this.mMultiProfilePagerAdapter.mo3313getInactiveAdapterView();
        if (inactiveListView.getCheckedItemCount() > 0) {
            inactiveListView.setItemChecked(inactiveListView.getCheckedItemPosition(), false);
        }
    }

    private void resetTabsHeaderStyle(TabWidget tabWidget) {
        String workContentDescription = getString(R.string.resolver_work_tab_accessibility);
        String personalContentDescription = getString(R.string.resolver_personal_tab_accessibility);
        for (int i = 0; i < tabWidget.getChildCount(); i++) {
            View tabView = tabWidget.getChildAt(i);
            TextView title = (TextView) tabView.findViewById(16908310);
            title.setTextAppearance(16974264);
            title.setTextColor(getAttrColor(this, 16843282));
            title.setTextSize(0, getResources().getDimension(R.dimen.resolver_tab_text_size));
            if (title.mo3084getText().equals(getString(R.string.resolver_personal_tab))) {
                tabView.setContentDescription(personalContentDescription);
            } else if (title.mo3084getText().equals(getString(R.string.resolver_work_tab))) {
                tabView.setContentDescription(workContentDescription);
            }
        }
    }

    private static int getAttrColor(Context context, int attr) {
        TypedArray ta = context.obtainStyledAttributes(new int[]{attr});
        int colorAccent = ta.getColor(0, 0);
        ta.recycle();
        return colorAccent;
    }

    private void updateActiveTabStyle(TabHost tabHost) {
        TextView title = (TextView) tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).findViewById(16908310);
        title.setTextColor(getAttrColor(this, 16843829));
    }

    private void setupViewVisibilities() {
        ResolverListAdapter activeListAdapter = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter();
        if (!this.mMultiProfilePagerAdapter.shouldShowEmptyStateScreen(activeListAdapter)) {
            addUseDifferentAppLabelIfNecessary(activeListAdapter);
        }
    }

    public void addUseDifferentAppLabelIfNecessary(ResolverListAdapter adapter) {
        boolean useHeader = adapter.hasFilteredItem();
        if (useHeader) {
            FrameLayout stub = (FrameLayout) findViewById(R.id.stub);
            stub.setVisibility(0);
            TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.resolver_different_item_header, (ViewGroup) null, false);
            if (shouldShowTabs()) {
                textView.setGravity(17);
            }
            stub.addView(textView);
        }
    }

    private void setupAdapterListView(ListView listView, ItemClickListener listener) {
        listView.setOnItemClickListener(listener);
        listView.setOnItemLongClickListener(listener);
        if (this.mSupportsAlwaysUseOption) {
            listView.setChoiceMode(1);
        }
    }

    private void maybeCreateHeader(ResolverListAdapter listAdapter) {
        TextView titleView;
        if (this.mHeaderCreatorUser != null && !listAdapter.getUserHandle().equals(this.mHeaderCreatorUser)) {
            return;
        }
        if (!shouldShowTabs() && listAdapter.getCount() == 0 && listAdapter.getPlaceholderCount() == 0 && (titleView = (TextView) findViewById(16908310)) != null) {
            titleView.setVisibility(8);
        }
        CharSequence title = this.mTitle;
        if (title == null) {
            title = getTitleForAction(getTargetIntent(), this.mDefaultTitleResId);
        }
        if (!TextUtils.isEmpty(title)) {
            TextView titleView2 = (TextView) findViewById(16908310);
            if (titleView2 != null) {
                titleView2.setText(title);
            }
            setTitle(title);
        }
        ImageView iconView = (ImageView) findViewById(16908294);
        if (iconView != null) {
            listAdapter.loadFilteredItemIconTaskAsync(iconView);
        }
        this.mHeaderCreatorUser = listAdapter.getUserHandle();
    }

    protected void resetButtonBar() {
        if (!this.mSupportsAlwaysUseOption) {
            return;
        }
        ViewGroup buttonLayout = (ViewGroup) findViewById(R.id.button_bar);
        if (buttonLayout == null) {
            Log.e(TAG, "Layout unexpectedly does not have a button bar");
            return;
        }
        ResolverListAdapter activeListAdapter = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter();
        View buttonBarDivider = findViewById(R.id.resolver_button_bar_divider);
        if (!useLayoutWithDefault()) {
            Insets insets = this.mSystemWindowInsets;
            int inset = insets != null ? insets.bottom : 0;
            buttonLayout.setPadding(buttonLayout.getPaddingLeft(), buttonLayout.getPaddingTop(), buttonLayout.getPaddingRight(), getResources().getDimensionPixelSize(R.dimen.resolver_button_bar_spacing) + inset);
        }
        if (activeListAdapter.isTabLoaded() && this.mMultiProfilePagerAdapter.shouldShowEmptyStateScreen(activeListAdapter) && !useLayoutWithDefault()) {
            buttonLayout.setVisibility(4);
            if (buttonBarDivider != null) {
                buttonBarDivider.setVisibility(4);
            }
            setButtonBarIgnoreOffset(false);
            return;
        }
        if (buttonBarDivider != null) {
            buttonBarDivider.setVisibility(0);
        }
        buttonLayout.setVisibility(0);
        setButtonBarIgnoreOffset(true);
        this.mOnceButton = (Button) buttonLayout.findViewById(R.id.button_once);
        this.mAlwaysButton = (Button) buttonLayout.findViewById(R.id.button_always);
        resetAlwaysOrOnceButtonBar();
    }

    private void setButtonBarIgnoreOffset(boolean ignoreOffset) {
        View buttonBarContainer = findViewById(R.id.button_bar_container);
        if (buttonBarContainer != null) {
            ResolverDrawerLayout.LayoutParams layoutParams = (ResolverDrawerLayout.LayoutParams) buttonBarContainer.getLayoutParams();
            layoutParams.ignoreOffset = ignoreOffset;
            buttonBarContainer.setLayoutParams(layoutParams);
        }
    }

    private void resetAlwaysOrOnceButtonBar() {
        setAlwaysButtonEnabled(false, -1, false);
        this.mOnceButton.setEnabled(false);
        int filteredPosition = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().getFilteredPosition();
        if (useLayoutWithDefault() && filteredPosition != -1) {
            setAlwaysButtonEnabled(true, filteredPosition, false);
            this.mOnceButton.setEnabled(true);
            this.mOnceButton.requestFocus();
            return;
        }
        ListView currentAdapterView = (ListView) this.mMultiProfilePagerAdapter.mo3321getActiveAdapterView();
        if (currentAdapterView != null && currentAdapterView.getCheckedItemPosition() != -1) {
            setAlwaysButtonEnabled(true, currentAdapterView.getCheckedItemPosition(), true);
            this.mOnceButton.setEnabled(true);
        }
    }

    @Override // com.android.internal.app.ResolverListAdapter.ResolverListCommunicator
    public boolean useLayoutWithDefault() {
        boolean currentUserAdapterHasFilteredItem;
        if (this.mMultiProfilePagerAdapter.getCurrentUserHandle().getIdentifier() == UserHandle.myUserId()) {
            currentUserAdapterHasFilteredItem = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().hasFilteredItem();
        } else {
            currentUserAdapterHasFilteredItem = this.mMultiProfilePagerAdapter.mo3314getInactiveListAdapter().hasFilteredItem();
        }
        return this.mSupportsAlwaysUseOption && currentUserAdapterHasFilteredItem;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setRetainInOnStop(boolean retainInOnStop) {
        this.mRetainInOnStop = retainInOnStop;
    }

    @Override // com.android.internal.app.ResolverListAdapter.ResolverListCommunicator
    public boolean resolveInfoMatch(ResolveInfo lhs, ResolveInfo rhs) {
        return lhs == null ? rhs == null : lhs.activityInfo == null ? rhs.activityInfo == null : Objects.equals(lhs.activityInfo.name, rhs.activityInfo.name) && Objects.equals(lhs.activityInfo.packageName, rhs.activityInfo.packageName);
    }

    protected String getMetricsCategory() {
        return METRICS_CATEGORY_RESOLVER;
    }

    public void onHandlePackagesChanged(ResolverListAdapter listAdapter) {
        if (listAdapter == this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter()) {
            if (listAdapter.getUserHandle().equals(getWorkProfileUserHandle()) && this.mMultiProfilePagerAdapter.isWaitingToEnableWorkProfile()) {
                return;
            }
            boolean listRebuilt = this.mMultiProfilePagerAdapter.rebuildActiveTab(true);
            if (listRebuilt) {
                ResolverListAdapter activeListAdapter = this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter();
                activeListAdapter.notifyDataSetChanged();
                if (activeListAdapter.getCount() == 0 && !inactiveListAdapterHasItems()) {
                    finish();
                    return;
                }
                return;
            }
            return;
        }
        this.mMultiProfilePagerAdapter.clearInactiveProfileCache();
    }

    private boolean inactiveListAdapterHasItems() {
        return shouldShowTabs() && this.mMultiProfilePagerAdapter.mo3314getInactiveListAdapter().getCount() > 0;
    }

    private BroadcastReceiver createWorkProfileStateReceiver() {
        return new BroadcastReceiver() { // from class: com.android.internal.app.ResolverActivity.4
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (!TextUtils.equals(action, Intent.ACTION_USER_UNLOCKED) && !TextUtils.equals(action, Intent.ACTION_MANAGED_PROFILE_UNAVAILABLE) && !TextUtils.equals(action, Intent.ACTION_MANAGED_PROFILE_AVAILABLE)) {
                    return;
                }
                int userId = intent.getIntExtra(Intent.EXTRA_USER_HANDLE, -1);
                if (userId == ResolverActivity.this.getWorkProfileUserHandle().getIdentifier()) {
                    if (ResolverActivity.this.isWorkProfileEnabled()) {
                        if (!ResolverActivity.this.mWorkProfileHasBeenEnabled) {
                            ResolverActivity.this.mWorkProfileHasBeenEnabled = true;
                            ResolverActivity.this.mMultiProfilePagerAdapter.markWorkProfileEnabledBroadcastReceived();
                        } else {
                            return;
                        }
                    } else {
                        ResolverActivity.this.mWorkProfileHasBeenEnabled = false;
                    }
                    if (ResolverActivity.this.mMultiProfilePagerAdapter.getCurrentUserHandle().equals(ResolverActivity.this.getWorkProfileUserHandle())) {
                        ResolverActivity.this.mMultiProfilePagerAdapter.rebuildActiveTab(true);
                    } else {
                        ResolverActivity.this.mMultiProfilePagerAdapter.clearInactiveProfileCache();
                    }
                }
            }
        };
    }

    /* loaded from: classes4.dex */
    public static final class ResolvedComponentInfo {
        private boolean mPinned;
        public final ComponentName name;
        private final List<Intent> mIntents = new ArrayList();
        private final List<ResolveInfo> mResolveInfos = new ArrayList();

        public ResolvedComponentInfo(ComponentName name, Intent intent, ResolveInfo info) {
            this.name = name;
            add(intent, info);
        }

        public void add(Intent intent, ResolveInfo info) {
            this.mIntents.add(intent);
            this.mResolveInfos.add(info);
        }

        public int getCount() {
            return this.mIntents.size();
        }

        public Intent getIntentAt(int index) {
            if (index >= 0) {
                return this.mIntents.get(index);
            }
            return null;
        }

        public ResolveInfo getResolveInfoAt(int index) {
            if (index >= 0) {
                return this.mResolveInfos.get(index);
            }
            return null;
        }

        public int findIntent(Intent intent) {
            int N = this.mIntents.size();
            for (int i = 0; i < N; i++) {
                if (intent.equals(this.mIntents.get(i))) {
                    return i;
                }
            }
            return -1;
        }

        public int findResolveInfo(ResolveInfo info) {
            int N = this.mResolveInfos.size();
            for (int i = 0; i < N; i++) {
                if (info.equals(this.mResolveInfos.get(i))) {
                    return i;
                }
            }
            return -1;
        }

        public boolean isPinned() {
            return this.mPinned;
        }

        public void setPinned(boolean pinned) {
            this.mPinned = pinned;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public class ItemClickListener implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
        ItemClickListener() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = parent instanceof ListView ? (ListView) parent : null;
            if (listView != null) {
                position -= listView.getHeaderViewsCount();
            }
            if (position < 0 || ResolverActivity.this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().resolveInfoForPosition(position, true) == null) {
                return;
            }
            ListView currentAdapterView = (ListView) ResolverActivity.this.mMultiProfilePagerAdapter.mo3321getActiveAdapterView();
            int checkedPos = currentAdapterView.getCheckedItemPosition();
            boolean hasValidSelection = checkedPos != -1;
            if (!ResolverActivity.this.useLayoutWithDefault() && ((!hasValidSelection || ResolverActivity.this.mLastSelected != checkedPos) && ResolverActivity.this.mAlwaysButton != null)) {
                ResolverActivity.this.setAlwaysButtonEnabled(hasValidSelection, checkedPos, true);
                ResolverActivity.this.mOnceButton.setEnabled(hasValidSelection);
                if (hasValidSelection) {
                    currentAdapterView.smoothScrollToPosition(checkedPos);
                    ResolverActivity.this.mOnceButton.requestFocus();
                }
                ResolverActivity.this.mLastSelected = checkedPos;
                return;
            }
            ResolverActivity.this.startSelected(position, false, true);
        }

        @Override // android.widget.AdapterView.OnItemLongClickListener
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            ListView listView = parent instanceof ListView ? (ListView) parent : null;
            if (listView != null) {
                position -= listView.getHeaderViewsCount();
            }
            if (position < 0) {
                return false;
            }
            ResolveInfo ri = ResolverActivity.this.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().resolveInfoForPosition(position, true);
            ResolverActivity.this.showTargetDetails(ri);
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static final boolean isSpecificUriMatch(int match) {
        int match2 = match & IntentFilter.MATCH_CATEGORY_MASK;
        return match2 >= 3145728 && match2 <= 5242880;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes4.dex */
    public static class PickTargetOptionRequest extends VoiceInteractor.PickOptionRequest {
        public PickTargetOptionRequest(VoiceInteractor.Prompt prompt, VoiceInteractor.PickOptionRequest.Option[] options, Bundle extras) {
            super(prompt, options, extras);
        }

        @Override // android.app.VoiceInteractor.Request
        public void onCancel() {
            super.onCancel();
            ResolverActivity ra = (ResolverActivity) getActivity();
            if (ra != null) {
                ra.mPickOptionRequest = null;
                ra.finish();
            }
        }

        @Override // android.app.VoiceInteractor.PickOptionRequest
        public void onPickOptionResult(boolean finished, VoiceInteractor.PickOptionRequest.Option[] selections, Bundle result) {
            ResolverActivity ra;
            super.onPickOptionResult(finished, selections, result);
            if (selections.length == 1 && (ra = (ResolverActivity) getActivity()) != null) {
                TargetInfo ti = ra.mMultiProfilePagerAdapter.mo3310getActiveListAdapter().mo3505getItem(selections[0].getIndex());
                if (ra.onTargetSelected(ti, false)) {
                    ra.mPickOptionRequest = null;
                    ra.finish();
                }
            }
        }
    }

    protected void maybeLogProfileChange() {
    }
}
