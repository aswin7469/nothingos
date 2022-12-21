package java.sql;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class SQLException extends Exception implements Iterable<Throwable> {
    private static final AtomicReferenceFieldUpdater<SQLException, SQLException> nextUpdater;
    private static final long serialVersionUID = 2135244094396331484L;
    private String SQLState;
    private volatile SQLException next;
    private int vendorCode;

    public SQLException(String str, String str2, int i) {
        super(str);
        this.SQLState = str2;
        this.vendorCode = i;
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            DriverManager.println("SQLState(" + str2 + ") vendor code(" + i + NavigationBarInflaterView.KEY_CODE_END);
            printStackTrace(DriverManager.getLogWriter());
        }
    }

    public SQLException(String str, String str2) {
        super(str);
        this.SQLState = str2;
        this.vendorCode = 0;
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            printStackTrace(DriverManager.getLogWriter());
            DriverManager.println("SQLException: SQLState(" + str2 + NavigationBarInflaterView.KEY_CODE_END);
        }
    }

    public SQLException(String str) {
        super(str);
        this.SQLState = null;
        this.vendorCode = 0;
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            printStackTrace(DriverManager.getLogWriter());
        }
    }

    public SQLException() {
        this.SQLState = null;
        this.vendorCode = 0;
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            printStackTrace(DriverManager.getLogWriter());
        }
    }

    public SQLException(Throwable th) {
        super(th);
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            printStackTrace(DriverManager.getLogWriter());
        }
    }

    public SQLException(String str, Throwable th) {
        super(str, th);
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            printStackTrace(DriverManager.getLogWriter());
        }
    }

    public SQLException(String str, String str2, Throwable th) {
        super(str, th);
        this.SQLState = str2;
        this.vendorCode = 0;
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            printStackTrace(DriverManager.getLogWriter());
            DriverManager.println("SQLState(" + this.SQLState + NavigationBarInflaterView.KEY_CODE_END);
        }
    }

    public SQLException(String str, String str2, int i, Throwable th) {
        super(str, th);
        this.SQLState = str2;
        this.vendorCode = i;
        if (!(this instanceof SQLWarning) && DriverManager.getLogWriter() != null) {
            DriverManager.println("SQLState(" + this.SQLState + ") vendor code(" + i + NavigationBarInflaterView.KEY_CODE_END);
            printStackTrace(DriverManager.getLogWriter());
        }
    }

    public String getSQLState() {
        return this.SQLState;
    }

    public int getErrorCode() {
        return this.vendorCode;
    }

    public SQLException getNextException() {
        return this.next;
    }

    public void setNextException(SQLException sQLException) {
        while (true) {
            SQLException sQLException2 = r2.next;
            if (sQLException2 != null) {
                r2 = sQLException2;
            } else if (!nextUpdater.compareAndSet(r2, null, sQLException)) {
                r2 = r2.next;
            } else {
                return;
            }
        }
    }

    public Iterator<Throwable> iterator() {
        return new Iterator<Throwable>() {
            Throwable cause = this.firstException.getCause();
            SQLException firstException;
            SQLException nextException;

            {
                this.firstException = SQLException.this;
                this.nextException = SQLException.this.getNextException();
            }

            public boolean hasNext() {
                return (this.firstException == null && this.nextException == null && this.cause == null) ? false : true;
            }

            public Throwable next() {
                Throwable th;
                Throwable th2 = this.firstException;
                if (th2 != null) {
                    this.firstException = null;
                    th = th2;
                } else {
                    Throwable th3 = this.cause;
                    if (th3 != null) {
                        this.cause = th3.getCause();
                        th = th3;
                    } else {
                        SQLException sQLException = this.nextException;
                        if (sQLException != null) {
                            this.cause = sQLException.getCause();
                            this.nextException = this.nextException.getNextException();
                            th = sQLException;
                        } else {
                            throw new NoSuchElementException();
                        }
                    }
                }
                return th;
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    static {
        Class<SQLException> cls = SQLException.class;
        nextUpdater = AtomicReferenceFieldUpdater.newUpdater(cls, cls, "next");
    }
}
