package com.todoAppWithAws.book.springboot.config.auth.dto;

import com.todoAppWithAws.book.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;
@Getter
public class SessionUser implements Serializable {
    /*
        User클래스가 아니고 SessionUser를 따로 만들어서 사용하는 이유?
            - user클래스는 직렬화 되지 않았고 나중에 @ManyToOne이나 @OneToMany같이 부수적인
            자식 엔티티까지 포함한다면 유지보수나 성능이슈때문에 피하고 따로 Dto를 가진 세션유저를 만들어서 개발
            하는것이 유지보수에 편리하다.
     */
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
