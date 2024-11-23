package com.example.demo4.SecurityApp.config;

import com.example.demo4.SecurityApp.entities.enums.Permissions;
import com.example.demo4.SecurityApp.entities.enums.Role;
import com.example.demo4.SecurityApp.filters.JwtAuthFilter;
import com.example.demo4.SecurityApp.handlers.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;



@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    private static final String[] publicRoutes = {
            "/error", "/auth/**", "/home.html"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicRoutes).permitAll()
                        .requestMatchers(HttpMethod.GET,"/posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/posts/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST,"/post/**").
                        hasAnyAuthority(Permissions.POST_CREATE.name())

                        .requestMatchers(HttpMethod.PUT,"/post/**").
                        hasAnyAuthority(Permissions.POST_UPDATE.name())
                        .requestMatchers(HttpMethod.DELETE,"/post/**").
                        hasAnyAuthority(Permissions.POST_DELETE.name())
                        .anyRequest().authenticated())
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionConfig -> sessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2Config -> oauth2Config
                        .failureUrl("/login?error=true")
                        .successHandler(oAuth2SuccessHandler)
                );
//                .formLogin(Customizer.withDefaults());

        return httpSecurity.build();


//        Key Behavior Flow:
//        Request Comes In:
//        The jwtAuthFilter checks if the request contains a valid JWT.
//        If the JWT is valid, the jwtAuthFilter creates an Authentication object and sets it in SecurityContextHolder.
//        Authorization (requestMatchers):
//        Once the SecurityContext is populated (by jwtAuthFilter), Spring evaluates the requestMatchers.
//                If the Authentication object is available (from the JWT filter), Spring Security evaluates if the user has the required roles or permissions to access the requested resource (e.g., hasRole(Role.ADMIN.name()) for /posts/**).
//         ________________________________________
//         Clarification on requestMatchers Execution:
//         requestMatchers execute in the context of the authenticated user: The role check (hasRole(Role.ADMIN.name())) or any other authorization decision depends on the Authentication object, which is set by your jwtAuthFilter.
//         The JWT Filter ensures the user is authenticated before any authorization rules are checked. If the JWT is invalid or missing, Spring will prevent access according to the authentication rules (anyRequest().authenticated()).
//         ________________________________________
//         Key Takeaway:
//         •	The flow works as expected: The jwtAuthFilter is executed first, and it sets the authentication in the SecurityContext.
//         Then, when the requestMatchers are processed, they can evaluate the roles and permissions of the authenticated user, as the SecurityContext will contain the user’s authorities (set by the jwtAuthFilter).


         }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
