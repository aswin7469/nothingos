package com.nt.settings.wifi.seeall;

import android.graphics.Outline;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.settings.R;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.nt.settings.wifi.seeall.WizardListActivityAdapter;
import java.util.List;
/* loaded from: classes2.dex */
public abstract class WizardListActivity extends AppCompatActivity {
    private static int NAVIGATION_BAR_SHOW_THRESHOLD;
    private static int NAVIGATION_BAR_TITLE_ALPHA_CHANGE_THRESHOLD;
    private WizardListActivityAdapter mAdapter;
    private ImageView mBackImageView;
    private int mHeaderHeight;
    private LinearLayout mHeaderLayout;
    private int mHeaderMarginTop;
    private TextView mHeaderTextView;
    private TextView mLeftFunctionTextView;
    private LinearLayoutManager mLinearManager;
    private ProgressBar mLoadingProgressBar;
    private View mNavigationBackgroundView;
    private int mNavigationBarHeight;
    private RecyclerView mRecyclerView;
    private TextView mRightFunctionTextView;
    private RippleUtils mRippleUtils;
    private TextView mSubHeaderTextView;
    private TextView mTitleTextView;
    private float mTotalScrollY;

    public abstract View getFooterView();

    public abstract int getItemResId();

    public abstract String getLeftFunctionText();

    public abstract String getRightFunctionText();

    public abstract int getRippleColor();

    public abstract String getSubTitleText();

    public abstract String getTitleText();

    public abstract void onBack();

    public abstract void onItemClick(View view, Object obj);

    public abstract void onItemShow(View view, Object obj);

    public abstract void onLeftFunctionClick();

    public abstract void onRightFunctionClick();

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.clearFlags(201326592);
        window.getDecorView().setSystemUiVisibility(1280);
        window.addFlags(Integer.MIN_VALUE);
        window.setStatusBarColor(0);
        setContentView(R.layout.activity_wizard_list);
        getWidgets();
        setWidgets();
        addListener();
    }

    private void getWidgets() {
        this.mRippleUtils = RippleUtils.getInstance();
        this.mAdapter = new WizardListActivityAdapter(this);
        this.mNavigationBackgroundView = findViewById(R.id.activity_wizard_list_background_view);
        this.mBackImageView = (ImageView) findViewById(R.id.activity_wizard_list_back_imageview);
        this.mTitleTextView = (TextView) findViewById(R.id.activity_wizard_list_title_textview);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.activity_wizard_list_recyclerview);
        this.mLeftFunctionTextView = (TextView) findViewById(R.id.activity_wizard_list_left_function_textview);
        this.mRightFunctionTextView = (TextView) findViewById(R.id.activity_wizard_list_right_function_textview);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.layout_wizard_list_header, (ViewGroup) null, false);
        this.mHeaderLayout = linearLayout;
        this.mHeaderTextView = (TextView) linearLayout.findViewById(R.id.layout_wizard_list_header_textview);
        this.mSubHeaderTextView = (TextView) this.mHeaderLayout.findViewById(R.id.layout_wizard_list_sub_header_textview);
        this.mLoadingProgressBar = (ProgressBar) this.mHeaderLayout.findViewById(R.id.layout_wizard_list_header_progressbar);
        this.mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.layout_wizard_list_header_height);
        this.mHeaderMarginTop = getResources().getDimensionPixelSize(R.dimen.layout_wizard_list_header_margin_top);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.activity_wizard_list_navigation_bar_height);
        this.mNavigationBarHeight = dimensionPixelSize;
        NAVIGATION_BAR_SHOW_THRESHOLD = this.mHeaderMarginTop - dimensionPixelSize;
        NAVIGATION_BAR_TITLE_ALPHA_CHANGE_THRESHOLD = (int) (this.mHeaderHeight * 0.5d);
        this.mLinearManager = new LinearLayoutManager(this);
    }

    private void setWidgets() {
        this.mRippleUtils.setRippleColor(getRippleColor());
        this.mRippleUtils.setRippleEffect(this.mBackImageView);
        this.mRippleUtils.setRippleEffect(this.mLeftFunctionTextView);
        this.mRippleUtils.setRippleEffect(this.mRightFunctionTextView);
        this.mTitleTextView.setText(getTitleText());
        this.mHeaderTextView.setText(getTitleText());
        this.mSubHeaderTextView.setText(getSubTitleText());
        this.mLeftFunctionTextView.setText(getLeftFunctionText());
        this.mLeftFunctionTextView.setVisibility(WizardManagerHelper.isPreDeferredSetupWizard(getIntent()) ? 8 : 0);
        this.mRightFunctionTextView.setText(getRightFunctionText());
        if (this.mLeftFunctionTextView.getText().length() == 0) {
            this.mLeftFunctionTextView.setVisibility(8);
        }
        if (this.mRightFunctionTextView.getText().length() == 0) {
            this.mRightFunctionTextView.setVisibility(8);
        }
        this.mAdapter.setHeaderView(this.mHeaderLayout);
        this.mAdapter.setItemResId(getItemResId());
        this.mAdapter.setFooterView(getFooterView());
        this.mRecyclerView.setLayoutManager(this.mLinearManager);
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    private void addListener() {
        this.mBackImageView.setOutlineProvider(new ViewOutlineProvider() { // from class: com.nt.settings.wifi.seeall.WizardListActivity.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(new Rect(0, 0, view.getWidth(), view.getHeight()), view.getHeight() / 2);
            }
        });
        this.mBackImageView.setClipToOutline(true);
        this.mBackImageView.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.wifi.seeall.WizardListActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                WizardListActivity.this.lambda$addListener$0(view);
            }
        });
        this.mAdapter.setCallBack(new WizardListActivityAdapter.WizardListActivityAdapterCallBack() { // from class: com.nt.settings.wifi.seeall.WizardListActivity.2
            @Override // com.nt.settings.wifi.seeall.WizardListActivityAdapter.WizardListActivityAdapterCallBack
            public void onItemClick(View view, Object obj) {
                WizardListActivity.this.onItemClick(view, obj);
            }

            @Override // com.nt.settings.wifi.seeall.WizardListActivityAdapter.WizardListActivityAdapterCallBack
            public void onItemShow(View view, Object obj) {
                WizardListActivity.this.onItemShow(view, obj);
            }
        });
        this.mLeftFunctionTextView.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.wifi.seeall.WizardListActivity$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                WizardListActivity.this.lambda$addListener$1(view);
            }
        });
        this.mRightFunctionTextView.setOnClickListener(new View.OnClickListener() { // from class: com.nt.settings.wifi.seeall.WizardListActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                WizardListActivity.this.lambda$addListener$2(view);
            }
        });
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.nt.settings.wifi.seeall.WizardListActivity.3
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                int findFirstVisibleItemPosition = WizardListActivity.this.mLinearManager.findFirstVisibleItemPosition();
                View findViewByPosition = WizardListActivity.this.mLinearManager.findViewByPosition(findFirstVisibleItemPosition);
                if (findFirstVisibleItemPosition == 0) {
                    WizardListActivity.this.mTotalScrollY = -findViewByPosition.getY();
                } else {
                    WizardListActivity wizardListActivity = WizardListActivity.this;
                    wizardListActivity.mTotalScrollY = ((wizardListActivity.mHeaderHeight + WizardListActivity.this.mHeaderMarginTop) - findViewByPosition.getY()) + ((findFirstVisibleItemPosition - 1) * findViewByPosition.getHeight());
                }
                if (WizardListActivity.this.mTotalScrollY > WizardListActivity.NAVIGATION_BAR_SHOW_THRESHOLD) {
                    WizardListActivity.this.mNavigationBackgroundView.setAlpha(1.0f);
                    float f = 1.0f - ((WizardListActivity.this.mTotalScrollY - WizardListActivity.NAVIGATION_BAR_SHOW_THRESHOLD) / WizardListActivity.NAVIGATION_BAR_TITLE_ALPHA_CHANGE_THRESHOLD);
                    int i3 = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
                    WizardListActivity.this.mHeaderTextView.setAlpha(i3 < 0 ? 0.0f : f);
                    if (i3 < 0) {
                        WizardListActivity.this.mTitleTextView.setAlpha(Math.abs(f));
                        return;
                    } else {
                        WizardListActivity.this.mTitleTextView.setAlpha(0.0f);
                        return;
                    }
                }
                WizardListActivity.this.mNavigationBackgroundView.setAlpha(0.0f);
                WizardListActivity.this.mHeaderTextView.setAlpha(1.0f);
                WizardListActivity.this.mTitleTextView.setAlpha(0.0f);
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                if (i == 0) {
                    WizardListActivity.this.amendScrollY();
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addListener$0(View view) {
        onBack();
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addListener$1(View view) {
        onLeftFunctionClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$addListener$2(View view) {
        onRightFunctionClick();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void amendScrollY() {
        float alpha = this.mHeaderTextView.getAlpha();
        if (this.mTotalScrollY > (this.mHeaderHeight + this.mHeaderMarginTop) - this.mNavigationBarHeight) {
            return;
        }
        boolean canScrollVertically = this.mRecyclerView.canScrollVertically(-1);
        boolean canScrollVertically2 = this.mRecyclerView.canScrollVertically(1);
        int findLastVisibleItemPosition = this.mLinearManager.findLastVisibleItemPosition();
        if (alpha < 0.5d) {
            if (!canScrollVertically2) {
                return;
            }
            this.mRecyclerView.smoothScrollBy(0, ((this.mHeaderHeight + this.mHeaderMarginTop) - this.mNavigationBarHeight) - ((int) this.mTotalScrollY));
        } else if (!canScrollVertically || findLastVisibleItemPosition == this.mAdapter.getItemCount() - 1) {
        } else {
            this.mRecyclerView.smoothScrollBy(0, -((int) this.mTotalScrollY));
        }
    }

    public void showData(List<?> list) {
        this.mAdapter.setData(list);
        this.mAdapter.notifyDataSetChanged();
    }

    public ProgressBar getProgressBar() {
        return this.mLoadingProgressBar;
    }
}
