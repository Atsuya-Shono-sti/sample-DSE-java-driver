package com.sios;

import java.time.Instant;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDataInserter {

    public static final Logger logger = LoggerFactory.getLogger(TestDataInserter.class);
    private final static List<String> datetimeLog = new ArrayList<>();
    private final String keyspace;
    private final String table;
    private final Integer wait_sec;
    private final Integer record_num;
    private final Integer duration_sec;

    public TestDataInserter(String keyspace, String table, Integer wait_sec, Integer record_num,
            Integer duration_sec) {
        this.keyspace = keyspace;
        this.table = table;
        this.wait_sec = wait_sec;
        this.record_num = record_num;
        this.duration_sec = duration_sec;
    }

    public void execute() {
        try {
            System.out.println("Start insert test at " + Instant.now());

            Timer timer = new Timer();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    System.out.print("Inserting...      ");
                    System.out.print("\r");
                    insertTask();
                    System.out.print("Waiting...        ");
                    System.out.print("\r");
                }
            };

            timer.schedule(task, 0, this.wait_sec * 1000);
            Thread.sleep(this.duration_sec * 1000);

            timer.cancel();
        } catch (InterruptedException e) {
            logger.error(e.toString());
            System.exit(1);
        }

        System.out.println("\nComplete Inserting Data.");
    }

    private void insertTask() {

        try {
            CassandraUtil cassandraUtil = new CassandraUtil(this.keyspace);

            String datetime = getFormattedDatetime();
            datetimeLog.add(datetime); // datetimeをArrayListに追加
            logger.info("Insert tasks started at " + datetime);

            for (int i = 1; i <= record_num; i++) {
                String value = "value_" + i;
                Instant created = Instant.now();

                // データを挿入
                // 以下のテーブルを想定
                // CREATE TABLE IF NOT EXISTS test_ks.test_insert_tb (
                // datetime TEXT,
                // value TEXT,
                // timestamp TIMESTAMP,
                // PRIMARY KEY ((datetime), value)
                // ) WITH CLUSTERING ORDER BY (value ASC)
                // AND compression = {'chunk_length_kb': '64', 'cipher_algorithm':
                // 'AES/CBC/PKCS5Padding', 'class':
                // 'org.apache.cassandra.io.compress.EncryptingLZ4Compressor',
                // 'secret_key_strength':
                // 128, 'system_key_file': 'systemkey_ashono'} ;

                String insertCql = "INSERT INTO " + this.table
                        + " (datetime, value, timestamp) VALUES (?, ?, ?)";
                cassandraUtil.insertData(insertCql, datetime, value, created);
            }

            logger.info("Insert tasks complete of " + datetime + " partition");

            cassandraUtil.closeSession();
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.toString());
            System.exit(1);
        }
    }

    public static List<String> getDatetimeLog() {
        return datetimeLog;
    }

    // 現在の日時を"yyyyMMddHH"形式で取得
    private static String getFormattedDatetime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        return dateFormat.format(new Date());
    }


}
