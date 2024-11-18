package draw;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoService {

  public String getUserInfo(String accessToken) {
    String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

    // 헤더 설정 (Authorization: Bearer {accessToken})
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + accessToken);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    // 사용자 정보 요청
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.postForEntity(userInfoUrl, entity, String.class);

    return response.getBody();
  }
}
