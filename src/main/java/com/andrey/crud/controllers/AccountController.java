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

    public<L extends Number> String blockAccount(L id) {
        try {
            Optional<Account> result = repository.find(longParser(id));
            if (result.isPresent()) {
                current = result.get();
                if (current.getStatus().toString().equals("BANNED"))
                    return "Аккаунт уже был заблокирован ранее.";

                current.setStatus(AccountStatus.BANNED);
                repository.update(current.getId(),current);
            } else {
                return "Пользователя с таким id не существует.";
            }
        } catch (ReadFileException e) {
            return "Не удалось получить данные для аккаунта";
        } catch (WriteFileException e) {
            return "Не удалось сохранить изменения";
        }
        return "Аккаунт успешно заблокирован";
    }

    public String showAllActive() {
        try {
            Map<Long,Account> accounts = accounts();
            List<Account> active = accounts.values().stream()
                                            .filter(account -> account.getStatus().toString().equals("ACTIVE"))
                                            .collect(Collectors.toList());
            if (active.size() == 0)
                return "в базе нет активных аккаунтов";

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

    public String showDeleted() {
        try {
            Map<Long,Account> accounts = accounts();
            List<Account> deleted = accounts.values().stream()
                    .filter(account -> account.getStatus().toString().equals("DELETED"))
                    .collect(Collectors.toList());
            if (deleted.size() == 0)
                return "в базе нет удалённых аккаунтов";

            StringBuilder result = new StringBuilder("Список удалённых аккаунтов:\n");
            for (Account a: deleted) {
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

    public String showBlocked() {
        try {
            Map<Long,Account> accounts = accounts();
            List<Account> deleted = accounts.values().stream()
                    .filter(account -> account.getStatus().toString().equals("BANNED"))
                    .collect(Collectors.toList());
            if (deleted.size() == 0)
                return "в базе нет заблокированных аккаунтов";

            StringBuilder result = new StringBuilder("Список заблокированных аккаунтов:\n");
            for (Account a: deleted) {
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
