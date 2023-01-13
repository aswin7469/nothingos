package com.android.systemui.controls.management;

import android.text.TextUtils;
import android.widget.TextView;
import androidx.viewpager2.widget.ViewPager2;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\u0005H\u0016J\u0010\u0010\t\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\n"}, mo65043d2 = {"com/android/systemui/controls/management/ControlsFavoritingActivity$setUpPager$1$1", "Landroidx/viewpager2/widget/ViewPager2$OnPageChangeCallback;", "onPageScrolled", "", "position", "", "positionOffset", "", "positionOffsetPixels", "onPageSelected", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlsFavoritingActivity.kt */
public final class ControlsFavoritingActivity$setUpPager$1$1 extends ViewPager2.OnPageChangeCallback {
    final /* synthetic */ ControlsFavoritingActivity this$0;

    ControlsFavoritingActivity$setUpPager$1$1(ControlsFavoritingActivity controlsFavoritingActivity) {
        this.this$0 = controlsFavoritingActivity;
    }

    public void onPageSelected(int i) {
        super.onPageSelected(i);
        CharSequence structureName = ((StructureContainer) this.this$0.listOfStructures.get(i)).getStructureName();
        if (TextUtils.isEmpty(structureName)) {
            structureName = this.this$0.appName;
        }
        TextView access$getTitleView$p = this.this$0.titleView;
        TextView textView = null;
        if (access$getTitleView$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("titleView");
            access$getTitleView$p = null;
        }
        access$getTitleView$p.setText(structureName);
        TextView access$getTitleView$p2 = this.this$0.titleView;
        if (access$getTitleView$p2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("titleView");
        } else {
            textView = access$getTitleView$p2;
        }
        textView.requestFocus();
    }

    public void onPageScrolled(int i, float f, int i2) {
        super.onPageScrolled(i, f, i2);
        ManagementPageIndicator access$getPageIndicator$p = this.this$0.pageIndicator;
        if (access$getPageIndicator$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
            access$getPageIndicator$p = null;
        }
        access$getPageIndicator$p.setLocation(((float) i) + f);
    }
}
