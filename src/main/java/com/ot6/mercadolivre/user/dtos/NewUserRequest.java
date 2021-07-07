package com.ot6.mercadolivre.user.dtos;

import com.ot6.mercadolivre.shared.validation.annotation.Unique;
import com.ot6.mercadolivre.user.User;
import com.ot6.mercadolivre.user.helpers.CleanPassword;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class NewUserRequest {

    @NotBlank
    @Email
    @Unique(field = "email", theClass = User.class, message = "E-mail já em uso")
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    public NewUserRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User toEntity() {
        return new User(this.email, new CleanPassword(password));
    }
}
