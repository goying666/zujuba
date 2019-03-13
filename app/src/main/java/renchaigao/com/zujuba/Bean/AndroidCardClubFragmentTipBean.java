package renchaigao.com.zujuba.Bean;

import org.litepal.crud.LitePalSupport;

import lombok.Getter;
import lombok.Setter;
import normal.UUIDUtil;
import renchaigao.com.zujuba.util.dateUse;


/**
 * Created by Administrator on 2018/11/27/027.
 */
@Getter
@Setter
//同 data 内的：CardClubFragmentTipBean，用于存储message Fragment页面的club的封面信息；
public class AndroidCardClubFragmentTipBean extends LitePalSupport {
    private String id = UUIDUtil.getUUID();
    private Boolean deleteStyle = false;
    private String upTime = dateUse.GetStringDateNow();
    private String createTime = dateUse.GetStringDateNow();

    private String clubId; //俱乐部Id
    private String clubName; //俱乐部名称
    private String placeName; //俱乐部场地名
    private String whoAmI; //对应身份
    private String allPeopleNum; //人数
    private String activitiesNum; //活动次数

}
