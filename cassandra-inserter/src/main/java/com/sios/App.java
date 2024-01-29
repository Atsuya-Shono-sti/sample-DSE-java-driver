package com.sios;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.cli.*;

public class App {

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
                System.out.println("Properties File Path: " + propertiesFilePath);
                // プロパティファイルを読み込む
                try (FileInputStream fis = new FileInputStream(propertiesFilePath)) {
                    properties.load(fis);
                } catch (IOException e) {
                    System.err.println("Failed to load properties file: " + propertiesFilePath);
                    e.printStackTrace();
                    System.exit(1);
                }
            }

            if (confFilePath != null) {
                System.out.println("Conf File Path: " + confFilePath);
                properties.put("confFilePath", confFilePath);
            }
        } catch (ParseException e) {
            System.err.println("Error parsing command line options: " + e.getMessage());
            System.exit(1);
        }
        // 読み込んだプロパティを表示
        System.out.println("Value of 'confFilePath': " + properties.getProperty("confFilePath"));
        System.out.println("Value of 'keyspace': " + properties.getProperty("keyspace"));
        System.out.println("Value of 'table': " + properties.getProperty("table"));
        System.out.println("Value of 'wait_sec': " + properties.getProperty("wait_sec"));
        System.out.println("Value of 'record_num': " + properties.getProperty("record_num"));
        System.out.println("Value of 'duration_sec': " + properties.getProperty("duration_sec"));

        new TestDataInserter(properties.getProperty("keyspace"), properties.getProperty("table"),
                Integer.parseInt(properties.getProperty("wait_sec")),
                Integer.parseInt(properties.getProperty("record_num")),
                Integer.parseInt(properties.getProperty("duration_sec"))).execute();

        // プログラムの終了
        System.exit(0);
    }
}
