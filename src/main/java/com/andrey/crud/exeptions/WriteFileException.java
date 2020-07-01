package com.andrey.crud.exeptions;

public class WriteFileException extends Exception {

    public WriteFileException() {

    }

    public WriteFileException(String massage) {
        super(massage);
    }
}
