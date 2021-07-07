package com.ot6.mercadolivre.user;

import com.ot6.mercadolivre.user.dtos.NewUserResponse;
import com.ot6.mercadolivre.user.helpers.CleanPassword;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6)
    private String password;

    @NotNull
    private LocalDateTime creationInstant = LocalDateTime.now();

    public User(String email, CleanPassword cleanPassword) {
        this.email = email;
        this.password = cleanPassword.encode();
    }

    public NewUserResponse toNewUserResponse() {
        return new NewUserResponse(this.id);
    }
}
