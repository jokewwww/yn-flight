package com.higer.flightinfo.component;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class MyPartitioner implements Partitioner {

        @Override
        public void configure(Map<String, ?> configs) {

        }

        @Override
        public int partition(String topic, Object key, byte[] keyBytes,
                             Object value, byte[] valueBytes, Cluster cluster) {

            List<PartitionInfo> partitions = cluster.partitionsForTopic(topic);
            int numPartitions = partitions.size();

            int randomNum = new Random().nextInt(numPartitions);

            return partitions.get(randomNum).partition();

        }

        @Override
        public void close() {

        }
}
