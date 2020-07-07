package com.andrey.crud.controllers;


import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
import com.andrey.crud.model.Account;
import com.andrey.crud.model.AccountStatus;
import com.andrey.crud.repository.IO.AccountRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountController {

    private AccountRepository repository = new AccountRepository();
    private Account current;

    public Account getAccount(Long id) {
        try {
            Optional<Account> result = repository.find(id);
            result.ifPresent(account -> current = account);

            return current;
        } catch (ReadFileException e) {
            System.out.println("Ошибка при работе с базой данных." + e.getMessage());
        }
        return null;
    }

    public Account createAccount(String accountName) {
        if (!checkAccountName(accountName)) {
            System.out.println("Недопустимое имя аккаунта.");
            return null;
        }

        try {
            current = repository.save(new Account(accountName));
        } catch (ReadFileException | WriteFileException e) {
            System.out.println("Ошибка при работе с базой данных." + e.getMessage());
        }
        return current;
    }

    public<ID extends Number> Account changeAccountStatus(ID id, AccountStatus newStatus) {
        try {
            Optional<Account> result = repository.find(longParser(id));
            if (result.isPresent()) {
                current = result.get();
                if (current.getStatus().toString().equals(newStatus.toString())) {
                    System.out.println("Аккаунт уже c указанным статусом.");
                    return current;
                }

                current.setStatus(newStatus);
                current = repository.update(current.getId(),current);
                return current;
            }
        } catch (ReadFileException e) {
            System.out.println("Не удалось получить данные для аккаунта");
            return null;
        } catch (WriteFileException e) {
            System.out.println("Не удалось записать данные для аккаунта");
            return null;
        }
        return null;
    }

    public String showAccountWithRequiredStatus(AccountStatus requiredStatus) {
        try {
            Map<Long,Account> accounts = repository.findAll();
            List<Account> active = accounts.values().stream()
                                            .filter(account -> account.getStatus().toString().equals(requiredStatus.toString()))
                                            .collect(Collectors.toList());
            if (active.size() == 0)
                return "в базе нет аккаунтов с таким статусом";

            StringBuilder result = new StringBuilder("Список активных аккаунтов:\n");
            for (Account a: active) {
                result.append("id:=")
                        .append(a.getId())
                        .append(", name:=")
                        .append(a.getAccountName())
                        .append(";\n");
            }
            return result.toString();
        } catch (ReadFileException e) {
            return "Не удалось прочитать базу данных. " + e.getMessage();
        }
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
