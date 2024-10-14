package draw.repository;
import org.apache.ibatis.annotations.Mapper;
import draw.domain.DrawDomain;

@Mapper
public interface DrawRepo {
  DrawDomain SELECT_DRAW();
}
