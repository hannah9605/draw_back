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
import java.util.Map;
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


  @PostMapping(value = "/update", consumes = "multipart/form-data")
  public ResponseEntity<String> uploadDraw(@RequestParam("file") MultipartFile file,
                                            @RequestParam(value = "title", required = false) String title,
                                            @RequestParam(value = "seq", required = false) String seq)
  {
    try {
      DrawDomain vo = new DrawDomain();

      vo.setFile(file.getBytes());
      vo.setFileName(file.getOriginalFilename());
      vo.setTitle(title);


      if (seq == null || seq.isEmpty()) {
        vo.setReg_dt(LocalDateTime.now());
        drawRepo.INSERT_DRAW(vo);

      }  else {
        vo.setSeq(Integer.parseInt(seq));
        drawRepo.UPDATE_DRAW(vo);

      }
      return ResponseEntity.ok("File uploaded successfully");
    } catch (Exception e) {
      LOG.error("Failed to upload file", e);
      return ResponseEntity.status(500).body("Failed to upload file");
    }
  }

  @PostMapping("/updateCount")
  public ResponseEntity<String> updateViewCount(@RequestBody DrawDomain vo) {
    try {
      drawRepo.INCREMENT_COUNT(vo); // DrawRepo의 메서드 호출
      return ResponseEntity.ok("View count updated");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Failed to update view count");
    }
  }

}
