package com.intuit.craft.controller;

import com.intuit.craft.model.Auction;
import com.intuit.craft.request.AuctionRequestDto;
import com.intuit.craft.service.AuctionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("auction")
public class AuctionController {
    private final AuctionService auctionService;
    AuctionController(AuctionService auctionService)
    {
        this.auctionService = auctionService;
    }

    @Operation(summary = "Get Auction by AuctionId", description = "AuctionId should be valid")
    @GetMapping("/{id}")
    public ResponseEntity<Auction> auctionDetails(@PathVariable("id") Long auctionId)
    {
        // fetch details from user table and return
        return new ResponseEntity<>(auctionService.getAuction(auctionId), HttpStatus.OK);
    }

    @Operation(summary = "Create new auction")
    @PostMapping("/create")
    public ResponseEntity<Auction> createAuction(@RequestBody AuctionRequestDto auctionRequestDto)
    {
        return new ResponseEntity<>(auctionService.createAuction(auctionRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Get all auctions for a category")
    @GetMapping("/category")
    public ResponseEntity<List<Auction>> getAllAuctionsForProductCategory(@RequestParam(name = "category") String category,
                                                                          @RequestParam(name = "pageNumber", defaultValue = "0", required = false) Integer pageNumber,
                                                                          @RequestParam(name = "pageSize", defaultValue = "5", required = false) Integer pageSize,
                                                                          @RequestParam(name = "sortBy", defaultValue = "startTime", required = false) String sortBy,
                                                                          @RequestParam(name = "sortDir", defaultValue = "asc", required = false) String sortDir)
    {
        return new ResponseEntity<>(auctionService.getAllAuctionsByCategory(category, pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
    }
}
