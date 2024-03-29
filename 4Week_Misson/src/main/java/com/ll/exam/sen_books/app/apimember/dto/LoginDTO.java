package com.ll.exam.sen_books.app.apimember.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginDTO {
    @NotBlank(message = "username 을(를) 입력해주세요.")
    private String username;
    @NotBlank(message = "password 을(를) 입력해주세요.")
    private String password;

    public boolean isNotValid() {
        return username == null || password == null || username.trim().length() == 0 || password.trim().length() == 0;
    }
}
