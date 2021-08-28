package nl.rabobank.mongo.model;

import lombok.Data;

@Data
public class AuthorizationDetails {
    private String authType;
    private String authGranteeName;
}
