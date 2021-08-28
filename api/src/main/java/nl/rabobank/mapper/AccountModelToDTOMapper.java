package nl.rabobank.mapper;

import nl.rabobank.account.Account;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.mongo.model.AccountDocument;
import org.springframework.stereotype.Component;

@Component
public class AccountModelToDTOMapper {
    public Account mapAccountModelToDTO(AccountDocument accountDocument) {
        Account account = null;
        switch(accountDocument.getAccountType()) {
            case SAVINGS:
                account = new SavingsAccount(accountDocument.getAccountNumber(), accountDocument.getAccountHolderName(), accountDocument.getAccountBalance());
                break;
            case PAYMENTS:
                account = new PaymentAccount(accountDocument.getAccountNumber(), accountDocument.getAccountHolderName(), accountDocument.getAccountBalance());
                break;
        }
        return account;
    }
}
