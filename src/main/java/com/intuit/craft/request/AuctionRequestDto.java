package com.intuit.craft.request;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AuctionRequestDto {
    @NotEmpty
    private String startTime;
    @NotEmpty
    private String endTime;
    @NotNull
    private Long productId;
    @NotNull
    private Long userId;
}
