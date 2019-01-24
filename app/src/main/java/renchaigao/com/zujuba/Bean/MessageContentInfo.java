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

//    public Long getSendTime() {
//        return sendTime;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public void setSenderId(String senderId) {
//        this.senderId = senderId;
//    }
//
//    public void setTeamId(String teamId) {
//        this.teamId = teamId;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public void setSendTime(long sendTime) {
//        this.sendTime = sendTime;
//    }
}
