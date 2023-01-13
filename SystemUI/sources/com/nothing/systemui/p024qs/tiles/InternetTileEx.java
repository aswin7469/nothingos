package com.nothing.systemui.p024qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.text.DateFormat;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.C1894R;
import com.android.systemui.p012qs.tiles.CellularTile;
import com.android.systemui.p012qs.tiles.dialog.InternetDialog;
import com.android.systemui.p012qs.tiles.dialog.InternetDialogController;
import com.nothing.systemui.NTDependencyEx;
import com.nothing.systemui.p024qs.tileimpl.QSTileImplEx;
import com.nothing.systemui.util.NTLogUtil;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000z\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0019\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000 J2\u00020\u0001:\u0001JB\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u0006\u0010*\u001a\u00020+J6\u0010,\u001a\u00020+2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010-\u001a\u00020.2\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010/\u001a\u00020\u000e2\u0006\u00100\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u000b\u001a\u00020\fJ\u000e\u00101\u001a\u00020+2\u0006\u00102\u001a\u00020.J\u0010\u00103\u001a\u00020+2\u0006\u00104\u001a\u00020\u0012H\u0002J\u000e\u00105\u001a\u00020+2\u0006\u00102\u001a\u00020.J\u0010\u00106\u001a\u00020+2\u0006\u00104\u001a\u00020\u0012H\u0002J&\u00107\u001a\u00020\f2\u0006\u00108\u001a\u00020\u00122\u0006\u00109\u001a\u00020.2\u0006\u0010:\u001a\u00020.2\u0006\u0010;\u001a\u00020.JP\u0010<\u001a\u00020+2\u0006\u0010=\u001a\u00020\u00122\u0006\u0010>\u001a\u00020\u001f2\u0006\u0010?\u001a\u00020\u001b2\u0006\u0010@\u001a\u00020\u001b2\u0006\u0010A\u001a\u00020!2\u0006\u0010B\u001a\u00020\u001f2\u0006\u0010C\u001a\u00020\u001b2\u0006\u0010D\u001a\u00020\u001b2\u0006\u0010E\u001a\u00020!H\u0002J\u0016\u0010F\u001a\u00020+2\u0006\u0010G\u001a\u00020H2\u0006\u0010I\u001a\u00020\fR\u000e\u0010\u0003\u001a\u00020\u0004X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX.¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX.¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX.¢\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X.¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0017\u001a\u00020\u0018X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u0010X.¢\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX.¢\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0010X.¢\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001bX.¢\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX.¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X.¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020\u0010X.¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020\u001bX.¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020\u0010X.¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020\u001bX.¢\u0006\u0002\n\u0000R\u000e\u0010&\u001a\u00020\u001fX.¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020!X.¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020)X\u0004¢\u0006\u0002\n\u0000¨\u0006K"}, mo65043d2 = {"Lcom/nothing/systemui/qs/tiles/InternetTileEx;", "", "()V", "bgExecutor", "Ljava/util/concurrent/Executor;", "context", "Landroid/content/Context;", "dialogController", "Lcom/android/systemui/qs/tiles/dialog/InternetDialogController;", "internetDialog", "Lcom/android/systemui/qs/tiles/dialog/InternetDialog;", "isCheckedChange", "", "mainHandler", "Landroid/os/Handler;", "mobileTitleLayout", "Landroid/widget/LinearLayout;", "networkHeaderHeight", "", "networkHeaderPaddingStart", "networkLayoutHeight", "networkLayoutPaddingEnd", "networkLayoutPaddingStart", "onCheckedChangeListener", "Landroid/widget/CompoundButton$OnCheckedChangeListener;", "simOneMobileNetworkLayout", "simOneMobileSummaryText", "Landroid/widget/TextView;", "simOneMobileTitleLayout", "simOneMobileTitleText", "simOneRadioButton", "Landroid/widget/RadioButton;", "simOneSignalIcon", "Landroid/widget/ImageView;", "simTwoMobileNetworkLayout", "simTwoMobileSummaryText", "simTwoMobileTitleLayout", "simTwoMobileTitleText", "simTwoRadioButton", "simTwoSignalIcon", "tileImplEx", "Lcom/nothing/systemui/qs/tileimpl/QSTileImplEx;", "hideMultiSIMLayout", "", "init", "dialog", "Landroid/view/View;", "handler", "executor", "resetListeners", "wifiHeader", "setDefaultSubscription", "subId", "setListeners", "setUserPrefDataSubIdInDb", "shouldUpdateMultiSIMLayout", "defaultDataSubId", "networkLayout", "mobileSummaryText", "signalIcon", "updateMultiSIMLayout", "secondarySub", "sim1Button", "sim1MobileTitleText", "sim1MobileSummaryText", "sim1SignalIcon", "sim2Button", "sim2MobileTitleText", "sim2MobileSummaryText", "sim2SignalIcon", "updateWindowSize", "window", "Landroid/view/Window;", "isOnCreate", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* renamed from: com.nothing.systemui.qs.tiles.InternetTileEx */
/* compiled from: InternetTileEx.kt */
public final class InternetTileEx {
    private static final long COMPOUND_BUTTON_CHECKED_DELAY = 500;
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "InternetTileEx";
    private Executor bgExecutor;
    private Context context;
    private InternetDialogController dialogController;
    private InternetDialog internetDialog;
    private boolean isCheckedChange;
    private Handler mainHandler;
    private LinearLayout mobileTitleLayout;
    private int networkHeaderHeight;
    private int networkHeaderPaddingStart;
    private int networkLayoutHeight;
    private int networkLayoutPaddingEnd;
    private int networkLayoutPaddingStart;
    private final CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new InternetTileEx$$ExternalSyntheticLambda2(this);
    private LinearLayout simOneMobileNetworkLayout;
    private TextView simOneMobileSummaryText;
    private LinearLayout simOneMobileTitleLayout;
    private TextView simOneMobileTitleText;
    private RadioButton simOneRadioButton;
    private ImageView simOneSignalIcon;
    private LinearLayout simTwoMobileNetworkLayout;
    private TextView simTwoMobileSummaryText;
    private LinearLayout simTwoMobileTitleLayout;
    private TextView simTwoMobileTitleText;
    private RadioButton simTwoRadioButton;
    private ImageView simTwoSignalIcon;
    private final QSTileImplEx tileImplEx;

    @Inject
    public InternetTileEx() {
        Object obj = NTDependencyEx.get(QSTileImplEx.class);
        Intrinsics.checkNotNullExpressionValue(obj, "get(QSTileImplEx::class.java)");
        this.tileImplEx = (QSTileImplEx) obj;
    }

    /* access modifiers changed from: private */
    /* renamed from: onCheckedChangeListener$lambda-1  reason: not valid java name */
    public static final void m3532onCheckedChangeListener$lambda1(InternetTileEx internetTileEx, CompoundButton compoundButton, boolean z) {
        Intrinsics.checkNotNullParameter(internetTileEx, "this$0");
        internetTileEx.isCheckedChange = true;
        int id = compoundButton.getId();
        RadioButton radioButton = internetTileEx.simOneRadioButton;
        InternetDialogController internetDialogController = null;
        if (radioButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simOneRadioButton");
            radioButton = null;
        }
        if (id == radioButton.getId()) {
            RadioButton radioButton2 = internetTileEx.simTwoRadioButton;
            if (radioButton2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("simTwoRadioButton");
                radioButton2 = null;
            }
            radioButton2.setChecked(false);
            InternetDialogController internetDialogController2 = internetTileEx.dialogController;
            if (internetDialogController2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("dialogController");
            } else {
                internetDialogController = internetDialogController2;
            }
            internetTileEx.setDefaultSubscription(internetDialogController.getSubInfos().get(0).getSubscriptionId());
        } else {
            RadioButton radioButton3 = internetTileEx.simOneRadioButton;
            if (radioButton3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("simOneRadioButton");
                radioButton3 = null;
            }
            radioButton3.setChecked(false);
            InternetDialogController internetDialogController3 = internetTileEx.dialogController;
            if (internetDialogController3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("dialogController");
            } else {
                internetDialogController = internetDialogController3;
            }
            internetTileEx.setDefaultSubscription(internetDialogController.getSubInfos().get(1).getSubscriptionId());
        }
        compoundButton.postDelayed(new InternetTileEx$$ExternalSyntheticLambda1(internetTileEx), 500);
    }

    /* access modifiers changed from: private */
    /* renamed from: onCheckedChangeListener$lambda-1$lambda-0  reason: not valid java name */
    public static final void m3533onCheckedChangeListener$lambda1$lambda0(InternetTileEx internetTileEx) {
        Intrinsics.checkNotNullParameter(internetTileEx, "this$0");
        internetTileEx.isCheckedChange = false;
    }

    public final void init(InternetDialog internetDialog2, View view, InternetDialogController internetDialogController, Handler handler, Executor executor, Context context2) {
        Intrinsics.checkNotNullParameter(internetDialog2, "internetDialog");
        Intrinsics.checkNotNullParameter(view, "dialog");
        Intrinsics.checkNotNullParameter(internetDialogController, "dialogController");
        Intrinsics.checkNotNullParameter(handler, "handler");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(context2, "context");
        this.context = context2;
        this.dialogController = internetDialogController;
        this.internetDialog = internetDialog2;
        this.mainHandler = handler;
        this.bgExecutor = executor;
        View requireViewById = view.requireViewById(C1894R.C1898id.mobile_title_layout);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "dialog.requireViewById(R.id.mobile_title_layout)");
        this.mobileTitleLayout = (LinearLayout) requireViewById;
        View requireViewById2 = view.requireViewById(C1894R.C1898id.sim_one_mobile_network_layout);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "dialog.requireViewById(R…ne_mobile_network_layout)");
        this.simOneMobileNetworkLayout = (LinearLayout) requireViewById2;
        View requireViewById3 = view.requireViewById(C1894R.C1898id.sim_two_mobile_network_layout);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "dialog.requireViewById(R…wo_mobile_network_layout)");
        this.simTwoMobileNetworkLayout = (LinearLayout) requireViewById3;
        View requireViewById4 = view.requireViewById(C1894R.C1898id.sim_one_mobile_title_layout);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "dialog.requireViewById(R…_one_mobile_title_layout)");
        this.simOneMobileTitleLayout = (LinearLayout) requireViewById4;
        View requireViewById5 = view.requireViewById(C1894R.C1898id.sim_two_mobile_title_layout);
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "dialog.requireViewById(R…_two_mobile_title_layout)");
        this.simTwoMobileTitleLayout = (LinearLayout) requireViewById5;
        View requireViewById6 = view.requireViewById(C1894R.C1898id.sim_one_mobile_title);
        Intrinsics.checkNotNullExpressionValue(requireViewById6, "dialog.requireViewById(R.id.sim_one_mobile_title)");
        this.simOneMobileTitleText = (TextView) requireViewById6;
        View requireViewById7 = view.requireViewById(C1894R.C1898id.sim_two_mobile_title);
        Intrinsics.checkNotNullExpressionValue(requireViewById7, "dialog.requireViewById(R.id.sim_two_mobile_title)");
        this.simTwoMobileTitleText = (TextView) requireViewById7;
        View requireViewById8 = view.requireViewById(C1894R.C1898id.sim_one_mobile_summary);
        Intrinsics.checkNotNullExpressionValue(requireViewById8, "dialog.requireViewById(R…d.sim_one_mobile_summary)");
        this.simOneMobileSummaryText = (TextView) requireViewById8;
        View requireViewById9 = view.requireViewById(C1894R.C1898id.sim_two_mobile_summary);
        Intrinsics.checkNotNullExpressionValue(requireViewById9, "dialog.requireViewById(R…d.sim_two_mobile_summary)");
        this.simTwoMobileSummaryText = (TextView) requireViewById9;
        View requireViewById10 = view.requireViewById(C1894R.C1898id.sim_one_signal_icon);
        Intrinsics.checkNotNullExpressionValue(requireViewById10, "dialog.requireViewById(R.id.sim_one_signal_icon)");
        this.simOneSignalIcon = (ImageView) requireViewById10;
        View requireViewById11 = view.requireViewById(C1894R.C1898id.sim_two_signal_icon);
        Intrinsics.checkNotNullExpressionValue(requireViewById11, "dialog.requireViewById(R.id.sim_two_signal_icon)");
        this.simTwoSignalIcon = (ImageView) requireViewById11;
        View requireViewById12 = view.requireViewById(C1894R.C1898id.sim_one_mobile_toggle);
        Intrinsics.checkNotNullExpressionValue(requireViewById12, "dialog.requireViewById(R.id.sim_one_mobile_toggle)");
        this.simOneRadioButton = (RadioButton) requireViewById12;
        View requireViewById13 = view.requireViewById(C1894R.C1898id.sim_two_mobile_toggle);
        Intrinsics.checkNotNullExpressionValue(requireViewById13, "dialog.requireViewById(R.id.sim_two_mobile_toggle)");
        this.simTwoRadioButton = (RadioButton) requireViewById13;
        ImageView imageView = this.simOneSignalIcon;
        if (imageView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simOneSignalIcon");
            imageView = null;
        }
        Resources resources = imageView.getContext().getResources();
        this.networkHeaderHeight = resources.getDimensionPixelSize(C1894R.dimen.internet_dialog_wifi_network_height);
        this.networkLayoutHeight = resources.getDimensionPixelSize(C1894R.dimen.mobile_network_layout_height);
        this.networkLayoutPaddingEnd = resources.getDimensionPixelSize(C1894R.dimen.mobile_network_layout_padding_end);
        this.networkLayoutPaddingStart = resources.getDimensionPixelSize(C1894R.dimen.mobile_network_layout_padding_start);
        this.networkHeaderPaddingStart = resources.getDimensionPixelSize(C1894R.dimen.mobile_network_layout_header_padding_start);
    }

    public final boolean isCheckedChange() {
        return this.isCheckedChange;
    }

    public final void hideMultiSIMLayout() {
        LinearLayout linearLayout = this.simOneMobileNetworkLayout;
        LinearLayout linearLayout2 = null;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simOneMobileNetworkLayout");
            linearLayout = null;
        }
        linearLayout.setVisibility(8);
        LinearLayout linearLayout3 = this.simTwoMobileNetworkLayout;
        if (linearLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simTwoMobileNetworkLayout");
        } else {
            linearLayout2 = linearLayout3;
        }
        linearLayout2.setVisibility(8);
    }

    public final boolean shouldUpdateMultiSIMLayout(int i, View view, View view2, View view3) {
        RadioButton radioButton;
        TextView textView;
        TextView textView2;
        ImageView imageView;
        TextView textView3;
        RadioButton radioButton2;
        TextView textView4;
        TextView textView5;
        ImageView imageView2;
        View view4 = view;
        View view5 = view2;
        View view6 = view3;
        Intrinsics.checkNotNullParameter(view4, "networkLayout");
        Intrinsics.checkNotNullParameter(view5, "mobileSummaryText");
        Intrinsics.checkNotNullParameter(view6, "signalIcon");
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams != null) {
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
            InternetDialogController internetDialogController = this.dialogController;
            LinearLayout linearLayout = null;
            if (internetDialogController == null) {
                Intrinsics.throwUninitializedPropertyAccessException("dialogController");
                internetDialogController = null;
            }
            if (internetDialogController.getNumberOfActiveSubscriptions() > 1) {
                view5.setVisibility(8);
                view6.setVisibility(8);
                view4.setPaddingRelative(this.networkHeaderPaddingStart, 0, this.networkLayoutPaddingEnd, 0);
                layoutParams2.height = this.networkHeaderHeight;
                view4.setLayoutParams(layoutParams2);
                InternetDialogController internetDialogController2 = this.dialogController;
                if (internetDialogController2 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("dialogController");
                    internetDialogController2 = null;
                }
                if (internetDialogController2.isMobileDataEnabled()) {
                    LinearLayout linearLayout2 = this.simOneMobileNetworkLayout;
                    if (linearLayout2 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("simOneMobileNetworkLayout");
                        linearLayout2 = null;
                    }
                    linearLayout2.setVisibility(0);
                    LinearLayout linearLayout3 = this.simTwoMobileNetworkLayout;
                    if (linearLayout3 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("simTwoMobileNetworkLayout");
                        linearLayout3 = null;
                    }
                    linearLayout3.setVisibility(0);
                    InternetDialogController internetDialogController3 = this.dialogController;
                    if (internetDialogController3 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("dialogController");
                        internetDialogController3 = null;
                    }
                    if (i == internetDialogController3.getSubInfos().get(0).getSubscriptionId()) {
                        RadioButton radioButton3 = this.simOneRadioButton;
                        if (radioButton3 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("simOneRadioButton");
                            radioButton3 = null;
                        }
                        TextView textView6 = this.simOneMobileTitleText;
                        if (textView6 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("simOneMobileTitleText");
                            textView6 = null;
                        }
                        TextView textView7 = this.simOneMobileSummaryText;
                        if (textView7 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("simOneMobileSummaryText");
                            textView3 = null;
                        } else {
                            textView3 = textView7;
                        }
                        ImageView imageView3 = this.simOneSignalIcon;
                        if (imageView3 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("simOneSignalIcon");
                            imageView3 = null;
                        }
                        RadioButton radioButton4 = this.simTwoRadioButton;
                        if (radioButton4 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("simTwoRadioButton");
                            radioButton2 = null;
                        } else {
                            radioButton2 = radioButton4;
                        }
                        TextView textView8 = this.simTwoMobileTitleText;
                        if (textView8 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("simTwoMobileTitleText");
                            textView4 = null;
                        } else {
                            textView4 = textView8;
                        }
                        TextView textView9 = this.simTwoMobileSummaryText;
                        if (textView9 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("simTwoMobileSummaryText");
                            textView5 = null;
                        } else {
                            textView5 = textView9;
                        }
                        ImageView imageView4 = this.simTwoSignalIcon;
                        if (imageView4 == null) {
                            Intrinsics.throwUninitializedPropertyAccessException("simTwoSignalIcon");
                            imageView2 = null;
                        } else {
                            imageView2 = imageView4;
                        }
                        updateMultiSIMLayout(1, radioButton3, textView6, textView3, imageView3, radioButton2, textView4, textView5, imageView2);
                        return true;
                    }
                    RadioButton radioButton5 = this.simTwoRadioButton;
                    if (radioButton5 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("simTwoRadioButton");
                        radioButton5 = null;
                    }
                    TextView textView10 = this.simTwoMobileTitleText;
                    if (textView10 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("simTwoMobileTitleText");
                        textView10 = null;
                    }
                    TextView textView11 = this.simTwoMobileSummaryText;
                    if (textView11 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("simTwoMobileSummaryText");
                        textView11 = null;
                    }
                    ImageView imageView5 = this.simTwoSignalIcon;
                    if (imageView5 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("simTwoSignalIcon");
                        imageView5 = null;
                    }
                    RadioButton radioButton6 = this.simOneRadioButton;
                    if (radioButton6 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("simOneRadioButton");
                        radioButton = null;
                    } else {
                        radioButton = radioButton6;
                    }
                    TextView textView12 = this.simOneMobileTitleText;
                    if (textView12 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("simOneMobileTitleText");
                        textView = null;
                    } else {
                        textView = textView12;
                    }
                    TextView textView13 = this.simOneMobileSummaryText;
                    if (textView13 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("simOneMobileSummaryText");
                        textView2 = null;
                    } else {
                        textView2 = textView13;
                    }
                    ImageView imageView6 = this.simOneSignalIcon;
                    if (imageView6 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("simOneSignalIcon");
                        imageView = null;
                    } else {
                        imageView = imageView6;
                    }
                    updateMultiSIMLayout(0, radioButton5, textView10, textView11, imageView5, radioButton, textView, textView2, imageView);
                    return true;
                }
                LinearLayout linearLayout4 = this.simOneMobileNetworkLayout;
                if (linearLayout4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("simOneMobileNetworkLayout");
                    linearLayout4 = null;
                }
                linearLayout4.setVisibility(8);
                LinearLayout linearLayout5 = this.simTwoMobileNetworkLayout;
                if (linearLayout5 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("simTwoMobileNetworkLayout");
                } else {
                    linearLayout = linearLayout5;
                }
                linearLayout.setVisibility(8);
                return true;
            }
            view4.setPaddingRelative(this.networkLayoutPaddingStart, 0, this.networkLayoutPaddingEnd, 0);
            layoutParams2.height = this.networkLayoutHeight;
            view4.setLayoutParams(layoutParams2);
            return false;
        }
        throw new NullPointerException("null cannot be cast to non-null type android.widget.LinearLayout.LayoutParams");
    }

    private final void updateMultiSIMLayout(int i, RadioButton radioButton, TextView textView, TextView textView2, ImageView imageView, RadioButton radioButton2, TextView textView3, TextView textView4, ImageView imageView2) {
        radioButton.setChecked(true);
        InternetDialogController internetDialogController = this.dialogController;
        InternetDialogController internetDialogController2 = null;
        if (internetDialogController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialogController");
            internetDialogController = null;
        }
        textView.setText(internetDialogController.getMobileNetworkTitle());
        InternetDialog internetDialog2 = this.internetDialog;
        if (internetDialog2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("internetDialog");
            internetDialog2 = null;
        }
        if (!TextUtils.isEmpty(internetDialog2.getMobileNetworkSummary())) {
            QSTileImplEx qSTileImplEx = this.tileImplEx;
            int defaultDataSubscriptionId = SubscriptionManager.getDefaultDataSubscriptionId();
            Context context2 = this.context;
            if (context2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("context");
                context2 = null;
            }
            textView2.setText(qSTileImplEx.getDataUsage(defaultDataSubscriptionId, context2));
            textView2.setVisibility(0);
        } else {
            textView2.setVisibility(8);
        }
        InternetDialogController internetDialogController3 = this.dialogController;
        if (internetDialogController3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialogController");
            internetDialogController3 = null;
        }
        imageView.setImageDrawable(internetDialogController3.getSignalStrengthDrawable(SubscriptionManager.getDefaultDataSubscriptionId()));
        textView.setTextAppearance(C1894R.style.TextAppearance_InternetDialog);
        textView2.setTextAppearance(C1894R.style.TextAppearance_InternetDialog_Secondary);
        radioButton2.setChecked(false);
        InternetDialogController internetDialogController4 = this.dialogController;
        if (internetDialogController4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialogController");
            internetDialogController4 = null;
        }
        String obj = internetDialogController4.getSubInfos().get(i).getDisplayName().toString();
        InternetDialogController internetDialogController5 = this.dialogController;
        if (internetDialogController5 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialogController");
            internetDialogController5 = null;
        }
        int subscriptionId = internetDialogController5.getSubInfos().get(i).getSubscriptionId();
        textView3.setText(obj);
        QSTileImplEx qSTileImplEx2 = this.tileImplEx;
        Context context3 = this.context;
        if (context3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("context");
            context3 = null;
        }
        textView4.setText(qSTileImplEx2.getDataUsage(subscriptionId, context3));
        InternetDialogController internetDialogController6 = this.dialogController;
        if (internetDialogController6 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialogController");
        } else {
            internetDialogController2 = internetDialogController6;
        }
        imageView2.setImageDrawable(internetDialogController2.getSignalStrengthDrawable(subscriptionId));
        textView3.setTextAppearance(C1894R.style.TextAppearance_InternetDialog);
        textView4.setTextAppearance(C1894R.style.TextAppearance_InternetDialog_Secondary);
    }

    private final void setDefaultSubscription(int i) {
        InternetDialogController internetDialogController = this.dialogController;
        if (internetDialogController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialogController");
            internetDialogController = null;
        }
        internetDialogController.getSubscriptionManager().setDefaultDataSubId(i);
        setUserPrefDataSubIdInDb(i);
    }

    private final void setUserPrefDataSubIdInDb(int i) {
        Context context2 = this.context;
        if (context2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("context");
            context2 = null;
        }
        Settings.Global.putInt(context2.getContentResolver(), "user_preferred_data_sub", i);
    }

    public final void setListeners(View view) {
        Intrinsics.checkNotNullParameter(view, "wifiHeader");
        LinearLayout linearLayout = this.mobileTitleLayout;
        RadioButton radioButton = null;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mobileTitleLayout");
            linearLayout = null;
        }
        linearLayout.setOnClickListener(new InternetTileEx$$ExternalSyntheticLambda3(this));
        view.setOnClickListener(new InternetTileEx$$ExternalSyntheticLambda4(this));
        LinearLayout linearLayout2 = this.simOneMobileNetworkLayout;
        if (linearLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simOneMobileNetworkLayout");
            linearLayout2 = null;
        }
        linearLayout2.setOnClickListener(new InternetTileEx$$ExternalSyntheticLambda5(this));
        LinearLayout linearLayout3 = this.simTwoMobileNetworkLayout;
        if (linearLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simTwoMobileNetworkLayout");
            linearLayout3 = null;
        }
        linearLayout3.setOnClickListener(new InternetTileEx$$ExternalSyntheticLambda6(this));
        RadioButton radioButton2 = this.simOneRadioButton;
        if (radioButton2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simOneRadioButton");
            radioButton2 = null;
        }
        radioButton2.setOnCheckedChangeListener(this.onCheckedChangeListener);
        RadioButton radioButton3 = this.simTwoRadioButton;
        if (radioButton3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simTwoRadioButton");
        } else {
            radioButton = radioButton3;
        }
        radioButton.setOnCheckedChangeListener(this.onCheckedChangeListener);
    }

    /* access modifiers changed from: private */
    /* renamed from: setListeners$lambda-2  reason: not valid java name */
    public static final void m3534setListeners$lambda2(InternetTileEx internetTileEx, View view) {
        Intrinsics.checkNotNullParameter(internetTileEx, "this$0");
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        InternetDialogController internetDialogController = internetTileEx.dialogController;
        InternetDialog internetDialog2 = null;
        if (internetDialogController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialogController");
            internetDialogController = null;
        }
        internetDialogController.startActivityDismissingKeyguard(CellularTile.getCellularSettingIntent(), view);
        InternetDialog internetDialog3 = internetTileEx.internetDialog;
        if (internetDialog3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("internetDialog");
        } else {
            internetDialog2 = internetDialog3;
        }
        internetDialog2.dismiss();
    }

    /* access modifiers changed from: private */
    /* renamed from: setListeners$lambda-3  reason: not valid java name */
    public static final void m3535setListeners$lambda3(InternetTileEx internetTileEx, View view) {
        Intrinsics.checkNotNullParameter(internetTileEx, "this$0");
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        InternetDialogController internetDialogController = internetTileEx.dialogController;
        InternetDialog internetDialog2 = null;
        if (internetDialogController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialogController");
            internetDialogController = null;
        }
        internetDialogController.startActivityDismissingKeyguard(new Intent("android.settings.WIFI_SETTINGS"), view);
        InternetDialog internetDialog3 = internetTileEx.internetDialog;
        if (internetDialog3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("internetDialog");
        } else {
            internetDialog2 = internetDialog3;
        }
        internetDialog2.dismiss();
    }

    /* access modifiers changed from: private */
    /* renamed from: setListeners$lambda-4  reason: not valid java name */
    public static final void m3536setListeners$lambda4(InternetTileEx internetTileEx, View view) {
        Intrinsics.checkNotNullParameter(internetTileEx, "this$0");
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        RadioButton radioButton = internetTileEx.simOneRadioButton;
        RadioButton radioButton2 = null;
        if (radioButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simOneRadioButton");
            radioButton = null;
        }
        if (!radioButton.isChecked()) {
            RadioButton radioButton3 = internetTileEx.simOneRadioButton;
            if (radioButton3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("simOneRadioButton");
            } else {
                radioButton2 = radioButton3;
            }
            radioButton2.setChecked(true);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: setListeners$lambda-5  reason: not valid java name */
    public static final void m3537setListeners$lambda5(InternetTileEx internetTileEx, View view) {
        Intrinsics.checkNotNullParameter(internetTileEx, "this$0");
        Intrinsics.checkNotNullParameter(view, DateFormat.ABBR_GENERIC_TZ);
        RadioButton radioButton = internetTileEx.simTwoRadioButton;
        RadioButton radioButton2 = null;
        if (radioButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simTwoRadioButton");
            radioButton = null;
        }
        if (!radioButton.isChecked()) {
            RadioButton radioButton3 = internetTileEx.simTwoRadioButton;
            if (radioButton3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("simTwoRadioButton");
            } else {
                radioButton2 = radioButton3;
            }
            radioButton2.setChecked(true);
        }
    }

    public final void resetListeners(View view) {
        Intrinsics.checkNotNullParameter(view, "wifiHeader");
        LinearLayout linearLayout = this.mobileTitleLayout;
        if (linearLayout == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mobileTitleLayout");
            linearLayout = null;
        }
        linearLayout.setOnClickListener((View.OnClickListener) null);
        view.setOnClickListener((View.OnClickListener) null);
        LinearLayout linearLayout2 = this.simOneMobileNetworkLayout;
        if (linearLayout2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simOneMobileNetworkLayout");
            linearLayout2 = null;
        }
        linearLayout2.setOnClickListener((View.OnClickListener) null);
        LinearLayout linearLayout3 = this.simTwoMobileNetworkLayout;
        if (linearLayout3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simTwoMobileNetworkLayout");
            linearLayout3 = null;
        }
        linearLayout3.setOnClickListener((View.OnClickListener) null);
        RadioButton radioButton = this.simOneRadioButton;
        if (radioButton == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simOneRadioButton");
            radioButton = null;
        }
        radioButton.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
        RadioButton radioButton2 = this.simTwoRadioButton;
        if (radioButton2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("simTwoRadioButton");
            radioButton2 = null;
        }
        radioButton2.setOnCheckedChangeListener((CompoundButton.OnCheckedChangeListener) null);
    }

    public final void updateWindowSize(Window window, boolean z) {
        int i;
        Intrinsics.checkNotNullParameter(window, "window");
        InternetDialogController internetDialogController = this.dialogController;
        Context context2 = null;
        if (internetDialogController == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialogController");
            internetDialogController = null;
        }
        boolean hasActiveSubId = internetDialogController.hasActiveSubId();
        InternetDialogController internetDialogController2 = this.dialogController;
        if (internetDialogController2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialogController");
            internetDialogController2 = null;
        }
        boolean isWifiEnabled = internetDialogController2.isWifiEnabled();
        InternetDialogController internetDialogController3 = this.dialogController;
        if (internetDialogController3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialogController");
            internetDialogController3 = null;
        }
        boolean isMobileDataEnabled = internetDialogController3.isMobileDataEnabled();
        InternetDialogController internetDialogController4 = this.dialogController;
        if (internetDialogController4 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("dialogController");
            internetDialogController4 = null;
        }
        int numberOfActiveSubscriptions = internetDialogController4.getNumberOfActiveSubscriptions();
        Context context3 = this.context;
        if (context3 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("context");
            context3 = null;
        }
        int dimensionPixelSize = context3.getResources().getDimensionPixelSize(C1894R.dimen.settings_panel_width);
        if (!isWifiEnabled) {
            i = -2;
        } else if (!hasActiveSubId) {
            Context context4 = this.context;
            if (context4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("context");
            } else {
                context2 = context4;
            }
            i = context2.getResources().getDimensionPixelSize(C1894R.dimen.wifi_settings_panel_height);
        } else if (!isMobileDataEnabled || numberOfActiveSubscriptions <= 1) {
            Context context5 = this.context;
            if (context5 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("context");
            } else {
                context2 = context5;
            }
            i = context2.getResources().getDimensionPixelSize(C1894R.dimen.settings_panel_height);
        } else {
            Context context6 = this.context;
            if (context6 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("context");
            } else {
                context2 = context6;
            }
            i = context2.getResources().getDimensionPixelSize(C1894R.dimen.internet_settings_panel_max_height);
        }
        NTLogUtil.m1686d(TAG, "updateWindowSize: " + dimensionPixelSize + ", " + i);
        if (z) {
            window.setLayout(dimensionPixelSize, i);
        }
        ThreadUtils.postOnMainThread(new InternetTileEx$$ExternalSyntheticLambda0(window, dimensionPixelSize, i));
    }

    /* access modifiers changed from: private */
    /* renamed from: updateWindowSize$lambda-6  reason: not valid java name */
    public static final void m3538updateWindowSize$lambda6(Window window, int i, int i2) {
        Intrinsics.checkNotNullParameter(window, "$window");
        window.setLayout(i, i2);
    }

    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XT¢\u0006\u0002\n\u0000¨\u0006\u0007"}, mo65043d2 = {"Lcom/nothing/systemui/qs/tiles/InternetTileEx$Companion;", "", "()V", "COMPOUND_BUTTON_CHECKED_DELAY", "", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* renamed from: com.nothing.systemui.qs.tiles.InternetTileEx$Companion */
    /* compiled from: InternetTileEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
