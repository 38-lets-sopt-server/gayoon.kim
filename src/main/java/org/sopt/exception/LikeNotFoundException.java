package org.sopt.exception;

public class LikeNotFoundException extends RuntimeException {

    public LikeNotFoundException() {
        super("존재하지 않는 좋아요 입니다.");
    }
}