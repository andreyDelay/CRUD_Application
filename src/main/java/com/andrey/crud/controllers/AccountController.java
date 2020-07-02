package com.andrey.crud.controllers;


import com.andrey.crud.model.Account;
import com.andrey.crud.repository.IO.AccountRepository;

public class AccountController {
//
//    private Account current;
//    AccountRepository repository = new AccountRepository();
//
//    public String find(String accountName) {
//        if (!checkAccountName(accountName))
//            return "Некорректное имя аккаунта";
//
//        try {
//            if (repository.findAccount(accountName))
//                    return "Вход выполнен";
//        } catch (AddAccountException e) {
//            System.out.println("Не удалось прочитать базу данных." + e.getMessage());
//        }
//        return "Такого аккаунта не существует.";
//    }
//
//    public String saveAccount(String accountName) {
//        if (checkAccountName(accountName))
//            return "Некорректное имя аккаунта";
//
//        try {
//            repository.saveAccount(new Account(accountName));
//        } catch (AddAccountException e) {
//            System.out.println("Не озранить аккаунт в базу данных." + e.getMessage());
//        }
//        return "Аккаунт сохранён.";
//    }
//
//    private boolean checkAccountName(String name) {
//        String tmp = name.trim();
//        if (tmp.length() == 0) return false;
//        return tmp.replaceAll("[^a-zA-Zа-яА-Я0-9]", "").length() != 0;
//    }

}
