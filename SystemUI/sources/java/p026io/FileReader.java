package java.p026io;

import java.nio.charset.Charset;

/* renamed from: java.io.FileReader */
public class FileReader extends InputStreamReader {
    public FileReader(String str) throws FileNotFoundException {
        super(new FileInputStream(str));
    }

    public FileReader(File file) throws FileNotFoundException {
        super(new FileInputStream(file));
    }

    public FileReader(FileDescriptor fileDescriptor) {
        super(new FileInputStream(fileDescriptor));
    }

    public FileReader(String str, Charset charset) throws IOException {
        super((InputStream) new FileInputStream(str), charset);
    }

    public FileReader(File file, Charset charset) throws IOException {
        super((InputStream) new FileInputStream(file), charset);
    }
}
