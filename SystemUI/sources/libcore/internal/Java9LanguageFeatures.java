package libcore.internal;

import java.p026io.ByteArrayInputStream;
import java.p026io.ByteArrayOutputStream;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Java9LanguageFeatures {

    public interface Person {
        String name();

        boolean isPalindrome() {
            return name().equals(reverse(name()));
        }

        boolean isPalindromeIgnoreCase() {
            return name().equalsIgnoreCase(reverse(name()));
        }

        private String reverse(String str) {
            return new StringBuilder(str).reverse().toString();
        }
    }

    @SafeVarargs
    public static <T> String toListString(T... tArr) {
        return toString(tArr).toString();
    }

    @SafeVarargs
    private static <T> List<String> toString(T... tArr) {
        ArrayList arrayList = new ArrayList();
        for (T obj : tArr) {
            arrayList.add(obj.toString());
        }
        return arrayList;
    }

    public <T> AtomicReference<T> createReference(T t) {
        return new AtomicReference<T>(t) {
        };
    }

    public static byte[] copy(byte[] bArr) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr);
        while (true) {
            try {
                int read = byteArrayInputStream.read();
                if (read != -1) {
                    byteArrayOutputStream.write(read);
                } else {
                    byteArrayInputStream.close();
                    return byteArrayOutputStream.toByteArray();
                }
            } catch (Throwable th) {
                th.addSuppressed(th);
            }
        }
        throw th;
    }
}
