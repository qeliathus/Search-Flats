package by.potapchuk.userservice.core.exceptions;

import by.potapchuk.userservice.core.exceptions.body.ErrorField;
import by.potapchuk.userservice.core.exceptions.body.ErrorResponse;
import by.potapchuk.userservice.core.exceptions.body.StructuredErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ResponseBody
@RestControllerAdvice
public class ExceptionHandlerResolver {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFound(EntityNotFoundException exception) {
        log.error(exception.getMessage());
        return new ErrorResponse("error", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public StructuredErrorResponse handleValidation(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());
        return new StructuredErrorResponse(
                "structured_error",
                exception.getBindingResult()
                        .getAllErrors()
                        .stream()
                        .map(this::convert)
                        .toList()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
        log.error(exception.getMessage());
        return new ErrorResponse("error", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidLinkException.class)
    public ErrorResponse handleInvalidLink(InvalidLinkException exception) {
        log.error(exception.getMessage());
        return new ErrorResponse("error", exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public String handleUnauthorized(UnauthorizedException exception) {
        log.error(exception.getMessage());
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(AccessDeniedException exception) {
        log.error(exception.getMessage());
        return exception.getMessage();
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception exception) {
        log.error(exception.getMessage());
        return new ErrorResponse(
                "error",
                "Сервер не смог корректно обработать запрос. Пожалуйста обратитесь к администратор");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handleValidationException(ValidationException exception) {
        log.error(exception.getMessage());
        return new ErrorResponse("error",
                exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse handleHandlerResolverException(HttpMessageNotReadableException exception) {
        log.error(exception.getMessage());
        return new ErrorResponse("error",
                exception.getMessage());
    }

    private ErrorField convert(ObjectError objectError) {
        return new ErrorField(
                ((FieldError) objectError).getField(),
                objectError.getDefaultMessage()
        );
    }
}
