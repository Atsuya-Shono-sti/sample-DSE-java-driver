package com.sios;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

    public static final Logger logger = LoggerFactory.getLogger(App.class);
    public static Properties properties = new Properties();

    public static void main(String[] args) {

        // コマンドラインオプションの定義
        Options options = new Options();
        options.addOption("p", "properties-file", true, "Path to properties file");
        options.addOption("c", "conf-file", true, "Path to conf file");

        // コマンドライン引数の解析
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            // オプションが指定されているか確認し、値を取得
            String propertiesFilePath = cmd.getOptionValue("properties-file");
            String confFilePath = cmd.getOptionValue("conf-file");

            // ファイルパスを表示
            if (propertiesFilePath != null) {
                logger.info("Properties File Path: " + propertiesFilePath);
                // プロパティファイルを読み込む
                try (FileInputStream fis = new FileInputStream(propertiesFilePath)) {
                    properties.load(fis);
                } catch (IOException e) {
                    logger.error("Failed to load properties file: " + propertiesFilePath);
                    logger.error(e.toString());
                    System.exit(1);
                }
            }

            if (confFilePath != null) {
                logger.info("Conf File Path: " + confFilePath);
                properties.put("confFilePath", confFilePath);
            }
        } catch (ParseException e) {
            logger.error("Error parsing command line options: " + e.getMessage());
            System.exit(1);
        }
        // 読み込んだプロパティを表示
        logger.info("Value of 'confFilePath': " + properties.getProperty("confFilePath"));
        logger.info("Value of 'keyspace': " + properties.getProperty("keyspace"));
        logger.info("Value of 'table': " + properties.getProperty("table"));
        logger.info("Value of 'wait_sec': " + properties.getProperty("wait_sec"));
        logger.info("Value of 'record_num': " + properties.getProperty("record_num"));
        logger.info("Value of 'duration_sec': " + properties.getProperty("duration_sec"));

        new TestDataInserter(properties.getProperty("keyspace"), properties.getProperty("table"),
                Integer.parseInt(properties.getProperty("wait_sec")),
                Integer.parseInt(properties.getProperty("record_num")),
                Integer.parseInt(properties.getProperty("duration_sec"))).execute();

        new Evaluator(properties.getProperty("keyspace"), properties.getProperty("table"))
                .execute();
        // プログラムの終了
        System.exit(0);
    }
}
