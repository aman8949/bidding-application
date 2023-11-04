package com.intuit.craft.service;

import com.intuit.craft.enums.Category;
import com.intuit.craft.excpetion.*;
import com.intuit.craft.model.Auction;
import com.intuit.craft.model.Product;
import com.intuit.craft.repository.AuctionRepository;
import com.intuit.craft.request.AuctionRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
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
@EnableCaching
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;

    private final ProductService productService;

    private final UserService userService;

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Autowired
    public AuctionServiceImpl(AuctionRepository auctionRepository, UserService userService, ProductService productService)
    {
        this.auctionRepository = auctionRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @Override
    @Cacheable(cacheNames = "auction", key="#auctionId", unless="#result.hasEnded")
    public Auction getAuction(final Long auctionId) throws AuctionNotFoundException {
        log.info("Received DB Calls: {}", auctionId);
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        if(auction.isEmpty())
            throw new AuctionNotFoundException("Auction with requested id does not exists");
        return auction.get();
    }

    @Override
    public Auction createAuction(final AuctionRequestDto auctionObj) throws ProductNotFoundException, EntityNotCreatedException, OperationNotAllowedException, InvalidInputException {
        Product product = productService.getProduct(auctionObj.getProductId());
        if(product.getUser().getId() != auctionObj.getUserId())
            throw new OperationNotAllowedException("Operation Forbidden for the role type");
        try {
            LocalDateTime currT = LocalDateTime.now();
            LocalDateTime startT = LocalDateTime.parse(auctionObj.getStartTime(), formatter);
            LocalDateTime endT = LocalDateTime.parse(auctionObj.getEndTime(), formatter);

            if(endT.isBefore(startT) || startT.isBefore(currT))
                throw new InvalidInputException("Invalid Date Time Provided");
            Auction auction = Auction.builder().product(product).startTime(startT).endTime(endT).currentMaxBid(product.getBasePrice()).hasEnded(false).build();
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
    public List<Auction> getAllAuctionsByCategory(final String categoryQuery, final Integer pageNumber, final Integer pageSize, final String sortBy, final String sortDir) throws AuctionNotFoundException, InvalidInputException {
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

    @Override
    @CachePut(cacheNames = "auction", key="#auction.id")
    public Auction updateAuction(Auction auction) {
        return auctionRepository.save(auction);
    }

    @Override
    @CacheEvict(value = "auction", key = "#auction.id")
    public void evictAuctionFromCache(Auction auction) {
        auction.setHasEnded(true);
        auctionRepository.save(auction);
    }
}
