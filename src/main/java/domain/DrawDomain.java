package domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

public class DrawDomain {
  @JsonProperty("seq")        private int rowNo = 0;
  @JsonProperty("title")      private String title = "";            // 제목
  @JsonProperty("auth")       private String auth = "";            //작성자
  @JsonProperty("reg_dt")    private String reg_dt = "";           //등록일자
  @JsonProperty("upt_dt")    private String upt_dt = "";           //수정일자
}
