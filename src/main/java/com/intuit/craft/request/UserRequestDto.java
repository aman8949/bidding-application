package com.intuit.craft.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
public class UserRequestDto implements Serializable {
    @NotEmpty
    private String firstName;
    @NotEmpty
    private String lastName;
    @NotEmpty
    private String emailId;
    @NotEmpty
    private String role;
}
