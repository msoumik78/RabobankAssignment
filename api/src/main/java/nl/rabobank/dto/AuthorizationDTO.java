package nl.rabobank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationDTO {
    @NotBlank(message ="Account Number is not provided")
    private String accountNumber;

    @NotBlank(message ="Grantee name is not provided")
    private String granteeName;

    @NotBlank(message ="Auth Type is not provided")
    private String authType;
}
