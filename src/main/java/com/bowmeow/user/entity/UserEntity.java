package com.bowmeow.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class UserEntity {
    private String id;
    private String username;
    private String password;
    private String email;
}
