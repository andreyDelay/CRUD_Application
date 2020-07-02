package com.andrey.crud.model;
import java.util.Objects;

public class Account {

    private Long id;
    private String accountName;
    private AccountStatus status;

    public Account(Long id, String accountName, AccountStatus status) {
        this.accountName = accountName;
        this.id = id;
        this.status = status;
    }

    public Account(String accountName) {
        this.accountName = accountName;
        status = AccountStatus.ACTIVE;
    }

    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        StringBuilder account = new StringBuilder();
        account.append("account id: ").append(this.getId()).append("; ")
                .append("name: ").append(this.getAccountName()).append("; ")
                .append("status: ").append(this.getStatus().toString());

        return account.toString();
    }
}
