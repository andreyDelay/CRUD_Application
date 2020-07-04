package com.andrey.crud.controllers;


import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
import com.andrey.crud.model.Account;
import com.andrey.crud.model.AccountStatus;
import com.andrey.crud.repository.IO.AccountRepository;

import java.util.Map;
import java.util.Optional;

public class AccountController {

    private AccountRepository repository = new AccountRepository();
    private Account current;

    public String showAccount(String id) {
        return "Такого аккаунта не существует.";
    }

    public String createAccount(String accountName) {
        if (!checkAccountName(accountName))
            return "некорректное имя аккаунта";

        try {
            repository.save(new Account(accountName));
        } catch (ReadFileException | WriteFileException e) {
            return "Ошибка при работе с базой данных." + e.getMessage();
        }
        return "Аккаунт сохранён.";
    }

    public<ID extends Number> String recoverAccount(ID id) {
        try {
            Optional<Account> result = repository.find(longParser(id));
            if (result.isPresent()) {
                current = result.get();
                if (current.getStatus().toString().equals("ACTIVE"))
                    return "Данный аккаунт уже активный";

                current.setStatus(AccountStatus.ACTIVE);
                repository.update(current.getId(),current);
            }
        } catch (ReadFileException e) {
            return "Не удалось получить данные для аккаунта";
        } catch (WriteFileException e) {
            return "Не удалось сохранить изменения";
        }
        return "Аккаунт восстановлен";
    }

    public<L extends Number> String deleteAccount(L id) {
        try {
            Optional<Account> result = repository.find(longParser(id));
            if (result.isPresent()) {
                current = result.get();
                if (current.getStatus().toString().equals("DELETED"))
                    return "Аккаунт уже был удалён ранее.";

                current.setStatus(AccountStatus.DELETED);
                repository.update(current.getId(),current);
            } else {
                return "Пользователя с таким id не существует.";
            }
        } catch (ReadFileException e) {
            return "Не удалось получить данные для аккаунта";
        } catch (WriteFileException e) {
            return "Не удалось сохранить изменения";
        }
        return "Аккаунт успешно удалён";
    }

    public Map<Long, Account> accounts() throws ReadFileException {
        return repository.findAll();
    }

    private boolean checkAccountName(String name) {
        String tmp = name.trim();
        if (tmp.length() == 0) return false;
        return tmp.replaceAll("[^a-zA-Zа-яА-Я0-9]", "").length() != 0;
    }

    private<L extends Number> Long longParser(L number) {
        return number.longValue();
    }

}
