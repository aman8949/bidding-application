package com.intuit.craft.service;

import com.intuit.craft.excpetion.*;
import com.intuit.craft.model.Auction;
import com.intuit.craft.request.AuctionRequestDto;

import java.util.List;

public interface AuctionService {
    Auction getAuction(Long auctionId) throws AuctionNotFoundException;
    Auction createAuction(AuctionRequestDto auction) throws EntityNotCreatedException, OperationNotAllowedException;

    List<Auction> getAllAuctionsByCategory(String category, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) throws AuctionNotFoundException;

    Auction updateAuction(Auction auction);

    void evictAuctionFromCache(Auction auction);

    void deleteAuction(Long auctionId) throws AuctionNotFoundException, OperationNotAllowedException;

    List<Auction> getAlreadyEndedAuctions();
}
