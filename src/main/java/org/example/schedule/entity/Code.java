package org.example.schedule.entity;

import lombok.Getter;

@Getter
public enum Code {
    SUCCESS_200("사용자가 생성되었습니다."),
    SUCCESS_201("로그인 되었습니다"),
    SUCCESS_202("댓글이 삭제 되었습니다."),
    FAIL_400("중복된 username 입니다."),
    FAIL_401("회원을 찾을 수 없습니다."),
    FAIL_402("시스템에러가 발생했습니다."),
    FAIL_403("입력된 할일카드가 없습니다."),
    FAIL_404("유효하지않은 토큰입니다"),
    FAIL_405("작성자만 삭제/수정할 수 있습니다."),
    FAIL_406("해당하는 할일카드가 없습니다."),
    FAIL_407("해당 댓글은 등록되어있지 않습니다.");


    private final String statusCode;

    private Code(String statusCode) {
        this.statusCode = statusCode;

    }

}