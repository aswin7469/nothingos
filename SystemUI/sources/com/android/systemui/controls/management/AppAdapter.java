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
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.management.ControlsListingController;
import java.text.Collator;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: AppAdapter.kt */
/* loaded from: classes.dex */
public final class AppAdapter extends RecyclerView.Adapter<Holder> {
    @NotNull
    private final AppAdapter$callback$1 callback;
    @NotNull
    private final FavoritesRenderer favoritesRenderer;
    @NotNull
    private final LayoutInflater layoutInflater;
    @NotNull
    private List<ControlsServiceInfo> listOfServices;
    @NotNull
    private final Function1<ComponentName, Unit> onAppSelected;
    @NotNull
    private final Resources resources;

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v2, types: [com.android.systemui.controls.management.AppAdapter$callback$1, java.lang.Object] */
    public AppAdapter(@NotNull final Executor backgroundExecutor, @NotNull final Executor uiExecutor, @NotNull Lifecycle lifecycle, @NotNull ControlsListingController controlsListingController, @NotNull LayoutInflater layoutInflater, @NotNull Function1<? super ComponentName, Unit> onAppSelected, @NotNull FavoritesRenderer favoritesRenderer, @NotNull Resources resources) {
        List<ControlsServiceInfo> emptyList;
        Intrinsics.checkNotNullParameter(backgroundExecutor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(uiExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
        Intrinsics.checkNotNullParameter(controlsListingController, "controlsListingController");
        Intrinsics.checkNotNullParameter(layoutInflater, "layoutInflater");
        Intrinsics.checkNotNullParameter(onAppSelected, "onAppSelected");
        Intrinsics.checkNotNullParameter(favoritesRenderer, "favoritesRenderer");
        Intrinsics.checkNotNullParameter(resources, "resources");
        this.layoutInflater = layoutInflater;
        this.onAppSelected = onAppSelected;
        this.favoritesRenderer = favoritesRenderer;
        this.resources = resources;
        emptyList = CollectionsKt__CollectionsKt.emptyList();
        this.listOfServices = emptyList;
        ?? r6 = new ControlsListingController.ControlsListingCallback() { // from class: com.android.systemui.controls.management.AppAdapter$callback$1
            @Override // com.android.systemui.controls.management.ControlsListingController.ControlsListingCallback
            public void onServicesUpdated(@NotNull final List<ControlsServiceInfo> serviceInfos) {
                Intrinsics.checkNotNullParameter(serviceInfos, "serviceInfos");
                Executor executor = backgroundExecutor;
                final AppAdapter appAdapter = this;
                final Executor executor2 = uiExecutor;
                executor.execute(new Runnable() { // from class: com.android.systemui.controls.management.AppAdapter$callback$1$onServicesUpdated$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Resources resources2;
                        List sortedWith;
                        resources2 = AppAdapter.this.resources;
                        final Collator collator = Collator.getInstance(resources2.getConfiguration().getLocales().get(0));
                        Intrinsics.checkNotNullExpressionValue(collator, "collator");
                        Comparator<T> comparator = new Comparator<T>() { // from class: com.android.systemui.controls.management.AppAdapter$callback$1$onServicesUpdated$1$run$$inlined$compareBy$1
                            @Override // java.util.Comparator
                            public final int compare(T t, T t2) {
                                Comparator comparator2 = collator;
                                CharSequence loadLabel = ((ControlsServiceInfo) t).loadLabel();
                                Intrinsics.checkNotNullExpressionValue(loadLabel, "it.loadLabel()");
                                CharSequence loadLabel2 = ((ControlsServiceInfo) t2).loadLabel();
                                Intrinsics.checkNotNullExpressionValue(loadLabel2, "it.loadLabel()");
                                return comparator2.compare(loadLabel, loadLabel2);
                            }
                        };
                        AppAdapter appAdapter2 = AppAdapter.this;
                        sortedWith = CollectionsKt___CollectionsKt.sortedWith(serviceInfos, comparator);
                        appAdapter2.listOfServices = sortedWith;
                        Executor executor3 = executor2;
                        final AppAdapter appAdapter3 = AppAdapter.this;
                        executor3.execute(new Runnable() { // from class: com.android.systemui.controls.management.AppAdapter$callback$1$onServicesUpdated$1.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                AppAdapter.this.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        };
        this.callback = r6;
        controlsListingController.observe(lifecycle, (Lifecycle) r6);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NotNull
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public Holder mo1838onCreateViewHolder(@NotNull ViewGroup parent, int i) {
        Intrinsics.checkNotNullParameter(parent, "parent");
        View inflate = this.layoutInflater.inflate(R$layout.controls_app_item, parent, false);
        Intrinsics.checkNotNullExpressionValue(inflate, "layoutInflater.inflate(R.layout.controls_app_item, parent, false)");
        return new Holder(inflate, this.favoritesRenderer);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.listOfServices.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NotNull Holder holder, final int i) {
        Intrinsics.checkNotNullParameter(holder, "holder");
        holder.bindData(this.listOfServices.get(i));
        holder.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.controls.management.AppAdapter$onBindViewHolder$1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                Function1 function1;
                List list;
                function1 = AppAdapter.this.onAppSelected;
                list = AppAdapter.this.listOfServices;
                function1.mo1949invoke(ComponentName.unflattenFromString(((ControlsServiceInfo) list.get(i)).getKey()));
            }
        });
    }

    /* compiled from: AppAdapter.kt */
    /* loaded from: classes.dex */
    public static final class Holder extends RecyclerView.ViewHolder {
        @NotNull
        private final FavoritesRenderer favRenderer;
        @NotNull
        private final TextView favorites;
        @NotNull
        private final ImageView icon;
        @NotNull
        private final TextView title;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public Holder(@NotNull View view, @NotNull FavoritesRenderer favRenderer) {
            super(view);
            Intrinsics.checkNotNullParameter(view, "view");
            Intrinsics.checkNotNullParameter(favRenderer, "favRenderer");
            this.favRenderer = favRenderer;
            View requireViewById = this.itemView.requireViewById(16908294);
            Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById(com.android.internal.R.id.icon)");
            this.icon = (ImageView) requireViewById;
            View requireViewById2 = this.itemView.requireViewById(16908310);
            Intrinsics.checkNotNullExpressionValue(requireViewById2, "itemView.requireViewById(com.android.internal.R.id.title)");
            this.title = (TextView) requireViewById2;
            View requireViewById3 = this.itemView.requireViewById(R$id.favorites);
            Intrinsics.checkNotNullExpressionValue(requireViewById3, "itemView.requireViewById(R.id.favorites)");
            this.favorites = (TextView) requireViewById3;
        }

        public final void bindData(@NotNull ControlsServiceInfo data) {
            Intrinsics.checkNotNullParameter(data, "data");
            this.icon.setImageDrawable(data.loadIcon());
            this.title.setText(data.loadLabel());
            FavoritesRenderer favoritesRenderer = this.favRenderer;
            ComponentName componentName = data.componentName;
            Intrinsics.checkNotNullExpressionValue(componentName, "data.componentName");
            String renderFavoritesForComponent = favoritesRenderer.renderFavoritesForComponent(componentName);
            this.favorites.setText(renderFavoritesForComponent);
            this.favorites.setVisibility(renderFavoritesForComponent == null ? 8 : 0);
        }
    }
}
