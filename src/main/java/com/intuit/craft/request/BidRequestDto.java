package com.intuit.craft.request;

import com.intuit.craft.enums.BidMessageType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BidRequestDto implements Serializable {
    @NotNull
    private Long auctionId;
    @NotNull
    private Double bidValue;
    @NotNull
    private Long userId;
    private String messageType = BidMessageType.BID.toString();
    private LocalDateTime biddingTime = LocalDateTime.now();
}
