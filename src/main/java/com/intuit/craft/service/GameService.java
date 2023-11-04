package com.intuit.craft.service;

import com.intuit.craft.excpetion.AuctionNotFoundException;
import com.intuit.craft.excpetion.OperationNotAllowedException;
import com.intuit.craft.request.BidRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class GameService {
    KafkaProducerService kafkaProducerService;

    AuctionService auctionService;

    @Autowired
    public GameService(KafkaProducerService kafkaProducerService, AuctionService auctionService){
        this.kafkaProducerService = kafkaProducerService;
        this.auctionService = auctionService;
    }

    public void processBidRequest(BidRequestDto bidRequestDto) throws OperationNotAllowedException, AuctionNotFoundException {
        bidRequestDto.setBiddingTime(LocalDateTime.now());
        auctionService.validateBid(bidRequestDto);
    }
}
