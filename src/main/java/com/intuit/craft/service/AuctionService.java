package com.intuit.craft.service;

import com.intuit.craft.excpetion.*;
import com.intuit.craft.model.Auction;
import com.intuit.craft.model.Product;
import com.intuit.craft.request.AuctionRequestDto;
import com.intuit.craft.request.ProductRequestDto;

import java.util.List;

public interface AuctionService {
    Auction getAuction(Long auctionId) throws AuctionNotFoundException;
    Auction createAuction(AuctionRequestDto auction) throws EntityNotCreatedException, OperationNotAllowedException;

    List<Auction> getAllAuctionsByCategory(String category) throws AuctionNotFoundException;
}
