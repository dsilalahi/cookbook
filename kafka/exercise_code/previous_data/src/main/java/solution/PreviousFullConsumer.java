package solution;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;


public class PreviousFullConsumer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "broker101:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "mygroup");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        
        try(KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
        	consumer.subscribe(Arrays.asList("hello_world_topic"));
        	consumer.poll(0);
        	consumer.seekToBeginning(consumer.assignment());
        	
        	while(true){
        		ConsumerRecords<String, String> records = consumer.poll(100);
        		for(ConsumerRecord<String, String> record : records)
        			System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());        		
        	}
        }        
        
    }
}

