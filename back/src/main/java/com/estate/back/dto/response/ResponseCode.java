package com.estate.back.dto.response;

// Response의 공통된 code 값
public interface ResponseCode {
    String SUCCESS = "SU";
    String VALIDATION_FAILED = "VF";
    String DUPLICATED_ID = "DI";
    String DUPLICATED_EMAIL = "DE";
    String NO_EXIST_BOARD = "NB";
    String WRITTEN_COMMENT = "WC";
    String SIGN_IN_FAILED = "SF";
    String AUTHENTICATION_FAILED = "AF";
    String AUTHORIZATION_FAILED = "AF";
    String NOT_FOUND = "NF";
    String TOKEN_CREATION_FAILED = "TF";
    String MAIL_SEND_FAILED = "MF";
    String DATABASE_ERROR = "DBE";

}
