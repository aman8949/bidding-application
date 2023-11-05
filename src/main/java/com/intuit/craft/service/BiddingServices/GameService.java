package com.intuit.craft.service.BiddingServices;

import com.intuit.craft.enums.BidMessageType;
import com.intuit.craft.enums.Role;
import com.intuit.craft.excpetion.AuctionNotFoundException;
import com.intuit.craft.excpetion.OperationNotAllowedException;
import com.intuit.craft.excpetion.UserNotFoundException;
import com.intuit.craft.model.Auction;
import com.intuit.craft.request.BidRequestDto;
import com.intuit.craft.service.AuctionService;
import com.intuit.craft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    BidProducerService bidProducerService;

    AuctionService auctionService;

    UserService userService;

    @Autowired
    public GameService(BidProducerService bidProducerService, AuctionService auctionService, UserService userService){
        this.bidProducerService = bidProducerService;
        this.auctionService = auctionService;
        this.userService = userService;
    }

    public boolean isLastMessage(final BidRequestDto bidRequestDto) throws UserNotFoundException, OperationNotAllowedException {
        return userService.isRoleType(bidRequestDto, Role.SYSTEM) && bidRequestDto.getMessageType().equals(BidMessageType.END_OF_BID.toString());
    }

    public void processBidRequest(final BidRequestDto bidRequestDto) throws OperationNotAllowedException, AuctionNotFoundException {
        if(!isLastMessage(bidRequestDto)){
            Auction auction = auctionService.getAuction(bidRequestDto.getAuctionId());
            if(auction.isHasEnded())
                throw new OperationNotAllowedException("Auction has already ended");
            if(!userService.isRoleType(bidRequestDto, Role.BIDDER))
                throw new OperationNotAllowedException("Role Bidder is expected to make bids only.");

            if(bidRequestDto.getBiddingTime().isBefore(auction.getStartTime()) || bidRequestDto.getBiddingTime().isAfter(auction.getEndTime()))
                throw new OperationNotAllowedException("Auction is not active at this time.");

            if(bidRequestDto.getBidValue() < auction.getProduct().getBasePrice())
                throw new OperationNotAllowedException("Bidding for a price lower than base price is not expected");
        }
        bidProducerService.sendMessage(bidRequestDto);
    }
}
