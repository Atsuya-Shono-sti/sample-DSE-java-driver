# sample-DSE-java-driver

## 機能

- 一定期間、一定間隔で指定した件数のインサートを実行する
  - インサート内容は以下
    ```
    INSERT INTO test_ks.test_tb (datetime, value, timestamp) VALUES (yyyyMMddHH, value_i, now());
    ```
- 一定期間、一定間隔でセレクトを実行する
  - セレクト内容は以下
    ```
    SELECT * FROM test_ks.test_tb WHERE datetime = 'yyyyMMddHH'
    ```

## ビルド

```
cd cassandra-query-executer
mvn install
```

target/CassandraQueryExecuter.jar が生成される．

## 準備

- Cassandra にキースペース、テーブル作成する．

  ```
  # Cassandraノードにて
  sudo cqlsh --ssl -u cassandra

  # Cqlsh起動
  ## キースペース作成
  CREATE KEYSPACE test_ks WITH replication = {'class': 'NetworkTopologyStrategy', 'SIOSDC01': '3'}  AND durable_writes = true;

  ## テーブル作成
  CREATE TABLE IF NOT EXISTS test_ks.test_tb (
      datetime TEXT,
      value TEXT,
      timestamp TIMESTAMP,
      PRIMARY KEY ((datetime), value)
  ) WITH CLUSTERING ORDER BY (value ASC)
  AND compression = {'chunk_length_kb': '64', 'cipher_algorithm': 'AES/CBC/PKCS5Padding', 'class': 'org.apache.cassandra.io.compress.EncryptingLZ4Compressor', 'secret_key_strength': 128, 'system_key_file': 'systemkey_ashono'} ;
  ```

以下を編集する．

- application.conf

  ```
  # 以下の接続情報を全て編集
  basic.contact-points = [ "xxx.xxx.xxx.xxx:9142", "xxx.xxx.xxx.xxx:9142" ]
  local-datacenter = dcname
  username = cassandra
  password = cassandra
  truststore-path = /path/to/client.truststore
  truststore-password = password
  keystore-path = /path/to/client.keystore
  keystore-password = password
  ```

- testScenario.properties
  ```
  # 以下の接続情報を全て編集
  keyspace=test_ks # キースペース名
  table=test_tb テーブル名
  wait_sec=5 # クエリ実行の感覚(秒)
  record_num=10 # インサートクエリの際1サイクルで挿入する件数
  duration_sec=60 # クエリ実行期間(秒)
  ```
  以上例だと、
  test_ks.test_tb に 60 秒間 5 秒感覚でセレクト、もしくは 10 件のインサートを実行する．

## 実行

application.conf,testScenario.properties のパスをオプションで指定して実行する．

```
# インサートテスト
java -cp cassandra-query-executer/target/CassandraQueryExecuter.jar com.sios.InsertApp -p testScenario.properties -c application.conf

# セレクトテスト
java -cp cassandra-query-executer/target/CassandraQueryExecuter.jar com.sios.SelectApp -p testScenario.properties -c application.conf
```
