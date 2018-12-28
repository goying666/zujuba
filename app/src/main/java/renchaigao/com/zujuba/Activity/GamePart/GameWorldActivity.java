package renchaigao.com.zujuba.Activity.GamePart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import renchaigao.com.zujuba.R;

public class GameWorldActivity extends AppCompatActivity {

    private ImageView game_info_user_icon;
    private Button back_to_mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_world);
        initToolbar();
        initButton();
    }

    private void initToolbar(){
        game_info_user_icon = findViewById(R.id.game_info_user_icon);
        game_info_user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(GameWorldActivity.this, GameInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initButton(){
        back_to_mine = findViewById(R.id.back_to_mine);
        back_to_mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(GameWorldActivity.this, GameInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
