package java.p026io;

import java.util.Enumeration;
import java.util.Vector;

/* renamed from: java.io.SequenceInputStream */
public class SequenceInputStream extends InputStream {

    /* renamed from: e */
    Enumeration<? extends InputStream> f532e;

    /* renamed from: in */
    InputStream f533in;

    public SequenceInputStream(Enumeration<? extends InputStream> enumeration) {
        this.f532e = enumeration;
        try {
            nextStream();
        } catch (IOException unused) {
            throw new Error("panic");
        }
    }

    public SequenceInputStream(InputStream inputStream, InputStream inputStream2) {
        Vector vector = new Vector(2);
        vector.addElement(inputStream);
        vector.addElement(inputStream2);
        this.f532e = vector.elements();
        try {
            nextStream();
        } catch (IOException unused) {
            throw new Error("panic");
        }
    }

    /* access modifiers changed from: package-private */
    public final void nextStream() throws IOException {
        InputStream inputStream = this.f533in;
        if (inputStream != null) {
            inputStream.close();
        }
        if (this.f532e.hasMoreElements()) {
            InputStream inputStream2 = (InputStream) this.f532e.nextElement();
            this.f533in = inputStream2;
            inputStream2.getClass();
            return;
        }
        this.f533in = null;
    }

    public int available() throws IOException {
        InputStream inputStream = this.f533in;
        if (inputStream == null) {
            return 0;
        }
        return inputStream.available();
    }

    public int read() throws IOException {
        while (true) {
            InputStream inputStream = this.f533in;
            if (inputStream == null) {
                return -1;
            }
            int read = inputStream.read();
            if (read != -1) {
                return read;
            }
            nextStream();
        }
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (this.f533in == null) {
            return -1;
        }
        bArr.getClass();
        if (i < 0 || i2 < 0 || i2 > bArr.length - i) {
            throw new IndexOutOfBoundsException();
        } else if (i2 == 0) {
            return 0;
        } else {
            do {
                int read = this.f533in.read(bArr, i, i2);
                if (read > 0) {
                    return read;
                }
                nextStream();
            } while (this.f533in != null);
            return -1;
        }
    }

    public void close() throws IOException {
        do {
            nextStream();
        } while (this.f533in != null);
    }
}
