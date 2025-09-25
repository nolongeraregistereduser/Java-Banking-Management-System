package model.exceptions;


import java.util.NoSuchElementException;


public class CompteNotFoundException extends NoSuchElementException {
    public CompteNotFoundException(String message) {
        super(message);
    }
}


