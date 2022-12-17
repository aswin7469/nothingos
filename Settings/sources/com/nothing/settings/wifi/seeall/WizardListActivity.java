package com.nothing.settings.wifi.seeall;

import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.Typeface;
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
import com.android.settings.R$dimen;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.google.android.setupcompat.util.WizardManagerHelper;
import com.nothing.settings.wifi.seeall.WizardListActivityAdapter;
import java.util.List;

public abstract class WizardListActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public static int NAVIGATION_BAR_SHOW_THRESHOLD;
    /* access modifiers changed from: private */
    public static int NAVIGATION_BAR_TITLE_ALPHA_CHANGE_THRESHOLD;
    private WizardListActivityAdapter mAdapter;
    private ImageView mBackImageView;
    /* access modifiers changed from: private */
    public int mHeaderHeight;
    private LinearLayout mHeaderLayout;
    /* access modifiers changed from: private */
    public int mHeaderMarginTop;
    /* access modifiers changed from: private */
    public TextView mHeaderTextView;
    private TextView mLeftFunctionTextView;
    /* access modifiers changed from: private */
    public LinearLayoutManager mLinearManager;
    private ProgressBar mLoadingProgressBar;
    /* access modifiers changed from: private */
    public View mNavigationBackgroundView;
    private int mNavigationBarHeight;
    private RecyclerView mRecyclerView;
    private TextView mRightFunctionTextView;
    private RippleUtils mRippleUtils;
    private TextView mSubHeaderTextView;
    /* access modifiers changed from: private */
    public TextView mTitleTextView;
    /* access modifiers changed from: private */
    public float mTotalScrollY;

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

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.clearFlags(201326592);
        window.getDecorView().setSystemUiVisibility(1280);
        window.setStatusBarColor(0);
        setContentView(R$layout.nt_activity_wizard_list);
        getWidgets();
        setWidgets();
        addListener();
    }

    private void getWidgets() {
        this.mRippleUtils = RippleUtils.getInstance();
        this.mAdapter = new WizardListActivityAdapter(this);
        this.mNavigationBackgroundView = findViewById(R$id.activity_wizard_list_background_view);
        this.mBackImageView = (ImageView) findViewById(R$id.activity_wizard_list_back_imageview);
        this.mTitleTextView = (TextView) findViewById(R$id.activity_wizard_list_title_textview);
        this.mRecyclerView = (RecyclerView) findViewById(R$id.activity_wizard_list_recyclerview);
        this.mLeftFunctionTextView = (TextView) findViewById(R$id.activity_wizard_list_left_function_textview);
        this.mRightFunctionTextView = (TextView) findViewById(R$id.activity_wizard_list_right_function_textview);
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this).inflate(R$layout.nt_layout_wizard_list_header, (ViewGroup) null, false);
        this.mHeaderLayout = linearLayout;
        this.mHeaderTextView = (TextView) linearLayout.findViewById(R$id.layout_wizard_list_header_textview);
        this.mSubHeaderTextView = (TextView) this.mHeaderLayout.findViewById(R$id.layout_wizard_list_sub_header_textview);
        this.mLoadingProgressBar = (ProgressBar) this.mHeaderLayout.findViewById(R$id.layout_wizard_list_header_progressbar);
        Typeface create = Typeface.create("NDot57", 0);
        this.mTitleTextView.setTypeface(create);
        this.mHeaderTextView.setTypeface(create);
        this.mHeaderHeight = getResources().getDimensionPixelSize(R$dimen.nt_layout_wizard_list_header_height);
        this.mHeaderMarginTop = getResources().getDimensionPixelSize(R$dimen.nt_layout_wizard_list_header_margin_top);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.nt_activity_wizard_list_navigation_bar_height);
        this.mNavigationBarHeight = dimensionPixelSize;
        NAVIGATION_BAR_SHOW_THRESHOLD = this.mHeaderMarginTop - dimensionPixelSize;
        NAVIGATION_BAR_TITLE_ALPHA_CHANGE_THRESHOLD = (int) (((double) this.mHeaderHeight) * 0.5d);
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
        this.mBackImageView.setOutlineProvider(new ViewOutlineProvider() {
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(new Rect(0, 0, view.getWidth(), view.getHeight()), (float) (view.getHeight() / 2));
            }
        });
        this.mBackImageView.setClipToOutline(true);
        this.mBackImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WizardListActivity.this.onBack();
                WizardListActivity.this.finish();
            }
        });
        this.mAdapter.setCallBack(new WizardListActivityAdapter.WizardListActivityAdapterCallBack() {
            public void onItemClick(View view, Object obj) {
                WizardListActivity.this.onItemClick(view, obj);
            }

            public void onItemShow(View view, Object obj) {
                WizardListActivity.this.onItemShow(view, obj);
            }
        });
        this.mLeftFunctionTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WizardListActivity.this.onLeftFunctionClick();
            }
        });
        this.mRightFunctionTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                WizardListActivity.this.onRightFunctionClick();
            }
        });
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
                int findFirstVisibleItemPosition = WizardListActivity.this.mLinearManager.findFirstVisibleItemPosition();
                View findViewByPosition = WizardListActivity.this.mLinearManager.findViewByPosition(findFirstVisibleItemPosition);
                if (findFirstVisibleItemPosition == 0) {
                    WizardListActivity.this.mTotalScrollY = -findViewByPosition.getY();
                } else {
                    WizardListActivity wizardListActivity = WizardListActivity.this;
                    wizardListActivity.mTotalScrollY = (((float) (wizardListActivity.mHeaderHeight + WizardListActivity.this.mHeaderMarginTop)) - findViewByPosition.getY()) + ((float) ((findFirstVisibleItemPosition - 1) * findViewByPosition.getHeight()));
                }
                if (WizardListActivity.this.mTotalScrollY > ((float) WizardListActivity.NAVIGATION_BAR_SHOW_THRESHOLD)) {
                    WizardListActivity.this.mNavigationBackgroundView.setAlpha(1.0f);
                    float r4 = 1.0f - ((WizardListActivity.this.mTotalScrollY - ((float) WizardListActivity.NAVIGATION_BAR_SHOW_THRESHOLD)) / ((float) WizardListActivity.NAVIGATION_BAR_TITLE_ALPHA_CHANGE_THRESHOLD));
                    int i3 = (r4 > 0.0f ? 1 : (r4 == 0.0f ? 0 : -1));
                    WizardListActivity.this.mHeaderTextView.setAlpha(i3 < 0 ? 0.0f : r4);
                    if (i3 < 0) {
                        WizardListActivity.this.mTitleTextView.setAlpha(Math.abs(r4));
                    } else {
                        WizardListActivity.this.mTitleTextView.setAlpha(0.0f);
                    }
                } else {
                    WizardListActivity.this.mNavigationBackgroundView.setAlpha(0.0f);
                    WizardListActivity.this.mHeaderTextView.setAlpha(1.0f);
                    WizardListActivity.this.mTitleTextView.setAlpha(0.0f);
                }
            }

            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                if (i == 0) {
                    WizardListActivity.this.amendScrollY();
                }
            }
        });
    }

    public void amendScrollY() {
        float alpha = this.mHeaderTextView.getAlpha();
        if (this.mTotalScrollY <= ((float) ((this.mHeaderHeight + this.mHeaderMarginTop) - this.mNavigationBarHeight))) {
            boolean canScrollVertically = this.mRecyclerView.canScrollVertically(-1);
            boolean canScrollVertically2 = this.mRecyclerView.canScrollVertically(1);
            int findLastVisibleItemPosition = this.mLinearManager.findLastVisibleItemPosition();
            if (((double) alpha) < 0.5d) {
                if (canScrollVertically2) {
                    this.mRecyclerView.smoothScrollBy(0, ((this.mHeaderHeight + this.mHeaderMarginTop) - this.mNavigationBarHeight) - ((int) this.mTotalScrollY));
                }
            } else if (canScrollVertically && findLastVisibleItemPosition != this.mAdapter.getItemCount() - 1) {
                this.mRecyclerView.smoothScrollBy(0, -((int) this.mTotalScrollY));
            }
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
