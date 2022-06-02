package com.example.railwaystationapprest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ObjectAlreadyExistsException.class,
            MethodArgumentNotValidException.class,
            InvalidUpdateException.class,
            ObjectCannotBeUpdatedException.class,
            BalanceUpdateException.class,
            ObjectCannotBeDeletedException.class,
            ObjectCannotBeCreatedException.class,
            HttpMessageNotReadableException.class
            })
    public ResponseEntity handleBadRequest(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({ObjectNotFoundException.class})
    public ResponseEntity handleNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler({BadCredentialsException.class,
            MissingRequestHeaderException.class
    })
    public ResponseEntity handleForbidden(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
