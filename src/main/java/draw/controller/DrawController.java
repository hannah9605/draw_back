package draw.controller;

import draw.domain.DrawDomain; // DrawDomain 클래스를 임포트하세요
import draw.repository.DrawRepo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class DrawController {

  private final DrawRepo drawRepo;

  public DrawController(DrawRepo drawRepo) {
    this.drawRepo = drawRepo;
  }

  @GetMapping("/draws")
  public HashMap<String, Object> selectDraw() { // @RequestBody 어노테이션 제거
    String result = "fail";
    HashMap<String, Object> rtn = new HashMap<>();
    DrawDomain domain; // DrawDomain 타입으로 변수 선언

    try {
      domain = this.drawRepo.SELECT_DRAW(); // SELECT_DRAW 호출
      result = "success";
      rtn.put("data", domain); // 결과 데이터를 rtn에 추가합니다.
    } catch (Exception e) {
      e.printStackTrace();
      return new HashMap<>();  // 예외 발생 시 빈 해시맵 반환
    } finally {
      rtn.put("result", result); // 결과 메시지를 추가합니다.
    }

    return rtn; // 결과를 반환합니다.
  }
}
