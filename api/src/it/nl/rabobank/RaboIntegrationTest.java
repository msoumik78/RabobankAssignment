import de.flapdoodle.embed.mongo.MongodExecutable;
import nl.rabobank.RaboAssignmentApplication;
import nl.rabobank.dto.AuthorizationDTO;
import nl.rabobank.mongo.model.AccountDocument;
import nl.rabobank.mongo.model.AccountType;
import nl.rabobank.mongo.model.AuthorizationDetails;
import nl.rabobank.mongo.repo.AccountAuthorizationManagementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(classes = RaboAssignmentApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RaboIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private AccountAuthorizationManagementRepository accountAuthorizationManagementRepositoryMock;

    @MockBean
    private MongodExecutable mongodExecutable;

    @Test
    public void testIfContextLoads() {
    }


    @Test
    public void testGetEndpointWhenGranteeIsEmpty()
    {
        ResponseEntity<String> result = restTemplate.getForEntity(createURLWithPort("/account-access?granteeName="), String.class);
        assert(result.getStatusCodeValue() == 400);
    }


    @Test
    public void testGetEndpointWhenGranteeHasAuthorizationForTwoAccounts()
    {
        List<AccountDocument> accountDocumentList = prepareDataBeforeInvokingGetEndpoint();
        when(accountAuthorizationManagementRepositoryMock.findAccountsForWhichGranteeHasAuthorization("Grantee2")).thenReturn(accountDocumentList);

        ResponseEntity<List> result = restTemplate.getForEntity(createURLWithPort("/account-access?granteeName=Grantee2"), List.class);
        assert((result.getStatusCodeValue()) == 200 && (result.getBody().size() == 2));

    }

    @Test
    public void testPostEndpointWhenAccountIsEmpty()
    {
        AuthorizationDTO authorizationDTO = new AuthorizationDTO();
        authorizationDTO.setAccountNumber("");
        authorizationDTO.setGranteeName("GRANTEE");
        authorizationDTO.setAuthType("R");

        ResponseEntity<String> result = restTemplate.postForEntity(createURLWithPort("/account-access"), authorizationDTO, String.class);
        assert(result.getStatusCodeValue() == 400);
    }

    @Test
    public void testPostEndpointWhenGranteeEmpty()
    {
        AuthorizationDTO authorizationDTO = new AuthorizationDTO();
        authorizationDTO.setAccountNumber("ACCOUNT");
        authorizationDTO.setGranteeName("");
        authorizationDTO.setAuthType("R");

        ResponseEntity<String> result = restTemplate.postForEntity(createURLWithPort("/account-access"), authorizationDTO, String.class);
        assert(result.getStatusCodeValue() == 400);
    }

    @Test
    public void testPostEndpointWhenAuthorizationTypeEmpty()
    {
        AuthorizationDTO authorizationDTO = new AuthorizationDTO();
        authorizationDTO.setAccountNumber("ACCOUNT");
        authorizationDTO.setGranteeName("GRANTEE");
        authorizationDTO.setAuthType("");

        ResponseEntity<String> result = restTemplate.postForEntity(createURLWithPort("/account-access"), authorizationDTO, String.class);
        assert(result.getStatusCodeValue() == 400);
    }


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private List<AccountDocument> prepareDataBeforeInvokingGetEndpoint(){
        List<AuthorizationDetails> authList1 = new ArrayList<>();
        AuthorizationDetails authorizationDetails1 = new AuthorizationDetails();
        authorizationDetails1.setAuthGranteeName("Grantee1");
        authorizationDetails1.setAuthType("R");
        authList1.add(authorizationDetails1);

        List<AuthorizationDetails> authList2 = new ArrayList<>();
        AuthorizationDetails authorizationDetails2 = new AuthorizationDetails();
        authorizationDetails2.setAuthGranteeName("Grantee2");
        authorizationDetails2.setAuthType("W");
        authList2.add(authorizationDetails2);


        AccountDocument accountDocument1 = new AccountDocument("12345","Soumik1", 120, new ArrayList<>(), AccountType.SAVINGS);
        AccountDocument accountDocument2 = new AccountDocument("123456","Soumik2", 240, authList2, AccountType.PAYMENTS);
        List<AccountDocument> accountDocumentList = new ArrayList<>();
        accountDocumentList.add(accountDocument1);
        accountDocumentList.add(accountDocument2);
        return accountDocumentList;
    }

}
