package com.nothing.systemui.privacy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.systemui.C1893R;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.PrivacyType;
import com.nothing.systemui.p024qs.MicModeView;
import com.nothing.systemui.privacy.PrivacyDialogControllerEx;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.NotImplementedError;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J&\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u0012R%\u0010\u0003\u001a\u0016\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0004j\n\u0012\u0006\u0012\u0004\u0018\u00010\u0005`\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\b¨\u0006\u0014"}, mo64987d2 = {"Lcom/nothing/systemui/privacy/PrivacyDialogEx;", "", "()V", "modeChoiceWhiteList", "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "getModeChoiceWhiteList", "()Ljava/util/ArrayList;", "createView", "", "context", "Landroid/content/Context;", "newView", "Landroid/view/View;", "element", "Lcom/android/systemui/privacy/PrivacyDialog$PrivacyElement;", "clickListener", "Landroid/view/View$OnClickListener;", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PrivacyDialogEx.kt */
public final class PrivacyDialogEx {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "PrivacyDialogEx";
    private final ArrayList<String> modeChoiceWhiteList = new PrivacyDialogEx$modeChoiceWhiteList$1();

    @Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: PrivacyDialogEx.kt */
    public /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[PrivacyType.values().length];
            iArr[PrivacyType.TYPE_CAMERA.ordinal()] = 1;
            iArr[PrivacyType.TYPE_MICROPHONE.ordinal()] = 2;
            iArr[PrivacyType.TYPE_LOCATION.ordinal()] = 3;
            $EnumSwitchMapping$0 = iArr;
        }
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/nothing/systemui/privacy/PrivacyDialogEx$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: PrivacyDialogEx.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final ArrayList<String> getModeChoiceWhiteList() {
        return this.modeChoiceWhiteList;
    }

    public final void createView(Context context, View view, PrivacyDialog.PrivacyElement privacyElement, View.OnClickListener onClickListener) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(view, "newView");
        Intrinsics.checkNotNullParameter(privacyElement, "element");
        Intrinsics.checkNotNullParameter(onClickListener, "clickListener");
        TextView textView = (TextView) view.requireViewById(C1893R.C1897id.text);
        textView.setTag(privacyElement);
        textView.setOnClickListener(onClickListener);
        if (!this.modeChoiceWhiteList.contains(privacyElement.getPackageName())) {
            int i = WhenMappings.$EnumSwitchMapping$0[privacyElement.getType().ordinal()];
            boolean z = true;
            if (i == 1) {
                View requireViewById = view.requireViewById(C1893R.C1897id.mode_container);
                if (requireViewById != null) {
                    ViewGroup viewGroup = (ViewGroup) requireViewById;
                    LayoutInflater.from(context).inflate(C1893R.layout.layout_privacy_item_camera, viewGroup, true);
                    viewGroup.setVisibility(0);
                    return;
                }
                throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
            } else if (i == 2) {
                PrivacyDialogControllerEx.MicModeInfo micModeInfo = privacyElement.getMicModeInfo();
                if (micModeInfo == null || !micModeInfo.getShowUI()) {
                    z = false;
                }
                if (z) {
                    View inflate = LayoutInflater.from(context).inflate(C1893R.layout.layout_privacy_item_audio, (ViewGroup) null, false);
                    if (inflate != null) {
                        MicModeView micModeView = (MicModeView) inflate;
                        micModeView.setPrivacyElement(privacyElement);
                        View requireViewById2 = view.requireViewById(C1893R.C1897id.mode_container);
                        if (requireViewById2 != null) {
                            ViewGroup viewGroup2 = (ViewGroup) requireViewById2;
                            viewGroup2.setVisibility(0);
                            viewGroup2.addView(micModeView);
                            return;
                        }
                        throw new NullPointerException("null cannot be cast to non-null type android.view.ViewGroup");
                    }
                    throw new NullPointerException("null cannot be cast to non-null type com.nothing.systemui.qs.MicModeView");
                }
            } else if (i == 3) {
                throw new NotImplementedError((String) null, 1, (DefaultConstructorMarker) null);
            }
        }
    }
}
