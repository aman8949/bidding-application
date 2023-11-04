package com.intuit.craft.request;

import com.intuit.craft.enums.BidMessageType;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BidRequestDto implements Serializable {
    @NotEmpty
    private Long auctionId;
    @NotEmpty
    private Double bidValue;
    @NotEmpty
    private Long userId;
    private String messageType = BidMessageType.BID.toString();
    private LocalDateTime biddingTime = LocalDateTime.now();
}
