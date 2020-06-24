package net.peihuan.blogapi.vo;

import org.springframework.http.HttpStatus;


public enum ResponseAPIStatus {

    SUCCESS(200, HttpStatus.OK, "success"),

    PARAMETER_ERROR(1001, HttpStatus.BAD_REQUEST, "bad request"),

    LOGIN_FAILED(2001, HttpStatus.BAD_REQUEST, "lgoin fail"),
    UNAUTHORIZED(2002, HttpStatus.UNAUTHORIZED, "unauthorized"),

    SAVE_DRAFT_ERROR(3001, HttpStatus.INTERNAL_SERVER_ERROR, "save draft failed"),
    PUBLISH_ERROR(3002, HttpStatus.INTERNAL_SERVER_ERROR, "publish failed"),

    FILE_NOT_EXIST(4001, HttpStatus.BAD_REQUEST, "file not exist"),

    INTERNAL_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "something unexcepted error");

    private final int code;
    private final HttpStatus httpStatus;
    private final String desc;

    public int getCode() {
        return this.code;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public String getDesc() {
        return this.desc;
    }

    private ResponseAPIStatus(int code, HttpStatus httpStatus, String desc) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.desc = desc;
    }
}
