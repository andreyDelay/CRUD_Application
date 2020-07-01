package com.andrey.crud.exeptions;

public class AddAccountException extends Exception {

    public AddAccountException() {
    }

    public AddAccountException(String massage) {
        super(massage);
    }
}
