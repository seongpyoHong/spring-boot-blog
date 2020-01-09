/*
 Session User를 따로 두는 이유

 기존 User class를 사용하기 위해서는 직렬화를 구현해야한다.
 Entity class에 직렬화를 구현하게 되면 Entity class과 관계(@OneToMany, @ManyToMany)를 맺는 child entity class가 있는 경우
 child entity class까지 직렬화 대상에 포함된다.
 이 점은 성능 이슈는 물론 side effect를 발생시킬 수 있다.
 */

package com.sphong.config.auth.dto;

import com.sphong.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}

