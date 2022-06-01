// package tqs.g11.zap.enums;

// import java.util.List;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;

// public enum UserRoles {
//     MANAGER("MANAGER"),
//     RIDER("CLIENT");

//     private final String role;

//     UserRoles(String role) {
//         this.role = role;
//     }

//     @Override
//     public String toString() {
//         return role;
//     }

//     public static boolean validRole(String value) {
//         List<String> rolesNames = Stream.of(UserRoles.values())
//                 .map(Enum::toString)
//                 .collect(Collectors.toList());
//         return rolesNames.contains(value);
//     }
// }
