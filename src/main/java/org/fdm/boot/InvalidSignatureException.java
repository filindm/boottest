package org.fdm.boot;


import org.springframework.security.core.AuthenticationException;


public class InvalidSignatureException extends AuthenticationException {

    public InvalidSignatureException(String msg){
        super(msg);
    }
    
}
