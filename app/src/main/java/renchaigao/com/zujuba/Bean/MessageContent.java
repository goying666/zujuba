package renchaigao.com.zujuba.Bean;

import org.litepal.crud.LitePalSupport;

import lombok.Getter;
import lombok.Setter;
import renchaigao.com.zujuba.util.dateUse;


/**
 * Created by Administrator on 2018/11/27/027.
 */
@Getter
@Setter
public class MessageContent extends LitePalSupport {
    private Long id;
    private Boolean deleteStyle = false;
    private String upTime = dateUse.GetStringDateNow();
    private Boolean isMe; //判断是否是我发送的
    private Boolean isGroup; //判断是否是俱乐部
    private String senderId;
    private String title;
    private String content;
    private String gotoId = null;
    private Long sendTime;
    private Boolean isReceived;
    private String senderImageUrl;
    private String myImageUrl;
    private String teamId;
    private String friendId;//好友id

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
