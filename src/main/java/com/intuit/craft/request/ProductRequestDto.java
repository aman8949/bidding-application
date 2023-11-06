package com.intuit.craft.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ProductRequestDto implements Serializable {
    @NotEmpty
    private String title;
    @NotNull
    @Positive
    private Double basePrice;
    @NotEmpty
    private String category;
    private String description;
    @NotNull
    private Long userId;
}
