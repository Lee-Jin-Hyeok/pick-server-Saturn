package com.dsm.pick.utils.handler;

import com.dsm.pick.utils.exception.IdOrPasswordMismatchException;
import com.dsm.pick.utils.exception.RefreshTokenMismatchException;
import com.dsm.pick.utils.exception.TokenExpirationException;
import com.dsm.pick.utils.form.ApiErrorResponseForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(IdOrPasswordMismatchException.class)
    public ResponseEntity<ApiErrorResponseForm> idMismatchExceptionHandler(IdOrPasswordMismatchException ex) {
        ApiErrorResponseForm response = new ApiErrorResponseForm("ID Mismatch Exception", "아이디가 일치하는 계정을 찾을 수 없음");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TokenExpirationException.class)
    public ResponseEntity<ApiErrorResponseForm> tokenExpirationExceptionHandler(TokenExpirationException ex) {
        ApiErrorResponseForm response = new ApiErrorResponseForm("Token Expiration Exception", "토큰이 만료되거나 잘못됨");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RefreshTokenMismatchException.class)
    public ResponseEntity<ApiErrorResponseForm> tokenExpirationExceptionHandler(RefreshTokenMismatchException ex) {
        ApiErrorResponseForm response = new ApiErrorResponseForm("Refresh Token Mismatch Exception", "일치하는 리프레시 토큰을 찾을 수 없음");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}