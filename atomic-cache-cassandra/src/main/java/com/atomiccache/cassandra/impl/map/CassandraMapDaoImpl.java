package com.atomiccache.cassandra.impl.map;

import com.atomiccache.cassandra.impl.map.stmt.MapDeleteStatement;
import com.atomiccache.cassandra.impl.map.stmt.MapPutStatement;
import com.atomiccache.cassandra.impl.map.stmt.MapSelectStatement;
import com.datastax.oss.driver.api.core.CqlSession;

import java.time.Duration;
import java.util.Optional;

public class CassandraMapDaoImpl {

    private final MapDeleteStatement deleteStatement;
    private final MapSelectStatement selectStatement;
    private final MapPutStatement    putStatement;

    public CassandraMapDaoImpl(CqlSession aSession, String aTableName) {
        deleteStatement = new MapDeleteStatement(aSession, aTableName);
        selectStatement = new MapSelectStatement(aSession, aTableName);
        putStatement    = new MapPutStatement(aSession, aTableName);
    }

    public void removeKey(String key) {
        deleteStatement.removeKey(key);
    }

    public Optional<String> select(String key) {
        return selectStatement.select(key);
    }

    public void put(String key, String value, Duration aTtl) {
        putStatement.put(key, value, aTtl);
    }

}
