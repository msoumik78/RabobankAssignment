package nl.rabobank;

import nl.rabobank.account.PaymentAccount;
import nl.rabobank.exception.InvalidInputException;
import nl.rabobank.mapper.AccountModelToDTOMapper;
import nl.rabobank.mongo.model.AccountDocument;
import nl.rabobank.mongo.model.AccountType;
import nl.rabobank.mongo.repo.AccountAuthorizationManagementRepository;
import nl.rabobank.service.AccountAuthorizationManagementServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AccountAuthorizationManagementServiceTest {

    @InjectMocks
    private AccountAuthorizationManagementServiceImpl accountAuthorizationAuthorizationManagementServiceFixture;

    @Mock
    private AccountAuthorizationManagementRepository accountAuthorizationManagementRepositoryMock;

    @Mock
    private AccountModelToDTOMapper accountModelToDTOMapperMock;

    private static List<AccountDocument> accountDocumentList = new ArrayList<>();


    @BeforeAll
    static void setup() {
        AccountDocument accountDocument1 = new AccountDocument();
        accountDocument1.setAccountType(AccountType.PAYMENTS);
        AccountDocument accountDocument2 = new AccountDocument();
        accountDocument2.setAccountType(AccountType.SAVINGS);

        accountDocumentList.add(accountDocument1);
        accountDocumentList.add(accountDocument2);
    }

    @Test
    void testRetrieveNoAccountsWithValidGrantee() {
        String granteeName = "TEST_GRANTEE";
        when(accountAuthorizationManagementRepositoryMock.findAccountsForWhichGranteeHasAuthorization(anyString())).thenReturn(Collections.emptyList());
        assert(accountAuthorizationAuthorizationManagementServiceFixture.getAccountsForWhichGranteeHasAuthorization(granteeName).size() == 0);
    }


    @Test
    void testRetrieve2AccountsWithValidGrantee() {

        when(accountModelToDTOMapperMock.mapAccountModelToDTO(any())).thenReturn(new PaymentAccount("","",1.0));

        when(accountAuthorizationManagementRepositoryMock.findAccountsForWhichGranteeHasAuthorization(anyString())).thenReturn(accountDocumentList);
        assert(accountAuthorizationAuthorizationManagementServiceFixture.getAccountsForWhichGranteeHasAuthorization(anyString()).size() == 2);
    }


    @Test
    void testCreateAuthorizationWhenAccountInvalid() {

        String accountNumber="NON_EXISTING_ACCOUNT";
        when(accountAuthorizationManagementRepositoryMock.findByAccountNumber(accountNumber)).thenReturn(null);
        InvalidInputException thrown = assertThrows(
                InvalidInputException.class,
                () -> accountAuthorizationAuthorizationManagementServiceFixture.createAuthorizationForSpecificAccountToGrantee(accountNumber,"",""),
                "Expected createAuthorizationForSpecificAccountToGrantee() to throw, but it didn't"
        );
        assertTrue(thrown.getMessage().contains("Invalid account provided"));

    }

}
