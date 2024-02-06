package com.sios;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestDataSelector {
    public static final Logger logger = LoggerFactory.getLogger(TestDataSelector.class);
    private final String keyspace;
    private final String table;
    private final Integer wait_sec;
    private final Integer duration_sec;

    public TestDataSelector(String keyspace, String table, Integer wait_sec, Integer duration_sec) {
        this.keyspace = keyspace;
        this.table = table;
        this.wait_sec = wait_sec;
        this.duration_sec = duration_sec;
    }

    public void execute() {
        try {
            System.out.println("Start select test at " + Instant.now());

            Timer timer = new Timer();

            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    System.out.print("Reading...      ");
                    System.out.print("\r");
                    selectTask();
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

        System.out.println("\nComplete Reading Data.");
    }

    private void selectTask() {

        try {
            CassandraUtil cassandraUtil = new CassandraUtil(this.keyspace);

            String datetime = getFormattedDatetime();
            logger.info("Select tasks started at " + datetime);

            // データを読み込み
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

            String selectCql =
                    "SELECT * FROM " + this.table + " WHERE datetime = '" + datetime + "'";
            cassandraUtil.selectData(selectCql);

            logger.info("Select tasks complete of " + datetime + " partition");

            cassandraUtil.closeSession();
        } catch (Exception e) {
            // TODO: handle exception
            logger.error(e.toString());
            System.exit(1);
        }
    }

    // 現在の日時を"yyyyMMddHH"形式で取得
    private static String getFormattedDatetime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHH");
        return dateFormat.format(new Date());
    }
}
