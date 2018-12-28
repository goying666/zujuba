package renchaigao.com.zujuba.Bean;

import org.litepal.crud.LitePalSupport;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2018/11/14/014.
 */

@Setter
@Getter
public class MessageNoteInfo extends LitePalSupport {
    private Integer id;
    private String messageName;
    private String groupId;//消息组ID；
    private String senderId;//0代表自己；
    private String notesInfo;//消息内容；
    private Long sendTime;//发送时间；
    private String state;
    private Integer unreadMessageSum;//消息数
    private String currentContent;//当前最后一条消息；
    private Integer messageImageUrl;//聊天头像；

}
