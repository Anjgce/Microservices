package com.programming.seekho.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(
        name = "Accounts",
        description = "Schema to hold Account information"
)
@Data
public class AccountsDto {

    @Schema(
            description = "Account number of PS Bank account", example = "7654234562"
    )
    @NotEmpty(message = "Account number cannot be null or empty")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Account number must be 10 digits")
    private Long accountNumber;

    @Schema(
            description = "Account Type of PS Bank account", example = "Savings"
    )
    @NotEmpty(message = "Account type cannot be null or empty")
    private String accountType;

    @Schema(
            description = "PS Bank branch address", example = "123, New York"
    )
    @NotEmpty(message = "Branch Address cannot be null or empty")
    private String branchAddress;
}
