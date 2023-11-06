package com.intuit.craft.service.BiddingServices;

import com.intuit.craft.request.BidRequestDto;

public interface BidConsumerService {
    void executeBid(BidRequestDto bidRequestDto);
}
