package net.thumbtack.forums.serviceimpl;

import net.thumbtack.forums.dto.response.Error;
import net.thumbtack.forums.dto.response.ErrorsDtoResponse;
import net.thumbtack.forums.errors.ForumException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;


@ControllerAdvice
class GlobalErrorHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ForumException.class)
    @ResponseBody
    public ErrorsDtoResponse handleForumException(ForumException ex) {
        ErrorsDtoResponse errors = new ErrorsDtoResponse();
        errors.addError(new Error(ex.getErrorCode().toString(), ex.getField(), ex.getMessage()));
        return errors;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorsDtoResponse handleValidationExceptions(MethodArgumentNotValidException ex) {
        ErrorsDtoResponse errors = new ErrorsDtoResponse();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.addError(new Error(fieldName, errorMessage));
        });
        return errors;
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ErrorsDtoResponse handleNotFound(NoHandlerFoundException ex) {
        ErrorsDtoResponse errors = new ErrorsDtoResponse();
        errors.addError(new Error(ex.getCause().toString(), ex.getMessage()));
        return errors;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ErrorsDtoResponse handleMissingParameterException(MissingServletRequestParameterException ex) {
        ErrorsDtoResponse errors = new ErrorsDtoResponse();
        errors.addError(new Error(ex.getCause().toString(), ex.getMessage()));
        return errors;
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ErrorsDtoResponse handleResponseStatusException(MethodArgumentTypeMismatchException ex) {
        ErrorsDtoResponse errors = new ErrorsDtoResponse();
        errors.addError(new Error(ex.getCause().toString(), ex.getMessage()));
        return errors;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NullPointerException.class)
    @ResponseBody
    public ErrorsDtoResponse handleNPE(NullPointerException ex) {
        ErrorsDtoResponse errors = new ErrorsDtoResponse();
        errors.addError(new Error(ex.getCause().toString(), ex.getMessage()));
        return errors;
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorsDtoResponse handleException(Exception ex) {
        ErrorsDtoResponse errors = new ErrorsDtoResponse();
        errors.addError(new Error(ex.getCause().toString(), ex.getMessage()));
        return errors;
    }

}
