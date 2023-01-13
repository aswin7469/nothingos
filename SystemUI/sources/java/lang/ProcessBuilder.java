package java.lang;

import java.p026io.File;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ProcessBuilder {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private List<String> command;
    private File directory;
    private Map<String, String> environment;
    private boolean redirectErrorStream;
    private Redirect[] redirects;

    public ProcessBuilder(List<String> list) {
        list.getClass();
        this.command = list;
    }

    public ProcessBuilder(String... strArr) {
        this.command = new ArrayList(strArr.length);
        for (String add : strArr) {
            this.command.add(add);
        }
    }

    public ProcessBuilder command(List<String> list) {
        list.getClass();
        this.command = list;
        return this;
    }

    public ProcessBuilder command(String... strArr) {
        this.command = new ArrayList(strArr.length);
        for (String add : strArr) {
            this.command.add(add);
        }
        return this;
    }

    public List<String> command() {
        return this.command;
    }

    public Map<String, String> environment() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new RuntimePermission("getenv.*"));
        }
        if (this.environment == null) {
            this.environment = ProcessEnvironment.environment();
        }
        return this.environment;
    }

    /* access modifiers changed from: package-private */
    public ProcessBuilder environment(String[] strArr) {
        if (strArr != null) {
            this.environment = ProcessEnvironment.emptyEnvironment(strArr.length);
            for (String str : strArr) {
                if (str.indexOf(0) != -1) {
                    str = str.replaceFirst("\u0000.*", "");
                }
                int indexOf = str.indexOf(61, 0);
                if (indexOf != -1) {
                    this.environment.put(str.substring(0, indexOf), str.substring(indexOf + 1));
                }
            }
        }
        return this;
    }

    public File directory() {
        return this.directory;
    }

    public ProcessBuilder directory(File file) {
        this.directory = file;
        return this;
    }

    static class NullInputStream extends InputStream {
        static final NullInputStream INSTANCE = new NullInputStream();

        public int available() {
            return 0;
        }

        public int read() {
            return -1;
        }

        private NullInputStream() {
        }
    }

    static class NullOutputStream extends OutputStream {
        static final NullOutputStream INSTANCE = new NullOutputStream();

        private NullOutputStream() {
        }

        public void write(int i) throws IOException {
            throw new IOException("Stream closed");
        }
    }

    public static abstract class Redirect {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        public static final Redirect INHERIT = new Redirect() {
            public Type type() {
                return Type.INHERIT;
            }

            public String toString() {
                return type().toString();
            }
        };
        public static final Redirect PIPE = new Redirect() {
            public Type type() {
                return Type.PIPE;
            }

            public String toString() {
                return type().toString();
            }
        };

        public enum Type {
            PIPE,
            INHERIT,
            READ,
            WRITE,
            APPEND
        }

        public File file() {
            return null;
        }

        public abstract Type type();

        static {
            Class<ProcessBuilder> cls = ProcessBuilder.class;
        }

        /* access modifiers changed from: package-private */
        public boolean append() {
            throw new UnsupportedOperationException();
        }

        public static Redirect from(final File file) {
            file.getClass();
            return new Redirect() {
                public Type type() {
                    return Type.READ;
                }

                public File file() {
                    return File.this;
                }

                public String toString() {
                    return "redirect to read from file \"" + File.this + "\"";
                }
            };
        }

        /* renamed from: to */
        public static Redirect m1697to(final File file) {
            file.getClass();
            return new Redirect() {
                /* access modifiers changed from: package-private */
                public boolean append() {
                    return false;
                }

                public Type type() {
                    return Type.WRITE;
                }

                public File file() {
                    return File.this;
                }

                public String toString() {
                    return "redirect to write to file \"" + File.this + "\"";
                }
            };
        }

        public static Redirect appendTo(final File file) {
            file.getClass();
            return new Redirect() {
                /* access modifiers changed from: package-private */
                public boolean append() {
                    return true;
                }

                public Type type() {
                    return Type.APPEND;
                }

                public File file() {
                    return File.this;
                }

                public String toString() {
                    return "redirect to append to file \"" + File.this + "\"";
                }
            };
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Redirect)) {
                return false;
            }
            Redirect redirect = (Redirect) obj;
            if (redirect.type() != type()) {
                return false;
            }
            return file().equals(redirect.file());
        }

        public int hashCode() {
            File file = file();
            if (file == null) {
                return super.hashCode();
            }
            return file.hashCode();
        }

        private Redirect() {
        }
    }

    private Redirect[] redirects() {
        if (this.redirects == null) {
            this.redirects = new Redirect[]{Redirect.PIPE, Redirect.PIPE, Redirect.PIPE};
        }
        return this.redirects;
    }

    public ProcessBuilder redirectInput(Redirect redirect) {
        if (redirect.type() == Redirect.Type.WRITE || redirect.type() == Redirect.Type.APPEND) {
            throw new IllegalArgumentException("Redirect invalid for reading: " + redirect);
        }
        redirects()[0] = redirect;
        return this;
    }

    public ProcessBuilder redirectOutput(Redirect redirect) {
        if (redirect.type() != Redirect.Type.READ) {
            redirects()[1] = redirect;
            return this;
        }
        throw new IllegalArgumentException("Redirect invalid for writing: " + redirect);
    }

    public ProcessBuilder redirectError(Redirect redirect) {
        if (redirect.type() != Redirect.Type.READ) {
            redirects()[2] = redirect;
            return this;
        }
        throw new IllegalArgumentException("Redirect invalid for writing: " + redirect);
    }

    public ProcessBuilder redirectInput(File file) {
        return redirectInput(Redirect.from(file));
    }

    public ProcessBuilder redirectOutput(File file) {
        return redirectOutput(Redirect.m1697to(file));
    }

    public ProcessBuilder redirectError(File file) {
        return redirectError(Redirect.m1697to(file));
    }

    public Redirect redirectInput() {
        Redirect[] redirectArr = this.redirects;
        return redirectArr == null ? Redirect.PIPE : redirectArr[0];
    }

    public Redirect redirectOutput() {
        Redirect[] redirectArr = this.redirects;
        return redirectArr == null ? Redirect.PIPE : redirectArr[1];
    }

    public Redirect redirectError() {
        Redirect[] redirectArr = this.redirects;
        return redirectArr == null ? Redirect.PIPE : redirectArr[2];
    }

    public ProcessBuilder inheritIO() {
        Arrays.fill((Object[]) redirects(), (Object) Redirect.INHERIT);
        return this;
    }

    public boolean redirectErrorStream() {
        return this.redirectErrorStream;
    }

    public ProcessBuilder redirectErrorStream(boolean z) {
        this.redirectErrorStream = z;
        return this;
    }

    public Process start() throws IOException {
        List<String> list = this.command;
        String[] strArr = (String[]) ((String[]) list.toArray(new String[list.size()])).clone();
        for (String str : strArr) {
            str.getClass();
        }
        String str2 = strArr[0];
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkExec(str2);
        }
        File file = this.directory;
        String file2 = file == null ? null : file.toString();
        int i = 1;
        while (i < strArr.length) {
            if (strArr[i].indexOf(0) < 0) {
                i++;
            } else {
                throw new IOException("invalid null character in command");
            }
        }
        try {
            return ProcessImpl.start(strArr, this.environment, file2, this.redirects, this.redirectErrorStream);
        } catch (IOException | IllegalArgumentException e) {
            e = e;
            String str3 = ": " + e.getMessage();
            String str4 = "";
            if ((e instanceof IOException) && securityManager != null) {
                try {
                    securityManager.checkRead(str2);
                } catch (SecurityException e2) {
                    e = e2;
                    str3 = str4;
                }
            }
            StringBuilder sb = new StringBuilder("Cannot run program \"");
            sb.append(str2);
            sb.append("\"");
            if (file2 != null) {
                str4 = " (in directory \"" + file2 + "\")";
            }
            sb.append(str4);
            sb.append(str3);
            throw new IOException(sb.toString(), e);
        }
    }
}
