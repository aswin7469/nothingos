package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.C1893R;
import com.android.systemui.controls.ControlsServiceInfo;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0016\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001$BU\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0016\b\u0002\u0010\f\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u000e\u0012\u0004\u0012\u00020\u000f0\r\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013¢\u0006\u0002\u0010\u0014J\b\u0010\u001b\u001a\u00020\u001cH\u0016J\u0018\u0010\u001d\u001a\u00020\u000f2\u0006\u0010\u001e\u001a\u00020\u00022\u0006\u0010\u001f\u001a\u00020\u001cH\u0016J\u0018\u0010 \u001a\u00020\u00022\u0006\u0010!\u001a\u00020\"2\u0006\u0010#\u001a\u00020\u001cH\u0016R\u0010\u0010\u0015\u001a\u00020\u0016X\u0004¢\u0006\u0004\n\u0002\u0010\u0017R\u000e\u0010\u0010\u001a\u00020\u0011X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u0019X\u000e¢\u0006\u0002\n\u0000R\u001c\u0010\f\u001a\u0010\u0012\u0006\u0012\u0004\u0018\u00010\u000e\u0012\u0004\u0012\u00020\u000f0\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0013X\u0004¢\u0006\u0002\n\u0000¨\u0006%"}, mo64987d2 = {"Lcom/android/systemui/controls/management/AppAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/android/systemui/controls/management/AppAdapter$Holder;", "backgroundExecutor", "Ljava/util/concurrent/Executor;", "uiExecutor", "lifecycle", "Landroidx/lifecycle/Lifecycle;", "controlsListingController", "Lcom/android/systemui/controls/management/ControlsListingController;", "layoutInflater", "Landroid/view/LayoutInflater;", "onAppSelected", "Lkotlin/Function1;", "Landroid/content/ComponentName;", "", "favoritesRenderer", "Lcom/android/systemui/controls/management/FavoritesRenderer;", "resources", "Landroid/content/res/Resources;", "(Ljava/util/concurrent/Executor;Ljava/util/concurrent/Executor;Landroidx/lifecycle/Lifecycle;Lcom/android/systemui/controls/management/ControlsListingController;Landroid/view/LayoutInflater;Lkotlin/jvm/functions/Function1;Lcom/android/systemui/controls/management/FavoritesRenderer;Landroid/content/res/Resources;)V", "callback", "com/android/systemui/controls/management/AppAdapter$callback$1", "Lcom/android/systemui/controls/management/AppAdapter$callback$1;", "listOfServices", "", "Lcom/android/systemui/controls/ControlsServiceInfo;", "getItemCount", "", "onBindViewHolder", "holder", "index", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "i", "Holder", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: AppAdapter.kt */
public final class AppAdapter extends RecyclerView.Adapter<Holder> {
    private final AppAdapter$callback$1 callback;
    private final FavoritesRenderer favoritesRenderer;
    private final LayoutInflater layoutInflater;
    /* access modifiers changed from: private */
    public List<ControlsServiceInfo> listOfServices;
    private final Function1<ComponentName, Unit> onAppSelected;
    /* access modifiers changed from: private */
    public final Resources resources;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public /* synthetic */ AppAdapter(java.util.concurrent.Executor r11, java.util.concurrent.Executor r12, androidx.lifecycle.Lifecycle r13, com.android.systemui.controls.management.ControlsListingController r14, android.view.LayoutInflater r15, kotlin.jvm.functions.Function1 r16, com.android.systemui.controls.management.FavoritesRenderer r17, android.content.res.Resources r18, int r19, kotlin.jvm.internal.DefaultConstructorMarker r20) {
        /*
            r10 = this;
            r0 = r19 & 32
            if (r0 == 0) goto L_0x000a
            com.android.systemui.controls.management.AppAdapter$1 r0 = com.android.systemui.controls.management.AppAdapter.C20301.INSTANCE
            kotlin.jvm.functions.Function1 r0 = (kotlin.jvm.functions.Function1) r0
            r7 = r0
            goto L_0x000c
        L_0x000a:
            r7 = r16
        L_0x000c:
            r1 = r10
            r2 = r11
            r3 = r12
            r4 = r13
            r5 = r14
            r6 = r15
            r8 = r17
            r9 = r18
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.management.AppAdapter.<init>(java.util.concurrent.Executor, java.util.concurrent.Executor, androidx.lifecycle.Lifecycle, com.android.systemui.controls.management.ControlsListingController, android.view.LayoutInflater, kotlin.jvm.functions.Function1, com.android.systemui.controls.management.FavoritesRenderer, android.content.res.Resources, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }

    public AppAdapter(Executor executor, Executor executor2, Lifecycle lifecycle, ControlsListingController controlsListingController, LayoutInflater layoutInflater2, Function1<? super ComponentName, Unit> function1, FavoritesRenderer favoritesRenderer2, Resources resources2) {
        Intrinsics.checkNotNullParameter(executor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(executor2, "uiExecutor");
        Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
        Intrinsics.checkNotNullParameter(controlsListingController, "controlsListingController");
        Intrinsics.checkNotNullParameter(layoutInflater2, "layoutInflater");
        Intrinsics.checkNotNullParameter(function1, "onAppSelected");
        Intrinsics.checkNotNullParameter(favoritesRenderer2, "favoritesRenderer");
        Intrinsics.checkNotNullParameter(resources2, "resources");
        this.layoutInflater = layoutInflater2;
        this.onAppSelected = function1;
        this.favoritesRenderer = favoritesRenderer2;
        this.resources = resources2;
        this.listOfServices = CollectionsKt.emptyList();
        AppAdapter$callback$1 appAdapter$callback$1 = new AppAdapter$callback$1(executor, this, executor2);
        this.callback = appAdapter$callback$1;
        controlsListingController.observe(lifecycle, appAdapter$callback$1);
    }

    public Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        View inflate = this.layoutInflater.inflate(C1893R.layout.controls_app_item, viewGroup, false);
        Intrinsics.checkNotNullExpressionValue(inflate, "layoutInflater.inflate(R…_app_item, parent, false)");
        return new Holder(inflate, this.favoritesRenderer);
    }

    public int getItemCount() {
        return this.listOfServices.size();
    }

    public void onBindViewHolder(Holder holder, int i) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        holder.bindData(this.listOfServices.get(i));
        holder.itemView.setOnClickListener(new AppAdapter$$ExternalSyntheticLambda0(this, i));
    }

    /* access modifiers changed from: private */
    /* renamed from: onBindViewHolder$lambda-0  reason: not valid java name */
    public static final void m2632onBindViewHolder$lambda0(AppAdapter appAdapter, int i, View view) {
        Intrinsics.checkNotNullParameter(appAdapter, "this$0");
        appAdapter.onAppSelected.invoke(ComponentName.unflattenFromString(appAdapter.listOfServices.get(i).getKey()));
    }

    @Metadata(mo64986d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u000e\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0012"}, mo64987d2 = {"Lcom/android/systemui/controls/management/AppAdapter$Holder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "favRenderer", "Lcom/android/systemui/controls/management/FavoritesRenderer;", "(Landroid/view/View;Lcom/android/systemui/controls/management/FavoritesRenderer;)V", "getFavRenderer", "()Lcom/android/systemui/controls/management/FavoritesRenderer;", "favorites", "Landroid/widget/TextView;", "icon", "Landroid/widget/ImageView;", "title", "bindData", "", "data", "Lcom/android/systemui/controls/ControlsServiceInfo;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: AppAdapter.kt */
    public static final class Holder extends RecyclerView.ViewHolder {
        private final FavoritesRenderer favRenderer;
        private final TextView favorites;
        private final ImageView icon;
        private final TextView title;

        /* JADX INFO: super call moved to the top of the method (can break code semantics) */
        public Holder(View view, FavoritesRenderer favoritesRenderer) {
            super(view);
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(favoritesRenderer, "favRenderer");
            this.favRenderer = favoritesRenderer;
            View requireViewById = this.itemView.requireViewById(16908294);
            Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById…droid.internal.R.id.icon)");
            this.icon = (ImageView) requireViewById;
            View requireViewById2 = this.itemView.requireViewById(16908310);
            Intrinsics.checkNotNullExpressionValue(requireViewById2, "itemView.requireViewById…roid.internal.R.id.title)");
            this.title = (TextView) requireViewById2;
            View requireViewById3 = this.itemView.requireViewById(C1893R.C1897id.favorites);
            Intrinsics.checkNotNullExpressionValue(requireViewById3, "itemView.requireViewById(R.id.favorites)");
            this.favorites = (TextView) requireViewById3;
        }

        public final FavoritesRenderer getFavRenderer() {
            return this.favRenderer;
        }

        public final void bindData(ControlsServiceInfo controlsServiceInfo) {
            Intrinsics.checkNotNullParameter(controlsServiceInfo, "data");
            this.icon.setImageDrawable(controlsServiceInfo.loadIcon());
            this.title.setText(controlsServiceInfo.loadLabel());
            FavoritesRenderer favoritesRenderer = this.favRenderer;
            ComponentName componentName = controlsServiceInfo.componentName;
            Intrinsics.checkNotNullExpressionValue(componentName, "data.componentName");
            String renderFavoritesForComponent = favoritesRenderer.renderFavoritesForComponent(componentName);
            this.favorites.setText(renderFavoritesForComponent);
            this.favorites.setVisibility(renderFavoritesForComponent == null ? 8 : 0);
        }
    }
}
