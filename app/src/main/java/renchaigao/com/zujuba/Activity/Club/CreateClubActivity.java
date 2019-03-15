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
import com.renchaigao.zujuba.mongoDB.info.store.StoreInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.Activity.Place.PlaceListActivity;
import renchaigao.com.zujuba.Activity.TeamPart.TeamCreateActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.ClubApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.PropertiesConfig;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_STORE;
import static renchaigao.com.zujuba.Activity.Place.PlaceListActivity.PLACE_LIST_ACTIVITY_CODE;
import static renchaigao.com.zujuba.Activity.TeamPart.TeamCreateActivity.CREATE_TEAM_ADDRESS_STORE;
import static renchaigao.com.zujuba.util.PropertiesConfig.CREATE_CLUB_ADDRESS_MINE;
import static renchaigao.com.zujuba.util.PropertiesConfig.CREATE_CLUB_ADDRESS_OPEN;
import static renchaigao.com.zujuba.util.PropertiesConfig.CREATE_CLUB_ADDRESS_STORE;

public class CreateClubActivity extends BaseActivity {

    private Toolbar toolbar;
    private ConstraintLayout partOne, partTwo;
    private Button cancle, next;
    private TextView placeName, stepText;
    private TextInputEditText inputEditText;
    private String userId;
    private ClubInfo clubInfo = new ClubInfo();
    private int stepInt = 0;

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
            case CREATE_CLUB_ADDRESS_STORE:
                if (resultCode == PLACE_LIST_ACTIVITY_CODE) {
                    JSONObject selectPlaceJson = JSONObject.parseObject(data.getStringExtra("place"));
                    clubInfo.setPlaceClass(ADDRESS_CLASS_STORE);
                    clubInfo.setPlaceId(selectPlaceJson.getString("placeid"));
                    clubInfo.setPlaceName(selectPlaceJson.getString("name"));
                    placeName.setText(selectPlaceJson.getString("name"));
                }
                break;
            case CREATE_CLUB_ADDRESS_OPEN:
                break;
            case CREATE_CLUB_ADDRESS_MINE:
                break;
        }
    }

    private String token;

    @Override
    protected void InitData() {
        UserInfo userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
        token = userInfo.getToken();
        clubInfo.setCreaterId(userId);
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
//                检查名字合法性
                if (s.length() != 0)
                    clubInfo.setClubName(s.toString());
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
                .CreateClub(userId, clubInfo.getPlaceId(), token, clubInfo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            switch (code) {
                                case RespCodeNumber.CLUB_CREATE_SUCCESS:
                                    finish();
                                    break;
                                case RespCodeNumber.CLUB_HAD_BEEN_CREATE:
                                    finish();
                                    break;
                                case RespCodeNumber.CLUB_CREATE_FAIL:
//                                    finish();
                                    break;
                                case RespCodeNumber.CLUB_CREATE_LIMIT:
//                                    finish();
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
        final Intent intent = new Intent(CreateClubActivity.this, PlaceListActivity.class);
        startActivityForResult(intent, CREATE_CLUB_ADDRESS_STORE);
    }

    public void selectMyPlace(View view) {
    }

    public void selectOpenPlace(View view) {
    }
}
