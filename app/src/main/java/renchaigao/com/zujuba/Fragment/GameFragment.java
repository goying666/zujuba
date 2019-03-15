package renchaigao.com.zujuba.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TableLayout;

import org.json.JSONObject;

import java.util.ArrayList;

import renchaigao.com.zujuba.Activity.Adapter.CommonRecycleAdapter;
import renchaigao.com.zujuba.Activity.Adapter.CommonViewHolder;
import renchaigao.com.zujuba.Activity.Adapter.MultiTypeSupport;
import renchaigao.com.zujuba.Activity.Game.GameBeginActivity;
import renchaigao.com.zujuba.Activity.Message.ClubMessageInfoAdapter;
import renchaigao.com.zujuba.Bean.AndroidMessageContent;
import renchaigao.com.zujuba.R;

public class GameFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener{

    private Button fragment_game_button2;
    private RecyclerView leftRecyclerView,rightRecyclerView;
    private GameFragmentAdapter leftAdapter,rightAdapter;
    private TableLayout gameTable;
    private ArrayList<JSONObject> leftJsonObjects;
    private ArrayList<JSONObject> rightJsonObjects;
    @Override
    protected void InitView(View rootView) {
        leftRecyclerView = rootView.findViewById(R.id.fragment_game_RecyclerView1);
        rightRecyclerView = rootView.findViewById(R.id.fragment_game_RecyclerView2);
        gameTable = rootView.findViewById(R.id.fragment_game_tablayout);

        LinearLayoutManager leftLayoutManager = new LinearLayoutManager(mContext);
        leftLayoutManager.setStackFromEnd(true);
        leftLayoutManager.setReverseLayout(true);
        leftRecyclerView.setLayoutManager(leftLayoutManager);
        leftAdapter = new GameFragmentAdapter(mContext, leftJsonObjects, R.id.fragment_game_RecyclerView1);
        leftRecyclerView.setAdapter(leftAdapter);
        leftRecyclerView.setHasFixedSize(true);

        LinearLayoutManager rightLayoutManager = new LinearLayoutManager(mContext);
        rightLayoutManager.setStackFromEnd(true);
        rightLayoutManager.setReverseLayout(true);
        rightRecyclerView.setLayoutManager(rightLayoutManager);
        rightAdapter = new GameFragmentAdapter(mContext, rightJsonObjects, R.id.fragment_game_RecyclerView2);
        rightRecyclerView.setAdapter(rightAdapter);
        rightRecyclerView.setHasFixedSize(true);


//        fragment_game_button2 = rootView.findViewById(R.id.fragment_game_button2);
//        fragment_game_button2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Intent intent = new Intent(getActivity(), GameBeginActivity.class);
//                getActivity().startActivity(intent);
//            }
//        });
    }

    @Override
    protected void InitData(View rootView) {

    }

    @Override
    protected void InitOther(View rootView) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_game;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onItemClickListener(int position) {

    }

    @Override
    public void onItemLongClickListener(int position) {

    }

    static class GameFragmentAdapter extends CommonRecycleAdapter<JSONObject> implements MultiTypeSupport<JSONObject>{


        public GameFragmentAdapter(Context context, ArrayList<JSONObject> dataList, int layoutId) {
            super(context, dataList, layoutId);
        }

        @Override
        public void bindData(CommonViewHolder holder, JSONObject data) {

        }

        @Override
        public int getLayoutId(JSONObject item, int position) {
            return 0;
        }
    }
}
