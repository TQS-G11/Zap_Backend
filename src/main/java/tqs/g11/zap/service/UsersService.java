// package tqs.g11.zap.service;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;
// import tqs.g11.zap.dto.UserDto;
// import tqs.g11.zap.model.User1;
// import tqs.g11.zap.repository.UsersRepository;

// import java.util.HashSet;
// import java.util.List;
// import java.util.Optional;
// import java.util.Set;

// @Service
// public class UsersService implements UserDetailsService {
//     @Autowired
//     private UsersRepository usersRepository;

//     public List<User1> getAllUsers() {
//         return usersRepository.findAll();
//     }

//     public Optional<User1> getUserById(Long id) {
//         return usersRepository.getUserById(id);
//     }

//     public User1 createUser(UserDto userDto) {


//         return usersRepository.save(new User1(userDto));
//     }

//     public User1 getAuthUser(UserDetails details) {
//         return usersRepository.findByUsernameAndPassword(details.getUsername(), details.getPassword());
//     }

//     @Override
//     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//         User1 user = usersRepository.findByUsername(username);
//         if (user == null)
//             throw new UsernameNotFoundException("User with such username does not exist.");
//         return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPass(), getAuthority(user));
//     }

//     private Set<SimpleGrantedAuthority> getAuthority(User1 user) {
//         Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//         authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRol()));
//         return authorities;
//     }

//     public boolean usernameAvailable(String username) {
//         try {
//             return usersRepository.findByUsername(username) == null;
//         } catch (Exception e) {
//             return false;
//         }
//     }
// }
