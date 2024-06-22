package com.todoAppWithAws.book.springboot.config.auth;

import com.todoAppWithAws.book.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity // Spring security를 활성화 시켜줍니다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // Disables these options to use the H2 console screen.
                .and()
                .authorizeRequests() // The starting point for setting URL-specific access management.
                .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                // Specifies the targets for access management.
                // Allows management by URL and HTTP method.
                // Grants full access to the specified URLs such as '/' through the permitAll() option.
                .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                // Only users with the USER role can access APIs with the "/api/v1/**" address.
                .anyRequest().authenticated()
                // Refers to the remaining URLs not specified.
                // Adds authenticated() to require login for all remaining URLs.
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .userInfoEndpoint()
                // Manages settings for retrieving user information after a successful OAuth2 login.
                .userService(customOAuth2UserService);
        // Registers the implementation of the UserService interface to handle post-login actions.
        // You can specify additional functions to proceed after retrieving user information from the resource server (i.e., social services).
    }


}
