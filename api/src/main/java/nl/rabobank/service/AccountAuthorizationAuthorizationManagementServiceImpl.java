package nl.rabobank.service;

import nl.rabobank.account.Account;
import nl.rabobank.exception.BusinessException;
import nl.rabobank.exception.InvalidInputException;
import nl.rabobank.mapper.AccountModelToDTOMapper;
import nl.rabobank.mongo.model.AccountDocument;
import nl.rabobank.mongo.model.AuthorizationDetails;
import nl.rabobank.mongo.repo.AccountAuthorizationManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 *  Represents the service layer representing the access management of accounts (either assigning or retrieving power of attorney privilege)
 */

@Service
public class AccountAuthorizationAuthorizationManagementServiceImpl implements IAccountAuthorizationManagementService {

    @Autowired
    private AccountAuthorizationManagementRepository accountAuthorizationManagementRepository;

    @Autowired
    private AccountModelToDTOMapper accountModelToDTOMapper;

    @Override
    public List<Account> getAccountsForWhichGranteeHasAuthorization(String granteeName) {
        List<AccountDocument> accountDocumentList = accountAuthorizationManagementRepository.findAccountsForWhichGranteeHasAuthorization(granteeName);
        return accountDocumentList
                .stream()
                .map(accountDocument -> accountModelToDTOMapper.mapAccountModelToDTO(accountDocument))
                .collect(Collectors.toList());
    }

    @Override
    public void createAuthorizationForSpecificAccountToGrantee(String accountNumber, String grantee, String accessType) {
        AccountDocument accountDocument = accountAuthorizationManagementRepository.findByAccountNumber(accountNumber);
        if (accountDocument == null) {
            throw new InvalidInputException("Invalid account provided");
        } else {
            createOrUpdateAuthorization(grantee, accessType, accountDocument);
        }
    }

    private void createOrUpdateAuthorization(String grantee, String accessType, AccountDocument accountDocument) {
        List<AuthorizationDetails> authorizationDetailsList = accountDocument.getAuthorizationDetailsList();
        if (!authorizationDetailsList.isEmpty()) {
            updateAuthorization(grantee, accessType, accountDocument, authorizationDetailsList);
        } else {
            createAuthorizationForAccount(accountDocument, authorizationDetailsList, grantee, accessType);
        }
    }

    private void updateAuthorization(String grantee, String accessType, AccountDocument accountDocument, List<AuthorizationDetails> authorizationDetailsList) {
        List<AuthorizationDetails> authorizationDetailsForCurrentGrantee = authorizationDetailsList
                .stream()
                .filter(authorizationDetails -> grantee.equals(authorizationDetails.getAuthGranteeName()))
                .collect(Collectors.toList());
        if (!authorizationDetailsForCurrentGrantee.isEmpty()) {
            // Current grantee may have authorization but not the requested one, throw a business exception if the requested authorization is already present
            handleWhenCurrentGranteeAlreadyHasSomeAuthorization(grantee, accessType, accountDocument, authorizationDetailsList, authorizationDetailsForCurrentGrantee);
        } else {
            createAuthorizationForAccount(accountDocument, authorizationDetailsList, grantee, accessType);
        }
    }

    private void handleWhenCurrentGranteeAlreadyHasSomeAuthorization(String grantee, String accessType, AccountDocument accountDocument, List<AuthorizationDetails> authorizationDetailsList, List<AuthorizationDetails> authorizationDetailsForCurrentGrantee) {
        for (AuthorizationDetails authorizationDetails : authorizationDetailsForCurrentGrantee) {
            if (accessType.equals(authorizationDetails.getAuthType())) {
                throw new BusinessException("Grantee already has the specified access on specified account");
            }
        }
        createAuthorizationForAccount(accountDocument, authorizationDetailsList, grantee, accessType);
    }


    private void createAuthorizationForAccount(AccountDocument accountDocument, List<AuthorizationDetails> authorizationDetailsList, String grantee, String accessType) {
        AuthorizationDetails authorizationDetails = new AuthorizationDetails();
        authorizationDetails.setAuthType(accessType);
        authorizationDetails.setAuthGranteeName(grantee);
        authorizationDetailsList.add(authorizationDetails);
        accountAuthorizationManagementRepository.save(accountDocument);
    }

}