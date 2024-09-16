package common.validate;

import java.util.Optional;

/**
 * @Description 입력값 null 혹은 공백 검사
 *
 * @author mono
 * @since 2023.10.11.
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   수정일        수정자     수정내용
 *   -----------   --------   ---------------------------------------------------
 *
 * </pre>
 */

public class CheckNotEmpty {

    public static Boolean chkNotEmpty(Object obj) {
        if (obj instanceof String)
            return !Optional.ofNullable((String) obj).orElse("").trim().isEmpty();
        else if (obj instanceof Integer)
            return Optional.ofNullable((Integer) obj).orElse(0) > 0;
        else if (obj instanceof Long)
            return Optional.ofNullable((Long) obj).orElse(0L) > 0L;
        else if (obj instanceof Float)
            return Optional.ofNullable((Float) obj).orElse(0.0f) > 0.0f;
        else if (obj instanceof Double)
            return Optional.ofNullable((Double) obj).orElse(0.0) > 0.0;
        return !(obj == null);
    }
}
