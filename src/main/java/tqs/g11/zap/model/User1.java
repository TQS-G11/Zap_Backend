// package tqs.g11.zap.model;

// import com.fasterxml.jackson.annotation.JsonIgnore;
// import lombok.AllArgsConstructor;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import tqs.g11.zap.dto.UserDto;
// import tqs.g11.zap.enums.UserRoles;

// import javax.persistence.*;

// @Entity
// @Table(name = "app_users")
// @NoArgsConstructor
// @AllArgsConstructor
// public class User1 {
//     @Id
//     @GeneratedValue(strategy = GenerationType.AUTO)
//     @Getter
//     @Setter
//     private Long id;

//     @Getter
//     @Setter
//     @Column(unique = true)
//     private String username;

//     @Getter
//     @Setter
//     private String nome;

//     @Getter
//     @Setter
//     @JsonIgnore
//     private String pass;

//     @Getter
//     @Setter
//     private String rol;

//     @Getter
//     @Setter
//     private String companyStatus;

//     @Getter
//     @Setter
//     private String riderStatus;

//     @Getter
//     @Setter
//     private Double riderRating;

//     public User1(UserDto dto) {
//         username = dto.getUsername();
//         nome = dto.getName();
//         pass = dto.getPassword();
//         rol = dto.getRole();
//     }

//     public User1(String username, String name, String password, UserRoles role) {
//         this.username = username;
//         this.nome = name;
//         this.pass = password;
//         this.rol = role.toString();
//     }
// }
