package com.example.demo4.SecurityApp.entities;

import com.example.demo4.SecurityApp.entities.enums.Permissions;
import com.example.demo4.SecurityApp.entities.enums.Role;
import com.example.demo4.SecurityApp.utils.PermissionMapping;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    @ElementCollection(fetch= FetchType.EAGER)
//    above when we set fetchtype.eager it means when we fetch the User all the roles associated with user also get fetched
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;


//    @ElementCollection(fetch= FetchType.EAGER)
////    above when we set fetchtype.eager it means when we fetch the User all the roles associated with user also get fetched
//    @Enumerated(EnumType.STRING)
//    private  Set<Permissions>  permissions;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        below we are just adding roles and permissions in authorities as it can have both
//        later on we will map a role with permissions like role=ADMIN permission=POST_CREATE,POST_DELETE etc
//        And we are doing it in utils package in PermissionMapping class

//        Set<SimpleGrantedAuthority> authorities= roles.stream().map(role->new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet());
//
//       permissions.forEach(permissions1 -> authorities.add(new SimpleGrantedAuthority(permissions1.name())));


//        Below we are adding role and its associated permission in the authorities

        Set<SimpleGrantedAuthority>  authorities=new HashSet<>();
        roles.forEach(role -> {
              Set<SimpleGrantedAuthority> permissions= PermissionMapping.getAuthoritiesForRole(role);
              authorities.addAll(permissions);
              authorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));

                }

                );
       return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
