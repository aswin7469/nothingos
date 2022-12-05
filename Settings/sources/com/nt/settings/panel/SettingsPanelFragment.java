package com.nt.settings.panel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.graphics.drawable.IconCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.android.settings.panel.PanelContentCallback;
import com.android.settingslib.utils.ThreadUtils;
import com.nothing.tesla.service.CmdObjectList;
import com.nt.settings.panel.SettingsPanelFragment;
import com.nt.settings.panel.TeslaConnectPanelPlugin;
/* loaded from: classes2.dex */
public class SettingsPanelFragment extends Fragment {
    private SettingsListAdapter mAdapter;
    private SettingContentRegistry mContentRegistry;
    private LinearLayout mHeaderLayout;
    private TextView mHeaderTitle;
    private View mLayoutView;
    private NtPanelContent mPanel;
    private boolean mPanelCreating;
    private LinearLayout mPanelHeader;
    private RecyclerView mPanelLists;
    private ProgressBar mProgressBar;
    private Button mSeeMoreButton;
    private TeslaConnectPanelPlugin mTeslaPlugin;
    private LinearLayout mTitleGroup;
    private ImageView mTitleIcon;
    private TextView mTitleView;
    private ViewTreeObserver.OnPreDrawListener mOnPreDrawListener = SettingsPanelFragment$$ExternalSyntheticLambda1.INSTANCE;
    private final ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.nt.settings.panel.SettingsPanelFragment.1
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            SettingsPanelFragment.this.animateIn();
            if (SettingsPanelFragment.this.mPanelLists != null) {
                SettingsPanelFragment.this.mPanelLists.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
            SettingsPanelFragment.this.mPanelCreating = false;
        }
    };
    private Handler mBtProgressBarState = new Handler();
    private Runnable mUpdateBtProgressBarState = new Runnable() { // from class: com.nt.settings.panel.SettingsPanelFragment.2
        @Override // java.lang.Runnable
        public void run() {
            SettingsPanelFragment.this.updateProgressBar();
            SettingsPanelFragment.this.mBtProgressBarState.postDelayed(this, 1500L);
        }
    };

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$new$0() {
        return false;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.nt_panel_layout, viewGroup, false);
        this.mLayoutView = inflate;
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.panel_parent_layout);
        this.mPanelLists = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.mLayoutView.getViewTreeObserver().addOnPreDrawListener(this.mOnPreDrawListener);
        this.mPanelCreating = true;
        this.mContentRegistry = new SettingContentRegistry();
        createPanelContent();
        return this.mLayoutView;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mContentRegistry.reset();
        removeTeslaPlugin();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void createPanelContent() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        View view = this.mLayoutView;
        if (view == null) {
            activity.finish();
            return;
        }
        this.mSeeMoreButton = (Button) view.findViewById(R.id.see_more);
        this.mTitleView = (TextView) this.mLayoutView.findViewById(R.id.panel_title);
        this.mPanelHeader = (LinearLayout) this.mLayoutView.findViewById(R.id.panel_header);
        this.mTitleIcon = (ImageView) this.mLayoutView.findViewById(R.id.title_icon);
        this.mTitleGroup = (LinearLayout) this.mLayoutView.findViewById(R.id.title_group);
        this.mHeaderLayout = (LinearLayout) this.mLayoutView.findViewById(R.id.header_layout);
        this.mHeaderTitle = (TextView) this.mLayoutView.findViewById(R.id.header_title);
        ProgressBar progressBar = (ProgressBar) this.mLayoutView.findViewById(R.id.progress_bar);
        this.mProgressBar = progressBar;
        progressBar.setMax(100);
        this.mProgressBar.setMin(0);
        NtPanelContent panel = getPanel();
        this.mPanel = panel;
        if (panel == null) {
            activity.finish();
            return;
        }
        panel.registerCallback(new LocalPanelCallback());
        if (this.mPanel instanceof LifecycleObserver) {
            mo959getLifecycle().addObserver((LifecycleObserver) this.mPanel);
        }
        updateProgressBar();
        this.mBtProgressBarState.post(this.mUpdateBtProgressBarState);
        SettingsListAdapter settingsListAdapter = new SettingsListAdapter(this.mPanel.getLists());
        this.mAdapter = settingsListAdapter;
        settingsListAdapter.setContentRegistry(this.mContentRegistry);
        this.mPanelLists.setAdapter(this.mAdapter);
        this.mPanelLists.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
        this.mAdapter.notifyDataSetChanged();
        IconCompat icon = this.mPanel.getIcon();
        CharSequence title = this.mPanel.getTitle();
        CharSequence subTitle = this.mPanel.getSubTitle();
        if (icon != null || (subTitle != null && subTitle.length() > 0)) {
            enablePanelHeader(icon, title, subTitle);
        } else {
            enableTitle(title);
        }
        initialTeslaIfNeeded();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void enablePanelHeader(IconCompat iconCompat, CharSequence charSequence, CharSequence charSequence2) {
        this.mTitleView.setVisibility(8);
        this.mPanelHeader.setVisibility(0);
        this.mPanelHeader.setAccessibilityPaneTitle(charSequence);
        this.mHeaderTitle.setText(charSequence);
        if (iconCompat != null) {
            this.mTitleGroup.setVisibility(0);
            this.mHeaderLayout.setGravity(3);
            this.mTitleIcon.setImageIcon(iconCompat.toIcon(getContext()));
            if (this.mPanel.getHeaderIconIntent() != null) {
                this.mTitleIcon.setOnClickListener(getHeaderIconListener());
                this.mTitleIcon.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                return;
            }
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.output_switcher_panel_icon_size);
            this.mTitleIcon.setLayoutParams(new LinearLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize));
            return;
        }
        this.mTitleGroup.setVisibility(8);
        this.mHeaderLayout.setGravity(1);
    }

    private void enableTitle(CharSequence charSequence) {
        this.mPanelHeader.setVisibility(8);
        this.mTitleView.setVisibility(0);
        this.mTitleView.setAccessibilityPaneTitle(charSequence);
        this.mTitleView.setText(charSequence);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProgressBar() {
        if (this.mPanel.isProgressBarVisible()) {
            ProgressBar progressBar = this.mProgressBar;
            progressBar.setProgress(progressBar.getMax(), true);
            this.mProgressBar.setIndeterminate(true);
            return;
        }
        ProgressBar progressBar2 = this.mProgressBar;
        progressBar2.setProgress(progressBar2.getMin(), false);
        this.mProgressBar.setIndeterminate(false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updatePanelWithAnimation() {
        this.mPanelCreating = true;
        AnimatorSet buildAnimatorSet = buildAnimatorSet(this.mLayoutView, 0.0f, this.mLayoutView.findViewById(R.id.panel_container).getHeight(), 1.0f, 0.0f, 200);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(0.0f, 1.0f);
        buildAnimatorSet.play(valueAnimator);
        buildAnimatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.nt.settings.panel.SettingsPanelFragment.3
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SettingsPanelFragment.this.createPanelContent();
            }
        });
        buildAnimatorSet.start();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean isPanelCreating() {
        return this.mPanelCreating;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateIn() {
        AnimatorSet buildAnimatorSet = buildAnimatorSet(this.mLayoutView, this.mLayoutView.findViewById(R.id.panel_container).getHeight(), 0.0f, 0.0f, 1.0f, 250);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setFloatValues(0.0f, 1.0f);
        buildAnimatorSet.play(valueAnimator);
        buildAnimatorSet.start();
        this.mLayoutView.getViewTreeObserver().removeOnPreDrawListener(this.mOnPreDrawListener);
    }

    private static AnimatorSet buildAnimatorSet(View view, float f, float f2, float f3, float f4, int i) {
        View findViewById = view.findViewById(R.id.panel_container);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(i);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(ObjectAnimator.ofFloat(findViewById, View.TRANSLATION_Y, f, f2), ObjectAnimator.ofFloat(findViewById, View.ALPHA, f3, f4));
        return animatorSet;
    }

    private NtPanelContent getPanel() {
        String string = getArguments().getString("NT_PANEL_TYPE_ARGUMENT");
        string.hashCode();
        if (!string.equals("android.settings.panel.action.NT_BLUE_TOOTH")) {
            if (string.equals("android.settings.panel.action.NT_INTERNET_CONNECTIVITY")) {
                return new InternetPanel(getContext(), this.mContentRegistry);
            }
            return null;
        }
        return new BluetoothPanel(getContext(), this.mContentRegistry);
    }

    View.OnClickListener getHeaderIconListener() {
        return new View.OnClickListener() { // from class: com.nt.settings.panel.SettingsPanelFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                SettingsPanelFragment.this.lambda$getHeaderIconListener$1(view);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$getHeaderIconListener$1(View view) {
        getActivity().startActivity(this.mPanel.getHeaderIconIntent());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes2.dex */
    public class LocalPanelCallback implements PanelContentCallback {
        LocalPanelCallback() {
        }

        @Override // com.android.settings.panel.PanelContentCallback
        public void onHeaderChanged() {
            ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.panel.SettingsPanelFragment$LocalPanelCallback$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SettingsPanelFragment.LocalPanelCallback.this.lambda$onHeaderChanged$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onHeaderChanged$0() {
            SettingsPanelFragment settingsPanelFragment = SettingsPanelFragment.this;
            settingsPanelFragment.enablePanelHeader(settingsPanelFragment.mPanel.getIcon(), SettingsPanelFragment.this.mPanel.getTitle(), SettingsPanelFragment.this.mPanel.getSubTitle());
        }

        @Override // com.android.settings.panel.PanelContentCallback
        public void forceClose() {
            getFragmentActivity().finish();
        }

        @Override // com.android.settings.panel.PanelContentCallback
        public void onProgressBarVisibleChanged() {
            ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.panel.SettingsPanelFragment$LocalPanelCallback$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    SettingsPanelFragment.LocalPanelCallback.this.lambda$onProgressBarVisibleChanged$2();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onProgressBarVisibleChanged$2() {
            SettingsPanelFragment.this.updateProgressBar();
        }

        FragmentActivity getFragmentActivity() {
            return SettingsPanelFragment.this.getActivity();
        }
    }

    private void initialTeslaIfNeeded() {
        boolean z = false;
        try {
            if (Settings.System.getInt(getContext().getContentResolver(), "tesla_active") == 0) {
                z = true;
            }
        } catch (Settings.SettingNotFoundException unused) {
        }
        if ("android.settings.panel.action.NT_BLUE_TOOTH".equals(getActivity().getIntent().getAction())) {
            this.mAdapter.initialTeslaView(z);
            if (!z) {
                return;
            }
            TeslaConnectPanelPlugin teslaConnectPanelPlugin = new TeslaConnectPanelPlugin(getContext());
            this.mTeslaPlugin = teslaConnectPanelPlugin;
            teslaConnectPanelPlugin.onCreate(new AnonymousClass4());
            this.mAdapter.setupTeslaPlugin(this.mTeslaPlugin);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.nt.settings.panel.SettingsPanelFragment$4  reason: invalid class name */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements TeslaConnectPanelPlugin.LoadCallback {
        AnonymousClass4() {
        }

        @Override // com.nt.settings.panel.TeslaConnectPanelPlugin.LoadCallback
        public void onLoaded() {
            ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.panel.SettingsPanelFragment$4$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SettingsPanelFragment.AnonymousClass4.this.lambda$onLoaded$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onLoaded$0() {
            SettingsPanelFragment.this.updateTeslaInfos();
        }

        @Override // com.nt.settings.panel.TeslaConnectPanelPlugin.LoadCallback
        public void onStatusChange(final CmdObjectList cmdObjectList) {
            ThreadUtils.postOnMainThread(new Runnable() { // from class: com.nt.settings.panel.SettingsPanelFragment$4$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    SettingsPanelFragment.AnonymousClass4.this.lambda$onStatusChange$1(cmdObjectList);
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$onStatusChange$1(CmdObjectList cmdObjectList) {
            SettingsPanelFragment.this.updateTeslaItemsStatus(cmdObjectList);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTeslaItemsStatus(CmdObjectList cmdObjectList) {
        TeslaConnectPanelPlugin teslaConnectPanelPlugin;
        SettingsListAdapter settingsListAdapter = this.mAdapter;
        if (settingsListAdapter == null || (teslaConnectPanelPlugin = this.mTeslaPlugin) == null) {
            return;
        }
        settingsListAdapter.updateTeslaItemStatus(teslaConnectPanelPlugin.getCmdList());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTeslaInfos() {
        TeslaConnectPanelPlugin teslaConnectPanelPlugin;
        SettingsListAdapter settingsListAdapter = this.mAdapter;
        if (settingsListAdapter == null || (teslaConnectPanelPlugin = this.mTeslaPlugin) == null) {
            return;
        }
        settingsListAdapter.updateTeslaInfo(teslaConnectPanelPlugin.getCmdList());
    }

    private void removeTeslaPlugin() {
        TeslaConnectPanelPlugin teslaConnectPanelPlugin = this.mTeslaPlugin;
        if (teslaConnectPanelPlugin != null) {
            teslaConnectPanelPlugin.onDestory();
        }
    }
}
