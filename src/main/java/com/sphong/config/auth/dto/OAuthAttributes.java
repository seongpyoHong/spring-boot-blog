package com.sphong.config.auth.dto;

import com.sphong.domain.user.Role;
import com.sphong.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String,Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes( Map<String,Object> attributes, String nameAttributeKey,
                              String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String,Object> attributes) {
        if ("naver".equals(registrationId)){
            //여기서 id를 user_name에 매핑
            return ofNaver("id",attributes);
        }
        return ofGoogle(userNameAttributeName,attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String,Object> attributes) {
        return OAuthAttributes.builder()
                .name((String)attributes.get("name"))
                .email((String)attributes.get("email"))
                .picture((String)attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    //naver oauth의 response : JSON
    //Spring Security에서는 최상위 필드밖에 사용하지 못하므로 user_name을 response 받는다.
    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String,Object> attributes) {
        Map<String,Object> response = (Map<String,Object>)attributes.get("response");
        return OAuthAttributes.builder()
                .name((String)attributes.get("name"))
                .email((String)attributes.get("email"))
                .picture((String)attributes.get("picture"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }
}
