package org.sopt.exception;

public enum ErrorCode {

    USER_NOT_FOUND("존재하지 않는 사용자입니다."),
    POST_NOT_FOUND("존재하지 않는 게시글입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}