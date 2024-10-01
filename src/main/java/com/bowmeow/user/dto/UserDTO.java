package com.bowmeow.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private String email;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String address; // 사용자 입력 주소
    private String postcode; // 우편번호
}
