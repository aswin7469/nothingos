package com.android.systemui.dreams.touch.dagger;

import android.view.GestureDetector;
import com.android.systemui.dreams.touch.InputSession;
import com.android.systemui.shared.system.InputChannelCompat;
import dagger.BindsInstance;
import dagger.Subcomponent;
import javax.inject.Named;

@Subcomponent
public interface InputSessionComponent {

    @Subcomponent.Factory
    public interface Factory {
        InputSessionComponent create(@BindsInstance @Named("INPUT_SESSION_NAME") String str, @BindsInstance InputChannelCompat.InputEventListener inputEventListener, @BindsInstance GestureDetector.OnGestureListener onGestureListener, @BindsInstance @Named("PILFER_ON_GESTURE_CONSUME") boolean z);
    }

    InputSession getInputSession();
}
