package response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by chenbo on 16/6/15.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse{

    public static final CommonResponseBuilder OKBuilder = CommonResponse.builder().status(0).msg("OK");
    public static final CommonResponse OK = CommonResponse.builder().status(0).msg("OK").build();

    public static final CommonResponse ERROR = CommonResponse.builder().status(-1).msg("ERROR").build();
    public static final CommonResponseBuilder ERRORBuilder = CommonResponse.builder().status(-1);

    private int status;

    private String msg;

    private Object data;

    public CommonResponse(int status, String msg) {
        this(status, msg, null);
    }

}
