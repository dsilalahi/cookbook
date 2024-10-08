provider = "kafka-connect" {
    url = ["localhost:8083"]        
}

resource "kafka-connect_connector" "sqlite-sink" {
    name = "test-sink"
    
    config = {
        "name" = "test-sink"
        "connector.class" = "io.confluent.connect.jdbc.JdbcSinkConnector"
        "tasks.max" = "1"
        "topics" = "orders"
        "connection.url" = "jdbc:sqlite:test.db"
        "auto.create" = "true"
    }
}