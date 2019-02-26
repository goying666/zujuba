package renchaigao.com.zujuba.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import renchaigao.com.zujuba.Activity.Game.GameBeginActivity;
import renchaigao.com.zujuba.R;

public class GameFragment extends BaseFragment {

    private Button fragment_game_button2;
    public Activity mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_game, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    protected void InitView(View rootView) {

    }

    @Override
    protected void InitData(View rootView) {

    }

    @Override
    protected void InitOther(View rootView) {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initView( View view){
        fragment_game_button2 = view.findViewById(R.id.fragment_game_button2);
        fragment_game_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), GameBeginActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }
}
