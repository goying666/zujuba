package renchaigao.com.zujuba.Activity.Club;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.club.ClubInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.ClubApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

public class CreateClubActivity extends BaseActivity {

    private Toolbar toolbar;
    private ConstraintLayout partOne, partTwo;
    private Button cancle, next;
    private TextView placeName,stepText;
    private TextInputEditText inputEditText;
    private String userId, token, clubName, placeId;
    private JSONObject clubJson = new JSONObject();
    private ClubInfo clubInfo = new ClubInfo();
    private int stepInt = 0;
    final static public int SELETCT_STORE = 1201;
    final static public int SELETCT_OPEN_PLACE = 1202;
    final static public int SELETCT_MY_PLACE = 1203;

    @Override
    protected void InitView() {
        setToolBar();
        stepText = findViewById(R.id.create_club_step_textView);
        partOne = findViewById(R.id.create_club_part1);
        partTwo = findViewById(R.id.create_club_part2);
        cancle = findViewById(R.id.button13);
        next = findViewById(R.id.button14);
        placeName = findViewById(R.id.textView124);
        inputEditText = findViewById(R.id.textInputEdite);
        partOne.setVisibility(View.VISIBLE);
        partTwo.setVisibility(View.GONE);
    }

    private void setToolBar() {
        toolbar = findViewById(R.id.crate_club_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setNavigationIcon(R.drawable.toolbar_back);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELETCT_MY_PLACE:
                placeId = data.getStringExtra("placeId");
//                if(resultCode == )
                break;
            case SELETCT_STORE:
                break;
            case SELETCT_OPEN_PLACE:
                break;
        }
    }

    @Override
    protected void InitData() {
        UserInfo userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
        token = userInfo.getToken();

    }

    @Override
    protected void InitOther() {
        setButton();
        setEditeView();
    }

    private void setEditeView() {
        inputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                clubName = s.toString();
                clubJson.put("clubName", clubName);
            }
        });
    }

    private void setButton() {
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (stepInt) {
                    case 0:
                        finish();
                        break;
                    case 1:
                        cancle.setText("取消");
                        next.setText("下一步");
                        partOne.setVisibility(View.VISIBLE);
                        partTwo.setVisibility(View.GONE);
                        stepText.setText("Step:1/2");
                        stepInt = 0;
                        break;
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (stepInt) {
                    case 0:
                        cancle.setText("上一步");
                        next.setText("完成");
                        partOne.setVisibility(View.GONE);
                        partTwo.setVisibility(View.VISIBLE);
                        stepInt = 1;
                        stepText.setText("Step:2/2");
                        break;
                    case 1:
                        sendCreateMessageToServer();
                        break;
                }
            }
        });
    }


    private void sendCreateMessageToServer() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(ClubApiService.class)
                .CreateClub(userId, placeId, clubInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            switch (code) {
                                case RespCodeNumber.SUCCESS:
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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_club;
    }

    public void selectStore(View view) {
    }

    public void selectMyPlace(View view) {
    }

    public void selectOpenPlace(View view) {
    }
}
