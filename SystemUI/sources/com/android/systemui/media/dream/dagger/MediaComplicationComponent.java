package com.android.systemui.media.dream.dagger;

import android.content.Context;
import android.widget.FrameLayout;
import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import com.android.systemui.media.dream.MediaViewHolder;
import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Named;
import javax.inject.Scope;

@MediaComplicationScope
@Subcomponent(modules = {MediaComplicationModule.class})
public interface MediaComplicationComponent {

    @Subcomponent.Factory
    public interface Factory {
        MediaComplicationComponent create();
    }

    @Scope
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MediaComplicationScope {
    }

    MediaViewHolder getViewHolder();

    @Module
    public interface MediaComplicationModule {
        public static final String MEDIA_COMPLICATION_CONTAINER = "media_complication_container";
        public static final String MEDIA_COMPLICATION_LAYOUT_PARAMS = "media_complication_layout_params";

        @MediaComplicationScope
        @Provides
        @Named("media_complication_container")
        static FrameLayout provideComplicationContainer(Context context) {
            return new FrameLayout(context);
        }

        @MediaComplicationScope
        @Provides
        @Named("media_complication_layout_params")
        static ComplicationLayoutParams provideLayoutParams() {
            return new ComplicationLayoutParams(0, -2, 5, 2, 0, true);
        }
    }
}
