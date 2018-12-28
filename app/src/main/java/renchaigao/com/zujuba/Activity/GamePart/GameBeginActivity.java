package renchaigao.com.zujuba.Activity.GamePart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import renchaigao.com.zujuba.Activity.TeamPart.TeamCreateActivity;
import renchaigao.com.zujuba.GamePart.DeepForest.DeepForestGmaeSetUpActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.support.ViewInject;

public class GameBeginActivity extends AppCompatActivity {

    private Button game_begin_button1, game_begin_button2;

    private void setButton(){
        game_begin_button1 = this.findViewById(R.id.game_begin_button1);
        game_begin_button2 = this.findViewById(R.id.game_begin_button2);
        game_begin_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(GameBeginActivity.this, TeamCreateActivity.class);
                startActivity(intent);
                finish();
            }
        });
        game_begin_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(GameBeginActivity.this, DeepForestGmaeSetUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_begin);
        setButton();
    }
}
