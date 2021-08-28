package nl.rabobank.service;

import nl.rabobank.account.Account;
import java.util.List;

public interface IAccountAuthorizationManagementService {
    List<Account> getAccountsForWhichGranteeHasAuthorization(String granteeName);
    void createAuthorizationForSpecificAccountToGrantee(String accountNumber, String grantee, String accessType);
}
