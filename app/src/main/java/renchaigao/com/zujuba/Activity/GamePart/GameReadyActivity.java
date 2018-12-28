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

public class GameReadyActivity extends AppCompatActivity {

    private Button game_ready_button;

    private void setButton() {
        game_ready_button = this.findViewById(R.id.game_ready_button);
        game_ready_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(GameReadyActivity.this, DeepForestGmaeSetUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_ready);
        setButton();
    }
}
