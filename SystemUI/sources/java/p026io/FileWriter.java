package java.p026io;

import java.nio.charset.Charset;

/* renamed from: java.io.FileWriter */
public class FileWriter extends OutputStreamWriter {
    public FileWriter(String str) throws IOException {
        super(new FileOutputStream(str));
    }

    public FileWriter(String str, boolean z) throws IOException {
        super(new FileOutputStream(str, z));
    }

    public FileWriter(File file) throws IOException {
        super(new FileOutputStream(file));
    }

    public FileWriter(File file, boolean z) throws IOException {
        super(new FileOutputStream(file, z));
    }

    public FileWriter(FileDescriptor fileDescriptor) {
        super(new FileOutputStream(fileDescriptor));
    }

    public FileWriter(String str, Charset charset) throws IOException {
        super((OutputStream) new FileOutputStream(str), charset);
    }

    public FileWriter(String str, Charset charset, boolean z) throws IOException {
        super((OutputStream) new FileOutputStream(str, z), charset);
    }

    public FileWriter(File file, Charset charset) throws IOException {
        super((OutputStream) new FileOutputStream(file), charset);
    }

    public FileWriter(File file, Charset charset, boolean z) throws IOException {
        super((OutputStream) new FileOutputStream(file, z), charset);
    }
}
