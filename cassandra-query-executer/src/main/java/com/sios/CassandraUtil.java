package com.sios;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.cql.*;

public class CassandraUtil {

    public static final Logger logger = LoggerFactory.getLogger(CassandraUtil.class);
    private final CqlSession cqlSession;
    File file = new File(GetResource.properties.getProperty("confFilePath"));

    public CassandraUtil(String keyspace) {
        // Cassandraの接続情報を設定
        try {
            this.cqlSession =
                    CqlSession.builder().withConfigLoader(DriverConfigLoader.fromFile(file))
                            .withKeyspace(keyspace).build();
        } catch (Exception e) {
            // 例外が発生した場合、エラーメッセージを表示して終了
            throw new RuntimeException("Failed to initialize CassandraUtil", e);
        }
    }

    public void insertData(String insertCql, Object... bindValues) {
        try {
            PreparedStatement preparedStatement = cqlSession.prepare(insertCql);
            cqlSession.execute(preparedStatement.bind(bindValues));
        } catch (Exception e) {
            throw new RuntimeException("Failed to insert data into Cassandra", e);
        }
    }

    // データを選択
    public ResultSet selectData(String selectCql) {
        try {
            SimpleStatement statement = SimpleStatement.newInstance(selectCql);
            return cqlSession.execute(statement);
        } catch (Exception e) {
            throw new RuntimeException("Failed to execute SELECT query in Cassandra", e);
        }
    }

    // セッションをクローズ
    public void closeSession() {
        cqlSession.close();
    }
}
