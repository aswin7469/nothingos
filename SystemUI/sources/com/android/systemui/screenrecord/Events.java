package com.android.systemui.screenrecord;

import android.icu.lang.UCharacter;
import com.android.internal.logging.UiEventLogger;

public class Events {

    public enum ScreenRecordEvent implements UiEventLogger.UiEventEnum {
        SCREEN_RECORD_START(UCharacter.UnicodeBlock.TAMIL_SUPPLEMENT_ID),
        SCREEN_RECORD_END_QS_TILE(300),
        SCREEN_RECORD_END_NOTIFICATION(301);
        
        private final int mId;

        private ScreenRecordEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
