package draw.controller;

import draw.domain.DrawDomain;
import draw.repository.DrawRepo;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.time.LocalDateTime;

@RestController
public class DrawController {

  private final DrawRepo drawRepo;
  private static final Logger LOG = LoggerFactory.getLogger(FileUpload.class);
  public DrawController(DrawRepo drawRepo) {
    this.drawRepo = drawRepo;
  }

  @GetMapping("/draws")
  public HashMap<String, Object> selectDraw() {
    String result = "fail";
    HashMap<String, Object> rtn = new HashMap<>();
    List<DrawDomain> domainList;

    try {
      domainList = this.drawRepo.SELECT_DRAW(); // SELECT_DRAW 호출
      result = "success";
      rtn.put("data", domainList); // 결과 데이터를 리스트로 추가
    } catch (Exception e) {
      LOG.error("Failed to fetch draws", e);
    } finally {
      rtn.put("result", result); // 결과 메시지를 추가
    }

    return rtn; // 결과를 반환
  }
  // ID로 데이터 조회 API 예제
  @GetMapping("/get/{id}")
  public DrawDomain getDrawById(@PathVariable Long id) {
    return drawRepo.SELECT_DRAW_BY_ID(id);
  }


  @PostMapping("/upload")
  public ResponseEntity<String> uploadDraw(@RequestParam("file") MultipartFile file,
                                           @RequestParam("title") String title) {
    try {
      // 파일 데이터 및 기타 정보 설정
      DrawDomain draw = new DrawDomain();
      draw.setFile(file.getBytes());
      draw.setFileName(file.getOriginalFilename());
      draw.setTitle(title);
      draw.setReg_dt(LocalDateTime.now());
      // 기타 정보 설정

      drawRepo.INSERT_DRAW(draw); // 데이터베이스에 저장
      return ResponseEntity.ok("File uploaded successfully");
    } catch (Exception e) {
      LOG.error("Failed to upload file", e);
      return ResponseEntity.status(500).body("Failed to upload file");
    }
  }

}
