package com.itoxi.petnuri.global.common.exception.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // COMMON
    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생하였습니다."), // 500
    FILE_TRANSFER_ERROR("파일 전송 중 오류가 발생했습니다."), // 500
    DUPE_POST_MEMBER("이미 인증글을 등록한 회원입니다."), // 400

    VALIDATION_ERROR("유효성 검사 중 예외가 발생했습니다."),
    S3UPLOADER_ERROR("s3 업로드 중 오류가 발생했습니다."),
    INVALID_FILE_REQUEST("유효하지 않은 파일이 전송되었습니다."), // 400


    // REDIS
    INVALID_OR_EXPIRED_KEY("만료되었거나 유효하지 않은 키입니다."),
    NOT_MATCH_TOKEN("토큰이 일치하지 않습니다."),

    // JSON
    JSON_PARSE_FAILED("JSON 파싱에 실패했습니다."),

    // PET TALK
    INVALID_PET_TALK_ID("유효하지 않은 펫톡 고유번호입니다."),
    INVALID_MAIN_CATEGORY_ID("유효하지 않은 메인 카테고리 고유번호입니다."),
    INVALID_SUB_CATEGORY_ID("유효하지 않은 서브 카테고리 고유번호입니다."),
    MISMATCH_PET_TALK_WRITER("해당 펫톡 게시글의 작성자가 아닙니다."),

    LOGIN_FAILED("회원 정보가 존재하지 않습니다."),
    UN_AUTHORIZED("인증되지 않았습니다."),
    FORBIDDEN("접근이 거부되었습니다."),
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    DUPLICATED_EMAIL("이미 가입된 이메일입니다."),
    DUPLICATED_NICKNAME("이미 사용중인 닉네임입니다."),
    REFERRAL_CODE_NOT_FOUND("잘못된 추천인 코드입니다."),
    POINT_NOT_FOUND("회원 포인트 정보를 찾을 수 없습니다."),

    // 회원
    NOT_FOUND_MEMBER_ID("존재하지 않는 회원 ID 입니다."),

    // 챌린지
    NOT_FOUND_CHALLENGE_ID("유효하지 않은 챌린지 ID 입니다."),
    NOT_FOUND_CHALLENGE_JOIN("챌린지 참여 내역이 존재하지 않습니다."),
    NOT_FOUND_CHALLENGE_KIT("챌린지에 등록된 검진 키트가 없습니다!"), //500 err
    DUPE_CHALLENGE_JOIN("이미 챌린지 참여 내역이 존재합니다."),

    // 포인트
    OUT_OF_POINT("보유 포인트가 부족합니다."),

    // 상품
    NOT_FOUND_CHALLENGE_PRODUCT_ID("유효하지 않은 챌린지 상품 ID 입니다.");

    private final String message;

}
