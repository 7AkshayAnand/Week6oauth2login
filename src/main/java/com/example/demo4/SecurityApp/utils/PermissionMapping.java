package com.example.demo4.SecurityApp.utils;

import com.example.demo4.SecurityApp.entities.enums.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public class PermissionMapping {
//    below we are mapping a Role with set of permissions like if Role=USER then it can has the permissions of
//    USER_VIEW and POST_VIEW
    private static final  Map<Role, Set<Permissions>> map=Map.of(
            Role.USER,Set.of(Permissions.USER_VIEW,Permissions.POST_VIEW),
            Role.CREATOR,Set.of(Permissions.USER_UPDATE,Permissions.POST_CREATE,Permissions.POST_UPDATE),
            Role.ADMIN,Set.of(Permissions.USER_UPDATE,Permissions.POST_CREATE,Permissions.POST_UPDATE,Permissions.USER_CREATE,Permissions.USER_DELETE,Permissions.POST_DELETE)
    );

//    below method recieves the role and returns what all permissions assigned for this role
    public static Set<SimpleGrantedAuthority> getAuthoritiesForRole(Role role){
        return map.get(role).stream().map(permissions -> new SimpleGrantedAuthority(permissions.name())).collect(Collectors.toSet());
    }
}
