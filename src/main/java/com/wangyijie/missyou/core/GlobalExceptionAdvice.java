package com.wangyijie.missyou.core;

import com.wangyijie.missyou.core.configuration.ExceptionCodeConfiguration;
import com.wangyijie.missyou.exception.http.HttpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionAdvice {
    @Autowired
    private ExceptionCodeConfiguration codeConfiguration;

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public UnifyResponse handleException(HttpServletRequest request, Exception e) {
        String requestUrl = request.getRequestURI();
        String method = request.getMethod();
        System.out.println(e);
        return new UnifyResponse(9999, "服务器异常", method + " " + requestUrl);
    }

    @ExceptionHandler(value = HttpException.class)
    public ResponseEntity<UnifyResponse> handleHttpException(HttpServletRequest request, HttpException e) {
        String requestUrl = request.getRequestURI();
        String method = request.getMethod();

        UnifyResponse response = new UnifyResponse(e.getCode(), codeConfiguration.getMessage(e.getCode()), method + " " + requestUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpStatus httpStatus = HttpStatus.resolve(e.getHttpStatusCode());

        return new ResponseEntity<>(response, httpHeaders, httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public UnifyResponse handleBeanValidation(HttpServletRequest request, MethodArgumentNotValidException e) {
        String requestUrl = request.getRequestURI();
        String method = request.getMethod();

        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        String message = formatAllErrorMessages(allErrors);
        return new UnifyResponse(10001, message, method + " " + requestUrl);
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public UnifyResponse handleConstraintException(HttpServletRequest request, ConstraintViolationException e) {
        String requestUrl = request.getRequestURI();
        String method = request.getMethod();
        String message = e.getMessage();
        return new UnifyResponse(10001, message, method + " " + requestUrl);
    }

    private String formatAllErrorMessages(List<ObjectError> errors) {
        StringBuffer errorMsg = new StringBuffer();
        for (ObjectError error : errors) {
            errorMsg.append(error.getDefaultMessage()).append(";");
        }
        return errorMsg.toString();
    }
}
