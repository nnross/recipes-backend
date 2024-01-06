package com.example.recipe.account;

/**
 * Class to update account.
 */
@SuppressWarnings("unused")
public class UpdateAccount {
    private Account account;
    private String confirmation;

    public UpdateAccount(Account account, String confirmation) {
        this.account = account;
        this.confirmation = confirmation;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }
}
