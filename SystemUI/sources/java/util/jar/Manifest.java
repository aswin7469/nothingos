package java.util.jar;

import java.p026io.DataOutputStream;
import java.p026io.FilterInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Manifest implements Cloneable {
    private Attributes attr = new Attributes();
    private Map<String, Attributes> entries = new HashMap();

    private int toLower(int i) {
        return (i < 65 || i > 90) ? i : (i - 65) + 97;
    }

    public Manifest() {
    }

    public Manifest(InputStream inputStream) throws IOException {
        read(inputStream);
    }

    public Manifest(Manifest manifest) {
        this.attr.putAll(manifest.getMainAttributes());
        this.entries.putAll(manifest.getEntries());
    }

    public Attributes getMainAttributes() {
        return this.attr;
    }

    public Map<String, Attributes> getEntries() {
        return this.entries;
    }

    public Attributes getAttributes(String str) {
        return getEntries().get(str);
    }

    public void clear() {
        this.attr.clear();
        this.entries.clear();
    }

    public void write(OutputStream outputStream) throws IOException {
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        this.attr.writeMain(dataOutputStream);
        for (Map.Entry next : this.entries.entrySet()) {
            StringBuffer stringBuffer = new StringBuffer("Name: ");
            String str = (String) next.getKey();
            if (str != null) {
                byte[] bytes = str.getBytes("UTF8");
                str = new String(bytes, 0, 0, bytes.length);
            }
            stringBuffer.append(str);
            stringBuffer.append("\r\n");
            make72Safe(stringBuffer);
            dataOutputStream.writeBytes(stringBuffer.toString());
            ((Attributes) next.getValue()).write(dataOutputStream);
        }
        dataOutputStream.flush();
    }

    static void make72Safe(StringBuffer stringBuffer) {
        int length = stringBuffer.length();
        if (length > 72) {
            int i = 70;
            while (i < length - 2) {
                stringBuffer.insert(i, "\r\n ");
                i += 72;
                length += 3;
            }
        }
    }

    public void read(InputStream inputStream) throws IOException {
        FastInputStream fastInputStream = new FastInputStream(inputStream);
        byte[] bArr = new byte[512];
        this.attr.read(fastInputStream, bArr);
        int i = 0;
        int i2 = 0;
        int i3 = 2;
        boolean z = true;
        String str = null;
        byte[] bArr2 = null;
        while (true) {
            int readLine = fastInputStream.readLine(bArr);
            if (readLine != -1) {
                int i4 = readLine - 1;
                if (bArr[i4] == 10) {
                    if (i4 > 0 && bArr[i4 - 1] == 13) {
                        i4--;
                    }
                    if (i4 != 0 || !z) {
                        if (str == null) {
                            str = parseName(bArr, i4);
                            if (str == null) {
                                throw new IOException("invalid manifest format");
                            } else if (fastInputStream.peek() == 32) {
                                int i5 = i4 - 6;
                                bArr2 = new byte[i5];
                                System.arraycopy((Object) bArr, 6, (Object) bArr2, 0, i5);
                                z = false;
                            }
                        } else {
                            int length = (bArr2.length + i4) - 1;
                            byte[] bArr3 = new byte[length];
                            System.arraycopy((Object) bArr2, 0, (Object) bArr3, 0, bArr2.length);
                            System.arraycopy((Object) bArr, 1, (Object) bArr3, bArr2.length, i4 - 1);
                            if (fastInputStream.peek() == 32) {
                                z = false;
                                bArr2 = bArr3;
                            } else {
                                str = new String(bArr3, 0, length, "UTF8");
                                bArr2 = null;
                            }
                        }
                        Attributes attributes = getAttributes(str);
                        if (attributes == null) {
                            attributes = new Attributes(i3);
                            this.entries.put(str, attributes);
                        }
                        attributes.read(fastInputStream, bArr);
                        i++;
                        i2 += attributes.size();
                        i3 = Math.max(2, i2 / i);
                        z = true;
                        str = null;
                    }
                } else {
                    throw new IOException("manifest line too long");
                }
            } else {
                return;
            }
        }
    }

    private String parseName(byte[] bArr, int i) {
        if (toLower(bArr[0]) != 110 || toLower(bArr[1]) != 97 || toLower(bArr[2]) != 109 || toLower(bArr[3]) != 101 || bArr[4] != 58 || bArr[5] != 32) {
            return null;
        }
        try {
            return new String(bArr, 6, i - 6, "UTF8");
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Manifest)) {
            return false;
        }
        Manifest manifest = (Manifest) obj;
        if (!this.attr.equals(manifest.getMainAttributes()) || !this.entries.equals(manifest.getEntries())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.attr.hashCode() + this.entries.hashCode();
    }

    public Object clone() {
        return new Manifest(this);
    }

    static class FastInputStream extends FilterInputStream {
        private byte[] buf;
        private int count;
        private int pos;

        FastInputStream(InputStream inputStream) {
            this(inputStream, 8192);
        }

        FastInputStream(InputStream inputStream, int i) {
            super(inputStream);
            this.count = 0;
            this.pos = 0;
            this.buf = new byte[i];
        }

        public int read() throws IOException {
            if (this.pos >= this.count) {
                fill();
                if (this.pos >= this.count) {
                    return -1;
                }
            }
            byte[] bArr = this.buf;
            int i = this.pos;
            this.pos = i + 1;
            return Byte.toUnsignedInt(bArr[i]);
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            int i3 = this.count - this.pos;
            if (i3 <= 0) {
                if (i2 >= this.buf.length) {
                    return this.f519in.read(bArr, i, i2);
                }
                fill();
                i3 = this.count - this.pos;
                if (i3 <= 0) {
                    return -1;
                }
            }
            if (i2 > i3) {
                i2 = i3;
            }
            System.arraycopy((Object) this.buf, this.pos, (Object) bArr, i, i2);
            this.pos += i2;
            return i2;
        }

        public int readLine(byte[] bArr, int i, int i2) throws IOException {
            byte[] bArr2 = this.buf;
            int i3 = 0;
            while (i3 < i2) {
                int i4 = this.count - this.pos;
                if (i4 <= 0) {
                    fill();
                    i4 = this.count - this.pos;
                    if (i4 <= 0) {
                        return -1;
                    }
                }
                int i5 = i2 - i3;
                if (i5 <= i4) {
                    i4 = i5;
                }
                int i6 = this.pos;
                int i7 = i4 + i6;
                while (true) {
                    if (i6 >= i7) {
                        break;
                    }
                    int i8 = i6 + 1;
                    if (bArr2[i6] == 10) {
                        i6 = i8;
                        break;
                    }
                    i6 = i8;
                }
                int i9 = this.pos;
                int i10 = i6 - i9;
                System.arraycopy((Object) bArr2, i9, (Object) bArr, i, i10);
                i += i10;
                i3 += i10;
                this.pos = i6;
                if (bArr2[i6 - 1] == 10) {
                    break;
                }
            }
            return i3;
        }

        public byte peek() throws IOException {
            if (this.pos == this.count) {
                fill();
            }
            int i = this.pos;
            if (i == this.count) {
                return -1;
            }
            return this.buf[i];
        }

        public int readLine(byte[] bArr) throws IOException {
            return readLine(bArr, 0, bArr.length);
        }

        public long skip(long j) throws IOException {
            if (j <= 0) {
                return 0;
            }
            int i = this.count;
            int i2 = this.pos;
            long j2 = (long) (i - i2);
            if (j2 <= 0) {
                return this.f519in.skip(j);
            }
            if (j > j2) {
                j = j2;
            }
            this.pos = (int) (((long) i2) + j);
            return j;
        }

        public int available() throws IOException {
            return (this.count - this.pos) + this.f519in.available();
        }

        public void close() throws IOException {
            if (this.f519in != null) {
                this.f519in.close();
                this.f519in = null;
                this.buf = null;
            }
        }

        private void fill() throws IOException {
            this.pos = 0;
            this.count = 0;
            InputStream inputStream = this.f519in;
            byte[] bArr = this.buf;
            int read = inputStream.read(bArr, 0, bArr.length);
            if (read > 0) {
                this.count = read;
            }
        }
    }
}
