package controller;

import common.FileUtil;
import domain.DrawDomain;
import repository.DrawRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.HashMap;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DrawController {
  private static final Logger LOG = LoggerFactory.getLogger(DrawController.class);
  private final DrawRepo drawRepo;

  @Autowired
  public DrawController(DrawRepo drawRepo) {
    this.drawRepo = drawRepo;
  }

  @GetMapping(value = "/draw")
  public HashMap<String, Object> listDraw() throws Exception {

    HashMap<String, Object> rtn = new HashMap<>();
    List<DrawDomain> resultList = new ArrayList<>();
    String result = "FAIL";
    try {
      DrawDomain domain = new DrawDomain();
    }catch (SQLException e) {
      LOG.error("■■■■■■■■■■■■■■■ 목록 요청 SQL 오류 : {}", e.getMessage());
    } catch (Exception e) {
      LOG.error("■■■■■■■■■■■■■■■  목록 요청 요청 오류 : {}", e.getMessage());
    } finally {
      rtn.put("resultList", resultList);
      rtn.put("result", result);
    }


  }

}
