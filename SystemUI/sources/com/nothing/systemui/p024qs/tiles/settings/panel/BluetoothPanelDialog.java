package com.nothing.systemui.p024qs.tiles.settings.panel;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.core.graphics.drawable.IconCompat;
import androidx.lifecycle.LifecycleObserver;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.C1894R;
import com.android.systemui.p012qs.QSFragment;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogController;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.QSFragmentEx;
import com.nothing.systemui.p024qs.tiles.BluetoothTileEx;
import com.nothing.systemui.p024qs.tiles.settings.tesla.TeslaConnectPanelPlugin;
import com.nothing.systemui.util.NTLogUtil;
import com.nothing.tesla.service.CmdObject;
import com.nothing.tesla.service.CmdObjectList;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.BluetoothPanelDialog */
public class BluetoothPanelDialog extends SystemUIDialog implements Window.Callback {
    private static final long BLUETOOTH_PROGRESS_BAR_UPDATE_DELAYED_TIME = 1500;
    private static final int DURATION_SLICE_BINDING_TIMEOUT_MS = 250;
    private static final String TAG = "BtPanelDialog";
    private SettingsListAdapter mAdapter;
    private BluetoothTileEx mBluetoothTileEx = ((BluetoothTileEx) NTDependencyEx.get(BluetoothTileEx.class));
    private SettingContentRegistry mContentRegistry;
    private Context mContext;
    private InternetDialogController mDialogController;
    private InternetDialogFactory mDialogFactory;
    private Button mDoneButton;
    private QSFragmentEx mEx = ((QSFragmentEx) NTDependencyEx.get(QSFragmentEx.class));
    /* access modifiers changed from: private */
    public Handler mHandler;
    private LinearLayout mHeaderLayout;
    private TextView mHeaderSubtitle;
    private TextView mHeaderTitle;
    View mLayoutView;
    private int mMaxHeight;
    /* access modifiers changed from: private */
    public BluetoothPanel mPanel;
    private LinearLayout mPanelHeader;
    private RecyclerView mPanelLists;
    private ProgressBar mProgressBar;
    private TeslaConnectPanelPlugin mTeslaPlugin;
    private LinearLayout mTitleGroup;
    private ImageView mTitleIcon;
    private TextView mTitleView;
    private Runnable mUpdateBtProgressBarState = new Runnable() {
        public void run() {
            BluetoothPanelDialog.this.updateProgressBar();
            BluetoothPanelDialog.this.mHandler.postDelayed(this, 1500);
        }
    };

    public BluetoothPanelDialog(Context context, InternetDialogFactory internetDialogFactory, InternetDialogController internetDialogController, boolean z, Handler handler) {
        super(((QSFragmentEx) NTDependencyEx.get(QSFragmentEx.class)).getQSFragment().getView().getContext());
        NTLogUtil.m1686d(TAG, "Init BluetoothPanelDialog");
        Context context2 = getContext();
        this.mContext = context2;
        this.mHandler = handler;
        this.mDialogController = internetDialogController;
        this.mDialogFactory = internetDialogFactory;
        this.mMaxHeight = context2.getResources().getDimensionPixelSize(C1894R.dimen.output_switcher_slice_max_height);
        this.mContentRegistry = new SettingContentRegistry();
        this.mPanel = new BluetoothPanel(this.mContext, this.mContentRegistry, this);
        if (!z) {
            getWindow().setType(2038);
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        NTLogUtil.m1686d(TAG, "onCreate");
        this.mLayoutView = LayoutInflater.from(this.mContext).inflate(C1894R.layout.nt_panel_layout, (ViewGroup) null);
        Window window = getWindow();
        window.setContentView(this.mLayoutView);
        window.setWindowAnimations(C1894R.style.Animation_InternetDialog);
        window.setBackgroundDrawable(this.mContext.getResources().getDrawable(C1894R.C1896drawable.nt_settings_panel_rounded_top_corner_background));
        updateWindowSize(true);
        RecyclerView recyclerView = (RecyclerView) this.mLayoutView.findViewById(C1894R.C1898id.panel_parent_layout);
        this.mPanelLists = recyclerView;
        recyclerView.setLayoutManager(new LayoutManagerWrapper(this.mContext));
        createPanelContent();
    }

    public void onStart() {
        super.onStart();
        NTLogUtil.m1686d(TAG, "onStart");
        this.mPanel.onStart();
    }

    public void onStop() {
        super.onStop();
        NTLogUtil.m1686d(TAG, "onStop");
        this.mPanel.onStop();
        this.mContentRegistry.reset();
        this.mDoneButton.setOnClickListener((View.OnClickListener) null);
        removeTeslaPlugin();
        this.mDialogFactory.destroyBluetoothDialog();
    }

    public void onConfigurationChanged(Configuration configuration) {
        updateWindowSize(false);
    }

    public void updateWindowSize(boolean z) {
        int i;
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.settings_panel_width);
        if (this.mBluetoothTileEx.isBluetoothEnabled() || this.mBluetoothTileEx.shouldShowTeslaInfo()) {
            i = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.settings_panel_height);
        } else {
            i = -2;
        }
        int i2 = this.mBluetoothTileEx.isBluetoothEnabled() ? 0 : 500;
        NTLogUtil.m1686d(TAG, "updateWindowSize: " + dimensionPixelSize + ", " + i);
        if (z) {
            getWindow().setLayout(dimensionPixelSize, i);
        } else {
            ThreadUtils.postOnMainThreadDelayed(new BluetoothPanelDialog$$ExternalSyntheticLambda0(this, dimensionPixelSize, i), (long) i2);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$updateWindowSize$0$com-nothing-systemui-qs-tiles-settings-panel-BluetoothPanelDialog */
    public /* synthetic */ void mo57885x5c51ff7b(int i, int i2) {
        getWindow().setLayout(i, i2);
    }

    public void dismissDialog() {
        NTLogUtil.m1686d(TAG, "dismissBtDialog");
        onStop();
        dismiss();
    }

    public void startActivity(Intent intent, View view) {
        NTLogUtil.m1686d(TAG, "startActivity");
        dismissDialog();
        this.mDialogController.startActivityDismissingKeyguard(intent, view);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
    }

    private void createPanelContent() {
        this.mDoneButton = (Button) this.mLayoutView.findViewById(C1894R.C1898id.done);
        this.mTitleView = (TextView) this.mLayoutView.findViewById(C1894R.C1898id.panel_title);
        this.mPanelHeader = (LinearLayout) this.mLayoutView.findViewById(C1894R.C1898id.panel_header);
        this.mTitleIcon = (ImageView) this.mLayoutView.findViewById(C1894R.C1898id.title_icon);
        this.mTitleGroup = (LinearLayout) this.mLayoutView.findViewById(C1894R.C1898id.title_group);
        this.mHeaderLayout = (LinearLayout) this.mLayoutView.findViewById(C1894R.C1898id.header_layout);
        this.mHeaderTitle = (TextView) this.mLayoutView.findViewById(C1894R.C1898id.header_title);
        ProgressBar progressBar = (ProgressBar) this.mLayoutView.findViewById(C1894R.C1898id.progress_bar);
        this.mProgressBar = progressBar;
        progressBar.setMax(100);
        this.mProgressBar.setMin(0);
        this.mPanel.registerCallback(new LocalPanelCallback());
        QSFragment qSFragment = this.mEx.getQSFragment();
        if (qSFragment instanceof LifecycleObserver) {
            qSFragment.getLifecycle().addObserver(this.mPanel);
        }
        updateProgressBar();
        this.mHandler.post(this.mUpdateBtProgressBarState);
        SettingsListAdapter settingsListAdapter = new SettingsListAdapter(this.mPanel.getLists(), this);
        this.mAdapter = settingsListAdapter;
        settingsListAdapter.setContentRegistry(this.mContentRegistry);
        this.mPanelLists.setAdapter(this.mAdapter);
        this.mAdapter.notifyDataSetChanged();
        IconCompat icon = this.mPanel.getIcon();
        CharSequence title = this.mPanel.getTitle();
        CharSequence subTitle = this.mPanel.getSubTitle();
        if (icon != null || (subTitle != null && subTitle.length() > 0)) {
            enablePanelHeader(icon, title, subTitle);
        } else {
            enableTitle(title);
        }
        this.mDoneButton.setOnClickListener(new BluetoothPanelDialog$$ExternalSyntheticLambda1(this));
        initTeslaIfNeeded();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$createPanelContent$1$com-nothing-systemui-qs-tiles-settings-panel-BluetoothPanelDialog */
    public /* synthetic */ void mo57884xb8efffb3(View view) {
        dismiss();
    }

    /* access modifiers changed from: private */
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

    /* access modifiers changed from: private */
    public void enablePanelHeader(IconCompat iconCompat, CharSequence charSequence, CharSequence charSequence2) {
        this.mTitleView.setVisibility(8);
        this.mPanelHeader.setVisibility(0);
        this.mPanelHeader.setAccessibilityPaneTitle(charSequence);
        this.mHeaderTitle.setText(charSequence);
        if (iconCompat != null) {
            this.mTitleGroup.setVisibility(0);
            this.mHeaderLayout.setGravity(3);
            this.mTitleIcon.setImageIcon(iconCompat.toIcon(this.mContext));
            if (this.mPanel.getHeaderIconIntent() != null) {
                this.mTitleIcon.setLayoutParams(new LinearLayout.LayoutParams(-2, -2));
                return;
            }
            int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1894R.dimen.output_switcher_panel_icon_size);
            this.mTitleIcon.setLayoutParams(new LinearLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize));
            return;
        }
        this.mTitleGroup.setVisibility(8);
        this.mHeaderLayout.setGravity(1);
    }

    /* access modifiers changed from: private */
    public void enableTitle(CharSequence charSequence) {
        this.mPanelHeader.setVisibility(8);
        this.mTitleView.setVisibility(0);
        this.mTitleView.setAccessibilityPaneTitle(charSequence);
        this.mTitleView.setText(charSequence);
    }

    private void initTeslaIfNeeded() {
        boolean z = false;
        try {
            if (Settings.System.getInt(getContext().getContentResolver(), CmdObject.TESLA_KEY_PANEL_ACTIVE) == 0) {
                z = true;
            }
        } catch (Settings.SettingNotFoundException unused) {
        }
        this.mAdapter.initialTeslaView(z);
        if (z) {
            TeslaConnectPanelPlugin teslaConnectPanelPlugin = new TeslaConnectPanelPlugin(getContext());
            this.mTeslaPlugin = teslaConnectPanelPlugin;
            teslaConnectPanelPlugin.onCreate(new TeslaConnectPanelPlugin.LoadCallback() {
                public void onLoaded() {
                    ThreadUtils.postOnMainThread(new BluetoothPanelDialog$2$$ExternalSyntheticLambda1(this));
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onLoaded$0$com-nothing-systemui-qs-tiles-settings-panel-BluetoothPanelDialog$2 */
                public /* synthetic */ void mo57889xb71dffd3() {
                    NTLogUtil.m1686d(BluetoothPanelDialog.TAG, "updateTeslaInfos");
                    BluetoothPanelDialog.this.updateTeslaInfos();
                }

                public void onStatusChange(CmdObjectList cmdObjectList) {
                    ThreadUtils.postOnMainThread(new BluetoothPanelDialog$2$$ExternalSyntheticLambda0(this, cmdObjectList));
                }

                /* access modifiers changed from: package-private */
                /* renamed from: lambda$onStatusChange$1$com-nothing-systemui-qs-tiles-settings-panel-BluetoothPanelDialog$2 */
                public /* synthetic */ void mo57890xd34ebad1(CmdObjectList cmdObjectList) {
                    NTLogUtil.m1686d(BluetoothPanelDialog.TAG, "updateTeslaItemsStatus");
                    BluetoothPanelDialog.this.updateTeslaItemsStatus(cmdObjectList);
                }
            });
            this.mAdapter.setupTeslaPlugin(this.mTeslaPlugin);
        }
    }

    /* access modifiers changed from: private */
    public void updateTeslaItemsStatus(CmdObjectList cmdObjectList) {
        TeslaConnectPanelPlugin teslaConnectPanelPlugin;
        SettingsListAdapter settingsListAdapter = this.mAdapter;
        if (settingsListAdapter != null && (teslaConnectPanelPlugin = this.mTeslaPlugin) != null) {
            settingsListAdapter.updateTeslaItemStatus(teslaConnectPanelPlugin.getCmdList());
        }
    }

    /* access modifiers changed from: private */
    public void updateTeslaInfos() {
        TeslaConnectPanelPlugin teslaConnectPanelPlugin;
        SettingsListAdapter settingsListAdapter = this.mAdapter;
        if (settingsListAdapter != null && (teslaConnectPanelPlugin = this.mTeslaPlugin) != null) {
            settingsListAdapter.updateTeslaInfo(teslaConnectPanelPlugin.getCmdList());
        }
    }

    private void removeTeslaPlugin() {
        TeslaConnectPanelPlugin teslaConnectPanelPlugin = this.mTeslaPlugin;
        if (teslaConnectPanelPlugin != null) {
            teslaConnectPanelPlugin.onDestory();
        }
    }

    /* renamed from: com.nothing.systemui.qs.tiles.settings.panel.BluetoothPanelDialog$LocalPanelCallback */
    class LocalPanelCallback implements PanelContentCallback {
        public void forceClose() {
        }

        public void onCustomizedButtonStateChanged() {
        }

        LocalPanelCallback() {
        }

        public void onHeaderChanged() {
            ThreadUtils.postOnMainThread(new C4217x2f862a15(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onHeaderChanged$0$com-nothing-systemui-qs-tiles-settings-panel-BluetoothPanelDialog$LocalPanelCallback */
        public /* synthetic */ void mo57894x9b516e33() {
            BluetoothPanelDialog bluetoothPanelDialog = BluetoothPanelDialog.this;
            bluetoothPanelDialog.enablePanelHeader(bluetoothPanelDialog.mPanel.getIcon(), BluetoothPanelDialog.this.mPanel.getTitle(), BluetoothPanelDialog.this.mPanel.getSubTitle());
        }

        public void onTitleChanged() {
            ThreadUtils.postOnMainThread(new C4216x2f862a14(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onTitleChanged$1$com-nothing-systemui-qs-tiles-settings-panel-BluetoothPanelDialog$LocalPanelCallback */
        public /* synthetic */ void mo57896xec0e97e5() {
            BluetoothPanelDialog bluetoothPanelDialog = BluetoothPanelDialog.this;
            bluetoothPanelDialog.enableTitle(bluetoothPanelDialog.mPanel.getTitle());
        }

        public void onProgressBarVisibleChanged() {
            ThreadUtils.postOnMainThread(new C4215x2f862a13(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onProgressBarVisibleChanged$2$com-nothing-systemui-qs-tiles-settings-panel-BluetoothPanelDialog$LocalPanelCallback */
        public /* synthetic */ void mo57895x8a5d3990() {
            BluetoothPanelDialog.this.updateProgressBar();
        }
    }

    /* renamed from: com.nothing.systemui.qs.tiles.settings.panel.BluetoothPanelDialog$LayoutManagerWrapper */
    private class LayoutManagerWrapper extends LinearLayoutManager {
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        LayoutManagerWrapper(Context context) {
            super(context);
        }
    }
}
