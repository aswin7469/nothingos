package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.C1893R;
import com.android.systemui.util.animation.TransitionLayout;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.collections.SetsKt;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u0018\u0000 $2\u00020\u0001:\u0001$B\u000f\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0016\u0010\u001e\u001a\u00020\u001f2\u0006\u0010 \u001a\u00020!2\u0006\u0010\"\u001a\u00020#R\u0019\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0017\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0017\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00060\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0017\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00160\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0012R\u0017\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00160\u000f¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0012R\u0011\u0010\u001a\u001a\u00020\u001b¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001d¨\u0006%"}, mo64987d2 = {"Lcom/android/systemui/media/RecommendationViewHolder;", "", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "cardIcon", "Landroid/widget/ImageView;", "kotlin.jvm.PlatformType", "getCardIcon", "()Landroid/widget/ImageView;", "gutsViewHolder", "Lcom/android/systemui/media/GutsViewHolder;", "getGutsViewHolder", "()Lcom/android/systemui/media/GutsViewHolder;", "mediaCoverContainers", "", "Landroid/view/ViewGroup;", "getMediaCoverContainers", "()Ljava/util/List;", "mediaCoverItems", "getMediaCoverItems", "mediaSubtitles", "Landroid/widget/TextView;", "getMediaSubtitles", "mediaTitles", "getMediaTitles", "recommendations", "Lcom/android/systemui/util/animation/TransitionLayout;", "getRecommendations", "()Lcom/android/systemui/util/animation/TransitionLayout;", "marquee", "", "start", "", "delay", "", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: RecommendationViewHolder.kt */
public final class RecommendationViewHolder {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final Set<Integer> controlsIds = SetsKt.setOf(Integer.valueOf((int) C1893R.C1897id.recommendation_card_icon), Integer.valueOf((int) C1893R.C1897id.media_cover1), Integer.valueOf((int) C1893R.C1897id.media_cover2), Integer.valueOf((int) C1893R.C1897id.media_cover3), Integer.valueOf((int) C1893R.C1897id.media_cover1_container), Integer.valueOf((int) C1893R.C1897id.media_cover2_container), Integer.valueOf((int) C1893R.C1897id.media_cover3_container), Integer.valueOf((int) C1893R.C1897id.media_title1), Integer.valueOf((int) C1893R.C1897id.media_title2), Integer.valueOf((int) C1893R.C1897id.media_title3), Integer.valueOf((int) C1893R.C1897id.media_subtitle1), Integer.valueOf((int) C1893R.C1897id.media_subtitle2), Integer.valueOf((int) C1893R.C1897id.media_subtitle3));
    private final ImageView cardIcon;
    private final GutsViewHolder gutsViewHolder;
    private final List<ViewGroup> mediaCoverContainers;
    private final List<ImageView> mediaCoverItems;
    private final List<TextView> mediaSubtitles;
    private final List<TextView> mediaTitles;
    private final TransitionLayout recommendations;

    public /* synthetic */ RecommendationViewHolder(View view, DefaultConstructorMarker defaultConstructorMarker) {
        this(view);
    }

    @JvmStatic
    public static final RecommendationViewHolder create(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return Companion.create(layoutInflater, viewGroup);
    }

    private RecommendationViewHolder(View view) {
        TransitionLayout transitionLayout = (TransitionLayout) view;
        this.recommendations = transitionLayout;
        this.cardIcon = (ImageView) view.requireViewById(C1893R.C1897id.recommendation_card_icon);
        View requireViewById = view.requireViewById(C1893R.C1897id.media_cover1);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById(R.id.media_cover1)");
        View requireViewById2 = view.requireViewById(C1893R.C1897id.media_cover2);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "itemView.requireViewById(R.id.media_cover2)");
        View requireViewById3 = view.requireViewById(C1893R.C1897id.media_cover3);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "itemView.requireViewById(R.id.media_cover3)");
        this.mediaCoverItems = CollectionsKt.listOf((ImageView) requireViewById, (ImageView) requireViewById2, (ImageView) requireViewById3);
        View requireViewById4 = view.requireViewById(C1893R.C1897id.media_cover1_container);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "itemView.requireViewById…d.media_cover1_container)");
        View requireViewById5 = view.requireViewById(C1893R.C1897id.media_cover2_container);
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "itemView.requireViewById…d.media_cover2_container)");
        View requireViewById6 = view.requireViewById(C1893R.C1897id.media_cover3_container);
        Intrinsics.checkNotNullExpressionValue(requireViewById6, "itemView.requireViewById…d.media_cover3_container)");
        List<ViewGroup> listOf = CollectionsKt.listOf((ViewGroup) requireViewById4, (ViewGroup) requireViewById5, (ViewGroup) requireViewById6);
        this.mediaCoverContainers = listOf;
        this.mediaTitles = CollectionsKt.listOf((TextView) view.requireViewById(C1893R.C1897id.media_title1), (TextView) view.requireViewById(C1893R.C1897id.media_title2), (TextView) view.requireViewById(C1893R.C1897id.media_title3));
        this.mediaSubtitles = CollectionsKt.listOf((TextView) view.requireViewById(C1893R.C1897id.media_subtitle1), (TextView) view.requireViewById(C1893R.C1897id.media_subtitle2), (TextView) view.requireViewById(C1893R.C1897id.media_subtitle3));
        this.gutsViewHolder = new GutsViewHolder(view);
        Drawable background = transitionLayout.getBackground();
        if (background != null) {
            IlluminationDrawable illuminationDrawable = (IlluminationDrawable) background;
            for (ViewGroup registerLightSource : listOf) {
                illuminationDrawable.registerLightSource((View) registerLightSource);
            }
            illuminationDrawable.registerLightSource(this.gutsViewHolder.getCancel());
            illuminationDrawable.registerLightSource((View) this.gutsViewHolder.getDismiss());
            illuminationDrawable.registerLightSource((View) this.gutsViewHolder.getSettings());
            return;
        }
        throw new NullPointerException("null cannot be cast to non-null type com.android.systemui.media.IlluminationDrawable");
    }

    public final TransitionLayout getRecommendations() {
        return this.recommendations;
    }

    public final ImageView getCardIcon() {
        return this.cardIcon;
    }

    public final List<ImageView> getMediaCoverItems() {
        return this.mediaCoverItems;
    }

    public final List<ViewGroup> getMediaCoverContainers() {
        return this.mediaCoverContainers;
    }

    public final List<TextView> getMediaTitles() {
        return this.mediaTitles;
    }

    public final List<TextView> getMediaSubtitles() {
        return this.mediaSubtitles;
    }

    public final GutsViewHolder getGutsViewHolder() {
        return this.gutsViewHolder;
    }

    public final void marquee(boolean z, long j) {
        this.gutsViewHolder.marquee(z, j, "RecommendationViewHolder");
    }

    @Metadata(mo64986d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0007R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007¨\u0006\u000e"}, mo64987d2 = {"Lcom/android/systemui/media/RecommendationViewHolder$Companion;", "", "()V", "controlsIds", "", "", "getControlsIds", "()Ljava/util/Set;", "create", "Lcom/android/systemui/media/RecommendationViewHolder;", "inflater", "Landroid/view/LayoutInflater;", "parent", "Landroid/view/ViewGroup;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: RecommendationViewHolder.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final RecommendationViewHolder create(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            Intrinsics.checkNotNullParameter(layoutInflater, "inflater");
            Intrinsics.checkNotNullParameter(viewGroup, "parent");
            View inflate = layoutInflater.inflate(C1893R.layout.media_smartspace_recommendations, viewGroup, false);
            inflate.setLayoutDirection(3);
            Intrinsics.checkNotNullExpressionValue(inflate, "itemView");
            return new RecommendationViewHolder(inflate, (DefaultConstructorMarker) null);
        }

        public final Set<Integer> getControlsIds() {
            return RecommendationViewHolder.controlsIds;
        }
    }
}
