package com.fbxmtjqj.membership.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOT_MEMBERSHIP_OWNER(HttpStatus.BAD_REQUEST, "Not a membership owner"),
    MEMBERSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "Membership Not found"),
    DUPLICATED_MEMBERSHIP_REGISTER(HttpStatus.BAD_REQUEST, "Duplicated Membership Register Request"),
    NOT_FOUND_TOKEN(HttpStatus.BAD_REQUEST, "Token Not found"),
    NOT_EQUAL_TOKEN(HttpStatus.BAD_REQUEST, "Token Not equal"),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "Token Invalid"),
    UNKNOWN_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown Exception"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}