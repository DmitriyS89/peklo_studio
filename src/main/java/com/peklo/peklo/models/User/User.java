package com.peklo.peklo.models.User;

import com.peklo.peklo.utils.Roles;
import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Builder
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Not Blank
    private String username;
//    @Email @NotBlank
    private String email;
//    @Min(8) @NotBlank
    private String password;
    //    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Roles role;

    private Boolean active;
}
