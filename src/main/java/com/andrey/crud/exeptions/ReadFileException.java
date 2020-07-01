package com.andrey.crud.exeptions;

public class ReadFileException extends Exception {

    public ReadFileException() {

    }

    public ReadFileException(String massage) {
        super(massage);
    }
}
