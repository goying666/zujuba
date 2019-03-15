package renchaigao.com.zujuba.Activity.Club;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.club.ClubInfo;
import com.renchaigao.zujuba.mongoDB.info.club.ClubMessageInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.Activity.Message.ClubMessageInfoActivity;
import renchaigao.com.zujuba.Bean.AndroidMessageContent;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.ClubApiService;
import renchaigao.com.zujuba.util.Api.MessageApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

public class ClubInfoActivity extends BaseActivity {


    private String userId,clubId,token;
    private ClubInfo clubInfo;

    @Override
    protected void InitView() {

    }

    @Override
    protected void InitData() {
        UserInfo userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
        token = userInfo.getToken();
        clubId = getIntent().getStringExtra("clubId");

    }

    @Override
    protected void InitOther() {
        reloadMessageInfo();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_club_info;
    }


    public void gotoClubMessageRoom(View view) {
        Intent intent = new Intent(ClubInfoActivity.this, ClubMessageInfoActivity.class);
        intent.putExtra("clubId",clubId);
        intent.putExtra("title",clubInfo.getClubName() != null ? clubInfo.getClubName():"null");
        startActivity(intent);
        finish();
    }

    public void reloadMessageInfo() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(ClubApiService.class)
                .GetClubInfo(userId, token, clubId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(ClubInfoActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            JSONObject responseJsonData = responseJson.getJSONObject("data");
                            Message msg = new Message();
                            switch (code) {
                                case RespCodeNumber.MESSAGE_USER_GET_CLUB_SUCCESS: //在数据库中更新用户数据出错；
                                    clubInfo = JSONObject.parseObject(JSONObject.toJSONString(responseJsonData), ClubInfo.class);
                                    break;
                                case RespCodeNumber.MESSAGE_USER_GET_CLUB_ZERO:
                                    break;
                            }
                        } catch (Exception e) {

                        }
                    }

                    @Override
                    protected void onSuccess(ResponseEntity responseEntity) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }


}
