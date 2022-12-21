package com.android.systemui.util.p016io;

import com.android.systemui.dagger.SysUISingleton;
import java.nio.charset.StandardCharsets;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.p026io.BufferedWriter;
import java.p026io.IOException;
import java.util.stream.Stream;

@SysUISingleton
/* renamed from: com.android.systemui.util.io.Files */
public class Files {
    public BufferedWriter newBufferedWriter(Path path, OpenOption... openOptionArr) throws IOException {
        return java.nio.file.Files.newBufferedWriter(path, StandardCharsets.UTF_8, openOptionArr);
    }

    public Stream<String> lines(Path path) throws IOException {
        return java.nio.file.Files.lines(path);
    }

    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> cls, LinkOption... linkOptionArr) throws IOException {
        return java.nio.file.Files.readAttributes(path, cls, linkOptionArr);
    }
}
