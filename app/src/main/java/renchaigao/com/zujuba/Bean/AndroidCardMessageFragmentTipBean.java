package renchaigao.com.zujuba.Bean;

import org.litepal.crud.LitePalSupport;

import lombok.Getter;
import lombok.Setter;
import normal.UUIDUtil;

/**
 * Created by Administrator on 2018/11/14/014.
 */

@Setter
@Getter
//同 data 内的：CardMessageFragmentTipBean，用于存储message Fragment页面的message 的封面信息；
public class AndroidCardMessageFragmentTipBean extends LitePalSupport {
    private String imageUrl; //图片url
    private String name; //聊天室名称
    private String content; //最后一条内容
    private String time; //最后一条发布时间
    private Integer noRead; //未读信息数
    private String mClass; //消息所属类型
    private String ownerId; //消息所属者id
    private Long lastTime; //最后一条消息发送时间logn型
//    private Integer id;
//    private String messageName;
//    private String groupId;//消息组ID；
//    private String senderId;//0代表自己；
//    private String notesInfo;//消息内容；
//    private Long sendTime;//发送时间；
//    private String state;
//    private Integer unreadMessageSum;//消息数
//    private String currentContent;//当前最后一条消息；
//    private Integer messageImageUrl;//聊天头像；
//
//    public Long getSendTime() {
//        return sendTime;
//    }
}
