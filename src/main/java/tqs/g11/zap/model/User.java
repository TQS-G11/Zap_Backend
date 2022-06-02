package tqs.g11.zap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tqs.g11.zap.dto.UserDto;
import tqs.g11.zap.enums.UserRoles;

import javax.persistence.*;

@Entity
@Table(name = "app_users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @Column(unique = true)
    private String username;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    @JsonIgnore
    private String password;

    @Getter
    @Setter
    private String role;


    public User(UserDto dto) {
        username = dto.getUsername();
        name = dto.getName();
        password = dto.getPassword();
        role = dto.getRole();
    }

    public User(String username, String name, String password, UserRoles role) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.role = role.toString();
    }
}
