package renchaigao.com.zujuba.Activity.Place;


import com.alibaba.fastjson.JSONObject;

import renchaigao.com.zujuba.Activity.BaseActivity;
import renchaigao.com.zujuba.R;

public class PlaceManagerActivity extends BaseActivity {


    @Override
    protected void InitView() {

    }

    JSONObject jsonObjectIntent = new JSONObject();
    @Override
    protected void InitData() {
        jsonObjectIntent = JSONObject.parseObject(getIntent().getStringExtra("storeinfo"));
    }

    @Override
    protected void InitOther() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_place_manager;
    }

}
