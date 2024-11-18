package draw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  private AuthUserService authUserService;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .csrf().disable()
      .authorizeHttpRequests()
      .requestMatchers("/", "/oauth/**").permitAll()
      .anyRequest().authenticated()
      .and()
      .oauth2Login()
      .userInfoEndpoint()
      .userService(authUserService)
      .and()
      .defaultSuccessUrl("/")
      .failureUrl("/loginFailure");

    return http.build();
  }

  @Bean
  public ClientRegistrationRepository clientRegistrationRepository() {
    ClientRegistration kakaoRegistration = ClientRegistration.withRegistrationId("kakao")
      .clientId("59423bd975fc14e79e8f0314203ed60a")
      .clientSecret("") // 카카오는 client-secret이 필요하지 않습니다.
      .redirectUri("http://localhost:8080/oauth/kakao/callback")
      .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
      .scope("profile_nickname", "profile_image", "account_email")
      .authorizationUri("https://kauth.kakao.com/oauth/authorize")
      .tokenUri("https://kauth.kakao.com/oauth/token")
      .userInfoUri("https://kapi.kakao.com/v2/user/me")
      .userNameAttributeName("id")
      .clientName("Kakao")
      .build();

    return new InMemoryClientRegistrationRepository(kakaoRegistration);
  }

}