package tqs.g11.zap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tqs.g11.zap.model.User;

@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String role;

    public UserDto(User user) {
        id = user.getId();
        username = user.getUsername();
        name = user.getName();
        password = user.getPassword();
        role = user.getRole();
    }
}
