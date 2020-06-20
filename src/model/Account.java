package model;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Objects;

public class Account implements Serializable {

    private Long id;
    private String accountName;
    private String dateOFRegistration;
    private AccountStatus status;
    private String password;

    private Developer thisDeveloper;


    public Account(String accountName, String password) {
        this.password = password;
        this.accountName = accountName;
        status = AccountStatus.ACTIVE;
        dateOFRegistration = Calendar.getInstance().getTime().toString();
    }

    public boolean comparePasswords(String password) {
        return this.password.equals(password);
    }

    public String getAccountName() {
        return accountName;
    }

    public String getDateOFRegistration() {
        return dateOFRegistration;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountName.equals(account.accountName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountName);
    }
}
