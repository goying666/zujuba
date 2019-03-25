package renchaigao.com.zujuba.ActivityAndFragment.Store.Create;

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
import renchaigao.com.zujuba.ActivityAndFragment.Function.GaoDeMapActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_STORE;

public class CreateStoreActivity extends BaseActivity {

    private String TAG = "This is CreateStoreActivity ";

    @Override

    protected int getLayoutId() {
        return R.layout.activity_create_store;
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

    public void readFinish(View view) {
        if (CheckBox_agreeUs.isChecked()) {
            LinearLayout_agreePartLinearLayout.setVisibility(View.GONE);
            LinearLayout_basicPartLinearLayout.setVisibility(View.VISIBLE);
        } else {
//            提示点击同意才能
            builder.setMessage("同意条款后才可以继续操作哦~")
                    .setTitle("Title")
                    .show();
        }

    }

    public void finishBasicPart(View view) {
        Intent intent = new Intent(CreateStoreActivity.this, CreateStorePartTwoActivity.class);
        intent.putExtra("storeInf", JSONObject.toJSONString(storeInfo));
        startActivity(intent);
    }

}
