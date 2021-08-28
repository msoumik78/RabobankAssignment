package nl.rabobank.controller;

import nl.rabobank.account.Account;
import nl.rabobank.dto.AuthorizationDTO;
import nl.rabobank.service.IAccountAuthorizationManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Represents the REST controller representing the access management of accounts (either assigning or retrieving power of attorney privilege)
 */

@Validated
@RestController
public class AccountAuthorizationManagementController {

    @Autowired
    private IAccountAuthorizationManagementService iAccountAuthorizationManagementService;


    /**
     * This is the controller method corresponding to the GET endpoint : /account-access
     *
     * @param granteeName   name of the grantee who has power of attorney to other's accounts
     * @return Possible outcomes :
     *        HTTP 400
     *        1) If the grantee name is null or blank, this endpoint returns HTTP 400
     *
     *        HTTP 200
     *        1) If the grantee name is non blank, this endpoint returns HTTP 200 and a list of accounts to which grantee has access (read or write).
     *        If the grantee does not have POA access to any account, it still returns HTTP 200 but an empty list.
     */
    @GetMapping("/account-access")
    public ResponseEntity<List<Account>> retrieveAccountsForWhichGranteeHasAuthorization(@NotBlank(message=" Grantee name is not provided")  @RequestParam("granteeName") String granteeName) {
        return ResponseEntity.ok().body(iAccountAuthorizationManagementService.getAccountsForWhichGranteeHasAuthorization(granteeName));
    }



    /**
     * This is the controller method corresponding to the POST endpoint : /account-access
     *
     * @param authorizationDTO   This object contains 3 attributes -
     *                           -- account number to which authorization needs to be provided
     *                           -- grantee name to whom access has to be provided
     *                           -- authorization type which states whether read or write access to be provided (should be wither R or W)
     * @return Possible outcomes :
     *      HTTP 400
     *        1) If the account number provided is null or blank, this endpoint returns HTTP 400
     *        2) If the grantee name provided is null or blank, this endpoint returns HTTP 400     *
     *        3) If the authorization type provided is null or blank, this endpoint returns HTTP 400     *
     *     *  3) If the account number, to which authorization is being requested, does not exist , this endpoint returns HTTP 400     *
     *
     *     HTTP 500
     *         1) If grantee has already the same authorisation to the same account which he is requesting now, this endpoint returns HTTP 500
     *
     *      HTTP 200     *
     *        1) If account number is valid and the authorization does not exist, then it creates the authorization for the grantee and returns HTTP 200
     */
    @PostMapping("/account-access")
    public ResponseEntity<String> createAuthorizationForGranteeForSpecificAccount(@Valid @RequestBody AuthorizationDTO authorizationDTO) {
        iAccountAuthorizationManagementService.createAuthorizationForSpecificAccountToGrantee(authorizationDTO.getAccountNumber(), authorizationDTO.getGranteeName(), authorizationDTO.getAuthType());
        return ResponseEntity.ok().body("OK");
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return errors;
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));
        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

}
