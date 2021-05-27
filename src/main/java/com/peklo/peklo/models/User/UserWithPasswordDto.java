package com.peklo.peklo.models.User;

import com.peklo.peklo.utils.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserWithPasswordDto {
    @NotBlank
    @Size(max = 50)
    private String username;

    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 4, max = 50)
    private String password;
    @NotBlank
    @Size(min = 4, max = 50)
    private String password2;


    public static User fromDTO(UserWithPasswordDto user) {
        return User.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(user.getPassword())
                .active(false)
                .role(Roles.USER)
                .build();
    }
}
