package java.util.function;

@FunctionalInterface
public interface UnaryOperator<T> extends Function<T, T> {
    static /* synthetic */ Object lambda$identity$0(Object obj) {
        return obj;
    }

    static <T> UnaryOperator<T> identity() {
        return new UnaryOperator$$ExternalSyntheticLambda0();
    }
}
