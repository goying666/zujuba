package renchaigao.com.zujuba.ActivityAndFragment.Function;

import android.content.Intent;
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
import renchaigao.com.zujuba.ActivityAndFragment.Store.StoreManagerBasicFragment;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.Api.UserApiService;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.http.BaseObserver;
import renchaigao.com.zujuba.util.http.RetrofitServiceManager;

import static com.renchaigao.zujuba.PropertiesConfig.UserConstant.USER_UPDATE_INFO_CLASS_BASIC;

public class InputActivity extends BaseActivity {

    public final static int UPDATE_STORE_NAME = 0X1001;
    public final static int UPDATE_SUCCESS = 0X1000;

    private TextView titleTextView, secondTitleTextView, updateNote;
    private ImageView goback;
    private TextInputEditText textInputEditText;
    private TextInputLayout textInputLayout;
    private String userId, token, oldContent, newContent;
    private Boolean contentIsChange = false;
    private int updateWhat = 0;


    @Override
    protected void InitView() {
        ConstraintLayout toolbar = (ConstraintLayout) findViewById(R.id.input_toolbar);
        updateNote = (TextView) toolbar.findViewById(R.id.textView46);
        titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        goback = (ImageView) toolbar.findViewById(R.id.toolbarBack);
        secondTitleTextView = (TextView) findViewById(R.id.toolbarSecondTitle);
        textInputLayout = (TextInputLayout) findViewById(R.id.input_layout);
        textInputEditText = (TextInputEditText) findViewById(R.id.input_edit);
    }

    @Override
    protected void InitData() {
        UserInfo userInfo = DataUtil.GetUserInfoData(this);
        userId = userInfo.getId();
        token = userInfo.getToken();
        oldContent = getIntent().getStringExtra("oldContent");
        updateWhat = getIntent().getIntExtra("updateWhat", 0);
        textInputEditText.setText(oldContent);
    }


    @Override
    protected void InitOther() {
        switch (updateWhat) {
            case UPDATE_STORE_NAME:
                titleTextView.setText("修改店铺名称");
                secondTitleTextView.setText("保存");
                updateNote.setText("");
                break;
        }

        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                newContent = s.toString();
                if (oldContent.equals(newContent)) {
                    secondTitleTextView.setVisibility(View.GONE);
                    contentIsChange = false;
                } else {
                    secondTitleTextView.setVisibility(View.VISIBLE);
                    contentIsChange = true;
                }
            }
        });
        secondTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (updateWhat) {
                    case UPDATE_STORE_NAME:
                        Intent intent = new Intent(InputActivity.this, StoreManagerBasicFragment.class);
                        intent.putExtra("contentIsChange", contentIsChange);
                        intent.putExtra("newContent", newContent);
                        setResult(UPDATE_SUCCESS,intent);
                        break;
                }

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
