package com.android.systemui.statusbar.notification.stack;

import android.view.View;
import com.android.systemui.statusbar.notification.stack.NotificationSectionsManager;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\r\n\u0002\u0010\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\b\u0010\u0010\u001a\u00020\u0011H\u0016R\u001e\u0010\u0002\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u0010\n\u0002\u0010\b\u001a\u0004\b\u0004\u0010\u0005\"\u0004\b\u0006\u0010\u0007R\u0016\u0010\t\u001a\u00028\u0000X\u0004¢\u0006\n\n\u0002\u0010\f\u001a\u0004\b\n\u0010\u000bR\u001e\u0010\r\u001a\u0004\u0018\u00010\u0003X\u000e¢\u0006\u0010\n\u0002\u0010\b\u001a\u0004\b\u000e\u0010\u0005\"\u0004\b\u000f\u0010\u0007¨\u0006\u0012"}, mo65043d2 = {"com/android/systemui/statusbar/notification/stack/NotificationSectionsManager$expandableViewHeaderState$1", "Lcom/android/systemui/statusbar/notification/stack/NotificationSectionsManager$SectionUpdateState;", "currentPosition", "", "getCurrentPosition", "()Ljava/lang/Integer;", "setCurrentPosition", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "header", "getHeader", "()Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "Lcom/android/systemui/statusbar/notification/row/ExpandableView;", "targetPosition", "getTargetPosition", "setTargetPosition", "adjustViewPosition", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: NotificationSectionsManager.kt */
public final class NotificationSectionsManager$expandableViewHeaderState$1 implements NotificationSectionsManager.SectionUpdateState<T> {
    final /* synthetic */ T $header;
    private Integer currentPosition;
    private final T header;
    private Integer targetPosition;
    final /* synthetic */ NotificationSectionsManager this$0;

    NotificationSectionsManager$expandableViewHeaderState$1(T t, NotificationSectionsManager notificationSectionsManager) {
        this.$header = t;
        this.this$0 = notificationSectionsManager;
        this.header = t;
    }

    public T getHeader() {
        return this.header;
    }

    public Integer getCurrentPosition() {
        return this.currentPosition;
    }

    public void setCurrentPosition(Integer num) {
        this.currentPosition = num;
    }

    public Integer getTargetPosition() {
        return this.targetPosition;
    }

    public void setTargetPosition(Integer num) {
        this.targetPosition = num;
    }

    public void adjustViewPosition() {
        this.this$0.notifPipelineFlags.checkLegacyPipelineEnabled();
        Integer targetPosition2 = getTargetPosition();
        Integer currentPosition2 = getCurrentPosition();
        NotificationStackScrollLayout notificationStackScrollLayout = null;
        if (targetPosition2 == null) {
            if (currentPosition2 != null) {
                NotificationStackScrollLayout access$getParent$p = this.this$0.parent;
                if (access$getParent$p == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("parent");
                } else {
                    notificationStackScrollLayout = access$getParent$p;
                }
                notificationStackScrollLayout.removeView((View) this.$header);
            }
        } else if (currentPosition2 == null) {
            this.$header.removeFromTransientContainer();
            NotificationStackScrollLayout access$getParent$p2 = this.this$0.parent;
            if (access$getParent$p2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
            } else {
                notificationStackScrollLayout = access$getParent$p2;
            }
            notificationStackScrollLayout.addView((View) this.$header, targetPosition2.intValue());
        } else {
            NotificationStackScrollLayout access$getParent$p3 = this.this$0.parent;
            if (access$getParent$p3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("parent");
            } else {
                notificationStackScrollLayout = access$getParent$p3;
            }
            notificationStackScrollLayout.changeViewPosition(this.$header, targetPosition2.intValue());
        }
    }
}
