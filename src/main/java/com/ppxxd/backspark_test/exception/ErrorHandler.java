package com.ppxxd.backspark_test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class ErrorHandler {
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FORMAT);

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException ex) {
        return new ErrorResponse(
                HttpStatus.CONFLICT.getReasonPhrase(),
                "Нарушено ограничение целостности.",
                ex.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter)
        );
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleObjectNotFoundException(final ObjectNotFoundException ex) {
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                "Нужный объект не найден.",
                ex.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter)
        );
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Некорректный формат данных.",
                ex.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter)
        );
    }

    @ExceptionHandler(FileHandlerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFileHandlerException(final FileHandlerException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Ошибки при обработке файлов.",
                ex.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter)
        );
    }

    @ExceptionHandler(SocksShortageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSocksShortageException(final SocksShortageException ex) {
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Нехватка носков на складе..",
                ex.getMessage(),
                LocalDateTime.now().format(dateTimeFormatter)
        );
    }
}
