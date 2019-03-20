package renchaigao.com.zujuba.GamePart.World;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import renchaigao.com.zujuba.ActivityAndFragment.Main.Adapter.MainTeamFragmentAdapter;
import renchaigao.com.zujuba.R;

public class GameInfoActivity extends AppCompatActivity {

    private Button  back_to_world;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        initButton();
    }

    private void initButton(){
        back_to_world = (Button) findViewById(R.id.back_to_world);
        back_to_world.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(GameInfoActivity.this, GameWorldActivity.class);
                startActivity(intent);
            }
        });
    }

    private MainTeamFragmentAdapter mainTeamFragmentAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

//    private void setRecyclerView() {
//        recyclerView = findViewById(R.id.team_page_recycler_view);
//        layoutManager = new LinearLayoutManager(GameInfoActivity.this);
//        recyclerView.setLayoutManager(layoutManager);
//        mainTeamFragmentAdapter = new MainTeamFragmentAdapter(mContext);
//        recyclerView.setAdapter(mainTeamFragmentAdapter);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
//        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
//
//    }


}
