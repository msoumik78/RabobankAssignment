package nl.rabobank.mongo.repo;

import nl.rabobank.mongo.model.AccountDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountAuthorizationManagementRepository extends MongoRepository<AccountDocument, String> {

    @Query("{ 'authorizationDetailsList.authGranteeName': ?0 }")
    List<AccountDocument> findAccountsForWhichGranteeHasAuthorization(String granteeName);

    AccountDocument findByAccountNumber(String accountNumber);

}
