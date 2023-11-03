package com.intuit.craft.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Builder
public class UserRequestDto {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String emailId;
    @NotEmpty
    private String role;
}
