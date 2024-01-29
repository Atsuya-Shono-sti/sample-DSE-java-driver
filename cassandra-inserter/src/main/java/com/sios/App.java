package com.sios;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar CassandraInserter.jar <path-to-properties-file>");
            System.exit(1);
        }

        String propertiesFilePath = args[0];

        // プロパティファイルを読み込む
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(propertiesFilePath)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Failed to load properties file: " + propertiesFilePath);
            e.printStackTrace();
            System.exit(1);
        }

        // 読み込んだプロパティを表示（例）
        System.out.println("Value of 'keyspace': " + properties.getProperty("keyspace"));
        System.out.println("Value of 'table': " + properties.getProperty("table"));
        System.out.println("Value of 'wait_sec': " + properties.getProperty("wait_sec"));
        System.out.println("Value of 'record_num': " + properties.getProperty("record_num"));
        System.out.println("Value of 'duration_sec': " + properties.getProperty("duration_sec"));

        // プログラムの終了
        System.exit(0);
    }
}
