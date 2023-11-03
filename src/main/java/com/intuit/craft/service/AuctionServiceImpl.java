package com.intuit.craft.service;

import com.intuit.craft.enums.Category;
import com.intuit.craft.excpetion.*;
import com.intuit.craft.model.Auction;
import com.intuit.craft.model.Product;
import com.intuit.craft.repository.AuctionRepository;
import com.intuit.craft.request.AuctionRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;

    private final ProductService productService;

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public AuctionServiceImpl(AuctionRepository auctionRepository, ProductService productService)
    {
        this.auctionRepository = auctionRepository;
        this.productService = productService;
    }

    @Override
    public Auction getAuction(Long auctionId) throws AuctionNotFoundException {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isEmpty())
            throw new AuctionNotFoundException("Auction with requested id does not exists");
        return auction.get();
    }

    @Override
    public Auction createAuction(AuctionRequestDto auctionObj) throws ProductNotFoundException, EntityNotCreatedException, OperationNotAllowedException, InvalidInputException {
        Product product = productService.getProduct(auctionObj.getProductId());
        if(product.getUser().getId() != auctionObj.getUserId())
            throw new OperationNotAllowedException("Operation Forbidden for the role type");
        try {
            LocalDateTime currT = LocalDateTime.now();
            LocalDateTime startT = LocalDateTime.parse(auctionObj.getStartTime(), formatter);
            LocalDateTime endT = LocalDateTime.parse(auctionObj.getEndTime(), formatter);

            if(endT.isBefore(startT) || startT.isBefore(currT))
                throw new InvalidInputException("Invalid Date Time Provided");
            Auction auction = Auction.builder().product(product).startTime(startT).endTime(endT).currentMaxBid(product.getBasePrice()).build();
            return auctionRepository.saveAndFlush(auction);
        }
        catch (DateTimeParseException e){
            throw new InvalidInputException("Date Time format not valid. Please specify it in yyyy-MM-dd HH:mm:ss a");
        }
        catch (DataAccessException e){
            log.error("Error while saving into database :{}", e.getMessage());
            throw new EntityNotCreatedException("Error while creating the Auction");
        }
    }

    @Override
    public List<Auction> getAllAuctionsByCategory(String categoryQuery, Integer pageNumber, Integer pageSize, String sortBy, String sortDir) throws AuctionNotFoundException, InvalidInputException {
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("asc"))
            sort = Sort.by(sortBy).ascending();
        else
            sort = Sort.by(sortBy).descending();
        Pageable p = PageRequest.of(pageNumber, pageSize, sort);

        try{
            Category category = Category.valueOf(categoryQuery);
            Page<Auction> fetchedPage = auctionRepository.findByProductCategoryAndEndTimeAfter(category, LocalDateTime.now(), p);
            List<Auction> auctionList = fetchedPage.getContent();
            return auctionList;
        }
        catch(IllegalArgumentException e) {
            log.error("Invalid Category Provided");
            throw new InvalidInputException("Invalid Category Provided");
        }
    }
}
