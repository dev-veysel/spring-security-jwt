package com.hacker.exception;

import com.hacker.exception.message.ApiResponseError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.*;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.security.core.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.*;
import org.springframework.web.servlet.mvc.method.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;


@ControllerAdvice
public class HackersSecurityExceptionHandler extends ResponseEntityExceptionHandler {
    //@ControllerAdvice + ResponseEntityExceptionHandler geben 100% Zugriff
    // auf die Zentralisierung des Handler!

    //FactoryDesignPattern!! ( die class die behandelt werden soll )
    Logger logger = LoggerFactory.getLogger(HackersSecurityExceptionHandler.class);

    private ResponseEntity<Object> buildResponseEntity(ApiResponseError error) {
        logger.error(error.getMessage());                                        // <-------- hier kommt der Logger rein, dadurch wird es überall aufgerufen!
        return new ResponseEntity<>(error,error.getStatus());
    }

    // ResourceNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException( //Object, da es jede Wiedergabe handle soll (String oder was anderes)
            ResourceNotFoundException ex, WebRequest request) { //WebRequest zeigt, von welchem Request es gekommen ist

        //Schablone, wie Error angezeigt werden soll!!
        ApiResponseError error = new ApiResponseError(  HttpStatus.NOT_FOUND,
                                                        ex.getMessage(),
                                                        request.getDescription(false)); //unnütze Information nicht anzeigen!
        return buildResponseEntity(error); // statt es mehrmals zu schreiben method extracting!
    }

    // ConflictException
    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(
            ConflictException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(  HttpStatus.CONFLICT,
                                                        ex.getMessage(),
                                                        request.getDescription(false));
        return buildResponseEntity(error);
    }

    // AccessDeniedException
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(  HttpStatus.FORBIDDEN,
                                                        ex.getMessage(),
                                                        request.getDescription(false));
        return buildResponseEntity(error);
    }

    // AuthenticationException
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(  HttpStatus.BAD_REQUEST,
                                                        ex.getMessage(),
                                                        request.getDescription(false));
        return buildResponseEntity(error);
    }

    // AuthenticationException
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(
            BadRequestException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(  HttpStatus.BAD_REQUEST,
                                                        ex.getMessage(),
                                                        request.getDescription(false));
        return buildResponseEntity(error);
    }


    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ApiResponseError error = new ApiResponseError(  HttpStatus.BAD_REQUEST,
                                                        ex.getMessage(),
                                                        request.getDescription(false))  ;
        return buildResponseEntity(error);
    }




    // handle schreiben und eine lange Liste voller handle Möglichkeiten zeigt sich, welche möglicherweise erscheinen können!
    // Alle werden als @Override angezeigt!


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        //List, da wir die Länge nicht wissen!
        // getBindingResult + getFieldErrors, wenn ich alle Error einfangen will!
        List<String> errors = ex.getBindingResult().getFieldErrors().
                                                        stream(). // dem User soll nur die Message erreichen und keine weiteren Informationen
                                                        map(e->e.getDefaultMessage()).
                                                        collect(Collectors.toList());

        ApiResponseError error = new ApiResponseError(  HttpStatus.BAD_REQUEST,
                                                        errors.get(0).toString(),
                                                        request.getDescription(false));
        return buildResponseEntity(error);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(  HttpStatus.BAD_REQUEST,
                                                        ex.getMessage(),
                                                        request.getDescription(false))  ;
        return buildResponseEntity(error);
    }


    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(  HttpStatus.INTERNAL_SERVER_ERROR,
                                                        ex.getMessage(),
                                                        request.getDescription(false))  ;
        return buildResponseEntity(error);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(  HttpStatus.BAD_REQUEST,
                                                        ex.getMessage(),
                                                        request.getDescription(false))  ;
        return buildResponseEntity(error);
    }

    //baba immer über dede, wegen von oben nach unten!
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(  HttpStatus.INTERNAL_SERVER_ERROR,
                                                        ex.getMessage(),
                                                        request.getDescription(false));
        return buildResponseEntity(error);
    }

    //dede
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGeneralException( Exception ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(  HttpStatus.INTERNAL_SERVER_ERROR,
                                                        ex.getMessage(),
                                                        request.getDescription(false));
        return buildResponseEntity(error);
    }
}