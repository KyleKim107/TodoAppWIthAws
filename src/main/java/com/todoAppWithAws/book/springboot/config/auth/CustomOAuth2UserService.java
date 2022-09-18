package com.todoAppWithAws.book.springboot.config.auth;

import com.todoAppWithAws.book.springboot.config.auth.dto.OAuthAttributes;
import com.todoAppWithAws.book.springboot.config.auth.dto.SessionUser;
import com.todoAppWithAws.book.springboot.domain.user.User;
import com.todoAppWithAws.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // registrationId : 현재 로그인 진행 서비스를 구분하는 코드입니다.
        // 로그인 기능 서비스 provider를 구분하기 위해 추후에 사용됩니다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        // userNameAttributeName 이란
        // 로그인시 PK역할을 하며 로그인에 필요한 데이터를 보관합니다.
        // 구글은 기본코드를 제공하지만 네이버 카카오는 제공 하지 않습니다.
        //추후 네이버와 구글 로그인을 동시 지원할때 사용합니다.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        //OAuthAttributes이란
        // Oauth2UserService를 통해 가져온 Oauth2User의 attribute를 담을 클래스 입니다.
        // 이후 네이버등 다른 소셜 로그인도 이 클래스를 사용
        OAuthAttributes attributes = OAuthAttributes
                .of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
        // SessoinUser이란
        // 세션 사용자의 정보를 저장하기 위한 Dto클래스입니다.
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
