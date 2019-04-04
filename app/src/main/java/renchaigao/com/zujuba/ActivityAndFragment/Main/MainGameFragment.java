package renchaigao.com.zujuba.ActivityAndFragment.Main;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonRecycleAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.MultiTypeSupport;
import renchaigao.com.zujuba.ActivityAndFragment.BaseFragment;
import renchaigao.com.zujuba.ActivityAndFragment.Game.lrs.GameLRSMainActivity;
import renchaigao.com.zujuba.R;

public class MainGameFragment extends BaseFragment implements CommonViewHolder.onItemCommonClickListener {

    private Button goto_gameMain;
    private RecyclerView leftRecyclerView, rightRecyclerView;
    private GameFragmentAdapter leftAdapter, rightAdapter;
    private TabLayout gameTable;
    private ArrayList<JSONObject> TipJsonObjects = new ArrayList<>();

    @Override
    protected void InitView(View rootView) {
        goto_gameMain = (Button) rootView.findViewById(R.id.goto_gameMain);
        goto_gameMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GameLRSMainActivity.class);
                startActivity(intent);
            }
        });
        leftRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_game_RecyclerView1);
        rightRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_game_RecyclerView2);
        gameTable = (TabLayout) rootView.findViewById(R.id.fragment_game_tablayout);

        ArrayList<String> gameClassList = new ArrayList<>();
        gameClassList.add("演技");
        gameClassList.add("策略");
        gameClassList.add("经营");
        gameClassList.add("题材");
        gameClassList.add("道具");
        LinearLayoutManager leftLayoutManager = new LinearLayoutManager(mContext);
        leftLayoutManager.setStackFromEnd(true);
        leftLayoutManager.setReverseLayout(true);
        leftRecyclerView.setLayoutManager(leftLayoutManager);
        leftAdapter = new GameFragmentAdapter("LIST",mContext, gameClassList, R.layout.card_game_list, this);
        leftRecyclerView.setAdapter(leftAdapter);
        leftRecyclerView.setHasFixedSize(true);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","狼人杀");
        TipJsonObjects.add(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("name","天黑请闭眼");
        jsonObject = new JSONObject();
        TipJsonObjects.add(jsonObject);
        jsonObject.put("name","暗黑森林");
        jsonObject = new JSONObject();
        TipJsonObjects.add(jsonObject);
        LinearLayoutManager rightLayoutManager = new LinearLayoutManager(mContext);
        rightLayoutManager.setStackFromEnd(true);
        rightLayoutManager.setReverseLayout(true);
        rightRecyclerView.setLayoutManager(rightLayoutManager);
        rightAdapter = new GameFragmentAdapter("TIP",mContext, TipJsonObjects, R.layout.card_game_tip, this);
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

    static class GameFragmentAdapter<T> extends CommonRecycleAdapter<T> implements MultiTypeSupport<T> {

        private CommonViewHolder.onItemCommonClickListener commonClickListener;
        private Context mContext;
        private String useAdapterFlag;

        public GameFragmentAdapter(String userName, Context context, ArrayList<T> dataList, int layoutId, CommonViewHolder.onItemCommonClickListener commonClickListener) {
            super(context, dataList, layoutId);
            this.useAdapterFlag = userName;
            this.mContext = context;
            this.commonClickListener = commonClickListener;
        }

        @Override
        public int getLayoutId(T item, int position) {
            return 0;
        }

        @Override
        public void bindData(CommonViewHolder holder, T data) {
            if (useAdapterFlag.equals("LIST")) {
                holder.setText(R.id.card_game_list_class, data.toString())
                        .setCommonClickListener(commonClickListener);
            } else {
                JSONObject jsonData = (JSONObject)data;
                        holder.setImageResource(R.id.card_game_tip_image,R.drawable.lrs)
                        .setText(R.id.card_game_tip_name,jsonData.getString("name"))
                        .setCommonClickListener(commonClickListener);

            }
        }
    }
}
