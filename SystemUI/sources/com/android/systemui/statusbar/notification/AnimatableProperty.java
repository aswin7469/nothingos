package com.android.systemui.statusbar.notification;

import android.util.FloatProperty;
import android.util.Property;
import android.view.View;
import com.android.systemui.C1893R;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class AnimatableProperty {
    public static final AnimatableProperty ABSOLUTE_X = from(new FloatProperty<View>("ViewAbsoluteX") {
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
            super.set(obj, (Float) obj2);
        }

        public void setValue(View view, float f) {
            view.setTag(C1893R.C1897id.absolute_x_current_value, Float.valueOf(f));
            View.X.set(view, Float.valueOf(f));
        }

        public Float get(View view) {
            Object tag = view.getTag(C1893R.C1897id.absolute_x_current_value);
            if (tag instanceof Float) {
                return (Float) tag;
            }
            return (Float) View.X.get(view);
        }
    }, C1893R.C1897id.absolute_x_animator_tag, C1893R.C1897id.absolute_x_animator_start_tag, C1893R.C1897id.absolute_x_animator_end_tag);
    public static final AnimatableProperty ABSOLUTE_Y = from(new FloatProperty<View>("ViewAbsoluteY") {
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
            super.set(obj, (Float) obj2);
        }

        public void setValue(View view, float f) {
            view.setTag(C1893R.C1897id.absolute_y_current_value, Float.valueOf(f));
            View.Y.set(view, Float.valueOf(f));
        }

        public Float get(View view) {
            Object tag = view.getTag(C1893R.C1897id.absolute_y_current_value);
            if (tag instanceof Float) {
                return (Float) tag;
            }
            return (Float) View.Y.get(view);
        }
    }, C1893R.C1897id.absolute_y_animator_tag, C1893R.C1897id.absolute_y_animator_start_tag, C1893R.C1897id.absolute_y_animator_end_tag);
    public static final AnimatableProperty HEIGHT = from(new FloatProperty<View>("ViewHeight") {
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
            super.set(obj, (Float) obj2);
        }

        public void setValue(View view, float f) {
            view.setTag(C1893R.C1897id.view_height_current_value, Float.valueOf(f));
            view.setBottom((int) (((float) view.getTop()) + f));
        }

        public Float get(View view) {
            Object tag = view.getTag(C1893R.C1897id.view_height_current_value);
            if (tag instanceof Float) {
                return (Float) tag;
            }
            return Float.valueOf((float) view.getHeight());
        }
    }, C1893R.C1897id.view_height_animator_tag, C1893R.C1897id.view_height_animator_start_tag, C1893R.C1897id.view_height_animator_end_tag);
    public static final AnimatableProperty SCALE_X = from(View.SCALE_X, C1893R.C1897id.scale_x_animator_tag, C1893R.C1897id.scale_x_animator_start_value_tag, C1893R.C1897id.scale_x_animator_end_value_tag);
    public static final AnimatableProperty SCALE_Y = from(View.SCALE_Y, C1893R.C1897id.scale_y_animator_tag, C1893R.C1897id.scale_y_animator_start_value_tag, C1893R.C1897id.scale_y_animator_end_value_tag);
    public static final AnimatableProperty TRANSLATION_X = from(View.TRANSLATION_X, C1893R.C1897id.x_animator_tag, C1893R.C1897id.x_animator_tag_start_value, C1893R.C1897id.x_animator_tag_end_value);
    public static final AnimatableProperty WIDTH = from(new FloatProperty<View>("ViewWidth") {
        public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
            super.set(obj, (Float) obj2);
        }

        public void setValue(View view, float f) {
            view.setTag(C1893R.C1897id.view_width_current_value, Float.valueOf(f));
            view.setRight((int) (((float) view.getLeft()) + f));
        }

        public Float get(View view) {
            Object tag = view.getTag(C1893R.C1897id.view_width_current_value);
            if (tag instanceof Float) {
                return (Float) tag;
            }
            return Float.valueOf((float) view.getWidth());
        }
    }, C1893R.C1897id.view_width_animator_tag, C1893R.C1897id.view_width_animator_start_tag, C1893R.C1897id.view_width_animator_end_tag);

    /* renamed from: X */
    public static final AnimatableProperty f375X = from(View.X, C1893R.C1897id.x_animator_tag, C1893R.C1897id.x_animator_tag_start_value, C1893R.C1897id.x_animator_tag_end_value);

    /* renamed from: Y */
    public static final AnimatableProperty f376Y = from(View.Y, C1893R.C1897id.y_animator_tag, C1893R.C1897id.y_animator_tag_start_value, C1893R.C1897id.y_animator_tag_end_value);

    public abstract int getAnimationEndTag();

    public abstract int getAnimationStartTag();

    public abstract int getAnimatorTag();

    public abstract Property getProperty();

    public static <T extends View> AnimatableProperty from(String str, final BiConsumer<T, Float> biConsumer, final Function<T, Float> function, final int i, final int i2, final int i3) {
        final C26425 r0 = new FloatProperty<T>(str) {
            public /* bridge */ /* synthetic */ void set(Object obj, Object obj2) {
                super.set(obj, (Float) obj2);
            }

            public Float get(T t) {
                return (Float) function.apply(t);
            }

            public void setValue(T t, float f) {
                biConsumer.accept(t, Float.valueOf(f));
            }
        };
        return new AnimatableProperty() {
            public int getAnimationStartTag() {
                return i2;
            }

            public int getAnimationEndTag() {
                return i3;
            }

            public int getAnimatorTag() {
                return i;
            }

            public Property getProperty() {
                return r0;
            }
        };
    }

    public static <T extends View> AnimatableProperty from(final Property<T, Float> property, final int i, final int i2, final int i3) {
        return new AnimatableProperty() {
            public int getAnimationStartTag() {
                return i2;
            }

            public int getAnimationEndTag() {
                return i3;
            }

            public int getAnimatorTag() {
                return i;
            }

            public Property getProperty() {
                return property;
            }
        };
    }
}
