package com.sios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsertApp {

    public static final Logger logger = LoggerFactory.getLogger(InsertApp.class);

    public static void main(String[] args) {

        new GetResource(args);

        // 読み込んだプロパティを表示
        logger.info(
                "Value of 'confFilePath': " + GetResource.properties.getProperty("confFilePath"));
        logger.info("Value of 'keyspace': " + GetResource.properties.getProperty("keyspace"));
        logger.info("Value of 'table': " + GetResource.properties.getProperty("table"));
        logger.info("Value of 'wait_sec': " + GetResource.properties.getProperty("wait_sec"));
        logger.info("Value of 'record_num': " + GetResource.properties.getProperty("record_num"));
        logger.info(
                "Value of 'duration_sec': " + GetResource.properties.getProperty("duration_sec"));

        new TestDataInserter(GetResource.properties.getProperty("keyspace"),
                GetResource.properties.getProperty("table"),
                Integer.parseInt(GetResource.properties.getProperty("wait_sec")),
                Integer.parseInt(GetResource.properties.getProperty("record_num")),
                Integer.parseInt(GetResource.properties.getProperty("duration_sec"))).execute();

        // new InsertEvaluator(properties.getProperty("keyspace"), properties.getProperty("table"))
        // .execute();
        // プログラムの終了
        System.exit(0);
    }
}
