package draw.repository;
import org.apache.ibatis.annotations.Mapper;
import draw.domain.DrawDomain;
import org.apache.ibatis.annotations.Param;
import java.util.List;
@Mapper
public interface DrawRepo {
   List<DrawDomain> SELECT_DRAW();
  void INSERT_DRAW(DrawDomain draw); // 데이터 삽입
  void UPDATE_DRAW(DrawDomain draw); // 데이터 삽입
  DrawDomain SELECT_DRAW_BY_ID(@Param("id") Long id); // 특정 ID로 조회
  void INCREMENT_COUNT(DrawDomain draw);
}
