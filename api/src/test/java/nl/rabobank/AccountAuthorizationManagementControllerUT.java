package nl.rabobank;

import nl.rabobank.controller.AccountAuthorizationManagementController;
import nl.rabobank.dto.AuthorizationDTO;
import nl.rabobank.service.IAccountAuthorizationManagementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AccountAuthorizationManagementControllerUT {

    @InjectMocks
    private AccountAuthorizationManagementController accountAuthorizationManagementControllerFixture;

    @Mock
    private IAccountAuthorizationManagementService iAccountAuthorizationManagementServiceMock;

    @Test
    void testRetrieveAccountsWithValidGrantee() {
        String granteeName = "TEST_GRANTEE";
        when(iAccountAuthorizationManagementServiceMock.getAccountsForWhichGranteeHasAuthorization(granteeName)).thenReturn(Collections.emptyList());

        assert (accountAuthorizationManagementControllerFixture.retrieveAccountsForWhichGranteeHasAuthorization(granteeName).getStatusCodeValue() == 200);
        assert ((accountAuthorizationManagementControllerFixture.retrieveAccountsForWhichGranteeHasAuthorization(granteeName)).getBody().size() == 0);
    }


    @Test
    void testCreateAuthorizationWithValidAccountAndValidGranteeAndValidAuthType() {
        AuthorizationDTO authorizationDTO = new AuthorizationDTO();
        authorizationDTO.setAccountNumber("TEST_ACCOUNT");
        authorizationDTO.setGranteeName("TEST_GRANTEE");
        authorizationDTO.setAuthType("TEST_AUTH");


        doNothing().when(iAccountAuthorizationManagementServiceMock).createAuthorizationForSpecificAccountToGrantee(authorizationDTO.getAccountNumber(), authorizationDTO.getGranteeName(), authorizationDTO.getAuthType());
        assert (accountAuthorizationManagementControllerFixture.createAuthorizationForGranteeForSpecificAccount(authorizationDTO).getStatusCodeValue() == 200);


    }


}
