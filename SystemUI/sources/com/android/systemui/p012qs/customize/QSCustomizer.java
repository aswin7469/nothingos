package com.android.systemui.p012qs.customize;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.LinearLayout;
import android.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.C1894R;
import com.android.systemui.p012qs.QSDetailClipper;
import com.android.systemui.p012qs.QSUtils;
import com.android.systemui.plugins.p011qs.C2304QS;
import com.android.systemui.plugins.p011qs.QSContainerController;
import com.android.systemui.statusbar.phone.LightBarController;

/* renamed from: com.android.systemui.qs.customize.QSCustomizer */
public class QSCustomizer extends LinearLayout {
    static final String EXTRA_QS_CUSTOMIZING = "qs_customizing";
    static final int MENU_RESET = 1;
    /* access modifiers changed from: private */
    public boolean isShown;
    private final QSDetailClipper mClipper;
    private final Animator.AnimatorListener mCollapseAnimationListener = new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator animator) {
            if (!QSCustomizer.this.isShown) {
                QSCustomizer.this.setVisibility(8);
            }
            QSCustomizer.this.mQsContainerController.setCustomizerAnimating(false);
        }

        public void onAnimationCancel(Animator animator) {
            if (!QSCustomizer.this.isShown) {
                QSCustomizer.this.setVisibility(8);
            }
            QSCustomizer.this.mQsContainerController.setCustomizerAnimating(false);
        }
    };
    private boolean mCustomizing;
    private boolean mIsShowingNavBackdrop;
    /* access modifiers changed from: private */
    public boolean mOpening;
    /* access modifiers changed from: private */
    public C2304QS mQs;
    /* access modifiers changed from: private */
    public QSContainerController mQsContainerController;
    /* access modifiers changed from: private */
    public final RecyclerView mRecyclerView;
    private final View mTransparentView;

    /* renamed from: mX */
    private int f330mX;

    /* renamed from: mY */
    private int f331mY;

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return super.generateLayoutParams(layoutParams);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public QSCustomizer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(getContext()).inflate(C1894R.layout.qs_customize_panel_content, this);
        this.mClipper = new QSDetailClipper(findViewById(C1894R.C1898id.customize_container));
        Toolbar toolbar = (Toolbar) findViewById(16908731);
        TypedValue typedValue = new TypedValue();
        this.mContext.getTheme().resolveAttribute(16843531, typedValue, true);
        toolbar.setNavigationIcon(getResources().getDrawable(typedValue.resourceId, this.mContext.getTheme()));
        toolbar.getMenu().add(0, 1, 0, this.mContext.getString(17041387));
        toolbar.setTitle(C1894R.string.qs_edit);
        RecyclerView recyclerView = (RecyclerView) findViewById(16908298);
        this.mRecyclerView = recyclerView;
        this.mTransparentView = findViewById(C1894R.C1898id.customizer_transparent_view);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setMoveDuration(150);
        recyclerView.setItemAnimator(defaultItemAnimator);
    }

    /* access modifiers changed from: package-private */
    public void updateResources() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mTransparentView.getLayoutParams();
        layoutParams.height = QSUtils.getQsHeaderSystemIconsAreaHeight(this.mContext);
        this.mTransparentView.setLayoutParams(layoutParams);
        this.mRecyclerView.getAdapter().notifyItemChanged(0);
    }

    /* access modifiers changed from: package-private */
    public void updateNavBackDrop(Configuration configuration, LightBarController lightBarController) {
        View findViewById = findViewById(C1894R.C1898id.nav_bar_background);
        int i = 0;
        boolean z = configuration.smallestScreenWidthDp >= 600 || configuration.orientation != 2;
        this.mIsShowingNavBackdrop = z;
        if (findViewById != null) {
            if (!z) {
                i = 8;
            }
            findViewById.setVisibility(i);
        }
        updateNavColors(lightBarController);
    }

    /* access modifiers changed from: package-private */
    public void updateNavColors(LightBarController lightBarController) {
        lightBarController.setQsCustomizing(this.mIsShowingNavBackdrop && this.isShown);
    }

    public void setContainerController(QSContainerController qSContainerController) {
        this.mQsContainerController = qSContainerController;
    }

    public void setQs(C2304QS qs) {
        this.mQs = qs;
    }

    /* access modifiers changed from: package-private */
    public void show(int i, int i2, TileAdapter tileAdapter) {
        if (!this.isShown) {
            this.mRecyclerView.getLayoutManager().scrollToPosition(0);
            int[] locationOnScreen = findViewById(C1894R.C1898id.customize_container).getLocationOnScreen();
            this.f330mX = i - locationOnScreen[0];
            this.f331mY = i2 - locationOnScreen[1];
            this.isShown = true;
            this.mOpening = true;
            setVisibility(0);
            this.mClipper.animateCircularClip(this.f330mX, this.f331mY, true, new ExpandAnimatorListener(tileAdapter));
            this.mQsContainerController.setCustomizerAnimating(true);
            this.mQsContainerController.setCustomizerShowing(true);
        }
    }

    /* access modifiers changed from: package-private */
    public void showImmediately() {
        if (!this.isShown) {
            this.mRecyclerView.getLayoutManager().scrollToPosition(0);
            setVisibility(0);
            this.mClipper.cancelAnimator();
            this.mClipper.showBackground();
            this.isShown = true;
            setCustomizing(true);
            this.mQsContainerController.setCustomizerAnimating(false);
            this.mQsContainerController.setCustomizerShowing(true);
        }
    }

    public void hide(boolean z) {
        if (this.isShown) {
            this.isShown = false;
            this.mClipper.cancelAnimator();
            this.mOpening = false;
            if (z) {
                this.mClipper.animateCircularClip(this.f330mX, this.f331mY, false, this.mCollapseAnimationListener);
            } else {
                setVisibility(8);
            }
            this.mQsContainerController.setCustomizerAnimating(z);
            this.mQsContainerController.setCustomizerShowing(false);
        }
    }

    public boolean isShown() {
        return this.isShown;
    }

    /* access modifiers changed from: package-private */
    public void setCustomizing(boolean z) {
        this.mCustomizing = z;
        this.mQs.notifyCustomizeChanged();
    }

    public boolean isCustomizing() {
        return this.mCustomizing || this.mOpening;
    }

    public void setEditLocation(int i, int i2) {
        int[] locationOnScreen = findViewById(C1894R.C1898id.customize_container).getLocationOnScreen();
        this.f330mX = i - locationOnScreen[0];
        this.f331mY = i2 - locationOnScreen[1];
    }

    /* renamed from: com.android.systemui.qs.customize.QSCustomizer$ExpandAnimatorListener */
    class ExpandAnimatorListener extends AnimatorListenerAdapter {
        private final TileAdapter mTileAdapter;

        ExpandAnimatorListener(TileAdapter tileAdapter) {
            this.mTileAdapter = tileAdapter;
        }

        public void onAnimationEnd(Animator animator) {
            if (QSCustomizer.this.isShown) {
                QSCustomizer.this.setCustomizing(true);
            }
            boolean unused = QSCustomizer.this.mOpening = false;
            QSCustomizer.this.mQsContainerController.setCustomizerAnimating(false);
            QSCustomizer.this.mRecyclerView.setAdapter(this.mTileAdapter);
        }

        public void onAnimationCancel(Animator animator) {
            boolean unused = QSCustomizer.this.mOpening = false;
            QSCustomizer.this.mQs.notifyCustomizeChanged();
            QSCustomizer.this.mQsContainerController.setCustomizerAnimating(false);
        }
    }

    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    public boolean isOpening() {
        return this.mOpening;
    }
}
