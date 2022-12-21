package com.android.systemui.statusbar.notification.stack;

import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import kotlin.Metadata;

@Metadata(mo64986d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\b\u0010\u000e\u001a\u00020\u000fH\u0016R\u001a\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u000f¢\u0006\f\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u0012\u0010\b\u001a\u00028\u0000X\u0005¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u001a\u0010\u000b\u001a\u0004\u0018\u00010\u0003X\u000f¢\u0006\f\u001a\u0004\b\f\u0010\u0005\"\u0004\b\r\u0010\u0007¨\u0006\u0010"}, mo64987d2 = {"com/android/systemui/statusbar/notification/stack/NotificationSectionsManager$decorViewHeaderState$1", "Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionUpdateState;", "currentPosition", "", "getCurrentPosition", "()Ljava/lang/Integer;", "setCurrentPosition", "(Ljava/lang/Integer;)V", "header", "getHeader", "()Lcom/android/systemui/statusbar/notification/row/StackScrollerDecorView;", "targetPosition", "getTargetPosition", "setTargetPosition", "adjustViewPosition", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NotificationSectionsManager.kt */
public final class NotificationSectionsManager$decorViewHeaderState$1 implements NotificationSectionsManager.SectionUpdateState<T> {
    private final /* synthetic */ NotificationSectionsManager.SectionUpdateState<T> $$delegate_0;
    final /* synthetic */ T $header;
    final /* synthetic */ NotificationSectionsManager.SectionUpdateState<T> $inner;

    public Integer getCurrentPosition() {
        return this.$$delegate_0.getCurrentPosition();
    }

    public T getHeader() {
        return (StackScrollerDecorView) this.$$delegate_0.getHeader();
    }

    public Integer getTargetPosition() {
        return this.$$delegate_0.getTargetPosition();
    }

    public void setCurrentPosition(Integer num) {
        this.$$delegate_0.setCurrentPosition(num);
    }

    public void setTargetPosition(Integer num) {
        this.$$delegate_0.setTargetPosition(num);
    }

    NotificationSectionsManager$decorViewHeaderState$1(NotificationSectionsManager.SectionUpdateState<? extends T> sectionUpdateState, T t) {
        this.$inner = sectionUpdateState;
        this.$header = t;
        this.$$delegate_0 = sectionUpdateState;
    }

    public void adjustViewPosition() {
        this.$inner.adjustViewPosition();
        if (getTargetPosition() != null && getCurrentPosition() == null) {
            this.$header.setContentVisible(true);
        }
    }
}
