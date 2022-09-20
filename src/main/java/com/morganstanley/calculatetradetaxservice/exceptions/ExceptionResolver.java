package com.morganstanley.calculatetradetaxservice.exceptions;

import com.morganstanley.calculatetradetaxservice.entities.AddedTradeDetailsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.concurrent.ExecutionException;

import static com.morganstanley.calculatetradetaxservice.constants.ApplicationConstants.*;

@ControllerAdvice
@ResponseBody
public class ExceptionResolver extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionResolver.class);

    @ExceptionHandler({javax.validation.ConstraintViolationException.class,
            org.hibernate.exception.ConstraintViolationException.class})
    public final ResponseEntity<AddedTradeDetailsResponse> constraintViolation(Exception constraintException) {
        final String message = constraintException.getMessage();
        AddedTradeDetailsResponse tradeDetailsErrorResponse = AddedTradeDetailsResponse.builder().trade_id(null).status(FAILED).message(message).
                timeStamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(tradeDetailsErrorResponse , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public final ResponseEntity<AddedTradeDetailsResponse> dataIntegrityException(DataIntegrityViolationException constraintException) {
        LOGGER.error("DataIntegrityViolationException Exception Occurred => ",constraintException);
        AddedTradeDetailsResponse tradeDetailsErrorResponse = AddedTradeDetailsResponse.builder().trade_id(null).status(FAILED).message(DATA_EXCEPTION).
                timeStamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(tradeDetailsErrorResponse , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class , IOException.class , ArithmeticException.class ,
            NumberFormatException.class , IllegalStateException.class , ConcurrentModificationException.class ,
            ExecutionException.class , JpaSystemException.class , InterruptedException.class})
    public final ResponseEntity<AddedTradeDetailsResponse> genericException(Exception exception) {
        LOGGER.error("Exception Occurred => ",exception);
        final String message = (exception.getMessage() != null && !exception.getMessage().isEmpty()) ? exception.getMessage() : ISR_MSG;
        AddedTradeDetailsResponse tradeDetailsErrorResponse = AddedTradeDetailsResponse.builder().trade_id(null).status(FAILED).message(message).
                timeStamp(LocalDateTime.now()).build();
        return new ResponseEntity<>(tradeDetailsErrorResponse , HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
