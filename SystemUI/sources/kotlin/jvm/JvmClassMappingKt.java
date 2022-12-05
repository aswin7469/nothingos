package kotlin.jvm;

import java.util.Objects;
import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
import org.jetbrains.annotations.NotNull;
/* compiled from: JvmClassMapping.kt */
/* loaded from: classes2.dex */
public final class JvmClassMappingKt {
    @NotNull
    public static final <T> Class<T> getJavaClass(@NotNull KClass<T> java) {
        Intrinsics.checkNotNullParameter(java, "$this$java");
        Class<T> cls = (Class<T>) ((ClassBasedDeclarationContainer) java).getJClass();
        Objects.requireNonNull(cls, "null cannot be cast to non-null type java.lang.Class<T>");
        return cls;
    }

    @NotNull
    public static final <T> Class<T> getJavaObjectType(@NotNull KClass<T> javaObjectType) {
        Intrinsics.checkNotNullParameter(javaObjectType, "$this$javaObjectType");
        Class<T> cls = (Class<T>) ((ClassBasedDeclarationContainer) javaObjectType).getJClass();
        if (!cls.isPrimitive()) {
            return cls;
        }
        String name = cls.getName();
        switch (name.hashCode()) {
            case -1325958191:
                return name.equals("double") ? Double.class : cls;
            case 104431:
                return name.equals("int") ? Integer.class : cls;
            case 3039496:
                return name.equals("byte") ? Byte.class : cls;
            case 3052374:
                return name.equals("char") ? Character.class : cls;
            case 3327612:
                return name.equals("long") ? Long.class : cls;
            case 3625364:
                return name.equals("void") ? Void.class : cls;
            case 64711720:
                return name.equals("boolean") ? Boolean.class : cls;
            case 97526364:
                return name.equals("float") ? Float.class : cls;
            case 109413500:
                return name.equals("short") ? Short.class : cls;
            default:
                return cls;
        }
    }

    @NotNull
    public static final <T> KClass<T> getKotlinClass(@NotNull Class<T> kotlin2) {
        Intrinsics.checkNotNullParameter(kotlin2, "$this$kotlin");
        return Reflection.getOrCreateKotlinClass(kotlin2);
    }
}
