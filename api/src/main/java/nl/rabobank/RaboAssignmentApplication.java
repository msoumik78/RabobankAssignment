package nl.rabobank;

import nl.rabobank.mongo.model.AccountDocument;
import nl.rabobank.mongo.model.AccountType;
import nl.rabobank.mongo.model.AuthorizationDetails;
import nl.rabobank.mongo.repo.AccountAuthorizationManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import nl.rabobank.mongo.MongoConfiguration;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@Import(MongoConfiguration.class)
public class RaboAssignmentApplication implements CommandLineRunner
{

    @Autowired
    private AccountAuthorizationManagementRepository accountAuthorizationManagementRepository;

    public static void main(final String[] args)
    {
        SpringApplication.run(RaboAssignmentApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // Create Test data

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

        AuthorizationDetails authorizationDetails3 = new AuthorizationDetails();
        authorizationDetails3.setAuthGranteeName("Grantee2");
        authorizationDetails3.setAuthType("R");
        authList2.add(authorizationDetails3);

        accountAuthorizationManagementRepository.save(new AccountDocument("12345","Soumik1", 120, new ArrayList<>(), AccountType.SAVINGS));
        accountAuthorizationManagementRepository.save(new AccountDocument("123456","Soumik2", 240, authList2, AccountType.PAYMENTS));


    }

}
