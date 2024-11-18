package draw.controller;

import draw.KakaoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OAuthController {

  private final KakaoService kakaoService;
  @Value("${security.oauth2.client.registration.kakao.client-id}")
  private String clientId;

  @Value("${security.oauth2.client.registration.kakao.redirect-uri}")
  private String redirectUri;

  public OAuthController(KakaoService kakaoService) {
    this.kakaoService = kakaoService;
  }

  @GetMapping("/oauth/kakao/callback")
  public ResponseEntity<String> kakaoCallback(@RequestParam String code) {
    // Access Token 요청 URL
    String tokenRequestUrl = "https://kauth.kakao.com/oauth/token";

    // 요청 파라미터 설정
    Map<String, String> params = new HashMap<>();
    params.put("grant_type", "authorization_code");
    params.put("client_id", clientId);
    params.put("redirect_uri", redirectUri);
    params.put("code", code);

    // RestTemplate을 사용하여 POST 요청 전송
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.postForEntity(tokenRequestUrl, params, String.class);

    // Access Token이 포함된 응답 반환
    return ResponseEntity.ok(response.getBody());
  }

  @GetMapping("/oauth/kakao/user")
  public ResponseEntity<String> getUserInfo(@RequestParam String accessToken) {
    String userInfo = kakaoService.getUserInfo(accessToken);
    return ResponseEntity.ok(userInfo);
  }

}
