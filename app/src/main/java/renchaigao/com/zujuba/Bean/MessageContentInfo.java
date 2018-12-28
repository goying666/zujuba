package renchaigao.com.zujuba.Bean;

import org.litepal.crud.LitePalSupport;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2018/11/27/027.
 */
@Getter
@Setter
public class MessageContentInfo extends LitePalSupport {
    private Long id;
    private Boolean isGroup;
    private String senderId;
    private String content;
    private Long sendTime;
    private Boolean isReceived;
    private Integer senderImageUrl;
    private Integer myImageUrl;
    private String teamId;

}
