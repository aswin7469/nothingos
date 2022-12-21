package libcore.content.type;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/* compiled from: D8$$SyntheticClass */
public final /* synthetic */ class MimeMap$$ExternalSyntheticLambda0 implements Supplier {
    public final Object get() {
        return MimeMap.builder().addMimeMapping("application/pdf", "pdf").addMimeMapping("image/jpeg", "jpg").addMimeMapping("image/x-ms-bmp", "bmp").addMimeMapping("text/html", (List<String>) Arrays.asList("htm", "html")).addMimeMapping("text/plain", (List<String>) Arrays.asList("text", "txt")).addMimeMapping("text/x-java", "java").build();
    }
}
