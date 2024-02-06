package com.sios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.datastax.oss.driver.api.core.cql.ResultSet;

public class InsertEvaluator {

    public static final Logger logger = LoggerFactory.getLogger(InsertEvaluator.class);
    private final String keyspace;
    private final String table;

    public InsertEvaluator(String keyspace, String table) {
        this.keyspace = keyspace;
        this.table = table;
    }

    public void execute() {

        try {
            CassandraUtil cassandraUtil = new CassandraUtil(this.keyspace);

            for (String datetime : TestDataInserter.getDatetimeLog()) {
                String selectCql = "SELECT * FROM " + this.table + " WHERE datetime = '" + datetime
                        + "' LIMIT " + GetResource.properties.getProperty("record_num");
                ResultSet resultSet = cassandraUtil.selectData(selectCql);

                // ResultSetの行数が挿入したレコード未満だった場合にエラーを出力
                if (resultSet.all().size() < Integer
                        .parseInt(GetResource.properties.getProperty("record_num"))) {
                    logger.error("Error: Table has less than 10 rows for datetime" + datetime);
                    System.exit(1);
                }
            }

            cassandraUtil.closeSession();
            System.out.println("All the data is in.");
            System.out.println("Complete Evaluate Data.");
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.toString());
            System.exit(1);
        }

    }
}
