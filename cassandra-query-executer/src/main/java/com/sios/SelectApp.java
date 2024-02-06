package com.sios;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectApp {

    public static final Logger logger = LoggerFactory.getLogger(SelectApp.class);

    public static void main(String[] args) {

        new GetResource(args);
        // 読み込んだプロパティを表示
        logger.info(
                "Value of 'confFilePath': " + GetResource.properties.getProperty("confFilePath"));
        logger.info("Value of 'keyspace': " + GetResource.properties.getProperty("keyspace"));
        logger.info("Value of 'table': " + GetResource.properties.getProperty("table"));
        logger.info("Value of 'wait_sec': " + GetResource.properties.getProperty("wait_sec"));
        logger.info(
                "Value of 'duration_sec': " + GetResource.properties.getProperty("duration_sec"));

        new TestDataSelector(GetResource.properties.getProperty("keyspace"),
                GetResource.properties.getProperty("table"),
                Integer.parseInt(GetResource.properties.getProperty("wait_sec")),
                Integer.parseInt(GetResource.properties.getProperty("duration_sec"))).execute();

        // プログラムの終了
        System.exit(0);
    }
}
