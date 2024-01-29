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
            String insertCql = "INSERT INTO ? (datetime, value, timestamp) VALUES (?, ?, ?)";
            cassandraUtil.insertData(insertCql, this.table, datetime, value, created);
        }

        cassandraUtil.closeSession();
    }

    // 現在の日時を"yyyy-MM-dd HH:mm:ss"形式で取得
    private static String getFormattedDatetime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date());
    }
}
