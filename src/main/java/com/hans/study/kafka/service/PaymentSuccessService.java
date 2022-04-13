package com.hans.study.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hans.study.kafka.dto.OrderReq;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@RequiredArgsConstructor
public class PaymentSuccessService {

    private static final String TOPIC_FULLFILLMENT = "fullfillment";
    private static final String TOPIC_SELLER_NOTIFICATION = "seller-notification";
    private static final String TOPIC_DELIVERY = "delivery";

    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 1. 결제가 완료라는 가정에서 호출함
     * 2. 결제 완료 메시지를 작성
     * 3. 결제 이후 처리해야할 아이템들을 각 서비스에 전달
     * 4. 각 서비스는 그에 해당하는 토픽을 작성
     */
    public String success(@RequestBody final OrderReq orderReq) throws Exception {
        // 결제 완료 토픽 작성
        System.out.println("결제 완료 각 토픽 작성");

        fullfillment(orderReq);
        sellerNotification(orderReq);
        delivery(orderReq);

        return "ok";
    }

    // 배송 시스템 추적
    public void fullfillment(final OrderReq orderReq) throws JsonProcessingException {
        System.out.println("send - fullfillment");
        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println("kafkaTemplate = " + kafkaTemplate);

        kafkaTemplate.send(TOPIC_FULLFILLMENT, objectMapper.writeValueAsString(orderReq));
    }

    // 셀러알림
    public void sellerNotification(final OrderReq orderReq) throws JsonProcessingException {
        System.out.println("send - sellerNotification");
        ObjectMapper objectMapper = new ObjectMapper();
        kafkaTemplate.send(TOPIC_SELLER_NOTIFICATION, objectMapper.writeValueAsString(orderReq));
    }

    // 배송
    public void delivery(final OrderReq orderReq) throws JsonProcessingException {
        System.out.println("send - delivery");
        ObjectMapper objectMapper = new ObjectMapper();
        kafkaTemplate.send(TOPIC_DELIVERY, objectMapper.writeValueAsString(orderReq));
    }


    // 배송 시스템 추적
    @KafkaListener(topics = TOPIC_FULLFILLMENT, groupId = "group-id-oing")
    public void fullfillmentConsumer(String message) {
        System.out.println("Listener, fullfillmentConsumer > " + message);
    }

    // 셀러알림
    @KafkaListener(topics = TOPIC_SELLER_NOTIFICATION, groupId = "group-id-oing")
    public void sellerNotificationConsumer(String message) {
        System.out.println("Listener, sellerNotificationConsumer > " + message);
    }

    // 배송
    @KafkaListener(topics = TOPIC_DELIVERY, groupId = "group-id-oing")
    public void deliveryConsumer(String message) {
        System.out.println("Listener, deliveryConsumer > " + message);
    }

}
