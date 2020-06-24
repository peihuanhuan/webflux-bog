package net.peihuan.blogapi.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.peihuan.blogapi.exception.AuthException;
import net.peihuan.blogapi.exception.RequestException;
import net.peihuan.blogapi.vo.ResponseAPIStatus;
import net.peihuan.blogapi.vo.RestResult;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResult> innerError(Exception e) {
        log.error(e.getMessage(),e);
        RestResult restResult = new RestResult(ResponseAPIStatus.INTERNAL_ERROR, "Something bad has happened.");
        return new ResponseEntity<>(restResult, restResult.getHttpStatus());
    }

    @ExceptionHandler(RequestException.class)
    @Order()
    public ResponseEntity<RestResult> requestError(Exception e) {
        log.info(e.getMessage());
        RestResult restResult = new RestResult(ResponseAPIStatus.PARAMETER_ERROR, "参数错误");
        return new ResponseEntity<>(restResult, restResult.getHttpStatus());
    }

    @ExceptionHandler(AuthException.class)
    @Order()
    public ResponseEntity<RestResult> AuthError(Exception e) {
        log.info(e.getMessage());
        RestResult restResult = new RestResult(ResponseAPIStatus.LOGIN_FAILED, e.getMessage());
        return new ResponseEntity<>(restResult, restResult.getHttpStatus());
    }

}
