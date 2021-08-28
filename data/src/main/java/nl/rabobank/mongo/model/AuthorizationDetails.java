package nl.rabobank.mongo.model;

import lombok.Data;

/**
 *  Represents the embedded entity which corresponds to the authorization to an account.
 *  A list of these documents are embedded/nested within the account outer/enclosing document.
 *
 */

@Data
public class AuthorizationDetails {
    private String authType;
    private String authGranteeName;
}
