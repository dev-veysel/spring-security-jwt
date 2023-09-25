package com.hacker.exception.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiResponseError {
    // AMACIM : custom error mesajlarının ana soblonunu ouşturmak



    private HttpStatus status;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    private String message;

    private String requestURI ;

    // Die veränderte Anzeige in Postman!! Schablone

    private ApiResponseError(){
        timestamp = LocalDateTime.now();
    }  //1

    public ApiResponseError(HttpStatus status){                     //1+2
        this(); // yukardaki parametresiz private const. çağırılıyor!
        this.message="Unexpected Error";
        this.status = status ;
    }

    public ApiResponseError(HttpStatus status, String message, String requestURI) {     //1+2+3
        this(status); // yukardaki 1 parametreli, public const. çağrılıyor
        this.message = message;
        this.requestURI = requestURI;
    }



    // GETTER -SETTER
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }
}
