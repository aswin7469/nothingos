package java.util.zip;

import android.net.wifi.hotspot2.pps.UpdateParameter;
import java.p026io.ByteArrayInputStream;
import java.p026io.EOFException;
import java.p026io.FilterInputStream;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.SequenceInputStream;

public class GZIPInputStream extends InflaterInputStream {
    private static final int FCOMMENT = 16;
    private static final int FEXTRA = 4;
    private static final int FHCRC = 2;
    private static final int FNAME = 8;
    private static final int FTEXT = 1;
    public static final int GZIP_MAGIC = 35615;
    private boolean closed;
    protected CRC32 crc;
    protected boolean eos;
    private byte[] tmpbuf;

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }

    public GZIPInputStream(InputStream inputStream, int i) throws IOException {
        super(inputStream, new Inflater(true), i);
        this.crc = new CRC32();
        this.closed = false;
        this.tmpbuf = new byte[128];
        try {
            readHeader(inputStream);
        } catch (Exception e) {
            this.inf.end();
            throw e;
        }
    }

    public GZIPInputStream(InputStream inputStream) throws IOException {
        this(inputStream, 512);
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        ensureOpen();
        if (this.eos) {
            return -1;
        }
        int read = super.read(bArr, i, i2);
        if (read != -1) {
            this.crc.update(bArr, i, read);
        } else if (!readTrailer()) {
            return read(bArr, i, i2);
        } else {
            this.eos = true;
        }
        return read;
    }

    public void close() throws IOException {
        if (!this.closed) {
            super.close();
            this.eos = true;
            this.closed = true;
        }
    }

    private int readHeader(InputStream inputStream) throws IOException {
        CheckedInputStream checkedInputStream = new CheckedInputStream(inputStream, this.crc);
        this.crc.reset();
        if (readUShort(checkedInputStream) != 35615) {
            throw new ZipException("Not in GZIP format");
        } else if (readUByte(checkedInputStream) == 8) {
            int readUByte = readUByte(checkedInputStream);
            skipBytes(checkedInputStream, 6);
            int i = 10;
            if ((readUByte & 4) == 4) {
                int readUShort = readUShort(checkedInputStream);
                skipBytes(checkedInputStream, readUShort);
                i = 10 + readUShort + 2;
            }
            if ((readUByte & 8) == 8) {
                do {
                    i++;
                } while (readUByte(checkedInputStream) != 0);
            }
            if ((readUByte & 16) == 16) {
                do {
                    i++;
                } while (readUByte(checkedInputStream) != 0);
            }
            if ((readUByte & 2) == 2) {
                if (readUShort(checkedInputStream) == (((int) this.crc.getValue()) & 65535)) {
                    i += 2;
                } else {
                    throw new ZipException("Corrupt GZIP header");
                }
            }
            this.crc.reset();
            return i;
        } else {
            throw new ZipException("Unsupported compression method");
        }
    }

    private boolean readTrailer() throws IOException {
        InputStream inputStream = this.f519in;
        int remaining = this.inf.getRemaining();
        if (remaining > 0) {
            inputStream = new SequenceInputStream(new ByteArrayInputStream(this.buf, this.len - remaining, remaining), new FilterInputStream(inputStream) {
                public void close() throws IOException {
                }
            });
        }
        if (readUInt(inputStream) != this.crc.getValue() || readUInt(inputStream) != (this.inf.getBytesWritten() & UpdateParameter.UPDATE_CHECK_INTERVAL_NEVER)) {
            throw new ZipException("Corrupt GZIP trailer");
        } else if (this.f519in.available() <= 0 && remaining <= 26) {
            return true;
        } else {
            try {
                int readHeader = readHeader(inputStream) + 8;
                this.inf.reset();
                if (remaining <= readHeader) {
                    return false;
                }
                this.inf.setInput(this.buf, (this.len - remaining) + readHeader, remaining - readHeader);
                return false;
            } catch (IOException unused) {
                return true;
            }
        }
    }

    private long readUInt(InputStream inputStream) throws IOException {
        return (((long) readUShort(inputStream)) << 16) | ((long) readUShort(inputStream));
    }

    private int readUShort(InputStream inputStream) throws IOException {
        return (readUByte(inputStream) << 8) | readUByte(inputStream);
    }

    private int readUByte(InputStream inputStream) throws IOException {
        int read = inputStream.read();
        if (read == -1) {
            throw new EOFException();
        } else if (read >= -1 && read <= 255) {
            return read;
        } else {
            throw new IOException(this.f519in.getClass().getName() + ".read() returned value out of range -1..255: " + read);
        }
    }

    private void skipBytes(InputStream inputStream, int i) throws IOException {
        while (i > 0) {
            byte[] bArr = this.tmpbuf;
            int read = inputStream.read(bArr, 0, i < bArr.length ? i : bArr.length);
            if (read != -1) {
                i -= read;
            } else {
                throw new EOFException();
            }
        }
    }
}
