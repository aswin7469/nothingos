package kotlin.collections;

import java.util.RandomAccess;
import kotlin.Metadata;

@Metadata(mo14007d1 = {"\u0000'\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\b*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00060\u0003j\u0002`\u0004J\u0011\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\u0002H\u0002J\u0016\u0010\f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u0006H\u0002¢\u0006\u0002\u0010\u000eJ\u0010\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0002H\u0016J\b\u0010\u0010\u001a\u00020\nH\u0016J\u0010\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u0002H\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0004¢\u0006\u0006\u001a\u0004\b\u0007\u0010\b¨\u0006\u0012"}, mo14008d2 = {"kotlin/collections/ArraysKt___ArraysJvmKt$asList$1", "Lkotlin/collections/AbstractList;", "", "Ljava/util/RandomAccess;", "Lkotlin/collections/RandomAccess;", "size", "", "getSize", "()I", "contains", "", "element", "get", "index", "(I)Ljava/lang/Byte;", "indexOf", "isEmpty", "lastIndexOf", "kotlin-stdlib"}, mo14009k = 1, mo14010mv = {1, 6, 0}, mo14012xi = 48)
/* compiled from: _ArraysJvm.kt */
public final class ArraysKt___ArraysJvmKt$asList$1 extends AbstractList<Byte> implements RandomAccess {
    final /* synthetic */ byte[] $this_asList;

    ArraysKt___ArraysJvmKt$asList$1(byte[] bArr) {
        this.$this_asList = bArr;
    }

    public final /* bridge */ boolean contains(Object obj) {
        if (!(obj instanceof Byte)) {
            return false;
        }
        return contains(((Number) obj).byteValue());
    }

    public final /* bridge */ int indexOf(Object obj) {
        if (!(obj instanceof Byte)) {
            return -1;
        }
        return indexOf(((Number) obj).byteValue());
    }

    public final /* bridge */ int lastIndexOf(Object obj) {
        if (!(obj instanceof Byte)) {
            return -1;
        }
        return lastIndexOf(((Number) obj).byteValue());
    }

    public int getSize() {
        return this.$this_asList.length;
    }

    public boolean isEmpty() {
        return this.$this_asList.length == 0;
    }

    public boolean contains(byte b) {
        return ArraysKt.contains(this.$this_asList, b);
    }

    public Byte get(int i) {
        return Byte.valueOf(this.$this_asList[i]);
    }

    public int indexOf(byte b) {
        return ArraysKt.indexOf(this.$this_asList, b);
    }

    public int lastIndexOf(byte b) {
        return ArraysKt.lastIndexOf(this.$this_asList, b);
    }
}
