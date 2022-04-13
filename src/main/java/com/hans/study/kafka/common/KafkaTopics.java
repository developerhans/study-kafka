package com.hans.study.kafka.common;

public enum KafkaTopics {
    FULL_FILLMENT("full-fillment"),
    SELLER_NOTIFICATION("seller-notification"),
    ERROR("delivery");

    private final String topic;

    KafkaTopics(String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
