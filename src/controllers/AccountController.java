package controllers;

import exeptions.AddAccountException;
import model.Account;
import repositories.account.AccountsAccess;
import java.util.Optional;
import java.util.Set;

public class AccountController implements GenericController<Account, AddAccountException> {
    private Set<Account> allAccounts = null;

    @Override
    public Account getEntity(Long id) throws AddAccountException {
        allAccounts = startApplication();
        if (allAccounts != null) {
            Optional<Account> entity =
                     allAccounts.stream()
                                .filter((a) -> a.getId() == id)
                                .findFirst();
            if (entity.isPresent())
                return entity.get();
        }
        throw new AddAccountException("Аккаунта с таким именем не существует!");
    }

    @Override
    public boolean addNewEntity(Account entity) throws AddAccountException {
        allAccounts = startApplication();

        if (allAccounts.contains(entity))
            throw new AddAccountException("Аккаунт с таким именем уже существует.");

        allAccounts.add(entity);
        return true;
    }

    @Override
    public void updateEntity(Account entity) {
        allAccounts = startApplication();
        allAccounts.remove(entity);
        allAccounts.add(entity);
    }

    @Override
    public Account deleteEntity(Account entity) {
        allAccounts = startApplication();
        Account oldValue = entity;
        allAccounts.remove(entity);
        return oldValue;
    }

    @Override
    public Set<Account> startApplication() {
        if (allAccounts != null)
            return allAccounts;

        AccountsAccess access = new AccountsAccess();
        allAccounts = access.readData();
        return allAccounts;
    }

    @Override
    public void endApplication(Set<Account> data) {
        AccountsAccess access = new AccountsAccess();
        access.writeData(data);
    }
}
