package nl.rabobank.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 *  Represents the MongoDB entity corresponding to the 'Accounts' collection.
 *  This collection has been designed to have nested/embedded list of sub documents which hold all the authorizations for the given account
 *
 */

@Document(collection = "Accounts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDocument {

    @Id
    private String id;

    private String accountNumber;
    private AccountType accountType;
    private String accountHolderName;
    private double accountBalance;
    private List<AuthorizationDetails> authorizationDetailsList;

    public AccountDocument(String actNo, String actName, double balance, List<AuthorizationDetails> authList, AccountType actType) {
        this.accountNumber= actNo;
        this.accountHolderName = actName;
        this.accountBalance = balance;
        this.authorizationDetailsList = authList;
        this.accountType= actType;
    }
}
