package renchaigao.com.zujuba.ActivityAndFragment.Store.Create;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.dao.Address;
import com.renchaigao.zujuba.mongoDB.info.store.BusinessPart.BusinessTimeInfo;
import com.renchaigao.zujuba.mongoDB.info.store.StoreInfo;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.ActivityAndFragment.CustomViewPagerAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.Function.GaoDeMapActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Store.StoreClubFragment;
import renchaigao.com.zujuba.ActivityAndFragment.Store.StoreTeamsFragment;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.widgets.CustomViewPager;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_STORE;

public class CreateStoreActivity extends BaseActivity {

    private String TAG = "This is CreateStoreActivity ";
    private Button Button_createPlaceCancleButton;
    private Button Button_createPlaceNextButton;


    @Override
    protected void InitView() {
        initToolbar();
        Button_createPlaceCancleButton = (Button) findViewById(R.id.Button_createPlaceCancleButton);
        Button_createPlaceCancleButton.setVisibility(View.GONE);
        Button_createPlaceNextButton = (Button) findViewById(R.id.Button_createPlaceNextButton);
        Button_createPlaceNextButton.setVisibility(View.VISIBLE);
        Button_createPlaceNextButton.setText("已阅读，下一步");
        customViewPager = (CustomViewPager) findViewById(R.id.CustomViewPager_createStoreCustomViewPager);
    }

    @Override
    protected void InitData() {

    }

    @Override
    protected void InitOther() {

        setViewPager();
        setClick();
    }

    @Override

    protected int getLayoutId() {
        return R.layout.activity_create_store;
    }

    private CustomViewPager customViewPager;

    private void setViewPager() {
        final CreateStorePartOneFragment createStorePartOneFragment = new CreateStorePartOneFragment();
        CustomViewPagerAdapter customViewPagerAdapter = new CustomViewPagerAdapter(getSupportFragmentManager());
        customViewPagerAdapter.addFragment(createStorePartOneFragment);
        customViewPagerAdapter.addFragment(createStorePartOneFragment);
        customViewPagerAdapter.addFragment(createStorePartOneFragment);
        customViewPagerAdapter.addFragment(createStorePartOneFragment);
        customViewPager.setAdapter(customViewPagerAdapter);
        customViewPager.setCurrentItem(0);
    }

    private Integer pageNum = 0;

    private void setClick() {
        Button_createPlaceCancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pageNum) {
                    case 1:
                        pageNum = 0;
                        Button_createPlaceNextButton.setVisibility(View.GONE);
                        Button_createPlaceCancleButton.setText("已阅读，下一步");
                        break;
                    case 2:
                        pageNum = 1;
                        break;
                    case 3:
                        pageNum = 2;
                        Button_createPlaceNextButton.setVisibility(View.VISIBLE);
                        Button_createPlaceNextButton.setText("上一步");
                        break;
                }
                customViewPager.setCurrentItem(pageNum);
                secondTitleTextView.setText("步骤：" + (pageNum + 1) + "/4");
            }
        });
        Button_createPlaceNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (pageNum) {
                    case 0:
                        pageNum = 1;
                        Button_createPlaceNextButton.setVisibility(View.VISIBLE);
                        Button_createPlaceNextButton.setText("上一步");
                        Button_createPlaceCancleButton.setText("下一步");
                        break;
                    case 1:
                        pageNum = 2;
                        break;
                    case 2:
                        pageNum = 3;
                        Button_createPlaceNextButton.setVisibility(View.GONE);
                        Button_createPlaceNextButton.setText("上一步");
                        Button_createPlaceCancleButton.setText("下一步");
                        break;
                    case 3:
                        Button_createPlaceCancleButton.setText("提交");
                        break;
                }
                customViewPager.setCurrentItem(pageNum);
                secondTitleTextView.setText("步骤：" + (pageNum + 1) + "/4");
            }
        });
    }

    private TextView titleTextView, secondTitleTextView;
    private ConstraintLayout toolbar;
    private Integer step = 1;

    @SuppressLint("SetTextI18n")
    private void initToolbar() {
        toolbar = (ConstraintLayout) findViewById(R.id.ConstraintLayout_toolbar);
        titleTextView = (TextView) toolbar.findViewById(R.id.toolbarTitle);
        titleTextView.setText("创建新场地");
        secondTitleTextView = (TextView) toolbar.findViewById(R.id.toolbarSecondTitle);
        secondTitleTextView.setText("步骤：" + (pageNum + 1) + "/4");
        ImageView goback = (ImageView) toolbar.findViewById(R.id.toolbarBack);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    long time = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - time > 1000)) {
                Toast.makeText(this, "再按一次取消入驻", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }


}
