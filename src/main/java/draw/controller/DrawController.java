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
@RequestMapping("/api")
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
    List<DrawDomain> resultList;

    try {
      resultList = this.drawRepo.SELECT_DRAW();
      result = "success";

      for (DrawDomain draw : resultList) {
        String fileName = draw.getFileName();
        if (fileName != null && fileName.contains(".")) {
          String type = fileName.substring(fileName.lastIndexOf(".") + 1);
          draw.setType(type);
        }
      }
      rtn.put("data", resultList);
    } catch (Exception e) {
      LOG.error("Failed to fetch draws", e);
    } finally {
      rtn.put("result", result);
    }

    return rtn;
  }


  @PostMapping(value = "/update", consumes = "multipart/form-data")
  public ResponseEntity<String> uploadDraw(@RequestParam("file") MultipartFile file,
                                           @RequestParam(value = "title") String title,
                                           @RequestParam(value = "seq", required = false) String seq) {
    try {
      DrawDomain vo = new DrawDomain();

      vo.setFile(file.getBytes());
      vo.setFileName(file.getOriginalFilename());
      vo.setTitle(title);


      if (seq == null || seq.isEmpty()) {
        vo.setReg_dt(LocalDateTime.now());
        drawRepo.INSERT_DRAW(vo);

      } else {
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
      drawRepo.INCREMENT_COUNT(vo);
      return ResponseEntity.ok("View count updated");
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Failed to update view count");
    }
  }
  @GetMapping("/draws/{seq}")
  public HashMap<String, Object> selectDraw(@PathVariable Long seq) {
    String result = "fail";
    HashMap<String, Object> rtn = new HashMap<>();

    try {
      DrawDomain resultList = drawRepo.SELECT_DRAW_BY_ID(seq);
      if (resultList != null) {
        result = "success";
        rtn.put("data", resultList);
      } else {
        rtn.put("data", null);
      }
    } catch (Exception e) {
      LOG.error("Failed to fetch draws by seq", e);
      rtn.put("data", null);
    }
    rtn.put("result", result);

    return rtn;
  }
}