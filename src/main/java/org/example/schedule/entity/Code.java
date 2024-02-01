package org.example.schedule.entity;

public enum Code {
    Success200("사용자가 생성되었습니다."),
    Success201("로그인 되었습니다"),
    Fail400("중복된 username 입니다."),
    Fail401("회원을 찾을 수 없습니다."),
    Fail402("시스템에러가 발생했습니다."),
    Fail403("입력된 할일이 없습니다."),
    Fail404("유효하지않은 토큰입니다"),
    Fail405("작성자만 삭제/수정할 수 있습니다."),
    Fail406("해당하는 할일카드가 없습니다."),
    Fail407("\"해당 댓글은 등록되어있지 않습니다.\"");

    private final String statusCode;


    Code(String statusCode) {
        this.statusCode = statusCode;
    }
}
