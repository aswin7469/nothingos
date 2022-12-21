package kotlinx.coroutines.internal;

import kotlin.Metadata;
import kotlin.text.Typography;

@Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\t\b\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\u0003H\u0016J\u001e\u0010\b\u001a\u0002H\t\"\u0004\b\u0000\u0010\t2\b\u0010\n\u001a\u0004\u0018\u00010\u0001H\b¢\u0006\u0002\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\f"}, mo64987d2 = {"Lkotlinx/coroutines/internal/Symbol;", "", "symbol", "", "(Ljava/lang/String;)V", "getSymbol", "()Ljava/lang/String;", "toString", "unbox", "T", "value", "(Ljava/lang/Object;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, mo64988k = 1, mo64989mv = {1, 5, 1}, mo64991xi = 48)
/* compiled from: Symbol.kt */
public final class Symbol {
    private final String symbol;

    public final <T> T unbox(Object obj) {
        if (obj == this) {
            return null;
        }
        return obj;
    }

    public Symbol(String str) {
        this.symbol = str;
    }

    public final String getSymbol() {
        return this.symbol;
    }

    public String toString() {
        return "<" + this.symbol + Typography.greater;
    }
}
