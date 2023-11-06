package com.intuit.craft.service.BiddingServices;

import com.intuit.craft.enums.BidMessageType;
import com.intuit.craft.model.Auction;
import com.intuit.craft.model.User;
import com.intuit.craft.request.BidRequestDto;
import com.intuit.craft.service.AuctionService;
import com.intuit.craft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class BidConsumerServiceImpl implements BidConsumerService{

    private final AuctionService auctionService;

    private final UserService userService;

    @Autowired
    public BidConsumerServiceImpl(AuctionService auctionService, UserService userService){this.auctionService = auctionService;this.userService = userService;}

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
            if(auction.getCurrentWinningBid() < bidRequestDto.getBidValue() || (auction.getCurrentWinningUser() == null && auction.getCurrentWinningBid().equals(bidRequestDto.getBidValue()))){
                User user = userService.getUser(bidRequestDto.getUserId());
                auction.setCurrentWinningBid(bidRequestDto.getBidValue());
                auction.setCurrentWinningUser(user);
                auctionService.updateAuction(auction);
            }
        }
    }
}