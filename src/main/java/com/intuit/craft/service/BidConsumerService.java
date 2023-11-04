package com.intuit.craft.service;

import com.intuit.craft.enums.BidMessageType;
import com.intuit.craft.model.Auction;
import com.intuit.craft.model.User;
import com.intuit.craft.request.BidRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BidConsumerService {

    private final AuctionService auctionService;

    private final UserService userService;

    private final RedisTemplate<String, Auction> redisTemplate;

    @Autowired
    public BidConsumerService(AuctionService auctionService, UserService userService, RedisTemplate redisTemplate){this.auctionService = auctionService;this.userService = userService;this.redisTemplate = redisTemplate;}

    @KafkaListener(topics = "kafkaTopic", groupId = "group_id")
    public void consumeMessage(BidRequestDto bidRequestDto) {
        executeBid(bidRequestDto);
    }

    public void executeBid(final BidRequestDto bidRequestDto){
        //returned from cache if its there, else inserted in cache
        Auction auction = auctionService.getAuction(bidRequestDto.getAuctionId());

        if(BidMessageType.END_OF_BID.equals(BidMessageType.valueOf(bidRequestDto.getMessageType())))
        auctionService.evictAuctionFromCache(auction);
        else{
            if(auction.getCurrentMaxBid() < bidRequestDto.getBidValue()){
                User user = userService.getUser(bidRequestDto.getUserId());
                auction.setCurrentMaxBid(bidRequestDto.getBidValue());
                auction.setCurrentWinningUser(user);
                auctionService.updateAuction(auction);
            }
        }
    }
}