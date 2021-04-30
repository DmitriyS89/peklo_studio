package com.peklo.peklo.models.User;

import com.peklo.peklo.utils.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private Roles role;
    private String active;

    public static UserDto from(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .active(getActive(user.getActive()))
                .role(user.getRole())
                .build();
    }

    public static String getActive(boolean b){
        if(b){
            return "Активный";
        }else {
            return "Неактивный";
        }
    }
}
