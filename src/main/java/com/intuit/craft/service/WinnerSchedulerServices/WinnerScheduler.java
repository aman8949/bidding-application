package com.intuit.craft.service.WinnerSchedulerServices;

import com.intuit.craft.model.Auction;
import com.intuit.craft.service.AuctionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WinnerScheduler {
    private final AuctionService auctionService;

    private final NotificationService notificationService;

    @Autowired
    public WinnerScheduler(AuctionService auctionService, NotificationService notificationService){
        this.auctionService = auctionService;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 30*60*1000)
    public void declareWinner(){
        log.info("Cron Running");
        List<Auction> auctionList = auctionService.getAlreadyEndedAuctions();
        for(Auction auction: auctionList){
            auctionService.evictAuctionFromCache(auction);
            notificationService.sendMessage(auction.toString());
        }
    }
}
