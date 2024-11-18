package draw;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) {
    DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    // 카카오 사용자 정보 처리 (예: 사용자 정보 가져오기)
    // Map<String, Object> attributes = oAuth2User.getAttributes();
    return oAuth2User;
  }
}
