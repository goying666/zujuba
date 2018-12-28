package renchaigao.com.zujuba.GamePart.DeepForest;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import renchaigao.com.zujuba.R;

public class DeepForestGmaeSetUpActivity extends AppCompatActivity {

    private TextView deep_forest_game_set_up_my_number, deep_forest_game_set_up_my_name;
    private Button deep_forest_game_set_up_button;
    private TextInputEditText deep_forest_game_set_up_TextInputEditText;
    private TextInputLayout deep_forest_game_set_up_TextInputLayout;


    private void setToolBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void initView(){
        deep_forest_game_set_up_my_number = findViewById(R.id.deep_forest_game_set_up_my_number);
        deep_forest_game_set_up_my_name = findViewById(R.id.deep_forest_game_set_up_my_name);
        deep_forest_game_set_up_button = findViewById(R.id.deep_forest_game_set_up_button);
        deep_forest_game_set_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        deep_forest_game_set_up_TextInputEditText = findViewById(R.id.deep_forest_game_set_up_TextInputEditText);
        deep_forest_game_set_up_TextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                deep_forest_game_set_up_my_name.setText("《" + s.toString()+"文明》");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        deep_forest_game_set_up_TextInputLayout = findViewById(R.id.deep_forest_game_set_up_TextInputLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_forest_gmae_set_up);
        setToolBar();
        initView();
    }
}
