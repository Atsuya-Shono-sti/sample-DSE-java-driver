CREATE KEYSPACE test_ks WITH replication = {'class': 'NetworkTopologyStrategy', 'SIOSDC01': '3'}  AND durable_writes = true;

CREATE TABLE IF NOT EXISTS test_ks.test_insert_tb (
    datetime TEXT,
    value TEXT,
    timestamp TIMESTAMP,
    PRIMARY KEY ((datetime), value)
) WITH CLUSTERING ORDER BY (value ASC)
AND compression = {'chunk_length_kb': '64', 'cipher_algorithm': 'AES/CBC/PKCS5Padding', 'class': 'org.apache.cassandra.io.compress.EncryptingLZ4Compressor', 'secret_key_strength': 128, 'system_key_file': 'systemkey_ashono'} ;