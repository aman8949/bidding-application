package com.intuit.craft.controller;

import com.intuit.craft.request.BidRequestDto;
import com.intuit.craft.service.BiddingServices.GameService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("bid")
public class BiddingController {
    private final GameService gameService;

    public BiddingController(GameService gameService) {this.gameService = gameService;}

    @Operation(summary = "Play a bid for an active auction")
    @PostMapping("")
    public ResponseEntity<String> createAuction(@Valid @RequestBody BidRequestDto bidRequestDto)
    {
        gameService.processBidRequest(bidRequestDto);
        return new ResponseEntity<String>("Bid Request has been submitted!", HttpStatus.OK);
    }
}
