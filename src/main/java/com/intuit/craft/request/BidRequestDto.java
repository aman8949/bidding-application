package com.intuit.craft.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BidRequestDto {
    @NotEmpty
    private Long auctionId;
    @NotEmpty
    private Double bidValue;
    @NotEmpty
    private Long userId;
    private LocalDateTime biddingTime;
}
