package draw.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Lob;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE,
  setterVisibility=JsonAutoDetect.Visibility.NONE, creatorVisibility= JsonAutoDetect.Visibility.NONE)
public class DrawDomain {

  @JsonProperty("seq")     private String seq = "";
  @JsonProperty("title")      private String title = "";
  @Lob @JsonProperty("file")      private byte[] file ;
  @Lob @JsonProperty("fileName")      private String fileName="" ;
  @JsonProperty("reg_dt")       private LocalDateTime  reg_dt ;
  @JsonProperty("view_cnt")       private String view_cnt = "";
  @JsonProperty("like_cnt")       private String like_cnt = "";
  private List<DrawDomain> childrenList = new ArrayList<>();


}