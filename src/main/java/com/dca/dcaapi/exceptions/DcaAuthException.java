package com.dca.dcaapi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class DcaAuthException extends RuntimeException{

    public DcaAuthException(String message){
        super(message);
    }
}
