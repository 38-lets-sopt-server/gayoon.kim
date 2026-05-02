package org.sopt.exception;

public class AlreadyLikedException extends RuntimeException {

    public AlreadyLikedException() {
        super("이미 좋아요를 눌렀습니다.");
    }
}
