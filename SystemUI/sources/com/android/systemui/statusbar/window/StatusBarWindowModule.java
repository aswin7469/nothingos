package com.android.systemui.statusbar.window;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import dagger.Module;
import dagger.Provides;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Qualifier;
import kotlin.Metadata;
import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0004\b'\u0018\u0000 \u00032\u00020\u0001:\u0002\u0003\u0004B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/statusbar/window/StatusBarWindowModule;", "", "()V", "Companion", "InternalWindowView", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@Module
/* compiled from: StatusBarWindowModule.kt */
public abstract class StatusBarWindowModule {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);

    @Qualifier
    @Metadata(mo65042d1 = {"\u0000\n\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\b\u0002\u0018\u00002\u00020\u0001B\u0000¨\u0006\u0002"}, mo65043d2 = {"Lcom/android/systemui/statusbar/window/StatusBarWindowModule$InternalWindowView;", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    @Retention(AnnotationRetention.BINARY)
    @java.lang.annotation.Retention(RetentionPolicy.CLASS)
    /* compiled from: StatusBarWindowModule.kt */
    protected @interface InternalWindowView {
    }

    @SysUISingleton
    @JvmStatic
    @Provides
    public static final StatusBarWindowView providesStatusBarWindowView(LayoutInflater layoutInflater) {
        return Companion.providesStatusBarWindowView(layoutInflater);
    }

    @Module
    @Metadata(mo65042d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007¨\u0006\u0007"}, mo65043d2 = {"Lcom/android/systemui/statusbar/window/StatusBarWindowModule$Companion;", "", "()V", "providesStatusBarWindowView", "Lcom/android/systemui/statusbar/window/StatusBarWindowView;", "layoutInflater", "Landroid/view/LayoutInflater;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: StatusBarWindowModule.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        @SysUISingleton
        @JvmStatic
        @Provides
        public final StatusBarWindowView providesStatusBarWindowView(LayoutInflater layoutInflater) {
            Intrinsics.checkNotNullParameter(layoutInflater, "layoutInflater");
            StatusBarWindowView statusBarWindowView = (StatusBarWindowView) layoutInflater.inflate(C1894R.layout.super_status_bar, (ViewGroup) null);
            if (statusBarWindowView != null) {
                return statusBarWindowView;
            }
            throw new IllegalStateException("R.layout.super_status_bar could not be properly inflated");
        }
    }
}
