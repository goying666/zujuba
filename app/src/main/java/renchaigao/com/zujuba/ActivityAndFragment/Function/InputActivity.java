package renchaigao.com.zujuba.ActivityAndFragment.Function;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.domain.response.RespCodeNumber;
import com.renchaigao.zujuba.domain.response.ResponseEntity;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.UserApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

import static com.renchaigao.zujuba.PropertiesConfig.UserConstant.USER_UPDATE_INFO_CLASS_BASIC;

public class InputActivity extends BaseActivity {


    private TextView titleTextView, secondTitleTextView;
    private ImageView goback;
    private TextInputEditText textInputEditText;
    private TextInputLayout textInputLayout;

    @Override
    protected void InitView() {
        ConstraintLayout toolbar = (ConstraintLayout) findViewById(R.id.input_toolbar);
        textInputLayout = (TextInputLayout) toolbar.findViewById(R.id.input_layout);
        textInputEditText = (TextInputEditText) toolbar.findViewById(R.id.input_edit);
        titleTextView.setText("组局吧");
        secondTitleTextView.setText("保存");

    }

    private String userId, token;
    @Override
    protected void InitData() {
        UserInfo userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
        token = userInfo.getToken();
    }

    @Override
    protected void InitOther() {
        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                jsonObject.put("signature", s.toString());
            }
        });
        secondTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textInputEditText.getTextSize() > 0)
                    SendMessage();

            }
        });
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_input;
    }

    private JSONObject jsonObject = new JSONObject();

    private void SendMessage() {
        addSubscribe(RetrofitServiceManager.getInstance().creat(UserApiService.class)
                .UpdateUserInfo(
                        USER_UPDATE_INFO_CLASS_BASIC,
                        userId,
                        token,
                        jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new BaseObserver<ResponseEntity>(InputActivity.this) {
                    @Override
                    public void onNext(ResponseEntity value) {
                        try {
                            JSONObject responseJson = JSONObject.parseObject(JSONObject.toJSONString(value));
                            int code = Integer.valueOf(responseJson.get("code").toString());
                            switch (code) {
                                case RespCodeNumber.USER_UPDATE_INFO_SUCCESS: //在数据库中更新用户数据出错；
                                    finish();
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
                    }

                    @Override
                    public void onComplete() {
                    }
                }));
    }

}
