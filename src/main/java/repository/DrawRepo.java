package repository;

import domain.DrawDomain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.HashMap;
import java.util.List;

/**
 * @Description 그림게시판 조회
 */

@Mapper  // MyBatis 매퍼 인터페이스로 인식
public interface DrawRepo {

    // 그림 게시판을 조회하는 쿼리
    List<HashMap<String, Object>> LIST_DRAW(DrawDomain domain) throws Exception;
}