package com.intuit.craft.service.BiddingServices;

import com.intuit.craft.request.BidRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class BidProducerService {
    private final KafkaTemplate<String, BidRequestDto> kafkaTemplate;
    private final String TOPIC = "kafkaTopic";

    @Autowired
    public BidProducerService(KafkaTemplate<String, BidRequestDto> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(BidRequestDto bidRequest) {
        this.kafkaTemplate.send(TOPIC, bidRequest);
    }
}