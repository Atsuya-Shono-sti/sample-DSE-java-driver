package com.sios;

import java.time.Instant;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TestDataInserter {


    private final String keyspace;
    private final String table;
    private final Integer wait_sec;
    private final Integer record_num;
    private final Integer duration_sec;

    /**
     * 
     */
    public TestDataInserter(String keyspace, String table, Integer wait_sec, Integer record_num,
            Integer duration_sec) {
        this.keyspace = keyspace;
        this.table = table;
        this.wait_sec = wait_sec;
        this.record_num = record_num;
        this.duration_sec = duration_sec;
    }

    public void execute() {
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                insertTask();
            }
        };

        timer.schedule(task, 0, wait_sec * 1000);

        try {
            Thread.sleep(duration_sec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timer.cancel();
    }

    private void insertTask() {

        CassandraUtil cassandraUtil = new CassandraUtil(this.keyspace);

        for (int i = 1; i <= record_num; i++) {
            String datetime = getFormattedDatetime();
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
            // 'org.apache.cassandra.io.compress.EncryptingLZ4Compressor', 'secret_key_strength':
            // 128, 'system_key_file': 'systemkey_ashono'} ;

            String insertCql =
                    "INSERT INTO " + this.table + " (datetime, value, timestamp) VALUES (?, ?, ?)";
            cassandraUtil.insertData(insertCql, datetime, value, created);
        }

        cassandraUtil.closeSession();
    }

    // 現在の日時を"yyyy-MM-dd HH:mm:ss"形式で取得
    private static String getFormattedDatetime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
