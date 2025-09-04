package com.service.product.error_handling.exception;

public class IncompleteDataException extends RuntimeException
{
    public IncompleteDataException (String message) { super(message); }
    public IncompleteDataException (Throwable cause) { super(cause); }
    public IncompleteDataException (String message, Throwable cause) { super(message, cause); }

}
