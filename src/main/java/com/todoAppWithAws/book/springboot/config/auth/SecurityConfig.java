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
                .headers().frameOptions().disable()//h2콘솔 화면을 사용하기 위해 해당 옵션들을 disable합니다.
                .and()
                    .authorizeRequests()//URL별 권한 관리를 설정하는 옵션의 시작점입니다.
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/profile").permitAll()
                    // 권한관리 대상을 지정하는 옵션입니다.
                    // URL,Http메소드별로 관리가 가능 합니다.
                    // '/'등 지정된 URL들은 permitAll()옵션을 통해 전체 열람 권한을 주었습니다.
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    // "/api/v1/**"주소를 가진 API는 USER권한을 가진 사람만 가능 하도록 했습니다.
                    .anyRequest().authenticated()
                    //설정된값들 외에 나머지 URL들을 나타냅니다.
                    //authenticated()을 추가하여 나머지 URL들은 모두 로그인한 사용자들에게만 허용하게 합니다.
                .and()
                    .logout()
                    .logoutSuccessUrl("/")
                .and()
                    .oauth2Login()
                        .userInfoEndpoint()
                        //Oauth2로그인 성공 이후 사용자 정보를 가져올때의 설정들을 담당합니다.
                            .userService(customOAuth2UserService);
                            //로그인 성공 이후에 후속 조치를 진행할 UserService인터페이스의 구현체를 등록합니다.
                            // 리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자
                            // 하는 기능을 명시할 수 있습니다
    }


}
