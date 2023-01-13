package javax.sql;

import java.sql.SQLException;
import java.util.EventObject;

public class ConnectionEvent extends EventObject {
    static final long serialVersionUID = -4843217645290030002L;

    /* renamed from: ex */
    private SQLException f828ex;

    public ConnectionEvent(PooledConnection pooledConnection) {
        super(pooledConnection);
        this.f828ex = null;
    }

    public ConnectionEvent(PooledConnection pooledConnection, SQLException sQLException) {
        super(pooledConnection);
        this.f828ex = sQLException;
    }

    public SQLException getSQLException() {
        return this.f828ex;
    }
}
