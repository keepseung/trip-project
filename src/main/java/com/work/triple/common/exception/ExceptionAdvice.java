package com.work.triple.common.exception;

import com.work.triple.common.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

    private final MessageSource messageSource;

    // 예기치 못한 에러 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected ErrorResponse serverException(Exception e) {
        log.error("error :{}",e.getMessage());
        String errKey = "unKnown";
        return getErrorResponse(errKey);
    }

    // 클라이언트 400 Error 발생하는 경우
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({BadRequestException.class})
    public ErrorResponse badRequestHandle(BadRequestException e) {
        return getErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public ErrorResponse illegalExceptionHandle(IllegalArgumentException e) {
        return getErrorResponse("requestValidationError");
    }

    // 유저 요청 데이터 유효성 에러
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ErrorResponse validException(MethodArgumentNotValidException e) {
        String errKey= "requestValidationError";
        return getErrorResponse(errKey);
    }

    // 유저 인증 에러
    @ExceptionHandler(UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse unauthorizedException(UnAuthorizedException e) {
        return getErrorResponse(e.getErrKey());
    }

    // 유저가 요청한 리소스가 존재하지 않은 경우
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundException(NotFoundException e) {
        return getErrorResponse(e.getErrKey());
    }

    // 에러 응답을 생성한다.
    private ErrorResponse getErrorResponse(String errKey) {
        return ErrorResponse.builder()
                .errorCode(getMessage(errKey + ".code"))
                .message(getMessage(errKey + ".msg"))
                .build();
    }

    // code정보에 해당하는 메시지를 조회합니다.
    private String getMessage(String code) {
        return getMessage(code, null);
    }

    // code정보, 추가 argument로 현재 locale에 맞는 메시지를 조회합니다.
    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
