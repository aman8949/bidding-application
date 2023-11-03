package com.intuit.craft.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRequestDto {
    @NotEmpty
    private String title;
    @NotEmpty
    @Positive
    private Double basePrice;
    @NotEmpty
    private String category;
    private String description;
    @NotEmpty
    private Long userId;
}
