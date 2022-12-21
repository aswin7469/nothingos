package com.android.systemui.media;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.Barrier;
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

@Metadata(mo64986d1 = {"\u0000r\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u0018\u0000 N2\u00020\u0001:\u0001NB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u000e\u0010C\u001a\u00020\u00062\u0006\u0010D\u001a\u00020EJ\f\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00060GJ\u0016\u0010H\u001a\u00020I2\u0006\u0010J\u001a\u00020K2\u0006\u0010L\u001a\u00020MR\u0019\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0019\u0010\n\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\tR\u0019\u0010\f\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\tR\u0019\u0010\u000e\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\tR\u0019\u0010\u0010\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\tR\u0019\u0010\u0012\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\tR\u0019\u0010\u0014\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\tR\u0019\u0010\u0016\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\tR\u0019\u0010\u0018\u001a\n \u0007*\u0004\u0018\u00010\u00190\u0019¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u0019\u0010\u001c\u001a\n \u0007*\u0004\u0018\u00010\u001d0\u001d¢\u0006\b\n\u0000\u001a\u0004\b\u001e\u0010\u001fR\u0019\u0010 \u001a\n \u0007*\u0004\u0018\u00010\u001d0\u001d¢\u0006\b\n\u0000\u001a\u0004\b!\u0010\u001fR\u0019\u0010\"\u001a\n \u0007*\u0004\u0018\u00010#0#¢\u0006\b\n\u0000\u001a\u0004\b$\u0010%R\u0011\u0010&\u001a\u00020'¢\u0006\b\n\u0000\u001a\u0004\b(\u0010)R\u0011\u0010*\u001a\u00020+¢\u0006\b\n\u0000\u001a\u0004\b,\u0010-R\u0011\u0010.\u001a\u00020#¢\u0006\b\n\u0000\u001a\u0004\b/\u0010%R\u0011\u00100\u001a\u00020#¢\u0006\b\n\u0000\u001a\u0004\b1\u0010%R\u0019\u00102\u001a\n \u0007*\u0004\u0018\u00010303¢\u0006\b\n\u0000\u001a\u0004\b4\u00105R\u0019\u00106\u001a\n \u0007*\u0004\u0018\u00010\u00030\u0003¢\u0006\b\n\u0000\u001a\u0004\b7\u00108R\u0019\u00109\u001a\n \u0007*\u0004\u0018\u00010\u001d0\u001d¢\u0006\b\n\u0000\u001a\u0004\b:\u0010\u001fR\u0019\u0010;\u001a\n \u0007*\u0004\u0018\u00010#0#¢\u0006\b\n\u0000\u001a\u0004\b<\u0010%R\u0019\u0010=\u001a\n \u0007*\u0004\u0018\u00010>0>¢\u0006\b\n\u0000\u001a\u0004\b?\u0010@R\u0019\u0010A\u001a\n \u0007*\u0004\u0018\u00010#0#¢\u0006\b\n\u0000\u001a\u0004\bB\u0010%¨\u0006O"}, mo64987d2 = {"Lcom/android/systemui/media/MediaViewHolder;", "", "itemView", "Landroid/view/View;", "(Landroid/view/View;)V", "action0", "Landroid/widget/ImageButton;", "kotlin.jvm.PlatformType", "getAction0", "()Landroid/widget/ImageButton;", "action1", "getAction1", "action2", "getAction2", "action3", "getAction3", "action4", "getAction4", "actionNext", "getActionNext", "actionPlayPause", "getActionPlayPause", "actionPrev", "getActionPrev", "actionsTopBarrier", "Landroidx/constraintlayout/widget/Barrier;", "getActionsTopBarrier", "()Landroidx/constraintlayout/widget/Barrier;", "albumView", "Landroid/widget/ImageView;", "getAlbumView", "()Landroid/widget/ImageView;", "appIcon", "getAppIcon", "artistText", "Landroid/widget/TextView;", "getArtistText", "()Landroid/widget/TextView;", "gutsViewHolder", "Lcom/android/systemui/media/GutsViewHolder;", "getGutsViewHolder", "()Lcom/android/systemui/media/GutsViewHolder;", "player", "Lcom/android/systemui/util/animation/TransitionLayout;", "getPlayer", "()Lcom/android/systemui/util/animation/TransitionLayout;", "scrubbingElapsedTimeView", "getScrubbingElapsedTimeView", "scrubbingTotalTimeView", "getScrubbingTotalTimeView", "seamless", "Landroid/view/ViewGroup;", "getSeamless", "()Landroid/view/ViewGroup;", "seamlessButton", "getSeamlessButton", "()Landroid/view/View;", "seamlessIcon", "getSeamlessIcon", "seamlessText", "getSeamlessText", "seekBar", "Landroid/widget/SeekBar;", "getSeekBar", "()Landroid/widget/SeekBar;", "titleText", "getTitleText", "getAction", "id", "", "getTransparentActionButtons", "", "marquee", "", "start", "", "delay", "", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaViewHolder.kt */
public final class MediaViewHolder {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    /* access modifiers changed from: private */
    public static final Set<Integer> controlsIds;
    /* access modifiers changed from: private */
    public static final Set<Integer> expandedBottomActionIds;
    /* access modifiers changed from: private */
    public static final Set<Integer> genericButtonIds;
    private final ImageButton action0;
    private final ImageButton action1;
    private final ImageButton action2;
    private final ImageButton action3;
    private final ImageButton action4;
    private final ImageButton actionNext;
    private final ImageButton actionPlayPause;
    private final ImageButton actionPrev;
    private final Barrier actionsTopBarrier;
    private final ImageView albumView;
    private final ImageView appIcon;
    private final TextView artistText;
    private final GutsViewHolder gutsViewHolder;
    private final TransitionLayout player;
    private final TextView scrubbingElapsedTimeView;
    private final TextView scrubbingTotalTimeView;
    private final ViewGroup seamless;
    private final View seamlessButton;
    private final ImageView seamlessIcon;
    private final TextView seamlessText;
    private final SeekBar seekBar;
    private final TextView titleText;

    @JvmStatic
    public static final MediaViewHolder create(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return Companion.create(layoutInflater, viewGroup);
    }

    public MediaViewHolder(View view) {
        Intrinsics.checkNotNullParameter(view, "itemView");
        this.player = (TransitionLayout) view;
        this.albumView = (ImageView) view.requireViewById(C1893R.C1897id.album_art);
        this.appIcon = (ImageView) view.requireViewById(C1893R.C1897id.icon);
        this.titleText = (TextView) view.requireViewById(C1893R.C1897id.header_title);
        this.artistText = (TextView) view.requireViewById(C1893R.C1897id.header_artist);
        this.seamless = (ViewGroup) view.requireViewById(C1893R.C1897id.media_seamless);
        this.seamlessIcon = (ImageView) view.requireViewById(C1893R.C1897id.media_seamless_image);
        this.seamlessText = (TextView) view.requireViewById(C1893R.C1897id.media_seamless_text);
        this.seamlessButton = view.requireViewById(C1893R.C1897id.media_seamless_button);
        this.seekBar = (SeekBar) view.requireViewById(C1893R.C1897id.media_progress_bar);
        View requireViewById = view.requireViewById(C1893R.C1897id.media_scrubbing_elapsed_time);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById…a_scrubbing_elapsed_time)");
        this.scrubbingElapsedTimeView = (TextView) requireViewById;
        View requireViewById2 = view.requireViewById(C1893R.C1897id.media_scrubbing_total_time);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "itemView.requireViewById…dia_scrubbing_total_time)");
        this.scrubbingTotalTimeView = (TextView) requireViewById2;
        this.gutsViewHolder = new GutsViewHolder(view);
        this.actionPlayPause = (ImageButton) view.requireViewById(C1893R.C1897id.actionPlayPause);
        this.actionNext = (ImageButton) view.requireViewById(C1893R.C1897id.actionNext);
        this.actionPrev = (ImageButton) view.requireViewById(C1893R.C1897id.actionPrev);
        this.action0 = (ImageButton) view.requireViewById(C1893R.C1897id.action0);
        this.action1 = (ImageButton) view.requireViewById(C1893R.C1897id.action1);
        this.action2 = (ImageButton) view.requireViewById(C1893R.C1897id.action2);
        this.action3 = (ImageButton) view.requireViewById(C1893R.C1897id.action3);
        this.action4 = (ImageButton) view.requireViewById(C1893R.C1897id.action4);
        this.actionsTopBarrier = (Barrier) view.requireViewById(C1893R.C1897id.media_action_barrier_top);
    }

    public final TransitionLayout getPlayer() {
        return this.player;
    }

    public final ImageView getAlbumView() {
        return this.albumView;
    }

    public final ImageView getAppIcon() {
        return this.appIcon;
    }

    public final TextView getTitleText() {
        return this.titleText;
    }

    public final TextView getArtistText() {
        return this.artistText;
    }

    public final ViewGroup getSeamless() {
        return this.seamless;
    }

    public final ImageView getSeamlessIcon() {
        return this.seamlessIcon;
    }

    public final TextView getSeamlessText() {
        return this.seamlessText;
    }

    public final View getSeamlessButton() {
        return this.seamlessButton;
    }

    public final SeekBar getSeekBar() {
        return this.seekBar;
    }

    public final TextView getScrubbingElapsedTimeView() {
        return this.scrubbingElapsedTimeView;
    }

    public final TextView getScrubbingTotalTimeView() {
        return this.scrubbingTotalTimeView;
    }

    public final GutsViewHolder getGutsViewHolder() {
        return this.gutsViewHolder;
    }

    public final ImageButton getActionPlayPause() {
        return this.actionPlayPause;
    }

    public final ImageButton getActionNext() {
        return this.actionNext;
    }

    public final ImageButton getActionPrev() {
        return this.actionPrev;
    }

    public final ImageButton getAction0() {
        return this.action0;
    }

    public final ImageButton getAction1() {
        return this.action1;
    }

    public final ImageButton getAction2() {
        return this.action2;
    }

    public final ImageButton getAction3() {
        return this.action3;
    }

    public final ImageButton getAction4() {
        return this.action4;
    }

    public final Barrier getActionsTopBarrier() {
        return this.actionsTopBarrier;
    }

    public final ImageButton getAction(int i) {
        switch (i) {
            case C1893R.C1897id.action0:
                ImageButton imageButton = this.action0;
                Intrinsics.checkNotNullExpressionValue(imageButton, "action0");
                return imageButton;
            case C1893R.C1897id.action1:
                ImageButton imageButton2 = this.action1;
                Intrinsics.checkNotNullExpressionValue(imageButton2, "action1");
                return imageButton2;
            case C1893R.C1897id.action2:
                ImageButton imageButton3 = this.action2;
                Intrinsics.checkNotNullExpressionValue(imageButton3, "action2");
                return imageButton3;
            case C1893R.C1897id.action3:
                ImageButton imageButton4 = this.action3;
                Intrinsics.checkNotNullExpressionValue(imageButton4, "action3");
                return imageButton4;
            case C1893R.C1897id.action4:
                ImageButton imageButton5 = this.action4;
                Intrinsics.checkNotNullExpressionValue(imageButton5, "action4");
                return imageButton5;
            case C1893R.C1897id.actionNext:
                ImageButton imageButton6 = this.actionNext;
                Intrinsics.checkNotNullExpressionValue(imageButton6, "actionNext");
                return imageButton6;
            case C1893R.C1897id.actionPlayPause:
                ImageButton imageButton7 = this.actionPlayPause;
                Intrinsics.checkNotNullExpressionValue(imageButton7, "actionPlayPause");
                return imageButton7;
            case C1893R.C1897id.actionPrev:
                ImageButton imageButton8 = this.actionPrev;
                Intrinsics.checkNotNullExpressionValue(imageButton8, "actionPrev");
                return imageButton8;
            default:
                throw new IllegalArgumentException();
        }
    }

    public final List<ImageButton> getTransparentActionButtons() {
        return CollectionsKt.listOf(this.actionNext, this.actionPrev, this.action0, this.action1, this.action2, this.action3, this.action4);
    }

    public final void marquee(boolean z, long j) {
        this.gutsViewHolder.marquee(z, j, "MediaViewHolder");
    }

    @Metadata(mo64986d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0007R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0017\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0007¨\u0006\u0012"}, mo64987d2 = {"Lcom/android/systemui/media/MediaViewHolder$Companion;", "", "()V", "controlsIds", "", "", "getControlsIds", "()Ljava/util/Set;", "expandedBottomActionIds", "getExpandedBottomActionIds", "genericButtonIds", "getGenericButtonIds", "create", "Lcom/android/systemui/media/MediaViewHolder;", "inflater", "Landroid/view/LayoutInflater;", "parent", "Landroid/view/ViewGroup;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: MediaViewHolder.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @JvmStatic
        public final MediaViewHolder create(LayoutInflater layoutInflater, ViewGroup viewGroup) {
            Intrinsics.checkNotNullParameter(layoutInflater, "inflater");
            Intrinsics.checkNotNullParameter(viewGroup, "parent");
            View inflate = layoutInflater.inflate(C1893R.layout.media_session_view, viewGroup, false);
            inflate.setLayerType(2, (Paint) null);
            inflate.setLayoutDirection(3);
            Intrinsics.checkNotNullExpressionValue(inflate, "mediaView");
            MediaViewHolder mediaViewHolder = new MediaViewHolder(inflate);
            mediaViewHolder.getSeekBar().setLayoutDirection(0);
            return mediaViewHolder;
        }

        public final Set<Integer> getControlsIds() {
            return MediaViewHolder.controlsIds;
        }

        public final Set<Integer> getGenericButtonIds() {
            return MediaViewHolder.genericButtonIds;
        }

        public final Set<Integer> getExpandedBottomActionIds() {
            return MediaViewHolder.expandedBottomActionIds;
        }
    }

    static {
        Integer valueOf = Integer.valueOf((int) C1893R.C1897id.icon);
        Integer valueOf2 = Integer.valueOf((int) C1893R.C1897id.actionNext);
        Integer valueOf3 = Integer.valueOf((int) C1893R.C1897id.actionPrev);
        Integer valueOf4 = Integer.valueOf((int) C1893R.C1897id.action0);
        Integer valueOf5 = Integer.valueOf((int) C1893R.C1897id.action1);
        Integer valueOf6 = Integer.valueOf((int) C1893R.C1897id.action2);
        Integer valueOf7 = Integer.valueOf((int) C1893R.C1897id.action3);
        Integer valueOf8 = Integer.valueOf((int) C1893R.C1897id.action4);
        Integer valueOf9 = Integer.valueOf((int) C1893R.C1897id.media_scrubbing_elapsed_time);
        Integer valueOf10 = Integer.valueOf((int) C1893R.C1897id.media_scrubbing_total_time);
        controlsIds = SetsKt.setOf(valueOf, Integer.valueOf((int) C1893R.C1897id.app_name), Integer.valueOf((int) C1893R.C1897id.header_title), Integer.valueOf((int) C1893R.C1897id.header_artist), Integer.valueOf((int) C1893R.C1897id.media_seamless), Integer.valueOf((int) C1893R.C1897id.media_progress_bar), Integer.valueOf((int) C1893R.C1897id.actionPlayPause), valueOf2, valueOf3, valueOf4, valueOf5, valueOf6, valueOf7, valueOf8, valueOf, valueOf9, valueOf10);
        genericButtonIds = SetsKt.setOf(valueOf4, valueOf5, valueOf6, valueOf7, valueOf8);
        expandedBottomActionIds = SetsKt.setOf(valueOf3, valueOf2, valueOf4, valueOf5, valueOf6, valueOf7, valueOf8, valueOf9, valueOf10);
    }
}
